/*
 * Copyright (c) 2020, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.eclipse.plugin.builder;

import java.io.File;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.oracle.coherence.eclipse.plugin.builder.visitors.DeltaVisitor;
import com.oracle.coherence.eclipse.plugin.builder.visitors.ResourceVisitor;

import static com.oracle.coherence.eclipse.plugin.builder.MarkerHelper.addMarker;
import static com.oracle.coherence.eclipse.plugin.builder.MarkerHelper.deleteMarkers;
import static com.oracle.coherence.eclipse.plugin.builder.MarkerHelper.deleteAllProjectMarkers;

/**
 * An implementation of {@link IncrementalProjectBuilder} to respond to when 
 * a project is built and instrument classes with <code>PortableType</code> annotation.
 *
 */
public class CoherencePluginBuilder 
    extends IncrementalProjectBuilder 
    {

    // ----- constructors ---------------------------------------------------
    
    /**
     * Construct a {@link CoherencePluginBuilder}.
     */
    public CoherencePluginBuilder() 
        {
        f_context = new PluginContext(this);
        f_deltaVisitor = new DeltaVisitor(f_context);
        f_resourceVisitor = new ResourceVisitor(f_context);
        f_reflectionHelper = new ReflectionHelper(f_context);
        }

    // ----- IncrementalProjectBuilder methods ------------------------------
    
    @Override
    protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException 
        {
        if (kind == FULL_BUILD) 
            {
            fullBuild(monitor);
            } 
        else 
            {
            IResourceDelta delta = getDelta(getProject());
            if (delta == null) 
                {
                fullBuild(monitor);
                } 
            else 
                {
                incrementalBuild(delta, monitor);
                }
            }
        return null;
        }

    @Override
    protected void clean(IProgressMonitor monitor) throws CoreException 
        {
        // delete markers set and files created
        deleteAllProjectMarkers(getProject());
        }

    //---- helpers ----------------------------------------------------------
    
    /**
     * Called on a full build for the project.
     * 
     * @param monitor
     * @throws CoreException
     */
    protected void fullBuild(final IProgressMonitor monitor) throws CoreException 
        {
        deleteAllProjectMarkers(getProject());
        addMarker(f_context.getProject(), "Processing Full Build", -1, IMarker.SEVERITY_INFO);
        
        f_context.getChangedFiles().clear();

        try 
            {
            getProject().accept(f_resourceVisitor);
            } 
        catch (CoreException e) 
            {
            }

        // process all the collected files
        processChangedFiles();
        }

    /**
     * Run on an incremental build of the project.
     * 
     * @param delta
     * @param monitor
     * @throws CoreException
     */
    protected void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) throws CoreException 
        {
        deleteAllProjectMarkers(getProject());
        
        f_context.getChangedFiles().clear();
        // the visitor does the work.
        delta.accept(f_deltaVisitor);

        // process all the collected files
        processChangedFiles();
        }

    /**
     * Process any changed files and instrument them.
     */
    private void processChangedFiles() 
        {
        addMarker(f_context.getProject(), "Processing " + f_context.getChangedFiles().size() + " changed files", 
                -1, IMarker.SEVERITY_INFO);
        // merge all the changed files into the schema files
        f_context.mergeChangedFilesToSchema();

        // TODO: Figure out how to update classpath if project changes and 
        // user adds new dependency
        f_context.getChangedFiles().forEach(f -> 
            {
            deleteMarkers(f);
            File classFile = f.getRawLocation().toFile();

            try 
                {
                Object[] oResult = f_reflectionHelper.instrumentClass(classFile);

                String message = oResult[1].toString();

                if (oResult != null && (Integer) oResult[0] == 0) 
                    {
                    MarkerHelper.addMarker(f, message.toString(), -1, IMarker.SEVERITY_INFO);
                    }
                else 
                    {
                    addMarker(f, "Class was not instrumented: " + message.toString(), -1, IMarker.SEVERITY_INFO);
                    }
                // else ignore as the file was not instrumented

                } 
            catch (Exception e) 
                {
                addMarker(f, "Error instrumenting file: " + e, -1, IMarker.SEVERITY_WARNING);
                }
            });
        }

    //----- constants -------------------------------------------------------
    
    public static final String BUILDER_ID = "coherence-eclipse-plugin.coherencePluginBuilder";

    //----- data members ----------------------------------------------------
    
    /**
     * Plugin context.
     */
    private final PluginContext f_context;
    
    /**
     * A visitor for incremental builds.
     */
    private final DeltaVisitor f_deltaVisitor;
    
    /**
     * A visitor for full builds.
     */
    private final ResourceVisitor f_resourceVisitor;
    
    /**
     * Helper for reflection tasks.
     */
    private final ReflectionHelper f_reflectionHelper;
}

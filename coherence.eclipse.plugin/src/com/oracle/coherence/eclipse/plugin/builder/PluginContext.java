/*
 * Copyright (c) 2020, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.eclipse.plugin.builder;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

/**
 * An object to store context for a Plugin which is populated used by the visitors.
 */
public class PluginContext 
    {
    
    //----- constructors ----------------------------------------------------

    /**
     * Construct a PluginContext.
     *  
     * @param builder the {@link CoherencePluginBuilder} that created this context
     */
    public PluginContext(CoherencePluginBuilder builder) 
        {
        this.f_builder = builder;
        this.f_setChangedFiles = new HashSet<>();
        this.f_setSchemaFiles = new HashSet<>();
        }

    //----- PluginContext methods -------------------------------------------

    /**
     * Add a changed file.
     * 
     * @param file the changed file
     */
    public void addChangedFile(IFile file) {
        f_setChangedFiles.add(file);
    }
    
    /**
     * Add a schema file.
     * 
     * @param file a schema file
     */
    public void addSchemaFile(IFile file) {
        f_setSchemaFiles.add(file);
    }
    
    /**
     * Merge the changed and schema files.
     */
    public void mergeChangedFilesToSchema() {
        f_setSchemaFiles.addAll(f_setChangedFiles);
    }
    
    /**
     * Return the {@link Set} of changed files.
     * @return the {@link Set} of changed files
     */
    public Set<IFile> getChangedFiles() {
        return f_setChangedFiles;
    }
    
    /**
     * Return the {@link Set} of schema files
     * @return the {@link Set} of schema file
     */
    public Set<IFile> getSchemaFiles() {
        return f_setSchemaFiles;
    }
    
    /**
     * Return the {@link IProject} for the  {@link CoherencePluginBuilder}.
     * @return the {@link IProject} for the  {@link CoherencePluginBuilder}
     */
    public IProject getProject() {
        return f_builder.getProject();
    }
    
    //----- data members ----------------------------------------------------
    
    /**
     * The {@link Set} of changed files
     */
    private final Set<IFile> f_setChangedFiles;
    
    /**
     * The {@link Set} of schema files
     */
    private final Set<IFile> f_setSchemaFiles;
    
    /**
     * The {@link CoherencePluginBuilder} that created this context.
     */
    private final CoherencePluginBuilder f_builder;
}

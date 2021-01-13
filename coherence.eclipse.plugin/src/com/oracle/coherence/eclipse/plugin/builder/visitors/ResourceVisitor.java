/*
 * Copyright (c) 2020, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.eclipse.plugin.builder.visitors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

import com.oracle.coherence.eclipse.plugin.builder.PluginContext;

/**
 * A Visitor implementation that will visit added and 
 * changed files for a full build.
 */
public class ResourceVisitor extends AbstractVisitor implements IResourceVisitor 
    {

    //----- constructors ----------------------------------------------------

    /**
     * Construct a ResourceVisitor.
     *
     * @param context {@link PluginContext} for this visitor
     */
    public ResourceVisitor(PluginContext context) 
        {
        super(context);
        }
    
    //---- IResourceDeltaVisitor interface ----------------------------------
    
    @Override
    public boolean visit(IResource resource) throws CoreException 
        {
        IFile file = triageResource(resource);
        if (file != null) 
            {
            getContext().addChangedFile(file);
            }
        return true;
        }
    }

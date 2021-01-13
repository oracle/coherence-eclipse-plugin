/*
 * Copyright (c) 2020, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.eclipse.plugin.builder.visitors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

import com.oracle.coherence.eclipse.plugin.builder.PluginContext;

/**
 * A Visitor implementation that will visit added and 
 * changed files for an incremental build.
 */
public class DeltaVisitor extends AbstractVisitor implements IResourceDeltaVisitor 
    {

    //----- constructors ----------------------------------------------------

    /**
     * Construct a DeltaVisitor.
     *
     * @param context {@link PluginContext} for this visitor
     */
    public DeltaVisitor(PluginContext context) 
        {
        super(context);
        }

    //---- IResourceDeltaVisitor interface ----------------------------------
    
    @Override
    public boolean visit(IResourceDelta delta) throws CoreException 
        {
        IResource resource = delta.getResource();
        IFile file = triageResource(resource);
        if (file != null) 
            {
            switch (delta.getKind()) 
                {
                case IResourceDelta.ADDED:
                    // handle added resource
                   getContext().addChangedFile(file);
                   break;
                case IResourceDelta.CHANGED:
                    // handle changed resource
                    getContext().addChangedFile(file);
                     break;
                 }
           }
        
        // return true to continue visiting children.
        return true;
        }
    }

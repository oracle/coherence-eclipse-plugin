/*
 * Copyright (c) 2020, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.eclipse.plugin.builder;

import java.util.Iterator;
import org.eclipse.core.commands.*;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Provides a handler for the Plug-in.
 */
public class AddRemoveCoherencePOFPluginHandler extends AbstractHandler 
    {

    //----- IHandler interface ----------------------------------------------
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException 
        {
        ISelection selection = HandlerUtil.getCurrentSelection(event);

        if (selection instanceof IStructuredSelection) 
            {
            for (Iterator<?> it = ((IStructuredSelection) selection).iterator(); it.hasNext();) {
                Object element = it.next();
                IProject project = null;
                if (element instanceof IProject) 
                    {
                    project = (IProject) element;
                    } 
                else if (element instanceof IAdaptable) 
                    {
                    project = ((IAdaptable) element).getAdapter(IProject.class);
                    }
                if (project != null) 
                    {
                    try 
                        {
                        toggleNature(project);
                        } 
                    catch (CoreException e) 
                        {
                        //TODO log something
                        throw new ExecutionException("Failed to toggle nature", e);
                        }
                    }
                }
            }

        return null;
        }

    /**
     * Toggles nature on a project - i.e. enables or disables the plug-in.
     *
     * @param project {@link IProject} to toggle          
     */
    private void toggleNature(IProject project) throws CoreException 
        {
        IProjectDescription description = project.getDescription();
        String[] natures = description.getNatureIds();

        for (int i = 0; i < natures.length; ++i) 
            {
            if (CoherencePOFPlugin.NATURE_ID.equals(natures[i])) 
                 {
                // Remove the nature
                String[] newNatures = new String[natures.length - 1];
                System.arraycopy(natures, 0, newNatures, 0, i);
                System.arraycopy(natures, i + 1, newNatures, i, natures.length - i - 1);
                description.setNatureIds(newNatures);
                project.setDescription(description, null);
                return;
                }
            }

        // Add the nature
        String[] newNatures = new String[natures.length + 1];
        System.arraycopy(natures, 0, newNatures, 0, natures.length);
        newNatures[natures.length] = CoherencePOFPlugin.NATURE_ID;
        description.setNatureIds(newNatures);
        project.setDescription(description, null);
        }
    }

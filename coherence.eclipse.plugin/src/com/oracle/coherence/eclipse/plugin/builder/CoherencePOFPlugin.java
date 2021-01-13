/*
 * Copyright (c) 2020, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.eclipse.plugin.builder;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

/**
 * A class to enable/ disable the Coherence Plugin.
 */
public class CoherencePOFPlugin implements IProjectNature 
   {

    //----- IProjectNature interface ----------------------------------------
    
    @Override
    public void configure() throws CoreException 
        {
        IProjectDescription desc = m_project.getDescription();
        ICommand[] commands = desc.getBuildSpec();

        for (int i = 0; i < commands.length; ++i) 
            {
            if (commands[i].getBuilderName().equals(CoherencePluginBuilder.BUILDER_ID)) 
                {
                return;
                }
            }

        ICommand[] newCommands = new ICommand[commands.length + 1];
        System.arraycopy(commands, 0, newCommands, 0, commands.length);
        ICommand command = desc.newCommand();
        command.setBuilderName(CoherencePluginBuilder.BUILDER_ID);
        newCommands[newCommands.length - 1] = command;
        desc.setBuildSpec(newCommands);
        m_project.setDescription(desc, null);
        }

    @Override
    public void deconfigure() throws CoreException 
        {
        IProjectDescription description = getProject().getDescription();
        ICommand[] commands = description.getBuildSpec();
        for (int i = 0; i < commands.length; ++i) 
            {
            if (commands[i].getBuilderName().equals(CoherencePluginBuilder.BUILDER_ID)) 
                {
                ICommand[] newCommands = new ICommand[commands.length - 1];
                System.arraycopy(commands, 0, newCommands, 0, i);
                System.arraycopy(commands, i + 1, newCommands, i,
                        commands.length - i - 1);
                description.setBuildSpec(newCommands);
                m_project.setDescription(description, null);
                return;
                }
            }
        }

    @Override
    public IProject getProject() 
        {
        return m_project;
        }

    @Override
    public void setProject(IProject project) 
        {
        this.m_project = project;
        }
    
    //----- constants -------------------------------------------------------
    
    /**
     * ID of this project nature.
     */
    public static final String NATURE_ID = "coherence-eclipse-plugin.coherencePOFPlugin";

    /**
     * The {@link IProject}for this plugin. 
     */
    private IProject m_project;
    }

/*
 * Copyright (c) 2020, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.eclipse.plugin;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin 
    {
    
    //----- constructors ----------------------------------------------------
    
    /**
     * Default constructor.
     */
    public Activator() 
        {
        }
    
    //----- BundleActivator interface ---------------------------------------
    
    @Override
    public void start(BundleContext context) throws Exception 
        {
        super.start(context);
        s_plugin = this;
        }

    @Override
    public void stop(BundleContext context) throws Exception 
        {
        s_plugin = null;
        super.stop(context);
        }

    //----- Activator methods -----------------------------------------------
    
    /**
     * Return the shared instance.
     *
     * @return the shared instance
     */
    public static Activator getDefault() 
        {
        return s_plugin;
        }
    
    //----- constants -------------------------------------------------------

    /**
     * Plug-in id.
     */
    public static final String PLUGIN_ID = "coherence-eclipse-plugin"; //$NON-NLS-1$

    /**
     * Shared plug-in instance.
     */
    private static Activator s_plugin;
    }

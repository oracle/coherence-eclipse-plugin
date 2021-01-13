/*
 * Copyright (c) 2020, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.eclipse.plugin.builder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * A helper adding markers.
 */
public class MarkerHelper {

	//---- constructors -----------------------------------------------------

	/**
	 * Private utilit class constructor.
	 */
	private MarkerHelper() 
	    {
	    }

	//----- helpers ---------------------------------------------------------
	
    /**
     * Add a marker for the given {@link IResource}
     * @param resource {@link IResource} to add marker for
     * @param message  the message to add
     * @param lineNumber the line number or -1 if no line number
     * @param severity severity of the message
     */
    public static void addMarker(IResource resource, String message, int lineNumber, int severity) 
        {
        System.err.println(resource + ": " + message + " (" + lineNumber + "} " + severity);
        try 
            {
            IMarker marker = resource.createMarker(MARKER_TYPE);
            marker.setAttribute(IMarker.MESSAGE, message);
            marker.setAttribute(IMarker.SEVERITY, severity);
            if (lineNumber == -1) 
                {
                lineNumber = 1;
                }
            marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
            } 
        catch (CoreException e) 
            {
            }
        }

    /**
     * Delete the markers for a given {@link IFile}.
     * 
     * @param file the {@link IFile} to delete markers for
     */
    public static void deleteMarkers(IFile file) 
        {
        try 
            {
            file.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
            } 
        catch (CoreException ce) 
            {
            }
        }

    /**
     * Delete all project markers.
     */
    public static void deleteAllProjectMarkers(IProject project) 
        {
        try 
            {
            project.deleteMarkers(MARKER_TYPE, true, IResource.DEPTH_INFINITE);
            } 
        catch (CoreException e) 
            {
            }
        }

    //----- constants -------------------------------------------------------
    
    /**
     * The marker type to be used.
     */
    public static final String MARKER_TYPE = "coherence-eclipse-plugin.portableTypeGeneratorMessage";
    }

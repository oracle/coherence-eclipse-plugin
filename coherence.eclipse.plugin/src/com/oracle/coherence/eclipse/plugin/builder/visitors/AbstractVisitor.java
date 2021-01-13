/*
 * Copyright (c) 2020, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.eclipse.plugin.builder.visitors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import com.oracle.coherence.eclipse.plugin.builder.PluginContext;

/**
 * An abstract implementation of a visitor.
 */
public abstract class AbstractVisitor 
    {

    //----- constructors ----------------------------------------------------
    
    /**
     * Construct an AbstractVisitor.
     *  
     * @param context {@link PluginContext} for this visitor
     */
    public AbstractVisitor(PluginContext context) 
        {
        f_context = context;
        }
    
    //----- AbstractVisitor methods -----------------------------------------
    
    /**
     * Return the {@link PluginContext} for this visitor.
     * 
     * @return the {@link PluginContext} for this visitor
     */
    protected PluginContext getContext() 
        {
        return f_context;
        }
    
    /**
     * Triage a file to see if it is a candidate to annotation processing.
     * 
     * @param resource resource to triage
     * 
     * @return a {@link IFile} if the resource is a candidate or null if it is not
     */
    protected IFile triageResource(IResource resource) 
        {
    	boolean fisCandidate = resource instanceof IFile && 
                resource.getName().endsWith(".class") &&
                !resource.getName().equals("module-info.class");
    	if (fisCandidate)
    	    {
    		IFile ifile = ((IFile) resource);
    		File file = ifile.getRawLocation().toFile();
    		try 
    		    {
    			// use ASM to determine if the class contains the PortableType annotation
				byte[] abBytes = Files.readAllBytes(file.toPath());
				ClassReader reader = new ClassReader(abBytes);
				PortableTypeVisitor visitor = new PortableTypeVisitor(Opcodes.ASM4);
				reader.accept(visitor, 0);
				return visitor.isAnnotated() ? ifile : null; 
			    } 
    		catch (IOException e) 
    		    {
    			System.err.print(e.getMessage());
				return null;
			    }
    	    }
    
    	return null;
        }
    
    //----- inner classes ---------------------------------------------------
    
    /**
     * A {@link ClassVisitor} implementation which checks to see if a class
     * is annotated with the <code>PortableType</code> annotation. 
     */
    public static class PortableTypeVisitor extends ClassVisitor
        {
    	
    	//----- constructors ------------------------------------------------
    	
    	public PortableTypeVisitor(int api) 
    	    {
            super(api);
            }
    	 
    	//----- ClassVisitor methods ----------------------------------------
    	
        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        	m_fIsAnnotated = desc.contains("com/tangosol/io/pof/schema/annotation/PortableType");
            return super.visitAnnotation(desc, visible);
        }
        
    	//----- PortableTypeVisitor methods --------------------------------
        
        /**
         * Return if the class is annotated with the PortableType annotation. 
         * @return true if the class is annotated with the PortableType annotation
         */
        public boolean isAnnotated()
            {
        	return m_fIsAnnotated;
            }
    	
    	 
    	//----- data members ------------------------------------------------
    	
    	/**
    	 * Indicates if the class has the PortableType annotation.
    	 */
    	private boolean m_fIsAnnotated;
    }
    
    
    //----- data members ----------------------------------------------------
    
    /**
     * Context for the visitor.
     */
    private final PluginContext f_context;
}

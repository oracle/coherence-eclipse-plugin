/*
 * Copyright (c) 2020, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.eclipse.plugin.builder;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;

/**
 * A helper class for issue reflection calls.
 */
public class ReflectionHelper 
    {
    
    //---- constructors -----------------------------------------------------
    
    /**
     * Construct a ReflectionHelper.
     */
    public ReflectionHelper(PluginContext context) 
        {
        f_listProjects = new ArrayList<>();
        f_listProjectLoaders = new ArrayList<>();
        f_listClasspath = new ArrayList<>();
        f_context = context;

        try 
            {
            refreshClassLoaders();
            } 
        catch (MalformedURLException | CoreException e) 
            {
            throw new RuntimeException("Unable to refresh ClassLoaders", e);
            }
        }

    //---- ReflectionHelper methods -----------------------------------------
    
    /**
     * Refresh the {@link URLClassLoader}s for all the open projects.
     * 
     * @throws MalformedURLException
     * @throws CoreException
     */
    public void refreshClassLoaders() throws MalformedURLException, CoreException 
        {
        f_listProjects.clear();
        f_listProjectLoaders.clear();

        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        for (IProject project : projects) 
            {
            if (project.isOpen()) 
                {
                IJavaProject javaProject = JavaCore.create(project);
                f_listProjectLoaders.add(getProjectClassLoader(javaProject));
                }
            }
        }
    
    /**
     * Instrument a given class file.
     * 
     * @param fileClass the class to introspect.
     * @return an {@link Object} array containing two elements. [0] is a integer with 0 
     *           indicating success and 1 indicating failure. [1] contains any message for success or failure  
     */
    public Object[] instrumentClass(File fileClass) 
        {
        ClassLoader loader = getClass().getClassLoader();
        Thread current = Thread.currentThread();
        ClassLoader oldLoader = current.getContextClassLoader();
        Properties properties = new Properties();
        ClassLoader loaderParent = loader.getParent();

        Object[] oResult = new Object[2];

        try 
            {
            // look through each of the URLClassLoaders until we find the POF generator class
            Class<?> clsPofGenerator = null;
            ClassLoader clsLoaderPofGenerator = null;
            
            for (URLClassLoader urlClassLoader : getProjectLoaders())
                {
                clsPofGenerator = findGeneratorClass(urlClassLoader);
                if (clsPofGenerator != null) 
                    {
                    clsLoaderPofGenerator = urlClassLoader;
                    break;
                    }
                }

            // the POF generator class was found
            if (clsPofGenerator != null) 
                {
                current.setContextClassLoader(clsLoaderPofGenerator);
                
                java.nio.file.Path pathClass = fileClass.toPath();
                byte[] abBytes = Files.readAllBytes(pathClass);
                int nOffset = 0;
                int nLen = abBytes.length;
                byte[] abInst = null;
                Method method = getInstrumentMethod(clsPofGenerator);
                Map<String, Object> mapEnv = new HashMap<>();

                mapEnv.put("libs", getListClasspathAsFile());
                mapEnv.put("schema", ensureSchema(clsPofGenerator, fileClass, loaderParent, mapEnv));

                if (method != null) 
                    {
                    abInst = (byte[]) method.invoke(null, fileClass, abBytes, nOffset, nLen, properties, mapEnv);
                    m_oSchema = mapEnv.get("schema");
                    }

                if (abInst != null) 
                    {
                    oResult[0] = 0; // success
                    oResult[1] = "Instrumented class";

                    // save the instrumented created class
                    Files.write(pathClass, abInst);
                    } 
                else 
                    {
                    oResult[0] = 1; // class was not instrumented
                    oResult[1] = "";
                    }
                }
            } 
        catch (Exception e) 
            {
            return new Object[] { 0, "POF generation internal error - " + e.getMessage() };
            } 
        finally 
            {
            current.setContextClassLoader(oldLoader);
            }

        return oResult;
        }
    
    // ----- helper methods -------------------------------------------------

    /**
     * Return the current classpath as a {@link List} of {@link File}s.
     * @return  the current classpath as a {@link List} of {@link File}s
     */
    private List<File> getListClasspathAsFile()
        {
        return f_listClasspath.stream().map(u -> new File(u.getFile()))
                .collect(Collectors.toList());
        }

    /**
     * Return the current classpath as a {@link List} of {@link URL}s.
     * @return  the current classpath as a {@link List} of {@link URL}s
     */
    public List<URL> getListClasspath()
        {
        return f_listClasspath;
        }

    /**
     * Return the {@link List} of {@link URLClassLoader}s for the project.
     * @return {@link List} of {@link URLClassLoader}s for the project
     */
    public List<URLClassLoader> getProjectLoaders()
        {
        return f_listProjectLoaders;
        }

    /**
     * Return the {@link URLClassLoader} for a given {@link IJavaProject}
     * 
     * @param javaProject the {@link IJavaProject}
     * 
     * @return a {@link URLClassLoader}
     * @throws MalformedURLException
     * @throws CoreException
     */
    private URLClassLoader getProjectClassLoader(IJavaProject javaProject) 
            throws MalformedURLException, CoreException 
        {
        f_listClasspath.clear();
        for (String entry : JavaRuntime.computeDefaultRuntimeClassPath(javaProject)) 
            {
            IPath path = org.eclipse.core.runtime.Path.fromOSString(entry);
            f_listClasspath.add(path.toFile().toURI().toURL());
            }
        
        ClassLoader parentClassLoader = javaProject.getClass().getClassLoader();
        URL[] urls = (URL[]) f_listClasspath.toArray(new URL[f_listClasspath.size()]);
        return new URLClassLoader(urls, parentClassLoader);
        }

    /**
     * Find the POF generator class to use.
     *
     * @param loader the {@link ClassLoader} to use to find the POF Generator
     *
     * @return the POF Generator class or {@code null} if no POF Generator is on the
     *         class path
     */
    private Class<?> findGeneratorClass(ClassLoader loader) 
        {
        for (String sCls : CLASS_NAMES) 
            {
            try 
                {
                return loader.loadClass(sCls);
                } 
            catch (ClassNotFoundException e) 
                {
                // ignored - class is not on class path
                }
            }
        
        return null;
        }

    /**
     * Generate the schema. 
     * @param clsPofGenerator POF Generate class
     * @param fileClass       {@link File} of the class file
     * @param loader          {@link ClassLoader} to use
     * @param mapEnv          environment {@Link Map} 
     * @return the Schema 
     */
    private Object ensureSchema(Class<?> clsPofGenerator, File fileClass, ClassLoader loader, Map<String, ?> mapEnv) 
        {
        if (m_loaderPrev == null || !m_loaderPrev.equals(loader)) 
            {
            m_oSchema = null;
            }

        m_loaderPrev = loader;

        if (m_oSchema == null) 
            {
            try 
                {
                Method createSchema = clsPofGenerator.getMethod("createSchema", File.class, Map.class);

                m_oSchema = createSchema.invoke(null, fileClass, mapEnv);
                } 
            catch (Exception e) 
                {
                // should not happen
                }
            }

        return m_oSchema;
        }

    /**
     * Find the instrumentation method.
     *
     * @param cls the {@link Class} to find the method on
     *
     * @return the instrumentation method reference
     */
    private Method getInstrumentMethod(Class<?> cls) 
        {
        try 
            {
            return cls.getMethod("instrumentClass", File.class, byte[].class, int.class, int.class, 
                                 Properties.class, Map.class);
            } 
        catch (Exception e) 
            {
            // ignore exception, method does not exist
            return null;
            }
        }

    // ---- constants -------------------------------------------------------
    
    /**
     * The array of possible POF generator class names.
     */
    private static final String[] CLASS_NAMES = 
        { 
            "com.oracle.io.pof.generator.PortableTypeGenerator",
            "com.tangosol.io.pof.generator.PortableTypeGenerator" 
        };

    // ---- data members ----------------------------------------------------
    
    /**
     * The previous {@link ClassLoader}.
     */
    private ClassLoader m_loaderPrev = null;

    /**
     * The Schema object.
     */
    private Object m_oSchema;

    /**
     * The  {@link List} of open projects in the workspace the plug-in is running in.
     */
    private final List<IJavaProject> f_listProjects;

    /**
     * The  {@link List} of {@link URLClassLoader}s for the current open projects. 
     */
    private final List<URLClassLoader> f_listProjectLoaders;
    
    /**
     * The {@link List} of {@link URL} that make up the classpath.
     */
    private final List<URL> f_listClasspath;
    
    /**
     * The Plugin context.
     */
    private final PluginContext f_context;
    }

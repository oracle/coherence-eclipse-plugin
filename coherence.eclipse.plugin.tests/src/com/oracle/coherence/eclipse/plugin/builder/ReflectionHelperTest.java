/*
 * Copyright (c) 2020, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.eclipse.plugin.builder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.junit.Test;

import com.oracle.coherence.eclipse.plugin.builder.PluginContext;
import com.oracle.coherence.eclipse.plugin.builder.ReflectionHelper;
import com.oracle.coherence.eclipse.plugin.builder.CoherencePluginBuilder;

import com.tangosol.net.CacheFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.is;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ReflectionHelper}.
 */
public class ReflectionHelperTest
    {

    //----- tests -----------------------------------------------------------
        
    @Test
    public void testInstrumentation()
            throws MalformedURLException
        {
        String sCoherenceVersion = System.getProperty("coherence.version");
        System.err.println("Testing against Coherence version " + sCoherenceVersion);
        CoherencePluginBuilder builder = new CoherencePluginBuilder();
        PluginContext context = new PluginContext(builder);
        ReflectionHelper helper = new ReflectionHelper(context);

        File filePerson = getClassFile(Person.class);
        File fileAddress = getClassFile(Address.class);
        File fileNonAnnotated = getClassFile(NonAnnotatedPerson.class);

        assertThat(filePerson.exists(), is(true));
        assertThat(fileAddress.exists(), is(true));
        assertThat(fileNonAnnotated.exists(), is(true));

        // add coherence jar to the classloader
        List<URLClassLoader> listProjectLoaders = new ArrayList<>();
        ClassLoader parentClassLoader = ReflectionHelperTest.class.getClassLoader();
        File fileCoherence = new File("target" + SEP + "runtime-bin" + SEP + "coherence-" + sCoherenceVersion + ".jar");
        File fileClasses = new File("target" + SEP + "classes");
        helper.getProjectLoaders().add(new URLClassLoader(new URL[] {
                                       fileCoherence.toURI().toURL(), fileClasses.toURI().toURL()}, parentClassLoader));
        helper.getListClasspath().add(new File("target" + SEP + "classes").toURI().toURL());
        
        Object[] oResult = helper.instrumentClass(filePerson);
        assertThat("Class was not instrumented: " + oResult[1], oResult[0], is(0)); // instrumented

        oResult = helper.instrumentClass(fileAddress);
        assertThat("Class was not instrumented: " + oResult[1], oResult[0], is(0)); // instrumented

        oResult = helper.instrumentClass(fileNonAnnotated);
        assertThat("Class was instrumented when it should not have been: " + oResult[1], oResult[0], is(1)); // not-instrumented
        }

    private File getClassFile(Class<?> clazz)
        {
        return new File("target" + SEP + "classes" + SEP + clazz.getName().replaceAll("\\.", SEP) + ".class");
        }

    //----- constants -------------------------------------------------------

    private static final String SEP = File.separator;
    }

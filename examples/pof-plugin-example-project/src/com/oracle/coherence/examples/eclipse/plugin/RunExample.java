/*
 * Copyright (c) 2020, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.eclipse.plugin;

import java.util.Random;

import com.tangosol.io.pof.PortableObject;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;


/**
 * Run the example.
 * 
 * Note: Ensure you have enabled the "Coherence POF Plugin" by right-clicking
 * on the project and choosing "Configure" then "Enable Coherence POF Plugin".   
 */
public class RunExample 
    {
	
	public static void main(String ...args)
	    {
		System.setProperty("coherence.cacheconfig", "demo-cache-config.xml");
		System.setProperty("coherence.distributed.localstorage", "true");
		System.setProperty("coherence.log.level", "2");
		
		NamedCache<Integer, Person> nc = CacheFactory.getCache("person");
		NamedCache<Integer, Employee> ncEmp = CacheFactory.getCache("employee");
		
		Person p = new Person(1, "John", getAddress());
		nc.put(p.getId(), p);
		System.out.println("Person cache size is " + nc.size());
		
		Person p2 = nc.get(1);
		System.out.println(p2);
		
		System.out.println("Person classes are equal? " + p.equals(p2));
		System.out.println("Person is PortableObject? " + (p2 instanceof PortableObject));
		System.out.println("Address is PortableObject? " + (p2.getAddress() instanceof PortableObject));
		
		Employee employee = new Employee(2, "Tim Jones", getAddress(), 123456, "Oracle");
		System.out.println("Name is " + employee.getName());
		ncEmp.put(employee.getEmpId(), employee);
		Employee employee2 = ncEmp.get(123456);
		System.out.println("Employee is " + employee2);
	    }   
	
	private static Address getAddress() 
	    {
		return new Address("Address 1", "Address 2", "Perth", "WA", Integer.valueOf(RANDOM.nextInt(5000) + 1).toString(), "AU");
	    }
	
	private static final Random RANDOM = new Random();
    }

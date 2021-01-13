/*
 * Copyright (c) 2020, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.eclipse.plugin.builder;

/**
 * This class is specifically not annotated with PortableType.
 */
public class NonAnnotatedPerson {

	//---- constructors -----------------------------------------------------

	public NonAnnotatedPerson()
	    {
	    }

	public NonAnnotatedPerson(int nId, String sName, Address sAddress)
	    {
		super();
		this.m_nId = nId;
		this.m_sName = sName;
		this.m_sAddress = sAddress;
	    }

	// ---- Object interface ------------------------------------------------

	@Override
	public String toString()
	    {
		return "Person [id=" + m_nId + ", name=" + m_sName + ", address=" + m_sAddress + "]";
	    }

    //----- Person methods --------------------------------------------------

	public int getId()
	    {
		return m_nId;
	    }

	public void setId(int id)
	    {
		this.m_nId = id;
	    }

	public String getName()
	    {
		return m_sName;
	    }

	public void setName(String name)
        {
	    this.m_sName = name;
	    }

	public Address getAddress()
	    {
		return m_sAddress;
	    }

	public void setAddress(Address address)
	    {
		this.m_sAddress = address;
	    }

	//----- data members ----------------------------------------------------

	int m_nId;
	String m_sName;
	Address m_sAddress;
    }

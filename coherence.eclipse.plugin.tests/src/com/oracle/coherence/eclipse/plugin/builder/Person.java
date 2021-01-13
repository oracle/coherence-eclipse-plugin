/*
 * Copyright (c) 2020, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.eclipse.plugin.builder;

import com.tangosol.io.pof.schema.annotation.PortableType;
import com.tangosol.util.Base;

@PortableType(id=1000)
public class Person {

	//---- constructors -----------------------------------------------------

	public Person()
	    {
	    }

	public Person(int nId, String sName, Address sAddress)
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

	@Override
	public int hashCode()
	    {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_sAddress == null) ? 0 : m_sAddress.hashCode());
		result = prime * result + m_nId;
		result = prime * result + ((m_sName == null) ? 0 : m_sName.hashCode());
		return result;
	    }

	@Override
	public boolean equals(Object obj)
	    {
		if (this == obj)
		    {
			return true;
		    }
		if (obj == null || getClass() != obj.getClass())
		    {
			return false;
		    }
		return Base.equals(obj, (Person) obj);
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

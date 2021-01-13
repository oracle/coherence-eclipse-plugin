/*
 * Copyright (c) 2020, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.eclipse.plugin;

import com.tangosol.io.pof.schema.annotation.PortableType;

/**
 * Example employee class.
 */
@PortableType(id = 1002)
public class Employee extends Person 
    {

	//---- constructors -----------------------------------------------------
	
	public Employee() 
	    {
		super();
	    }

	public Employee(int nId, String sName, Address sAddress, int nEmpId, String sEmployer) 
	    {
		super(nId, sName, sAddress);
		this.m_nEmpId = nEmpId;
		this.sEmployer = sEmployer; 
	    } 

    //---- Employee methods -------------------------------------------------
	
	public int getEmpId() 
	    {
		return m_nEmpId;
	    }

	public void setEmpId(int nEmpId) 
	    {
		this.m_nEmpId = nEmpId;
	    }

	public String getEmployer() {
		return sEmployer;
	}

	public void setEmployer(String sEmployer) 
	    {
		this.sEmployer = sEmployer;
	    }
	
	//----- Object interface ------------------------------------------------

	@Override
	public String toString() 
	    {
		return "Employee [empId=" + m_nEmpId + ", employer=" + sEmployer + ", id=" + getId() + 
				 ", name=" + getName() + ", address="
			+ getAddress() + "]";
	    }
	
	//----- data members ----------------------------------------------------
	
	private int m_nEmpId;
	private String sEmployer;
    }

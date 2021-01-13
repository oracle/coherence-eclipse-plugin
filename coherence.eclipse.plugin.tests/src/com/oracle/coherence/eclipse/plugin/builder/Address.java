package com.oracle.coherence.eclipse.plugin.builder;

/*
 * Copyright (c) 2020, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

import com.tangosol.io.pof.schema.annotation.PortableType;
import com.tangosol.util.Base;

/**
 * Example Address class.
 */
@PortableType(id = 1001)
public class Address
    {

	//---- constructors -----------------------------------------------------

	public Address()
        {
	    }

    public Address(String sAddressLine1, String sAddressLine2, String sCity, String sState,
    		String sPostCode, String sCountry)
        {
		super();
		this.m_sAddressLine1 = sAddressLine1;
		this.m_sAddressLine2 = sAddressLine2;
		this.m_sCity = sCity;
		this.m_sState = sState;
		this.m_sPostCode = sPostCode;
		this.m_sCountry = sCountry;
	    }

    //---- Address methods --------------------------------------------------

	public String getAddressLine1()
	    {
		return m_sAddressLine1;
	    }

	public void setAddressLine1(String addressLine1)
	    {
		this.m_sAddressLine1 = addressLine1;
	    }

	public String getAddressLine2()
	    {
		return m_sAddressLine2;
	    }

	public void setAddressLine2(String addressLine2)
	    {
		this.m_sAddressLine2 = addressLine2;
	    }

	public String getCity()
	    {
		return m_sCity;
	    }

	public void setCity(String city)
	    {
		this.m_sCity = city;
	    }

	public String getState()
	    {
		return m_sState;
	    }

	public void setState(String state)
	    {
		this.m_sState = state;
	    }

	public String getPostCode()
	    {
		return m_sPostCode;
	    }

	public void setPostCode(String postCode)
	    {
		this.m_sPostCode = postCode;
	    }

	public String getCountry()
	    {
		return m_sCountry;
	    }

	public void setCountry(String country)
	    {
		this.m_sCountry = country;
	    }

	//----- Object interface ------------------------------------------------

	@Override
	public String toString()
	    {
		return "Address [addressLine1=" + m_sAddressLine1 + ", addressLine2=" + m_sAddressLine2 + ", city=" + m_sCity
				+ ", state=" + m_sState + ", postCode=" + m_sPostCode + ", country=" + m_sCountry + "]";
	    }

	@Override
	public int hashCode()
	    {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_sAddressLine1 == null) ? 0 : m_sAddressLine1.hashCode());
		result = prime * result + ((m_sAddressLine2 == null) ? 0 : m_sAddressLine2.hashCode());
		result = prime * result + ((m_sCity == null) ? 0 : m_sCity.hashCode());
		result = prime * result + ((m_sCountry == null) ? 0 : m_sCountry.hashCode());
		result = prime * result + ((m_sPostCode == null) ? 0 : m_sPostCode.hashCode());
		result = prime * result + ((m_sState == null) ? 0 : m_sState.hashCode());
		return result;
	    }

	@Override
	public boolean equals(Object obj)
	    {
		if (this == obj)
		    {
			return true;
		    }
		if (obj == null)
		    {
			return false;
		    }
		if (getClass() != obj.getClass())
		    {
			return false;
		    }
		Address other = (Address) obj;
		return Base.equals(obj, other);
	}

	//---- data members -----------------------------------------------------

	private String m_sAddressLine1;
    private String m_sAddressLine2;
    private String m_sCity;
    private String m_sState;
    private String m_sPostCode;
    private String m_sCountry;
    }

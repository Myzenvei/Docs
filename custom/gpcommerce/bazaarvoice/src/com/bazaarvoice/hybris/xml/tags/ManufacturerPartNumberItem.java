/**
 * 
 */
package com.bazaarvoice.hybris.xml.tags;

import javax.xml.bind.annotation.XmlValue;


/**
 * @author christina
 * 
 */
public class ManufacturerPartNumberItem
{
	@XmlValue
	String value;

	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * @param value
	 *           the value to set
	 */
	public void setValue(final String value)
	{
		this.value = value;
	}

	/**
	 * 
	 */
	public ManufacturerPartNumberItem()
	{
		super();
	}

	/**
	 * @param value
	 */
	public ManufacturerPartNumberItem(final String value)
	{
		super();
		this.value = value;
	}


}

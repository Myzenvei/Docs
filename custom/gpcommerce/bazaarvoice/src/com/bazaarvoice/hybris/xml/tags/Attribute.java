/**
 * 
 */
package com.bazaarvoice.hybris.xml.tags;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



/**
 * @author christina romashchenko
 * 
 */
@XmlType(propOrder =
{ "id", "Value" })
@XmlAccessorType(XmlAccessType.FIELD)
public class Attribute
{
	@XmlAttribute
	String id;
	@XmlElement
	String Value;

	/**
	 * @param id
	 * @param value
	 */
	public Attribute(final String id, final String value)
	{
		super();
		this.id = id;
		this.Value = value;
	}

	/**
	 * 
	 */
	public Attribute()
	{
		super();
	}



}

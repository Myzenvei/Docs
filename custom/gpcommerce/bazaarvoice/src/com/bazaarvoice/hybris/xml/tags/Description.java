/**
 * 
 */
package com.bazaarvoice.hybris.xml.tags;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * @author christina romashchenko
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Description
{
	@XmlAttribute
	String locale;
	@XmlJavaTypeAdapter(AdapterCDATA.class)
	@XmlValue
	String value;

	/**
	 * @param locale
	 * @param value
	 */
	public Description(final String locale, final String value)
	{
		super();
		this.locale = locale;
		this.value = value;
	}

	/**
	 * 
	 */
	public Description()
	{
		super();
	}


}

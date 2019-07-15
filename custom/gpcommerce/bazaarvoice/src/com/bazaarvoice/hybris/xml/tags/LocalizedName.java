/**
 * 
 */
package com.bazaarvoice.hybris.xml.tags;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * @author christina romashchenko
 * 
 */
public class LocalizedName
{
	String locale;
	String value;

	public LocalizedName(final String locale, final String value)
	{
		super();
		this.locale = locale;
		this.value = value;
	}

	public LocalizedName()
	{
		super();
	}

	public String getLocale()
	{
		return locale;
	}

	@XmlAttribute(name = "locale", required = true)
	public void setLocale(final String locale)
	{
		this.locale = locale;
	}

	public String getValue()
	{
		return value;
	}

	@XmlJavaTypeAdapter(AdapterCDATA.class)
	@XmlValue
	public void setValue(final String value)
	{
		this.value = value;
	}

}

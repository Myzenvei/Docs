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
//@XmlRootElement(namespace = "bazaarvoice.xml.tags.CategoryTag")
public class LocalizedImageUrl
{

	String locale;

	String value;

	/**
	 * @param locale
	 * @param value
	 */
	public LocalizedImageUrl(final String locale, final String value)
	{
		super();
		this.locale = locale;
		this.value = value;
	}

	/**
	 * @param locale
	 * @param value
	 */
	public LocalizedImageUrl()
	{
		super();
	}

	/**
	 * @return the locale
	 */
	public String getLocale()
	{
		return locale;
	}

	/**
	 * @param locale
	 *           the locale to set
	 */
	@XmlAttribute(name = "locale", required = true)
	public void setLocale(final String locale)
	{
		this.locale = locale;
	}

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
	@XmlJavaTypeAdapter(AdapterCDATA.class)
	@XmlValue
	public void setValue(final String value)
	{
		this.value = value;
	}



}

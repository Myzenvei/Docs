/**
 * 
 */
package com.bazaarvoice.hybris.xml.tags;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;


/**
 * @author christina romashchenko
 * 
 */
//@XmlRootElement(namespace = "bazaarvoice.xml.tags.CategoryTag")
public class CategoryPageUrl
{

	String locale;

	String url;


	public CategoryPageUrl(final String locale, final String url)
	{
		super();
		this.locale = locale;
		this.url = url;
	}



	/**
	 * 
	 */
	public CategoryPageUrl()
	{
		super();
	}


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
	 * @return the url
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * @param url
	 *           the url to set
	 */
	@XmlValue
	public void setUrl(final String url)
	{
		this.url = url;
	}


}

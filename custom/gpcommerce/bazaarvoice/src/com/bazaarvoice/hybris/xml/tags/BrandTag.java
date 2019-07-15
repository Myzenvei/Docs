/**
 * 
 */
package com.bazaarvoice.hybris.xml.tags;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * @author christina romashchenko
 * 
 */
@XmlType(propOrder =
{ "externalId", "name", "names" })
@XmlAccessorType(XmlAccessType.FIELD)
public class BrandTag
{
	@XmlElement(name = "ExternalId", required = true)
	String externalId;

	@XmlJavaTypeAdapter(AdapterCDATA.class)
	@XmlElement(name = "Name", required = true)
	String name;

	@XmlElementWrapper(name = "Names", required = true)
	@XmlElement(name = "Name")
	List<LocalizedName> names;


	public String getExternalId()
	{
		return externalId;
	}



	public void setExternalId(final String externalId)
	{
		this.externalId = externalId;
	}


	public String getName()
	{
		return name;
	}


	public void setName(final String name)
	{
		this.name = name;
	}


	public List<LocalizedName> getNames()
	{
		return names;
	}

	public void setNames(final List<LocalizedName> names)
	{
		this.names = names;
	}



}

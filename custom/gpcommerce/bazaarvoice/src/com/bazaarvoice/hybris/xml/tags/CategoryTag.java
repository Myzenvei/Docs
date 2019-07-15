/**
 * 
 */
package com.bazaarvoice.hybris.xml.tags;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * @author christina
 * 
 */
//@XmlRootElement(namespace = "bazaarvoice.xml.tags.ProductFeed")
@XmlType(propOrder =
{ "name", "names", "externalId", "categoryPageUrl", "categoryPageUrls", "parentExternalId", "imageUrl", "imageUrls" })
public class CategoryTag
{

	String name;
	Collection<LocalizedName> names;
	String externalId;
	String categoryPageUrl;
	Collection<CategoryPageUrl> categoryPageUrls;
	String parentExternalId;
	String imageUrl;
	Collection<LocalizedImageUrl> imageUrls;


	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *           the name to set
	 */
	@XmlJavaTypeAdapter(AdapterCDATA.class)
	@XmlElement(name = "Name", required = true)
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * @return the names
	 */
	public Collection<LocalizedName> getNames()
	{
		return names;
	}

	/**
	 * @param names
	 *           the names to set
	 */
	@XmlElementWrapper(name = "Names", required = true)
	@XmlElement(name = "Name")
	public void setNames(final Collection<LocalizedName> names)
	{
		this.names = names;
	}

	/**
	 * @return the externalId
	 */
	public String getExternalId()
	{
		return externalId;
	}

	/**
	 * @param externalId
	 *           the externalId to set
	 */
	@XmlElement(name = "ExternalId", required = true)
	public void setExternalId(final String externalId)
	{
		this.externalId = externalId;
	}

	/**
	 * @return the categoryPageUrl
	 */
	public String getCategoryPageUrl()
	{
		return categoryPageUrl;
	}

	/**
	 * @param categoryPageUrl
	 *           the categoryPageUrl to set
	 */
	@XmlJavaTypeAdapter(AdapterCDATA.class)
	@XmlElement(name = "CategoryPageUrl")
	public void setCategoryPageUrl(final String categoryPageUrl)
	{
		this.categoryPageUrl = categoryPageUrl;
	}

	/**
	 * @return the categoryPageUrls
	 */
	public Collection<CategoryPageUrl> getCategoryPageUrls()
	{
		return categoryPageUrls;
	}

	/**
	 * @param categoryPageUrls
	 *           the categoryPageUrls to set
	 */
	@XmlElementWrapper(name = "CategoryPageUrls", required = true)
	@XmlElement(name = "CategoryPageUrl")
	public void setCategoryPageUrls(final Collection<CategoryPageUrl> categoryPageUrls)
	{
		this.categoryPageUrls = categoryPageUrls;
	}

	/**
	 * @return the parentExternalId
	 */
	public String getParentExternalId()
	{
		return parentExternalId;
	}

	/**
	 * @param parentExternalId
	 *           the parentExternalId to set
	 */
	@XmlElement(name = "ParentExternalId")
	public void setParentExternalId(final String parentExternalId)
	{
		this.parentExternalId = parentExternalId;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl()
	{
		return imageUrl;
	}

	/**
	 * @param imageUrl
	 *           the imageUrl to set
	 */
	@XmlJavaTypeAdapter(AdapterCDATA.class)
	@XmlElement(name = "ImageUrl")
	public void setImageUrl(final String imageUrl)
	{
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the imageUrls
	 */
	public Collection<LocalizedImageUrl> getImageUrls()
	{
		return imageUrls;
	}

	/**
	 * @param imageUrls
	 *           the imageUrls to set
	 */
	@XmlElementWrapper(name = "ImageUrls", required = true)
	@XmlElement(name = "ImageUrl")
	public void setImageUrls(final Collection<LocalizedImageUrl> imageUrls)
	{
		this.imageUrls = imageUrls;
	}



}

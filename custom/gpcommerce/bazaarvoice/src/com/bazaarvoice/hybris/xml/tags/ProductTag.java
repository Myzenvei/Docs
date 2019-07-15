/**
 *
 */
package com.bazaarvoice.hybris.xml.tags;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * @author christina romashchenko
 */
@XmlType(propOrder =
{ "externalId", "name", "names", "description", "descriptions", "brandExternalId", "categoryExternalId", "productPageUrl",
		"productPageUrls", "imageUrl", "modelNumbers", "eans", "ean", "manufacturerPartNumbers", "manufacturerPartNumber", "upcs",
		"upc", "isbns", "isbn", "attributes" })
//@XmlAccessorType(XmlAccessType.FIELD)
public class ProductTag
{
	String name;
	List<LocalizedName> names;
	String externalId;
	String categoryExternalId;
	String brandExternalId;
	String productPageUrl;
	List<LocalizedName> productPageUrls;
	String imageUrl;
	String ean;
	String manufacturerPartNumber;
	String upc;
	String isbn;
	String description;
	List<Description> descriptions;
	List<String> eans;
	List<String> manufacturerPartNumbers;
	List<String> upcs;
	List<String> isbns;
	List<String> modelNumbers;
	List<Attribute> attributes;

	/**
	 * @return the name
	 */
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
	 * @return the categoryExternalId
	 */
	public String getCategoryExternalId()
	{
		return categoryExternalId;
	}

	/**
	 * @param categoryExternalId
	 *           the categoryExternalId to set
	 */
	@XmlElement(name = "CategoryExternalId", required = true)
	public void setCategoryExternalId(final String categoryExternalId)
	{
		this.categoryExternalId = categoryExternalId;
	}

	/**
	 * @return the productPageUrl
	 */
	public String getProductPageUrl()
	{
		return productPageUrl;
	}

	/**
	 * @param productPageUrl
	 *           the productPageUrl to set
	 */
	@XmlJavaTypeAdapter(AdapterCDATA.class)
	@XmlElement(name = "ProductPageUrl", required = true)
	public void setProductPageUrl(final String productPageUrl)
	{
		this.productPageUrl = productPageUrl;
	}

	/**
	 * @return the productPageUrls
	 */
	public List<LocalizedName> getProductPageUrls()
	{
		return productPageUrls;
	}

	/**
	 * @param productPageUrls
	 *           the names to set
	 */
	@XmlElementWrapper(name = "ProductPageUrls")
	@XmlElement(name = "ProductPageUrl")
	public void setProductPageUrls(final List<LocalizedName> productPageUrls)
	{
		this.productPageUrls = productPageUrls;
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
	@XmlElement(name = "ImageUrl", required = true)
	public void setImageUrl(final String imageUrl)
	{
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the ean
	 */
	public String getEan()
	{
		return ean;
	}

	/**
	 * @param ean
	 *           the ean to set
	 */
	@XmlElement(name = "EAN")
	public void setEan(final String ean)
	{
		this.ean = ean;
	}

	/**
	 * @return the manufacturerPartNumber
	 */
	public String getManufacturerPartNumber()
	{
		return manufacturerPartNumber;
	}

	/**
	 * @param manufacturerPartNumber
	 *           the manufacturerPartNumber to set
	 */
	@XmlElement(name = "ManufacturerPartNumber")
	public void setManufacturerPartNumber(final String manufacturerPartNumber)
	{
		this.manufacturerPartNumber = manufacturerPartNumber;
	}

	/**
	 * @return the upc
	 */
	public String getUpc()
	{
		return upc;
	}

	/**
	 * @param upc
	 *           the upc to set
	 */
	@XmlElement(name = "UPC")
	public void setUpc(final String upc)
	{
		this.upc = upc;
	}

	/**
	 * @return the isbn
	 */
	public String getIsbn()
	{
		return isbn;
	}

	/**
	 * @param isbn
	 *           the isbn to set
	 */
	@XmlElement(name = "ISBN")
	public void setIsbn(final String isbn)
	{
		this.isbn = isbn;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 *           the description to set
	 */
	@XmlJavaTypeAdapter(AdapterCDATA.class)
	@XmlElement(name = "Description")
	public void setDescription(final String description)
	{
		this.description = description;
	}

	/**
	 * @return the descriptions
	 */
	public List<Description> getDescriptions()
	{
		return descriptions;
	}

	/**
	 * @param descriptions
	 *           the descriptions to set
	 */
	@XmlElementWrapper(name = "Descriptions", required = true)
	@XmlElement(name = "Description")
	public void setDescriptions(final List<Description> descriptions)
	{
		this.descriptions = descriptions;
	}

	/**
	 * @return the eans
	 */
	public List<String> getEans()
	{
		return eans;
	}

	/**
	 * @param eans
	 *           the eans to set
	 */
	@XmlElementWrapper(name = "EANs")
	@XmlElement(name = "EAN")
	public void setEans(final List<String> eans)
	{
		this.eans = eans;
	}

	/**
	 * @return the manufacturerPartNumbers
	 */
	public List<String> getManufacturerPartNumbers()
	{
		return manufacturerPartNumbers;
	}

	/**
	 * @param manufacturerPartNumbers
	 *           the manufacturerPartNumbers to set
	 */
	@XmlElementWrapper(name = "ManufacturerPartNumbers")
	@XmlElement(name = "ManufacturerPartNumber")
	public void setManufacturerPartNumbers(final List<String> manufacturerPartNumbers)
	{
		this.manufacturerPartNumbers = manufacturerPartNumbers;
	}

	/**
	 * @return the upcs
	 */
	public List<String> getUpcs()
	{
		return upcs;
	}

	/**
	 * @param upcs
	 *           the upcs to set
	 */
	@XmlElementWrapper(name = "UPCs")
	@XmlElement(name = "UPC")
	public void setUpcs(final List<String> upcs)
	{
		this.upcs = upcs;
	}

	/**
	 * @return the isbns
	 */
	public List<String> getIsbns()
	{
		return isbns;
	}

	/**
	 * @param isbns
	 *           the isbns to set
	 */
	@XmlElementWrapper(name = "ISBNs")
	@XmlElement(name = "ISBN")
	public void setIsbns(final List<String> isbns)
	{
		this.isbns = isbns;
	}

	/**
	 * @return the names
	 */
	public List<LocalizedName> getNames()
	{
		return names;
	}

	/**
	 * @param names
	 *           the names to set
	 */
	@XmlElementWrapper(name = "Names")
	@XmlElement(name = "Name")
	public void setNames(final List<LocalizedName> names)
	{
		this.names = names;
	}

	/**
	 * @return the attributes
	 */
	public List<Attribute> getAttributes()
	{
		return attributes;
	}

	/**
	 * @param attributes
	 *           the attributes to set
	 */
	@XmlElementWrapper(name = "Attributes")
	@XmlElement(name = "Attribute")
	public void setAttributes(final List<Attribute> attributes)
	{
		this.attributes = attributes;
	}

	/**
	 * @return the modelNumbers
	 */
	public List<String> getModelNumbers()
	{
		return modelNumbers;
	}

	/**
	 * @param modelNumbers
	 *           the modelNumbers to set
	 */
	@XmlElementWrapper(name = "ModelNumbers")
	@XmlElement(name = "ModelNumber")
	public void setModelNumbers(final List<String> modelNumbers)
	{
		this.modelNumbers = modelNumbers;
	}

	/**
	 * @return the brandExternalId
	 */
	public String getBrandExternalId()
	{
		return brandExternalId;
	}

	/**
	 * @param brandExternalId
	 *           the brandExternalId to set
	 */
	@XmlElement(name = "BrandExternalId")
	public void setBrandExternalId(final String brandExternalId)
	{
		this.brandExternalId = brandExternalId;
	}

}

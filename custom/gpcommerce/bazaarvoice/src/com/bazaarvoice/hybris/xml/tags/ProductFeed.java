/**
 * 
 */
package com.bazaarvoice.hybris.xml.tags;

import java.util.Collection;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author christina romashchenko
 * 
 */
@XmlRootElement(name = "Feed")
@XmlType(propOrder =
{ "xmlns", "generator", "name", "incremental", "extractDate", "brands", "categories", "products" })
//@XmlAccessorType(XmlAccessType.FIELD)
public class ProductFeed
{

	String xmlns;
	String name;
	String generator;
	Boolean incremental;
	Date extractDate;
	Collection<CategoryTag> categories;
	Collection<ProductTag> products;
	Collection<BrandTag> brands;


	public ProductFeed()
	{
		super();
	}

	public String getXmlns()
	{
		return xmlns;
	}

	/**
	 * @param xmlns
	 *           the xmlns to set
	 */
	@XmlAttribute(name = "xmlns", required = true)
	public void setXmlns(final String xmlns)
	{
		this.xmlns = xmlns;
	}

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
	@XmlAttribute
	public void setName(final String name)
	{
		this.name = name;
	}


	public String getGenerator()
	{
		return generator;
	}

	@XmlAttribute
	public void setGenerator(final String generator)
	{
		this.generator = generator;
	}

	/**
	 * @return the incremental
	 */
	public Boolean getIncremental()
	{
		return incremental;
	}

	/**
	 * @param incremental
	 *           the incremental to set
	 */
	@XmlAttribute
	public void setIncremental(final Boolean incremental)
	{
		this.incremental = incremental;
	}

	/**
	 * @return the extractDate
	 */
	public Date getExtractDate()
	{
		return extractDate;
	}

	/**
	 * @param extractDate
	 *           the extractDate to set
	 */
	@XmlAttribute
	public void setExtractDate(final Date extractDate)
	{
		this.extractDate = extractDate;
	}

	/**
	 * @return the categories
	 */
	public Collection<CategoryTag> getCategories()
	{
		return categories;
	}

	/**
	 * @param categories
	 *           the categories to set
	 */
	@XmlElementWrapper(name = "Categories", required = true)
	@XmlElement(name = "Category")
	public void setCategories(final Collection<CategoryTag> categories)
	{
		this.categories = categories;
	}

	/**
	 * @return the products
	 */
	public Collection<ProductTag> getProducts()
	{
		return products;
	}

	/**
	 * @param products
	 *           the products to set
	 */
	@XmlElementWrapper(name = "Products", required = true)
	@XmlElement(name = "Product")
	public void setProducts(final Collection<ProductTag> products)
	{
		this.products = products;
	}

	/**
	 * @return the brands
	 */
	public Collection<BrandTag> getBrands()
	{
		return brands;
	}

	/**
	 * @param brands
	 *           the brands to set
	 */
	@XmlElementWrapper(name = "Brands", required = true)
	@XmlElement(name = "Brand")
	public void setBrands(final Collection<BrandTag> brands)
	{
		this.brands = brands;
	}








}

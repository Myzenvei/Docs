/**
 * 
 */
package com.bazaarvoice.hybris.xml;

import static org.junit.Assert.assertTrue;

import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bazaarvoice.hybris.xml.tags.Attribute;
import com.bazaarvoice.hybris.xml.tags.BrandTag;
import com.bazaarvoice.hybris.xml.tags.CategoryPageUrl;
import com.bazaarvoice.hybris.xml.tags.CategoryTag;
import com.bazaarvoice.hybris.xml.tags.Description;
import com.bazaarvoice.hybris.xml.tags.LocalizedImageUrl;
import com.bazaarvoice.hybris.xml.tags.LocalizedName;
import com.bazaarvoice.hybris.xml.tags.ProductFeed;
import com.bazaarvoice.hybris.xml.tags.ProductTag;


/**
 * @author christina romashchenko
 * 
 */
public class ProductFeedXmlCreatorTest extends ServicelayerTransactionalTest
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ProductFeedXmlCreatorTest.class.getName());

	@Resource
	private ProductFeedXmlCreator productFeedXmlCreator;


	private Collection<CategoryTag> categories;
	private Collection<ProductTag> products;
	private Collection<BrandTag> brands;
	private final Collection<Locale> locales = Arrays.asList(Locale.ENGLISH, Locale.FRANCE, Locale.GERMAN);
	private ProductFeed productFeed;
	private final int itemNumber = 3;
	private final File file = new File("/test.xml");

	@Before
	public void setUp()
	{
		categories = createCategories();
		brands = createBrands();
		products = createProducts();
		productFeed = createTestProductFeed();

	}

	@After
	public void tearDown()
	{
		// implement here code executed after each test
	}


	@Test
	public void testProductFeedXmlCreator()
	{
		productFeedXmlCreator.writeXmlToFile(file, productFeed);
		assertTrue("Test gone succesfully", true);
	}


	public Collection<CategoryTag> createCategories()
	{
		final List<CategoryTag> result = new ArrayList<CategoryTag>();

		for (int i = 0; i < itemNumber; i++)
		{
			final CategoryTag tag = new CategoryTag();
			final String name = "CategoryName" + i;
			final String parentExternalId = "peid" + i;
			final String externalId = "eid" + i;
			final String imageUrl = "imageUrl" + i;
			final String categoryPageUrl = "categoryPageUrl" + i;
			final List<LocalizedImageUrl> imageUrls = new ArrayList<LocalizedImageUrl>();
			final List<CategoryPageUrl> categoryPageUrls = new ArrayList<CategoryPageUrl>();
			final List<LocalizedName> names = new ArrayList<LocalizedName>();
			for (final Locale locale : locales)
			{
				final LocalizedImageUrl localizedImageUrl = new LocalizedImageUrl(locale.getLanguage(), imageUrl);
				final LocalizedName localizedName = new LocalizedName(locale.getLanguage(), name);
				final CategoryPageUrl cpurl = new CategoryPageUrl(locale.getLanguage(), categoryPageUrl);
				imageUrls.add(localizedImageUrl);
				categoryPageUrls.add(cpurl);
				names.add(localizedName);
			}
			tag.setName(name);
			tag.setParentExternalId(parentExternalId);
			tag.setExternalId(externalId);
			tag.setImageUrl(imageUrl);
			tag.setImageUrls(imageUrls);
			tag.setCategoryPageUrl(categoryPageUrl);
			tag.setCategoryPageUrls(categoryPageUrls);
			tag.setNames(names);
			result.add(tag);
		}

		return result;
	}

	public Collection<ProductTag> createProducts()
	{
		final List<ProductTag> result = new ArrayList<ProductTag>();

		for (int i = 0; i < itemNumber; i++)
		{
			final ProductTag tag = new ProductTag();

			final String name = "ProductName" + i;
			tag.setName(name);
			final List<Attribute> attributes = new ArrayList<>();
			attributes.addAll(Arrays.asList(new Attribute("attrId1", "attrValue1"), new Attribute("attrId2", "attrValue2")));
			tag.setAttributes(attributes);
			tag.setCategoryExternalId("categoryExternalId" + i);
			tag.setDescription("description" + i);
			final List<Description> descriptions = new ArrayList<>();
			for (final Locale locale : locales)
			{
				final Description desc = new Description(locale.getLanguage(), "description" + i);
				descriptions.add(desc);
			}
			tag.setDescriptions(descriptions);
			final String ean = "ean" + i;
			tag.setEan(ean);
			final List<String> eans = new ArrayList<>();
			eans.addAll(Arrays.asList(ean + 1, ean + 2));
			tag.setEans(eans);
			tag.setExternalId("externalId" + i);
			tag.setImageUrl("imageUrl" + i);
			final String isbn = "isbn" + i;
			tag.setIsbn(isbn);
			final List<String> isbns = new ArrayList<>();
			isbns.addAll(Arrays.asList(isbn + 1, isbn + 2));
			tag.setIsbns(isbns);
			tag.setManufacturerPartNumber("manufacturerPartNumber" + i);
			final List<LocalizedName> names1 = new ArrayList<>();
			for (final Locale locale : locales)
			{
				final LocalizedName localizedName = new LocalizedName(locale.getLanguage(), name);
				names1.add(localizedName);
			}
			tag.setNames(names1);
			tag.setProductPageUrl("productPageUrl" + i);
			final List<LocalizedName> productPageUrls = new ArrayList<>();
			for (final Locale locale : locales)
			{
				final LocalizedName localizedName = new LocalizedName(locale.getLanguage(), "productPageUrl");
				productPageUrls.add(localizedName);
			}
			tag.setProductPageUrls(productPageUrls);
			final String upc = "upc" + i;
			tag.setUpc(upc);
			final List<String> upcs = new ArrayList<>();
			upcs.addAll(Arrays.asList(upc + 1, upc + 2));
			tag.setUpcs(upcs);

			result.add(tag);
		}

		return result;
	}

	public Collection<BrandTag> createBrands()
	{
		final List<BrandTag> result = new ArrayList<BrandTag>();

		for (int i = 0; i < itemNumber; i++)
		{
			final BrandTag tag = new BrandTag();
			final String name = "BrandName" + i;
			final String externalId = "externalId" + i;
			//on future
			final List<LocalizedName> names = new ArrayList<LocalizedName>();
			for (final Locale locale : locales)
			{
				final LocalizedName localizedName = new LocalizedName(locale.getLanguage(), name);
				names.add(localizedName);
			}
			tag.setName(name);
			tag.setExternalId(externalId);
			result.add(tag);
			//tag.setNames(names);
		}

		return result;
	}

	public ProductFeed createTestProductFeed()
	{
		final ProductFeed result = new ProductFeed();
		final Date extractDate = new Date();
		final String name = "ProductFeedName";
		final String xmlns = "xmlns";
		final Boolean incremental = Boolean.FALSE;

		result.setBrands(brands);
		result.setCategories(categories);
		result.setExtractDate(extractDate);
		result.setIncremental(incremental);
		result.setName(name);
		result.setProducts(products);
		result.setXmlns(xmlns);

		return result;
	}
}

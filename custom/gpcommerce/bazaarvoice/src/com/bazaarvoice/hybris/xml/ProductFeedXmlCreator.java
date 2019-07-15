/**
 *
 */
package com.bazaarvoice.hybris.xml;

import de.hybris.platform.acceleratorservices.urlencoder.attributes.impl.DefaultLanguageAttributeManager;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2lib.model.components.ProductDetailComponentModel;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.GenericVariantProductModel;
import de.hybris.platform.variants.model.VariantCategoryModel;
import de.hybris.platform.variants.model.VariantProductModel;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bazaarvoice.hybris.model.BazaarvoiceProductFeedExportCronJobModel;
import com.bazaarvoice.hybris.xml.tags.Attribute;
import com.bazaarvoice.hybris.xml.tags.BrandTag;
import com.bazaarvoice.hybris.xml.tags.CategoryTag;
import com.bazaarvoice.hybris.xml.tags.Description;
import com.bazaarvoice.hybris.xml.tags.LocalizedName;
import com.bazaarvoice.hybris.xml.tags.ProductFeed;
import com.bazaarvoice.hybris.xml.tags.ProductTag;
import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import static com.bazaarvoice.hybris.utils.BazaarVoiceUtils.ReplaceUnsupportedCharacters;


/**
 * @author christina romashchenko
 */
@Scope("prototype")
@Component
public class ProductFeedXmlCreator
{
	// constants used to declare families for products (used with hybris product variants)
	private static final String BV_FAMILY_NAME_ATTRIBUTE_ID = "BV_FE_FAMILY";
	private static final String BV_FAMILY_EXPAND = "BV_FE_EXPAND";
	

	@Resource(name = "categoryModelUrlResolver")
	private UrlResolver<CategoryModel> categoryModelUrlResolver;

	@Resource(name = "productModelUrlResolver")
	private UrlResolver<ProductModel> productModelUrlResolver;

	@Resource(name = "siteBaseUrlResolutionService")
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;

	@Resource(name = "languageAttributeManager")
	private DefaultLanguageAttributeManager languageAttributeManager;

	private static final Logger LOG = Logger.getLogger(ProductFeedXmlCreator.class.getName());
	//private String dateCounter;
	private int fileCounter = 1;
	private List<CategoryModel> brands;

	//	private String host_url;
	private String store_webroot;
	private CMSSiteModel cmsSiteModel;

	private String upcMethodName;

	public void setUpcMethodName(final String upcMethodName)
	{
		this.upcMethodName = upcMethodName;
	}

	/**
	 * @return the brands
	 */
	public List<CategoryModel> getBrands()
	{
		return brands;
	}

	/**
	 * @param brands
	 *           the brands to set
	 */
	public void setBrands(final List<CategoryModel> brands)
	{
		this.brands = brands;
	}

	public void setCMSSiteModel(final CMSSiteModel cmsSiteModel)
	{
		this.cmsSiteModel = cmsSiteModel;
	}

	public void writeXmlToFile(final File file, final ProductFeed productFeed)
	{

		LOG.debug("===========Marshalling Product Fedd to file " + file.getAbsolutePath() + "===================");

		try
		{
			final JAXBContext jaxbContext = JAXBContext.newInstance(ProductFeed.class);
			final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.setProperty(CharacterEscapeHandler.class.getName(), new CharacterEscapeHandler()
			{
				@Override
				public void escape(final char[] ac, final int i, final int j, final boolean flag, final Writer writer)
						throws IOException
				{
					writer.write(ac, i, j);
				}
			});
			jaxbMarshaller.marshal(productFeed, file);


		}
		catch (final JAXBException e)
		{
			LOG.error(e.getStackTrace());
		}

	}


	private CategoryTag getCategoryTag(final CategoryModel model, final Map<Locale, String> locales)
	{
		final CategoryTag resultTag = new CategoryTag();

		/* Required */

		//ExternalId
		resultTag.setExternalId((model.getCode() == null || StringEscapeUtils.escapeXml(model.getCode()).isEmpty()) ? null
				: StringEscapeUtils.escapeXml(ReplaceUnsupportedCharacters(model.getCode())));
		//Name
		resultTag.setName((model.getName() == null || model.getName().isEmpty()) ? null : model.getName());
		//Localized category names
		final List<LocalizedName> names = new ArrayList<LocalizedName>();
		for (final Locale locale : locales.keySet())
		{
			try
			{
				final String localName = model.getName(locale);
				final String normalizedLocale = locales.get(locale);
				if (localName != null && normalizedLocale != null)
				{
					final LocalizedName lname = new LocalizedName(normalizedLocale, localName);
					names.add(lname);
				}
			}
			catch (final Exception ex)
			{
				//LOG.debug(ex.getMessage());
				continue;
			}

		}
		resultTag.setNames(names.isEmpty() ? null : names);

		//CategoryPageUrl
		//		resultTag.setCategoryPageUrl(normalizeText(host_url + categoryModelUrlResolver.resolve(model)));

		final String categoryPageUrl = siteBaseUrlResolutionService.getWebsiteUrlForSite(cmsSiteModel, false,
				categoryModelUrlResolver.resolve(model));
		resultTag.setCategoryPageUrl((categoryPageUrl == null || categoryPageUrl.isEmpty()) ? null : categoryPageUrl);

		/*
		 * Not required parameters
		 */

		/* CategoryPageUrls */
		//		final List<CMSLinkComponentModel> pageUrls = model.getLinkComponents();
		//		if (pageUrls != null)
		//		{
		//
		//		}

		/* ParentExternalId */
		String parentExternalId = null;
		final List<CategoryModel> supercategories = model.getSupercategories();

		/*
		 * supercategories contain several types of elements: 1. a single CategoryModel type element that is our
		 * supercategory 2. ClassificationClassModel that is for classification system usage. So at the moment we use the
		 * only instance of CategoryModel as a parent Category
		 */
		if (supercategories.isEmpty())
		{
			parentExternalId = (model.getCode() == null || StringEscapeUtils.escapeXml(model.getCode()).isEmpty()) ? null
					: StringEscapeUtils.escapeXml(ReplaceUnsupportedCharacters(model.getCode().replace("/", "_")));
		}
		else
		{
			for (final CategoryModel supercategory : supercategories)
			{
				if (supercategory.getClass().equals(CategoryModel.class))
				{
					parentExternalId = StringEscapeUtils.escapeXml(ReplaceUnsupportedCharacters(supercategory.getCode()));
				}

				// TODO: think of using classification categories
			}
		}
		if (parentExternalId != null && !(StringEscapeUtils.escapeXml(parentExternalId).isEmpty()))
		{
			if (!(parentExternalId.equals("1") || parentExternalId.contains("/") || parentExternalId.equals("collections")))
			{
				resultTag.setParentExternalId(StringEscapeUtils.escapeXml(parentExternalId));
			}
		}

		//ImageUrl
		final MediaModel media = model.getPicture();
		final String imageUrl = (media != null)
				? siteBaseUrlResolutionService.getMediaUrlForSite(cmsSiteModel, false, media.getDownloadURL()) : null;
		resultTag.setImageUrl((imageUrl == null || imageUrl.isEmpty()) ? null : imageUrl);

		//ImageUrls
		//
		//		List<LocalizedImageUrl> imageUrls = new ArrayList<>();
		//		 resultTag.setImageUrls(imageUrls);

		return resultTag;
	}

	/**
	 * @param categories
	 * @return
	 */
	public List<CategoryTag> getCategoryTagsFromCollection(final Collection<CategoryModel> categories)
	{
		final List<CategoryTag> result = new ArrayList<CategoryTag>();
		for (final CategoryModel model : categories)
		{
			final CategoryTag resultTag = new CategoryTag();

			//ExternalId
			resultTag.setExternalId((model.getCode() == null || StringEscapeUtils.escapeXml(model.getCode()).isEmpty()) ? null
					: StringEscapeUtils.escapeXml(ReplaceUnsupportedCharacters(model.getCode())));
			//Name
			resultTag.setName((model.getName() == null || model.getName().isEmpty()) ? null : model.getName());

			//CategoryPageUrl

			final String categoryPageUrl = siteBaseUrlResolutionService.getWebsiteUrlForSite(cmsSiteModel, false,
					categoryModelUrlResolver.resolve(model));
			resultTag.setCategoryPageUrl((categoryPageUrl == null || categoryPageUrl.isEmpty()) ? null : categoryPageUrl);

			result.add(resultTag);
		}
		return result;
	}

	/**
	 * @param cronJob
	 * @return
	 */
	public List<BrandTag> getBrandTagsFromCollection(final BazaarvoiceProductFeedExportCronJobModel cronJob)
	{
		final List<BrandTag> result = new ArrayList<BrandTag>();

		final BrandTag tag = new BrandTag();
		tag.setName((cronJob.getBrandName() == null || cronJob.getBrandName().isEmpty()) ? null : cronJob.getBrandName());
		tag.setExternalId((cronJob.getBrandID() == null || StringEscapeUtils.escapeXml(cronJob.getBrandID()).isEmpty()) ? null
				: StringEscapeUtils.escapeXml(ReplaceUnsupportedCharacters(cronJob.getBrandID())));

		result.add(tag);
		return result;
	}

	public List<ProductTag> getProductTagsFromCollection(final Collection<ProductModel> productModels,
			final Map<Locale, String> locales, final BazaarvoiceProductFeedExportCronJobModel cronJob)
	{
		return getProductTagsFromCollection(productModels, locales, false, cronJob);
	}

	public List<ProductTag> getProductTagsFromCollection(final Collection<ProductModel> productModels,
			final Map<Locale, String> locales, final boolean isFamilyEnabled, final BazaarvoiceProductFeedExportCronJobModel cronJob)
	{
		final List<ProductTag> result = new ArrayList<ProductTag>();
		final Field[] fields = ProductModel.class.getDeclaredFields();
		boolean flag = false;
		if (upcMethodName != null && !upcMethodName.isEmpty())
		{
			for (final Field f : fields)
			{
				f.setAccessible(true);
				if (f.getName().equalsIgnoreCase(upcMethodName))
				{
					try
					{
						flag = true;
						upcMethodName = ((String) ProductModel.class.getField(f.getName()).get(f.getName())).substring(0, 1)
								.toUpperCase() + ((String) ProductModel.class.getField(f.getName()).get(f.getName())).substring(1);
					}
					catch (final NoSuchFieldException e)
					{
						LOG.debug(
								"UPC field not configured correctly. No such field exists. Please check the field. UPC will not print.");
						e.printStackTrace();
						upcMethodName = null;
					}
					catch (final IllegalAccessException e)
					{
						LOG.debug("Unable to access UPC attribute. Please check UPC field in config.");
						e.printStackTrace();
						upcMethodName = null;
					}
					break;
				}
			}
		}
		if (!flag)
		{
			upcMethodName = null;
		}
		for (final ProductModel model : productModels)
		{
			final ProductTag tag = getProductTag(model, locales, isFamilyEnabled, cronJob);
			result.add(tag);
		}
		return result;
	}

	private BrandTag getBrandTag(final CategoryModel model, final Map<Locale, String> locales)
	{

		final BrandTag tag = new BrandTag();
		tag.setName((model.getName() == null || model.getName().isEmpty()) ? null : model.getName());
		tag.setExternalId((model.getCode() == null || StringEscapeUtils.escapeXml(model.getCode()).isEmpty()) ? null
				: StringEscapeUtils.escapeXml(ReplaceUnsupportedCharacters(model.getCode())));

		//Localized brand names
		final List<LocalizedName> names = new ArrayList<LocalizedName>();
		for (final Locale locale : locales.keySet())
		{
			try
			{
				final String localName = model.getName(locale);
				final String normalizedLocale = locales.get(locale);
				if (localName != null && normalizedLocale != null)
				{
					final LocalizedName lname = new LocalizedName(normalizedLocale, localName);
					names.add(lname);
				}
			}
			catch (final Exception ex)
			{
				//LOG.debug(ex.getMessage());
				continue;
			}

		}
		tag.setNames(names.isEmpty() ? null : names);
		return tag;
	}

	private ProductTag getProductTag(final ProductModel model, final Map<Locale, String> locales,
			final BazaarvoiceProductFeedExportCronJobModel cronJob)
	{
		return getProductTag(model, locales, false, cronJob);
	}

	private ProductTag getProductTag(final ProductModel model, final Map<Locale, String> locales, final boolean isFamilyEnabled,
			final BazaarvoiceProductFeedExportCronJobModel cronJob)
	{
		final ProductTag tag = new ProductTag();
		final String name = model.getName();
		tag.setName(name == null || name.isEmpty() ? model.getCode() : name);
		tag.setExternalId((model.getCode() == null || model.getCode().isEmpty()) ? null
				: StringEscapeUtils.escapeXml(ReplaceUnsupportedCharacters(model.getCode().replace("/", "_"))));

		final String brandExternalId = cronJob.getBrandID();
		String categoryExternalId = null;
		final Collection<CategoryModel> superCategories = model.getSupercategories();
		final Collection<CategoryModel> siteSpecificCategories = cronJob.getCategories();
		for (final CategoryModel category : siteSpecificCategories)
		{
			for (final CategoryModel productCategory : superCategories)
			{
				if (productCategory.getCode().equalsIgnoreCase(category.getCode()) && categoryExternalId == null)
				{
					categoryExternalId = StringEscapeUtils.escapeXml(ReplaceUnsupportedCharacters(category.getCode()));
					break;
				}
				else
				{
					final Collection<CategoryModel> categories = getSuperCategories(productCategory);
					if (!CollectionUtils.isEmpty(categories) && categoryExternalId == null)
					{
						for (final CategoryModel superCategory : categories)
						{
							if (superCategory.getCode().equalsIgnoreCase(category.getCode()) && categoryExternalId == null)
							{
								categoryExternalId = StringEscapeUtils.escapeXml(ReplaceUnsupportedCharacters(category.getCode()));
								break;
							}
							else
							{
								final Collection<CategoryModel> supercategories = getSuperCategories(superCategory);
								if (!CollectionUtils.isEmpty(supercategories) && categoryExternalId == null)
								{
									for (final CategoryModel superCat : supercategories)
									{
										if (superCat.getCode().equalsIgnoreCase(category.getCode()) && categoryExternalId == null)
										{
											categoryExternalId = StringEscapeUtils
													.escapeXml(ReplaceUnsupportedCharacters(category.getCode()));
											break;
										}

									}
								}

							}
						}

					}
				}
			}

		}
		tag.setBrandExternalId((brandExternalId == null || StringEscapeUtils.escapeXml(brandExternalId).isEmpty()) ? null
				: StringEscapeUtils.escapeXml(brandExternalId));
		tag.setCategoryExternalId((categoryExternalId == null || StringEscapeUtils.escapeXml(categoryExternalId).isEmpty()) ? null
				: StringEscapeUtils.escapeXml(categoryExternalId));

		final String productPageUrl = siteBaseUrlResolutionService.getWebsiteUrlForSite(cmsSiteModel, false,
				productModelUrlResolver.resolve(model));
		tag.setProductPageUrl((productPageUrl == null || productPageUrl.isEmpty()) ? null : productPageUrl);

		//CatalogVersionModel catalogVersionModel = model.getCatalogVersion();

		String imageUrlString = null;
		MediaModel media;
		media = model.getPicture();
		imageUrlString = (media != null) ? media.getURL() : null;

		/*
		 * if a variant product does not have an imageUrl - set it's parent's image url
		 */
		if ((imageUrlString == null || imageUrlString.isEmpty()) && model instanceof VariantProductModel)
		{
			ProductModel rootProduct = model;
			while (rootProduct instanceof VariantProductModel)
			{
				if (rootProduct.getPicture() != null)
				{
					media = rootProduct.getPicture();
					imageUrlString = (media != null) ? media.getURL() : null;
					break;
				}
				rootProduct = ((VariantProductModel) rootProduct).getBaseProduct();
			}
		}
		tag.setImageUrl((imageUrlString == null || imageUrlString.isEmpty()) ? null : imageUrlString);

		if (cronJob.getIsUPCEANRequired().booleanValue())
		{
		final List<String> eans = new ArrayList<String>();
		if (model.getEan() != null)
		{
			eans.add(StringEscapeUtils.escapeXml(model.getEan()));
		}
		tag.setEans(eans.isEmpty() ? null : eans);


		final List<String> upcs = new ArrayList<String>();
		if (upcMethodName != null && !upcMethodName.isEmpty())
		{
			String upc = null;
			Method method = null;
			try
			{

				if (model.getClass().equals(ProductModel.class))
				{
					method = model.getClass().getDeclaredMethod("get" + upcMethodName, null);
					upc = (String) method.invoke(model, null);
				}
				else
				{
					method = model.getClass().forName("de.hybris.platform.core.model.product.ProductModel")
							.getDeclaredMethod("get" + upcMethodName, null);
					upc = (String) method.invoke(model, null);
				}

			}
			catch (final NoSuchMethodException e)
			{
				LOG.debug("No such method exists. Please ensure that attribute name is spelled correctly. Could not find: get"
						+ upcMethodName);
				e.printStackTrace();
			}
			catch (final InvocationTargetException e)
			{
				LOG.debug("Invocation Error on: get" + upcMethodName);
				e.printStackTrace();
			}
			catch (final IllegalAccessException e)
			{
				LOG.debug("Unable to access: get" + upcMethodName);
				e.printStackTrace();
			}
			catch (final ClassNotFoundException e)
			{
				LOG.debug("Please ensure that the model is part of de.hybris.platform.core.model.product.ProductModel hierarchy.");
				e.printStackTrace();
			}
			catch (final Exception ex)
			{
				ex.printStackTrace();
			}
			if (upc != null)
			{
				upcs.add(StringEscapeUtils.escapeXml(upc));
			}
		}

		tag.setUpcs(upcs.isEmpty() ? null : upcs);
		}

		String description = model.getDescription();
		if (description == null || description.isEmpty())
		{
			description = brandExternalId != null ? brandExternalId + " brand" : "empty description";
		}
		tag.setDescription((description == null || description.isEmpty()) ? null : description);

		final List<Attribute> attributes = new ArrayList<>();
		// product families
		if (isFamilyEnabled && model instanceof GenericVariantProductModel)
		{
			String familyName ="";
	
			ProductModel productModel = ((GenericVariantProductModel) model).getBaseProduct();
		
		
					familyName = productModel.getCode();
			

			// Old implementation
			/*
			 * for (int rate = 0; rate < list.size(); rate++) { final StringBuilder value = new StringBuilder(); final int
			 * controlValue = list.size() - 1 - rate; for (int i = controlValue; i >= 0; i--) { if (list.size() > 1 && i !=
			 * controlValue) { value.append("_"); } value.append(list.get(i)); } attributes.add(new
			 * Attribute(BV_FAMILY_NAME_ATTRIBUTE_ID, value.toString())); }
			 */
			attributes.add(new Attribute(BV_FAMILY_NAME_ATTRIBUTE_ID, familyName));
			attributes.add(new Attribute(BV_FAMILY_EXPAND, BV_FAMILY_NAME_ATTRIBUTE_ID + ":" + familyName));
		}

		tag.setAttributes(attributes.isEmpty() ? null : attributes);
		return tag;
	}

	public String generateTodayFilename(final String filePrefix, final String dateNumber, final String siteID)
	{

		final String name = siteID + "_" + (filePrefix == null ? "product_feed" : filePrefix) + fileCounter + "_" + dateNumber;
		fileCounter++;
		return name;
	}

	public String gerenateDate()
	{
		final Date date = new Date(); // your date
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		final NumberFormat formatter = NumberFormat.getNumberInstance();
		formatter.setMinimumIntegerDigits(2);
		final String year = "" + cal.get(Calendar.YEAR);
		final String month = formatter.format(cal.get(Calendar.MONTH) + 1);
		final String day = formatter.format(cal.get(Calendar.DAY_OF_MONTH));
		final String hour = formatter.format(cal.get(Calendar.HOUR_OF_DAY));
		final String minute = formatter.format(cal.get(Calendar.MINUTE));
		final String secund = formatter.format(cal.get(Calendar.SECOND));
		final String dateNumber = year + month + day + hour + minute + secund;
		return dateNumber;
	}

	private Collection<CategoryModel> getSuperCategories(final CategoryModel category)
	{

		final Collection<CategoryModel> categories = new ArrayList<>();
		categories.addAll(category.getSupercategories());
		return categories;
	}

}

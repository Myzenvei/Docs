/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.storefront.controllers.pages;

import de.hybris.platform.acceleratorfacades.futurestock.FutureStockFacade;
import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.ProductBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.forms.ReviewForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.ReviewValidator;
import de.hybris.platform.acceleratorstorefrontcommons.util.MetaSanitizerUtil;
import de.hybris.platform.acceleratorstorefrontcommons.variants.VariantSortStrategy;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.commercefacades.order.data.ConfigurationInfoData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.BaseOptionData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.util.Config;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.site.BaseSiteService;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.gp.gpassistedservicesstorefront.constants.GpassistedservicesstorefrontConstants;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gp.commerce.facades.user.impl.GPDefaultUserFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.facades.product.GPProductFacade;

/**
 * Controller for product details page
 */
@Controller
@RequestMapping(value = "/**/p")
public class ProductPageController extends AbstractPageController {

	private static final String CANONICAL_URL = "canonicalUrl";

	private static final String PRODUCT_NAME = "productName";

	private static final String PRODUCT_URL = "productUrl";

	private static final String PAGE_TYPE = "pageType";

	private static final String FUTURE_STOCK_ENABLED_ATTR = "futureStockEnabled";

	private static final String PRODUCT_CODE = "productCode";

	private static final String GP_BV_ID = "gpbvId";

	private static final String BV_CLIENT = "bvClient";

	private static final String BV_LOCALE = "bvLocale";

	private static final String BV_ENV = "bvEnv";

	private static final String GALLERY_IMAGES = "galleryImages";

	private static final String PRODUCT_OBJECT = "product";

	private static final String CONFIGURATOR_TYPE = "configuratorType";

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ProductPageController.class);

	/**
	 * We use this suffix pattern because of an issue with Spring 3.1 where a Uri
	 * value is incorrectly extracted if it contains one or more '.' characters.
	 * Please see https://jira.springsource.org/browse/SPR-6164 for a discussion on
	 * the issue and future resolution.
	 */
	private static final String PRODUCT_CODE_PATH_VARIABLE_PATTERN = "/{productCode:.*}";
	private static final String PRODUCT_CODE_VARIANT_PATH_VARIABLE_PATTERN = "/{productCode:.*}/{variantCode:.*}";

	private static final String FUTURE_STOCK_ENABLED = "storefront.products.futurestock.enabled";
	private static final String DEFAULT_BV_SITE_ID = "gp.bv.default.siteId";
	private static final String GP_BV_SITE_ID = "gp_bvId";
	private static final String GP_BV_CLIENT_NAME = "cmsSite.bvConfig.clientName";
	private static final String GP_BV_LOCALE = "cmsSite.bvLocale";
	private static final String GP_BV_ENV = "cmsSite.bvEnv";
	public static final String REDIRECT_PREFIX = "redirect:"; 
	private static final String LOGIN_EMPLOYEESTORE_DEFAULTPAGE_REDIRECT = "gpcommercestorefront.employeestore.basesiteID";
	private static final String PRICE = "price";
	private static final String AVAILABILITY = "availability";
	private static final String PRICE_CURRENCY = "priceCurrency";
	private static final String PRODUCT_DESCRIPTION = "description";
	private static final String IMAGE_URL = "image";
	private static final String IS_SUBSCRIBLE_CART = "true";
	private static final String SUBSCRIBLE_CART_ID = "subscrCartId";
	private static final String SUBSCRIBLE_CART_SESSION_VAR = "subscrCart";

	@Resource(name = "productDataUrlResolver")
	private UrlResolver<ProductData> productDataUrlResolver;

	@Resource(name = "productVariantFacade")
	private ProductFacade productFacade;

	@Resource(name = "productFacade")
	private GPProductFacade gpProductFacade;

	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "productBreadcrumbBuilder")
	private ProductBreadcrumbBuilder productBreadcrumbBuilder;

	@Resource(name = "cmsPageService")
	private CMSPageService cmsPageService;

	@Resource(name = "variantSortStrategy")
	private VariantSortStrategy variantSortStrategy;

	@Resource(name = "reviewValidator")
	private ReviewValidator reviewValidator;

	@Resource(name = "futureStockFacade")
	private FutureStockFacade futureStockFacade;

	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	
	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Resource(name = "userFacade")
	private UserFacade userFacade;
	
	@Resource(name = "gpDefaultUserFacade")
	private GPDefaultUserFacade gpDefaultUserFacade;


	@RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String productDetail(@PathVariable(PRODUCT_CODE) final String encodedProductCode, final Model model,
			final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException, UnsupportedEncodingException {
		final String isSubCart = GPFunctions.getSessionAttributeValue(request,SUBSCRIBLE_CART_SESSION_VAR);
		if (StringUtils.isNotBlank(isSubCart) && StringUtils.equalsIgnoreCase(IS_SUBSCRIBLE_CART, isSubCart))
		{
			gpDefaultUserFacade.changeSubscripCartUser(request,SUBSCRIBLE_CART_ID);
			return REDIRECT_PREFIX + "/checkout";
		}
		final String productCode = decodeWithScheme(encodedProductCode, UTF_8);
		return populateModelAttributes(model,productCode,request);
	}

	@RequestMapping(value = PRODUCT_CODE_VARIANT_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String productDetailVariant(@PathVariable(PRODUCT_CODE) final String encodedProductCode,
			@PathVariable("variantCode") final String encodedVariantCode, final Model model,
			final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException, UnsupportedEncodingException {
		final String isSubCart = GPFunctions.getSessionAttributeValue(request,SUBSCRIBLE_CART_SESSION_VAR);
		if (StringUtils.isNotBlank(isSubCart) && StringUtils.equalsIgnoreCase(IS_SUBSCRIBLE_CART, isSubCart))
		{
			gpDefaultUserFacade.changeSubscripCartUser(request,SUBSCRIBLE_CART_ID);
			return REDIRECT_PREFIX + "/checkout";
		}
		final String productCode = decodeWithScheme(encodedProductCode, UTF_8) + "/" + decodeWithScheme(encodedVariantCode, UTF_8);
		return populateModelAttributes(model,productCode,request);
		
	}


	@ExceptionHandler(UnknownIdentifierException.class)
	public String handleUnknownIdentifierException(final UnknownIdentifierException exception,
			final HttpServletRequest request) {
		request.setAttribute("message", exception.getMessage());
		return FORWARD_PREFIX + "/404";
	}

	protected void updatePageTitle(final String productCode, final Model model) {
		storeContentPageTitleInModel(model, getPageTitleResolver().resolveProductPageTitle(productCode));
	}

	protected void populateProductDetailForDisplay(final String productCode, final Model model,
			final HttpServletRequest request, final List<ProductOption> extraOptions) throws CMSItemNotFoundException {
		final ProductModel productModel = productService.getProductForCode(productCode);

		getRequestContextData(request).setProduct(productModel);

		storeCmsPageInModel(model, getPageForProduct(productCode));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, productBreadcrumbBuilder.getBreadcrumbs(productCode));

	}

	protected void populateProductData(final ProductData productData, final Model model) {
		model.addAttribute(GALLERY_IMAGES, getGalleryImages(productData));
		model.addAttribute(PRODUCT_OBJECT, productData);
		if (productData.getConfigurable()) {
			final List<ConfigurationInfoData> configurations = productFacade
					.getConfiguratorSettingsForCode(productData.getCode());
			if (CollectionUtils.isNotEmpty(configurations)) {
				model.addAttribute(CONFIGURATOR_TYPE, configurations.get(0).getConfiguratorType());
			}
		}
	}

	protected void sortVariantOptionData(final ProductData productData) {
		if (CollectionUtils.isNotEmpty(productData.getBaseOptions())) {
			for (final BaseOptionData baseOptionData : productData.getBaseOptions()) {
				if (CollectionUtils.isNotEmpty(baseOptionData.getOptions())) {
					Collections.sort(baseOptionData.getOptions(), variantSortStrategy);
				}
			}
		}

		if (CollectionUtils.isNotEmpty(productData.getVariantOptions())) {
			Collections.sort(productData.getVariantOptions(), variantSortStrategy);
		}
	}

	protected List<Map<String, ImageData>> getGalleryImages(final ProductData productData) {
		final List<Map<String, ImageData>> galleryImages = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(productData.getImages())) {
			final List<ImageData> images = new ArrayList<>();
			for (final ImageData image : productData.getImages()) {
				if (ImageDataType.GALLERY.equals(image.getImageType())) {
					images.add(image);
				}
			}
			Collections.sort(images, new Comparator<ImageData>() {
				@Override
				public int compare(final ImageData image1, final ImageData image2) {
					return image1.getGalleryIndex().compareTo(image2.getGalleryIndex());
				}
			});

			if (CollectionUtils.isNotEmpty(images)) {
				addFormatsToGalleryImages(galleryImages, images);
			}
		}
		return galleryImages;
	}

	protected void addFormatsToGalleryImages(final List<Map<String, ImageData>> galleryImages,
			final List<ImageData> images) {
		int currentIndex = images.get(0).getGalleryIndex().intValue();
		Map<String, ImageData> formats = new HashMap<String, ImageData>();
		for (final ImageData image : images) {
			if (currentIndex != image.getGalleryIndex().intValue()) {
				galleryImages.add(formats);
				formats = new HashMap<>();
				currentIndex = image.getGalleryIndex().intValue();
			}
			formats.put(image.getFormat(), image);
		}
		if (!formats.isEmpty()) {
			galleryImages.add(formats);
		}
	}

	protected ReviewValidator getReviewValidator() {
		return reviewValidator;
	}

	protected AbstractPageModel getPageForProduct(final String productCode) throws CMSItemNotFoundException {
		final ProductModel productModel = productService.getProductForCode(productCode);
		return cmsPageService.getPageForProduct(productModel);
	}
	
	
	private String populateModelAttributes(final Model model ,final String productCode,final HttpServletRequest request) throws CMSItemNotFoundException
	{
		final List<ProductOption> extraOptions = Arrays.asList(ProductOption.DESCRIPTION,ProductOption.KEYWORDS,ProductOption.PRICE,ProductOption.STOCK,ProductOption.IMAGES);

		final ProductData productData = gpProductFacade.getProductForCodeAndOptions(productCode, extraOptions);

		updatePageTitle(productCode, model);

		populateProductDetailForDisplay(productCode, model, request, extraOptions);

		model.addAttribute(new ReviewForm());
		model.addAttribute(PAGE_TYPE, PageType.PRODUCT.name());
		model.addAttribute(FUTURE_STOCK_ENABLED_ATTR, Boolean.valueOf(Config.getBoolean(FUTURE_STOCK_ENABLED, false)));
		model.addAttribute(PRODUCT_CODE, productCode);
		model.addAttribute(PRODUCT_OBJECT, productData);
		if(null!=productData)
		{
		model.addAttribute(PRODUCT_NAME,productData.getName());
		model.addAttribute(PRODUCT_URL,productData.getUrl());
		model.addAttribute(PRODUCT_DESCRIPTION,productData.getDescription());
		if(null!=productData.getImages()) {
		for (final ImageData image : productData.getImages()) {
			model.addAttribute(IMAGE_URL,image.getUrl());
		}
		}
		if(null!=productData.getPrice()) {
			model.addAttribute(PRICE,productData.getPrice().getFormattedValue());
			model.addAttribute(PRICE_CURRENCY,productData.getPrice().getCurrencyIso());
			}
			if(null!=productData.getStock()) {
			model.addAttribute(AVAILABILITY,productData.getStock().getStockLevelStatus());
			}
		}

		model.addAttribute(GP_BV_ID,
				cmsSiteService.getSiteConfig(GP_BV_SITE_ID) != null ? cmsSiteService.getSiteConfig(GP_BV_SITE_ID)
						: configurationService.getConfiguration().getString(DEFAULT_BV_SITE_ID));
		model.addAttribute(BV_LOCALE,
				cmsSiteService.getCurrentSite().getBvLocale() != null ? cmsSiteService.getCurrentSite().getBvLocale()
						: configurationService.getConfiguration().getString(GP_BV_LOCALE));
			model.addAttribute(BV_CLIENT,
					cmsSiteService.getCurrentSite().getBvConfig() != null
							? cmsSiteService.getCurrentSite().getBvConfig().getClientName()
							: configurationService.getConfiguration().getString(GP_BV_CLIENT_NAME));
			model.addAttribute(BV_ENV,
					cmsSiteService.getCurrentSite().getBvConfig() != null
							? cmsSiteService.getCurrentSite().getBvConfig().getEnvironment().getCode().toLowerCase()
							: configurationService.getConfiguration().getString(GP_BV_ENV));

		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				GPFunctions.convertToJSON(productBreadcrumbBuilder.getBreadcrumbs(productCode)));
		if(null!=productData)
		{
			if (StringUtils.isNotBlank(productData.getCanonicalUrl()))
			{
				model.addAttribute(CANONICAL_URL, productData.getCanonicalUrl());
			}
			else
			{
				model.addAttribute(CANONICAL_URL, request.getRequestURL());
			}
			model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, productData.getSeoIndex());
			final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(productData.getKeywords());
			final String metaDescription = MetaSanitizerUtil.sanitizeDescription(productData.getDescription());
			setUpMetaData(model, metaKeywords, metaDescription);
		}

		final String asmRequestParam = request.getParameter(GpassistedservicesstorefrontConstants.ASM_REQUEST_PARAM);
		if(userFacade.isAnonymousUser()) {
			if(baseSiteService.getCurrentBaseSite().getUid().equals(configurationService.getConfiguration().getString(LOGIN_EMPLOYEESTORE_DEFAULTPAGE_REDIRECT)) && asmRequestParam == null) {
				return REDIRECT_PREFIX+"/login";
			}
		}
		return getViewForPage(model);
	}
}
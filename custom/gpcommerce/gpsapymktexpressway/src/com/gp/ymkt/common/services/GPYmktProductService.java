/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.ymkt.common.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.ymkt.common.product.ProductURLService;

import de.hybris.platform.acceleratorservices.model.SiteMapConfigModel;
import de.hybris.platform.acceleratorservices.model.SiteMapLanguageCurrencyModel;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;

/**
 * The Class GPYmktProductService.
 */
public class GPYmktProductService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ProductURLService.class);


	protected ProductService productService;
	

	private static final String CURRENCY="currency";
	private static final String LANGUAGE="language";
	private static final String STORE="storefront";
	private static final String DEFAULT_CURRENCY = "USD";
	private static final String DEFAULT_LANGUAGE = "en";
	private static final String SLASH = "/";

	@Resource(name="siteBaseUrlResolutionService")
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;

	@Resource(name="productModelUrlResolver")
	private UrlResolver<ProductModel> productModelUrlResolver;

	/**
	 * Provide the URL of the product image if available.
	 *
	 * @param product
	 *           {@link ProductModel} to get image URL from.
	 * @return {@link Optional} image URL if the product has one.
	 */
	public Optional<String> getProductImageURL(final ProductModel product)
	{
		return Optional.ofNullable(product.getPicture()) //
				.map(MediaModel::getURL);
	}

	/**
	 * Provide the URL of the product shop detail page.
	 *
	 * @param siteModel the site model
	 * @param productModel the product model
	 * @return URL accessible by web user.
	 */
	public String getProductURL(final CMSSiteModel siteModel, final ProductModel productModel)
	{
		if(siteModel != null) {
			final String navigationUrl = getSiteBaseUrlResolutionService().getWebsiteUrlForSite(siteModel, getSiteUrlAttributes(siteModel, siteModel.getName()), true, StringEscapeUtils.escapeXml(getProductModelUrlResolver().resolve(productModel)));
			LOG.debug(" product url {} ", navigationUrl);
			return navigationUrl;
		} 
		return null;
	}
	
	/**
	 * Method to get the URL parameters for given site id
	 * @param siteModel
	 * @param siteId
	 * @return
	 */
	private String getSiteUrlAttributes(final CMSSiteModel siteModel, final String siteId){
		final Map <String, String> urlAttributesMap = new HashMap<>();
		urlAttributesMap.put(STORE, siteId);
		urlAttributesMap.put(CURRENCY, getCurrencyParam(siteModel));
		urlAttributesMap.put(LANGUAGE, siteModel.getDefaultLanguage() != null ?siteModel.getDefaultLanguage().getIsocode()  :DEFAULT_LANGUAGE);
		final List<String> urlAttributes = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(siteModel.getUrlEncodingAttributes())) {
			siteModel.getUrlEncodingAttributes().stream().forEach(attribute->{
				if(StringUtils.isNotEmpty(urlAttributesMap.get(attribute)))
					urlAttributes.add(urlAttributesMap.get(attribute));
			});
		}
		return CollectionUtils.isNotEmpty(urlAttributes)? SLASH + String.join(SLASH , urlAttributes) : "";

	}

	/**
	 * Method to get currency data for the given site
	 * @param siteModel
	 * @return
	 */
	private String getCurrencyParam(final CMSSiteModel siteModel) {
		final SiteMapConfigModel siteMapConfigModel = siteModel.getSiteMapConfig();
		final SiteMapLanguageCurrencyModel siteMapLanguageCurrencyModel = siteMapConfigModel.getSiteMapLanguageCurrencies().stream().findFirst().orElse(null);
		if(siteMapLanguageCurrencyModel !=  null) {
			return siteMapLanguageCurrencyModel.getCurrency().getIsocode();
		}
		return DEFAULT_CURRENCY;

	}


	public SiteBaseUrlResolutionService getSiteBaseUrlResolutionService() {
		return siteBaseUrlResolutionService;
	}

	public void setSiteBaseUrlResolutionService(SiteBaseUrlResolutionService siteBaseUrlResolutionService) {
		this.siteBaseUrlResolutionService = siteBaseUrlResolutionService;
	}

	public UrlResolver<ProductModel> getProductModelUrlResolver() {
		return productModelUrlResolver;
	}

	public void setProductModelUrlResolver(UrlResolver<ProductModel> productModelUrlResolver) {
		this.productModelUrlResolver = productModelUrlResolver;
	}
	
	
	

}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.process.email.context;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.gp.commerce.core.model.GPSubscriptionCartModel;
import com.gp.commerce.core.model.SubscriptionCartProcessModel;
import com.gp.commerce.core.services.GPCMSSiteService;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;

public class SubscriptionOrderStatusEmailContext extends GPAbstractEmailContext<SubscriptionCartProcessModel>{

	public static final String BASE_SITE = "baseSite";
	
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	
	private MediaService mediaService;
	private CatalogVersionService catalogVersionService;
	private Converter<GPSubscriptionCartModel, CartData> emailCartConverter;
	private CartData cartData;
	
	private BaseSiteModel baseSite;
	
	@Override
	public void init(final SubscriptionCartProcessModel subscriptionCartProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(subscriptionCartProcessModel, emailPageModel);
		cartData = getEmailCartConverter().convert(subscriptionCartProcessModel.getSubscriptionCart());
		baseSite = subscriptionCartProcessModel.getSite();
		put("createdDate", getCreateDate(subscriptionCartProcessModel.getSubscriptionCart().getModifiedtime()));
	}

	/**
	 * @param mediaCode
	 * @return
	 */
	private String getCreateDate(final Date date) {
	    final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
	    final String createdDate= formatter.format(date);
		return createdDate;
	}
	
	protected MediaModel getMediaByCode(final String mediaCode)
	{
		if (StringUtils.isNotEmpty(mediaCode))
		{
			for (final CatalogVersionModel catalogVersionModel : getCatalogVersionService().getSessionCatalogVersions())
			{
				final MediaModel media = getMediaByCodeAndCatalogVersion(mediaCode, catalogVersionModel);
				if (media != null)
				{
					return media;
				}
			}
		}
		return null;
	}
	
	/**
	 * @param mediaCode
	 * @param catalogVersionModel
	 * @return
	 */
	protected MediaModel getMediaByCodeAndCatalogVersion(final String mediaCode, final CatalogVersionModel catalogVersionModel)
	{
		try
		{
			return getMediaService().getMedia(catalogVersionModel, mediaCode);
		}
		catch (final UnknownIdentifierException ignore)
		{
			// Ignore this exception
		}
		return null;
	}
	public String getSecureBaseUrl() {
		return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(baseSite,getUrlEncodingAttributes(), true, "");
	}
	@Override
	protected CustomerModel getCustomer(final SubscriptionCartProcessModel subscriptionCartProcessModel) {

		return (CustomerModel) subscriptionCartProcessModel.getSubscriptionCart().getUser();
	}
	@Override
	protected LanguageModel getEmailLanguage(final SubscriptionCartProcessModel subscriptionCartProcessModel) {

		return null;
	}

	public CartData getCartData() {
		return cartData;
	}

	public void setCartData(final CartData cartData) {
		this.cartData = cartData;
	}
	
	public Converter<GPSubscriptionCartModel, CartData> getEmailCartConverter() {
		return emailCartConverter;
	}

	public void setEmailCartConverter(Converter<GPSubscriptionCartModel, CartData> emailCartConverter) {
		this.emailCartConverter = emailCartConverter;
	}

	public MediaService getMediaService() {
		return mediaService;
	}

	public void setMediaService(MediaService mediaService) {
		this.mediaService = mediaService;
	}

	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}

	public void setCatalogVersionService(CatalogVersionService catalogVersionService) {
		this.catalogVersionService = catalogVersionService;
	}

}

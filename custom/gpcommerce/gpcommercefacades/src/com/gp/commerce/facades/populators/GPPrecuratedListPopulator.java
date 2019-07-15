/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.gp.commerce.core.services.impl.DefaultGPCMSSiteService;
import com.gp.commerce.facades.component.data.GPPreCurateddata;
import com.gp.commerce.facades.data.WishlistData;


public class GPPrecuratedListPopulator implements Populator<WishlistData, GPPreCurateddata>
{

	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	//@Resource(name = "defaultGpCMSSiteService")
	private DefaultGPCMSSiteService defaultGpCMSSiteService;

	@Override
	public void populate(final WishlistData wishlistData, final GPPreCurateddata precuratedData) throws ConversionException
	{
		 precuratedData.setCuratedImageUrl(wishlistData.getBannerImageD());
		 precuratedData.setCuratedText(wishlistData.getDescription());
		 precuratedData.setCuratedLinkText(wishlistData.getCtaText());
		 // Have to change the link,once the wishlist page url is designed
		 precuratedData.setCuratedLinkUrl(getPrecuratedDetailsUrl(wishlistData)+"/?listName="+wishlistData.getWishlistUid());
	}

	public String getPrecuratedDetailsUrl(final WishlistData wishlistData)
	{
		return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(
				defaultGpCMSSiteService.getBaseSiteService().getBaseSiteForUID(wishlistData.getCmssite()), StringUtils.EMPTY, true,
				configurationService.getConfiguration().getString("precurated.prepend.link"));
	}

	public SiteBaseUrlResolutionService getSiteBaseUrlResolutionService()
	{
		return siteBaseUrlResolutionService;
	}

	public void setSiteBaseUrlResolutionService(final SiteBaseUrlResolutionService siteBaseUrlResolutionService)
	{
		this.siteBaseUrlResolutionService = siteBaseUrlResolutionService;
	}

	public DefaultGPCMSSiteService getDefaultGpCMSSiteService()
	{
		return defaultGpCMSSiteService;
	}

	public void setDefaultGpCMSSiteService(final DefaultGPCMSSiteService defaultGpCMSSiteService)
	{
		this.defaultGpCMSSiteService = defaultGpCMSSiteService;
	}

}

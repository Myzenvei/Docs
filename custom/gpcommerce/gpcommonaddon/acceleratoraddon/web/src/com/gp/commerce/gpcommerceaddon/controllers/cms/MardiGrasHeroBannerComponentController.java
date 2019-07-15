/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.controllers.cms;


import de.hybris.platform.acceleratorfacades.device.ResponsiveMediaFacade;
import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.model.GPBannerComponentModel;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.gpcommerceaddon.controllers.GpcommonaddonControllerConstants;
import com.gp.commerce.gpcommerceaddon.facades.GPBannerImageData;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultBannerImageDataFacade;
import com.gp.commerce.gpcommerceaddon.model.MardiGrasHeroBannerComponentModel;
import com.gp.commerce.gpcommerceaddon.populators.GPBannerImageDataPopulator;


/**
 * MardiGras HeroBannerComponent Controller
 */
@Controller("MardiGrasHeroBannerComponentController")
@RequestMapping(value = GpcommonaddonControllerConstants.Actions.Cms.MardiGrasHeroBannerComponent)
public class MardiGrasHeroBannerComponentController
		extends AbstractCMSAddOnComponentController<MardiGrasHeroBannerComponentModel>
{

	/** The responsive media facade. */
	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	/** The commerce common I 18 N service. */
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	@Resource(name = "gpBannerImageDataFacade")
	private GPDefaultBannerImageDataFacade gpBannerImageDataFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final MardiGrasHeroBannerComponentModel component)
	{
		final List<ImageData> mediaList = new ArrayList<>();
		final GPBannerImageData gpImageComponentData = new GPBannerImageData();
		final GPBannerComponentModel firstBanner = component.getFirstBanner();
		final GPBannerComponentModel secondBanner = component.getSecondBanner();
		if (null != firstBanner)
		{
			mediaList.addAll(responsiveMediaFacade
					.getImagesFromMediaContainer(firstBanner.getMedia(commerceCommonI18NService.getCurrentLocale())));
		}
		if (null != secondBanner)
		{
		mediaList.addAll(responsiveMediaFacade
					.getImagesFromMediaContainer(secondBanner.getMedia(commerceCommonI18NService.getCurrentLocale())));
		}
		if (CollectionUtils.isNotEmpty(mediaList))
		{
			gpBannerImageDataFacade.populateBannerImageData(mediaList, gpImageComponentData);
		}
		model.addAttribute("images", GPFunctions.convertToJSON(gpImageComponentData));
	}

	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final MardiGrasHeroBannerComponentModel component)
	{
		return GpcommonaddonControllerConstants.EXTENSION_NAME;
	}



}


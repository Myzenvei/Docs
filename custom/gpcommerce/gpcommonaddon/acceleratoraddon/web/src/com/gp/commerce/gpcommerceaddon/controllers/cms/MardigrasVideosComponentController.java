/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.controllers.cms;

import de.hybris.platform.acceleratorfacades.device.ResponsiveMediaFacade;
import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.model.GPBannerComponentModel;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.gpcommerceaddon.facades.MardigrasBannerData;
import com.gp.commerce.gpcommerceaddon.facades.impl.DefaultMardigrasVideoFacade;
import com.gp.commerce.gpcommerceaddon.model.MardigrasVideosComponentModel;
import com.gp.commercegpcommerceaddon.facades.MardigrasVideoComponentData;


/**
 * Controller for mardigras video component.
 */
@Controller("MardigrasVideosComponentController")
@RequestMapping(value = "/view/" + MardigrasVideosComponentModel._TYPECODE + "Controller")
public class MardigrasVideosComponentController extends AbstractCMSAddOnComponentController<MardigrasVideosComponentModel>
{
	@Resource(name = "mardigrasVideoFacade")
	private DefaultMardigrasVideoFacade mardigrasVideoFacade;
	
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final MardigrasVideosComponentModel component)
	{
		final MardigrasVideoComponentData videoData = new MardigrasVideoComponentData();
		final List<MardigrasBannerData> bannerData = new ArrayList<MardigrasBannerData>();
		mardigrasVideoFacade.populateMardigrasVideos(component, videoData);
		mardigrasVideoFacade.populateBanners(bannerData,component.getBanners());
		
		videoData.setImagesList(bannerData);
		model.addAttribute("videoData", GPFunctions.convertToJSON(videoData));
	}

}

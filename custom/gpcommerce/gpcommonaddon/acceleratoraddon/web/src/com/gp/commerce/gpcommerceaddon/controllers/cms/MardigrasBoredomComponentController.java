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
import com.gp.commerce.gpcommerceaddon.facades.impl.DefaultMardigrasBoredomFacade;
import com.gp.commerce.gpcommerceaddon.model.MardigrasBoredomComponentModel;
import com.gp.commercegpcommerceaddon.facades.MardigrasBoredomComponentData;


/**
 * Controller for mardigras boredom component.
 */
@Controller("MardigrasBoredomComponentController")
@RequestMapping(value = "/view/" + MardigrasBoredomComponentModel._TYPECODE + "Controller")
public class MardigrasBoredomComponentController extends AbstractCMSAddOnComponentController<MardigrasBoredomComponentModel>
{

	@Resource(name = "mardigrasBoredomFacade")
	private DefaultMardigrasBoredomFacade mardigrasBoredomFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final MardigrasBoredomComponentModel component)
	{
		final MardigrasBoredomComponentData boredomData = new MardigrasBoredomComponentData();
		final List<MardigrasBannerData> banners = new ArrayList<MardigrasBannerData>();
		mardigrasBoredomFacade.populateMardigrasBoredom(component, boredomData);
		
		final Collection<GPBannerComponentModel> gpImages = component.getBanners();
		mardigrasBoredomFacade.populateMardigrasBannerData(gpImages, boredomData, banners);
		
		
		boredomData.setBanners(banners);
		model.addAttribute("boredomData", GPFunctions.convertToJSON(boredomData));
	}
	
	
}

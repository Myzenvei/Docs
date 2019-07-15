/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.controllers.cms;


import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.model.GPBannerComponentModel;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.gpcommerceaddon.facades.MardigrasBannerData;
import com.gp.commerce.gpcommerceaddon.facades.impl.DefaultMardigrasOurPacksFacade;
import com.gp.commerce.gpcommerceaddon.model.MardigrasOurPacksComponentModel;
import com.gp.commercegpcommerceaddon.facades.MardigrasVideoComponentData;


/**
 * Controller for mardigras video component.
 */
@Controller("MardigrasOurPacksComponentController")
@RequestMapping(value = "/view/" + MardigrasOurPacksComponentModel._TYPECODE + "Controller")
public class MardigrasOurPacksComponentController extends AbstractCMSAddOnComponentController<MardigrasOurPacksComponentModel>
{
	
	@Resource(name = "mardigrasOurPacksFacade")
	private DefaultMardigrasOurPacksFacade mardigrasOurPacksFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final MardigrasOurPacksComponentModel component)
	{
		final MardigrasVideoComponentData ourPacksData = new MardigrasVideoComponentData();
		final List<MardigrasBannerData> bannerData = new ArrayList<MardigrasBannerData>();
		mardigrasOurPacksFacade.populateMardigrasVideoData(component, ourPacksData);
		
		final Collection<GPBannerComponentModel> banners = component.getBanners();
		mardigrasOurPacksFacade.populateBannerData(bannerData, banners);
		
		ourPacksData.setImagesList(bannerData);
		model.addAttribute("ourPacksData", GPFunctions.convertToJSON(ourPacksData));
	}

}

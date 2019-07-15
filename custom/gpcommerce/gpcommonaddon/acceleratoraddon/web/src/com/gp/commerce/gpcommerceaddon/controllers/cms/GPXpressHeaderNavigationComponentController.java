/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.controllers.cms;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.facades.user.GPUserFacade;
import com.gp.commerce.gpcommerceaddon.facades.GPHeaderNavigationComponentData;
import com.gp.commerce.gpcommerceaddon.facades.GpXpressHeaderMenuOptionData;
import com.gp.commerce.gpcommerceaddon.facades.impl.DefaultGPXpressHeaderNavigationFacade;
import com.gp.commerce.gpcommerceaddon.model.GPXpressHeaderNavigationComponentModel;
import com.gp.commerece.gpcommerceaddon.facades.GpXpressHeaderNavComponentData;


/**
 * GPXpress Header Navigation Component Controller.
 */
@Controller("GPXpressHeaderNavigationComponentController")
@RequestMapping(value = "/view/" + GPXpressHeaderNavigationComponentModel._TYPECODE + "Controller")
public class GPXpressHeaderNavigationComponentController
		extends AbstractCMSAddOnComponentController<GPXpressHeaderNavigationComponentModel>
{
	@Resource(name = "gpXpressHeaderNavigationFacade")
	private DefaultGPXpressHeaderNavigationFacade gpXpressHeaderNavigationFacade;
	
	@Resource(name = "gpUserFacade")
	private GPUserFacade gpUserFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final GPXpressHeaderNavigationComponentModel component)
	{
		final GPHeaderNavigationComponentData gpHeaderNavigation = gpUserFacade.getHeaderData(request, component, model);
		final GpXpressHeaderNavComponentData headerData = new GpXpressHeaderNavComponentData();
		final List<GpXpressHeaderMenuOptionData> navLinks = new ArrayList<>();
		if (null != component.getMyLists())
		{
			final GpXpressHeaderMenuOptionData linkData = gpXpressHeaderNavigationFacade.getMyListsData(component.getMyLists());
			navLinks.add(linkData);
		}
		headerData.setNavLinks(navLinks);
		model.addAttribute("headerData", GPFunctions.convertToJSON(headerData));
		model.addAttribute("headerSection", GPFunctions.convertToJSON(gpHeaderNavigation));
	}

	

}

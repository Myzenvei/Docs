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

import com.gp.commerce.core.model.GPImageLinkComponentModel;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.facades.user.GPUserFacade;
import com.gp.commerce.gpcommerceaddon.facades.GPHeaderNavigationComponentData;
import com.gp.commerce.gpcommerceaddon.facades.MardigrasMenuOptionData;
import com.gp.commerce.gpcommerceaddon.facades.impl.DefaultMardigrasHeaderNavigationFacade;
import com.gp.commerce.gpcommerceaddon.model.MardigrasHeaderNavigationComponentModel;
import com.gp.commerece.gpcommerceaddon.facades.MardigrasHeaderNavigationComponentData;


/**
 * Mardigras Header Navigation Component Controller.
 */
@Controller("MardigrasHeaderNavigationComponentController")
@RequestMapping(value = "/view/" + MardigrasHeaderNavigationComponentModel._TYPECODE + "Controller")
public class MardigrasHeaderNavigationComponentController
		extends AbstractCMSAddOnComponentController<MardigrasHeaderNavigationComponentModel>
{

	@Resource(name = "gpUserFacade")
	private GPUserFacade gpUserFacade;
	
	@Resource(name = "mardigrasHeaderNavigationFacade")
	private DefaultMardigrasHeaderNavigationFacade mardigrasHeaderNavigationFacade;
	
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final MardigrasHeaderNavigationComponentModel component)
	{
		final GPHeaderNavigationComponentData gpHeaderNavigation = gpUserFacade.getHeaderData(request, component, model);
		model.addAttribute("headerSection", GPFunctions.convertToJSON(gpHeaderNavigation));
		
		final MardigrasHeaderNavigationComponentData headerData = new MardigrasHeaderNavigationComponentData();
		final List<MardigrasMenuOptionData> navLinks = new ArrayList<MardigrasMenuOptionData>();
		if (null != component.getSocialLink())
		{
			final MardigrasMenuOptionData socialLinkData = mardigrasHeaderNavigationFacade.getSocialLinkData(component.getSocialLink());
			navLinks.add(socialLinkData);
		}
		
		if(null != component.getSocialIcon())
		{
			final MardigrasMenuOptionData socialIconData = mardigrasHeaderNavigationFacade.getSocialIconData(component.getSocialIcon());
			navLinks.add(socialIconData);
		}
		headerData.setNavLinks(navLinks);
		model.addAttribute("headerData", GPFunctions.convertToJSON(headerData));
	}



}

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
package com.gp.b2baccaddon.commerce.interceptors;


import de.hybris.platform.acceleratorstorefrontcommons.interceptors.BeforeViewHandler;
import com.gp.b2baccaddon.commerce.constants.Gpb2baccaddonConstants;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;


public class B2BAcceleratorAddonBeforeViewHandler implements BeforeViewHandler
{

	public static final String VIEW_NAME_MAP_KEY = "viewName";
	private Map<String, Map<String, String>> viewMap;

	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
			throws Exception
	{
		final String viewName = modelAndView.getViewName();
		if (viewMap.containsKey(viewName))
		{
			modelAndView.setViewName(Gpb2baccaddonConstants.ADDON_PREFIX + viewMap.get(viewName).get(VIEW_NAME_MAP_KEY));
		}
	}

	public Map<String, Map<String, String>> getViewMap()
	{
		return viewMap;
	}

	public void setViewMap(final Map<String, Map<String, String>> viewMap)
	{
		this.viewMap = viewMap;
	}

}

/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.controllers.cms;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.facades.component.data.GPImagetileComponentdata;
import com.gp.commerce.gpcommerceaddon.controllers.GpcommonaddonControllerConstants;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultImageTileFacade;
import com.gp.commerce.gpcommerceaddon.model.GPFeatureProductsComponentModel;
import com.gp.commerce.gpcommerceaddon.populators.GPImageTilePopulator;


/**
 * A controller for operations component.
 */
@Controller("GPFeatureProductsComponentController")
@RequestMapping(value = "/view/" + GPFeatureProductsComponentModel._TYPECODE + "Controller")
public class GPFeatureProductsComponentController
		extends AbstractCMSAddOnComponentController<GPFeatureProductsComponentModel>
{
	@Resource (name = "gpImageTileFacade")
	private GPDefaultImageTileFacade gpImageTileFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPFeatureProductsComponentModel component)
	{
		final GPImagetileComponentdata gpImagetileComponentdata = new GPImagetileComponentdata();
		gpImageTileFacade.populateImageTile(component, gpImagetileComponentdata);
		final String imageTile = GPFunctions.convertToJSON(gpImagetileComponentdata);
		model.addAttribute("operations", imageTile);

	}

	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final GPFeatureProductsComponentModel component)
	{
		return GpcommonaddonControllerConstants.EXTENSION_NAME;
	}

}


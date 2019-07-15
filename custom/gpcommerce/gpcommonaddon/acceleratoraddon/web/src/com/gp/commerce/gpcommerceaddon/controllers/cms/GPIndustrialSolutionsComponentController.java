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
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultIndustrialSolutionsFacade;
import com.gp.commerce.gpcommerceaddon.model.GPIndustrialSolutionsComponentModel;
import com.gp.commerce.gpcommerceaddon.populators.GPImageTilePopulator;


/**
 * controller for industrial solutions component.
 */
@Controller("GPIndustrialSolutionsComponentController")
@RequestMapping(value = "/view/" + GPIndustrialSolutionsComponentModel._TYPECODE + "Controller")
public class GPIndustrialSolutionsComponentController
		extends AbstractCMSAddOnComponentController<GPIndustrialSolutionsComponentModel>
{

	@Resource (name = "gpImageTileFacade")
	private GPDefaultImageTileFacade gpImageTileFacade;
	
	@Resource(name ="gpIndustrialSolutionsFacade")
	private GPDefaultIndustrialSolutionsFacade gpIndustrialSolutionsFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final GPIndustrialSolutionsComponentModel component)
	{
		final GPImagetileComponentdata gpImagetileComponentdata = new GPImagetileComponentdata();
		gpImageTileFacade.populateImageTile(component, gpImagetileComponentdata);
		gpIndustrialSolutionsFacade.populateImageTileData(component,gpImagetileComponentdata);
		
		final String imageTile = GPFunctions.convertToJSON(gpImagetileComponentdata);
		model.addAttribute("industrialSolutions", imageTile);

	}

	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final GPIndustrialSolutionsComponentModel component)
	{
		return GpcommonaddonControllerConstants.EXTENSION_NAME;
	}

}

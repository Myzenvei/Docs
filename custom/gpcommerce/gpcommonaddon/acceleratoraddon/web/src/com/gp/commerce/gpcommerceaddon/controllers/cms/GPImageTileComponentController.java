/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.controllers.cms;


import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.model.GPImageTileComponentModel;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.facade.catalog.impl.GpCatalogFacadeImpl;
import com.gp.commerce.facades.component.data.GPImagetileComponentdata;
import com.gp.commerce.gpcommerceaddon.controllers.GpcommonaddonControllerConstants;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultImageTileFacade;
import com.gp.commerce.gpcommerceaddon.populators.GPImageTilePopulator;



/**
 * Controller for GPIMageTileCOmponent.
 */
@Controller("GPImageTileComponentController")
@RequestMapping(value = "/view/" + GPImageTileComponentModel._TYPECODE + "Controller")
public class GPImageTileComponentController extends AbstractCMSAddOnComponentController<GPImageTileComponentModel>
{

	private static final Logger LOG = Logger.getLogger(GPImageTileComponentController.class);
	
	@Resource (name = "gpImageTileFacade")
	private GPDefaultImageTileFacade gpImageTileFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPImageTileComponentModel component)
	{
		final GPImagetileComponentdata gpImagetileComponentdata = new GPImagetileComponentdata();
		//imageTileDataPopulator.populate(component, gpImagetileComponentdata);
		gpImageTileFacade.populateImageTile(component, gpImagetileComponentdata);
		
		final String imageTile = GPFunctions.convertToJSON(gpImagetileComponentdata);
		model.addAttribute("imageTile", imageTile);

	}

	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final GPImageTileComponentModel component)
	{
		return GpcommonaddonControllerConstants.EXTENSION_NAME;
	}
}


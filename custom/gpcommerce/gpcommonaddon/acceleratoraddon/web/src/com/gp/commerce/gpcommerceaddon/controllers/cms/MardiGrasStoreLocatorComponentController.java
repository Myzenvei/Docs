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
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonWebConstants;
import com.gp.commerce.gpcommerceaddon.controllers.GpcommonaddonControllerConstants;
import com.gp.commerce.gpcommerceaddon.facades.GPImageAndTextData;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultImageAndTextDataFacade;
import com.gp.commerce.gpcommerceaddon.model.MardiGrasStoreLocatorComponentModel;
import com.gp.commerce.gpcommerceaddon.populators.GPImageAndTextDataPopulator;


/**
 * Mardi Gras StoreLocator Component Controller
 */

@Controller("MardiGrasStoreLocatorComponentController")
@RequestMapping(value = GpcommonaddonControllerConstants.Actions.Cms.MardiGrasStoreLocatorComponent)
public class MardiGrasStoreLocatorComponentController
		extends AbstractCMSAddOnComponentController<MardiGrasStoreLocatorComponentModel>
{

	@Resource(name = "gpImageAndTextDataFacade")
	private GPDefaultImageAndTextDataFacade gpImageAndTextDataFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final MardiGrasStoreLocatorComponentModel component)
	{
		final GPImageAndTextData imageAndTextData = new GPImageAndTextData();
		gpImageAndTextDataFacade.populateImageAndTextData(component, imageAndTextData);
		model.addAttribute("storeLocatorData", GPFunctions.convertToJSON(imageAndTextData));
	}

	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final MardiGrasStoreLocatorComponentModel component)
	{
		return GpcommonaddonWebConstants.EXTENSION_NAME;
	}


}

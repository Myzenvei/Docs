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

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.model.GPBannerComponentModel;
import com.gp.commerce.core.model.GPMediaComponentModel;
import com.gp.commerce.core.util.GPFunctions;

import com.gp.commerce.facades.component.data.GPImageComponentdata;
import com.gp.commerce.facades.component.data.GPImagetextComponentdata;
import com.gp.commerce.gpcommerceaddon.controllers.GpcommonaddonControllerConstants;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultBannerImageComponentFacade;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultBannerImageTextFacade;


@Controller("GPBannerComponentController")
@RequestMapping(value = "/view/" + GPBannerComponentModel._TYPECODE + "Controller")
public class GPBannerComponentController extends AbstractCMSAddOnComponentController<GPBannerComponentModel>
{
	@Resource(name = "gpBannerImageTextFacade")
	private GPDefaultBannerImageTextFacade gpBannerImageTextFacade;
	
	@Resource (name = "gpBannerImageComponentFacade")
	private GPDefaultBannerImageComponentFacade gpBannerImageComponentFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPBannerComponentModel component)
	{
		final GPImagetextComponentdata gpImagetextComponentdata = new GPImagetextComponentdata();
		final GPImageComponentdata gpImageComponentdata= new GPImageComponentdata();
		gpBannerImageTextFacade.populateBannerImageText(component, gpImagetextComponentdata);
		gpBannerImageComponentFacade.populateBannerImage(component, gpImageComponentdata);
		model.addAttribute("text",GPFunctions.convertToJSON(gpImagetextComponentdata));
		model.addAttribute("image",GPFunctions.convertToJSON(gpImageComponentdata));
	}
	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final GPBannerComponentModel component)
	{
		return  GpcommonaddonControllerConstants.EXTENSION_NAME;
	}

}


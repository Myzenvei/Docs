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

import com.gp.commerce.core.model.GPMarketingComponentModel;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.facades.component.data.GpPromotionData;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultMarketingFacade;


@Controller("GPMarketingComponentController")
@RequestMapping(value = "/view/" + GPMarketingComponentModel._TYPECODE + "Controller")
public class GPMarketingComponentController extends AbstractCMSAddOnComponentController<GPMarketingComponentModel>
{
	
	private static final String EXTENSION_NAME = "gpcommonaddon";

	
	@Resource(name = "gpMarketingFacade")
	private GPDefaultMarketingFacade gpMarketingFacade; 

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPMarketingComponentModel component)
	{
		final GpPromotionData gpPromotionData = new GpPromotionData();
		gpMarketingFacade.populatePromotionData(component,gpPromotionData);
		
		gpPromotionData.setComponentId(component.getUid());
		model.addAttribute("promotionData", GPFunctions.convertToJSON(gpPromotionData));
	}

	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final GPMarketingComponentModel component)
	{
		return EXTENSION_NAME;
	}

}

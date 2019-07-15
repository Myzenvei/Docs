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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.model.GPBannerComponentModel;
import com.gp.commerce.core.model.GPMarketingSidebySideComponentModel;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.facades.component.data.GPMarketingImagedata;
import com.gp.commerce.facades.component.data.GPMarketingSidebySideComponentdata;
import com.gp.commerce.gpcommerceaddon.controllers.GpcommonaddonControllerConstants;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultMarketingSidebySideFacade;

@Controller("GPMarketingSidebySideComponentController")
@RequestMapping(value = "/view/" + GPMarketingSidebySideComponentModel._TYPECODE + "Controller")
public class GPMarketingSidebySideComponentController extends AbstractCMSAddOnComponentController<GPMarketingSidebySideComponentModel>
{
	/** The responsive media facade. */
	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	/** The commerce common I 18 N service. */
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;
	
	@Resource(name = "gpMarketingSidebySideFacade")
	private GPDefaultMarketingSidebySideFacade gpMarketingSidebySideFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPMarketingSidebySideComponentModel component)
	{
		final GPMarketingSidebySideComponentdata gpMarketingSidebySideComponentdata = new GPMarketingSidebySideComponentdata();
		gpMarketingSidebySideFacade.populateMarketingSidebySideComponentdata(component, gpMarketingSidebySideComponentdata);
		
		final List<GPMarketingImagedata> marketingImages = new ArrayList<GPMarketingImagedata>(2);
		final List<GPBannerComponentModel> marketingSide = (List<GPBannerComponentModel>) component.getMarketingSideBanner();
		if (CollectionUtils.isNotEmpty(marketingSide))
		{
			if (marketingSide.size() > 0)
   		{
				gpMarketingSidebySideFacade.setMarketingBanner(marketingSide.get(0), marketingImages);
   		}
			if (marketingSide.size() > 1)
   		{
				gpMarketingSidebySideFacade.setMarketingBanner(marketingSide.get(1), marketingImages);
   		}
		}
		gpMarketingSidebySideComponentdata.setSbsMktgData(marketingImages);
		gpMarketingSidebySideComponentdata.setComponentId(component.getUid());
		model.addAttribute("componentTheme", component.getComponentTheme());
		model.addAttribute("marketingSide", GPFunctions.convertToJSON(gpMarketingSidebySideComponentdata));
	}

	
	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final GPMarketingSidebySideComponentModel component)
	{
		return GpcommonaddonControllerConstants.EXTENSION_NAME;
	}

}

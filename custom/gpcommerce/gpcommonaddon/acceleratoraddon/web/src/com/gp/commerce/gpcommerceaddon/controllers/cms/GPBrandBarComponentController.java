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

import com.gp.commerce.core.model.GPBrandBarComponentModel;
import com.gp.commerce.core.model.GPImageLinkComponentModel;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.facades.component.data.GPBrandBarComponentdata;
import com.gp.commerce.facades.component.data.GPBranddata;
import com.gp.commerce.gpcommerceaddon.controllers.GpcommonaddonControllerConstants;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultBrandBarFacade;

/**
 * @author deloitte
 *
 *Controller for GP Brand Bar component
 */
@Controller("GPBrandBarComponentController")
@RequestMapping(value = "/view/" + GPBrandBarComponentModel._TYPECODE + "Controller")
public class GPBrandBarComponentController extends AbstractCMSAddOnComponentController<GPBrandBarComponentModel>
{
	@Resource(name = "gpBrandBarFacade")
	private GPDefaultBrandBarFacade gpBrandBarFacade;
	
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPBrandBarComponentModel component)
	{
		final GPBrandBarComponentdata gpBrandBarComponentdata = new GPBrandBarComponentdata();
		gpBrandBarFacade.populateBrandBarData(component,gpBrandBarComponentdata);
		
		final List<GPBranddata> brands = new ArrayList<GPBranddata>();
		final List<GPImageLinkComponentModel> images = (List<GPImageLinkComponentModel>) component.getImages();
		for (final GPImageLinkComponentModel banner : images)
		{
			final GPBranddata gpBrandData = new GPBranddata();
			gpBrandBarFacade.populateBrandData(banner,gpBrandData);
			
			brands.add(gpBrandData);
		}
		
		gpBrandBarComponentdata.setBrands(brands);
		gpBrandBarComponentdata.setComponentId(component.getUid());
		model.addAttribute("brandbar", GPFunctions.convertToJSON(gpBrandBarComponentdata));
	}

	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final GPBrandBarComponentModel component)
	{
		return GpcommonaddonControllerConstants.EXTENSION_NAME;
	}

}

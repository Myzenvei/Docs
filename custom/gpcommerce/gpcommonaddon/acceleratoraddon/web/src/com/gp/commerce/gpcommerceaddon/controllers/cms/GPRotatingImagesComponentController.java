/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.controllers.cms;

import de.hybris.platform.acceleratorcms.model.components.SimpleResponsiveBannerComponentModel;
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

import com.gp.commerce.core.model.GPImageComponentModel;
import com.gp.commerce.core.model.GPMediaComponentModel;
import com.gp.commerce.core.model.GPRotatingImagesComponentModel;
import com.gp.commerce.core.model.GPVideoBannerComponentModel;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.facades.component.data.Rotatingbannerdata;
import com.gp.commerce.facades.component.data.defaultCarousel;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultRotatingImagesFacade;


@Controller("GPRotatingImagesComponentController")
@RequestMapping(value = "/view/" + GPRotatingImagesComponentModel._TYPECODE + "Controller")
public class GPRotatingImagesComponentController extends AbstractCMSAddOnComponentController<GPRotatingImagesComponentModel>
{

	private static final String EXTENSION_NAME = "gpcommonaddon";
	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	@Resource(name = "gpRotatingImagesFacade")
	private GPDefaultRotatingImagesFacade gpRotatingImagesFacade;
	/*
	 * fillModel is used to fill model object to GPRotating Images Component
	 */
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPRotatingImagesComponentModel component)
	{
		final defaultCarousel defaultCarouseldata = new defaultCarousel();
		final List<Rotatingbannerdata> rotatingbannerListdata = new ArrayList<Rotatingbannerdata>();
		for (final SimpleResponsiveBannerComponentModel banner : component.getCmsComponents())
		{
			if (banner != null && banner instanceof GPVideoBannerComponentModel)
			{
				gpRotatingImagesFacade.getGpVideoData(rotatingbannerListdata, banner);
			}
			else if (banner != null && banner instanceof GPImageComponentModel)
			{
				gpRotatingImagesFacade.getGpImageOrWrapperData(rotatingbannerListdata, banner);
			}
		}
		if (component.getTimeout() != null)
		{
			defaultCarouseldata.setAutoStart(component.getTimeout());
		}
		if (component.isLoopCarousel() == true)
		{
			defaultCarouseldata.setLoopCarousel(true);
		}
		defaultCarouseldata.setGpRotatingData(rotatingbannerListdata);
		defaultCarouseldata.setComponentId(component.getUid());
		model.addAttribute("defaultCarouseldata", GPFunctions.convertToJSON(defaultCarouseldata));
	}

	
	@Override
	protected String getAddonUiExtensionName(final GPRotatingImagesComponentModel component)
	{
		return EXTENSION_NAME;
	}
}

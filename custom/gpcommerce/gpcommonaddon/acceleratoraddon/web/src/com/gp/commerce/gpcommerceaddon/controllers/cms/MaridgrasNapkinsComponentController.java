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
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.model.GPBannerComponentModel;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.gpcommerceaddon.facades.GPFAQComponentData;
import com.gp.commerce.gpcommerceaddon.facades.GPImageAndTextData;
import com.gp.commerce.gpcommerceaddon.facades.MardigrasBannerData;
import com.gp.commerce.gpcommerceaddon.facades.impl.DefaultMaridgrasNapkinsFacade;
import com.gp.commerce.gpcommerceaddon.model.MaridgrasNapkinsComponentModel;


/**
 * Controller for mardigras napkins component.
 */
@Controller("MaridgrasNapkinsComponentController")
@RequestMapping(value = "/view/" + MaridgrasNapkinsComponentModel._TYPECODE + "Controller")
public class MaridgrasNapkinsComponentController extends AbstractCMSAddOnComponentController<MaridgrasNapkinsComponentModel>
{
	@Resource(name ="maridgrasNapkinsFacade")
	private DefaultMaridgrasNapkinsFacade maridgrasNapkinsFacade;
	
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final MaridgrasNapkinsComponentModel component)
	{
		final GPFAQComponentData napkins = new GPFAQComponentData();
		final GPImageAndTextData napkinsData = new GPImageAndTextData();
		maridgrasNapkinsFacade.populateMaridgrasNapkins(component, napkinsData);
		
		final List<MardigrasBannerData> imageCarouselData = maridgrasNapkinsFacade.getImageCarouselData(component);
		napkins.setImageAndText(napkinsData);
		model.addAttribute("napkinsData", GPFunctions.convertToJSON(napkins));
		model.addAttribute("componentTheme",component.getComponentTheme());
		model.addAttribute("imageCarouselData", GPFunctions.convertToJSON(imageCarouselData));

	}

}

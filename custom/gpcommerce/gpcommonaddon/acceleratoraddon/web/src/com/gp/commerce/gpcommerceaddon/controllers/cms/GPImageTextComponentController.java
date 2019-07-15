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
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.gp.commerce.core.model.GPImageTextComponentModel;
import com.gp.commerce.core.model.GPMediaComponentModel;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.facades.component.data.GPImageComponentdata;
import com.gp.commerce.facades.component.data.GPImagetextComponentdata;
import com.gp.commerce.gpcommerceaddon.controllers.GpcommonaddonControllerConstants;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultImageComponentFacade;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultImageTextFacade;
import com.gp.commerce.gpcommerceaddon.populators.GPImageComponentPopulator;
import com.gp.commerce.gpcommerceaddon.populators.GPImageTextPopulator;
import com.gp.commerce.gpcommerceaddon.populators.GPImageTilePopulator;

/**
 * A Component for GPImage Text Component.
 */
@Controller("GPImageTextComponentController")
@RequestMapping(value = "/view/" + GPImageTextComponentModel._TYPECODE + "Controller")
public class GPImageTextComponentController extends AbstractCMSAddOnComponentController<GPImageTextComponentModel>
{
	@Resource(name = "gpImageTextFacade")
	private GPDefaultImageTextFacade gpImageTextFacade;
	
	@Resource(name = "gpImageComponentFacade")
	private GPDefaultImageComponentFacade gpImageComponentFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPImageTextComponentModel component)
	{
		final GPImagetextComponentdata gpImagetextComponentdata = new GPImagetextComponentdata();
		final GPImageComponentdata gpImageComponentdata = new GPImageComponentdata();
		gpImageTextFacade.populateImageText(component, gpImagetextComponentdata);
		gpImageComponentFacade.populateImageComponent(component, gpImageComponentdata);
		model.addAttribute("text", GPFunctions.convertToJSON(gpImagetextComponentdata));
		model.addAttribute("image", GPFunctions.convertToJSON(gpImageComponentdata));
		if(null != component.getComponentTheme())
		{
			model.addAttribute("componentTheme", component.getComponentTheme().trim());
		}
	}
	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final GPImageTextComponentModel component)
	{
		return GpcommonaddonControllerConstants.EXTENSION_NAME;
	}

}


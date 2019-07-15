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
import com.gp.commerce.facades.faqs.GPFAQFacade;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonWebConstants;
import com.gp.commerce.gpcommerceaddon.controllers.GpcommonaddonControllerConstants;
import com.gp.commerce.gpcommerceaddon.facades.GPFAQComponentData;
import com.gp.commerce.gpcommerceaddon.facades.GPImageAndTextData;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultImageAndTextDataFacade;
import com.gp.commerce.gpcommerceaddon.model.MardiGrasFAQComponentModel;
import com.gp.commerce.gpcommerceaddon.populators.GPImageAndTextDataPopulator;


/**
 * Mardi Gras FAQ Controller
 */

@Controller("MardiGrasFAQComponentController")
@RequestMapping(value = GpcommonaddonControllerConstants.Actions.Cms.MardiGrasFAQComponent)
public class MardiGrasFAQComponentController extends AbstractCMSAddOnComponentController<MardiGrasFAQComponentModel>
{

	@Resource(name = "gpImageAndTextDataFacade")
	private GPDefaultImageAndTextDataFacade gpImageAndTextDataFacade;

	@Resource(name = "faqFacade")
	private GPFAQFacade faqFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final MardiGrasFAQComponentModel component)
	{
		final GPFAQComponentData faqData = new GPFAQComponentData();
		faqData.setFaqs(faqFacade.getFaqs());
		final GPImageAndTextData imageAndTextData = new GPImageAndTextData();
		gpImageAndTextDataFacade.populateImageAndTextData(component, imageAndTextData);
		faqData.setImageAndText(imageAndTextData);
		model.addAttribute("faq", GPFunctions.convertToJSON(faqData));
	}

	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final MardiGrasFAQComponentModel component)
	{
		return GpcommonaddonWebConstants.EXTENSION_NAME;
	}


}

package com.gp.commerce.gpcommerceaddon.controllers.cms;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonWebConstants;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultJspIncludePropertiesFacade;
import com.gp.commerce.gpcommerceaddon.model.GPJuicerFeedComponentModel;

import com.gp.commerce.gpcommerceaddon.populators.GPJspIncludePropertiesDataPopulator;


/**
 * GPJuicerFeedComponent
 */
@Controller("GPJuicerFeedComponentController")
@RequestMapping(value = "/view/" + GPJuicerFeedComponentModel._TYPECODE + "Controller")
public class GPJuicerFeedComponentController extends AbstractCMSAddOnComponentController<GPJuicerFeedComponentModel>
{
	
	@Resource(name = "gpJspIncludePropertiesFacade")
	private GPDefaultJspIncludePropertiesFacade gpJspIncludePropertiesFacade ;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPJuicerFeedComponentModel component)
	{
		
		final String juicerFeed = gpJspIncludePropertiesFacade.populateJuicerFeed();
		model.addAttribute("juicerfeed", juicerFeed);
	}

	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final GPJuicerFeedComponentModel component)
	{
		return GpcommonaddonWebConstants.EXTENSION_NAME;
	}

}

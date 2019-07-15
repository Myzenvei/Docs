package com.gp.commerce.gpcommerceaddon.controllers.cms;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.model.GPProductSolutionCMSComponentModel;
import com.gp.commerce.core.model.GPTabbedImageTileComponentModel;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.facades.component.data.GPImagetileComponentdata;
import com.gp.commerce.facades.component.data.GpProductSolutionComponentData;
import com.gp.commerce.gpcommerceaddon.facades.impl.DefaultGPProductSolutionFacade;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultImageTileFacade;
import com.gp.commerce.gpcommerceaddon.populators.GPImageTilePopulator;


/**
 * A Component controller for gp product Solution cms component.
 */
@Controller("GPProductSolutionCMSComponentController")
@RequestMapping(value = "/view/" + GPProductSolutionCMSComponentModel._TYPECODE + "Controller")
public class GPProductSolutionCMSComponentController
		extends AbstractCMSAddOnComponentController<GPProductSolutionCMSComponentModel>
{

	@Resource(name = "gpProductSolutionFacade")
	private DefaultGPProductSolutionFacade gpProductSolutionFacade;
	
	@Resource (name = "gpImageTileFacade")
	private GPDefaultImageTileFacade gpImageTileFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final GPProductSolutionCMSComponentModel component)
	{
		final GpProductSolutionComponentData gpProductSolutionComponentData = new GpProductSolutionComponentData();
		gpProductSolutionFacade.populateProductSolution(component, gpProductSolutionComponentData);
		
		final List<GPTabbedImageTileComponentModel> tabs = (List<GPTabbedImageTileComponentModel>) component.getTabs();
		if (CollectionUtils.isNotEmpty(tabs))
		{
			final List<GPImagetileComponentdata> tabData = new ArrayList<GPImagetileComponentdata>();
			for (final GPTabbedImageTileComponentModel gpTabbedImageTileComponentModel : tabs)
			{
				final GPImagetileComponentdata tab = new GPImagetileComponentdata();
				gpImageTileFacade.populateImageTile(gpTabbedImageTileComponentModel, tab);
				gpProductSolutionFacade.populateTabbedImageTile(gpTabbedImageTileComponentModel, tab);
				
				tabData.add(tab);
			}
			gpProductSolutionComponentData.setTabs(tabData);
		}
		
		model.addAttribute("componentId", component.getUid());
		model.addAttribute("productSolutionData", GPFunctions.convertToJSON(gpProductSolutionComponentData));
	}

	
}

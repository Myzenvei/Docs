/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.controllers.cms;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.model.GPMarketingCategoriesModel;
import com.gp.commerce.core.model.GPTipsAndKnowledgeInfoComponentModel;
import com.gp.commerce.core.model.GPTipsAndKnowledgeInfoModel;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.gpcommerceaddon.controllers.GpcommonaddonControllerConstants;
import com.gp.commerce.gpcommerceaddon.facades.GPMarketingCategoryData;
import com.gp.commerce.gpcommerceaddon.facades.GpTipsAndKnowledgeData;
import com.gp.commerce.gpcommerceaddon.facades.GpTipsAndKnowledgeInfoData;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultTipsAndKnowledgeInfoFacade;


/**
 * Method returns Tips and Knowledge component to gptipsandknowledgecomponent.jsp
 *
 * @author svalapi
 *
 */
@Controller("GPTipsAndKnowledgeInfoComponentController")
@RequestMapping(value = GpcommonaddonControllerConstants.Actions.Cms.GPTipsAndKnowledgeInfoComponent)
public class GPTipsAndKnowledgeInfoComponentController
		extends AbstractCMSAddOnComponentController<GPTipsAndKnowledgeInfoComponentModel>
{

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	@Resource(name = "commerceCategoryService")
	private CommerceCategoryService commerceCategoryService;
	
	@Resource(name = "gpTipsAndKnowledgeInfoFacade")
	private GPDefaultTipsAndKnowledgeInfoFacade gpTipsAndKnowledgeInfoFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final GPTipsAndKnowledgeInfoComponentModel component)
	{
		List<GpTipsAndKnowledgeInfoData> tipsAndKnowledgeInfoDataFinalList =new ArrayList<>();
		int fetchCount = 10;
		int displayCount = 10;
		if (null != component.getTitle())
		{
			model.addAttribute("title", GPFunctions.convertToJSON(component.getTitle()));
		}
		if (null != request.getParameter("activeTab"))
		{
			model.addAttribute("activeTab", request.getParameter("activeTab"));
		}
		else
		{
			model.addAttribute("activeTab", "0");
		}
		if (null != component.getFetchCount())
		{
			fetchCount = component.getFetchCount();
		}
		model.addAttribute("fetchCount", fetchCount);

		if (null != component.getDisplayCount())
		{
			displayCount = component.getDisplayCount();
		}
		model.addAttribute("displayCount", fetchCount);
		for (final CategoryModel categoryModel : component.getCategories())
		{

			GPMarketingCategoriesModel marketingCategoriesModel = new GPMarketingCategoriesModel();
			if (categoryModel instanceof GPMarketingCategoriesModel)
			{
				marketingCategoriesModel = (GPMarketingCategoriesModel) categoryModel;
			}
			
			tipsAndKnowledgeInfoDataFinalList = gpTipsAndKnowledgeInfoFacade.populateGPTipsAndKnowledge(marketingCategoriesModel
					.getTipsAndKnowledgeInfoList(), marketingCategoriesModel, fetchCount);
		}
		model.addAttribute("componentId", component.getUid());
		model.addAttribute("componentTheme", component.getComponentTheme());
		model.addAttribute("tipsAndKnowledgeInfo", GPFunctions.convertToJSON(tipsAndKnowledgeInfoDataFinalList));

	}
	
	
	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final GPTipsAndKnowledgeInfoComponentModel component)
	{
		return GpcommonaddonControllerConstants.EXTENSION_NAME;
	}
}


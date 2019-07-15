/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.facades.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.gp.commerce.core.model.GPMarketingCategoriesModel;
import com.gp.commerce.core.model.GPTipsAndKnowledgeInfoModel;
import com.gp.commerce.gpcommerceaddon.facades.GPMarketingCategoryData;
import com.gp.commerce.gpcommerceaddon.facades.GPTipsAndKnowledgeInfoFacade;
import com.gp.commerce.gpcommerceaddon.facades.GpTipsAndKnowledgeData;
import com.gp.commerce.gpcommerceaddon.facades.GpTipsAndKnowledgeInfoData;
import com.gp.commerce.gpcommerceaddon.populators.GPTipsAndKnowledgeInfoPopulator;

import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;

public class GPDefaultTipsAndKnowledgeInfoFacade implements GPTipsAndKnowledgeInfoFacade{
	
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	@Resource(name = "commerceCategoryService")
	private CommerceCategoryService commerceCategoryService;

	@Resource(name = "gpTipsAndKnowledgeInfoPopulator")
	GPTipsAndKnowledgeInfoPopulator gpTipsAndKnowledgeInfoPopulator;
	
	@Override
	public List<GpTipsAndKnowledgeInfoData> populateGPTipsAndKnowledge(final List<GPTipsAndKnowledgeInfoModel> gpTipsAndKnowledgeInfo, GPMarketingCategoriesModel marketingCategoriesModel,Integer fetchCount) {
		
		final List<GpTipsAndKnowledgeInfoData> tipsAndKnowledgeInfoDataList = new ArrayList<>();
		final GpTipsAndKnowledgeInfoData tipsAndKnowledgeInfoData = new GpTipsAndKnowledgeInfoData();
		final GPMarketingCategoryData marketingCategoryData = new GPMarketingCategoryData();
		marketingCategoryData.setMarketingCategoryHeader(marketingCategoriesModel.getCode());
		final List<GpTipsAndKnowledgeData> tipsAndKnowledgeDataList = new ArrayList<>();
		for (final GPTipsAndKnowledgeInfoModel gpTipsAndKnowledgeInfoModel : marketingCategoriesModel
				.getTipsAndKnowledgeInfoList())
		{
			
			GpTipsAndKnowledgeData tipsAndKnowledgeData = new GpTipsAndKnowledgeData();
			
			tipsAndKnowledgeData = gpTipsAndKnowledgeInfoPopulator.populate(gpTipsAndKnowledgeInfoModel, tipsAndKnowledgeData);
			tipsAndKnowledgeDataList.add(tipsAndKnowledgeData);

			if (tipsAndKnowledgeDataList.size() == fetchCount)
			{
				//FIXME : Need to handle in better way
				break;
			}
		}
		tipsAndKnowledgeInfoData.setGpMarketingCategoryData(marketingCategoryData);
		tipsAndKnowledgeInfoData.setGpTipsAndKnowledgeData(tipsAndKnowledgeDataList);
		tipsAndKnowledgeInfoDataList.add(tipsAndKnowledgeInfoData);
		return tipsAndKnowledgeInfoDataList;
	}
	
	
}

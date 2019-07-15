/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.facades;

import java.util.List;

import com.gp.commerce.core.model.GPMarketingCategoriesModel;
import com.gp.commerce.core.model.GPTipsAndKnowledgeInfoModel;

public interface GPTipsAndKnowledgeInfoFacade {

	public List<GpTipsAndKnowledgeInfoData> populateGPTipsAndKnowledge(final List<GPTipsAndKnowledgeInfoModel> gpTipsAndKnowledgeInfo, GPMarketingCategoriesModel marketingCategoriesModel,Integer fetchCount); 
}

/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.populators;

import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.gp.commerce.core.model.GPTipsAndKnowledgeInfoModel;
import com.gp.commerce.gpcommerceaddon.facades.GpTipsAndKnowledgeData;

import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;

public class GPTipsAndKnowledgeInfoPopulator {
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	public GpTipsAndKnowledgeData populate(GPTipsAndKnowledgeInfoModel gpTipsAndKnowledgeInfoModel,GpTipsAndKnowledgeData tipsAndKnowledgeData) {
		Locale loc = commerceCommonI18NService.getCurrentLocale();
		if(StringUtils.isNotEmpty(gpTipsAndKnowledgeInfoModel.getTipText(loc))) 
		{
			tipsAndKnowledgeData.setTipText(gpTipsAndKnowledgeInfoModel.getTipText(commerceCommonI18NService.getCurrentLocale()));
		}
		
		if(null != (gpTipsAndKnowledgeInfoModel.getKnowledgeMedia(loc))) 
		{
			tipsAndKnowledgeData.setTipImageUrl(gpTipsAndKnowledgeInfoModel.getKnowledgeMedia(commerceCommonI18NService.getCurrentLocale()).getURL());
		}
		if(StringUtils.isNotEmpty(gpTipsAndKnowledgeInfoModel.getKnowledgeLinkUrl(loc))) 
		{
			tipsAndKnowledgeData
				.setTipLinkUrl(gpTipsAndKnowledgeInfoModel.getKnowledgeLinkUrl(commerceCommonI18NService.getCurrentLocale()));
		}
		if(StringUtils.isNotEmpty(gpTipsAndKnowledgeInfoModel.getKnowledgeLinkText(loc))) 
		{
			tipsAndKnowledgeData
				.setTipLinkText(gpTipsAndKnowledgeInfoModel.getKnowledgeLinkText(commerceCommonI18NService.getCurrentLocale()));
		}
		
		tipsAndKnowledgeData.setExternal(gpTipsAndKnowledgeInfoModel.getIsExternalLink() == null ? Boolean.FALSE
				: gpTipsAndKnowledgeInfoModel.getIsExternalLink());
		
		if(StringUtils.isNotEmpty(gpTipsAndKnowledgeInfoModel.getTipAltText(loc)))
		{
			tipsAndKnowledgeData.setTipAltText(gpTipsAndKnowledgeInfoModel.getTipAltText(commerceCommonI18NService.getCurrentLocale()));
		}
		return tipsAndKnowledgeData;
	}
	
}

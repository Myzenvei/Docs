/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.facades.faqs.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.gp.commerce.core.model.FAQModel;
import com.gp.commerce.core.services.GPFAQService;
import com.gp.commerce.facades.faqs.GPFAQFacade;
import com.gp.commerce.facades.populators.GPFAQPopulator;
import com.gp.commerce.gpcommerceaddon.facades.GPFAQData;


/**
 * FAQ Facade Implementation
 */
public class GPDefaultFAQFacade implements GPFAQFacade
{
	@Resource(name = "faqService")
	private GPFAQService faqService;

	@Resource(name = "faqPopulator")
	private GPFAQPopulator faqPopulator;

	@Override
	public List<GPFAQData> getFaqs()
	{
		final List<FAQModel> faqs = faqService.getFAQs();
		final List<GPFAQData> faqDataList = new ArrayList<>();
		for (final FAQModel faq : faqs) {
			final GPFAQData faqData = new GPFAQData();
			faqPopulator.populate(faq, faqData);
			faqDataList.add(faqData);
		}
		return faqDataList;
	}

}

/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.services.impl;

import java.util.List;

import javax.annotation.Resource;

import com.gp.commerce.core.dao.GPFAQDao;
import com.gp.commerce.core.model.FAQModel;
import com.gp.commerce.core.services.GPFAQService;


/**
 * FAQ service implementation
 */
public class GPDefaultFAQService implements GPFAQService
{

	@Resource(name = "faqDao")
	private GPFAQDao faqDao;


	@Override
	public List<FAQModel> getFAQs()
	{
		return faqDao.getFAQs();
	}

}

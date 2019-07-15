/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.dao;

import java.util.List;

import com.gp.commerce.core.model.FAQModel;


/**
 * FAQ DAO
 */
public interface GPFAQDao
{
	/**
	 * Method to get list of FAQ
	 *
	 * @return list of FAQ model
	 */
	List<FAQModel> getFAQs();
}

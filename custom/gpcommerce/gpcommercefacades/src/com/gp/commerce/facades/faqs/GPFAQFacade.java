/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.facades.faqs;

import java.util.List;

import com.gp.commerce.gpcommerceaddon.facades.GPFAQData;

/**
 * FAQs Facade
 */
public interface GPFAQFacade
{

	/**
	 * Method to get list of FAQ for a site
	 *
	 * @return List<FAQData>
	 */
	List<GPFAQData> getFaqs();
}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.payment;

import java.util.List;

import com.gp.commerce.order.data.CardTypeDataList;

/**
 * Supported methods of credit card payment
 *
 */
public interface GPPaymentFacade
{
	
	/**
	 * Get supported credit card types for payment
	 * @return CardTypeDataList
	 */
	CardTypeDataList getCreditCardTypes();
	
	/**
	 * Get credit card expiry year list for payment
	 * @return List<String>
	 */
	List<String> getExpiryYearList();
}

/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services;

import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;

import java.util.List;

import com.gp.commerce.core.model.ShippingNotificationModel;
import com.gp.commerce.core.model.TrackingModel;

/**
 *Interface for consignment service
 */
public interface GPConsignmentService {

	/**
	 * Method to get ShippingNotification by its code
	 *
	 * @param shippingNotificationCode the shipping notification code
	 *
	 * @return {@link ShippingNotificationModel}
	 */
	ShippingNotificationModel getShippingNotificationDetails(String shippingNotificationCode);

	/**
	 * Method to get ConsignmentEntry by its entry number and Consignment code
	 *
	 * @param entryNumber the entry number
	 * @param consignmentCode the consignment code
	 *
	 * @return {@link ConsignmentEntryModel}
	 */
	ConsignmentEntryModel getConsignmentEntryByEntryNumber(Integer entryNumber, String consignmentCode);
	
	/**
 	 * Method to get Tracking details for shipping email
 	 *
 	 * @return list of {@link TrackingModel}s
 	 */
	List<TrackingModel> getTrackingDetailsForEmail();
	
	/**
	 * Method to get shipping code that are to be processed
	 *
	 * @param retryCount the count
	 * @return ShippingCodes which are to be processed
	 */
	public List<ShippingNotificationModel> getShippingCodesToProcess(int retryCount);
}

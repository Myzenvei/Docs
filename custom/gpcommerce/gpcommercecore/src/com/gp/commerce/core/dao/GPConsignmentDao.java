/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.dao;

import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;

import java.util.List;

import com.gp.commerce.core.model.ShippingNotificationModel;
import com.gp.commerce.core.model.TrackingModel;

/**
 * Interface class to perform DAO functionalities for GP Consignment
 *
 */
public interface GPConsignmentDao
{
 	/**
 	 * Method to get ShippingNotification by its code
 	 *
 	 * @param shippingNotificationCode
 	 *
 	 * @return ShippingNotificationModel
 	 */
	ShippingNotificationModel getShippingNotificationDetails(String shippingNotificationCode);

	/**
	 * Method to get ConsignmentEntry by its entry number and Consignment code
	 *
	 * @param entryNumber
	 * @param consignmentCode
	 *
	 * @return ConsignmentEntryModel
	 */
	ConsignmentEntryModel getConsignmentEntryByEntryNumber(Integer entryNumber, String consignmentCode);
	
	/**
 	 * Method to get Tracking details for shipping email
 	 *
 	 * @return TrackingModelList
 	 */
	List<TrackingModel> getTrackingDetailsForEmail();
	
	/**
 	 * Method to get shipping details to be processed 
 	 *
 	 * @return list of shippingCodes
 	 */
	public List<ShippingNotificationModel> getShippingCodeToBeProcessed(int retryCount);
}

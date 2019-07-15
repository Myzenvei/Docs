/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.gp.commerce.core.dao.GPConsignmentDao;
import com.gp.commerce.core.model.ShippingNotificationModel;
import com.gp.commerce.core.model.TrackingModel;
import com.gp.commerce.core.services.GPConsignmentService;

import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;

public class GPConsignmentServiceImpl implements GPConsignmentService {

	private static final Logger LOGGER = Logger.getLogger(GPConsignmentServiceImpl.class);
	
	private GPConsignmentDao gpConsignmentDao;
	
	@Override
	public ShippingNotificationModel getShippingNotificationDetails(String shippingNotificationCode) {
		LOGGER.info("Inside GPConsignmentService");
		return gpConsignmentDao.getShippingNotificationDetails(shippingNotificationCode);
	}
	
	@Override
	public List<ShippingNotificationModel> getShippingCodesToProcess(int retryCount) {
		LOGGER.debug("Inside GPConsignmentService");
		return gpConsignmentDao.getShippingCodeToBeProcessed(retryCount);
		
	}

	@Override
	public ConsignmentEntryModel getConsignmentEntryByEntryNumber(Integer entryNumber, String consignmentCode) {
		return gpConsignmentDao.getConsignmentEntryByEntryNumber(entryNumber, consignmentCode);
	}
	
	@Override
	public List<TrackingModel> getTrackingDetailsForEmail() {
		return gpConsignmentDao.getTrackingDetailsForEmail();
	}
	
	/**
	 * 
	 * @return GPConsignmentDao
	 */
	public GPConsignmentDao getGpConsignmentDao() {
		return gpConsignmentDao;
	}

	/**
	 * 
	 * @param gpConsignmentDao
	 */
	public void setGpConsignmentDao(GPConsignmentDao gpConsignmentDao) {
		this.gpConsignmentDao = gpConsignmentDao;
	}
}

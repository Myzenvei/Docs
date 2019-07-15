/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services;

import de.hybris.platform.consignmenttrackingservices.service.ConsignmentTrackingService;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

/**
 * Extension of {@link ConsignmentTrackingService} 
 */
public interface GPConsignmentTrackingService extends ConsignmentTrackingService{

	/**
	 * This method gets consignment by code
	 * @param code
	 * 			the code
	 * @return the consignment model
	 */
	ConsignmentModel getConsignmentByCode(String code);
}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services;

import com.gp.commerce.core.exceptions.GPSampleCartException;
import com.gp.commerce.order.data.GPSampleCartResponseData;
 

/**
 * This interface is for processing GP Sample cart services
 * 
 * @author shikhgupta
 *
 */
public interface GPSampleCartService {

	/**
	 * Returns cart response data by sending request to GPXpress
	 * 
	 * @return {@link GPSampleCartResponseData}
	 * @throws GPSampleCartException on cart error
	 */
	GPSampleCartResponseData sendRequestToGpExpress() throws GPSampleCartException;
 

}

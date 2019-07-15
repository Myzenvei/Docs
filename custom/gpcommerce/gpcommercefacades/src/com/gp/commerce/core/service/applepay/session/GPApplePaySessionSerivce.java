/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gp.commerce.core.service.applepay.session;

import com.gp.commerce.core.util.GPApplePaySessionResponseDTO;

public interface GPApplePaySessionSerivce {
	
	GPApplePaySessionResponseDTO validateSessionService(String validateURL, String baseSiteId);

}

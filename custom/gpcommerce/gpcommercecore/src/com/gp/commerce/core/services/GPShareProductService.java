/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services;

import java.io.IOException;

import com.gp.commerce.core.forms.ShareProductForm;

/**
 * Share Product Email Process Service
 *
 */
public interface GPShareProductService {
	/**
	 * Share product service takes in form as input
	 * 
	 * @param form the {@link ShareProductForm}
	 * @throws IOException on iput error
	 */
	void shareProductService(ShareProductForm form);

}

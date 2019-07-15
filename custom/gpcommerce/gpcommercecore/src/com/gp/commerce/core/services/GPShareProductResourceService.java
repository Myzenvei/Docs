/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services;

import com.gp.commerce.core.forms.ShareProductResourceForm;

/**
 * Share Product Email Process Service
 *
 */
public interface GPShareProductResourceService {
	/**
	 * Share product resource service takes in form as input
	 * @param form the {@link ShareProductResourceForm}
	 */
	void shareProductResourceService(ShareProductResourceForm form);
}

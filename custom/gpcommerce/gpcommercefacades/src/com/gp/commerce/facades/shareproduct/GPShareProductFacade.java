/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.shareproduct;

import com.gp.commerce.core.forms.ShareProductForm;
import com.gp.commerce.core.forms.ShareProductResourceForm;

/**
 * Share Product Email Process Facade.
 */
public interface GPShareProductFacade {
	
	/**
	 * Share product takes in form as input.
	 *
	 * @param form the form
	 */
	void shareProduct(ShareProductForm form);
	
	/**
	 * Share product.
	 *
	 * @param form the form
	 */
	void shareProduct(ShareProductResourceForm form);
}

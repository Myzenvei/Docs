/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.shareproduct.impl;

import com.gp.commerce.facades.shareproduct.GPShareProductFacade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.forms.ShareProductForm;
import com.gp.commerce.core.forms.ShareProductResourceForm;
import com.gp.commerce.core.services.GPShareProductResourceService;
import com.gp.commerce.core.services.GPShareProductService;

/**
 * Share Product Email process Facade implementation
 * 
 */
public class GPDefaultShareProductFacade implements GPShareProductFacade {

	@Autowired
	private GPShareProductService gpShareProductService;

	public GPShareProductService getGpShareProductService() {
		return gpShareProductService;
	}
	
	@Autowired
	private GPShareProductResourceService gpShareProductResourceService;
	
	

	public GPShareProductResourceService getGpShareProductResourceService() {
		return gpShareProductResourceService;
	}

	@Required
	public void setGpShareProductResourceService(GPShareProductResourceService gpShareProductResourceService) {
		this.gpShareProductResourceService = gpShareProductResourceService;
	}


	@Required
	public void setGpShareProductService(GPShareProductService gpShareProductService) {
		this.gpShareProductService = gpShareProductService;
	}

	
	/* 
	 * Share product calls service takes in form as input
	 */
	public void shareProduct(ShareProductForm form) {
		gpShareProductService.shareProductService(form);
	}

	/* 
	 * Share product resource calls service takes in form as input
	 */
	@Override
	public void shareProduct(ShareProductResourceForm form) {
		gpShareProductResourceService.shareProductResourceService(form);
		
	}
}

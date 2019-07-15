/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import javax.annotation.Resource;

import com.gp.commerce.core.model.GPBundleImageComponentModel;
import com.gp.commerce.core.model.GPMediaContainerlinkModel;
import com.gp.commerce.facades.component.data.GPBundleImageData;
import com.gp.commerce.facades.component.data.GPImageComponentdata;
import com.gp.commerce.gpcommerceaddon.facades.GPBundleImageFacade;
import com.gp.commerce.gpcommerceaddon.populators.GPBundleImagePopulator;

import de.hybris.platform.commercefacades.product.data.ImageData;

public class GPDefaultBundleImageFacade implements GPBundleImageFacade {
	
	@Resource(name = "gpBundleImagePopulator")
	private GPBundleImagePopulator gpBundleImagePopulator;

	public void populateImageComponent(ImageData imageData, GPImageComponentdata gpImageComponentData) {
		gpBundleImagePopulator.populate(imageData,gpImageComponentData);
		
	}

	public void populateImageLink(GPMediaContainerlinkModel mediaContainer, GPImageComponentdata gpImageComponentData) {
		gpBundleImagePopulator.populate(mediaContainer, gpImageComponentData);
		
	}

	public void populateBundleTitle(GPBundleImageComponentModel component, GPBundleImageData gpBundleData) {
		gpBundleImagePopulator.populate(component, gpBundleData);
		
	}

}

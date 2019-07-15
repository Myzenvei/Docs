/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades;

import com.gp.commerce.core.model.GPBundleImageComponentModel;
import com.gp.commerce.core.model.GPMediaContainerlinkModel;
import com.gp.commerce.facades.component.data.GPBundleImageData;
import com.gp.commerce.facades.component.data.GPImageComponentdata;

import de.hybris.platform.commercefacades.product.data.ImageData;

public interface GPBundleImageFacade {

	public void populateBundleTitle(GPBundleImageComponentModel component, GPBundleImageData gpBundleData);
	public void populateImageLink(GPMediaContainerlinkModel mediaContainer, GPImageComponentdata gpImageComponentData) ;
	public void populateImageComponent(ImageData imageData, GPImageComponentdata gpImageComponentData);
}

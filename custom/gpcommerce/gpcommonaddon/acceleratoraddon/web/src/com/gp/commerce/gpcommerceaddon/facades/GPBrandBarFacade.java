/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades;

import com.gp.commerce.core.model.GPBrandBarComponentModel;
import com.gp.commerce.core.model.GPImageLinkComponentModel;
import com.gp.commerce.facades.component.data.GPBrandBarComponentdata;
import com.gp.commerce.facades.component.data.GPBranddata;

/**
 * @author minal
 *
 */
public interface GPBrandBarFacade {
	public void populateBrandBarData(GPBrandBarComponentModel component,
			GPBrandBarComponentdata gpBrandBarComponentdata);
	
	public void populateBrandData(GPImageLinkComponentModel banner, GPBranddata gpBrandData);

}

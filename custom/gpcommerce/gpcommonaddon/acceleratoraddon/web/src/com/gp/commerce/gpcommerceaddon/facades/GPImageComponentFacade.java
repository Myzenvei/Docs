/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades;

import com.gp.commerce.core.model.GPImageTextComponentModel;
import com.gp.commerce.facades.component.data.GPImageComponentdata;

public interface GPImageComponentFacade {
	
	public void populateImageComponent(GPImageTextComponentModel component, GPImageComponentdata gpImageComponentdata);
}

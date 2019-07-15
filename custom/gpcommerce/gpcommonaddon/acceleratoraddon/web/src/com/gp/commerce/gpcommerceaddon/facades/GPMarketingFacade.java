/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades;

import com.gp.commerce.core.model.GPMarketingComponentModel;
import com.gp.commerce.facades.component.data.GpPromotionData;

public interface GPMarketingFacade {
	public void populatePromotionData(GPMarketingComponentModel component, GpPromotionData gpPromotionData);
}

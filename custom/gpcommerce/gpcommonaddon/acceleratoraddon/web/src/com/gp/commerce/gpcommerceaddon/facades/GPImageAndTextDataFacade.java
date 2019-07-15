/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades;

import com.gp.commerce.gpcommerceaddon.model.MardiGrasImageAndTextComponentModel;

public interface GPImageAndTextDataFacade {

	public void populateImageAndTextData(final MardiGrasImageAndTextComponentModel source, final GPImageAndTextData target);
}

/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades;

import java.util.List;

import de.hybris.platform.acceleratorcms.model.components.FooterNavigationComponentModel;

public interface GPFooterDataFacade {

	public void populateFooterData(final FooterNavigationComponentModel source, final List<GPFooterData> target);
}

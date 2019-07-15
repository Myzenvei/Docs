/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import java.util.List;

import javax.annotation.Resource;

import com.gp.commerce.gpcommerceaddon.facades.GPFooterData;
import com.gp.commerce.gpcommerceaddon.facades.GPFooterDataFacade;
import com.gp.commerce.gpcommerceaddon.populators.GPFooterDataPopulator;

import de.hybris.platform.acceleratorcms.model.components.FooterNavigationComponentModel;

public class GPDefaultFooterDataFacade implements GPFooterDataFacade{

	@Resource(name = "footerDataPopulator")
	private GPFooterDataPopulator footerDataPopulator;
	
	@Override
	public void populateFooterData(FooterNavigationComponentModel source, List<GPFooterData> target) {
		footerDataPopulator.populate(source, target);
		
	}

}

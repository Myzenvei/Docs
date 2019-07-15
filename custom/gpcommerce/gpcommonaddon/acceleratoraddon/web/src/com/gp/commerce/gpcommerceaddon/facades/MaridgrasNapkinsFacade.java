/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades;

import java.util.List;

import com.gp.commerce.gpcommerceaddon.model.MaridgrasNapkinsComponentModel;

public interface MaridgrasNapkinsFacade {
	
	public GPImageAndTextData populateMaridgrasNapkins(final MaridgrasNapkinsComponentModel component, GPImageAndTextData napkinsData);
	
	public List<MardigrasBannerData> getImageCarouselData(final MaridgrasNapkinsComponentModel component);
}

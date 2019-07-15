/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.populators;

import com.gp.commerce.facades.component.data.GPImagetileComponentdata;
import com.gp.commerce.gpcommerceaddon.model.GPIndustrialSolutionsComponentModel;

import de.hybris.platform.converters.Populator;

public class GPIndustrialSolutionsPopulator implements Populator<GPIndustrialSolutionsComponentModel, GPImagetileComponentdata>{

	public void populate(GPIndustrialSolutionsComponentModel component,
			GPImagetileComponentdata gpImagetileComponentdata) {
		
		if (null != component.getCtaText2())
		{
			gpImagetileComponentdata.setCtaText2(component.getCtaText2());
		}
		if (null != component.getCtaTextColor2())
		{
			gpImagetileComponentdata.setCtaColor2(component.getCtaTextColor2());
		}
		if (null != component.getCtaTextLink2())
		{
			gpImagetileComponentdata.setCtaLink2(component.getCtaTextLink2());
		}
		if (null != component.getIsExternalCta2())
		{
			gpImagetileComponentdata.setIsExternalCta2(component.getIsExternalCta2());
		}
	}

}

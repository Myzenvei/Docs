/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.populators;

import com.gp.commerce.core.model.GPProductSolutionCMSComponentModel;
import com.gp.commerce.core.model.GPTabbedImageTileComponentModel;
import com.gp.commerce.facades.component.data.GPImagetileComponentdata;
import com.gp.commerce.facades.component.data.GpProductSolutionComponentData;

public class GPProductSolutionPopulator {

	public GpProductSolutionComponentData populateProductSolution(GPProductSolutionCMSComponentModel component, GpProductSolutionComponentData gpProductSolutionComponentData)
	{
		if (null != component.getComponentTheme())
		{
			gpProductSolutionComponentData.setComponentTheme(component.getComponentTheme());
		}
		if (null != component.getHeaderText())
		{
			gpProductSolutionComponentData.setComponentHeader(component.getHeaderText());
		}
		if (null != component.getHeaderColor())
		{
			gpProductSolutionComponentData.setComponentHeaderColor(component.getHeaderColor());
		}
		if (null != component.getSubHeadingText())
		{
			gpProductSolutionComponentData.setSubHeaderText(component.getSubHeadingText());
		}
		if (null != component.getSubHeadingColor())
		{
			gpProductSolutionComponentData.setSubHeaderTextColor(component.getSubHeadingColor());
		}
		if (null != component.getCtaLink())
		{
			gpProductSolutionComponentData.setCtaLink(component.getCtaLink());
		}
		if (null != component.getCtaText())
		{
			gpProductSolutionComponentData.setCtaText(component.getCtaText());
		}
		if (null != component.getCtaStyle())
		{
			gpProductSolutionComponentData.setCtaStyle(component.getCtaStyle());
		}
		if (null != component.getIsExternalLink())
		{
			gpProductSolutionComponentData.setIsExternalLink(component.getIsExternalLink());
		}
		return gpProductSolutionComponentData;
		
	}

	public void populateTabbedImageTile(GPTabbedImageTileComponentModel gpTabbedImageTileComponentModel,
			GPImagetileComponentdata tab) {
		if (null != gpTabbedImageTileComponentModel.getCtaText2())
		{
			tab.setCtaText2(gpTabbedImageTileComponentModel.getCtaText2());
		}
		if (null != gpTabbedImageTileComponentModel.getCtaTextLink2())
		{
			tab.setCtaLink2(gpTabbedImageTileComponentModel.getCtaTextLink2());
		}
		if (null != gpTabbedImageTileComponentModel.getIsExternalCta2())
		{
			tab.setIsExternalCta2(gpTabbedImageTileComponentModel.getIsExternalCta2());
		}
		
	}

}

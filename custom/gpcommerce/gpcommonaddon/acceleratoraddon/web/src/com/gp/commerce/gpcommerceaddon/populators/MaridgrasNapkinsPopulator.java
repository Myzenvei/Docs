/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.populators;

import com.gp.commerce.gpcommerceaddon.facades.GPImageAndTextData;
import com.gp.commerce.gpcommerceaddon.model.MaridgrasNapkinsComponentModel;

import de.hybris.platform.converters.Populator;


public class MaridgrasNapkinsPopulator implements Populator<MaridgrasNapkinsComponentModel, GPImageAndTextData>{

	public void populate(final MaridgrasNapkinsComponentModel component, GPImageAndTextData napkinsData)
	{
		if (null != component.getComponentHeader())
		{
			napkinsData.setHeadingText(component.getComponentHeader());
		}
		if (null != component.getSubTileText())
		{
			napkinsData.setSubHeadingText(component.getSubTileText());
		}
		if (null != component.getInformationalText())
		{
			napkinsData.setInformationText(component.getInformationalText());
		}
	}

}

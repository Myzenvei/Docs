/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import com.gp.commerce.core.model.GPProductTabInfoModel;
import com.gp.commerce.facades.product.data.ProductInfoTabData;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class GPProductInfoTabPopulator implements Populator<GPProductTabInfoModel,ProductInfoTabData>{

	@Override
	public void populate(GPProductTabInfoModel source, ProductInfoTabData target) throws ConversionException {
		target.setSequence(source.getSequenceNumber());
		target.setName(source.getTabKey());
		target.setContent(source.getTabContent());
	}

}
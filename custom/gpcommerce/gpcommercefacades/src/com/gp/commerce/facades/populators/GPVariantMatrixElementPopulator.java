/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import com.gp.commerce.core.model.GPDefaultVariantValueCategoryModel;

import de.hybris.platform.commercefacades.product.converters.populator.VariantMatrixElementPopulator;
import de.hybris.platform.commercefacades.product.data.VariantMatrixElementData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.variants.model.VariantValueCategoryModel;

public class GPVariantMatrixElementPopulator<SOURCE extends VariantValueCategoryModel, TARGET extends VariantMatrixElementData>
		extends VariantMatrixElementPopulator {

	@Override
	public void populate(final VariantValueCategoryModel variantValueCategory,
			final VariantMatrixElementData variantMatrixElementData) throws ConversionException {
		
		super.populate(variantValueCategory, variantMatrixElementData);
		
		if(null != variantValueCategory.getThumbnail()) {
			variantMatrixElementData.getVariantValueCategory().setSwatchImageUrl(variantValueCategory.getThumbnail().getURL());
		}
		
		if(variantValueCategory instanceof GPDefaultVariantValueCategoryModel) {
			variantMatrixElementData.getVariantValueCategory().setHexCode(((GPDefaultVariantValueCategoryModel)variantValueCategory).getHexCode());
		}

	}
}

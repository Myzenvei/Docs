/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.gp.commerce.core.product.services.impl.GPB2BDefaultProductService;
import com.gp.commerce.core.services.GPCMSSiteService;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

public class GPEmailProductPopulator implements Populator<ProductModel, ProductData> {

	@Resource(name = "gpB2BProductService")
	private GPB2BDefaultProductService gpB2BProductService;
	@Resource(name = "cmsSiteService")
	GPCMSSiteService cmsSiteService;
	private Converter<ProductModel, ProductData> productConverter;
	private static final String ENABLE_DISPLAY_ATRR = "enableDisplayAttributes";

	@Override
	public void populate(ProductModel source, ProductData target) throws ConversionException {

		this.getProductConverter().convert(source, target);
		populateProductAttributes(source, target);
	}

	private void populateProductAttributes(ProductModel product, ProductData product2) {

		String enable = cmsSiteService.getSiteConfig(ENABLE_DISPLAY_ATRR);
		if (StringUtils.isNotBlank(enable) && Boolean.valueOf(enable).equals(Boolean.TRUE)) {
			product2.setDisplayAttributes(gpB2BProductService.getDisplayAttributesForProduct(product));
		}
	}

	public Converter<ProductModel, ProductData> getProductConverter() {
		return productConverter;
	}

	public void setProductConverter(Converter<ProductModel, ProductData> productConverter) {
		this.productConverter = productConverter;
	}
	
	

}

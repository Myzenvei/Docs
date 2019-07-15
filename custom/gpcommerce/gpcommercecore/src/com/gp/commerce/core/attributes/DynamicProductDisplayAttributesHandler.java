/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.attributes;
//com.gp.commerce.core.attributes.DynamicProductDisplayAttributesHandler ,dynamicProductDisplayAttributesHandler
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import com.gp.commerce.core.product.services.impl.GPB2BDefaultProductService;
import com.gp.commerce.core.services.GPCMSSiteService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;

public class DynamicProductDisplayAttributesHandler extends AbstractDynamicAttributeHandler<String, ProductModel> {

	private static final String ENABLE_DISPLAY_ATRR = "enableDisplayAttributes";

	@Resource(name = "gpB2BProductService")
	private GPB2BDefaultProductService gpB2BProductService;
	@Resource(name = "cmsSiteService")
	GPCMSSiteService cmsSiteService;

	@Override
	public String get(final ProductModel product) {
		String enable = cmsSiteService.getSiteConfig(ENABLE_DISPLAY_ATRR);
		if (StringUtils.isNotBlank(enable) && Boolean.valueOf(enable).equals(Boolean.TRUE)) {
			return gpB2BProductService.getDisplayAttributesForProduct(product);
		} else {
			return StringUtils.EMPTY;
		}
	}
	
}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.strategies;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.gp.commerce.core.exceptions.GPCommerceBusinessException;
import com.gp.commerce.core.services.GPCartService;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;

public class GPSampleCartDeleteStrategy {

	private static final String SAMPLE_CART_NOT_FOUND_MESSAGE = "delete.sample.cart.not.found";
	private static final String SAMPLE_CART_ID_NULL_MESSAGE = "delete.sample.cart.null";
	private static final String SAMPLE_CART_INVALID_STATUS_MESSAGE = "delete.sample.status.incorrect";
	private static final String SAMPLE_CART_DELETE_ERROR_CODE = "115";
	private final String SUCCESS_STATUS = "Successful";
	@Resource
	BaseSiteService baseSiteService;
	@Resource
	private ModelService modelService;
	@Resource
	private ConfigurationService configurationService;
	@Resource
	private GPCartService gpCartService;

	public boolean removeSampleCart(String code, String status) {
		if (StringUtils.isNotEmpty(code) && status.equalsIgnoreCase(SUCCESS_STATUS)) {
			CartModel cartModel = gpCartService.getCartForCodeAndSite(code, baseSiteService.getCurrentBaseSite());
			if (null == cartModel) {
				throw new GPCommerceBusinessException(SAMPLE_CART_DELETE_ERROR_CODE,
						configurationService.getConfiguration().getString(SAMPLE_CART_NOT_FOUND_MESSAGE));
			} else {
				modelService.remove(cartModel);
				return true;
			}
		} else {
			StringBuilder errorMsg=new StringBuilder(StringUtils.EMPTY);
			if(StringUtils.isEmpty(code))
			{
				errorMsg.append(configurationService.getConfiguration().getString(SAMPLE_CART_ID_NULL_MESSAGE));
			}
			if(!status.equalsIgnoreCase(SUCCESS_STATUS))
			{
				if(StringUtils.isNotBlank(errorMsg))
				{
					errorMsg.append(" ,");
				}
				errorMsg.append(configurationService.getConfiguration().getString(SAMPLE_CART_INVALID_STATUS_MESSAGE));
			}
			
			throw new GPCommerceBusinessException(SAMPLE_CART_DELETE_ERROR_CODE,errorMsg.toString());

		}

	}

}

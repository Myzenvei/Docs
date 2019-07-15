/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import com.gp.commerce.core.model.ApplePayPaymentInfoModel;
import com.gp.commerce.order.data.GPApplePayPaymentInfoData;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class ApplePayPaymentInfoDataPopulator implements Populator<ApplePayPaymentInfoModel, GPApplePayPaymentInfoData> {

	@Override
	public void populate(ApplePayPaymentInfoModel source, GPApplePayPaymentInfoData target) throws ConversionException {

		target.setPaymentData(source.getPaymentData());
		target.setTransactionIdentifier(source.getTransactionIdentifier()); 
	}

}

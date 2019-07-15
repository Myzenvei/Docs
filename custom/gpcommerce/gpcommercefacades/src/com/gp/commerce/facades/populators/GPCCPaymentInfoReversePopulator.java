/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

/**
 * populating payment fields: paymentToken
 *
 */
public class GPCCPaymentInfoReversePopulator implements Populator<CCPaymentInfoData, CreditCardPaymentInfoModel>
{

	@Override
	public void populate(final CCPaymentInfoData ccPaymentInfoData, final CreditCardPaymentInfoModel cardPaymentInfoModel)
			throws ConversionException
	{
		Assert.notNull(ccPaymentInfoData, "Parameter ccPaymentInfoData cannot be null.");
		Assert.notNull(cardPaymentInfoModel, "Parameter creditCardPaymentInfoModel cannot be null.");

		cardPaymentInfoModel.setPaymentToken(ccPaymentInfoData.getPaymentToken());

	}

}

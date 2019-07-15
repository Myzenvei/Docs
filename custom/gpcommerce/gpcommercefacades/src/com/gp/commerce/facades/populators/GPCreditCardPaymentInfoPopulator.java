/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;

/**
 * populating payment fields: paymentToken
 *
 */
public class GPCreditCardPaymentInfoPopulator implements Populator<CreditCardPaymentInfoModel, CCPaymentInfoData>
{

	@Override
	public void populate(final CreditCardPaymentInfoModel source, final CCPaymentInfoData target)
	{
		target.setPaymentToken(source.getPaymentToken());
		target.setCreationTime(source.getCreationtime());
	}

}

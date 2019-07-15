/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.facades.subscription.populators;

import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import com.gp.commerce.core.model.GPSubscriptionCartModel ;

import de.hybris.platform.commercefacades.order.converters.populator.CartPopulator;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

/**
 * @author shikhgupta
 *
 */
public class GPSubscriptionDefaultCartPopulator extends CartPopulator
{
	@Resource(name = "creditCardPaymentInfoConverter")
	private Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoConverter;
	
	@Override
	public void populate(final CartModel source, final CartData target)
	{
		super.populate(source, target);
		if (source instanceof GPSubscriptionCartModel)
		{
			final GPSubscriptionCartModel subscriptionCartModel = (GPSubscriptionCartModel) source;
			target.setFrequency(subscriptionCartModel.getFrequency());
			target.setIsActive(subscriptionCartModel.getIsActive());
			if(null != subscriptionCartModel.getSubscriptionCartStatus() && null != subscriptionCartModel.getSubscriptionCartStatus().toString()) {
				target.setSubscriptionCartStatus(subscriptionCartModel.getSubscriptionCartStatus().toString());
			}
			final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.m");
			if (null != subscriptionCartModel.getNextActivationDate())
			{
				target.setNextActivationDate(sf.format(subscriptionCartModel.getNextActivationDate()));
			}
			target.setIsSubscription(subscriptionCartModel.getIsSubscription());
			final PaymentInfoModel paymentInfo = subscriptionCartModel.getPaymentInfo();
			CCPaymentInfoData payInfoData = null;
			if (paymentInfo instanceof CreditCardPaymentInfoModel)
			{
				payInfoData = creditCardPaymentInfoConverter.convert((CreditCardPaymentInfoModel) paymentInfo);
				target.setPayment(payInfoData);
			}
			if(null != subscriptionCartModel.getSubscriptionCancellationReason()) {
				target.setSubscriptionCancellationReason(subscriptionCartModel.getSubscriptionCancellationReason());
			}
		}
	}

}

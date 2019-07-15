/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.populators;

import com.gp.commerce.core.model.GooglePayPaymentInfoModel;
import com.gp.commerce.order.data.GooglePayPaymentInfoData;
import de.hybris.platform.commercefacades.order.converters.populator.ExtendedCartPopulator;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import javax.annotation.Resource;

public class GPGooglePayInfoCartPopulator implements Populator<AbstractOrderModel,AbstractOrderData> {

    @Resource
    private Converter<GooglePayPaymentInfoModel, GooglePayPaymentInfoData> gpGooglePayPaymentInfoConverter;

    @Override
    public void populate(AbstractOrderModel abstractOrderModel, AbstractOrderData abstractOrderData) throws ConversionException {

        final PaymentInfoModel paymentInfo = abstractOrderModel.getPaymentInfo();

        if(paymentInfo instanceof GooglePayPaymentInfoModel){

            GooglePayPaymentInfoModel googlePayPaymentInfoModel=(GooglePayPaymentInfoModel) abstractOrderModel.getPaymentInfo();

            GooglePayPaymentInfoData googlePayPaymentInfoData= gpGooglePayPaymentInfoConverter.convert(googlePayPaymentInfoModel);

            abstractOrderData.setPaymentInfo(googlePayPaymentInfoData);
        }
    }

}

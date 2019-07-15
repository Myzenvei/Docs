/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;
/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
import com.gp.commerce.core.model.GooglePayPaymentInfoModel;
import com.gp.commerce.order.data.GooglePayPaymentInfoData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class GooglePayPaymentInfoDataPopulator implements Populator<GooglePayPaymentInfoModel, GooglePayPaymentInfoData> {

    @Override
    public void populate(GooglePayPaymentInfoModel googlePayPaymentInfoModel, GooglePayPaymentInfoData googlePayPaymentInfoData) throws ConversionException {
        
        googlePayPaymentInfoData.setMerchantRefCodeDesc(googlePayPaymentInfoModel.getPaymentMethodTypeDescription());

        googlePayPaymentInfoData.setPaymentToken(googlePayPaymentInfoModel.getGooglePayToken());
    }
}

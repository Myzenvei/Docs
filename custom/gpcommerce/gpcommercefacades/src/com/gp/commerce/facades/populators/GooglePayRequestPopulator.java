/*
 *
 *  *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  *  This software is the confidential and proprietary information of Georgia-Pacific.
 *
 */

package com.gp.commerce.facades.populators;

import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gp.commerce.facades.data.*;
import com.gpintegration.constants.GpintegrationConstants;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.util.Config;

import java.util.HashMap;
import java.util.Map;

public class GooglePayRequestPopulator implements Populator<CartModel, GooglePayRequestData> {


    @Override
    public void populate(CartModel cartModel, GooglePayRequestData googlePayRequestData) throws ConversionException {

        String cyberSourceMerchantID=Config.getParameter(GpintegrationConstants.MERCHANT_ID+cartModel.getSite().getUid());

        String googlePayMerchantName=Config.getParameter(GpcommerceFacadesConstants.GOOGLE_PAY_MERCHANTNAME+cartModel.getSite().getUid());

        String googlePayMerchantID=Config.getParameter(GpcommerceFacadesConstants.GOOGLE_PAY_MERCHANTID+cartModel.getSite().getUid());

        googlePayRequestData.setApiVersion(Config.getParameter(GpcommerceFacadesConstants.GOOGLE_PAY_APIVERSION));

        googlePayRequestData.setApiVersionMinor(Config.getParameter(GpcommerceFacadesConstants.GOOGLE_PAY_APIVERSIONMINOR));

        googlePayRequestData.setEnvironment(Config.getParameter(GpcommerceFacadesConstants.GOOGLE_PAY_ENVIRONMENT));

        MerchantInfoData merchantInfoData=new MerchantInfoData();

        merchantInfoData.setMerchantName(googlePayMerchantName);

        merchantInfoData.setMerchantID(googlePayMerchantID);

        googlePayRequestData.setMerchantInfo(merchantInfoData);

        AllowedPaymentMethodsData allowedPaymentMethodsData=new AllowedPaymentMethodsData();

        allowedPaymentMethodsData.setType(Config.getParameter(GpcommerceFacadesConstants.GOOGLE_PAY_ALLOWEDPAYMENTMETHODS_TYPE));

        AllowedPaymentMethodsParamsData allowedPaymentMethodsParamsData=new AllowedPaymentMethodsParamsData();

        allowedPaymentMethodsParamsData.setAllowedAuthMethods(Config.getParameter(GpcommerceFacadesConstants.GOOGLE_PAY_ALLOWEDAUTHMETHODS));

        allowedPaymentMethodsParamsData.setAllowedCardNetworks(Config.getParameter(GpcommerceFacadesConstants.GOOGLE_PAY_ALLOWEDCARDS));

        allowedPaymentMethodsParamsData.setBillingAddressRequired(GpcommerceFacadesConstants.GOOGLE_PAY_BILLING_ADDRESS_REQUIRED);

        BillingAddressParamsData billingAddressParameters=new BillingAddressParamsData();

        billingAddressParameters.setFormat(GpcommerceFacadesConstants.GOOGLE_PAY_BILLING_ADDRESS_FORMAT);

        billingAddressParameters.setPhoneNumberRequired(GpcommerceFacadesConstants.GOOGLE_PAY_BILLING_PHONE_REQUIRED);

        allowedPaymentMethodsParamsData.setBillingAddressParameters(billingAddressParameters);

        allowedPaymentMethodsData.setParameters(allowedPaymentMethodsParamsData);

        googlePayRequestData.setAllowedPaymentMethods(allowedPaymentMethodsData);

        TokenizationSpecificationData tokenizationSpecificationData=new TokenizationSpecificationData();

        tokenizationSpecificationData.setType(Config.getParameter(GpcommerceFacadesConstants.GOOGLE_PAY_TOKENIZATION_TYPE));

        TokenizationSpecificationParamsData tokenizationSpecificationParamsData=new TokenizationSpecificationParamsData();

        tokenizationSpecificationParamsData.setGateway(Config.getParameter(GpcommerceFacadesConstants.GOOGLE_PAY_TOKENIZATION_GATEWAY));

        tokenizationSpecificationParamsData.setGatewayMerchantId(cyberSourceMerchantID);

        tokenizationSpecificationData.setParameters(tokenizationSpecificationParamsData);

        allowedPaymentMethodsData.setTokenizationSpecification(tokenizationSpecificationData);

        TransactionInfoData transactionInfoData=new TransactionInfoData();

        transactionInfoData.setCurrencyCode(cartModel.getCurrency().getIsocode());
        transactionInfoData.setTotalPrice(cartModel.getTotalPrice());
        transactionInfoData.setTotalPriceStatus(GpcommerceFacadesConstants.GOOGLE_PAY_TOTALPRICE_STATUS_CONSTANT);

        googlePayRequestData.setTransactionInfo(transactionInfoData);

    }
}

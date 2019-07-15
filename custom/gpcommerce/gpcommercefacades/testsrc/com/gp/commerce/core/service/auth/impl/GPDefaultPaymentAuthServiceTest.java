/*
 *
 *  *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  *  This software is the confidential and proprietary information of Georgia-Pacific.
 *
 */

package com.gp.commerce.core.service.auth.impl;

import com.cybersource.schemas.transaction_data_1.CCAuthReply;
import com.cybersource.schemas.transaction_data_1.ReplyMessage;
import com.gp.commerce.core.jalo.GooglePayPaymentInfo;
import com.gp.commerce.core.model.GooglePayPaymentInfoModel;
import com.gp.commerce.order.data.GooglePayPaymentInfoData;
import com.gpintegration.service.impl.DefaultGPCommerceCybersourceIntegrationService;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

@UnitTest
public class GPDefaultPaymentAuthServiceTest {

    @Mock
    private DefaultGPCommerceCybersourceIntegrationService defaultGPCommerceCybersourceIntegrationService;

    private GPDefaultPaymentAuthService SUT=new GPDefaultPaymentAuthService();

    @Test
    public void testGetGooglePayPaymentAuthentication(){

        CommerceCheckoutParameter commerceCheckoutParameter=new CommerceCheckoutParameter();

        CartModel cartModel=new CartModel();
        cartModel.setCode("1234");

        GooglePayPaymentInfoModel googlePayPaymentInfoModel=new GooglePayPaymentInfoModel();
        cartModel.setPaymentInfo(googlePayPaymentInfoModel);
        commerceCheckoutParameter.setCart(cartModel);

        CCAuthReply ccAuthReply=new CCAuthReply();
        ccAuthReply.setAmount("10.0");
        ccAuthReply.setAuthorizationCode("AuthCode");

        ReplyMessage replyMessage=new ReplyMessage();
        replyMessage.setRequestID("requestID");
        replyMessage.setRequestToken("requestToken");
        replyMessage.setMerchantReferenceCode("MerchantRefereceCode");
        replyMessage.setCcAuthReply(ccAuthReply);

        GooglePayPaymentInfoData googlePayPaymentInfoData=new GooglePayPaymentInfoData();

        Mockito.when(defaultGPCommerceCybersourceIntegrationService.authorizeUsingCybersource(googlePayPaymentInfoData,commerceCheckoutParameter,Mockito.anyString(),Mockito.anyBoolean(),Mockito.anyBoolean(),Mockito.anyBoolean())).thenReturn(replyMessage);

        PaymentTransactionEntryModel paymentTransactionEntryModel=SUT.authorizeCybersource(googlePayPaymentInfoData,commerceCheckoutParameter);

        Assert.assertNotNull(paymentTransactionEntryModel);
    }

    @Test
    public void testGetGooglePayPaymentAuthentication_NULL(){

        CommerceCheckoutParameter commerceCheckoutParameter=new CommerceCheckoutParameter();

        CartModel cartModel=new CartModel();
        cartModel.setCode("1234");

        GooglePayPaymentInfoModel googlePayPaymentInfoModel=new GooglePayPaymentInfoModel();
        cartModel.setPaymentInfo(googlePayPaymentInfoModel);

        CCAuthReply ccAuthReply=new CCAuthReply();
        ccAuthReply.setAmount("10.0");
        ccAuthReply.setAuthorizationCode("AuthCode");

        ReplyMessage replyMessage=new ReplyMessage();
        replyMessage.setRequestID("requestID");
        replyMessage.setRequestToken("requestToken");
        replyMessage.setMerchantReferenceCode("MerchantRefereceCode");
        replyMessage.setCcAuthReply(ccAuthReply);

        GooglePayPaymentInfoData googlePayPaymentInfoData=new GooglePayPaymentInfoData();

        Mockito.when(defaultGPCommerceCybersourceIntegrationService.authorizeUsingCybersource(googlePayPaymentInfoData,commerceCheckoutParameter,Mockito.anyString(),Mockito.anyBoolean(),Mockito.anyBoolean(),Mockito.anyBoolean())).thenReturn(replyMessage);

        PaymentTransactionEntryModel paymentTransactionEntryModel=SUT.authorizeCybersource(googlePayPaymentInfoData,commerceCheckoutParameter);

        Assert.assertNull(paymentTransactionEntryModel);
    }
}

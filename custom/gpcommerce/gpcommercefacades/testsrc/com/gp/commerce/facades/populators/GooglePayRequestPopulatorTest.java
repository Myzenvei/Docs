/*
 *
 *  *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  *  This software is the confidential and proprietary information of Georgia-Pacific.
 *
 */

package com.gp.commerce.facades.populators;

import com.gp.commerce.facades.data.GooglePayRequestData;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.util.Config;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class GooglePayRequestPopulatorTest {

    private static final String PAYMENTTOKEN = "successful";

    @InjectMocks
    GooglePayRequestPopulator populator = new GooglePayRequestPopulator();

    CCPaymentInfoData target = new CCPaymentInfoData();


    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPopulate()
    {

        GooglePayRequestData googlePayRequestData=new GooglePayRequestData();

        CartModel cartModel=new CartModel();
        CurrencyModel currencyModel=new CurrencyModel();
        currencyModel.setIsocode("USD");
        cartModel.setCurrency(currencyModel);
        cartModel.setTotalPrice(50.0);

        Mockito.when(Config.getParameter(Mockito.anyString())).thenReturn("configParam");

        populator.populate(cartModel,googlePayRequestData);

        Assert.assertEquals(cartModel.getTotalTax(),googlePayRequestData.getTransactionInfo().getTotalPrice());
    }
}

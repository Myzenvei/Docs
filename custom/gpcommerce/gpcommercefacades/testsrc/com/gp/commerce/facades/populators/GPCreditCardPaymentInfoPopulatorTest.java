/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;

/**
 * @author akapandey
 */
@UnitTest
public class GPCreditCardPaymentInfoPopulatorTest {

	private static final String PAYMENTTOKEN = "successful";
	
	@InjectMocks
	GPCreditCardPaymentInfoPopulator populator = new GPCreditCardPaymentInfoPopulator();
	
	CreditCardPaymentInfoModel source = new CreditCardPaymentInfoModel();
	CCPaymentInfoData target = new CCPaymentInfoData();
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		source.setPaymentToken(PAYMENTTOKEN);
	}
	
	@Test
	public void testPopulate()
	{
		populator.populate(source, target);
		Assert.assertEquals(target.getPaymentToken(), source.getPaymentToken());
	}
}

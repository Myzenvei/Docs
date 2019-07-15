/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
/**
 * @author akapandey
 */
package com.gp.commerce.facades.populators;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.gp.commerce.facades.populators.GPPersonalDetailsPopulator;
import org.mockito.Mock;
import org.mockito.Mockito;

import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;

public class GPPersonalDetailsPopulatorTest {

	private static final String CELLPHONE = "9876543210";
	private static final String LOGINTYPE = "LoginType";
	
	@InjectMocks
	private GPPersonalDetailsPopulator populator=new GPPersonalDetailsPopulator();
	
	UserModel source = new UserModel();
	CustomerData target = new CustomerData();
	CustomerModel customer = new CustomerModel();
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		customer.setCellPhone(CELLPHONE);
		customer.setLoginType(LOGINTYPE);
		customer.setAddToMarketComm(Boolean.TRUE);
		customer.setAddToAffilatedMarketComm(Boolean.TRUE);
	}
	
	@Test
	public void testPopulate()
	{
		populator.populate(customer, target);
		Assert.assertEquals(target.getCellPhone(), customer.getCellPhone());
		Assert.assertEquals(target.getLoginType(), customer.getLoginType());
		Assert.assertEquals(target.getAddToMarketComm(), customer.getAddToMarketComm());
		Assert.assertEquals(target.getAddToAffilatedMarketComm(), customer.getAddToAffilatedMarketComm());
	}
	
	
}

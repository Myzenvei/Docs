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

import com.gp.commerce.core.strategies.GPDefaultCustomerNameStrategy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.core.model.security.PrincipalModel;

/**
 * @author akapandey
 */
@UnitTest
public class GPPrincipalPopulatorTest {

	private static final String UID = "104";
	private static final String DISPLAYNAME = "abc";
	
	@InjectMocks
	GPPrincipalPopulator populator = new GPPrincipalPopulator();
	@Mock
	GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy;
	
	PrincipalModel source = new PrincipalModel();;
	PrincipalData target = new PrincipalData();
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		source.setUid(UID);
		source.setName(DISPLAYNAME);
	}
	
	@Test
	public void testPopulate()
	{
		populator.populate(source, target);
		Assert.assertEquals(target.getUid() ,source.getUid());
		Assert.assertEquals(target.getName(), source.getDisplayName());
	}
}

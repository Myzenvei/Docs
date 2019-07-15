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

import com.gp.commerce.core.model.ProductCategoriesModel;
import com.gp.commerce.facades.store.data.GPStoreCategoriesData;

import de.hybris.bootstrap.annotations.UnitTest;

/**
 * @author akapandey
 */
@UnitTest
public class GPStoreLocatorCategoriesPopulatorTest {

	private static final String GROUPID = "A2019";
	private static final String GROUPNAME = "ABCDE";
	
	@InjectMocks
	GPStoreLocatorCategoriesPopulator populator = new GPStoreLocatorCategoriesPopulator();
	
	ProductCategoriesModel source = new ProductCategoriesModel();
	GPStoreCategoriesData target = new GPStoreCategoriesData();
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		source.setGroupId(GROUPID);
		source.setGroupName(GROUPNAME);
	}
	
	@Test
	public void testPopulate()
	{
		populator.populate(source, target);
		Assert.assertEquals(target.getGroupId(), source.getGroupId());
		Assert.assertEquals(target.getGroupName(), source.getGroupName());
	}
	
}

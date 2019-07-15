/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.model.StoreProductModel;
import com.gp.commerce.facades.store.data.GPStoreProductData;

import de.hybris.bootstrap.annotations.UnitTest;
/**
 * @author akapandey
 */
@UnitTest
public class GPStoreProductsPopulatorTest {
	
	private static final String CODE = "104";
	private static final String DESCRIPTION = "Description";
	
	@InjectMocks
	GPStoreProductsPopulator populator = new GPStoreProductsPopulator();
	
	@Mock
	StoreProductModel source = new StoreProductModel();
	@Mock
	GPStoreProductData target = new GPStoreProductData();
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		source.setCode(CODE);
		source.setDescription(DESCRIPTION);
	}
	
	@Test
	public void testPopulate()
	{
		populator.populate(source, target);
		Assert.assertEquals(target.getCode(), source.getCode());
		Assert.assertEquals(target.getDescription(), source.getDescription());
	}

}

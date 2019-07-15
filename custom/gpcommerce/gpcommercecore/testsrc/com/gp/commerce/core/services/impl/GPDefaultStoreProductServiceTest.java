package com.gp.commerce.core.services.impl;

import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.gp.commerce.core.dao.GPStoreProductDao;
import com.gp.commerce.core.model.StoreProductModel;
import de.hybris.bootstrap.annotations.UnitTest;

@UnitTest
public class GPDefaultStoreProductServiceTest {
	
	@InjectMocks
	GPDefaultStoreProductService gpStoreProductService = new GPDefaultStoreProductService();
	
	@Mock
	private StoreProductModel storeProduct;
	@Mock
	private GPStoreProductDao storeProductDao;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(gpStoreProductService, "storeProductDao", storeProductDao);
		
		Mockito.when(storeProductDao.getStoreProducts()).thenReturn(Collections.singletonList(storeProduct));
			}
	
	@Test
	public void testRemoveSelectedWishlists1() {
		Assert.assertTrue(gpStoreProductService.getStoreProducts().get(0).equals(storeProduct));
	}
	

}

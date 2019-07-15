/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.storeLocator.categories.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.gp.commerce.core.model.StoreProductModel;
import com.gp.commerce.core.services.GPStoreProductService;
import com.gp.commerce.facades.populators.GPStoreProductsPopulator;
import com.gp.commerce.facades.store.data.GPStoreProductsData;

import de.hybris.bootstrap.annotations.UnitTest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

@UnitTest
public class GPDefaultStoreProductsFacadeTest {

    @InjectMocks
    private com.gp.commerce.facades.storeLocator.categories.impl.GPDefaultStoreProductsFacade gpDefaultStoreProductsFacadeUnderTest;

    private StoreProductModel product;
    private List<StoreProductModel> products;
    @Mock
    private GPStoreProductService storeProductService;
    @Mock
    private GPStoreProductsPopulator storeProductPopulator;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        product=Mockito.mock(StoreProductModel.class);
        products=new ArrayList<StoreProductModel>();
        products.add(product);
        ReflectionTestUtils.setField(gpDefaultStoreProductsFacadeUnderTest, "storeProductService", storeProductService);
        // gpDefaultStoreProductsFacadeUnderTest = new GPDefaultStoreProductsFacade();
        Mockito.when(storeProductService.getStoreProducts()).thenReturn(products);
    }

    @Test
    public void testGetStoreProducts() {
        // Setup
        final GPStoreProductsData expectedResult = null;

        // Run the test
        final GPStoreProductsData result = gpDefaultStoreProductsFacadeUnderTest.getStoreProducts();

        // Verify the results
        assertEquals(product.getCode(), result.getGpStoreProducts().iterator().next().getCode());
    }
}

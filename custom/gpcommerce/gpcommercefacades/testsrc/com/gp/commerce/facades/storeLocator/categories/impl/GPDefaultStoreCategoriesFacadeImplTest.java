/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.storeLocator.categories.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.gp.commerce.core.model.ProductCategoriesModel;
import com.gp.commerce.facades.populators.GPStoreLocatorCategoriesPopulator;
import com.gp.commerce.facades.store.data.GPStoreCategoriesData;
import com.gp.commerce.facades.store.data.GPStoreCategoriesListData;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.site.BaseSiteService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.gp.commerce.facades.storeLocator.categories.impl.GPDefaultStoreCategoriesFacadeImpl;

@UnitTest
public class GPDefaultStoreCategoriesFacadeImplTest {

    @InjectMocks
    private GPDefaultStoreCategoriesFacadeImpl gpDefaultStoreCategoriesFacadeImplUnderTest;

    @Mock
    private BaseSiteService baseSiteService;

    @Mock
    private BaseSiteModel baseSite;

    @Mock
    private ProductCategoriesModel category;

    private Collection<ProductCategoriesModel> categories = new ArrayList<>();

    private GPStoreLocatorCategoriesPopulator gpStoreLocatorCategoriesPopulator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        gpDefaultStoreCategoriesFacadeImplUnderTest  = new GPDefaultStoreCategoriesFacadeImpl();
        categories.add(category);
        gpDefaultStoreCategoriesFacadeImplUnderTest.setBaseSiteService(baseSiteService);
        Mockito.when(baseSiteService.getCurrentBaseSite()).thenReturn(baseSite);
        Mockito.when(baseSite.getCategories()).thenReturn(categories);
        gpStoreLocatorCategoriesPopulator=new GPStoreLocatorCategoriesPopulator();
        gpDefaultStoreCategoriesFacadeImplUnderTest.setDefaultGpStoreLocatorCategoriesPopulator(gpStoreLocatorCategoriesPopulator);

    }

    @Test
    public void testGetStoreCategories() {


        final GPStoreCategoriesListData result = gpDefaultStoreCategoriesFacadeImplUnderTest.getStoreCategories();
        assertEquals(category.getGroupId(), result.getGpCategoriesList().iterator().next().getGroupId());
        assertEquals(category.getGroupName(), result.getGpCategoriesList().iterator().next().getGroupName());
    }

    @Test
    public void testGetDefaultGpStoreLocatorCategoriesPopulator() {
        final GPStoreLocatorCategoriesPopulator expectedResult = gpStoreLocatorCategoriesPopulator;
        final GPStoreLocatorCategoriesPopulator result = gpDefaultStoreCategoriesFacadeImplUnderTest.getDefaultGpStoreLocatorCategoriesPopulator();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSetDefaultGpStoreLocatorCategoriesPopulator() {
        // Setup
        final GPStoreLocatorCategoriesPopulator defaultGpStoreLocatorCategoriesPopulator = null;

        // Run the test
        gpDefaultStoreCategoriesFacadeImplUnderTest.setDefaultGpStoreLocatorCategoriesPopulator(defaultGpStoreLocatorCategoriesPopulator);
    }

    @Test
    public void testGetBaseSiteService() {
        // Setup
        final BaseSiteService expectedResult = baseSiteService;

        // Run the test
        final BaseSiteService result = gpDefaultStoreCategoriesFacadeImplUnderTest.getBaseSiteService();

        assertEquals(expectedResult, result);
    }

    @Test
    public void testSetBaseSiteService() {
        // Setup
        final BaseSiteService baseSiteService = null;

        gpDefaultStoreCategoriesFacadeImplUnderTest.setBaseSiteService(baseSiteService);
    }
}

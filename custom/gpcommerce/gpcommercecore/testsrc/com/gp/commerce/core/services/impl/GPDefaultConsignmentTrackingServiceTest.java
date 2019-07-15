package com.gp.commerce.core.services.impl;

import java.util.ArrayList;
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
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

@UnitTest
public class GPDefaultConsignmentTrackingServiceTest {
	
	@InjectMocks
	GPDefaultConsignmentTrackingService gpConsignmentTrackingService = new GPDefaultConsignmentTrackingService();
	
	@Mock
	private FlexibleSearchService flexibleSearchService;
	
	@Mock
	private ConsignmentModel consignment;
	@Mock
	private GPStoreProductDao storeProductDao;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(gpConsignmentTrackingService, "flexibleSearchService", flexibleSearchService);
		
		final SearchResult<ConsignmentModel> searchResult=Mockito.mock(SearchResult.class);
		Mockito.when(flexibleSearchService.search(Mockito.anyString(),Mockito.anyMap())).thenReturn(searchResult);
		Mockito.when(searchResult.getResult()).thenReturn(Collections.singletonList(consignment));
		
			}
	
	@Test
	public void testGetConsignmentByCode() {
		Assert.assertTrue(gpConsignmentTrackingService.getConsignmentByCode("Code").equals(consignment));
	}
	
	@Test
	public void testGetConsignmentByCodeNull() {
		
		final SearchResult<ConsignmentModel> searchResult1=Mockito.mock(SearchResult.class);
		Mockito.when(flexibleSearchService.search(Mockito.anyString(),Mockito.anyMap())).thenReturn(searchResult1);
		Mockito.when(searchResult1.getResult()).thenReturn(new ArrayList<>());
		Assert.assertTrue(null ==gpConsignmentTrackingService.getConsignmentByCode("Code"));
	}


}

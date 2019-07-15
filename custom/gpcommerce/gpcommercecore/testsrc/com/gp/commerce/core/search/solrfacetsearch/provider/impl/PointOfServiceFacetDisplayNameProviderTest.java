/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.pos.PointOfServiceService;

/**
 * @author dadidam
 */
@UnitTest
public class PointOfServiceFacetDisplayNameProviderTest {
	
	@InjectMocks
	private final PointOfServiceFacetDisplayNameProvider pointOfServiceFacetDisplayNameProvider = new PointOfServiceFacetDisplayNameProvider();
	
	@Mock
	private IndexedProperty indexedProperty;
	@Mock
	private PointOfServiceService pointOfServiceService;
	@Mock
	private PointOfServiceModel pointOfServiceModel;
	@Mock
	private SearchQuery searchQuery;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);	
		ReflectionTestUtils.setField(pointOfServiceFacetDisplayNameProvider, "pointOfServiceService", pointOfServiceService);
	}

	@Test
	public void testGetDisplayName() throws FieldValueProviderException {
		
		Mockito.when(pointOfServiceService.getPointOfServiceForName(Mockito.anyString())).thenReturn(pointOfServiceModel);
		Mockito.when(pointOfServiceModel.getName()).thenReturn("abcd");
		assertNotNull(pointOfServiceModel);
		assertNotNull(pointOfServiceModel.getName());
		pointOfServiceFacetDisplayNameProvider.getDisplayName(searchQuery, indexedProperty, "MultiSelectOr");
		when(pointOfServiceService.getPointOfServiceForName(Mockito.anyString())).thenReturn(null);
		pointOfServiceFacetDisplayNameProvider.getDisplayName(searchQuery, indexedProperty, "MultiSelectOr");
		
	}

}
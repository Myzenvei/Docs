/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */ 
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

/**
 * @author dgandabonu
 */
@UnitTest
public class ColorFacetDisplayNameProviderTest {
	
	
	@InjectMocks
	private final ColorFacetDisplayNameProvider colorFacetDisplayNameProvider = new ColorFacetDisplayNameProvider();
	
	@Mock
	private EnumerationService enumerationService;
	
	@Mock
	private I18NService i18nService;
	
	@Mock
	private CommonI18NService commonI18NService;
	
	@Mock
	private FacetSearchConfig facetSearchConfig;
	
	@Mock
	private IndexedType indexedType;	
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		when(i18nService.getCurrentLocale()).thenReturn(Locale.ENGLISH);
		
	}
	
	@Test
	public void testGetDisplayName() {
		
		
		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		IndexedProperty property = new IndexedProperty();
		String facetValue = "";
		colorFacetDisplayNameProvider.getDisplayName(query, property, facetValue);
		colorFacetDisplayNameProvider.getDisplayName(query, property, null);
		
		query.setLanguage("EN"); 
		when(enumerationService.getEnumerationName(Mockito.anyObject(), Mockito.anyObject())).thenReturn("RED");
		colorFacetDisplayNameProvider.getDisplayName(query, property, "");
	}

}
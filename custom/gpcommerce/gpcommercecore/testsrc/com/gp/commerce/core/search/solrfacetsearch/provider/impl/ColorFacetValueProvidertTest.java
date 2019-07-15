/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */ 

package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.enums.SwatchColorEnum;
import com.gp.commerce.core.model.GPCommerceSizeVariantProductModel;
import com.gp.commerce.core.model.GPCommerceStyleVariantProductModel;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;

/**
 * @author dgandabonu
 */
@UnitTest
public class ColorFacetValueProvidertTest {
	
	@InjectMocks
	private final ColorFacetValueProvider colorFacetValueProvider = new ColorFacetValueProvider();
	
	@Mock
	private IndexConfig indexConfig;
	
	@Mock
	private IndexedProperty indexedProperty;
	
	@Mock
	private FieldNameProvider fieldNameProvider;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		List<String> fieldNames = new ArrayList<>();
		fieldNames.add("color");
		when(fieldNameProvider.getFieldNames(indexedProperty, null)).thenReturn(fieldNames); 
		
	}
	
	@Test
	public void testGetFieldValues() throws FieldValueProviderException {
		
		GPCommerceSizeVariantProductModel apparelSizeVariantProductModel = new GPCommerceSizeVariantProductModel();
		GPCommerceStyleVariantProductModel ApparelStyleVariantProductModel = new GPCommerceStyleVariantProductModel();
		Set<SwatchColorEnum> swatchColors = new HashSet<>();
		swatchColors.add(SwatchColorEnum.BLUE);
		swatchColors.add(SwatchColorEnum.BLACK); 
		ApparelStyleVariantProductModel.setSwatchColors(swatchColors);
		apparelSizeVariantProductModel.setBaseProduct(ApparelStyleVariantProductModel);
		
		colorFacetValueProvider.getFieldValues(indexConfig, indexedProperty, apparelSizeVariantProductModel);
	}

}
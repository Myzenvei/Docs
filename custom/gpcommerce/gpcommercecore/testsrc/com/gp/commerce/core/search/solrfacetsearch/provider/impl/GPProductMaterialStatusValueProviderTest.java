package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.gp.commerce.core.enums.MaterialStatusEnum;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;

@UnitTest
public class GPProductMaterialStatusValueProviderTest {

	@InjectMocks
	GPProductMaterialStatusValueProvider gpProductMaterialStatusValueProvider = new GPProductMaterialStatusValueProvider();
	
	@Mock
	private FieldNameProvider fieldNameProvider;
	@Mock
	private IndexConfig indexConfig;
	@Mock
	private IndexedProperty indexedProperty;
	@Mock
	private ProductModel productModel;
	@Mock
	private Object object;
	@Mock
	private FieldValue fieldValue;
	@Mock
	List<FieldValue> listFieldValues;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		gpProductMaterialStatusValueProvider.setFieldNameProvider(fieldNameProvider);
	}

	@Test
	public void testGetFieldValues() throws FieldValueProviderException {
		List<FieldValue> list = new ArrayList<FieldValue>();
		Collection<FieldValue> field = new ArrayList<FieldValue>();
		field.addAll(list);
		assertEquals(gpProductMaterialStatusValueProvider.getFieldValues(indexConfig, indexedProperty, productModel), field);
		List<FieldValue> emptyList = Collections.emptyList();
		Collection<FieldValue> emptyField = emptyList;
		assertEquals(gpProductMaterialStatusValueProvider.getFieldValues(indexConfig, indexedProperty, object), emptyField);
		
	}
	
	@Test
	public void testCreateFieldValue() {
		Mockito.when(productModel.getMaterialStatus()).thenReturn(MaterialStatusEnum.ACTIVE);
		Collection<String> fieldNames = new ArrayList<String>();
		fieldNames.add("sample");
		Mockito.when(fieldNameProvider.getFieldNames(indexedProperty, null)).thenReturn(fieldNames);
		assertTrue(!gpProductMaterialStatusValueProvider.createFieldValue(productModel, indexedProperty).isEmpty());
		
	}

	@Test
	public void testAddFieldValues() {
		Collection<String> fieldNames = new ArrayList<String>();
		fieldNames.add("sample");
		Mockito.when(fieldNameProvider.getFieldNames(indexedProperty, null)).thenReturn(fieldNames);
		gpProductMaterialStatusValueProvider.addFieldValues(listFieldValues, indexedProperty, productModel);
		Mockito.verify(listFieldValues).add(Mockito.any());
	}

}

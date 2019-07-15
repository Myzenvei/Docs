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

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;

public class GPReplacementProductCodeValueProviderTest {

	@InjectMocks
	GPReplacementProductCodeValueProvider service = new GPReplacementProductCodeValueProvider();
	@Mock
	private FieldNameProvider fieldNameProvider;
	@Mock
	private IndexConfig indexConfig;
	@Mock
	private IndexedProperty indexedProperty;
	@Mock
	private Object object;
	@Mock
	private ProductModel productModel;
	@Mock
	private FieldValue fieldValue;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(service, "fieldNameProvider", fieldNameProvider);
		service.setFieldNameProvider(fieldNameProvider);
	}

	@Test
	public void testGetFieldValues() throws FieldValueProviderException {
		List<FieldValue> list = new ArrayList<FieldValue>();
		Collection<FieldValue> field = new ArrayList<FieldValue>();
		field.addAll(list);
		assertEquals(service.getFieldValues(indexConfig, indexedProperty, productModel), field);
	}
	
	@Test
	public void testCreateFieldValue() {
		List<FieldValue> list = new ArrayList<FieldValue>();
		list.add(fieldValue);
		
		Mockito.when(productModel.getReplacedBy()).thenReturn(productModel);
		Collection<String> fieldNames = new ArrayList<String>();
		fieldNames.add("sample");
		Mockito.when(fieldNameProvider.getFieldNames(indexedProperty, null)).thenReturn(fieldNames);
		assertNotNull(service.createFieldValue(productModel, indexedProperty));
		
	}

	@Test
	public void testAddFieldValues() {
		Collection<String> fieldNames = new ArrayList<String>();
		fieldNames.add("sample");
		Mockito.when(fieldNameProvider.getFieldNames(indexedProperty, null)).thenReturn(fieldNames);
	}


}

/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.enums.CertificationsEnum;
import com.gp.commerce.core.model.GPCertificationsModel;

/**
 * @author dgandabonu
 */
@UnitTest
public class GPProductCertificationValueProviderTest {

	@InjectMocks
	private final GPProductCertificationValueProvider gpProductCertificationValueProvider = new GPProductCertificationValueProvider();

	@Mock
	private IndexedProperty indexedProperty;

	@Mock
	private FieldNameProvider fieldNameProvider;

	@Mock
	private IndexConfig indexConfig;
	
	@Mock
	private ModelService modelService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		final List<String> certificates = new ArrayList<>();
		certificates.add("ECC");
		when(fieldNameProvider.getFieldNames(indexedProperty, null)).thenReturn(certificates);

	}

	@Test
	public void testGetFieldValues() throws FieldValueProviderException {

		final ProductModel productModel = new ProductModel();
		final GPCertificationsModel gpCertificationModel=modelService.create(GPCertificationsModel.class);
		final Set<GPCertificationsModel> certifications = new HashSet<>();
		certifications.add(gpCertificationModel);
		productModel.setGpcertifications(certifications);

		//method call-1
		gpProductCertificationValueProvider.getFieldValues(indexConfig, indexedProperty, productModel);

		when(indexedProperty.isLocalized()).thenReturn(true);
		final List<LanguageModel> languages = new ArrayList<>(1);
		final LanguageModel language = new LanguageModel();
		language.setIsocode("en");
		languages.add(language);
		when(indexConfig.getLanguages()).thenReturn(languages);

		//method call-2
		gpProductCertificationValueProvider.getFieldValues(indexConfig, indexedProperty, productModel);

		//method call-3
		gpProductCertificationValueProvider.getFieldValues(indexConfig, indexedProperty,new CartModel());
	}


}

package com.gp.commerce.core.services.impl;

import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.gp.commerce.core.model.ProductTaxCodeCustomModel;
import com.gp.commerce.core.services.GPProductTaxCodeService;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.testframework.Assert;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultGPProductTaxCodeServiceTest {

	@Mock
	FlexibleSearchService flexibleSearchService;

	@InjectMocks
	GPProductTaxCodeService gpProductTaxCodeService = new DefaultGPProductTaxCodeService();

	@Before
	public void setup() {

	}

	@SuppressWarnings("unchecked")
	@Test
	public void fetchTaxCodeTest() {
		@SuppressWarnings("unchecked")
		final SearchResult<ProductTaxCodeCustomModel> searchResult = Mockito.mock(SearchResult.class);
		ProductTaxCodeCustomModel productTax=Mockito.mock(ProductTaxCodeCustomModel.class);
		final List<ProductTaxCodeCustomModel> productTaxList = Collections
				.singletonList(productTax);
		Mockito.when(productTax.getTaxCode()).thenReturn("TaxCode");
		Mockito.when(flexibleSearchService.search(Mockito.anyString(), Mockito.anyMap())).thenReturn(searchResult);
				
		Mockito.when(searchResult.getResult()).thenReturn(productTaxList);

		assertTrue(gpProductTaxCodeService.fetchTaxCode("productCode1", "TaxArea").equalsIgnoreCase("TaxCode"));
		// flexibleSearchService.search(query, params);
	}
	@Test
	public void fetchTaxCodeNullResult()
	{
		
		Mockito.when(flexibleSearchService.search(Mockito.anyString(), Mockito.anyMap())).thenReturn(null);
		assertTrue(null==gpProductTaxCodeService.fetchTaxCode("productCode1", "TaxArea"));
	
	}
}
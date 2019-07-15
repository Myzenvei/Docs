package com.gp.commerce.core.dao.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import com.gp.commerce.core.model.GPEndUserLegalTermsModel;

import com.gp.commerce.core.dao.impl.GPDefaultCartDao;
import com.gp.commerce.core.model.ShippingRestrictionModel;
import com.gp.commerce.facade.order.data.LeaseAgreementData;

@UnitTest
public class GPDefaultCartDaoTest {

	@InjectMocks
	private GPDefaultCartDao dao = new GPDefaultCartDao();
	
	@Mock
	private FlexibleSearchService flexibleSearchService;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		dao.setFlexibleSearchService(flexibleSearchService);
	}
	
	@Test
	public void testGetFlexibleSearchService()
	{
		Assert.assertNotNull(dao.getFlexibleSearchService());
	}
	
	@Test
	public void testFetchShippingRestrictions()
	{
		String productCode = "code";
		String countryCode = "US";
		String region = "TX";
		
		List<ShippingRestrictionModel> result = new ArrayList<>();
		result.add(Mockito.mock(ShippingRestrictionModel.class));
		final SearchResult searchResult = new SearchResultImpl(result, 1, 1, 1);
		
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		Assert.assertNotNull(dao.fetchShippingRestrictions(productCode, countryCode, region));
	}
	
	@Test
	public void testGetLeaseAgreementForCountry()
	{
		String country = "US";
		
		List<GPEndUserLegalTermsModel> legalTerms = new ArrayList<>();
		GPEndUserLegalTermsModel gpEndUserLegalTermsModel = Mockito.mock(GPEndUserLegalTermsModel.class);
		gpEndUserLegalTermsModel.setCountry("US");
		gpEndUserLegalTermsModel.setLegalLanguage("EN");
		gpEndUserLegalTermsModel.setLegalTermName("Name");
		gpEndUserLegalTermsModel.setLegalTermsText("text");
		gpEndUserLegalTermsModel.setVersion(new Integer(1));
		legalTerms.add(gpEndUserLegalTermsModel);
		
		final SearchResult searchResult = new SearchResultImpl(legalTerms, 1, 1, 1);
		
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		Assert.assertNotNull(dao.getLeaseAgreementForCountry(country));
	}
}

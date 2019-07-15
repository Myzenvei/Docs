package com.gp.commerce.core.user.dao.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.user.AddressModel;

import com.gp.commerce.core.enums.GPApprovalEnum;
import com.gp.commerce.core.model.MarketingPreferenceModel;
import com.gp.commerce.core.model.MarketingPreferenceTypeModel;
import org.junit.Assert;

@UnitTest
public class GPDefaultUserDaoTest {

	@InjectMocks
	private GPDefaultUserDao dao = new GPDefaultUserDao();
	
	@Mock
	private FlexibleSearchService flexibleSearchService;
	
	@Mock
	private SearchRestrictionService searchRestrictionService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		dao.setFlexibleSearchService(flexibleSearchService);
	}
	
	@Test
	public void testGetMarketingPreferencesForSite()
	{
		CMSSiteModel site = Mockito.mock(CMSSiteModel.class);
		MarketingPreferenceModel marketingPrefModel = new MarketingPreferenceModel();
		marketingPrefModel.setValue("value");
		List<MarketingPreferenceModel> result = new ArrayList<>();
		result.add(marketingPrefModel);
		final SearchResult searchResult = new SearchResultImpl(result, 1, 1, 1);
		
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		
		Assert.assertEquals("value",dao.getMarketingPreferencesForSite(site).get(0).getValue());
		
	}
	
	@Test
	public void testGetAddressesForB2BUser()
	{
		List<B2BUnitModel> units = new ArrayList<>();
		units.add(Mockito.mock(B2BUnitModel.class));
		AddressModel addressModel = new AddressModel();
		addressModel.setCode("code");
		List<AddressModel> result = new ArrayList<>();
		result.add(addressModel);
		final SearchResult searchResult = new SearchResultImpl(result, 1, 1, 1);
		
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		Assert.assertNotNull(dao.getAddressesForB2BUser(units,false));
		Assert.assertNotNull(dao.getAddressesForB2BUser(units,true));
		Assert.assertEquals("code",dao.getAddressesForB2BUser(units,false).get(0).getCode());
	}
	
	@Test
	public void testGetDistMarketingPreferences()
	{
		CMSSiteModel site = Mockito.mock(CMSSiteModel.class);
		MarketingPreferenceTypeModel marketingPref = new MarketingPreferenceTypeModel();
		marketingPref.setMarketingPreferenceId("id");
		List<MarketingPreferenceTypeModel> result =  new ArrayList<>();
		result.add(marketingPref);
		final SearchResult searchResult = new SearchResultImpl(result, 1, 1, 1);

		when(flexibleSearchService.<MarketingPreferenceTypeModel>search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		Assert.assertEquals("id",dao.getDistMarketingPreferences(site).get(0).getMarketingPreferenceId());
	}

	@Test
	public void testFindUsersByCodes() {
		List<String> uids = new ArrayList<>();
		uids.add("uid");
		B2BCustomerModel customer = new B2BCustomerModel();
		customer.setUid("uid");
		 List<B2BCustomerModel> b2bCustomers = new ArrayList<>();
		 b2bCustomers.add(customer);
		final SearchResult searchResult = new SearchResultImpl(b2bCustomers, 1, 1, 1);
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);

		searchRestrictionService.disableSearchRestrictions();
		Assert.assertEquals(b2bCustomers,dao.findUsersByCodes(uids));
	}
	
	@Test
	public void testGetMarketingPreferencesForSiteAndType() {
		CMSSiteModel site = Mockito.mock(CMSSiteModel.class);
		MarketingPreferenceModel marketingPref = new MarketingPreferenceModel();
		marketingPref.setValue("value");
		List<MarketingPreferenceModel> marketingPreferences = new ArrayList<>();
		marketingPreferences.add(marketingPref);
		SearchResult searchResult = new  SearchResultImpl<>(marketingPreferences, 1, 1, 1);
		MarketingPreferenceTypeModel markPrefType = Mockito.mock(MarketingPreferenceTypeModel.class);
		
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		Assert.assertEquals("value",dao.getMarketingPreferencesForSiteAndType(site, markPrefType).get(0).getValue());
	}

	@Test
	public void testGetAllAddressesOnStatus() {
		AddressModel addressModel = new AddressModel();
		addressModel.setCode("code");
		List<AddressModel> result = new ArrayList<>();
		result.add(addressModel);
		List<GPApprovalEnum> approvalStatuses = new ArrayList<>();
		approvalStatuses.add(Mockito.mock(GPApprovalEnum.class));
		
		final SearchResult searchResult = new SearchResultImpl(result, 1, 1, 1);
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		Assert.assertEquals("code",dao.getAllAddressesOnStatus(approvalStatuses).get(0).getCode());
	}
}

package com.gp.commerce.core.company.dao.impl;

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
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;

import com.gp.commerce.core.company.dao.impl.GPDefaultB2BUnitDao;

@UnitTest
public class GPDefaultB2BUnitDaoTest {

	@InjectMocks
	private GPDefaultB2BUnitDao dao = new GPDefaultB2BUnitDao();
	
	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		dao.setFlexibleSearchService(flexibleSearchService);
	}
	
	@Test
	public void testFindB2BUnitMembersByGroup()
	{
		final List<B2BUnitModel> units = new ArrayList<>();
		units.add(Mockito.mock(B2BUnitModel.class));
		final List<String> userGroupIds = new ArrayList<>();
		userGroupIds.add(new String("id"));
		List<B2BCustomerModel> result = new ArrayList<>();
		result.add(Mockito.mock(B2BCustomerModel.class));
		final SearchResult searchResult = new SearchResultImpl(result, 1, 1, 1); 
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		
		Assert.assertNotNull(dao.findB2BUnitMembersByGroup(units, userGroupIds));
	}
}

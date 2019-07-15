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

import com.gp.commerce.core.dao.impl.DefaultGPB2BUserGroupDao;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;

import com.gp.commerce.dto.company.data.UserB2BUnitWsDTO;

@UnitTest
public class DefaultGPB2BUserGroupDaoTest {

	@InjectMocks
	private DefaultGPB2BUserGroupDao dao = new DefaultGPB2BUserGroupDao("code");
	
	@Mock
	private FlexibleSearchService flexibleSearchService;
	
	@Mock
	private SearchRestrictionService searchRestrictionService;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testFindUserGroupsByUnits()
	{
		UserB2BUnitWsDTO userB2BUnitList = Mockito.mock(UserB2BUnitWsDTO.class);
		
		List<String> uids = new ArrayList<>();
		uids.add("uid");
		userB2BUnitList.setB2BUnitList(uids);
		
		Mockito.doNothing().when(searchRestrictionService).disableSearchRestrictions();
		List<B2BUserGroupModel> userGroups = new ArrayList<>();
		userGroups.add(Mockito.mock(B2BUserGroupModel.class));
		final SearchResult searchResult = new SearchResultImpl(userGroups, 1, 1, 1);
		
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		Mockito.doNothing().when(searchRestrictionService).enableSearchRestrictions();
		
		Assert.assertNotNull(dao.findUserGroupsByUnits(userB2BUnitList));
	}
	
	@Test
	public void testFindB2BUserGroupsByCodes()
	{
		final List<String> codes = new ArrayList<>();
		codes.add("code");
		
		Mockito.doNothing().when(searchRestrictionService).disableSearchRestrictions();
		List<B2BUserGroupModel> userGroups = new ArrayList<>();
		userGroups.add(Mockito.mock(B2BUserGroupModel.class));
		final SearchResult searchResult = new SearchResultImpl(userGroups, 1, 1, 1);
		
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		Mockito.doNothing().when(searchRestrictionService).enableSearchRestrictions();
		
		Assert.assertNotNull(dao.findB2BUserGroupsByCodes(codes));
	}
}

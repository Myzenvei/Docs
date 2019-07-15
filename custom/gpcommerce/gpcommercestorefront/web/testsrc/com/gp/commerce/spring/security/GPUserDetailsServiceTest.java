/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.spring.security;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Integer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.spring.security.CoreUserDetails;
import de.hybris.platform.spring.security.CoreUserDetailsService;
import de.hybris.platform.util.StandardSearchResult;

@SuppressWarnings("deprecation")
@UnitTest
public class GPUserDetailsServiceTest {

	@InjectMocks
	private GPUserDetailsService gpUserDetails = new GPUserDetailsService();
	@Mock
	FlexibleSearch flexibleSearch;
	@Mock
	SearchResult<Integer> searchResult;
	final List<Integer> result = Arrays.asList(Integer.valueOf(10));

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		//final Tenant tenant = Registry.getCurrentTenant();
		MockitoAnnotations.initMocks(this);
		searchResult=Mockito.mock(SearchResult.class);
	}

	@Test
	public void testLoadUserByUsername() {
		GPUserDetailsService spy = Mockito.spy(new GPUserDetailsService());
		GPUserDetails coreUserDetails = Mockito.mock(GPUserDetails.class);
		Mockito.doReturn(coreUserDetails).when((CoreUserDetailsService) spy).loadUserByUsername(Mockito.anyString());
	}
	@Test
	public void testFetchCustomerFailedLogins() {
     
		Mockito.doReturn(searchResult).when(flexibleSearch).search(Mockito.anyString(),Mockito.anyMap(),Mockito.any());
		Mockito.when(searchResult.getResult()).thenReturn(result);
	}

}

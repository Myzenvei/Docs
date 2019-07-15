package com.gp.commerce.core.coupon.dao.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import com.gp.commerce.core.coupon.dao.impl.GPCouponDaoImpl;

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

@UnitTest
public class GPCouponDaoImplTest {

	@InjectMocks
	private GPCouponDaoImpl dao = new GPCouponDaoImpl();
	
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
	public void testGetCouponForCode()
	{
		List<AbstractCouponModel> result = new ArrayList<>();
		result.add(Mockito.mock(AbstractCouponModel.class));
		final SearchResult searchResult = new SearchResultImpl(result, 1, 1, 1);
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		
		Assert.assertNotNull(dao.getCouponForCode("code"));
	}
}

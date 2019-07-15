package com.gp.commerce.core.dao.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;

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

import com.gp.commerce.core.dao.impl.GPDefaultDeliveryDao;

@UnitTest
public class GPDefaultDeliveryDaoTest {

	@InjectMocks
	private GPDefaultDeliveryDao dao = new GPDefaultDeliveryDao();
	
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
	public void testgetDeliveryModeValueList() throws Exception
	{
		CurrencyModel curr = Mockito.mock(CurrencyModel.class);
		CountryModel country = Mockito.mock(CountryModel.class);
		DeliveryModeModel deliveryMode = Mockito.mock(DeliveryModeModel.class);
		double totalPrice = 1;
		
		List<ZoneDeliveryModeValueModel> result = new ArrayList<>();
		result.add(Mockito.mock(ZoneDeliveryModeValueModel.class));
		final SearchResult searchResult = new SearchResultImpl(result, 1, 1, 1);
		
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		when(curr.getBase()).thenReturn(Boolean.TRUE);
		Assert.assertNotNull(dao.getDeliveryModeValueList(curr, country, deliveryMode, totalPrice));
	}
}

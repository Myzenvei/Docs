package com.gp.commerce.core.savedvalus.dao.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.gp.commerce.core.savedvalus.dao.impl.GPDefaultSavedValuesDao;
import de.hybris.platform.hmc.model.SavedValuesModel;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;

@UnitTest
public class GPDefaultSavedValuesDaoTest {

	@InjectMocks
	private GPDefaultSavedValuesDao dao = new GPDefaultSavedValuesDao();
	
	@Mock
	private FlexibleSearchService flexibleSearchService;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		dao.setFlexibleSearchService(flexibleSearchService);
	}
	
	@Test
	public void testGetChangedLogs()
	{
		List<SavedValuesModel> result = new ArrayList<>();
		result.add(Mockito.mock(SavedValuesModel.class));
		final SearchResult searchResult = new SearchResultImpl(result, 1, 1, 1);
		
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		Assert.assertNotNull(dao.getChangedLogs(Mockito.mock(ItemModel.class)));
	}
}

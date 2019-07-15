package com.gp.commerce.core.quickorder.dao.impl;

import static org.mockito.Mockito.mock;
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

import com.gp.commerce.core.model.GPCustomerMaterialInfoModel;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;

@UnitTest
public class GPQuickOrderDaoImplTest {
	@InjectMocks
	private GPQuickOrderDaoImpl dao = new GPQuickOrderDaoImpl();
	@Mock
	private FlexibleSearchService flexibleSearchService;
	
	GPCustomerMaterialInfoModel gpCustomerMaterialInfoModel=mock(GPCustomerMaterialInfoModel.class);
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		dao.setFlexibleSearchService(flexibleSearchService);
	}
	
	
	@Test
	public void getMaterialInfoForB2BUnit() {
		String productCodes = "12,34";
		String b2bUnit ="b2bUnit";
		List<GPCustomerMaterialInfoModel> result = new ArrayList();
		result.add(gpCustomerMaterialInfoModel);
		final SearchResult searchResult = new SearchResultImpl(result, 1, 1, 1);
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		Assert.assertNotNull(dao.getMaterialInfoForB2BUnit(b2bUnit, productCodes));
	}
}

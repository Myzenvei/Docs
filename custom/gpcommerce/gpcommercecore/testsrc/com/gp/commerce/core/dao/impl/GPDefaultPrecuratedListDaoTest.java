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
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import com.gp.commerce.core.dao.impl.GPDefaultPrecuratedListDao;

@UnitTest
public class GPDefaultPrecuratedListDaoTest {

	@InjectMocks
	private GPDefaultPrecuratedListDao dao = new GPDefaultPrecuratedListDao();
	
	@Mock
	private FlexibleSearchService flexibleSearchService;
	
	@Mock
	private CMSSiteService cmsSiteService;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetPrecuratedWishList()
	{
		CMSSiteModel site = Mockito.mock(CMSSiteModel.class);
		Mockito.when(cmsSiteService.getCurrentSite()).thenReturn(site);
		Mockito.when(site.getUid()).thenReturn("id");
		
		List<Wishlist2Model> result = new ArrayList<>();
		result.add(Mockito.mock(Wishlist2Model.class));
		final SearchResult searchResult = new SearchResultImpl(result, 1, 1, 1);
		
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		Assert.assertNotNull(dao.getPrecuratedWishList());
	}
}

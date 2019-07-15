package com.gp.commerce.core.wishlist.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import com.gp.commerce.core.wishlist.dao.impl.GPDefaultWishlistDao;
import de.hybris.platform.wishlist2.model.Wishlist2Model;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;

import org.junit.Assert;

/**
 * JUnit test suite for {@link GPDefaultWishlistDaoTest}
 */
@UnitTest
public class GPDefaultWishlistDaoTest {

	@InjectMocks
	private GPDefaultWishlistDao dao = new GPDefaultWishlistDao();
	
	@Mock
	private FlexibleSearchService flexibleSearchService;
	
	@Mock
	private CMSSiteService cmsSiteService;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		dao.setFlexibleSearchService(flexibleSearchService);
	}
	
	@Test
	public void testGetWishlistByName()
	{
		UserModel user = Mockito.mock(UserModel.class);
		String name = "user";
		
		final List<Wishlist2Model> result = new ArrayList<>();
		result.add(Mockito.mock(Wishlist2Model.class));
		final SearchResult searchResult = new SearchResultImpl(result, 1, 1, 1);
		
		Mockito.when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		
		Assert.assertNotNull(dao.getWishlistByName(user, name));
	}
	
	@Test
	public void testGetWishlistByType()
	{
		UserModel user = Mockito.mock(UserModel.class);
		String type = "type";
		
		final List<Wishlist2Model> result = new ArrayList<>();
		result.add(Mockito.mock(Wishlist2Model.class));
		final SearchResult searchResult = new SearchResultImpl(result, 1, 1, 1);
		
		Mockito.when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		
		Assert.assertNotNull(dao.getWishlistByType(user, type));
	}
	
	@Test
	public void testGetWishlistById()
	{
		final List<Wishlist2Model> result = new ArrayList<>();
		result.add(Mockito.mock(Wishlist2Model.class));
		final SearchResult searchResult = new SearchResultImpl(result, 1, 1, 1);
		
		Mockito.when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		
		Assert.assertNotNull(dao.getWishlistById(Mockito.anyString()));
	}
	
	@Test
	public void testGetPrecuratedlist()
	{
		CMSSiteModel site = Mockito.mock(CMSSiteModel.class);

		final List<Wishlist2Model> result = new ArrayList<>();
		result.add(Mockito.mock(Wishlist2Model.class));
		final SearchResult searchResult = new SearchResultImpl(result, 1, 1, 1);
		Mockito.when(cmsSiteService.getCurrentSite()).thenReturn(site);
		Mockito.when(cmsSiteService.getCurrentSite().getUid()).thenReturn("uid");
		Mockito.when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		
		Assert.assertNotNull(dao.getPrecuratedlist("list"));
	}
	
	@Test
	public void testGetB2BCustomerForEmail()
	{
		final List<B2BCustomerModel> result = new ArrayList<>();
		result.add(Mockito.mock(B2BCustomerModel.class));
		String email= "sneha.nagarajan@gapac.com";
		final SearchResult searchResult = new SearchResultImpl(result, 1, 1, 1);
		
		Mockito.when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		Assert.assertNotNull(dao.getB2BCustomerForEmail(Mockito.anyString()));
	}
}

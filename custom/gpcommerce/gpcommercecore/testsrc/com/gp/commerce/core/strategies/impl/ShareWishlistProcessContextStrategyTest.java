package com.gp.commerce.core.strategies.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.model.WishlistProcessModel;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

@UnitTest
public class ShareWishlistProcessContextStrategyTest {
	
	@InjectMocks
	ShareWishlistProcessContextStrategy shareWishlistStrategy = new ShareWishlistProcessContextStrategy();
	
	@Mock
	private BaseSiteModel site;
	@Mock
	private UserModel user;
	@Mock
	private Wishlist2Model wishlist;
	@Mock
	private WishlistProcessModel wishlistProcessModel;
	@Mock
	CustomerModel customer;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetCmsSite()
	{
		Mockito.when(wishlistProcessModel.getSite()).thenReturn(site);
		Assert.assertTrue(shareWishlistStrategy.getCmsSite(wishlistProcessModel).equals(site));
	}
	
	@Test
	public void testGetCustomer()
	{
		Mockito.when(wishlistProcessModel.getWishlist()).thenReturn(wishlist);
		Mockito.when(wishlistProcessModel.getWishlist().getUser()).thenReturn(customer);
		Assert.assertTrue(shareWishlistStrategy.getCustomer(wishlistProcessModel).equals(customer));
	}

}

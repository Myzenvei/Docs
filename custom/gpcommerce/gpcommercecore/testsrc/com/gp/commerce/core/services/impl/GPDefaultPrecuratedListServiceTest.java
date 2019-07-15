package com.gp.commerce.core.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import com.gp.commerce.core.dao.GPPrecuratedListDao;
import com.gp.commerce.core.services.impl.GPDefaultPrecuratedListService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

/**
 * JUnit test suite for {@link GPDefaultPrecuratedListServiceTest}
 */
@UnitTest
public class GPDefaultPrecuratedListServiceTest {

	@InjectMocks
	private final GPDefaultPrecuratedListService precuratedListService = new GPDefaultPrecuratedListService();
	
	@Mock
	private GPPrecuratedListDao gpPrecuratedListDao;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetPrecuratedWishList() {
		List<Wishlist2Model> models = new ArrayList<>();
		Mockito.when(gpPrecuratedListDao.getPrecuratedWishList()).thenReturn(models);
		Assert.assertNotNull(precuratedListService.getPrecuratedWishList());
	}
}
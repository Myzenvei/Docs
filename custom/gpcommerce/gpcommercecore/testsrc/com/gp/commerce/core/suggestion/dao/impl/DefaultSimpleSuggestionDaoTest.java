package com.gp.commerce.core.suggestion.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.suggestion.dao.impl.DefaultSimpleSuggestionDao;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;

import org.junit.Assert;

/**
 * JUnit test suite for {@link DefaultSimpleSuggestionDaoTest}
 */
@SuppressWarnings("deprecation")
@UnitTest
public class DefaultSimpleSuggestionDaoTest {

	@InjectMocks
	private final DefaultSimpleSuggestionDao defaultSimpleSuggestionDao = new DefaultSimpleSuggestionDao();
	
	@Mock
	private FlexibleSearchService flexibleSearchService;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		defaultSimpleSuggestionDao.setFlexibleSearchService(flexibleSearchService);
	}
	
	@Test
	public void testFindProductsRelatedToPurchasedProductsByCategory()
	{
		final List<ProductReferenceTypeEnum> referenceTypes = new ArrayList<>();
		referenceTypes.add(ProductReferenceTypeEnum.FOLLOWUP);
		
		List<ProductModel> products = new ArrayList<>();
		final SearchResult result = new SearchResultImpl(products, 1, 1, 1);
		Mockito.when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(result);
		
		Assert.assertNotNull(defaultSimpleSuggestionDao.findProductsRelatedToPurchasedProductsByCategory(Mockito.mock(CategoryModel.class),
				referenceTypes, Mockito.mock(UserModel.class), true, NumberUtils.INTEGER_ONE));
	}
	
	@Test
	public void testFindProductsRelatedToProducts()
	{
		final List<ProductReferenceTypeEnum> referenceTypes = new ArrayList<>();
		referenceTypes.add(ProductReferenceTypeEnum.FOLLOWUP);
		
		List<ProductModel> products = new ArrayList<>();
		products.add(Mockito.mock(ProductModel.class));
		final SearchResult result = new SearchResultImpl(products, 1, 1, 1);
		Mockito.when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(result);
		
		Assert.assertNotNull(defaultSimpleSuggestionDao.findProductsRelatedToProducts(products,
				referenceTypes, Mockito.mock(UserModel.class), true, NumberUtils.INTEGER_ONE));
	}
}
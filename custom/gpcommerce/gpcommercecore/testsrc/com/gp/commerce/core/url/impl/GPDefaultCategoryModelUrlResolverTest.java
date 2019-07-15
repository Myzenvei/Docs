/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.url.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.drools.core.command.assertion.AssertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.core.PK;
import de.hybris.platform.site.BaseSiteService;

@UnitTest
public class GPDefaultCategoryModelUrlResolverTest {

	@InjectMocks
	private GPDefaultCategoryModelUrlResolver categoryModelResolver = new GPDefaultCategoryModelUrlResolver();

	private static final String BASE_SITE_UID_PARAMETER = "{baseSiteUid}";

	@Mock
	private BaseSiteModel currentBaseSite;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private CommerceCategoryService commerceCategoryService;
	@Mock
	private CategoryModel source;
	private CatalogVersionModel catalogVersion=new CatalogVersionModel();

	private CatalogModel catalog =new CatalogModel();

	private Collection<List<CategoryModel>> paths;
	private String pattern;
	private List<CategoryModel> categoryModels;

	private CategoryModel categoryModel;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		source=new CategoryModel();
		source.setCode("1232131");
	}

	@Test
	public void getBaseSiteUidTest() {
		BaseSiteModel currentBaseSite = Mockito.mock(BaseSiteModel.class);
		Mockito.when(baseSiteService.getCurrentBaseSite()).thenReturn(currentBaseSite);
		assertEquals(currentBaseSite.getUid(),categoryModelResolver.getBaseSiteService().getCurrentBaseSite().getUid());
	}

	@Test
	public void buildPathStringTest() {
		categoryModels=new ArrayList<>();
		categoryModel=Mockito.mock(CategoryModel.class);
		categoryModels.add(categoryModel);
		Mockito.when(categoryModels.size()).thenReturn(1);
		categoryModels.get(0).setName("Category");
	}

	@Test
	public void getBaseSiteUidNullTest() {
		BaseSiteModel currentBaseSite = Mockito.mock(BaseSiteModel.class);
		Mockito.when(baseSiteService.getCurrentBaseSite()).thenReturn(null);
	}

	@Test
	public void getCategoryPathTest() {
		CategoryModel category = Mockito.mock(CategoryModel.class);
		CategoryModel category2 = Mockito.mock(CategoryModel.class);

		paths = new LinkedList<List<CategoryModel>>();
		List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		categoryList.add(category);
		categoryList.add(category2);
		List<CategoryModel> categoryList2 = new ArrayList<CategoryModel>();
		categoryList2.add(category);
		categoryList2.add(category2);
		paths.add(categoryList);
		paths.add(categoryList2);

		Mockito.when(commerceCategoryService.getPathsForCategory(category)).thenReturn(paths);

		categoryModelResolver.getCategoryPath(category);
	}

	@Test
	public void testResolveInternal() {
		categoryModelResolver.setPattern("{baseSite-uid}");
		String url=categoryModelResolver.resolveInternal(source);
		assertTrue(url.contains("baseSite"));
	}

	@Test
	public void testResolveInternalUrlContainsPath() {
		categoryModelResolver.setPattern("{category-path}");
		String url=categoryModelResolver.resolveInternal(source);
		assertTrue(url.contains("baseSite"));
	}

	@Test
	public void testResolveInternalUrlContainsCode() {
		categoryModelResolver.setPattern("{category-code}");
		Mockito.when(source.getCode()).thenReturn("123213");
		String url=categoryModelResolver.resolveInternal(source);
		assertTrue(url.contains("1232131"));
	}

	@Test
	public void testResolveInternalUrlContainsCatalogId() {
		catalog.setId("123");
		catalogVersion.setCatalog(catalog);
		source.setCatalogVersion(catalogVersion);
		categoryModelResolver.setPattern("{catalog-id}");
		Mockito.when(source.getCatalogVersion().getCatalog().getId()).thenReturn("567567");
		String url=categoryModelResolver.resolveInternal(source);
		assertTrue(url.contains("1232131"));
	}
}

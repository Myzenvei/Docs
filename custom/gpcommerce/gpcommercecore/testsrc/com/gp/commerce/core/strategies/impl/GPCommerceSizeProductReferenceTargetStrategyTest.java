/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.strategies.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.model.GPCommerceSizeVariantProductModel;
import com.gp.commerce.core.model.GPCommerceStyleVariantProductModel;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.variants.model.VariantProductModel;

@UnitTest
public class GPCommerceSizeProductReferenceTargetStrategyTest {

	@InjectMocks
	private GPCommerceSizeProductReferenceTargetStrategy gpSizeProductStrategy = new GPCommerceSizeProductReferenceTargetStrategy();

	@Mock
	private GPCommerceSizeVariantProductModel sourceProduct;
	@Mock
	private ProductReferenceModel reference;
	@Mock
	private CategoryModel superCategory;
	@Mock
	private CommerceCategoryService commerceCategoryService;
	@Mock
	private VariantProductModel variant;
	@Mock
	private GPCommerceStyleVariantProductModel target;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		gpSizeProductStrategy.setRootCategoryCode("abc");
	}

	
	@Test
	public void testGetTarget()
	{
		List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		categoryList.add(superCategory);
		LinkedList<List<CategoryModel>> path = new LinkedList<List<CategoryModel>>();
		Mockito.when(commerceCategoryService.getPathsForCategory(superCategory)).thenReturn(path);
		
		List<CategoryModel> sourceSuperCategories = new ArrayList<CategoryModel>();
		sourceSuperCategories.add(superCategory);
		
		List<CategoryModel> targetSuperCategories = new ArrayList<CategoryModel>();
		targetSuperCategories.add(superCategory);

		Mockito.when(reference.getTarget()).thenReturn(target);
		Mockito.when(sourceProduct.getSupercategories()).thenReturn(sourceSuperCategories);
		Mockito.when(target.getSupercategories()).thenReturn(targetSuperCategories);
		Mockito.when(reference.getTarget().getVariants()).thenReturn(Collections.singletonList(sourceProduct));
		
		
		Mockito.when(commerceCategoryService.getPathsForCategory(superCategory)).thenReturn(Collections.singleton(categoryList));
		Mockito.when(sourceProduct.getSize()).thenReturn("90");
		Mockito.when(superCategory.getCode()).thenReturn("abc");
		Assert.assertTrue(gpSizeProductStrategy.getTarget(sourceProduct, reference).equals(sourceProduct));
		
	}

}

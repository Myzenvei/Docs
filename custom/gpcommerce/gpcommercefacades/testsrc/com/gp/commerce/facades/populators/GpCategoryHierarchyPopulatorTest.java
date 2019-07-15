/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.catalog.CatalogOption;
import de.hybris.platform.commercefacades.catalog.PageOption;
import de.hybris.platform.commercefacades.catalog.data.CategoryHierarchyData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.url.impl.AbstractUrlResolver;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

@UnitTest
public class GpCategoryHierarchyPopulatorTest {

	@Mock
	private CategoryModel source;
	@Mock
	private CategoryModel category;

	private CategoryHierarchyData target;

	private Collection<CatalogOption> options;

	@Mock
	private AbstractPopulatingConverter<ProductModel, ProductData> productConverter;
	@Mock
	private ProductService productService;
	@Mock
	private ProductModel productModel;
	@Mock
	private AbstractUrlResolver<CategoryModel> categoryUrlResolver;

	PageOption page=PageOption.createForPageNumberAndPageSize(0, 20);
	
	@InjectMocks
	GpCategoryHierarchyPopulator categoryHierarchyPopulator = new GpCategoryHierarchyPopulator();

	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);
		options = new ArrayList<>();
		target=new CategoryHierarchyData();
		target.setProducts(Collections.EMPTY_LIST);
		options.add(CatalogOption.PRODUCTS);
		options.add(CatalogOption.SUBCATEGORIES);
		categoryHierarchyPopulator.setCategoryUrlResolver(categoryUrlResolver);
		categoryHierarchyPopulator.setProductConverter(productConverter);
		categoryHierarchyPopulator.setProductService(productService);

		
		Mockito.when(category.getCode()).thenReturn("Code");
		Mockito.when(category.getName()).thenReturn("Name");
		Mockito.when(category.getBrochureLink()).thenReturn("Name");
		Mockito.when(category.getRequestTrialLink()).thenReturn("Name");
		Mockito.when(category.getRequestTrialText()).thenReturn("Name");
		Mockito.when(category.getLearnMoreLink()).thenReturn("Name");
		Mockito.when(category.getLearnMoreText()).thenReturn("Name");
		Mockito.when(category.getBrochureText()).thenReturn("Name");
		Mockito.when(category.getIsBrochureLinkExternal()).thenReturn(true);
		Mockito.when(category.getIsLearnMoreLinkExternal()).thenReturn(true);
		Mockito.when(category.getIsRequestTrialLinkExternal()).thenReturn(true);
		Mockito.when(category.getModifiedtime()).thenReturn(new Date());
		Mockito.when(category.getProducts()).thenReturn(Collections.singletonList(productModel));
		
		Mockito.when(categoryUrlResolver.resolve(source)).thenReturn("http://localhost:9090/urlResolver");
		Mockito.when(source.getCode()).thenReturn("Code");
		Mockito.when(source.getName()).thenReturn("Code");
		Mockito.when(source.getModifiedtime()).thenReturn(new Date());
		Mockito.when(source.getCategories()).thenReturn(Collections.singletonList(category));

		Mockito.when(productConverter.convert(productModel)).thenReturn(Mockito.mock(ProductData.class));
		Mockito.when(productService.getProductsForCategory(Mockito.eq(source), Mockito.anyInt(), Mockito.anyInt()))
				.thenReturn(Collections.singletonList(productModel));

	}

	@Test
	public void testPopulate() throws ConversionException {
		categoryHierarchyPopulator.populate(source, target, options,page);
		assertTrue(target.getId().equals("Code"));
		assertTrue(target.getId().equals("Code"));
	}

}

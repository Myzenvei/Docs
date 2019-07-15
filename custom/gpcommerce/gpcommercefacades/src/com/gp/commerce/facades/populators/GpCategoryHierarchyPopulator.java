/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.catalog.CatalogOption;
import de.hybris.platform.commercefacades.catalog.PageOption;
import de.hybris.platform.commercefacades.catalog.data.CategoryHierarchyData;
import de.hybris.platform.commercefacades.converter.PageablePopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.url.impl.AbstractUrlResolver;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class GpCategoryHierarchyPopulator implements PageablePopulator<CategoryModel, CategoryHierarchyData, CatalogOption>{
	
	
	private AbstractPopulatingConverter<ProductModel, ProductData> productConverter;
	private ProductService productService;
	private AbstractUrlResolver<CategoryModel> categoryUrlResolver;
	
	
	@Override
	public void populate(final CategoryModel source, final CategoryHierarchyData target,
			final Collection<? extends CatalogOption> options, final PageOption page) throws ConversionException
	{
		
		target.setId(source.getCode());
		target.setName(source.getName());
		target.setLastModified(source.getModifiedtime());
		target.setUrl(categoryUrlResolver.resolve(source));
		target.setProducts(new ArrayList<ProductData>());
		target.setSubcategories(new ArrayList<CategoryHierarchyData>());
		
		if (options.contains(CatalogOption.PRODUCTS))
		{
			final List<ProductModel> products = getProductService().getProductsForCategory(source, page.getPageStart(),
					page.getPageSize());
			for (final ProductModel product : products)
			{
				final ProductData productData = getProductConverter().convert(product);
				target.getProducts().add(productData);
			}
		}

		if (page.includeInformationAboutPages())
		{
			final Integer totalNumber = getProductService().getAllProductsCountForCategory(source);
			final Integer numberOfPages = Integer.valueOf((int) (Math.ceil(totalNumber.doubleValue() / page.getPageSize())));
			target.setTotalNumber(totalNumber);
			target.setCurrentPage(Integer.valueOf(page.getPageNumber()));
			target.setPageSize(Integer.valueOf(page.getPageSize()));
			target.setNumberOfPages(numberOfPages);
		}

		if (options.contains(CatalogOption.SUBCATEGORIES))
		{
			recursive(target, source, true, options);
		}

		target.setLearnMore(source.getLearnMoreLink());
		target.setBrochure(source.getBrochureLink());
		target.setRequestTrialLink(source.getRequestTrialLink());
		target.setRequestTrialText(source.getRequestTrialText());
		target.setIsBrochureLinkExternal(source.getIsBrochureLinkExternal());
		target.setIsLearnMoreLinkExternal(source.getIsLearnMoreLinkExternal());
		target.setIsRequestTrialLinkExternal(source.getIsRequestTrialLinkExternal());
		target.setOrder(source.getOrder());
	}
	
	protected void recursive(final CategoryHierarchyData categoryData2, final CategoryModel category, final boolean root,
			final Collection<? extends CatalogOption> options)
	{
		if (root)
		{
			for (final CategoryModel subc : category.getCategories())
			{
				recursive(categoryData2, subc, false, options);
			}
		}
		else
		{
			final CategoryHierarchyData categoryData = new CategoryHierarchyData();
			categoryData.setId(category.getCode());
			categoryData.setName(category.getName());
			categoryData.setLastModified(category.getModifiedtime());
			categoryData.setUrl(categoryUrlResolver.resolve(category));
			categoryData.setProducts(new ArrayList<ProductData>());
			categoryData.setSubcategories(new ArrayList<CategoryHierarchyData>());
			categoryData.setBrochure(category.getBrochureLink());
			categoryData.setRequestTrialLink(category.getRequestTrialLink());
			categoryData.setRequestTrialText(category.getRequestTrialText());
			categoryData.setLearnMore(category.getLearnMoreLink());
			categoryData.setLearnMoreText(category.getLearnMoreText());
			categoryData.setBrochureText(category.getBrochureText());
			categoryData.setIsBrochureLinkExternal(category.getIsBrochureLinkExternal());
			categoryData.setIsLearnMoreLinkExternal(category.getIsLearnMoreLinkExternal());
			categoryData.setIsRequestTrialLinkExternal(category.getIsRequestTrialLinkExternal());
			categoryData.setOrder(category.getOrder());
			
			if (options.contains(CatalogOption.PRODUCTS))
			{
				final List<ProductModel> products = category.getProducts();
				for (final ProductModel product : products)
				{
					final ProductData productData = getProductConverter().convert(product);
					categoryData.getProducts().add(productData);
				}
			}
			categoryData2.getSubcategories().add(categoryData);
			for (final CategoryModel subc : category.getCategories())
			{
				recursive(categoryData, subc, false, options);
			}
		}
	}

	public AbstractPopulatingConverter<ProductModel, ProductData> getProductConverter() {
		return productConverter;
	}

	public void setProductConverter(AbstractPopulatingConverter<ProductModel, ProductData> productConverter) {
		this.productConverter = productConverter;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public AbstractUrlResolver<CategoryModel> getCategoryUrlResolver() {
		return categoryUrlResolver;
	}

	public void setCategoryUrlResolver(AbstractUrlResolver<CategoryModel> categoryUrlResolver) {
		this.categoryUrlResolver = categoryUrlResolver;
	}

	
}

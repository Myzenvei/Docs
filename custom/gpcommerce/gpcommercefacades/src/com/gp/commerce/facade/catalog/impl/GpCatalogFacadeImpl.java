/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facade.catalog.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import com.gp.commerce.facade.catalog.GpCatalogFacade;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.catalog.CatalogOption;
import de.hybris.platform.commercefacades.catalog.PageOption;
import de.hybris.platform.commercefacades.catalog.data.CategoryHierarchyData;
import de.hybris.platform.commercefacades.catalog.impl.DefaultCatalogFacade;
import de.hybris.platform.commercefacades.converter.PageablePopulator;

public class GpCatalogFacadeImpl extends DefaultCatalogFacade implements GpCatalogFacade{

	private static final String SLASH = "/";
	private PageablePopulator<CategoryModel, CategoryHierarchyData, CatalogOption> gpCategoryHierarchyPopulator;
	
	@Override
	public CategoryHierarchyData getCategoryById(String catalogId, String catalogVersionId, String categoryId,
			PageOption page, Set<CatalogOption> opts) {
		final CategoryHierarchyData data = new CategoryHierarchyData();
		data.setUrl(catalogId + SLASH + catalogVersionId);

		final CatalogVersionModel catalogVersionModel = getProductCatalogVersionModelForBaseSite(catalogId, catalogVersionId);
		final CategoryModel category = getCategoryService().getCategoryForCode(catalogVersionModel, categoryId);

		getGpCategoryHierarchyPopulator().populate(category, data, opts, page);

		return data;
	}

	public PageablePopulator<CategoryModel, CategoryHierarchyData, CatalogOption> getGpCategoryHierarchyPopulator() {
		return gpCategoryHierarchyPopulator;
	}

	public void setGpCategoryHierarchyPopulator(
			PageablePopulator<CategoryModel, CategoryHierarchyData, CatalogOption> gpCategoryHierarchyPopulator) {
		this.gpCategoryHierarchyPopulator = gpCategoryHierarchyPopulator;
	}

	@Override
	public void sortCategoriesByOrder(CategoryHierarchyData categoryHierarchyData) {
		if(categoryHierarchyData.getSubcategories() != null && !categoryHierarchyData.getSubcategories().isEmpty())
		{
			Collections.sort(categoryHierarchyData.getSubcategories(), new Comparator<CategoryHierarchyData>(){
				  @Override
		            public int compare(final CategoryHierarchyData o1, final CategoryHierarchyData o2) {
		            	Integer l1 = o1.getOrder();
		            	Integer l2 = o2.getOrder();
		            	if(null!=l1 && null!=l2){
		            		return (l1.compareTo(l2));
		            	}
		            		return 0;
		            }
		        });
			
			for(CategoryHierarchyData categoryHeader:categoryHierarchyData.getSubcategories())
			{
				if(categoryHeader.getSubcategories()!=null && !categoryHeader.getSubcategories().isEmpty())
				{
					Collections.sort(categoryHeader.getSubcategories(), new Comparator<CategoryHierarchyData>() {
			            @Override
			            public int compare(final CategoryHierarchyData o1, final CategoryHierarchyData o2) {
			            	Integer l1 = o1.getOrder();
			            	Integer l2 = o2.getOrder();
			            	if(null!=l1 && null!=l2){
			            		return (l1.compareTo(l2));
			            	}
			            		return 0;
			            }
			        });
				}
			}
		}
	}

	
	
	

}

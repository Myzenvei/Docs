/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.distributorfeed.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.gp.commerce.core.distributorfeed.services.GPDistributorFeedService;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

/**
 * This class is for Distributor Feed Services
 */
public class GPDefaultDistributorFeedService implements GPDistributorFeedService{
	
	private ModelService modelService;
	private ProductService productService;
	@Resource(name = "catalogVersionService")
	private CatalogVersionService catalogVersionService;
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;
	private static final String RETRIEVE_PRODUCT_FOR_CODE = "SELECT {pk} from {Product} where {code} in (?code)";
	private static final String RETRIEVE_B2BUNIT_FOR_CODE = "SELECT {pk} from {B2BUnit} where {uid} in (?uid)";

	/* (non-Javadoc)
	 * @see com.gp.commerce.core.distributorfeed.services.GPDistributorFeedService#fetchDistributorFeed()
	 */
	@Override
	public void fetchDistributorFeed() {
		//<to-do>
		List<String> sku1 = new ArrayList<String>();
		List<String> sku2 = new ArrayList<String>();
		sku1.add("3387");
		sku1.add("4872");
		sku2.add("3387");
		sku2.add("4872");
		Map<String,List<String>> distributorFeed = new HashMap<String,List<String>>();
		distributorFeed.put("UX9PATH", sku1);
		distributorFeed.put("SXP9PATH", sku2);
		persistSoldTos(distributorFeed);
	}

	/**
	 * @param distributorFeed
	 */
	private void persistSoldTos(Map<String, List<String>> distributorFeed) {
		List<String> prodCode = new ArrayList<>();
		distributorFeed.entrySet().forEach(entry->{
			prodCode.add(entry.getKey());
		});
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(RETRIEVE_PRODUCT_FOR_CODE);
		final Map<String, Object> params = new HashMap<>();
		params.put("code", prodCode);
		searchQuery.addQueryParameters(params);
		List<ProductModel> products = flexibleSearchService.<ProductModel>search(searchQuery).getResult();
		
		for(ProductModel prodModel:products) {
			distributorFeed.entrySet().forEach(entry->{
				if(prodModel.getCode().equals(entry.getKey())) {
					saveDistributorsId(entry.getValue(), prodModel);
				}
			});
		}
	}
	
	/**
	 * @param distributorIdsForProduct
	 * @param prodModel 
	 */
	private void saveDistributorsId(List<String> distributorIdsForProduct, ProductModel prodModel) {
		
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(RETRIEVE_B2BUNIT_FOR_CODE);
		final Map<String, Object> params = new HashMap<>();
		params.put("uid", distributorIdsForProduct);
		searchQuery.addQueryParameters(params);
		List<B2BUnitModel> b2bUnit = flexibleSearchService.<B2BUnitModel>search(searchQuery).getResult();
		prodModel.setDistributorIds(b2bUnit);
		modelService.save(prodModel);
	}
	/**
	 * @return modelService
	 */
	public ModelService getModelService() {
		return modelService;
	}

	/**
	 * @param modelService
	 */
	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	/**
	 * @return productService
	 */
	public ProductService getProductService() {
		return productService;
	}

	/**
	 * @param productService
	 */
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

}

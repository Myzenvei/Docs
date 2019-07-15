/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.stock.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.stock.impl.DefaultStockLevelDao;


public class DefaultGPStockLevelDao extends DefaultStockLevelDao {

	@Resource(name="productDao")
	private ProductDao productDao;
	
	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;

	private static final String FETCH_STOCKLEVEL = "select {s.pk} from {GPKitComponentsRln as rln join GPProductKit as kit on {kit.pk}={rln.source} join GPComponentProducts as comp on {comp.pk}={rln.target} join Product as prod on {prod.pk}={comp.kitcomponent}join StockLevel as s on {s.productCode}={prod.code}} where {kit.code}=?productCode ";
	
	
	private static final String FETCH_VARIANT_STOCKLEVEL = "select {s.pk} from {GPKitVrtComponentRln as rln join GPVariantProductKit as kit on {kit.pk}={rln.source} join GPComponentProducts as comp on {comp.pk}={rln.target} join Product as prod on {prod.pk}={comp.kitcomponent}join StockLevel as s on {s.productCode}={prod.code}} where {kit.code}=?productCode ";
	
	
	private static final String PREORDERQUANTITY = "and {maxPreOrder} >= ?maxPreOrder ";

	private static final String ORDERBY = "order by ({s.available}-{s.reserved})/{comp.quantity}";
	
	private static final Logger LOG = Logger.getLogger(DefaultGPStockLevelDao.class);

	@Override
	public Collection<StockLevelModel> findStockLevels(String productCode, Collection<WarehouseModel> warehouses) {

		if (checkIfKitsProduct(productCode)) {
			LOG.debug("Type is GPProductKit");
			return findStockLevelsImpl(productCode, warehouses, (Integer) null);
		} else if(checkIfKitsVariantProduct(productCode)) {
			return findKitVariantStockLevelsImpl(productCode, warehouses, (Integer) null);
		}
		else {
			return super.findStockLevels(productCode, warehouses);
		}
	}


	@Override
	public Collection<StockLevelModel> findStockLevels(String productCode, Collection<WarehouseModel> warehouses,
			int preOrderQuantity) {

		if (checkIfKitsProduct(productCode)) {
			return findStockLevelsImpl(productCode, warehouses, (Integer) preOrderQuantity);
		} else if (checkIfKitsVariantProduct(productCode)) {
			return findKitVariantStockLevelsImpl(productCode, warehouses, (Integer) preOrderQuantity);
		} else {
			return super.findStockLevels(productCode, warehouses, preOrderQuantity);
		}
	}

	private Collection<StockLevelModel> findStockLevelsImpl(String productCode, Collection<WarehouseModel> warehouses,
			Integer preOrderQuantity) {
		checkProductCode(productCode);
		String queryString;
		List filteredWarehouses = filterWarehouses(warehouses);
		if (filteredWarehouses.isEmpty()) {
			return Collections.emptyList();
		} else {

			if (null == preOrderQuantity) {
				queryString = FETCH_STOCKLEVEL + ORDERBY;
			} else {

				queryString = FETCH_STOCKLEVEL + PREORDERQUANTITY + ORDERBY;
			}

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			
			query.addQueryParameter("productCode", productCode);

			if (preOrderQuantity != null) {
				query.addQueryParameter("maxPreOrder", preOrderQuantity);
			}
			LOG.debug("Query to find stocklevels:"+query);
			searchRestrictionService.disableSearchRestrictions();
			SearchResult<StockLevelModel> result = getFlexibleSearchService().search(query);
			searchRestrictionService.enableSearchRestrictions();
			
			List<StockLevelModel> stock = new ArrayList<>();
			
			if(CollectionUtils.isNotEmpty(result.getResult()))
			{
				stock.add(result.getResult().get(0));
				return stock;
			}
			else
			{
				return null;
			}
			
		}
	}
	
	
	private Collection<StockLevelModel> findKitVariantStockLevelsImpl(String productCode,
			Collection<WarehouseModel> warehouses, Integer preOrderQuantity) {

		checkProductCode(productCode);
		String queryString;
		List filteredWarehouses = filterWarehouses(warehouses);
		if (filteredWarehouses.isEmpty()) {
			return Collections.emptyList();
		} else {

			if (null == preOrderQuantity) {
				queryString = FETCH_VARIANT_STOCKLEVEL + ORDERBY;
			} else {

				queryString = FETCH_VARIANT_STOCKLEVEL + PREORDERQUANTITY + ORDERBY;
			}

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			
			query.addQueryParameter("productCode", productCode);

			if (preOrderQuantity != null) {
				query.addQueryParameter("maxPreOrder", preOrderQuantity);
			}
			LOG.debug("Query to find stocklevels:"+query);
			searchRestrictionService.disableSearchRestrictions();
			SearchResult<StockLevelModel> result = getFlexibleSearchService().search(query);
			searchRestrictionService.enableSearchRestrictions();
			List<StockLevelModel> stock = new ArrayList<>();
			
			if(CollectionUtils.isNotEmpty(result.getResult()))
			{
				stock.add(result.getResult().get(0));
				return stock;
			}
			else
			{
				return null;
			}
			
		}
	
	}

	private void checkProductCode(String productCode) {
		if (productCode == null) {
			throw new IllegalArgumentException("product code cannot be null.");
		}
	}

	private List<WarehouseModel> filterWarehouses(Collection<WarehouseModel> warehouses) {
		if (warehouses == null) {
			throw new IllegalArgumentException("warehouses cannot be null.");
		} else {
			HashSet result = new HashSet();
			Iterator arg3 = warehouses.iterator();

			while (arg3.hasNext()) {
				WarehouseModel house = (WarehouseModel) arg3.next();
				if (house != null) {
					result.add(house);
				}
			}

			return new ArrayList(result);
		}

	}
	
	private boolean checkIfKitsProduct(String productCode) {

		final List<ProductModel> products = productDao.findProductsByCode(productCode);

		return CollectionUtils.isNotEmpty(products) && "GPProductKit".equalsIgnoreCase(products.get(0).getItemtype());

	}
	
	private boolean checkIfKitsVariantProduct(String productCode) {
		
		final List<ProductModel> products = productDao.findProductsByCode(productCode);

		return CollectionUtils.isNotEmpty(products) && "GPVariantProductKit".equalsIgnoreCase(products.get(0).getItemtype());

	}

}






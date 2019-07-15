/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2012 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.multicountry.productcockpit.services.impl;

import de.hybris.platform.cockpit.model.dynamicquery.DynamicQuery;
import de.hybris.platform.cockpit.model.dynamicquery.impl.DynamicQueryImpl;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.productcockpit.services.catalog.impl.DynamicQueryServiceImpl;

import java.util.HashMap;
import java.util.List;

import org.zkoss.util.resource.Labels;


/**
 * Add two new dynamic queries to the default implementation: one for getting all products locked by the current user,
 * and one for getting all locked products.
 */
public class MulticountryDynamicQueryServiceImpl extends DynamicQueryServiceImpl
{
	//Labels to show at the front-end box
	final private static String MY_LOCKED_PRODUCTS_LABEL = "multicountry.query.mylockedproducts";
	final private static String ALL_LOCKED_PRODUCTS_LABEL = "multicountry.query.alllockedproducts";


	//Flexible search queries and params
	final private static String CATALOG_VERSIONS_PARAM = "catalogVersions";
	final private static String USER_PARAM = "user";

	final private static String ALL_LOCKED_PRODUCTS_QUERY = " SELECT {p:pk} FROM {Product AS p} "
			+ " WHERE {p:catalogVersion} IN (?" + CATALOG_VERSIONS_PARAM
			+ ") AND EXISTS ({{SELECT 1 FROM  {Employee2LockedProductsRel AS lp} WHERE {lp:target}={p:pk} }}) ";

	final private static String MY_LOCKED_PRODUCTS_QUERY = " SELECT {p:pk} FROM {Product AS p} WHERE {p:catalogVersion} IN (?"
			+ CATALOG_VERSIONS_PARAM
			+ ") AND  EXISTS ({{SELECT 1 FROM  {Employee2LockedProductsRel AS lp} WHERE {lp:target}={p:pk} AND {lp:source}=(?"
			+ USER_PARAM + ") }})";



	@Override
	public List<DynamicQuery> getAllDynamicQuery()
	{
		//Get the default dynamic queries.
		final List<DynamicQuery> allDynamicQuery = super.getAllDynamicQuery();


		//Add all-locked-products query.
		allDynamicQuery.add(new DynamicQueryImpl(Labels.getLabel(ALL_LOCKED_PRODUCTS_LABEL), ALL_LOCKED_PRODUCTS_QUERY,
				new HashMap<String, Object>()));


		//Add my-locked-products query. 
		final HashMap params = new HashMap<String, Object>();
		params.put(USER_PARAM, UISessionUtils.getCurrentSession().getUser().getPk());
		allDynamicQuery.add(new DynamicQueryImpl(Labels.getLabel(MY_LOCKED_PRODUCTS_LABEL), MY_LOCKED_PRODUCTS_QUERY, params));


		return allDynamicQuery;
	}



}

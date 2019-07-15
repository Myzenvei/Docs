/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.savedvalus.dao.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.hmc.model.SavedValuesModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gp.commerce.core.savedvalus.dao.GPSavedValuesDao;


public class GPDefaultSavedValuesDao extends AbstractItemDao implements GPSavedValuesDao
{

	private static final String MODIFIED_ITEM_MODEL = "modifiedItemModel";

	/**
	 * This method returns the list of saved instances or changed values at different instances for the specified item
	 * Model.
	 *
	 * @param itemModel
	 *           the item model for which the changed value list is needed
	 * @return the list of saved values
	 */
	@Override
	public List<SavedValuesModel> getChangedLogs(final ItemModel itemModel)
	{
		final String queryString = "select {pk} from {SavedValues} where {modifiedItem} = ?modifiedItemModel ORDER BY {timestamp} DESC ";
		final Map<String, Object> params = new HashMap<>();
		params.put(MODIFIED_ITEM_MODEL, itemModel);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.getQueryParameters().putAll(params);
		final SearchResult<SavedValuesModel> searchResult = getFlexibleSearchService().search(query);
		return searchResult.getResult();
	}

}

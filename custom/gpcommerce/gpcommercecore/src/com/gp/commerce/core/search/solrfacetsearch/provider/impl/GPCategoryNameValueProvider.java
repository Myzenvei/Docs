/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

public class GPCategoryNameValueProvider extends GPCategoryCodeValueProvider {
	@Override
	protected Object getPropertyValue(final Object model)
	{
		return getPropertyValue(model, "name");
	}
}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.content.search.facetdata;

import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;


public class GPContentSearchPageData<STATE, RESULT> extends FacetSearchPageData<STATE, RESULT>
{

	private String textSearch;

	public GPContentSearchPageData()
	{
		// default constructor
	}



	public void setTextSearch(final String textSearch)
	{
		this.textSearch = textSearch;
	}



	public String getTextSearch()
	{
		return textSearch;
	}

}

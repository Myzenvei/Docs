/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.dao.GPCartDao;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import com.gp.commerce.core.model.GPEndUserLegalTermsModel;
import com.gp.commerce.core.model.ShippingRestrictionModel;

public class GPDefaultCartDao implements  GPCartDao{
	
	private static final String LEGAL_LANGUAGE = "legalLanguage";
	private static final String RETRIEVE_SHIPPING_RESTRICTIONS= "select {pk} from {ShippingRestriction} where {productCode} = ?productCode AND {countryIsoCode} = ?countryCode  AND {region} = ?region OR {productCode} = ?productCode AND"
			+ "{countryIsoCode} = ?countryCode AND {region} = '-1' OR {productCode} = ?productCode AND {countryIsoCode} = '-1' AND {region} = '-1'";	
	private static final String GET_LEASE_DATA = "SELECT {pk} from {GPEndUserLegalTerms} where {country} = ?country AND {legalLanguage} = ?legalLanguage order by {version} desc";
	private static final String CANADA_FRENCH = "CA_fr";
	private static final String CANADA_ENGLISH = "CA_en";
	private static final String US_ENGLISH = "US_en";
	
	protected final static String SELECTCLAUSE = "SELECT {" + CartModel.PK + "} FROM {" + CartModel._TYPECODE + "} ";
	protected final static String ORDERBYCLAUSE = " ORDER BY {" + CartModel.MODIFIEDTIME + "} DESC";
	protected final static String FIND_CART_FOR_CODE_AND_SITE = SELECTCLAUSE + "WHERE {" + CartModel.SITE + "} = ?site AND {"
			+ CartModel.CODE + "} = ?code " + ORDERBYCLAUSE;
	
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	
	private FlexibleSearchService flexibleSearchService;


	public List<ShippingRestrictionModel>  fetchShippingRestrictions(String productCode, String countryCode,String region){
		
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(RETRIEVE_SHIPPING_RESTRICTIONS);
		final Map<String, Object> params = new HashMap<>();
		params.put(GpcommerceCoreConstants.PRODUCT_CODE, productCode);
		params.put(GpcommerceCoreConstants.COUNTRY_CODE, countryCode);
		params.put(GpcommerceCoreConstants.REGION, region);
		searchQuery.addQueryParameters(params);
		final SearchResult<ShippingRestrictionModel> searchResult = getFlexibleSearchService().search(searchQuery);
		if (searchResult.getResult().isEmpty()) {
			return Collections.emptyList();
		}
		return searchResult.getResult();
		
	}
	

	@Override
	public List<GPEndUserLegalTermsModel> getLeaseAgreementForCountry(String country) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(CANADA_FRENCH.equalsIgnoreCase(country)) {
	        params.put(GpcommerceCoreConstants.COUNTRY, country.split("_")[0]);
	        params.put(LEGAL_LANGUAGE, configurationService.getConfiguration().getString("legal.agreement.language.canada.french"));
		}
		else if(CANADA_ENGLISH.equalsIgnoreCase(country)) {
	        params.put(GpcommerceCoreConstants.COUNTRY, country.split("_")[0]);
	        params.put(LEGAL_LANGUAGE, configurationService.getConfiguration().getString("legal.agreement.language.canada.english"));
		}
		else if(US_ENGLISH.equalsIgnoreCase(country)){
	        params.put(GpcommerceCoreConstants.COUNTRY, country.split("_")[0]);
	        params.put(LEGAL_LANGUAGE, configurationService.getConfiguration().getString("legal.agreement.language.us.english"));
		}
        
        final FlexibleSearchQuery flexiQry = new FlexibleSearchQuery(GET_LEASE_DATA);
        flexiQry.getQueryParameters().putAll(params); 
        
        return getFlexibleSearchService().<GPEndUserLegalTermsModel>search(flexiQry).getResult();
		
	}
	
	
	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}


	@Override
	public CartModel getCartForCodeAndSite(String code, BaseSiteModel currentBaseSite) {
		if (code != null)
			{
				final Map<String, Object> params = new HashMap<>();
				params.put("code", code);
				params.put("site", currentBaseSite);
				final List<CartModel> carts = doSearch(FIND_CART_FOR_CODE_AND_SITE, params, CartModel.class, 1);
				if (carts != null && !carts.isEmpty())
				{
					return carts.get(0);
				}
				return null;
			}
		return null;
	}
		
		protected <T> List<T> doSearch(final String query, final Map<String, Object> params, final Class<T> resultClass,
				final int count)
		{
			final FlexibleSearchQuery fQuery = createSearchQuery(query, params, resultClass);
			fQuery.setCount(count);
			final SearchResult<T> searchResult = flexibleSearchService.search(fQuery);
			return searchResult.getResult();
		}

		protected <T> FlexibleSearchQuery createSearchQuery(final String query, final Map<String, Object> params,
				final Class<T> resultClass)
		{
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			if (params != null)
			{
				fQuery.addQueryParameters(params);
			}
			fQuery.setResultClassList(Collections.singletonList(resultClass));
			return fQuery;
		}

}
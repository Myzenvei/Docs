/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.user.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.daos.impl.DefaultUserDao;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.GPApprovalEnum;
import com.gp.commerce.core.model.MarketingPreferenceModel;
import com.gp.commerce.core.model.MarketingPreferenceTypeModel;
import com.gp.commerce.core.user.dao.GPUserDao;


public class GPDefaultUserDao extends DefaultUserDao implements GPUserDao
{

	private static final String APPROVAL_STATUS = "approvalStatus";
	private static final String APPROVAL_STATUSES = "approvalStatuses";
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;
	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;

	private static final String FIND_USERS_BY_CODES = "SELECT {b2BCustomer:pk} FROM {B2BCustomer as b2BCustomer} "
			+ "where {b2BCustomer:uid} in (?uids)";
	private static final String FIND_DISTINCT_MARKETING_PREF = "SELECT DISTINCT{PK} FROM {MarketingPreferenceType} WHERE {site}=?site";
	
	private static final String FIND_USER_FOR_FB_ID = "SELECT {Customer:originalUid} FROM {Customer as Customer} WHERE {fbUniqueId}=?fbUniqueId";
	
	private static final String CMS_SITE = "cmsSite";
	private static final String FB_UNIQUE_ID = "fbUniqueId";
	private static final String MARKETING_TYPE = "marketingType";
	private static final String FIND_ADDRESSES_BY_STATUS = "SELECT {address.pk} FROM {Address as address} where {address.approvalStatus} in (?approvalStatuses)";
	private static final String FIND_ADDRESSES_B2B_TIME = "SELECT {address.pk} FROM {Address as address join B2BUnit as unit on {address.b2bunit} = {unit.pk} join B2BCustomer as customer on {address.owner} = {customer.pk} join CMSSite as site on {site.pk} = {customer.site}} where {unit.b2bUnitLevel} != ?unitLevel AND {site.pk} = ?cmsSite";
	private static final String FIND_CUSTOMERS_B2B_TIME = "SELECT {customer.pk} FROM {B2BCustomer as customer join B2BUnit as unit on {customer.defaultB2BUnit} = {unit.pk} join CMSSite as site on {customer.site} = {site.pk}} where {unit.b2bUnitLevel} != ?unitLevel AND {site.pk} = ?cmsSite";
	private static final String FIND_NEW_CUSTOMERS_B2B = "SELECT {customer.pk} FROM {B2BCustomer as customer join CMSSite as site on {customer.site} = {site.pk}} where {site.pk} = ?cmsSite AND {customer.registrationEmailSent} = 0";
	private static final String UNIT_LEVEL = "unitLevel";
	private static final String MODIFIED_TIME = "lastModifiedTime";


	@Override
	public List<MarketingPreferenceModel> getMarketingPreferencesForSite(final CMSSiteModel site)
	{
		validateParameterNotNull(site, "No Cms Site specified");

		final StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT {mp:").append(MarketingPreferenceModel.PK).append("}");
		queryString.append(" FROM {").append(MarketingPreferenceModel._TYPECODE).append(" AS mp}");
		queryString.append(" WHERE {mp:").append(MarketingPreferenceModel.SITE).append("}").append(" = ?").append(CMS_SITE);
		final Map params = new HashMap();
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(queryString.toString());
		params.put(CMS_SITE, site);
		searchQuery.addQueryParameters(params);

		final SearchResult searchResult = getFlexibleSearchService().search(searchQuery);
		return searchResult.getResult();
	}

	@Override
	public List<MarketingPreferenceTypeModel> getDistMarketingPreferences(final CMSSiteModel site)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_DISTINCT_MARKETING_PREF);
		query.addQueryParameter(GpcommerceCoreConstants.SITE, site);
		final List<MarketingPreferenceTypeModel> result =  getFlexibleSearchService().<MarketingPreferenceTypeModel>search(query).getResult();

		return CollectionUtils.isEmpty(result) ? Collections.emptyList() : result;
	}
	
	@Override
	public String getUserForFBUniqueId(final String fbUniqueId)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_USER_FOR_FB_ID);
		query.addQueryParameter(FB_UNIQUE_ID, fbUniqueId);
		query.setResultClassList(Collections.singletonList(String.class));

		final SearchResult<String> result = getFlexibleSearchService().search(query);
		return CollectionUtils.isNotEmpty((Collection) result.getResult())?result.getResult().get(0):null;
	}


	@Override
	public List<B2BCustomerModel> findUsersByCodes(final List<String> uids)
	{

		final Map<String, Object> attr = new HashMap<>(1);
		attr.put(GpcommerceCoreConstants.UIDS, uids);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_USERS_BY_CODES);
		query.getQueryParameters().putAll(attr);

		searchRestrictionService.disableSearchRestrictions();

		final List<B2BCustomerModel> b2bCustomers = getFlexibleSearchService().<B2BCustomerModel> search(query).getResult();
		searchRestrictionService.enableSearchRestrictions();

		return CollectionUtils.isEmpty(b2bCustomers) ? Collections.emptyList() : b2bCustomers;
	}

	@Override
	public List<AddressModel> getAddressesForB2BUser(final List<B2BUnitModel> units,final Boolean fetchActiveAddresses)
	{
		final StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT {address.").append(AddressModel.PK).append("}");
		queryString.append(" FROM {").append(AddressModel._TYPECODE).append(" AS address JOIN ")
				.append(B2BUnitModel._TYPECODE);
		queryString.append(" AS unit ON {address.").append(AddressModel.B2BUNIT).append("}={unit.")
				.append(B2BUnitModel.PK).append("}}");
		queryString.append(" WHERE {unit.").append(B2BUnitModel.PK).append("} in (?units)");
		if (fetchActiveAddresses) {
			queryString.append(" AND {address.").append(AddressModel.APPROVALSTATUS).append("}=?approvalStatus");
		}

		final Map params = new HashMap();
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(queryString.toString());
		params.put(GpcommerceCoreConstants.UNITS, units);
		if (fetchActiveAddresses) {
			params.put(APPROVAL_STATUS, GPApprovalEnum.ACTIVE);
		}
		searchQuery.addQueryParameters(params);
		final SearchResult searchResult = getFlexibleSearchService().search(searchQuery);
		return searchResult.getResult();
	}

	@Override
	public List<MarketingPreferenceModel> getMarketingPreferencesForSiteAndType(final CMSSiteModel site,final MarketingPreferenceTypeModel markPrefType)
	{
		validateParameterNotNull(site, "No Cms Site specified");

		final StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT {mp:").append(MarketingPreferenceModel.PK).append("}");
		queryString.append(" FROM {").append(MarketingPreferenceModel._TYPECODE).append(" AS mp}");
		queryString.append(" WHERE {mp:").append(MarketingPreferenceModel.SITE).append("}").append(" = ?").append(CMS_SITE);
		queryString.append(" AND {mp:").append(MarketingPreferenceModel.MARKETINGPREFERENCETYPEID).append("}").append(" = ?").append(MARKETING_TYPE);
		final Map params = new HashMap();
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(queryString.toString());
		params.put(CMS_SITE, site);
		params.put(MARKETING_TYPE, markPrefType);
		searchQuery.addQueryParameters(params);

		final SearchResult searchResult = getFlexibleSearchService().search(searchQuery);
		return searchResult.getResult();
	}
	/**
	 * get all addresses in rejected status
	 *
	 * @param List<GPApprovalEnum>
	 *          approvalStatuses
	 *
	 * returns
	 *   	List<AddressModel>
	 */

	@Override
	public List<AddressModel> getAllAddressesOnStatus(final List<GPApprovalEnum> approvalStatuses)
	{
		final Map params = new HashMap();
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(FIND_ADDRESSES_BY_STATUS);
		params.put(APPROVAL_STATUSES, approvalStatuses);
		searchQuery.addQueryParameters(params);

		final SearchResult searchResult = getFlexibleSearchService().search(searchQuery);
		return searchResult.getResult();
	}

	@Override
	public List<AddressModel> getAllAddressesForB2B(final Date lastModifiedTime, final CMSSiteModel site)
	{
		String query = FIND_ADDRESSES_B2B_TIME;
		if (lastModifiedTime != null)
		{
			query = query + " AND  {address:modifiedtime} >= ?lastModifiedTime";
		}
		final Map<String, Object> params = new HashMap<>();
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
		params.put(UNIT_LEVEL, GpcommerceCoreConstants.B2B_UNIT_L1);
		if (lastModifiedTime != null)
		{
			params.put(MODIFIED_TIME, lastModifiedTime);
		}
		params.put(CMS_SITE, site);
		searchQuery.addQueryParameters(params);
		final List<AddressModel> result = getFlexibleSearchService().<AddressModel> search(searchQuery).getResult();
		return CollectionUtils.isNotEmpty(result) ? result : Collections.emptyList();
	}

	public List<B2BCustomerModel> getAllCustomersForB2B(final Date lastModifiedTime, final CMSSiteModel site)
	{
		String query = FIND_CUSTOMERS_B2B_TIME;
		if (lastModifiedTime != null)
		{
			query = query + " AND  {customer:modifiedtime} >= ?lastModifiedTime";
		}
		final Map<String, Object> params = new HashMap<>();
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
		params.put(UNIT_LEVEL, GpcommerceCoreConstants.B2B_UNIT_L1);
		if (lastModifiedTime != null)
		{
			params.put(MODIFIED_TIME, lastModifiedTime);
		}
		params.put(CMS_SITE, site);
		searchQuery.addQueryParameters(params);
		final List<B2BCustomerModel> result = getFlexibleSearchService().<B2BCustomerModel> search(searchQuery).getResult();
		return CollectionUtils.isNotEmpty(result) ? result : Collections.emptyList();
	}

	@Override
	public List<B2BCustomerModel> getAllNewCustomersForB2B(final CMSSiteModel site)
	{
		final String query = FIND_NEW_CUSTOMERS_B2B;
		final Map<String, Object> params = new HashMap<>();
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
		params.put(CMS_SITE, site);
		searchQuery.addQueryParameters(params);
		final List<B2BCustomerModel> result = getFlexibleSearchService().<B2BCustomerModel> search(searchQuery).getResult();
		return CollectionUtils.isNotEmpty(result) ? result : Collections.emptyList();
	}
}

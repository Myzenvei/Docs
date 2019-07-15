/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.customer.dao.impl.DefaultCustomerAccountDao;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.core.dao.GPB2BCustomerAccountDao;
import com.gp.commerce.core.util.GPFunctions;

public class GPB2BCustomerAccountDaoImpl  extends DefaultCustomerAccountDao implements GPB2BCustomerAccountDao  {

	private static final String SELECTED_START_DATE = "selectedStartDate";

	private static final String SELECTED_END_DATE = "selectedEndDate";

	private static final String CODE = "code";

	private static final String CUSTOMER = "customer";

	private static final String STORE = "store";

	private static final String B2B_UNIT = "b2bUnit";

	private static final String FILTER_STATUS_LIST = "filterStatusList";

	private static final String STATUS_LIST = "statusList";

	private static final String SORT_BY_DATE = "byDate";

	private static final String SORT_BY_ORDER_NUMBER = "byOrderNumber";

	private static final String SORT_BY_STATUS = "byStatus";

	private static final String SORT_BY_TOTAL_AMOUNT = "byTotalAmount";

	@Resource
	private UserService userService;

	@Resource
	private SessionService sessionService;

	@Resource
	private SearchRestrictionService searchRestrictionService;


	private static final Logger LOG = Logger.getLogger(GPB2BCustomerAccountDaoImpl.class);

	private static final String FIND_ORDERS_BY_CUSTOMER_UNIT_QUERY = "SELECT {" + OrderModel.PK + "}, {"
			+ OrderModel.CREATIONTIME + "}, {" + OrderModel.CODE + "} FROM {" + OrderModel._TYPECODE + " AS O JOIN "
			+ B2BUnitModel._TYPECODE +" AS BU ON {O.UNIT}={BU.PK}} WHERE {" + OrderModel.USER
			+ "} = ?customer AND {" + OrderModel.VERSIONID + "} IS NULL AND {" + OrderModel.STORE + "} = ?store"
			+ "  AND {" +OrderModel.CREATIONTIME +"}  BETWEEN   ?selectedStartDate AND  ?selectedEndDate  AND {BU.PK} in(?b2bUnit) " ;

	private static final String FIND_ORDERS_BY_CUSTOMER_CODE_STORE_QUERY = "SELECT {" + OrderModel.PK + "}, {"
			+ OrderModel.CREATIONTIME + "}, {" + OrderModel.CODE + "} FROM {" + OrderModel._TYPECODE + "} WHERE {" + OrderModel.CODE
			+ "} = ?code AND {" + OrderModel.VERSIONID + "} IS NULL AND {" + OrderModel.USER + "} = ?customer AND {"
			+ OrderModel.STORE + "} = ?store";

	private static final String FIND_ORDERS_BY_UNIT_QUERY = "SELECT {" + OrderModel.PK + "}, {"
			+ OrderModel.CREATIONTIME + "}, {" + OrderModel.CODE + "} FROM {" + OrderModel._TYPECODE + " AS O JOIN "
			+ B2BUnitModel._TYPECODE +" AS BU ON {O.UNIT}={BU.PK}} WHERE {"  + OrderModel.VERSIONID + "} IS NULL AND {" + OrderModel.STORE + "} = ?store"
			+ "  AND {" +OrderModel.CREATIONTIME +"} BETWEEN  ?selectedStartDate  AND  ?selectedEndDate AND {BU.PK} in (?b2bUnit) " ;

	private static final String FIND_ORDERS_BY_CUSTOMER_UNIT_QUERY_AND_STATUS = FIND_ORDERS_BY_CUSTOMER_UNIT_QUERY + " AND {"
			+ OrderModel.STATUS + "} IN (?statusList)";


	private static final String FIND_ORDERS_BY_CUSTOMER_UNIT_QUERY_AND_STATUS_ADMIN = FIND_ORDERS_BY_UNIT_QUERY + " AND {"
			+ OrderModel.STATUS + "} IN (?statusList)";

	private static final String FILTER_ORDER_STATUS = " AND {" + OrderModel.STATUS + "} NOT IN (?filterStatusList)";

	private static final String SORT_ORDERS_BY_DATE = " ORDER BY {" + OrderModel.CREATIONTIME + "} DESC, {" + OrderModel.PK + "}";

	private static final String SORT_ORDERS_BY_CODE = " ORDER BY {" + OrderModel.CODE + "},{" + OrderModel.CREATIONTIME
			+ "} DESC, {" + OrderModel.PK + "}";

	private static final String SORT_ORDERS_BY_STATUS = " ORDER BY {" + OrderModel.STATUS
			+ "} DESC, {" + OrderModel.PK + "}";

	private static final String SORT_ORDERS_BY_TOTALAMOUNT = " ORDER BY {" + OrderModel.TOTALPRICE
			+ "} DESC, {" + OrderModel.PK + "}";

	private static final String FIND_ORDERS_FOR_ADMIN = "SELECT {" + OrderModel.PK + "}, {"
			+ OrderModel.CREATIONTIME + "}, {" + OrderModel.CODE + "} FROM {" + OrderModel._TYPECODE + " AS O JOIN "
			+ B2BUnitModel._TYPECODE +" AS BU ON {O.UNIT}={BU.PK} JOIN Ord2B2BPermission as rln on {O.pk}={rln.source}} WHERE {"  + OrderModel.VERSIONID + "} IS NULL AND {" + OrderModel.STORE + "} = ?store"
			+ "  AND {BU.PK} in (?b2bUnit) " ;

	private static final String FIND_ORDERS_FOR_STATUS_ADMIN = FIND_ORDERS_FOR_ADMIN + " AND {"
			+ OrderModel.STATUS + "} IN (?statusList)";
	String orderQuery =null ;

	@Override
	public SearchPageData<OrderModel> findOrdersByCustomerAndUnit(final CustomerModel customerModel,
			final BaseStoreModel store, final OrderStatus[] status, final PageableData pageableData, final List<B2BUnitModel> b2bUnit) {
		
		String startDate = GPFunctions.getStartDateRange(pageableData.getStartDate());
		String endDate= GPFunctions.getEndDateRange(pageableData.getEndDate());
		validateParameterNotNull(customerModel, "Customer must not be null");
		validateParameterNotNull(store, "Store must not be null");

		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put(CUSTOMER, customerModel);
		queryParams.put(STORE, store);
		queryParams.put(SELECTED_START_DATE,startDate) ;
		if(LOG.isDebugEnabled()){
			LOG.debug("StartDate for B2BOrderHistory :"+ startDate);
		}
		queryParams.put(SELECTED_END_DATE,endDate) ;
		if(LOG.isDebugEnabled()){
			LOG.debug("EndDate for B2BOrderHistory :"+ endDate);
		}
		queryParams.put(B2B_UNIT,b2bUnit);

		String filterClause = StringUtils.EMPTY;
		if (CollectionUtils.isNotEmpty(getFilterOrderStatusList()))
		{
			queryParams.put(FILTER_STATUS_LIST, getFilterOrderStatusList());
			filterClause = FILTER_ORDER_STATUS;
		}

		final List<SortQueryData> sortQueries;
		final Set<PrincipalGroupModel> userGroups =customerModel.getGroups();
		final PrincipalGroupModel principalgroup =userGroups.stream().filter(usergroup->usergroup.getUid().equals(B2BConstants.B2BADMINGROUP)).findFirst().orElse(null) ;
		if (ArrayUtils.isNotEmpty(status))
		{
			if(null!=principalgroup) {
				 orderQuery = FIND_ORDERS_BY_CUSTOMER_UNIT_QUERY_AND_STATUS_ADMIN ;
			}else {
				 orderQuery =FIND_ORDERS_BY_CUSTOMER_UNIT_QUERY_AND_STATUS ;
			}
			queryParams.put(STATUS_LIST, Arrays.asList(status));
			sortQueries = Arrays.asList(createSortQueryData(SORT_BY_DATE, createQuery(orderQuery, filterClause, SORT_ORDERS_BY_DATE)),
					createSortQueryData(SORT_BY_ORDER_NUMBER, createQuery(orderQuery, filterClause, SORT_ORDERS_BY_CODE)),
					createSortQueryData(SORT_BY_STATUS, createQuery(orderQuery, filterClause, SORT_ORDERS_BY_STATUS)));
		}
		else
		{
			if(null!=principalgroup) {
				 orderQuery = FIND_ORDERS_BY_UNIT_QUERY ;
			}else {
				 orderQuery =FIND_ORDERS_BY_CUSTOMER_UNIT_QUERY ;
			}
			sortQueries = Arrays.asList(createSortQueryData(SORT_BY_DATE, createQuery(orderQuery, filterClause, SORT_ORDERS_BY_DATE)),
					createSortQueryData(SORT_BY_ORDER_NUMBER, createQuery(orderQuery, filterClause, SORT_ORDERS_BY_CODE)),
					createSortQueryData(SORT_BY_STATUS, createQuery(orderQuery, filterClause, SORT_ORDERS_BY_STATUS)),
					createSortQueryData(SORT_BY_TOTAL_AMOUNT, createQuery(orderQuery, filterClause, SORT_ORDERS_BY_TOTALAMOUNT)));
		}

		return ((SearchPageData<OrderModel>) getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				try {
					getSearchRestrictionService().disableSearchRestrictions();
					return getPagedFlexibleSearchService().search(sortQueries, SORT_BY_DATE, queryParams, pageableData);
				}finally
				{
					getSearchRestrictionService().enableSearchRestrictions();
				}
			}
		}, userService.getAdminUser()));

	}



	@Override
	public OrderModel findOrderByB2BCustomerAndCodeAndStore(final CustomerModel customerModel, final String code,
			final BaseStoreModel store)
	{
		validateParameterNotNull(customerModel, "Customer must not be null");
		validateParameterNotNull(code, "Code must not be null");
		validateParameterNotNull(store, "Store must not be null");
		if(customerModel instanceof B2BCustomerModel) {
		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put(CUSTOMER, customerModel);
		queryParams.put(CODE, code);
		queryParams.put(STORE, store);

		return ((OrderModel) getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				try {
				getSearchRestrictionService().disableSearchRestrictions();
				return getFlexibleSearchService().searchUnique(
						new FlexibleSearchQuery(FIND_ORDERS_BY_CUSTOMER_CODE_STORE_QUERY, queryParams));
				}finally
				{
					getSearchRestrictionService().enableSearchRestrictions();
				}
			}
		}));
		}else {
			return null ;
		}

	}


	@Override
	public SearchPageData<OrderModel> findOrdersForAdmin(final CustomerModel customerModel,
			final BaseStoreModel store, final OrderStatus[] status, final PageableData pageableData, final List<B2BUnitModel> b2bUnit) {
		validateParameterNotNull(customerModel, "Customer must not be null");
		validateParameterNotNull(store, "Store must not be null");

		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put(CUSTOMER, customerModel);
		queryParams.put(STORE, store);
		queryParams.put(B2B_UNIT,b2bUnit);

		String filterClause = StringUtils.EMPTY;
		if (CollectionUtils.isNotEmpty(getFilterOrderStatusList()))
		{
			queryParams.put(FILTER_STATUS_LIST, getFilterOrderStatusList());
			filterClause = FILTER_ORDER_STATUS;
		}

		final List<SortQueryData> sortQueries;
		if (ArrayUtils.isNotEmpty(status))
		{
			orderQuery = FIND_ORDERS_FOR_STATUS_ADMIN ;
			queryParams.put(STATUS_LIST, Arrays.asList(status));
			sortQueries = Arrays.asList(createSortQueryData(SORT_BY_DATE, createQuery(orderQuery, filterClause, SORT_ORDERS_BY_DATE)),
					createSortQueryData(SORT_BY_ORDER_NUMBER, createQuery(orderQuery, filterClause, SORT_ORDERS_BY_CODE)),
					createSortQueryData(SORT_BY_STATUS, createQuery(orderQuery, filterClause, SORT_ORDERS_BY_STATUS)));
		}
		else
		{
			orderQuery = FIND_ORDERS_FOR_ADMIN ;
			sortQueries = Arrays.asList(createSortQueryData(SORT_BY_DATE, createQuery(orderQuery, filterClause, SORT_ORDERS_BY_DATE)),
					createSortQueryData(SORT_BY_ORDER_NUMBER, createQuery(orderQuery, filterClause, SORT_ORDERS_BY_CODE)),
					createSortQueryData(SORT_BY_STATUS, createQuery(orderQuery, filterClause, SORT_ORDERS_BY_STATUS)),
					createSortQueryData(SORT_BY_TOTAL_AMOUNT, createQuery(orderQuery, filterClause, SORT_ORDERS_BY_TOTALAMOUNT)));
		}

		return ((SearchPageData<OrderModel>) getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				try {
					getSearchRestrictionService().disableSearchRestrictions();
					return getPagedFlexibleSearchService().search(sortQueries, SORT_BY_DATE, queryParams, pageableData);
				}finally
				{
					getSearchRestrictionService().enableSearchRestrictions();
				}
			}
		}, userService.getAdminUser()));

	}


	/**
	 *
	 * @return searchRestrictionService
	 */
	protected SearchRestrictionService getSearchRestrictionService() {
		return searchRestrictionService;
	}

	/**
	 * @param searchRestrictionService
	 *           the searchRestrictionService to set
	 */
	public void setSearchRestrictionService(final SearchRestrictionService searchRestrictionService) {
		this.searchRestrictionService = searchRestrictionService;
	}

	/**
	 *
	 * @return sessionService
	 */
	protected SessionService getSessionService() {
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService) {
		this.sessionService = sessionService;
	}

}

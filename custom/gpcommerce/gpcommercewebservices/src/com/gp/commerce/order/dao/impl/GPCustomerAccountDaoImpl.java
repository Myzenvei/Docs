/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.order.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.constants.YcommercewebservicesConstants;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.order.dao.GPCustomerAccountDao;

import de.hybris.platform.commerceservices.customer.dao.impl.DefaultCustomerAccountDao;
import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.util.Config;

public class GPCustomerAccountDaoImpl  extends DefaultCustomerAccountDao implements GPCustomerAccountDao {
	
	private static final String BY_TOTAL_AMOUNT = "byTotalAmount";

	private static final String BY_STATUS = "byStatus";

	private static final String BY_ORDER_NUMBER = "byOrderNumber";

	private static final String BY_DATE = "byDate";

	private static final String STATUS_LIST = "statusList";

	private static final String FILTER_STATUS_LIST = "filterStatusList";

	private static final String SELECTED_END_DATE = "selectedEndDate";

	private static final String SELECTED_START_DATE = "selectedStartDate";

	private static final String STORE = "store";

	private static final String CUSTOMER = "customer";

	private static final Logger LOG = Logger.getLogger(GPCustomerAccountDaoImpl.class);
	
	private PagedFlexibleSearchService pagedFlexibleSearchService ;

	
	private static final String FIND_ORDERS_BY_CUSTOMER_STORE_QUERY = "SELECT {" + OrderModel.PK + "}, {"
			+ OrderModel.CREATIONTIME + "}, {" + OrderModel.CODE + "} FROM {" + OrderModel._TYPECODE + "} WHERE {" + OrderModel.USER
			+ "} = ?customer AND {" + OrderModel.VERSIONID + "} IS NULL AND {" + OrderModel.STORE + "} = ?store" 
			+ "  AND {" +OrderModel.CREATIONTIME +"} BETWEEN  ?selectedStartDate AND  ?selectedEndDate " ;


	private static final String FIND_ORDERS_BY_CUSTOMER_STORE_QUERY_AND_STATUS = FIND_ORDERS_BY_CUSTOMER_STORE_QUERY + " AND {"
			+ OrderModel.STATUS + "} IN (?statusList)";

	private static final String FILTER_ORDER_STATUS = " AND {" + OrderModel.STATUS + "} NOT IN (?filterStatusList)";

	private static final String SORT_ORDERS_BY_DATE = " ORDER BY {" + OrderModel.CREATIONTIME + "} DESC, {" + OrderModel.PK + "}";

	private static final String SORT_ORDERS_BY_CODE = " ORDER BY {" + OrderModel.CODE + "},{" + OrderModel.CREATIONTIME
			+ "} DESC, {" + OrderModel.PK + "}";
	
	private static final String SORT_ORDERS_BY_STATUS = " ORDER BY {" + OrderModel.STATUS 
			+ "} DESC, {" + OrderModel.PK + "}";
	
	private static final String SORT_ORDERS_BY_TOTALAMOUNT = " ORDER BY {" + OrderModel.TOTALPRICE 
			+ "} DESC, {" + OrderModel.PK + "}";
	
	@Override
	public SearchPageData<OrderModel> findOrdersByCustomerAndStore(final CustomerModel customerModel, final BaseStoreModel store,
			final OrderStatus[] status, final PageableData pageableData){

		String startDate = GPFunctions.getStartDateRange(pageableData.getStartDate());
		String endDate= GPFunctions.getEndDateRange(pageableData.getEndDate());
		
		validateParameterNotNull(customerModel, "Customer must not be null");
		validateParameterNotNull(store, "Store must not be null");
		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put(CUSTOMER, customerModel);
		queryParams.put(STORE, store);
		queryParams.put(SELECTED_START_DATE,startDate);
		if(LOG.isDebugEnabled()){
			LOG.debug("StartDate for OrderHistory :"+ startDate);
		}
		queryParams.put(SELECTED_END_DATE,endDate) ;
		if(LOG.isDebugEnabled()){
			LOG.debug("EndDate for B2BOrderHistory :"+ endDate);
		}

		String filterClause = StringUtils.EMPTY;
		if (CollectionUtils.isNotEmpty(getFilterOrderStatusList()))
		{
			queryParams.put(FILTER_STATUS_LIST, getFilterOrderStatusList());
			filterClause = FILTER_ORDER_STATUS;
		}

		final List<SortQueryData> sortQueries;

		if (ArrayUtils.isNotEmpty(status))
		{
			queryParams.put(STATUS_LIST, Arrays.asList(status));
			sortQueries = Arrays.asList(
					createSortQueryData(BY_DATE,
							createQuery(FIND_ORDERS_BY_CUSTOMER_STORE_QUERY_AND_STATUS, filterClause, SORT_ORDERS_BY_DATE)),
					createSortQueryData(BY_ORDER_NUMBER,
							createQuery(FIND_ORDERS_BY_CUSTOMER_STORE_QUERY_AND_STATUS, filterClause, SORT_ORDERS_BY_CODE)),
					createSortQueryData(BY_STATUS,
							createQuery(FIND_ORDERS_BY_CUSTOMER_STORE_QUERY_AND_STATUS, filterClause, SORT_ORDERS_BY_STATUS)),
					createSortQueryData(BY_TOTAL_AMOUNT,
							createQuery(FIND_ORDERS_BY_CUSTOMER_STORE_QUERY_AND_STATUS, filterClause, SORT_ORDERS_BY_TOTALAMOUNT)));	
					
		
		}
		else
		{
			sortQueries = Arrays
					.asList(
							createSortQueryData(BY_DATE,
									createQuery(FIND_ORDERS_BY_CUSTOMER_STORE_QUERY, filterClause, SORT_ORDERS_BY_DATE)),
					createSortQueryData(BY_ORDER_NUMBER,
							createQuery(FIND_ORDERS_BY_CUSTOMER_STORE_QUERY, filterClause, SORT_ORDERS_BY_CODE)),
					createSortQueryData(BY_STATUS,
							createQuery(FIND_ORDERS_BY_CUSTOMER_STORE_QUERY, filterClause, SORT_ORDERS_BY_STATUS)),
					createSortQueryData(BY_TOTAL_AMOUNT,
							createQuery(FIND_ORDERS_BY_CUSTOMER_STORE_QUERY, filterClause, SORT_ORDERS_BY_TOTALAMOUNT)));
	
		}

		return getPagedFlexibleSearchService().search(sortQueries, BY_DATE, queryParams, pageableData);
	
		
	}
	
	@Override
	public PagedFlexibleSearchService getPagedFlexibleSearchService() {
		return pagedFlexibleSearchService;
	}

	@Override
	public void setPagedFlexibleSearchService(PagedFlexibleSearchService pagedFlexibleSearchService) {
		this.pagedFlexibleSearchService = pagedFlexibleSearchService;
	}
}

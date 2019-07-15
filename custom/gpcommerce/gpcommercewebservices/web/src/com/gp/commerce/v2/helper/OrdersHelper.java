/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.v2.helper;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderHistoriesData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderHistoryListWsDTO;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import com.gp.commerce.constants.YcommercewebservicesConstants;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.dto.company.B2BUnitWsDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.gp.commerce.facade.order.GPB2BOrderFacade;
import com.gp.commerce.facade.order.impl.GPB2BOrderFacadeImpl ;


@Component
public class OrdersHelper extends AbstractHelper
{
	private static final String END_DATE = "endDate";
	private static final String START_DATE = "startDate";
	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;
	@Resource(name = "gpB2BOrderFacade")
	private GPB2BOrderFacade gpB2BOrderFacade ;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "modelService")
	private  ModelService modelService;
	
	private static Map<String, List<OrderStatus>> orderStatusesMap;
	static {
		orderStatusesMap = new HashMap<>();
		orderStatusesMap.put("PLACED",
				Arrays.asList(new OrderStatus[] { OrderStatus.CREATED, OrderStatus.FRAUD_CHECKED_PASSED,
						OrderStatus.WAIT_FRAUD_MANUAL_CHECK, OrderStatus.APPROVED, OrderStatus.FRAUD_REJECTED,
						OrderStatus.PAYMENT_AUTHORIZED }));
		orderStatusesMap.put(OrderStatus.REJECTED.toString(), Arrays.asList(new OrderStatus[] { OrderStatus.REJECTED }));
		orderStatusesMap.put(OrderStatus.SUBMITTED.toString(), Arrays.asList(new OrderStatus[] { OrderStatus.SUBMITTED }));
		orderStatusesMap.put("IN_PROCESS",
				Arrays.asList(new OrderStatus[] { OrderStatus.PARTIALLY_SHIPPED, OrderStatus.PAYMENT_ERROR }));
		orderStatusesMap.put(OrderStatus.SHIPPED.toString(), Arrays.asList(new OrderStatus[] { OrderStatus.SHIPPED }));
		orderStatusesMap.put(OrderStatus.CANCELLED.toString(), Arrays.asList(new OrderStatus[] { OrderStatus.CANCELLED }));
		orderStatusesMap.put(OrderStatus.COMPLETED.toString(), Arrays.asList(new OrderStatus[] { OrderStatus.COMPLETED }));
		orderStatusesMap.put(OrderStatus.PENDING_APPROVAL.toString(), Arrays.asList(new OrderStatus[] { OrderStatus.PENDING_APPROVAL }));

	}

	@Cacheable(value = "orderCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,'DTO',#statuses,#currentPage,#pageSize,#sort,#fields)")
	public OrderHistoryListWsDTO searchOrderHistory(final String statuses, final int currentPage, final int pageSize,
			final String sort, final String fields)
	{
		final OrderHistoriesData orderHistoriesData = searchOrderHistory(statuses, currentPage, pageSize, sort);
		return getDataMapper().map(orderHistoriesData, OrderHistoryListWsDTO.class, fields);
	}

	@Cacheable(value = "orderCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,'Data',#statuses,#currentPage,#pageSize,#sort)")
	public OrderHistoriesData searchOrderHistory(final String statuses, final int currentPage, final int pageSize,
			final String sort)
	{
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort);

		final OrderHistoriesData orderHistoriesData;
		if (statuses != null)
		{
			final Set<OrderStatus> statusSet = extractOrderStatuses(statuses);
			orderHistoriesData = createOrderHistoriesData(orderFacade.getPagedOrderHistoryForStatuses(pageableData,
					statusSet.toArray(new OrderStatus[statusSet.size()])));
		}
		else
		{
			orderHistoriesData = createOrderHistoriesData(orderFacade.getPagedOrderHistoryForStatuses(pageableData));
		}
		return orderHistoriesData;
	}

	protected Set<OrderStatus> extractOrderStatuses(final String statuses)
	{
		final String[] statusesStrings = statuses.split(YcommercewebservicesConstants.OPTIONS_SEPARATOR);

		final Set<OrderStatus> statusesEnum = new HashSet<>();
		for (final String status : statusesStrings)
		{
			statusesEnum.add(OrderStatus.valueOf(status));
		}
		return statusesEnum;
	}

	protected OrderHistoriesData createOrderHistoriesData(final SearchPageData<OrderHistoryData> result)
	{
		final OrderHistoriesData orderHistoriesData = new OrderHistoriesData();
		orderHistoriesData.setOrders(result.getResults());
		orderHistoriesData.setSorts(result.getSorts());
		orderHistoriesData.setPagination(result.getPagination());

		return orderHistoriesData;
	}
	
	@Cacheable(value = "orderCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,'DTO',#statuses,#currentPage,#pageSize,#sort,#fields,#dateRange)")
	public OrderHistoryListWsDTO searchOrderHistory(String statuses, int currentPage, int pageSize, String sort,
		final String fields ,String dateRange) {
		HashMap<String,Date>  orderDuration =calculateOrderDuration(dateRange) ;
		Date startDate=orderDuration.get(START_DATE);
		Date endDate=orderDuration.get(END_DATE);
		final OrderHistoriesData orderHistoriesData = searchOrderHistory(statuses, currentPage, pageSize, sort, startDate,endDate);
		return getDataMapper().map(orderHistoriesData, OrderHistoryListWsDTO.class, fields);
	}

	@Cacheable(value = "orderCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,'Data',#statuses,#currentPage,#pageSize,#sort,#selectedDate)")
	public OrderHistoriesData searchOrderHistory(String statuses, int currentPage, int pageSize, String sort,Date startDate,Date endDate) {
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort,startDate,endDate);

		final OrderHistoriesData orderHistoriesData;
		if (statuses != null)
		{
			final Set<OrderStatus> statusSet = extractOrderStatuses(statuses);
			orderHistoriesData = createOrderHistoriesData(orderFacade.getPagedOrderHistoryForStatuses(pageableData,
					statusSet.toArray(new OrderStatus[statusSet.size()])));
		}
		else
		{
			orderHistoriesData = createOrderHistoriesData(orderFacade.getPagedOrderHistoryForStatuses(pageableData));
		}
		return orderHistoriesData;
	}
	
	
	protected PageableData createPageableData(final int currentPage, final int pageSize, final String sort,final Date startDate,final Date endDate)
	{
		final PageableData pageable = new PageableData();
		pageable.setCurrentPage(currentPage);
		pageable.setPageSize(pageSize);
		pageable.setSort(sort);
		pageable.setStartDate(startDate);
		pageable.setEndDate(endDate);
		return pageable;
	}

 
	

	@Cacheable(value = "orderCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,'Data',#statuses,#currentPage,#pageSize,#sort,#startDate,#endDate,#b2bUnit)")
		private OrderHistoriesData getPagedB2BOrderHistory(String statuses, int currentPage, int pageSize, String sort,
			Date startDate,Date endDate, String b2bUnit) {
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort,startDate,endDate);

		final OrderHistoriesData orderHistoriesData;
		if (statuses != null)
		{
			final Set<OrderStatus> statusSet = extractOrderStatuses(statuses);
			orderHistoriesData = createOrderHistoriesData(gpB2BOrderFacade.getPagedOrderHistoryForStatuses(pageableData,b2bUnit,
					statusSet.toArray(new OrderStatus[statusSet.size()])));
		}
		else
		{
			orderHistoriesData = createOrderHistoriesData(gpB2BOrderFacade.getPagedOrderHistoryForStatuses(pageableData,b2bUnit));
		}
		return orderHistoriesData;
	}
	@Cacheable(value = "orderCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,'DTO',#statuses,#currentPage,#pageSize,#sort,#fields,#daterange,#b2bUnit)")
	public OrderHistoryListWsDTO searchB2BOrderHistory(String statuses, int currentPage, int pageSize, String sort,
			String fields, String daterange, String b2bUnit) {
		HashMap<String,Date>  orderDuration =calculateOrderDuration(daterange) ;
		Date startDate=orderDuration.get(START_DATE);
		Date endDate=orderDuration.get(END_DATE);
		final OrderHistoriesData orderHistoriesData = getPagedB2BOrderHistory(statuses, currentPage, pageSize, sort, startDate,endDate,b2bUnit);
		return getDataMapper().map(orderHistoriesData, OrderHistoryListWsDTO.class, fields);
	}
	
	public HashMap<String,Date>  calculateOrderDuration(String selectedDate) {
		HashMap<String,Date> dateMap =new HashMap<>();
		Date startDate=null ;
		Date endDate=null ;
		int dateSelection =Integer.parseInt(selectedDate) ;
		Calendar cal = Calendar.getInstance();
		cal.setTime( cal.getTime());
		endDate=cal.getTime();
		if(dateSelection==GpcommerceCoreConstants.THIRTY_DAYS) {
			cal.add(Calendar.DATE,-dateSelection);
			startDate=cal.getTime();
		}
		else if(dateSelection==GpcommerceCoreConstants.SIX_MONTHS) {
			cal.add(Calendar.MONTH, -dateSelection);
			startDate=cal.getTime();
		}else {
			endDate= setYearEndDate(dateSelection);
			startDate=setYearStartDate(dateSelection) ;
	     }
		dateMap.put(START_DATE, startDate);
		dateMap.put(END_DATE, endDate);
		return dateMap  ;
	}

	private Date setYearEndDate(int dateSelection) {
		Calendar cal = Calendar.getInstance();
		 cal.set(Calendar.YEAR, dateSelection);
	        cal.set(Calendar.MONTH, 11);
	        cal.set(Calendar.DAY_OF_MONTH, 31);
	     return cal.getTime();
		
	}
	
	private Date setYearStartDate(int dateSelection) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, dateSelection);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
	     return cal.getTime();
	}
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	
	
	public OrderHistoryListWsDTO searchB2BOrderHistoryForAdmin(String statuses, int currentPage, int pageSize, String sort,
			String fields, String daterange) {
		final OrderHistoriesData orderHistoriesData = getPagedB2BOrderHistoryForAdmin(statuses, currentPage, pageSize, sort, null,null);
		return getDataMapper().map(orderHistoriesData, OrderHistoryListWsDTO.class, fields);
	}

	private OrderHistoriesData getPagedB2BOrderHistoryForAdmin(String statuses, int currentPage, int pageSize, String sort,
		Date startDate,Date endDate) {
	final PageableData pageableData = createPageableData(currentPage, pageSize, sort,startDate,endDate);

	final OrderHistoriesData orderHistoriesData;
	Set<OrderStatus> statusSet = new HashSet<>();
	OrderStatus[] statusArray={};
	if(null != statuses){
		statusSet = extractOrderStatuses(statuses);
		statusArray=statusSet.toArray(new OrderStatus[statusSet.size()]);
	}
	orderHistoriesData = createOrderHistoriesData(gpB2BOrderFacade.getPagedOrderHistoryForAdmin(pageableData,
			statusArray));
	return orderHistoriesData;
}

 public String getCorrespondingStatus(String statuses)
 {
	 if(StringUtils.isNotEmpty(statuses))
	 {
	 final String[] statusesStrings = statuses.split(YcommercewebservicesConstants.OPTIONS_SEPARATOR);

		final Set<OrderStatus> statusesEnum = new HashSet<>();
		StringBuilder finalStatuses=new StringBuilder();
		for (final String status : statusesStrings)
		{
			statusesEnum.addAll(orderStatusesMap.get(status));
		}
		statusesEnum.stream().map(status->status.toString()).forEach(status->finalStatuses.append(status).append(","));
	 return StringUtils.chomp(finalStatuses.toString(), ",");
	 }
	 else
	 {
		 return	getAllB2BStatuses(); 
	 }
	 
 }
 private String getAllB2BStatuses()
 {
	 final Set<OrderStatus> statusesEnum = new HashSet<>();
	 StringBuilder finalStatuses=new StringBuilder();
	 orderStatusesMap.keySet().stream().forEach(key->statusesEnum.addAll(orderStatusesMap.get(key)));
	 statusesEnum.stream().map(status->status.toString()).forEach(status->finalStatuses.append(status).append(","));
	 return StringUtils.chomp(finalStatuses.toString(), ",");
 }


}

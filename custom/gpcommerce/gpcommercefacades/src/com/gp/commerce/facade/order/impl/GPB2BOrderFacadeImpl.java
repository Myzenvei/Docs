/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.facade.order.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.order.impl.DefaultOrderFacade;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;

import com.gp.commerce.core.company.services.GPB2BUnitsService;
import com.gp.commerce.core.customer.GPB2BCustomerAccountService;
import com.gp.commerce.facade.order.GPB2BOrderFacade;
import com.gp.commerce.facades.company.GPB2BUnitsFacade;



public class GPB2BOrderFacadeImpl extends DefaultOrderFacade  implements  GPB2BOrderFacade{
 
	GPB2BCustomerAccountService gpB2BCustomerAccountService   ;
	private Converter<OrderModel, OrderHistoryData> orderHistoryConverter;
	private BaseStoreService baseStoreService;
	private UserService userService;
	private static final String ORDER_NOT_FOUND_FOR_USER_AND_BASE_STORE = "Order with guid %s not found for current user in current BaseStore";

	@Resource(name = "b2bUnitService")
	protected B2BUnitService<B2BUnitModel, UserModel> b2bUnitService;
	@Resource(name = "b2bUnitConverter")
	private Converter<B2BUnitModel, B2BUnitData> b2bUnitConverter;
	@Resource(name = "gpB2BUnitsFacade")
	private GPB2BUnitsFacade gpB2BUnitsFacade ;
	@Resource(name = "b2bUnitReverseConverter")
	protected Converter<B2BUnitData, B2BUnitModel> b2BUnitReverseConverter;
	@Resource(name = "gpB2BUnitsService")
	protected GPB2BUnitsService<B2BUnitModel, UserModel> gpB2BUnitsService;
	
	@Override
	public SearchPageData<OrderHistoryData> getPagedOrderHistoryForStatuses(PageableData pageableData,
			 String b2bUnit,  OrderStatus... statuses) {
		final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		List<B2BUnitModel> b2bUnitModel=findAllUnitsforUser(b2bUnit,currentCustomer);
		
		final SearchPageData<OrderModel> orderResults = getGpB2BCustomerAccountService().getB2BOrderList(currentCustomer, currentBaseStore,
				statuses, pageableData,b2bUnitModel);
		return convertPageData(orderResults, getOrderHistoryConverter());
	}

	@Override
	public SearchPageData<OrderHistoryData> getPagedOrderHistoryForAdmin(PageableData pageableData, OrderStatus... statuses) {
		final B2BCustomerModel currentCustomer = (B2BCustomerModel) getUserService().getCurrentUser();
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		List<B2BUnitModel> units=gpB2BUnitsService.getUnitsWithChildNodes(currentCustomer, true);
		
		final SearchPageData<OrderModel> orderResults = getGpB2BCustomerAccountService().getB2BOrderListForAdmin(currentCustomer, currentBaseStore,
				statuses, pageableData,units);
		return convertPageData(orderResults, getOrderHistoryConverter());
	}
	
	private List<B2BUnitModel> findAllUnitsforUser(String b2bUnit,CustomerModel currentCustomer) {
		List<B2BUnitModel> b2bUnitList=new ArrayList<>();
		final B2BUnitModel customerUnit = b2bUnitService.getUnitForUid(b2bUnit);
		
		b2bUnitList.add(customerUnit) ;
		boolean isB2BAdmin=false;
		if(currentCustomer instanceof B2BCustomerModel){
			isB2BAdmin=gpB2BUnitsService.isB2BAdmin((B2BCustomerModel)currentCustomer);
		}
		if(isB2BAdmin) {
		final Collection<B2BUnitModel> childUnitModels = CollectionUtils.select(customerUnit.getMembers(),
					PredicateUtils.instanceofPredicate(B2BUnitModel.class));
		if(CollectionUtils.isNotEmpty(childUnitModels)) {
				for(B2BUnitModel childUnit :childUnitModels) {
					b2bUnitList.add(childUnit) ;
				}
			}
		final B2BUnitModel parent = b2bUnitService.getParent(customerUnit);
		if(parent!=null) {
		b2bUnitList.add(parent) ;
		}
	}
	return b2bUnitList;
	}
 
	@Override
	public OrderData getOrderDetailsForCode(final String code)
	{
		final BaseStoreModel baseStoreModel = getBaseStoreService().getCurrentBaseStore();

		OrderModel orderModel = null;
			try
			{
				orderModel = getGpB2BCustomerAccountService().getOrderForCode((CustomerModel) getUserService().getCurrentUser(), code,
						baseStoreModel);
			}
			catch (final ModelNotFoundException e)
			{
				throw new UnknownIdentifierException(String.format(ORDER_NOT_FOUND_FOR_USER_AND_BASE_STORE, code));
			}
		

		if (orderModel == null)
		{
			throw new UnknownIdentifierException(String.format(ORDER_NOT_FOUND_FOR_USER_AND_BASE_STORE, code));
		}
		return getOrderConverter().convert(orderModel);
	}

	@Override
	public Converter<OrderModel, OrderHistoryData> getOrderHistoryConverter() {
		return orderHistoryConverter;
	}

	public void setOrderHistoryConverter(Converter<OrderModel, OrderHistoryData> orderHistoryConverter) {
		this.orderHistoryConverter = orderHistoryConverter;
	}

	@Override
	public BaseStoreService getBaseStoreService() {
		return baseStoreService;
	}

	public void setBaseStoreService(BaseStoreService baseStoreService) {
		this.baseStoreService = baseStoreService;
	}

	@Override
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}



	public GPB2BCustomerAccountService getGpB2BCustomerAccountService() {
		return gpB2BCustomerAccountService;
	}



	public void setGpB2BCustomerAccountService(GPB2BCustomerAccountService gpB2BCustomerAccountService) {
		this.gpB2BCustomerAccountService = gpB2BCustomerAccountService;
	}



 


}

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
package com.gp.commerce.v2.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gp.commerce.core.constants.GPB2BExceptionEnum;
import com.gp.commerce.core.exceptions.GPCommerceBusinessException;
import com.gp.commerce.dto.UpdateOrderResponseDTO;
import com.gp.commerce.dto.UpdateOrderWsDTO;
import com.gp.commerce.exceptions.NoCheckoutCartException;
import com.gp.commerce.exceptions.PaymentAuthorizationException;
import com.gp.commerce.facade.data.NsConsignmentData;
import com.gp.commerce.facade.order.GPB2BOrderFacade;
import com.gp.commerce.facade.order.GPOrderFacade;
import com.gp.commerce.facade.order.GpOmsOrderFacade;
import com.gp.commerce.facade.order.data.LeaseAgreementData;
import com.gp.commerce.facade.order.data.LeaseAgreementWsDTO;
import com.gp.commerce.facade.order.data.SplitEntryListWsDTO;
import com.gp.commerce.facade.order.data.SplitEntryWsDTO;
import com.gp.commerce.facades.customer.GpCustomerFacade;
import com.gp.commerce.facades.order.impl.GPCheckoutFacade;
import com.gp.commerce.facades.subscription.GPSubscriptionFacade;
import com.gp.commerce.strategies.OrderCodeIdentificationStrategy;
import com.gp.commerce.swagger.ApiBaseSiteIdAndUserIdParam;
import com.gp.commerce.swagger.ApiBaseSiteIdParam;
import com.gp.commerce.swagger.ApiBaseSiteIdUserIdAndCartIdParam;
import com.gp.commerce.v2.helper.CartsHelper;
import com.gp.commerce.v2.helper.OrdersHelper;

import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoriesData;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderHistoryListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO;
import de.hybris.platform.commercewebservicescommons.strategies.CartLoaderStrategy;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;


/**
 * Web Service Controller for the ORDERS resource. Most methods check orders of the user. Methods require authentication
 * and are restricted to https channel.
 */

@Secured(
{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT","ROLE_ASAGENTSALESMANAGERGROUP" ,"ROLE_CLIENT","ROLE_GUEST"})
@Controller
@RequestMapping(value = "/{baseSiteId}")
@Api(tags = "Orders")
public class OrdersController extends BaseCommerceController
{
	private static final Logger LOG = Logger.getLogger(OrdersController.class);

	public static final String REDIRECT_PREFIX = "redirect:";
	private static final String GUEST_CREATE_ACCOUNT = "guestRegisterAfterPlaceOrder";
	
	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;
	@Resource(name = "orderCodeIdentificationStrategy")
	private OrderCodeIdentificationStrategy orderCodeIdentificationStrategy;
	@Resource(name = "cartLoaderStrategy")
	private CartLoaderStrategy cartLoaderStrategy;
	@Resource(name = "ordersHelper")
	private OrdersHelper ordersHelper;
	@Resource(name = "gpDefaultOrderFacade")
	private GPOrderFacade gpOrderFacade;
	@Resource(name="gpOmsOrderFacade")
	private GpOmsOrderFacade gpOmsOrderFacade;
	@Resource(name = "gpB2BOrderFacade")
	private  GPB2BOrderFacade gpB2BOrderFacade;
	@Resource
	private DeliveryService deliveryService;
	@Resource(name = "gpDefaultCustomerFacade")
	private GpCustomerFacade gpCustomerFacade;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource(name = "cartsHelper")
	private CartsHelper cartsHelper;
	@Resource(name="subscriptionCartFacadeImpl")
	private GPSubscriptionFacade subscriptionCartFacadeImpl ;

	@Resource(name = "gpCheckoutFacade")
	private GPCheckoutFacade gpCheckoutFacade;

	
	@RequestMapping(value = "/orders/{code}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get a order", notes = "Returns details of a specific order based on order GUID (Globally Unique Identifier) or order CODE. The response contains a detailed order information.", authorizations =
	{ @Authorization(value = "oauth2_client_credentials") })
	@ApiBaseSiteIdParam
	public OrderWsDTO getOrder(
			@ApiParam(value = "Order GUID (Globally Unique Identifier) or order CODE", required = true) @PathVariable final String code,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		OrderData orderData;
		if (orderCodeIdentificationStrategy.isID(code))
		{
			orderData = orderFacade.getOrderDetailsForGUID(code);
		}
		else
		{
			orderData = orderFacade.getOrderDetailsForCodeWithoutUser(code);
		}

		OrderWsDTO orderWsDTO=  getDataMapper().map(orderData, OrderWsDTO.class, fields);
		orderWsDTO.setIsSubscription(orderData.isIsSubscription()) ;
		orderWsDTO.setDeliveryGroup(gpOrderFacade.getDeliveryGroup(orderWsDTO));
		return orderWsDTO;
		
	}

	@RequestMapping(value = "/users/{userId}/orders/{code}", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 120)
	@Cacheable(value = "orderCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,'getOrderForUserByCode',#code,#fields)")
	@ResponseBody
	@ApiOperation(value = "Get a order", notes = "Returns specific order details based on a specific order code. The response contains detailed order information.")
	@ApiBaseSiteIdAndUserIdParam
	public OrderWsDTO getOrderForUserByCode(
			@ApiParam(value = "Order GUID (Globally Unique Identifier) or order CODE", required = true) @PathVariable final String code,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final OrderData orderData = orderFacade.getOrderDetailsForCode(code);
		OrderWsDTO orderWsDTO= getDataMapper().map(orderData, OrderWsDTO.class, fields);
		orderWsDTO.setIsSubscription(orderData.isIsSubscription());
		orderWsDTO.setDeliveryGroup(gpOrderFacade.getDeliveryGroup(orderWsDTO));
		return orderWsDTO;
		
	}
	
	@RequestMapping(value = "/users/{userId}/orders/b2b/{code}", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 120)
	@Cacheable(value = "orderCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,'getOrderForUserByCode',#code,#fields)")
	@ResponseBody
	@ApiOperation(value = "Get a order", notes = "Returns specific order details based on a specific order code. The response contains detailed order information.")
	@ApiBaseSiteIdAndUserIdParam
	public OrderWsDTO getOrderForB2BUserByCode(
			@ApiParam(value = "Order GUID (Globally Unique Identifier) or order CODE", required = true) @PathVariable final String code,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final OrderData orderData =gpB2BOrderFacade.getOrderDetailsForCode(code);
		OrderWsDTO orderWsDTO= getDataMapper().map(orderData, OrderWsDTO.class, fields);
		orderWsDTO.setIsSubscription(orderData.isIsSubscription());
		orderWsDTO.setDeliveryGroup(gpOrderFacade.getDeliveryGroup(orderWsDTO));
		return orderWsDTO;
		
	}


	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 120)
	@RequestMapping(value = "/users/{userId}/orders", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get order history for user", notes = "Returns order history data for all orders placed by the specific user for the specific base store. Response contains orders search result displayed in several pages if needed.")
	@ApiBaseSiteIdAndUserIdParam
	public OrderHistoryListWsDTO getOrdersForUser(
			@ApiParam(value = "Filters only certain order statuses. It means: statuses=CANCELLED,CHECKED_VALID would only return orders with status CANCELLED or CHECKED_VALID.") @RequestParam(required = false) final String statuses,
			@ApiParam(value = "The current result page requested.") @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.") @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Sorting method applied to the return results.") @RequestParam(required = false) final String sort,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			final HttpServletResponse response,@RequestParam(required = false) final String dateRange)
	{
		validateStatusesEnumValue(statuses);

		final OrderHistoryListWsDTO orderHistoryList = ordersHelper.searchOrderHistory(statuses, currentPage, pageSize, sort,addPaginationField(fields),dateRange);

		// X-Total-Count header
		setTotalCountHeader(response, orderHistoryList.getPagination());

		return orderHistoryList;
	}

			@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 120)
			@RequestMapping(value = "/users/{userId}/orders/{b2bunit}/b2b", method = RequestMethod.GET)
			@ResponseBody
			@ApiOperation(value = "Get order history for user", notes = "Returns order history data for all orders placed by the specific user for the specific base store. Response contains orders search result displayed in several pages if needed.")
			@ApiBaseSiteIdAndUserIdParam
			public OrderHistoryListWsDTO getOrdersForB2BUser(
					@ApiParam(value = "Filters only certain order statuses. It means: statuses=CANCELLED,CHECKED_VALID would only return orders with status CANCELLED or CHECKED_VALID.") @RequestParam(required = false) final String statuses,
					@ApiParam(value = "The current result page requested.") @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
					@ApiParam(value = "The number of results returned per page.") @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
					@ApiParam(value = "Sorting method applied to the return results.") @RequestParam(required = false) final String sort,
					@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
					@ApiParam(value = "b2bunit", required = true) @PathVariable final String b2bunit,
					final HttpServletResponse response,@RequestParam(required = false) final String dateRange)
			{
				validateStatusesEnumValue(statuses);

				final OrderHistoryListWsDTO orderHistoryList = ordersHelper.searchB2BOrderHistory(statuses, currentPage, pageSize, sort,addPaginationField(fields),dateRange,b2bunit);

				// X-Total-Count header
				setTotalCountHeader(response, orderHistoryList.getPagination());

				return orderHistoryList;
			}

	@RequestMapping(value = "/users/{userId}/orders", method = RequestMethod.HEAD)
	@ResponseBody
	@ApiOperation(value = "Get total number of orders", notes = "Returns X-Total-Count header with a total number of results (orders history for all orders placed by the specific user for the specific base store).")
	@ApiBaseSiteIdAndUserIdParam
	public void getCountOrdersForUser(
			@ApiParam(value = "Filters only certain order statuses. It means: statuses=CANCELLED,CHECKED_VALID would only return orders with status CANCELLED or CHECKED_VALID.") @RequestParam(required = false) final String statuses,
			final HttpServletResponse response)
	{
		final OrderHistoriesData orderHistoriesData = ordersHelper.searchOrderHistory(statuses, 0, 1, null);

		setTotalCountHeader(response, orderHistoriesData.getPagination());
	}
	
	@RequestMapping(value = "/users/{userId}/orders", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(value = "Post a order", notes = "Authorizes cart and places the order. Response contains the new order data.")
	@ApiBaseSiteIdAndUserIdParam
	@SuppressWarnings("squid:S1160")
	public OrderWsDTO placeOrder(
			final HttpServletRequest request, final HttpServletResponse response,
			@ApiParam(value = "Cart code for logged in user, cart GUID for guest checkout", required = true) @RequestParam(required = true) final String cartId, //NOSONAR
			@ApiParam(value = "CCV security code.") @RequestParam(required = false) final String securityCode,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws PaymentAuthorizationException, InvalidCartException, WebserviceValidationException, NoCheckoutCartException, GPCommerceBusinessException //NOSONAR
	{
		if (LOG.isDebugEnabled())
	{
		if(LOG.isDebugEnabled()){
			LOG.debug("placeOrder");
		}
	}

		cartLoaderStrategy.loadCart(cartId);
		if(cartsHelper.checkSubscriptionCart(cartId)) {
			validateCartForPlaceOrder(cartId);
		}else {
		validateCartForPlaceOrder();
		}


		if(gpOrderFacade.isUserInReview())
		{
			throw new GPCommerceBusinessException(GPB2BExceptionEnum.USER_UNDERREVIEW.getCode(),
					GPB2BExceptionEnum.USER_UNDERREVIEW.getErrMsg());
		}
		OrderData orderData=null ;
		//authorize
		if (cartsHelper.checkSubscriptionCart(cartId)) {
			if (!subscriptionCartFacadeImpl.authorizePayment(securityCode, cartId)) {
			throw new PaymentAuthorizationException();
		}
			  orderData = subscriptionCartFacadeImpl.placeOrder(cartId);
		} else {
			if (!getCheckoutFacade().authorizePayment(securityCode)) {
				throw new PaymentAuthorizationException();
			}
		//placeorder
			orderData = getCheckoutFacade().placeOrder();
		}
		final CartData cart = super.getSessionCart();
		gpOrderFacade.resetCartCookieForASM(request, response, cart.getCode());
		final String guestRegisterAfterPlaceOrder = sessionService.getCurrentSession()
				.getAttribute(GUEST_CREATE_ACCOUNT);
		if(guestRegisterAfterPlaceOrder != null) {
			final Cookie cookie = new Cookie(GUEST_CREATE_ACCOUNT, "true");
			cookie.setSecure(true);
			cookie.setMaxAge(60 * 60 * 24);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		return getDataMapper().map(orderData, OrderWsDTO.class, fields);
		
	}
	
	@RequestMapping(value = "/users/{userId}/getleaseagreement/{code}", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 120)
	@Cacheable(value = "orderCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,'getLeaseAgreement',#code,#fields)")
	@ResponseBody
	@ApiOperation(value = "Get Lease Agreement for order. ", notes = "Returns the lease agreement for the leasable products")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public LeaseAgreementWsDTO getLeaseAgreement(
			@ApiParam(value = "Order GUID (Globally Unique Identifier) or order CODE", required = true) @PathVariable final String code,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) {
		
		final OrderData orderData = orderFacade.getOrderDetailsForCode(code);
		final LeaseAgreementData leaseData = gpOrderFacade.getLeaseAgreementById(orderData.getLeaseTermId());
		return getDataMapper().map(leaseData, LeaseAgreementWsDTO.class, fields);
	}
	

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/updateOrder", method = RequestMethod.POST)
	@ResponseBody
	public UpdateOrderResponseDTO updateOrder(
			@ApiParam(value = "Consignment object.", required = true) @RequestBody final UpdateOrderWsDTO updateOrder,@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		NsConsignmentData consignmentData = getDataMapper().map(updateOrder, NsConsignmentData.class,fields);
		return getDataMapper().map(gpOrderFacade.updateOrderStatus(consignmentData),UpdateOrderResponseDTO.class, fields);
	}
	
	
		

	/**
 	 * Submit the confirmed cancel items to be cancelled
 	 */
	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT","ROLE_ASAGENTSALESMANAGERGROUP" })
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "users/{userId}/orders/{orderCode}", method = { RequestMethod.POST})
	public void submitCancelOrderPage(@PathVariable("orderCode") final String orderCode) throws OrderCancelException
	{
		gpOmsOrderFacade.createRequestOrderCancel(orderCode);

	}
	
	
	@Secured(
			{ "ROLE_TRUSTED_CLIENT", "ROLE_B2BADMINGROUP" })
			@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 120)
			@RequestMapping(value = "/users/{userId}/b2borders", method = RequestMethod.GET)
			@ResponseBody
			@ApiOperation(value = "Get order history for user", notes = "Returns order history data for all orders placed by the specific user for the specific base store. Response contains orders search result displayed in several pages if needed.")
			@ApiBaseSiteIdAndUserIdParam
			public OrderHistoryListWsDTO getOrdersForB2BAdmin(
					@ApiParam(value = "Filters only certain order statuses. It means: statuses=CANCELLED,CHECKED_VALID would only return orders with status CANCELLED or CHECKED_VALID.") @RequestParam(required = false) final String statuses,
					@ApiParam(value = "The current result page requested.") @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
					@ApiParam(value = "The number of results returned per page.") @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
					@ApiParam(value = "Sorting method applied to the return results.") @RequestParam(required = false) final String sort,
					@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
					final HttpServletResponse response,@RequestParam(required = false) final String dateRange)
			{
		
				String finalStatuses=ordersHelper.getCorrespondingStatus(statuses);
				validateStatusesEnumValue(finalStatuses);

				final OrderHistoryListWsDTO orderHistoryList = ordersHelper.searchB2BOrderHistoryForAdmin(finalStatuses, currentPage, pageSize, sort,addPaginationField(fields),dateRange);

				// X-Total-Count header
				setTotalCountHeader(response, orderHistoryList.getPagination());

				return orderHistoryList;
			}

	/**
	 * Approve or reject order
	 * @param orderCode 
	 * @param status 
	 * @param fields
	 * @return 
	 * @throws GPCommerceBusinessException 
	 */
	@Secured(
	{ "ROLE_TRUSTED_CLIENT", "ROLE_B2BADMINGROUP" })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(value = "users/{userId}/b2border/{orderCode}/{status}", method =
	{ RequestMethod.POST })
	public OrderWsDTO approveOrRejectOrder(@PathVariable("orderCode") final String orderCode,
			@PathVariable("status") final String status, @RequestBody final OrderWsDTO orderDetails,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) throws GPCommerceBusinessException
	{
		try
		{
			return getDataMapper().map(gpOmsOrderFacade.approveOrRejectOrder(orderCode, status, orderDetails.getApproverComments()), OrderWsDTO.class, fields);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new GPCommerceBusinessException(GPB2BExceptionEnum.ORDERSTATUS.getCode(),
					GPB2BExceptionEnum.ORDERSTATUS.getErrMsg());
		}
	}
	
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "kochauthtoken/{authorizationCode}", method = RequestMethod.POST)
	@ResponseBody
	public RegisterData getKochAuthToken(@PathVariable final String authorizationCode) {
		RegisterData kochAuthTokenDetails = null;
		if(!StringUtils.isEmpty(authorizationCode)) {
			if(LOG.isDebugEnabled()){
				LOG.debug("--------------- Calling getKochAutToken Facade Call ---------------");
			}
			kochAuthTokenDetails = gpCustomerFacade.getKochAuthToken(authorizationCode);
				if(null != kochAuthTokenDetails) {
					if(LOG.isDebugEnabled()){
						LOG.debug("Access token:"+kochAuthTokenDetails.getKochAuthAccessToken());
						LOG.debug("Id Token:"+kochAuthTokenDetails.getKochAuthIdToken());
						LOG.debug("Auth TS:"+kochAuthTokenDetails.getKochAuthTS());
						LOG.debug("Refresh token:"+kochAuthTokenDetails.getKochAuthRefreshToken());
					}
				}
			}
			return kochAuthTokenDetails;
		}

	public DeliveryService getDeliveryService() {
		return deliveryService;
	}


	public void setDeliveryService(DeliveryService deliveryService) {
		this.deliveryService = deliveryService;
	}

}

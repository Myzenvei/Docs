/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.v2.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gp.commerce.cart.impl.CommerceWebServicesCartFacade;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.SubscriptionCartStatusEnum;
import com.gp.commerce.core.jalo.SubscriptionCartProcess;
import com.gp.commerce.core.model.GPSubscriptionCartModel;
import com.gp.commerce.core.model.SubscriptionCartProcessModel;
import com.gp.commerce.core.util.GPCommerceCoreUtils;
import com.gp.commerce.facades.subscription.dto.GPSubscriptionFrequencyWsDTO;
import com.gp.commerce.facades.subscription.dto.GPSubscriptionUpdateWsDTO;
import com.gp.commerce.facades.subscription.impl.GPSubscriptionFacadeImpl;
import com.gp.commerce.order.data.CartDataList;
import com.gp.commerce.swagger.ApiBaseSiteIdParam;
import com.gp.commerce.swagger.ApiBaseSiteIdUserIdAndCartIdParam;
import com.gp.commerce.v2.helper.CartsHelper;
import com.gpintegration.cronjob.subscription.GPSubscriptionOrder;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercewebservicescommons.dto.order.CartListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.CartWsDTO;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
 

@Secured(
{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT","ROLE_ASAGENTSALESMANAGERGROUP" ,"ROLE_CLIENT"})
@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/subscription")
@CacheControl(directive = CacheControlDirective.NO_CACHE)
public class GPSubscriptionController extends BaseCommerceController {
	
	
	protected static final String DEFAULT_FIELD_SET = FieldSetLevelHelper.DEFAULT_LEVEL;
	private static final String SUBSCRIPTION_ORDER_CANCELLED_EMAIL_PROCESS = "subscriptionOrderCancelledEmailProcess";
	private static final String SUBSCRIPTION_ORDER_CANCELLED_EMAIL_PROCESS_LABEL = "subscriptionOrderCancelledEmailProcess-";
	
	@Resource(name = "cartsHelper")
	private CartsHelper cartsHelper;
	@Resource(name = "subscriptionCartFacadeImpl")
	private GPSubscriptionFacadeImpl subscriptionCartFacadeImpl ;
	
	@Resource(name = "commerceWebServicesCartFacade2")
	private CommerceWebServicesCartFacade commerceWebServicesCartFacade2;
	
	@Resource(name = "businessProcessService")
	protected BusinessProcessService businessProcessService;
	
	@Resource(name = "modelService")
	protected ModelService modelService;
	
	private static final String CART = "cart";
	private static final String CALCULATION_TYPE="calculationType";
	@Resource(name = "sessionService")
	private SessionService sessionService;
	
	@Resource(name="gpSubscriptionOrder")
	private GPSubscriptionOrder gpSubscriptionOrder;
	
	@Resource(name="gpCommerceCoreUtils")
	private GPCommerceCoreUtils gpCommerceCoreUtils;
	
	@RequestMapping(value = "/{cartId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get a cart with a given identifier.", notes = "Returns the cart with a given identifier.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartWsDTO getCart(@RequestParam(required = false, defaultValue = CART) final String calculationType,@ApiParam(value = "Subscription CartId.", required = true) @PathVariable final String cartId,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,final HttpServletRequest httpRequest, final HttpServletResponse httpResponse)
	{
		sessionService.setAttribute(CALCULATION_TYPE, calculationType);
		final CartData cart = commerceWebServicesCartFacade2.getSubscriptionCart(cartId) ;
		final CartWsDTO cartDto = cartsHelper.getCart(cart, fields);
		httpResponse.setHeader("Cache-Control","no-cache, no-store, max-age=0, must-revalidate");  //NOSONAR
		return cartDto;
	}
	
	@RequestMapping(value = "/getsubscriptions", method = RequestMethod.GET)
	@ApiBaseSiteIdParam
	@ResponseBody
	public CartListWsDTO getActiveSubscriptions(
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) {
		List<CartData> subscriptionCartDataList = subscriptionCartFacadeImpl.getActiveSubscriptions();
		CartDataList subscriptionData = new CartDataList();
		if(subscriptionCartDataList != null) {
			subscriptionData.setCarts(subscriptionCartDataList);
		}
		return getDataMapper().map(subscriptionData, CartListWsDTO.class, fields);
	}

	@RequestMapping(value = "/cancelsubscription", method = RequestMethod.POST)	
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Cancel subscription", notes = "Returns stauts 200 OK.")
	@ApiBaseSiteIdUserIdAndCartIdParam 
	public void cancelSubscription(@RequestBody final GPSubscriptionFrequencyWsDTO subscriptionDTO,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) {
	 
		GPSubscriptionCartModel subscriptionCartModel = subscriptionCartFacadeImpl.cancelSubscription(subscriptionDTO.getCode());
		if(subscriptionCartModel != null) {
			final SubscriptionCartProcessModel subscriptionCartProcessModel = (SubscriptionCartProcessModel) businessProcessService.createProcess(
					SUBSCRIPTION_ORDER_CANCELLED_EMAIL_PROCESS_LABEL + subscriptionCartModel.getCode() + "-" + System.currentTimeMillis(),
					SUBSCRIPTION_ORDER_CANCELLED_EMAIL_PROCESS);
			subscriptionCartProcessModel.setSubscriptionCart(subscriptionCartModel);
			subscriptionCartProcessModel.setSite(subscriptionCartModel.getSite());
			modelService.save(subscriptionCartProcessModel);
			businessProcessService.startProcess(subscriptionCartProcessModel);
		}
	}
	
	@RequestMapping(value = "/{cartId}/updatesubscription", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Update subscription", notes = "Returns stauts 200 OK.")
	@ApiBaseSiteIdUserIdAndCartIdParam 
	public void updateSubscription(@RequestBody final GPSubscriptionUpdateWsDTO subscriptionUpdateDTO) {
		Assert.notNull(subscriptionUpdateDTO.getCode(), "Parameter code cannot be null.");
		AddressData addressData = null;
		CCPaymentInfoData paymentInfoData = null;
		if(null != subscriptionUpdateDTO.getAddress() && null != subscriptionUpdateDTO.getAddress().getId()) {
			addressData = getUserFacade().getAddressForCode(subscriptionUpdateDTO.getAddress().getId());
			getDataMapper().map(subscriptionUpdateDTO.getAddress(), addressData, GpcommerceCoreConstants.PATCH_ADDRESS_FIELDS,true);
		}
		if(null != subscriptionUpdateDTO.getPaymentDetails() && null != subscriptionUpdateDTO.getPaymentDetails().getId()) {
			paymentInfoData = getDataMapper().map(subscriptionUpdateDTO.getPaymentDetails(),CCPaymentInfoData.class, GpcommerceCoreConstants.UPDATE_PAYMENT_SUBSCRIPTION_FIELDS);
		}
		subscriptionCartFacadeImpl.updateSubscription(addressData, paymentInfoData, subscriptionUpdateDTO.getCode());
	}
	

	@RequestMapping(value = "/{cartId}/placeorder", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "place order for subscription", notes = "Returns stauts 200 OK.")
	@ApiBaseSiteIdUserIdAndCartIdParam 
	public void placeSubscriptionOrder(@ApiParam(value = "Subscription CartId.", required = true) @PathVariable final String cartId) {
		GPSubscriptionCartModel subscriptionCartModel = (GPSubscriptionCartModel) subscriptionCartFacadeImpl.getSubsCartModel(cartId);
		if (subscriptionCartModel.getSubscriptionCartStatus().equals(SubscriptionCartStatusEnum.PAYMENTFAILURE)) {
			if (gpSubscriptionOrder.placeOrder(subscriptionCartModel)) {
				subscriptionCartModel.setSubscriptionCartStatus(SubscriptionCartStatusEnum.ACTIVE);
				subscriptionCartModel.setPaymentTransactions(null);
				subscriptionCartModel.setNextActivationDate(calculateNextFrequencyDate(subscriptionCartModel));
				modelService.save(subscriptionCartModel);
			}
		}
	}
	
	public Date calculateNextFrequencyDate(GPSubscriptionCartModel subscriptionCartModel) {
		Map<String, String> frequencyMap = subscriptionCartModel.getFrequency();
		int frequency = 0;
		Iterator<Entry<String, String>> iterator = frequencyMap.entrySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().getKey();
			frequency = Integer.valueOf(frequencyMap.get(key));
		}
		Integer nDaysBeforeSubscription=  (Integer) gpCommerceCoreUtils.getProductAttribute(subscriptionCartModel.getEntries().get(0).getProduct(), "nDaysBeforeSubscription");
		if(nDaysBeforeSubscription==null) {
			nDaysBeforeSubscription = new Integer(0);
		}
		LocalDate nextDate = LocalDate.now().plusDays(frequency).plusDays(nDaysBeforeSubscription.intValue());
		return Date.from(nextDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	
}

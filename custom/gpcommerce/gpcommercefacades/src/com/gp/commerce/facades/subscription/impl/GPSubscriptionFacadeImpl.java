/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.facades.subscription.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.gp.commerce.core.enums.SubscriptionCartStatusEnum;
import com.gp.commerce.core.enums.SubscriptionStatusEnum;
import com.gp.commerce.core.model.GPSubscriptionCartModel;
import com.gp.commerce.core.model.GPSubscriptionFrequencyModel;
import com.gp.commerce.core.model.GPSubscriptionScheduleModel;
import com.gp.commerce.core.model.PaypalPaymentInfoModel;
import com.gp.commerce.core.service.auth.impl.GPDefaultPaymentAuthService;
import com.gp.commerce.facades.cart.impl.GPDefaultCartFacade;
import com.gp.commerce.facades.subscription.GPSubscriptionCartFacade;
import com.gp.commerce.facades.subscription.GPSubscriptionFacade;
import com.gp.commerce.facades.subscription.data.GPSubscriptionFrequencyData;
import com.gp.commerce.facades.subscription.data.GPSubscriptionFrequencyListData;
import com.gp.commerce.facades.subscription.populators.GPSubscriptionDefaultCartPopulator;
import com.gp.commerce.subscription.service.GPSubscriptionService;

import de.hybris.platform.commercefacades.order.data.AddToCartParams;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.impl.DefaultCheckoutFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.strategies.CheckoutCustomerStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
 import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService ;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.PriceValue;
 

public class GPSubscriptionFacadeImpl extends DefaultCheckoutFacade implements GPSubscriptionFacade {

	private GPSubscriptionService subscriptionCartServiceImpl;

	private Converter<GPSubscriptionFrequencyModel, GPSubscriptionFrequencyData> gpSubscriptionFrequencyConverter;
	
	@Resource(name = "cartModificationConverter")
	private Converter<CommerceCartModification, CartModificationData> cartModificationConverter;

	@Resource(name = "gpsubscriptionCartParameterConverter")
	private Converter<AddToCartParams, CommerceCartParameter> gpsubscriptionCartParameterConverter;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "modelService")
	private ModelService modelService;
	
	@Resource(name = "cartService")
	private CartService cartService;
	
	@Resource(name = "userService")
	private UserService userService;
	
	@Resource(name = "cartConverter")
	private Converter<CartModel, CartData> cartConverter;
	
	@Resource(name = "customerAccountService")
	private DefaultCustomerAccountService customerAccountService;
	
	@Resource(name="commerceCheckoutService")
	private CommerceCheckoutService commerceCheckoutService;
	
	@Resource(name="checkoutCustomerStrategy")
	private CheckoutCustomerStrategy checkoutCustomerStrategy;
	
	
	@Resource(name = "checkoutFacade")
	private DefaultCheckoutFacade checkoutFacade ;
	
	@Resource
	private DeliveryService deliveryService;
	
	@Resource(name = "commerceCartService")
	private CommerceCartService commerceCartService;
	
	private GPDefaultCartFacade  gpCarFacade ;
	
	private Converter<DeliveryModeModel, DeliveryModeData> deliveryModeConverter;
	
	@Resource(name ="paymentAuthService")
	private GPDefaultPaymentAuthService paymentAuthService; 
	
	@Resource(name = "cartPopulator")
	private GPSubscriptionDefaultCartPopulator cartPopulator ;
	
	@Resource(name = "subsCartFacade")
	private GPSubscriptionCartFacade subsCartFacade;
	
	@Resource(name = "calculationService")
	private CalculationService calculationService;
	
	
	private static final Logger LOG = Logger.getLogger(GPSubscriptionFacadeImpl.class);
	

	@Override
	public GPSubscriptionFrequencyListData getSubscriptionFrequency(String productcode) {
		GPSubscriptionFrequencyListData subsfrequencylistData = new GPSubscriptionFrequencyListData();

		List<GPSubscriptionFrequencyModel> gpSubscriptionfreqList = getSubscriptionCartServiceImpl()
				.getSubsfrequencyforProduct(productcode);
		subsfrequencylistData.setSubscriptionFrequencyList(getGpSubscriptionFrequencyConverter().convertAll(gpSubscriptionfreqList));

		return subsfrequencylistData;
	}

	/* (non-Javadoc)
	 * @see com.gp.commerce.facades.subscription.GPSubscriptionFacade#addToCartInternal(java.lang.String, long, java.util.Map, java.lang.String)
	 */
	@Override
	public CartModificationData addToCartInternal(final String code, final long quantity,
			final Map<String, String> subscriptionFrequecy,
			final String cartId) throws CommerceCartModificationException
	{
		final AddToCartParams params = new AddToCartParams();
		params.setProductCode(code);
		params.setQuantity(quantity);
		params.setCartId(cartId);
		params.setFrequency(subscriptionFrequecy);
		params.setAdditionalAttributes(null);  
		params.setSubscriptionCart(true);
		return addToCart(params);
	}

	/* (non-Javadoc)
	 * @see com.gp.commerce.facades.subscription.GPSubscriptionFacade#addToCart(de.hybris.platform.commercefacades.order.data.AddToCartParams)
	 */
	@Override
	public CartModificationData addToCart(final AddToCartParams addToCartParams) throws CommerceCartModificationException
	{
		final CommerceCartParameter parameter = gpsubscriptionCartParameterConverter.convert(addToCartParams);     
		parameter.setAdditionalAttributes(addToCartParams.getAdditionalAttributes());
		final CommerceCartModification modification = subscriptionCartServiceImpl.addToCart(parameter);  
		final CartModificationData cartModificationData = cartModificationConverter.convert(modification);
		if (parameter.getCart() instanceof GPSubscriptionCartModel)
		{
			createSubscriptionScheduleForCart(parameter);
		} 
		return cartModificationData;
	}

	/**
	 * This method is used to cancel subscription for the cart
	 *
	 * @param parameter
	 *           code
	 */
	@Override
	public GPSubscriptionCartModel cancelSubscription(String code) {
		List<GPSubscriptionCartModel> subscriptionCartList = subscriptionCartServiceImpl.cancelSubscription(code);
		GPSubscriptionCartModel subscriptionCart = subscriptionCartList.stream().findFirst().orElse(null);
		if(subscriptionCart != null) {
			subscriptionCart.setIsActive(false);
			subscriptionCart.setSubscriptionCartStatus(SubscriptionCartStatusEnum.CANCELLED);
			modelService.save(subscriptionCart);
		}
		return subscriptionCart;
	}
	/**
	 * This method is used to create subscription schedule entries for given cart.
	 *
	 * @param parameter
	 *           CommerceCartParameter
	 */
	 private void createSubscriptionScheduleForCart(final CommerceCartParameter parameter)
	{
		final GPSubscriptionCartModel GPSubscriptionCartModel = (GPSubscriptionCartModel) parameter.getCart();
		GPSubscriptionCartModel.setIsSubscription(true);
		int frequency =0;
		if (CollectionUtils.isEmpty(GPSubscriptionCartModel.getGpSubscriptionSchedule())){
			 Map<String, String> frequencyMap=	GPSubscriptionCartModel.getFrequency(); 
			Iterator<Entry<String, String>> iterator= frequencyMap.entrySet().iterator();
			while(iterator.hasNext()) {
				String key=iterator.next().getKey() ;
				frequency=Integer.valueOf(frequencyMap.get(key)) ;
			}
			for (int count = 1; count <= 3; count++)
			{
				final LocalDate tomorrow = LocalDate.now().plusDays(frequency*count);
				final Date endDate = Date.from(tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant());
				createSubscriptionSchedule(GPSubscriptionCartModel, count, endDate);	
			}
		}
		
	} 

	/**
	 * This method is used to create Subscription schedule entries.
	 *
	 * @param GPSubscriptionCartModel
	 *           GPSubscriptionCartModel
	 * @param count
	 *           count
	 * @param endDate
	 *           endDate
	 */
	private void createSubscriptionSchedule(final GPSubscriptionCartModel gpSubscriptionCartModel, final int count,
			final Date endDate)
	{
		if (count == 1)
		{
			gpSubscriptionCartModel.setNextActivationDate(endDate);
			modelService.save(gpSubscriptionCartModel);
		}
		final GPSubscriptionScheduleModel subscriptionScheduleModel = new GPSubscriptionScheduleModel();
		subscriptionScheduleModel.setNextActivationDate(endDate);
		subscriptionScheduleModel.setGpSubscriptionCart(gpSubscriptionCartModel);
		subscriptionScheduleModel.setSubscriptionStatus(SubscriptionStatusEnum.PENDING);
		modelService.save(subscriptionScheduleModel);
	} 
	
	/**
	 * This method is used to update subscription for the cart
	 *
	 * @param parameter
	 *           updateData
	 */
	@Override
	public void updateSubscription(AddressData addData, CCPaymentInfoData payInfoData, String code) {
		subscriptionCartServiceImpl.updateSubscription(addData, payInfoData, code);
	}
	 
	@Override
	public CartModel getSubsCartModel(String cartId) {
		final UserModel currentUser = userService.getCurrentUser();
		CartModel cartModel = commerceCartService.getCartForCodeAndUser(cartId, currentUser);
		return cartModel;
	}
	
 //Delivery Method
	public List<? extends DeliveryModeData> getSupportedDeliveryModes(String cartId) throws Exception {
		final List<DeliveryModeData> result = new ArrayList<>();
		final CartModel cartModel =getSubsCartModel( cartId) ;
		if (cartModel != null) {
			for (final DeliveryModeModel deliveryModeModel : deliveryService.getSupportedDeliveryModeListForOrder(cartModel)) {
				result.add(convert(deliveryModeModel,cartModel));
			}
		}
		return result;
	}
	
	protected DeliveryModeData convert(final DeliveryModeModel deliveryModeModel,final CartModel cartModel) throws Exception {
		if (deliveryModeModel instanceof ZoneDeliveryModeModel) {

			if (cartModel != null) {
				final PriceValue deliveryCost = deliveryService.getDeliveryCostForDeliveryModeAndAbstractOrder(deliveryModeModel, cartModel);
				return getGpCarFacade().getDeliveryModeData(deliveryCost, deliveryModeModel,cartModel);
			}
			return null;
		}
		return getDeliveryModeConverter().convert(deliveryModeModel);
	}

	

	
	@Override
	public boolean authorizePayment(final String securityCode,final String cartId) {
		
		final CartModel cartModel =  getSubsCartModel( cartId) ;
		final Double amountForCC = cartModel.getTotalPrice();
		
		final CommerceCheckoutParameter parameter = createCommerceCheckoutParameter(cartModel, true);
		if (null == amountForCC || amountForCC >= 0) {
			if (cartModel.getPaymentInfo() instanceof CreditCardPaymentInfoModel) {
				
				final CreditCardPaymentInfoModel creditCardPaymentInfoModel = (CreditCardPaymentInfoModel) cartModel
						.getPaymentInfo();
				if (checkIfCurrentUserIsTheCartUser(cartId) && creditCardPaymentInfoModel != null) {
					
					parameter.setSecurityCode(securityCode);
					final PaymentTransactionEntryModel paymentTransactionEntryModel = paymentAuthService
							.getCCPaymentAuthentication(parameter);
					return paymentTransactionEntryModel != null && (TransactionStatus.ACCEPTED.name()
							.equals(paymentTransactionEntryModel.getTransactionStatus()));
				}
			} else if (cartModel.getPaymentInfo() instanceof PaypalPaymentInfoModel && checkIfCurrentUserIsTheCartUser(cartId)) {
				final PaymentTransactionEntryModel paymentTransactionEntryModel = paymentAuthService.executeAuthorizePayment(parameter);
				return paymentTransactionEntryModel != null && (TransactionStatus.ACCEPTED.name()
						.equals(paymentTransactionEntryModel.getTransactionStatus()));
			}
		} else {
			return true;
		}

		return false;
	}

	@Override
	public OrderData placeOrder(String cartId) throws InvalidCartException
	{
		final CartModel cartModel =  getSubsCartModel( cartId) ;
		if (cartModel != null)
		{
			if (cartModel.getUser().equals(getCurrentUserForCheckout(cartModel.getUser().getUid())) || checkoutCustomerStrategy.isAnonymousCheckout())
			{
				final OrderModel orderModel = placeOrder(cartModel);

				if (orderModel != null)
				{
					getModelService().refresh(orderModel);
				}
				if (orderModel != null)
				{
				 ((GPSubscriptionCartModel) (cartModel)).setIsActive(Boolean.TRUE);
				 ((GPSubscriptionCartModel) (cartModel)).setSubscriptionCartStatus(SubscriptionCartStatusEnum.ACTIVE);
				 setLeastPriceDeliveryMode(cartModel);
				 getModelService().save(cartModel);
				 subsCartFacade.releaseCoupons(cartModel);
				 getModelService().save(cartModel);
				 return  getOrderConverter().convert(orderModel);
					 
				}
			}
		}
		return null;
	}
	private void setLeastPriceDeliveryMode(AbstractOrderModel order) {
		
		List<DeliveryModeModel> deliveryModes = (List<DeliveryModeModel>) getDeliveryService().getSupportedDeliveryModeListForOrder(order) ;
		boolean hasChange=false;
		for(DeliveryModeModel deliveryModeModel: deliveryModes) {
			PriceValue price =  deliveryService.getDeliveryCostForDeliveryModeAndAbstractOrder(deliveryModeModel, order);
			if(price.getValue()<order.getDeliveryCost()) {
				order.setDeliveryMode(deliveryModeModel);
				order.setDeliveryCost(price.getValue());
				getModelService().save(order);
				hasChange=true;
			}
		}
		if (hasChange) {
			try {
				calculationService.calculateTotals(order, true);
			} catch (CalculationException e) {
				LOG.error("Calculate totals failed: "+e.getMessage(),e);
			}
		}

	}
	protected boolean checkIfCurrentUserIsTheCartUser(String cartId)
	{
		final CartModel cartModel =  getSubsCartModel( cartId) ;
		return cartModel == null ? false : cartModel.getUser().equals(getCurrentUserForCheckout(cartModel.getUser().getUid()));
	}

	public boolean setSubscriptionDeliveryMode(String deliveryModeCode, String cartId) {
		validateParameterNotNullStandardMessage("deliveryModeCode", deliveryModeCode);

		final CartModel cartModel =  getSubsCartModel(cartId) ;
		if (cartModel != null && isSupportedDeliveryMode(deliveryModeCode, cartModel))
		{
			final DeliveryModeModel deliveryModeModel = deliveryService.getDeliveryModeForCode(deliveryModeCode);
			if (deliveryModeModel != null)
			{
				final CommerceCheckoutParameter parameter = createCommerceCheckoutParameter(cartModel, true);
				parameter.setDeliveryMode(deliveryModeModel);
				return commerceCheckoutService.setDeliveryMode(parameter);
			}
		}
		return false;
	}
	
	protected CommerceCheckoutParameter createCommerceCheckoutParameter(final CartModel cart, final boolean enableHooks)
	{
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(enableHooks);
		parameter.setCart(cart);
		return parameter;
	}
	
	protected boolean isSupportedDeliveryMode(final String deliveryModeCode, final CartModel cartModel)
	{
		for (final DeliveryModeModel supportedDeliveryMode : deliveryService.getSupportedDeliveryModeListForOrder(cartModel))
		{
			if (deliveryModeCode.equals(supportedDeliveryMode.getCode()))
			{
				return true;
			}
		}
		return false;
	}
	
	
	
	public List<CartData> getActiveSubscriptions()
	{
		List<GPSubscriptionCartModel>carts=getSubscriptionCartServiceImpl().getActiveSubscriptions(userService.getCurrentUser());
		List<CartData>cartDataList=new ArrayList<>();
		if(carts != null) {
			for(GPSubscriptionCartModel cart:carts){
				CartData cartData=new CartData();
				cartPopulator.populate(cart, cartData);
				cartDataList.add(cartData);
			}
		}
		return cartDataList;
	}
	
	

	public void removeInActiveSubscriptions()
	{
		List<GPSubscriptionCartModel>carts=getSubscriptionCartServiceImpl().getInActiveSubscriptions(userService.getCurrentUser());
		if(carts != null) {
		getModelService().removeAll(carts);
		}
	}
	
	public boolean setPaymentDetails(String paymentInfoId, String cartId) {
		validateParameterNotNullStandardMessage("paymentInfoId", paymentInfoId);
		final CartModel cartModel = getSubsCartModel(cartId);
		final CustomerModel currentUserForCheckout = getCurrentUserForCheckout(cartModel.getUser().getUid());
		final CreditCardPaymentInfoModel ccPaymentInfoModel = customerAccountService
				.getCreditCardPaymentInfoForCode(currentUserForCheckout, paymentInfoId);
		
		if (ccPaymentInfoModel != null) {
			final CommerceCheckoutParameter parameter = createCommerceCheckoutParameter(cartModel, true);
			parameter.setPaymentInfo(ccPaymentInfoModel);
			return commerceCheckoutService.setPaymentInfo(parameter);
		}
		LOG.warn(String.format(
				"Did not find CreditCardPaymentInfoModel for user: %s, cart: %s &  paymentInfoId: %s. PaymentInfo Will not get set.",
				currentUserForCheckout, cartModel, paymentInfoId));
		return false;

	}
	
 
	public CustomerModel getCurrentUserForCheckout(String uid) {

		final CustomerModel checkoutCustomer = (CustomerModel) userService.getUserForUID(uid);

		Assert.state(!userService.isAnonymousUser(checkoutCustomer), "Checkout user must not be the anonymous user");

		return checkoutCustomer;

	}

	/**
	 * @return the gpSubscriptionFrequencyConverter
	 */
	public Converter<GPSubscriptionFrequencyModel, GPSubscriptionFrequencyData> getGpSubscriptionFrequencyConverter() {
		return gpSubscriptionFrequencyConverter;
	}

	/**
	 * @param gpSubscriptionFrequencyConverter
	 *            the gpSubscriptionFrequencyConverter to set
	 */
	public void setGpSubscriptionFrequencyConverter(
			Converter<GPSubscriptionFrequencyModel, GPSubscriptionFrequencyData> gpSubscriptionFrequencyConverter) {
		this.gpSubscriptionFrequencyConverter = gpSubscriptionFrequencyConverter;
	}

	/**
	 * @return the subscriptionCartServiceImpl
	 */
	public GPSubscriptionService getSubscriptionCartServiceImpl() {
		return subscriptionCartServiceImpl;
	}

	/**
	 * @param subscriptionCartServiceImpl the subscriptionCartServiceImpl to set
	 */
	public void setSubscriptionCartServiceImpl(GPSubscriptionService subscriptionCartServiceImpl) {
		this.subscriptionCartServiceImpl = subscriptionCartServiceImpl;
	}
 

	/**
	 * @return the cartModificationConverter
	 */
	public Converter<CommerceCartModification, CartModificationData> getCartModificationConverter()
	{
		return cartModificationConverter;
	}

	/**
	 * @param cartModificationConverter
	 *           the cartModificationConverter to set
	 */
	public void setCartModificationConverter(
			final Converter<CommerceCartModification, CartModificationData> cartModificationConverter)
	{
		this.cartModificationConverter = cartModificationConverter;
	}

	/**
	 * @return the cartConverter
	 */
	public Converter<CartModel, CartData> getCartConverter() {
		return cartConverter;
	}

	/**
	 * @param cartConverter the cartConverter to set
	 */
	public void setCartConverter(Converter<CartModel, CartData> cartConverter) {
		this.cartConverter = cartConverter;
	}

	/**
	 * @return the deliveryModeConverter
	 */
	public Converter<DeliveryModeModel, DeliveryModeData> getDeliveryModeConverter() {
		return deliveryModeConverter;
	}

	/**
	 * @param deliveryModeConverter the deliveryModeConverter to set
	 */
	public void setDeliveryModeConverter(Converter<DeliveryModeModel, DeliveryModeData> deliveryModeConverter) {
		this.deliveryModeConverter = deliveryModeConverter;
	}

	/**
	 * @return the gpCarFacade
	 */
	public GPDefaultCartFacade getGpCarFacade() {
		return gpCarFacade;
	}

	/**
	 * @param gpCarFacade the gpCarFacade to set
	 */
	public void setGpCarFacade(GPDefaultCartFacade gpCarFacade) {
		this.gpCarFacade = gpCarFacade;
	}

	public CalculationService getCalculationService() {
		return calculationService;
	}

	public void setCalculationService(CalculationService calculationService) {
		this.calculationService = calculationService;
	}

 
 

}

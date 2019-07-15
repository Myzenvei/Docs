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

import de.hybris.platform.acceleratorfacades.product.data.ProductWrapperData;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.promotion.CommercePromotionRestrictionFacade;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.voucher.VoucherFacade;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commercewebservicescommons.dto.order.CartWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartAddressException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartException;
import de.hybris.platform.converters.ConfigurablePopulator;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.validators.EnumValueValidator;
import com.gp.commerce.cart.impl.CommerceWebServicesCartFacade;
import com.gp.commerce.constants.YcommercewebservicesConstants;
import com.gp.commerce.core.exceptions.GPInvalidPromotionException;
import com.gp.commerce.exceptions.InvalidPaymentInfoException;
import com.gp.commerce.exceptions.NoCheckoutCartException;
import com.gp.commerce.exceptions.UnsupportedDeliveryModeException;
import com.gp.commerce.facades.cart.GPCartFacade;
import com.gp.commerce.facades.order.impl.GPCheckoutFacade;
import com.gp.commerce.facades.subscription.GPSubscriptionFacade;
import com.gp.commerce.facades.subscription.impl.GPSubscriptionDefaultCartFacade;
import com.gp.commerce.populator.options.PaymentInfoOption;
import com.gp.commerce.request.support.impl.PaymentProviderRequestSupportedStrategy;
import com.gp.commerce.v2.helper.CartsHelper;
import com.gp.commerce.validator.PlaceOrderCartValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class BaseCommerceController extends BaseController
{
	private static final Logger LOG = Logger.getLogger(BaseCommerceController.class);
	//TODO change commerceWebServicesCartFacade2 to commerceWebServicesCartFacade after removing it in commercefacades
	@Resource(name = "commerceWebServicesCartFacade2")
	private CartFacade cartFacade;
	@Resource(name = "checkoutFacade")
	private CheckoutFacade checkoutFacade;
	@Resource(name = "voucherFacade")
	private VoucherFacade voucherFacade;
	@Resource(name = "deliveryAddressValidator")
	private Validator deliveryAddressValidator;
	@Resource(name = "httpRequestAddressDataPopulator")
	private Populator<HttpServletRequest, AddressData> httpRequestAddressDataPopulator;
	@Resource(name = "addressValidator")
	private Validator addressValidator;
	@Resource(name = "addressDTOValidator")
	private Validator addressDTOValidator;
	@Resource(name = "userFacade")
	private UserFacade userFacade;
	@Resource(name = "ccPaymentInfoValidator")
	private Validator ccPaymentInfoValidator;
	@Resource(name = "paymentDetailsDTOValidator")
	private Validator paymentDetailsDTOValidator;
	@Resource(name = "httpRequestPaymentInfoPopulator")
	private ConfigurablePopulator<HttpServletRequest, CCPaymentInfoData, PaymentInfoOption> httpRequestPaymentInfoPopulator;
	@Resource(name = "placeOrderCartValidator")
	private PlaceOrderCartValidator placeOrderCartValidator;
	@Resource(name = "orderStatusValueValidator")
	private EnumValueValidator orderStatusValueValidator;
	@Resource(name = "cartsHelper")
	private CartsHelper cartsHelper;
	@Resource(name="subscriptionCartFacadeImpl")
	private GPSubscriptionFacade subscriptionCartFacadeImpl ;
	@Resource(name="commerceWebServicesCartFacade2")
	private CommerceWebServicesCartFacade commerceWebServicesCartFacade2 ;
	@Resource(name = "commercePromotionRestrictionFacade")
	private CommercePromotionRestrictionFacade commercePromotionRestrictionFacade;
	@Resource(name = "messageSource")
	private MessageSource messageSource;
	@Resource(name = "i18nService")
	private I18NService i18nService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;
	@Resource(name = "pointOfServiceValidator")
	private Validator pointOfServiceValidator;
	@Resource(name = "orderEntryCreateValidator")
	private Validator orderEntryCreateValidator;
	@Resource(name = "paymentProviderRequestSupportedStrategy")
	private PaymentProviderRequestSupportedStrategy paymentProviderRequestSupportedStrategy;
	@Resource(name = "gpCarFacade")
	private GPCartFacade gpCarFacade;
	@Resource(name = "gpCheckoutFacade")
	private GPCheckoutFacade gpCheckoutFacade;	
	@Resource(name = "splitEntryWsDTOValidator")
	private Validator splitEntryWsDTOValidator;
	@Resource(name = "splitEntryListWsDTOValidator")
	private Validator splitEntryListWsDTOValidator;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource(name="subsCartFacade")
	private GPSubscriptionDefaultCartFacade subsCartFacade ;
	private static final String CHECKOUTPAGE="checkout";

	protected AddressData createAddressInternal(final HttpServletRequest request) throws WebserviceValidationException //NOSONAR
	{
		final AddressData addressData = new AddressData();
		httpRequestAddressDataPopulator.populate(request, addressData);

		validate(addressData, "addressData", addressValidator);

		return createAddressInternal(addressData);
	}

	protected AddressData createAddressInternal(final AddressData addressData)
	{
		addressData.setShippingAddress(true);
		addressData.setVisibleInAddressBook(true);
		userFacade.addAddress(addressData);
		if (addressData.isDefaultAddress())
		{
			userFacade.setDefaultAddress(addressData);
		}
		return addressData;
	}

	protected CartData setCartDeliveryAddressInternal(final String addressId) throws NoCheckoutCartException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("setCartDeliveryAddressInternal: " + logParam("addressId", addressId));
		}
		final AddressData address = new AddressData();
		address.setId(addressId);
		final Errors errors = new BeanPropertyBindingResult(address, "addressData");
		deliveryAddressValidator.validate(address, errors);
		if (errors.hasErrors())
		{
			throw new CartAddressException("Address given by id " + sanitize(addressId) + " is not valid",
					CartAddressException.NOT_VALID, addressId);
		}
		if (checkoutFacade.setDeliveryAddress(address))
		{
			return getSessionCart();
		}
		throw new CartAddressException("Address given by id " + sanitize(addressId)
				+ " cannot be set as delivery address in this cart", CartAddressException.CANNOT_SET, addressId);
	}

	protected CartData setCartDeliveryModeInternal(final String deliveryModeId) throws UnsupportedDeliveryModeException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("setCartDeliveryModeInternal: " + logParam("deliveryModeId", deliveryModeId));
		}
		if (checkoutFacade.setDeliveryMode(deliveryModeId))
		{
			return getSessionCart();
		}
		throw new UnsupportedDeliveryModeException(deliveryModeId);
	}

	protected CartData applyVoucherForCartInternal(final String voucherId) throws NoCheckoutCartException,
			VoucherOperationException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("apply voucher: " + logParam("voucherId", voucherId));
		}
		if (!checkoutFacade.hasCheckoutCart())
		{
			throw new NoCheckoutCartException("Cannot apply voucher. There was no checkout cart created yet!");
		}
		try{
			voucherFacade.applyVoucher(voucherId);
		}catch(VoucherOperationException ex){
			throw new GPInvalidPromotionException("109",messageSource.getMessage(ex.getMessage(), null, "Promo code entered is invalid or out of date", i18nService.getCurrentLocale()),ex);
		}
		return getSessionCart();
	}

	protected CartData addPaymentDetailsInternal(final HttpServletRequest request,String cartId) 
			throws WebserviceValidationException, InvalidPaymentInfoException, NoCheckoutCartException //NOSONAR
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("addPaymentInfo");
		}
		if (!checkoutFacade.hasCheckoutCart())
		{
			throw new NoCheckoutCartException("Cannot add PaymentInfo. There was no checkout cart created yet!");
		}

		final CCPaymentInfoData paymentInfoData = new CCPaymentInfoData();
		final Errors errors = new BeanPropertyBindingResult(paymentInfoData, "paymentInfoData");

		final Collection<PaymentInfoOption> options = new ArrayList<PaymentInfoOption>();
		options.add(PaymentInfoOption.BASIC);
		options.add(PaymentInfoOption.BILLING_ADDRESS);

		httpRequestPaymentInfoPopulator.populate(request, paymentInfoData, options);
		ccPaymentInfoValidator.validate(paymentInfoData, errors);

		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}

		
		return addPaymentDetailsInternal(paymentInfoData,cartId, StringUtils.EMPTY);
	}

	protected CartData addPaymentDetailsInternal(final CCPaymentInfoData paymentInfoData,String cartId, String pageId) throws InvalidPaymentInfoException
	{
		boolean isCheckout = false;
		if(null != pageId && pageId.equals(CHECKOUTPAGE)) {
			isCheckout = true;
		}
		final CCPaymentInfoData createdPaymentInfoData = checkoutFacade.createPaymentSubscription(paymentInfoData);

		if (createdPaymentInfoData == null)
		{
			throw new InvalidPaymentInfoException("null");
		}

		if (createdPaymentInfoData.isSaved() && paymentInfoData.isDefaultPaymentInfo())
		{
			userFacade.setDefaultPaymentInfo(createdPaymentInfoData);
		}
		if(paymentInfoData.isDefaultPaymentInfo() || isCheckout) {
		if (checkoutFacade.setPaymentDetails(createdPaymentInfoData.getId()))
		{
			if(cartsHelper.checkSubscriptionCart(cartId)) {
				CartData subscriptionCart=((CommerceWebServicesCartFacade)getCartFacade()).getSubscriptionCart(cartId) ;
				subscriptionCart.setPaymentInfo(createdPaymentInfoData);
				return subscriptionCart ; 
			}else {
			return getSessionCart();
			}
		}
		}else {
			return  getSessionCart();
		}
		throw new InvalidPaymentInfoException(createdPaymentInfoData.getId());
	}

	protected CartData setPaymentDetailsInternal(final String paymentDetailsId, final String cartId)
			throws InvalidPaymentInfoException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("setPaymentDetailsInternal: " + logParam("paymentDetailsId", paymentDetailsId));
		}
		if (cartsHelper.checkSubscriptionCart(cartId)) {
			if (subscriptionCartFacadeImpl.setPaymentDetails(paymentDetailsId, cartId)) {
				return commerceWebServicesCartFacade2.getSubscriptionCart(cartId);
			}
			throw new InvalidPaymentInfoException(paymentDetailsId);
		} else {
			if (checkoutFacade.setPaymentDetails(paymentDetailsId)) {
				return getSessionCart();
			}
			throw new InvalidPaymentInfoException(paymentDetailsId);
		}

	}

	protected void validateCartForPlaceOrder() 
			throws NoCheckoutCartException, InvalidCartException, WebserviceValidationException //NOSONAR
	{
		if (!checkoutFacade.hasCheckoutCart())
		{
			throw new NoCheckoutCartException("Cannot place order. There was no checkout cart created yet!");
		}

		final CartData cartData = getSessionCart();

		final Errors errors = new BeanPropertyBindingResult(cartData, "sessionCart");
		placeOrderCartValidator.validate(cartData, errors);
		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}

		try
		{
			final List<CartModificationData> modificationList = cartFacade.validateCartData();
			StringBuilder sb = new StringBuilder();
			if (modificationList != null && !modificationList.isEmpty())
			{
				for(CartModificationData data : modificationList)
				{
					sb.append(configurationService.getConfiguration().getString("gp.low.stock.message.checkout"));
				}
				throw new WebserviceValidationException(sb.toString());
			}
		}
		catch (final CommerceCartModificationException e)
		{
			throw new InvalidCartException(e);
		}
	}
	
	protected void validateCartForPlaceOrder(String cartId) throws NoCheckoutCartException, InvalidCartException {
		if (!checkoutFacade.hasCheckoutCart())
		{
			throw new NoCheckoutCartException("Cannot place order. There was no checkout cart created yet!");
		}

		final CartData cartData = commerceWebServicesCartFacade2.getSubscriptionCart(cartId) ;

		final Errors errors = new BeanPropertyBindingResult(cartData, "sessionCart");
		placeOrderCartValidator.validate(cartData, errors);
		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}

		try
		{
			final List<CartModificationData> modificationList = cartFacade.validateCartData();
			StringBuilder sb = new StringBuilder();
			if (modificationList != null && !modificationList.isEmpty())
			{
				for(CartModificationData data : modificationList)
				{
					sb.append(configurationService.getConfiguration().getString("gp.low.stock.message.checkout"));
				}
				throw new WebserviceValidationException(sb.toString());
			}
		}
		catch (final CommerceCartModificationException e)
		{
			throw new InvalidCartException(e);
		}
		
	}

	protected CartData getSessionCart()
	{
		return cartFacade.getSessionCart();
	}

	/**
	 * Checks if given statuses are valid
	 *
	 * @param statuses
	 */
	protected void validateStatusesEnumValue(final String statuses)
	{
		if (statuses == null)
		{
			return;
		}

		final String[] statusesStrings = statuses.split(YcommercewebservicesConstants.OPTIONS_SEPARATOR);
		validate(statusesStrings, "", orderStatusValueValidator);
	}
	
	protected boolean isValidProductEntry(final ProductData productData)
	{
		return productData != null && StringUtils.isNotBlank(productData.getCode());
	}

	protected boolean isValidQuantity(final ProductData productData)
	{
		return productData.getCount() != null && productData.getCount().longValue() >= 1L;
	}
	protected ProductWrapperData createProductWrapperData(final String sku, final String errorMsg)
	{
		final ProductWrapperData productWrapperData = new ProductWrapperData();
		final ProductData productData = new ProductData();
		productData.setCode(sku);
		productWrapperData.setProductData(productData);
		productWrapperData.setErrorMsg(errorMsg);
		return productWrapperData;
	}
	
	protected CartWsDTO createCart( final String oldCartId,final String toMergeCartGuid, final String fields)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("createCart");
		}

		String evaluatedToMergeCartGuid = toMergeCartGuid;
		if (StringUtils.isEmpty(evaluatedToMergeCartGuid))
		{
			if(!getUserFacade().isAnonymousUser()) {
				final List<CartData> carts = getCartFacade().getCartsForCurrentUser();
				if(CollectionUtils.isNotEmpty(carts)) {
					evaluatedToMergeCartGuid = carts.get(0).getGuid();
				}

			}else {
				evaluatedToMergeCartGuid = getSessionCart().getGuid();
			}
		}

		if (StringUtils.isNotEmpty(oldCartId))
		{
			if (getUserFacade().isAnonymousUser())
			{
				throw new CartException("Anonymous user is not allowed to copy cart!");
			}

			if (!cartsHelper.isCartAnonymous(oldCartId))
			{
				throw new CartException("Cart is not anonymous", CartException.CANNOT_RESTORE, oldCartId);
			}

			if (StringUtils.isEmpty(evaluatedToMergeCartGuid))
			{
				evaluatedToMergeCartGuid = getSessionCart().getGuid();
			}
			else
			{
				if (!cartsHelper.isUserCart(evaluatedToMergeCartGuid))
				{
					throw new CartException("Cart is not current user's cart", CartException.CANNOT_RESTORE, evaluatedToMergeCartGuid);
				}
			}

			try
			{
				getCartFacade().restoreAnonymousCartAndMerge(oldCartId, evaluatedToMergeCartGuid);
				return getDataMapper().map(getSessionCart(), CartWsDTO.class, fields);
			}
			catch (final CommerceCartMergingException e)
			{
				throw new CartException("Couldn't merge carts", CartException.CANNOT_MERGE, e);
			}
			catch (final CommerceCartRestorationException e)
			{
				throw new CartException("Couldn't restore cart", CartException.CANNOT_RESTORE, e);
			}
		}
		else
		{
			if (StringUtils.isNotEmpty(evaluatedToMergeCartGuid))
			{
				if (!cartsHelper.isUserCart(evaluatedToMergeCartGuid))
				{
					throw new CartException("Cart is not current user's cart", CartException.CANNOT_RESTORE, evaluatedToMergeCartGuid);
				}

				try
				{
					getCartFacade().restoreSavedCart(evaluatedToMergeCartGuid);
					return getDataMapper().map(getSessionCart(), CartWsDTO.class, fields);
				}
				catch (final CommerceCartRestorationException e)
				{
					throw new CartException("Couldn't restore cart", CartException.CANNOT_RESTORE, oldCartId, e);
				}

			}
			return getDataMapper().map(getSessionCart(), CartWsDTO.class, fields);
		}
	}

	protected void logDebug(String msg) {
		if (LOG.isDebugEnabled())
		{
			LOG.debug(msg);
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected CartFacade getCartFacade()
	{
		return cartFacade;
	}

	protected void setCartFacade(final CartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
	}

	protected CheckoutFacade getCheckoutFacade()
	{
		return checkoutFacade;
	}

	protected void setCheckoutFacade(final CheckoutFacade checkoutFacade)
	{
		this.checkoutFacade = checkoutFacade;
	}

	protected VoucherFacade getVoucherFacade()
	{
		return voucherFacade;
	}

	protected void setVoucherFacade(final VoucherFacade voucherFacade)
	{
		this.voucherFacade = voucherFacade;
	}

	protected Validator getDeliveryAddressValidator()
	{
		return deliveryAddressValidator;
	}

	protected void setDeliveryAddressValidator(final Validator deliveryAddressValidator)
	{
		this.deliveryAddressValidator = deliveryAddressValidator;
	}

	protected Populator<HttpServletRequest, AddressData> getHttpRequestAddressDataPopulator()
	{
		return httpRequestAddressDataPopulator;
	}

	protected void setHttpRequestAddressDataPopulator(
			final Populator<HttpServletRequest, AddressData> httpRequestAddressDataPopulator)
	{
		this.httpRequestAddressDataPopulator = httpRequestAddressDataPopulator;
	}

	protected Validator getAddressValidator()
	{
		return addressValidator;
	}

	protected void setAddressValidator(final Validator addressValidator)
	{
		this.addressValidator = addressValidator;
	}

	protected Validator getAddressDTOValidator()
	{
		return addressDTOValidator;
	}

	protected void setAddressDTOValidator(final Validator addressDTOValidator)
	{
		this.addressDTOValidator = addressDTOValidator;
	}

	protected UserFacade getUserFacade()
	{
		return userFacade;
	}

	protected void setUserFacade(final UserFacade userFacade)
	{
		this.userFacade = userFacade;
	}

	protected Validator getCcPaymentInfoValidator()
	{
		return ccPaymentInfoValidator;
	}

	protected void setCcPaymentInfoValidator(final Validator ccPaymentInfoValidator)
	{
		this.ccPaymentInfoValidator = ccPaymentInfoValidator;
	}

	protected Validator getPaymentDetailsDTOValidator()
	{
		return paymentDetailsDTOValidator;
	}

	protected void setPaymentDetailsDTOValidator(final Validator paymentDetailsDTOValidator)
	{
		this.paymentDetailsDTOValidator = paymentDetailsDTOValidator;
	}

	protected ConfigurablePopulator<HttpServletRequest, CCPaymentInfoData, PaymentInfoOption> getHttpRequestPaymentInfoPopulator()
	{
		return httpRequestPaymentInfoPopulator;
	}

	protected void setHttpRequestPaymentInfoPopulator(
			final ConfigurablePopulator<HttpServletRequest, CCPaymentInfoData, PaymentInfoOption> httpRequestPaymentInfoPopulator)
	{
		this.httpRequestPaymentInfoPopulator = httpRequestPaymentInfoPopulator;
	}

	public CartsHelper getCartsHelper() {
		return cartsHelper;
	}

	public GPSubscriptionFacade getSubscriptionCartFacadeImpl() {
		return subscriptionCartFacadeImpl;
	}

	public CommercePromotionRestrictionFacade getCommercePromotionRestrictionFacade() {
		return commercePromotionRestrictionFacade;
	}

	public CustomerFacade getCustomerFacade() {
		return customerFacade;
	}

	public Validator getPointOfServiceValidator() {
		return pointOfServiceValidator;
	}

	public PaymentProviderRequestSupportedStrategy getPaymentProviderRequestSupportedStrategy() {
		return paymentProviderRequestSupportedStrategy;
	}


	public GPCartFacade getGpCarFacade() {
		return gpCarFacade;
	}

	public GPCheckoutFacade getGpCheckoutFacade() {
		return gpCheckoutFacade;
	}

	public void setGpCheckoutFacade(GPCheckoutFacade gpCheckoutFacade) {
		this.gpCheckoutFacade = gpCheckoutFacade;
	}

	public Validator getSplitEntryWsDTOValidator() {
		return splitEntryWsDTOValidator;
	}

	public Validator getSplitEntryListWsDTOValidator() {
		return splitEntryListWsDTOValidator;
	}

	public SessionService getSessionService() {
		return sessionService;
	}
	
	public Validator getOrderEntryCreateValidator() {
		return orderEntryCreateValidator;
	}

	public GPSubscriptionDefaultCartFacade getSubsCartFacade() {
		return subsCartFacade;
	}

}

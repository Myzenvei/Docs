/*
O * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.v2.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.gp.commerce.cart.impl.CommerceWebServicesCartFacade;
import com.gp.commerce.core.exceptions.GPCommerceBusinessException;
import com.gp.commerce.core.exceptions.GPException;
import com.gp.commerce.core.exceptions.GPSampleCartException;
import com.gp.commerce.core.util.GPApplePaySessionResponseDTO;
import com.gp.commerce.data.cart.GPApplePayPaymentTokenData;
import com.gp.commerce.dto.GPScheduleInstallationWsDTO;
import com.gp.commerce.dto.GooglePayRequestWsDTO;
import com.gp.commerce.dto.cart.CartValidationWsDTO;
import com.gp.commerce.dto.cart.GPApplePayPaymentTokenDetailsWsDTO;
import com.gp.commerce.dto.cart.GPApplePaySessionRequestDTO;
import com.gp.commerce.dto.cart.GPCartModificationListWSDTO;
import com.gp.commerce.dto.cart.GPMultipleProductWsDTO;
import com.gp.commerce.dto.cart.GPQuickOrderWSDTO;
import com.gp.commerce.dto.cart.PayPalSavePayerWsDTO;
import com.gp.commerce.dto.cybersource.GPSignatureDTO;
import com.gp.commerce.dto.group.dto.DeliveryAddressGroupWsDTO;
import com.gp.commerce.dto.payment.PaymentDataWsDTO;
import com.gp.commerce.exceptions.InvalidPaymentInfoException;
import com.gp.commerce.exceptions.NoCheckoutCartException;
import com.gp.commerce.exceptions.UnknownResourceException;
import com.gp.commerce.exceptions.UnsupportedDeliveryModeException;
import com.gp.commerce.exceptions.UnsupportedRequestException;
import com.gp.commerce.facade.data.PaypalPayerData;
import com.gp.commerce.facade.order.data.GPRestrictionProductListWsDTO;
import com.gp.commerce.facade.order.data.LeaseAgreementData;
import com.gp.commerce.facade.order.data.LeaseAgreementWsDTO;
import com.gp.commerce.facade.order.data.SaveAgreementData;
import com.gp.commerce.facade.order.data.SaveAgreementWsDTO;
import com.gp.commerce.facade.order.data.SplitEntryListWsDTO;
import com.gp.commerce.facades.data.GooglePayRequestData;
import com.gp.commerce.facades.subscription.data.GPSubscriptionFrequencyListData;
import com.gp.commerce.facades.subscription.dto.GPSubscriptionFrequencyListWsDTO;
import com.gp.commerce.order.data.GPApplePayPaymentInfoData;
import com.gp.commerce.order.data.GPSampleCartResponseData;
import com.gp.commerce.order.data.GooglePayPaymentInfoData;
import com.gp.commerce.order.data.OrderEntryDataList;
import com.gp.commerce.order.entry.data.GPSplitListWsDTO;
import com.gp.commerce.order.entry.data.GiftWrapListWsDTO;
import com.gp.commerce.swagger.ApiBaseSiteIdAndUserIdParam;
import com.gp.commerce.swagger.ApiBaseSiteIdUserIdAndCartIdParam;
import com.gp.commerce.user.data.AddressDataList;
import com.gp.commerce.voucher.data.VoucherDataList;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.leaseagreement.dto.GPLeaseAgreementDTO;
import com.gpintegration.utils.GPSignatureResponseDTO;
import de.hybris.platform.acceleratorcms.model.components.CartSuggestionComponentModel;
import de.hybris.platform.cms2.misc.UrlUtils;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.DeliveryModesData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.promotion.CommercePromotionRestrictionException;
import de.hybris.platform.commercewebservicescommons.dto.order.CartListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.CartModificationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.CartWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.DeliveryModeListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.DeliveryModeWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PromotionResultListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.voucher.VoucherListWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.ProductLowStockException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.StockSystemException;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.Authorization;

@Secured(
{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT","ROLE_ASAGENTSALESMANAGERGROUP" ,"ROLE_CLIENT"})
@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/carts")
@CacheControl(directive = CacheControlDirective.NO_CACHE)
@Api(tags = "Carts")
public class CartsController extends BaseCommerceController
{
	private static final Logger LOG = Logger.getLogger(BaseCommerceController.class);
	private static final String ADD_PAYMENT_DETAILS = "addPaymentDetails";
	private static final String ENTRY_NUMBER = "entryNumber";
	private static final long DEFAULT_PRODUCT_QUANTITY = 1;
	private static final String CART = "cart";
	private static final String CALCULATION_TYPE="calculationType";
	private static final String GUEST="guest";
	private static final String PICKUP_STORE="pickupStore";
	private static final String ENTRY="entry";
	private static final String SPLIT_ENTRY = "splitEntry";

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get all customer carts.", notes = "Lists all customer carts.")
	@ApiBaseSiteIdAndUserIdParam
	public CartListWsDTO getCarts(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@ApiParam(value = "Optional parameter. If the parameter is provided and its value is true only saved carts are returned.") @RequestParam(required = false, defaultValue = "false") final boolean savedCartsOnly,
			@ApiParam(value = "Optional pagination parameter in case of savedCartsOnly == true. Default value 0.") @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "Optional {@link PaginationData} parameter in case of savedCartsOnly == true. Default value 20.") @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Optional sort criterion in case of savedCartsOnly == true. No default value.") @RequestParam(required = false) final String sort)
	{
		if (getUserFacade().isAnonymousUser()) {
			throw new AccessDeniedException("Access is denied");
		}
		return getCartsHelper().getCarts(savedCartsOnly, currentPage, pageSize, sort, fields);
	}

	@RequestMapping(value = "/{cartId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get a cart with a given identifier.", notes = "Returns the cart with a given identifier.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartWsDTO getCart(@RequestParam(required = false, defaultValue = CART) final String calculationType,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,final HttpServletRequest httpRequest, final HttpServletResponse httpResponse)
	{
		getSessionService().setAttribute(CALCULATION_TYPE, calculationType);
		final CartData cart = getSessionCart();
		final CartWsDTO cartDto = getCartsHelper().getCart(cart, fields);
		httpResponse.setHeader("Cache-Control","no-cache, no-store, max-age=0, must-revalidate");  //NOSONAR
		return cartDto;
	}

	@RequestMapping(value = "/{cartId}/deliverygroup", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get Delivery group with a given identifier.", notes = "Returns the Delivery group for that cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public DeliveryAddressGroupWsDTO getDeliveryGroup(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final CartWsDTO cart= getDataMapper().map(getSessionCart(), CartWsDTO.class, fields);
		return getCartsHelper().getDeliveryGroup(cart);
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(value = "Creates or restore a cart for a user.", notes = "Creates a new cart or restores an anonymous cart as a user's cart (if an old Cart Id is given in the request).")
	@ApiBaseSiteIdAndUserIdParam
	public CartWsDTO createCart(@ApiParam(value = "Anonymous cart GUID.") @RequestParam(required = false) final String oldCartId,
			@ApiParam(value = "User's cart GUID to merge anonymous cart to.") @RequestParam(required = false) final String toMergeCartGuid,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		return super.createCart(oldCartId, toMergeCartGuid, fields);
	}

	@RequestMapping(value = "/{cartId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Deletes a cart with a given cart id.", notes = "Deletes a cart with a given cart id.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void deleteCart() {
		getCartFacade().removeSessionCart();
	}

	@Secured(
	{ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/email", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Assigns an email to the cart.", notes = "Assigns an email to the cart. This step is required to make a guest checkout.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void guestLogin(
			@ApiParam(value = "Email of the guest user. It will be used during checkout process.", required = true) @RequestParam final String email)
			throws DuplicateUidException
	{
		super.logDebug("createGuestUserForAnonymousCheckout: email=" + sanitize(email));
		if (!EmailValidator.getInstance().isValid(email)) {
			throw new RequestParameterException("Email [" + sanitize(email) + "] is not a valid e-mail address!",
					RequestParameterException.INVALID, "login");
		}
		getCustomerFacade().createGuestUserForAnonymousCheckout(email, GUEST);
	}

	@RequestMapping(value = "/{cartId}/entries", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get cart entries.", notes = "Returns cart entries.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public OrderEntryListWsDTO getCartEntries(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		super.logDebug("getCartEntries");
		final OrderEntryDataList dataList = new OrderEntryDataList();
		dataList.setOrderEntries(getSessionCart().getEntries());
		return getDataMapper().map(dataList, OrderEntryListWsDTO.class, fields);
	}

	@RequestMapping(value = "/{cartId}/multiEntries", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(hidden = true, value = "Adds a product to the cart.", notes = "Adds a product to the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public List<CartModificationWsDTO> addCartMultipleEntries(@ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,
			@ApiParam(value = "List of product codes to be added to cart. The format of a serialized query: productCode1:productCode2:productCode3:productCode4") @RequestParam(required = true) final String code,
			@ApiParam(value = "Quantity of product.The format of a serialized query: productCode1Qty:productCode2Qty:productCode3Qty:productCode4Qty") @RequestParam(required = true) final String qty,
			@ApiParam(value = "Name of the store where product will be picked. Set only if want to pick up from a store.") @RequestParam(required = false) final String pickupStore,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws CommerceCartModificationException, WebserviceValidationException, ProductLowStockException, StockSystemException //NOSONAR
	{
		super.logDebug("addCartEntry: " + logParam("code", code) + ", " + logParam("qty", qty) + ", "
				+ logParam(PICKUP_STORE, pickupStore));
		if (StringUtils.isNotEmpty(pickupStore)) {
			validate(pickupStore, PICKUP_STORE, getPointOfServiceValidator());
		}
		return getCartsHelper().addCartMultipleEntriesInternal(baseSiteId, code, qty, pickupStore, fields);
	}
	
    @RequestMapping(value = "/{cartId}/multipleproducts", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(hidden = true, value = "Adds a product to the cart.", notes = "Adds a product to the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public List<CartModificationWsDTO> addMultipleProductsToCart(@ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,
			@ApiParam(value = "Quick Order form body ", required = true) @RequestBody final GPMultipleProductWsDTO multiProdForm,
			@ApiParam(value = "Name of the store where product will be picked. Set only if want to pick up from a store.") @RequestParam(required = false) final String pickupStore,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
					throws CommerceCartModificationException, WebserviceValidationException, ProductLowStockException, StockSystemException //NOSONAR
	{
		if (StringUtils.isNotEmpty(pickupStore)) {
			validate(pickupStore, PICKUP_STORE, getPointOfServiceValidator());
		}
		return getCartsHelper().addMultipleProductsInternal(baseSiteId, multiProdForm, pickupStore, fields);
	}

	@RequestMapping(value = "/{cartId}/entries", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(hidden = true, value = "Adds a product to the cart.", notes = "Adds a product to the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartModificationWsDTO addCartEntry(@ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,
			@ApiParam(value = "Code of the product to be added to cart. Product look-up is performed for the current product catalog version.") @RequestParam(required = true) final String code,
			@ApiParam(value = "Quantity of product.") @RequestParam(required = false, defaultValue = "1") final long qty,
			@ApiParam(value = "Name of the store where product will be picked. Set only if want to pick up from a store.") @RequestParam(required = false) final String pickupStore,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws CommerceCartModificationException, WebserviceValidationException, ProductLowStockException, StockSystemException //NOSONAR
	{
		super.logDebug("addCartEntry: " + logParam("code", code) + ", " + logParam("qty", qty) + ", " + logParam(PICKUP_STORE, pickupStore));
		if (StringUtils.isNotEmpty(pickupStore)) {
			validate(pickupStore, PICKUP_STORE, getPointOfServiceValidator());
		}
		return getCartsHelper().addCartEntryInternal(baseSiteId, code, qty, pickupStore, null, fields);
	}

	@RequestMapping(value = "/{cartId}/entries", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Adds a product to the cart.", notes = "Adds a product to the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartModificationWsDTO addCartEntry(@ApiParam(value = "Base site identifier") @PathVariable final String baseSiteId,
			@ApiParam(value = "Request body parameter (DTO in xml or json format) which contains details like : "
					+ "product code (product.code), quantity of product (quantity), pickup store name (deliveryPointOfService.name)", required = true) @RequestBody final OrderEntryWsDTO entry,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws CommerceCartModificationException, WebserviceValidationException, ProductLowStockException, StockSystemException //NOSONAR
	{
		if (entry.getQuantity() == null) {
			entry.setQuantity(Long.valueOf(DEFAULT_PRODUCT_QUANTITY));
		}
		validate(entry, ENTRY, getOrderEntryCreateValidator());
		final String pickupStore = entry.getDeliveryPointOfService() == null ? null : entry.getDeliveryPointOfService().getName();
		return getCartsHelper().addCartEntryInternal(baseSiteId, entry.getProduct().getCode(), entry.getQuantity().longValue(),
				pickupStore, entry.getAdditionalAttributes(), fields);
	}
	
	@RequestMapping(value = "/{cartId}/ordersamplecart", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "Sample current cart functionality", notes = "Generates share cart and makes call to GPExpress for checkout.")
	public GPSampleCartResponseData orderSample(
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws CommerceCartRestorationException, GPSampleCartException {

		final GPSampleCartResponseData gpSampleCartResponseData = getGpCarFacade().orderSampleCart();
		return gpSampleCartResponseData;
	}

	@RequestMapping(value = "/{cartId}/sharecart", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@ApiOperation(value = "Share current cart functionality", notes = "Generates share cart email with csv attachement to the recipient.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void shareCart(@ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,
			@ApiParam(value = "Recipient's emailId to share cart", required = true) @RequestParam final String recipientemails,
			@ApiParam(value = "Sender's email Id", required = true) @RequestParam final String senderEmail,
			@ApiParam(value = "Sender's name", required = true) @RequestParam final String senderName)
			throws CommerceCartRestorationException {
		final CartData cartData = getCartFacade().getSessionCartWithEntryOrdering(false);
		getGpCarFacade().shareCart(recipientemails,senderEmail,senderName, cartData);

	}

	@RequestMapping(value = "/{cartId}/entries/{entryNumber}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get the details of the cart entries.", notes = "Returns the details of the cart entries.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public OrderEntryWsDTO getCartEntry(
			@ApiParam(value = "Entry number. Zero-based numbering.", required = true) @PathVariable final long entryNumber,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		super.logDebug("getCartEntry: " + logParam(ENTRY_NUMBER, entryNumber));
		final OrderEntryData orderEntry = getCartsHelper().getCartEntryForNumber(getSessionCart(), entryNumber);
		return getDataMapper().map(orderEntry, OrderEntryWsDTO.class, fields);
	}

	@RequestMapping(value = "/{cartId}/entries/{entryNumber}", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(hidden = true, value = "Set quantity and store details of a cart entry.", notes = "Updates the quantity of a single cart entry and details of the store where the cart "
			+ "entry will be picked. Attributes not provided in request will be defined again (set to null or default)")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartModificationWsDTO setCartEntry(@ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,
			@ApiParam(value = "Entry number. Zero-based numbering.") @PathVariable final long entryNumber,
			@ApiParam(value = "Quantity of product.") @RequestParam(required = true) final Long qty,
			@ApiParam(value = "Name of the store where product will be picked. Set only if want to pick up from a store.") @RequestParam(required = false) final String pickupStore,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws CommerceCartModificationException
	{
		super.logDebug("setCartEntry: " + logParam(ENTRY_NUMBER, entryNumber) + ", " + logParam("qty", qty) + ", "
					+ logParam(PICKUP_STORE, pickupStore));
		final CartData cart = getSessionCart();
		final OrderEntryData orderEntry = getCartsHelper().getCartEntryForNumber(cart, entryNumber);
		if (!StringUtils.isEmpty(pickupStore)) {
			validate(pickupStore, PICKUP_STORE, getPointOfServiceValidator());
		}
		return getCartsHelper().updateCartEntryInternal(baseSiteId, cart, orderEntry, qty, pickupStore, null, fields, true);
	}

	@RequestMapping(value = "/{cartId}/multipleentries", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(hidden = true, value = "Adds a product to the cart.", notes = "Adds a product to the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public GPCartModificationListWSDTO addMultipleEntriesToCart(@ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,
			@ApiParam(value = "Quick Order form body ", required = true) @RequestBody final GPQuickOrderWSDTO quickOrderForm,
			@ApiParam(value = "Name of the store where product will be picked. Set only if want to pick up from a store.") @RequestParam(required = false) final String pickupStore,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
					throws CommerceCartModificationException, WebserviceValidationException, ProductLowStockException, StockSystemException //NOSONAR
	{
		return getCartsHelper().addMultipleEntriesToCart(quickOrderForm, fields);
	}

	@RequestMapping(value = "/{cartId}/entries/{entryNumber}", method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Set quantity and store details of a cart entry.", notes = "Updates the quantity of a single cart entry and details "
			+ "of the store where the cart entry will be picked. Attributes not provided in request will be defined again (set to null or default)")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartModificationWsDTO setCartEntry(@ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,
			@ApiParam(value = "Entry number. Zero-based numbering.", required = true) @PathVariable final long entryNumber,
			@ApiParam(value = "Request body parameter (DTO in xml or json format) which contains details like : quantity of product (quantity), pickup store name (deliveryPointOfService.name)", required = true) @RequestBody final OrderEntryWsDTO entry,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws CommerceCartModificationException
	{
		final CartData cart = getSessionCart();
		final OrderEntryData orderEntry = getCartsHelper().getCartEntryForNumber(cart, entryNumber);
		final String pickupStore = entry.getDeliveryPointOfService() == null ? null : entry.getDeliveryPointOfService().getName();
		getCartsHelper().validateCartEntryForReplace(orderEntry, entry);
		if(entry.isInstalled()) {
			((CommerceWebServicesCartFacade) getCartFacade()).updateInstalledQuantity(entryNumber, entry.getQuantity());
		}
		return getCartsHelper().updateCartEntryInternal(baseSiteId, cart, orderEntry, entry.getQuantity(), pickupStore,
				entry.getAdditionalAttributes(), fields, true);
	}

	@RequestMapping(value = "/{cartId}/entries/{entryNumber}", method = RequestMethod.PATCH, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Update quantity and store details of a cart entry.", notes = "Updates the quantity of a single cart entry and details of the store where the cart entry will be picked.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartModificationWsDTO updateCartEntry(@ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,
			@ApiParam(value = "Entry number. Zero-based numbering.", required = true) @PathVariable final long entryNumber,
			@ApiParam(value = "Request body parameter (DTO in xml or json format) which contains details like : quantity of product (quantity), pickup store name (deliveryPointOfService.name)", required = true) @RequestBody final OrderEntryWsDTO entry,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws CommerceCartModificationException
	{
		final CartData cart = getSessionCart();
		return getCartsHelper().updateCartEntry(cart, entry, fields, entryNumber, baseSiteId);
	}

	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/entries/splitentries", method = RequestMethod.PUT, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Update entry : split entry", notes = "Split entry with quantity and address.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void splitCartEntries(@ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,
			@ApiParam(value = "Request body parameter (DTO in xml or json format) which contains details like : quantity of product (quantity), pickup store name (deliveryPointOfService.name)", required = true) @RequestBody final GPSplitListWsDTO splitEntry,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,@ApiParam(value = "Subscription CartId.", required = false) @PathVariable final String cartId)
			throws CommerceCartModificationException
	{
		validate(splitEntry, SPLIT_ENTRY, getSplitEntryWsDTOValidator());
		((CommerceWebServicesCartFacade)getCartFacade()).updateSpliEntries(splitEntry,cartId);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/splitentries/deliverymode", method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Update entry : split entry", notes = "Split entry with quantity and address.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void updateSplitCartEntries(@ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,
			@ApiParam(value = "Request body parameter (DTO in xml or json format) which contains details like : quantity of product (quantity), pickup store name (deliveryPointOfService.name)", required = true) @RequestBody final List<SplitEntryListWsDTO> splitEntry,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws CommerceCartModificationException
	{
		for (final SplitEntryListWsDTO splitEntryListWsDTO : splitEntry) {
			validate(splitEntryListWsDTO, SPLIT_ENTRY, getSplitEntryListWsDTOValidator());
		}
		((CommerceWebServicesCartFacade) getCartFacade()).updateSpliEntriesWithDeliveryMode(splitEntry);
	}

	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/scheduleinstallation", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Update entry : split entry", notes = "Split entry with quantity and address.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void scheduleInstallation(@ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,
			@ApiParam(value = "Request body parameter (DTO in xml or json format) which contains information for schedule Installation", required = true) @RequestBody final GPScheduleInstallationWsDTO installationWsDTO,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws CommerceCartModificationException
	{
		((CommerceWebServicesCartFacade) getCartFacade()).scheduleInstallation(installationWsDTO);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/scheduleinstallation", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Update entry : split entry", notes = "Split entry with quantity and address.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public GPScheduleInstallationWsDTO getScheduleInstallation(
			@ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws CommerceCartModificationException
	{
		return getDataMapper().map(((CommerceWebServicesCartFacade) getCartFacade()).getScheduleInstallation(),
				GPScheduleInstallationWsDTO.class, fields);
	}

	@RequestMapping(value = "/{cartId}/entries/{entryNumber}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Deletes cart entry.", notes = "Deletes cart entry.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void removeCartEntry(
			@ApiParam(value = "Entry number. Zero-based numbering.", required = true) @PathVariable final long entryNumber)
			throws CommerceCartModificationException
	{
		super.logDebug("removeCartEntry: " + logParam(ENTRY_NUMBER, entryNumber));
		final CartData cart = getSessionCart();
		final CommerceWebServicesCartFacade commerceWebServicesCartFacade = (CommerceWebServicesCartFacade) getCartFacade();
		getCartsHelper().getCartEntryForNumber(cart, entryNumber);
		getCartFacade().updateCartEntry(entryNumber, 0);
		commerceWebServicesCartFacade.deleteInstallableProduct(entryNumber);
	}

	@Secured(
	{ "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP" })
	@RequestMapping(value = "/{cartId}/addresses/delivery", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(value = "Creates a delivery adress for the cart.", notes = "Creates an address and assigns it to the cart as the delivery address.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public AddressWsDTO createAndSetAddress(
			@ApiParam(value = "Request body parameter (DTO in xml or json format) which contains details like : Customer's first name(firstName), Customer's last name(lastName), Customer's title code(titleCode), "
					+ "country(country.isocode), first part of address(line1) , second part of address(line2), town (town), postal code(postalCode), region (region.isocode)", required = true) @RequestBody final AddressWsDTO address,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws WebserviceValidationException, NoCheckoutCartException //NOSONAR
	{
		if (LOG.isDebugEnabled()) {
			LOG.debug("createAddress");
		}
		AddressData addressData = getCartsHelper().createAndSetAddress(address, fields);
		addressData = createAddressInternal(addressData);
		setCartDeliveryAddressInternal(addressData.getId());
		return getDataMapper().map(addressData, AddressWsDTO.class, fields);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/addresses/delivery", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Sets a delivery address for the cart.", notes = "Sets a delivery address for the cart. The address country must be placed among the delivery countries of the current base store.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void setCartDeliveryAddress(
			@ApiParam(value = "Address identifier", required = true) @RequestParam(required = true) final String addressId)
			throws NoCheckoutCartException {
		super.setCartDeliveryAddressInternal(addressId);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/addresses/delivery", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Delete the delivery address from the cart.", notes = "Removes the delivery address from the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void removeCartDeliveryAddress() {
		super.logDebug("removeDeliveryAddress");
		if (!getCheckoutFacade().removeDeliveryAddress()) {
			throw new CartException("Cannot reset address!", CartException.CANNOT_RESET_ADDRESS);
		}
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/deliverymode", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get the delivery mode selected for the cart.", notes = "Returns the delivery mode selected for the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public DeliveryModeWsDTO getCartDeliveryMode(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		super.logDebug("getCartDeliveryMode");
		return getDataMapper().map(getSessionCart().getDeliveryMode(), DeliveryModeWsDTO.class, fields);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/deliverymode", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Sets the delivery mode for a cart.", notes = "Sets the delivery mode with a given identifier for the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void setCartDeliveryMode(
			@ApiParam(value = "Delivery mode identifier (code)", required = true) @RequestParam(required = true) final String deliveryModeId,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields ,
			@RequestParam(required = false) final String deliveryInst,
			@ApiParam(value = "Subscription CartId.", required = false) @PathVariable final String cartId)
			throws UnsupportedDeliveryModeException,GPException
	{
		
		if(getCartsHelper().checkSubscriptionCart(cartId)) {
			((CommerceWebServicesCartFacade)getCartFacade()).setSubscriptionCartDeliveryMode(deliveryModeId,cartId) ;
		}else {
		super.setCartDeliveryModeInternal(deliveryModeId);
		getGpCarFacade().updatePreviousDeliveryMode(deliveryModeId);
		}
		if(null!=deliveryInst) {
			getGpCarFacade().updateDeliveryInstruction(deliveryInst);
		}
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/deliverymode", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Delete the delivery mode from the cart.", notes = "Removes the delivery mode from the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void removeDeliveryMode() {
		super.logDebug("removeDeliveryMode");
		if (!getCheckoutFacade().removeDeliveryMode()) {
			throw new CartException("Cannot reset delivery mode!", CartException.CANNOT_RESET_DELIVERYMODE);
		}
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_ASAGENTSALESMANAGERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/deliverymodes", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get all delivery modes for the current store and delivery address.", notes = "Returns all delivery modes supported for the current "
			+ "base store and cart delivery address. A delivery address must be set for the cart, otherwise an empty list will be returned.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public DeliveryModeListWsDTO getSupportedDeliveryModes(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@ApiParam(value = "Subscription CartId.", required = true) @PathVariable final String cartId) throws Exception
	{
		super.logDebug("getSupportedDeliveryModes");
		final DeliveryModesData deliveryModesData = new DeliveryModesData();
		if(getCartsHelper().checkSubscriptionCart(cartId)) {
			deliveryModesData.setDeliveryModes(getSubscriptionCartFacadeImpl().getSupportedDeliveryModes(cartId));
		}else {
		deliveryModesData.setDeliveryModes(getGpCarFacade().getSupportedDeliveryModes());
		}
		return getDataMapper().map(deliveryModesData, DeliveryModeListWsDTO.class, fields);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_ASAGENTSALESMANAGERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/multideliverymodes", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get all delivery modes for the current store and delivery address.", notes = "Returns all delivery modes supported for the current "
			+ "base store and cart delivery address. A delivery address must be set for the cart, otherwise an empty list will be returned.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public DeliveryAddressGroupWsDTO getSupportedDeliveryModesforMultishipping(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) throws Exception
	{
		super.logDebug("getSupportedDeliveryModes");
		return getCartsHelper().getSupportedDeliveryModesforMultishipping(getSessionCart(), fields);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_ASAGENTSALESMANAGERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/paymentdetails", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(hidden = true, value = "Defines and assigns details of a new credit card payment to the cart.", notes = "Defines details of a new credit card payment details and assigns the payment to the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public PaymentDetailsWsDTO addPaymentDetails(final HttpServletRequest request, //NOSONAR
			@ApiParam( value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@ApiParam(value = "Subscription CartId.", required = true) @PathVariable final String cartId)
			throws WebserviceValidationException, InvalidPaymentInfoException, NoCheckoutCartException, UnsupportedRequestException //NOSONAR
	{
		getPaymentProviderRequestSupportedStrategy().checkIfRequestSupported(ADD_PAYMENT_DETAILS);
		final CCPaymentInfoData paymentInfoData = super.addPaymentDetailsInternal(request,cartId).getPaymentInfo();
		return getDataMapper().map(paymentInfoData, PaymentDetailsWsDTO.class, fields);
	}

	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_ASAGENTSALESMANAGERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/paymentsignature", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(hidden = true, value = "Defines and assigns details of a new credit card payment to the cart.", notes = "Defines details of a new credit card payment details and assigns the payment to the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public GPSignatureResponseDTO generateSignature(@PathVariable final String baseSiteId, @RequestBody final GPSignatureDTO signature, // NOSONAR
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws WebserviceValidationException, InvalidPaymentInfoException, NoCheckoutCartException, UnsupportedRequestException // NOSONAR
	{
		getPaymentProviderRequestSupportedStrategy().checkIfRequestSupported(ADD_PAYMENT_DETAILS);
		return ((CommerceWebServicesCartFacade)getCartFacade()).getSignature(signature, baseSiteId);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_ASAGENTSALESMANAGERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/paymentdetails", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(value = "Defines and assigns details of a new credit card payment to the cart.", notes = "Defines details of a new credit card payment details and assigns the payment to the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public PaymentDetailsWsDTO addPaymentDetails(final HttpServletRequest request,
			@ApiParam(value = "Request body parameter (DTO in xml or json format) which contains details like : Name on card (accountHolderName), card number(cardNumber), card type (cardType.code), "
					+ "Month of expiry date (expiryMonth), Year of expiry date (expiryYear), if payment details should be saved (saved), if if the payment details should be used as default (defaultPaymentInfo), "
					+ "billing address (billingAddress.firstName, billingAddress.lastName, billingAddress.titleCode, billingAddress.country.isocode, billingAddress.line1, billingAddress.line2, "
					+ "billingAddress.town, billingAddress.postalCode, billingAddress.region.isocode)", required = true) @RequestBody final PaymentDetailsWsDTO paymentDetails, //NOSONAR
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@ApiParam(value = "Subscription CartId.", required = true) @PathVariable final String cartId)
			throws WebserviceValidationException, InvalidPaymentInfoException, NoCheckoutCartException, UnsupportedRequestException //NOSONAR
	{
		getPaymentProviderRequestSupportedStrategy().checkIfRequestSupported(ADD_PAYMENT_DETAILS);
		getCartsHelper().validatePayment(paymentDetails);
		final String copiedfields = "accountHolderName,cardNumber,paymentToken,cardType,cardTypeData(code),expiryMonth,expiryYear,issueNumber,startMonth,startYear,subscriptionId,defaultPaymentInfo,saved,"
				+ "billingAddress(titleCode,firstName,lastName,line1,line2,town,postalCode,country(isocode),region(isocode),defaultAddress)";
		CCPaymentInfoData paymentInfoData = getDataMapper().map(paymentDetails, CCPaymentInfoData.class, copiedfields);
		paymentInfoData = addPaymentDetailsInternal(paymentInfoData,cartId, paymentDetails.getPageId()).getPaymentInfo();
		return getDataMapper().map(paymentInfoData, PaymentDetailsWsDTO.class, fields);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_ASAGENTSALESMANAGERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/paymentdetails", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Sets credit card payment details for the cart.", notes = "Sets credit card payment details for the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void setPaymentDetails(
			@ApiParam(value = "Payment details identifier.", required = true) @RequestParam(required = true) final String paymentDetailsId,
			@ApiParam(value = "Subscription CartId.", required = true) @PathVariable final String cartId)
			throws InvalidPaymentInfoException
	{
		super.setPaymentDetailsInternal(paymentDetailsId,cartId);
	}
	
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_ASAGENTSALESMANAGERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/paypalpaymentdetails", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "Sets credit card payment details for the cart.", notes = "Sets credit card payment details for the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public String getPayPalPayerDetails(final HttpServletRequest request,
			@ApiParam(value = "Payment details identifier.", required = true)  @RequestParam(required = true) final String paypalType)
			throws InvalidPaymentInfoException
	{
		final String hostUrl = UrlUtils.extractHostInformationFromRequest(request);
		String getPayerLink = getGpCheckoutFacade().authorizePayPalPayment(paypalType, hostUrl);
		if(null!= getPayerLink)
			return getPayerLink;
		else
			return StringUtils.EMPTY;
	}

	@RequestMapping(value = "/{cartId}/savePayPalPayerInfo", method = RequestMethod.POST)
	@ResponseBody
	@ApiBaseSiteIdUserIdAndCartIdParam
	public boolean savePayPalPaymentInfos(@RequestBody final PayPalSavePayerWsDTO payPalPayerInfo, @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields){
		return getGpCheckoutFacade().savePayPalDetails(getDataMapper().map(payPalPayerInfo, PaypalPayerData.class,fields));
	}
	
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_ASAGENTSALESMANAGERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/applepaysessiondetails", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "Get apple pay session details.", notes = "Get apple pay session details.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public GPApplePaySessionResponseDTO getApplePaySessionDetails(@RequestBody GPApplePaySessionRequestDTO applePaySessionRequestDTO,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		if (applePaySessionRequestDTO.getValidationURL() == null) {
			throw new UnknownResourceException("Apple Session Validation URL Cannot be Null");
		} else {
			return getGpCheckoutFacade().validateSessionDetails(applePaySessionRequestDTO.getValidationURL());
		}
	}
	
	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "{cartId}/saveApplePayPaymentInfo", method = RequestMethod.POST)
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	public boolean saveApplePayPaymentInfo(@RequestBody final GPApplePayPaymentTokenDetailsWsDTO applePayPaymentTokenDetailsWsDTO, @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		GPApplePayPaymentTokenData applePayPaymentTokenData = getDataMapper().map(applePayPaymentTokenDetailsWsDTO, GPApplePayPaymentTokenData.class, fields);
		GPApplePayPaymentInfoData gpApplePayPaymentInfoData = getGpCheckoutFacade().createApplePayPaymentInfo(applePayPaymentTokenData);
		if (gpApplePayPaymentInfoData == null) {
			return false;
		}
		return true;		
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_ASAGENTSALESMANAGERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "{cartId}/addGooglePayPaymentInfo", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "Process response from google pay and add payment info to cart .", notes = " Add google pay payment info to cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public boolean addGooglePayPaymentInfo(final HttpServletRequest request, final HttpServletResponse response,
										  @ApiParam(value = "Payment data response from Google Pay cart.", required = true)  @RequestBody(required = true) final PaymentDataWsDTO paymentDataWsDTO)
			throws WebserviceValidationException, GPCommerceBusinessException,InvalidPaymentInfoException
	{
		GooglePayPaymentInfoData googlePayPaymentInfoData=getGpCheckoutFacade().createGooglePayPaymentInfo(paymentDataWsDTO);
		if (googlePayPaymentInfoData == null) {
			return false;
		}
		return true;
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_GUEST", "ROLE_ASAGENTSALESMANAGERGROUP", "ROLE_CUSTOMERMANAGERGROUP",
					"ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/getGooglePayRequestParams", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Build the request to be sent to Google pay.", notes = "Return information that needs to be sent to google pay")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public GooglePayRequestWsDTO getGooglePayRequestParams(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields){
		GooglePayRequestData googlePayRequestData = getGpCheckoutFacade().buildGooglePayRequest();
	    return getDataMapper().map(googlePayRequestData, GooglePayRequestWsDTO.class, fields);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_GUEST", "ROLE_ASAGENTSALESMANAGERGROUP", "ROLE_CUSTOMERMANAGERGROUP",
			"ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/promotions", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get information about promotions applied on cart.", notes = "Return information about promotions applied on cart. Requests pertaining "
			+ "to promotions have been developed for the previous version of promotions and vouchers and therefore some of them are currently not compatible with the new promotion engine.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public PromotionResultListWsDTO getPromotions(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		super.logDebug("getPromotions");
		final CartData cart = getSessionCart();
		return getCartsHelper().getPromotions(cart, fields);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_GUEST", "ROLE_ASAGENTSALESMANAGERGROUP", "ROLE_CUSTOMERMANAGERGROUP",
			"ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/promotions/{promotionId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get information about promotions applied on cart.", notes = "Return information about promotion with given id, applied on cart. Requests pertaining to promotions "
			+ "have been developed for the previous version of promotions and vouchers and therefore some of them are currently not compatible with the new promotion engine.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public PromotionResultListWsDTO getPromotion(
			@ApiParam(value = "Promotion identifier (code)", required = true) @PathVariable final String promotionId,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		super.logDebug("getPromotion: promotionId = " + sanitize(promotionId));
		final CartData cart = getSessionCart();
		return getCartsHelper().getPromotion(cart, promotionId, fields);
	}

	@Secured(
	{ "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/promotions", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Enables promotions based on the promotionsId of the cart.", notes = "Enables the promotion for the order based on the promotionId defined for the cart. "
			+ "Requests pertaining to promotions have been developed for the previous version of promotions and vouchers and therefore some of them are currently not compatible with the new promotion engine.", authorizations =
	{ @Authorization(value = "oauth2_client_credentials") })
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void applyPromotion(
			@ApiParam(value = "Promotion identifier (code)", required = true) @RequestParam(required = true) final String promotionId)
			throws CommercePromotionRestrictionException
	{
		super.logDebug("applyPromotion: promotionId = " + sanitize(promotionId));
		getCommercePromotionRestrictionFacade().enablePromotionForCurrentCart(promotionId);
	}

	@Secured(
	{ "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{cartId}/promotions/{promotionId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Disables the promotion based on the promotionsId of the cart.", notes = "Disables the promotion for the order based on the promotionId defined for the cart. "
			+ "Requests pertaining to promotions have been developed for the previous version of promotions and vouchers and therefore some of them are currently not compatible with the new promotion engine.", authorizations =
	{ @Authorization(value = "oauth2_client_credentials") })
	@ApiBaseSiteIdUserIdAndCartIdParam
	@SuppressWarnings("squid:S1160")
	public void removePromotion(
			@ApiParam(value = "Promotion identifier (code)", required = true) @PathVariable final String promotionId) //NOSONAR
			throws CommercePromotionRestrictionException, NoCheckoutCartException //NOSONAR
	{
		super.logDebug("removePromotion: promotionId = " + sanitize(promotionId));
		getCommercePromotionRestrictionFacade().disablePromotionForCurrentCart(promotionId);
	}

	@Secured(
	{ "ROLE_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_GUEST",
			"ROLE_ASAGENTSALESMANAGERGROUP" })
	@RequestMapping(value = "/{cartId}/vouchers", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get a list of vouchers applied to the cart.", notes = "Returns list of vouchers applied to the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public VoucherListWsDTO getVouchers(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		super.logDebug("getVouchers");
		final VoucherDataList dataList = new VoucherDataList();
		dataList.setVouchers(getVoucherFacade().getVouchersForCart());
		return getDataMapper().map(dataList, VoucherListWsDTO.class, fields);
	}

	@Secured(
	{ "ROLE_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_GUEST",
			"ROLE_ASAGENTSALESMANAGERGROUP" })
	@RequestMapping(value = "/{cartId}/vouchers", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Applies a voucher based on the voucherId defined for the cart.", notes = "Applies a voucher based on the voucherId defined for the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	@SuppressWarnings("squid:S1160")
	public void applyVoucherForCart(@PathVariable final String baseSiteId,
			@ApiParam(value = "Voucher identifier (code)", required = true) @RequestParam(required = true) final String voucherId,
			@ApiParam(value = "Subscription CartId.", required = true) @PathVariable final String cartId) //NOSONAR
			throws NoCheckoutCartException, VoucherOperationException //NOSONAR
	{
		if(getGpCarFacade().validateCouponforSite(baseSiteId, voucherId)) {
			if(getCartsHelper().checkSubscriptionCart(cartId)) {
				getSubsCartFacade().applyVoucherForCartInternal(voucherId,cartId) ;
			}else {
			super.applyVoucherForCartInternal(voucherId);
			}
		}else {
			throw new GPException("111", "Promo code entered is invalid or out of date.");
		}
	}

	@Secured(
	{ "ROLE_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_GUEST",
			"ROLE_ASAGENTSALESMANAGERGROUP" })
	@RequestMapping(value = "/{cartId}/vouchers/{voucherId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Delete a voucher defined for the current cart.", notes = "Removes a voucher based on the voucherId defined for the current cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	@SuppressWarnings("squid:S1160")
	public void releaseVoucherFromCart(
			@ApiParam(value = "Voucher identifier (code)", required = true) @PathVariable final String voucherId,
			@ApiParam(value = "Subscription CartId.", required = true) @PathVariable final String cartId) //NOSONAR
			throws NoCheckoutCartException, VoucherOperationException //NOSONAR
	{
		super.logDebug("release voucher : voucherCode = " + sanitize(voucherId));
		if (!getCheckoutFacade().hasCheckoutCart()) {
			throw new NoCheckoutCartException("Cannot realese voucher. There was no checkout cart created yet!");
		}
		if(getCartsHelper().checkSubscriptionCart(cartId)) {
			getSubsCartFacade().releaseVoucher(voucherId,cartId) ;
		}
		getVoucherFacade().releaseVoucher(voucherId);
	}

	@Secured(
	{ "ROLE_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_GUEST",
			"ROLE_ASAGENTSALESMANAGERGROUP" })
	@RequestMapping(value = "/{cartId}/giftwrap", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Adds gift wrap with message to entry numbers products.", notes = "Adds gift wrap with message to entry numebers products.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void giftWrapOption(
			@ApiParam(value = "Request body parameter (DTO in xml or json format) which contains details like : "
					+ " entrynumber, giftMessage, giftWrapped", required = true) @RequestBody final GiftWrapListWsDTO giftWrap) throws CommerceCartModificationException
	{
		final CommerceWebServicesCartFacade commerceWebServicesCartFacade = (CommerceWebServicesCartFacade) getCartFacade();
		commerceWebServicesCartFacade.giftWrap(giftWrap);
	}

	@Secured(
	{ "ROLE_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_GUEST",
			"ROLE_ASAGENTSALESMANAGERGROUP" })
	@RequestMapping(value = "/{cartId}/suggestions", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get suggested products for the cart products with a given identifier.", notes = "Returns the suggested products for the carts products")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public List<ProductWsDTO> productList(final CartSuggestionComponentModel component,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) {
		return getCartsHelper().getProductList(component);
	}

	@RequestMapping(value = "/{cartId}/shoppingList/{wishlistuuid}", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(hidden = true, value = "Adds a product to the cart.", notes = "Adds a product to the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public List<CartModificationWsDTO> addShoppingList(
			@ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,
			@ApiParam(value = "List of product codes to be added to cart.") @PathVariable final String wishlistuuid,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws CommerceCartModificationException, WebserviceValidationException, ProductLowStockException, StockSystemException //NOSONAR
	{
		return getCartsHelper().addShoppingList(baseSiteId, wishlistuuid, fields);
	}

	@Secured({ "ROLE_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_GUEST", "ROLE_ASAGENTSALESMANAGERGROUP" })
	@RequestMapping(value = "/{cartId}/shippingRestriction", method = RequestMethod.PUT, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Gets the list of products and address", notes = "saves the address if no restricted products present")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public GPRestrictionProductListWsDTO getShppingRestrictions(@ApiParam(value = "Requestbody parameter (DTO in xml or json format) : productCode,countryIsoCode,region", required = true) @RequestBody final GPRestrictionProductListWsDTO shippingProductAdressList ,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,@ApiParam(value = "Subscription CartId.", required = false) @PathVariable final String cartId)
					throws CommerceCartModificationException,WebserviceValidationException, NoCheckoutCartException
	{
		return getCartsHelper().getShppingRestrictions(shippingProductAdressList, cartId,fields);
	}

	@Secured(
	{ "ROLE_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_GUEST",
			"ROLE_ASAGENTSALESMANAGERGROUP" })
	@RequestMapping(value = "/{cartId}/leaseagreement/{country}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get Lease Agreement for product. ", notes = "Returns the lease agreement for the leasable products")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public LeaseAgreementWsDTO getLeaseAgreement(
			@ApiParam(value = "Country for which lease agreement", required = true) @PathVariable final String country,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) {
		final LeaseAgreementData leaseData = getGpCarFacade().getLeaseAgreementForCountry(country);
		getGpCarFacade().updateLeaseName(leaseData.getLegalTermName());
		return getDataMapper().map(leaseData, LeaseAgreementWsDTO.class, fields);
	}

	@Secured(
	{ "ROLE_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_GUEST",
			"ROLE_ASAGENTSALESMANAGERGROUP" })
	@RequestMapping(value = "/{cartId}/saveleaseagreement", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Saves lease agreement when user accepts it. ", notes = "Saves lease agreement when user accepts it. ")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public SaveAgreementWsDTO saveLeaseAggrement(
			@ApiParam(value = "Request body parameter (DTO in xml or json format) which contains details like : "
					+ " LeaseTermsID, AgreementID, OrderDate...", required = true) @RequestBody final GPLeaseAgreementDTO saveLeaseAgreementReq,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws CommerceCartModificationException, GPIntegrationException
	{
		final SaveAgreementData leaseResponseData = getGpCarFacade().saveLeaseAgreement(saveLeaseAgreementReq);
		getGpCarFacade().updateLeaseAgreementId(leaseResponseData.getAgreementId(), saveLeaseAgreementReq.getLeaseTermsID());
		return getDataMapper().map(leaseResponseData, SaveAgreementWsDTO.class, fields);
	}

	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_GUEST", "ROLE_ASAGENTSALESMANAGERGROUP" })
	@RequestMapping(value = "/{cartId}/addresses", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get Guest customer's addresses", notes = "Returns customer's addresses.")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(code = 200, message = "List of customer's addresses")
	public AddressListWsDTO getAddresses(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields) {
		final List<AddressData> addresses = ((CommerceWebServicesCartFacade) getCartFacade()).getGuestUserAddressses();
		final AddressDataList addressDataList = new AddressDataList();
		addressDataList.setAddresses(addresses);
		return getDataMapper().map(addressDataList, AddressListWsDTO.class, fields);
	}

	@Secured(
	{ "ROLE_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_GUEST",
			"ROLE_ASAGENTSALESMANAGERGROUP" })
	@RequestMapping(value = "/{cartId}/validateCart", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Checks if cart contains validation errors", notes = "Returns response containing the validation errors.")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(code = 200, message = "Cart Validation Data")
	public CartValidationWsDTO validateCart() {
		return getCartsHelper().valiateCart();
	}
	
	@Secured(
	{ "ROLE_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_GUEST",
			"ROLE_ASAGENTSALESMANAGERGROUP" })
	@RequestMapping(value = "/{cartId}/checkIncompatibleProducts", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Checks if cart contains incompatible products", notes = "Returns response containing the name of incompatible products.")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(code = 200, message = "Incompatible Products Data")
	public CartValidationWsDTO checkIncompatibleProducts() {
		return getCartsHelper().checkIncompatibleProducts();
	}
	
	@Secured(
			{ "ROLE_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_GUEST",
					"ROLE_ASAGENTSALESMANAGERGROUP" })
			@RequestMapping(value = "/{cartId}/getSubscriptionFrequency", method = RequestMethod.GET)
			@ResponseBody
			@ApiOperation(value = "Fetches the subscription frequency", notes = "Returns response containing subscription frequency for each product.")
			@ApiBaseSiteIdAndUserIdParam
			public GPSubscriptionFrequencyListWsDTO getSubscriptionFrequency(@RequestParam String productCode,@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			{
		       final GPSubscriptionFrequencyListData subsFreqData= getSubscriptionCartFacadeImpl().getSubscriptionFrequency(productCode) ;
		       return getDataMapper().map(subsFreqData, GPSubscriptionFrequencyListWsDTO.class, fields);
			}
	
	@Secured(
			{ "ROLE_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_GUEST",
					"ROLE_ASAGENTSALESMANAGERGROUP" })
	@RequestMapping(value = "/{cartId}/addSubsCartEntries", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "Adds a product to the subscription cart.", notes = "Adds a subscription product to the cart.")
	public CartModificationWsDTO addSubscriptionCartEntry(@RequestParam(value = "cartId", required = false) final String cartId,@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
	      @ApiParam(value = "Request body parameter (DTO in xml or json format) which contains details like : "
			+ "product code (product.code), quantity of product (quantity), frequency", required = true) @RequestBody final OrderEntryWsDTO entry,final HttpServletResponse httpResponse)
			throws CommerceCartModificationException
	{
		getSubscriptionCartFacadeImpl().removeInActiveSubscriptions();
		CartModificationData cartModificationData= getSubscriptionCartFacadeImpl().addToCartInternal(entry.getProduct().getCode(), entry.getQuantity(), entry.getSubscriptionFrequency(), cartId);
		final Cookie cookie = new Cookie("subscrCartId", cartModificationData.getCartCode());
		cookie.setSecure(true);
		cookie.setMaxAge(60 * 60 * 24);
		cookie.setPath("/");
		httpResponse.addCookie(cookie);
		return getDataMapper().map(cartModificationData, CartModificationWsDTO.class, fields) ;
	}
}
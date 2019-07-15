/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.v2.helper;

import de.hybris.platform.acceleratorfacades.product.data.ProductWrapperData;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.CartModificationListData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commercewebservicescommons.dto.order.CartModificationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.CartWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PromotionResultListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PromotionResultWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartEntryException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.LowStockException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.ProductLowStockException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.StockSystemException;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.util.YSanitizer;
import com.gp.commerce.dto.cart.GPMultipleProductWsDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.gp.commerce.cart.impl.CommerceWebServicesCartFacade;
import com.gp.commerce.constants.YcommercewebservicesConstants;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.exceptions.GPLowStockException;
import com.gp.commerce.core.exceptions.GPMaximumStockException;
import com.gp.commerce.core.order.hook.impl.GPCommerceAddToCartMethodHook;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.dto.cart.CartValidationWsDTO;
import com.gp.commerce.dto.cart.GPCartModificationListWSDTO;
import com.gp.commerce.dto.cart.GPQuickOrderWSDTO;
import com.gp.commerce.facade.data.CartValidationData;
import com.gp.commerce.facade.data.IncompatibleProductsData;
import com.gp.commerce.facade.order.data.GPRestrictionProductWsDTO;
import com.gp.commerce.facade.order.data.ShippingRestrictionsData;
import com.gp.commerce.facades.cart.GPCartFacade;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gp.commerce.facades.data.WishlistData;
import com.gp.commerce.facades.data.WishlistEntryData;
import com.gp.commerce.facades.wishlist.GPWishlistFacade;
import com.gp.commerce.product.data.PromotionResultDataList;
import com.gp.commerce.stock.CommerceStockFacade;

@Component
public class CartsHelper extends GPCommonHelper
{
	private static final Logger LOG = Logger.getLogger(CartsHelper.class);
	private static final String FAILURE = "FAILURE";
	private static final String OUT_OF_STOCK = "OUT OF STOCK";
	private static final String COLON = ":";
	private static final String ENTRY = "entry";
	private static final String ERROR_CODE_197 = "197";
	private static final String ERROR_CODE_198 = "198";
	private static final String GP_LOW_STOCK_MESSAGE = "gp.low.stock.message.cart";
	private static final String GP_NO_STOCK_MESSAGE = "gp.no.stock.message.cart";
	private static final String GP_NO_STOCK_EMPTY_CART_MESSAGE = "gp.no.stock.empty.message.cart";
	private static final String GP_MULTIPLE_STOCK_ERROR_MESSAGE = "gp.multiple.stock.error.message.cart";
	private static final String FULL = "FULL";
	private static final String CURRENT = "current";

	@Resource(name = "gpCommerceWebServicesCartFacade")
	private CartFacade cartFacade;
	@Resource(name = "commerceStockFacade")
	private CommerceStockFacade commerceStockFacade;
	@Resource(name = "orderEntryReplaceValidator")
	private Validator orderEntryReplaceValidator;
	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "gpCommerceAddToCartMethodHook")
	private GPCommerceAddToCartMethodHook gpCommerceAddToCartMethodHook;
	@Resource(name = "userFacade")
	private UserFacade userFacade;
	@Resource(name = "greaterThanZeroValidator")
	private Validator greaterThanZeroValidator;
	@Resource(name = "pointOfServiceValidator")
	private Validator pointOfServiceValidator;
	@Resource(name = "gpCarFacade")
	private GPCartFacade gpCarFacade;
	@Resource(name = "wishlistFacade")
	private GPWishlistFacade wishlistFacade;
	@Resource(name = "orderEntryCreateValidator")
	private Validator orderEntryCreateValidator;

	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	@Resource(name = "userService")
	private UserService userService;
	
	/**
	 * @param cmd1
	 * @param cmd2
	 * @return CartModificationData
	 */
	public CartModificationData mergeCartModificationData(final CartModificationData cmd1,
			final CartModificationData cmd2)
	{
		if ((cmd1 == null) && (cmd2 == null))
		{
			return new CartModificationData();
		}
		if (cmd1 == null)
		{
			return cmd2;
		}
		if (cmd2 == null)
		{
			return cmd1;
		}
		final CartModificationData cmd = new CartModificationData();
		cmd.setDeliveryModeChanged(Boolean
				.valueOf(Boolean.TRUE.equals(cmd1.getDeliveryModeChanged()) || Boolean.TRUE.equals(cmd2.getDeliveryModeChanged())));
		cmd.setEntry(cmd2.getEntry());
		cmd.setQuantity(cmd2.getQuantity());
		cmd.setQuantityAdded(cmd1.getQuantityAdded() + cmd2.getQuantityAdded());
		cmd.setStatusCode(cmd2.getStatusCode());
		return cmd;
	}

	/**
	 * @param cart
	 * @param number
	 * @return OrderEntryData
	 * @throws CartEntryException
	 */
	public OrderEntryData getCartEntryForNumber(final CartData cart, final long number) throws CartEntryException //NOSONAR
	{
		final List<OrderEntryData> entries = cart.getEntries();
		if (entries != null && !entries.isEmpty())
		{
			final Integer requestedEntryNumber = Integer.valueOf((int) number);
			for (final OrderEntryData entry : entries)
			{
				if (entry != null && requestedEntryNumber.equals(entry.getEntryNumber()))
				{
					return entry;
				}
			}
		}
		throw new CartEntryException("Entry not found", CartEntryException.NOT_FOUND, String.valueOf(number));
	}

	/**
	 * @param cart
	 * @param productCode
	 * @param pickupStore
	 * @return OrderEntryData
	 */
	public OrderEntryData getCartEntry(final CartData cart, final String productCode, final String pickupStore)
	{
		for (final OrderEntryData oed : cart.getEntries())
		{
			if (oed.getProduct().getCode().equals(productCode))
			{
				if (pickupStore == null && oed.getDeliveryPointOfService() == null)
				{
					return oed;
				}
				else if (pickupStore != null && oed.getDeliveryPointOfService() != null
						&& pickupStore.equals(oed.getDeliveryPointOfService().getName()))
				{
					return oed;
				}
			}
		}
		return null;
	}

	/**
	 * @param currentCart
	 * @param currentEntry
	 * @param newPickupStore
	 * @throws CommerceCartModificationException
	 */
	public void validateForAmbiguousPositions(final CartData currentCart, final OrderEntryData currentEntry,
			final String newPickupStore) throws CommerceCartModificationException
	{
		final OrderEntryData entryToBeModified = getCartEntry(currentCart, currentEntry.getProduct().getCode(), newPickupStore);
		if (entryToBeModified != null && !entryToBeModified.equals(currentEntry))
		{
			throw new CartEntryException(
					"Ambiguous cart entries! Entry number " + currentEntry.getEntryNumber()
							+ " after change would be the same as entry " + entryToBeModified.getEntryNumber(),
					CartEntryException.AMBIGIOUS_ENTRY, entryToBeModified.getEntryNumber().toString());
		}
	}

	/**
	 * @param toMergeCartGuid
	 * @return boolean
	 */
	public boolean isUserCart(final String toMergeCartGuid)
	{
		if (cartFacade instanceof CommerceWebServicesCartFacade)
		{
			final CommerceWebServicesCartFacade commerceWebServicesCartFacade = (CommerceWebServicesCartFacade) cartFacade;
			return commerceWebServicesCartFacade.isCurrentUserCart(toMergeCartGuid);
		}
		return true;
	}

	/**
	 * @param cartGuid
	 * @return boolean
	 */
	public boolean isCartAnonymous(final String cartGuid)
	{
		if (cartFacade instanceof CommerceWebServicesCartFacade)
		{
			final CommerceWebServicesCartFacade commerceWebServicesCartFacade = (CommerceWebServicesCartFacade) cartFacade;
			return commerceWebServicesCartFacade.isAnonymousUserCart(cartGuid);
		}
		return true;
	}

	/**
	 * @param baseSiteId
	 * @param code
	 * @param qty
	 * @param pickupStore
	 * @param additionalAttributes
	 * @param fields
	 * @return CartModificationWsDTO
	 * @throws CommerceCartModificationException
	 */
	public CartModificationWsDTO addCartEntryInternal(final String baseSiteId, final String code, final long qty,
			final String pickupStore, final Map<String, String> additionalAttributes, final String fields)
			throws CommerceCartModificationException
	{
		final CartModificationData cartModificationData;
		if (StringUtils.isNotEmpty(pickupStore))
		{
			validateIfProductIsInStockInPOS(baseSiteId, code, pickupStore, null);
			cartModificationData = cartFacade.addToCart(code, qty, pickupStore);
		}
		else
		{
			validateIfProductIsInStockOnline(baseSiteId, code, null);
			cartModificationData = ((CommerceWebServicesCartFacade) cartFacade).addToCart(code, qty, additionalAttributes);
		}
		if (CommerceCartModificationStatus.MAX_ORDER_QUANTITY_EXCEEDED.equals(cartModificationData.getStatusCode()))
		{
			cartModificationData.setStatusCode(GpcommerceCoreConstants.MAX_ORDER_QUANTITY_ERROR_CODE);
		}
		else if (CommerceCartModificationStatus.LOW_STOCK.equals(cartModificationData.getStatusCode()))
		{
			cartModificationData.setStatusCode(GpcommerceCoreConstants.STOCK_LEVEL_ERROR_CODE);
		}

		return getDataMapper().map(cartModificationData, CartModificationWsDTO.class, fields);
	}

	/**
	 * Method handles adding multiple products to the cart
	 *
	 * @param baseSiteId
	 *           - cms site
	 * @param code
	 *           - List of product codes to be added to cart. The format of a serialized query:
	 *           productCode1:productCode2:productCode3:productCode4
	 * @param qty
	 *           - Quantity of product.The format of a serialized query:
	 *           productCode1Qty:productCode2Qty:productCode3Qty:productCode4Qty
	 * @param pickupStore
	 * @param fields
	 * @return list of cart entries
	 * @throws CommerceCartModificationException
	 */
	public List<CartModificationWsDTO> addCartMultipleEntriesInternal(final String baseSiteId, final String code,
			final String qty, final String pickupStore, final String fields) throws CommerceCartModificationException
	{
		final List<String> productCodes = getSplitList(code, COLON);
		final List<String> quantities = getSplitList(qty, COLON);
		final List<CartModificationWsDTO> modifiedList = new ArrayList<>();
		for (int i = 0; i < productCodes.size(); i++)
		{
			CartModificationData cartModificationData;
			try
			{
				if (StringUtils.isNotEmpty(pickupStore))
				{
					validateIfProductIsInStockInPOS(baseSiteId, productCodes.get(i), pickupStore, null);
					cartModificationData = cartFacade.addToCart(productCodes.get(i), Long.parseLong(quantities.get(i)),
							pickupStore);
				}
				else
				{
					validateIfProductIsInStockOnline(baseSiteId, productCodes.get(i), null);
					cartModificationData = cartFacade.addToCart(productCodes.get(i), Long.parseLong(quantities.get(i)));
				}
				if (CommerceCartModificationStatus.MAX_ORDER_QUANTITY_EXCEEDED.equals(cartModificationData.getStatusCode()))
				{
					prepareCartModificationData(cartModificationData, GpcommerceCoreConstants.MAX_ORDER_QUANTITY_ERROR_CODE, null,
							productCodes.get(i), null);

				}
				else if (CommerceCartModificationStatus.LOW_STOCK.equals(cartModificationData.getStatusCode()))
				{
					prepareCartModificationData(cartModificationData, GpcommerceCoreConstants.STOCK_LEVEL_ERROR_CODE, null,
							productCodes.get(i), null);
				}
			}
			catch (final LowStockException e)
			{
				LOG.error(e.getMessage(),e);
				cartModificationData = new CartModificationData();
				prepareCartModificationData(cartModificationData, FAILURE, OUT_OF_STOCK, productCodes.get(i), null);
			}
			catch (final GPLowStockException e)
			{
				LOG.error(e.getMessage(),e);
				cartModificationData = new CartModificationData();
				prepareCartModificationData(cartModificationData, GpcommerceCoreConstants.STOCK_LEVEL_ERROR_CODE, FAILURE,
						productCodes.get(i), null);
			}
			catch (final GPMaximumStockException e)
			{
				LOG.error(e.getMessage(),e);
				cartModificationData = new CartModificationData();
				prepareCartModificationData(cartModificationData, GpcommerceCoreConstants.MAX_ORDER_QUANTITY_ERROR_CODE, FAILURE,
						productCodes.get(i), null);
			}
			modifiedList.add(getDataMapper().map(cartModificationData, CartModificationWsDTO.class, fields));
		}
		return modifiedList;
	}
	
	
	
	/**
	 * Method handles adding multiple products to the cart
	 *
	 * @param baseSiteId
	 *           - cms site
	 * @param code
	 *           - List of product codes to be added to cart. The format of a serialized query:
	 *           productCode1:productCode2:productCode3:productCode4
	 * @param qty
	 *           - Quantity of product.The format of a serialized query:
	 *           productCode1Qty:productCode2Qty:productCode3Qty:productCode4Qty
	 * @param pickupStore
	 * @param fields
	 * @return list of cart entries
	 * @throws CommerceCartModificationException
	 */
	public List<CartModificationWsDTO> addMultipleProductsInternal(final String baseSiteId, final GPMultipleProductWsDTO multiProdForm, final String pickupStore, final String fields) throws CommerceCartModificationException
	{
		final List<CartModificationWsDTO> modifiedList = new ArrayList<>();
		for (ProductWsDTO product : multiProdForm.getProducts()) {
			CartModificationData cartModificationData;
			try {
				if (StringUtils.isNotEmpty(pickupStore)) {
					validateIfProductIsInStockInPOS(baseSiteId, product.getCode(), pickupStore, null);
					cartModificationData = cartFacade.addToCart(product.getCode(), product.getMinOrderQuantity(), pickupStore);
				} else {
					validateIfProductIsInStockOnline(baseSiteId, product.getCode(), null);
					cartModificationData = cartFacade.addToCart(product.getCode(), product.getMinOrderQuantity());
				}
				if (CommerceCartModificationStatus.MAX_ORDER_QUANTITY_EXCEEDED
						.equals(cartModificationData.getStatusCode())) {
					prepareCartModificationData(cartModificationData,
							GpcommerceCoreConstants.MAX_ORDER_QUANTITY_ERROR_CODE, null, product.getCode(), null);

				} else if (CommerceCartModificationStatus.LOW_STOCK.equals(cartModificationData.getStatusCode())) {
					prepareCartModificationData(cartModificationData, GpcommerceCoreConstants.STOCK_LEVEL_ERROR_CODE,
							null, product.getCode(), null);
				}
			} catch (final LowStockException e) {
				LOG.error(e.getMessage(), e);
				cartModificationData = new CartModificationData();
				prepareCartModificationData(cartModificationData, FAILURE, OUT_OF_STOCK, product.getCode(), null);
			} catch (final GPLowStockException e) {
				LOG.error(e.getMessage(), e);
				cartModificationData = new CartModificationData();
				prepareCartModificationData(cartModificationData, GpcommerceCoreConstants.STOCK_LEVEL_ERROR_CODE,
						FAILURE, product.getCode(), null);
			} catch (final GPMaximumStockException e) {
				LOG.error(e.getMessage(), e);
				cartModificationData = new CartModificationData();
				prepareCartModificationData(cartModificationData, GpcommerceCoreConstants.MAX_ORDER_QUANTITY_ERROR_CODE,
						FAILURE, product.getCode(), null);
			}
			modifiedList.add(getDataMapper().map(cartModificationData, CartModificationWsDTO.class, fields));
		}
		return modifiedList;
	}
	
	
	
	

	public CartModificationWsDTO updateCartEntryInternal(final String baseSiteId, final CartData cart,
			final OrderEntryData orderEntry, final Long qty, final String pickupStore,
			final Map<String, String> additionalAttributes, final String fields, final boolean putMode)
			throws CommerceCartModificationException
	{
		final long entryNumber = orderEntry.getEntryNumber().longValue();
		final String productCode = orderEntry.getProduct().getCode();
		final PointOfServiceData currentPointOfService = orderEntry.getDeliveryPointOfService();
		CartModificationData cartModificationData1 = null;
		CartModificationData cartModificationData2 = null;
		if (!StringUtils.isEmpty(pickupStore))
		{
			if (currentPointOfService == null || !currentPointOfService.getName().equals(pickupStore))
			{
				//was 'shipping mode' or store is changed
				validateForAmbiguousPositions(cart, orderEntry, pickupStore);
				validateIfProductIsInStockInPOS(baseSiteId, productCode, pickupStore, Long.valueOf(entryNumber));
				cartModificationData1 = cartFacade.updateCartEntry(entryNumber, pickupStore);
			}
		}
		else if (putMode && currentPointOfService != null)
		{
			//was 'pickup in store', now switch to 'shipping mode'
			validateForAmbiguousPositions(cart, orderEntry, pickupStore);
			validateIfProductIsInStockOnline(baseSiteId, productCode, Long.valueOf(entryNumber));
			cartModificationData1 = cartFacade.updateCartEntry(entryNumber, pickupStore);
		}
		if (qty != null)
		{
			cartModificationData2 = ((CommerceWebServicesCartFacade) cartFacade).updateCartEntry(entryNumber, qty.longValue(),
					additionalAttributes);
		}
		else
		{
			cartModificationData2 = ((CommerceWebServicesCartFacade) cartFacade).updateCartEntry(entryNumber, 0,
					additionalAttributes);
		}
		return getDataMapper().map(mergeCartModificationData(cartModificationData1, cartModificationData2),
				CartModificationWsDTO.class, fields);
	}

	/**
	 * @param baseSiteId
	 * @param productCode
	 * @param entryNumber
	 */
	public void validateIfProductIsInStockOnline(final String baseSiteId, final String productCode, final Long entryNumber)
	{
		if(cmsSiteService.isSampleSite())
		{
			return;
		}
		if (!commerceStockFacade.isStockSystemEnabled(baseSiteId))
		{
			throw new StockSystemException("Stock system is not enabled on this site", StockSystemException.NOT_ENABLED, baseSiteId);
		}
		final StockData stock = commerceStockFacade.getStockDataForProductAndBaseSite(productCode, baseSiteId);
		if (stock != null && stock.getStockLevelStatus().equals(StockLevelStatus.OUTOFSTOCK))
		{
			if (entryNumber != null)
			{
				throw new LowStockException("Product [" + sanitize(productCode) + "] cannot be shipped - out of stock online",
						LowStockException.NO_STOCK, String.valueOf(entryNumber));
			}
			else
			{
				throw new ProductLowStockException("Product [" + sanitize(productCode) + "] cannot be shipped - out of stock online",
						LowStockException.NO_STOCK, productCode);
			}
		}
	}

	/**
	 * @param request
	 * @param delimiter
	 * @return List<String>
	 */
	public List<String> getSplitList(final String request, final String delimiter)
	{
		final String[] splitListArray = request.split(delimiter);
		final List<String> splitList = new ArrayList<String>();
		for (final String token : splitListArray)
		{
			if (!token.trim().isEmpty())
			{
				splitList.add(token);
			}
		}
		return splitList;
	}

	private void validateIfProductIsInStockInPOS(final String baseSiteId, final String productCode, final String storeName,
			final Long entryNumber)
	{
		if(cmsSiteService.isSampleSite())
		{
			return;
		}
		if (!commerceStockFacade.isStockSystemEnabled(baseSiteId) && !cmsSiteService.isSampleSite())
		{
			throw new StockSystemException("Stock system is not enabled on this site", StockSystemException.NOT_ENABLED, baseSiteId);
		}
		final StockData stock = commerceStockFacade.getStockDataForProductAndPointOfService(productCode, storeName);
		if (stock != null && stock.getStockLevelStatus().equals(StockLevelStatus.OUTOFSTOCK))
		{
			if (entryNumber != null)
			{
				throw new LowStockException("Product [" + sanitize(productCode) + "] is currently out of stock", //NOSONAR
						LowStockException.NO_STOCK, String.valueOf(entryNumber));
			}
			else
			{
				throw new ProductLowStockException("Product [" + sanitize(productCode) + "] is currently out of stock",
						LowStockException.NO_STOCK, productCode);
			}
		}
		else if (stock != null && stock.getStockLevelStatus().equals(StockLevelStatus.LOWSTOCK))
		{
			if (entryNumber != null)
			{
				throw new LowStockException("Not enough product in stock", LowStockException.LOW_STOCK, String.valueOf(entryNumber));
			}
			else
			{
				throw new ProductLowStockException("Not enough product in stock", LowStockException.LOW_STOCK, productCode);
			}
		}
	}

	private static String sanitize(final String input)
	{
		return YSanitizer.sanitize(input);
	}

	/**
	 *
	 * @param modificationDataList
	 * @param productData
	 * @param isReducedQtyError
	 * @return String
	 */
	public String addEntryToCart(final List<CartModificationData> modificationDataList, final ProductData productData,
			final boolean isReducedQtyError)
	{
		String errorMsg = StringUtils.EMPTY;
		try
		{
			final long qty = productData.getCount();
			final CartModificationData cartModificationData = cartFacade.addToCart(productData.getCode(), qty);
			if (cartModificationData.getQuantityAdded() == 0L)
			{
				errorMsg = YcommercewebservicesConstants.BASKET_INFORMATION_QUANTITY_NO_ITEMS_ADDED
						+ cartModificationData.getStatusCode();
			}
			else if (cartModificationData.getQuantityAdded() < qty && isReducedQtyError)
			{
				errorMsg = YcommercewebservicesConstants.BASKET_INFORMATION_QUANTITY_REDUCED_ITEMS_ADDED
						+ cartModificationData.getStatusCode();
			}
			if (CommerceCartModificationStatus.MAX_ORDER_QUANTITY_EXCEEDED.equals(cartModificationData.getStatusCode()))
			{
				errorMsg = GpcommerceCoreConstants.MAX_ORDER_QUANTITY_ERROR_CODE;
			}
			else if (CommerceCartModificationStatus.LOW_STOCK.equals(cartModificationData.getStatusCode()))
			{
				errorMsg = GpcommerceCoreConstants.STOCK_LEVEL_ERROR_CODE;
			}
			modificationDataList.add(cartModificationData);
		}
		catch (final GPLowStockException ex)
		{
			LOG.error(ex.getMessage(),ex);
			errorMsg = ERROR_CODE_198;
		}
		catch (final GPMaximumStockException ex)
		{
			LOG.error(ex.getMessage(),ex);
			errorMsg = ERROR_CODE_197;
		}
		catch (final CommerceCartModificationException ex)
		{
			LOG.error(ex.getMessage(),ex);
			errorMsg = YcommercewebservicesConstants.BASKET_ERROR_OCCURRED;
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage(),ex);
			errorMsg = YcommercewebservicesConstants.GENERIC_ERROR_OCCURRED;
		}
		return errorMsg;
	}

	/**
	 * @param oryginalEntry
	 * @param entry
	 */
	public void validateCartEntryForReplace(final OrderEntryData oryginalEntry, final OrderEntryWsDTO entry)
	{
		final String productCode = oryginalEntry.getProduct().getCode();
		final Errors errors = new BeanPropertyBindingResult(entry, ENTRY);
		if (entry.getProduct() != null && entry.getProduct().getCode() != null && !entry.getProduct().getCode().equals(productCode))
		{
			errors.reject("cartEntry.productCodeNotMatch");
			throw new WebserviceValidationException(errors);
		}
		validate(entry, ENTRY, orderEntryReplaceValidator);
	}

	/**
	 * @param cart
	 * @param fields
	 * @return CartWsDTO
	 */
	public CartWsDTO getCart(final CartData cart, final String fields)
	{
		String freeShippingAmount = null;
		final List<PromotionResultData> potentialOrderPromotions = new ArrayList<>();
		for (final PromotionResultData potentialOrderPromo : cart.getPotentialOrderPromotions())
		{
			if (potentialOrderPromo.getPromotionData().getCode()
					.contains(configurationService.getConfiguration().getString(GpcommerceFacadesConstants.FREE_SHIPPING)))
			{
				try
				{
					final String description = potentialOrderPromo.getDescription();
					final String str = description.substring(description.indexOf(GpcommerceFacadesConstants.DOLLAR) + 1);
					final Integer replacedAmount = Integer.parseInt(str.substring(0, str.indexOf(" ")));
					final Double cartTotal = cart.getTotalPrice().getValue().doubleValue();
					if (cartTotal <= replacedAmount)
					{
						final String amount = String.valueOf(replacedAmount - cartTotal);
						freeShippingAmount = amount;
						if (CollectionUtils.isNotEmpty(cart.getEntries()))
						{
							if (LOG.isDebugEnabled())
							{
								LOG.debug("Checking for empty Cart in Carts Controller");
							}
							final CurrencyModel currency = commonI18NService
									.getCurrency(cart.getEntries().get(0).getBasePrice().getCurrencyIso());
							final String formattedPrice = gpCommerceAddToCartMethodHook
									.formatPrice(BigDecimal.valueOf(Double.valueOf(amount)), currency);
							final String newDescription = description.replace(currency.getSymbol() + replacedAmount, formattedPrice);
							potentialOrderPromo.setDescription(newDescription);
						}
					}
				}
				catch (final Exception e)
				{
					LOG.error(e.getMessage(),e);
					LOG.error("Error in promotion");
				}
			}
			potentialOrderPromotions.add(potentialOrderPromo);
		}
		cart.setPotentialOrderPromotions(potentialOrderPromotions);
		final CartWsDTO cartDto = getDataMapper().map(cart, CartWsDTO.class, fields);
		cartDto.setDisableCheckoutButton(false);
		if (userService.getCurrentUser() instanceof B2BCustomerModel) {
			B2BCustomerModel b2bCustomer = (B2BCustomerModel) userService.getCurrentUser();
			if (null != b2bCustomer.getUserApprovalStatus() && b2bCustomer.getUserApprovalStatus().getCode()
					.equalsIgnoreCase((GpcommerceCoreConstants.PENDING))) {
				cartDto.setDisableCheckoutButton(true);
			}
		}
		cartDto.setFreeShippingLimit(freeShippingAmount);
		cartDto.setIsSubscription(cart.isIsSubscription());
		if (CollectionUtils.isNotEmpty(cart.getShippingRestrictions()))
		{
			final List<GPRestrictionProductWsDTO> shippingRestWsDTOList = new ArrayList<>();
			for (final ShippingRestrictionsData shippingRestData : cart.getShippingRestrictions())
			{
				final GPRestrictionProductWsDTO shippingRestWsDTO = getDataMapper().map(shippingRestData,
						GPRestrictionProductWsDTO.class, fields);
				shippingRestWsDTOList.add(shippingRestWsDTO);
			}
			cartDto.setShippingRestrictions(shippingRestWsDTOList);
		}
		if (CollectionUtils.isNotEmpty(cartDto.getAppliedOrderPromotions()))
		{
			final List<String> appliedCodes = new ArrayList<>();
			final List<PromotionResultWsDTO> promotions = new ArrayList<>();
			for (final PromotionResultWsDTO promotion : cartDto.getAppliedOrderPromotions())
			{
				if (CollectionUtils.isNotEmpty(promotion.getAppliedCouponCodes()))
				{
					if (!appliedCodes.containsAll(promotion.getAppliedCouponCodes()))
					{
						promotions.add(promotion);
						appliedCodes.addAll(promotion.getAppliedCouponCodes());
					}
				}
				else
				{
					promotions.add(promotion);
				}
			}
			cartDto.setAppliedOrderPromotions(promotions);
		}
		return cartDto;
	}

	/**
	 * @param cart
	 * @param evaluatedToMergeCartGuid
	 * @param fields
	 * @param oldCartId
	 * @return CartWsDTO
	 */
	public CartWsDTO creatCart(final CartData cart, String evaluatedToMergeCartGuid, final String fields, final String oldCartId)
	{
		if (StringUtils.isEmpty(evaluatedToMergeCartGuid))
		{
			if (!userFacade.isAnonymousUser())
			{
				final List<CartData> carts = cartFacade.getCartsForCurrentUser();
				if (CollectionUtils.isNotEmpty(carts))
				{
					evaluatedToMergeCartGuid = carts.get(0).getGuid();
				}
			}
			else
			{
				evaluatedToMergeCartGuid = cart.getGuid();
			}
		}
		if (StringUtils.isNotEmpty(oldCartId))
		{
			if (userFacade.isAnonymousUser())
			{
				throw new CartException("Anonymous user is not allowed to copy cart!");
			}
			if (!isCartAnonymous(oldCartId))
			{
				throw new CartException("Cart is not anonymous", CartException.CANNOT_RESTORE, oldCartId);
			}
			if (StringUtils.isEmpty(evaluatedToMergeCartGuid))
			{
				evaluatedToMergeCartGuid = cart.getGuid();
			}
			else
			{
				if (!isUserCart(evaluatedToMergeCartGuid))
				{
					throw new CartException("Cart is not current user's cart", CartException.CANNOT_RESTORE, evaluatedToMergeCartGuid);
				}
			}
			try
			{
				cartFacade.restoreAnonymousCartAndMerge(oldCartId, evaluatedToMergeCartGuid);
				return getDataMapper().map(cart, CartWsDTO.class, fields);
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
				if (!isUserCart(evaluatedToMergeCartGuid))
				{
					throw new CartException("Cart is not current user's cart", CartException.CANNOT_RESTORE, evaluatedToMergeCartGuid);
				}
				try
				{
					cartFacade.restoreSavedCart(evaluatedToMergeCartGuid);
					return getDataMapper().map(cart, CartWsDTO.class, fields);
				}
				catch (final CommerceCartRestorationException e)
				{
					throw new CartException("Couldn't restore cart", CartException.CANNOT_RESTORE, oldCartId, e);
				}
			}
			return getDataMapper().map(cart, CartWsDTO.class, fields);
		}
	}

	/**
	 * @param quickOrderForm
	 * @param fields
	 * @return GPCartModificationListWSDTO
	 */
	public GPCartModificationListWSDTO addMultipleEntriesToCart(final GPQuickOrderWSDTO quickOrderForm, final String fields)
	{
		final List<CartModificationData> modificationDataList = new ArrayList<>();
		final List<ProductWrapperData> productWrapperDataList = new ArrayList<>();
		final CartModificationListData gpModificationData = new CartModificationListData();
		final int maxQuickOrderEntries = Config.getInt(YcommercewebservicesConstants.QUICKORDER_PRODUCTS_MAXIMUM, 100);
		final int quickOrderEntryCount = CollectionUtils.size(quickOrderForm.getItems());
		quickOrderForm.getItems().stream().limit(Math.min(quickOrderEntryCount, maxQuickOrderEntries)).forEach(productEntry -> {
			String errorMsg = StringUtils.EMPTY;
			final ProductData productData = getDataMapper().map(productEntry, ProductData.class);
			final String sku = !isValidProductEntry(productData) ? StringUtils.EMPTY : productData.getCode();
			if (StringUtils.isEmpty(sku))
			{
				errorMsg = YcommercewebservicesConstants.QUICKORDER_INVALID_PRODUCT;
			}
			else if (!isValidQuantity(productData))
			{
				errorMsg = YcommercewebservicesConstants.QUICKORDER_INVALID_COUNT;
			}
			else
			{
				errorMsg = addEntryToCart(modificationDataList, productData, false);
			}

			if (StringUtils.isNotEmpty(errorMsg))
			{
				productWrapperDataList.add(createProductWrapperData(sku, errorMsg));
			}
		});
		gpModificationData.setCartModifications(modificationDataList);
		gpModificationData.setQuickOrderError(productWrapperDataList);
		return getDataMapper().map(gpModificationData, GPCartModificationListWSDTO.class, fields);
	}

	private boolean isValidProductEntry(final ProductData productData)
	{
		return productData != null && StringUtils.isNotBlank(productData.getCode());
	}

	protected boolean isValidQuantity(final ProductData productData)
	{
		return productData.getCount() != null && productData.getCount().longValue() >= 1L;
	}

	private ProductWrapperData createProductWrapperData(final String sku, final String errorMsg)
	{
		final ProductWrapperData productWrapperData = new ProductWrapperData();
		final ProductData productData = new ProductData();
		productData.setCode(sku);
		productWrapperData.setProductData(productData);
		productWrapperData.setErrorMsg(errorMsg);
		return productWrapperData;
	}

	public CartModificationWsDTO updateCartEntry(final CartData cart, final OrderEntryWsDTO entry, final String fields,
			final long entryNumber, final String baseSiteId) throws CommerceCartModificationException
	{
		final OrderEntryData orderEntry = getCartEntryForNumber(cart, entryNumber);
		final String productCode = orderEntry.getProduct().getCode();
		final Errors errors = new BeanPropertyBindingResult(entry, ENTRY);
		if (entry.getProduct() != null && entry.getProduct().getCode() != null && !entry.getProduct().getCode().equals(productCode))
		{
			errors.reject("cartEntry.productCodeNotMatch");
			throw new WebserviceValidationException(errors);
		}
		if (entry.getQuantity() == null)
		{
			entry.setQuantity(orderEntry.getQuantity());
		}
		validate(entry, ENTRY, orderEntryCreateValidator);
		final String pickupStore = entry.getDeliveryPointOfService() == null ? null : entry.getDeliveryPointOfService().getName();
		return updateCartEntryInternal(baseSiteId, cart, orderEntry, entry.getQuantity(), pickupStore, null, fields,
				false);
	}

	public CartValidationWsDTO valiateCart()
	{

		final IncompatibleProductsData incompatibleData = gpCarFacade.checkIncompatibleProducts();
		final CartValidationData validationData = new CartValidationData();
		validationData.setIncompatibleData(incompatibleData);
		try
		{
			validationData.setCartModificationDataList(cartFacade.validateCartData());
			if (CollectionUtils.isNotEmpty(validationData.getCartModificationDataList()))
			{
				Set<String> msgSet = new HashSet<>();
				validationData.getCartModificationDataList().stream().forEach(modData -> {
					if(cmsSiteService.isSampleSite())
					{
						return;
					}
					if (CommerceCartModificationStatus.LOW_STOCK.equalsIgnoreCase(modData.getStatusCode()))
					{
						modData.setStatusMessage(configurationService.getConfiguration().getString(GP_LOW_STOCK_MESSAGE));
						msgSet.add(GP_LOW_STOCK_MESSAGE);
					}
					else if (CommerceCartModificationStatus.NO_STOCK.equalsIgnoreCase(modData.getStatusCode()))
					{
						modData.setStatusMessage(configurationService.getConfiguration().getString(GP_NO_STOCK_MESSAGE));
						msgSet.add(GP_NO_STOCK_MESSAGE);
					}
				});
				if(!cartFacade.hasEntries()) {
					validationData.getCartModificationDataList().get(0).setStatusMessage(configurationService.getConfiguration().getString(GP_NO_STOCK_EMPTY_CART_MESSAGE));
				}
				if(msgSet.size()>1) {
					validationData.getCartModificationDataList().get(0).setStatusMessage(configurationService.getConfiguration().getString(GP_MULTIPLE_STOCK_ERROR_MESSAGE));
				}
			}
			
			
		}
		catch (final CommerceCartModificationException e)
		{
			LOG.error("Error in cart validation: ", e);
		}
		return getDataMapper().map(validationData, CartValidationWsDTO.class, FULL);
	}
	
	public CartValidationWsDTO checkIncompatibleProducts()
	{
		final IncompatibleProductsData incompatibleData = gpCarFacade.checkIncompatibleProducts();
		final CartValidationData validationData = new CartValidationData();
		validationData.setIncompatibleData(incompatibleData);
		return getDataMapper().map(validationData, CartValidationWsDTO.class, FULL);
	}

	public PromotionResultListWsDTO getPromotion(final CartData cart, final String fields, final String promotionId)
	{
		final List<PromotionResultData> appliedPromotions = new ArrayList<>();
		final List<PromotionResultData> orderPromotions = cart.getAppliedOrderPromotions();
		final List<PromotionResultData> productPromotions = cart.getAppliedProductPromotions();
		for (final PromotionResultData prd : orderPromotions)
		{
			if (prd.getPromotionData().getCode().equals(promotionId))
			{
				appliedPromotions.add(prd);
			}
		}
		for (final PromotionResultData prd : productPromotions)
		{
			if (prd.getPromotionData().getCode().equals(promotionId))
			{
				appliedPromotions.add(prd);
			}
		}
		final PromotionResultDataList dataList = new PromotionResultDataList();
		dataList.setPromotions(appliedPromotions);
		return getDataMapper().map(dataList, PromotionResultListWsDTO.class, fields);
	}

	public void prepareCartModificationData(final CartModificationData cartModificationData, final String errorCode,
			final String statusMessage, final String productCode, final String productName)
	{
		cartModificationData.setStatusCode(errorCode);
		if (null != statusMessage)
		{
			cartModificationData.setStatusMessage(statusMessage);
		}
		if (null != productCode)
		{
			cartModificationData.setErrorProductCode(productCode);
		}
		if (null != productName)
		{
			cartModificationData.setErrorProductName(productName);
		}
	}

	public List<CartModificationWsDTO> addShoppingList(final String baseSiteId, final String wishlistuuid, final String fields)
			throws CommerceCartModificationException
	{
		final List<CartModificationWsDTO> modifiedList = new ArrayList<>();
		final WishlistData shoppingList = wishlistFacade.getSharedlist(wishlistuuid);
		CartModificationData cartModificationData;
		for (final WishlistEntryData wishlistEntryData : shoppingList.getWishlistEntries())
		{
			try
			{
				validateIfProductIsInStockOnline(baseSiteId, wishlistEntryData.getProduct().getCode(), null);
				cartModificationData = cartFacade.addToCart(wishlistEntryData.getProduct().getCode(),
						wishlistEntryData.getQuantity());
				if (CommerceCartModificationStatus.MAX_ORDER_QUANTITY_EXCEEDED.equals(cartModificationData.getStatusCode()))
				{
					prepareCartModificationData(cartModificationData,
							GpcommerceCoreConstants.MAX_ORDER_QUANTITY_ERROR_CODE, FAILURE, wishlistEntryData.getProduct().getCode(),
							wishlistEntryData.getProduct().getName());
				}
				else if (CommerceCartModificationStatus.LOW_STOCK.equals(cartModificationData.getStatusCode()))
				{
					prepareCartModificationData(cartModificationData, GpcommerceCoreConstants.STOCK_LEVEL_ERROR_CODE,
							FAILURE, wishlistEntryData.getProduct().getCode(), wishlistEntryData.getProduct().getName());
				}
			}
			catch (final LowStockException e)
			{
				LOG.error(e.getMessage(),e);
				cartModificationData = new CartModificationData();
				prepareCartModificationData(cartModificationData, FAILURE, OUT_OF_STOCK,
						wishlistEntryData.getProduct().getCode(), wishlistEntryData.getProduct().getName());
			}
			catch (final GPLowStockException e)
			{
				LOG.error(e.getMessage(),e);
				cartModificationData = new CartModificationData();
				prepareCartModificationData(cartModificationData, e.getCode(), FAILURE,
						wishlistEntryData.getProduct().getCode(), wishlistEntryData.getProduct().getName());
			}
			catch (final GPMaximumStockException e)
			{
				LOG.error(e.getMessage(),e);
				cartModificationData = new CartModificationData();
				prepareCartModificationData(cartModificationData, e.getCode(), FAILURE,
						wishlistEntryData.getProduct().getCode(), wishlistEntryData.getProduct().getName());
			}
			modifiedList.add(getDataMapper().map(cartModificationData, CartModificationWsDTO.class, fields));
		}
		return modifiedList;
	}

	public boolean checkSubscriptionCart(String cartId) {
		final UserModel currentUser = userService.getCurrentUser();
		if(!cartId.equalsIgnoreCase(CURRENT) && !userService.isAnonymousUser(currentUser)) {
			return true ;
		}
		return false;
	}
}
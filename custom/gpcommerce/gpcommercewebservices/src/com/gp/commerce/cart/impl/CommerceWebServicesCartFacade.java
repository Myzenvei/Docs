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
package com.gp.commerce.cart.impl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Resource;
import com.gp.commerce.bundle.data.BundleEntryData;
import com.gp.commerce.bundle.data.BundleProductData;
import com.gp.commerce.core.services.configurablebundle.GPBundleService;
import com.gp.commerce.core.util.GPCommerceCoreUtils;
import com.gp.commerce.facades.bundles.dto.BundleEntryWsDTO;
import com.gp.commerce.facades.bundles.dto.BundleCartUpdateWsDTO;
import de.hybris.platform.acceleratorfacades.product.data.ProductWrapperData;
import de.hybris.platform.commercefacades.order.EntryGroupData;
import de.hybris.platform.commercefacades.order.data.*;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.configurablebundlefacades.order.BundleCartFacade;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import com.gp.commerce.core.calculation.AvalaraResponseDetailDTO;
import com.gp.commerce.core.calculation.AvalaraResponseLineItemDTO;
import com.gp.commerce.core.calculation.AvalaraSplitEntryTax;
import com.gp.commerce.core.calculation.AvalaraTaxResponseDTO;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.exceptions.GPException;
import com.gp.commerce.core.model.GPOrderEntryAttributeModel;
import com.gp.commerce.core.model.LeadTimeModel;
import com.gp.commerce.core.model.ScheduleInstallationModel;
import com.gp.commerce.core.model.ShippingRestrictionModel;
import com.gp.commerce.core.model.SplitEntryModel;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.services.GPCartService;
import com.gp.commerce.dto.GPScheduleInstallationWsDTO;
import com.gp.commerce.dto.cybersource.GPSignatureDTO;
import com.gp.commerce.facade.order.data.ShippingRestrictionsData;
import com.gp.commerce.facade.order.data.SplitEntryData;
import com.gp.commerce.facade.order.data.SplitEntryListWsDTO;
import com.gp.commerce.facade.order.data.SplitEntryWsDTO;
import com.gp.commerce.facades.data.GPScheduleInstallationData;
import com.gp.commerce.facades.subscription.impl.GPSubscriptionFacadeImpl;
import com.gp.commerce.order.entry.data.GPSplitListWsDTO;
import com.gp.commerce.order.entry.data.GPSplitWsDTO;
import com.gp.commerce.order.entry.data.GiftWrapListWsDTO;
import com.gp.commerce.order.entry.data.GiftWrapWsDTO;
import com.gpintegration.service.impl.DefaultGPCyberSourceSignatureService;
import com.gpintegration.utils.GPSignaturePojo;
import com.gpintegration.utils.GPSignatureResponseDTO;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static org.junit.Assert.assertNotNull;
import de.hybris.platform.commercefacades.order.impl.DefaultCartFacade;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.externaltax.ExternalTaxesService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartEntryException;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.TaxValue;

/**
 * Extension of {@link DefaultCartFacade} for commercewebservices.
 */
public class CommerceWebServicesCartFacade extends DefaultCartFacade {
	private static final String INSTALLED_ENTRY = "installedEntry";
	private static final String ZERO = "0";
	private static final String CART = "cart";
	private static final String CURRENT = "current";
	private static final String INSTALLED = "installed";
	private static final String ADD_GIFT_ENTRY = "addGiftEntry";
	private static final Logger LOG = Logger.getLogger(CommerceWebServicesCartFacade.class);
	private static final String MAXORDERQUANTITY = "maxOrderQuantity";
	private Converter<DeliveryModeModel, DeliveryModeData> deliveryModeConverter;
	private Converter<AddressModel, AddressData> addressConverter;
	private SessionService sessionService;
	@Resource
	private ExternalTaxesService externalTaxesService;
	@Resource
	private GPBundleService gpBundleService;
	@Resource
	private Converter<CartEntryModel,BundleEntryData> gpBundleProductDataConverter;
	@Resource
	private DefaultGPCyberSourceSignatureService signatureService;
	private GPCartService gpCartService;
	private Converter<ShippingRestrictionModel, ShippingRestrictionsData> gpShippingRestrictionConverter;
	@Resource(name = "subscriptionCartFacadeImpl")
	private GPSubscriptionFacadeImpl subscriptionCartFacadeImpl ;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "calculationService")
	private CalculationService calculationService;
	@Resource(name = "bundleCartFacade")
	private BundleCartFacade bundleCartFacade;
	@Resource(name = "dataMapper")
	private DataMapper dataMapper;
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "gpCommerceCoreUtils")
	private GPCommerceCoreUtils gpCommerceCoreUtils;

	@Override
	public CartData getSessionCart() {
		final CartData cartData;
		final CartModel cart = getCartService().getSessionCart();
		 List<ShippingRestrictionsData> restrictionList = new ArrayList<>();
		if(null != getSessionService().getAttribute("calculationType") && CART.equalsIgnoreCase(getSessionService().getAttribute("calculationType"))) {
			if(cart.getDeliveryMode()!=null) {
			cart.setDeliveryMode(null);
			cart.setDeliveryCost(0.00d);
			getModelService().save(cart);
			getCommerceCartService().calculateCart(cart);
			getModelService().refresh(cart);
			getCartService().setSessionCart(cart);
			}
		}else {
			// valiating cart for split entries qty and cart entry qty 
			if(null != getSessionService().getAttribute("calculationType")) {
				validateCartForSplitEntry(cart);
			}
			if (null != cart.getDeliveryAddress()) {
				restrictionList=getShippingRestrictionList(cart) ;
			}
		}
		cartData = getCartConverter().convert(cart);
		//populating low stock message
		populateLowStockMessage(cartData);
		if(CollectionUtils.isNotEmpty(restrictionList)) {
			cartData.setShippingRestrictions(restrictionList);
		}
		if(CollectionUtils.isNotEmpty(cartData.getAppliedVouchers())) {
			List<String> uniqueVouchers=getUniqueVouchers( cartData) ;
			cartData.setAppliedVouchers(uniqueVouchers);
		}
		return cartData;
	}

	/**
	 * Returns the subscribtion cart for the provided cardId.
	 * @param cartId the cart id used
	 * @return {@link CartData}
	 */
	public CartData getSubscriptionCart(String cartId) {
		CartData cartData = null;
		List<ShippingRestrictionsData> restrictionList = new ArrayList<>();
		CartModel cartmodel = subscriptionCartFacadeImpl.getSubsCartModel(cartId);
		if (null != getSessionService().getAttribute("calculationType")
				&& CART.equalsIgnoreCase(getSessionService().getAttribute("calculationType"))) {
			try {
				calculationService.calculateTotals(cartmodel, true);
			} catch (CalculationException ex) {
				LOG.error("Error while calculation the totals in GetSubscriptionCart "+ex.getMessage(),ex);
			}
		} else {
			// valiating cart for split entries qty and cart entry qty
			if (null != getSessionService().getAttribute("calculationType")) {
				getCommerceCartService().calculateCart(cartmodel);
				validateCartForSplitEntry(cartmodel);
			}
			if (null != cartmodel.getDeliveryAddress()) {
				restrictionList = getShippingRestrictionList(cartmodel);
			}
		}
		cartData = getCartConverter().convert(cartmodel);
		populateLowStockMessage(cartData);
		if (CollectionUtils.isNotEmpty(restrictionList)) {
			cartData.setShippingRestrictions(restrictionList);
		}
		if (CollectionUtils.isNotEmpty(cartData.getAppliedVouchers())) {
			List<String> uniqueVouchers = getUniqueVouchers(cartData);
			cartData.setAppliedVouchers(uniqueVouchers);
		}
		return cartData;
	}
	
	/**
	 * Returns the Cartdata for the provided delivery mode id and card id.
	 * @param deliveryModeId the deliverymode id used
	 * @param cartId         the card id used
	 * @return {@link CartData}
	 * @throws GPException the GP exception
	 */
	public CartData setSubscriptionCartDeliveryMode(final String deliveryModeId, final String cartId) throws GPException {
		if (subscriptionCartFacadeImpl.setSubscriptionDeliveryMode(deliveryModeId, cartId)) {
			return getSubscriptionCart(cartId);
		}
		throw new GPException(deliveryModeId,cartId);
	}
	
	/**
	 * Returns the list of vouchers for the provided cartData
	 * @param cartData {@link CartData}
	 * @return list of vouchers
	 */
	public  List<String> getUniqueVouchers(CartData cartData) {
		final List<String> uniqueVouchers = new ArrayList<>();
		for(final String voucher : cartData.getAppliedVouchers()) {
			if(!uniqueVouchers.contains(voucher)) {
				uniqueVouchers.add(voucher);
			}
		}
		return uniqueVouchers ;
	}
	
	/**
	 * Returns list of shipping restrictions for the provided cart
	 * @param cart {@link CartModel}
	 * @return list of {@link ShippingRestrictionsData}
	 */
	public List<ShippingRestrictionsData> getShippingRestrictionList(CartModel cart) {
		final List<ShippingRestrictionsData> restrictionList = new ArrayList<>();
		final List<ShippingRestrictionModel> restrictedProductList = new ArrayList<>();
		for (final AbstractOrderEntryModel entry : cart.getEntries()) {
			if (CollectionUtils.isNotEmpty(entry.getSplitEntry())) {
				for (final SplitEntryModel splitEntry : entry.getSplitEntry()) {
					if (null != splitEntry.getDeliveryAddress()) {
						final List<ShippingRestrictionModel> restrictedList = getGpCartService()
								.fetchShippingRestrictions(entry.getProduct().getCode(),
										splitEntry.getDeliveryAddress().getCountry().getIsocode(),
										splitEntry.getDeliveryAddress().getRegion().getIsocodeShort());
						if (CollectionUtils.isNotEmpty(restrictedList)) {
							for (final ShippingRestrictionModel restriction : restrictedList) {
								if (null != restriction && null != restriction.getDeliveryMode() && restriction.getDeliveryMode().equals(ZERO)) {
									restrictedProductList.add(restriction);
								}
							}
						}
					}
				}
			}
		}
		for (final ShippingRestrictionModel shipRestModel : restrictedProductList) {
			final ShippingRestrictionsData shippingRestData = getGpShippingRestrictionConverter().convert(shipRestModel);
			restrictionList.add(shippingRestData);
		}
		return  restrictionList ;
	}

	/**
	 * Validate cart for split entry against entry total qty.
	 * @param cart the cart
	 */
	private void validateCartForSplitEntry(CartModel cart) {
		if (null != cart && CollectionUtils.isNotEmpty(cart.getEntries())) {
			for (AbstractOrderEntryModel entryModel : cart.getEntries()) {
				if(null != entryModel && CollectionUtils.isNotEmpty(entryModel.getSplitEntry())) {
					long entryLevelQty = entryModel.getQuantity();
					long splitEntryTotal = entryModel.getSplitEntry().stream().mapToLong(split -> (null != split.getQty()?Long.valueOf(split.getQty()):0l)).sum();
					if(entryLevelQty > splitEntryTotal){
						long addedQty = entryLevelQty - splitEntryTotal;
						SplitEntryModel splitEntryModel = entryModel.getSplitEntry().get(0);
						long newQtyForSpliEntry = Long.valueOf(splitEntryModel.getQty())+addedQty;
						splitEntryModel.setQty(Long.toString(newQtyForSpliEntry));
						getModelService().save(splitEntryModel);
					}else if(entryLevelQty < splitEntryTotal){
						long removedQty = splitEntryTotal - entryLevelQty;
						int indexSize = entryModel.getSplitEntry().size()-1;
						for(int start = indexSize ; start >= 0 && removedQty!=0 ; start--) {
							SplitEntryModel splitEntryModel = entryModel.getSplitEntry().get(start);
							if(null != splitEntryModel) {
								if(null != splitEntryModel.getQty() && Long.valueOf(splitEntryModel.getQty()) > removedQty) {
									long newQtyForSpliEntry = Long.valueOf(splitEntryModel.getQty())-removedQty;
									splitEntryModel.setQty(Long.toString(newQtyForSpliEntry));
									removedQty =0;
									getModelService().save(splitEntryModel);
								}else {
									if(null != splitEntryModel.getQty()  && Long.valueOf(splitEntryModel.getQty()) <= removedQty) {
										 removedQty = removedQty - Long.valueOf(splitEntryModel.getQty());
										 getModelService().remove(splitEntryModel);
									}
								}
							}
							getModelService().refresh(entryModel);
						}
					}
				}
			}
		}
	}

	private Map<String, AvalaraSplitEntryTax> convertAvalaraResponseToSplitEntries(final CartModel cart, final AvalaraTaxResponseDTO taxResponse){
		final Map<String, AvalaraSplitEntryTax> splitEntriesTaxValue = new HashMap<>();
		final List<AvalaraResponseLineItemDTO> listLineItem = taxResponse.getLines();
		if(null != listLineItem) {
			for(final AvalaraResponseLineItemDTO lineItem: listLineItem) {
				if(!lineItem.getLineNumber().endsWith("SHIP")) {
					final AvalaraSplitEntryTax splitEntryTax = new AvalaraSplitEntryTax();
					final List<TaxValue> splitEntryTaxValueList = new ArrayList<>();
					for (final AvalaraResponseDetailDTO details : lineItem.getDetails()) {
						final TaxValue entryTaxValue = new TaxValue(details.getTaxName(), details.getRate(), true, "USD");
						splitEntryTaxValueList.add(entryTaxValue);
					}
					splitEntryTax.setSplitEntryTaxValue(splitEntryTaxValueList);
					splitEntryTax.setSplitEntryTotalTax(lineItem.getTax());
					splitEntriesTaxValue.put(lineItem.getLineNumber(), splitEntryTax);
					splitEntryTax.setSplitEntryMerchantTax(splitEntryTax.getSplitEntryTotalTax());
				}else{
					final AvalaraSplitEntryTax splitEntryTax =splitEntriesTaxValue.get(StringUtils.removeEnd(lineItem.getLineNumber(),"-SHIP"));
					splitEntryTax.setSplitEntryShipmentTax(lineItem.getTax());
					splitEntryTax.setSplitEntryTotalTax(splitEntryTax.getSplitEntryTotalTax()+lineItem.getTax());
					splitEntriesTaxValue.put(StringUtils.removeEnd(lineItem.getLineNumber(),"-SHIP"),splitEntryTax);
				}
			}
			cart.setTotalTax(Double.parseDouble(taxResponse.getTotalTax()));
		}
		return splitEntriesTaxValue;
	}

	protected void saveOrder(final AbstractOrderModel cartModel) {
		getModelService().save(cartModel);
		getModelService().saveAll(cartModel.getEntries());
		setCalculatedStatus(cartModel);
	}

	protected void setCalculatedStatus(final AbstractOrderModel order) {
		order.setCalculated(Boolean.TRUE);
		final List<AbstractOrderEntryModel> entries = order.getEntries();
		if (entries != null) {
			for (final AbstractOrderEntryModel entry : entries) {
				entry.setCalculated(Boolean.TRUE);
			}
			getModelService().saveAll(entries);
		}
		getModelService().save(order);
	}

	public List<AddressData> getGuestUserAddressses() {
		final CartModel cart = getCartService().getSessionCart();
		if(null != cart.getUser() && CollectionUtils.isNotEmpty(cart.getUser().getAddresses())) {
			return getAddressConverter().convertAll(cart.getUser().getAddresses());
		}
		return Collections.emptyList();
	}
	
	/**
	 * Returns {@link CartModificationData} for the provided quantity and additional
	 * attributes of the product
	 * @param code                 the product code
	 * @param quantity             the product quantity
	 * @param additionalAttributes the additional attributes of the product
	 * @return {@link CartModificationData}
	 * @throws CommerceCartModificationException if invalid product
	 */
	public CartModificationData addToCart(final String code, final long quantity,final Map<String,String> additionalAttributes) throws CommerceCartModificationException {
		final AddToCartParams params = new AddToCartParams();
		params.setProductCode(code);
		params.setQuantity(quantity);
		params.setAdditionalAttributes(additionalAttributes);
		return addToCart(params);
	}

	@Override
	public CartModificationData addToCart(final AddToCartParams addToCartParams) throws CommerceCartModificationException {
		final CommerceCartParameter parameter = getCommerceCartParameterConverter().convert(addToCartParams);
		parameter.setAdditionalAttributes(addToCartParams.getAdditionalAttributes());
		final CommerceCartModification modification = getCommerceCartService().addToCart(parameter);
		return getCartModificationConverter().convert(modification);
	}

	/**
	 * Updates cart by processing product cart entryNumber, product quantity and
	 * additional Attributes
	 * @param entryNumber          cart entry number
	 * @param quantity             the product quantity
	 * @param additionalAttributes the additional attributes of the product
	 * @return {@link CartModificationData}
	 * @throws CommerceCartModificationException on error
	 */
	public CartModificationData updateCartEntry(final long entryNumber, final long quantity,
			final Map<String, String> additionalAttributes) throws CommerceCartModificationException {
		final AddToCartParams dto = new AddToCartParams();
		dto.setQuantity(quantity);
		final CommerceCartParameter parameter = getCommerceCartParameterConverter().convert(dto);
		parameter.setEnableHooks(true);
		parameter.setEntryNumber(entryNumber);
		parameter.setAdditionalAttributes(additionalAttributes);
		ProductModel product = getProductToUpdate(parameter);
		String gpxpressSiteId = configurationService.getConfiguration().getString(GpcommerceCoreConstants.GPXPRESS_SITE_ID);
		if (null != product) {
		if(cmsSiteService.getCurrentSite().getUid().equalsIgnoreCase(gpxpressSiteId) && product.getSample()) {
			updateSampleOrderLimitForSampleProduct(product);
			} else {
			Integer maxOrderQuantity = (Integer)gpCommerceCoreUtils.getProductAttribute(product, MAXORDERQUANTITY);
			product.setMaxOrderQuantity(maxOrderQuantity);
			}
		}
		if (MapUtils.isNotEmpty(additionalAttributes)) {
			if (null != additionalAttributes.get(INSTALLED)) {
				if ("false".equalsIgnoreCase(additionalAttributes.get(INSTALLED))) {
					final CartModel cartModel = getCartService().getSessionCart();
					final List<AbstractOrderEntryModel> entries = cartModel.getEntries();
					if (CollectionUtils.isNotEmpty(entries)) {
						for (final AbstractOrderEntryModel entry : entries) {
							updateEntrywithQty(entryNumber, cartModel, entry);
						}
					}
				}
			}
			final CommerceCartModification modification = getCommerceCartService().updateQuantityForCartEntry(parameter);
		}
		final CommerceCartModification modification = getCommerceCartService().updateQuantityForCartEntry(parameter);
		if(null != modification.getEntry()) {
			final AbstractOrderEntryModel entry =modification.getEntry();
			if(CollectionUtils.isNotEmpty(entry.getSplitEntry()) && entry.getSplitEntry().size() == 1) {
				final SplitEntryModel splitEntry = entry.getSplitEntry().get(0);
				splitEntry.setQty(entry.getQuantity().toString());
				getModelService().save(splitEntry);
			}
		}
		return getCartModificationConverter().convert(modification);
	}

	/**
	 * Returns the Signature response for the provided sinature and baseSiteId
	 * @param signature  {@link GPSignatureDTO}
	 * @param baseSiteId the site id used
	 * @return {@link GPSignatureResponseDTO}
	 */
	public GPSignatureResponseDTO getSignature(final GPSignatureDTO signature, final String baseSiteId) {
		return signatureService.getTheSignature(createRequest(signature),baseSiteId);
	}

	private GPSignaturePojo createRequest(final GPSignatureDTO signatureDTO) {
		final GPSignaturePojo pojo =new GPSignaturePojo();
		pojo.setAmount("0.0");
		pojo.setBillToAddressCity(signatureDTO.getBill_to_address_city());
		pojo.setBillToAddressState(signatureDTO.getBill_to_address_state());
		pojo.setBillToAddressCountry(signatureDTO.getBill_to_address_country());
		pojo.setBillToAddressLine1(signatureDTO.getBill_to_address_line1());
		pojo.setBillToAddressPostalCode(signatureDTO.getBill_to_address_postal_code());
		pojo.setBillToEmail(signatureDTO.getBill_to_email());
		pojo.setBillToForename(signatureDTO.getBill_to_forename());
		pojo.setBillToPhone(signatureDTO.getBill_to_phone());
		pojo.setBillToSurname(signatureDTO.getBill_to_surname());
		pojo.setPaymentMethod(signatureDTO.getPayment_method());
		pojo.setReferenceNumber(signatureDTO.getReference_number());
		pojo.setTransactionType(signatureDTO.getTransaction_type());
		pojo.setSignedFieldNames(signatureDTO.getSigned_field_names());
		pojo.setUnsignedFieldNames(signatureDTO.getUnsigned_field_names());
		pojo.setSignedDateTime(signatureDTO.getSigned_date_time());
		return pojo;
	}

	private void updateEntrywithQty(final long entryNumber, final CartModel cartModel, final AbstractOrderEntryModel entry) throws CommerceCartModificationException {
		final Collection<GPOrderEntryAttributeModel> attributes = entry.getAdditionalAttributes();
		if (CollectionUtils.isNotEmpty(attributes)) {
			final GPOrderEntryAttributeModel attribute = attributes.stream()
					.filter(entryData -> INSTALLED_ENTRY.equalsIgnoreCase(entryData.getName())).findFirst().orElse(null);
			if (null != attribute && (Long.toString(entryNumber).equalsIgnoreCase(attribute.getValue()))) {
				updateCartEntryForCart(cartModel, entry);
			}
		}
	}

	private void updateCartEntryForCart(final CartModel cartModel, final AbstractOrderEntryModel entry) throws CommerceCartModificationException {
		final CommerceCartParameter updateparameter = new CommerceCartParameter();
		updateparameter.setEnableHooks(true);
		updateparameter.setEntryNumber(entry.getEntryNumber());
		updateparameter.setQuantity(0);
		updateparameter.setCart(cartModel);
		getCommerceCartService().updateQuantityForCartEntry(updateparameter);
	}

	/**
	 * Update each entry with qty anmd address
	 * @param splitEntries {@link GPSplitListWsDTO}
	 * @param cartId       the cart id used
	 * @throws CommerceCartModificationException on cart modification error
	 */
	public void updateSpliEntries(final GPSplitListWsDTO splitEntries,final String  cartId) throws CommerceCartModificationException {
		final UserModel currentUser = userService.getCurrentUser();
		assertNotNull(splitEntries);
		 CartModel cart =null ;
		if (CollectionUtils.isNotEmpty(splitEntries.getSplitEntry())) {
			if(!cartId.equalsIgnoreCase(CURRENT) && !userService.isAnonymousUser(currentUser)) {
				cart=subscriptionCartFacadeImpl.getSubsCartModel(cartId) ;
			}else {
			  cart = getCartService().getSessionCart();
			}
			if (null != cart) {
				cart.setCalculated(false);
				for (final GPSplitWsDTO splitEntry : splitEntries.getSplitEntry()) {
					final int entryNumber = splitEntry.getEntryNumber();
					final AbstractOrderEntryModel entryModel = getCartEntryForNumber(cart, entryNumber);
					if(CollectionUtils.isNotEmpty(splitEntry.getSplitEntry())) {
						final List<SplitEntryModel> spList= new ArrayList<>();
						for(final SplitEntryWsDTO spEntry: splitEntry.getSplitEntry()) {
							if(!"0".equals(spEntry.getQty())) {
							final SplitEntryModel splitEntryModel = getModelService().create(SplitEntryModel.class);
							splitEntryModel.setCode(cart.getCode()+"-"+entryNumber+"-"+System.currentTimeMillis());
							splitEntryModel.setQty(spEntry.getQty());
							if(null != spEntry.getDeliveryAddress() && null != spEntry.getDeliveryAddress().getId()) {
								AddressModel deliveryAddressModel = getDeliveryAddressModelForCode(cart, spEntry.getDeliveryAddress().getId());
								splitEntryModel.setDeliveryAddress(deliveryAddressModel);
								cart.setDeliveryAddress(deliveryAddressModel);
								getModelService().save(cart);
							}
							splitEntryModel.setProductCode(spEntry.getProductCode());
							final AbstractOrderEntryModel carEntryModel = getCartEntryForNumber(cart, entryNumber);
							splitEntryModel.setPrice(BigDecimal.valueOf((carEntryModel.getTotalPrice()/carEntryModel.getQuantity())*Long.parseLong(spEntry.getQty())).doubleValue());
							getModelService().save(splitEntryModel);
							getModelService().refresh(splitEntryModel);
							spList.add(splitEntryModel);
							}
						}
						entryModel.setSplitEntry(spList);
						getModelService().save(entryModel);
					}
				}
				for(final AbstractOrderEntryModel entry :cart.getEntries()) {
					if(CollectionUtils.isEmpty(entry.getSplitEntry())){
						if(CollectionUtils.isNotEmpty(entry.getAdditionalAttributes())) {
							final GPOrderEntryAttributeModel attribute =entry.getAdditionalAttributes().stream().filter(additionalAttribute -> additionalAttribute.getName().equalsIgnoreCase(INSTALLED_ENTRY)).findFirst().orElse(null);
							if(null !=  attribute) {
								final int entryNo = Integer.parseInt(attribute.getValue());
								final AbstractOrderEntryModel installedEntry = getCartEntryForNumber(cart, entryNo);
								if(null != installedEntry ) {
									if(CollectionUtils.isNotEmpty(installedEntry.getSplitEntry())) {
										final List<SplitEntryModel> spList= new ArrayList<>();
										for(final SplitEntryModel splitEntryModel : installedEntry.getSplitEntry()) {
												final SplitEntryModel splitEntry = getModelService().create(SplitEntryModel.class);
												splitEntry.setCode(cart.getCode()+"-"+entry.getEntryNumber()+"-"+System.currentTimeMillis());
												splitEntry.setQty(splitEntryModel.getQty());
												if(null != splitEntryModel.getDeliveryAddress() && null != splitEntryModel.getDeliveryAddress().getPk()) {
													AddressModel shippingAddress = getDeliveryAddressModelForCode(cart, splitEntryModel.getDeliveryAddress().getPk().toString());
													splitEntry.setDeliveryAddress(shippingAddress);
													cart.setDeliveryAddress(shippingAddress);
													getModelService().save(cart);
												}
												splitEntry.setProductCode(entry.getProduct().getCode());
												splitEntry.setPrice(entry.getBasePrice());
												getModelService().save(splitEntry);

												spList.add(splitEntry);
											}
											entry.setSplitEntry(spList);
											getModelService().save(entry);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
	/**
	 * Updates split entries with delivery mode for the provided list of
	 * splitEntries
	 * @param splitEntries the list of split entries provided
	 * @throws CommerceCartModificationException on cart error
	 */
	public void updateSpliEntriesWithDeliveryMode(final List<SplitEntryListWsDTO> splitEntries) throws CommerceCartModificationException {
		Double totalDeliveryCost=0.0 ;
		Double totaldeliveryPrice=0.0 ;
		assertNotNull("Split Entry update Address can not be null",splitEntries);
		final CartModel cart = getCartService().getSessionCart();
		 final DecimalFormat decimalFormat = new DecimalFormat(".##");
		for (final SplitEntryListWsDTO splitEntryListWsDTO : splitEntries) {
			for (final SplitEntryWsDTO splitEntry : splitEntryListWsDTO.getSplitEntries()) {
			 DeliveryModeModel deliveryModeModel = getDeliveryService()
						.getDeliveryModeForCode(splitEntryListWsDTO.getDeliveryMode().getCode());  
				deliveryModeModel.setName(splitEntryListWsDTO.getDeliveryMode().getName());
				if(null == cart.getDeliveryMode() && null != deliveryModeModel) {
					cart.setDeliveryMode(deliveryModeModel);
				}
				final SplitEntryModel splitEntryModel = getSplitEntry(cart, splitEntry.getCode());
				if (null != splitEntryModel) {
					if (null != splitEntryListWsDTO.getDeliveryInstruction()) {
						splitEntryModel.setDeliveryInstruction(splitEntryListWsDTO.getDeliveryInstruction());
					}
					final DeliveryModeModel splitEntryDeliveryModeModel=getDeliveryPrice( deliveryModeModel, splitEntryModel) ; 
					splitEntryDeliveryModeModel.setName(splitEntryListWsDTO.getDeliveryMode().getName());
					getModelService().save(splitEntryDeliveryModeModel);
					getDeliveryModeConverter().convert(splitEntryDeliveryModeModel);
					splitEntryModel.setDeliveryMode(splitEntryDeliveryModeModel);
					getModelService().save(splitEntryModel);
					 totaldeliveryPrice =splitEntryModel.getDeliveryMode().getDeliveryFormattedPrice();
				}
			}
			totalDeliveryCost +=totaldeliveryPrice ;
			cart.setTotalDeliveryCost(Double.valueOf(decimalFormat.format(totalDeliveryCost)));
			getModelService().save(cart);
		}
	}

	/**
	 * Returns Delivery mode for the provided Deliverymode and split entry
	 * @param deliveryModeModel the delivery mode used
	 * @param splitEntryModel the split entry used
	 * @return {@link DeliveryModeModel}
	 */
	public DeliveryModeModel getDeliveryPrice(final DeliveryModeModel deliveryModeModel,final SplitEntryModel splitEntryModel){
		if(CollectionUtils.isNotEmpty( splitEntryModel.getDeliveryModes())){
			for( final DeliveryModeModel SplitEntrydeliveryModeModel   : splitEntryModel.getDeliveryModes()){
				final String deliverycode= SplitEntrydeliveryModeModel.getCode();
				if(deliverycode.substring(0, deliverycode.lastIndexOf("_")).equals(deliveryModeModel.getCode())){
						return  SplitEntrydeliveryModeModel;
				}
			}
		} 
		return null ;
	}

	/**
	 * Returns SPlit entry for the provided cartmodel and code
	 * @param cart the cartmodel
	 * @param code the code used
	 * @return {@link SplitEntryModel}
	 */
	private SplitEntryModel getSplitEntry(final CartModel cart,final String code) {
		if(CollectionUtils.isNotEmpty(cart.getEntries())) {
			for(final AbstractOrderEntryModel entry :cart.getEntries()) {
				final SplitEntryModel splitEntryModel = entry.getSplitEntry().stream().filter(split -> code.equalsIgnoreCase(split.getCode())).findFirst().orElse(null);
				if(null != splitEntryModel) {
					return splitEntryModel;
				}
			}
		}
		return null;
	}

	public GPScheduleInstallationData getScheduleInstallation() {
		final CartData cart = getSessionCart();
		GPScheduleInstallationData installationData = null;
		if(null != cart.getScheduleInstallation()) {
			installationData = cart.getScheduleInstallation();
		}else {
			installationData = new GPScheduleInstallationData();
		}
		final long installedProductCount = countInstalledProduct(cart);
		installationData.setItem(installedProductCount+"");
		installationData.setAddress(getAddress(cart));
		installationData.setLeadWeek(getLeadWeek());
		return installationData;
	}

	private String getLeadWeek() {
		int max =0;
		final String baseStoreId = getBaseSiteService().getCurrentBaseSite().getUid();
		final CartModel cart = getCartService().getSessionCart();
		final List<String> leadTime=new ArrayList<>();
		for (final AbstractOrderEntryModel entry : cart.getEntries()) {
			if (CollectionUtils.isNotEmpty(entry.getAdditionalAttributes())) {
				for(final GPOrderEntryAttributeModel attribute:entry.getAdditionalAttributes()) {
					if(INSTALLED.equalsIgnoreCase(attribute.getName())) {
						if(CollectionUtils.isNotEmpty(entry.getSplitEntry())){
							for(final SplitEntryModel splitEntry:entry.getSplitEntry()) {
								if(null != splitEntry.getDeliveryAddress()) {
									final LeadTimeModel leadsTime = splitEntry.getDeliveryAddress().getRegion().getLeadTimes().stream().filter(id -> baseStoreId.equalsIgnoreCase(id.getSiteId())).findFirst().orElse(null);
									if(null != leadsTime) {
										leadTime.add(leadsTime.getLeadTime());
									}
								}
							}
						}
					}
				}
			}
		}
		if(CollectionUtils.isNotEmpty(leadTime)) {
			for(final String time : leadTime) {
				final int currentValue = Integer.parseInt(time);
				if(max < currentValue) {
					max = currentValue;
				}
			}
		}
		return max+"";
	}

	private  Map<String, AddressData> getAddress(final CartData cart) {
		final Map<String, AddressData> addressGroupMap = new HashMap<>();
		for (final OrderEntryData entry : cart.getEntries()) {
			if (MapUtils.isNotEmpty(entry.getAdditionalAttributes())) {
				for(final Map.Entry<String, String> mapEntry:entry.getAdditionalAttributes().entrySet()) {
					if(INSTALLED.equalsIgnoreCase(mapEntry.getKey())) {
						if(CollectionUtils.isNotEmpty(entry.getSplitEntries())) {
							for(final SplitEntryData splitEntry:entry.getSplitEntries()) {
								if(null != splitEntry.getDeliveryAddress()) {
									addressGroupMap.put(splitEntry.getDeliveryAddress().getId(), splitEntry.getDeliveryAddress());
								}
							}
						}
					}
				}
			}
		}
		if(addressGroupMap.size() == 0 && null != cart.getDeliveryAddress()) {
			addressGroupMap.put(cart.getDeliveryAddress().getId(), cart.getDeliveryAddress());
		}
		return addressGroupMap;
	}

	private long countInstalledProduct(final CartData cart) {
		long quantity = 0;
		for (final OrderEntryData entry : cart.getEntries()) {
			if (MapUtils.isNotEmpty(entry.getAdditionalAttributes())) {
				for(final Map.Entry<String, String> mapEntry:entry.getAdditionalAttributes().entrySet()) {
					if(INSTALLED_ENTRY.equalsIgnoreCase(mapEntry.getKey())) {
						quantity += entry.getQuantity();
					}
				}
			}
		}
		return quantity;
	}

	/**
	 * updates cart with schedule installation model for the provided schedules
	 * @param scheduleWsDTO {@link GPScheduleInstallationWsDTO}
	 */
	public void scheduleInstallation(final GPScheduleInstallationWsDTO scheduleWsDTO) {
		final CartModel cart = getCartService().getSessionCart();
		ScheduleInstallationModel insatallationModel = null;
		if (null != cart.getScheduleInstallation()) {
			insatallationModel = cart.getScheduleInstallation();
		} else {
			insatallationModel = getModelService().create(ScheduleInstallationModel.class);
			insatallationModel.setCode(UUID.randomUUID().toString());
		}
		insatallationModel.setName(scheduleWsDTO.getName());
		insatallationModel.setPhoneNo(scheduleWsDTO.getPhoneNo());
		insatallationModel.setExtraInfo(scheduleWsDTO.getExtraInfo());
		insatallationModel.setPreferedTime(scheduleWsDTO.getPreferredTime());
		insatallationModel.setDate(scheduleWsDTO.getPreferredDate());
		cart.setScheduleInstallation(insatallationModel);
		getModelService().saveAll(insatallationModel, cart);
	}

	/**
	 * Get delivery address
	 * @param cartModel the cart used
	 * @param code      the code used
	 * @return {@link AddressModel}
	 */
	protected AddressModel getDeliveryAddressModelForCode(final CartModel cartModel, final String code) {
		Assert.notNull(code, "Parameter code cannot be null.");
		if (cartModel != null) {
			for (final AddressModel address : getDeliveryService().getSupportedDeliveryAddressesForOrder(cartModel,
					false)) {
				if (code.equals(address.getPk().toString())) {
					return address;
				}
			}
		}
		return null;
	}
	/**
	 * Get Cart Entry
	 * @param cartModel
	 * @param number
	 * @return {@link AbstractOrderEntryModel}
	 * @throws CartEntryException
	 */
	protected AbstractOrderEntryModel getCartEntryForNumber(final CartModel cartModel, final long number) throws CartEntryException { // NOSONAR
		final List<AbstractOrderEntryModel> entries = cartModel.getEntries();
		if (entries != null && !entries.isEmpty()) {
			final Integer requestedEntryNumber = Integer.valueOf((int) number);
			for (final AbstractOrderEntryModel entry : entries) {
				if (entry != null && requestedEntryNumber.equals(entry.getEntryNumber())) {
					return entry;
				}
			}
		}
		throw new CartEntryException("Entry not found", CartEntryException.NOT_FOUND, String.valueOf(number));
	}

	/**
	 * Checks if given card belongs to anonymous user.
	 * @param cartGuid GUID of the cart.
	 * @return <tt>true</tt> if the cart belongs to anonymous user.
	 */
	public boolean isAnonymousUserCart(final String cartGuid) {
		final CartModel cart = getCommerceCartService().getCartForGuidAndSiteAndUser(cartGuid,
				getBaseSiteService().getCurrentBaseSite(), getUserService().getAnonymousUser());
		return cart != null;
	}

	/**
	 * Checks if given card belongs to current user.
	 * @param cartGuid GUID of the cart.
	 * @return <tt>true</tt> if the cart belongs to current user.
	 */
	public boolean isCurrentUserCart(final String cartGuid) {
		final CartModel cart = getCommerceCartService().getCartForGuidAndSiteAndUser(cartGuid,
				getBaseSiteService().getCurrentBaseSite(), getUserService().getCurrentUser());
		return cart != null;
	}

	/**
	 * Add or Remove gift wraps to the product.
	 * @param giftWrap the gift wrap
	 * @throws CommerceCartModificationException the commerce cart modification exception
	 */
	public void giftWrap(final GiftWrapListWsDTO giftWrap) throws CommerceCartModificationException {
		final CartModel cart = getCartService().getSessionCart();
		for(final GiftWrapWsDTO gift: giftWrap.getGiftWrap()) {
			final AbstractOrderEntryModel entryModel = getCartEntryForNumber(cart, gift.getEntryNumber());
			if(!entryModel.isGiftWrapped() && gift.isGiftWrapped()) {
				final Map<String, String> addAttr = new HashMap<String, String>();
				entryModel.setGiftWrapped(gift.isGiftWrapped());
				entryModel.setGiftMessage(gift.getGiftMessage());
				addAttr.put(ADD_GIFT_ENTRY, entryModel.getEntryNumber().toString());
				for(final GPOrderEntryAttributeModel attribute: entryModel.getAdditionalAttributes()) {
					if("giftableProduct".equalsIgnoreCase(attribute.getName())) {
						addToCart(attribute.getValue(), entryModel.getQuantity().longValue(),addAttr);
					}
				}
				getModelService().save(entryModel);
			}
			else if(entryModel.isGiftWrapped() && !gift.isGiftWrapped()) {
				entryModel.setGiftWrapped(gift.isGiftWrapped());
				entryModel.setGiftMessage("");
				final List<AbstractOrderEntryModel> entries = cart.getEntries();
				if (CollectionUtils.isNotEmpty(entries)) {
					final AbstractOrderEntryModel model = getGiftEntry(cart,gift.getEntryNumber().toString());
					if (null != model) {
						updateCartEntryForCart(cart, model);
					}
					getModelService().remove(model);
				}	
			}
		}
	}

	private AbstractOrderEntryModel getGiftEntry(final CartModel cart, final String entryNumber) throws CommerceCartModificationException {
		for(final AbstractOrderEntryModel entry: cart.getEntries()) {
			if(CollectionUtils.isNotEmpty(entry.getAdditionalAttributes())){
				final GPOrderEntryAttributeModel entryData = entry.getAdditionalAttributes().stream().filter(d ->d.getValue().equalsIgnoreCase(entryNumber)).findFirst().orElse(null);
				if(null != entryData) {
					return entry;
				}
			}
		}
		return null;
	}

	/**
	 * Delete installable product.
	 * @param entryNumber the entry number
	 * @throws CommerceCartModificationException the commerce cart modification exception
	 */
	public void deleteInstallableProduct(final long entryNumber) throws CommerceCartModificationException {
		final CartModel cart = getCartService().getSessionCart();
		for (final AbstractOrderEntryModel entry : cart.getEntries()) {
			final Collection<GPOrderEntryAttributeModel> attributes = entry.getAdditionalAttributes();
			if (CollectionUtils.isNotEmpty(attributes)) {
				final GPOrderEntryAttributeModel attribute = attributes.stream()
						.filter(entryData -> INSTALLED_ENTRY.equalsIgnoreCase(entryData.getName())).findFirst().orElse(null);
				if (null != attribute && (Long.toString(entryNumber).equalsIgnoreCase(attribute.getValue()))) {
					updateCartEntryForCart(cart, entry);
				}
			}
		}
	}

	private void populateLowStockMessage(final CartData cartData) {
		final List<OrderEntryData> entries = cartData.getEntries();
		entries.stream().forEach(entry -> populateMessage(entry));
	}

	private void populateMessage(final OrderEntryData entry) {
		final StockData stockData = entry.getProduct().getStock();
		if (null != stockData && StringUtils.isNotBlank(stockData.getLowStockMessage())) {
			stockData.setLowStockMessage(Config.getString(stockData.getLowStockMessage(), "There is not enough inventory available for your order. Please try to reduce quantity or remove item from your cart."));
		}
	}
	
	/**
	 * Updates cart installation quantity for the provided entry number and quantity
	 * @param entryNumber the cart entry number
	 * @param qty         the quantity provided
	 * @throws CommerceCartModificationException on cart error
	 */
	public void updateInstalledQuantity(final long entryNumber, final Long qty) throws CommerceCartModificationException {
		final CartModel cart = getCartService().getSessionCart();
		for (final AbstractOrderEntryModel entry : cart.getEntries()) {
			final Collection<GPOrderEntryAttributeModel> attributes = entry.getAdditionalAttributes();
			if (CollectionUtils.isNotEmpty(attributes)) {
				final GPOrderEntryAttributeModel attribute = attributes.stream()
						.filter(entryData -> INSTALLED_ENTRY.equalsIgnoreCase(entryData.getName())).findFirst().orElse(null);
				if (null != attribute && (Long.toString(entryNumber).equalsIgnoreCase(attribute.getValue()))) {
					updateInstallableCartEntry(cart, entry.getEntryNumber(), qty);
				}
			}
		}
	}

	private void updateInstallableCartEntry(final CartModel cart, final Integer entryNumber, final Long qty) throws CommerceCartModificationException {
		final CommerceCartParameter updateparameter = new CommerceCartParameter();
		updateparameter.setEnableHooks(true);
		updateparameter.setEntryNumber(entryNumber);
		updateparameter.setQuantity(qty);
		updateparameter.setCart(cart);
		getCommerceCartService().updateQuantityForCartEntry(updateparameter);
	}

	public Converter<DeliveryModeModel, DeliveryModeData> getDeliveryModeConverter() {
		return deliveryModeConverter;
	}
	public void setDeliveryModeConverter(final Converter<DeliveryModeModel, DeliveryModeData> deliveryModeConverter) {
		this.deliveryModeConverter = deliveryModeConverter;
	}
	public ExternalTaxesService getExternalTaxesService() {
		return externalTaxesService;
	}
	public void setExternalTaxesService(final ExternalTaxesService externalTaxesService) {
		this.externalTaxesService = externalTaxesService;
	}
	public Converter<AddressModel, AddressData> getAddressConverter() {
		return addressConverter;
	}
	public void setAddressConverter(final Converter<AddressModel, AddressData> addressConverter) {
		this.addressConverter = addressConverter;
	}
	public SessionService getSessionService() {
		return sessionService;
	}
	public void setSessionService(final SessionService sessionService) {
		this.sessionService = sessionService;
	}
	public GPCartService getGpCartService() {
		return gpCartService;
	}
	public void setGpCartService(final GPCartService gpCartService) {
		this.gpCartService = gpCartService;
	}
	public Converter<ShippingRestrictionModel, ShippingRestrictionsData> getGpShippingRestrictionConverter() {
		return gpShippingRestrictionConverter;
	}
	public void setGpShippingRestrictionConverter(final Converter<ShippingRestrictionModel, ShippingRestrictionsData> gpShippingRestrictionConverter) {
		this.gpShippingRestrictionConverter = gpShippingRestrictionConverter;
	}

	/**
	 * Iterate and add products to bundle
	 * @param bundleProductWsDTO the bundle products
	 * @param entryGroupData     the entry group data
	 * @return list of {@link ProductWrapperData}
	 */
	public List<ProductWrapperData> addBundleProductsToCart(BundleCartUpdateWsDTO bundleProductWsDTO, EntryGroupData entryGroupData) {
		final List<ProductWrapperData> productWrapperDataList = new ArrayList<>();
		Map<String,Integer> bundleMap=new HashMap<>();
		List<EntryGroupData> entryGroupDataList=new ArrayList<>();
		entryGroupDataList.add(entryGroupData);
		mapBundleTemplatesWithGroup(entryGroupDataList,bundleMap);
		bundleProductWsDTO.getBundleEntries().forEach(bundleEntryWSDTO -> {
			try {
				bundleCartFacade.addToCart(bundleEntryWSDTO.getProductCode(),1,bundleMap.get(bundleEntryWSDTO.getBundleTemplateId()));
				productWrapperDataList.add(createProductWrapperData(bundleEntryWSDTO.getProductCode(), null));
			} catch (Exception e) {
				LOG.error("Exception adding product "+ bundleEntryWSDTO.getProductCode() +" to bundle with exception "+e.getMessage(),e);
				productWrapperDataList.add(createProductWrapperData(bundleEntryWSDTO.getProductCode(), "Failed to add product to bundle "));
			}
		});
		return productWrapperDataList;
	}

	private ProductWrapperData createProductWrapperData(final String sku, final String errorMsg) {
		final ProductWrapperData productWrapperData = new ProductWrapperData();
		final ProductData productData = new ProductData();
		productData.setCode(sku);
		productWrapperData.setProductData(productData);
		productWrapperData.setErrorMsg(errorMsg);
		return productWrapperData;
	}

	/**
	 * Iterate and updates products to bundle
	 * @param bundleProductWsDTO {@link BundleCartUpdateWsDTO}
	 * @return list of {@link ProductWrapperData}
	 */
	public List<ProductWrapperData> updateBundleProductsInCart(BundleCartUpdateWsDTO bundleProductWsDTO) {
		final List<ProductWrapperData> productWrapperDataList = new ArrayList<>();
		final List<BundleEntryWsDTO> addList = new ArrayList<>();
		bundleProductWsDTO.getBundleEntries().forEach(bundleEntryWSDTO -> {
			if(bundleEntryWSDTO.isDelete()) {
				try {
					updateCartEntry(bundleEntryWSDTO.getEntryNumber(), 0);
					productWrapperDataList.add(createProductWrapperData(bundleEntryWSDTO.getProductCode(), null));
				} catch (CommerceCartModificationException e) {
					LOG.error("Exception deleting product "+ bundleEntryWSDTO.getProductCode() +" from bundle with exception "+e.getMessage(),e);
					productWrapperDataList.add(createProductWrapperData(bundleEntryWSDTO.getProductCode(), "Failed to delete product from bundle "));
				}
			}
			else{
				addList.add(bundleEntryWSDTO);
			}
		});
		addList.forEach(bundleEntryWSDTO -> {
			try {
				bundleCartFacade.addToCart(bundleEntryWSDTO.getProductCode(),1,bundleEntryWSDTO.getEntryGroupNumber());
				productWrapperDataList.add(createProductWrapperData(bundleEntryWSDTO.getProductCode(), null));
			} catch (CommerceCartModificationException e) {
				LOG.error("Exception adding product "+ bundleEntryWSDTO.getProductCode() +" to bundle with exception "+e.getMessage(),e);
				productWrapperDataList.add(createProductWrapperData(bundleEntryWSDTO.getProductCode(), "Failed to add product to bundle "));
			}
		});
		return productWrapperDataList;
	}

	/**
	 * Iterate and delete products from cart for the provided bundle number
	 * @param bundleNo the bundle number
	 * @return list of {@link ProductWrapperData}
	 */
	public List<ProductWrapperData> deleteBundleFromCart(int bundleNo) {
		final List<ProductWrapperData> productWrapperDataList = new ArrayList<>();
		final CartModel cartModel = getCartService().getSessionCart();
		List<CartEntryModel> cartEntries=gpBundleService.getOrderEntriesByBundleNum(bundleNo,cartModel.getCode());
		if(cartEntries!=null) {
			cartEntries.forEach(cartEntry -> {
				try {
					updateCartEntry(cartEntry.getEntryNumber(), 0);
					productWrapperDataList.add(createProductWrapperData(cartEntry.getProduct().getCode(), null));
				} catch (CommerceCartModificationException e) {
					LOG.error("Exception while deleting entry:" + cartEntry.getEntryNumber() + " for product:" + cartEntry.getProduct().getCode() + " for bundle:" + bundleNo + " with exception " + e.getMessage(),e);
					productWrapperDataList.add(createProductWrapperData(cartEntry.getProduct().getCode(), "Failed to delete entry:" + cartEntry.getEntryNumber() + " for product from bundle:" + bundleNo));
				}
			});
		}
		return productWrapperDataList;
	}

	/**
	 * Updates map components with entryGroup numbers
	 * @param entryGroupDataList the list of {@link EntryGroupData}
	 * @param bundleMap          the bundle map used
	 */
	private void mapBundleTemplatesWithGroup(List<EntryGroupData> entryGroupDataList, Map<String,Integer> bundleMap){
		entryGroupDataList.forEach(entryGroupData -> {
			if(CollectionUtils.isNotEmpty(entryGroupData.getChildren())){
				mapBundleTemplatesWithGroup(entryGroupData.getChildren(),bundleMap);
			}else{
				if(StringUtils.isNotEmpty(entryGroupData.getExternalReferenceId())) {
					bundleMap.put(entryGroupData.getExternalReferenceId(), entryGroupData.getGroupNumber());
				}
			}
		});
	}

	/**
	 * Gets bundle product details using bundle number
	 * @param bundleNo       the bundle number
	 * @param rootTemplateId the root template id
	 * @return {@link BundleProductData}
	 */
	public BundleProductData getBundleProductsInfo(int bundleNo, String rootTemplateId){
		final CartModel cartModel = getCartService().getSessionCart();
		List<CartEntryModel> cartEntries=gpBundleService.getOrderEntriesByBundleNum(bundleNo,cartModel.getCode());
		if(cartEntries!=null) {
			BundleProductData bundleProductData = new BundleProductData();
			bundleProductData.setBundleNo(String.valueOf(bundleNo));
			bundleProductData.setRootTemplateId(rootTemplateId);
			Map<String, String> entryGroupMap = new HashMap<>();
			List<BundleEntryData> bundleEntryDataList = new ArrayList<>();
			cartEntries.forEach(cartEntryModel -> {
				buildBundleEntryGroupMap(entryGroupMap, cartModel.getEntryGroups(), cartEntryModel.getEntryGroupNumbers().iterator().next());
				bundleEntryDataList.add(gpBundleProductDataConverter.convert(cartEntryModel));
			});
			bundleProductData.setEntryGroupMap(entryGroupMap);
			bundleProductData.setBundleEntries(bundleEntryDataList);
			return bundleProductData;
		}
		return null;
	}
	
	private void updateSampleOrderLimitForSampleProduct(ProductModel productModel) {
		productModel.setMaxOrderQuantity(productModel.getSampleOrderLimit());
	}
	
	private AbstractOrderEntryModel getEntryForNumber(final AbstractOrderModel order, final int number) {
		final List<AbstractOrderEntryModel> entries = order.getEntries();
		if (entries != null && !entries.isEmpty()) {
			final Integer requestedEntryNumber = Integer.valueOf(number);
			for (final AbstractOrderEntryModel entry : entries) {
				if (entry != null && requestedEntryNumber.equals(entry.getEntryNumber())) {
					return entry;
				}
			}
		}
		return null;
	}
	
	private ProductModel getProductToUpdate(CommerceCartParameter parameters) {
		final long entryNumber = parameters.getEntryNumber();
		final CartModel cartModel = parameters.getCart();
		validateParameterNotNull(cartModel, "Cart model cannot be null");
		final AbstractOrderEntryModel entryToUpdate = getEntryForNumber(cartModel, (int) entryNumber);
		return null != entryToUpdate ? entryToUpdate.getProduct():null;
	}

	/**
	 * Builds the bundle entry Group Map for the compomnents in the bundle
	 * @param entryGroupMap
	 * @param entryGroups
	 * @param cartEntryGroupNumber
	 * @return void
	 */
	private void buildBundleEntryGroupMap(Map<String, String> entryGroupMap, List<EntryGroup> entryGroups, Integer cartEntryGroupNumber){
		entryGroups.forEach(entryGroup -> {
			if(entryGroupMap.isEmpty() && !entryGroup.getChildren().isEmpty()) {
				final Map<String, String> tempMap =  new HashMap<>();
				final AtomicBoolean setMap = new AtomicBoolean(false);
				entryGroup.getChildren().forEach(childEntryGroup -> {
					tempMap.put(childEntryGroup.getExternalReferenceId(), childEntryGroup.getGroupNumber().toString());
					if(childEntryGroup.getGroupNumber().equals(cartEntryGroupNumber)){
						setMap.set(true);
					}
				});
				if(setMap.get()){
					entryGroupMap.putAll(tempMap);
				}
			}
		});
	}
}

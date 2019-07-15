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
package com.gp.commerce.facades.cart.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commercefacades.i18n.comparators.CountryComparator;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.ZoneDeliveryModeData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.enums.ExportStatus;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.PriceValue;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.exceptions.GPSampleCartException;
import com.gp.commerce.core.model.GPEndUserLegalTermsModel;
import com.gp.commerce.core.model.ShippingRestrictionModel;
import com.gp.commerce.core.model.SplitEntryModel;
import com.gp.commerce.core.services.GPCartService;
import com.gp.commerce.core.services.GPDeliveryService;
import com.gp.commerce.core.services.GPSampleCartService;
import com.gp.commerce.core.strategies.GPDefaultCustomerNameStrategy;
import com.gp.commerce.dto.group.dto.DeliveryAddressGroupWsDTO;
import com.gp.commerce.facade.data.IncompatibleProductsData;
import com.gp.commerce.facade.order.data.GPRestrictionProductListWsDTO;
import com.gp.commerce.facade.order.data.GPRestrictionProductWsDTO;
import com.gp.commerce.facade.order.data.LeaseAgreementData;
import com.gp.commerce.facade.order.data.SaveAgreementData;
import com.gp.commerce.facade.order.data.ShippingRestrictionsData;
import com.gp.commerce.facades.cart.GPCartFacade;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gp.commerce.order.data.GPOrderSampleErrorResponseData;
import com.gp.commerce.order.data.GPSampleCartResponseData;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.leaseagreement.dto.GPAgreementResponseDTO;
import com.gpintegration.leaseagreement.dto.GPLeaseAgreementDTO;
import com.gpintegration.service.GPCommerceLeaseAgreementService;
import com.gpintegration.service.impl.GPDefaultAddressVerificationService;



/**
 * The Class GPDefaultCartFacade.
 *
 * @author dadidam
 */
public class GPDefaultCartFacade implements GPCartFacade {

	private static final String ZERO = "0";
	private GPCartService gpCartService;
	private CartService cartService;
	private GPDefaultAddressVerificationService avsService;
	private Converter<ShippingRestrictionModel, ShippingRestrictionsData> gpShippingRestrictionConverter;
	private GPCommerceLeaseAgreementService gpCommerceLeaseAgreementService;
	private UserService userService;
	private static final Logger LOG = Logger.getLogger(GPDefaultCartFacade.class);

	@Resource
	private DeliveryService deliveryService;
	private Converter<ZoneDeliveryModeModel, ZoneDeliveryModeData> zoneDeliveryModeConverter;
	private Converter<DeliveryModeModel, DeliveryModeData> deliveryModeConverter;
	private PriceDataFactory priceDataFactory;
	@Resource
	private GPDeliveryService gpDeliveryService;
	private Converter<DeliveryModeData, DeliveryModeModel> deliveryModeReverseConverter;
	private SessionService sessionService;
	private ModelService modelService;
	private Converter<GPEndUserLegalTermsModel, LeaseAgreementData> gpLeaseDataConverter;
	@Resource(name = "countryConverter")
	private Converter<CountryModel, CountryData> countryConverter;
	@Resource(name = "gpDefaultCustomerNameStrategy")
	private GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy;
	@Resource(name = "gpSampleCartService")
	private GPSampleCartService gpSampleCartService ;

	@Override
	public void shareCart(final String toEmail,final String senderEmail,final String senderName, final CartData cartData) {

		final CartModel cart = getCartService().getSessionCart();
		getGpCartService().shareCart(toEmail,senderEmail,senderName, cart);

	}

	@Override
	public List<ShippingRestrictionsData> fetchShippingRestrictions(final GPRestrictionProductListWsDTO shippingProductList)
			throws CommerceCartModificationException {
		final List<ShippingRestrictionsData> restrictionList = new ArrayList<>();

		final List<ShippingRestrictionModel> restrictedProductList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(shippingProductList.getShippingDetails())) {
			for (final GPRestrictionProductWsDTO shippingProd : shippingProductList.getShippingDetails()) {
				final List<ShippingRestrictionModel> restrictedList = getGpCartService().fetchShippingRestrictions(
						shippingProd.getProductCode(), shippingProd.getCountry().getIsocode(),
						shippingProd.getRegion().getIsocodeShort());
				if (CollectionUtils.isNotEmpty(restrictedList)) {
					for(final ShippingRestrictionModel restriction:restrictedList) {
						if(null!=restriction && null!=restriction.getDeliveryMode() && restriction.getDeliveryMode().equals(ZERO))
						{
							restrictedProductList.add(restriction);
						}
					}
				}
			}

			for (final ShippingRestrictionModel shipRestModel : restrictedProductList) {
				final ShippingRestrictionsData shippingRestData = getGpShippingRestrictionConverter()
						.convert(shipRestModel);

				restrictionList.add(shippingRestData);
			}
		}
		return restrictionList;
	}
	
	@Override
	public GPSampleCartResponseData orderSampleCart() throws GPSampleCartException{
		return gpSampleCartService.sendRequestToGpExpress() ;
		
	}


	@Override
	public LeaseAgreementData getLeaseAgreementForCountry(final String country) {
		
		List<GPEndUserLegalTermsModel> legalTerms = getGpCartService().getLeaseAgreementForCountry(country);
		
		return !legalTerms.isEmpty() ? getGpLeaseDataConverter().convert(legalTerms.get(0)) : null;
		
	}

	@Override
	public SaveAgreementData saveLeaseAgreement(final GPLeaseAgreementDTO saveLeaseAgreementReq) throws GPIntegrationException {

		final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();

		if(customer instanceof B2BCustomerModel) {
			final String[] name = gpDefaultCustomerNameStrategy.splitName(customer.getName());
			saveLeaseAgreementReq.setFirstName(name[0]);
			saveLeaseAgreementReq.setLastName(name[1]);
			saveLeaseAgreementReq.setContactEmail(customer.getContactEmail());

			final Optional<AddressModel> address =customer.getAddresses().stream().reduce((first,second) -> second);

			if (address.isPresent()) {
				final AddressModel add = address.get();
				if (null != add.getPhone1()) {
					saveLeaseAgreementReq.setContactPhone(add.getPhone1());
				}
				if (null != add.getStreetnumber() && null != add.getStreetname()) {
					saveLeaseAgreementReq
							.setStreet(add.getStreetnumber() + " " + add.getStreetname());
				}
				if (null != add.getTown()) {
					saveLeaseAgreementReq.setCity(add.getTown());
				}
				if (null != add.getRegion()) {
					saveLeaseAgreementReq.setState(add.getRegion().getIsocode());
				}
				if (null != add.getCountry()) {
					saveLeaseAgreementReq.setCountry(add.getCountry().getIsocode());
				}
				if (null != add.getCompany()) {
					saveLeaseAgreementReq.setCompanyName(add.getCompany());
				}
			}
			}

		final GPAgreementResponseDTO resp = getgpCommerceLeaseAgreementService().createLeaseAgreement(saveLeaseAgreementReq).getAgreementResponse();
		return saveAgreement(resp);
	}


	/**
	 * This method saves agreement
	 * @param resp
	 * 			the response
	 * @return save agreement data
	 */
	public SaveAgreementData saveAgreement(final GPAgreementResponseDTO resp) {

		final SaveAgreementData saveAgreement  = new SaveAgreementData();
		if(null != resp && null !=resp.getAgreementId()) {
			saveAgreement.setAgreementId(resp.getAgreementId());
		}
		if(null != resp && null !=resp.getStatus()) {
			saveAgreement.setStatus(resp.getStatus());
		}
		if(null != resp && null !=resp.getErrorCode()) {
			saveAgreement.setErrorCode(resp.getErrorCode());
		}
		if(null != resp && null !=resp.getErrorMessage()) {
			saveAgreement.setErrorMessage(resp.getErrorMessage());
		}
		return saveAgreement;
	}

	public Converter<ShippingRestrictionModel, ShippingRestrictionsData> getGpShippingRestrictionConverter() {
		return gpShippingRestrictionConverter;
	}
	public void setGpShippingRestrictionConverter(
			final Converter<ShippingRestrictionModel, ShippingRestrictionsData> gpShippingRestrictionConverter) {
		this.gpShippingRestrictionConverter = gpShippingRestrictionConverter;
	}

	public boolean validateCouponforSite(final String siteId,String couponCode) {
		AbstractCouponModel couponModel = gpCartService.getCouponForCode(couponCode);
		if(null == couponModel) {
			if(couponCode.contains("-")) {
				 couponCode=couponCode.split("-")[0];
			}
			couponModel =gpCartService.getCouponForCode(couponCode);
			if(null == couponModel) {
				return false;
			}
		}
		if(null != couponModel.getBaseSite() && siteId.equalsIgnoreCase(couponModel.getBaseSite().getUid())) {
			return true;
		}
		return false;
	}


	/**
	 * This method verifies address
	 * @param data
	 * 			the data
	 * @return verified address data
	 */
	public AddressData verifyAddress(AddressData data) {
		try {
			data = getAvsService().verifyAddress(data);
		} catch (final GPIntegrationException e) {
			LOG.error(e.getMessage(), e);
			data.setFormattedAddress(e.getMessage());
		}
		return data;
	}

	/**
	 * This method suggests addresses
	 * @param data
	 * 			the data
	 * @return list of suggested addresses
	 */
	public List<AddressData> suggestAddresses(final AddressData data) {
		List<AddressData> addressList = null;
		try {
			addressList = getAvsService().suggestAddresses(data);
		} catch (final GPIntegrationException e) {
			LOG.error(e.getMessage(), e);
			data.setFormattedAddress(e.getMessage());
			addressList = new ArrayList<>();
			addressList.add(data);
		}
		return addressList;
	}

	@Override
	public Map<String, List<DeliveryModeData>> getSupportedDeliveryModes(final DeliveryAddressGroupWsDTO deliveryGroupDto)
			throws Exception {
		final Map<String, List<DeliveryModeData>> deliveryModeListMap = new HashMap<>();
		final CartModel cartModel = getCartService().getSessionCart();
		if (cartModel != null) {
			for (final DeliveryModeModel deliveryModeModel : getDeliveryService()
					.getSupportedDeliveryModeListForOrder(cartModel)) {
				final Map<String, DeliveryModeData> deliveryModeMap = convert(deliveryModeModel, deliveryGroupDto);
				if (!deliveryModeListMap.isEmpty()) {
					for (final Entry<String, DeliveryModeData> deliveryMode : deliveryModeMap.entrySet()) {
						final String key = deliveryMode.getKey();
						final DeliveryModeData deliveryModeData = deliveryMode.getValue();
						if (deliveryModeListMap.containsKey(key)) {
							final List<DeliveryModeData> delivery = deliveryModeListMap.get(key);
							delivery.add(deliveryModeData);
							}
						}
				} else {
					addDeliveryModeToMap(deliveryModeMap, deliveryModeListMap);
				}
			}
		}
		return deliveryModeListMap;
	}
	@Override
	public void updateSpliEntriesWithDeliveryModes(final Map<String, List<DeliveryModeData>> deliveryModeMap){
	final CartModel cartModel = getCartService().getSessionCart();
	try{
	final List<AbstractOrderEntryModel> entries =cartModel.getEntries();
		for(final AbstractOrderEntryModel  entry : entries){
			if(CollectionUtils.isNotEmpty(entry.getSplitEntry())){
				for(final SplitEntryModel splitEntryModel : entry.getSplitEntry() ){
					if(deliveryModeMap.containsKey(splitEntryModel.getDeliveryAddress().getPk().toString())){
						final List<DeliveryModeData> deliveryModeData =deliveryModeMap.get(splitEntryModel.getDeliveryAddress().getPk().toString());
						final List<DeliveryModeModel> deliveryModeModelList  =getDeliveryModeReverseConverter().convertAll(deliveryModeData) ;
						splitEntryModel.setDeliveryModes(deliveryModeModelList);
							getGpCartService().updateSplitEntry(splitEntryModel);
					}
				}
			}
		}
	}catch(final Exception e){
		LOG.error("Error occured while updating delivery modes for split Entry" +e);
	}
	}

	/**
	 * This method adds delivery mode to map
	 * @param deliveryModeMap
	 * 			the delivery mode map
	 * @param deliveryModeListMap
	 * 			the delivery mode map list
	 */
	public void addDeliveryModeToMap(final Map<String, DeliveryModeData> deliveryModeMap,
			final Map<String, List<DeliveryModeData>> deliveryModeListMap) {
		for (final Entry<String, DeliveryModeData> deliveryModeEntry : deliveryModeMap.entrySet()) {
			final List<DeliveryModeData> deliveryModeList = new ArrayList<>();
			final String key = deliveryModeEntry.getKey();
			final DeliveryModeData deliveryModeData = deliveryModeEntry.getValue();
			deliveryModeList.add(deliveryModeData);
			deliveryModeListMap.put(key, deliveryModeList);
		}
	}

	@Override
	public List<? extends DeliveryModeData> getSupportedDeliveryModes() throws Exception {
		final List<DeliveryModeData> result = new ArrayList<>();
		final CartModel cartModel = getCartService().getSessionCart();
		if (cartModel != null) {
			for (final DeliveryModeModel deliveryModeModel : getDeliveryService()
					.getSupportedDeliveryModeListForOrder(cartModel)) {
				result.add(convert(deliveryModeModel));
			}
		}
		return result;
	}

	@Override
	public void updateDeliveryInstruction(final String deliveryInst) {
		final CartModel cartModel = getCartService().getSessionCart();
		getModelService().refresh(cartModel);
		final DeliveryModeModel deliverymode=cartModel.getDeliveryMode() ;
		if(deliverymode!=null) {
			deliverymode.setDescription(deliveryInst);
			getGpCartService().updateDeliveryInstruction(deliverymode);
		}

	}
	protected DeliveryModeData convert(final DeliveryModeModel deliveryModeModel) throws Exception {
		if (deliveryModeModel instanceof ZoneDeliveryModeModel) {
			final CartModel cartModel = getCartService().getSessionCart();

			if (cartModel != null) {
				final PriceValue deliveryCost = getDeliveryService().getDeliveryCostForDeliveryModeAndAbstractOrder(deliveryModeModel, cartModel);
				return getDeliveryModeData(deliveryCost, deliveryModeModel,cartModel);
			}
			return null;
		}
		return getDeliveryModeConverter().convert(deliveryModeModel);
	}


	protected Map<String, DeliveryModeData> convert(final DeliveryModeModel deliveryModeModel,final DeliveryAddressGroupWsDTO deliveryGroupDto) throws Exception {
		final Map<String, DeliveryModeData> zoneDeliveryMap = new HashMap<>();
		if (deliveryModeModel instanceof ZoneDeliveryModeModel) {
			final CartModel cartModel = getCartService().getSessionCart();

			if (cartModel != null) {
			final Map<String, PriceValue> deliveryCostList = getGpDeliveryService().getDeliveryCostForDeliveryModeAndAbstractOrder(deliveryModeModel, cartModel, deliveryGroupDto);
				for (final Entry<String, PriceValue> deliveryCostEntry : deliveryCostList.entrySet()) {
					final PriceValue deliveryCost = deliveryCostEntry.getValue();
					final ZoneDeliveryModeData zoneDeliveryModeData = getDeliveryModeData(deliveryCost, deliveryModeModel,
							cartModel);
					zoneDeliveryMap.put(deliveryCostEntry.getKey(), zoneDeliveryModeData);
					}
				return zoneDeliveryMap;
			}
			return zoneDeliveryMap;
		}
		zoneDeliveryMap.put(StringUtils.EMPTY, getDeliveryModeConverter().convert(deliveryModeModel));
		return zoneDeliveryMap ;
	}

	/**
	 * Method to get delivery mode data.
	 *
	 * @param deliveryCost     the delivery cost
	 * @param deliveryModeMode the delivery mode mode
	 * @param cartModel        the cart model
	 * @return delivery mode data
	 */
	public ZoneDeliveryModeData getDeliveryModeData(PriceValue deliveryCost, final DeliveryModeModel deliveryModeMode,
			final CartModel cartModel) {
		String deliveryModeType = null;
		final ZoneDeliveryModeModel zoneDeliveryModeModel = (ZoneDeliveryModeModel) deliveryModeMode;
		final ZoneDeliveryModeData zoneDeliveryModeData = getZoneDeliveryModeConverter().convert(zoneDeliveryModeModel);
		final List<ZoneDeliveryModeValueModel> zoneDeliveryList = zoneDeliveryModeModel.getValues().stream()
				.filter(value -> value.getZone().getCountries().contains(cartModel.getDeliveryAddress().getCountry()))
				.collect(Collectors.toList());
		for (final ZoneDeliveryModeValueModel zoneValue : zoneDeliveryList) {
			if (zoneValue.getValue().equals(deliveryCost.getValue())) {
				deliveryModeType = zoneValue.getDeliveryModeType();
			}
		}
		if (deliveryCost != null) {
			if (null!=deliveryModeType && deliveryModeType.equals(GpcommerceFacadesConstants.PERCENTAGE)) {
				final double updatedDeliveryCost = cartModel.getSubtotal() * (deliveryCost.getValue()) / 100;
				deliveryCost = new PriceValue(cartModel.getCurrency().getIsocode(), updatedDeliveryCost, false);
			}
			zoneDeliveryModeData.setDeliveryCost(getPriceDataFactory().create(PriceDataType.BUY,
					BigDecimal.valueOf(deliveryCost.getValue()), deliveryCost.getCurrencyIso()));
		}

		return zoneDeliveryModeData;
	}

	/* (non-Javadoc)
	 * @see com.gp.commerce.facades.cart.GPCartFacade#updateLeaseDetails(com.gp.commerce.facade.order.data.SaveAgreementData)
	 */
	@Override
	public void updateLeaseName(final String leaseName) {
		CartModel sessionCart =null;
		if(cartService.hasSessionCart()) {
			sessionCart = cartService.getSessionCart();
		}
		if(StringUtils.isNotEmpty(leaseName) && null != sessionCart)
		{
			sessionCart.setAgreementName(leaseName);
			getGpCartService().updateLease(sessionCart);
		}
	}

	@Override
	public void updateLeaseAgreementId(final String agreementId, final String leaseTermId)
	{
		CartModel sessionCart =null;
		if(cartService.hasSessionCart()) {
			sessionCart = cartService.getSessionCart();
		}
		if (null != sessionCart) {
			if (StringUtils.isNotEmpty(agreementId)) {
				sessionCart.setAgreementId(agreementId);
			}
			if (StringUtils.isNotEmpty(leaseTermId)) {
				sessionCart.setLeaseTermId(leaseTermId);
			}
			// Set sfdc lease agreement status based
			sessionCart.setSfdcLeaseAgreementExportStatus(
					Config.getBoolean(GpcommerceCoreConstants.ENABLE_LEASE_AGREEMENT, true) ? ExportStatus.EXPORTED
							: ExportStatus.NOTEXPORTED);
			getGpCartService().updateLease(sessionCart);
		}
	}

	/**
	 * Checks if there are incompatible products in cart.
	 *
	 * @return IncompatibleProductsData
	 */
	@Override
	public IncompatibleProductsData checkIncompatibleProducts() {
		CartModel sessionCart = null;
		if (cartService.hasSessionCart()) {
			sessionCart = cartService.getSessionCart();
		}
		final IncompatibleProductsData response = new IncompatibleProductsData();
		if (null != sessionCart) {
			final List<ProductModel> incompatibleProducts = gpCartService.checkIncompatibleProducts(sessionCart);
			if (CollectionUtils.isNotEmpty(incompatibleProducts)) {
				response.setIncompatible(true);

				final List<String> productNames = new ArrayList<>();
				for (final ProductModel product : incompatibleProducts) {
					productNames.add(product.getName());
				}

				response.setProductNames(productNames);
			}
		}
		return response;
	}

	@Override
	public List<CountryData> getShippingCountries()
	{
		final List<CountryData> countries = Converters.convertAll(getGpDeliveryService().getShippingCountries(),
				countryConverter);
		Collections.sort(countries, CountryComparator.INSTANCE);
		return countries;
	}


	public Converter<ZoneDeliveryModeModel, ZoneDeliveryModeData> getZoneDeliveryModeConverter() {
		return zoneDeliveryModeConverter;
	}

	public void setZoneDeliveryModeConverter(
			final Converter<ZoneDeliveryModeModel, ZoneDeliveryModeData> zoneDeliveryModeConverter) {
		this.zoneDeliveryModeConverter = zoneDeliveryModeConverter;
	}

	public Converter<DeliveryModeModel, DeliveryModeData> getDeliveryModeConverter() {
		return deliveryModeConverter;
	}

	public void setDeliveryModeConverter(final Converter<DeliveryModeModel, DeliveryModeData> deliveryModeConverter) {
		this.deliveryModeConverter = deliveryModeConverter;
	}

	public PriceDataFactory getPriceDataFactory() {
		return priceDataFactory;
	}

	public void setPriceDataFactory(final PriceDataFactory priceDataFactory) {
		this.priceDataFactory = priceDataFactory;
	}

	public GPDeliveryService getGpDeliveryService() {
		return gpDeliveryService;
	}

	public void setGpDeliveryService(final GPDeliveryService gpDeliveryService) {
		this.gpDeliveryService = gpDeliveryService;
	}

	public DeliveryService getDeliveryService() {
		return deliveryService;
	}

	public void setDeliveryService(final DeliveryService deliveryService) {
		this.deliveryService = deliveryService;
	}

	public GPCartService getGpCartService() {
		return gpCartService;
	}

	public void setGpCartService(final GPCartService gpCartService) {
		this.gpCartService = gpCartService;
	}

	public GPDefaultAddressVerificationService getAvsService() {
		return avsService;
	}

	public void setAvsService(final GPDefaultAddressVerificationService avsService) {
		this.avsService = avsService;
	}

	public Converter<DeliveryModeData, DeliveryModeModel> getDeliveryModeReverseConverter() {
		return deliveryModeReverseConverter;
	}

	public void setDeliveryModeReverseConverter(
			final Converter<DeliveryModeData, DeliveryModeModel> deliveryModeReverseConverter) {
		this.deliveryModeReverseConverter = deliveryModeReverseConverter;
	}

	public CartService getCartService() {
		return cartService;
	}
	public void setCartService(final CartService cartService) {
		this.cartService = cartService;
	}

	/**
	 * Gets the gp commerce lease agreement service.
	 *
	 * @return the gp commerce lease agreement service
	 */
	public GPCommerceLeaseAgreementService getgpCommerceLeaseAgreementService() {
		return gpCommerceLeaseAgreementService;
	}

	/**
	 * Sets the gp commerce lease agreement service.
	 *
	 * @param gpCommerceLeaseAgreementService the new gp commerce lease agreement service
	 */
	public void setgpCommerceLeaseAgreementService(final GPCommerceLeaseAgreementService gpCommerceLeaseAgreementService) {
		this.gpCommerceLeaseAgreementService = gpCommerceLeaseAgreementService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService) {
		this.sessionService = sessionService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
	public Converter<GPEndUserLegalTermsModel, LeaseAgreementData> getGpLeaseDataConverter() {
		return gpLeaseDataConverter;
	}

	public void setGpLeaseDataConverter(Converter<GPEndUserLegalTermsModel, LeaseAgreementData> gpLeaseDataConverter) {
		this.gpLeaseDataConverter = gpLeaseDataConverter;
	}

	@Override
	public void updatePreviousDeliveryMode(final String deliveryModeCode) {
		final CartModel cartModel = getCartService().getSessionCart();
		cartModel.setPreviousDeliveryMode(getDeliveryService().getDeliveryModeForCode(deliveryModeCode));
		modelService.save(cartModel);
	}
}

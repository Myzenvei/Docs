/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.strategies;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Stream;
import java.util.HashSet;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.common.math.DoubleMath;
import com.gp.commerce.core.enums.GPConsignmentEntryStatus;
import com.gp.commerce.core.exceptions.GPInsufficientStockLevelException;
import com.gp.commerce.core.model.SplitEntryModel;
import com.gp.commerce.core.order.impl.GPDefaultCommerceCartCalculationStrategy;
import com.gp.commerce.core.stock.services.GPStockService;
import com.gp.commerce.core.util.GPSiteConfigUtils;
import de.hybris.platform.b2b.enums.PermissionStatus;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BOrderThresholdPermissionModel;
import de.hybris.platform.b2b.model.B2BOrderThresholdTimespanPermissionModel;
import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.b2b.model.B2BPermissionResultModel;
import de.hybris.platform.b2b.services.B2BPermissionService;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.impl.DefaultCommercePlaceOrderStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.order.DeliveryModeService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.order.CartService;

import de.hybris.platform.util.PriceValue;
import static de.hybris.platform.multicountry.constants.MulticountryConstants.AVAILABILITY_GROUPS;
public class GPCommercePlaceOrderStrategy extends DefaultCommercePlaceOrderStrategy
{
	private static final Logger LOG = Logger.getLogger(GPCommercePlaceOrderStrategy.class);
	private static final String GUEST_CREATE_ACCOUNT = "guestRegisterAfterPlaceOrder";

	
	@Resource(name = "b2bPermissionService")
	private B2BPermissionService<B2BCustomerModel, B2BPermissionResultModel> b2bPermissionService;
	private KeyGenerator consignmentCodeGenerator;
	private GPStockService stockService;
	@Resource(name="deliveryModeService")
	private DeliveryModeService deliveryModeService;

	@Resource(name="cartService")
	private CartService cartService;
	
	@Resource(name = "userFacade")
	private UserFacade userFacade;
	
	@Resource(name = "sessionService")
	private SessionService sessionService;
	
	private static final double EPSILON = 0.01d;
	private static final String DECIMAL_FORMAT = "##.00";

	@Resource(name="promotionsService")
	private PromotionsService promotionsService;
	
	@Resource(name="timeService")
	private TimeService timeService;
	
	
	@Resource(name="commerceCartCalculationStrategy")
	private GPDefaultCommerceCartCalculationStrategy commerceCartCalculationStrategy ;
 

	@Override
	public CommerceOrderResult placeOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException {
		CartModel cartModel = parameter.getCart();
		AbstractOrderEntryModel abstractOrder = parameter.getCart().getEntries().get(0);
		final CommerceCartParameter cartParameter = createCommerceCartParameter();
		if (parameter.getCart().getIsSubscription() != null && parameter.getCart().getIsSubscription()) {
			getBaseSiteService().setCurrentBaseSite(parameter.getCart().getSite().getUid(), true);
			if (CollectionUtils.isNotEmpty(commerceCartCalculationStrategy.getPromotionGroups())) {

				cartParameter.setEnableHooks(true);
				cartParameter.setCart(parameter.getCart());
				cartParameter.setQuantity(abstractOrder.getQuantity());
				cartParameter.setProduct(abstractOrder.getProduct());
				cartParameter.setCreateNewEntry(false);
				parameter.getCart().setCalculated(false);
				commerceCartCalculationStrategy.calculateCart(cartParameter);
				cartModel = cartParameter.getCart();
			}

		} 
		validateParameterNotNull(cartModel, "Cart model cannot be null");
		final CommerceOrderResult result = new CommerceOrderResult();
		try
		{
			beforePlaceOrder(parameter);
			if (getCalculationService().requiresCalculation(cartModel))
			{
				// does not make sense to fail here especially since we don't fail below when we calculate order.
				LOG.error(String.format("CartModel's [%s] calculated flag was false", cartModel.getCode()));
			}

			final CustomerModel customer = (CustomerModel) cartModel.getUser();
			validateParameterNotNull(customer, "Customer model cannot be null");

			final OrderModel orderModel = getOrderService().createOrderFromCart(cartModel);
			if (orderModel != null)
			{
				// Reset the Date attribute for use in determining when the order was placed
				orderModel.setDate(getTimeService().getCurrentTime());



				// Store the current site and store on the order
				if(parameter.getCart().getIsSubscription()!=null && parameter.getCart().getIsSubscription()) {
					getBaseSiteService().setCurrentBaseSite(parameter.getCart().getSite().getUid(), true);
					orderModel.setSite(getBaseSiteService().getCurrentBaseSite());
					orderModel.setStore(parameter.getCart().getStore());
					orderModel.setUser(parameter.getCart().getUser());
					sessionService.getCurrentSession().setAttribute(AVAILABILITY_GROUPS, parameter.getCart().getStore().getAvailabilityGroups());
					sessionService.getCurrentSession().setAttribute("soldToRootcategory", "DEFAULT");
					sessionService.getCurrentSession().setAttribute("soldToUnitId", "DEFAULT");
					sessionService.getCurrentSession().setAttribute("Europe1PriceFactory_UPG", parameter.getCart().getStore().getUserPriceGroup());
				}
				else {
					orderModel.setSite(getBaseSiteService().getCurrentBaseSite());
					orderModel.setStore(getBaseStoreService().getCurrentBaseStore());
				}
				orderModel.setLanguage(getCommonI18NService().getCurrentLanguage());

				if (parameter.getSalesApplication() != null)
				{
					orderModel.setSalesApplication(parameter.getSalesApplication());
				}

				// clear the promotionResults that where cloned from cart PromotionService.transferPromotionsToOrder will copy them over bellow.
				orderModel.setAllPromotionResults(Collections.<PromotionResultModel> emptySet());
				// if Consignment creation fails, order placement should fail : No Exception Handling is used 
				createConsignmentForOrder(orderModel);
				getModelService().saveAll(customer, orderModel);

				if (cartModel.getPaymentInfo() != null && cartModel.getPaymentInfo().getBillingAddress() != null)
				{
					final AddressModel billingAddress = cartModel.getPaymentInfo().getBillingAddress();
					orderModel.setPaymentAddress(billingAddress);
					orderModel.getPaymentInfo().setBillingAddress(getModelService().clone(billingAddress));
					getModelService().save(orderModel.getPaymentInfo());
				}
				orderModel.setStatus(OrderStatus.CREATED);
				if (GPSiteConfigUtils.isB2BSite(getBaseSiteService().getCurrentBaseSite()))
				{
					
					//Pick unit from delivery address from split entry of first order entry
					final AbstractOrderEntryModel entry = CollectionUtils.isNotEmpty(orderModel.getEntries())
							? orderModel.getEntries().get(0) : null;
					final SplitEntryModel splitEntry = (null != entry && CollectionUtils.isNotEmpty(entry.getSplitEntry()))
							? entry.getSplitEntry().get(0) : null;
					//Set unit at order level
					orderModel.setUnit((null != splitEntry && null != splitEntry.getDeliveryAddress())
							? splitEntry.getDeliveryAddress().getB2bUnit() : null); 
					
					final List<Class<? extends B2BPermissionModel>> permissionsThatNeedApproval = new ArrayList<>();
					permissionsThatNeedApproval.add(B2BOrderThresholdPermissionModel.class);
					permissionsThatNeedApproval.add(B2BOrderThresholdTimespanPermissionModel.class);
					final B2BCustomerModel user = (B2BCustomerModel) orderModel.getUser();
					final Set<B2BPermissionResultModel> evaluatePermissions = b2bPermissionService.evaluatePermissions(orderModel,
							user, permissionsThatNeedApproval);


					final Set<B2BPermissionModel> violatedPemissions = new HashSet<>();

					evaluatePermissions.stream()
							.filter(item -> (null != item.getPermission() && PermissionStatus.PENDING_APPROVAL.equals(item.getStatus())))
							.forEach(item -> violatedPemissions.add(item.getPermission()));
					//setting violated permissions at order level
					orderModel.setB2bViolatedPermissions(CollectionUtils.isNotEmpty(violatedPemissions) ? violatedPemissions : null);
					//Order status is set to PENDING_APPROVAL if permissions are violated
					orderModel.setStatus(
							CollectionUtils.isNotEmpty(violatedPemissions) ? OrderStatus.PENDING_APPROVAL : OrderStatus.CREATED);

				}
				
				getModelService().save(orderModel);
				// Transfer promotions to the order
				getPromotionsService().transferPromotionsToOrder(cartModel, orderModel, false);

				// Calculate the order now that it has been copied
				try
				{
					getCalculationService().calculateTotals(orderModel, false);
				}
				catch (final CalculationException ex)
				{
					LOG.error("Failed to calculate order [" + orderModel + "]", ex);
				}
				
				getModelService().refresh(orderModel);
				getModelService().refresh(customer);

				result.setOrder(orderModel);
				
				//Update stock when order is placed successfully
				try {
					getStockService().reserveStockForOrder(orderModel);
				} catch (GPInsufficientStockLevelException e) {
					throw new InvalidCartException("The product "+e.getCode()+ "in the cart is running low on stock", e);
				}

				this.beforeSubmitOrder(parameter, result);
				getOrderService().submitOrder(orderModel);
			}
			else
			{
				throw new IllegalArgumentException(String.format("Order was not properly created from cart %s", cartModel.getCode()));
			}
		}
		finally
		{
			getExternalTaxesService().clearSessionTaxDocument();
		}
		this.afterPlaceOrder(parameter, result);
		if(userFacade.isAnonymousUser()) {
			cartService.setSessionCart(null);
			JaloSession.getCurrentSession().getCurrentSession().setCart(null);
			sessionService.getCurrentSession().setAttribute(GUEST_CREATE_ACCOUNT, "true");
		}
		return result;
	}
 

	private void createConsignmentForOrder(AbstractOrderModel order) {
		Map<String, List<SplitEntryModel>> addressGroupMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(order.getEntries())) {
			for (AbstractOrderEntryModel entry : order.getEntries()) {
				if (CollectionUtils.isNotEmpty(entry.getSplitEntry())) {
					for (SplitEntryModel splitEntry : entry.getSplitEntry()) {
						String key = splitEntry.getDeliveryAddress().getPk().toString();
						splitEntry.setEntryNumber(entry.getEntryNumber());
						if (null != addressGroupMap.get(key)) {
							List<SplitEntryModel> entryList = addressGroupMap.get(key);
							entryList.add(splitEntry);
						} else {
							List<SplitEntryModel> splitEntries = new ArrayList<>();
							splitEntries.add(splitEntry);
							addressGroupMap.put(key, splitEntries);
						}
					}
				}
			}
		}
		
		for(Entry<String, List<SplitEntryModel>> entry : addressGroupMap.entrySet()) {
			ConsignmentModel consignmentModel=getModelService().create(ConsignmentModel.class);
			consignmentModel.setCode(generateConsignmentCode(consignmentModel));
			consignmentModel.setShippingAddress(entry.getValue().get(0).getDeliveryAddress());
			consignmentModel.setStatus(ConsignmentStatus.READY);
			consignmentModel.setOrder(order);
			TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
			consignmentModel.setCreationtime(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime());
			consignmentModel.setWarehouse(getDefaultWarehouseForStore(order.getStore()));
			if(CollectionUtils.isNotEmpty(entry.getValue())) {
				LOG.info("Shipping tax per consignment: " + entry.getValue().get(0).getShippingTax());
				consignmentModel.setShippingTax(entry.getValue().get(0).getShippingTax());
			} else
				LOG.info("Shipping tax per consignment is null");
			getModelService().save(consignmentModel);
			for(SplitEntryModel splitEntry :entry.getValue()) {
				ConsignmentEntryModel consignmentEntryModel=getModelService().create(ConsignmentEntryModel.class);
				consignmentEntryModel.setConsignmentEntryNumber(splitEntry.getEntryNumber());
				consignmentEntryModel.setOrderEntry(getOrderEntryForNumber(order,splitEntry.getEntryNumber()));
				consignmentEntryModel.setCreationtime(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime());
				consignmentEntryModel.setConsignmentEntryStatus(GPConsignmentEntryStatus.READY);
				consignmentEntryModel.setQuantity(Long.valueOf(splitEntry.getQty()));
				consignmentEntryModel.setMerchantTax(splitEntry.getMerchantTax());
				consignmentEntryModel.setTotalPrice(splitEntry.getPrice());
				consignmentEntryModel.setTotalTax(splitEntry.getTotalTax());
				if (null != splitEntry.getDeliveryMode()) {
					
					if (null != splitEntry.getDeliveryMode().getCode())
					{
						consignmentEntryModel.setDeliveryModeCode(splitEntry.getDeliveryMode().getCode());
					}
					if (null != splitEntry.getDeliveryMode().getDeliveryFormattedPrice())
					{
						consignmentEntryModel.setDeliveryCost(splitEntry.getDeliveryMode().getDeliveryFormattedPrice());
					}
				}
				else {
					LOG.info("This is a single address delivery");
					if (null != order.getDeliveryMode()) {
						consignmentEntryModel.setDeliveryModeCode(order.getDeliveryMode().getCode());
						if (null != order.getDeliveryCost())
						{
							consignmentEntryModel.setDeliveryCost(order.getDeliveryCost());
						}
						else
						{
							LOG.error("Delivery price is not set at order level");
						}
					} 
					else{
						LOG.error("This is a single address delivery and delivery mode is not available");
					}
				}
				consignmentEntryModel.setTotalTaxValues(splitEntry.getTaxValues());
				consignmentEntryModel.setConsignment(consignmentModel);
				consignmentEntryModel.setSplitEntryNumber(splitEntry.getEntryNumber());
				getModelService().save(consignmentEntryModel);
			}
			setProratedDiscountForConsignment(consignmentModel);
			getModelService().save(consignmentModel);			
		}
	}
	
	private void setProratedDiscountForConsignment(ConsignmentModel consignmentModel) {
		DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
		double totalEntryPrice = 0.0;
		for(AbstractOrderEntryModel orderEntry :consignmentModel.getOrder().getEntries()) {
			totalEntryPrice = totalEntryPrice + orderEntry.getTotalPrice();
		}
		
		double orderDiscount = 0.0;
		final List<DiscountValue> orderDiscountList = consignmentModel.getOrder().getGlobalDiscountValues(); // discounts on the cart itself
		if (orderDiscountList != null && !orderDiscountList.isEmpty()) {
			for (final DiscountValue discount : orderDiscountList) {
				final double value = discount.getAppliedValue();
				if (DoubleMath.fuzzyCompare(value, 0, EPSILON) > 0
						&& !CommerceServicesConstants.QUOTE_DISCOUNT_CODE.equals(discount.getCode())) {
					orderDiscount += value;
				}
			}
		}

		if (BigDecimal.valueOf(orderDiscount).compareTo(BigDecimal.valueOf(0.0d)) != 0) {
			decimalFormat.format(orderDiscount);
		}
		
		double consignmentProratedDiscount = 0.0;
		for(ConsignmentEntryModel consignmentEntryModel : consignmentModel.getConsignmentEntries()) {
			double consignmentEntryDiscount=0.0;
			if(consignmentEntryModel.getOrderEntry().getTotalPrice().compareTo(0.0)!=0) {
				consignmentEntryDiscount = (consignmentEntryModel.getOrderEntry().getTotalPrice() / totalEntryPrice) * orderDiscount;
			}
			decimalFormat.format(consignmentEntryDiscount);
			double proratePerEntry = (consignmentEntryDiscount / consignmentEntryModel.getOrderEntry().getQuantity().doubleValue()) * consignmentEntryModel.getQuantity().doubleValue();
			consignmentProratedDiscount = consignmentProratedDiscount + proratePerEntry;
			consignmentEntryModel.setDiscountPrice(Double.valueOf(Double.parseDouble(decimalFormat.format(proratePerEntry))));
			getModelService().save(consignmentEntryModel);
			
		}
		consignmentModel.setProratedDiscount(Double.valueOf(Double.parseDouble(decimalFormat.format(consignmentProratedDiscount))));
	}
	
	private WarehouseModel getDefaultWarehouseForStore(BaseStoreModel store) {
		return store.getWarehouses().stream().filter(warehouse -> warehouse.getDefault()).findFirst().orElse(store.getWarehouses().get(0));
	}
	
	protected CommerceCartParameter createCommerceCartParameter()
	{
		return new CommerceCartParameter();
	}

	/**
	 * Get Cart Entry
	 * @param cartModel
	 * @param number
	 * @return
	 * @throws CartEntryException
	 */
	protected AbstractOrderEntryModel getOrderEntryForNumber(final AbstractOrderModel orderModel, final long number)
	{
		final List<AbstractOrderEntryModel> entries = orderModel.getEntries();
		if (entries != null && !entries.isEmpty())
		{
			final Integer requestedEntryNumber = Integer.valueOf((int) number);
			for (final AbstractOrderEntryModel entry : entries)
			{
				if (entry != null && requestedEntryNumber.equals(entry.getEntryNumber()))
				{
					return entry;
				}
			}
		}
		return null;
	}
	
	protected String generateConsignmentCode(final ConsignmentModel consignment)
	{
		final Object generatedValue = getConsignmentCodeGenerator().generate();
		if (generatedValue instanceof String)
		{
			return (String)generatedValue;
		}
		else
		{
			return String.valueOf(generatedValue);
		}
	}

	public KeyGenerator getConsignmentCodeGenerator() {
		return consignmentCodeGenerator;
	}
	
	public void setConsignmentCodeGenerator(KeyGenerator consignmentCodeGenerator) {
		this.consignmentCodeGenerator = consignmentCodeGenerator;
	}

	protected GPStockService getStockService() {
		return stockService;
	}

	public void setStockService(GPStockService stockService) {
		this.stockService = stockService;
	}
	
	public DeliveryModeService getDeliveryModeService() {
		return deliveryModeService;
	}

	public void setDeliveryModeService(DeliveryModeService deliveryModeService) {
		this.deliveryModeService = deliveryModeService;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

}

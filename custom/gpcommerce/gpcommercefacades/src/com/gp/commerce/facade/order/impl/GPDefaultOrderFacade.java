/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facade.order.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.springframework.web.util.WebUtils;

import com.gp.commerce.core.enums.GPUserApprovalStatusEnum;
import com.gp.commerce.core.model.GPEndUserLegalTermsModel;
import com.gp.commerce.core.model.ShippingNotificationModel;
import com.gp.commerce.core.order.service.GpOrderService;
import com.gp.commerce.core.services.GPConsignmentTrackingService;
import com.gp.commerce.facade.data.NsConsignmentData;
import com.gp.commerce.facade.data.NsItemData;
import com.gp.commerce.facade.order.GPOrderFacade;
import com.gp.commerce.facade.order.data.LeaseAgreementData;
import com.gp.commerce.facade.order.data.SplitEntryListWsDTO;
import com.gp.commerce.facade.order.data.SplitEntryWsDTO;
import com.gpintegration.core.services.event.GPOrderStatusUpdateEvent;

/**
 * The Class GPDefaultOrderFacade.
 */
public class GPDefaultOrderFacade  implements GPOrderFacade{

	private static final Logger LOG = Logger.getLogger(GPDefaultOrderFacade.class);

	private GPConsignmentTrackingService gpConsignmentTrackingService;
	private ConfigurationService configurationService;
	private GpOrderService gpOrderService;
	private CustomerAccountService customerAccountService;
	private EventService eventService;
	private CartService cartService;
	private SessionService sessionService;
	private Converter<GPEndUserLegalTermsModel, LeaseAgreementData> gpLeaseDataConverter;

	public static final String ASM_CARTID= "asmCartId";
	@Resource
	private DeliveryService deliveryService;
	

	/* (non-Javadoc)
	 * @see com.gp.commerce.facade.order.GPOrderFacade#updateOrderStatus(com.gp.commerce.facade.data.NsConsignmentData)
	 * consignmentData is data from NetSuite or Third party
	 * Order Status is updated from Third party after Order is Synch
	 * Search by consignment Id <orderId-EntryNumber-SplitEntry>
	 * Entry search is made by sku (productCode in Hybris)
	 */


	public CartService getCartService()
	{
		return cartService;
	}

	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	@Override
	public NsConsignmentData updateOrderStatus(final NsConsignmentData consignmentData) {
		final Configuration configuration = configurationService.getConfiguration();
		try {
			final ConsignmentModel consignmentModel = gpConsignmentTrackingService.getConsignmentByCode(consignmentData.getHybrisConsignmentId());
			if (null != consignmentModel) {
				if (null == consignmentModel.getPaymentId()) {
					consignmentModel.setPaymentId(consignmentData.getNsPaymentId());
					getGpOrderService().updateOrderConsignment(consignmentModel);
				}
				final ShippingNotificationModel shippingNotificationModel = new ShippingNotificationModel();
				for (final NsItemData item : consignmentData.getItems()) {
					shippingNotificationModel.setConsignmentCode(consignmentData.getHybrisConsignmentId());
					shippingNotificationModel.setConsignmentEntryNumber(item.getConsignmentEntryNumber());
					shippingNotificationModel.setQuantityConfirmed(item.getQuantityConfirmed());
					shippingNotificationModel.setQuantityRejected(item.getQuantityRejected());
					shippingNotificationModel.setReasonForRejection(item.getReasonForRejection());
					shippingNotificationModel.setQuantityShipped(item.getQuantityShipped());
					shippingNotificationModel.setTrackingNumber(item.getTrackingId());
					shippingNotificationModel.setTrackingURL(item.getTrackingUrl());
					shippingNotificationModel.setSource("NS");
					final String code = String.valueOf(setShippingNotificationUID());
					shippingNotificationModel.setCode(code);
					shippingNotificationModel.setIsCCAcknowledged(false);
					getGpOrderService().saveShippingNotification(shippingNotificationModel);
					getEventService().publishEvent(new GPOrderStatusUpdateEvent(code));
				}
				consignmentData.setStatus(true);
				return consignmentData;

			} else {
				consignmentData.setStatus(false);
				consignmentData.setErrorCode(configuration.getString("gp.error.invalid.consignment.code"));
				consignmentData.setErrorMessage(configuration.getString("gp.error.invalid.consignment.msg"));
			}
		} catch (final Exception e) {
			LOG.error(e.getMessage(),e);
			consignmentData.setStatus(false);
			consignmentData.setErrorCode(configuration.getString("gp.error.invalid.consignment.code"));
			consignmentData.setErrorMessage(configuration.getString("gp.error.invalid.consignment.msg"));
		}

		return consignmentData;
	}

	//YCOM-8336 :To check whether the USER APPROVAL STATUS is Under Review
	@Override
	public boolean isUserInReview()
	{
		CartModel cart = null;
		if(getCartService().hasSessionCart()) {
			cart = getCartService().getSessionCart();
		}
		if(null!=cart) {
        final UserModel user = cart.getUser();
		return (null != user && user instanceof B2BCustomerModel && GPUserApprovalStatusEnum.PENDING.getCode()
				.equalsIgnoreCase(((B2BCustomerModel) user).getUserApprovalStatus().getCode())) ? true : false;
		}else {
			return false;
		}

	}

	public boolean isAddressApproved()
	{
		CartModel cart = null;
		if (getCartService().hasSessionCart()) {
			cart = getCartService().getSessionCart();
		}
		if (null != cart) {
			final UserModel user = cart.getUser();
			return (null != user && user instanceof B2BCustomerModel
					&& GPUserApprovalStatusEnum.APPROVED.getCode()
							.equalsIgnoreCase(cart.getPaymentInfo().getBillingAddress().getApprovalStatus().getCode())
					&& GPUserApprovalStatusEnum.APPROVED.getCode()
							.equalsIgnoreCase(cart.getDeliveryAddress().getApprovalStatus().getCode())) ? true : false;
		} else {
			return false;
		}
	}

	@Override
	public LeaseAgreementData getLeaseAgreementById(final String leaseId) {

		List<GPEndUserLegalTermsModel> legalTerms = getGpOrderService().getLeaseAgreementById(leaseId);
		
		return !legalTerms.isEmpty() ? getGpLeaseDataConverter().convert(legalTerms.get(0)) : null;
		
	}

	@Override
	public void resetCartCookieForASM(final HttpServletRequest request, final HttpServletResponse response, String cartId)
	{
		final Cookie asmCartIdCookie = WebUtils.getCookie(request, ASM_CARTID);
		if(asmCartIdCookie != null)
		{
			asmCartIdCookie.setValue(cartId);
			asmCartIdCookie.setMaxAge(-1);
			asmCartIdCookie.setPath("/");
			response.addCookie(asmCartIdCookie);
		}
	}

	public GPConsignmentTrackingService getGpConsignmentTrackingService() {
		return gpConsignmentTrackingService;
	}

	public void setGpConsignmentTrackingService(final GPConsignmentTrackingService gpConsignmentTrackingService) {
		this.gpConsignmentTrackingService = gpConsignmentTrackingService;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public GpOrderService getGpOrderService() {
		return gpOrderService;
	}


	public void setGpOrderService(final GpOrderService gpOrderService) {
		this.gpOrderService = gpOrderService;
	}

	public CustomerAccountService getCustomerAccountService() {
		return customerAccountService;
	}


	public void setCustomerAccountService(final CustomerAccountService customerAccountService) {
		this.customerAccountService = customerAccountService;
	}

	/**
	 * Sets the shipping notification UID.
	 *
	 * @return the long
	 */
	public long setShippingNotificationUID() {
		final Random rand = new Random();
		final int inc = rand.nextInt(1000000000);
		final long id = Long.parseLong(String.valueOf(System.currentTimeMillis()).substring(1, 10).concat(String.valueOf(inc)));
		return id;
	}

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(final EventService eventService) {
		this.eventService = eventService;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService) {
		this.sessionService = sessionService;
	}

	public Converter<GPEndUserLegalTermsModel, LeaseAgreementData> getGpLeaseDataConverter() {
		return gpLeaseDataConverter;
	}

	public void setGpLeaseDataConverter(Converter<GPEndUserLegalTermsModel, LeaseAgreementData> gpLeaseDataConverter) {
		this.gpLeaseDataConverter = gpLeaseDataConverter;
	}
	
	public Map<String, SplitEntryListWsDTO> getDeliveryGroup(OrderWsDTO order) {
		Map<String, SplitEntryListWsDTO> addressGroupMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(order.getEntries())) {
			for (OrderEntryWsDTO entry : order.getEntries()) {
				if (CollectionUtils.isNotEmpty(entry.getSplitEntries())) {
					for (SplitEntryWsDTO splitEntry : entry.getSplitEntries()) {
						String key = splitEntry.getDeliveryAddress().getId();
						if (null != addressGroupMap.get(key)) {
							SplitEntryListWsDTO entryList = addressGroupMap.get(key);
							splitEntry.setEntryNumber(entry.getEntryNumber().toString());
							splitEntry.setVisible(entry.isVisible());
							entryList.getSplitEntries().add(splitEntry);
						} else {
							SplitEntryListWsDTO splitEntryList = new SplitEntryListWsDTO();
							if(splitEntry.getDeliveryMode()!=null && splitEntry.getDeliveryMode().getCode()!=null) {
							DeliveryModeModel deliveryModeModel = getDeliveryService()
									.getDeliveryModeForCode(splitEntry.getDeliveryMode().getCode());
							splitEntry.getDeliveryMode().setName(deliveryModeModel.getName());
							splitEntryList.setDeliveryMode(splitEntry.getDeliveryMode());
							}
							
							splitEntryList.setDeliveryInstruction(splitEntry.getDeliveryInstruction());
							List<SplitEntryWsDTO> splitEntryDto = new ArrayList<>();
							splitEntry.setEntryNumber(entry.getEntryNumber().toString());
							splitEntry.setVisible(entry.isVisible());
							splitEntryDto.add(splitEntry);
							splitEntryList.setSplitEntries(splitEntryDto);
							addressGroupMap.put(key, splitEntryList);
						}
					}
				}
			}
		}
		return addressGroupMap;
	}

	public DeliveryService getDeliveryService() {
		return deliveryService;
	}


	public void setDeliveryService(DeliveryService deliveryService) {
		this.deliveryService = deliveryService;
	}
}

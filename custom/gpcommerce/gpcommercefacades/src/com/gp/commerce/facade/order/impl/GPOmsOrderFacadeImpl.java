/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facade.order.impl;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.event.OrderCancelledEvent;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordermanagementfacades.cancellation.data.OrderCancelEntryData;
import de.hybris.platform.ordermanagementfacades.cancellation.data.OrderCancelRequestData;
import de.hybris.platform.ordermanagementfacades.order.impl.DefaultOmsOrderFacade;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.localization.Localization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.event.B2BOrderApprovalNotifyEvent;
import com.gp.commerce.core.order.impl.GPOmsOrderService;
import com.gp.commerce.core.stock.services.GPStockService;
import com.gp.commerce.facade.order.GpOmsOrderFacade;
import com.gp.commerce.facades.populators.GPOrderCancelRequestPopulator;
import com.gpintegration.service.impl.GPDefaultCRMShellOrderReplicationService;

public class GPOmsOrderFacadeImpl extends DefaultOmsOrderFacade implements GpOmsOrderFacade
{
	private static final Logger LOG = Logger.getLogger(GPOmsOrderFacadeImpl.class);
	protected static final String ORDER_APPROVAL_EVENT_NAME = "B2BAdminOrderVerified";
	private final String ERROR_CODE="123";
	private final String ERROR_MESSAGE="Order Cancellation Failed";
	private static final String ORDER_CANCEL_LIMIT="order.cancel.retry";

	private GPOmsOrderService gpOMSOrderService;
	private ConfigurationService configurationService;
	private EventService eventService;
	private GPDefaultCRMShellOrderReplicationService gpCRMShellOrderReplicationService;
	private GPStockService stockService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "gpOrderCancelRequestPopulator")
	private GPOrderCancelRequestPopulator gpOrderCancelRequestPopulator;
	
	

	@Override
	public void createRequestOrderCancel(final String orderCode) throws OrderCancelException{
		final OrderModel order = getOrderModelForCode(orderCode);

		boolean status = createGPRequestOrderCancel(prepareOrderCancelRequestData(order),order);
		if(evaluateStatus(status)) {
			throw new OrderCancelException(ERROR_CODE, ERROR_MESSAGE);
		}
		getGpOMSOrderService().updateOrderStatusToCancel(order);

		//Update stock when order is cancelled successfully. Here order snapshot is used to fetch the original entry quantity
		getStockService().releaseStockForOrder(getSnapshotOrderForCode(orderCode));

		//triggering order cancellation email
		final OrderProcessModel orderProcess = getGpOMSOrderService().updateOrderProcess(order);
		getEventService().publishEvent(new OrderCancelledEvent(orderProcess));
	}

	protected OrderModel getSnapshotOrderForCode(final String orderCode)
	{
		final Map<String, String> params = new HashMap<>();
		params.put(OrderModel.CODE, orderCode);
		final List<OrderModel> resultSet = getOrderSnapshots(gpOMSOrderService.getOrderSnapshots(params));

		if (resultSet.isEmpty())
		{
			throw new ModelNotFoundException(
					String.format(Localization.getLocalizedString("ordermanagementfacade.orders.validation.missing.code"), orderCode));
		}
		return resultSet.get(0);
	}

	/**
	 * Get snapshots of orders. i.e: orders with versionID
	 *
	 * @param orders
	 * 		a list of orders
	 * @return a list of orders snapshots
	 */
	protected List<OrderModel> getOrderSnapshots(final List<OrderModel> orders)
	{
		if (CollectionUtils.isEmpty(orders))
		{
			return new ArrayList<>();
		}
		return orders.stream().filter(order -> Objects.nonNull(order.getVersionID())).collect(Collectors.toList());
	}

	/**
	 * Prepare order cancel request data.
	 *
	 * @param order the order
	 * @return the order cancel request data
	 */
	private OrderCancelRequestData prepareOrderCancelRequestData(final OrderModel order) {
		final OrderCancelRequestData result = new OrderCancelRequestData();
		gpOrderCancelRequestPopulator.populate(order, result);
		return result;
	}

	private boolean createGPRequestOrderCancel(final OrderCancelRequestData orderCancelRequestData,final OrderModel order)
	{

		validateReturnRequestData(orderCancelRequestData);
		boolean isSuccess = true;
		final UserModel user = getUserService().getUserForUID(orderCancelRequestData.getUserId());
		final int orderCancelLimit = getConfigurationService().getConfiguration().getInt(ORDER_CANCEL_LIMIT, 1);
		for(int n=0; n < orderCancelLimit; n++)
		{
			LOG.info("Order cancel limit property is configured to :"+orderCancelLimit+" times,trying "+ n +"th time to cancel the order");
			try {
				getOrderCancelService().requestOrderCancel(buildOrderCancelRequest(orderCancelRequestData, order), user);
				isSuccess = true;
				LOG.info("Order Cancelled successfully for order no:" +order.getCode());
				break;
			} catch (final OrderCancelException e) // NOSONAR
			{
				isSuccess = false;
					LOG.error("FULFILLMENT_ERROR : Order Cancellation Failed'" + order.getCode() + "'",e);
			}
		}
		if(evaluateStatus(isSuccess))
		{
			LOG.info("Order processing status has been updated to :" +OrderStatus.FULFILLMENT_ERROR + " as order cancellation failed for order :" +order.getCode());
			order.setProcessingStatus(OrderStatus.FULFILLMENT_ERROR);
			getGpOMSOrderService().updateOrder(order);
		}
		return isSuccess;
	}

	private boolean evaluateStatus(boolean status) {
		return Boolean.FALSE.equals(status);
	}

	@Override
	public OrderData approveOrRejectOrder(final String orderCode, final String orderStatus, final String approverComments)
	{
		final OrderModel order = getOrderModelForCode(orderCode);

		LOG.info("Updating Order Status");
		if (OrderStatus.CREATED.getCode().equalsIgnoreCase(orderStatus))
		{
			order.setStatus(OrderStatus.CREATED);
		}
		else
		{
			order.setStatus(OrderStatus.REJECTED);
		}

		if (StringUtils.isNotBlank(approverComments))
		{
			order.setApproverComments(approverComments);
		}
		getGpOMSOrderService().updateOrder(order);
		
		modelService.refresh(order);
		eventService.publishEvent(new B2BOrderApprovalNotifyEvent(order));

		order.getOrderProcess().stream()
				.filter(process -> process.getCode().startsWith(order.getStore().getSubmitOrderProcessCode()))
				.forEach(filteredProcess -> getBusinessProcessService()
						.triggerEvent(filteredProcess.getCode() + "_" + ORDER_APPROVAL_EVENT_NAME));
		
		LOG.debug(" CRM Shell order triggering update on status update");
		gpCRMShellOrderReplicationService.updateCRMShellOrder(order);
		return getOrderConverter().convert(order);
	}


	public GPOmsOrderService getGpOMSOrderService() {
		return gpOMSOrderService;
	}

	@Required
	public void setGpOMSOrderService(final GPOmsOrderService gpOMSOrderService) {
		this.gpOMSOrderService = gpOMSOrderService;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public EventService getEventService() {
		return eventService;
	}

	@Required
	public void setEventService(final EventService eventService) {
		this.eventService = eventService;
	}

	/**
	 * @return the gpCRMShellOrderReplicationService
	 */
	public GPDefaultCRMShellOrderReplicationService getGpCRMShellOrderReplicationService() {
		return gpCRMShellOrderReplicationService;
	}

	/**
	 * @param gpCRMShellOrderReplicationService the gpCRMShellOrderReplicationService to set
	 */
	public void setGpCRMShellOrderReplicationService(
			final GPDefaultCRMShellOrderReplicationService gpCRMShellOrderReplicationService) {
		this.gpCRMShellOrderReplicationService = gpCRMShellOrderReplicationService;
	}

	protected GPStockService getStockService() {
		return stockService;
	}

	public void setStockService(final GPStockService stockService) {
		this.stockService = stockService;
	}
}

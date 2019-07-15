/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.order.impl;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.commerceservices.event.OrderCancelledEvent;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.OrderCancelService;
import de.hybris.platform.ordermanagementfacades.cancellation.data.OrderCancelEntryData;
import de.hybris.platform.ordermanagementfacades.cancellation.data.OrderCancelRequestData;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.store.BaseStoreModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.GPConsignmentEntryStatus;

public class GPOmsOrderServiceImpl implements GPOmsOrderService{
	private static final String SEND_ORDER_CANCELLED_PROCESS = "sendOrderCancelledProcess";
	private static final String SEND_ORDER_PARTIAL_CANCELLED_PROCESS = "sendOrderPartialCancelledProcess";
	private static final Logger LOG = Logger.getLogger(GPOmsOrderServiceImpl.class);
	private static final String ORDER_CANCEL_LIMIT = "order.cancel.retry";
	private ModelService modelService;
	private OrderCancelService orderCancelService;
	private UserService userService;
	private ConfigurationService configurationService;
	private StockService stockService;
	private EnumerationService enumerationService;
	private EventService eventService;
	private GenericDao<OrderModel> orderGenericDao;


	/* (non-Javadoc)
	 * @see com.gp.commerce.core.order.impl.GPOmsOrderService#updateOrderStatusToCancel(de.hybris.platform.core.model.order.OrderModel)
	 */
	@Override
	public void updateOrderStatusToCancel(final OrderModel order) {
		order.setStatus(OrderStatus.CANCELLED);
		updateEntryStatus(order, OrderEntryStatus.DEAD);
		updateConsignmentStatusToCancel(order);
		getModelService().save(order);

	}

	/* (non-Javadoc)
	 * @see com.gp.commerce.core.order.impl.GPOmsOrderService#updateConsignmentStatusToCancel(de.hybris.platform.core.model.order.OrderModel)
	 */
	public void updateConsignmentStatusToCancel(final OrderModel order) {
		for(final ConsignmentModel consignment:order.getConsignments()) {
			consignment.setStatus(ConsignmentStatus.CANCELLED);
			for(final ConsignmentEntryModel consignmentEntryModel:consignment.getConsignmentEntries()) {
				consignmentEntryModel.setConsignmentEntryStatus(GPConsignmentEntryStatus.CANCELLED);
			}
		}
		getModelService().saveAll();
	}

	/* (non-Javadoc)
	 * @see com.gp.commerce.core.order.impl.GPOmsOrderService#partialCancelConsignment(java.lang.Long, de.hybris.platform.ordersplitting.model.ConsignmentModel, de.hybris.platform.ordersplitting.model.ConsignmentEntryModel)
	 */
	public void partialCancelConsignment(final Long qtyDeclined, final ConsignmentModel consignmentModel, final ConsignmentEntryModel entryModel, final String reason) {
		final OrderCancelRequestData result = prepareOrderCancelRequestData(consignmentModel.getOrder(), qtyDeclined, entryModel, reason);
		boolean isSuccess = false;
		final int orderCancelLimit = getConfigurationService().getConfiguration().getInt(ORDER_CANCEL_LIMIT, 1);
		for(int n=0; n < orderCancelLimit; n++)
		{
			LOG.info("Order cancel limit property is configured to: " + orderCancelLimit + " times,trying "+ n +" time to cancel the order");
			try
			{
				final UserModel user = getUserService().getUserForUID(GpcommerceCoreConstants.ADMIN);
				getOrderCancelService().requestOrderCancel(buildOrderCancelRequest(result,(OrderModel)consignmentModel.getOrder()),user);
				isSuccess = true;
				LOG.info("Order: " + consignmentModel.getOrder().getCode() + " has been Partially Cancelled." );

				// If Partial order is cancelled successfully then update Stock
				getStockService().release(entryModel.getOrderEntry().getProduct(), getDefaultWarehouseForStore(consignmentModel.getOrder().getStore()), qtyDeclined.intValue(), reason);

				break;
			}
			catch (final OrderCancelException e)
			{
				isSuccess = false;
				LOG.error("FULFILLMENT_ERROR : Partial Cancellation Failed for order '" + consignmentModel.getOrder().getCode() + "'",e);
			}catch (final Exception e)
			{
				isSuccess = false;
				LOG.error("FULFILLMENT_ERROR : Error in Partial Cancel Consignment for order '" + consignmentModel.getOrder().getCode() + "'",e);
			}
		}
		if(!isSuccess)
		{
			updateProcessingStatus(entryModel, GPConsignmentEntryStatus.FULFILLMENT_ERROR);
		}
		else
		{
			//Compute Total Cancelled Quantity
			Long totalDeclinedQty = qtyDeclined;
			if(entryModel.getDeclinedQty() != null && entryModel.getDeclinedQty() > 0)
			{
				totalDeclinedQty = totalDeclinedQty + entryModel.getDeclinedQty();
			}
			entryModel.setDeclinedQty(totalDeclinedQty);
			if(entryModel.getQuantity().longValue() == totalDeclinedQty.longValue())
			{
				entryModel.setConsignmentEntryStatus(GPConsignmentEntryStatus.CANCELLED);
			}
			getModelService().save(entryModel);

			updateOrderAndConsignmentStatus((OrderModel)consignmentModel.getOrder(), consignmentModel);

			final OrderProcessModel orderProcess = getModelService().create(OrderProcessModel.class);
			orderProcess.setCode(SEND_ORDER_PARTIAL_CANCELLED_PROCESS + consignmentModel.getOrder().getCode() + System.currentTimeMillis());
			orderProcess.setProcessDefinitionName(SEND_ORDER_PARTIAL_CANCELLED_PROCESS);
			orderProcess.setOrder((OrderModel)consignmentModel.getOrder());
			orderProcess.setConsignmentEntry(entryModel);
			orderProcess.setDeclinedQty(qtyDeclined.intValue());
			getModelService().save(orderProcess);
			getEventService().publishEvent(new OrderCancelledEvent(orderProcess));
		}
	}


	private WarehouseModel getDefaultWarehouseForStore(final BaseStoreModel store) {
		return store.getWarehouses().stream().filter(warehouse -> warehouse.getDefault()).findFirst()
				.orElse(store.getWarehouses().get(0));
	}

	/* (non-Javadoc)
	 * @see com.gp.commerce.core.order.impl.GPOmsOrderService#updateConsignEntryProcessingStatus(de.hybris.platform.ordersplitting.model.ConsignmentEntryModel, de.hybris.platform.sap.sapmodel.enums.ConsignmentEntryStatus)
	 */
	@Override
	public void updateProcessingStatus(final ConsignmentEntryModel consignEntry, final GPConsignmentEntryStatus entryStatus)
	{
		consignEntry.setProcessingStatus(entryStatus);

		ConsignmentStatus consignmentStatus = ConsignmentStatus.FULFILLMENT_ERROR;
		OrderStatus orderStatus = OrderStatus.FULFILLMENT_ERROR;

		if(entryStatus.equals(GPConsignmentEntryStatus.PAYMENT_ERROR)) {
			consignmentStatus = ConsignmentStatus.PAYMENT_ERROR;
			orderStatus = OrderStatus.PAYMENT_ERROR;
		}

		final ConsignmentModel consignment = consignEntry.getConsignment();
		consignment.setProcessingStatus(consignmentStatus);

		final AbstractOrderModel order = consignment.getOrder();
		order.setProcessingStatus(orderStatus);
		LOG.info(entryStatus.getCode() + " | Updating processing status to " + entryStatus.getCode() + " for Order :" + order.getCode());

		modelService.saveAll(consignEntry, consignment, order);
	}


	/**
	 * prepares order cancel request data by the given consignment entry and quantity declined
	 *
	 * @param order
	 *
	 * @param qtyDeclined
	 *
	 * @param entryModel
	 *
	 * @return OrderCancelRequestData
	 */
	private OrderCancelRequestData prepareOrderCancelRequestData(final AbstractOrderModel order,final Long qtyDeclined, final ConsignmentEntryModel entryModel, final String reason) {
		final OrderCancelRequestData result = new OrderCancelRequestData();
		result.setOrderCode(order.getCode());
		result.setEntries(prepareOrderCancelEntryData(order, qtyDeclined, entryModel, reason));
		result.setUserId(GpcommerceCoreConstants.ADMIN);
		return result;
	}

	/**
	 *  Prepares order cancel entry request by the given consignment entry and quantity declined
	 *
	 * @param orderCancelRequestData
	 *
	 * @param order
	 *
	 * @return OrderCancelRequest
	 */
	protected OrderCancelRequest buildOrderCancelRequest(final OrderCancelRequestData orderCancelRequestData,
			final OrderModel order)
	{
		final List<OrderCancelEntry> orderCancelEntries = new ArrayList<>();
		orderCancelRequestData.getEntries().stream().forEach(entryData -> {
			final AbstractOrderEntryModel orderEntry = getEntryForNumber(order, entryData.getOrderEntryNumber());

			final OrderCancelEntry cancellationEntry = new OrderCancelEntry(orderEntry, entryData.getCancelQuantity(),
					entryData.getNotes());
			orderCancelEntries.add(cancellationEntry);
		});

		return new OrderCancelRequest(order, orderCancelEntries);
	}

	/**
	 * Retrieves order entry by the entry number
	 *
	 * @param order
	 *
	 * @param orderEntryNumber
	 *
	 * @return
	 */
	private AbstractOrderEntryModel getEntryForNumber(final OrderModel order, final Integer orderEntryNumber) {
		for(final AbstractOrderEntryModel entry : order.getEntries()) {
			if(orderEntryNumber.equals(entry.getEntryNumber())) {
				return entry;
			}
		}
		return null;
	}


	/**
	 * Prepares order cancel entry data
	 *
	 * @param order
	 *
	 * @param qtyDeclined
	 *
	 * @param entryModel
	 *
	 * @return List<OrderCancelEntryData>
	 */
	protected List<OrderCancelEntryData> prepareOrderCancelEntryData(final AbstractOrderModel order,final Long qtyDeclined, final ConsignmentEntryModel entryModel, final String reason)
	{
		final Map<Integer, Integer> cancelEntryQuantityMap = new HashMap<>();
		cancelEntryQuantityMap.put(entryModel.getOrderEntry().getEntryNumber(), qtyDeclined.intValue());

		final CancelReason cancelReasonEnum = StringUtils.isNotBlank(reason) ? getEnumerationService().getEnumerationValue(CancelReason.class, reason) : CancelReason.WAREHOUSE;
		final List<OrderCancelEntryData> result = new ArrayList<>();

		cancelEntryQuantityMap.forEach((entryNum, cancelQty) -> {
			final OrderCancelEntryData orderCancelEntryData = new OrderCancelEntryData();
			orderCancelEntryData.setOrderEntryNumber(entryNum);
			orderCancelEntryData.setCancelQuantity(Long.valueOf(cancelQty));
			orderCancelEntryData.setCancelReason(cancelReasonEnum);
			result.add(orderCancelEntryData);
		});
		return result;
	}

	/* (non-Javadoc)
	 * @see com.gp.commerce.core.order.impl.GPOmsOrderService#updateEntryStatus(de.hybris.platform.core.model.order.OrderModel, de.hybris.platform.basecommerce.enums.OrderEntryStatus)
	 */
	@Override
	public void updateEntryStatus(final OrderModel order,final OrderEntryStatus status) {
		for(final AbstractOrderEntryModel entry : order.getEntries()) {
			entry.setQuantityStatus(status);
		}
		getModelService().saveAll();
	}

	@Override
	public void updateConsignmentEntryStatus(final ConsignmentEntryModel consignEntry, final GPConsignmentEntryStatus entryStatus) {
		consignEntry.setConsignmentEntryStatus(entryStatus);

		if(entryStatus.equals(GPConsignmentEntryStatus.PAYMENT_ERROR)) {
			final ConsignmentModel consignment = consignEntry.getConsignment();
			consignment.setStatus(ConsignmentStatus.PAYMENT_ERROR);
			final AbstractOrderModel order = consignment.getOrder();
			order.setStatus(OrderStatus.PAYMENT_ERROR);
			modelService.saveAll(consignment, order);
		}

		modelService.save(consignEntry);
	}

	/* (non-Javadoc)
	 * @see com.gp.commerce.core.order.impl.GPOmsOrderService#updateOrderAndConsignmentStatus(de.hybris.platform.core.model.order.OrderModel, de.hybris.platform.ordersplitting.model.ConsignmentModel)
	 */
	@Override
	public void updateOrderAndConsignmentStatus(final OrderModel order, final ConsignmentModel consignment) {
		LOG.info("Inside GPOmsOrderServiceImpl : updateOrderAndConsignmentStatus");
		final int entryCount = consignment.getConsignmentEntries().size();
		int shippedEntryCount = 0;
		int partiallyShippedEntryCount = 0;
		int cancelledEntryCount = 0;

		for (final ConsignmentEntryModel consignmentEntry : consignment.getConsignmentEntries()) {
			if(consignmentEntry.getConsignmentEntryStatus().equals(GPConsignmentEntryStatus.SHIPPED))
			{
				shippedEntryCount++;
			}
			else if(consignmentEntry.getConsignmentEntryStatus().equals(GPConsignmentEntryStatus.PARTIALLY_SHIPPED))
			{
				partiallyShippedEntryCount++;
			}
			else if(consignmentEntry.getConsignmentEntryStatus().equals(GPConsignmentEntryStatus.CANCELLED))
			{
				cancelledEntryCount++;
			}
		}

		if(entryCount == cancelledEntryCount)
		{
			consignment.setStatus(ConsignmentStatus.CANCELLED);
		}
		else if((entryCount == shippedEntryCount) || (entryCount == (shippedEntryCount + cancelledEntryCount)))
		{
			consignment.setStatus(ConsignmentStatus.SHIPPED);
		}
		else if((shippedEntryCount > 0) || (partiallyShippedEntryCount > 0))
		{
			consignment.setStatus(ConsignmentStatus.PARTIALLY_SHIPPED);
		}
		modelService.save(consignment);
		updateOrderStatus(order);
		modelService.refresh(order);
	}
	
	@Override
	public void updateOrderByStatus(final OrderModel order, OrderStatus orderstatus) {
		order.setStatus(orderstatus);
		modelService.save(order);
	}
	

	/* (non-Javadoc)
	 * @see com.gp.commerce.core.order.impl.GPOmsOrderService#updateOrderStatus(de.hybris.platform.core.model.order.OrderModel)
	 */
	@Override
	public void updateOrderStatus(final OrderModel order) {
		final int consignmentsCount = order.getConsignments().size();
		int shippedCount = 0;
		int partiallyShippedCount = 0;
		int paymentError = 0;
		int cancelledCount = 0;

		for (final ConsignmentModel consignment : order.getConsignments()) {
			if(consignment.getStatus().equals(ConsignmentStatus.SHIPPED))
			{
				shippedCount++;
			}
			else if(consignment.getStatus().equals(ConsignmentStatus.PARTIALLY_SHIPPED))
			{
				partiallyShippedCount++;
			}
			else if(consignment.getStatus().equals(ConsignmentStatus.PAYMENT_ERROR))
			{
				paymentError++;
			}
			else if(consignment.getStatus().equals(ConsignmentStatus.CANCELLED))
			{
				cancelledCount++;
			}
		}

		final int totalShippedCancelled = shippedCount + cancelledCount;
		if(consignmentsCount == cancelledCount)
		{
			order.setStatus(OrderStatus.CANCELLED);
		}
		else if((consignmentsCount == shippedCount) || (consignmentsCount == totalShippedCancelled))
		{
			order.setStatus(OrderStatus.SHIPPED);
		}
		else if(consignmentsCount == (totalShippedCancelled + paymentError))
		{
			order.setStatus(OrderStatus.CANCELLED);
		}
		else if((shippedCount > 0) || (partiallyShippedCount > 0))
		{
			order.setStatus(OrderStatus.PARTIALLY_SHIPPED);
		}
		modelService.save(order);
	}
	
	public OrderCancelService getOrderCancelService() {
		return orderCancelService;
	}

	public void setOrderCancelService(final OrderCancelService orderCancelService) {
		this.orderCancelService = orderCancelService;
	}
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(final UserService userService) {
		this.userService = userService;
	}


	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public StockService getStockService() {
		return stockService;
	}

	@Required
	public void setStockService(final StockService stockService) {
		this.stockService = stockService;
	}

	public EnumerationService getEnumerationService() {
		return enumerationService;
	}

	@Required
	public void setEnumerationService(final EnumerationService enumerationService) {
		this.enumerationService = enumerationService;
	}

	public EventService getEventService() {
		return eventService;
	}

	@Required
	public void setEventService(final EventService eventService) {
		this.eventService = eventService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService) {
		this.modelService = modelService;
	}

	@Override
	public OrderProcessModel updateOrderProcess(final OrderModel order)
	{
		final OrderProcessModel orderProcess = getModelService().create(OrderProcessModel.class);
		orderProcess.setCode(SEND_ORDER_CANCELLED_PROCESS + order.getCode() + System.currentTimeMillis());
		orderProcess.setProcessDefinitionName(SEND_ORDER_CANCELLED_PROCESS);
		orderProcess.setOrder(order);
		getModelService().save(orderProcess);
		return orderProcess;
	}

	@Override
	public void updateOrder(final OrderModel order)
	{
		if (null != order)
		{
			getModelService().save(order);
		}
	}

	@Override
	public List<OrderModel> getOrderSnapshots(Map<String, String> params) {
		return orderGenericDao.find(params);
	}

	@Required
	public void setOrderGenericDao(GenericDao<OrderModel> orderGenericDao) {
		this.orderGenericDao = orderGenericDao;
	}
}

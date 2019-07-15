/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.order.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.enums.GPNetsuiteOrderExportStatus;
import com.gp.commerce.core.model.GPEndUserLegalTermsModel;
import com.gp.commerce.core.model.ShippingNotificationModel;
import com.gp.commerce.core.order.dao.GpOrderDao;
import com.gp.commerce.core.order.service.GpOrderService;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;

public class GPOrderServiceImpl implements GpOrderService{

	private GpOrderDao gpOrderDao;

	@Resource(name = "modelService")
	private ModelService modelService;

	/* (non-Javadoc)
	 * @see com.gp.commerce.core.order.service.GpOrderService#getFailedOrders(de.hybris.platform.cms2.model.site.CMSSiteModel)
	 */
	@Override
	public List<OrderModel> getFailedOrders(final CMSSiteModel site, final OrderStatus orderProcessingStatus) {
		return gpOrderDao.getOrdersInError(site,orderProcessingStatus);

	}
	
	/* (non-Javadoc)
	 * @see com.gp.commerce.core.order.service.GpOrderService#getOrdersByStatus(de.hybris.platform.core.enums.OrderStatus)
	 */
	@Override
	public List<OrderModel> getOrdersByStatus(OrderStatus orderProcessingStatus){
		return gpOrderDao.getOrdersByStatus(orderProcessingStatus);
	}

	/* (non-Javadoc)
	 * @see com.gp.commerce.core.order.service.GpOrderService#getFailedConsignments(de.hybris.platform.cms2.model.site.CMSSiteModel, de.hybris.platform.basecommerce.enums.ConsignmentStatus)
	 */
	@Override
	public List<ConsignmentModel> getFailedConsignments(final CMSSiteModel site, final ConsignmentStatus consignProcessingStatus) {
		return gpOrderDao.getConsignmentsInError(site,consignProcessingStatus);

	}

	/* (non-Javadoc)
	 * @see com.gp.commerce.core.order.service.GpOrderService#getLeaseAgreementById(java.lang.String)
	 */
	@Override
	public List<GPEndUserLegalTermsModel> getLeaseAgreementById(final String leaseId) {

		return gpOrderDao.getLeaseAgreementById(leaseId);
	}

	public GpOrderDao getGpOrderDao() {
		return gpOrderDao;
	}

	@Required
	public void setGpOrderDao(final GpOrderDao gpOrderDao) {
		this.gpOrderDao = gpOrderDao;
	}

	@Override
	public void updateOrderConsignment(final ConsignmentModel consignment)
	{
		if (null != consignment)
		{
			modelService.save(consignment);
		}
	}

	@Override
	public void saveShippingNotification(final ShippingNotificationModel model)
	{
		if (null != model)
		{
			modelService.save(model);
		}
	}

	@Override
	public List<OrderModel> getFailedOrdersOfNetSuiteAndPayment(CMSSiteModel site) {
		return gpOrderDao.getOrdersInErrorOfNetsuiteAndPayment(site);
	}

}

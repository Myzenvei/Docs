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
package com.gp.commerce.core.event;

import de.hybris.platform.acceleratorservices.site.AbstractAcceleratorSiteEventListener;
import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.orderprocessing.events.OrderPlacedEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.util.GPSiteConfigUtils;


/**
 * Listener for order confirmation events.
 */
public class OrderConfirmationEventListener extends GPAbstractAcceleratorSiteEventListener<OrderPlacedEvent>
{

	private static final String GP_ORDER_APPROVAL_EMAIL_PROCESS = "gpOrderApprovalEmailProcess";
	private static final String GP_ORDER_APPROVAL_EMAIL_PROCESS_LABEL = "gpOrderApprovalEmailProcess-";
	private static final String ORDER_CONFIRMATION_EMAIL_PROCESS = "orderConfirmationEmailProcess";
	private static final String ORDER_CONFIRMATION_EMAIL_PROCESS_LABEL = "orderConfirmationEmailProcess-";
	private static final String SUBSCRIPTION_ORDER_CONFIRMATION_EMAIL_PROCESS = "subscriptionOrderConfirmationEmailProcess";
	private static final String SUBSCRIPTION_ORDER_CONFIRMATION_EMAIL_PROCESS_LABEL = "subscriptionOrderConfirmationEmailProcess-";
	
	private static final String GP_INSTALLATION_PRODUCT_EMAIL_PROCESS = "installationProductEmailProcess";
	private static final String GP_INSTALLATION_PRODUCT_EMAIL_PROCESS_LABEL = "installationProductEmailProcess-";
	
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
	
	public B2BUnitService<B2BUnitModel, B2BCustomerModel> getB2bUnitService()
	{
		return b2bUnitService;
	}

	public void setB2bUnitService(final B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}


	@Override
	protected void onSiteEvent(final OrderPlacedEvent orderPlacedEvent)
	{
		final OrderModel orderModel = orderPlacedEvent.getProcess().getOrder();
		
		final OrderProcessModel orderProcessModel ;
		if(null != orderModel.getIsSubscription() && orderModel.getIsSubscription())
		{
			orderProcessModel = (OrderProcessModel) getBusinessProcessService().createProcess(
					 SUBSCRIPTION_ORDER_CONFIRMATION_EMAIL_PROCESS_LABEL + orderModel.getCode() + "-" + System.currentTimeMillis(),
					SUBSCRIPTION_ORDER_CONFIRMATION_EMAIL_PROCESS);
		}
		else
		{
			 orderProcessModel = (OrderProcessModel) getBusinessProcessService().createProcess(
					ORDER_CONFIRMATION_EMAIL_PROCESS_LABEL + orderModel.getCode() + "-" + System.currentTimeMillis(),
					ORDER_CONFIRMATION_EMAIL_PROCESS);
		}
		
		orderProcessModel.setOrder(orderModel);
		getModelService().save(orderProcessModel);
		getBusinessProcessService().startProcess(orderProcessModel);
		
		if(null != orderModel.getScheduleInstallation() && "b2bwhitelabel".equalsIgnoreCase(orderModel.getSite().getUid())) {
		    final OrderProcessModel installationProductProcessModel = (OrderProcessModel) getBusinessProcessService().createProcess(
		    		GP_INSTALLATION_PRODUCT_EMAIL_PROCESS_LABEL + orderModel.getCode() + "-" + System.currentTimeMillis(),
		    		GP_INSTALLATION_PRODUCT_EMAIL_PROCESS);
		    installationProductProcessModel.setOrder(orderModel);
			getModelService().save(installationProductProcessModel);
			getBusinessProcessService().startProcess(installationProductProcessModel);
		}
    
		//condition for Violation/Threshold
		if (orderModel.getStatus().equals(OrderStatus.PENDING_APPROVAL)
				&& GPSiteConfigUtils.isB2BSite(orderModel.getSite()))
		{
			final OrderProcessModel orderApprovalModel = (OrderProcessModel) getBusinessProcessService().createProcess(
					GP_ORDER_APPROVAL_EMAIL_PROCESS_LABEL + orderModel.getCode() + "-" + System.currentTimeMillis(),
					GP_ORDER_APPROVAL_EMAIL_PROCESS);
			orderApprovalModel.setOrder(orderModel);

			final UserModel user = orderModel.getUser();
			if (user instanceof B2BCustomerModel)
			{
				final Collection<B2BCustomerModel> administrators = getB2bUnitService()
						.getUsersOfUserGroup(((B2BCustomerModel) user).getDefaultB2BUnit(), B2BConstants.B2BADMINGROUP, false);
				B2BCustomerModel adminDetails = new B2BCustomerModel();
				if (CollectionUtils.isNotEmpty(administrators))
				{
					final List<B2BCustomerModel> list = new ArrayList<>(administrators);
					if(!list.isEmpty()){
						adminDetails = list.get(0);
					}
					orderApprovalModel.setAdminDetails(adminDetails);
				}
			}

			getModelService().save(orderApprovalModel);
			getBusinessProcessService().startProcess(orderApprovalModel);
		}
	}

	@Override
	protected SiteChannel getSiteChannelForEvent(final OrderPlacedEvent event)
	{
		final OrderModel order = event.getProcess().getOrder();
		ServicesUtil.validateParameterNotNullStandardMessage(GpcommerceCoreConstants.EVENT_ORDER, order);
		final BaseSiteModel site = order.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage(GpcommerceCoreConstants.EVENT_ORDER_SITE, site);
		return site.getChannel();
	}
}
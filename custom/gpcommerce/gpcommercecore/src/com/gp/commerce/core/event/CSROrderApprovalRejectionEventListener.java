/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.event;
import javax.annotation.Resource;

//com.gp.commerce.core.event.CSROrderApprovalRejectionEventListener
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.acceleratorservices.site.strategies.SiteChannelValidationStrategy;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

public class CSROrderApprovalRejectionEventListener extends AbstractEventListener<CSROrderApprovalRejectionEvent>{
	
	private ModelService modelService;
	private BusinessProcessService businessProcessService;
	
	
	private SiteChannelValidationStrategy siteChannelValidationStrategy;

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	protected void onEvent(final CSROrderApprovalRejectionEvent event)
	{
		final OrderModel orderModel = event.getProcess().getOrder();
		final OrderProcessModel orderProcessModel = (OrderProcessModel) getBusinessProcessService().createProcess(
				"csrOrderApprovalRejectionEmailProcess" + "-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
				"csrOrderApprovalRejectionEmailProcess");
		orderProcessModel.setOrder(orderModel);
		orderProcessModel.setFraudType(event.getFraudType());
		getModelService().save(orderProcessModel);
		getBusinessProcessService().startProcess(orderProcessModel);
	}
	
	public SiteChannelValidationStrategy getSiteChannelValidationStrategy() {
		return siteChannelValidationStrategy;
	}

	public void setSiteChannelValidationStrategy(SiteChannelValidationStrategy siteChannelValidationStrategy) {
		this.siteChannelValidationStrategy = siteChannelValidationStrategy;
	}

	


}

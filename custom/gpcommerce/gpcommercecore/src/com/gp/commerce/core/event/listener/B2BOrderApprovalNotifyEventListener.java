/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.event.listener;
//com.gp.commerce.core.event.listener.B2BOrderApprovalNotifyEventListener

import com.gp.commerce.core.event.B2BOrderApprovalNotifyEvent;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

public class B2BOrderApprovalNotifyEventListener extends AbstractEventListener<B2BOrderApprovalNotifyEvent> {

	private ModelService modelService;
	
	private BusinessProcessService businessProcessService;
	
	@Override
	protected void onEvent(B2BOrderApprovalNotifyEvent event) {
		final OrderModel orderModel = event.getOrder();
		final OrderProcessModel orderProcessModel = (OrderProcessModel) getBusinessProcessService().createProcess(
				"b2bOrderApprovalNotifyEmailProcess" + "-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
				"b2bOrderApprovalNotifyEmailProcess");
		orderProcessModel.setOrder(orderModel);
		getModelService().save(orderProcessModel);
		getBusinessProcessService().startProcess(orderProcessModel);
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	public BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}

	public void setBusinessProcessService(BusinessProcessService businessProcessService) {
		this.businessProcessService = businessProcessService;
	}
	

}

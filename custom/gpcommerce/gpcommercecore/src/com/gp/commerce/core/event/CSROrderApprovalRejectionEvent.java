/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.event;

import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public class CSROrderApprovalRejectionEvent extends AbstractEvent{
	
	private OrderProcessModel process;
	private String fraudType;

	public CSROrderApprovalRejectionEvent(final OrderProcessModel process,String fraudType)
	{
		this.process = process;
		this.fraudType = fraudType;
		
	}

	public OrderProcessModel getProcess()
	{
		return process;
	}

	public String getFraudType() {
		return fraudType;
	}

}

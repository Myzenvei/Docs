/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.event;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public class B2BOrderApprovalNotifyEvent extends AbstractEvent{
	
	private OrderModel order;

	public B2BOrderApprovalNotifyEvent(final OrderModel order)
	{
		this.order = order;
	}

	public OrderModel getOrder() {
		return order;
	}

}

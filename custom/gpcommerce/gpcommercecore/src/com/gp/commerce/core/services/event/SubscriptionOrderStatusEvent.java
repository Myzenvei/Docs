/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.event;

import com.gp.commerce.core.model.SubscriptionCartProcessModel;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;


/**
 * Listener for "reset password" functionality event.
 */

public class SubscriptionOrderStatusEvent extends AbstractEvent
{
	private static final long serialVersionUID = -293422455711438189L;
	private SubscriptionCartProcessModel process;
	
	public SubscriptionCartProcessModel getProcess() {
		return process;
	}
	
	public void setProcess(SubscriptionCartProcessModel process) {
		this.process = process;
	}
	
	/**
	 * Default constructor
	 * @param process 
	 * 
	 */
	public SubscriptionOrderStatusEvent(SubscriptionCartProcessModel process)
	{
		super(process);
	}
	
}
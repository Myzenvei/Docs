/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.event;

import com.gp.commerce.core.model.CartProcessModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


/**
 * Listener for "reset password" functionality event.
 */

public class ShareCartEvent extends AbstractEvent
{
	private static final long serialVersionUID = -293422455711438189L;
	private String toEmail;
	private String senderEmail;
	private String senderName;
	private CartProcessModel process;
	
	

	public CartProcessModel getProcess() {
		return process;
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
	

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	/**
	 * Default constructor
	 * @param process 
	 * 
	 */
	public ShareCartEvent(CartProcessModel process)
	{
		super(process);
	}

}
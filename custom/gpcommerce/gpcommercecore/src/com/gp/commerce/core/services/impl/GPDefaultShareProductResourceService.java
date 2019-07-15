/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
 
package com.gp.commerce.core.services.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.forms.ShareProductResourceForm;
import com.gp.commerce.core.services.GPShareProductResourceService;
import com.gp.commerce.core.util.ShareProductResourceEvent;

import de.hybris.platform.servicelayer.event.EventService;

/**
 * Share Product Email Process Service Implementation
 * 
 */
public class GPDefaultShareProductResourceService implements GPShareProductResourceService {
	private static final Logger LOG = Logger.getLogger(GPDefaultShareProductResourceService.class);
	private EventService eventService;

	public EventService getEventService() {
		return eventService;
	}

	@Required
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	/**
	 * This method sets value in the event from the input form values and publishes
	 * event
	 * 
	 * @param form
	 */
	@Override
	public void shareProductResourceService(ShareProductResourceForm form) {
		LOG.info("Method: shareProduct " + form);
		ShareProductResourceEvent shareProdResourceEvent = new ShareProductResourceEvent();
		shareProdResourceEvent.setRecipientEmails(form.getRecipientEmails());
		shareProdResourceEvent.setSenderEmail(form.getSenderEmail());
		shareProdResourceEvent.setSenderName(form.getSenderName());
		shareProdResourceEvent.setSenderMessage(form.getSenderMessage());
		shareProdResourceEvent.setResourceTitle(form.getResourceTitle());
		shareProdResourceEvent.setResourceDescription(form.getResourceDescription());
		shareProdResourceEvent.setImgurl(form.getImgurl());
		shareProdResourceEvent.setResourcePageUrl(form.getResourcePageUrl());
		shareProdResourceEvent.setEmbeddedLink(form.getEmbeddedLink());
		shareProdResourceEvent.setCheckBoxSelected(form.getCheckBoxSelected());
		eventService.publishEvent(shareProdResourceEvent);
	}
}

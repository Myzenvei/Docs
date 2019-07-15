/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.event;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.model.ShareProductResourceEmailProcessModel;
import com.gp.commerce.core.util.ShareProductResourceEvent;

import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;

/**
 * Share Product Email Process To start the business process
 */
public class ShareProductResourceEventListener extends AbstractEventListener<ShareProductResourceEvent> {

	private static final String SHARE_PRODUCT_RESOURCE_EMAIL_PROCESS = "shareProductResourceEmailProcess";
	private static final Logger LOG = Logger.getLogger(ShareProductResourceEventListener.class);
	private ModelService modelService;

	public ModelService getModelService() {
		return modelService;
	}

	private BaseSiteService baseSiteService;

	private BusinessProcessService businessProcessService;

	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService) {
		this.businessProcessService = businessProcessService;
	}

	@Required
	public void setModelService(final ModelService modelService) {
		this.modelService = modelService;
	}

	@Required
	public void setBaseSiteService(BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}

	/**
	 * Start business process after setting
	 * values from event to the process model
	 * 
	 * @param event
	 */
	protected void onEvent(final ShareProductResourceEvent event) {
		LOG.info("Method: onEvent ");
		final ShareProductResourceEmailProcessModel shareProcessModel = (ShareProductResourceEmailProcessModel) businessProcessService
				.createProcess(SHARE_PRODUCT_RESOURCE_EMAIL_PROCESS + System.currentTimeMillis(), SHARE_PRODUCT_RESOURCE_EMAIL_PROCESS);
		shareProcessModel.setRecipientEmails(event.getRecipientEmails());
		shareProcessModel.setSenderEmail(event.getSenderEmail());
		shareProcessModel.setSenderName(event.getSenderName());
		shareProcessModel.setSenderMessage(event.getSenderMessage());
		shareProcessModel.setResourceTitle(event.getResourceTitle());
		shareProcessModel.setResourceDescription(event.getResourceDescription());
		shareProcessModel.setImgurl(event.getImgurl());
		shareProcessModel.setResourcePageUrl(event.getResourcePageUrl());
		shareProcessModel.setEmbeddedLink(event.getEmbeddedLink());
		shareProcessModel.setCheckBoxSelected(event.getCheckBoxSelected());
		shareProcessModel.setSite(baseSiteService.getCurrentBaseSite());
		getModelService().save(shareProcessModel);
		businessProcessService.startProcess(shareProcessModel);
	}

}

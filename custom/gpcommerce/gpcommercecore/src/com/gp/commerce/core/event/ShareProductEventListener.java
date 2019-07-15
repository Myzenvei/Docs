/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.event;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.model.ShareProductEmailProcessModel;
import com.gp.commerce.core.util.ShareProductEvent;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

/**
 * Share Product Email Process To start the business process
 */
public class ShareProductEventListener extends AbstractEventListener<ShareProductEvent> {

	private static final String SHARE_PRODUCT_EMAIL_PROCESS = "shareProductEmailProcess";
	private static final Logger LOG = Logger.getLogger(ShareProductEventListener.class);
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
	protected void onEvent(final ShareProductEvent event) {
		LOG.info("Method: onEvent ");
		LOG.debug("Create ShareProductEmailProcess");
		final ShareProductEmailProcessModel shareProcessModel = (ShareProductEmailProcessModel) businessProcessService
				.createProcess(SHARE_PRODUCT_EMAIL_PROCESS + System.currentTimeMillis(), SHARE_PRODUCT_EMAIL_PROCESS);
		LOG.debug("Create ShareProductEmailProcess" + shareProcessModel);
		shareProcessModel.setAddLink(event.getAddLink());
		shareProcessModel.setAttachPDF(event.getAttachPDF());
		shareProcessModel.setRecipientEmails(event.getRecipientEmails());
		shareProcessModel.setSenderEmail(event.getSenderEmail());
		shareProcessModel.setSenderMessage(event.getSenderMessage());
		shareProcessModel.setSenderName(event.getSenderName());
		shareProcessModel.setSubject(event.getSubject());
		shareProcessModel.setSoldTo(event.getSoldTo());
		
		Map<String, String> shareProduct = new HashMap<>();
		for(ProductModel product :event.getProduct())
		{
			shareProduct.put(product.getCode(), product.getShareProductUrl());
		}
		shareProcessModel.setShareProductMap(shareProduct);
		
		shareProcessModel.setEmailAttachmentModelList(event.getEmailAttachmentList());
		shareProcessModel.setSite(baseSiteService.getCurrentBaseSite());
		shareProcessModel.setProducts(event.getProduct());
		LOG.debug("To Save ShareProductEmailProcess");
		getModelService().save(shareProcessModel);
		LOG.debug("Saved ShareProductEmailProcess");
		businessProcessService.startProcess(shareProcessModel);
	}

}

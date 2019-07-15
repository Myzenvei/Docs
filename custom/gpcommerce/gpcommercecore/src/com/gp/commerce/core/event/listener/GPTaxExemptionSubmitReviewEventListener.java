/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.event.listener;


import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.event.GPTaxExemptionSubmitReviewEvent;
import com.gp.commerce.core.model.TaxExemptionSubmitReviewEmailProcessModel;
import com.gp.commerce.core.services.GPCMSSiteService;

import de.hybris.platform.acceleratorservices.site.AbstractAcceleratorSiteEventListener;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

/**
 * 
 * Tax Exemption submit review event listener to listen the TaxExemptionSubmitReviewEvent and trggers an email to GP User
 *
 */

public class GPTaxExemptionSubmitReviewEventListener extends AbstractAcceleratorSiteEventListener<GPTaxExemptionSubmitReviewEvent> {

	private static final String TAXEXEMPTIONREVIEWUSEREMAIL = "taxexemptionreviewuseremail";
	private static final String TAX_EXEMPTION_SUBMIT_REVIEW_EMAIL_PROCESS = "taxExemptionSubmitReviewEmailProcess";
	private BusinessProcessService businessProcessService;
	private ModelService modelService;
	private BaseSiteService baseSiteService;
	private BaseStoreService baseStoreService;
	private GPCMSSiteService cmsSiteService;
	
	@Override
	protected void onSiteEvent(GPTaxExemptionSubmitReviewEvent event) {
		final TaxExemptionSubmitReviewEmailProcessModel emailProcessModel = (TaxExemptionSubmitReviewEmailProcessModel) businessProcessService
				.createProcess(TAX_EXEMPTION_SUBMIT_REVIEW_EMAIL_PROCESS + System.currentTimeMillis(), TAX_EXEMPTION_SUBMIT_REVIEW_EMAIL_PROCESS);
		emailProcessModel.setToEmail(cmsSiteService.getSiteConfig(TAXEXEMPTIONREVIEWUSEREMAIL));
		emailProcessModel.setSite(baseSiteService.getCurrentBaseSite());
		emailProcessModel.setStore(baseStoreService.getCurrentBaseStore());
		emailProcessModel.setUser(event.getCustomer());
		emailProcessModel.setTaxExemptionDocuments(event.getTaxExemptionDocumentList());
		getModelService().save(emailProcessModel);
		businessProcessService.startProcess(emailProcessModel);
		
	}

	@Override
	protected SiteChannel getSiteChannelForEvent(GPTaxExemptionSubmitReviewEvent event) {

		return baseSiteService.getCurrentBaseSite().getChannel();
	}

	protected BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(BusinessProcessService businessProcessService) {
		this.businessProcessService = businessProcessService;
	}

	protected ModelService getModelService() {
		return modelService;
	}
	
	@Required
	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	protected BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}
	@Required
	public void setBaseSiteService(BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}

	protected BaseStoreService getBaseStoreService() {
		return baseStoreService;
	}
	@Required
	public void setBaseStoreService(BaseStoreService baseStoreService) {
		this.baseStoreService = baseStoreService;
	}

	protected GPCMSSiteService getCmsSiteService() {
		return cmsSiteService;
	}
	@Required
	public void setCmsSiteService(GPCMSSiteService cmsSiteService) {
		this.cmsSiteService = cmsSiteService;
	}
}

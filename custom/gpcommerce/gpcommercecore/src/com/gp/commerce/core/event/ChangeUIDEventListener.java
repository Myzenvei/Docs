/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.event;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.ChangeUIDEvent;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

public class ChangeUIDEventListener extends GPAbstractAcceleratorSiteEventListener<ChangeUIDEvent> {

	private static final String CHANGE_UID_EMAIL_PROCESS = "changeUIDEmailProcess";

	private static final String CHANGE_UID_EMAIL_PROCESS_LABEL = "ChangeUIDEmailProcess-";

	private CustomerEmailResolutionService customerEmailResolutionService;

	public CustomerEmailResolutionService getCustomerEmailResolutionService() {
		return customerEmailResolutionService;
	}

	public void setCustomerEmailResolutionService(final CustomerEmailResolutionService customerEmailResolutionService) {
		this.customerEmailResolutionService = customerEmailResolutionService;
	}

	@Override
	protected void onSiteEvent(final ChangeUIDEvent event) {
		final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel = (StoreFrontCustomerProcessModel) getBusinessProcessService()
				.createProcess(CHANGE_UID_EMAIL_PROCESS_LABEL + event.getCustomer().getUid() + GpcommerceCoreConstants.HYPHEN
						+ System.currentTimeMillis(),
						CHANGE_UID_EMAIL_PROCESS);
		storeFrontCustomerProcessModel.setSite(event.getSite());
		storeFrontCustomerProcessModel.setCustomer(event.getCustomer());
		storeFrontCustomerProcessModel.setLanguage(event.getLanguage());
		storeFrontCustomerProcessModel.setCurrency(event.getCurrency());
		storeFrontCustomerProcessModel.setStore(event.getBaseStore());
		getModelService().save(storeFrontCustomerProcessModel);
		getBusinessProcessService().startProcess(storeFrontCustomerProcessModel);

	}

	@Override
	protected SiteChannel getSiteChannelForEvent(final ChangeUIDEvent event)
	{
		final BaseSiteModel site = event.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage(GpcommerceCoreConstants.EVENT_SITE, site);
		return site.getChannel();
	}
}

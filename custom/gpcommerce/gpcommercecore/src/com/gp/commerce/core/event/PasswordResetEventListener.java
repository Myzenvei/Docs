/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.event;


import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;

import de.hybris.platform.servicelayer.util.ServicesUtil;



import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.services.event.PasswordResetEvent;


/**
 * @author dadidam
 *
 */
public class PasswordResetEventListener extends GPAbstractAcceleratorSiteEventListener<PasswordResetEvent>
{
	private static final String PASSWORD_RESET_EMAIL_PROCESS = "passwordResetEmailProcess";
	private static final String PASSWORD_RESET_EMAIL_PROCESS_LABEL = "passwordResetEmailProcess-";
	

	@Override
	protected void onSiteEvent(final PasswordResetEvent pwdResetEvent)
	{
		final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel = (StoreFrontCustomerProcessModel) getBusinessProcessService()
				.createProcess(
						PASSWORD_RESET_EMAIL_PROCESS_LABEL + pwdResetEvent.getCustomer().getUid() + "-" + System.currentTimeMillis(),
						PASSWORD_RESET_EMAIL_PROCESS);
		storeFrontCustomerProcessModel.setSite(pwdResetEvent.getSite());
		storeFrontCustomerProcessModel.setCustomer(pwdResetEvent.getCustomer());
		storeFrontCustomerProcessModel.setLanguage(pwdResetEvent.getLanguage());
		storeFrontCustomerProcessModel.setCurrency(pwdResetEvent.getCurrency());
		storeFrontCustomerProcessModel.setStore(pwdResetEvent.getBaseStore());
		getModelService().save(storeFrontCustomerProcessModel);
		getBusinessProcessService().startProcess(storeFrontCustomerProcessModel);
	}

	@Override
	protected SiteChannel getSiteChannelForEvent(final PasswordResetEvent event)
	{
		final BaseSiteModel site = event.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage(GpcommerceCoreConstants.EVENT_SITE, site);
		return site.getChannel();
	}
}

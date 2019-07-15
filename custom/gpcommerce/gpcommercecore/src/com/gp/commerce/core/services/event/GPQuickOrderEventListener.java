/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.event;

import de.hybris.platform.acceleratorservices.site.AbstractAcceleratorSiteEventListener;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.model.QuickOrderEmailProcessModel;


public class GPQuickOrderEventListener extends AbstractAcceleratorSiteEventListener<GPQuickOrderEvent>{

	private static final String GP_QUICK_ORDER_EMAIL_PROCESS = "gpQuickOrderEmailProcess";
	private BusinessProcessService businessProcessService;

	@Override
	protected SiteChannel getSiteChannelForEvent(final GPQuickOrderEvent event) {
		final BaseSiteModel site = event.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage(GpcommerceCoreConstants.EVENT_ORDER_SITE, site);
		return site.getChannel();
	}

	@Override
	protected void onSiteEvent(final GPQuickOrderEvent event)  {

		final QuickOrderEmailProcessModel quickOrderEmailProcessModel = (QuickOrderEmailProcessModel) getBusinessProcessService()
				.createProcess(GP_QUICK_ORDER_EMAIL_PROCESS + "-" + System.currentTimeMillis(), GP_QUICK_ORDER_EMAIL_PROCESS);
		quickOrderEmailProcessModel.setSite(event.getSite());
		quickOrderEmailProcessModel.setLanguage(event.getLanguage());
		quickOrderEmailProcessModel.setCurrency(event.getCurrency());
		quickOrderEmailProcessModel.setStore(event.getBaseStore());
		quickOrderEmailProcessModel.setCustomer(event.getCustomer());
		quickOrderEmailProcessModel.setWishlist(event.getWishlistModel());
		quickOrderEmailProcessModel.setEmailIds(event.getEmailIds());
		getBusinessProcessService().startProcess(quickOrderEmailProcessModel);
	}

	public BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}

	public void setBusinessProcessService(final BusinessProcessService businessProcessService) {
		this.businessProcessService = businessProcessService;
	}
}

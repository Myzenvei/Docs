/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.event;


import javax.annotation.Resource;

import org.springframework.context.MessageSource;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.model.GPSubscriptionCartModel;
import com.gp.commerce.core.model.SubscriptionCartProcessModel;
import com.gp.commerce.core.services.event.SubscriptionOrderStatusEvent;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.util.ServicesUtil;


/**
 * @author dadidam
 *
 */
public class SubscriptionOrderStatusEventListener extends GPAbstractAcceleratorSiteEventListener<SubscriptionOrderStatusEvent>
{
	private static final String SUBSCRIPTION_ORDER_STATUS_EMAIL_PROCESS = "subscriptionOrderStatusEmailProcess";
	private static final String SUBSCRIPTION_ORDER_STATUS_EMAIL_PROCESS_LABEL = "subscriptionOrderStatusEmailProcess-";
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;
	@Resource(name = "i18nService")
	private I18NService i18nService;


	@Override
	protected SiteChannel getSiteChannelForEvent(SubscriptionOrderStatusEvent event) {
		final GPSubscriptionCartModel subsciptionCartModel = ((SubscriptionCartProcessModel)event.getSource()).getSubscriptionCart();
		final BaseSiteModel site = subsciptionCartModel.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage(GpcommerceCoreConstants.EVENT_SITE, site);
		return site.getChannel();
	}

	@Override
	protected void onSiteEvent(final SubscriptionOrderStatusEvent event) {
		final GPSubscriptionCartModel subsciptionCartModel = ((SubscriptionCartProcessModel)event.getSource()).getSubscriptionCart();
		final SubscriptionCartProcessModel subsciptionCartProcessModel = (SubscriptionCartProcessModel) getBusinessProcessService().createProcess(
				SUBSCRIPTION_ORDER_STATUS_EMAIL_PROCESS_LABEL + subsciptionCartModel.getCode() + "-" + System.currentTimeMillis(),
				SUBSCRIPTION_ORDER_STATUS_EMAIL_PROCESS);
		subsciptionCartProcessModel.setSubscriptionCart(subsciptionCartModel);
		subsciptionCartProcessModel.setSite(subsciptionCartModel.getSite());
		getModelService().save(subsciptionCartProcessModel);
		getBusinessProcessService().startProcess(subsciptionCartProcessModel);
		
	}
	
}

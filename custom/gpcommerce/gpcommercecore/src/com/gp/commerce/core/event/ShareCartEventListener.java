/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.core.event;

import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.site.AbstractAcceleratorSiteEventListener;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.model.order.CartModel;
import com.gp.commerce.core.model.CartProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.services.event.ShareCartEvent;


/**
 * Listener for "forgotten password" functionality event.
 */
public class ShareCartEventListener extends GPAbstractAcceleratorSiteEventListener<ShareCartEvent>
{

	private static final String SHARE_CART_EMAIL_PROCESS_LABEL = "shareCartEmailProcess-";
	private static final String SHARE_CART_EMAIL_PROCESS = "shareCartEmailProcess";

	private EmailService emailService;
	@Resource(name = "messageSource")
	private MessageSource messageSource;
	@Resource(name = "i18nService")
	private I18NService i18nService;

	public I18NService getI18nService() {
		return i18nService;
	}

	public void setI18nService(final I18NService i18nService) {
		this.i18nService = i18nService;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(final MessageSource messageSource) {
		this.messageSource = messageSource;
	}


	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(final EmailService emailService) {
		this.emailService = emailService;
	}

	@Override
	protected void onSiteEvent(final ShareCartEvent cartEvent)
	{
		final CartModel cartModel = ((CartProcessModel)cartEvent.getSource()).getCart();
		final CartProcessModel cartProcessModel = (CartProcessModel) getBusinessProcessService().createProcess(
				SHARE_CART_EMAIL_PROCESS_LABEL + cartModel.getCode() + "-" + System.currentTimeMillis(),
				SHARE_CART_EMAIL_PROCESS);
		cartProcessModel.setCart(cartModel);
		cartProcessModel.setToEmail(cartEvent.getToEmail());
		cartProcessModel.setSenderEmail(cartEvent.getSenderEmail());
		cartProcessModel.setSenderName(cartEvent.getSenderName());
		cartProcessModel.setSite(cartModel.getSite());
		cartProcessModel.setWishlist(((CartProcessModel)cartEvent.getSource()).getWishlist());

		getModelService().save(cartProcessModel);
		getBusinessProcessService().startProcess(cartProcessModel);

	}

	@Override
	protected SiteChannel getSiteChannelForEvent(final ShareCartEvent event)
	{

		final CartModel cart = ((CartProcessModel)event.getSource()).getCart();
		final BaseSiteModel site = cart.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage(GpcommerceCoreConstants.EVENT_SITE, site);
		return site.getChannel();

	}

}

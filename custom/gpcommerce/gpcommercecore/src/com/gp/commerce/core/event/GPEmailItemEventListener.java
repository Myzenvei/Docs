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
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.context.MessageSource;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.model.GPItemProcessModel;
import com.gp.commerce.core.services.event.GPEmailItemEvent;


/**
 * Listener for item functionality event.
 */
public class GPEmailItemEventListener extends GPAbstractAcceleratorSiteEventListener<GPEmailItemEvent>
{

	private static final Logger LOGGER = LogManager.getLogger(GPEmailItemEventListener.class);
	
	private EmailService emailService;
	@Resource(name = "messageSource")
	private MessageSource messageSource;
	@Resource(name = "i18nService")
	private I18NService i18nService;
	@Resource(name = "userService")
	private UserService userService;


	public I18NService getI18nService()
	{
		return i18nService;
	}

	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

	public MessageSource getMessageSource()
	{
		return messageSource;
	}

	public void setMessageSource(final MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	
	public EmailService getEmailService()
	{
		return emailService;
	}

	public void setEmailService(final EmailService emailService)
	{
		this.emailService = emailService;
	}

	@Override
	protected void onSiteEvent(final GPEmailItemEvent itemEvent)
	{
		if (itemEvent.getCustomer() instanceof B2BCustomerModel)
		{
			final B2BCustomerModel customer = (B2BCustomerModel) itemEvent.getCustomer();

			final GPItemProcessModel itemProcessModel = (GPItemProcessModel) getBusinessProcessService().createProcess(
					"gpAddressNotificationEmailProcess-" + itemEvent.getCustomer().getUid() + "-" + System.currentTimeMillis(),
					"gpAddressNotificationEmailProcess");
			itemProcessModel.setSite(itemEvent.getSite());
			itemProcessModel.setCustomer(itemEvent.getCustomer());
			itemProcessModel.setLanguage(itemEvent.getLanguage());
			itemProcessModel.setCurrency(itemEvent.getCurrency());
			itemProcessModel.setStore(itemEvent.getBaseStore());
			itemProcessModel.setRecipientEmails(itemEvent.getToEmails());
			itemProcessModel.setIsBackOfficeUser(itemEvent.isBackOfficeUser());
			itemProcessModel.setAdminName(itemEvent.getAdminName());

			if (itemEvent.getAddress() != null)
			{
				itemProcessModel.setItem(itemEvent.getAddress());
			}
			if (itemEvent.getInvitedCustomer() != null)
			{
				itemProcessModel.setInvitedCustomer(itemEvent.getInvitedCustomer());
			}

			if (itemEvent.getBccEmail() != null)
			{
				itemProcessModel.setBccEmail(itemEvent.getBccEmail());
			}

			if (itemEvent.getEmailSubject() != null)
			{
				itemProcessModel.setEmailSubject(itemEvent.getEmailSubject());
			}

			if (itemEvent.getAdminModel() != null)
			{
				itemProcessModel.setCustomer(itemEvent.getAdminModel());
			}

			if (itemEvent.getToken() != null)
			{
				itemProcessModel.setToken(itemEvent.getToken());
			}

			getModelService().save(itemProcessModel);
			getBusinessProcessService().startProcess(itemProcessModel);
			LOGGER.debug("GPEmailItemEventListener - INSIDE Event Listener:" + customer.getUid());
		}
	}

	@Override
	protected SiteChannel getSiteChannelForEvent(final GPEmailItemEvent itemEvent)
	{
		final BaseSiteModel site = itemEvent.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage(GpcommerceCoreConstants.EVENT_ORDER_SITE, site);
		return site.getChannel();
	}

}

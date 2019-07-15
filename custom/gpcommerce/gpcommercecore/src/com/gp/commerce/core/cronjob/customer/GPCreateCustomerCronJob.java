/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gp.commerce.core.cronjob.customer;

import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.commerceservices.security.SecureTokenService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.event.EmailSubjectType;
import com.gp.commerce.core.model.CustomerSaveCronJobModel;
import com.gp.commerce.core.services.GPUserService;
import com.gp.commerce.core.services.event.GPEmailItemEvent;
import com.gp.commerce.core.user.dao.GPUserDao;


/**
 * Cron Job triggers email after creation of customer
 */
public class GPCreateCustomerCronJob extends AbstractJobPerformable<CustomerSaveCronJobModel>
{
	private static final Logger LOG = Logger.getLogger(GPCreateCustomerCronJob.class);
	private static final String BUSINESS_USER_EMAIL = "gp.businessuser.email.id";

	@Resource(name = "userDao")
	private GPUserDao userDao;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

	@Resource(name = "userService")
	private GPUserService userService;

	@Resource(name = "eventService")
	private EventService eventService;

	@Resource(name = "b2bUnitService")
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "secureTokenService")
	private SecureTokenService secureTokenService;

	private long tokenValiditySeconds;

	/*
	 * job to trigger a event for customer
	 *
	 * @param arg0 CustomerSaveCronJobModel returns PerformResult
	 */
	@Override
	public PerformResult perform(final CustomerSaveCronJobModel arg0)
	{
		LOG.debug("CronJob started sucessfully");

		try {
			LOG.debug("Sending email for created customer");
			
			final List<B2BCustomerModel> customers = userDao.getAllNewCustomersForB2B(arg0.getSite());
			for (final B2BCustomerModel customer : customers)
			{
				if (clearAbortRequestedIfNeeded( arg0))
				{
					LOG.debug("The job is aborted.");
					return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
				}
				final String unitLevel = customer.getDefaultB2BUnit().getB2bUnitLevel();
				if (customer.getIsBackofficeCreated())
				{
					if (!GpcommerceCoreConstants.B2B_UNIT_L1.equalsIgnoreCase(unitLevel)
							&& customer.getUserApprovalStatus().getCode().equalsIgnoreCase(GpcommerceCoreConstants.PENDING))
					{
						final Collection<B2BCustomerModel> administrators = b2bUnitService
								.getUsersOfUserGroup((customer).getDefaultB2BUnit(), B2BConstants.B2BADMINGROUP, false);

						final GPEmailItemEvent item = new GPEmailItemEvent();
						item.setBccEmail(Config.getParameter(BUSINESS_USER_EMAIL));
						item.setInvitedCustomer(customer);
						if (CollectionUtils.isNotEmpty(administrators))
						{
							final List<B2BCustomerModel> list = new ArrayList<>(administrators);
							item.setAdminModel(list.get(0));
						}
						final List<String> listEmails = new ArrayList<>();
						for (final B2BCustomerModel administrator : administrators)
						{
							listEmails.add(administrator.getEmail());
						}
						item.setToEmails(listEmails);
						item.setEmailSubject(EmailSubjectType.NEW_USER_REVIEW.getSubject());
						eventService.publishEvent(initializeEvent(item, customer));
					}
					else
					{
						final GPEmailItemEvent item = new GPEmailItemEvent();
						item.setEmailSubject(EmailSubjectType.CUSTOMER_REGISTRATION.getSubject());
						item.setBackOfficeUser(true);
						final long timeStamp = getTokenValiditySeconds() > 0L ? new Date().getTime() : 0L;
						final SecureToken data = new SecureToken(customer.getUid(), timeStamp);
						final String token = secureTokenService.encryptData(data);
						customer.setToken(token);
						item.setToken(token);
						eventService.publishEvent(initializeEvent(item, customer));
					}
					customer.setRegistrationEmailSent(Boolean.TRUE);
					modelService.save(customer);
				}
			}
		}

		catch (final Exception e)
		{
			LOG.error("Error in create customer running job" + e.getMessage(), e);
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
		}
		LOG.debug("CronJob create customer finised sucessfully");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	protected AbstractCommerceUserEvent<BaseSiteModel> initializeEvent(final AbstractCommerceUserEvent<BaseSiteModel> event,
			final B2BCustomerModel customerModel)
	{
		event.setBaseStore(customerModel.getSite().getStores().get(0));
		event.setSite(customerModel.getSite());
		event.setCustomer(customerModel);
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		return event;
	}

	@Required
	public void setTokenValiditySeconds(final long tokenValiditySeconds)
	{
		if (tokenValiditySeconds < 0)
		{
			throw new IllegalArgumentException("tokenValiditySeconds has to be >= 0");
		}
		this.tokenValiditySeconds = tokenValiditySeconds;
	}

	public long getTokenValiditySeconds()
	{
		return tokenValiditySeconds;
	}
	@Override
	public boolean isAbortable() {
		return true;
	} 
}

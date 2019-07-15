/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gp.commerce.core.cronjob.customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.event.EmailSubjectType;
import com.gp.commerce.core.model.CustomerSaveCronJobModel;
import com.gp.commerce.core.services.GPUserService;
import com.gp.commerce.core.services.event.GPEmailItemEvent;
import com.gp.commerce.core.user.dao.GPUserDao;

import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;


/**
 * Cron Job triggers email after change in customer model
 */
public class GPUpdateCustomerCronJob extends AbstractJobPerformable<CustomerSaveCronJobModel>
{
	private static final Logger LOG = Logger.getLogger(GPUpdateCustomerCronJob.class);
	private static final String BASESITE_DELIMITER = "gpcommercewebservices.user.delimiter";

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
					
			final List<B2BCustomerModel> customers = userDao.getAllCustomersForB2B(
					(null != arg0.getLastStartTime() ? arg0.getLastStartTime() : arg0.getStartTime()), arg0.getSite());
			for (final B2BCustomerModel customer : customers)
			{
				if (clearAbortRequestedIfNeeded( arg0))
				{
					LOG.debug("The job is aborted.");
					return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
				}
				if (userService.isCustomerApproved(customer))
				{
					final B2BUnitModel b2bUnit = customer.getDefaultB2BUnit();
					Collection<B2BCustomerModel> administrators = b2bUnitService.getUsersOfUserGroup(
							b2bUnit, B2BConstants.B2BADMINGROUP, false);
					final B2BUnitModel rootUnit = b2bUnitService.getRootUnit(b2bUnit);
					if(CollectionUtils.isEmpty(administrators) && CollectionUtils.isNotEmpty(b2bUnit.getGroups())){
						administrators = b2bUnitService.getUsersOfUserGroup(rootUnit, B2BConstants.B2BADMINGROUP, false);
					}

					if (CollectionUtils.isNotEmpty(administrators))
					{
						customer.setActive(true);
						if (null != customer.getNewEmail()
								&& !customer.getNewEmail().isEmpty())
						{
							final String uidDelimiter = configurationService.getConfiguration().getString(BASESITE_DELIMITER);
							customer.setUid(customer.getNewEmail() + uidDelimiter + customer.getSite().getUid());
							customer.setEmail(customer.getNewEmail());
							customer.setNewEmail(null);
						}
						B2BCustomerModel adminUser= null!=customer.getB2bAdmin()?customer.getB2bAdmin():(B2BCustomerModel) new ArrayList(administrators).get(0);
						customer.setB2bAdmin(null);
						modelService.save(customer);

						final GPEmailItemEvent item = new GPEmailItemEvent();
						item.setInvitedCustomer(customer);
						item.setEmailSubject(EmailSubjectType.L3_ADMIN_APPROVED.getSubject());
						final List<String> listEmails=new ArrayList<>();
						if(null != adminUser && adminUser.equals(customer))
						{
							listEmails.add(customer.getB2bAdmin().getEmail());
							
						}
						item.setToEmails(listEmails);
						eventService.publishEvent(initializeEvent(item,adminUser,customer.getSite()));
						
						if (!GpcommerceCoreConstants.B2B_UNIT_L1
								.equalsIgnoreCase(customer.getDefaultB2BUnit().getB2bUnitLevel()) && BooleanUtils.isFalse(customer.getRegistrationEmailSent()))
						{
							final B2BCustomerModel b2bCustomerModel = customer;

							LOG.info("Sending email for Approved customer");

							final GPEmailItemEvent itemEvent = new GPEmailItemEvent();
							itemEvent.setEmailSubject(EmailSubjectType.CUSTOMER_REGISTRATION.getSubject());
							itemEvent.setBackOfficeUser(true);
							itemEvent.setToken(b2bCustomerModel.getToken());
							eventService.publishEvent(initializeEvent(itemEvent, b2bCustomerModel));
							customer.setRegistrationEmailSent(Boolean.TRUE);
							modelService.save(customer);
						}
					}
				}
			}
		}
		
		catch (final Exception e)
		{
			LOG.error("Error in running update customer job" + e.getMessage(), e);
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
		}
		arg0.setLastStartTime(arg0.getStartTime());
		modelService.save(arg0);
		LOG.debug("CronJob update customer finised sucessfully");
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
	
	protected AbstractCommerceUserEvent<BaseSiteModel> initializeEvent(final AbstractCommerceUserEvent<BaseSiteModel> event,
			final B2BCustomerModel adminModel, BaseSiteModel site)
	{
		event.setBaseStore(site.getStores().get(0));
		event.setSite(site);
		event.setCustomer(adminModel);
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		return event;
	}
	
	
	@Override
	public boolean isAbortable() {
		return true;
	} 
}

/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gp.commerce.core.cronjob.addresses;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.event.EmailSubjectType;
import com.gp.commerce.core.model.AddressSaveCronJobModel;
import com.gp.commerce.core.services.GPUserService;
import com.gp.commerce.core.services.event.GPEmailItemEvent;
import com.gp.commerce.core.user.dao.GPUserDao;


/**
 * Cron Job triggers email after change in address approval status
 */
public class GPAfterSaveAddressesCronJob extends AbstractJobPerformable<AddressSaveCronJobModel>
{
	private static final Logger LOG = Logger.getLogger(GPAfterSaveAddressesCronJob.class);

	@Resource(name = "userDao")
	private GPUserDao userDao;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

	@Resource(name = "userService")
	private GPUserService userService;

	@Resource(name = "eventService")
	private EventService eventService;

	/*
	 * job to trigger a event for addresses status change
	 *
	 * @param arg0 AddressSaveCronJobModel returns PerformResult
	 */
	@Override
	public PerformResult perform(final AddressSaveCronJobModel arg0)
	{
		LOG.debug("Save Addresses CronJob started sucessfully");
		try {
			final List<AddressModel> addresses = userDao.getAllAddressesForB2B(
					(null != arg0.getLastStartTime() ? arg0.getLastStartTime() : arg0.getStartTime()), arg0.getSite());
			for (final AddressModel addressModel : addresses)
			{
				if (clearAbortRequestedIfNeeded( arg0))
				{
					LOG.debug("The job is aborted.");
					return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
				}
				final B2BCustomerModel customerModel = (B2BCustomerModel) addressModel.getOwner();
				if (userService.isGPAdminApproved(addressModel))
				{
					final GPEmailItemEvent item = new GPEmailItemEvent();
					item.setAddress(addressModel);
					if (GpcommerceCoreConstants.ACTIVE.equals(addressModel.getApprovalStatus().getCode()))
					{
						item.setEmailSubject(EmailSubjectType.ADDRESS_APPROVED.getSubject());
					}
					if (GpcommerceCoreConstants.REJECTED.equals(addressModel.getApprovalStatus().getCode()))
					{
						item.setEmailSubject(EmailSubjectType.ADDRESS_REJECTED.getSubject());
					}

					eventService.publishEvent(initializeEvent(item, customerModel));
				}
			}
		}

		catch (final Exception e)
		{
			LOG.error("Error in running Update Addresses job" + e.getMessage(), e);
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
		}
		arg0.setLastStartTime(arg0.getStartTime());
		modelService.save(arg0);
		LOG.debug("Update Addresses CronJob finised sucessfully");
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
	@Override
	public boolean isAbortable() {
		return true;
	} 
}

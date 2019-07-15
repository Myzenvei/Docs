/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.impl;


import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.hmc.model.SavedValueEntryModel;
import de.hybris.platform.hmc.model.SavedValuesModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.GPApprovalEnum;
import com.gp.commerce.core.enums.GPUserApprovalStatusEnum;
import com.gp.commerce.core.enums.MarketingFrequencyEnum;
import com.gp.commerce.core.model.MarketingPreferenceModel;
import com.gp.commerce.core.model.MarketingPreferenceTypeModel;
import com.gp.commerce.core.model.MarketingPreferenceUpdateModel;
import com.gp.commerce.core.savedvalus.dao.GPSavedValuesDao;
import com.gp.commerce.core.services.GPUserService;
import com.gp.commerce.core.user.dao.GPUserDao;
import com.gp.commerce.facades.data.user.data.MarketingPreferenceDataList;

/**
 * This class is used for processing GP user services
 */
public class DefaultGPUserService extends DefaultUserService implements GPUserService
{

	@Resource(name = "gpUserDao")
	private transient GPUserDao userDao;

	@Resource(name = "gpSavedValuesDao")
	private transient GPSavedValuesDao gpSavedValuesDao;

	@Resource(name = "enumerationService")
	private transient EnumerationService enumerationService;
	
	@Resource(name = "i18nService")
	private transient I18NService i18nService;

	private static final String REJECTED_USER = "REJECTED";

	@Override
	public void register()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public List<MarketingPreferenceModel> getMarketingPreferences(final CMSSiteModel site)
	{
		return userDao.getMarketingPreferencesForSite(site);
	}

	@Override
	public List<MarketingPreferenceModel> getMarketingPreferencesForSiteAndType(final CMSSiteModel site ,final MarketingPreferenceTypeModel markPrefType)
	{
		return userDao.getMarketingPreferencesForSiteAndType(site,markPrefType);
	}

	@Override
	public void updateCustomerPreferences(final MarketingPreferenceDataList preferenceList, final CMSSiteModel site)
	{
		final List<MarketingPreferenceModel> sitePreferences = userDao.getMarketingPreferencesForSite(site);
		//create the list of selected preference id's which user has opted for.
		final List<String> selectedPreferenceIds = preferenceList.getMarketingPreferences().stream()
				.filter(marketingPreference -> marketingPreference.isSelected())
				.map(marketingPreference -> marketingPreference.getPreferenceTypeId()).collect(Collectors.toList());

		//create the list of rejected preference id's which user had opted for before but has now opted out.
		final List<String> rejectedPreferenceIds = preferenceList.getMarketingPreferences().stream()
				.filter(marketingPreference -> !marketingPreference.isSelected())
				.map(marketingPreference -> marketingPreference.getPreferenceTypeId()).collect(Collectors.toList());

		final CustomerModel currentCustomer = getCurrentCustomer();

		if(StringUtils.isNotEmpty(preferenceList.getFrequency()))
		{
			currentCustomer.setMarkFrequency(enumerationService.getEnumerationValue(MarketingFrequencyEnum.class, preferenceList.getFrequency()));
		}
		if(!sitePreferences.isEmpty())
		{
			final List<MarketingPreferenceModel> selectedCustomerPreferences = sitePreferences.stream()
					.filter(sitePreference -> selectedPreferenceIds.contains(sitePreference.getPreferenceTypeId()))
					.collect(Collectors.toList());
			final List<MarketingPreferenceModel> rejectedCustomerPreferences = sitePreferences.stream()
					.filter(sitePreference -> rejectedPreferenceIds.contains(sitePreference.getPreferenceTypeId()))
					.collect(Collectors.toList());

			final List<MarketingPreferenceModel> newPreferenceList = CollectionUtils
					.isEmpty(currentCustomer.getMarketingPreferences()) ? new ArrayList<>()
							: new ArrayList<>(currentCustomer.getMarketingPreferences());

			if (!selectedCustomerPreferences.isEmpty())
			{ //Adding only those entries of selectedPreferences which are not there in newPreferenceList already.
				selectedCustomerPreferences.removeAll(newPreferenceList);
				newPreferenceList.addAll(selectedCustomerPreferences);
			}
			if (!rejectedCustomerPreferences.isEmpty())
			{
				newPreferenceList.removeAll(rejectedCustomerPreferences);
			}

			currentCustomer.setMarketingPreferences(newPreferenceList);
			getModelService().save(currentCustomer);

			reviseMarketingPreferenceUpdate(selectedCustomerPreferences, site, currentCustomer, true);
			reviseMarketingPreferenceUpdate(rejectedCustomerPreferences, site, currentCustomer, false);
		}
	}

	protected CustomerModel getCurrentCustomer()
	{
		return (CustomerModel) super.getCurrentUser();
	}

	@Override
	public void reviseMarketingPreferenceUpdate(final List<MarketingPreferenceModel> preferences, final CMSSiteModel site,
			final CustomerModel customer, final boolean optIn)
	{
		if (!preferences.isEmpty())
		{
			for (final MarketingPreferenceModel preference : preferences)
			{
				final MarketingPreferenceUpdateModel mpu = new MarketingPreferenceUpdateModel();
				final Date date = new Date();
				mpu.setUid(Long.toString(date.getTime()));
				mpu.setMarketingPreference(preference);
				mpu.setSite(site);
				mpu.setCustomer(customer);
				mpu.setOptIn(optIn);
				getModelService().save(mpu);
			}

		}
	}

	@Override
	public Boolean isCustomerApproved(final B2BCustomerModel b2bCustomer)
	{
		Boolean status = false;
		final List<SavedValuesModel> changedLogs = gpSavedValuesDao.getChangedLogs(b2bCustomer);

		if (CollectionUtils.isNotEmpty(changedLogs))
		{
			final SavedValuesModel savedValuesModel = changedLogs.get(0);

			if (savedValuesModel != null)
			{
				final Set<SavedValueEntryModel> savedValueEntry = savedValuesModel.getSavedValuesEntries();
				for (final SavedValueEntryModel savedValueEntryModel : savedValueEntry)
				{

					if (savedValueEntryModel.getModifiedAttribute().equalsIgnoreCase(B2BCustomerModel.USERAPPROVALSTATUS))
					{
						final String oldValue = null != savedValueEntryModel.getOldValue()
								? ((GPUserApprovalStatusEnum) savedValueEntryModel.getOldValue()).getCode() : null;
						final String newValue = null != savedValueEntryModel.getNewValue()
								? ((GPUserApprovalStatusEnum) savedValueEntryModel.getNewValue()).getCode() : null;

						if (null != newValue && !newValue.equalsIgnoreCase(oldValue)
								&& GpcommerceCoreConstants.APPROVED.equalsIgnoreCase(newValue)
								&& GpcommerceCoreConstants.APPROVED.equalsIgnoreCase(b2bCustomer.getUserApprovalStatus().getCode()))
						{
							status = true;
						}
						if (null != newValue && REJECTED_USER.equalsIgnoreCase(newValue))
						{
							getModelService().save(b2bCustomer);
						}
					}

				}
			}
		}
		return status;
	}
	@Override
	public Boolean isGPAdminApproved(final AddressModel addressModel)
	{

		Boolean status = false;
		final List<SavedValuesModel> changedLogs = gpSavedValuesDao.getChangedLogs(addressModel);

		if (CollectionUtils.isNotEmpty(changedLogs))
		{
			final SavedValuesModel savedValuesModel = changedLogs.get(0);

			if (savedValuesModel != null)
			{
				final Set<SavedValueEntryModel> savedValueEntry = savedValuesModel.getSavedValuesEntries();
				for (final SavedValueEntryModel savedValueEntryModel : savedValueEntry)
				{

					if (savedValueEntryModel.getModifiedAttribute().equalsIgnoreCase(AddressModel.APPROVALSTATUS))
					{
						final String oldValue = null != savedValueEntryModel.getOldValue()
								? ((GPApprovalEnum) savedValueEntryModel.getOldValue()).getCode() : null;
						final String newValue = null != savedValueEntryModel.getNewValue()
								? ((GPApprovalEnum) savedValueEntryModel.getNewValue()).getCode() : null;

						if (null != newValue && !newValue.equalsIgnoreCase(oldValue)  && GpcommerceCoreConstants.PENDING_BY_GP.equalsIgnoreCase(oldValue)
								&& (GpcommerceCoreConstants.ACTIVE.equalsIgnoreCase(newValue)
										|| GpcommerceCoreConstants.REJECTED.equalsIgnoreCase(newValue)))
						{
							status = true;
						}

					}

				}
			}
		}
		return status;
	}
	@Override
	public Boolean isAdminApproved(final AddressModel addressModel)
	{

		Boolean status = false;
		final List<SavedValuesModel> changedLogs = gpSavedValuesDao.getChangedLogs(addressModel);

		if (CollectionUtils.isNotEmpty(changedLogs))
		{
			final SavedValuesModel savedValuesModel = changedLogs.get(0);

			if (savedValuesModel != null)
			{
				final Set<SavedValueEntryModel> savedValueEntry = savedValuesModel.getSavedValuesEntries();
				for (final SavedValueEntryModel savedValueEntryModel : savedValueEntry)
				{

					if (savedValueEntryModel.getModifiedAttribute().equalsIgnoreCase(AddressModel.APPROVALSTATUS))
					{
						final String oldValue = null != savedValueEntryModel.getOldValue()
								? ((GPApprovalEnum) savedValueEntryModel.getOldValue()).getCode() : null;
						final String newValue = null != savedValueEntryModel.getNewValue()
								? ((GPApprovalEnum) savedValueEntryModel.getNewValue()).getCode() : null;

						if (null != oldValue && !oldValue.equalsIgnoreCase(newValue)
								&& GpcommerceCoreConstants.PENDING_BY_ADMIN.equalsIgnoreCase(oldValue)
								&& GpcommerceCoreConstants.PENDING_BY_GP.equalsIgnoreCase(newValue))
						{
							status = true;
						}

					}

				}
			}
		}
		return status;
	}
	/**
	 * method to get the Distinct marketing preferences.
	 *
	 * @param preferencesDataList
	 *           The list of marketing preferences which user has opted in or out.
	 */

	@Override
	public List<MarketingPreferenceTypeModel> getDistMarketingPreferences(final CMSSiteModel site) {
		return userDao.getDistMarketingPreferences(site);
	}
	
	@Override
	public Locale getCurrentLocale() {
		return null!=i18nService.getCurrentLocale()?i18nService.getCurrentLocale():Locale.ENGLISH;
}

}

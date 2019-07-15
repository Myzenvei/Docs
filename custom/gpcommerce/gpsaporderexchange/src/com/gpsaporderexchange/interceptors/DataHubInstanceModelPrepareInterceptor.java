/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gpsaporderexchange.interceptors;


import de.hybris.platform.datahubbackoffice.model.DataHubInstanceModelModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import org.springframework.beans.factory.annotation.Required;




/**
 * @author Siddharth Jain
 */
public class DataHubInstanceModelPrepareInterceptor implements PrepareInterceptor<DataHubInstanceModelModel>
{
	private static final String DATAHUBINSTANCELOCATION = "datahubadapter.datahuboutbound.url";
	private static final String DATAHUBINSTANCENAME = "dthcluster";
	private static final String LOCALENV = "local";
	private static final String DATAHUBLOCALHOSTINSTANCENAME = "localhost";
	private static final String DATAHUBCONFIGENVNAME = "datahub.config.env.name";


	private ConfigurationService configurationService;


	@Override
	public void onPrepare(final DataHubInstanceModelModel datahubInstance, final InterceptorContext ctx)
			throws InterceptorException
	{
		if (getConfigurationService().getConfiguration().containsKey(DATAHUBCONFIGENVNAME) && isApplicable(datahubInstance))
		{
			datahubInstance.setInstanceName(DATAHUBINSTANCENAME);
			datahubInstance.setInstanceLocation(getConfigurationService().getConfiguration().getString(DATAHUBINSTANCELOCATION));

		}
	}

	private boolean isApplicable(final DataHubInstanceModelModel datahubInstance)
	{

		if ((datahubInstance.getInstanceName() != null && datahubInstance.getInstanceName().equals(DATAHUBLOCALHOSTINSTANCENAME))
				&& !getConfigurationService().getConfiguration().getString(DATAHUBCONFIGENVNAME).equals(LOCALENV))
		{
			return true;
		}
		return false;
	}

	/**
	 * @return configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 */
	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

}

/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gpsaporderexchange.setup;

import static com.gpsaporderexchange.constants.GpsaporderexchangeConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import com.gpsaporderexchange.constants.GpsaporderexchangeConstants;
import com.gpsaporderexchange.service.GpsaporderexchangeService;


@SystemSetup(extension = GpsaporderexchangeConstants.EXTENSIONNAME)
public class GpsaporderexchangeSystemSetup
{
	private final GpsaporderexchangeService gpsaporderexchangeService;

	public GpsaporderexchangeSystemSetup(final GpsaporderexchangeService gpsaporderexchangeService)
	{
		this.gpsaporderexchangeService = gpsaporderexchangeService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		gpsaporderexchangeService.createLogo(PLATFORM_LOGO_CODE);
	}
}

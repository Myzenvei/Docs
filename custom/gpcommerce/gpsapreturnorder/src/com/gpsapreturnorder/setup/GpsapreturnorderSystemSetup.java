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
package com.gpsapreturnorder.setup;

import static com.gpsapreturnorder.constants.GpsapreturnorderConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import com.gpsapreturnorder.constants.GpsapreturnorderConstants;
import com.gpsapreturnorder.service.GpsapreturnorderService;


@SystemSetup(extension = GpsapreturnorderConstants.EXTENSIONNAME)
public class GpsapreturnorderSystemSetup
{
	private final GpsapreturnorderService gpsapreturnorderService;

	public GpsapreturnorderSystemSetup(final GpsapreturnorderService gpsapreturnorderService)
	{
		this.gpsapreturnorderService = gpsapreturnorderService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		gpsapreturnorderService.createLogo(PLATFORM_LOGO_CODE);
	}
}

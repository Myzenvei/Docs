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
package com.gpintegration.setup;

import static com.gpintegration.constants.GpintegrationConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.service.GpintegrationService;


@SystemSetup(extension = GpintegrationConstants.EXTENSIONNAME)
public class GpintegrationSystemSetup
{
	private final GpintegrationService gpintegrationService;

	public GpintegrationSystemSetup(final GpintegrationService gpintegrationService)
	{
		this.gpintegrationService = gpintegrationService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		gpintegrationService.createLogo(PLATFORM_LOGO_CODE);
	}
}

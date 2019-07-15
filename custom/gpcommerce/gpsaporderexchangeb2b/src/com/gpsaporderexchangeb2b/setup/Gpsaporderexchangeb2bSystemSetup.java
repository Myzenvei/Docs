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
package com.gpsaporderexchangeb2b.setup;

import static com.gpsaporderexchangeb2b.constants.Gpsaporderexchangeb2bConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import com.gpsaporderexchangeb2b.constants.Gpsaporderexchangeb2bConstants;
import com.gpsaporderexchangeb2b.service.Gpsaporderexchangeb2bService;


@SystemSetup(extension = Gpsaporderexchangeb2bConstants.EXTENSIONNAME)
public class Gpsaporderexchangeb2bSystemSetup
{
	private final Gpsaporderexchangeb2bService gpsaporderexchangeb2bService;

	public Gpsaporderexchangeb2bSystemSetup(final Gpsaporderexchangeb2bService gpsaporderexchangeb2bService)
	{
		this.gpsaporderexchangeb2bService = gpsaporderexchangeb2bService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		gpsaporderexchangeb2bService.createLogo(PLATFORM_LOGO_CODE);
	}
}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.acceleratorservices.urlresolver.impl.DefaultSiteBaseUrlResolutionService;

import java.lang.String;

import java.time.LocalDate;

import com.gp.commerce.core.services.GPSiteBaseUrlResolutionService;

import java.lang.Integer;

public class GPSiteBaseUrlResolutionServiceImpl extends DefaultSiteBaseUrlResolutionService implements GPSiteBaseUrlResolutionService {

	public Integer getYear()
	{
		final LocalDate currentDate = LocalDate.now();
		final int year = currentDate.getYear();
		return year;
	}

}

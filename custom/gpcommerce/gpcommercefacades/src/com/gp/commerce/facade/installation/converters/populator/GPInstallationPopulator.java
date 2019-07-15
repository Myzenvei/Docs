/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facade.installation.converters.populator;

import com.gp.commerce.core.model.ScheduleInstallationModel;
import com.gp.commerce.facades.data.GPScheduleInstallationData;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class GPInstallationPopulator implements Populator<ScheduleInstallationModel, GPScheduleInstallationData>{

	@Override
	public void populate(ScheduleInstallationModel source, GPScheduleInstallationData target)
			throws ConversionException {
		target.setName(source.getName());
		target.setExtraInfo(source.getExtraInfo());
		target.setPhoneNo(source.getPhoneNo());
		target.setPreferredDate(source.getDate());
		target.setPreferredTime(source.getPreferedTime());
		
	}

}

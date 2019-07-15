/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facade.survey.converters.populator;

import com.gp.commerce.core.model.GPSurveyOptionModel;
import com.gp.commerce.facades.survey.data.OptionData;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class GPOptionPopulator implements Populator<GPSurveyOptionModel, OptionData>{

	@Override
	public void populate(GPSurveyOptionModel source, OptionData target) throws ConversionException {
		target.setCode(source.getCode());
		target.setLabel(source.getOptionDescription());
		if(null != source.getReDirectQuestion()) {
			target.setReDirectQuestion(source.getReDirectQuestion().getCode());
		}
	}

}

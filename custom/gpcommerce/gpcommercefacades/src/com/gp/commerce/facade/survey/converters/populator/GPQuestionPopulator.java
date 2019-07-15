/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facade.survey.converters.populator;

import org.apache.commons.collections4.CollectionUtils;

import com.gp.commerce.core.model.GPSurveyOptionModel;
import com.gp.commerce.core.model.GPSurveyQuestionModel;
import com.gp.commerce.facades.survey.data.OptionData;
import com.gp.commerce.facades.survey.data.QuestionData;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

public class GPQuestionPopulator implements Populator<GPSurveyQuestionModel, QuestionData>{

	private Converter<GPSurveyOptionModel, OptionData> gpOptionConverter;

	@Override
	public void populate(GPSurveyQuestionModel source, QuestionData target) throws ConversionException {
		target.setCode(source.getCode());
		target.setLabel(source.getQuestionDescription());
		if(null != source.getQuestionType()) {
			target.setQuestionType(source.getQuestionType().getCode());
		}
		if(CollectionUtils.isNotEmpty(source.getGpSurveyOption())) {
			target.setOptions(getGpOptionConverter().convertAll(source.getGpSurveyOption()));
		}
	}
	
	public Converter<GPSurveyOptionModel, OptionData> getGpOptionConverter() {
		return gpOptionConverter;
	}
	
	public void setGpOptionConverter(Converter<GPSurveyOptionModel, OptionData> gpOptionConverter) {
		this.gpOptionConverter = gpOptionConverter;
	}

}

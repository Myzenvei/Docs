/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facade.survey.converters.populator;

import org.apache.commons.collections4.CollectionUtils;

import com.gp.commerce.core.model.GPSurveyModel;
import com.gp.commerce.core.model.GPSurveyQuestionModel;
import com.gp.commerce.facades.survey.data.QuestionData;
import com.gp.commerce.facades.user.data.SurveyRegisterData;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

public class GPSurveyPopulator implements Populator<GPSurveyModel, SurveyRegisterData>{

	private Converter<GPSurveyQuestionModel, QuestionData> gpQuestionConverter;

	@Override
	public void populate(GPSurveyModel source, SurveyRegisterData target) throws ConversionException {
		
		target.setSurveyCode(source.getCode());
		if(CollectionUtils.isNotEmpty(source.getGpSurveyQuestions())) {
			target.setQuestionSelected(getGpQuestionConverter().convertAll(source.getGpSurveyQuestions()));
		}
	}
	
	public Converter<GPSurveyQuestionModel, QuestionData> getGpQuestionConverter() {
		return gpQuestionConverter;
	}
	
	public void setGpQuestionConverter(Converter<GPSurveyQuestionModel, QuestionData> gpQuestionConverter) {
		this.gpQuestionConverter = gpQuestionConverter;
	}
}
/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;

import com.gp.commerce.core.model.GPSurveyOptionResponseModel;
import com.gp.commerce.core.model.GPSurveyQuestionResponseModel;
import com.gp.commerce.core.model.GPSurveyResponseModel;
import com.gp.commerce.facades.survey.data.OptionData;
import com.gp.commerce.facades.survey.data.QuestionData;
import com.gp.commerce.facades.user.data.SurveyRegisterData;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

/**
 * The Class GPSurveyResponsePopulator.
 */
public class GPSurveyResponsePopulator implements Populator<GPSurveyResponseModel, SurveyRegisterData>{

	/** The Constant HYPHEN. */
	private static final String HYPHEN = "-";
	
	/** The Constant QUESTION_1. */
	private static final String QUESTION_1 = "Q1";
	
	/**
	 * Populate target from source.
	 *
	 * @param source the source
	 * @param target the target
	 * @throws ConversionException the conversion exception
	 */
	@Override
	public void populate(GPSurveyResponseModel source, SurveyRegisterData target) throws ConversionException {
		final List<GPSurveyQuestionResponseModel> questionResponses = source.getGpSurveyResponseQuestion();
		@SuppressWarnings("unchecked")
		final Map<String, List<OptionData>> surveyMap = new HashedMap();
		for (final GPSurveyQuestionResponseModel question : questionResponses)
		{
			final List<GPSurveyOptionResponseModel> options = question.getGpSurveyOption();

			for (final GPSurveyOptionResponseModel opt : options)
			{
				final String qKey = splitAndGetQuestion(opt.getCode(), true);
				final String aValue = splitAndGetQuestion(opt.getCode(), false);
				if (surveyMap.containsKey(qKey))
				{
					final OptionData optionData = new OptionData();
					optionData.setCode(aValue);
					if (!StringUtils.isEmpty(opt.getOptionDescription())) {
						optionData.setLabel(opt.getOptionDescription());
					} else {
						optionData.setLabel(StringUtils.EMPTY);
					}
					if (null != opt.getReDirectQuestion()) {
						if (null != opt.getReDirectQuestion().getCode()) {
							optionData.setReDirectQuestion(opt.getReDirectQuestion().getCode());
						}
					} else {
						optionData.setReDirectQuestion(StringUtils.EMPTY);
					}
					surveyMap.get(qKey).add(optionData);
				}
				else
				{
					final OptionData optionData = new OptionData();
					optionData.setCode(aValue);
					if (!StringUtils.isEmpty(opt.getOptionDescription())) {
						optionData.setLabel(opt.getOptionDescription());
					} else {
						optionData.setLabel(StringUtils.EMPTY);
					}
					if (null != opt.getReDirectQuestion()) {
						if (null != opt.getReDirectQuestion().getCode()) {
							optionData.setReDirectQuestion(opt.getReDirectQuestion().getCode());
						}
					} else {
						optionData.setReDirectQuestion(StringUtils.EMPTY);
					}
					final List<OptionData> optionsData = new ArrayList<>();
					optionsData.add(optionData);
					surveyMap.put(qKey, optionsData);
				}
			}
		}

		final List<QuestionData> questions = new ArrayList<>();
		surveyMap.forEach((k, v) -> {
			final QuestionData q = new QuestionData();
			q.setCode(k);
			q.setOptions(v);
			questions.add(q);
		});

		target.setSurveyCode(splitAndGetCodeSurvey(source.getCode()));
		target.setQuestionSelected(questions);
	}

	/**
	 * Split and get question.
	 *
	 * @param surveyText the survey text
	 * @param valueFinder the value finder
	 * @return the string
	 */
	private String splitAndGetQuestion(final String surveyText, final boolean valueFinder)
	{
		final String[] splits = surveyText.split(HYPHEN);
		if (valueFinder)
		{
			//Return Q1 as default
			return (splits != null && splits[0] != null) ? splits[0] : QUESTION_1;
		}
		else
		{
			//Return O1 as default
			return (splits != null && splits[1] != null) ? splits[1] : QUESTION_1;
		}
	}
	
	/**
	 * Split and get code survey.
	 *
	 * @param surveyText the survey text
	 * @return the string
	 */
	private String splitAndGetCodeSurvey(final String surveyText)
	{
		final String[] splits = surveyText.split(HYPHEN);
		return (splits != null && splits[1] != null) ? splits[1] : "S1";
	}

}

/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.survey;

import java.util.List;

import com.gp.commerce.core.model.GPSurveyModel;
import com.gp.commerce.core.model.GPSurveyOptionModel;
import com.gp.commerce.core.model.GPSurveyQuestionModel;
import com.gp.commerce.facades.user.data.SurveyRegisterData;

/**
 * This interface is used for processing GP Survey services
 */
public interface GPSurveyService {


	/**
	 * Get All survey Options
	 * @return GPSurveyOptionModel
	 */
	List<GPSurveyOptionModel> getSurveyOptions();

	/**
	 * Get All survey Question
	 * @return GPSurveyQuestionModel
	 */
	List<GPSurveyQuestionModel> getSurveyQuestions();

	/**
	 * Get survey Data
	 * @param surveyCode the survey code
	 * @return {@link GPSurveyModel}
	 */
	GPSurveyModel getSurveyData(String surveyCode);

	/**
	 * update B2b Unit details , address , company details address and b2bUnitLevel
	 * @param userId the user ID
	 * @param surveyData the survey register data
	 */
	void submitSurvey(final String userId, final SurveyRegisterData surveyData);

}

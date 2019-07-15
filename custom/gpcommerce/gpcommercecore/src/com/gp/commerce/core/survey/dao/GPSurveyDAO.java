/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.survey.dao;

import java.util.List;

import com.gp.commerce.core.model.GPSurveyModel;
import com.gp.commerce.core.model.GPSurveyOptionModel;
import com.gp.commerce.core.model.GPSurveyQuestionModel;

public interface GPSurveyDAO {

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
	 * @return GPSurveyModel
	 */
	GPSurveyModel getSurveyData(String surveyCode);

}

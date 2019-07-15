/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.customer.survey;

import com.gp.commerce.facades.user.data.SurveyRegisterData;


/**
 * The Interface GPSurveyFacade.
 */
public interface GPSurveyFacade
{

	/**
	 * Get Survey Question and options
	 *
	 * @return QuestionListData
	 */
	SurveyRegisterData getSurveyOptions();

	/**
	 * Submits survey
	 * @param userId
	 * 			the userid
	 * @param surveyData
	 * 			the survey data
	 */
	void submitSurvey(String userId, SurveyRegisterData surveyData);


	/**
	 * this method gets survey
	 * @param baseSiteId
	 * 			the base site id
	 * @param userId
	 * 			the user id
	 * @return the survey register data
	 */
	SurveyRegisterData getSurvey(String baseSiteId, String userId);
}

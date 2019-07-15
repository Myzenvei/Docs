/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.survey.dao.impl;

import java.util.Collections;
import java.util.List;

import com.gp.commerce.core.model.GPSurveyModel;
import com.gp.commerce.core.model.GPSurveyOptionModel;
import com.gp.commerce.core.model.GPSurveyQuestionModel;
import com.gp.commerce.core.survey.dao.GPSurveyDAO;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

public class GPSurveyDAOImpl implements GPSurveyDAO {
	
	private FlexibleSearchService flexibleSearchService;

	private static final String SURVEY_OPTION_QUERY = "SELECT {gps:" + GPSurveyOptionModel.PK + "} FROM {"
			+ GPSurveyOptionModel._TYPECODE + " AS gps}";

	private static final String SURVEY_QUESTION_QUERY = "SELECT {gps:" + GPSurveyQuestionModel.PK + "} FROM {"
			+ GPSurveyQuestionModel._TYPECODE + " AS gps}";
	
	/**
	 * Get All survey Options
	 * @return GPSurveyOptionModel
	 */
	@Override
	public List<GPSurveyOptionModel> getSurveyOptions() {
		
		final FlexibleSearchQuery query = new FlexibleSearchQuery(SURVEY_OPTION_QUERY);
		final SearchResult<GPSurveyOptionModel> result = getFlexibleSearchService().search(query);
		if (null != result)
		{
			return result.getResult();
		}
		return Collections.emptyList();
	}
	
	/**
	 * Get All survey Question
	 * @return GPSurveyQuestionModel
	 */
	@Override
	public List<GPSurveyQuestionModel> getSurveyQuestions() {
		final FlexibleSearchQuery query = new FlexibleSearchQuery(SURVEY_QUESTION_QUERY);
		final SearchResult<GPSurveyQuestionModel> result = getFlexibleSearchService().search(query);
		if (null != result)
		{
			return result.getResult();
		}
		return Collections.emptyList();
	}
	/**
	 * Get survey Data
	 * @return GPSurveyModel
	 */
	@Override
	public GPSurveyModel getSurveyData(String surveyCode){
		GPSurveyModel model=new GPSurveyModel();
		model.setCode(surveyCode);
		return (GPSurveyModel)getFlexibleSearchService().getModelByExample(model);
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}
}


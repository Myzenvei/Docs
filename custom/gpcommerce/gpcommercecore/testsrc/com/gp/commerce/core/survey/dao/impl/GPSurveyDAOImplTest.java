package com.gp.commerce.core.survey.dao.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.model.GPSurveyModel;
import com.gp.commerce.core.model.GPSurveyOptionModel;
import com.gp.commerce.core.model.GPSurveyQuestionModel;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import junit.framework.Assert;

public class GPSurveyDAOImplTest {

	@InjectMocks
	GPSurveyDAOImpl surveyDao=new GPSurveyDAOImpl();
	
	@Mock
	private FlexibleSearchService flexibleSearchService;

	List<GPSurveyOptionModel> surveyOption=new ArrayList<>();
	List<GPSurveyQuestionModel> surveyQuestions=new ArrayList<>();
	GPSurveyModel surveyModel = new GPSurveyModel();
	
	SearchResult result=new SearchResultImpl(surveyOption, 1, 1, 1);
	SearchResult resultQuestion=new SearchResultImpl(surveyQuestions, 1, 1, 1);
	
	String surveyCode = "s1";
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
	}

	@Test
	public void testGetSurveyOptions() {
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(result);
		assertNotNull(surveyDao.getSurveyOptions());
	}
	@Test
	public void testGetSurveyOptions1() {
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(null);
		//assertTrue(result1);
		Assert.assertTrue(surveyDao.getSurveyOptions().isEmpty());
	}

	@Test
	public void testGetSurveyQuestions() {
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(resultQuestion);
		assertNotNull(surveyDao.getSurveyQuestions());
	}
	
	@Test
	public void testGetSurveyQuestions1() {
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(null);
		Assert.assertTrue(surveyDao.getSurveyQuestions().isEmpty());
	}
	
	@Test
	public void testGetSurveyData() {
		
		when(flexibleSearchService.getModelByExample(Mockito.any())).thenReturn(surveyModel);
		assertNotNull(surveyDao.getSurveyData(surveyCode));	
	}

}

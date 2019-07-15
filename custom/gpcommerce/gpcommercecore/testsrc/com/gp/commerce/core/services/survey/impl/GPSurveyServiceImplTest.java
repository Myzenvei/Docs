/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.survey.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.gp.commerce.core.company.services.GPB2BUnitsService;
import com.gp.commerce.core.enums.AnswerType;
import com.gp.commerce.core.model.GPSurveyModel;
import com.gp.commerce.core.model.GPSurveyOptionModel;
import com.gp.commerce.core.model.GPSurveyOptionResponseModel;
import com.gp.commerce.core.model.GPSurveyQuestionModel;
import com.gp.commerce.core.model.GPSurveyQuestionResponseModel;
import com.gp.commerce.core.model.GPSurveyResponseModel;
import com.gp.commerce.core.survey.dao.GPSurveyDAO;
import com.gp.commerce.facades.survey.data.OptionData;
import com.gp.commerce.facades.survey.data.QuestionData;
import com.gp.commerce.facades.user.data.SurveyRegisterData;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import static org.junit.Assert.assertTrue;

@UnitTest
public class GPSurveyServiceImplTest {
	
	private static final String L1 = "b2b.unit.level.l1";
	private static final String L2 = "b2b.unit.level.l2";

	@Mock
	GPSurveyOptionModel gpSurveyOptionModel;
	@Mock
	GPSurveyOptionResponseModel gpSurveyOptionResponseModel;
	@Mock
	GPSurveyQuestionResponseModel gpSurveyQuestionResponseModel;
	@Mock
	GPSurveyQuestionModel gpSurveyQuestionModel;
	@Mock
	OptionData optionData;
	@Mock
	QuestionData questionData;
	@Mock
	GPSurveyModel surveyModel;
	@Mock
	SurveyRegisterData surveyData;
	@Mock
	B2BUnitModel b2bUnitModel; 
	@Mock
	GPSurveyResponseModel gpSurveyResponseModel;
	@Mock
	B2BCustomerModel b2bCustomer;
	
	@Mock
	protected ConfigurationService configurationService;

	@Mock
	protected GPB2BUnitsService<B2BUnitModel, UserModel> gpB2BUnitsService;

	@Mock
	private ModelService modelService;

	@Mock
	private UserService userService;

	@Mock
	private Populator<AddressData, AddressModel> addressReversePopulator;

	@Mock
	private GPSurveyDAO gpSurveyDAO;
	private String surveyCode = "S1";

	List<GPSurveyOptionModel> surveyOption = new ArrayList<>();
	
	List<GPSurveyQuestionModel> surveyQuestions = new ArrayList<>();
	
	@InjectMocks
	GPSurveyServiceImpl service = new GPSurveyServiceImpl();

	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(service, "userService", userService);
		ReflectionTestUtils.setField(service, "modelService", modelService);
		ReflectionTestUtils.setField(service, "configurationService", configurationService);
		ReflectionTestUtils.setField(service, "addressReversePopulator", addressReversePopulator);
		ReflectionTestUtils.setField(service, "gpB2BUnitsService", gpB2BUnitsService);
		
		service.setGpSurveyDAO(gpSurveyDAO);
		when(gpSurveyDAO.getSurveyOptions()).thenReturn(surveyOption);
		when(gpSurveyDAO.getSurveyQuestions()).thenReturn(surveyQuestions);
		when(gpSurveyDAO.getSurveyData(surveyCode)).thenReturn(surveyModel);
		when(userService.getUserForUID("UserID")).thenReturn(b2bCustomer);
		when(modelService.create(GPSurveyResponseModel.class)).thenReturn(gpSurveyResponseModel);
		when(modelService.create(GPSurveyOptionResponseModel.class)).thenReturn(gpSurveyOptionResponseModel);
		when(modelService.create(GPSurveyQuestionResponseModel.class)).thenReturn(gpSurveyQuestionResponseModel);
		when( modelService.create(AddressModel.class)).thenReturn(Mockito.mock(AddressModel.class));
		when(b2bCustomer.getDefaultB2BUnit()).thenReturn(b2bUnitModel);
		when(modelService.create(B2BUnitModel.class)).thenReturn(b2bUnitModel);
		when(surveyData.getSurveyCode()).thenReturn(surveyCode);
		when(surveyData.isSurveyEdit()).thenReturn(true);
		when(surveyData.getAddress()).thenReturn(Mockito.mock(AddressData.class));
		when(surveyData.getNoOfEmployees()).thenReturn(10);
		when(surveyData.getAddress()).thenReturn(Mockito.mock(AddressData.class));
		when(b2bCustomer.getPrimaryAdmin()).thenReturn(true);
		when(questionData.getCode()).thenReturn("questionCode");
		when(optionData.getCode()).thenReturn("optionCode");
		when(gpSurveyOptionModel.getCode()).thenReturn("optionCode");
		when(gpSurveyOptionModel.getOptionDescription()).thenReturn("optionCode");
		when(gpSurveyOptionModel.getReDirectQuestion()).thenReturn(gpSurveyQuestionModel);
		when(gpSurveyOptionModel.getLevel()).thenReturn("1");
		when(gpSurveyQuestionModel.getCode()).thenReturn("questionCode");
		when(gpSurveyQuestionModel.getQuestionType()).thenReturn(AnswerType.TEXT_FIELD);
		when(questionData.getOptions()).thenReturn(Collections.singletonList(optionData));
		when(surveyData.getQuestionSelected()).thenReturn(Collections.singletonList(questionData));
		when(surveyModel.getGpSurveyQuestions()).thenReturn(Collections.singletonList(gpSurveyQuestionModel));
		when(questionData.getOptions()).thenReturn(Collections.singletonList(optionData));
		when(b2bCustomer.getUid()).thenReturn("b2bCustomerID");
		when(gpSurveyQuestionModel.getGpSurveyOption()).thenReturn(Collections.singletonList(gpSurveyOptionModel));
		when(configurationService.getConfiguration()).thenReturn(Mockito.mock(Configuration.class));
		when(configurationService.getConfiguration().getString(L1)).thenReturn("L1");
		when(configurationService.getConfiguration().getString(L2)).thenReturn("L2");
		when(b2bUnitModel.getAddresses()).thenReturn(Collections.singletonList(Mockito.mock(AddressModel.class)));
		when( gpB2BUnitsService.getUnitsWithChildNodes(b2bCustomer, true)).thenReturn(Collections.singletonList(b2bUnitModel));
		when(b2bUnitModel.getB2bUnitLevel()).thenReturn("UnitCode");
		
		
	}

	@Test
	public void testGetSurveyOptions() {
		assertTrue(service.getSurveyOptions().equals(surveyOption));
	}

	@Test
	public void testGetSurveyQuestions() {
		assertTrue(service.getSurveyQuestions().equals(surveyQuestions));
	}

	@Test
	public void testGetSurveyData() {
		assertTrue(service.getSurveyData(surveyCode).equals(surveyModel));
	}

	private GPSurveyServiceImpl createTestSubject() {
		return new GPSurveyServiceImpl();
	}

	@Test
	public void testSubmitSurvey() throws Exception {
		
		String userId = "UserID";
		service.submitSurvey(userId, surveyData);
		Mockito.verify(modelService,Mockito.times(2)).save(b2bUnitModel);
	}
	
	@Test
	public void testSubmitSurvey2() throws Exception {
		
		when(surveyData.isSurveyEdit()).thenReturn(false);
		when(b2bCustomer.getDefaultB2BUnit()).thenReturn(null);
		when(surveyData.getAddress()).thenReturn(null);
		String userId = "UserID";
		service.submitSurvey(userId, surveyData);
		Mockito.verify(modelService).save(b2bUnitModel);
	}
}

package com.gp.commerce.facades.customer.survey.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.gp.commerce.core.model.GPSurveyModel;
import com.gp.commerce.core.model.GPSurveyOptionModel;
import com.gp.commerce.core.model.GPSurveyOptionResponseModel;
import com.gp.commerce.core.model.GPSurveyQuestionModel;
import com.gp.commerce.core.model.GPSurveyQuestionResponseModel;
import com.gp.commerce.core.model.GPSurveyResponseModel;
import com.gp.commerce.core.services.survey.GPSurveyService;
import com.gp.commerce.facades.survey.data.OptionData;
import com.gp.commerce.facades.survey.data.QuestionData;
import com.gp.commerce.facades.user.data.SurveyRegisterData;

@UnitTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(
{ Config.class })
@PowerMockIgnore(
{ "org.apache.logging.log4j.*" })
public class GPSurveyFacadeImplTest {

	@InjectMocks
	GPSurveyFacadeImpl facade=new GPSurveyFacadeImpl();

	@Mock
	private GPSurveyService gpSurveyService;
	@Mock
	UserService userService;
	@Mock
	private ModelService modelService;
	@Mock
	private Populator<AddressData, AddressModel> addressReversePopulator;
	@Mock
	BaseSiteService baseSiteService;
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private Configuration config;
	
	@Mock 
	BaseSiteModel baseSite;
	@Mock
	private Converter<GPSurveyModel, SurveyRegisterData> gpSurveyConverter;

	List<GPSurveyOptionModel> surveyOption=new ArrayList<>();
	List<GPSurveyQuestionModel> surveyQuestions=new ArrayList<>();
	GPSurveyModel survey=mock(GPSurveyModel.class);
	GPSurveyModel survey1=mock(GPSurveyModel.class);


	GPSurveyOptionModel option = mock(GPSurveyOptionModel.class);
	GPSurveyOptionModel option1 = mock(GPSurveyOptionModel.class);
	GPSurveyQuestionModel question = mock(GPSurveyQuestionModel.class);
	GPSurveyQuestionModel question1 = mock(GPSurveyQuestionModel.class);

	B2BCustomerModel b2bCustomer =mock(B2BCustomerModel.class);
	B2BUnitModel unit=mock(B2BUnitModel.class);
	AddressModel address=mock(AddressModel.class);

	@Mock
	private GPSurveyOptionResponseModel op;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		
		option.setCode("o1");
		option.setOptionDescription("test");
		option.setReDirectQuestion(question1);

		option1.setCode("o2");
		option1.setOptionDescription("test");
		option1.setReDirectQuestion(question);

		surveyOption.add(option);

		question.setCode("Q1");
		question.setGpSurveyOption(surveyOption);

		surveyOption.add(option1);
		question1.setCode("Q2");
		question.setGpSurveyOption(surveyOption);

		surveyQuestions.add(question);
		surveyQuestions.add(question1);

		survey.setGpSurveyQuestions(surveyQuestions);
		survey.setOfflineDate(new Date());
		survey.setOnlineDate(new Date());
		survey1.setOfflineDate(new Date());
		survey1.setOnlineDate(new Date());
		final List<GPSurveyModel> gpSurveys= new ArrayList();
		gpSurveys.add(survey);
		gpSurveys.add(survey1);
		when(gpSurveyService.getSurveyOptions()).thenReturn(surveyOption);
		when(gpSurveyService.getSurveyQuestions()).thenReturn(surveyQuestions);

		when(option.getReDirectQuestion()).thenReturn(question1);
		when(question.getCode()).thenReturn("Q1");
		when(question.getGpSurveyOption()).thenReturn(surveyOption);
		when(question1.getCode()).thenReturn("Q2");
		when(question1.getGpSurveyOption()).thenReturn(surveyOption);
		when(configurationService.getConfiguration()).thenReturn(config);
		when(config.getString(Mockito.anyString())).thenReturn("L1");
	}
	@Test
	public void testGetSurveyOptions() {
		final List<GPSurveyModel> gpSurveys1= new ArrayList<GPSurveyModel>();
		final GPSurveyModel gpSurveyModel = Mockito.mock(GPSurveyModel.class);
		Mockito.when(survey.getOfflineDate()).thenReturn(new Date());
		Mockito.when(survey.getOnlineDate()).thenReturn(new Date());
		Mockito.when(survey1.getOfflineDate()).thenReturn(new Date());
		Mockito.when(survey1.getOnlineDate()).thenReturn(new Date());
		gpSurveys1.add(survey);
		gpSurveys1.add(survey1);
		gpSurveys1.add(gpSurveyModel);
		when(baseSiteService.getCurrentBaseSite()).thenReturn(baseSite);
		facade.setGpSurveyConverter(gpSurveyConverter);
	    when(baseSite.getGpSurveys()).thenReturn(gpSurveys1);
		assertNull(facade.getSurveyOptions());
	}

	@Test
	public void testGetEmptySurveyOptions() {
		when(gpSurveyService.getSurveyQuestions()).thenReturn(null);

		assertNull(facade.getSurveyOptions());
	}

	@Test
	public void submitSurveyTest() {
		final SurveyRegisterData surveyData = new SurveyRegisterData();
		final AddressData addressData = new AddressData();

		final CountryData countryData = new CountryData();
		countryData.setIsocode("US");
		countryData.setName("United States");
		addressData.setCountry(countryData);

		final RegionData regionData = new RegionData();
		regionData.setCountryIso("US");
		regionData.setIsocode("US-IL");
		regionData.setIsocodeShort("IL");
		regionData.setName("Illinois");

		addressData.setRegion(regionData);

		addressData.setFirstName("Lars");
		addressData.setLastName("Bauer");
		addressData.setBillingAddress(true);
		addressData.setLine1("151 N Michigan");
		addressData.setPostalCode("606066");
		addressData.setTown("CHICAGO");
		addressData.setPhone("(432) 243-2434");
		addressData.setCompanyName("Rustic");

		final List<QuestionData> que = new ArrayList<QuestionData>();
		final List<OptionData> opt = new ArrayList<OptionData>();
		for(final QuestionData q: que) {
			for(final OptionData o: q.getOptions()) {
				o.setCode("O1-Q1");
				o.setLabel("L2");
				o.setReDirectQuestion("Q1");
				opt.add(o);
			}
			q.setOptions(opt);
			q.setCode("Q1-S1");
			que.add(q);
		}
		surveyData.setQuestionSelected(que);


		surveyData.setUserId("lars.bauer@rustic-hw.com");
		surveyData.setRole("developer");
		surveyData.setCompanyDetails("Software Solutions Company");
		surveyData.setTypeOfBusiness("Consulting and Services");
		surveyData.setNoOfEmployees(20);
		surveyData.setAddress(addressData);

		when(userService.getUserForUID("test@test.com")).thenReturn(b2bCustomer);
		when(modelService.create(B2BUnitModel.class)).thenReturn(unit);
		when(modelService.create(AddressModel.class)).thenReturn(address);
		doNothing().when(modelService).save(unit);

		facade.submitSurvey("test@test.com", surveyData);

		assertNotNull(surveyData);
	}


	@Test
	public void getSurveyTest() {
		PowerMockito.mockStatic(Config.class);
		final String baseSiteId = "gppro";
		final String userId = "test@test.com";
		final B2BCustomerModel customer = new B2BCustomerModel();
		final B2BUnitModel unitModel = new B2BUnitModel();
		final List<GPSurveyQuestionResponseModel> gpSurveyQuestionResponseModel = new ArrayList<>();
		final List<GPSurveyOptionResponseModel> options = new ArrayList<>();
		when(op.getCode()).thenReturn("question-answer");
		when(op.getOptionDescription()).thenReturn("Option description");

		options.add(op);
		final GPSurveyQuestionResponseModel gpsq = new GPSurveyQuestionResponseModel();
		gpsq.setGpSurveyOption(options);
		gpSurveyQuestionResponseModel.add(gpsq);
		final GPSurveyResponseModel gpSurveyResponseModel = new GPSurveyResponseModel();
		gpSurveyResponseModel.setCode("test-rest");
		gpSurveyResponseModel.setGpSurveyResponseQuestion(gpSurveyQuestionResponseModel);
		unitModel.setSubmittedSurvey(gpSurveyResponseModel);
		customer.setDefaultB2BUnit(unitModel);
		Mockito.when(Config.getParameter("gpcommercewebservices.user.delimiter")).thenReturn("|");
		when(userService.getUserForUID(Mockito.anyString())).thenReturn(customer);
		final SurveyRegisterData data = facade.getSurvey(baseSiteId, userId);
		assertNotNull(data.getSurveyCode());
		assertTrue("rest".equalsIgnoreCase(data.getSurveyCode()));
		assertTrue(data.getQuestionSelected().size() == 1);
		assertTrue("question".equalsIgnoreCase(data.getQuestionSelected().get(0).getCode()));
		assertTrue(data.getQuestionSelected().get(0).getOptions().size() == 1);
		assertTrue("answer".equalsIgnoreCase(data.getQuestionSelected().get(0).getOptions().get(0).getCode()));
	}

}

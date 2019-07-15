/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.survey.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.gp.commerce.core.company.services.GPB2BUnitsService;
import com.gp.commerce.core.model.GPSurveyModel;
import com.gp.commerce.core.model.GPSurveyOptionModel;
import com.gp.commerce.core.model.GPSurveyOptionResponseModel;
import com.gp.commerce.core.model.GPSurveyQuestionModel;
import com.gp.commerce.core.model.GPSurveyQuestionResponseModel;
import com.gp.commerce.core.model.GPSurveyResponseModel;
import com.gp.commerce.core.services.survey.GPSurveyService;
import com.gp.commerce.core.survey.dao.GPSurveyDAO;
import com.gp.commerce.facades.survey.data.OptionData;
import com.gp.commerce.facades.survey.data.QuestionData;
import com.gp.commerce.facades.user.data.SurveyRegisterData;

/**
 * @author dgandabonu
 *
 *         class used for displaying survey questions and options in B2B customer survey page.
 */
public class GPSurveyServiceImpl implements GPSurveyService {


	private static final String L1 = "b2b.unit.level.l1";
	private static final String L2 = "b2b.unit.level.l2";
	private static final String HYPHEN = "-";

	private GPSurveyDAO gpSurveyDAO;

	@Resource(name = "configurationService")
	protected ConfigurationService configurationService;

	@Resource(name = "gpB2BUnitsService")
	protected GPB2BUnitsService<B2BUnitModel, UserModel> gpB2BUnitsService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "addressReversePopulator")
	private Populator<AddressData, AddressModel> addressReversePopulator;

	/**
	 * Get All survey Options
	 * @return GPSurveyOptionModel
	 */
	@Override
	public List<GPSurveyOptionModel> getSurveyOptions() {

		return getGpSurveyDAO().getSurveyOptions();
	}

	/**
	 * Get All survey Question
	 * @return GPSurveyQuestionModel
	 */
	@Override
	public List<GPSurveyQuestionModel> getSurveyQuestions() {

		return getGpSurveyDAO().getSurveyQuestions();
	}
	/**
	 * Get Survey Model
	 * @return GPSurveyModel
	 */
	@Override
	public GPSurveyModel getSurveyData(final String surveyCode){

		return getGpSurveyDAO().getSurveyData(surveyCode);

	}
	public GPSurveyDAO getGpSurveyDAO() {
		return gpSurveyDAO;
	}

	public void setGpSurveyDAO(final GPSurveyDAO gpSurveyDAO) {
		this.gpSurveyDAO = gpSurveyDAO;
	}

	@Override
	public void submitSurvey(final String userId, final SurveyRegisterData surveyData)
	{

		final B2BCustomerModel b2bCustomer = (B2BCustomerModel) userService.getUserForUID(userId);
		B2BUnitModel unitModel = null;
		final GPSurveyResponseModel sResponse = modelService.create(GPSurveyResponseModel.class);
		if (null != b2bCustomer.getDefaultB2BUnit())
		{
			unitModel = b2bCustomer.getDefaultB2BUnit();
		}
		else
		{
			unitModel = modelService.create(B2BUnitModel.class);
		}
		if (StringUtils.isNoneBlank(surveyData.getSurveyCode()))
		{
			if (b2bCustomer.getPrimaryAdmin())
			{

				final GPSurveyModel model = getSurveyData(surveyData.getSurveyCode());
				final List<String> questionCodes = surveyData.getQuestionSelected().stream().map(q -> q.getCode())
						.collect(Collectors.toList());
				final List<String> optionCodes = new ArrayList<>();
				for (final QuestionData d : surveyData.getQuestionSelected())
				{
					for (final OptionData code : d.getOptions())
					{
						optionCodes.add(code.getCode());
					}
				}

				final List<GPSurveyQuestionResponseModel> questionResponses = new ArrayList<>();
				final List<GPSurveyOptionResponseModel> allOptionResponses = new ArrayList<>();

				for (final GPSurveyQuestionModel question : model.getGpSurveyQuestions())
				{
					if (questionCodes.contains(question.getCode()))
					{
						final GPSurveyQuestionResponseModel questionResponse = modelService.create(GPSurveyQuestionResponseModel.class);
						questionResponse
								.setCode(surveyData.getSurveyCode() + HYPHEN + question.getCode() + HYPHEN + System.currentTimeMillis());
						questionResponse.setQuestionDescription(question.getQuestionDescription());
						questionResponse.setQuestionType(question.getQuestionType());

						if ("TEXT_FIELD".equalsIgnoreCase(question.getQuestionType().getCode()))
						{
							final List<GPSurveyOptionResponseModel> optionResponses = new ArrayList<>();
							final GPSurveyOptionResponseModel optionModel = modelService.create(GPSurveyOptionResponseModel.class);

							for (final QuestionData d : surveyData.getQuestionSelected())
							{
								if (d.getCode().equals(question.getCode()))
								{

									for (final OptionData optionData : d.getOptions())
									{
										optionModel.setCode(
												question.getCode() + HYPHEN + optionData.getCode() + HYPHEN + System.currentTimeMillis());
										optionModel.setOptionDescription(optionData.getCode());
									}
									break;
								}

							}



							optionResponses.add(optionModel);
							questionResponse.setGpSurveyOption(optionResponses);
						}


						final List<GPSurveyOptionModel> options = question.getGpSurveyOption().stream()
								.filter(oCode -> optionCodes.contains(oCode.getCode())).collect(Collectors.toList());
						if (CollectionUtils.isNotEmpty(options))
						{
							final List<GPSurveyOptionResponseModel> optionResponses = new ArrayList<>();
							for (final GPSurveyOptionModel option : options)
							{
								final GPSurveyOptionResponseModel optionModel = modelService.create(GPSurveyOptionResponseModel.class);
								optionModel.setCode(question.getCode() + HYPHEN + option.getCode() + HYPHEN + System.currentTimeMillis());
								optionModel.setOptionDescription(option.getOptionDescription());
								optionModel.setReDirectQuestion(option.getReDirectQuestion());
								optionModel.setLevel(option.getLevel());
								optionResponses.add(optionModel);
							}
							questionResponse.setGpSurveyOption(optionResponses);
							allOptionResponses.addAll(optionResponses);
						}
						questionResponses.add(questionResponse);

					}
				}
				sResponse.setCode(b2bCustomer.getUid() + HYPHEN + surveyData.getSurveyCode() + HYPHEN + System.currentTimeMillis());
				sResponse.setGpSurveyResponseQuestion(questionResponses);
				modelService.saveAll();

				unitModel.setB2bUnitLevel(calculateB2bUnitLevel(allOptionResponses));
			}
		}
		else
		{
			unitModel.setB2bUnitLevel(getProperty(L1));
		}

		AddressModel addressModel;
		if (surveyData.isSurveyEdit() && !unitModel.getAddresses().isEmpty())
		{
			addressModel = unitModel.getAddresses().iterator().next();
			addressReversePopulator.populate(surveyData.getAddress(), addressModel);
			modelService.save(addressModel);
		}
		else
		{
			addressModel = modelService.create(AddressModel.class);
			addressReversePopulator.populate(surveyData.getAddress(), addressModel);
			addressModel.setOwner(unitModel);
		}

		unitModel.setName(getCompanyName(surveyData));
		unitModel.setLocName(getCompanyName(surveyData));
		unitModel.setId(getCompanyName(surveyData));
		unitModel.setContactAddress(addressModel);
		unitModel.setNoOfEmployees(surveyData.getNoOfEmployees());
		unitModel.setRole(surveyData.getRole());
		unitModel.setTypeOfBusiness(surveyData.getTypeOfBusiness());
		if (null != sResponse.getCode())
		{
			unitModel.setSubmittedSurvey(sResponse);
		}
		unitModel.setSurveySubmittedBy(b2bCustomer);

		if (surveyData.isSurveyEdit())
		{
			final List<B2BUnitModel> units = gpB2BUnitsService.getUnitsWithChildNodes(b2bCustomer, true);
			for (final B2BUnitModel unit : units)
			{
				unit.setB2bUnitLevel(unitModel.getB2bUnitLevel());
				modelService.save(unit);
			}

		}
		modelService.save(unitModel);
	}

	/**
	 * calculate B2b Unit levels based on Survey options
	 *
	 * @param List<GPSurveyOptionModel>
	 *           options
	 */
	private String calculateB2bUnitLevel(final List<GPSurveyOptionResponseModel> options)
	{
		final List<String> levels = options.stream().map(data -> data.getLevel()).collect(Collectors.toList());
		final int countLevel2 = Collections.frequency(levels, getProperty(L2));
		final int countLevel1 = Collections.frequency(levels, getProperty(L1));
		return countLevel2 > countLevel1 ? getProperty(L2) : getProperty(L1);

	}

	/**
	 * Gets the property.
	 *
	 * @param property
	 *           the property
	 * @return the property
	 */
	private String getProperty(final String property)
	{
		return configurationService.getConfiguration().getString(property);
	}

	private String getCompanyName(final SurveyRegisterData surveyData)
	{
		if (null != surveyData.getAddress())
		{
			return null != surveyData.getAddress().getCompanyName() ? surveyData.getAddress().getCompanyName() : "b2bUnit";
		}
		return "b2bUnit";
	}

}

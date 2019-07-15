/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.customer.survey.impl;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.Instant;

import com.gp.commerce.core.company.services.impl.GPDefaultB2BUnitsService;
import com.gp.commerce.core.model.GPSurveyModel;
import com.gp.commerce.core.model.GPSurveyResponseModel;
import com.gp.commerce.core.services.survey.GPSurveyService;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gp.commerce.facades.customer.survey.GPSurveyFacade;
import com.gp.commerce.facades.populators.GPSurveyResponsePopulator;
import com.gp.commerce.facades.user.data.SurveyRegisterData;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;


public class GPSurveyFacadeImpl implements GPSurveyFacade
{

	private ConfigurationService configurationService;
	private GPSurveyService gpSurveyService;
	private BaseSiteService baseSiteService;
	private ModelService modelService;
	private UserService userService;
	private Populator<AddressData, AddressModel> addressReversePopulator;
	private Converter<GPSurveyModel, SurveyRegisterData> gpSurveyConverter;
	@Resource(name = "gpB2BUnitsService")
	private GPDefaultB2BUnitsService gpDefaultB2BUnitsService;
	@Resource(name = "gpSurveyResponsePopulator")
	private GPSurveyResponsePopulator gpSurveyResponsePopulator;
	/**
	 * Get Survey Question and options
	 *
	 * @return QuestionListData
	 */
	@Override
	public SurveyRegisterData getSurveyOptions()
	{
		final BaseSiteModel baseSite = getBaseSiteService().getCurrentBaseSite();
		if (null != baseSite && CollectionUtils.isNotEmpty(baseSite.getGpSurveys()))
		{
			final GPSurveyModel survey = getActiveSurvey(baseSite.getGpSurveys());
			return getGpSurveyConverter().convert(survey);
		}

		return null;
	}

	private GPSurveyModel getActiveSurvey(final List<GPSurveyModel> gpSurveys)
	{
		final Predicate<GPSurveyModel> p = survey -> {
			if (null != survey.getOnlineDate() && null != survey.getOfflineDate())
			{
				return Instant.now().toDate().after(survey.getOnlineDate()) && Instant.now().toDate().before(survey.getOnlineDate()) ? true : false;
			}
			return false;
		};

		GPSurveyModel survey = null;
		survey = gpSurveys.stream().filter(p).findFirst().orElse(null);
		if (null == survey)
		{
			gpSurveys.stream().filter(s -> s.isActive()).findFirst().orElse(null);
		}
		if (null == survey)
		{
			survey = gpSurveys.get(0);
		}
		return survey;
	}

	/**
	 * update B2b Unit details , address , company details address and b2bUnitLevel
	 *
	 * @param userId
	 * @param surveyData
	 */
	@Override
	public void submitSurvey(final String userId, final SurveyRegisterData surveyData)
	{
		getGpSurveyService().submitSurvey(userId, surveyData);
	}


	/**
	 * Use getSurvey method for survey details submitted by the user
	 *
	 * @param baseSiteId
	 * @param userId
	 * @return SurveyRegisterData
	 */
	public SurveyRegisterData getSurvey(final String baseSiteId, final String userId)
	{
		final String splitter = Config.getParameter(GpcommerceFacadesConstants.GPWEBSER_DELIMETER);
		final String customerId = userId + splitter + baseSiteId;
		final B2BCustomerModel b2bCustomer = (B2BCustomerModel) getUserService().getUserForUID(customerId);
		B2BUnitModel unitModel = getGpDefaultB2BUnitsService().getB2bUnitForCustomer(b2bCustomer);

		final GPSurveyResponseModel gpSurveyResponseModel = unitModel.getSubmittedSurvey();
		final SurveyRegisterData surveyRegisterData = new SurveyRegisterData();
		getGpSurveyResponsePopulator().populate(gpSurveyResponseModel, surveyRegisterData);
		return surveyRegisterData;
	}

	public GPSurveyService getGpSurveyService()
	{
		return gpSurveyService;
	}

	public void setGpSurveyService(final GPSurveyService gpSurveyService)
	{
		this.gpSurveyService = gpSurveyService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public Populator<AddressData, AddressModel> getAddressReversePopulator()
	{
		return addressReversePopulator;
	}

	public void setAddressReversePopulator(final Populator<AddressData, AddressModel> addressReversePopulator)
	{
		this.addressReversePopulator = addressReversePopulator;
	}

	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	public Converter<GPSurveyModel, SurveyRegisterData> getGpSurveyConverter()
	{
		return gpSurveyConverter;
	}

	public void setGpSurveyConverter(final Converter<GPSurveyModel, SurveyRegisterData> gpSurveyConverter)
	{
		this.gpSurveyConverter = gpSurveyConverter;
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
	
	public GPDefaultB2BUnitsService getGpDefaultB2BUnitsService() {
		return gpDefaultB2BUnitsService;
	}

	public void setGpDefaultB2BUnitsService(GPDefaultB2BUnitsService gpDefaultB2BUnitsService) {
		this.gpDefaultB2BUnitsService = gpDefaultB2BUnitsService;
	}
	
	public GPSurveyResponsePopulator getGpSurveyResponsePopulator() {
		return gpSurveyResponsePopulator;
	}

	public void setGpSurveyResponsePopulator(GPSurveyResponsePopulator gpSurveyResponsePopulator) {
		this.gpSurveyResponsePopulator = gpSurveyResponsePopulator;
	}
}

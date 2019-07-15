/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.v2.controller;

import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gp.commerce.facades.customer.survey.GPSurveyFacade;
import com.gp.commerce.facades.user.data.SurveyRegisterData;
import com.gp.commerce.user.data.SurveyRegisterWsDTO;

import io.swagger.annotations.Api;


@Controller
@RequestMapping(value = "/{baseSiteId}/survey")
@CacheControl(directive = CacheControlDirective.PRIVATE)
@Api(tags = "Survey")
public class SurveyController extends BaseController
{

	private static final String COPIED_FIELDS = "surveyCode,userId,role,companyDetails,typeOfBusiness,noOfEmployees,questionSelected,address(companyName,line1,line2,town,postalCode,phone,country(isocode),region(isocode)), surveyEdit";

	@Resource(name = "gpSurveyFacade")
	private GPSurveyFacade gpSurveyFacade;

	@Secured(
	{ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_B2BADMINGROUP" })
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public SurveyRegisterWsDTO getSurveyQuestionOptions(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{

		final SurveyRegisterData survey = gpSurveyFacade.getSurveyOptions();
		return getDataMapper().map(survey, SurveyRegisterWsDTO.class, fields);
	}

	@Secured(
	{ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_B2BADMINGROUP" })
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void submitCompanyAndServeyDetails(@PathVariable final String baseSiteId, @RequestBody final SurveyRegisterWsDTO survey,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final SurveyRegisterData surveyData = getDataMapper().map(survey, SurveyRegisterData.class, COPIED_FIELDS);
		final String splitter = Config.getParameter("gpcommercewebservices.user.delimiter");
		gpSurveyFacade.submitSurvey(surveyData.getUserId() + splitter + baseSiteId, surveyData);
	}

	/*
	 * getSurvey - Gets submitted survey details by user
	 */
	@Secured(
	{ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_B2BADMINGROUP" })
	@RequestMapping(value = "/getsurvey", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public SurveyRegisterWsDTO getSurveyDetails(@PathVariable final String baseSiteId,
			@RequestBody final SurveyRegisterWsDTO survey)
	{
		return getDataMapper().map(gpSurveyFacade.getSurvey(baseSiteId, survey.getUserId()), SurveyRegisterWsDTO.class);
	}
}

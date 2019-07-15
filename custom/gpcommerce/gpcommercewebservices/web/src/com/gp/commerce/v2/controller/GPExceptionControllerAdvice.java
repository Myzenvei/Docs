/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.v2.controller;

import com.gp.commerce.core.exceptions.*;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;

import org.apache.commons.lang.StringUtils;

@ControllerAdvice
public class GPExceptionControllerAdvice {

	private static final Logger LOG = Logger.getLogger(GPExceptionControllerAdvice.class);
	@Resource(name = "messageSource")
	private MessageSource messageSource;
	@Resource(name = "i18NService")
	private I18NService i18NService;

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorListWsDTO> exceptionHandler(final Exception ex) {
		LOG.error("Exception: ", ex);

		final ErrorListWsDTO errorListWsDTO = new ErrorListWsDTO();
		final List<ErrorWsDTO> errors = new ArrayList<>();
		final boolean isSecurityException = isSecurityException(ex);
		final boolean isNoDataException = Stream.of(Config.getParameter("security.exception").split(",")).anyMatch(d -> d.equalsIgnoreCase(ex.getClass().getSimpleName()));
		final boolean isSaveException = Stream.of(Config.getParameter("security.exception").split(",")).anyMatch(d -> d.equalsIgnoreCase(ex.getClass().getSimpleName()));
		final boolean isPromotionException = Stream.of(Config.getParameter("promotion.exception").split(",")).anyMatch(d -> d.equalsIgnoreCase(ex.getClass().getSimpleName()));
		final boolean isPromotionCodeException = Stream.of(Config.getParameter("security.exception").split(",")).anyMatch(d -> d.equalsIgnoreCase(ex.getClass().getSimpleName()));
		final boolean emailException = Stream.of(Config.getParameter("security.exception").split(",")).anyMatch(d -> d.equalsIgnoreCase(ex.getClass().getSimpleName()));
		final boolean isUniqueException=Stream.of(Config.getParameter("security.exception").split(",")).anyMatch(d -> d.equalsIgnoreCase(ex.getClass().getSimpleName()));
		final boolean isDataException = Stream.of(Config.getParameter("security.exception").split(",")).anyMatch(d -> d.equalsIgnoreCase(ex.getClass().getSimpleName()));

		final ErrorWsDTO error = new ErrorWsDTO();
		if (isSecurityException(ex) || ex instanceof GPCommerceSecurityException) {
			if(isSecurityException) {
				error.setCode("100");
			}else {
				error.setCode(((GPException)ex).getCode());
			}
			error.setMessage(ex.getMessage());
			errors.add(error);
			errorListWsDTO.setErrors(errors);
			return new ResponseEntity<>(errorListWsDTO, HttpStatus.CONFLICT);
		}else if (ex instanceof GPCommerceSaveDataException || isSaveException) {
			if(isSecurityException) {
				error.setCode("195");
			}else {
				error.setCode(((GPException)ex).getCode());
			}
			error.setMessage(ex.getMessage());
			errors.add(error);
			errorListWsDTO.setErrors(errors);
			return new ResponseEntity<>(errorListWsDTO, HttpStatus.EXPECTATION_FAILED);
		}else if (ex instanceof GPPromotionExpiredException || isPromotionException) {
			if(isSecurityException) {
				error.setCode("102");
			}else {
				error.setCode(((GPException)ex).getCode());
			}
			error.setMessage(ex.getMessage());
			errors.add(error);
			errorListWsDTO.setErrors(errors);
			return new ResponseEntity<>(errorListWsDTO, HttpStatus.NOT_FOUND);
		}else if (ex instanceof GPInvalidPromotionException || isPromotionCodeException) {
			if(isSecurityException) {
				error.setCode("103");
			}else {
				error.setCode(((GPException)ex).getCode());
			}
			error.setMessage(ex.getMessage());
			errors.add(error);
			errorListWsDTO.setErrors(errors);
			return new ResponseEntity<>(errorListWsDTO, HttpStatus.NOT_FOUND);
		}else if (ex instanceof GPCommerceEmailException || emailException) {
			if(isSecurityException) {
				error.setCode("104");
			}else {
				error.setCode(((GPException)ex).getCode());
			}
			error.setMessage(ex.getMessage());
			errors.add(error);
			errorListWsDTO.setErrors(errors);
			return new ResponseEntity<>(errorListWsDTO, HttpStatus.FAILED_DEPENDENCY);
		}else if (ex instanceof GPCommerceDataException || isDataException) {
			if(isSecurityException) {
				error.setCode("105");
			}else {
				error.setCode(((GPException)ex).getCode());
			}
			error.setMessage(ex.getMessage());
			errors.add(error);
			errorListWsDTO.setErrors(errors);
			return new ResponseEntity<>(errorListWsDTO, HttpStatus.NOT_FOUND);
		}else if (ex instanceof GPCommerceNoDataException || isNoDataException) {
			if(isSecurityException) {
				error.setCode("106");
			}else {
				error.setCode(((GPException)ex).getCode());
			}
			error.setMessage(ex.getMessage());
			errors.add(error);
			errorListWsDTO.setErrors(errors);
			return new ResponseEntity<>(errorListWsDTO, HttpStatus.NOT_FOUND);
		}else if (ex instanceof GPCommerceUniqueUIDException || isUniqueException) {
			if(isSecurityException) {
				error.setCode("107");
			}else {
				error.setCode(((GPException)ex).getCode());
			}
			error.setMessage(ex.getMessage());
			errors.add(error);
			errorListWsDTO.setErrors(errors);
			return new ResponseEntity<>(errorListWsDTO, HttpStatus.CONFLICT);
		}
		else if (ex instanceof GPCommerceBusinessException)
		{
			if(StringUtils.isNotBlank(((GPCommerceBusinessException) ex).getCode()))
			{
				error.setCode(((GPCommerceBusinessException) ex).getCode());
			}
			else
			{
			error.setCode("108");
			}
			error.setMessage(ex.getMessage());
			errors.add(error);
			errorListWsDTO.setErrors(errors);
			return new ResponseEntity<>(errorListWsDTO, HttpStatus.CONFLICT);
		}
		else if (ex instanceof WebserviceValidationException)
		{
			error.setCode("113");
			error.setMessage((String)((WebserviceValidationException)ex).getValidationObject());
			errors.add(error);
			errorListWsDTO.setErrors(errors);
			return new ResponseEntity<>(errorListWsDTO, HttpStatus.NOT_ACCEPTABLE);
		
		}
		else if(ex instanceof GPMaximumStockException) {
			error.setCode("197");
			error.setMessage(ex.getMessage());
			errors.add(error);
			errorListWsDTO.setErrors(errors);
			return new ResponseEntity<>(errorListWsDTO, HttpStatus.NOT_ACCEPTABLE);
		}else if(ex instanceof GPLowStockException) {
			error.setCode("198");
			error.setMessage(ex.getMessage());
			errors.add(error);
			errorListWsDTO.setErrors(errors);
			return new ResponseEntity<>(errorListWsDTO, HttpStatus.NOT_ACCEPTABLE);
		}else if(ex instanceof GPSampleCartException) {
			error.setCode(GpcommerceCoreConstants.SAMPLECART_ERROR_CODE);
			error.setMessage(ex.getMessage());
			errors.add(error);
			errorListWsDTO.setErrors(errors);
			return new ResponseEntity<>(errorListWsDTO, HttpStatus.NOT_ACCEPTABLE);
		}else if(ex.getCause() instanceof GPInsufficientStockLevelException) {
			error.setCode("199");
			error.setMessage(ex.getMessage());
			errors.add(error);
			errorListWsDTO.setErrors(errors);
			return new ResponseEntity<>(errorListWsDTO, HttpStatus.NOT_ACCEPTABLE);
			
		} else if (ex.getCause() instanceof GPCommercePasswordException) {
			error.setCode("200");
			error.setMessage(ex.getMessage());
			errors.add(error);
			errorListWsDTO.setErrors(errors);
			return new ResponseEntity<>(errorListWsDTO, HttpStatus.NOT_ACCEPTABLE);
		}else if (ex instanceof GPIllegalArgumentException) {
			error.setCode("1000");
			error.setMessage(ex.getMessage());
			errors.add(error);
			errorListWsDTO.setErrors(errors);
			return new ResponseEntity<>(errorListWsDTO, HttpStatus.NOT_ACCEPTABLE);

		}else if (ex instanceof GPTicketException){
				error.setCode("188");
				error.setMessage(ex.getMessage());
				errors.add(error);
				errorListWsDTO.setErrors(errors);
				return new ResponseEntity<>(errorListWsDTO, HttpStatus.NOT_ACCEPTABLE);
		}else
		{
				error.setCode("109");
				error.setMessage(ex.getMessage());
				errors.add(error);
				errorListWsDTO.setErrors(errors);
				return new ResponseEntity<>(errorListWsDTO, HttpStatus.NOT_FOUND);
		}
	}

	private boolean isSecurityException(Exception ex) {
	return Stream.of(Config.getParameter("security.exception").split(",")).anyMatch(d -> d.equalsIgnoreCase(ex.getClass().getSimpleName()));
	}

}

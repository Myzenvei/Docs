/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.v2.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

import com.gp.commerce.dto.cybersource.GPSignatureDTO;
import com.gp.commerce.exceptions.InvalidPaymentInfoException;
import com.gp.commerce.exceptions.NoCheckoutCartException;
import com.gp.commerce.exceptions.UnsupportedRequestException;
import com.gp.commerce.swagger.ApiBaseSiteIdUserIdAndCartIdParam;
import com.gpintegration.service.impl.DefaultGPCommerceCybersourceIntegrationService;
import com.gpintegration.service.impl.DefaultGPCyberSourceSignatureService;
import com.gpintegration.utils.GPSignaturePojo;
import com.gpintegration.utils.GPSignatureResponseDTO;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Secured(
{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT","ROLE_ASAGENTSALESMANAGERGROUP" ,"ROLE_CLIENT","ROLE_GUEST"})
@Controller
@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 1800)
@Api(tags = "Payments")
public class PaymentsController extends BaseCommerceController{
	@Resource(name = "signatureService")
	DefaultGPCyberSourceSignatureService signatureService;
	@Resource(name = "gpCyberSource")
	DefaultGPCommerceCybersourceIntegrationService gpCyberSource;
	private static String AUTH_AMOUNT = "0.00";
	
	
	@RequestMapping(value = "/{baseSiteId}/paymentsignature", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public GPSignatureResponseDTO generateSignature(@PathVariable final String baseSiteId, @RequestBody final GPSignatureDTO signature, // NOSONAR
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws WebserviceValidationException, InvalidPaymentInfoException,
			UnsupportedRequestException // NOSONAR
	{
		return signatureService.getTheSignature(createRequest(signature), baseSiteId);
	}
	
	private GPSignaturePojo createRequest(GPSignatureDTO signatureDTO) {
		GPSignaturePojo pojo =new GPSignaturePojo();
		pojo.setAmount(AUTH_AMOUNT);
		pojo.setBillToAddressCity(signatureDTO.getBill_to_address_city());
		pojo.setBillToAddressState(signatureDTO.getBill_to_address_state());
		pojo.setBillToAddressCountry(signatureDTO.getBill_to_address_country());
		pojo.setBillToAddressLine1(signatureDTO.getBill_to_address_line1());
		pojo.setBillToAddressPostalCode(signatureDTO.getBill_to_address_postal_code());
		pojo.setBillToEmail(signatureDTO.getBill_to_email());
		pojo.setBillToForename(signatureDTO.getBill_to_forename());
		pojo.setBillToPhone(signatureDTO.getBill_to_phone());
		pojo.setBillToSurname(signatureDTO.getBill_to_surname());
		pojo.setPaymentMethod(signatureDTO.getPayment_method());
		pojo.setReferenceNumber(signatureDTO.getReference_number());
		pojo.setTransactionType(signatureDTO.getTransaction_type());
		pojo.setSignedFieldNames(signatureDTO.getSigned_field_names());
		pojo.setUnsignedFieldNames(signatureDTO.getUnsigned_field_names());
		pojo.setSignedDateTime(signatureDTO.getSigned_date_time());
		pojo.setPayment_token(signatureDTO.getPayment_token());
		
		return pojo;
	}

}

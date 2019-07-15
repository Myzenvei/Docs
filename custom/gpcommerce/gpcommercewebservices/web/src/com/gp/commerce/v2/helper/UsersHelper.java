/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.v2.helper;

import de.hybris.platform.commercefacades.address.AddressVerificationFacade;
import de.hybris.platform.commercefacades.address.data.AddressVerificationResult;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.address.AddressVerificationDecision;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commercewebservicescommons.dto.user.UserWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.util.UriUtils;

import com.gp.commerce.constants.YcommercewebservicesConstants;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.facades.customer.GpCustomerFacade;
import com.gp.commerce.user.data.AddressDataList;
import com.gp.commerce.validation.data.AddressValidationData;


/**
 * Helper class for UsersController
 */
@Component
public class UsersHelper extends AbstractHelper
{
	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Resource(name = "addressVerificationFacade")
	private AddressVerificationFacade addressVerificationFacade;

	@Resource(name = "validationErrorConverter")
	private Converter<Object, List<ErrorWsDTO>> validationErrorConverter;

	@Resource(name = "addressDataErrorsPopulator")
	private Populator<AddressVerificationResult<AddressVerificationDecision>, Errors> addressDataErrorsPopulator;

	@Resource(name = "addressValidator")
	private Validator addressValidator;

	@Resource(name = "gpDefaultCustomerFacade")
	private GpCustomerFacade gpCustomerFacade;

	private static final Logger LOG = Logger.getLogger(UsersHelper.class);

	/**
	 * @param httpRequest
	 * @param uid
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getAbsoluteLocationURL(final HttpServletRequest httpRequest, final String uid)
			throws UnsupportedEncodingException
	{
		final String requestURL = httpRequest.getRequestURL().toString();
		String absoluteURLSb = requestURL;
		if (!requestURL.endsWith(YcommercewebservicesConstants.SLASH))
		{
			absoluteURLSb = absoluteURLSb + YcommercewebservicesConstants.SLASH;
		}
		absoluteURLSb = absoluteURLSb + UriUtils.encodePathSegment(uid, StandardCharsets.UTF_8.name());
		return absoluteURLSb;
	}

	/**
	 * @param login
	 * @param password
	 * @param titleCode
	 * @param firstName
	 * @param lastName
	 * @throws RequestParameterException
	 * @throws DuplicateUidException
	 */
	public void registerNewUser(final String login, final String password, final String titleCode, final String firstName,
			final String lastName) throws RequestParameterException, DuplicateUidException //NOSONAR
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("registerUser: login=" + sanitize(login));
		}
		if (!EmailValidator.getInstance().isValid(login))
		{
			throw new RequestParameterException("Login [" + sanitize(login) + "] is not a valid e-mail address!",
					RequestParameterException.INVALID, GpcommerceCoreConstants.LOGIN);
		}
		final RegisterData registration = new RegisterData();
		registration.setFirstName(firstName);
		registration.setLastName(lastName);
		registration.setLogin(login);
		registration.setPassword(password);
		registration.setTitleCode(titleCode);
		customerFacade.register(registration);
	}

	/**
	 * @param password
	 * @param guid
	 * @throws DuplicateUidException
	 */
	public void convertToCustomer(final String password, final String guid) throws DuplicateUidException //NOSONAR
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("registerUser: guid=" + sanitize(guid));
		}
		try
		{
			customerFacade.changeGuestToCustomer(password, guid);
		}
		catch (final UnknownIdentifierException ex)
		{
			throw new RequestParameterException(
					GpcommerceCoreConstants.ORDER_WITH_GUID + sanitize(guid) + GpcommerceCoreConstants.NOT_FOUND_IN_CURRENT_BASE_STORE,
					RequestParameterException.UNKNOWN_IDENTIFIER, GpcommerceCoreConstants.GUID, ex);
		}
		catch (final IllegalArgumentException ex)
		{
			throw new RequestParameterException(
					GpcommerceCoreConstants.ORDER_WITH_GUID + sanitize(guid) + GpcommerceCoreConstants.NOT_FOUND_IN_CURRENT_BASE_STORE,
					RequestParameterException.UNKNOWN_IDENTIFIER, GpcommerceCoreConstants.GUID, ex);
		}
	}

	/**
	 * @param auth
	 * @param role
	 * @return
	 */
	public boolean containsRole(final Authentication auth, final String role)
	{
		for (final GrantedAuthority ga : auth.getAuthorities())
		{
			if (ga.getAuthority().equals(role))
			{
				return true;
			}
		}
		return false;
	}

	private static String sanitize(final String input)
	{
		return YSanitizer.sanitize(input);
	}

	/**
	 * Checks if address is valid by a validators
	 *
	 * @formparam addressData
	 * @formparam errors
	 * @formparam validationData
	 * @return true - adress is valid , false - address is invalid
	 */
	public boolean isAddressValid(final AddressData addressData, final Errors errors,
			final AddressValidationData validationData)
	{
		addressValidator.validate(addressData, errors);
		if (errors.hasErrors())
		{
			validationData.setDecision(AddressVerificationDecision.REJECT.toString());
			validationData.setErrors(createResponseErrors(errors));
			return false;
		}
		return true;
	}

	/**
	 * Verifies address by commerce service
	 *
	 * @formparam addressData
	 * @formparam errors
	 * @formparam validationData
	 * @return object with verification errors and suggested addresses list
	 */
	public AddressValidationData verifyAddresByService(final AddressData addressData, final Errors errors,
			final AddressValidationData validationData)
	{
		final AddressVerificationResult<AddressVerificationDecision> verificationDecision = addressVerificationFacade
				.verifyAddressData(addressData);
		if (verificationDecision.getErrors() != null && !verificationDecision.getErrors().isEmpty())
		{
			populateErrors(errors, verificationDecision);
			validationData.setErrors(createResponseErrors(errors));
		}
		validationData.setDecision(verificationDecision.getDecision().toString());
		if (verificationDecision.getSuggestedAddresses() != null && !verificationDecision.getSuggestedAddresses().isEmpty())
		{
			final AddressDataList addressDataList = new AddressDataList();
			addressDataList.setAddresses(verificationDecision.getSuggestedAddresses());
			validationData.setSuggestedAddressesList(addressDataList);
		}
		return validationData;
	}

	public ErrorListWsDTO createResponseErrors(final Errors errors)
	{
		final List<ErrorWsDTO> webserviceErrorDto = new ArrayList<>();
		validationErrorConverter.convert(errors, webserviceErrorDto);
		final ErrorListWsDTO webserviceErrorList = new ErrorListWsDTO();
		webserviceErrorList.setErrors(webserviceErrorDto);
		return webserviceErrorList;
	}

	/**
	 * Populates Errors object
	 *
	 * @param errors
	 * @param addressVerificationResult
	 */
	protected void populateErrors(final Errors errors,
			final AddressVerificationResult<AddressVerificationDecision> addressVerificationResult)
	{
		addressDataErrorsPopulator.populate(addressVerificationResult, errors);
	}

	/**
	 * This function will get only unique payment token to avoid duplicate card details and sorted with cart attach time
	 *
	 * @param ccPaymentInfos
	 * @return
	 */
	public List<CCPaymentInfoData> getUniquePaymentInfo(final List<CCPaymentInfoData> ccPaymentInfos)
	{
		final List<String> token = new ArrayList<>();
		final List<CCPaymentInfoData> unique = new ArrayList<>();
		for (final CCPaymentInfoData cc : ccPaymentInfos)
		{
			if (!token.contains(cc.getPaymentToken()) && cc.isSaved())
			{
				token.add(cc.getPaymentToken());
				unique.add(cc);
			}
		}
		if (!unique.isEmpty())
		{
			unique.sort(Comparator.comparing(CCPaymentInfoData::getCreationTime).reversed());
		}
		return unique;
	}

	/**
	 * @param user
	 * @return
	 * @throws DuplicateUidException
	 */
	public CustomerData personalDetails(final UserWsDTO user) throws DuplicateUidException
	{
		CustomerData customerData = user.getCellPhone() != null
				? customerData = getDataMapper().map(user, CustomerData.class, GpcommerceCoreConstants.UPDATE_USER_FIELDS1)
				: getDataMapper().map(user, CustomerData.class, GpcommerceCoreConstants.UPDATE_USER_FIELDS2);
		return gpCustomerFacade.updatePersonalDetails(customerData);
	}
}

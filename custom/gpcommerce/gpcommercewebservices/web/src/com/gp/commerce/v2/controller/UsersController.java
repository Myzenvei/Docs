/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All  rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.v2.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import com.gp.commerce.core.constants.GPB2BExceptionEnum;
import com.gp.commerce.core.constants.GpErrorConstants;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.GPLoginType;
import com.gp.commerce.core.exceptions.GPCommerceBusinessException;
import com.gp.commerce.core.exceptions.GPCommerceNoDataException;
import com.gp.commerce.core.exceptions.GPCommercePasswordException;
import com.gp.commerce.core.exceptions.GPCommerceSaveDataException;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.strategies.GPDefaultCustomerNameStrategy;
import com.gp.commerce.core.util.GPSiteConfigUtils;
import com.gp.commerce.dto.PdfUserDetailsWsDTO;
import com.gp.commerce.dto.company.B2BUserGroupWsDTO;
import com.gp.commerce.dto.company.data.B2BUserGroupDataList;
import com.gp.commerce.dto.company.data.B2BUserGroupListWsDTO;
import com.gp.commerce.dto.company.data.B2BUserGroupRequestDTO;
import com.gp.commerce.facades.customer.GpCustomerFacade;
import com.gp.commerce.facades.data.PdfDownloadUserData;
import com.gp.commerce.facades.data.user.data.MarketingPreferenceDataList;
import com.gp.commerce.facades.marketing.data.UpdatePreferenceData;
import com.gp.commerce.facades.subscription.GPSubscriptionFacade;
import com.gp.commerce.facades.user.data.MarketingPreferenceQuestionAnsDataList;
import com.gp.commerce.facades.user.impl.GPDefaultUserFacade;
import com.gp.commerce.facades.usergroups.impl.GPDefaultB2BUserGroupFacade;
import com.gp.commerce.marketing.data.MarketingPreferenceListWsDTO;
import com.gp.commerce.marketing.data.MarketingPreferenceQuestionAnsListWsDTO;
import com.gp.commerce.marketing.data.UpdatePreferenceWsDTO;
import com.gp.commerce.populator.HttpRequestCustomerDataPopulator;
import com.gp.commerce.swagger.ApiBaseSiteIdAndUserIdParam;
import com.gp.commerce.swagger.ApiBaseSiteIdParam;
import com.gp.commerce.user.data.AddressDataList;
import com.gp.commerce.v2.helper.UsersHelper;
import com.gp.commerce.validation.data.AddressValidationData;
import com.gpintegration.constants.GpintegrationConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2bcommercefacades.company.B2BUserGroupFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUserGroupData;
import de.hybris.platform.commercefacades.address.AddressVerificationFacade;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.customergroups.CustomerGroupFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoDatas;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commercefacades.user.data.UserGroupDataList;
import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressValidationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.UserGroupListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.UserSignUpWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.UserWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.PK.PKException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

@Controller
@RequestMapping(value = "/{baseSiteId}/users")
@CacheControl(directive = CacheControlDirective.PRIVATE)
@Api(tags = "Users")
public class UsersController extends BaseCommerceController
{
	private static final String ROLE_GUEST = "ROLE_GUEST";
	private static final String ROLE_CUSTOMERGROUP = "ROLE_CUSTOMERGROUP";
	private static final String ROLE_CLIENT = "ROLE_CLIENT";
	private static final String ROLE_CUSTOMER_MANAGER_GROUP = "ROLE_CUSTOMERMANAGERGROUP";
	private static final String ROLE_TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";
	private static final Logger LOG = Logger.getLogger(UsersController.class);
	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;
	@Resource(name = "gpDefaultCustomerFacade")
	private GpCustomerFacade gpCustomerFacade;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "customerGroupFacade")
	private CustomerGroupFacade customerGroupFacade;
	@Resource(name = "addressVerificationFacade")
	private AddressVerificationFacade addressVerificationFacade;
	@Resource(name = "httpRequestCustomerDataPopulator")
	private HttpRequestCustomerDataPopulator httpRequestCustomerDataPopulator;
	@Resource(name = "HttpRequestUserSignUpDTOPopulator")
	private Populator<HttpServletRequest, UserSignUpWsDTO> httpRequestUserSignUpDTOPopulator;
	@Resource(name = "putUserDTOValidator")
	private Validator putUserDTOValidator;
	@Resource(name = "userSignUpDTOValidator")
	private Validator userSignUpDTOValidator;
	@Resource(name = "guestConvertingDTOValidator")
	private Validator guestConvertingDTOValidator;
	@Resource(name = "passwordStrengthValidator")
	private Validator passwordStrengthValidator;
	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;
	@Resource(name = "gpDefaultUserFacade")
	GPDefaultUserFacade gpDefaultUserFacade;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "b2bUserGroupFacade")
	private B2BUserGroupFacade b2bUserGroupFacade;
	@Resource(name = "messageSource")
	private MessageSource messageSource;
	@Resource(name = "i18NService")
	private I18NService i18NService;
	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;
	@Resource(name = "gpB2BUserGroupFacade")
	private GPDefaultB2BUserGroupFacade gpB2BUserGroupFacade;
	@Resource(name = "gpDefaultCustomerNameStrategy")
	private GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy;
	@Resource(name = "usersHelper")
	private UsersHelper usersHelper;
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource(name = "subscriptionCartFacadeImpl")
	private GPSubscriptionFacade subscriptionCartFacadeImpl;
	
	@Secured(
	{ ROLE_CLIENT, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	@SuppressWarnings("squid:S1160")
	@ApiOperation(value = " Registers a customer", notes = "Registers a customer. The following two sets of parameters are available: First set is used to register a customer. In this case the required parameters are: login, password, firstName, lastName, titleCode. Second set is used to convert a guest to a customer. In this case the required parameters are: guid, password.")
	@ApiBaseSiteIdParam
	public UserSignUpWsDTO registerUser(@PathVariable final String baseSiteId,
			@ApiParam(value = "User's object.", required = true) @RequestBody final UserSignUpWsDTO user)
			throws DuplicateUidException, UnknownIdentifierException, //NOSONAR
			IllegalArgumentException, WebserviceValidationException, UnsupportedEncodingException //NOSONAR
	{
		RegisterData registration;
		if (!StringUtils.equalsIgnoreCase(GPLoginType.GOOGLE.getCode(), user.getLoginType())
				&& !StringUtils.equalsIgnoreCase(GPLoginType.FACEBOOK.getCode(), user.getLoginType()) && null==user.getFbMobileAccount())
		{
			validate(user, GpcommerceCoreConstants.USER, userSignUpDTOValidator);
		}
		if(null!=user.getFbMobileAccount())
		{
			user.setPassword(configurationService.getConfiguration().getString(GpintegrationConstants.SOCIAL_LOGIN_PASSWORD));
		}
		registration = getDataMapper().map(user, RegisterData.class,GpcommerceCoreConstants.REGISTER_USER_FIELDS);
		registration.setBaseSiteId(baseSiteId);
		
		try
		{
			if (StringUtils.equalsIgnoreCase(GPLoginType.FACEBOOK.getCode(), user.getLoginType()) && null==user.getFbMobileAccount()) 
			{
				registration = gpCustomerFacade.registerUser(registration);
			} else {
				gpCustomerFacade.register(registration);
			}
		}
		catch (final DuplicateUidException e)
		{
			throw new DuplicateUidException(GpErrorConstants.ERROR_195);
		}
		return getDataMapper().map(registration, UserSignUpWsDTO.class,GpcommerceCoreConstants.REGISTERDTO_USER_FIELDS);
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get customer profile", notes = "Returns customer profile.")
	@ApiBaseSiteIdAndUserIdParam
	public UserWsDTO getUser(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final CustomerData customerData = customerFacade.getCurrentCustomer();
		return getDataMapper().map(customerData, UserWsDTO.class, fields);
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}", method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Updates customer profile", notes = "Updates customer profile. Attributes not provided in the request body will be defined again (set to null or default).")
	@ApiBaseSiteIdAndUserIdParam
	public void putUser(@ApiParam(value = "User's object", required = true) @RequestBody final UserWsDTO user)
			throws DuplicateUidException
	{
		validate(user, GpcommerceCoreConstants.USER, putUserDTOValidator);
		final CustomerData customer = customerFacade.getCurrentCustomer();
		if (LOG.isDebugEnabled()) {
			LOG.debug("putCustomer: userId=" + customer.getUid());
		}
		getDataMapper().map(user, customer, GpcommerceCoreConstants.PUT_USER_FIELDS, true);
		customerFacade.updateFullProfile(customer);
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}", method = RequestMethod.PATCH, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Updates customer profile", notes = "Updates customer profile. Only attributes provided in the request body will be changed.")
	@ApiBaseSiteIdAndUserIdParam
	public void updateUser(@ApiParam(value = "User's object.", required = true) @RequestBody final UserWsDTO user)
			throws DuplicateUidException
	{
		final CustomerData customer = customerFacade.getCurrentCustomer();
		if (LOG.isDebugEnabled()) {
			LOG.debug("updateUser: userId=" + customer.getUid());
		}
		getDataMapper().map(user, customer, GpcommerceCoreConstants.PUT_USER_FIELDS, false);
		customerFacade.updateFullProfile(customer);
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Delete customer profile", notes = "Removes customer profile.")
	@ApiBaseSiteIdAndUserIdParam
	public void deactivateUser()
	{
		final CustomerData customer = customerFacade.closeAccount();
		if (LOG.isDebugEnabled()) {
			LOG.debug("deactivateUser: userId=" + customer.getUid());
		}
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/login", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Changes customer's login", notes = "Changes customer's login.")
	@ApiBaseSiteIdAndUserIdParam
	public void changeLogin(
			@ApiParam(value = "Customer's new login. Customer login is case insensitive.", required = true) @RequestParam final String newLogin,
			@ApiParam(value = "Customer's current password.", required = true) @RequestParam final String password)
			throws DuplicateUidException, PasswordMismatchException, RequestParameterException //NOSONAR
	{
		final String baseSiteId = baseSiteService.getCurrentBaseSite().getUid();
		final String newLoginBySite = newLogin + "|" + baseSiteId;
		if (!EmailValidator.getInstance().isValid(newLoginBySite)){
			throw new RequestParameterException("Login [" + newLogin + "] is not a valid e-mail address!",
					RequestParameterException.INVALID, GpcommerceCoreConstants.NEW_LOGIN);
		}
		customerFacade.changeUid(newLoginBySite, password);
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/password", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	@ApiOperation(value = "Changes customer's password", notes = "Changes customer's password.")
	@ApiBaseSiteIdAndUserIdParam
	public void changePassword(@ApiParam("User identifier.") @PathVariable final String userId,
			@ApiParam("Old password. Required only for ROLE_CUSTOMERGROUP") @RequestParam(required = false) final String old,
			@ApiParam(value = "New password.", required = true) @RequestParam(value = "new") final String newPassword)
	{
		if (!old.equals(newPassword)) {
			final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			final UserSignUpWsDTO customer = new UserSignUpWsDTO();
			customer.setPassword(newPassword);
			validate(customer, "password", passwordStrengthValidator);
			if (usersHelper.containsRole(auth, ROLE_TRUSTED_CLIENT)
					|| usersHelper.containsRole(auth, ROLE_CUSTOMER_MANAGER_GROUP)) {
				userService.setPassword(userId, newPassword);
			} else {
				if (StringUtils.isEmpty(old)) {
					throw new RequestParameterException("Request parameter 'old' is missing.",
							RequestParameterException.MISSING, "old");
				}
				gpCustomerFacade.changePassword(old, newPassword);
			}

		} else
			throw new GPCommercePasswordException("New Password cannot be equal to Old Password!",
					RequestParameterException.INVALID);
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP, ROLE_GUEST })
	@RequestMapping(value = "/{userId}/addresses", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get customer's addresses", notes = "Returns customer's addresses.")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(code = 200, message = "List of customer's addresses")
	public AddressListWsDTO getAddresses(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final List<AddressData> addressList = gpDefaultUserFacade.getWholeAddressBook();
		final Boolean isAdmin=gpDefaultUserFacade.isAdmin();
		final AddressDataList addressDataList = new AddressDataList();
		addressDataList.setAddresses(addressList);
		addressDataList.setIsAdmin(isAdmin);
		return getDataMapper().map(addressDataList, AddressListWsDTO.class, fields);
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP, ROLE_GUEST })
	@RequestMapping(value = "/{userId}/getActiveAddresses", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get customer's active addresses", notes = "Returns customer's active addresses.")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(code = 200, message = "List of customer's addresses")
	public AddressListWsDTO getActiveAddresses(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final List<AddressData> addressList = gpDefaultUserFacade.getActiveAddresses();
		final AddressDataList addressDataList = new AddressDataList();
		addressDataList.setAddresses(addressList);
		return getDataMapper().map(addressDataList, AddressListWsDTO.class, fields);
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_GUEST, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/addresses", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	@ApiOperation(value = "Created a new address.", notes = "Created a new address.")
	@ApiBaseSiteIdAndUserIdParam
	public AddressWsDTO createAddress(@ApiParam(value = "Address object.", required = true) @RequestBody final AddressWsDTO address,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		if((address.getFromPage()!=null) && (address.getFromPage().equals(GpcommerceCoreConstants.MY_ACCOUNT))){
			gpDefaultUserFacade.populateApprovalStatusForL2L3(address);
		}
		validate(address, GpcommerceCoreConstants.ADDRESS, getAddressDTOValidator());
		final AddressData addressData = getDataMapper().map(address, AddressData.class, GpcommerceCoreConstants.CREATE_ADDRESS_FIELDS);
		if (addressData.isShippingAddress())
		{
			addressData.setShippingAddress(true);
		}
		addressData.setVisibleInAddressBook(true);

		gpDefaultUserFacade.addAddress(addressData);

		if (addressData.isDefaultAddress()){
			gpDefaultUserFacade.setDefaultAddressEntry(addressData, true, false);
		}
		if (addressData.isDefaultBillingAddress()){
			gpDefaultUserFacade.setDefaultAddressEntry(addressData, false, true);
		}
		return getDataMapper().map(addressData, AddressWsDTO.class, fields);
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_GUEST, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/addresses/{addressId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get info about address", notes = "Returns detailed information about address with a given id.")
	@ApiBaseSiteIdAndUserIdParam
	public AddressWsDTO getAddress(@ApiParam(value = "Address identifier.", required = true) @PathVariable final String addressId,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws WebserviceValidationException //NOSONAR
	{
		if (LOG.isDebugEnabled()){
			LOG.debug("getAddress: id=" + sanitize(addressId));
		}
		final AddressData addressData = getUserFacade().getAddressForCode(addressId);

		if (null!=addressData && addressData.getId().equals(gpDefaultUserFacade.getDefaultAddress(false, true).getId())){
			addressData.setDefaultBillingAddress(true);
		}
		if (addressData == null){
			throw new RequestParameterException(
					"Address with given id: '" + sanitize(addressId) + "' doesn't exist or belong to another user", //NOSONAR
					RequestParameterException.INVALID, GpcommerceCoreConstants.ADDRESS_ID);
		}
		return getDataMapper().map(addressData, AddressWsDTO.class, fields);
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_GUEST, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/addresses/{addressId}", method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Updates the address", notes = "Updates the address. Attributes not provided in the request will be defined again (set to null or default).")
	@ApiBaseSiteIdAndUserIdParam
	public void putAddress(@ApiParam(value = "Address identifier.", required = true) @PathVariable final String addressId,
			@ApiParam(value = "Address object.", required = true) @RequestBody final AddressWsDTO address)
			throws WebserviceValidationException //NOSONAR
	{
		validate(address, GpcommerceCoreConstants.ADDRESS, getAddressDTOValidator());
		final AddressData addressData = getUserFacade().getAddressForCode(addressId);
		if (addressData == null){
			throw new RequestParameterException(
					"Address with given id: '" + sanitize(addressId) + "' doesn't exist or belong to another user",
					RequestParameterException.INVALID, GpcommerceCoreConstants.ADDRESS_ID);
		}
		final boolean isAlreadyDefaultAddress = addressData.isDefaultAddress();
		addressData.setFormattedAddress(null);
		getDataMapper().map(address, addressData, GpcommerceCoreConstants.PUT_ADDRESS_FIELDS,
				true);
		getUserFacade().editAddress(addressData);
		if (!isAlreadyDefaultAddress && addressData.isDefaultAddress()){
			getUserFacade().setDefaultAddress(addressData);
		}
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_GUEST, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/addresses/{addressId}", method = RequestMethod.PATCH, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ApiOperation(value = "Updates the address", notes = "Updates the address. Only attributes provided in the request body will be changed.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void patchAddress(@ApiParam(value = "Address identifier.", required = true) @PathVariable final String addressId,
			@ApiParam(value = "Address object", required = true) @RequestBody final AddressWsDTO address)
			throws WebserviceValidationException //NOSONAR
	{
		final boolean currentSiteIsB2B=GPSiteConfigUtils.isB2BSite(baseSiteService.getCurrentBaseSite());
		AddressData addressData=null;
		final boolean editAddress=false;
		if((currentSiteIsB2B) && (address.getApprovalStatus()!=null) &&(address.getUserId()!=null)){
			 addressData = gpDefaultUserFacade.getUsersAddressForCode(addressId,address.getUserId());
		}else if((address.getFromPage()!=null) && (address.getFromPage().equals(GpcommerceCoreConstants.MY_ACCOUNT))){
			 gpDefaultUserFacade.populateApprovalStatusForL2L3(address);
			 addressData = getUserFacade().getAddressForCode(addressId);
		} else {
			addressData = getUserFacade().getAddressForCode(addressId);
		}
		if (addressData == null) {
			throw new RequestParameterException(
					"Address with given id: '" + sanitize(addressId) + "' doesn't exist or belong to another user",
					RequestParameterException.INVALID, GpcommerceCoreConstants.ADDRESS_ID);
		}
		final boolean isAlreadyDefaultAddress = addressData.isDefaultAddress();
		final boolean isAlreadyDefaultBillingAddress = addressData.isDefaultBillingAddress();
		addressData.setFormattedAddress(null);
		
		if (null == address.getApprovalStatus()) {
			getDataMapper().map(address, addressData, GpcommerceCoreConstants.PATCH_ADDRESS_FIELDS, true);
		} else {
			addressData.setApprovalStatus(address.getApprovalStatus());
		}
		validate(addressData, GpcommerceCoreConstants.ADDRESS, getAddressValidator());
		if((currentSiteIsB2B) && (addressData.getUserId()!=null)){
				gpDefaultUserFacade.editAddressForB2B(addressData,editAddress);
		}else{
			if (addressData.getId().equals(gpDefaultUserFacade.getDefaultAddress(true, false).getId())) {
				addressData.setDefaultAddress(true);
				addressData.setVisibleInAddressBook(true);
			}
			if (addressData.getId().equals(gpDefaultUserFacade.getDefaultAddress(false, true).getId())) {
				addressData.setDefaultBillingAddress(true);
			}
			if (!isAlreadyDefaultAddress && addressData.isDefaultAddress()) { // set it as default shipping address.
				gpDefaultUserFacade.setDefaultAddressEntry(addressData, true, false);
			}
			if (!isAlreadyDefaultBillingAddress && addressData.isDefaultBillingAddress()) { // set it as default billing
																							// address.
				gpDefaultUserFacade.setDefaultAddressEntry(addressData, false, true);
			}
			getUserFacade().editAddress(addressData);
		}
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_GUEST, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/addresses/update/{addressId}", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ApiOperation(value = "Updates the address", notes = "Updates the address. Only attributes provided in the request body will be changed.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void updateAddress(@ApiParam(value = "Address identifier.", required = true) @PathVariable final String addressId,
			@ApiParam(value = "Address object", required = true) @RequestBody final AddressWsDTO address)
			throws WebserviceValidationException //NOSONAR
	{
		final boolean currentSiteIsB2B=GPSiteConfigUtils.isB2BSite(baseSiteService.getCurrentBaseSite());
		AddressData addressData=null;
		final boolean editAddress=true;
		if(currentSiteIsB2B && (address.getFromPage()!=null) && (address.getFromPage().equals(GpcommerceCoreConstants.MY_ACCOUNT))
				&& address.getUserId()!=null){
			 gpDefaultUserFacade.populateApprovalStatusForL2L3(address);
			 addressData = gpDefaultUserFacade.getUsersAddressForCode(addressId,address.getUserId());

		} else {
			addressData = getUserFacade().getAddressForCode(addressId);
		}
		if (addressData == null) {
			throw new RequestParameterException(
					"Address with given id: '" + sanitize(addressId) + "' doesn't exist or belong to another user",
					RequestParameterException.INVALID, GpcommerceCoreConstants.ADDRESS_ID);
		}
		addressData.setFormattedAddress(null);
		getDataMapper().map(address, addressData, GpcommerceCoreConstants.PATCH_ADDRESS_FIELDS, true);
		validate(addressData, GpcommerceCoreConstants.ADDRESS, getAddressValidator());
		if ((currentSiteIsB2B) && (addressData.getUserId() != null)) {
			gpDefaultUserFacade.editAddressForB2B(addressData,editAddress);
		} else {
			gpDefaultUserFacade.editAddress(addressData);
		}
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_GUEST, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/addresses/{addressId}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete customer's address", notes = "Removes customer's address.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void deleteAddress(@ApiParam(value = "Address identifier.", required = true) @PathVariable final String addressId)
	{
		if (LOG.isDebugEnabled()) {
			LOG.debug("deleteAddress: id=" + sanitize(addressId));
		}
		final AddressData address = getUserFacade().getAddressForCode(addressId);
		if (address == null) {
			throw new RequestParameterException(
					"Address with given id: '" + sanitize(addressId) + "' doesn't exist or belong to another user",
					RequestParameterException.INVALID, GpcommerceCoreConstants.ADDRESS_ID);
		}
		getUserFacade().removeAddress(address);
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_GUEST, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/addresses/verification", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ApiOperation(value = "Verifies address", notes = "Verifies address.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public AddressValidationWsDTO verifyAddress(
			@ApiParam(value = "Address object.", required = true) @RequestBody final AddressWsDTO address,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final AddressData addressData = getDataMapper().map(address, AddressData.class,GpcommerceCoreConstants.VERIFY_ADDRESS_FIELDS);
		final Errors errors = new BeanPropertyBindingResult(addressData, GpcommerceCoreConstants.ADDRESS_DATA);
		AddressValidationData validationData = new AddressValidationData();
		if (usersHelper.isAddressValid(addressData, errors, validationData)) {
			validationData = usersHelper.verifyAddresByService(addressData, errors, validationData);
		}
		return getDataMapper().map(validationData, AddressValidationWsDTO.class, fields);
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/paymentdetails", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get customer's credit card payment details list.", notes = "Return customer's credit card payment details list.")
	@ApiBaseSiteIdAndUserIdParam
	public PaymentDetailsListWsDTO getPaymentInfos(
			@ApiParam(value = "Type of payment details.", required = true) @RequestParam(required = false, defaultValue = "false") final boolean saved,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		if (LOG.isDebugEnabled()) {
			LOG.debug("getPaymentInfos");
		}
		final CCPaymentInfoDatas paymentInfoDataList = new CCPaymentInfoDatas();
		paymentInfoDataList.setPaymentInfos(usersHelper.getUniquePaymentInfo(getUserFacade().getCCPaymentInfos(saved)));
		return getDataMapper().map(paymentInfoDataList, PaymentDetailsListWsDTO.class, fields);
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/paymentdetails/{paymentDetailsId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get customer's credit card payment details.", notes = "Returns customer's credit card payment details for a given id.")
	@ApiBaseSiteIdAndUserIdParam
	public PaymentDetailsWsDTO getPaymentDetails(
			@ApiParam(value = "Payment details identifier.", required = true) @PathVariable final String paymentDetailsId,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		return getDataMapper().map(getPaymentInfo(paymentDetailsId), PaymentDetailsWsDTO.class, fields);
	}
	public CCPaymentInfoData getPaymentInfo(final String paymentDetailsId)
	{
		LOG.debug("getPaymentInfo : id = " + sanitize(paymentDetailsId));
		try
		{
			final CCPaymentInfoData paymentInfoData = getUserFacade().getCCPaymentInfoForCode(paymentDetailsId);
			if (paymentInfoData == null) {
				throw new RequestParameterException("Payment details [" + sanitize(paymentDetailsId) + "] not found.",
						RequestParameterException.UNKNOWN_IDENTIFIER, GpcommerceCoreConstants.PAYMENT_DETAILS_ID);
			}
			return paymentInfoData;
		} catch (final PKException e) {
			throw new RequestParameterException("Payment details [" + sanitize(paymentDetailsId) + "] not found.",
					RequestParameterException.UNKNOWN_IDENTIFIER, GpcommerceCoreConstants.PAYMENT_DETAILS_ID, e);
		}
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/paymentdetails/{paymentDetailsId}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete customer's credit card payment details.", notes = "Removes customer's credit card payment details based on its ID.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void deletePaymentInfo(
			@ApiParam(value = "Payment details identifier.", required = true) @PathVariable final String paymentDetailsId)
	{
		if (LOG.isDebugEnabled()) {
			LOG.debug("deletePaymentInfo: id = " + sanitize(paymentDetailsId));
		}
		getPaymentInfo(paymentDetailsId);
		gpDefaultUserFacade.removeCCPaymentInfo(paymentDetailsId);
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/paymentdetails/{paymentDetailsId}", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ApiOperation(value = "Updates existing customer's credit card payment details.", notes = "Updates existing customer's credit card payment details based on its ID. Only attributes given in request will be changed.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void updatePaymentInfo(
			@ApiParam(value = "Payment details identifier.", required = true) @PathVariable final String paymentDetailsId,
			@ApiParam(value = "Payment details object", required = true) @RequestBody final PaymentDetailsWsDTO paymentDetails)
			throws RequestParameterException //NOSONAR
	{
		final CCPaymentInfoData paymentInfoData = getPaymentInfo(paymentDetailsId);
		final boolean isAlreadyDefaultPaymentInfo = paymentInfoData.isDefaultPaymentInfo();
		getDataMapper().map(paymentDetails, paymentInfoData,
				GpcommerceCoreConstants.UPDATE_PAYMENT_INFO_FIELDS, false);
		validate(paymentInfoData, GpcommerceCoreConstants.PAYMENT_DETAILS, getCcPaymentInfoValidator());
		getUserFacade().updateCCPaymentInfo(paymentInfoData);
		if (paymentInfoData.isSaved() && !isAlreadyDefaultPaymentInfo && paymentInfoData.isDefaultPaymentInfo()) {
			getUserFacade().setDefaultPaymentInfo(paymentInfoData);
		}
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/paymentdetails/{paymentDetailsId}", method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ApiOperation(value = "Updates existing customer's credit card payment info.", notes = "Updates existing customer's credit card payment info based on the payment info ID. Attributes not given in request will be defined again (set to null or default).")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void putPaymentInfo(
			@ApiParam(value = "Payment details identifier.", required = true) @PathVariable final String paymentDetailsId,
			@ApiParam(value = "Payment details object.", required = true) @RequestBody final PaymentDetailsWsDTO paymentDetails)
			throws RequestParameterException //NOSONAR
	{
		final CCPaymentInfoData paymentInfoData = getPaymentInfo(paymentDetailsId);
		final boolean isAlreadyDefaultPaymentInfo = paymentInfoData.isDefaultPaymentInfo();
		validate(paymentDetails, GpcommerceCoreConstants.PAYMENT_DETAILS, getPaymentDetailsDTOValidator());
		getDataMapper().map(paymentDetails, paymentInfoData, GpcommerceCoreConstants.UPDATE_PAYMENT_INFO_FIELDS, true);
		getUserFacade().updateCCPaymentInfo(paymentInfoData);
		if (paymentInfoData.isSaved() && !isAlreadyDefaultPaymentInfo && paymentInfoData.isDefaultPaymentInfo()) {
			getUserFacade().setDefaultPaymentInfo(paymentInfoData);
		}
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/paymentdetails/add", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ApiOperation(value = "Add customer's credit card payment info.", notes = "Add customer's credit card payment info. Attributes not given in request will be set to null or default.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void addPaymentInfo(
			@ApiParam(value = "Payment details object.", required = true) @RequestBody final PaymentDetailsWsDTO paymentDetails)
			throws RequestParameterException //NOSONAR
	{
		final CCPaymentInfoData paymentInfoData = new CCPaymentInfoData();
		validate(paymentDetails, GpcommerceCoreConstants.PAYMENT_DETAILS, getPaymentDetailsDTOValidator());
		getDataMapper().map(paymentDetails, paymentInfoData, GpcommerceCoreConstants.UPDATE_PAYMENT_INFO_FIELDS, true);
		gpDefaultUserFacade.addCCPaymentInfo(paymentInfoData);
	}
	
	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/customergroups", method = RequestMethod.GET)
	@ApiOperation(value = "Get all customer groups of a customer.", notes = "Returns all customer groups of a customer.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public UserGroupListWsDTO getAllCustomerGroupsForCustomer(
			@ApiParam(value = "User identifier.", required = true) @PathVariable final String userId,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final UserGroupDataList userGroupDataList = new UserGroupDataList();
		userGroupDataList.setUserGroups(customerGroupFacade.getCustomerGroupsForUser(userId));
		return getDataMapper().map(userGroupDataList, UserGroupListWsDTO.class, fields);
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/marketingPreferences", method = RequestMethod.GET)
	@ApiOperation(value = "Get all marketing preferences of a site.", notes = "Returns all the marketing preferences of a site.")
	//@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public MarketingPreferenceQuestionAnsListWsDTO getMarketingPrefernces(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final MarketingPreferenceQuestionAnsDataList marketingPreferenceQuesAnsDataList = gpDefaultUserFacade.getAllMarketingPreferences();
		return getDataMapper().map(marketingPreferenceQuesAnsDataList, MarketingPreferenceQuestionAnsListWsDTO.class, fields);
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP, ROLE_GUEST })
	@RequestMapping(value = "/{userId}/upDatePreferences", method = RequestMethod.POST)
	@ApiOperation(value = "update the selected preferences to customer information.", notes = "Returns all customer groups of a customer.")
	//@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void updateCustomerPreferences(
			@ApiParam(value = "marketing preferences list object.", required = true) @RequestBody final MarketingPreferenceListWsDTO marketingPreferences)
	{
		final MarketingPreferenceDataList preferencesDataList = new MarketingPreferenceDataList();
		getDataMapper().map(marketingPreferences, preferencesDataList, GpcommerceCoreConstants.UPDATE_CUSTOMER_PREF_FIELDS, false);
		gpDefaultUserFacade.updateCustomerPreferences(preferencesDataList);
	}

	@Secured(
	{ ROLE_CLIENT, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMERGROUP })
	@RequestMapping(value = "/{userId}/usergroups", method = RequestMethod.GET)
	@ApiOperation(value = "Get Permission for an admin.", notes = "Returns permissions for an admin.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BUserGroupListWsDTO getUserGroups(
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "sort", defaultValue = B2BCustomerModel.UID) final String sortCode)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(page);
		pageableData.setSort(sortCode);
		pageableData.setPageSize(Config.getInt("gpcommercewebservices.search.pageSize", 100));
		final List<B2BUserGroupData> customers = b2bUserGroupFacade.getPagedB2BUserGroups(pageableData).getResults();
		final B2BUserGroupDataList dataList = new B2BUserGroupDataList();
		dataList.setUsergroups(customers);
		return getDataMapper().map(dataList, B2BUserGroupListWsDTO.class, fields);
	}

	@Secured(
	{ ROLE_CLIENT, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMERGROUP })
	@RequestMapping(value = "/{userId}/usergroups/{userGroupUid}", method = RequestMethod.GET)
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BUserGroupWsDTO getUserGroupDetails(@PathVariable final String userGroupUid,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final B2BUserGroupData userGroupData = b2bUserGroupFacade.getB2BUserGroup(userGroupUid);
		return getDataMapper().map(userGroupData, B2BUserGroupWsDTO.class, fields);
	}

	@Secured(
	{ ROLE_CLIENT, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMERGROUP })
	@RequestMapping(value = "/{userId}/manageusergroup", method = RequestMethod.POST)
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void manageUserGroup(@RequestBody final B2BUserGroupRequestDTO userGroupReqDTO,
			@RequestParam(required = false) final String userGroupUid,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) throws GPCommerceBusinessException
	{
		final B2BUserGroupData b2bUserGroupData = new B2BUserGroupData();
		b2bUserGroupData.setUid(userGroupReqDTO.getUserGroupUid());
		b2bUserGroupData.setName(userGroupReqDTO.getUserGroupName());
		final B2BUnitData unitData = new B2BUnitData();
		unitData.setUid(userGroupReqDTO.getUnitUid());
		b2bUserGroupData.setUnit(unitData);
		try {
			if (userGroupUid != null) {
				gpB2BUserGroupFacade.updateUserGroup(userGroupUid, b2bUserGroupData, false);
			} else {
				gpB2BUserGroupFacade.updateUserGroup(userGroupReqDTO.getUserGroupUid(), b2bUserGroupData, true);
			}
		} catch (final ModelSavingException | AmbiguousIdentifierException | JaloItemNotFoundException e) {
			LOG.error("Manage Usergroup " + userGroupReqDTO.getUserGroupUid(), e);
			throw new GPCommerceBusinessException(GPB2BExceptionEnum.USERGROUP.getCode(), GPB2BExceptionEnum.USERGROUP.getErrMsg());
		}
	}

	@Secured(
	{ ROLE_CLIENT, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMERGROUP })
	@RequestMapping(value = "/{userId}/disableusergroup/{userGroupUid}", method = RequestMethod.POST)
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void disableUserGroup(@PathVariable final String userGroupUid,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		searchRestrictionService.disableSearchRestrictions();
		b2bUserGroupFacade.disableUserGroup(userGroupUid);
		searchRestrictionService.enableSearchRestrictions();
	}

	@Secured(
	{ ROLE_CLIENT, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMERGROUP })
	@RequestMapping(value = "/{userId}/removeusergroup/{userGroupUid}", method = RequestMethod.POST)
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void removeUserGroup(@PathVariable final String userGroupUid,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		searchRestrictionService.disableSearchRestrictions();
		b2bUserGroupFacade.removeUserGroup(userGroupUid);
		searchRestrictionService.enableSearchRestrictions();
	}

	@Secured(
	{ ROLE_CLIENT, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP, ROLE_CUSTOMERGROUP })
	@RequestMapping(method = RequestMethod.GET, value = "/{userId}/taxExemption/status")
	@ResponseBody
	public String getCustomerTaxExemptionStatus(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws GPCommerceNoDataException
	{
		return gpCustomerFacade.getCustomerTaxExemptionStatus();
	}

	@Secured(
	{ ROLE_CLIENT, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP, ROLE_CUSTOMERGROUP })
	@RequestMapping(method = RequestMethod.POST, value = "/{userId}/taxExemption/submit", consumes =
	{ MediaType.MULTIPART_FORM_DATA_VALUE })
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void submitTaxExemptionDocuments(
			@ApiParam(value = GpcommerceCoreConstants.DOCUMENTS, required = true) @RequestParam(GpcommerceCoreConstants.DOCUMENTS) final MultipartFile[] taxExemptionRequestDocumentArray,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		try {
			final Locale currentLocale = i18NService.getCurrentLocale();
			if (taxExemptionRequestDocumentArray.length == 0) {
				throw new RequestParameterException(messageSource.getMessage(GpcommerceCoreConstants.INVALID_TAX_EXEMPTION_FILE_UPLOAD, null, currentLocale),
						RequestParameterException.INVALID, GpcommerceCoreConstants.DOCUMENTS);
			}
			gpCustomerFacade.submitTaxExemptionDocuments(taxExemptionRequestDocumentArray);
		} catch (final GPCommerceSaveDataException e) {
			LOG.error(e.getMessage(),e);
			throw new GPCommerceSaveDataException(GpErrorConstants.ERROR_SUBMIT_TAX_EXEMPTION_DOCS, e.getMessage());
		}
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/updatepersonaldetails", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Updates customer profile", notes = "Updates customer profile. Only attributes provided in the request body will be changed.")
	@ApiBaseSiteIdAndUserIdParam
	public UserWsDTO updateUser(@ApiParam(value = "User's object.", required = true) @RequestBody final UserWsDTO user,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL,UPDATE") @RequestParam(defaultValue = "UPDATE") final String fields)
			throws DuplicateUidException, PasswordMismatchException, RequestParameterException
	{
		CustomerData customer = null;
		if(user.getUserIdUpdateFlag() == true)
		{
			final String newLoginId = user.getUid();
			if (!EmailValidator.getInstance().isValid(user.getUid())) {
				throw new RequestParameterException("Login [" + newLoginId + "] is not a valid e-mail address!",
						RequestParameterException.INVALID, GpcommerceCoreConstants.NEW_LOGIN_ID);
			}
			gpCustomerFacade.changeUid(newLoginId, user.getPassword());
			final String userName= gpDefaultCustomerNameStrategy.getName(userService.getCurrentUser().getName());
			final String name = user.getFirstName() + " " + user.getLastName();
			if(!(userName.equals(name)) || (StringUtils.isNotEmpty(user.getCellPhone()))) {
				customer = usersHelper.personalDetails(user);
			}
		} else {
			customer = usersHelper.personalDetails(user);
		}
		return getDataMapper().map(customer, UserWsDTO.class, fields);
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/connect", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = " connect user to social account", notes = "connects user to social account")
	@ApiBaseSiteIdParam
	public void connect(@PathVariable final String baseSiteId,@ApiParam(value = "Ws"+ "'s object.", required = true) @RequestBody final UserSignUpWsDTO user)
			throws DuplicateUidException, UnknownIdentifierException,
			IllegalArgumentException, WebserviceValidationException, UnsupportedEncodingException
	{
		RegisterData registration;
		registration = getDataMapper().map(user, RegisterData.class, GpcommerceCoreConstants.CONNECT_FIELDS);
		registration.setBaseSiteId(baseSiteId);
		try {
			gpCustomerFacade.connect(registration);
		} catch (final DuplicateUidException e) {
			throw new DuplicateUidException(GpErrorConstants.ERROR_195);
		}
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/disconnect",method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = " disconnect user to social account", notes = "disconnect user to social account")
	@ApiBaseSiteIdParam
	public void disconnect(@PathVariable final String baseSiteId,@ApiParam(value = "User's object.", required = true) @RequestBody final UserSignUpWsDTO user)
			throws DuplicateUidException, UnsupportedEncodingException
	{
		RegisterData registration;
		registration = getDataMapper().map(user, RegisterData.class, GpcommerceCoreConstants.DISCONNECT_FIELDS);
		registration.setBaseSiteId(baseSiteId);
		try {
			gpCustomerFacade.disconnect(registration);
		}catch (final DuplicateUidException e) {
			throw new DuplicateUidException(GpErrorConstants.ERROR_195);
		}
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP })
	@RequestMapping(value = "/{userId}/reset", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Generates a token to restore customer's forgotten password.", notes = "Generates a token to restore customer's forgotten password.")
	@ApiBaseSiteIdParam
	public void resetPassword(@PathVariable final String baseSiteId,
			@ApiParam(value = "Customer's user id. Customer user id is case insensitive.", required = true) @RequestParam final String resetUserId)
	{
		final String uidDelimiter = configurationService.getConfiguration().getString(GpcommerceCoreConstants.BASESITE_DELIMITER);
		if (baseSiteId != null) {
			gpCustomerFacade.forgottenPassword(resetUserId.toLowerCase()+uidDelimiter+baseSiteId);
		}
	}

	@Secured(
	{ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP, ROLE_GUEST })
	@RequestMapping(value = "/{userId}/createUpDatePreferences", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	@ApiOperation(value = "update the selected preferences to customer information.", notes = "Returns stauts 200 OK.")
	@ResponseStatus(HttpStatus.OK)
	public String createUpdateCustomerPreferences(
	@ApiParam(value = "marketing preferences list object.", required = true) @RequestBody final UpdatePreferenceWsDTO marketingPreferences)
	{
		final UpdatePreferenceData preferenceData = getDataMapper().map(marketingPreferences, UpdatePreferenceData.class,
				GpcommerceCoreConstants.CREATE_CUSTOMER_PREFS_FIELDS);
		return gpCustomerFacade.createUpdateContact(preferenceData,false);
	}
	
	@Secured({ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP,ROLE_CLIENT,ROLE_GUEST })
	@RequestMapping(value = "/{userId}/pdfDownloadUserDetails", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ApiOperation(value = "save the user details entered in PDF Download Form.", notes = "Returns stauts 200 OK.")
	@ResponseStatus(HttpStatus.OK)
	public void pdfDownloadUserDetails(
			@ApiParam(value = "PDF user entered details", required = true) @RequestBody  PdfUserDetailsWsDTO pdfUserDetailsWsDTO) {
		PdfDownloadUserData pdfDownloadUserData = getDataMapper().map(pdfUserDetailsWsDTO, PdfDownloadUserData.class,
				GpcommerceCoreConstants.CREATE_PDF_USER_DETAILS_FIELDS);
		gpCustomerFacade.pdfDownloadUserDetails(pdfDownloadUserData);
	}
	
	@Secured({ ROLE_CUSTOMERGROUP, ROLE_TRUSTED_CLIENT, ROLE_CUSTOMER_MANAGER_GROUP,ROLE_CLIENT,ROLE_GUEST })
	@RequestMapping(value = "/{userId}/pdfDownloadUserDetails", method = RequestMethod.GET)
	@ApiBaseSiteIdParam
	@ResponseBody
	public PdfUserDetailsWsDTO getPdfDownloadUserDetails(
			@ApiParam(value = "certification codes", required = true) @RequestParam final String certificationCodes,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) {
		PdfDownloadUserData pdfDownloadUserData = gpCustomerFacade.getPdfDownloadUserDetails();
		PdfUserDetailsWsDTO pdfUserDetailsWsDTO = getDataMapper().map(pdfDownloadUserData, PdfUserDetailsWsDTO.class, fields);
		Map<String, String> gpCerficationImagesMap = cmsSiteService.getCertificationImagesForCodes(certificationCodes);
		Map<String, String> pdfCoverImagesList = cmsSiteService.getPdfCoverImages();
		if(MapUtils.isNotEmpty(gpCerficationImagesMap)) {
			pdfUserDetailsWsDTO.setGpCertificationsImages(gpCerficationImagesMap);
		}
		if(MapUtils.isNotEmpty(pdfCoverImagesList)) {
			pdfUserDetailsWsDTO.setPdfCoverImages(pdfCoverImagesList);
		}
		return pdfUserDetailsWsDTO;	
	}
}
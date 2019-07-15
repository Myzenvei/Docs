/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.gp.commerce.core.company.services.GPB2BUnitsService;
import com.gp.commerce.core.constants.GpErrorConstants;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.GPApprovalEnum;
import com.gp.commerce.core.enums.GenderEnum;
import com.gp.commerce.core.enums.TaxExemptionStatusEnum;
import com.gp.commerce.core.event.EmailSubjectType;
import com.gp.commerce.core.event.GPTaxExemptionSubmitReviewEvent;
import com.gp.commerce.core.exceptions.GPCommercePasswordException;
import com.gp.commerce.core.exceptions.GPCommerceSaveDataException;
import com.gp.commerce.core.model.GPPdfDownloadUserDetailsModel;
import com.gp.commerce.core.model.TaxExemptionModel;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.services.GPCustomerAccountService;
import com.gp.commerce.core.services.GPUserService;
import com.gp.commerce.core.services.event.GPEmailItemEvent;
import com.gp.commerce.core.services.event.PasswordResetEvent;
import com.gp.commerce.core.services.event.UpdateProfileEvent;
import com.gp.commerce.core.strategies.GPDefaultCustomerNameStrategy;
import com.gp.commerce.core.user.dao.GPUserDao;
import com.gp.commerce.facades.data.PdfDownloadUserData;

import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.commerceservices.event.ChangeUIDEvent;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.impl.UniqueAttributesInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;


/**
 * This class is used for processing GP Customer account services
 */
public class DefaultGPCustomerAccountService extends DefaultCustomerAccountService implements GPCustomerAccountService
{

	@Resource(name = "eventService")
	private EventService eventService;

	@Resource(name = "userService")
	private GPUserService userService;

	@Resource(name = "gpB2BUnitsService")
	private GPB2BUnitsService<B2BUnitModel, UserModel> gpB2BUnitsService;

	@Resource(name = "userDao")
	private GPUserDao userDao;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;
	
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;

	private static final Logger LOG = Logger.getLogger(DefaultGPCustomerAccountService.class);
	private static final String ERROR_3312 = "3312";
	private static final String BASESITE_DELIMITER = "gpcommercewebservices.user.delimiter";
	private static final String BUSINESS_USER_EMAIL = "gp.businessuser.email.id";
	private static final String GP_BUSINESS_USER_NAME = "gp.businessuser.name";

	private static final String LEAP_YEAR = "config.dob.leap.year";

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "b2bUnitService")
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "guidKeyGenerator")
	private KeyGenerator guidKeyGenerator;

	@Resource(name = "catalogVersionService")
	private CatalogVersionService catalogVersionService;

	@Resource(name = "cardPaymentInfoReverseConverter")
	private Converter<CCPaymentInfoData, CreditCardPaymentInfoModel> cardPaymentInfoReverseConverter;

	@Resource(name = "addressReverseConverter")
	private Converter<AddressData, AddressModel> addressReverseConverter;

	@Resource(name = "mediaService")
	private MediaService mediaService;
	
	@Resource(name = "gpDefaultCustomerNameStrategy")
	private GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy;
	
	@Resource(name = "gpPdfUserDetailsReverseConverter")
	private Converter<PdfDownloadUserData, GPPdfDownloadUserDetailsModel> gpPdfUserDetailsReverseConverter;
	
	@Resource(name = "passwordEncoderService")
	private PasswordEncoderService passwordEncoderService;

	@Override
	public void updatePassword(final String token, final String newPassword) throws TokenInvalidatedException {
		Assert.hasText(token, "The field [token] cannot be empty");
		Assert.hasText(newPassword, "The field [newPassword] cannot be empty");
		final SecureToken data = getSecureTokenService().decryptData(token);
		if (getTokenValiditySeconds() > 0L) {
			final long delta = new Date().getTime() - data.getTimeStamp();
			if (delta / 1000 > getTokenValiditySeconds()) {
				throw new IllegalArgumentException("token expired");
			}
		}
		final CustomerModel customer = getUserService().getUserForUID(data.getData(), CustomerModel.class);
		if (customer == null) {
			throw new IllegalArgumentException("user for token not found");
		}
		if (!token.equals(customer.getToken())) {
			throw new TokenInvalidatedException();
		}
		if (!passwordEncoderService.isValid(customer, newPassword)) {
			getUserService().setPassword(data.getData(), newPassword, getPasswordEncoding());
			customer.setToken(null);
			customer.setLoginDisabled(false);
			customer.setFailedLoginAttempts(0);
			getModelService().save(customer);
			getEventService().publishEvent(initializeEvent(new PasswordResetEvent(), customer));
		} else
			throw new GPCommercePasswordException("200",
					"New Password cannot be equal to Old Password!");
	}

	/**
	 * a boolean method to Verify Token validity
	 * @param token
	 * 		the token
	 * @return valid status of token
	 */
	public Boolean verfiyTokenValidity(final String token)
	{
		Boolean isValidToken = true;
		Assert.hasText(token, "The field [token] cannot be empty");

		final SecureToken data = getSecureTokenService().decryptData(token);
		if (getTokenValiditySeconds() > 0L)
		{
			final long delta = new Date().getTime() - data.getTimeStamp();
			if (delta / 1000 > getTokenValiditySeconds())
			{
				isValidToken = false;
			}
		}
		return isValidToken;
	}


	/**
	 * Register B 2 b unit.
	 *
	 * @param b2bCustomerModel
	 *           the customer model
	 * @param password
	 *           the password
	 * @throws DuplicateUidException
	 *            the duplicate uid exception
	 */
	public void registerB2bUnit(final B2BCustomerModel b2bCustomerModel, final String password) throws DuplicateUidException
	{
		registerB2bCustomer(b2bCustomerModel, password);
	}
	/**
	 * to set values in event
	 *
	 * @param AbstractCommerceUserEvent
	 *           The event
	 *  @param CustomerModel
	 *           The customerModel
	 *
	 */
	@Override
	public void saveAddressEntry(final CustomerModel customerModel, final AddressModel addressModel)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		validateParameterNotNull(addressModel, "Address model cannot be null");
		final List<AddressModel> customerAddresses = new ArrayList<>();
		customerAddresses.addAll(customerModel.getAddresses());
		if (!customerModel.getAddresses().contains(addressModel))
		{
			addressModel.setOwner(customerModel);
			customerAddresses.add(addressModel);
		}
		customerModel.setAddresses(customerAddresses);

		getModelService().save(addressModel);
		getModelService().save(customerModel);

			final GPEmailItemEvent item = new GPEmailItemEvent();
			item.setAddress(addressModel);
			final List<String> listEmails= new ArrayList<>();
		if (!getUserService().isAnonymousUser(getUserService().getCurrentUser()))
		{
			if (customerModel instanceof B2BCustomerModel && null != addressModel.getB2bUnit()
					&& !GpcommerceCoreConstants.B2B_UNIT_L1
							.equalsIgnoreCase(((B2BCustomerModel) customerModel).getDefaultB2BUnit().getB2bUnitLevel()))
			{
				final boolean pendingByGP = (GpcommerceCoreConstants.PENDING_BY_GP
						.equalsIgnoreCase(addressModel.getApprovalStatus().getCode())) ? true : false;
				if (checkIfAddressAddedByAdmin(addressModel.getB2bUnit(), (B2BCustomerModel) customerModel))
				{
					// email to gp user
					item.setAdminName(cmsSiteService.getSiteConfig(GP_BUSINESS_USER_NAME));
					listEmails.add(cmsSiteService.getSiteConfig(BUSINESS_USER_EMAIL));
					item.setToEmails(listEmails);
					item.setEmailSubject(EmailSubjectType.ADDRESS_SUBMITTED.getSubject());
					eventService.publishEvent(initializeEvent(item, customerModel));
				}
				else if(!pendingByGP)
				{
					//email to go admin when buyer adds
					final Collection<B2BCustomerModel> administrators = b2bUnitService.getUsersOfUserGroup(
							((B2BCustomerModel) customerModel).getDefaultB2BUnit(), B2BConstants.B2BADMINGROUP, false);

					for (final B2BCustomerModel administrator : administrators)
					{
						listEmails.add(administrator.getEmail());
					}
					if (administrators.iterator().hasNext()) {
						String adminName = gpDefaultCustomerNameStrategy.getName(administrators.iterator().next().getDisplayName());
						item.setAdminName(adminName);
					}
					item.setToEmails(listEmails);
					item.setEmailSubject(EmailSubjectType.ADDRESS_SUBMITTED.getSubject());
					eventService.publishEvent(initializeEvent(item, customerModel));
				}
			}


		}



	}
	/**
	 * to set values in event
	 *
	 * @param AbstractCommerceUserEvent
	 *           The event
	 *  @param B2BCustomerModel
	 *           The customerModel
	 *
	 */
	protected AbstractCommerceUserEvent initializeEvent(final AbstractCommerceUserEvent event,
			final B2BCustomerModel customerModel)
	{
		event.setBaseStore(customerModel.getSite().getStores().get(0));
		event.setSite(customerModel.getSite());
		event.setCustomer(customerModel);
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		return event;
	}
	
	protected AbstractCommerceUserEvent initializeEvent(final AbstractCommerceUserEvent event,
			final B2BCustomerModel adminUser,BaseSiteModel site)
	{
		event.setBaseStore(site.getStores().get(0));
		event.setSite(site);
		event.setCustomer(adminUser);
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		return event;
	}
	/**
	 * to get property from config
	 *
	 * @param String
	 *           The property
	 */
	private String getProperty(final String property) {
		return configurationService.getConfiguration().getString(property);
	}

	/**
	 *  to check if B2B user has added address
	 * @param b2bUnit the b2b unit
	 * @param currentCustomer the current B2B customer
	 * @return <b>true</b> if the address added by Admin, <br><b>false</b> otherwise
	 */
	public Boolean checkIfAddressAddedByAdmin(final B2BUnitModel b2bUnit, final B2BCustomerModel currentCustomer)
	{
		final Boolean hasAdminAdded =(getProperty(GpcommerceCoreConstants.B2BUNITL2).equalsIgnoreCase(b2bUnit.getB2bUnitLevel())
				|| getProperty(GpcommerceCoreConstants.B2BUNITL3).equalsIgnoreCase(b2bUnit.getB2bUnitLevel())) && gpB2BUnitsService.isB2BAdmin(currentCustomer);

		return hasAdminAdded;
	}


	/**
	 * Register B 2 b customer.
	 *
	 * @param b2bCustomerModel
	 *           the customer model
	 * @param password
	 *           the password
	 * @throws DuplicateUidException
	 *            the duplicate uid exception
	 */
	protected void registerB2bCustomer(final B2BCustomerModel b2bCustomerModel, final String password) throws DuplicateUidException
	{
		validateParameterNotNullStandardMessage("b2bCustomerModel", b2bCustomerModel);

		generateCustomerId(b2bCustomerModel);
		if (password != null)
		{
			getUserService().setPassword(b2bCustomerModel, password, getPasswordEncoding());
		}
		try
		{
			getModelService().save(b2bCustomerModel);
			registerB2BCustomerEvent(b2bCustomerModel);
		}
		catch (final ModelSavingException e)
		{
			if (e.getCause() instanceof InterceptorException
					&& ((InterceptorException) e.getCause()).getInterceptor().getClass().equals(UniqueAttributesInterceptor.class))
			{
				throw new DuplicateUidException(b2bCustomerModel.getUid(), e);
			}
			else
			{
				throw e;
			}
		}
		catch (final AmbiguousIdentifierException e)
		{
			throw new DuplicateUidException(b2bCustomerModel.getUid(), e);
		}
	}

	/**
	 * Gets user id based on argument token
	 * @param token the token
	 * @return user id data
	 */
	public String getUserId(final String token)
	{
		final SecureToken data = getSecureTokenService().decryptData(token);
		return data.getData().split("\\|")[0];
	}

	/**
	 * Create tax exemption models and save the tax exemption documents.
	 *
	 * @param taxExemptionDocumentList list of tax exemption documents
	 * @throws GPCommerceSaveDataException on error
	 */
	public void submitTaxExemptionDocuments(final List<MediaModel> taxExemptionDocumentList) throws GPCommerceSaveDataException
	{
		try
		{
			final TaxExemptionModel taxExemptionModel = getModelService().create(TaxExemptionModel.class);
			taxExemptionModel.setCode(UUID.randomUUID().toString());
			taxExemptionModel.setTaxExemptionDocumentList(taxExemptionDocumentList);
			final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
			taxExemptionModel.setCustomer(currentCustomer);
			getModelService().save(taxExemptionModel);
			currentCustomer.setTaxExemptionStatus(TaxExemptionStatusEnum.IN_PROCESS);
			getModelService().save(currentCustomer);
			//sending an email to gp user for approval
			final GPTaxExemptionSubmitReviewEvent event = new GPTaxExemptionSubmitReviewEvent();
			event.setCustomer(currentCustomer);
			event.setTaxExemptionDocumentList(taxExemptionDocumentList);
			eventService.publishEvent(event);
		}
		catch (final GPCommerceSaveDataException e)
		{
			LOG.error("GPCommerceSaveDataException while saving tax exemption documents"+e.getMessage(),e);
			throw new GPCommerceSaveDataException(ERROR_3312, "Error while saving tax exemption documents");
		}
		catch (final Exception e)
		{
			LOG.error("Error while saving tax exemption documents");
			throw new GPCommerceSaveDataException(ERROR_3312, "Error while saving tax exemption documents",e);
		}
	}


	/**
	 * This method overloads the super class method. Takes two boolean flags along with customer model and address model
	 * and sets the address model as the default shipping and/or default billing address in customer model as indicated
	 * by the boolean flags
	 *
	 * @param customerModel
	 *           the customer model
	 * @param addressModel
	 *           the address model
	 * @param isDefaultShipping
	 *           whether the given address model a default shipping address
	 * @param isDefaultBilling
	 *           whether the given address model a default billing address
	 */
	public void setDefaultAddressEntry(final CustomerModel customerModel, final AddressModel addressModel,
			final boolean isDefaultShipping, final boolean isDefaultBilling)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		validateParameterNotNull(addressModel, "Address model cannot be null");
		if (customerModel.getAddresses().contains(addressModel))
		{
			if (isDefaultBilling)
			{
				customerModel.setDefaultPaymentAddress(addressModel);
			}
			if (isDefaultShipping)
			{
				customerModel.setDefaultShipmentAddress(addressModel);
			}
		}
		else
		{
			final AddressModel clone = getModelService().clone(addressModel);
			clone.setOwner(customerModel);
			getModelService().save(clone);
			final List<AddressModel> customerAddresses = new ArrayList<AddressModel>();
			customerAddresses.addAll(customerModel.getAddresses());
			customerAddresses.add(clone);
			customerModel.setAddresses(customerAddresses);
			if (isDefaultBilling)
			{
				customerModel.setDefaultPaymentAddress(clone);
			}
			if (isDefaultShipping)
			{
				customerModel.setDefaultShipmentAddress(clone);
			}
		}
		getModelService().save(customerModel);
	}

	/**
	 * This method overloads the super class method. Takes two boolean flags along with customer model and removes the
	 * default shipping and/or default billing address from customer model as indicated by the boolean flags
	 *
	 * @param customerModel
	 *           the customer model
	 * @param removeDefaultShipping
	 *           whether to clear the default shipping address
	 * @param removeDefaultBilling
	 *           whether to clear the default billing address
	 */
	public void clearDefaultAddressEntry(final CustomerModel customerModel, final boolean removeDefaultShipping,
			final boolean removeDefaultBilling)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		if (removeDefaultShipping)
		{
			customerModel.setDefaultShipmentAddress(null);
		}
		if (removeDefaultBilling)
		{
			customerModel.setDefaultPaymentAddress(null);
		}
		getModelService().save(customerModel);
	}

	/**
	 * updates customer personal details with given parameters
	 * 
	 * @param name         the name
	 * @param customerData the customer
	 * @return the customer
	 */
	public CustomerModel updatePersonalDetails(final String name ,final CustomerData customerData)
	{
		String cellPhone = customerData.getCellPhone();
		String gender = customerData.getGender();
		final CustomerModel currentUser = (CustomerModel) getUserService().getCurrentUser();
		final boolean hasCellPhoneChanged = (StringUtils.isNotEmpty(currentUser.getCellPhone()) && !currentUser.getCellPhone().equals(cellPhone)) || (StringUtils.isEmpty(currentUser.getCellPhone()) && StringUtils.isNotEmpty(cellPhone));
		final boolean hasGenderChanged = (null != currentUser.getGender() && !currentUser.getGender().getCode().equals(gender)) || (null == currentUser.getGender());
		final boolean hasDOBChanged = checkIfDOBUpdated(customerData, currentUser);
		if(!gpDefaultCustomerNameStrategy.getName(currentUser.getName()).equals(name) || hasCellPhoneChanged || hasGenderChanged ||hasDOBChanged) {
			currentUser.setName(name);
			currentUser.setCellPhone(cellPhone);
			setCustomerGenderDob(customerData, currentUser);
			getModelService().save(currentUser);
			getEventService().publishEvent(initializeEvent(new UpdateProfileEvent(), currentUser));
		}
		return currentUser;
	}

	private boolean checkIfDOBUpdated(final CustomerData customerData, final CustomerModel currentUser) {
		String updatedDOB = customerData.getDateOfBirth();
		Date dateOfBirth = currentUser.getDateOfBirth();
		String currentDOB= "";
		if(null != dateOfBirth)
		{
			SimpleDateFormat sdf= new SimpleDateFormat("dd-MM");
			currentDOB = sdf.format(dateOfBirth);
		}
		
		return (null == dateOfBirth && StringUtils.isNotEmpty(updatedDOB))|| (StringUtils.isNotEmpty(currentDOB) && StringUtils.isNotEmpty(updatedDOB) && !currentDOB.equalsIgnoreCase(updatedDOB)) ||(null != dateOfBirth && StringUtils.isEmpty(updatedDOB));
		
	}

	private void setCustomerGenderDob(final CustomerData customerData, CustomerModel customer) {
		String dateOfBirth = customerData.getDateOfBirth() + "-" + configurationService.getConfiguration().getString(LEAP_YEAR);
		if(StringUtils.isNotEmpty(dateOfBirth))
		{
			try {
				Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateOfBirth);
				customer.setDateOfBirth(date);
				LOG.info("DOB:" +dateOfBirth + ", date: " + date);
			} catch (ParseException e) {
				LOG.error("Exception while creating date :" +e);
			}
		}
		else
		{
			customer.setDateOfBirth(null);
		}
		String gender = customerData.getGender();
		if(StringUtils.isNotEmpty(gender) && null != gender)
		{
			customer.setGender(GenderEnum.valueOf(gender));
		}
		else {
			customer.setGender(null);
		}
	}
	
	/**
	 * disconnect/connect user from social account
	 * 
	 * @param customer the customer
	 * @param password the password
	 * @throws DuplicateUidException on duplicate uid
	 */
	public void connectOrDisconnect(final CustomerModel customer, final String password) throws DuplicateUidException
	{
		if (password != null)
		{
			getUserService().setPassword(customer, password, getPasswordEncoding());
		}
		internalSaveCustomer(customer);
	}
	/**
	 * to get address for b2b customer.
	 *
	 * @param String
	 *           the code
	 *  @param  String
	 *           userId
	 */
	@Override
	public AddressModel getAddressForB2BCustomer(final String code, final String userId)
	{
		final UserModel userModel = getUserService().getUserForUID(userId);
		final AddressModel address = super.getAddressForCode((CustomerModel) userModel, code);
		return address;
	}
	/**
	 * to get address book entries.
	 *
	 * @param CustomerModel
	 *           the customer model
	 */
	@Override
	public List<AddressModel> getAddressBookEntries(final CustomerModel customerModel)
	{
		 List<AddressModel> addressModels = new ArrayList<AddressModel>();

		validateParameterNotNull(customerModel, "Customer model cannot be null");
		if((customerModel instanceof B2BCustomerModel)
				&& (gpB2BUnitsService.isB2BAdmin((B2BCustomerModel)customerModel))){
			LOG.debug("b2b User is Admin");
			final List<B2BUnitModel> units = gpB2BUnitsService.getUnitsWithChildNodes((B2BCustomerModel)customerModel, true);
			addressModels=userDao.getAddressesForB2BUser(units,false);
		}
		else{
			for (final AddressModel address : customerModel.getAddresses())
			{
				if (Boolean.TRUE.equals(address.getVisibleInAddressBook())) {
					addressModels.add(address);
				}
			}
		}

		return addressModels;
	}
	/**
	 * to get address book entries.
	 *
	 * @param CustomerModel
	 *           the customer model
	 */
	@Override
    public List<AddressModel> getActiveAddressBookEntries(final CustomerModel customerModel,final Boolean fetchActiveAddresses)
    {
          List<AddressModel> addressModels = new ArrayList<>();

          validateParameterNotNull(customerModel, "Customer model cannot be null");
          if(customerModel instanceof B2BCustomerModel){
                      if (gpB2BUnitsService.isB2BAdmin((B2BCustomerModel)customerModel)) {
                            LOG.debug("b2b User is Admin");
                            final List<B2BUnitModel> units = gpB2BUnitsService
                                        .getUnitsWithChildNodes(
                                                    (B2BCustomerModel) customerModel, true);
                            addressModels = userDao
                                        .getAddressesForB2BUser(units, fetchActiveAddresses);
                      }else{
                            for (final AddressModel address : customerModel.getAddresses())
                            {
                                  if (GPApprovalEnum.ACTIVE.equals(address.getApprovalStatus()) && Boolean.TRUE.equals(address.getVisibleInAddressBook())) {
                                        addressModels.add(address);
                                  }
                            }
                      }
          }
          else{
                for (final AddressModel address : customerModel.getAddresses())
                {
                      if (Boolean.TRUE.equals(address.getVisibleInAddressBook())) {
                            addressModels.add(address);
                      }
                }
          }

          return addressModels;
    }

	@Override
	public void deleteAddressEntry(final CustomerModel customerModel, final AddressModel addressModel)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		validateParameterNotNull(addressModel, "Address model cannot be null");

		if (customerModel.getAddresses().contains(addressModel))
		{
			getModelService().remove(addressModel);
			getModelService().refresh(customerModel);

		}
		else
		{
			throw new IllegalArgumentException("Address " + addressModel + " does not belong to the customer " + customerModel
					+ " and will not be removed.");
		}
	}

	//overriding change uid method to set original uid without site id in it.
	@Override
	public void changeUid(final String newUid, final String currentPassword)
			throws DuplicateUidException, PasswordMismatchException
	{
		Assert.hasText(newUid, "The field [newEmail] cannot be empty");
		Assert.hasText(currentPassword, "The field [currentPassword] cannot be empty");

		final String uidDelimiter = configurationService.getConfiguration().getString(BASESITE_DELIMITER);
		final String newUserId = newUid.toLowerCase() + uidDelimiter + getBaseSiteService().getCurrentBaseSite().getUid();

		final String newUidLower = newUserId.toLowerCase();

		final CustomerModel currentUser = (CustomerModel) getUserService().getCurrentUser();
		currentUser.setOriginalUid(newUid);

		// check uniqueness only if the uids are case insensitive different
		if (!currentUser.getUid().equalsIgnoreCase(newUserId))
		{
			checkUidUniqueness(newUidLower);
		}
		adjustPassword(currentUser, newUidLower, currentPassword);
		getEventService().publishEvent(initializeEvent(new ChangeUIDEvent(), currentUser));
	}

	@Override
	public void updateCustomer(final CustomerModel customer)
	{
		if (null != customer)
		{
			modelService.save(customer);
		}
	}

	@Override
	public MediaModel convertMultipartFileToMedia(final MultipartFile multipartFile) throws ConversionException
	{
		final MediaModel mediaModel = getModelService().create(MediaModel.class);
		mediaModel.setCode(multipartFile.getOriginalFilename() + "_" + guidKeyGenerator.generate().toString());
		mediaModel.setMime(multipartFile.getContentType());
		mediaModel.setCatalogVersion(catalogVersionService.getSessionCatalogVersions().iterator().next());
		getModelService().save(mediaModel);
		//populate the stream after the media is saved
		//hybris doesn't allow to populate stream of unsaved medias, it throws IllegalStateException
		populateStream(multipartFile, mediaModel);
		return mediaModel;
	}

	@Override
	public void updateCCPaymentInfo(final CCPaymentInfoData paymentInfo)
	{
		final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
		final CreditCardPaymentInfoModel paymentInfoModel = getCreditCardPaymentInfoForCode(currentCustomer, paymentInfo.getId());
		cardPaymentInfoReverseConverter.convert(paymentInfo, paymentInfoModel);
		getModelService().saveAll(paymentInfoModel);
	}

	@Override
	public void addCCPaymentInfo(final CCPaymentInfoData paymentInfo)
	{
		final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
		final CreditCardPaymentInfoModel paymentInfoModel = getModelService().create(CreditCardPaymentInfoModel.class);
		cardPaymentInfoReverseConverter.convert(paymentInfo, paymentInfoModel);
		paymentInfoModel.setUser(currentCustomer);
		paymentInfoModel.setCode(currentCustomer.getUid() + "_" + UUID.randomUUID());
		getModelService().save(paymentInfoModel);
		if (null == paymentInfoModel.getBillingAddress() && null != paymentInfo.getBillingAddress())
		{
			createBillingAddress(paymentInfo, paymentInfoModel);
		}
		if (paymentInfo.isDefaultPaymentInfo())
		{
			setDefaultPaymentInfo(currentCustomer, paymentInfoModel);
		}

	}

	private void createBillingAddress(final CCPaymentInfoData paymentInfo, final CreditCardPaymentInfoModel paymentInfoModel)
	{
		final AddressModel address = getModelService().create(AddressModel.class);
		final AddressData data = paymentInfo.getBillingAddress();
		data.setTitleCode(GpcommerceCoreConstants.TITLE_MR);
		addressReverseConverter.convert(data, address);
		address.setBillingAddress(true);
		address.setOwner(paymentInfoModel);
		paymentInfoModel.setBillingAddress(address);
		getModelService().save(address);
		getModelService().save(paymentInfoModel);
	}

	/**
	 * Populates the stream of media model.
	 *
	 * @param multipartFile
	 * @param mediaModel
	 */
	private void populateStream(final MultipartFile multipartFile, final MediaModel mediaModel)
	{
		try (InputStream inputStream = multipartFile.getInputStream())
		{
			mediaService.setStreamForMedia(mediaModel, inputStream);
			mediaModel.setMime(multipartFile.getContentType());
			getModelService().save(mediaModel);
		}
		catch (final IOException e)
		{
			LOG.error("Error while populating stream from multi part file." +e.getMessage(),e);
			throw new GPCommerceSaveDataException(GpErrorConstants.ERROR_SUBMIT_TAX_EXEMPTION_DOCS,
					"Error while populating stream from multi part file.");
		}
	}

	@Override
	public void registerB2BCustomerEvent(final B2BCustomerModel b2bCustomerModel)
	{
		final String unitLevel = b2bCustomerModel.getDefaultB2BUnit().getB2bUnitLevel();
		if (!GpcommerceCoreConstants.B2B_UNIT_L1.equalsIgnoreCase(unitLevel)
				&& b2bCustomerModel.getUserApprovalStatus().getCode().equalsIgnoreCase(GpcommerceCoreConstants.PENDING))
		{
			final Collection<B2BCustomerModel> administrators = b2bUnitService
					.getUsersOfUserGroup(b2bCustomerModel.getDefaultB2BUnit(), B2BConstants.B2BADMINGROUP, false);

			final GPEmailItemEvent item = new GPEmailItemEvent();
			item.setBccEmail(cmsSiteService.getSiteConfig((BUSINESS_USER_EMAIL)));
			item.setInvitedCustomer(b2bCustomerModel);
			final List<String> listEmails=new ArrayList<>();
			B2BCustomerModel adminUser= null!=b2bCustomerModel.getB2bAdmin()?b2bCustomerModel.getB2bAdmin():(B2BCustomerModel) new ArrayList(administrators).get(0);
			
			if(adminUser instanceof B2BCustomerModel)
			{
				listEmails.add(adminUser.getEmail());
			}
			item.setToEmails(listEmails);
			if(LOG.isDebugEnabled())
			{
				LOG.debug("In DefaultGPCustomerService list of emails:"+listEmails.toString());
			}
			item.setEmailSubject(EmailSubjectType.NEW_USER_REVIEW.getSubject());
			eventService.publishEvent(initializeEvent(item, adminUser,b2bCustomerModel.getSite()));
		}
		else
		{
			final GPEmailItemEvent item = new GPEmailItemEvent();
			item.setEmailSubject(EmailSubjectType.CUSTOMER_REGISTRATION.getSubject());
			item.setBackOfficeUser(false);
			eventService.publishEvent(initializeEvent(item, b2bCustomerModel));
			b2bCustomerModel.setRegistrationEmailSent(Boolean.TRUE);
		}
		modelService.save(b2bCustomerModel);
	}
	
	@Override
	public void changePassword(final UserModel userModel, final String oldPassword, final String newPassword)
			throws PasswordMismatchException
	{
		validateParameterNotNullStandardMessage("customerModel", userModel);
		if (!getUserService().isAnonymousUser(userModel))
		{
			if (getPasswordEncoderService().isValid(userModel, oldPassword))
			{
				getUserService().setPassword(userModel, newPassword, userModel.getPasswordEncoding());
				getModelService().save(userModel);
				final CustomerModel customer = getUserService().getUserForUID(userModel.getUid(), CustomerModel.class);
				getEventService().publishEvent(initializeEvent(new PasswordResetEvent(), customer));
			}
			else
			{
				throw new PasswordMismatchException(userModel.getUid());
			}
		}
	}

	/**
	 * This method downloads the pdf file with user data
	 * 
	 * @param pdfDownloadUserData the {@link PdfDownloadUserData}
	 */
	public void pdfDownloadUserDetails(PdfDownloadUserData pdfDownloadUserData) {
		GPPdfDownloadUserDetailsModel pdfDownloadUserDetailsModel = modelService.create(GPPdfDownloadUserDetailsModel.class);
		gpPdfUserDetailsReverseConverter.convert(pdfDownloadUserData, pdfDownloadUserDetailsModel);
		final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
		currentCustomer.setPdfDownloadUserDetails(pdfDownloadUserDetailsModel);
		modelService.save(currentCustomer);
	}
}

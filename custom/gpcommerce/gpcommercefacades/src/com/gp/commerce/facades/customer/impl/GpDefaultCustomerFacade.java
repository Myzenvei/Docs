/**
 *
 */
package com.gp.commerce.facades.customer.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.gp.commerce.core.constants.GpErrorConstants;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.GPLoginType;
import com.gp.commerce.core.enums.GPUserApprovalStatusEnum;
import com.gp.commerce.core.enums.GenderEnum;
import com.gp.commerce.core.enums.TaxExemptionStatusEnum;
import com.gp.commerce.core.exceptions.GPCommerceBusinessException;
import com.gp.commerce.core.exceptions.GPCommerceNoDataException;
import com.gp.commerce.core.exceptions.GPCommerceSaveDataException;
import com.gp.commerce.core.exceptions.GPIllegalArgumentException;
import com.gp.commerce.core.exceptions.SocialLoginPasswordResetException;
import com.gp.commerce.core.services.impl.DefaultGPCustomerAccountService;
import com.gp.commerce.core.strategies.GPDefaultCustomerNameStrategy;
import com.gp.commerce.core.util.GPSiteConfigUtils;
import com.gp.commerce.facades.company.GPB2BUnitsFacade;
import com.gp.commerce.facades.customer.GpCustomerFacade;
import com.gp.commerce.facades.data.PdfDownloadUserData;
import com.gp.commerce.facades.marketing.data.UpdatePreferenceData;
import com.gp.commerce.facades.populators.GPPdfUserDetailsPopulator;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.service.GPYMarketingSyncService;
import com.gpintegration.service.impl.GPDefaultCommerceKochAuthService;
import com.gpintegration.service.impl.GPDefaultCommerceSocialAccountService;
import com.gpintegration.service.impl.GPDefaultQuplesTokenService;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.QuplesData;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.commerceservices.security.SecureTokenService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;




/**
 * The Class GpDefaultCustomerFacade.
 *
 * @author vdannina
 */
public class GpDefaultCustomerFacade extends DefaultCustomerFacade implements GpCustomerFacade
{
	private static final String Y = "y";

	private static final Logger LOG = Logger.getLogger(GpDefaultCustomerFacade.class);

	private static final String BASESITE_DELIMITER = "gpcommercewebservices.user.delimiter";
	private static final String ERROR_196 = "196";
	private static final String ERROR_198 = "198";
	private static final String B2BUNITL1="L1";
	private static final String LEAP_YEAR = "config.dob.leap.year";
	private static final String REGISTER_DATA = "registerData";
	private static final String B2BADMIN_GROUP = "b2badmingroup";
	private static final String L1USER_GROUP = "L1UserGroup";
	private static final String GUEST_B2BUNIT = "guest.b2b.default.unit";

	private static final String SUCCESS = "SUCCESS";

	private static final String FAILURE = "FAILURE";
	private DefaultGPCustomerAccountService gpCustomerAccountService;
	private ConfigurationService configurationService;
	private BaseSiteService baseSiteService;
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
	private GPDefaultCommerceSocialAccountService socialAccountService;
	private GPDefaultCommerceKochAuthService gpCommerceKochAuthService;
	private CatalogVersionService catalogVersionService;
	private MediaService mediaService;
	private Converter<CustomerData, B2BCustomerModel> b2BCustomerReverseConverter;
	private KeyGenerator guidKeyGenerator;
	private ModelService modelService;
	private long tokenValiditySeconds;
	private GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy;
	private GPDefaultQuplesTokenService quplesService;
	private GPPdfUserDetailsPopulator gpDefaultPdfUserDetailsPopulator;


	@Resource
	private SecureTokenService secureTokenService;

	@Resource(name = "eventService")
	private EventService eventService;

	@Resource(name = "customerConverter")
	private Converter<UserModel, CustomerData> customerConverter;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	@Resource(name = "b2BCustomerConverter")
	protected Converter<B2BCustomerModel, CustomerData> b2BCustomerConverter;

	@Resource(name="gpYMarketingSyncService")
	private GPYMarketingSyncService yMarketingService;
	
	@Resource(name = "gpB2BUnitsFacade")
	private GPB2BUnitsFacade gpB2BUnitsFacade ;
	
	@Resource(name = "sessionService")
	private SessionService sessionService;

	public KeyGenerator getGuidKeyGenerator()
	{
		return guidKeyGenerator;
	}

	@Required
	public void setGuidKeyGenerator(final KeyGenerator guidKeyGenerator)
	{
		this.guidKeyGenerator = guidKeyGenerator;
	}



	/**
	 * Register a user with given parameters
	 *
	 * @param registerData
	 *           the user data the user will be registered with
	 * @throws DuplicateUidException
	 *            if the login is not unique
	 * create B2B and B2C Customer
	 */
	@Override
	public void register(RegisterData registerData) throws DuplicateUidException,GPIllegalArgumentException
	{
		if(StringUtils.equalsIgnoreCase(GPLoginType.GOOGLE.getCode(),registerData.getLoginType()) || StringUtils.equalsIgnoreCase(GPLoginType.FACEBOOK.getCode(),registerData.getLoginType())){
			registerData=socialAccountService.getRegisterData(registerData);
		}

		validateParameterNotNullStandardMessage(REGISTER_DATA, registerData);
		Assert.hasText(registerData.getFirstName(), "The field [FirstName] cannot be empty");
		Assert.hasText(registerData.getLastName(), "The field [LastName] cannot be empty");
		Assert.hasText(registerData.getLogin(), "The field [Login] cannot be empty");

		if(GPSiteConfigUtils.isB2BSite(baseSiteService.getCurrentBaseSite())) {
			createB2bCustomer(registerData);
		}else {
			createB2cCustomer(registerData);
		}

	}
	
	
	public RegisterData registerUser(RegisterData registerData) throws DuplicateUidException,GPIllegalArgumentException
	{
		if(StringUtils.equalsIgnoreCase(GPLoginType.GOOGLE.getCode(),registerData.getLoginType()) || StringUtils.equalsIgnoreCase(GPLoginType.FACEBOOK.getCode(),registerData.getLoginType())){
			registerData=socialAccountService.getRegisterData(registerData);
		}

		if(null==registerData.getLogin() && StringUtils.equalsIgnoreCase(GPLoginType.FACEBOOK.getCode(),registerData.getLoginType()) )
		{
			return registerData;
		}
		validateParameterNotNullStandardMessage(REGISTER_DATA, registerData);
		Assert.hasText(registerData.getFirstName(), "The field [FirstName] cannot be empty");
		Assert.hasText(registerData.getLastName(), "The field [LastName] cannot be empty");
		Assert.hasText(registerData.getLogin(), "The field [Login] cannot be empty");

		if(GPSiteConfigUtils.isB2BSite(baseSiteService.getCurrentBaseSite())) {
			createB2bCustomer(registerData);
		}else {
			createB2cCustomer(registerData);
		}
		
		return registerData;

	}
	

	public RegisterData getKochAuthToken(final String authorizationCode) {
		final RegisterData inputData = new RegisterData();
		RegisterData kochAuthTokenDetails = null;
		inputData.setToken(authorizationCode);
		kochAuthTokenDetails = gpCommerceKochAuthService.getKochAuthToken(inputData);
		return kochAuthTokenDetails;
	}

	/**
	 * Creates the B 2 b customer.
	 *
	 * @param registerData the register data
	 * @throws DuplicateUidException the duplicate uid exception
	 */
	private void createB2bCustomer(final RegisterData registerData) throws DuplicateUidException {
		final B2BCustomerModel b2bCustomer = getModelService().create(B2BCustomerModel.class);
		b2bCustomer.setName(getGpDefaultCustomerNameStrategy().getName(registerData.getFirstName(), registerData.getLastName()));

		if (StringUtils.isNotBlank(registerData.getFirstName()) && StringUtils.isNotBlank(registerData.getLastName()))
		{
			b2bCustomer.setName(getGpDefaultCustomerNameStrategy().getName(registerData.getFirstName(), registerData.getLastName()));
		}
		b2bCustomer.setSite(baseSiteService.getCurrentBaseSite());
		b2bCustomer.setCountry(registerData.getCountry());
		b2bCustomer.setAddToMarketComm(registerData.getAddToMarketComm());
		b2bCustomer.setAddToAffilatedMarketComm(registerData.getAddToAffilatedMarketComm());
		setUidForRegister(registerData, b2bCustomer);
		// To add B2bUnit which doesn't have any Parent Organization ,user needs to have the admin rights
		final PrincipalGroupModel adminModel = getUserService().getUserGroupForUID("b2badmingroup");
		b2bCustomer.setDefaultB2BUnit(createB2bUnit(registerData.getLogin()));
		b2bCustomer.setUserApprovalStatus(GPUserApprovalStatusEnum.APPROVED);
		b2bCustomer.setPrimaryAdmin(true);
		b2bCustomer.setIsBackofficeCreated(false);
		b2bCustomer.setLeaseSigner(true);
		setCustomerGenderDob(registerData, b2bCustomer);
		final PrincipalGroupModel l1UserGroup = getUserService().getUserGroupForUID("L1UserGroup");

		final Set<PrincipalGroupModel> groups = new HashSet<>();
		groups.add(adminModel);
		groups.add(l1UserGroup);
		getUserService().getCurrentUser().setGroups(groups);
		b2bCustomer.setGroups(groups);
		b2bCustomer.setEmail(registerData.getLogin());
		getGpCustomerAccountService().registerB2bUnit(b2bCustomer, registerData.getPassword());

		updateYMarketing(registerData, b2bCustomer);
	}

	private void setCustomerGenderDob(final RegisterData registerData, CustomerModel customer) {
		if(StringUtils.isNotEmpty(registerData.getDateOfBirth()))
		{
			String dateOfBirth = registerData.getDateOfBirth() + "-" + configurationService.getConfiguration().getString(LEAP_YEAR);
			try {
				Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateOfBirth);
				customer.setDateOfBirth(date);
				LOG.info("DOB:" +dateOfBirth + ", date: " + date);
			} catch (ParseException e) {
				LOG.error("Exception while creating date :" +e);
			}
		}
		String gender = registerData.getGender();
		if(StringUtils.isNotEmpty(gender) && StringUtils.isNotBlank(gender))
		{
			customer.setGender(GenderEnum.valueOf(gender));
		}
		else
		{
			customer.setGender(null);
		}
	}

	private void updateYMarketing(final RegisterData registerData, final CustomerModel customer) {
		if(null!=registerData.getAddToMarketComm()) {
			final UpdatePreferenceData request = new UpdatePreferenceData();
			request.setFirstName(registerData.getFirstName());
			request.setLastName(registerData.getLastName());
			request.setOpt(registerData.getAddToMarketComm()?"Y":"N");
			request.setEmail(registerData.getLogin());
			try {
				createUpdateContact(request, true);
			}catch(final Exception e) {
				LOG.error("yMarketing call failed.", e);
				customer.setAddToMarketComm(false);
				getGpCustomerAccountService().updateCustomer(customer);
			}
		}
	}

	private B2BUnitModel createB2bUnit(final String userId) {
		final B2BUnitModel b2bUnit = getModelService().create(B2BUnitModel.class);
		b2bUnit.setUid(userId+System.currentTimeMillis());
		b2bUnit.setName(userId+System.currentTimeMillis());
		b2bUnit.setLocName(userId+System.currentTimeMillis());
		b2bUnit.setB2bUnitLevel(B2BUNITL1);
		return b2bUnit;
	}

	/**
	 * Creates the B 2 B user group.
	 *
	 * @param userId the user id
	 * @param b2bCustomer the b 2 b customer
	 * @param adminModel the admin model
	 */
	B2BUserGroupModel createB2BUserGroup(final String userId,final B2BCustomerModel b2bCustomer,final PrincipalGroupModel adminModel,final B2BUnitModel unit){
		final B2BUserGroupModel userGroup=getModelService().create(B2BUserGroupModel.class);
		userGroup.setUid(userId+System.currentTimeMillis());
		userGroup.setUnit(unit);
		userGroup.setName(userId);
		final Set<PrincipalModel> groups=new HashSet<>();
		groups.add(adminModel);
		userGroup.setMembers(groups);

		return userGroup;
	}
	/**
	 * Creates the B 2 c customer.
	 *
	 * @param registerData the register data
	 * @throws DuplicateUidException the duplicate uid exception
	 */
	private void createB2cCustomer(final RegisterData registerData) throws DuplicateUidException{
		final CustomerModel b2cCustomer = getModelService().create(CustomerModel.class);
		b2cCustomer.setName(getGpDefaultCustomerNameStrategy().getName(registerData.getFirstName(), registerData.getLastName()));

		if (StringUtils.isNotBlank(registerData.getFirstName()) && StringUtils.isNotBlank(registerData.getLastName()))
		{
			b2cCustomer.setName(getGpDefaultCustomerNameStrategy().getName(registerData.getFirstName(), registerData.getLastName()));
		}
		if(null!=registerData.getFbUniqueId())
		{
			registerData.setLoginType(GPLoginType.FACEBOOK.getCode().toLowerCase());
		}
		if (null != registerData.getLoginType()) {
			b2cCustomer.setLoginType(registerData.getLoginType());
		}

		if(null != registerData.getKochAuthTS()) {
			b2cCustomer.setKochAuthTS(registerData.getKochAuthTS());
		}
		if(null != registerData.getKochAuthAccessToken()) {
			b2cCustomer.setKochAuthAccessToken(registerData.getKochAuthAccessToken());
		}
		if(null != registerData.getKochAuthIdToken()) {
			b2cCustomer.setKochAuthIdToken(registerData.getKochAuthIdToken());
		}
		if(null != registerData.getKochAuthRefreshToken()) {
			b2cCustomer.setKochAuthRefreshToken(registerData.getKochAuthRefreshToken());
		}

		if(null != registerData.getKochEmailId()) {
			b2cCustomer.setKochEmailId(registerData.getKochEmailId());
		}
		
		if(null!=registerData.getFbUniqueId())
		{
			b2cCustomer.setFbUniqueId(registerData.getFbUniqueId());
		}
		b2cCustomer.setSite(baseSiteService.getCurrentBaseSite());
		b2cCustomer.setCountry(registerData.getCountry());
		b2cCustomer.setAddToMarketComm(registerData.getAddToMarketComm());
		b2cCustomer.setAddToAffilatedMarketComm(registerData.getAddToAffilatedMarketComm());
		setUidForRegister(registerData, b2cCustomer);
		b2cCustomer.setSessionLanguage(getCommonI18NService().getCurrentLanguage());
		b2cCustomer.setSessionCurrency(getCommonI18NService().getCurrentCurrency());
		setCustomerGenderDob(registerData, b2cCustomer);
		getCustomerAccountService().register(b2cCustomer, registerData.getPassword());
		updateYMarketing(registerData, b2cCustomer);
	}

	/**
	 * Initializes a customer with given registerData
	 */
	@Override
	protected void setUidForRegister(final RegisterData registerData, final CustomerModel customer)
	{
		final String uidDelimiter = configurationService.getConfiguration().getString(BASESITE_DELIMITER);
		customer.setUid(registerData.getLogin().toLowerCase() + uidDelimiter + registerData.getBaseSiteId());
		customer.setOriginalUid(registerData.getLogin());
	}

	@Override
	public void updatePassword(final String token, final String newPassword) throws TokenInvalidatedException
	{
		getGpCustomerAccountService().updatePassword(token, newPassword);
	}


	@Override
	public Boolean verfiyTokenValidity(final String token)
	{
		return getGpCustomerAccountService().verfiyTokenValidity(token);
	}

	@Override
	public void forgottenPassword(final String uid)
	{
		Assert.hasText(uid, "The field [uid] cannot be empty");
		CustomerModel customerModel = null;
		try {
			customerModel = getUserService().getUserForUID(uid.toLowerCase(), CustomerModel.class);
		} catch (final Exception e) {
			// This is a temporary fix. Need to be fixed once Exception Advisory class is built
			throw new BadCredentialsException(ERROR_198, e);
		}
		final String loginType = customerModel.getLoginType();
		if (!StringUtils.equalsIgnoreCase(GPLoginType.GOOGLE.getCode(), loginType) && !StringUtils.equalsIgnoreCase(GPLoginType.FACEBOOK.getCode(), loginType))
		{
			getCustomerAccountService().forgottenPassword(customerModel);
		}
		else
		{
			// This is a temporary fix. Need to be fixed once Exception Advisory class is built
			throw new SocialLoginPasswordResetException(ERROR_196);
		}
	}

	/**
	 * This method returns the tax exemption status of the customer.
	 *
	 * @return
	 * @throws GPCommerceNoDataException
	 */
	@Override
	public String getCustomerTaxExemptionStatus()
	{
		final CustomerModel customerModel = (CustomerModel) getUserService().getCurrentUser();
		final TaxExemptionStatusEnum taxExemptionStatus = customerModel.getTaxExemptionStatus();
		if (null != taxExemptionStatus ) {
			return taxExemptionStatus.getCode();
		}
		return TaxExemptionStatusEnum.NOT_SUBMITTED.getCode();

	}

	/**
	 * This method uploads tax exemption documents submitted by the customer.
	 *
	 * @param taxExemptionRequestDocumentArray
	 * @throws GPCommerceSaveDataException
	 */
	@Override
	public void submitTaxExemptionDocuments(final MultipartFile[] taxExemptionRequestDocumentArray) throws GPCommerceSaveDataException
	{
		try
		{
			final List<MediaModel> taxExemptionRequestDocumentModelList = new ArrayList<>();
			for(final MultipartFile taxExemptionRequestDocument : taxExemptionRequestDocumentArray)
			{
				final MediaModel mediaModel = getGpCustomerAccountService().convertMultipartFileToMedia(taxExemptionRequestDocument);
				taxExemptionRequestDocumentModelList.add(mediaModel);
			}
			getGpCustomerAccountService().submitTaxExemptionDocuments(taxExemptionRequestDocumentModelList);
		}
		catch(final GPCommerceSaveDataException e)
		{
			LOG.error(e.getMessage() , e);
			throw new GPCommerceSaveDataException(GpErrorConstants.ERROR_SUBMIT_TAX_EXEMPTION_DOCS, "Error while saving tax exemption documents");
		}
	}

	/**
	 * updates customer personal details with given parameters
	 *
	 * @param customerData
	 * 			the user data the user was registered with
	 * @throws DuplicateUidException
	 * 			if the login is not unique
	 */
	@Override
	public CustomerData updatePersonalDetails(final CustomerData customerData) throws DuplicateUidException
	{
		validateParameterNotNullStandardMessage("CustomerData", customerData);
		Assert.hasText(customerData.getFirstName(), "The field [FirstName] cannot be empty");
		Assert.hasText(customerData.getLastName(), "The field [LastName] cannot be empty");
		String name = "";

		if (StringUtils.isNotBlank(customerData.getFirstName()) && StringUtils.isNotBlank(customerData.getLastName()))
		{
			name = getGpDefaultCustomerNameStrategy().getName(customerData.getFirstName(), customerData.getLastName());
		}

		final CustomerModel customer = getGpCustomerAccountService().updatePersonalDetails(name, customerData);
		return getCustomerConverter().convert(customer);
	}

	/**
	 * disconnect user from social account
	 *
	 * @param registerData
	 * 			the user data the user was registered with
	 * @throws DuplicateUidException
	 * 			if the login is not unique
	 * @throws IllegalArgumentException
	 * 			if required data is missing
	 */
	@Override
	public void disconnect(final RegisterData registerData) throws DuplicateUidException,IllegalArgumentException
	{
		validateParameterNotNullStandardMessage(REGISTER_DATA, registerData);
		Assert.hasText(registerData.getLogin(), "The field [Login] cannot be empty");
		Assert.hasText(registerData.getLoginType(), "The field [LoginType] cannot be empty");

		final CustomerModel currentUser = getCurrentSessionCustomer();

		if (null != registerData.getLoginType()) {
			if(StringUtils.equalsIgnoreCase(GPLoginType.GOOGLE.getCode(),registerData.getLoginType()) || StringUtils.equalsIgnoreCase(GPLoginType.FACEBOOK.getCode(),registerData.getLoginType())){
				currentUser.setLoginType(GPLoginType.GPEMPLOYEE.getCode());
			}
		}

		setUidForRegister(registerData, currentUser);
		getGpCustomerAccountService().connectOrDisconnect(currentUser, registerData.getPassword());
	}

	/**
	 * connect user to social account
	 *
	 * @param registerData
	 * 			the user data the user was registered with
	 * @throws DuplicateUidException
	 * 			if the login is not unique
	 */
	@Override
	public void connect(RegisterData registerData) throws DuplicateUidException
	{
		if(StringUtils.equalsIgnoreCase(GPLoginType.GOOGLE.getCode(),registerData.getLoginType()) || StringUtils.equalsIgnoreCase(GPLoginType.FACEBOOK.getCode(),registerData.getLoginType())){
			registerData=socialAccountService.getRegisterData(registerData);
		}

		validateParameterNotNullStandardMessage(REGISTER_DATA, registerData);
		Assert.hasText(registerData.getLogin(), "The field [Login] cannot be empty");
		Assert.hasText(registerData.getLoginType(), "The field [LoginType] cannot be empty");

		final CustomerModel currentUser = getCurrentSessionCustomer();

		if (null != registerData.getLoginType()) {
			currentUser.setLoginType(registerData.getLoginType());
		}

		setUidForRegister(registerData, currentUser);
		getGpCustomerAccountService().connectOrDisconnect(currentUser, registerData.getPassword());
	}

	@Override
	public CustomerData updateCustomer(final CustomerData customerData, final Boolean isEmailChanged)
	{
		validateParameterNotNullStandardMessage("customerData", customerData);

		Assert.hasText(customerData.getFirstName(), "The field [FirstName] cannot be empty");
		Assert.hasText(customerData.getLastName(), "The field [LastName] cannot be empty");
		B2BCustomerModel customerModel = null;
		Boolean isCreate = false;
		if (!customerData.getUid().isEmpty())
		{
			try
			{
				customerModel = getUserService().getUserForUID(customerData.getUid(), B2BCustomerModel.class);
			}
			catch (final UnknownIdentifierException e)
			{
				customerModel = null;
			}
		}
		if (null == customerModel)
		{
			customerModel = this.getModelService().create(B2BCustomerModel.class);
			isCreate = true;
		}
		customerData.setCellPhone(customerModel.getCellPhone());
		customerModel = getB2BCustomerReverseConverter().convert(customerData, customerModel);
		
		final String baseSiteId = baseSiteService.getCurrentBaseSite().getUid();
		final String uidDelimiter = configurationService.getConfiguration().getString(BASESITE_DELIMITER);
		//correcting uid population from reverse populator
		customerModel.setUid(customerData.getEmail().toLowerCase() + uidDelimiter + baseSiteId);
		if (isCreate)
		{
			customerModel.setOriginalUid(customerData.getUid());
			customerModel.setCustomerID(generateGUID());
			customerModel.setType(CustomerType.REGISTERED);
			customerModel.setUserApprovalStatus(GPUserApprovalStatusEnum.APPROVED);
			customerModel.setIsBackofficeCreated(false);
			//Setting Active to false explicitly for L3 users when added for the first time
			if (customerModel instanceof B2BCustomerModel
					&& !B2BUNITL1.equalsIgnoreCase(customerModel.getDefaultB2BUnit().getB2bUnitLevel()))
			{
				customerModel.setActive(false);
				customerModel.setUserApprovalStatus(GPUserApprovalStatusEnum.PENDING);
				//YCOM-8446 : Set token for Newly Created Buyers/Admins
				final long timeStamp = getTokenValiditySeconds() > 0L ? new Date().getTime() : 0L;
				final SecureToken data = new SecureToken(customerModel.getUid(), timeStamp);
				final String token = getSecureTokenService().encryptData(data);
				customerModel.setToken(token);
			}


			//update user group for all customers of units list only for L1/L2
			final List<String> b2bUnitLevels = Arrays.asList(Config.getParameter(GpcommerceCoreConstants.B2B_UNITLEVEL_CODES).split(","));
			final String b2bUnitLevel = customerModel.getDefaultB2BUnit().getB2bUnitLevel();
			if (StringUtils.isNotEmpty(b2bUnitLevel) && b2bUnitLevels.contains(b2bUnitLevel)){
				final Set<PrincipalGroupModel> groups = new HashSet<>(customerModel.getGroups());
				final UserGroupModel userGroupModel = userService.getUserGroupForUID(b2bUnitLevel+ "UserGroup");
				groups.add(userGroupModel);
				customerModel.setGroups(groups);
			}
			customerModel.setB2bAdmin(userService.getCurrentUser() instanceof B2BCustomerModel ?((B2BCustomerModel)userService.getCurrentUser()):null);
			if (LOG.isDebugEnabled()) {
				if(null!=customerModel.getB2bAdmin())
				{
					LOG.debug("GPDefaultCUstomerFacade current b2b admin set to customer added"+customerModel.getB2bAdmin().getEmail());
				}
			}
			getGpCustomerAccountService().registerB2BCustomerEvent(customerModel);
		}
		else
		{
			if (isEmailChanged)
			{
				customerModel.setNewEmail(customerData.getNewEmail());

				if (customerModel instanceof B2BCustomerModel
						&& !"L1".equalsIgnoreCase(customerModel.getDefaultB2BUnit().getB2bUnitLevel()))
				{
					customerModel.setUserApprovalStatus(GPUserApprovalStatusEnum.PENDING);
					customerModel.setB2bAdmin(userService.getCurrentUser() instanceof B2BCustomerModel ?((B2BCustomerModel)userService.getCurrentUser()):null);
					
				}
			}
		}
		modelService.save(customerModel);
		getGpCustomerAccountService().updateCustomer(customerModel);
		return b2BCustomerConverter.convert(customerModel);
	}

	@Override
	public String createUpdateContact(final UpdatePreferenceData userPreferences, final boolean create) {
		try{
			yMarketingService.createOrUpdateContact(userPreferences, create);
		}catch(final GPCommerceBusinessException e) {
			LOG.error(e.getMessage(), e);
			return FAILURE;
		}
		return SUCCESS;
	}


	//overriding change uid method to set original uid without site id in it.
	@Override
	public void changeUid(final String newUid, final String currentPassword)
			throws DuplicateUidException, PasswordMismatchException
	{
		try
		{
			getGpCustomerAccountService().changeUid(newUid, currentPassword);
		}
		catch (final de.hybris.platform.commerceservices.customer.PasswordMismatchException pse)
		{
			throw new PasswordMismatchException(pse);
		}

	}

	public QuplesData createQuplesToken(final UpdatePreferenceData userPreferences)
	{
		QuplesData quplesReponse = null;

		try
		{
			yMarketingService.createOrUpdateContact(userPreferences, true);
			final QuplesData quplesRequest = new QuplesData();
			quplesRequest.setEmail(userPreferences.getEmail());
			boolean optIn = (null!=userPreferences.getOpt() && StringUtils.equalsIgnoreCase(Y, userPreferences.getOpt()))?true:false;
			quplesRequest.setOptIn(optIn);
			quplesReponse = quplesService.getQuplesToken(quplesRequest);
		}
		catch (final GPCommerceBusinessException | GPIntegrationException e)
		{
			LOG.error(e.getMessage(), e);
		}
		return quplesReponse;
	}

	@Override
	public void changePassword(final String oldPassword, final String newPassword) throws PasswordMismatchException
	{
		final UserModel currentUser = getCurrentUser();
		try
		{
			getGpCustomerAccountService().changePassword(currentUser, oldPassword, newPassword);
		}
		catch (final de.hybris.platform.commerceservices.customer.PasswordMismatchException e)
		{
			throw new PasswordMismatchException(e);
		}
	}
	
	@Override
	public void pdfDownloadUserDetails(PdfDownloadUserData pdfDownloadUserData) {
			getGpCustomerAccountService().pdfDownloadUserDetails(pdfDownloadUserData);
	}
	
	@Override
	public  PdfDownloadUserData getPdfDownloadUserDetails() {
		
		PdfDownloadUserData pdfDownloadUserData = new PdfDownloadUserData();
		final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
		getGpDefaultPdfUserDetailsPopulator().populate(currentCustomer, pdfDownloadUserData);
		return pdfDownloadUserData;
	}
	
	@Override
	public void createGuestUserForAnonymousCheckout(final String email, final String name) throws DuplicateUidException
	{
		validateParameterNotNullStandardMessage("email", email);
		final String guid = generateGUID();
		// Setting b2bunit, b2badmingropup and L1UserGroup to guest user
		if (GPSiteConfigUtils.isB2BSite(baseSiteService.getCurrentBaseSite())) {
			createB2BGuestUser(email, guid, name);
		} else {
			final CustomerModel guestCustomer = getModelService().create(CustomerModel.class);
			// takes care of localizing the name based on the site language
			guestCustomer.setUid(guid + "|" + email);
			guestCustomer.setName(name);
			guestCustomer.setType(CustomerType.valueOf(CustomerType.GUEST.getCode()));
			guestCustomer.setSessionLanguage(getCommonI18NService().getCurrentLanguage());
			guestCustomer.setSessionCurrency(getCommonI18NService().getCurrentCurrency());
			getCustomerAccountService().registerGuestForAnonymousCheckout(guestCustomer, guid);
			updateCartWithGuestForAnonymousCheckout(getCustomerConverter().convert(guestCustomer));
		}
	}

	private void createB2BGuestUser(String email, String guid, String name)
			throws DuplicateUidException {
		final B2BCustomerModel b2bCustomer = getModelService().create(B2BCustomerModel.class);
		b2bCustomer.setUid(guid + "|" + email);
		b2bCustomer.setName(name);
		b2bCustomer.setType(CustomerType.valueOf(CustomerType.GUEST.getCode()));
		b2bCustomer.setSessionLanguage(getCommonI18NService().getCurrentLanguage());
		b2bCustomer.setSessionCurrency(getCommonI18NService().getCurrentCurrency());
		b2bCustomer.setSite(baseSiteService.getCurrentBaseSite());
		final PrincipalGroupModel adminModel = getUserService().getUserGroupForUID(B2BADMIN_GROUP);
		B2BUnitModel b2bUnit = b2bUnitService.getUnitForUid(configurationService.getConfiguration().getString(GUEST_B2BUNIT));
		if (null == b2bUnit) {
			b2bUnit = getModelService().create(B2BUnitModel.class);
		}
		b2bCustomer.setDefaultB2BUnit(b2bUnit);
		b2bCustomer.setUserApprovalStatus(GPUserApprovalStatusEnum.APPROVED);
		b2bCustomer.setPrimaryAdmin(Boolean.FALSE);
		final PrincipalGroupModel l1UserGroup = getUserService().getUserGroupForUID(L1USER_GROUP);

		final Set<PrincipalGroupModel> groups = new HashSet<>();
		groups.add(adminModel);
		groups.add(l1UserGroup);
		getUserService().getCurrentUser().setGroups(groups);
		b2bCustomer.setGroups(groups);
		b2bCustomer.setEmail(email);
		getCustomerAccountService().registerGuestForAnonymousCheckout(b2bCustomer, guid);
		updateCartWithGuestForAnonymousCheckout(getCustomerConverter().convert(b2bCustomer));
	}

	public DefaultGPCustomerAccountService getGpCustomerAccountService()
	{
		return gpCustomerAccountService;
	}

	public void setGpCustomerAccountService(final DefaultGPCustomerAccountService gpCustomerAccountService)
	{
		this.gpCustomerAccountService = gpCustomerAccountService;
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public B2BUnitService<B2BUnitModel, B2BCustomerModel> getB2bUnitService()
	{
		return b2bUnitService;
	}

	public void setB2bUnitService(final B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}

	public GPDefaultCommerceSocialAccountService getSocialAccountService()
	{
		return socialAccountService;
	}

	public void setSocialAccountService(final GPDefaultCommerceSocialAccountService socialAccountService)
	{
		this.socialAccountService = socialAccountService;
	}

	public GPDefaultCommerceKochAuthService getGpCommerceKochAuthService()
	{
		return gpCommerceKochAuthService;
	}

	public void setGpCommerceKochAuthService(final GPDefaultCommerceKochAuthService gpCommerceKochAuthService)
	{
		this.gpCommerceKochAuthService = gpCommerceKochAuthService;
	}

	public String getUserId(final String token)
	{
		return getGpCustomerAccountService().getUserId(token);
	}

	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	protected MediaService getMediaService()
	{
		return mediaService;
	}

	@Required
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}


	public Converter<CustomerData, B2BCustomerModel> getB2BCustomerReverseConverter()
	{
		return b2BCustomerReverseConverter;
	}

	@Required
	public void setB2BCustomerReverseConverter(final Converter<CustomerData, B2BCustomerModel> b2bCustomerReverseConverter)
	{
		b2BCustomerReverseConverter = b2bCustomerReverseConverter;
	}


	public long getTokenValiditySeconds() {
		return tokenValiditySeconds;
	}

	public void setTokenValiditySeconds(final long tokenValiditySeconds)
	{
		if (tokenValiditySeconds < 0)
		{
			throw new IllegalArgumentException("tokenValiditySeconds has to be >= 0");
		}
		this.tokenValiditySeconds = tokenValiditySeconds;
	}

	public SecureTokenService getSecureTokenService() {
		return secureTokenService;
	}

	public void setSecureTokenService(final SecureTokenService secureTokenService) {
		this.secureTokenService = secureTokenService;
	}

	@Override
	public ModelService getModelService()
	{
		return modelService;
	}

	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public GPDefaultCustomerNameStrategy getGpDefaultCustomerNameStrategy() {
		return gpDefaultCustomerNameStrategy;
	}

	public void setGpDefaultCustomerNameStrategy(final GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy) {
		this.gpDefaultCustomerNameStrategy = gpDefaultCustomerNameStrategy;
	}

	public GPDefaultQuplesTokenService getQuplesService() {
		return quplesService;
	}

	public void setQuplesService(GPDefaultQuplesTokenService quplesService) {
		this.quplesService = quplesService;
	}

	public GPPdfUserDetailsPopulator getGpDefaultPdfUserDetailsPopulator() {
		return gpDefaultPdfUserDetailsPopulator;
	}

	public void setGpDefaultPdfUserDetailsPopulator(GPPdfUserDetailsPopulator gpDefaultPdfUserDetailsPopulator) {
		this.gpDefaultPdfUserDetailsPopulator = gpDefaultPdfUserDetailsPopulator;
	}

	
}

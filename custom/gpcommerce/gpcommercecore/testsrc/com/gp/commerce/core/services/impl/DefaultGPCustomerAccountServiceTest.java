package com.gp.commerce.core.services.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.commerceservices.customer.dao.CustomerAccountDao;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.commerceservices.event.ChangeUIDEvent;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.commerceservices.security.SecureTokenService;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
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
import de.hybris.platform.servicelayer.user.PasswordEncoderConstants;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;

import org.apache.commons.configuration.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.gp.commerce.core.company.services.GPB2BUnitsService;
import com.gp.commerce.core.enums.GPApprovalEnum;
import com.gp.commerce.core.enums.GPUserApprovalStatusEnum;
import com.gp.commerce.core.model.TaxExemptionModel;
import com.gp.commerce.core.strategies.GPDefaultCustomerNameStrategy;
import com.gp.commerce.core.user.dao.GPUserDao;
import com.gp.commerce.core.util.GPSiteConfigUtils;

@UnitTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(Config.class)
@PowerMockIgnore("org.apache.logging.log4j.*")
public class DefaultGPCustomerAccountServiceTest {

	private static final String ID = "123456";

	@InjectMocks
	private final DefaultGPCustomerAccountService service = new DefaultGPCustomerAccountService();

	private static final String BUSINESS_USER_EMAIL = "gp.businessuser.email.id";

	public static final String B2BUNITL2 = "b2b.unit.level.l2";
	private static final String BASESITE_DELIMITER = "gpcommercewebservices.user.delimiter";

	@Mock
	MediaService mediaService;
	@Mock
	CustomerAccountDao customerAccountDao;
	@Mock
	CatalogVersionService catalogVersionService;
	
	@Mock
	private Converter<AddressData, AddressModel> addressReverseConverter;

	@Mock
	private Converter<CCPaymentInfoData, CreditCardPaymentInfoModel> cardPaymentInfoReverseConverter;

	@Mock
	private UserService userService;
	@Mock
	private SecureTokenService secureTokenService;
	@Mock
	private ModelService modelService;
	@Mock
	private EventService eventService;
	@Mock
	private BaseStoreService baseStoreService;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private CommonI18NService commonI18NService;
	@Mock
	protected GPB2BUnitsService<B2BUnitModel, UserModel> gpB2BUnitsService;
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private Configuration configuration;
	@Mock
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
	@Mock
	PasswordEncoderService passwordEncoderService;
	@Mock
	GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy;

	@Mock
	private KeyGenerator guidKeyGenerator;

	@Mock
	B2BUnitModel unit;
	@Mock
	private GPUserDao userDao;

	SecureToken secureToken = new SecureToken("test", System.currentTimeMillis());
	B2BCustomerModel b2bCustomer = mock(B2BCustomerModel.class);
	CustomerModel customer = mock(CustomerModel.class);
	BaseSiteModel site = mock(BaseSiteModel.class);
	BaseStoreModel store = mock(BaseStoreModel.class);
	LanguageModel language = mock(LanguageModel.class);
	CurrencyModel currency = mock(CurrencyModel.class);
	AddressModel address = mock(AddressModel.class);

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		service.setTokenValiditySeconds(1000l);
		service.setConfigurationService(configurationService);
		service.setPasswordEncoding("PasswordEncoding");
		service.setCustomerAccountDao(customerAccountDao);
		ReflectionTestUtils.setField(service, "commonI18NService", commonI18NService);
		ReflectionTestUtils.setField(service, "eventService", eventService);
		ReflectionTestUtils.setField(service, "modelService", modelService);
		ReflectionTestUtils.setField(service, "gpDefaultCustomerNameStrategy", gpDefaultCustomerNameStrategy);
		ReflectionTestUtils.setField(service, "configurationService", configurationService);
		ReflectionTestUtils.setField(service, "secureTokenService", secureTokenService);
		ReflectionTestUtils.setField(service, "passwordEncoderService", passwordEncoderService);
		ReflectionTestUtils.setField(service, "guidKeyGenerator", guidKeyGenerator);
		ReflectionTestUtils.setField(service, "catalogVersionService", catalogVersionService);
		ReflectionTestUtils.setField(service, "cardPaymentInfoReverseConverter", cardPaymentInfoReverseConverter);
		ReflectionTestUtils.setField(service, "addressReverseConverter", addressReverseConverter);
		ReflectionTestUtils.setField(service, "mediaService", mediaService);
		
		service.setTokenValiditySeconds(10L);
		when(userService.getUserForUID("test", CustomerModel.class)).thenReturn(customer);
		when(secureTokenService.decryptData(ID)).thenReturn(secureToken);
		when(customer.getToken()).thenReturn(ID);
		when(baseSiteService.getCurrentBaseSite()).thenReturn(site);
		when(baseStoreService.getCurrentBaseStore()).thenReturn(store);
		when(commonI18NService.getCurrentCurrency()).thenReturn(currency);
		when(commonI18NService.getCurrentLanguage()).thenReturn(language);
		when(modelService.clone(Mockito.any(AddressModel.class))).thenReturn(address);

		Mockito.when(b2bCustomer.getDefaultB2BUnit()).thenReturn(unit);
		Mockito.when(b2bCustomer.getDefaultB2BUnit().getB2bUnitLevel()).thenReturn("L2");
		Mockito.when(b2bCustomer.getUserApprovalStatus()).thenReturn(GPUserApprovalStatusEnum.PENDING);
		Mockito.when(b2bCustomer.getEmail()).thenReturn("gpcommerce@gp.com");
		Mockito.when(b2bCustomer.getSite()).thenReturn(Mockito.mock(BaseSiteModel.class));
		Mockito.when(b2bCustomer.getSite().getStores())
				.thenReturn(Collections.singletonList(Mockito.mock(BaseStoreModel.class)));

		doNothing().when(modelService).save(Mockito.any(AddressModel.class));
		doNothing().when(modelService).refresh(Mockito.any(AddressModel.class));
		doNothing().when(modelService).refresh(Mockito.any(CustomerModel.class));
		doNothing().when(modelService).save(Mockito.any(CustomerModel.class));
		doNothing().when(eventService).publishEvent(Mockito.any(AbstractCommerceUserEvent.class));
	}

	protected void setCurrentUser(final String userId) {
		final UserModel user = getUserService().getUserForUID(userId);
		getUserService().setCurrentUser(user);
	}

	protected UserService getUserService() {
		return userService;
	}

	@Test
	public void testUpdatePasswordTokenExpired() {
		final SecureToken secureToken = new SecureToken("test", 10l);

		when(secureTokenService.decryptData(ID)).thenReturn(secureToken);

		boolean isException = false;
		service.setTokenValiditySeconds(100l);
		try {
			service.updatePassword(ID, "45678");
		} catch (final Exception e) {
			isException = true;
		}
		assertTrue(isException);
	}

	@Test
	public void testUpdatePasswordCustomerNull() {
		when(userService.getUserForUID("test", CustomerModel.class)).thenReturn(null);

		boolean isException = false;

		try {
			service.updatePassword(ID, "45678");
		} catch (final Exception e) {
			isException = true;
		}
		assertTrue(isException);
	}

	@Test
	public void testUpdatePasswordToeknExpired() {
		when(customer.getToken()).thenReturn("1234567");

		boolean isException = false;

		try {
			service.updatePassword(ID, "45678");
		} catch (final Exception e) {
			isException = true;
		}
		assertTrue(isException);
	}

	@Test
	public void testUpdatePasswordSuccess() throws TokenInvalidatedException {
		boolean isException = false;

		try {
			service.updatePassword(ID, "45678");
		} catch (final Exception e) {
			isException = true;
		}
		assertTrue(!isException);
	}

	@Test
	public void testRegisterB2bUnit() throws DuplicateUidException {

		PowerMockito.mockStatic(Config.class);
		Mockito.when(Config.getParameter(BUSINESS_USER_EMAIL)).thenReturn("gpcommerce@gp.com");
		Mockito.when(b2bUnitService.getUsersOfUserGroup(unit, "b2badmingroup", false))
				.thenReturn(Collections.singletonList(b2bCustomer));
		service.registerB2bUnit(b2bCustomer, "ASDWER");
	}

	@Test
	public void testRegisterB2bUnitSavingException() {
		final ModelSavingException exception = mock(ModelSavingException.class);
		final InterceptorException intercept = mock(InterceptorException.class);
		final UniqueAttributesInterceptor unique = mock(UniqueAttributesInterceptor.class);

		doNothing().when(userService).setPassword(b2bCustomer, ID, PasswordEncoderConstants.DEFAULT_ENCODING);
		doThrow(exception).when(modelService).save(b2bCustomer);

		when(exception.getCause()).thenReturn(intercept);
		when(intercept.getInterceptor()).thenReturn(unique);

		boolean isException = false;
		try {
			service.registerB2bUnit(b2bCustomer, ID);
		} catch (final Exception e) {
			isException = true;
		}
		assertTrue(isException);
	}

	@Test
	public void testVerfiyTokenValidity() throws TokenInvalidatedException {
		final SecureToken secureToken = Mockito.mock(SecureToken.class);

		when(secureTokenService.decryptData("12341234")).thenReturn(secureToken);
		when(secureToken.getTimeStamp()).thenReturn(System.currentTimeMillis());

		Assert.assertTrue(service.verfiyTokenValidity("12341234"));
	}

	@Test
	public void testRegisterB2bUnitSavingEAmbiguousIdentifierExceptionxception() {
		final AmbiguousIdentifierException exception = mock(AmbiguousIdentifierException.class);

		doNothing().when(userService).setPassword(b2bCustomer, ID, PasswordEncoderConstants.DEFAULT_ENCODING);
		doThrow(exception).when(modelService).save(b2bCustomer);

		boolean isException = false;
		try {
			service.registerB2bUnit(b2bCustomer, ID);
		} catch (final Exception e) {
			isException = true;
		}
		assertTrue(isException);
	}

	@Test
	public void testGetUserId() {
		when(secureTokenService.decryptData(ID)).thenReturn(secureToken);

		final String userid = service.getUserId(ID);
		assertTrue(userid.equalsIgnoreCase("test"));

	}

	@Test
	public void testUpdatePersonalDetails() {
		final String name = "customer name";
		CustomerData customerdata = Mockito.mock(CustomerData.class);
		final String uid = "newCustomer1@test.com";
		setCurrentUser(uid);
		when(userService.getCurrentUser()).thenReturn(customer);
		when(customer.getCellPhone()).thenReturn("676768331");
		when(customer.getName()).thenReturn("ComerceCust");
		when(gpDefaultCustomerNameStrategy.getName("ComerceCust")).thenReturn("customer name");
		doNothing().when(eventService).publishEvent(Mockito.any(AbstractCommerceUserEvent.class));
		doNothing().when(modelService).save(Mockito.any(CustomerModel.class));
		doNothing().when(modelService).refresh(Mockito.any(CustomerModel.class));
		final CustomerModel customerModel = service.updatePersonalDetails(name, customerdata);
		assertTrue(customerModel.getName().equalsIgnoreCase("customer name"));
		assertTrue(customerModel.getCellPhone().equalsIgnoreCase("9080756435"));
	}

	@Test
	public void testSetDefaultAddressEntry() {
		final AddressModel address1 = Mockito.mock(AddressModel.class);
		final CustomerModel customer1 = Mockito.mock(CustomerModel.class);
		final List<AddressModel> addresses = new ArrayList<>();
		addresses.add(address1);
		when(customer1.getAddresses()).thenReturn(addresses);
		when(address1.getFirstname()).thenReturn("firstName");
		when(address1.getBillingAddress()).thenReturn(true);
		when(address1.getShippingAddress()).thenReturn(true);
	

		service.setDefaultAddressEntry(customer1, address1, true, true);

		Mockito.verify(customer1).setDefaultPaymentAddress(address1);
	}

	@Test
	public void testSetDefaultAddressEntryWithDefaultAddressPresent() {
		final AddressModel address1 = Mockito.mock(AddressModel.class);
		final CustomerModel customer1 = Mockito.mock(CustomerModel.class);
		final List<AddressModel> addresses = new ArrayList<>();
		addresses.add(address1);
		Mockito.when(customer1.getAddresses()).thenReturn(addresses);
		Mockito.when(address1.getFirstname()).thenReturn("First");
		Mockito.when(address1.getBillingAddress()).thenReturn(true);
		Mockito.when(address1.getShippingAddress()).thenReturn(true);
		
		
		

		service.setDefaultAddressEntry(customer1, address1, true, true);

		Mockito.verify(customer1).setDefaultPaymentAddress(address1);
		assertTrue(CollectionUtils.isNotEmpty(customer1.getAddresses()));
	}

	@Test
	public void testClearDefaultAddressEntry() {
		final AddressModel address1 = Mockito.mock(AddressModel.class);
		final CustomerModel customer1 = Mockito.mock(CustomerModel.class);
		final List<AddressModel> addresses = new ArrayList<>();
		addresses.add(address1);
		
		Mockito.when(customer1.getAddresses()).thenReturn(addresses);
		Mockito.when(address1.getFirstname()).thenReturn("First");
		Mockito.when(address1.getBillingAddress()).thenReturn(true);
		Mockito.when(address1.getShippingAddress()).thenReturn(true);
		
		Mockito.when(customer1.getAddresses()).thenReturn(addresses);
		Mockito.when(customer1.getDefaultPaymentAddress()).thenReturn(address1);
		
		service.clearDefaultAddressEntry(customer1, true, true);

		Mockito.verify(customer1).setDefaultPaymentAddress(null);
		Mockito.verify(customer1).setDefaultShipmentAddress(null);
		
	}

	@Test
	public void saveAddressEntryTest() {

		B2BCustomerModel customerModel = Mockito.mock(B2BCustomerModel.class);
		List<B2BCustomerModel> b2bcustomerModel = new ArrayList<>();
		B2BCustomerModel member = Mockito.mock(B2BCustomerModel.class);
		Mockito.when(member.getEmail()).thenReturn("test@test.com");
		b2bcustomerModel.add(member);
		AddressModel addressModel = Mockito.mock(AddressModel.class);
		Mockito.when(addressModel.getB2bUnit()).thenReturn(unit);
		Mockito.when(unit.getB2bUnitLevel()).thenReturn("L2");
		List<AddressModel> addresses = new ArrayList<>();
		Mockito.when(customerModel.getDefaultB2BUnit()).thenReturn(unit);
		Mockito.when(customerModel.getAddresses()).thenReturn(addresses);
		
		doNothing().when(modelService).save(Mockito.any(AddressModel.class));
		doNothing().when(modelService).refresh(Mockito.any(AddressModel.class));
		doNothing().when(modelService).save(Mockito.any(CustomerModel.class));
		doNothing().when(modelService).refresh(Mockito.any(CustomerModel.class));
		when(userService.getCurrentUser()).thenReturn(customerModel);
		when(userService.isAnonymousUser(customerModel)).thenReturn(false);
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configurationService.getConfiguration().getString("b2b.unit.level.l3")).thenReturn("b2b.unit.level.l1");
		when(configurationService.getConfiguration().getString(Mockito.anyString(), Mockito.anyString()))
				.thenReturn("b2b.approval.admin.email", "admin@gppro.com");
		doNothing().when(eventService).publishEvent(Mockito.any(AbstractCommerceUserEvent.class));
		when(b2bUnitService.getUsersOfUserGroup(customerModel.getDefaultB2BUnit(), "b2badmingroup", false))
				.thenReturn(b2bcustomerModel);
		when(configurationService.getConfiguration().getString(B2BUNITL2)).thenReturn("L2");
		when(unit.getB2bUnitLevel()).thenReturn("L2");
		service.saveAddressEntry(customerModel, addressModel);

	}

	@Test
	public void submitTaxExemptionDocumentsTest() {
		List<MediaModel> medias = new ArrayList<>();
		CustomerModel customer =Mockito.mock(CustomerModel.class);
		TaxExemptionModel taxExemptionModel =Mockito.mock(TaxExemptionModel.class);
		MediaModel media = Mockito.mock(MediaModel.class);
		medias.add(media);
		when(modelService.create(TaxExemptionModel.class)).thenReturn(taxExemptionModel);
		when(userService.getCurrentUser()).thenReturn(customer);
		doNothing().when(modelService).save(Mockito.any(TaxExemptionModel.class));
		doNothing().when(eventService).publishEvent(Mockito.any(AbstractCommerceUserEvent.class));
		service.submitTaxExemptionDocuments(medias);

	}

	@Test
	public void connectOrDisconnectTest() throws DuplicateUidException {
		CustomerModel customer = Mockito.mock(CustomerModel.class);
		doNothing().when(userService).setEncodedPassword(customer, "test", "test");
		doNothing().when(modelService).save(Mockito.any(CustomerModel.class));
		service.connectOrDisconnect(customer, "test");
	}

	@Test
	public void getAddressForB2BCustomerTest() {

		when(userService.getUserForUID("test")).thenReturn(customer);
		AddressModel address = Mockito.mock(AddressModel.class);
		when(customer.getAddresses()).thenReturn(Collections.singletonList(address));
		when(address.getPk()).thenReturn(PK.fromLong(123456));
		service.getAddressForB2BCustomer("123456", "test");

	}

	@Test
	public void getAddressBookEntriesTest() {
		B2BCustomerModel customerModel = Mockito.mock(B2BCustomerModel.class);
		List<B2BUnitModel> units = new ArrayList<>();
		List<AddressModel> addresses = new ArrayList<>();
		when(gpB2BUnitsService.isB2BAdmin(customerModel)).thenReturn(true);
		when(gpB2BUnitsService.getUnitsWithChildNodes(customerModel, true)).thenReturn(units);
		when(userDao.getAddressesForB2BUser(units, false)).thenReturn(addresses);
		service.getAddressBookEntries(customerModel);

	}

	@Test
	public void getAddressBookEntriesNotAdminTest() {
		B2BCustomerModel customerModel = Mockito.mock(B2BCustomerModel.class);
		List<B2BUnitModel> units = new ArrayList<>();
		List<AddressModel> addresses = new ArrayList<>();
		AddressModel a1 =  Mockito.mock(AddressModel.class);
		when(a1.getVisibleInAddressBook()).thenReturn(true);
		addresses.add(a1);
		when(customerModel.getAddresses()).thenReturn(addresses);
		when(gpB2BUnitsService.isB2BAdmin(customerModel)).thenReturn(false);
		service.getAddressBookEntries(customerModel);

	}

	@Test
	public void getActiveAddressBookEntriesTest() {
		B2BCustomerModel customer = Mockito.mock(B2BCustomerModel.class);
		List<B2BUnitModel> units = new ArrayList<>();
		List<AddressModel> addresses = new ArrayList<>();
		when(gpB2BUnitsService.isB2BAdmin(customer)).thenReturn(true);
		when(userDao.getAddressesForB2BUser(units, true)).thenReturn(addresses);
		when(gpB2BUnitsService.getUnitsWithChildNodes(customer, true)).thenReturn(units);
		service.getActiveAddressBookEntries(customer, true);
	}

	@Test
	public void getActiveAddressBookEntriesNotAdminTest() {
		B2BCustomerModel customer = Mockito.mock(B2BCustomerModel.class);
		List<AddressModel> addresses = new ArrayList<>();
		AddressModel address = Mockito.mock(AddressModel.class);
		when(address.getApprovalStatus()).thenReturn(GPApprovalEnum.ACTIVE);
		addresses.add(address);
		when(customer.getAddresses()).thenReturn(addresses);
		when(gpB2BUnitsService.isB2BAdmin(customer)).thenReturn(false);
		service.getActiveAddressBookEntries(customer, true);
	}

	@Test
	public void getActiveAddressBookEntriesNotB2B() {
		CustomerModel customer = Mockito.mock(CustomerModel.class);
		List<AddressModel> addresses = new ArrayList<>();
		AddressModel address =Mockito.mock(AddressModel.class);
		when(address.getVisibleInAddressBook()).thenReturn(true);
		addresses.add(address);
		when(customer.getAddresses()).thenReturn(addresses);
		service.getActiveAddressBookEntries(customer, true);
	}

	@Test
	public void getDeleteAddressEntry() {
		CustomerModel customerModel = Mockito.mock(CustomerModel.class);
		AddressModel addressModel = Mockito.mock(AddressModel.class);
		when(customerModel.getAddresses()).thenReturn(Collections.singletonList(addressModel));
		service.deleteAddressEntry(customerModel, addressModel);
		Mockito.verify(modelService).remove(addressModel);
		Mockito.verify(modelService).refresh(customerModel);

	}

	@Test(expected = IllegalArgumentException.class)
	public void getDeleteAddressEntryException() {
		CustomerModel customerModel = Mockito.mock(CustomerModel.class);
		AddressModel addressModel = Mockito.mock(AddressModel.class);
		when(customerModel.getAddresses()).thenReturn(Collections.emptyList());
		service.deleteAddressEntry(customerModel, addressModel);

	}

	@Test
	public void testChangeUid() throws DuplicateUidException, PasswordMismatchException {
		CustomerModel customerModel = Mockito.mock(CustomerModel.class);
		when(configurationService.getConfiguration()).thenReturn(Mockito.mock(Configuration.class));
		when(configurationService.getConfiguration().getString(BASESITE_DELIMITER)).thenReturn("ABCDEF");
		when(baseSiteService.getCurrentBaseSite()).thenReturn(Mockito.mock(BaseSiteModel.class));
		when(baseSiteService.getCurrentBaseSite().getUid()).thenReturn("gpcommerce");
		when(userService.getCurrentUser()).thenReturn(customerModel);
		when(customerModel.getUid()).thenReturn("gpcustomer");
		when(userService.getUserForUID("gpcustomer")).thenReturn(null);
		when(passwordEncoderService.isValid(customerModel, "ASDQWE")).thenReturn(true);

		service.changeUid("gpcommerceCustomer", "ASDQWE");

	}

	@Test
	public void testConvertMultipartFileToMedia() throws IOException {
		MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
		final MediaModel mediaModel = Mockito.mock(MediaModel.class);
		when(modelService.create(MediaModel.class)).thenReturn(mediaModel);
		when(multipartFile.getOriginalFilename()).thenReturn("C:/gp/commerce/media.png");
		when(guidKeyGenerator.generate()).thenReturn("123wer456");
		when(multipartFile.getContentType()).thenReturn("Media");
		when(multipartFile.getContentType()).thenReturn("Media");
		when(multipartFile.getInputStream()).thenReturn(Mockito.mock(InputStream.class));
		when(catalogVersionService.getSessionCatalogVersions()).thenReturn(Collections.singletonList(Mockito.mock(CatalogVersionModel.class)));

		Assert.assertTrue(service.convertMultipartFileToMedia(multipartFile).equals(mediaModel));
	}

	@Test
	public void testUpdateCCPaymentInfo()
	{
		final CustomerModel currentCustomer = Mockito.mock(CustomerModel.class);
		final CreditCardPaymentInfoModel creditCard = Mockito.mock(CreditCardPaymentInfoModel.class);
		final CCPaymentInfoData paymentInfo=Mockito.mock(CCPaymentInfoData.class);
		when(userService.getCurrentUser()).thenReturn(currentCustomer);
		when(customerAccountDao.findCreditCardPaymentInfoByCustomer(currentCustomer,"abcdef")).thenReturn(creditCard);
		when(cardPaymentInfoReverseConverter.convert(paymentInfo, creditCard)).thenReturn(creditCard);
		
		
		final CreditCardPaymentInfoModel paymentInfoModel = Mockito.mock(CreditCardPaymentInfoModel.class);
		
		service.updateCCPaymentInfo(paymentInfo);
		
	}
	
	@Test
	public void testAddCCPaymentInfo()
	{
		
		AddressModel addressModel = Mockito.mock(AddressModel.class);
		AddressData addressdata = Mockito.mock(AddressData.class);
		
		final CustomerModel currentCustomer = Mockito.mock(CustomerModel.class);
		final CreditCardPaymentInfoModel creditCard = Mockito.mock(CreditCardPaymentInfoModel.class);
		final CCPaymentInfoData paymentInfo=Mockito.mock(CCPaymentInfoData.class);
		when(userService.getCurrentUser()).thenReturn(currentCustomer);
		when(modelService.create(CreditCardPaymentInfoModel.class)).thenReturn(creditCard);
		when(modelService.create(AddressModel.class)).thenReturn(addressModel);
		when(addressReverseConverter.convert(addressdata, addressModel)).thenReturn(addressModel);
		//when(creditCard.getBillingAddress()).thenReturn(addressModel);
		when(paymentInfo.getBillingAddress()).thenReturn(addressdata);
		when(paymentInfo.isDefaultPaymentInfo()).thenReturn(true);
		when(currentCustomer.getPaymentInfos()).thenReturn(Collections.singletonList(creditCard));
		
		service.addCCPaymentInfo(paymentInfo);
			
	}
}

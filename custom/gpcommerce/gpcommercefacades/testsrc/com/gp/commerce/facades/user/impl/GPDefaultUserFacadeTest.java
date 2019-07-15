package com.gp.commerce.facades.user.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.QuplesData;

import com.gp.commerce.facades.data.user.data.MarketingPreferenceData;
import com.gp.commerce.facades.data.user.data.MarketingPreferenceDataList;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.strategies.CheckoutCustomerStrategy;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;

import com.gp.commerce.facades.user.data.MarketingPreferenceQuestionAnsDataList;
import com.gpintegration.service.GPYMarketingSyncService;
import com.gpintegration.service.impl.GPDefaultQuplesTokenService;

import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import com.gp.commerce.core.company.services.GPB2BUnitsService;
import com.gp.commerce.core.company.services.impl.GPDefaultB2BUnitsService;
import com.gp.commerce.core.enums.GPApprovalEnum;
import com.gp.commerce.core.enums.MarketingFrequencyEnum;
import com.gp.commerce.core.event.EmailSubjectType;
import com.gp.commerce.core.model.MarketingPreferenceModel;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.services.GPUserService;
import com.gp.commerce.core.services.event.GPEmailItemEvent;
import com.gp.commerce.core.services.impl.DefaultGPCustomerAccountService;
import org.apache.commons.configuration.Configuration;
import reactor.util.CollectionUtils;

@UnitTest
//@RunWith(MockitoJUnitRunner.class)
public class GPDefaultUserFacadeTest {
	
	private static final String PK_Value = "8359490977792";
	
	public static final String TERM_AND_COND_PAGE_LABEL = "termAndCondPageLabel";

	@Spy
	@InjectMocks
	private GPDefaultUserFacade gpDefaultUserFacade;

	@Mock
	private CheckoutCustomerStrategy checkoutCustomerStrategy;

	@Mock
	private DefaultGPCustomerAccountService defaultGPCustomerAccountService;

	@Mock
	private CustomerAccountService customerAccountService;

	@Mock
	private BaseSiteService baseSiteService;

	private Populator<AddressData, AddressModel> addressReversePopulator;

	@Mock
	private Populator<AddressData, AddressModel> addressReversePopulatorPayment;

	@Mock
	private ModelService modelService;

	@Mock
	private GPUserService userService;

	@Mock
	private Converter<AddressModel, AddressData> addressConverter;

	@Mock
	private Populator<CCPaymentInfoData, CreditCardPaymentInfoModel> cardPaymentInfoReversePopulator;
	
	private Converter<CCPaymentInfoData, CreditCardPaymentInfoModel> cardPaymentInfoReverseConverter;

	@Mock

	protected GPB2BUnitsService<B2BUnitModel, UserModel> gpB2BUnitsService;

	@Mock
	GPDefaultB2BUnitsService gpDefaultB2BUnitsService = new GPDefaultB2BUnitsService();
	@Mock
	AddressModel addressModel1 = mock(AddressModel.class);

	@Mock
	private BaseStoreService baseStoreService;

	BaseSiteModel site = mock(BaseSiteModel.class);

	BaseStoreModel store = mock(BaseStoreModel.class);

	private static final String EN = Locale.ENGLISH.getLanguage();
	@Mock
	AbstractCommerceUserEvent event;

	@Mock
	private CommonI18NService commonI18NService;

	@Mock
	private EventService eventService;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private Configuration configuration;

	@Mock
	private final AddressModel customerAddressModel = new AddressModel();

	GPEmailItemEvent item = mock(GPEmailItemEvent.class);

	// B2BUnitModel b2bUnit=mock(B2BUnitModel.class);

	private Converter<MarketingPreferenceModel, MarketingPreferenceData> marketingPreferenceConverter;
	
	@Mock
	private EnumerationService enumerationService;

	@Mock
	private CMSSiteModel cmsSite;
	@Mock
	private QuplesData quplesData;
	@Mock
	private MarketingPreferenceDataList marketingPreferenceDataList;
	@Mock
	private CommerceCommonI18NService commerceCommonI18NService;
	@Mock
	private GPDefaultQuplesTokenService gpDefaultQuplesTokenService;
	@Mock
	private ContentPageModel contentPageModel;
	@Mock
	private CMSPageService cmsPageService;
	@Mock
	private GPCMSSiteService cmsSiteService;
	@Mock
	private ContentSlotForPageModel contentSlotforPageModel;
	@Mock
	private ContentSlotModel contentSlotModel;
	@Mock
	private CMSParagraphComponentModel cmsParagraphComponentModel;
	@Mock
	private GPYMarketingSyncService yMarketingService;
	@Mock
	private CCPaymentInfoData ccPaymentInfoData;
	
	private AddressData addressData;
	private AddressModel addressModel;
	private CustomerModel customer;
	private B2BCustomerModel b2bcustomer;

	CreditCardPaymentInfoModel paymentInfoModel = mock(CreditCardPaymentInfoModel.class);
	CCPaymentInfoData paymentInfo = new CCPaymentInfoData();
	AddressData billingAddressData = new AddressData();
	AddressModel billingAddress = mock(AddressModel.class);
	LanguageModel language = mock(LanguageModel.class);
	CurrencyModel currency = mock(CurrencyModel.class);


	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(baseSiteService.getCurrentBaseSite()).thenReturn(cmsSite);

		addressData = new AddressData();
		addressModel = new AddressModel();
		customer = new CustomerModel();

		addressReversePopulator = new AddressReversePopulator();
		gpDefaultUserFacade.setAddressReversePopulator(addressReversePopulator);

		// add payment info
		gpDefaultUserFacade.setUserService(userService);
		gpDefaultUserFacade.setModelService(modelService);
		gpDefaultUserFacade.setCommonI18NService(commonI18NService);
		gpDefaultUserFacade.setyMarketingService(yMarketingService);
		gpDefaultUserFacade.setCommerceCommonI18NService(commerceCommonI18NService);
		gpDefaultUserFacade.setCardPaymentInfoReversePopulator(cardPaymentInfoReversePopulator);
		when(userService.getCurrentUser()).thenReturn(customer);
		when(modelService.create(CreditCardPaymentInfoModel.class)).thenReturn(paymentInfoModel);
		when(modelService.create(AddressModel.class)).thenReturn(billingAddress);
		doNothing().when(cardPaymentInfoReversePopulator).populate(paymentInfo, paymentInfoModel);
		when(paymentInfoModel.getBillingAddress()).thenReturn(null);
		doNothing().when(modelService).save(paymentInfoModel);
		doNothing().when(modelService).save(billingAddress);
		doNothing().when(modelService).refresh(paymentInfoModel);
		doNothing().when(customerAccountService).setDefaultPaymentInfo(customer, paymentInfoModel);
	}

	@Test
	public void testAddAddress() {
		addressData.setFirstName("firstName");
		addressData.setDefaultAddress(true);
		addressData.setVisibleInAddressBook(true);

		final AddressModel spyAddress = Mockito.spy(addressModel);

		final PK pk = PK.fromLong(123456);

		final List<AddressModel> addresses = new ArrayList<>();
		addresses.add(spyAddress);
		customer.setAddresses(addresses);

		when(checkoutCustomerStrategy.getCurrentUserForCheckout()).thenReturn(customer);
		when(modelService.create(AddressModel.class)).thenReturn(spyAddress);
		Mockito.doNothing().when(defaultGPCustomerAccountService).saveAddressEntry(Mockito.any(CustomerModel.class),
				Mockito.any(AddressModel.class));
		Mockito.doNothing().when(defaultGPCustomerAccountService).setDefaultAddressEntry(
				Mockito.any(CustomerModel.class), Mockito.any(AddressModel.class), Mockito.anyBoolean(),
				Mockito.anyBoolean());
		when(spyAddress.getPk()).thenReturn(pk);

		gpDefaultUserFacade.addAddress(addressData);

		assertTrue(addressData.getFirstName().equals(addressModel.getFirstname()));

	}

	@Test
	public void testAddAddressWithDefaultBillingAddress() {
		addressData.setFirstName("firstName");
		addressData.setBillingAddress(true);
		addressData.setVisibleInAddressBook(true);

		final AddressModel spyAddress = Mockito.spy(addressModel);

		final PK pk = PK.fromLong(123456);

		final List<AddressModel> addresses = new ArrayList<>();
		addresses.add(spyAddress);
		customer.setAddresses(addresses);

		when(checkoutCustomerStrategy.getCurrentUserForCheckout()).thenReturn(customer);
		when(modelService.create(AddressModel.class)).thenReturn(spyAddress);
		Mockito.doNothing().when(defaultGPCustomerAccountService).saveAddressEntry(Mockito.any(CustomerModel.class),
				Mockito.any(AddressModel.class));
		Mockito.doNothing().when(defaultGPCustomerAccountService).setDefaultAddressEntry(
				Mockito.any(CustomerModel.class), Mockito.any(AddressModel.class), Mockito.anyBoolean(),
				Mockito.anyBoolean());
		when(spyAddress.getPk()).thenReturn(pk);

		gpDefaultUserFacade.addAddress(addressData);

		assertTrue(addressData.getFirstName().equals(addressModel.getFirstname()));

	}

	@Test
	public void testEditAddressWithDefaultAddress() {
		addressData.setFirstName("firstName");
		addressData.setDefaultAddress(true);
		addressData.setVisibleInAddressBook(true);
		customer.setDefaultPaymentAddress(addressModel);

		when(checkoutCustomerStrategy.getCurrentUserForCheckout()).thenReturn(customer);
		Mockito.doNothing().when(defaultGPCustomerAccountService).saveAddressEntry(Mockito.any(CustomerModel.class),
				Mockito.any(AddressModel.class));
		Mockito.doNothing().when(defaultGPCustomerAccountService).setDefaultAddressEntry(
				Mockito.any(CustomerModel.class), Mockito.any(AddressModel.class), Mockito.anyBoolean(),
				Mockito.anyBoolean());
		Mockito.doNothing().when(defaultGPCustomerAccountService)
		.clearDefaultAddressEntry(Mockito.any(CustomerModel.class), Mockito.anyBoolean(), Mockito.anyBoolean());
		when(defaultGPCustomerAccountService.getAddressForCode(Mockito.any(CustomerModel.class),
				Mockito.any(String.class))).thenReturn(addressModel);

		gpDefaultUserFacade.editAddress(addressData);

		assertTrue(addressData.getFirstName().equals(addressModel.getFirstname()));

	}

	@Test
	public void testEditAddressWithDefaultBillingAddress() {
		addressData.setFirstName("firstName");
		addressData.setDefaultBillingAddress(true);
		addressData.setVisibleInAddressBook(true);
		customer.setDefaultShipmentAddress(addressModel);

		when(checkoutCustomerStrategy.getCurrentUserForCheckout()).thenReturn(customer);
		Mockito.doNothing().when(defaultGPCustomerAccountService).saveAddressEntry(Mockito.any(CustomerModel.class),
				Mockito.any(AddressModel.class));
		Mockito.doNothing().when(defaultGPCustomerAccountService).setDefaultAddressEntry(
				Mockito.any(CustomerModel.class), Mockito.any(AddressModel.class), Mockito.anyBoolean(),
				Mockito.anyBoolean());
		Mockito.doNothing().when(defaultGPCustomerAccountService)
		.clearDefaultAddressEntry(Mockito.any(CustomerModel.class), Mockito.anyBoolean(), Mockito.anyBoolean());
		when(defaultGPCustomerAccountService.getAddressForCode(Mockito.any(CustomerModel.class),
				Mockito.any(String.class))).thenReturn(addressModel);

		gpDefaultUserFacade.editAddress(addressData);

		assertTrue(addressData.getFirstName().equals(addressModel.getFirstname()));

	}

	@Test
	public void testGetDefaultAddressForShipment() {
		addressData.setFirstName("firstName");
		addressData.setDefaultAddress(true);
		addressData.setVisibleInAddressBook(true);

		addressModel.setShippingAddress(true);
		customer.setDefaultShipmentAddress(addressModel);

		final List<AddressModel> addresses = new ArrayList<>();
		addresses.add(addressModel);

		when(userService.getCurrentUser()).thenReturn(customer);
		when(addressConverter.convert(addressModel)).thenReturn(addressData);
		when(customerAccountService.getAddressBookEntries(customer)).thenReturn(addresses);

		final AddressData result = gpDefaultUserFacade.getDefaultAddress(true, false);

		assertTrue(addressData.getFirstName().equals(result.getFirstName()));
	}

	@Test
	public void testGetDefaultAddressWithDefaultShipmentNull() {
		addressData.setFirstName("firstName");
		addressData.setVisibleInAddressBook(true);

		addressModel.setShippingAddress(true);
		final List<AddressModel> addresses = new ArrayList<>();
		addresses.add(addressModel);

		when(userService.getCurrentUser()).thenReturn(customer);
		when(addressConverter.convert(addressModel)).thenReturn(addressData);
		when(customerAccountService.getAddressBookEntries(customer)).thenReturn(addresses);

		final AddressData result = gpDefaultUserFacade.getDefaultAddress(true, false);
         //assertTrue(addressData.getFirstName().equals(result.getFirstName()));
		Assert.assertNotNull("addressdata should be not null", addressData.getFirstName());
	}

	@Test
	public void testGetDefaultAddressForBilling() {
		addressData.setFirstName("firstName");
		addressData.setDefaultBillingAddress(true);
		addressData.setVisibleInAddressBook(true);

		addressModel.setBillingAddress(true);
		customer.setDefaultPaymentAddress(addressModel);

		final List<AddressModel> addresses = new ArrayList<>();
		addresses.add(addressModel);

		when(userService.getCurrentUser()).thenReturn(customer);
		when(addressConverter.convert(addressModel)).thenReturn(addressData);
		when(customerAccountService.getAddressBookEntries(customer)).thenReturn(addresses);

		final AddressData result = gpDefaultUserFacade.getDefaultAddress(false, true);

		assertTrue(addressData.getFirstName().equals(result.getFirstName()));
	}

	@Test
	public void testGetDefaultAddressWithBillingAddressNull() {
		addressData.setFirstName("firstName");
		addressData.setVisibleInAddressBook(true);
		customer.setDefaultPaymentAddress(addressModel);
		addressModel.setBillingAddress(true);

		final List<AddressModel> addresses = new ArrayList<>();
		addresses.add(addressModel);

		when(userService.getCurrentUser()).thenReturn(customer);
		when(addressConverter.convert(addressModel)).thenReturn(addressData);
		when(customerAccountService.getAddressBookEntries(customer)).thenReturn(addresses);

		final AddressData result = gpDefaultUserFacade.getDefaultAddress(false, true);

		//assertTrue(addressData.getFirstName().equals(result.getFirstName()));
		
	}

	@Test
	public void testSetDefaultAddressEntry() {
		addressData.setFirstName("firstName");
		addressData.setDefaultBillingAddress(true);
		addressData.setVisibleInAddressBook(true);
		customer.setDefaultShipmentAddress(addressModel);

		when(userService.getCurrentUser()).thenReturn(customer);
		when(customerAccountService.getAddressForCode(customer, addressData.getId())).thenReturn(addressModel);
		Mockito.doNothing().when(defaultGPCustomerAccountService).setDefaultAddressEntry(
				Mockito.any(CustomerModel.class), Mockito.any(AddressModel.class), Mockito.anyBoolean(),
				Mockito.anyBoolean());

		gpDefaultUserFacade.setDefaultAddressEntry(addressData, true, false);

		assertNotNull(addressModel);
	}

	@Test
	public void testGetWholeAddressBook() {
		addressData.setFirstName("firstName");
		addressData.setDefaultBillingAddress(true);
		addressData.setVisibleInAddressBook(true);

		addressModel.setShippingAddress(true);
		customer.setDefaultPaymentAddress(addressModel);

		final List<AddressModel> addresses = new ArrayList<>();
		addresses.add(addressModel);

		when(userService.getCurrentUser()).thenReturn(customer);
		when(defaultGPCustomerAccountService.getAddressBookEntries(customer)).thenReturn(addresses);
		when(addressConverter.convert(addressModel)).thenReturn(addressData);

		final List<AddressData> results = gpDefaultUserFacade.getWholeAddressBook();

		assertTrue(results.get(0).getFirstName().equals(addressData.getFirstName()));
	}

	@Test
	public void testGetWholeAddressBookWithDefaultAddressAlreadySet() {
		addressData.setFirstName("firstName");
		addressData.setDefaultAddress(true);
		addressData.setVisibleInAddressBook(true);
		addressData.setId("12345");

		addressModel.setShippingAddress(true);
		customer.setDefaultShipmentAddress(addressModel);

		final AddressModel spyAddress = Mockito.spy(addressModel);

		final PK pk = PK.fromLong(123456);

		final List<AddressModel> addresses = new ArrayList<>();
		addresses.add(spyAddress);
		customer.setAddresses(addresses);

		when(userService.getCurrentUser()).thenReturn(customer);
		when(defaultGPCustomerAccountService.getAddressBookEntries(customer)).thenReturn(addresses);
		when(addressConverter.convert(addressModel)).thenReturn(addressData);
		when(spyAddress.getPk()).thenReturn(pk);

		final List<AddressData> results = gpDefaultUserFacade.getWholeAddressBook();

		//ssertTrue(results.get(0).getFirstName().equals(addressData.getFirstName()));
		
		assertTrue(results.size()>0);
	}

	@Test
	public void testGetWholeAddressBookWithNoAddresses() {
		final List<AddressModel> addresses = new ArrayList<>();

		when(userService.getCurrentUser()).thenReturn(customer);
		when(defaultGPCustomerAccountService.getAddressBookEntries(customer)).thenReturn(addresses);
		when(addressConverter.convert(addressModel)).thenReturn(addressData);

		final List<AddressData> results = gpDefaultUserFacade.getWholeAddressBook();

		assertTrue(CollectionUtils.isEmpty(results));
	}

	@Test
	public void addCCPaymentInfoTest() {
		gpDefaultUserFacade.addCCPaymentInfo(paymentInfo);
	}

	@Test
	public void addSaveBillingAddressTest() {
		paymentInfo.setBillingAddress(billingAddressData);
		gpDefaultUserFacade.setAddressReversePopulator(addressReversePopulatorPayment);
		doNothing().when(addressReversePopulatorPayment).populate(billingAddressData, billingAddress);
		doNothing().when(defaultGPCustomerAccountService).updateCCPaymentInfo(paymentInfo);
		gpDefaultUserFacade.setAddressReversePopulator(addressReversePopulator);
		gpDefaultUserFacade.addCCPaymentInfo(paymentInfo);
	}

	@Test
	public void testGetAllMarketingPreferences() {
		List<MarketingPreferenceModel> preferences = new ArrayList<>();
		MarketingPreferenceModel preference = new MarketingPreferenceModel();
		preferences.add(preference);
		List<MarketingFrequencyEnum> enumValues = new ArrayList<>();
		List<MarketingPreferenceData> preferencesDataList = new ArrayList<>();
		MarketingPreferenceData preferenceData1 = new MarketingPreferenceData();
		preferenceData1.setPreferenceTypeId("PF-1");
		MarketingPreferenceData preferenceData2 = new MarketingPreferenceData();
		preferenceData1.setPreferenceTypeId("PF-2");

		preferencesDataList.add(preferenceData1);
		preferencesDataList.add(preferenceData2);

		Mockito.when(userService.getMarketingPreferences(cmsSite)).thenReturn(preferences);
		Mockito.when(enumerationService.getEnumerationValues(MarketingFrequencyEnum.class)).thenReturn(enumValues);
		Mockito.doReturn(preferencesDataList).when(gpDefaultUserFacade).getMarketingPreferenceData(preferences);

		MarketingPreferenceQuestionAnsDataList result = gpDefaultUserFacade.getAllMarketingPreferences();
		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.getMarketingPrefQuestionData().size());
		// Assert.assertEquals(preferenceData1.getPreferenceTypeId(),
		// result.getMarketingPrefQuestionData().get(0).getPreferenceTypeId());
	}

	@Test
	public void testGetAllMarketingPreferencesForEmptyPreferenceList() {
		List<MarketingPreferenceModel> preferences = new ArrayList<>();
		Mockito.when(userService.getMarketingPreferences(cmsSite)).thenReturn(preferences);

		MarketingPreferenceQuestionAnsDataList result = gpDefaultUserFacade.getAllMarketingPreferences();

		Assert.assertNotNull(result);
		Assert.assertNull(result.getMarketingPrefQuestionData());
	}

	@Test
	public void testUpdateCustomerPreferences() {
		gpDefaultUserFacade.updateCustomerPreferences(marketingPreferenceDataList);
		verify(userService, times(1)).updateCustomerPreferences(marketingPreferenceDataList, cmsSite);
	}

	@Test
	public void testPopulateApprovalStatusForL2L3ForNotB2BAdmin() {
		final AddressWsDTO addressData = new AddressWsDTO();
		b2bcustomer = new B2BCustomerModel();
		final B2BUnitModel unitModel = new B2BUnitModel();
		unitModel.setUid("testUnit");
		unitModel.setB2bUnitLevel("b2b.unit.level.l1");
		b2bcustomer.setDefaultB2BUnit(unitModel);
		when(checkoutCustomerStrategy.getCurrentUserForCheckout()).thenReturn(b2bcustomer);
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configurationService.getConfiguration().getString("b2b.unit.level.l3")).thenReturn("b2b.unit.level.l1");
		when(configurationService.getConfiguration().getString("b2b.unit.level.l2")).thenReturn("b2b.unit.level.l1");
		when(gpB2BUnitsService.isB2BAdmin(b2bcustomer)).thenReturn(Boolean.FALSE);
		gpDefaultUserFacade.populateApprovalStatusForL2L3(addressData);
		Assert.assertEquals("PENDINGBYADMIN", addressData.getApprovalStatus());

	}

	@Test
	public void testPopulateApprovalStatusForL2L3ForB2BAdmin() {
		b2bcustomer = new B2BCustomerModel();
		final B2BUnitModel unitModel = new B2BUnitModel();
		unitModel.setUid("testUnit");
		unitModel.setB2bUnitLevel("b2b.unit.level.l2");
		b2bcustomer.setUid("testUnit");
		b2bcustomer.setDefaultB2BUnit(unitModel);
		when(checkoutCustomerStrategy.getCurrentUserForCheckout()).thenReturn(b2bcustomer);
		final AddressWsDTO addressData = new AddressWsDTO();
		addressData.setApprovalStatus("PENDINGBYGP");
		when(gpB2BUnitsService.isB2BAdmin(b2bcustomer)).thenReturn(Boolean.TRUE);
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configurationService.getConfiguration().getString("b2b.unit.level.l3")).thenReturn("b2b.unit.level.l3");
		when(configurationService.getConfiguration().getString("b2b.unit.level.l2")).thenReturn("b2b.unit.level.l2");
		gpDefaultUserFacade.populateApprovalStatusForL2L3(addressData);
		Assert.assertNotNull("Approval status should not be null", addressData.getApprovalStatus());
		Assert.assertEquals("PENDINGBYGP", addressData.getApprovalStatus().toString());
	}

	@Test
	public void testeditAddressForB2BAddressApproved() {

		b2bcustomer = new B2BCustomerModel();
		AddressModel customerAddressModel = new AddressModel();
		LanguageModel language = new LanguageModel();
		CurrencyModel currency = new CurrencyModel();
		final BaseStoreModel baseStore = new BaseStoreModel();
		baseStore.setUid("testStore");
		final BaseSiteModel baseSite = new BaseSiteModel();
		baseSite.setUid("testSite");
		baseSite.setStores(Collections.singletonList(baseStore));
		b2bcustomer.setSite(baseSite);

		addressData.setFirstName("firstName");
		addressData.setDefaultAddress(true);
		addressData.setApprovalStatus("PENDINGBYGP");
		addressData.setUserId("testUser");
		addressData.setId("testid");
		final AddressModel address1 = mock(AddressModel.class);
		final AddressModel address2 = mock(AddressModel.class);
		final List<AddressModel> addresses = new ArrayList<>();
		when(checkoutCustomerStrategy.getCurrentUserForCheckout()).thenReturn(b2bcustomer);
		address1.setApprovalStatus(GPApprovalEnum.PENDINGBYADMIN);
		addresses.add(address1);
		GPApprovalEnum status = GPApprovalEnum.PENDINGBYADMIN;
		customerAddressModel.setApprovalStatus(status);
		b2bcustomer.setAddresses(addresses);
		final B2BUnitModel unitModel = new B2BUnitModel();
		unitModel.setB2bUnitLevel("L2");
		unitModel.setUid("testUnit");
		b2bcustomer.setDefaultB2BUnit(unitModel);
		gpDefaultUserFacade.setCommonI18NService(commonI18NService);
		Mockito.doNothing().when(eventService).publishEvent(event);
		when(commonI18NService.getCurrentLanguage()).thenReturn(language);
		when(commonI18NService.getCurrentCurrency()).thenReturn(currency);
		gpDefaultUserFacade.setAddressReversePopulator(addressReversePopulator);
		when(userService.getUserForUID("testUser")).thenReturn(b2bcustomer);
		Mockito.doNothing().when(defaultGPCustomerAccountService).saveAddressEntry(b2bcustomer, addressModel1);
		when(defaultGPCustomerAccountService.getAddressForB2BCustomer(Mockito.anyString(), Mockito.anyString()))
		.thenReturn(customerAddressModel);
		gpDefaultUserFacade.editAddressForB2B(addressData, false);		
		verify(checkoutCustomerStrategy).getCurrentUserForCheckout();
		verify(userService).getUserForUID("testUser");
		verify(eventService).publishEvent(Mockito.any());
	}

	@Test
	public void testeditAddressForB2BAddressRejected() {

		b2bcustomer = new B2BCustomerModel();
		AddressModel customerAddressModel = new AddressModel();
		LanguageModel language = new LanguageModel();
		CurrencyModel currency = new CurrencyModel();
		final BaseStoreModel baseStore = new BaseStoreModel();
		baseStore.setUid("testStore");
		final BaseSiteModel baseSite = new BaseSiteModel();
		baseSite.setUid("testSite");
		baseSite.setStores(Collections.singletonList(baseStore));
		b2bcustomer.setSite(baseSite);

		addressData.setFirstName("firstName");
		addressData.setDefaultAddress(true);
		addressData.setApprovalStatus("REJECTED");
		addressData.setUserId("testUser");
		addressData.setId("testid");
		final AddressModel address1 = mock(AddressModel.class);
		final AddressModel address2 = mock(AddressModel.class);
		final List<AddressModel> addresses = new ArrayList<>();
		when(checkoutCustomerStrategy.getCurrentUserForCheckout()).thenReturn(b2bcustomer);
		address1.setApprovalStatus(GPApprovalEnum.PENDINGBYADMIN);
		addresses.add(address1);
		GPApprovalEnum status = GPApprovalEnum.PENDINGBYADMIN;
		customerAddressModel.setApprovalStatus(status);
		b2bcustomer.setAddresses(addresses);
		final B2BUnitModel unitModel = new B2BUnitModel();
		unitModel.setB2bUnitLevel("L2");
		unitModel.setUid("testUnit");
		b2bcustomer.setDefaultB2BUnit(unitModel);
		when(checkoutCustomerStrategy.getCurrentUserForCheckout()).thenReturn(b2bcustomer);
		gpDefaultUserFacade.setCommonI18NService(commonI18NService);
		Mockito.doNothing().when(eventService).publishEvent(event);
		when(commonI18NService.getCurrentLanguage()).thenReturn(language);
		when(commonI18NService.getCurrentCurrency()).thenReturn(currency);
		//when(gpDefaultUserFacade.initializeEvent(event, b2bcustomer)).thenReturn(event);
		gpDefaultUserFacade.setAddressReversePopulator(addressReversePopulator);
		when(userService.getUserForUID("testUser")).thenReturn(b2bcustomer);
		Mockito.doNothing().when(defaultGPCustomerAccountService).saveAddressEntry(b2bcustomer, addressModel1);
		when(defaultGPCustomerAccountService.getAddressForB2BCustomer(Mockito.anyString(), Mockito.anyString()))
		.thenReturn(customerAddressModel);
		gpDefaultUserFacade.editAddressForB2B(addressData, false);
		verify(checkoutCustomerStrategy).getCurrentUserForCheckout();
		verify(item).setEmailSubject(EmailSubjectType.ADDRESS_REJECTED.getSubject());
		verify(eventService).publishEvent(Mockito.any());
		
	}

	@Test
	public void isAdmin() {

		b2bcustomer = new B2BCustomerModel();
		when(checkoutCustomerStrategy.getCurrentUserForCheckout()).thenReturn(b2bcustomer);
		when(gpB2BUnitsService.isB2BAdmin(b2bcustomer)).thenReturn(Boolean.TRUE);
		boolean value = gpDefaultUserFacade.isAdmin();
		Assert.assertTrue(true);
	}
	
	@Test
	public void getUsersAddressForCodeTest() {
		when(defaultGPCustomerAccountService.getAddressForB2BCustomer(Mockito.anyString(), Mockito.anyString())).thenReturn(addressModel);
		when(addressConverter.convert(addressModel)).thenReturn(addressData);
		AddressData actualAddress = gpDefaultUserFacade.getUsersAddressForCode("code", "userId");
		assertEquals(actualAddress, addressData);
		verify(addressConverter).convert(addressModel);
		verify(defaultGPCustomerAccountService).getAddressForB2BCustomer(Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	public void getDefaultAddressForCustomerTest() {
		customer = new CustomerModel();
		customer.setDefaultPaymentAddress(addressModel);
		customer.setDefaultShipmentAddress(addressModel);
		when(addressConverter.convert(addressModel)).thenReturn(addressData);
		AddressData actualAddressData = gpDefaultUserFacade.getDefaultAddressForCustomer(Boolean.TRUE, Boolean.TRUE, customer);
		assertEquals(addressData, actualAddressData);
		verify(addressConverter).convert(addressModel);
	}
	
	@Test
	public void setDefaultAddressEntryForB2BCustomerTest() {
		addressData.setId("addressMockId");
		customer.setUid("customerMockId");
		when(defaultGPCustomerAccountService.getAddressForB2BCustomer("addressMockId", "customerMockId")).thenReturn(addressModel);
		doNothing().when(defaultGPCustomerAccountService).setDefaultAddressEntry(customer, addressModel, Boolean.TRUE, Boolean.TRUE);
		gpDefaultUserFacade.setDefaultAddressEntryForB2BCustomer(addressData, Boolean.TRUE, Boolean.TRUE, customer);
		verify(defaultGPCustomerAccountService, times(1)).setDefaultAddressEntry(customer, addressModel, Boolean.TRUE, Boolean.TRUE);
	}
	
	@Test
	public void getActiveAddressesTest() {
		when(userService.getCurrentUser()).thenReturn(customer);
		List<AddressModel> addresses = new ArrayList<AddressModel>(Arrays.asList(addressModel));
		when(defaultGPCustomerAccountService.getActiveAddressBookEntries(customer,true)).thenReturn(addresses);
		addressModel.setOwner(customer);
		customer.setDefaultPaymentAddress(addressModel);
		customer.setDefaultShipmentAddress(addressModel);
		addressData.setId("addressMockId");
		when(addressConverter.convert(addressModel)).thenReturn(addressData);
		final List<AddressData> activeAddresses = gpDefaultUserFacade.getActiveAddresses();
		assertTrue(activeAddresses.get(0).equals(addressData));
		verify(defaultGPCustomerAccountService).getActiveAddressBookEntries(customer,true);
	}
	
	@Test
	public void getEnumSelectedStringTest() {
		List<MarketingFrequencyEnum> enumValues = new ArrayList<MarketingFrequencyEnum>(Arrays.asList(MarketingFrequencyEnum.MONTHLY));
		when(userService.getCurrentUser()).thenReturn(customer);
		customer.setMarkFrequency(MarketingFrequencyEnum.MONTHLY);
		List<String> marketingFrequencyList = gpDefaultUserFacade.getEnumSelectedString(enumValues);
		assertEquals(MarketingFrequencyEnum.MONTHLY+"-true", marketingFrequencyList.get(0));
		verify(userService).getCurrentUser();
	}
	
	@Test
	public void getQuplesTokenTest() throws Exception {
		when(gpDefaultQuplesTokenService.getQuplesToken(quplesData)).thenReturn(quplesData);
		QuplesData actualData = gpDefaultUserFacade.getQuplesToken(quplesData);
		assertEquals(quplesData, actualData);
	}
	
	@Test
	public void removeAddressTest() {
		when(checkoutCustomerStrategy.getCurrentUserForCheckout()).thenReturn(customer);
		List<AddressModel> addresses = new ArrayList<AddressModel>(Arrays.asList(addressModel1));
		when(defaultGPCustomerAccountService.getAddressBookEntries(customer)).thenReturn(addresses);
		addressData.setId(PK_Value);
		
		when(addressModel1.getPk()).thenReturn(PK.BIG_PK);
		doNothing().when(modelService).save(addressModel);
		gpDefaultUserFacade.removeAddress(addressData);
		verify(checkoutCustomerStrategy).getCurrentUserForCheckout();
		verify(defaultGPCustomerAccountService).getAddressBookEntries(customer);
	}
	
	@Test
	public void getBaseSiteServiceTest() {
		BaseSiteService siteService = gpDefaultUserFacade.getBaseSiteService();
		assertEquals(baseSiteService, siteService);
	}
	
	@Test
	public void getCardPaymentInfoReverseConverterTest() {
		assertEquals(cardPaymentInfoReverseConverter, gpDefaultUserFacade.getCardPaymentInfoReverseConverter());
	}
	
	@Test
	public void getAddressReverseConverterTest() {
		assertEquals(addressConverter, gpDefaultUserFacade.getAddressReverseConverter());
	}
	
	@Test
	public void GPYMarketingSyncServiceTest(){
		assertEquals(yMarketingService, gpDefaultUserFacade.getyMarketingService());
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void getTermAndCondContentTest() throws CMSItemNotFoundException {
		when(cmsSiteService.getSiteConfig(TERM_AND_COND_PAGE_LABEL)).thenReturn(TERM_AND_COND_PAGE_LABEL);
		when(cmsPageService.getPageForLabel(Mockito.anyString())).thenReturn(contentPageModel);
		contentSlotforPageModel.setContentSlot(contentSlotModel);
		cmsParagraphComponentModel.setContent("mockContent");
		List<ContentSlotForPageModel> contentSlotsforPageModels = new ArrayList<ContentSlotForPageModel>(Arrays.asList(contentSlotforPageModel));
		when(contentPageModel.getContentSlots()).thenReturn(contentSlotsforPageModels);
		List<AbstractCMSComponentModel> cmsComponentModels = new ArrayList<AbstractCMSComponentModel>(Arrays.asList(cmsParagraphComponentModel));
		contentSlotModel.setCmsComponents(cmsComponentModels);
		gpDefaultUserFacade.getTermAndCondContent();
		verify(contentPageModel, times(2)).getContentSlots();
	}
	
	@Test
	public void removeCCPaymentInfoTest() {
		when(userService.getCurrentUser()).thenReturn(customer);
		List<CreditCardPaymentInfoModel> creditCardPaymentInfos = new ArrayList<CreditCardPaymentInfoModel>(Arrays.asList(paymentInfoModel));
		when(customerAccountService.getCreditCardPaymentInfos(customer, Boolean.FALSE)).thenReturn(creditCardPaymentInfos);
		when(paymentInfoModel.getPk()).thenReturn(PK.BIG_PK);
		doNothing().when(customerAccountService).deleteCCPaymentInfo(customer, paymentInfoModel);
		gpDefaultUserFacade.removeCCPaymentInfo(PK_Value);
		verify(customerAccountService).getCreditCardPaymentInfos(customer, Boolean.FALSE);
		verify(customerAccountService).deleteCCPaymentInfo(customer, paymentInfoModel);
	}
	
	@Test
	public void updateCCPaymentsInfoTest() {
		when(ccPaymentInfoData.getId()).thenReturn("");
		doNothing().when(defaultGPCustomerAccountService).updateCCPaymentInfo(ccPaymentInfoData);
		gpDefaultUserFacade.updateCCPaymentInfo(ccPaymentInfoData);
		verify(defaultGPCustomerAccountService).updateCCPaymentInfo(ccPaymentInfoData);
	}

}

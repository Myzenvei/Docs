package com.gp.commerce.facades.order.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import com.gp.commerce.core.model.ApplePayPaymentInfoModel;
import com.gp.commerce.core.model.GooglePayPaymentInfoModel;
import com.gp.commerce.dto.payment.*;
import com.gp.commerce.facades.data.GooglePayRequestData;
import com.gp.commerce.order.data.GPApplePayPaymentInfoData;
import com.gp.commerce.order.data.GooglePayPaymentInfoData;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commerceservices.order.CommerceCardTypeService;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.strategies.impl.DefaultCheckoutCustomerStrategy;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.dto.CardType;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.cybersource.schemas.transaction_data_1.ReplyMessage;
import com.gp.commerce.core.customer.impl.GPCustomerAccountService;
import com.gp.commerce.core.service.auth.impl.GPDefaultPaymentAuthService;
import com.gp.commerce.data.cart.GPApplePayBillingContactData;
import com.gp.commerce.data.cart.GPApplePayPaymentMethodData;
import com.gp.commerce.data.cart.GPApplePayPaymentTokenData;
import com.gp.commerce.data.cart.GPApplePayTokenInfoData;
import com.gpintegration.service.GPCybersourceIntegrationService;

/**
 *
 */
@UnitTest
public class GPCheckoutFacadeTest
{

	@InjectMocks
	GPCheckoutFacade gpCheckoutFacade = new GPCheckoutFacade();

	@Mock
	private GPCustomerAccountService gpCustomerAccountService;

	@Mock
	private GPDefaultPaymentAuthService paymentAuthService;

	@Mock
	private CartService cartService;

	@Mock
	private CommerceCardTypeService commerceCardTypeService;

	@Mock
	private DefaultCheckoutCustomerStrategy checkoutCustomerStrategy;

	@Mock
	private UserService userService;

	@Mock
	private AbstractPopulatingConverter<CreditCardPaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoConverter;

	@Mock
	private CartFacade mockCartFacade;

	@Mock
	private CommerceCheckoutService commerceCheckoutService;

	@Mock
	private GPCybersourceIntegrationService gpCybersourceIntegrationService;

	@Mock
	private ReplyMessage replyMessage;

	@Mock
	private Converter<CartModel, GooglePayRequestData> gpGooglePayPayRequestConverter;

	private AddressData addressData;

	private CCPaymentInfoData paymentInfoData;

	private PaymentDataWsDTO paymentDataWsDTO;

	private CartModel cartModel;

	private CustomerModel userModel;

	private AddressModel addressModel;

	private CommerceCheckoutParameter commerceCheckoutParameter;

	private final String provider = "paymentProvider";
	
	private GPApplePayPaymentTokenData applePayPaymentTokenData;

	protected static class MockAddressModel extends AddressModel
	{
		@Override
		public PK getPk()
		{
			return de.hybris.platform.core.PK.fromLong(9999l);
		}
	}

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		gpCheckoutFacade.setCartService(cartService);
		gpCheckoutFacade.setCommerceCardTypeService(commerceCardTypeService);
		gpCheckoutFacade.setCheckoutCustomerStrategy(checkoutCustomerStrategy);
		gpCheckoutFacade.setUserService(userService);
		gpCheckoutFacade.setCreditCardPaymentInfoConverter(creditCardPaymentInfoConverter);
		gpCheckoutFacade.setCartFacade(mockCartFacade);
		gpCheckoutFacade.setCommerceCheckoutService(commerceCheckoutService);
		gpCheckoutFacade.setCustomerAccountService(gpCustomerAccountService);
		final CountryData countryData = new CountryData();
		countryData.setIsocode("US");
		addressData = new AddressData();
		addressData.setId("9999");
		addressData.setTown("Newyork");
		addressData.setCountry(countryData);
		addressData.setFirstName("Test_User");
		addressData.setLastName("TS");
		addressData.setEmail("test@abc.com");
		addressData.setPhone("9999999999");
		addressData.setPostalCode("657657");
		addressData.setLine1("sample street");
		addressData.setLine2("sample street2");
		paymentInfoData = new CCPaymentInfoData();
		paymentInfoData.setId("paymentId");
		paymentInfoData.setBillingAddress(addressData);
		paymentInfoData.setExpiryMonth("5");
		paymentInfoData.setExpiryYear("2012");
		paymentInfoData.setIssueNumber("22");
		paymentInfoData.setAccountHolderName("Test_User_Name");
		commerceCheckoutParameter = new CommerceCheckoutParameter();
		commerceCheckoutParameter.setEnableHooks(true);
		final CreditCardPaymentInfoModel paymentInfoModel = new CreditCardPaymentInfoModel();
		paymentInfoModel.setSubscriptionId("subid");
		cartModel = new CartModel();
		userModel = new CustomerModel();
		addressModel = new MockAddressModel();
		final CountryModel countryModel = new CountryModel();
		addressModel.setCountry(countryModel);
		userModel.setDefaultShipmentAddress(addressModel);
		cartModel.setUser(userModel);
		cartModel.setPaymentInfo(paymentInfoModel);
		final CardType cardType = new CardType(CreditCardType.MASTER.getCode().toLowerCase(), CreditCardType.MASTER,
				CreditCardType.MASTER.getCode().toLowerCase());
		paymentInfoData.setCardType(cardType.getCode().toString().toLowerCase());
		given(Boolean.valueOf(cartService.hasSessionCart())).willReturn(Boolean.TRUE);
		//given(mockCartFacade.getSessionCart()).willReturn(cartData);
		given(Boolean.valueOf(mockCartFacade.hasSessionCart())).willReturn(Boolean.TRUE);
		given(commerceCardTypeService.getCardTypeForCode(CreditCardType.MASTER.getCode().toLowerCase())).willReturn(cardType);
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(checkoutCustomerStrategy.getCurrentUserForCheckout()).willReturn(userModel);
		given(userService.getCurrentUser()).willReturn(userModel);
		given(creditCardPaymentInfoConverter.convert(paymentInfoModel)).willReturn(paymentInfoData);


		final PaymentTransactionEntryModel paymentTransactionEntryModel = new PaymentTransactionEntryModel();

		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setSecurityCode("1234");
		parameter.setPaymentProvider(provider);
		given(paymentAuthService.getCCPaymentAuthentication((any(CommerceCheckoutParameter.class))))
				.willReturn(paymentTransactionEntryModel);

		buildPaymentDataWsDTO();

	}

	/**
	 * Test Method to check create Payment subscription
	 */
	@Test
	public void testCreatePaymentSubscription()
	{
		final CreditCardPaymentInfoModel ccPaymentInfoModel = new CreditCardPaymentInfoModel();
		given(gpCustomerAccountService.createPaymentSubscription(Mockito.any(CustomerModel.class), Mockito.any(CardInfo.class),
				Mockito.any(BillingInfo.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString()))
						.willReturn(ccPaymentInfoModel);
		gpCheckoutFacade.createPaymentSubscription(paymentInfoData);
		verify(gpCustomerAccountService).createPaymentSubscription(Mockito.any(CustomerModel.class), Mockito.any(CardInfo.class),
				Mockito.any(BillingInfo.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString());
	}

	/**
	 * Test Method to check create Google pay Payment info
	 */
	@Test
	public void testcreateGooglePayPaymentInfo()
	{
		final GooglePayPaymentInfoModel googlePayPaymentInfoModel = new GooglePayPaymentInfoModel();

		given(gpCustomerAccountService.createGooglePayPaymentInfo(Mockito.any(CustomerModel.class), Mockito.any(CardInfo.class),
				Mockito.any(BillingInfo.class), Mockito.anyString(),Mockito.anyBoolean(), Mockito.anyString(),  Mockito.anyString(),Mockito.anyString(),Mockito.anyString()))
				.willReturn(googlePayPaymentInfoModel);

		gpCheckoutFacade.createGooglePayPaymentInfo(paymentDataWsDTO);

		verify(gpCustomerAccountService).createGooglePayPaymentInfo(Mockito.any(CustomerModel.class), Mockito.any(CardInfo.class),
				Mockito.any(BillingInfo.class), Mockito.anyString(),Mockito.anyBoolean(), Mockito.anyString(),  Mockito.anyString(),Mockito.anyString(),Mockito.anyString());
	}
	
	@Test
	public void testCreateApplePayPaymentInfo() {
		ApplePayPaymentInfoModel applePayPaymentInfoModel = new ApplePayPaymentInfoModel();

		given(gpCustomerAccountService.createApplePayPaymentInfo(Mockito.any(CustomerModel.class),
				Mockito.any(CardInfo.class), Mockito.any(BillingInfo.class), Mockito.anyString(), Mockito.anyBoolean(),
				applePayPaymentTokenData)).willReturn(applePayPaymentInfoModel);

		gpCheckoutFacade.createApplePayPaymentInfo(applePayPaymentTokenData);
		verify(gpCustomerAccountService).createApplePayPaymentInfo(Mockito.any(CustomerModel.class),
				Mockito.any(CardInfo.class), Mockito.any(BillingInfo.class), Mockito.anyString(), Mockito.anyBoolean(),
				applePayPaymentTokenData);
	}

	/**
	 *  Test method to buildGooglePayRequest
	 */
	@Test
	public  void testBuildGooglePayRequest(){

		cartModel=new CartModel();

		GooglePayRequestData googlePayRequestData=new GooglePayRequestData();

		googlePayRequestData.setApiVersionMinor("2");
		googlePayRequestData.setApiVersion("0");

		Mockito.when(gpGooglePayPayRequestConverter.convert(cartModel)).thenReturn(Mockito.mock(GooglePayRequestData.class));

		GooglePayRequestData googlePayRequestData1=  gpCheckoutFacade.buildGooglePayRequest();

		Assert.assertSame(googlePayRequestData.getApiVersion(),googlePayRequestData1.getApiVersion());

	}

	/**
	 * Not Null method for createPaymentSubscription
	 */
	@Test
	public void testCreatePaymentSubscriptionNoInfoModel()
	{
		final CCPaymentInfoData data = gpCheckoutFacade.createPaymentSubscription(paymentInfoData);
		Assert.assertNull(data);
	}

	@Test
	public void testAuthorizePayment()
	{
		final String provider = "paymentProvider";
		final PaymentTransactionEntryModel paymentTransactionEntryModel = new PaymentTransactionEntryModel();
		given(commerceCheckoutService.getPaymentProvider()).willReturn(provider);

		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setSecurityCode("1234");
		parameter.setPaymentProvider(provider);
		given(paymentAuthService.getCCPaymentAuthentication((any(CommerceCheckoutParameter.class))))
				.willReturn(paymentTransactionEntryModel);
		gpCheckoutFacade.authorizePayment("1234");
		verify(paymentAuthService).getCCPaymentAuthentication(any(CommerceCheckoutParameter.class));
	}

	/**
	 * Test Method to checkIfCurrentUserIsNotTheCartUser
	 */
	@Test
	public void testNotCartUserCreatePaymentSubscription()
	{
		Mockito.when(cartService.getSessionCart()).thenReturn(null);
		Assert.assertEquals(null, gpCheckoutFacade.createPaymentSubscription(paymentInfoData));
		
	}
	
	private GPApplePayPaymentTokenData buildApplePayPaymentData() {
		applePayPaymentTokenData = new GPApplePayPaymentTokenData();

		GPApplePayTokenInfoData applePayTokenInfoData = new GPApplePayTokenInfoData();
		applePayTokenInfoData.setPaymentData("ApplePayPaymentData");
		applePayTokenInfoData.setTransactionIdentifier("111111000000111");

		applePayPaymentTokenData.setToken(applePayTokenInfoData);

		GPApplePayPaymentMethodData applePayPaymentMethodData = new GPApplePayPaymentMethodData();
		applePayPaymentMethodData.setDisplayName("MasterCard 1505");
		applePayPaymentMethodData.setNetwork("MasterCard");
		applePayPaymentMethodData.setType("debit");

		applePayPaymentTokenData.getToken().setPaymentMethod(applePayPaymentMethodData);

		GPApplePayBillingContactData applePayBillingContactData = new GPApplePayBillingContactData();
		List<String> supplierNames = Arrays.asList("901 International Pkwy");
		applePayBillingContactData.setAddressLines(supplierNames);
		applePayBillingContactData.setAdministrativeArea("US-FL");
		applePayBillingContactData.setCountry("US");
		applePayBillingContactData.setFamilyName("Apple");
		applePayBillingContactData.setGivenName("Test");
		applePayBillingContactData.setLocality("Lake Mary");
		applePayBillingContactData.setPostalCode("32746");

		applePayPaymentTokenData.setBillingContact(applePayBillingContactData);

		return applePayPaymentTokenData;

	}

	@Test
	private void testAuthorizeApplePayPayment() {

		GPApplePayPaymentInfoData applePayPaymentInfoData = new GPApplePayPaymentInfoData();

		final PaymentTransactionEntryModel paymentTransactionEntryModel = new PaymentTransactionEntryModel();

		paymentTransactionEntryModel.setTransactionStatus(TransactionStatus.ACCEPTED.name());

		Mockito.when(paymentAuthService.authorizeCybersource(applePayPaymentInfoData,Mockito.mock(CommerceCheckoutParameter.class))).thenReturn(paymentTransactionEntryModel);

		boolean result=gpCheckoutFacade.authorizePayment("");

		Assert.assertEquals(true, result);

	}

	private PaymentDataWsDTO buildPaymentDataWsDTO(){

		paymentDataWsDTO=new PaymentDataWsDTO();

		PaymentMethodDataWsDTO paymentMethodDataWsDTO=new PaymentMethodDataWsDTO();

		paymentMethodDataWsDTO.setDescription("Visa 1234");
		paymentMethodDataWsDTO.setType("CARD");

		CardInfoWsDTO cardInfoWsDTO= new CardInfoWsDTO();
		cardInfoWsDTO.setCardDetails("1234");
		cardInfoWsDTO.setCardNetwork("VISA");

		AddressWsDTO addressWsDTO=new AddressWsDTO();

		addressWsDTO.setAddress1(" 342 Plano st");
		addressWsDTO.setAdministrativeArea("FL");
		addressWsDTO.setLocality("Kimberland");
		addressWsDTO.setPostalCode("08352");
		addressWsDTO.setPhoneNumber("9999999999");
		addressWsDTO.setCountryCode("US");

		PaymentMethodTokenizationDataWsDTO paymentMethodTokenizationDataWsDTO=new PaymentMethodTokenizationDataWsDTO();
		paymentMethodTokenizationDataWsDTO.setType("PAYMENT_GATEWAY");
		paymentMethodTokenizationDataWsDTO.setToken("2136Token");
		paymentMethodDataWsDTO.setInfo(cardInfoWsDTO);
		paymentMethodDataWsDTO.setTokenizationData(paymentMethodTokenizationDataWsDTO);

		paymentDataWsDTO.setPaymentMethodData(paymentMethodDataWsDTO);

		return paymentDataWsDTO;
	}

	@Test
	private void testAuthorizeGooglePayPayment(){


		GooglePayPaymentInfoData googlePayPaymentInfoData=new GooglePayPaymentInfoData();

		final PaymentTransactionEntryModel paymentTransactionEntryModel = new PaymentTransactionEntryModel();

		paymentTransactionEntryModel.setTransactionStatus(TransactionStatus.ACCEPTED.name());

		Mockito.when(paymentAuthService.authorizeCybersource( googlePayPaymentInfoData,Mockito.mock(CommerceCheckoutParameter.class))).thenReturn(paymentTransactionEntryModel);

		boolean result=gpCheckoutFacade.authorizePayment("");

		Assert.assertEquals(true,result);

	}
}

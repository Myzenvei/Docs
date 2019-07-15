package com.gp.commerce.core.customer.impl;

import com.gp.commerce.core.model.ApplePayPaymentInfoModel;
import com.gp.commerce.core.model.GooglePayPaymentInfoModel;
import com.gp.commerce.data.cart.GPApplePayPaymentMethodData;
import com.gp.commerce.data.cart.GPApplePayPaymentTokenData;
import com.gp.commerce.data.cart.GPApplePayTokenInfoData;

import de.hybris.platform.core.model.c2l.RegionModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Currency;

import org.junit.Assert;
import com.gp.commerce.core.customer.impl.GPCustomerAccountService;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.dto.NewSubscription;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

@UnitTest
public class GPCustomerAccountServiceTest {

	@InjectMocks
	private GPCustomerAccountService service = new GPCustomerAccountService();
	
	@Mock
	private CommonI18NService commonI18NService;
	
	@Mock
	private I18NService i18nService;
	
	@Mock
	private ModelService modelService;
	
	@Mock
	private FlexibleSearchService flexibleSearchService;
	
	@Mock
	private CustomerEmailResolutionService customerEmailResolutionService;
	
	@Mock
	private PaymentService paymentService;
	
	@Mock
	private CartService cartService;
	
	@Mock
	protected ProductService productService;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testCreatePaymentSubscription()
	{
		CustomerModel customerModel = Mockito.mock(CustomerModel.class);
		CardInfo cardInfo = Mockito.mock(CardInfo.class);
		BillingInfo billingInfo = Mockito.mock(BillingInfo.class);
		String titleCode = "title";
		String paymentProvider = "provider";
		boolean saveInAccount = true;
		final String paymentToken = "token";
		
		final CurrencyModel currencyModel = Mockito.mock(CurrencyModel.class);
		Mockito.when(customerModel.getSessionCurrency()).thenReturn(currencyModel);
		
		Mockito.when(currencyModel.getIsocode()).thenReturn("USD");
		
		final Currency currency = Currency.getInstance("USD");
		Mockito.when(i18nService.getBestMatchingJavaCurrency(Mockito.anyString())).thenReturn(currency);
		
		final AddressModel billingAddress = new AddressModel();
		Mockito.when(modelService.create(AddressModel.class)).thenReturn(billingAddress);
		
		Mockito.when(flexibleSearchService.getModelByExample(Mockito.any(TitleModel.class))).thenReturn(Mockito.mock(TitleModel.class));
		
		Mockito.when(billingInfo.getFirstName()).thenReturn("firstName");
		Mockito.when(billingInfo.getLastName()).thenReturn("lastName");
		Mockito.when(billingInfo.getStreet1()).thenReturn("street1");
		Mockito.when(billingInfo.getStreet2()).thenReturn("street2");
		Mockito.when(billingInfo.getCity()).thenReturn("city");
		Mockito.when(billingInfo.getPostalCode()).thenReturn("pCode");
		Mockito.when(billingInfo.getCountry()).thenReturn("US");
		
		Mockito.when(commonI18NService.getCountry(Mockito.anyString())).thenReturn(Mockito.mock(CountryModel.class));
		
		Mockito.when(billingInfo.getRegion()).thenReturn(null);
		Mockito.when(billingInfo.getPhoneNumber()).thenReturn("1234567890");
		
		Mockito.when(customerEmailResolutionService.getEmailForCustomer(customerModel)).thenReturn("abc@xyz.com");
		
		Mockito.when(customerModel.getUid()).thenReturn("customerUid");
		
		final NewSubscription subscription = Mockito.mock(NewSubscription.class);
		Mockito.when(paymentService.createSubscription(Mockito.anyString(), Mockito.anyString(),
				Mockito.any(Currency.class), Mockito.any(AddressModel.class),
				Mockito.any(CardInfo.class))).thenReturn(subscription);
		
		Mockito.when(subscription.getSubscriptionID()).thenReturn("subscriptionID");
		
		final CreditCardPaymentInfoModel cardPaymentInfoModel = new CreditCardPaymentInfoModel();
		Mockito.when(modelService.create(CreditCardPaymentInfoModel.class)).thenReturn(cardPaymentInfoModel);
		
		Mockito.when(cardInfo.getCardNumber()).thenReturn("1111 1111 1111 1111");
		
		final CreditCardType creditCard = CreditCardType.VISA;
		Mockito.when(cardInfo.getCardType()).thenReturn(creditCard);
		
		Mockito.when(cardInfo.getCardHolderFullName()).thenReturn("fullName");
		Mockito.when(cardInfo.getExpirationMonth()).thenReturn(new Integer(1));
		Mockito.when(cardInfo.getExpirationYear()).thenReturn(new Integer(2020));
		Mockito.when(cardInfo.getIssueMonth()).thenReturn(new Integer(1));
		Mockito.when(cardInfo.getIssueYear()).thenReturn(new Integer(2016));
		Mockito.when(cardInfo.getIssueNumber()).thenReturn("222");
		
		Mockito.doNothing().when(modelService).saveAll(billingAddress, cardPaymentInfoModel);
		Mockito.doNothing().when(modelService).refresh(customerModel);
		
		Assert.assertNotNull(service.createPaymentSubscription(customerModel, cardInfo,
			billingInfo, titleCode, paymentProvider, saveInAccount, paymentToken));
	}

	@Test
	public void testCreateGooglePayPaymentInfo()
	{
		CustomerModel customerModel = Mockito.mock(CustomerModel.class);
		CardInfo cardInfo = Mockito.mock(CardInfo.class);
		BillingInfo billingInfo = Mockito.mock(BillingInfo.class);
		String paymentProvider = "provider";
		boolean saveInAccount = true;
		final String paymentToken = "token";
		final String merchantDescription = "merchantDescription";
        final String paymentMethodType="paymentMethodType";
        final String paymentTokenizationType="paymentTokenizationType";

		final CurrencyModel currencyModel = Mockito.mock(CurrencyModel.class);
		Mockito.when(customerModel.getSessionCurrency()).thenReturn(currencyModel);

		Mockito.when(currencyModel.getIsocode()).thenReturn("USD");

		final Currency currency = Currency.getInstance("USD");
		Mockito.when(i18nService.getBestMatchingJavaCurrency(Mockito.anyString())).thenReturn(currency);

		final AddressModel billingAddress = new AddressModel();
		Mockito.when(modelService.create(AddressModel.class)).thenReturn(billingAddress);

		Mockito.when(flexibleSearchService.getModelByExample(Mockito.any(TitleModel.class))).thenReturn(Mockito.mock(TitleModel.class));

		Mockito.when(billingInfo.getFirstName()).thenReturn("firstName");
		Mockito.when(billingInfo.getLastName()).thenReturn("lastName");
		Mockito.when(billingInfo.getStreet1()).thenReturn("street1");
		Mockito.when(billingInfo.getStreet2()).thenReturn("street2");
		Mockito.when(billingInfo.getCity()).thenReturn("city");
		Mockito.when(billingInfo.getPostalCode()).thenReturn("pCode");
		Mockito.when(billingInfo.getCountry()).thenReturn("US");

		Mockito.when(commonI18NService.getCountry(Mockito.anyString())).thenReturn(Mockito.mock(CountryModel.class));
		Mockito.when(commonI18NService.getRegion((CountryModel) Mockito.anyObject(),Mockito.anyString())).thenReturn(Mockito.mock(RegionModel.class));

		Mockito.when(billingInfo.getPhoneNumber()).thenReturn("1234567890");

		Mockito.when(customerEmailResolutionService.getEmailForCustomer(customerModel)).thenReturn("abc@xyz.com");

		Mockito.when(customerModel.getUid()).thenReturn("customerUid");


		final GooglePayPaymentInfoModel googlePayPaymentInfoModel = new GooglePayPaymentInfoModel();

		Mockito.when(modelService.create(GooglePayPaymentInfoModel.class)).thenReturn(googlePayPaymentInfoModel);

		Mockito.when(cardInfo.getCardNumber()).thenReturn("1111");

		final CreditCardType creditCard = CreditCardType.VISA;

		Mockito.when(cardInfo.getCardType()).thenReturn(creditCard);

		Mockito.when(cardInfo.getCardHolderFullName()).thenReturn("fullName");

		Mockito.doNothing().when(modelService).saveAll(billingAddress, googlePayPaymentInfoModel);
		Mockito.doNothing().when(modelService).refresh(customerModel);

		Assert.assertNotNull(service.createGooglePayPaymentInfo(customerModel, cardInfo,
				billingInfo, paymentProvider, saveInAccount, paymentToken,merchantDescription,paymentMethodType,paymentTokenizationType));
	}
	
	@Test
	public void testCreateApplePaymentSubscription()
	{
		CustomerModel customerModel = Mockito.mock(CustomerModel.class);
		CardInfo cardInfo = Mockito.mock(CardInfo.class);
		BillingInfo billingInfo = Mockito.mock(BillingInfo.class);
		GPApplePayPaymentTokenData applePayPaymentTokenData = Mockito.mock(GPApplePayPaymentTokenData.class);
		GPApplePayTokenInfoData applePayTokenInfoData = Mockito.mock(GPApplePayTokenInfoData.class);
		GPApplePayPaymentMethodData applePayPaymentMethodData = Mockito.mock(GPApplePayPaymentMethodData.class);
		CartModel cartModel = Mockito.mock(CartModel.class);
		ProductModel productModel = Mockito.mock(ProductModel.class);
		CartEntryModel cartEntryModel = Mockito.mock(CartEntryModel.class);
		String paymentProvider = "provider";
		boolean saveInAccount = true;

		final CurrencyModel currencyModel = Mockito.mock(CurrencyModel.class);
		Mockito.when(customerModel.getSessionCurrency()).thenReturn(currencyModel);
		
		Mockito.when(currencyModel.getIsocode()).thenReturn("USD");
		
		final Currency currency = Currency.getInstance("USD");
		Mockito.when(i18nService.getBestMatchingJavaCurrency(Mockito.anyString())).thenReturn(currency);
		
		final AddressModel billingAddress = new AddressModel();
		Mockito.when(modelService.create(AddressModel.class)).thenReturn(billingAddress);

		Mockito.when(flexibleSearchService.getModelByExample(Mockito.any(TitleModel.class))).thenReturn(Mockito.mock(TitleModel.class));

		Mockito.when(billingInfo.getFirstName()).thenReturn("ApplePay");
		Mockito.when(billingInfo.getLastName()).thenReturn("Test");
		Mockito.when(billingInfo.getStreet1()).thenReturn("472 Sun Lake Cir");
		Mockito.when(billingInfo.getStreet2()).thenReturn("");
		Mockito.when(billingInfo.getCity()).thenReturn("Lake Mary");
		Mockito.when(billingInfo.getPostalCode()).thenReturn("32746");
		Mockito.when(billingInfo.getCountry()).thenReturn("US");

		Mockito.when(commonI18NService.getCountry(Mockito.anyString())).thenReturn(Mockito.mock(CountryModel.class));

		Mockito.when(billingInfo.getRegion()).thenReturn(null);
		Mockito.when(billingInfo.getPhoneNumber()).thenReturn("1234567890");

		Mockito.when(customerEmailResolutionService.getEmailForCustomer(customerModel)).thenReturn("applepay@gmail.com");

		Mockito.when(customerModel.getUid()).thenReturn("applepay@gmail.com|b2bwhitelabel");

		final ApplePayPaymentInfoModel applePayPaymentInfoModel = new ApplePayPaymentInfoModel();

		Mockito.when(modelService.create(ApplePayPaymentInfoModel.class)).thenReturn(applePayPaymentInfoModel);

		final CreditCardType creditCard = CreditCardType.VISA;
		Mockito.when(cardInfo.getCardType()).thenReturn(creditCard);

		Mockito.when(cardInfo.getCardHolderFullName()).thenReturn("ApplePay Test");
		
		Mockito.when(applePayPaymentTokenData.getToken()).thenReturn(applePayTokenInfoData);
		Mockito.when(applePayPaymentTokenData.getToken().getPaymentMethod()).thenReturn(applePayPaymentMethodData);
		
		Mockito.when(applePayPaymentTokenData.getToken().getPaymentData()).thenReturn("ApplePayPaymentDataToken");
		Mockito.when(applePayPaymentTokenData.getToken().getPaymentMethod().getDisplayName()).thenReturn("Amex 1212");
		Mockito.when(applePayPaymentTokenData.getToken().getTransactionIdentifier()).thenReturn("ApplePayTestIdentifier");

		Mockito.doNothing().when(modelService).saveAll(billingAddress, applePayPaymentInfoModel);
		Mockito.doNothing().when(modelService).refresh(customerModel);
		
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);
		Mockito.when(productService.getProductForCode("testproduct")).thenReturn(productModel);
		Mockito.when(cartService.addNewEntry(cartModel, productModel, 1, null)).thenReturn(cartEntryModel);

		modelService.save(cartEntryModel);
		modelService.save(cartModel);
		cartService.setSessionCart(cartModel);
		
		Assert.assertNotNull(service.createApplePayPaymentInfo(customerModel, cardInfo, billingInfo, paymentProvider, saveInAccount, applePayPaymentTokenData));
		
	}

}

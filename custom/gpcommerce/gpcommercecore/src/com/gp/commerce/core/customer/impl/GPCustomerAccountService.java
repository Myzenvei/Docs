/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.customer.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.util.Currency;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.core.model.ApplePayPaymentInfoModel;
import com.gp.commerce.core.model.GooglePayPaymentInfoModel;
import com.gp.commerce.data.cart.GPApplePayPaymentTokenData;

import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.dto.NewSubscription;


/**
 * Extension of {@link DefaultCustomerAccountService}
 */
public class GPCustomerAccountService extends DefaultCustomerAccountService{

	private static final String DECIMAL_FORMAT = "%02d";
	private static final Logger LOG = Logger.getLogger(GPCustomerAccountService.class);

	@Resource
	private CartService cartService;

	/**
	 * This method creates google pay payment info model and returns the created
	 * model or null if any exceptions
	 * 
	 * @param customerModel           the customer model
	 * @param cardInfo                the card info
	 * @param billingInfo             the billing info
	 * @param paymentProvider         the payment provider
	 * @param saveInAccount           the save in account
	 * @param paymentToken            the payment token
	 * @param merchantDescription     the merchant description
	 * @param paymentMethodType       the payment type
	 * @param paymentTokenizationType the toeknisation type of payment
	 * @return {@link GooglePayPaymentInfoModel}
	 */
	public GooglePayPaymentInfoModel createGooglePayPaymentInfo(final CustomerModel customerModel, final CardInfo cardInfo,
			final BillingInfo billingInfo, final String paymentProvider, final boolean saveInAccount,final String paymentToken,String merchantDescription,String paymentMethodType,String paymentTokenizationType) {

		validateParameterNotNull(customerModel,"Customer cannot be null");
		validateParameterNotNull(cardInfo,"CardInfo cannot be null");
		validateParameterNotNull(billingInfo,"BillingInfo cannot be null");
		validateParameterNotNull(paymentProvider,"Payment Provider cannot be null");
		validateParameterNotNull(paymentToken,"paymentToken cannot be null");

		final AddressModel billingAddress = populateAddress(billingInfo,"");

		final String email = getCustomerEmailResolutionService().getEmailForCustomer(customerModel);

		billingAddress.setEmail(email);

		try
		{
				final GooglePayPaymentInfoModel googlePayPaymentInfoModel = getModelService().create(GooglePayPaymentInfoModel.class);

				googlePayPaymentInfoModel.setCode(customerModel.getUid() + "_" + UUID.randomUUID());
				googlePayPaymentInfoModel.setUser(customerModel);
				googlePayPaymentInfoModel.setNumber(cardInfo.getCardNumber());
			    googlePayPaymentInfoModel.setGooglePayEmail(billingInfo.getEmail());
				googlePayPaymentInfoModel.setType(cardInfo.getCardType());
				googlePayPaymentInfoModel.setCcOwner(cardInfo.getCardHolderFullName());
				googlePayPaymentInfoModel.setGooglePayToken(paymentToken);
			    googlePayPaymentInfoModel.setPaymentMethodTypeDescription(merchantDescription);
				googlePayPaymentInfoModel.setSaved(saveInAccount);
			    googlePayPaymentInfoModel.setPaymentMethodType(paymentMethodType);
			    googlePayPaymentInfoModel.setPaymentTokenizationType(paymentTokenizationType);

			    //Since these fields are manadatory but will not be received in google pay response, set to empty.
    			googlePayPaymentInfoModel.setValidToMonth("");
    			googlePayPaymentInfoModel.setValidToYear("");
    			googlePayPaymentInfoModel.setPaymentToken("");

				billingAddress.setOwner(googlePayPaymentInfoModel);

				googlePayPaymentInfoModel.setBillingAddress(billingAddress);

				getModelService().saveAll(billingAddress, googlePayPaymentInfoModel);

				addPaymentInfo(customerModel, googlePayPaymentInfoModel);

     			addPaymentInfoToCart(googlePayPaymentInfoModel);

     			return googlePayPaymentInfoModel;

		}
		catch (final AdapterException ae) //NOSONAR
		{
			LOG.error("Failed to create google pay payment info for customer. Customer PK: " + customerModel.getPk() +
					" Exception: " + ae.getMessage());

			return null;
		}

	}


	/**
	 * This method creates payment subscription
	 * @param customerModel
	 * 			the customer model
	 * @param cardInfo
	 * 			the card info
	 * @param billingInfo
	 * 			the billing info
	 * @param titleCode
	 * 			the title code
	 * @param paymentProvider
	 * 			the payment provider
	 * @param saveInAccount
	 * 			the save in account
	 * @param paymentToken
	 * 			the payment token
	 * @return credit card payment info model
	 */
	public CreditCardPaymentInfoModel createPaymentSubscription(final CustomerModel customerModel, final CardInfo cardInfo,
																final BillingInfo billingInfo, final String titleCode, final String paymentProvider, final boolean saveInAccount,final String paymentToken) {

		validateParameterNotNull(customerModel, "Customer cannot be null");
		validateParameterNotNull(cardInfo, "CardInfo cannot be null");
		validateParameterNotNull(billingInfo, "billingInfo cannot be null");
		validateParameterNotNull(paymentProvider, "PaymentProvider cannot be null");
		validateParameterNotNull(paymentToken, "paymentToken cannot be null");

		final CurrencyModel currencyModel = getCurrency(customerModel);
		validateParameterNotNull(currencyModel, "Customer session currency cannot be null");

		final Currency currency = getI18nService().getBestMatchingJavaCurrency(currencyModel.getIsocode());

		final AddressModel billingAddress = populateAddress(billingInfo,titleCode);

		final String email = getCustomerEmailResolutionService().getEmailForCustomer(customerModel);

		billingAddress.setEmail(email);

		final String merchantTransactionCode = customerModel.getUid() + "-" + UUID.randomUUID();

		try
		{
			final NewSubscription subscription = getPaymentService().createSubscription(merchantTransactionCode, paymentProvider,
					currency, billingAddress, cardInfo);

			if (StringUtils.isNotBlank(subscription.getSubscriptionID()))
			{
				final CreditCardPaymentInfoModel cardPaymentInfoModel = getModelService().create(CreditCardPaymentInfoModel.class);
				cardPaymentInfoModel.setCode(customerModel.getUid() + "_" + UUID.randomUUID());
				cardPaymentInfoModel.setUser(customerModel);
				cardPaymentInfoModel.setSubscriptionId(subscription.getSubscriptionID());
				cardPaymentInfoModel.setNumber(getMaskedCardNumber(cardInfo.getCardNumber()));
				cardPaymentInfoModel.setType(cardInfo.getCardType());
				cardPaymentInfoModel.setCcOwner(cardInfo.getCardHolderFullName());
				cardPaymentInfoModel.setValidToMonth(String.format(DECIMAL_FORMAT, cardInfo.getExpirationMonth()));
				cardPaymentInfoModel.setValidToYear(String.valueOf(cardInfo.getExpirationYear()));
				cardPaymentInfoModel.setPaymentToken(paymentToken);
				if (cardInfo.getIssueMonth() != null)
				{
					cardPaymentInfoModel.setValidFromMonth(String.valueOf(cardInfo.getIssueMonth()));
				}
				if (cardInfo.getIssueYear() != null)
				{
					cardPaymentInfoModel.setValidFromYear(String.valueOf(cardInfo.getIssueYear()));
				}

				cardPaymentInfoModel.setSubscriptionId(subscription.getSubscriptionID());
				cardPaymentInfoModel.setSaved(saveInAccount);
				if (!StringUtils.isEmpty(cardInfo.getIssueNumber()))
				{
					cardPaymentInfoModel.setIssueNumber(Integer.valueOf(cardInfo.getIssueNumber()));
				}

				billingAddress.setOwner(cardPaymentInfoModel);
				cardPaymentInfoModel.setBillingAddress(billingAddress);

				getModelService().saveAll(billingAddress, cardPaymentInfoModel);

				addPaymentInfo(customerModel, cardPaymentInfoModel);


				return cardPaymentInfoModel;
			}
		}
		catch (final AdapterException ae) //NOSONAR
		{
			LOG.error("Failed to create subscription for customer. Customer PK: " + customerModel.getPk() +
					" Exception: " + ae.getClass().getName());

			return null;
		}

		return null;
	}
	
	/**
	 * Creates an Apple Pay Payment Info based on the customer, cardInfo,
	 * billingInfo, payment provider, savingsAccount and Apple payment token data
	 * 
	 * @param customerModel            the customer
	 * @param cardInfo                 the card information
	 * @param billingInfo              the billing information
	 * @param paymentProvider          the payment provider
	 * @param saveInAccount            the account
	 * @param applePayPaymentTokenData the apple payment token data
	 * @return {@link ApplePayPaymentInfoModel}
	 */
	public ApplePayPaymentInfoModel createApplePayPaymentInfo(CustomerModel customerModel, CardInfo cardInfo, BillingInfo billingInfo, 
			String paymentProvider, boolean saveInAccount, GPApplePayPaymentTokenData applePayPaymentTokenData)
	{
		validateParameterNotNull(customerModel,"Customer cannot be null");
		validateParameterNotNull(cardInfo,"CardInfo cannot be null");
		validateParameterNotNull(billingInfo,"BillingInfo cannot be null");
		validateParameterNotNull(paymentProvider,"Payment Provider cannot be null");
		
		final String email = getCustomerEmailResolutionService().getEmailForCustomer(customerModel);
		
		AddressModel billingAddress = getModelService().create(AddressModel.class);
		
		billingAddress.setFirstname(billingInfo.getFirstName());
		billingAddress.setLastname(billingInfo.getLastName());
		billingAddress.setLine1(billingInfo.getStreet1());
		billingAddress.setLine2(billingInfo.getStreet2());
		billingAddress.setTown(billingInfo.getCity());
		billingAddress.setPostalcode(billingInfo.getPostalCode());
		billingAddress.setCountry(getCommonI18NService().getCountry(billingInfo.getCountry().toUpperCase()));
		if (billingInfo.getRegion()!=null)
		{
			billingAddress.setRegion(getCommonI18NService().getRegion(billingAddress.getCountry(), billingInfo.getRegion()));
		}
		
		billingAddress.setEmail(email);
		
		try {
			
				ApplePayPaymentInfoModel applePayPaymentInfoModel = getModelService().create(ApplePayPaymentInfoModel.class);
				
				applePayPaymentInfoModel.setCode(customerModel.getUid() + "_" + UUID.randomUUID()); 
				applePayPaymentInfoModel.setUser(customerModel);
				applePayPaymentInfoModel.setNumber(applePayPaymentTokenData.getToken().getPaymentMethod().getDisplayName());
				applePayPaymentInfoModel.setApplePayEmail(billingAddress.getEmail());
				applePayPaymentInfoModel.setType(cardInfo.getCardType());
				applePayPaymentInfoModel.setCcOwner(cardInfo.getCardHolderFullName());
				applePayPaymentInfoModel.setSaved(saveInAccount);
				applePayPaymentInfoModel.setPaymentProvider(paymentProvider); 		
				applePayPaymentInfoModel.setBillingAddress(billingAddress);
				
				applePayPaymentInfoModel.setPaymentData(applePayPaymentTokenData.getToken().getPaymentData()); 
				applePayPaymentInfoModel.setTransactionIdentifier(applePayPaymentTokenData.getToken().getTransactionIdentifier());
				
				applePayPaymentInfoModel.setPaymentToken("");
				applePayPaymentInfoModel.setValidToMonth("");
				applePayPaymentInfoModel.setValidToYear("");
				
				billingAddress.setOwner(applePayPaymentInfoModel);
				
				getModelService().saveAll(billingAddress, applePayPaymentInfoModel);
				
				addPaymentInfo(customerModel, applePayPaymentInfoModel);
				
				addPaymentInfoToCart(applePayPaymentInfoModel);
				
				return applePayPaymentInfoModel;
		}
		catch (final AdapterException ae) //NOSONAR
		{
			LOG.error("Failed to create apple pay payment info for customer. Customer PK: " + customerModel.getPk() +
					" Exception: " + ae.getMessage());

			return null;
		}
	}

	/**
	 *  Create new AddressModel and populate values
	 * @param billingInfo
	 * @param titleCode
	 * @return AddressModel
	 */
	private AddressModel populateAddress(final BillingInfo billingInfo,String titleCode){

		final AddressModel billingAddress = getModelService().create(AddressModel.class);

		if (StringUtils.isNotBlank(titleCode))
		{
			final TitleModel title = new TitleModel();
			title.setCode(titleCode);
			billingAddress.setTitle(getFlexibleSearchService().getModelByExample(title));
		}

		billingAddress.setFirstname(billingInfo.getFirstName());
		billingAddress.setLastname(billingInfo.getLastName());
		billingAddress.setLine1(billingInfo.getStreet1());
		billingAddress.setLine2(billingInfo.getStreet2());
		billingAddress.setTown(billingInfo.getCity());
		billingAddress.setPostalcode(billingInfo.getPostalCode());
		billingAddress.setCountry(getCommonI18NService().getCountry(billingInfo.getCountry()));

		if(null!=billingInfo.getRegion()){

			String region=billingInfo.getRegion();

			if(!region.contains("-")){

				region=billingInfo.getCountry()+"-"+billingInfo.getRegion();

			}

			billingAddress.setRegion(getCommonI18NService().getRegion(billingAddress.getCountry(),region));
		}

		billingAddress.setPhone1(billingInfo.getPhoneNumber());

		return billingAddress;
	}

	/**
	 * set the newly created payment info to Cart
	 * @param googlePayPaymentInfoModel
	 */
	private void addPaymentInfoToCart(GooglePayPaymentInfoModel googlePayPaymentInfoModel){

		final CartModel cartModel = cartService.getSessionCart();

		cartModel.setPaymentInfo(googlePayPaymentInfoModel);

		getModelService().save(cartModel);
	}
	
	private void addPaymentInfoToCart(ApplePayPaymentInfoModel applePayPaymentInfoModel) 
	{		
		final CartModel cartModel = cartService.getSessionCart();
		cartModel.setPaymentInfo(applePayPaymentInfoModel);
		getModelService().save(cartModel);		
	}
}

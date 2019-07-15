/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.order.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.gp.commerce.core.customer.impl.GPCustomerAccountService;
import com.gp.commerce.core.model.ApplePayPaymentInfoModel;
import com.gp.commerce.core.model.GooglePayPaymentInfoModel;
import com.gp.commerce.core.model.PaypalPaymentInfoModel;
import com.gp.commerce.core.service.applepay.session.impl.GPApplePaySessionSerivceImpl;
import com.gp.commerce.core.service.auth.impl.GPDefaultPaymentAuthService;
import com.gp.commerce.core.util.GPApplePaySessionResponseDTO;
import com.gp.commerce.data.cart.GPApplePayBillingContactData;
import com.gp.commerce.data.cart.GPApplePayPaymentTokenData;
import com.gp.commerce.dto.payment.AddressWsDTO;
import com.gp.commerce.dto.payment.PaymentDataWsDTO;
import com.gp.commerce.facade.data.PaypalPayerData;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gp.commerce.facades.data.GooglePayRequestData;
import com.gp.commerce.order.data.GPApplePayPaymentInfoData;
import com.gp.commerce.order.data.GooglePayPaymentInfoData;

import de.hybris.platform.commercefacades.i18n.impl.DefaultI18NFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.impl.DefaultCheckoutFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.dto.CardType;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

/**
 * The Class GPCheckoutFacade.
 */
public class GPCheckoutFacade extends DefaultCheckoutFacade {

	private GPCustomerAccountService gpCustomerAccountService;
	private GPDefaultPaymentAuthService paymentAuthService;
	private GPApplePaySessionSerivceImpl gpApplePaySessionSerivce;
	private FlexibleSearchService flexibleSearchService;
	private static final Logger LOG = Logger.getLogger(GPCheckoutFacade.class);

	@Resource
	private ModelService modelService;

	@Resource
	private DefaultI18NFacade defaultI18NFacade;

	@Resource
	private Converter<ApplePayPaymentInfoModel, GPApplePayPaymentInfoData> gpApplePayPaymentInfoConverter;

	@Resource
	private Converter<GooglePayPaymentInfoModel, GooglePayPaymentInfoData> gpGooglePayPaymentInfoConverter;

	@Resource
	private Converter<CartModel, GooglePayRequestData> gpGooglePayPayRequestConverter;

	public GPCustomerAccountService getGpCustomerAccountService() {
		return gpCustomerAccountService;
	}

	public void setGpCustomerAccountService(final GPCustomerAccountService gpCustomerAccountService) {
		this.gpCustomerAccountService = gpCustomerAccountService;
	}

	public GPDefaultPaymentAuthService getPaymentAuthService() {
		return paymentAuthService;
	}

	public void setPaymentAuthService(final GPDefaultPaymentAuthService paymentAuthService) {
		this.paymentAuthService = paymentAuthService;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}

	public GPApplePaySessionSerivceImpl getGpApplePaySessionSerivce() {
		return gpApplePaySessionSerivce;
	}

	public void setGpApplePaySessionSerivce(GPApplePaySessionSerivceImpl gpApplePaySessionSerivce) {
		this.gpApplePaySessionSerivce = gpApplePaySessionSerivce;
	}

	public DefaultI18NFacade getDefaultI18NFacade() {
		return defaultI18NFacade;
	}

	public void setDefaultI18NFacade(DefaultI18NFacade defaultI18NFacade) {
		this.defaultI18NFacade = defaultI18NFacade;
	}

	/**
	 * Method to create googlePay payment info record with response from googlePay
	 * 
	 * @param paymentDataWsDTO - google pay response
	 * @return GooglePayPaymentInfoData - google pay data
	 */
	public GooglePayPaymentInfoData createGooglePayPaymentInfo(final PaymentDataWsDTO paymentDataWsDTO) {

		validateParameterNotNullStandardMessage("paymentInfoData", paymentDataWsDTO);

		final AddressWsDTO billingAddressWsDTO = paymentDataWsDTO.getPaymentMethodData().getInfo().getBillingAddress();

		validateParameterNotNullStandardMessage("billingAddressWsDTO", billingAddressWsDTO);

		if (checkIfCurrentUserIsTheCartUser()) {
			try {

				String merchantDescription = paymentDataWsDTO.getPaymentMethodData().getDescription();
				String googlePayToken = paymentDataWsDTO.getPaymentMethodData().getTokenizationData().getToken();
				String paymentMethodType = paymentDataWsDTO.getPaymentMethodData().getType();
				String paymentTokenizationType = paymentDataWsDTO.getPaymentMethodData().getTokenizationData()
						.getType();

				final CardInfo cardInfo = new CardInfo();
				final CardType cardType = getCommerceCardTypeService().getCardTypeForCode(
						paymentDataWsDTO.getPaymentMethodData().getInfo().getCardNetwork().toLowerCase());

				cardInfo.setCardNumber(paymentDataWsDTO.getPaymentMethodData().getInfo().getCardDetails());
				cardInfo.setCardHolderFullName(billingAddressWsDTO.getName());
				cardInfo.setCardType(cardType == null ? null : cardType.getCode());

				final BillingInfo billingInfo = new BillingInfo();

				billingInfo.setEmail(paymentDataWsDTO.getEmail());
				billingInfo.setCity(billingAddressWsDTO.getLocality());
				billingInfo.setCountry(billingAddressWsDTO.getCountryCode());
				billingInfo.setRegion(billingAddressWsDTO.getAdministrativeArea());
				billingInfo.setFirstName(billingAddressWsDTO.getName());
				billingInfo.setLastName(billingAddressWsDTO.getName());
				billingInfo.setPhoneNumber(billingAddressWsDTO.getPhoneNumber());
				billingInfo.setPostalCode(billingAddressWsDTO.getPostalCode());
				billingInfo.setStreet1(billingAddressWsDTO.getAddress1());
				billingInfo.setStreet2(billingAddressWsDTO.getAddress2());

				final GooglePayPaymentInfoModel googlePayPaymentInfoModel = getGpCustomerAccountService()
						.createGooglePayPaymentInfo(getCurrentUserForCheckout(), cardInfo, billingInfo,
								GpcommerceFacadesConstants.GOOGLE_PAY_PAYMENT_PROVIDER, false, googlePayToken,
								merchantDescription, paymentMethodType, paymentTokenizationType);

				return googlePayPaymentInfoModel == null ? null
						: gpGooglePayPaymentInfoConverter.convert(googlePayPaymentInfoModel);

			} catch (Exception e) {

				LOG.error("Failed to create google pay payment info for customer. Customer PK: "
						+ getCurrentUserForCheckout().getPk() + " Exception: " + e.getMessage(),e);

				return null;
			}
		}

		return null;
	}

	@Override
	public boolean authorizePayment(final String securityCode) {

		final CartModel cartModel = getCart();

		final Double amountForCC = cartModel.getTotalPrice();

		PaymentTransactionEntryModel paymentTransactionEntryModel = null;

		final CommerceCheckoutParameter parameter = createCommerceCheckoutParameter(cartModel, true);

		if (null == amountForCC || amountForCC > 0) {

			if (cartModel.getPaymentInfo() != null && cartModel.getPaymentInfo() instanceof GooglePayPaymentInfoModel) {

				try {

					paymentTransactionEntryModel = getPaymentAuthService()
							.authorizeCybersource(gpGooglePayPaymentInfoConverter
									.convert((GooglePayPaymentInfoModel) cartModel.getPaymentInfo()), parameter);

				} catch (Exception e) {
					
					LOG.error("On exception setting paymentTransactionModel to null"+e.getMessage(), e);
					paymentTransactionEntryModel = null;
				}
			} else if (cartModel.getPaymentInfo() != null
					&& cartModel.getPaymentInfo() instanceof ApplePayPaymentInfoModel) {
				try {

					paymentTransactionEntryModel = getPaymentAuthService()
							.authorizeCybersource(gpApplePayPaymentInfoConverter
									.convert((ApplePayPaymentInfoModel) cartModel.getPaymentInfo()), parameter);

				} catch (Exception e) {
					
					LOG.error("On exception setting paymentTransactionModel to null"+e.getMessage(), e);
					paymentTransactionEntryModel = null;
				}
			} else if (cartModel.getPaymentInfo() != null
					&& cartModel.getPaymentInfo() instanceof CreditCardPaymentInfoModel) {

				if (checkIfCurrentUserIsTheCartUser()) {

					parameter.setSecurityCode(securityCode);

					paymentTransactionEntryModel = getPaymentAuthService().getCCPaymentAuthentication(parameter);

				}
			} else if (cartModel.getPaymentInfo() instanceof PaypalPaymentInfoModel
					&& checkIfCurrentUserIsTheCartUser()) {

				paymentTransactionEntryModel = getPaymentAuthService().executeAuthorizePayment(parameter);
			}

		} else {
			return true;
		}

		if (paymentTransactionEntryModel == null) {

			// Remove google pay payment info/Apple pay payment info
			removePaymentInfoFromCart();

			return false;
		}

		return paymentTransactionEntryModel != null
				&& (TransactionStatus.ACCEPTED.name().equals(paymentTransactionEntryModel.getTransactionStatus()));
	}

	/**
	 * Build the GooglePayRequestData object
	 * 
	 * @return GooglePayRequestData
	 */
	public GooglePayRequestData buildGooglePayRequest() {

		return gpGooglePayPayRequestConverter.convert(getCart());
	}

	@Override
	public CCPaymentInfoData createPaymentSubscription(final CCPaymentInfoData paymentInfoData) {
		validateParameterNotNullStandardMessage("paymentInfoData", paymentInfoData);
		final AddressData billingAddressData = paymentInfoData.getBillingAddress();
		validateParameterNotNullStandardMessage("billingAddress", billingAddressData);

		if (checkIfCurrentUserIsTheCartUser()) {
			final CardInfo cardInfo = new CardInfo();
			cardInfo.setCardHolderFullName(paymentInfoData.getAccountHolderName());
			cardInfo.setCardNumber(paymentInfoData.getCardNumber());
			final CardType cardType = getCommerceCardTypeService()
					.getCardTypeForCode(paymentInfoData.getCardType().toLowerCase());
			cardInfo.setCardType(cardType == null ? null : cardType.getCode());
			cardInfo.setExpirationMonth(Integer.valueOf(paymentInfoData.getExpiryMonth()));
			cardInfo.setExpirationYear(Integer.valueOf(paymentInfoData.getExpiryYear()));
			cardInfo.setIssueNumber(paymentInfoData.getIssueNumber());

			final BillingInfo billingInfo = new BillingInfo();
			billingInfo.setCity(billingAddressData.getTown());
			billingInfo.setCountry(
					billingAddressData.getCountry() == null ? null : billingAddressData.getCountry().getIsocode());
			billingInfo.setRegion(
					billingAddressData.getRegion() == null ? null : billingAddressData.getRegion().getIsocode());
			billingInfo.setFirstName(billingAddressData.getFirstName());
			billingInfo.setLastName(billingAddressData.getLastName());
			billingInfo.setEmail(billingAddressData.getEmail());
			billingInfo.setPhoneNumber(billingAddressData.getPhone());
			billingInfo.setPostalCode(billingAddressData.getPostalCode());
			billingInfo.setStreet1(billingAddressData.getLine1());
			billingInfo.setStreet2(billingAddressData.getLine2());

			final CreditCardPaymentInfoModel ccPaymentInfoModel = ((GPCustomerAccountService) getGpCustomerAccountService())
					.createPaymentSubscription(getCurrentUserForCheckout(), cardInfo, billingInfo,
							billingAddressData.getTitleCode(), getPaymentProvider(), paymentInfoData.isSaved(),
							paymentInfoData.getPaymentToken());
			return ccPaymentInfoModel == null ? null : getCreditCardPaymentInfoConverter().convert(ccPaymentInfoModel);
		}
		return null;
	}

	/**
	 * Creates the apple pay payment info.
	 *
	 * @param applePayPaymentTokenData the apple pay payment token data
	 * @return the GP apple pay payment info data
	 */
	public GPApplePayPaymentInfoData createApplePayPaymentInfo(GPApplePayPaymentTokenData applePayPaymentTokenData) {
		GPApplePayBillingContactData billingContactData = applePayPaymentTokenData.getBillingContact();
		if (checkIfCurrentUserIsTheCartUser()) {
			try {
				final CardInfo cardInfo = new CardInfo();
				cardInfo.setCardNumber(applePayPaymentTokenData.getToken().getPaymentMethod().getDisplayName());
				cardInfo.setCardHolderFullName(applePayPaymentTokenData.getBillingContact().getGivenName() + " "
						+ applePayPaymentTokenData.getBillingContact().getFamilyName());

				String cardNetwork = applePayPaymentTokenData.getToken().getPaymentMethod().getNetwork() == null ? null
						: applePayPaymentTokenData.getToken().getPaymentMethod().getNetwork().toLowerCase();

				CardType cardType = getCommerceCardTypeService()
						.getCardTypeForCode(getCreditCardType(cardNetwork, cardInfo).getCode());
				cardInfo.setCardType(cardType == null ? null : cardType.getCode());

				BillingInfo billingInfo = new BillingInfo();

				billingInfo = getAddressLines(billingInfo, billingContactData.getAddressLines());
				billingInfo.setRegion(billingContactData.getAdministrativeArea() == null ? null
						: getRegionIsoCode(billingContactData.getCountryCode().toUpperCase(),
								billingContactData.getAdministrativeArea()));
				billingInfo.setCountry(
						billingContactData.getCountryCode() == null ? null : billingContactData.getCountryCode());
				billingInfo.setLastName(billingContactData.getFamilyName());
				billingInfo.setFirstName(billingContactData.getGivenName());
				billingInfo.setCity(billingContactData.getLocality());
				billingInfo.setPostalCode(billingContactData.getPostalCode());

				ApplePayPaymentInfoModel applePayPaymentInfoModel = getGpCustomerAccountService()
						.createApplePayPaymentInfo(getCurrentUserForCheckout(), cardInfo, billingInfo,
								GpcommerceFacadesConstants.APPLE_PAY_PAYMENT_PROVIDER, false, applePayPaymentTokenData);

				return applePayPaymentInfoModel == null ? null
						: gpApplePayPaymentInfoConverter.convert(applePayPaymentInfoModel);
			} catch (Exception e) {

				LOG.error("Failed to create apple pay payment info for customer. Customer PK: "
						+ getCurrentUserForCheckout().getPk() + " Exception: " + e.getMessage(),e);

				return null;
			}
		}

		return null;
	}

	/**
	 * Validate session details.
	 *
	 * @param validationURL the validation URL
	 * @return the GP apple pay session response DTO
	 */
	public GPApplePaySessionResponseDTO validateSessionDetails(String validationURL) {
		final CartModel cartModel = getCart();
		return gpApplePaySessionSerivce.validateSessionService(validationURL, cartModel.getSite().getUid());
	}

	/**
	 * Authorize pay pal payment.
	 *
	 * @param paypalType the paypal type
	 * @param url the url
	 * @return the string
	 */
	public String authorizePayPalPayment(String paypalType, String url) {
		final CartModel cartModel = getCart();
		return getPaymentAuthService().authorizePaypalTransaction(cartModel, paypalType, url);
	}

	/**
	 * Save pay pal details.
	 *
	 * @param paypalPayerData the paypal payer data
	 * @return true, if successful
	 */
	public boolean savePayPalDetails(PaypalPayerData paypalPayerData) {
		CartModel cartModel = getCart();
		if (paypalPayerData.getPayerID() != null && !paypalPayerData.getPayerID().isEmpty()) {
			return getPaymentAuthService().savePayPalDetails(cartModel, paypalPayerData);
		}
		return false;
	}

	/**
	 * Remove payment info from cart
	 */
	public void removePaymentInfoFromCart() {
		CartModel cartModel = getCart();

		if (cartModel.getPaymentInfo() != null && cartModel.getPaymentInfo() instanceof GooglePayPaymentInfoModel) {
			modelService.remove(cartModel.getPaymentInfo());

			LOG.error("Removed Payment info from Cart " + cartModel.getCode() + " Since Cybersource Auth Failed ");
		}
	}

	private BillingInfo getAddressLines(BillingInfo billingInfo, List<String> addressLines) {
		if (!addressLines.isEmpty()) {
			if (addressLines.size() > 1) {
				billingInfo.setStreet1(addressLines.get(0));
				billingInfo.setStreet2(addressLines.get(1));
			} else {
				billingInfo.setStreet1(addressLines.get(0));
			}
		}

		return billingInfo;
	}

	/**
	 * Gets the credit card type.
	 *
	 * @param cardType the card type
	 * @param cardInfo the card info
	 * @return the credit card type
	 */
	public CreditCardType getCreditCardType(String cardType, CardInfo cardInfo) {
		switch (cardType) {
		case GpcommerceFacadesConstants.CARD_TYPE_VISA:
			cardInfo.setCardType(CreditCardType.VISA);
			break;
		case GpcommerceFacadesConstants.CARD_TYPE_MASTERCARD:
			cardInfo.setCardType(CreditCardType.MASTER);
			break;
		case GpcommerceFacadesConstants.CARD_TYPE_AMEX:
			cardInfo.setCardType(CreditCardType.AMEX);
			break;
		case GpcommerceFacadesConstants.CARD_TYPE_DISCOVER:
			cardInfo.setCardType(CreditCardType.DISCOVER);
			break;

		}

		return cardInfo.getCardType();
	}

	/**
	 * Gets the region iso code.
	 *
	 * @param countryCode the country code
	 * @param administrativeArea the administrative area
	 * @return the region iso code
	 */
	public String getRegionIsoCode(String countryCode, String administrativeArea) {
		String regionIsoCode = null;
		RegionData regionData = null;

		regionData = defaultI18NFacade.getRegion(countryCode, countryCode + "-" + administrativeArea);

		if (regionData != null) {
			regionIsoCode = regionData.getIsocode();
		} else {
			List<RegionData> regionDatas = defaultI18NFacade.getRegionsForCountryIso(countryCode);

			for (RegionData regionData1 : regionDatas) {
				if (administrativeArea.equalsIgnoreCase(regionData1.getName())) {
					regionIsoCode = regionData1.getIsocode();
					break;
				}
			}
		}

		return regionIsoCode;
	}

}

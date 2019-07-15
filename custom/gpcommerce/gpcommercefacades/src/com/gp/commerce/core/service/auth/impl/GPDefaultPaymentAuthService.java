/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.service.auth.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.cybersource.schemas.transaction_data_1.ReplyMessage;
import com.gp.commerce.core.exceptions.GPException;
import com.gp.commerce.core.model.PaypalPaymentInfoModel;
import com.gp.commerce.core.model.SplitEntryModel;
import com.gp.commerce.core.service.auth.GPPaymentAuthService;
import com.gp.commerce.facade.data.PaypalPayerData;
import com.gp.commerce.order.data.GPApplePayPaymentInfoData;
import com.gp.commerce.order.data.GooglePayPaymentInfoData;
import com.gpintegration.dto.paypal.Amount;
import com.gpintegration.dto.paypal.Details;
import com.gpintegration.dto.paypal.Item_list;
import com.gpintegration.dto.paypal.Items;
import com.gpintegration.dto.paypal.PayPalAuthorizationRequest;
import com.gpintegration.dto.paypal.PayPalAuthorizationResponse;
import com.gpintegration.dto.paypal.PayPalExecuteRequest;
import com.gpintegration.dto.paypal.PayPalExecuteResponse;
import com.gpintegration.dto.paypal.Payer;
import com.gpintegration.dto.paypal.Payment_options;
import com.gpintegration.dto.paypal.Redirect_urls;
import com.gpintegration.dto.paypal.Shipping_address;
import com.gpintegration.dto.paypal.Transactions;
import com.gpintegration.service.GPCybersourceIntegrationService;
import com.gpintegration.service.GPPayPalIntegrationService;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;

/**
 * Default implementation of {@link GPPaymentAuthService}
 */
public class GPDefaultPaymentAuthService implements GPPaymentAuthService {
	private static final Logger LOG = Logger.getLogger(GPDefaultPaymentAuthService.class);
	private static final String DECIMAL_FORMAT = "##.00";
	private static final String ZERO = "0.0";
	private static final String B2B_URL_PAYPAL = "/en/USD/checkout/multi/payment-method/getPayPalPayerInfo";
	private static final String B2C_URL_PAYPAL = "/en/checkout/multi/payment-method/getPayPalPayerInfo";
	private static final String CONTEXT = "/gp/";
	private static final String TRANSACTION_STATUS_SUCCESS = "ACCEPTED";
	private static final String ADDRESS_REGION_CHECK_US = "US";
	private static final String ADDRESS_REGION_CHECK_CA = "CA";
	private static final String PAYPAL = "paypal";
	private GPCybersourceIntegrationService gpCyberSource;
	private GPPayPalIntegrationService gpPayPalService;
	private Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoConverter;
	private ModelService modelService;
	private KeyGenerator guidKeyGenerator;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	/**
	 * Returns Credit Card payment Authentication Details
	 *
	 * @param cartParameter the cart parameter
	 * @return credit card payment authentication
	 */
	public PaymentTransactionEntryModel getCCPaymentAuthentication(CommerceCheckoutParameter cartParameter) {

		if (null != cartParameter.getCart() && null != cartParameter.getCart().getPaymentInfo()) {

			CreditCardPaymentInfoModel creditCardPaymentInfo = (CreditCardPaymentInfoModel) cartParameter.getCart()
					.getPaymentInfo();

			CCPaymentInfoData ccPaymentInfoData = getCreditCardPaymentInfoConverter().convert(creditCardPaymentInfo);

			ccPaymentInfoData.setPaymentToken(creditCardPaymentInfo.getPaymentToken());

			return authorizeCybersource(ccPaymentInfoData,cartParameter);
		}
		return null;
	}

	/**
	 * Creates payment transaction entry
	 *
	 * @param replyMessage the reply message for transaction entry
	 * @return payment transaction entry
	 */
	private PaymentTransactionEntryModel createPaymentTransactionEntry(ReplyMessage replyMessage) {
		PaymentTransactionEntryModel transactionEntry = getModelService().create(PaymentTransactionEntryModel.class);
		transactionEntry.setAmount(new BigDecimal(replyMessage.getCcAuthReply().getAmount()));
		transactionEntry.setCode(replyMessage.getCcAuthReply().getAuthorizationCode());
		transactionEntry.setRequestId(replyMessage.getRequestID());
		transactionEntry.setRequestToken(replyMessage.getRequestToken());
		transactionEntry.setSubscriptionID(replyMessage.getMerchantReferenceCode());
		return transactionEntry;
	}

	@Override
	public String authorizePaypalTransaction(CartModel cart, String paypalType, String url) {
		LOG.info("Authorizing PayPal Payment");
		PayPalAuthorizationRequest request = new PayPalAuthorizationRequest();
		Payer payer = new Payer();
		request.setIntent("authorize");
		payer.setPayment_method("paypal");
		request.setPayer(payer);

		DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);

		// Payer
		if (null == cart.getDeliveryAddress()) {
			LOG.error("Please refersh the Cart Page, Cart Data is empty ");
			return null;
		}

		Shipping_address shippingAddress = populateShippingAddress(cart.getDeliveryAddress());

		// Amount
		Amount amount = mapAmountDetails(cart, decimalFormat);

		// Item List
		Item_list itemList = mapItemDetails(cart);
		List<Transactions> transactions = mapTransactionsAndPaymentOptions(cart, amount, itemList);
		request.setTransactions(transactions.toArray(new Transactions[transactions.size()]));
		request.setNote_to_payer("This is a GP transaction");

		// Redirect-Urls
		Redirect_urls redirectUrls = new Redirect_urls();
		if (configurationService.getConfiguration().getString("gp.b2b.unit.site").contains(cart.getSite().getUid())) {
			redirectUrls.setReturn_url(
					url + CONTEXT + cart.getSite().getUid() + B2B_URL_PAYPAL + "?cartId=" + cart.getCode());
			redirectUrls.setCancel_url(
					url + CONTEXT + cart.getSite().getUid() + B2B_URL_PAYPAL + "?cartId=" + cart.getCode());
		} else {
			redirectUrls.setReturn_url(url + B2C_URL_PAYPAL + "?cartId=" + cart.getCode());
			redirectUrls.setCancel_url(url + B2C_URL_PAYPAL + "?cartId=" + cart.getCode());
		}
		request.setRedirect_urls(redirectUrls);

		PayPalAuthorizationResponse response = null;
		try {
			response = gpPayPalService.authorize(request);
		} catch (Exception e) {
			LOG.error("Error occured in paypal service ", e);
			return null;
		}

		LOG.info("Forming Authorization request");
		if (response != null) {
			if ("created".equals(response.getState())) {
				return response.getId();
			}
		}
		LOG.info("Authorization failed");
		return null;
	}

	private List<Transactions> mapTransactionsAndPaymentOptions(CartModel cart, Amount amount, Item_list itemList) {
		Transactions transaction = new Transactions();
		transaction.setAmount(amount);
		transaction.setItem_list(itemList);
		transaction.setDescription("Sample");
		transaction.setInvoice_number(cart.getCode() + new Date().getTime());
		transaction.setSoft_descriptor(cart.getCode());
		transaction.setCustom("");

		// PaymentOptions
		Payment_options paymentOptions = new Payment_options();
		paymentOptions.setAllowed_payment_method("INSTANT_FUNDING_SOURCE");
		paymentOptions.setRecurring_flag(false);
		paymentOptions.setSkip_fmf(false);

		transaction.setPayment_options(paymentOptions);
		List<Transactions> transactions = new ArrayList<Transactions>();
		transactions.add(transaction);
		return transactions;
	}

	private Item_list mapItemDetails(CartModel cart) {
		Item_list itemList = new Item_list();
		List<Items> items = new ArrayList<Items>();
		for (AbstractOrderEntryModel orderEntry : cart.getEntries()) {
			Items item = new Items();
			item.setName(orderEntry.getProduct().getName());
			item.setCurrency(cart.getCurrency().getIsocode());
			item.setDescription(orderEntry.getProduct().getDescription());
			item.setPrice(new BigDecimal(orderEntry.getTotalPrice().toString())
					.divide(new BigDecimal(orderEntry.getQuantity().toString())).toString());
			item.setQuantity(orderEntry.getQuantity().toString());
			item.setSku(orderEntry.getProduct().getCode());
			double tax = 0.0d;
			for (SplitEntryModel splitEntry : orderEntry.getSplitEntry()) {
				if (null != splitEntry.getTotalTax())
					tax = tax + splitEntry.getTotalTax();
			}
			DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
			item.setTax(decimalFormat.format(tax));
			items.add(item);
		}
		itemList.setItems(items.toArray(new Items[items.size()]));
		return itemList;
	}

	private Amount mapAmountDetails(CartModel cart, DecimalFormat decimalFormat) {
		Amount amount = new Amount();
		amount.setCurrency("USD");
		amount.setTotal(decimalFormat.format(cart.getTotalPrice() + cart.getTotalTax()));
		Details details = new Details();
		details.setSubtotal(
				decimalFormat.format(cart.getTotalPrice() - cart.getDeliveryCost() + cart.getTotalDiscounts()));
		details.setTax(cart.getTotalTax().toString());
		details.setShipping(decimalFormat.format(cart.getDeliveryCost()));
		details.setInsurance(ZERO);
		details.setShipping_discount(decimalFormat.format(cart.getTotalDiscounts()));
		details.setHandling_fee(ZERO);
		amount.setDetails(details);
		return amount;
	}

	@Override
	public boolean savePayPalDetails(CartModel cart, PaypalPayerData paypalPayerInfo) {
		return updatePayPalTransactionEntry(cart, paypalPayerInfo);
	}

	// Create PayPalPaymentInfo after getting the response from authorization.
	private boolean updatePayPalTransactionEntry(CartModel cart, PaypalPayerData paypalPayerInfo) {
		try {
			PaypalPaymentInfoModel paypalInfos = getModelService().create(PaypalPaymentInfoModel.class);
			paypalInfos.setCode(getGuidKeyGenerator().generate().toString());
			paypalInfos.setUser(cart.getUser());
			if (paypalPayerInfo.getPayerID() != null && !paypalPayerInfo.getPayerID().isEmpty()) {
				paypalInfos.setAccountType(PAYPAL);
				paypalInfos.setNonce(paypalPayerInfo.getPaymentID());
				paypalInfos.setTransactionId(paypalPayerInfo.getPaymentToken());
				paypalInfos.setPayerId(paypalPayerInfo.getPayerID());
				getModelService().save(paypalInfos);
				cart.setPaymentInfo(paypalInfos);
			} else {
			LOG.error("error with payer info, no payer info in response");
			return false;
			}
			getModelService().save(cart);
			return true;
		}catch (Exception e) {
			LOG.error("error while savings paypal details: " + e.getMessage(),e);
		}
		return false;
	}
	private Shipping_address populateShippingAddress(AddressModel address) {

		Shipping_address shippingAddress = new Shipping_address();
		String country = address.getCountry().getIsocode();
		String state = address.getRegion().getIsocodeShort();
		shippingAddress.setRecipient_name(address.getFirstname() + " " + address.getLastname());
		shippingAddress.setLine1(address.getLine1());
		shippingAddress.setLine2(address.getLine2());
		shippingAddress.setPostal_code(address.getPostalcode());
		shippingAddress.setCity(address.getTown());
		if (null != country && !ADDRESS_REGION_CHECK_US.equals(country) && !ADDRESS_REGION_CHECK_CA.equals(country)) {
			state = "";
		}
		shippingAddress.setState(state);
		shippingAddress.setCountry_code(country);
		shippingAddress.setPhone("1234567890");

		return shippingAddress;
	}


	public GPPayPalIntegrationService getGpPayPalService() {
		return gpPayPalService;
	}

	public void setGpPayPalService(GPPayPalIntegrationService gpPayPalService) {
		this.gpPayPalService = gpPayPalService;
	}

	public GPCybersourceIntegrationService getGpCyberSource() {
		return gpCyberSource;
	}

	public void setGpCyberSource(GPCybersourceIntegrationService gpCyberSource) {
		this.gpCyberSource = gpCyberSource;
	}

	public Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> getCreditCardPaymentInfoConverter() {
		return creditCardPaymentInfoConverter;
	}

	public void setCreditCardPaymentInfoConverter(
			Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoConverter) {
		this.creditCardPaymentInfoConverter = creditCardPaymentInfoConverter;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	@Override
	public String getPaypalToken(BaseSiteModel baseSite) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean voidPaypalTransaction(CommerceCheckoutParameter cartParameter) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean settlePaypalTransaction(CommerceCheckoutParameter cartParameter, OrderModel orderModel) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PaymentTransactionEntryModel executeAuthorizePayment(CommerceCheckoutParameter cartParameter) {
		if (null != cartParameter.getCart() && null != cartParameter.getCart().getPaymentInfo()) {

			PaypalPaymentInfoModel paypal = (PaypalPaymentInfoModel) cartParameter.getCart().getPaymentInfo();

			PayPalExecuteRequest payPalRequest = new PayPalExecuteRequest();
			payPalRequest.setPayer_id(paypal.getPayerId());
			PayPalExecuteResponse response = null;
			PaymentTransactionModel paymentTransaction = modelService.create(PaymentTransactionModel.class);
			PaymentTransactionEntryModel paymentTransactionEntry = modelService
					.create(PaymentTransactionEntryModel.class);
			CartModel cart = cartParameter.getCart();
			paymentTransactionEntry.setType(PaymentTransactionType.AUTHORIZATION);

			try {
				response = gpPayPalService.execute(payPalRequest, paypal.getNonce());
				if (response!=null && "approved".equals(response.getState())) {
					paypal.setFirstName(response.getPayer().getPayer_info().getFirst_name());
					paypal.setLastName(response.getPayer().getPayer_info().getLast_name());
					paypal.setEmailId(response.getPayer().getPayer_info().getEmail());
					paymentTransactionEntry.setTransactionStatus(TRANSACTION_STATUS_SUCCESS);
					paymentTransactionEntry.setRequestId(
							response.getTransactions()[0].getRelated_resources()[0].getAuthorization().getId());
					paymentTransactionEntry.setCode(response.getTransactions()[0].getInvoice_number());
					paypal.setNonce(response.getId());
					getModelService().save(paypal);
				} else {
					paymentTransactionEntry.setTransactionStatus("VALIDATION_ERROR");
					paymentTransactionEntry.setCode("PAYPAL_AUTH_ERROR");
				}

				paymentTransaction.setEntries(Arrays.asList(paymentTransactionEntry));
				paymentTransaction.setInfo(paypal);
				paymentTransaction.setPaymentProvider(PAYPAL);
				paymentTransaction.setOrder(cart);
				getModelService().saveAll(paymentTransactionEntry, paymentTransaction, cart);
				return paymentTransactionEntry;

			} catch (Exception e) {
				LOG.error("Exception while executing the payment through paypal: " + e.getMessage(),e);
				return null;
			}
		}
		return null;

	}

	protected KeyGenerator getGuidKeyGenerator() {
		return guidKeyGenerator;
	}

	@Required
	public void setGuidKeyGenerator(final KeyGenerator guidKeyGenerator) {
		this.guidKeyGenerator = guidKeyGenerator;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}


	/**
	 * Returns a Payment Transaction record by processing the given
	 * {@link CCPaymentInfoData} and cart parameter
	 * 
	 * @param ccPaymentInfoData the {@link CCPaymentInfoData}
	 * @param cartParameter     the {@link CommerceCheckoutParameter}
	 * @return PaymentTransactionEntryModel if Cybersource auth is success else null
	 */
	public PaymentTransactionEntryModel authorizeCybersource(CCPaymentInfoData ccPaymentInfoData ,CommerceCheckoutParameter cartParameter){

		ReplyMessage replyMessage = null;

		boolean isCrediCard=false;

		boolean isGooglePay=false;
		
		boolean isApplePay=false;

		try {

			if(ccPaymentInfoData instanceof GooglePayPaymentInfoData){

				isGooglePay=true;

			}else if (ccPaymentInfoData instanceof GPApplePayPaymentInfoData) {

				isApplePay = true;

			}else if(ccPaymentInfoData instanceof CCPaymentInfoData) {

				isCrediCard=true;
			}

			replyMessage = gpCyberSource.authorizeUsingCybersource(ccPaymentInfoData, cartParameter,
					cartParameter.getCart().getSite().getUid(),isCrediCard,isGooglePay,isApplePay);

			if(LOG.isDebugEnabled()){

				LOG.debug("Cybersource reply message starts :: ");
				LOG.debug("Request ID :"+replyMessage.getRequestID());
				LOG.debug("Reason code"+replyMessage.getReasonCode());
				LOG.debug("Decision"+replyMessage.getDecision());
				LOG.debug("Missing fields "+replyMessage.getMissingField().stream().collect(Collectors.joining(",")));
				LOG.debug("Cybersource reply message ends");
			}

			if ("ACCEPT".equalsIgnoreCase(replyMessage.getDecision())) {

				PaymentTransactionModel ccPaymentTransaction = getModelService()
						.create(PaymentTransactionModel.class);
				PaymentTransactionEntryModel paymentTransactionEntry = createPaymentTransactionEntry(replyMessage);
				paymentTransactionEntry.setTransactionStatus(TRANSACTION_STATUS_SUCCESS);
				paymentTransactionEntry.setType(PaymentTransactionType.AUTHORIZATION);
				ccPaymentTransaction.setEntries(Arrays.asList(paymentTransactionEntry));
				final PaymentInfoModel paymentInfo = cartParameter.getCart().getPaymentInfo();
				ccPaymentTransaction.setOrder(cartParameter.getCart());
				ccPaymentTransaction.setInfo(paymentInfo);
				getModelService().saveAll(cartParameter.getCart(), ccPaymentTransaction);
				return paymentTransactionEntry;

			} else {

				LOG.error("Payment Auth Failed for cart " + cartParameter.getCart().getCode() + " +decision " + replyMessage.getDecision() + " with reason code " + replyMessage.getReasonCode()+" for cybersource request ID "+replyMessage.getRequestID());
			}

		}catch (Exception e){

			if (null != replyMessage) {

				LOG.error("Payment Auth failed error code :)" + replyMessage.getReasonCode().toString(),e);

				throw new GPException(replyMessage.getReasonCode().toString(), "Payment Auth failed");
			}

			LOG.error("Payment Auth Failed And cybersource reply message is null "+e.getMessage());
		}

		return null;
	}

	@Override
	public PaymentTransactionEntryModel getApplePayPaymentAuthentication(
			GPApplePayPaymentInfoData gpApplePayPaymentInfoData, CommerceCheckoutParameter cartParameter) {
		
		if (null != cartParameter.getCart() && null != cartParameter.getCart().getPaymentInfo()) {

			return authorizeCybersource(gpApplePayPaymentInfoData, cartParameter);
		}
		return null;
	}

}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;

import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.service.GPCyberSourceSignatureService;
import com.gpintegration.utils.GPCybersourceMerchantAliasManager;
import com.gpintegration.utils.GPSignaturePojo;
import com.gpintegration.utils.GPSignatureResponseDTO;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.Config;

/**
 * The Class DefaultGPCyberSourceSignatureService.
 */
public class DefaultGPCyberSourceSignatureService implements GPCyberSourceSignatureService{

	private static final String COMMA = ",";
	private static final String UTF_8 = "UTF-8";
	private static final String UTC = "UTC";
	private static final String YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String BILL_TO_ADDRESS_POSTAL_CODE = "bill_to_address_postal_code";
	private static final String BILL_TO_ADDRESS_COUNTRY = "bill_to_address_country";
	private static final String BILL_TO_ADDRESS_STATE = "bill_to_address_state";
	private static final String BILL_TO_ADDRESS_CITY = "bill_to_address_city";
	private static final String BILL_TO_ADDRESS_LINE1 = "bill_to_address_line1";
	private static final String BILL_TO_PHONE = "bill_to_phone";
	private static final String BILL_TO_EMAIL = "bill_to_email";
	private static final String BILL_TO_SURNAME = "bill_to_surname";
	private static final String BILL_TO_FORENAME = "bill_to_forename";
	private static final String PAYMENT_METHOD = "payment_method";
	private static final String CURRENCY = "currency";
	private static final String AMOUNT = "amount";
	private static final String REFERENCE_NUMBER = "reference_number";
	private static final String TRANSACTION_TYPE = "transaction_type";
	private static final String LOCALE = "locale";
	private static final String SIGNED_DATE_TIME = "signed_date_time";
	private static final String UNSIGNED_FIELD_NAMES = "unsigned_field_names";
	private static final String SIGNED_FIELD_NAMES = "signed_field_names";
	private static final String TRANSACTION_UUID = "transaction_uuid";
	private static final String PROFILE_ID = "profile_id";
	private static final String ACCESS_KEY = "access_key";
	private static final String PAYMENT_TOKEN = "payment_token";
	private static final String USD = "USD";
	private static final String EN = "en";
	@Resource
	GPCybersourceMerchantAliasManager cybersourceMerchantAliasManager;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	
	private static final String CYB_SRC_ACCESS_KEY = "cybersource.checkout.profile.access_key.";
	
	private static final String CYB_SRC_PROFILE_ID = "cybersource.checkout.profile.profile_id.";
	
	private static final String CYB_SRC_SECRET_KEY = "cybersource.checkout.profile.secret_key.";
	
	private static final String CYB_SRC_CREATE_TOKEN_URL = "cybersource.signature.create.response.url";
	private static final String CYB_SRC_UPDATE_TOKEN_URL = "cybersource.signature.update.response.url";
	private static final String BASESITE_DELIMITER = "gpcommercewebservices.user.delimiter";
	private static final int REFERENCE_CODE_LENGTH = 50;
	
	
	/**
	 * Gets the the signature.
	 *
	 * @param pojo the pojo
	 * @param baseSiteId the base site id
	 * @return the the signature
	 */
	@Override
	public GPSignatureResponseDTO getTheSignature(GPSignaturePojo pojo, String baseSiteId) {
		pojo.setTransactionUuid(UUID.randomUUID().toString());
		pojo.setReferenceNumber(String.valueOf(System.currentTimeMillis()));
		pojo.setSignedDateTime(getUTCDateTime());
		Map<String, Object> params = getValuesForTheParam(pojo, baseSiteId);

		String signature = null;
		signature = sign(params, getSecretKey(baseSiteId));
		GPSignatureResponseDTO signatureResponseDTO = new GPSignatureResponseDTO();
		signatureResponseDTO.setAccess_key(getAccessKey(baseSiteId));
		signatureResponseDTO.setProfile_id(getProfileId(baseSiteId));
		signatureResponseDTO.setSignature(signature);
		if(null != pojo.getPayment_token())
			signatureResponseDTO.setCyber_source_url(Config.getParameter(CYB_SRC_UPDATE_TOKEN_URL));
		else
			signatureResponseDTO.setCyber_source_url(Config.getParameter(CYB_SRC_CREATE_TOKEN_URL));
		
		String referenceNumber = pojo.getBillToEmail()+configurationService.getConfiguration().getString(BASESITE_DELIMITER)+baseSiteId;
		if(referenceNumber.length()>REFERENCE_CODE_LENGTH) {
			referenceNumber = referenceNumber.substring(0, REFERENCE_CODE_LENGTH);
		}
		signatureResponseDTO.setReference_number(referenceNumber);
		signatureResponseDTO.setTransaction_uuid(pojo.getTransactionUuid());
		signatureResponseDTO.setLocale(EN);
		signatureResponseDTO.setCurrency(USD);
		signatureResponseDTO.setSigned_date_time(pojo.getSignedDateTime());
		
		return signatureResponseDTO;

	}

	/**
	 * This method gets Values for the parameters.
	 *
	 * @param pojo       the pojo
	 * @param baseSiteId the basesite id
	 * @return values for the param
	 */
	public Map<String, Object> getValuesForTheParam(GPSignaturePojo pojo, String baseSiteId) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(null != pojo.getPayment_token())
			params.put(PAYMENT_TOKEN, pojo.getPayment_token());
		params.put(ACCESS_KEY, getAccessKey(baseSiteId));
		params.put(PROFILE_ID, getProfileId(baseSiteId));
		params.put(TRANSACTION_UUID, pojo.getTransactionUuid());
		params.put(SIGNED_FIELD_NAMES, pojo.getSignedFieldNames());
		params.put(UNSIGNED_FIELD_NAMES, pojo.getUnsignedFieldNames());
		params.put(SIGNED_DATE_TIME, pojo.getSignedDateTime());
		params.put(LOCALE, EN);
		params.put(TRANSACTION_TYPE, pojo.getTransactionType());
		String referenceNumber = pojo.getBillToEmail()+configurationService.getConfiguration().getString(BASESITE_DELIMITER)+baseSiteId;
		if(referenceNumber.length()>REFERENCE_CODE_LENGTH) {
			referenceNumber = referenceNumber.substring(0, REFERENCE_CODE_LENGTH);
		}
		params.put(REFERENCE_NUMBER,referenceNumber);
		params.put(AMOUNT, pojo.getAmount());
		params.put(CURRENCY, USD);
		params.put(PAYMENT_METHOD, pojo.getPaymentMethod());
		params.put(BILL_TO_FORENAME, pojo.getBillToForename());
		params.put(BILL_TO_SURNAME, pojo.getBillToSurname());
		params.put(BILL_TO_EMAIL, pojo.getBillToEmail());
		params.put(BILL_TO_PHONE, pojo.getBillToPhone());
		params.put(BILL_TO_ADDRESS_LINE1, pojo.getBillToAddressLine1());
		params.put(BILL_TO_ADDRESS_CITY, pojo.getBillToAddressCity());
		params.put(BILL_TO_ADDRESS_STATE, pojo.getBillToAddressState());
		params.put(BILL_TO_ADDRESS_COUNTRY, pojo.getBillToAddressCountry());
		params.put(BILL_TO_ADDRESS_POSTAL_CODE, pojo.getBillToAddressPostalCode());
			
		return params;
	}
	
	 private String getUTCDateTime() {
	        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_T_HH_MM_SS_Z);
	        sdf.setTimeZone(TimeZone.getTimeZone(UTC));
	        return sdf.format(new Date());
	    }

	/** The log. */
	public final Logger LOG = Logger.getLogger(DefaultGPCyberSourceSignatureService.class);

	public static final String HMAC_SHA256 = "HmacSHA256";

	/**
	 * This method is used to sign using a secret key .
	 *
	 * @param params the params
	 * @param secretKey the secret key
	 * @return sign
	 */
	public String sign(Map<String, Object> params, String secretKey) {
		return sign(buildDataToSign(params), secretKey);
	}

	/**
	 * This method is used to sign.
	 *
	 * @param data the data
	 * @param secretKey the secret key
	 * @return result
	 */
	public String sign(String data, String secretKey) {
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), HMAC_SHA256);

		String result = null;
		byte[] rawHmac;
		Mac mac;
		try {
			mac = Mac.getInstance(HMAC_SHA256);
			mac.init(secretKeySpec);
			rawHmac = mac.doFinal(data.getBytes(UTF_8));
			result = DatatypeConverter.printBase64Binary(rawHmac).replace("\n", "");

		} catch (NoSuchAlgorithmException e1) {
			LOG.error(e1);
		} catch (InvalidKeyException | IllegalStateException | UnsupportedEncodingException e) {
			LOG.error(e);
		}
		return result;
	}

	/**
	 * This method is used to build data to sign.
	 *
	 * @param params the params
	 * @return data to sign
	 */
	public String buildDataToSign(Map<String, Object> params) {
		String[] signedFieldNames = String.valueOf(params.get(SIGNED_FIELD_NAMES)).split(COMMA);
		ArrayList<String> dataToSign = new ArrayList<String>();
		for (String signedFieldName : signedFieldNames) {
			dataToSign.add(signedFieldName + "=" + String.valueOf(params.get(signedFieldName)));
		}
		return commaSeparate(dataToSign);
	}

	/**
	 * This methods separates the data by comma value.
	 *
	 * @param dataToSign the data to sign
	 * @return comma separated value
	 */
	public String commaSeparate(ArrayList<String> dataToSign) {
		StringBuilder csv = new StringBuilder();
		for (Iterator<String> it = dataToSign.iterator(); it.hasNext();) {
			csv.append(it.next());
			if (it.hasNext()) {
				csv.append(COMMA);
			}
		}
		return csv.toString();
	}
	
	/**
	 * Get secret key.
	 *
	 * @param baseSiteId the base site id
	 * @return secret key
	 */
	public String getSecretKey(String baseSiteId) {
			return Config.getParameter(CYB_SRC_SECRET_KEY+baseSiteId);
	}

	/**
	 * Gets access key.
	 *
	 * @param baseSiteId the base site id
	 * @return access key
	 */
	public String getAccessKey(String baseSiteId) {
		return Config.getParameter(CYB_SRC_ACCESS_KEY+baseSiteId);
	}

	/**
	 * Gets profile id.
	 *
	 * @param baseSiteId the base site id
	 * @return profile id
	 */
	public String getProfileId(String baseSiteId) {
		return Config.getParameter(CYB_SRC_PROFILE_ID+baseSiteId);
	}
	
	
}

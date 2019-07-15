/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gpintegration.utils;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import de.hybris.platform.servicelayer.config.ConfigurationService;


public class GPCybersourceMerchantAliasManager {

	public static final String PAYER_AUTH_RESPONSE_URL = "cybersourceaddon.payer.auth.response.url";

	public static final String CREATEORDERANDREJECT_CODES = "cybersource.createorderandreject.codes";

	public static final String CREATEORDERANDFLAG_CODES = "cybersource.createorderandflag.codes";

	public static final String CREATEORDER_CODES = "cybersource.createorder.codes";

	public static final String PAYER_AUTH_CREDIT_CARD_TYPE = "cybersourceaddon.payer.auth.credit.card.type";

	public static final String IGNORE_CVS_RESULT = "business.rules.ignore.cvs.result";

	public static final String IGNORE_AVS_RESULT = "business.rules.ignore.avs.result";

	public static final String ENABLE_PAYER_AUTH = "cybersource.enable.payer.auth";

	public static final String SHOW_CVV_FIELD = "cybersource.show.cvv.field";

	public static final String CHECKOUT_DEVICE_ORGID = "cybersource.checkout.device.orgid";

	public static final String CHECKOUT_PROFILE_PROFILE_ID = "cybersource.checkout.profile.profile_id";
	
	public static final String CHECKOUT_PROFILE_ACCESS_KEY = "cybersource.checkout.profile.access_key";
	
	public static final String CHECKOUT_PROFILE_SECRET_KEY = "cybersource.checkout.profile.secret_key";
	
	public static final String TOKEN_PROFILE_PROFILE_ID = "cybersource.token.profile.profile_id";

	public static final String TOKEN_PROFILE_ACCESS_KEY = "cybersource.token.profile.access_key";

	public static final String TOKEN_PROFILE_SECRET_KEY = "cybersource.token.profile.secret_key";

	public static final String ONDEMAND_QUERY_TEST_URL = "cybersource.ondemand.query.test.url";

	public static final String ONDEMAND_QUERY_PRODUCTION_URL = "cybersource.ondemand.query.production.url";

	public static final String ONDEMAND_QUERY_PASSWORD = "cybersource.ondemand.query.password";

	public static final String ONDEMAND_QUERY_USERNAME = "cybersource.ondemand.query.username";

	public static final String ONDEMAND_QUERY_MERCHANT_ID = "cybersource.ondemand.query.merchant_id";

	public static final String MERCHANT_ID = "cybersource.merchant.id";

	private static final Logger LOG = Logger.getLogger(GPCybersourceMerchantAliasManager.class);
	
	public static final String MERCHANT_ALIAS_KEY = "merchant_alias_key";
	
	@Resource
	private ConfigurationService configurationService;
	
	
	private String globalMerchantId;
	

	public Boolean getBoolean(String key) {
		 return getBoolean(key, null); 
	}

	public Boolean getBoolean(String key, String merchantAlias) {
		String result = getString(key, merchantAlias);
		if ("true".equalsIgnoreCase(result)) {
			return Boolean.TRUE;
		}
		else {
			return Boolean.FALSE;
		}
	}

	
	public String getString(String key) {
		if (StringUtils.isNotBlank(globalMerchantId)) {
			return getString(key, globalMerchantId);
		}
		return getString(key, null); 
	}
	
	public String getString(String key, String merchantAlias) {
				
		if (StringUtils.isBlank(key)) {
			LOG.error("Property key [" + key + "] is not defined.");
			return null;
		}
		
		String aliasKey = key;
		if (StringUtils.isNotBlank(merchantAlias)) {
			aliasKey += "." + merchantAlias;
		}
		
		String aliasValue = getConfigString(aliasKey);
		
		if (StringUtils.isBlank(aliasValue)) {
			if (StringUtils.isNotBlank(merchantAlias)) {
				LOG.error("Property key [" + aliasKey + "] for merchant alias [" + merchantAlias + "] does not have a defined value.");
			}
			aliasValue = getConfigString(key);
		}
		
		return aliasValue;
	}
	
	protected String getConfigString(String key) {
		return configurationService.getConfiguration().getString(key, "");
	}

	public String getGlobalMerchantId() {
		return globalMerchantId;
	}

	public void setGlobalMerchantId(String globalMerchantId) {
		this.globalMerchantId = globalMerchantId;
	}
}

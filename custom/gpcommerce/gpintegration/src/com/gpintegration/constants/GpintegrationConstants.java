/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gpintegration.constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gpintegration.exception.GPDataException;

/**
 * Global class for all Gpintegration constants. You can add global constants for your extension into this class.
 */
public final class GpintegrationConstants extends GeneratedGpintegrationConstants
{
	public static final String EXTENSIONNAME = "gpintegration";

	private GpintegrationConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension

    public static final String PLATFORM_LOGO_CODE = "gpintegrationPlatformLogo";
    public static final String EMAIL_ID="email";
    public static final String FIRST_NAME="first_name";
    public static final String LAST_NAME="last_name";
    public static final String ID="id";
    public static final String GENDER="gender";
    public static final String FAMILY_NAME="family_name";
    public static final String GIVEN_NAME="given_name";
    public static final String GOOGLE="google";
    public static final String FACEBOOK="facebook";
    public static final String EXCEPTION="The exception occurred";
    public static final String FACEBOOK_ENDPOINT="facebook.social.login.graph.api.url";
    public static final String GOOGLE_ENDPOINT="google.social.login.userinfo.url";
    public static final String GOOGLE_REDIRECT_URL="google.social.login.redirecturl";
    public static final String GOOGLE_CLIENT_ID="google.social.login.clientid";
    public static final String GOOGLE_CLIENT_SECRET="google.social.login.clientsecret";
    public static final String GOOGLE_AUTH_TOKEN_URL="google.social.login.authtoken.url";
    public static final String SOCIAL_LOGIN_PASSWORD="social.login.password";
    
    
    public static final String FB_EMAIL_ID="fb_email";
    public static final String FB_FIRST_NAME="fb_first_name";
    public static final String FB_LAST_NAME="fb_last_name";
    public static final String FB_ID="fbId";
    public static final String FB_PASSWORD="fbPassword";

    // Koch Auth constants
    public static final String KOCH_ATUH_TS_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String HTTP_STATUS_SUCCESS = "200";
    public static final String HTTP_STATUS_BAD_REQUEST = "400";
    public static final String HTTP_STATUS_UNAUTHORIZED = "401";
    public static final String KOCH_AUTH_CLIENT_ID ="client_id";
    public static final String KOCH_AUTH_CLIENT_SECRET="client_secret";
    public static final String KOCH_AUTH_GRANT_TYPE="grant_type";
    public static final String KOCH_AUTH_REDIRECT_URI="redirect_uri";
    public static final String KOCH_AUTH_GRANT_TYPE_VALUE="authorization_code";
    public static final String KOCH_AUTH_ACCESS_CODE="code";
    public static final String ACCESS_TOKEN="access_token";
    public static final String ID_TOKEN="id_token";
    public static final String REFRESH_TOKEN="refresh_token";
    public static final String USER_PRINCIPAL_NAME = "userPrincipalName";

    //SCPI
     public static final String AUTHORIZATION="Authorization";
     public static final String AUTH_BASIC="BASIC ";
     public static final String BEARER="BEARER  ";
     public static final String COLON=":";
     public static final String SEMI_COLON=";";
     public static final String HYPHEN="-";

     public static final String SUCCESS="Success";
     public static final String FAILURE="Failure";

     public static final String CRM_SHELL_ORDER_REPLICATION_FLAG = "gp.scpi.enable.shell.order.replication";

     //Subscription Constants
     
     public static final String SUBSCRIPTION_REASON_OOS = "gp.subscription.cancel.out.of.stock.reason";
     public static final String SUBSCRIPTION_REASON_DISC = "gp.subscription.cancel.discontinue.reason";
     public static final String SUBSCRIPTION_REASON_MAX_Q = "gp.subscription.cancel.max.edit.reason";
    	 public static final String SUBSCRIPTION_REASON_MIN_Q	 = "gp.subscription.cancel.min.edit.reason";
    	 public static final String SUBSCRIPTION_REASON_SHIP_RESTRICT = "gp.subscription.cancel.shipping.restrict.reason";
    	 public static final String SUBSCRIPTION_REASON_NOT_SUBSCRIBLE = "gp.subscription.cancel.not.subscribable";
    // Constants added for Cybersource
    public static final String CAPTURE_SEQ_DELIMITER = "_";
    public static final String WSS4J_VERSION = "wss4j.version";
	public static final String OS_NAME = "os.name";
	public static final String OS_VERSION = "os.version";
	public static final String JAVA_VENDOR = "java.vendor";
	public static final String JAVA_VERSION = "java.version";
	public static final String MESSAGE_ID_KEY = "messageId";
	public static final String MERCHANT_ID = "cybersource.merchant.id.";
	public static final String MERCHANT_KEY = "cybersource.merchant.soap.secret.key.";
	public static final String SERVER_URL = "cybersource.transaction.server.url";
	public static final String CAPTURE_COUNT_REQUIRED = "cybersource.merchant.capture.count.required";
	public static final String CLIENT_LIB_VERSION = "cybersource.clientlib.version";
	public static final String CLIENT_LIBRARY = "cybersource.clientlib";
	public static final String SERVICE_RUN_TRUE = "true";
	public static final String SERVICE_RUN_FALSE = "false";
	public static final String TRANSACTION_DECRIPTION = "cybersource.transaction.refernce";

	public static final String CYB_FIRST_NAME = "bill_to_forename";

	public static final String CYB_LAST_NAME = "bill_to_surname";

	public static final String CYB_ADDRESS_LINE_1 = "bill_to_address_line1";

	public static final String CYB_ADDRESS_LINE_2 = "bill_to_address_line2";

	public static final String CYB_CITY = "bill_to_address_city";

	public static final String CYB_STATE = "bill_to_address_state";

	public static final String CYB_POSTAL_CODE = "bill_to_address_postal_code";

	public static final String CYB_COUNTRY = "bill_to_address_country";

	public static final String CYB_EMAIL = "bill_to_email";
	public static final String SYMBOL_UNDERSCORE = "_";

	private String globalMerchantId;
	public static final String GP_NETSUITE_SERVICE_CUSTOMER_URI = "gp.netsuite.service.customer.uri";
	public static final String GP_NETSUITE_SERVICE_ORDER_URI = "gp.netsuite.service.order.uri";
	public static final String GP_NETSUITE_CUSTOMER_ORDER_REPLICATION_REMORSE_PERIOD_TIMEZONE = "gp.netsuite.customer.order.replication.remorse.period.timezone";
	public static final String GP_NETSUITE_CUSTOMER_ORDER_REPLICATION_REMORSE_PERIOD = "gp.netsuite.customer.order.replication.remorse.period";
	public static final String GP_NETSUITE_CUSTOMER_ORDER_REPLICATION_RETRY_COUNT = "gp.netsuite.customer.order.replication.retry.count";
	public static final String GP_SITE_B2B_NAME = "gp.site.b2b.name";
	public static final String GP_NETSUITE_CUSTOMER_ORDER_REPLICATION_DEPARTMENT_DIXY_FOOD_SERVICE_DIRECT = "gp.netsuite.customer.order.replication.department.dixy.food.service.direct";
	public static final String GP_NETSUITE_CUSTOMER_ORDER_REPLICATION_DEPARTMENT_GP_STORE = "gp.netsuite.customer.order.replication.department.gp.store";
	public static final String GP_TAX_CALCULATION_SERVICE_ONESOURCE_EXTERNAL_COMPANY_ID = "gp.tax.calculation.service.onesource.external.company.id";
	public static final String GP_TAX_CALCULATION_SERVICE_ONESOURCE_TAX_CALCULATION_DIRECTION = "gp.tax.calculation.service.onesource.tax.calculation.direction";
	public static final String GP_TAX_CALCULATION_SERVICE_ONESOURCE_COMPANY_ROLE = "gp.tax.calculation.service.onesource.company.role";
	public static final String GP_TAX_CALCULATION_SERVICE_ONESOURCE_CURRENCY_CODE = "gp.tax.calculation.service.onesource.currency.code";
	public static final String GP_TAX_CALCULATION_SERVICE_ONESOURCE_IS_AUDITED = "gp.tax.calculation.service.onesource.is.audited";
	public static final String GP_TAX_CALCULATION_SERVICE_ONESOURCE_TRANSACTION_TYPE = "gp.tax.calculation.service.onesource.transaction.type";
	public static final String GP_TAX_CALCULATION_SERVICE_AVALARA_WAREHOUSEADDRESS_LINE1 = "gp.tax.calculation.service.avalara.warehouseaddress.line1";
	public static final String GP_TAX_CALCULATION_SERVICE_AVALARA_SHIPPING_TAXCODE = "gp.tax.calculation.service.avalara.shipping.taxcode";
	public static final String GP_TAX_CALCULATION_SERVICE_AVALARA_TAXCODE = "gp.tax.calculation.service.avalara.taxcode";
	public static final String GP_TAX_CALCULATION_SERVICE_AVALARA_CURRENCY_CODE = "gp.tax.calculation.service.avalara.currency.code";
	public static final String GP_TAX_CALCULATION_SERVICE_AVALARA_COMPANY_CODE = "gp.tax.calculation.service.avalara.company.code";
	public static final String GP_TAX_CALCULATION_SERVICE_AVALARA_COMMIT = "gp.tax.calculation.service.avalara.commit";
	public static final String GP_AVALARA_TAX_CALCULATION_SERVICE_USERNAME = "gp.avalara.tax.calculation.service.username";
	public static final String GP_AVALARA_TAX_CALCULATION_SERVICE_PASSWORD = "gp.avalara.tax.calculation.service.password";
	public static final String GP_AVALARA_TAX_CALCULATION_SERVICE_URL = "gp.avalara.tax.calculation.service.url";
	
	 //001 is default value for apple pay
	public static final String APPLE_PAY_PAYMENT_SOLUTION = "001";
	public static final String APPLE_PAY_ENCRYPTED_PAYMENT_DESC = "RklEPUNPTU1PTi5BUFBMRS5JTkFQUC5QQVlNRU5U";
	public static final String CYBERSOURCE_ENCODING = "Base64";
	
	public static final String CREDIT_CARD_TYPE_VISA = "visa";
	public static final String CREDIT_CARD_TYPE_MASTERCARD = "mastercard";
	public static final String CREDIT_CARD_TYPE_AMEX = "americanexpress";
	public static final String CREDIT_CARD_TYPE_DISCOVER = "discover";
	
	public static final String CREDIT_CARD_TYPE_VISA_VALUE = "001";
	public static final String CREDIT_CARD_TYPE_MASTERCARD_VALUE = "002";
	public static final String CREDIT_CARD_TYPE_AMEX_VALUE = "003";
	public static final String CREDIT_CARD_TYPE_DISCOVER_VALUE = "004";

	public String getGlobalMerchantId() {
		return globalMerchantId;
	}

	public void setGlobalMerchantId(final String globalMerchantId) {
		this.globalMerchantId = globalMerchantId;
	}

    /*SCPI constants*/
    /*SCPI customer constants*/

    public static final String SCPI_ENDPOINT = "gp.scpi.end.point.url";
    public static final String SCPI_OAUTH_ENDPOINT = "gp.scpi.oauth.end.point.url";
    public static final String SCPI_USERNAME = "gp.scpi.username";
    public static final String SCPI_PASSWORD = "gp.scpi.password";
    public static final String SCPI_CUSTOMER_RETRY_MAX_COUNT = "gp.scpi.max.retry.count";
    public static final String GUEST_USER = "guest";
    public static final String SITE_DELIMITER = "\\|";
    public static final String PIPE_SYMBOL = "|";
    public static final String SPACE = " ";


    public static final String SERVICE_UNAVAILABLE_ERROR_CODE = "502";
    public static final String SCPI_FEATURE = "gp.scpi.enable.add.customer";
    public static final String B2B_UNIT = "gp.scpi.customer.b2b.unit";

    //yMarketing
    public static final String YMARKETING_AUTH_ENDPOINT = "ymarketing.authentication.endpoint";
    public static final String YMARKETING_CREATE_UPDATE_ENDPOINT = "ymarketing.create.update.endpoint";
    public static final String YMARKETING_AUTH_USERNAME ="ymarketing.endpoint.username";
    public static final String YMARKETING_AUTH_PASSWORD="ymarketing.endpoint.password";
    public static final String YMARKETING_REQUEST_USERNAME="ymarketing.request.username";
    public static final String YMARKETING_SOURCE_SYSTEM_TYPE="ymarketing.source.system.type";
    public static final String YMARKETING_SOURCE_SYSTEM_ID="ymarketing.source.system.id";
	public static final String MARKETING_PERMISSIONS_ID_ORIGIN_CREATE = "ymarketing.permissions.id.origin.create";
	public static final String MARKETING_PERMISSIONS_ID_ORIGIN_UPDATE = "ymarketing.permissions.id.origin.update";
    public static final String OUTBOUND_COMMUNICATION_MEDIUM="ymarketing.outbound.communication.medium";
    public static final String COMMUNICATION_DIRECTION="ymarketing.communication.direction";
    public static final String COMMUNICATION_MEDIUM="ymarketing.communication.medium";
    public static final String MARKETING_AREA_ID="ymarketing.marketing.area.id";
    public static final String YMARKETING_ENABLE_COUPONS="ymarketing.enable.coupon";
    public static final String SITE_COMMUNICATION_CATEGORY=".communication.category";
    public static final String SITE_WELCOME_SERIES_CATEGORY=".zwelcomeSeries.communication.category";
    public static final String SITE_PAGE_COUPON_CATEGORY=".communication.category";


    public static Date convertStringToDate(final String dateString) {
    	final SimpleDateFormat formatter = new SimpleDateFormat(GpintegrationConstants.KOCH_ATUH_TS_DATE_FORMAT);
    	Date formattedDate = null;
    	try {
    		formattedDate = formatter.parse(dateString);
		} catch (final ParseException parseException) {
			throw new GPDataException("Date parsing failed in convertStringToDate");
		}
    	return formattedDate;
    }
    //constants added for Hybris to Netsuite Customer and Order replication
    public static final String NETSUITE_OAUTH_SIGNATURE_ALGORITHM = "HmacSHA1";
    public static final String NETSUITE_OAUTH_SIGNATURE_METHOD = "HMAC-SHA1";
    public static final String NETSUITE_OAUTH_VERSION = "1.0";
    public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HTTP_MIME_TYPE_APPLICATION_JSON = "application/json";
    public static final String HTTP_HEADER_AUTHORIZATION = "Authorization";
    public static final String HTTP_HEADER_AUTHORIZATION_BEARER = "Bearer";
    public static final String HTTP_METHOD_POST="POST";
    public static final String HTTP_METHOD_PUT="PUT";
    public static final String HTTP_METHOD_GET="GET";
    public static final String NETSUITE_REPLICATION_FEATURE = "gp.netsuite.enable.customer.order.replication";

    public static final String HYBRIS_ORDER_CREATION_DATE_FORMAT = "E MMM dd HH:mm:ss Z yyyy";
	public static final String NETSUITE_ORDER_TRANSACTION_DATE_FORMAT = "MM/dd/yyyy";
	public static final String ONE_SOURCE_TAX_SERVICE_INVOICE_DATE_FROMAT = "yyyy-MM-dd";
	public static final String AVALARA_TAX_SERVICE_INVOICE_DATE_FROMAT = "yyyy-MM-dd'T'hh:mm:ssZ";
	public static final String COMMA_SYMBOL=",";
	public static final String SCPI_PRODUCT_REPLICATION_URL = "gp.scpi.crm.product.replication.url";
	public static final String SCPI_REPLICATION_USER = "gp.scpi.crm.product.replication.user";
	public static final String SCPI_REPLICATION_PASSWORD = "gp.scpi.crm.product.replication.password";
	
	//constants for SFDC Product Replication
	public static final String HAS_SAMPLE = "hassample";
	public static final String IS_SAMPLE = "issample";
	public static final String THUMBNAIL = "thumbnail";
	public static final String CODE = "code";
	public static final String SAMPLE_ORDER_LIMIT = "sampleorderlimit";
	public static final String SAMPLE_UOM_DESCRIPTION = "sampleuomdescription";
	public static final String COUNTRY_ISO_CODE = "countryisocode";
	public static final String DELIVERY_MODE = "deliverymode";
	public static final String REGION = "region";
	public static final String CASE_BUOM = "casebuom";
	public static final String COMPETITOR_NAME = "competitorname";
	public static final String COMPETITOR_NUMBER ="competitornumber";
	public static final String CASE_QTY_BUOM = "caseqtybuom";
	public static final String MATERIAL = "material";
	public static final String COLOR = "color";
	public static final String EXTERNAL_ID = "externalid";
	public static final String BRAND = "brand";
	public static final String COMPETITOR_ID = "competitorid";
	public static final String RELATIONSHIP_TYPE ="relationshiptype";
	public static final String DESCRIPTION = "description";
	public static final String STREET = "street";
	public static final String CITY = "city";
	public static final String STATE = "state";
	public static final String POSTALCODE = "postalcode";
	public static final String COUNTRY = "country";
	
	
	public static final String GP_SFDC_PRODUCT_REPLICATION_RETRY_COUNT = "sfdc.product.replication.max.retry.count";
	public static final String GP_SFDC_PRODUCT_REFERENCE_REPLICATION_RETRY_COUNT = "sfdc.product.reference.replication.max.retry.count";
	public static final String SFDC_PRODUCT_REPLICATION_LAST_RUNTIME_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String HTTP_STATUS_CODE_SUCCESS = "200";
	public static final String SCPI_PRODUCT_REFERENCE_REPLICATION_URL ="gp.scpi.crm.product.reference.replication.url";
	public static final String SCPI_PRODUCT_REFERENCE_REPLICATION_USER = "gp.scpi.crm.product.reference.replication.user";
	public static final String SCPI_PRODUCT_REFERENCE_REPLICATION_PASSWORD = "gp.scpi.crm.product.reference.replication.password";
	
	public static final String PROD_REFERENCE_DELETION_YES = "Y";
	
	//Ticket attachment replication properties
	
	public static final String TICKET_ATTACHMENT_FILE_NAME = "realFileName";
	public static final String TICKET_ATTACHMENT_FILE_MIME = "mime";
	public static final String TICKET_ATTACHMENT_FILE_LOCATION = "location";
	public static final String CUSTOMER_B2B = "B2B";
	public static final String CUSTOMER_B2C = "B2C";
	
    public static final String QUOTE_SYMBOL="\"";
    public static final String CRM_SFDC_TICKET_FLAG = "gp.scpi.enable.crm.ticket";
    public static final String DUMMY_VALUE = "0";
    //012 is default value for google pay
	public static final String GOOGLE_PAY_PAYMENT_SOLUTION = "012";
	public static final String VISA = "visa";
	public static final String MASTER_CARD = "mastercard";
	public static final String AMEX = "amex";
	public static final String DISCOVER = "discover";
	public static final String VISA_CARDTYPE = "001";
	public static final String MASTER_CARD_CARDTYPE = "002";
	public static final String AMEX_CARDTYPE = "003";
	public static final String DISCOVER_CARDTYPE = "004";



}
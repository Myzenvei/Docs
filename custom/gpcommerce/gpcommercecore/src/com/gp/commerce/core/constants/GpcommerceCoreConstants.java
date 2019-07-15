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
package com.gp.commerce.core.constants;

/**
 * Global class for all GpcommerceCore constants. You can add global constants for your extension into this class.
 */
public final class GpcommerceCoreConstants extends GeneratedGpcommerceCoreConstants
{
	public static final String EXTENSIONNAME = "gpcommercecore";
	public static final String UNITS = "units";
	public static final String IS_PRIMARY_ADMIN = "isPrimaryAdmin";
	public static final String CODE = "code";
	public static final String COUNTRY = "country";
	public static final String REGION = "region";
	public static final String COUNTRY_CODE = "countryCode";
	public static final String PRODUCT_CODE = "productCode";
	public static final String SITE = "site";
	public static final String MAX_ORDER_QUANTITY_ERROR_CODE = "197";
	public static final String STOCK_LEVEL_ERROR_CODE = "198";
	public static final String SAMPLECART_ERROR_CODE = "115";
	public static final String BRANDS_ENABLED="brands.enabled";
	public static final String SOLD_TO_ID = "soldTo";
	public static final String PRODUCT_CODES = "productCodes";


	private GpcommerceCoreConstants()
	{
		//empty
	}

	// implement here constants used by this extension
	public static final String QUOTE_BUYER_PROCESS = "quote-buyer-process";
	public static final String QUOTE_SALES_REP_PROCESS = "quote-salesrep-process";
	public static final String QUOTE_USER_TYPE = "QUOTE_USER_TYPE";
	public static final String QUOTE_SELLER_APPROVER_PROCESS = "quote-seller-approval-process";
	public static final String QUOTE_TO_EXPIRE_SOON_EMAIL_PROCESS = "quote-to-expire-soon-email-process";
	public static final String QUOTE_EXPIRED_EMAIL_PROCESS = "quote-expired-email-process";
	public static final String QUOTE_POST_CANCELLATION_PROCESS = "quote-post-cancellation-process";

	public static final String DUMMY_STYLE_VARIANT_COLOR = "dummy.style.variant.color";
	public static final String FAVORITES_WISHLIST = "favorites";
	public static final String FAVORITES_WISHLIST_DESC = "My favorite wishlist";
	public static final int CUSTOMER_MODEL_TYPECODE = 4;
	public static final int ADDRESS_MODEL_TYPECODE = 23;
	public static final int ORDER_MODEL_TYPECODE = 45;
	public static final String DBLOGGER = "gp.db.logger";

	public static final String FALSE = "false";
	public static final String PENDING_BY_ADMIN = "PENDINGBYADMIN";
	public static final String PENDING_BY_GP = "PENDINGBYGP";
	public static final String B2BUNITL2 = "b2b.unit.level.l2";
	public static final String B2BUNITL3 = "b2b.unit.level.l3";
	public static final String ACTIVE = "ACTIVE";
	public static final String REJECTED = "REJECTED";
	public static final int THIRTY_DAYS =30;
	public static final int SIX_MONTHS = 6;

	public static final String COMPATIBLE_ASSET_CODES = "product.compatible.asset.codes";
	public static final String B2B_CUSTOMER_GROUP_CODES = "b2b.customergroup.codes";
	public static final String B2B_UNITLEVEL_CODES = "b2b.unitlevel.codes";
	public static final String ENV_LOCAL="local" ;

	public static final String ENABLE_LEASE_AGREEMENT="gp.scpi.enable.leaseagrement";

	public static final String B2B="B2B";
	public static final String B2C="B2C";
	public static final String EMPSTORE="EMPSTORE";
	public static final String ADMIN="admin";
	public static final String ADMIN_INITIAL_CAP="Admin";

	public static final String EVENT_SITE = "event.site";
	public static final String HYPHEN = "-";

	public static final String UIDS = "uids";
	public static final String CODES = "codes";
	public static final String CART_LABEL = "CART";

	public static final String B2B_UNIT_L1 = "L1";
	public static final String TITLE_MR = "mr";
	public static final String APPROVED = "APPROVED";
	public static final String PENDING = "Pending";
	public static final String EVENT_ORDER_SITE = "event.order.site";
	public static final String EVENT_ORDER = "event.order";
	public static final String GP_AVALARA_TAX_CALCULATION_SERVICE_USERNAME = "gp.avalara.tax.calculation.service.username";
	public static final String GP_AVALARA_TAX_CALCULATION_SERVICE_PASSWORD = "gp.avalara.tax.calculation.service.password";
	public static final String GP_AVALARA_TAX_CALCULATION_SERVICE_URL = "gp.avalara.tax.calculation.service.url";
	public static final String GP_TAX_CALCULATION_SERVICE_AVALARA_COMMIT_TRUE = "gp.tax.calculation.service.avalara.commit.true";
	public static final String GP_TAX_CALCULATION_SERVICE_AVALARA_COMMIT_FALSE = "gp.tax.calculation.service.avalara.commit.false";
	public static final String GP_TAX_CALCULATION_SERVICE_AVALARA_COMPANY_CODE = "gp.tax.calculation.service.avalara.company.code";
	public static final String GP_TAX_CALCULATION_SERVICE_AVALARA_CURRENCY_CODE = "gp.tax.calculation.service.avalara.currency.code";
	public static final String GP_AVALARA_TAX_CODE = "gp.tax.calculation.service.avalara.taxcode";
	public static final String GP_SHIPPING_TAX_CODE = "gp.tax.calculation.service.avalara.shipping.taxcode";
	public static final String GP_SHIPPING_LINE = "gp.tax.calculation.service.avalara.warehouseaddress.line1";
	public static final String ADDRESS_VERIFICATION_SERVICE_ERROR="Error occured during BODS Service call to validate Address";
	public static final String ADDRESS_SUGGESTIONS_SERVICE_ERROR="Error occured during BODS Service call to suggest Addresses";
	public static final String CONTACT_NUMBER = "contactNumber";
	public static final String CUSTOMER_LEASE_AGREEMENT_ERROR="Error in creating customer lease agreement";
	public static final String CUSTOMER_LEASE_AGREEMENT_EMPTY_ERROR="Error in creating customer lease agreement : Agreement id not generated";
	public static final String ONE_SOURCE_TAX_SERVICE_ERROR = "OneSource tax calculation service failed with execption";
	public static final String ONE_SOURCE_TAX_COMPANY_ID = "gp.tax.calculation.service.onesource.external.company.id.";
	public static final String ONE_SOURCE_TAX_CAL_DIRECTION = "gp.tax.calculation.service.onesource.tax.calculation.direction";
	public static final String ONE_SOURCE_TAX_COMPANY_ROLE = "gp.tax.calculation.service.onesource.company.role";
	public static final String ONE_SOURCE_TAX_CURRENCY_CODE = "gp.tax.calculation.service.onesource.currency.code";
	public static final String ONE_SOURCE_TAX_IS_AUDITED = "gp.tax.calculation.service.onesource.is.audited";
	public static final String ONE_SOURCE_TAX_TRANS_TYPE = "gp.tax.calculation.service.onesource.transaction.type";
	public static final String ONE_SOURCE_TAX_VALUE_LIMIT = "gp.tax.calculation.service.onesource.tax.values.limit";
	public static final String ONE_SOURCE_TAX_DELIVERY_PRODUCT_CODE = "FREIGHT";
	public static final String IS_TAX_CAL_FROM_AVALARA = "gp.tax.calculation.serivce.avalara";
	public static final String QUPLES_ERROR= "Error occurred in quples integration";
	public static final String SITE_DELIMITER = "\\|";
	public static final String GUEST_USER = "guest";


	// Users Controller
	public static final String CREATE_CUSTOMER_PREFS_FIELDS = "email,opt,firstName,lastName,mobilePhoneNumber,postalCode,age,address1,address2";
	public static final String CREATE_PDF_USER_DETAILS_FIELDS = "pdfName,phoneNumber,emailId,senderMessage,barColor,largeHeading,headLineColor,mediumHeading,isCategoryDescription,isProductSellingStatement,displayHeadlineFirstPageOnly,coverPage,listFormat,featureCheckedItems,featureCheckedItemsValue,pdfCoverImages,gpCertificationsImages";
	public static final String DISCONNECT_FIELDS = "login,password,loginType";
	public static final String CONNECT_FIELDS = "login,password,loginType,token";
	public static final String UPDATE_USER_FIELDS2 = "firstName,lastName,gender,dateOfBirth";
	public static final String UPDATE_USER_FIELDS1 = "cellPhone,firstName,lastName,gender,dateOfBirth";
	public static final String NEW_LOGIN_ID = "newLoginId";
	public static final String DOCUMENTS = "documents";
	public static final String UPDATE_CUSTOMER_PREF_FIELDS = "frequency,marketingPreferences";
	public static final String PAYMENT_DETAILS = "paymentDetails";
	public static final String UPDATE_PAYMENT_INFO_FIELDS = "accountHolderName,cardNumber,cardType,issueNumber,startMonth,expiryMonth,startYear,expiryYear,subscriptionId,paymentToken,defaultPaymentInfo,saved,billingAddress(firstName,lastName,titleCode,line1,line2,town,postalCode,region(isocode),country(isocode),defaultAddress,phone,email)";
	public static final String PAYMENT_DETAILS_ID = "paymentDetailsId";
	public static final String VERIFY_ADDRESS_FIELDS = "firstName,lastName,titleCode,line1,line2,town,postalCode,country(isocode),region(isocode)";
	public static final String PATCH_ADDRESS_FIELDS = "firstName,lastName,titleCode,line1,line2,town,postalCode,region(isocode),country(isocode),defaultAddress,phone,shippingAddress,billingAddress,defaultBillingAddress,approvalStatus,palletShipment,unit(uid),companyName";
	public static final String ADDRESS_ID = "addressId";
	public static final String PUT_ADDRESS_FIELDS = "firstName,lastName,titleCode,line1,line2,town,postalCode,region(isocode),country(isocode),defaultAddress,phone,shippingAddress,billingAddress,defaultBillingAddress";
	public static final String ADDRESS_DATA = "addressData";
	public static final String CREATE_ADDRESS_FIELDS = "firstName,lastName,titleCode,line1,line2,town,postalCode,country(isocode),region(isocode),defaultAddress,phone,shippingAddress,billingAddress,defaultBillingAddress,unit(uid),approvalStatus,palletShipment,companyName";
	public static final String NEW_LOGIN = "newLogin";
	public static final String PUT_USER_FIELDS = "firstName,lastName,titleCode,currency(isocode),language(isocode)";
	public static final String GUID = "guid";
	public static final String NOT_FOUND_IN_CURRENT_BASE_STORE = " not found in current BaseStore";
	public static final String ORDER_WITH_GUID = "Order with guid ";
	public static final String LOGIN = "login";
	public static final String REGISTER_USER_FIELDS = "login,password,titleCode,firstName,lastName,country,addToMarketComm,addToAffilatedMarketComm,token,loginType,kochAuthAccessToken,kochAuthIdToken,kochAuthRefreshToken,kochAuthTS,kochEmailId,gender,dateOfBirth,fbUniqueId,errorCode";
	public static final String REGISTERDTO_USER_FIELDS = "uid,password,titleCode,firstName,lastName,country,gender,dateOfBirth,fbUniqueId,errorCode";
	public static final String USER = "user";
	public static final String ADDRESS = "address";
	public static final String INVALID_TAX_EXEMPTION_FILE_UPLOAD = "invalid.tax.exemption.file.upload";
	public static final String MY_ACCOUNT = "my-account";
	public static final String BASESITE_DELIMITER = "gpcommercewebservices.user.delimiter";
	public static final String SAMPLECART_ENDPOINT = "samplecart.checkout.end.point.url";
	public static final String SAMPLECART_SAMPLECART_ERROR_CODE = "115";
	public static final String SAMPLECART_USERNAME = "samplecart.checkout.user";
	public static final String SAMPLECART_PASSWORD = "samplecart.checkout.password";
	public static final String ENABLE_DISTRIBUTOR_FEED="gp.scpi.enable.distributorFeed";
	public static final String SENDDUMMY_RESPONSE = "samplecart.dummy.response";
	public static final String SENDDUMMY_SUCCESS_RESPONSE = "samplecart.dummy.success.response";
	public static final String SAMPLECART_ERROR_MESSAGE = "samplecart.dummy.error.response";
	public static final String SAMPLECART_FAIL_RESPONSE = "order.sample.cart.failed";
	public static final String GPXPRESS_SITE_ID = "config.gpxpress.site.id";
	public static final String B2BWHITELABEL_SITE_ID = "config.b2bwhitelabel.site.id";
	public static final String GPXPRESS_REDIRECT_URL = "sso.redirect.url";
	public static final String UPDATE_SUBSCRIPTION_FIELDS = "code,address,paymentDetails";
	public static final String UPDATE_PAYMENT_SUBSCRIPTION_FIELDS = "accountHolderName,cardNumber,id,cardType,issueNumber,id,startMonth,expiryMonth,startYear,expiryYear,subscriptionId,paymentToken,defaultPaymentInfo,saved,billingAddress(firstName,lastName,titleCode,line1,line2,town,postalCode,region(isocode),country(isocode),defaultAddress,phone,email)";



	public static final String GPXPRESS_PAGESIZE="gpxpress.pagesize";

	//Delivery modes:
	public static final String DFS_1_DAY = "DFS / 1-Day";
	public static final String DFS_2_DAY = "DFS / 2-Day";
	public static final String DFS_STANDARD = "DFS / Standard 3-5 day";
	public static final String EMPS_1_DAY = "GPS / Next Day Express";
	public static final String EMPS_2_DAY = "GPS / 2 Day Express";
	public static final String EMPS_FEDEX = "GPS / FedEx Ground";
	public static final String CC_1_DAY = "CC / 1-Day";
	public static final String CC_2_DAY = "CC / 2-Day";
	public static final String CC_STANDARD = "CC / Standard 3-5 day";
	

	public static final String WISHLIST_EXPORT_ZIP = "wishlist.exportzip.filename";
	public static final String CSV_FILE_EXTENSION = ".csv";
	public static final String ZIP_FILE_EXTENSION = ".zip";

	public static final int SKU_NGRAM_START_INDEX = 0;
	public static final int SKU_NGRAM_END_INDEX = 4;
	public static final int SKU_EDGE_NO_CHARACTERS = 4;


	public static final String GP_US_CLASSIFICATION_CODE = "gpUSClassification";
	public static final String GP_US_CLASSIFICATION_VERSION = "1.0";
	public static final String ERP_CLASSIFICATION_VERSION = "ERP_IMPORT";
	public static final String ERP_CLASSIFICATION_CODE = "ERP_CLASSIFICATION_001";
	public static final String DOT = ".";
	public static final String COMMA = ",";
	public static final String DIMENSION = "x";
	public static final String VOLUME = "volume";
	public static final String DIMENSIONS = "dimensions";
	public static final String BUNDLE = "Bundle";
	public static final String BUNDLE_NO = "BundleNo";
	public static final String QSU = "QSU";
	public static final String UNIT = "Unit";
	public static final String ZSU = "ZSU";
	public static final String EACH = "Each";
	public static final String CASE = "Case";
	public static final String PCE = "PCE";
	public static final String EA = "EA";
	public static final String CS = "CS";
	public static final String BDL = "BDL";
	public static final String PRODUCT_DETAIL = "productDetail";

	public static final String PRODUCT_DETAILS_CODE = "10001";
	public static final String CASE_SHIPPING_INFO_CODE = "20001";
	public static final String EACH_SHIPPING_INFO_CODE = "30001";
	public static final String UNIT_SHIPPING_INFO_CODE = "40001";
	public static final String QSU_SHIPPING_INFO_CODE = "50001";
	public static final String BUNDLE_SHIPPING_INFO_CODE = "60001";

	public static final String SYMBOL_UNDERSCORE = "_";

	public static final String ACCESS_TOKEN_COOKIE = "access_token";
	public static final String ACC_SECURE_GUID_COOKIE = "acceleratorSecureGUID";
	public static final String REFRESH_TOKEN_COOKIE = "refresh_token";
	public static final String GPXPRESS = "gpxpress";
	public static final String EMPTY_STRING = "";
	public static final String IS_DISCONTINUED_ONLY = "isDiscontinued";
}

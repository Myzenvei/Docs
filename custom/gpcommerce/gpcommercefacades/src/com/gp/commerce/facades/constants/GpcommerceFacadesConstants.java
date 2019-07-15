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
package com.gp.commerce.facades.constants;

/**
 * Global class for all GpcommerceFacades constants.
 */
@SuppressWarnings("PMD")
public class GpcommerceFacadesConstants extends GeneratedGpcommerceFacadesConstants
{
	public static final String EXTENSIONNAME = "gpcommercefacades";

	private GpcommerceFacadesConstants()
	{
		//empty
	}

	public static final String FORMAT_TYPE_ZOOM = "zoom";
	public static final String FORMAT_TYPE_PRODUCT = "product";
	public static final String FORMAT_TYPE_CART_ICON = "cartIcon";
	public static final String FORMAT_TYPE_THUMBNAIL = "thumbnail";
	public static final String ADD_WISHLIST_ERROR_CODE = "1002";
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	public static final String DEFAULT_MAX_PRODUCT_QUANTITY = "default.max.product.quantity";
	public static final String PERCENTAGE = "percentage";
	public static final String COMMA = ",";
	public static final String QUICK_ORDER_WISHLIST_EXCEPTION = "create_quick_order_wishlist_exception";

	//Navigation URLs
	public static final String EMPTY_PAGE_URL = "";
	public static final String HOME_PAGE_URL = "/";
	public static final String LOGIN_PAGE_URL = "/login";
	public static final String LOGOUT_PAGE_URL = "/logout";
	public static final String REGISTER_PAGE_URL = "/register";
	public static final String SEARCH_RESULTS_PAGE_URL = "/search";
	public static final String COMPARE_PRODUCTS_PAGE_URL = "/compare";
	public static final String CART_PAGE_URL = "/cart";
	public static final String PDP_PAGE_URL = "/pdp";
	public static final String CHECKOUT_PAGE_URL = "/checkout";
	public static final String CONTACT_US_PAGE_URL = "/contact-us";
	public static final String QUICK_ORDER_PAGE_URL = "/quickOrder";
	public static final String ORDER_CONFIRMATION_PAGE_URL = "/checkout/orderConfirmation";
	public static final String ADD_ADDRESS_PAGE_URL = "/my-account/addresses";
	public static final String PERMISSION_LANDING_PAGE_URL = "/businessorg/permissions/details";
	public static final String USER_GROUP_LANDING_PAGE_URL = "/businessorg/usergroups/details";
	public static final String USER_GROUPS_PAGE_URL = "/businessorg/usergroups";
	public static final String USER_LANDING_PAGE_URL = "/businessorg/users/details";
	public static final String BUSINESS_UNITS_LANDING_PAGE_URL = "/businessorg/units/details";
	public static final String USER_DETAILS_PAGE_URL = "/businessorg/users/details";
	public static final String ORDER_DETAILS_PAGE_URL = "/my-account/order/";
	public static final String ORDERAPPROVAL_DETAILS_PAGE_URL = "/businessorg/orderapproval/";
	public static final String TERMS_CONDITIONS = "/termsAndConditions";
	public static final String CLUDO = "/cludosearchresults";
	public static final String LEASE = "/lease";
	public static final String CATEGORY = "/category/categories";
	public static final String MY_LIST_DETAILS_PAGE_URL = "/my-account/lists";
	public static final String FAV_DETAILS_PAGE_URL = "/my-account/listdetails?listName=favorites";
	public static final String LIST_DETAILS_PAGE_URL = "/my-account/listdetails";
	public static final String SUPPORT_TICKETS_PAGE_URL = "/my-account/supportTickets";
	public static final String LEARN_MORE_URL = "/learnMore";
	public static final String SUBSCRIPTION_LANDING = "/my-account/subscriptions";
	public static final String BUNDLE_PAGE = "/bundle";
	
	//Third Party App Data
	public static final String GOOGLE_APP_ID = "google.app.id";
	public static final String GOOGLE_MAPS_KEY = "google.maps.key";
	public static final String FACEBOOK_APP_ID = "facebook.app.id";
	public static final String YFORMS_ENDPOINT = "yforms.endpoint";
	public static final String QPLES_ENDPOINT = "qples.endpoint";

	public static final String FREE_SHIPPING = "free.shipping.text";
	public static final String DOLLAR = "$";
	public static final String PRODUCT_CODE_INDEX = "product.code.index";
	public static final String PRODUCT_QUANTITY_INDEX = "product.quantity.index";

	public static final String SOURCE_VALIDATION_ERROR = "Parameter source cannot be null.";
	public static final String TARGET_VALIDATION_ERROR = "Parameter target cannot be null.";
	public static final String CSV_FILE_EXTENSION = ".csv";
	public static final String CSV_FILE_TYPE = "text/csv";
	public static final String QUICK_ORDER_EMAIL= "quickorder.email.csv.directory";
	public static final String GPWEBSER_DELIMETER = "gpcommercewebservices.user.delimiter";
	public static final String FAILED_ORDER_EMAIL = "failed.orders.email.csv.filename";
	public static final String B2B_APPROVAL_EMAIL = "b2b.approval.admin.email";
	public static final String CART_EMAIL = "cart.email.csv.filename";
	public static final String YMARKETING = "ymarketing.preference.type";

	public static final String AUTH_PARAMS_CLIENT_ID = "auth.params.client.id";
	public static final String AUTH_PARAMS_CLIENT_SECRET = "auth.params.client.secret";
	public static final String AUTH_PARAMS_GRANT_TYPE = "auth.params.grant.type";

	public static final String ACCESS_TOKEN_COOKIE = "access_token";
	public static final String ACC_SECURE_GUID_COOKIE = "acceleratorSecureGUID";
	public static final String REFRESH_TOKEN_COOKIE = "refresh_token";
	
	public static final String ZIP_FILE_EXTENSION = ".zip";
	public static final String IMAGE_FILE_PATH = "///images";
	public static final String FORMAT_IMAGE_FILE_PATH = "///formatimages_";
	public static final String PRODUCT_FORMAT_IMAGE_FILE_PATH = "///productimages_";
	
	public static final String PRODUCT_EMAIL = "product.email.csv.filename";
	public static final String GPXPRESS = "gpxpress";
	public final static String RELAY_ENABLED = "relayStateEnabled";
	
	public static final String PAYPAL_PAYMENT_PROVIDER="Paypal";
	public static final String GOOGLE_PAY_PAYMENT_PROVIDER="Google Pay";
    public static final String GOOGLE_PAY_APIVERSION="googlepay.apiversion";
	public static final String GOOGLE_PAY_APIVERSIONMINOR="googlepay.apiversionminor";
	public static final String GOOGLE_PAY_ENVIRONMENT="googlepay.environment";
	public static final String GOOGLE_PAY_MERCHANTNAME="googlepay.merchant.name.";
	public static final String GOOGLE_PAY_MERCHANTID="googlepay.merchant.id.";
	public static final String GOOGLE_PAY_ALLOWEDPAYMENTMETHODS_TYPE="gogglepay.allowedpaymethods.type";
	public static final String GOOGLE_PAY_ALLOWEDCARDS="googlepay.allowedcards";
	public static final String GOOGLE_PAY_ALLOWEDAUTHMETHODS="googlepay.allowedauthmethods";
	public static final String GOOGLE_PAY_TOKENIZATION_TYPE="googlepay.tokenizationspecification.type";
	public static final String GOOGLE_PAY_TOKENIZATION_GATEWAY="googlepay.tokenizationspecification.gateway";
	public static final String GOOGLE_PAY_TOTALPRICE_STATUS_CONSTANT="FINAL";
	public static final String GOOGLE_PAY_BILLING_ADDRESS_FORMAT="FULL";
	public static final boolean GOOGLE_PAY_BILLING_ADDRESS_REQUIRED=Boolean.TRUE;
	public static final boolean GOOGLE_PAY_BILLING_PHONE_REQUIRED=Boolean.TRUE;
	
	public static final String APPLE_PAY_PAYMENT_PROVIDER = "Apple Pay";
	
	public static final String CARD_TYPE_VISA = "visa";
	public static final String CARD_TYPE_MASTERCARD = "mastercard";
	public static final String CARD_TYPE_AMEX = "amex";
	public static final String CARD_TYPE_DISCOVER = "discover";
	
	public static final String APPLE_PAY_MERCHANT_IDENTIFIER = "apple.pay.merchantIdentifier.";
	public static final String APPLE_PAY_MERCHANT_ID_CERTIFICATE_FILE_PATH = "apple.pay.merchantId.cert.file.path";
	public static final String APPLE_PAY_MERCHANT_ID_CERTIFICATE_PWD = "apple.pay.merchantId.cert.pass";
	public static final String APPLE_PAY_DISPLAY_NAME = "apple.pay.displayName";
	public static final String APPLE_PAY_INITIATIVE = "apple.pay.initiative";
	public static final String APPLE_PAY_INITIATIVE_CONTEXT = "apple.pay.initiativeContext";
	public static final String APPLE_PAY_MERCHANT_NAME = "apple.pay.merchant.name.";

	public static final String DISPENSER_HEADER_TEXT="dispenser.header.text";
	public static final String DISPENSER_HEADER_DESCRIPTION="dispenser.header.description";
	public static final String DISPENSER_DISCLAIMER_TEXT="dispenser.disclaimer.text";
	public static final String DISPENSER_KEY_QUANTITY="dispenser.keyQuantity.value";
    public static final String DISPENSER_TICKET_SUCCESS="dispenser.ticket.success.message";
	public static final String DISPENSER_TICKET_FAILURE="dispenser.ticket.failure.message";

}

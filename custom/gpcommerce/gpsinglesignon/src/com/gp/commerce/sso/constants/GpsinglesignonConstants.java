/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.sso.constants;

/**
 * Global class for all Gpsinglesignon constants. You can add global constants for your extension into this class.
 */
public final class GpsinglesignonConstants extends GeneratedGpsinglesignonConstants
{
	public static final String EXTENSIONNAME = "gpsinglesignon";

	public static final String SSO_COOKIE_MAX_AGE = "sso.cookie.max.age";

	public static final String SSO_COOKIE_PATH = "sso.cookie.path";

	public static final String SSO_COOKIE_DOMAIN = "sso.cookie.domain";

	public static final String SSO_DEFAULT_COOKIE_DOMAIN = null;

	public static final String SSO_DEFAULT_COOKIE_NAME = "samlPassThroughToken";

	public static final String DEFAULT_COOKIE_PATH = "/";

	public static final String DEFAULT_REDIRECT_URL = "https://localhost:9002/";

	public static final int DEFAULT_COOKIE_MAX_AGE = -1;

	public static final String SSO_COOKIE_NAME = "sso.gp.cookie.name";

	public static final String SSO_PASSWORD_ENCODING = "sso.password.encoding";

	public static final String SSO_REDIRECT_URL = "sso.gp.redirect.url";

	public static final String SSO_USERID_KEY = "sso.userid.attribute.key";

	public static final String SSO_USERGROUP_KEY = "sso.usergroup.attribute.key";

	public static final String SSO_FIRSTNAME_KEY = "sso.firstname.attribute.key";

	public static final String SSO_LASTNAME_KEY = "sso.lastname.attribute.key";

	public static final String MD5_PASSWORD_ENCODING = "md5";

	public static final String REDIRECT_PREFIX = "redirect:";

	public final static String SOLD_TO = "soldTo";

	public final static String USER_ID = "userId";
	
	public final static String EMAIL = "email";
	
	public final static String RELAY_ENABLED = "relayStateEnabled";

	public final static
	String SOLD_TO_URL = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/soldto";
	
	public final static
	String USER_ROLE_URL = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/userrole";

	public final static
	String APP_SAMPLE_STATUS = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/approvedsamplestatus";
	
	public final static String ROLE = "b2bcustomer";
	
	public static final String SSO_RELAY_REDIRECT_URL = "sso.gp.relay.redirect.url";

	public static final String SESSION_COOKIE = "JSESSIONID";

	public static final String SSO_RELAY_ENABLED = "sso.relay.enabled";
}

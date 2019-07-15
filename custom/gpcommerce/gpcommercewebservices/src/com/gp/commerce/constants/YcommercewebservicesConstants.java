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
package com.gp.commerce.constants;

@SuppressWarnings(
{ "deprecation", "squid:CallToDeprecatedMethod" })
public class YcommercewebservicesConstants extends GeneratedYcommercewebservicesConstants
{
	public static final String MODULE_NAME = "gpcommercewebservices";
	public static final String MODULE_WEBROOT = ("y" + "commercewebservices").equals(MODULE_NAME) ? "rest" : MODULE_NAME;
	public static final String CONTINUE_URL = "session_continue_url";
	public static final String CONTINUE_URL_PAGE = "session_continue_url_page";
	public static final String OPTIONS_SEPARATOR = ",";
	public static final String HTTP_START = "http";

	public static final String HTTP_REQUEST_PARAM_LANGUAGE = "lang";
	public static final String HTTP_REQUEST_PARAM_CURRENCY = "curr";

	public static final String ROOT_CONTEXT_PROPERTY = "commercewebservices.rootcontext";
	public static final String V1_ROOT_CONTEXT = "/" + MODULE_WEBROOT + "/v1";
	public static final String V2_ROOT_CONTEXT = "/" + MODULE_WEBROOT + "/v2";
	public static final String URL_SPECIAL_CHARACTERS_PROPERTY = "commercewebservices.url.special.characters";
	public static final String DEFAULT_URL_SPECIAL_CHARACTERS = "?,/";
	public static final String LOCATION = "Location";
	public static final String SLASH = "/";
	public static final String TRACEDATA = "traceData";
	public static final String DATEFORMAT = "gp.dateFormat";
	public static final String GP_ORDER_HISTORY_ENV = "gp.order.history.env";
	public static final String QUICKORDER_PRODUCTS_MAXIMUM = "quickorder.products.maximum";
	public static final String QUICKORDER_INVALID_PRODUCT = "text.quickOrder.product.code.invalid";
	public static final String QUICKORDER_INVALID_COUNT =  "text.quickOrder.product.quantity.invalid";
	public static final String BASKET_INFORMATION_QUANTITY_NO_ITEMS_ADDED="basket.information.quantity.noItemsAdded.";
	public static final String BASKET_INFORMATION_QUANTITY_REDUCED_ITEMS_ADDED="basket.information.quantity.reducedNumberOfItemsAdded.";
	public static final String GENERIC_ERROR_OCCURRED="generic.error.occurred";
	public static final String BASKET_ERROR_OCCURRED="basket.error.occurred";
	public static final String GPCOMMERCEWEBSERVICES_SEARCH_PAGE_SIZE = "gpcommercewebservices.search.pageSize";
	public static final int DEFAULT_SEARCH_PAGE_SIZE = 64;
	public static final String DEFAULT_START_DATE = "default.start.date";
	public static final String DEFAULT_END_DATE = "default.end.date";
	public static final String GP_PRICE_ROW_DATE_FORAMAT = "gp.price.row.date.format";
	public static final String SITE_ID_LIST = "gp.site.id";
	public static final String GP_DEFAULT_PRICE_GROUP = "gp.default.price.group";
	public static final String IS_BRAND_ENABLED = "brands.enabled";
	public static final String IS_AVAILABLE_LOCATION_ENABLED = "isAvailable.enabled";
	public static final String AVAILABLE_LOCATION_KEY = "available.forLocation.key";
	public static final String NO_BRAND = "gp.noBrand.constant";
	public static final String NO_DISTRIBUTOR = "gp.noDistributor.constant";
	public static final String IS_CONTENT_INDEXING_ENABLED = "isContentIndexingEnabled";
	public static final String IS_SEQUENCE_BY_COUNT_ENABLED = "isSequenceByCountEnabled";
	public static final String ASC_SEQUENCE_BY_COUNT = "sequenceByCountASC";
	public static final String NO_CATEGORY = "gp.noCategory.constant";
	public static final String PAYMENT_INFO_NULL_EXCEPTION_MSG="Exception creating Google pay payment info ";

	private YcommercewebservicesConstants()
	{
		//empty
	}
}

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
package com.gp.b2baccaddon.commerce.controllers;

/**
 */
public interface Gpb2baccaddonControllerConstants
{
	String ADDON_PREFIX = "addon:/gpb2baccaddon/";
	String STOREFRONT_PREFIX = "/";

	interface Views
	{
		interface Pages
		{
			interface MultiStepCheckout
			{
				String ChoosePaymentTypePage = ADDON_PREFIX + "pages/checkout/multi/choosePaymentTypePage";
				String CheckoutSummaryPage = ADDON_PREFIX + "pages/checkout/multi/checkoutSummaryPage";
			}

			interface Checkout
			{
				String CheckoutLoginPage = STOREFRONT_PREFIX + "pages/checkout/checkoutLoginPage";
				String ReadOnlyExpandedOrderForm = STOREFRONT_PREFIX + "fragments/checkout/readOnlyExpandedOrderForm";
			}

		}

		interface Fragments
		{

			interface Product
			{
				String ProductLister = ADDON_PREFIX + "fragments/product/productLister";
			}

		}
	}
}

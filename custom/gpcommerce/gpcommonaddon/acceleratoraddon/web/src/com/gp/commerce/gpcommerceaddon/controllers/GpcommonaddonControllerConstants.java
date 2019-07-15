/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.gpcommerceaddon.controllers;

import de.hybris.platform.acceleratorcms.model.components.FooterNavigationComponentModel;

import com.gp.commerce.core.model.GPFooterComponentModel;
import com.gp.commerce.core.model.GPHeaderNavigationComponentModel;
import com.gp.commerce.core.model.GPSubscriptionFooterComponentModel;
import com.gp.commerce.core.model.GPTipsAndKnowledgeInfoComponentModel;
import com.gp.commerce.gpcommerceaddon.model.GPCheckoutFooterNavigationComponentModel;
import com.gp.commerce.gpcommerceaddon.model.GPLiveChatComponentModel;
import com.gp.commerce.gpcommerceaddon.model.GPNavigationComponentModel;
import com.gp.commerce.gpcommerceaddon.model.GPQuplesComponentModel;
import com.gp.commerce.gpcommerceaddon.model.GPUnsubscribeComponentModel;
import com.gp.commerce.gpcommerceaddon.model.MardiGrasFAQComponentModel;
import com.gp.commerce.gpcommerceaddon.model.MardiGrasFooterComponentModel;
import com.gp.commerce.gpcommerceaddon.model.MardiGrasHeroBannerComponentModel;
import com.gp.commerce.gpcommerceaddon.model.MardiGrasStoreLocatorComponentModel;

/**
 */
public interface GpcommonaddonControllerConstants
{
	String EXTENSION_NAME = "gpcommonaddon";
	/**
	 * Class with action name constants
	 */
	interface Actions
	{
		interface Cms // NOSONAR
		{
			String _Prefix = "/view/"; // NOSONAR
			String _Suffix = "Controller"; // NOSONAR

			/**
			 * Default CMS component controller
			 */
			String DefaultCMSComponent = _Prefix + "DefaultCMSComponentController"; // NOSONAR

			String GPHeaderNavigationComponent = _Prefix + GPHeaderNavigationComponentModel._TYPECODE + _Suffix; // NOSONAR

			/**
			 * CMS components that have specific handlers
			 */
			String FooterNavigationComponent = _Prefix + FooterNavigationComponentModel._TYPECODE + _Suffix; // NOSONAR
			String GPCheckoutFooterNavigationComponent = _Prefix + GPCheckoutFooterNavigationComponentModel._TYPECODE + _Suffix; // NOSONAR
			String GPTipsAndKnowledgeInfoComponent = _Prefix + GPTipsAndKnowledgeInfoComponentModel._TYPECODE + _Suffix; // NOSONAR
			String GPNavigationComponent = _Prefix + GPNavigationComponentModel._TYPECODE + _Suffix; // NOSONAR
			String MardiGrasFooterComponent = _Prefix + MardiGrasFooterComponentModel._TYPECODE + _Suffix; // NOSONAR
			String MardiGrasHeroBannerComponent = _Prefix + MardiGrasHeroBannerComponentModel._TYPECODE + _Suffix; // NOSONAR
			String MardiGrasFAQComponent = _Prefix + MardiGrasFAQComponentModel._TYPECODE + _Suffix; // NOSONAR
			String MardiGrasStoreLocatorComponent = _Prefix + MardiGrasStoreLocatorComponentModel._TYPECODE + _Suffix; // NOSONAR
			String GPLiveChatComponent = _Prefix + GPLiveChatComponentModel._TYPECODE + _Suffix; // NOSONAR
			String GPUnsubscribeComponent = _Prefix + GPUnsubscribeComponentModel._TYPECODE + _Suffix; // NOSONAR
			String GPQuplesComponent = _Prefix + GPQuplesComponentModel._TYPECODE + _Suffix; // NOSONAR
			String GPFooterComponent = _Prefix + GPFooterComponentModel._TYPECODE + _Suffix; //NOSONAR
			String GPSubscriptionFooterComponent = _Prefix + GPSubscriptionFooterComponentModel._TYPECODE + _Suffix; //NOSONAR
		}
	}

	/**
	 * Class with view name constants
	 */
	interface Views
	{
		interface Cms // NOSONAR
		{
			String ComponentPrefix = "cms/"; // NOSONAR
		}

		interface Pages
		{
		}

		interface Fragments
		{
		}
	}
}

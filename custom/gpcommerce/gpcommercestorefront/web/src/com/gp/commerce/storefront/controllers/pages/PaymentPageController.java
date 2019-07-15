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
package com.gp.commerce.storefront.controllers.pages;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gp.commerce.storefront.controllers.ControllerConstants;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.servicelayer.config.ConfigurationService;

/**
 * Register Controller. Handles register for the account flow.
 */
@Controller
@RequestMapping(value = "/payment")
public class PaymentPageController extends AbstractPageController
{
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "userFacade")
	private UserFacade userFacade;
	

	private static final String PAYMENTPAGE = "paymentPage";
	
	/**
	 * @param model
	 * @throws CMSItemNotFoundException
	 *            This method is used to display registration Page for B2C
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String getRegistrationPage(final Model model) throws CMSItemNotFoundException
	{
		
		
		final ContentPageModel contactUSCMSPage = getContentPageForLabelOrId(PAYMENTPAGE);
		storeCmsPageInModel(model, contactUSCMSPage);
		setUpMetaDataForContentPage(model, contactUSCMSPage);

		return ControllerConstants.Views.Pages.Account.PaymentPage;
	}
}

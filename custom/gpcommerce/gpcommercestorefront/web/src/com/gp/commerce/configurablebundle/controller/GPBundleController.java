/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.configurablebundle.controller;

import com.gp.commerce.storefront.controllers.ControllerConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping(value = "/bundle")
public class GPBundleController extends AbstractPageController {

    private static final String BUNDLE_CMS_PAGE = "bundle-setup";



    @RequestMapping(method = RequestMethod.GET)
    public String addBundleToCart(final Model model) throws CMSItemNotFoundException {

        storeCmsPageInModel(model, getContentPageForLabelOrId(BUNDLE_CMS_PAGE));

        return ControllerConstants.Views.Pages.Bundle.gpStartBundlePage;


    }


    @RequestMapping(value = "/start", method = RequestMethod.GET, produces = "application/json")
    public String addBundleToCart(@RequestParam("productCodeForBundle") final String code,
                                  @RequestParam("bundleTemplate") final String bundle, final Model model) throws CMSItemNotFoundException {

        storeCmsPageInModel(model, getContentPageForLabelOrId(BUNDLE_CMS_PAGE));

        return ControllerConstants.Views.Pages.Bundle.gpStartBundlePage;


    }
}

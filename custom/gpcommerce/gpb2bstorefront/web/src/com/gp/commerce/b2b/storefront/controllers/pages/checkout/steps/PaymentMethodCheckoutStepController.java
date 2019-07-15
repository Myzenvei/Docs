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
package com.gp.commerce.b2b.storefront.controllers.pages.checkout.steps;


import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractCartPageController;
import de.hybris.platform.acceleratorstorefrontcommons.util.AddressDataUtil;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CardTypeData;
import de.hybris.platform.commercefacades.user.data.CountryData;

import com.gp.commerce.b2b.storefront.controllers.ControllerConstants;
import com.gp.commerce.b2b.storefront.security.GPPaymentInfoForm;
import com.gp.commerce.b2b.storefront.security.GpPayPalPaymentInfoForm;
import com.gp.commerce.facades.order.impl.GPCheckoutFacade;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping(value = "/checkout/multi/payment-method")
public class PaymentMethodCheckoutStepController extends AbstractCartPageController
{
	protected static final Map<String, String> CYBERSOURCE_SOP_CARD_TYPES = new HashMap<>();

	@Resource(name = "addressDataUtil")
	private AddressDataUtil addressDataUtil;
	
	@Resource(name = "gpCheckoutFacade")
	private GPCheckoutFacade gpCheckoutFacade;
	

	@ModelAttribute("billingCountries")
	public Collection<CountryData> getBillingCountries()
	{
		return getCheckoutFacade().getBillingCountries();
	}

	@ModelAttribute("cardTypes")
	public Collection<CardTypeData> getCardTypes()
	{
		return getCheckoutFacade().getSupportedCardTypes();
	}
	
	@RequestMapping(value = "/addPaymentInfo", method = RequestMethod.POST)
	public String getPaymentInfos(final GPPaymentInfoForm gpInfoForm, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException {
		gpInfoForm.setPaymentMethod("CreditCard");
		model.addAttribute("paymentForm", gpInfoForm);
		return ControllerConstants.Views.Pages.MultiStepCheckout.GpPaymentPage;
	}
	
	@RequestMapping(value = "/getPayPalPayerInfo", method = RequestMethod.GET)
	public String getPayPalPaymentInfos(@RequestParam(value = "paymentId", defaultValue="") String paymentId, @RequestParam(value = "token", defaultValue="") String token,
			@RequestParam(value = "PayerID" ,defaultValue="") String payerId, @RequestParam(value = "cartId", defaultValue="") String cartId,
			final Model model, final GpPayPalPaymentInfoForm gpInfoForm) throws CMSItemNotFoundException {
		return null;
		}
	
	public GPCheckoutFacade getGpCheckoutFacade() {
		return gpCheckoutFacade;
	}

	public void setGpCheckoutFacade(GPCheckoutFacade gpCheckoutFacade) {
		this.gpCheckoutFacade = gpCheckoutFacade;
	}


}

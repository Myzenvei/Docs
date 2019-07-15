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
package com.gp.b2baccaddon.commerce.controllers.pages;

import java.util.Arrays;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.hybris.platform.acceleratorfacades.flow.CheckoutFlowFacade;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractLoginPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.GuestForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.LoginForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.RegisterForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.GuestValidator;
import de.hybris.platform.acceleratorstorefrontcommons.security.GUIDCookieStrategy;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;

import com.gp.commerce.facades.customer.GpCustomerFacade;

import com.gp.b2baccaddon.commerce.controllers.Gpb2baccaddonControllerConstants;


/**
 * Checkout Login Controller. Handles login and register for the checkout flow.
 */
@RequestMapping(value = "/login/checkout")
public class CheckoutLoginController extends AbstractLoginPageController
{
	private static final Logger LOGGER = Logger.getLogger(CheckoutLoginController.class);
	
	private static final String FORM_GLOBAL_ERROR = "form.global.error";
	
	@Resource(name = "checkoutFlowFacade")
	private CheckoutFlowFacade checkoutFlowFacade;

	@Resource(name = "guidCookieStrategy")
	private GUIDCookieStrategy guidCookieStrategy;

	@Resource(name = "authenticationManager")
	private AuthenticationManager authenticationManager;
	
	@Resource(name = "guestValidator")
	private GuestValidator guestValidator;
	
	@Resource(name = "gpDefaultCustomerFacade")
	private GpCustomerFacade gpCustomerFacade;
	
	@Resource(name="commerceCartService")
	private CommerceCartService commerceCartService;
	
	@Resource(name="cartService")
	private CartService cartService;
	
	@Override
	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId("checkout-login");
	}

	@RequestMapping(method = RequestMethod.GET)
	public String doCheckoutLogin(@RequestParam(value = "error", defaultValue = "false") final boolean loginError,
			final HttpSession session, final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		// Express Checkout is not available yet in B2B. 
		model.addAttribute("expressCheckoutAllowed", Boolean.FALSE );
		return getDefaultLoginPage(loginError, session, model);
	}
	
	@RequestMapping(value = "/guest", method = RequestMethod.POST)
	public String doAnonymousCheckout(final GuestForm form, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException
	{
		guestValidator.validate(form, bindingResult);
		getSessionService().setAttribute("guestEmail", form.getEmail());
		
		if (null != request.getCookies()) {
			Cookie cookieValue = Arrays.asList(request.getCookies()).stream()
					.filter(cookie -> "guid".equalsIgnoreCase(cookie.getName())).findFirst().orElse(null);
			if (null != cookieValue) {
				CartModel cart = commerceCartService.getCartForGuidAndSite(cookieValue.getValue(),
						getBaseSiteService().getCurrentBaseSite());
				if (null != cart) {
					cartService.setSessionCart(cart);
				}
			}
		}
		try
		{
			if (bindingResult.hasErrors())
			{
				model.addAttribute(form);
				model.addAttribute(new LoginForm());
				model.addAttribute(new RegisterForm());
				GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
				return handleRegistrationError(model);
			}

			gpCustomerFacade.createGuestUserForAnonymousCheckout(form.getEmail(),
					getMessageSource().getMessage("text.guest.customer", null, getI18nService().getCurrentLocale()));
			getGuidCookieStrategy().setCookie(request, response);
			getSessionService().setAttribute(WebConstants.ANONYMOUS_CHECKOUT, Boolean.FALSE);
		}
		catch (final DuplicateUidException e)
		{
			LOGGER.warn("guest registration failed: ", e);
			GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
			return handleRegistrationError(model);
		}

		return REDIRECT_PREFIX + getSuccessRedirect(request, response);
	
	}

	@Override
	protected String getView()
	{
		return Gpb2baccaddonControllerConstants.Views.Pages.Checkout.CheckoutLoginPage;
	}

	@Override
	protected String getSuccessRedirect(final HttpServletRequest request, final HttpServletResponse response)
	{
		// Redirect to the main checkout controller to handle checkout.
		return "/checkout";
	}
	/**
	 * Checks if there are any items in the cart.
	 * 
	 * @return returns true if items found in cart.
	 */
	protected boolean hasItemsInCart()
	{
		final CartData cartData = getCheckoutFlowFacade().getCheckoutCart();

		return (cartData.getEntries() != null && !cartData.getEntries().isEmpty());
	}

	protected String getCheckoutUrl()
	{
		// Default to the multi-step checkout
		return "/checkout/multi";
	}

	protected CheckoutFlowFacade getCheckoutFlowFacade()
	{
		return checkoutFlowFacade;
	}

	@Override
	protected GUIDCookieStrategy getGuidCookieStrategy()
	{
		return guidCookieStrategy;
	}

	protected AuthenticationManager getAuthenticationManager()
	{
		return authenticationManager;
	}
}
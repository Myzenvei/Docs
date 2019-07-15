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

import de.hybris.platform.acceleratorfacades.flow.impl.SessionOverrideCheckoutFlowFacade;
import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.consent.data.ConsentCookieData;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractCheckoutController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.ConsentForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.GuestRegisterForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.GuestRegisterValidator;
import de.hybris.platform.acceleratorstorefrontcommons.security.AutoLoginStrategy;
import de.hybris.platform.acceleratorstorefrontcommons.strategy.CustomerConsentDataStrategy;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.consent.ConsentFacade;
import de.hybris.platform.commercefacades.coupon.data.CouponData;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.util.ResponsiveUtils;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.storefront.controllers.ControllerConstants;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;



/**
 * CheckoutController
 */
@Controller
@RequestMapping(value = "/checkout")
public class CheckoutController extends AbstractCheckoutController
{
	private static final String REFERER = "Referer";
	private static final String GUEST_REGISTER_FORM = "guestRegisterForm";
	private static final Logger LOG = Logger.getLogger(CheckoutController.class);
	/**
	 * We use this suffix pattern because of an issue with Spring 3.1 where a Uri value is incorrectly extracted if it
	 * contains on or more '.' characters. Please see https://jira.springsource.org/browse/SPR-6164 for a discussion on
	 * the issue and future resolution.
	 */
	private static final String ORDER_CODE_PATH_VARIABLE_PATTERN = "{orderCode:.*}";

	private static final String CHECKOUT_ORDER_CONFIRMATION_CMS_PAGE_LABEL = "orderConfirmation";
	private static final String CONSENT_FORM_GLOBAL_ERROR = "consent.form.global.error";
	public static final String REDIRECT_TO_LOGIN_FOR_CHECKOUT = REDIRECT_PREFIX + "/login";
	private static final String CHECKOUTPAGE = "checkoutPage";
	private static final String CART_PAGE_URL = "/cart";
	private static final String LAST_LINK_CLASS = "active";
	private static final String BREADCRUMBS_ATTR = "breadcrumbs";
	
	private static final String TEXT_LINK_CART = "text.link.cart";
	private static final String TEXT_LINK_CHECKOUT = "text.link.checkout";
	
	protected static final String IS_CHECKOUT_ENABLED="isCheckoutEnabled";
	private static final String GUEST_CREATE_ACCOUNT = "guestRegisterAfterPlaceOrder";

	@Resource(name="cartService")
	private CartService cartService;
	
	@Resource(name = "productFacade")
	private ProductFacade productFacade;

	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;

	@Resource(name = "checkoutFacade")
	private CheckoutFacade checkoutFacade;

	@Resource(name = "guestRegisterValidator")
	private GuestRegisterValidator guestRegisterValidator;

	@Resource(name = "autoLoginStrategy")
	private AutoLoginStrategy autoLoginStrategy;

	@Resource(name = "consentFacade")
	protected ConsentFacade consentFacade;

	@Resource(name = "customerConsentDataStrategy")
	protected CustomerConsentDataStrategy customerConsentDataStrategy;

	@Resource(name = "simpleBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder checkoutBreadcrumbBuilder;
	
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	
	@ExceptionHandler(ModelNotFoundException.class)
	public String handleModelNotFoundException(final ModelNotFoundException exception, final HttpServletRequest request)
	{
		request.setAttribute("message", exception.getMessage());
		return FORWARD_PREFIX + "/404";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String checkout(final Model model, final RedirectAttributes redirectModel, final HttpServletRequest request, 
			final HttpServletResponse response) throws CMSItemNotFoundException
	{
		final ContentPageModel checkoutCMSPage = getContentPageForLabelOrId(CHECKOUTPAGE);
		storeCmsPageInModel(model, checkoutCMSPage);
		setUpMetaDataForContentPage(model, checkoutCMSPage);
		final Cookie subCartCookie = WebUtils.getCookie(request, "subscrCartId");
		final List<Breadcrumb> breadcrumbs = checkoutBreadcrumbBuilder.getBreadcrumbs(null);
		if(null==subCartCookie) {
		breadcrumbs.add(new Breadcrumb(CART_PAGE_URL, getMessageSource().getMessage(TEXT_LINK_CART, null,
                getI18nService().getCurrentLocale()), null));
		breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage(TEXT_LINK_CHECKOUT, null,
                getI18nService().getCurrentLocale()), LAST_LINK_CLASS));
		}
		model.addAttribute(BREADCRUMBS_ATTR, GPFunctions.convertToJSON(breadcrumbs));

		if(null!=getSessionService().getAttribute("guestEmail"))
		{
		model.addAttribute("guestEmail",getSessionService().getAttribute("guestEmail"));
		}
		return getView(request, response);
	}

	private String getView(HttpServletRequest request, HttpServletResponse response) {
		final Cookie guestRegAfterPlaceOrder = WebUtils.getCookie(request, GUEST_CREATE_ACCOUNT);
		if(null != guestRegAfterPlaceOrder && "true".equals(guestRegAfterPlaceOrder.getValue())) {
			getSessionService().getCurrentSession().removeAttribute(GUEST_CREATE_ACCOUNT);
			guestRegAfterPlaceOrder.setValue(null);
			guestRegAfterPlaceOrder.setMaxAge(0);
			guestRegAfterPlaceOrder.setPath("/");
			response.addCookie(guestRegAfterPlaceOrder);
			return REDIRECT_PREFIX + "/";
		}
		else if ((!getUserFacade().isAnonymousUser()) || (StringUtils.isNotEmpty(cmsSiteService.getSiteConfig(IS_CHECKOUT_ENABLED)) && Boolean.valueOf(cmsSiteService.getSiteConfig(IS_CHECKOUT_ENABLED)))) {
			return ControllerConstants.Views.Pages.Checkout.CheckoutPage;
		} else {
			return REDIRECT_TO_LOGIN_FOR_CHECKOUT;
		}
	}

	@RequestMapping(value = "/orderConfirmation/" + ORDER_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String orderConfirmation(@PathVariable("orderCode") final String orderCode, final HttpServletRequest request,
			final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		SessionOverrideCheckoutFlowFacade.resetSessionOverrides();
		return processOrderCode(orderCode, model, request, redirectModel);
	}


	@RequestMapping(value = "/orderConfirmation/" + ORDER_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.POST)
	public String orderConfirmation(final GuestRegisterForm form, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		getSessionService().removeAttribute("guestEmail");
		getGuestRegisterValidator().validate(form, bindingResult);
		return processRegisterGuestUserRequest(form, bindingResult, model, request, response, redirectModel);
	}

	protected String processRegisterGuestUserRequest(final GuestRegisterForm form, final BindingResult bindingResult,
			final Model model, final HttpServletRequest request, final HttpServletResponse response,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
			return processOrderCode(form.getOrderCode(), model, request, redirectModel);
		}
		try
		{
			getCustomerFacade().changeGuestToCustomer(form.getPwd(), form.getOrderCode());
			getAutoLoginStrategy().login(getCustomerFacade().getCurrentCustomer().getUid(), form.getPwd(), request, response);
			getSessionService().removeAttribute(WebConstants.ANONYMOUS_CHECKOUT);
		}
		catch (final DuplicateUidException e)
		{
			// User already exists
			LOG.warn("guest registration failed: " + e);
			model.addAttribute(new GuestRegisterForm());
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
					"guest.checkout.existingaccount.register.error", new Object[]
					{ form.getUid() });
			return REDIRECT_PREFIX + request.getHeader(REFERER);
		}

		// Consent form data
		try
		{
			final ConsentForm consentForm = form.getConsentForm();
			if (consentForm != null && consentForm.getConsentGiven())
			{
				getConsentFacade().giveConsent(consentForm.getConsentTemplateId(), consentForm.getConsentTemplateVersion());
			}
		}
		catch (final Exception e)
		{
			LOG.error("Error occurred while creating consents during registration", e);
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, CONSENT_FORM_GLOBAL_ERROR);
		}

		// save anonymous-consent cookies as ConsentData
		final Cookie cookie = WebUtils.getCookie(request, WebConstants.ANONYMOUS_CONSENT_COOKIE);
		if (cookie != null)
		{
			try
			{
				final ObjectMapper mapper = new ObjectMapper();
				final List<ConsentCookieData> consentCookieDataList = Arrays.asList(mapper.readValue(
						URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8.displayName()), ConsentCookieData[].class));
				consentCookieDataList.stream().filter(consentData -> WebConstants.CONSENT_GIVEN.equals(consentData.getConsentState()))
						.forEach(consentData -> consentFacade.giveConsent(consentData.getTemplateCode(),
								Integer.valueOf(consentData.getTemplateVersion())));
			}
			catch (final UnsupportedEncodingException e)
			{
				LOG.error(String.format("Cookie Data could not be decoded : %s", cookie.getValue()), e);
			}
			catch (final IOException e)
			{
				LOG.error("Cookie Data could not be mapped into the Object", e);
			}
			catch (final Exception e)
			{
				LOG.error("Error occurred while creating Anonymous cookie consents", e);
			}
		}

		customerConsentDataStrategy.populateCustomerConsentDataInSession();

		return REDIRECT_PREFIX + "/";
	}

	protected String processOrderCode(final String orderCode, final Model model, final HttpServletRequest request,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		model.addAttribute("pageType", PageType.ORDERCONFIRMATION.name());
		//Adding order code for clickstream
		final OrderData orderData = new OrderData();
		orderData.setCode(orderCode);
		model.addAttribute("orderData", orderData);
		model.addAttribute(GpcommerceCoreConstants.CONTACT_NUMBER, (cmsSiteService.getSiteConfig(GpcommerceCoreConstants.CONTACT_NUMBER)));
		final ContentPageModel contactUSCMSPage = getContentPageForLabelOrId(CHECKOUT_ORDER_CONFIRMATION_CMS_PAGE_LABEL);
		storeCmsPageInModel(model, contactUSCMSPage);
		setUpMetaDataForContentPage(model, contactUSCMSPage);
		getSessionService().removeAttribute("guestEmail");
		return ControllerConstants.Views.Pages.Checkout.CheckoutConfirmationPage;
	}

	protected void processEmailAddress(final Model model, final OrderData orderDetails)
	{
		final String uid;

		if (orderDetails.isGuestCustomer() && !model.containsAttribute(GUEST_REGISTER_FORM))
		{
			final GuestRegisterForm guestRegisterForm = new GuestRegisterForm();
			guestRegisterForm.setOrderCode(orderDetails.getGuid());
			uid = orderDetails.getPaymentInfo().getBillingAddress().getEmail();
			guestRegisterForm.setUid(uid);
			model.addAttribute(guestRegisterForm);
		}
		else
		{
			uid = orderDetails.getUser().getUid();
		}
		model.addAttribute("email", uid);
	}

	protected GuestRegisterValidator getGuestRegisterValidator()
	{
		return guestRegisterValidator;
	}

	protected AutoLoginStrategy getAutoLoginStrategy()
	{
		return autoLoginStrategy;
	}

	public CartService getCartService() {
		return cartService;
	}
}

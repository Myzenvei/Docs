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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.hybris.platform.acceleratorstorefrontcommons.strategy.CustomerConsentDataStrategy;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import de.hybris.platform.acceleratorfacades.ordergridform.OrderGridFormFacade;
import de.hybris.platform.acceleratorfacades.product.data.ReadOnlyOrderGridData;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.AddressForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdateEmailForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdatePasswordForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdateProfileForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.AddressValidator;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.EmailValidator;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.PasswordValidator;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.ProfileValidator;
import de.hybris.platform.acceleratorstorefrontcommons.forms.verification.AddressVerificationResultHandler;
import de.hybris.platform.acceleratorstorefrontcommons.util.AddressDataUtil;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.address.AddressVerificationFacade;
import de.hybris.platform.commercefacades.address.data.AddressVerificationResult;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.i18n.I18NFacade;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.TitleData;
import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.address.AddressVerificationDecision;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.util.ResponsiveUtils;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.util.Config;

import com.gp.commerce.facades.data.WishlistData;
import com.gp.commerce.facades.wishlist.GPWishlistFacade;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.storefront.controllers.ControllerConstants;


/**
 * Controller for home page
 */
@Controller
@RequestMapping("/my-account")
public class AccountPageController extends AbstractSearchPageController
{
	private static final String READ_ONLY_MULTI_D_MAP = "readOnlyMultiDMap";
	private static final String UPDATE_EMAIL_FORM = "updateEmailForm";
	private static final String CHK_EMAIL = "chkEmail";
	private static final String UPDATE_PROFILE_FORM = "updateProfileForm";
	private static final String UPDATE_PASSWORD_FORM = "updatePasswordForm";
	private static final String CURRENT_PASSWORD = "currentPassword";
	private static final String CHECK_NEW_PASSWORD = "checkNewPassword";
	private static final String CUSTOMER_DATA = "customerData";
	private static final String PAYMENT_INFO_DATA = "paymentInfoData";
	private static final String CONSENT_TEMPLATE_DATA_LIST = "consentTemplateDataList";
	private static final String TEXT_ACCOUNT_ADDRESS_BOOK = "text.account.addressBook";
	private static final String BREADCRUMBS_ATTR = "breadcrumbs";
	private static final String IS_DEFAULT_ADDRESS_ATTR = "isDefaultAddress";
	private static final String COUNTRY_DATA_ATTR = "countryData";
	private static final String ADDRESS_BOOK_EMPTY_ATTR = "addressBookEmpty";
	private static final String TITLE_DATA_ATTR = "titleData";
	private static final String FORM_GLOBAL_ERROR = "form.global.error";
	private static final String PROFILE_CURRENT_PASSWORD_INVALID = "profile.currentPassword.invalid";
	private static final String TEXT_ACCOUNT_PROFILE = "text.account.profile";
	private static final String ADDRESS_DATA_ATTR = "addressData";
	private static final String ADDRESS_FORM_ATTR = "addressForm";
	private static final String COUNTRY_ATTR = "country";
	private static final String REGIONS_ATTR = "regions";
	private static final String MY_ACCOUNT_ADDRESS_BOOK_URL = "/my-account/address-book";
	private static final String MY_ACCOUNT_LISTS_PAGE_URL = "/my-account/lists";
	private static final String ORDER_HISTORY_PAGE_URL = "/my-account/orders";
	private static final String TEXT_ACCOUNT_CONSENT_MANAGEMENT = "text.account.consent.consentManagement";
	private static final String TEXT_ACCOUNT_CONSENT_GIVEN = "text.account.consent.given";
	private static final String TEXT_ACCOUNT_CONSENT_WITHDRAWN = "text.account.consent.withdrawn";
	private static final String TEXT_ACCOUNT_CONSENT_NOT_FOUND = "text.account.consent.notFound";
	private static final String TEXT_ACCOUNT_CONSENT_TEMPLATE_NOT_FOUND = "text.account.consent.template.notFound";
	private static final String TEXT_ACCOUNT_CLOSE = "text.account.close";
	private static final String PRECURATED_LIST = "PRE_CURATED_LIST";
	private static final String USER_LIST= "USER_LIST";
	private static final String IS_PRECURATED = "isPrecuratedList";
	private static final String LAST_LINK_CLASS = "active";
	private static final String IS_SHARED_CART = "isSharedCart";

	// Internal Redirects
	private static final String REDIRECT_TO_ADDRESS_BOOK_PAGE = REDIRECT_PREFIX + MY_ACCOUNT_ADDRESS_BOOK_URL;
	private static final String REDIRECT_TO_PAYMENT_INFO_PAGE = REDIRECT_PREFIX + "/my-account/payment-details";
	private static final String REDIRECT_TO_EDIT_ADDRESS_PAGE = REDIRECT_PREFIX + "/my-account/edit-address/";
	private static final String REDIRECT_TO_UPDATE_EMAIL_PAGE = REDIRECT_PREFIX + "/my-account/update-email";
	private static final String REDIRECT_TO_UPDATE_PROFILE = REDIRECT_PREFIX + "/my-account/update-profile";
	private static final String REDIRECT_TO_PASSWORD_UPDATE_PAGE = REDIRECT_PREFIX + "/my-account/update-password";
	private static final String REDIRECT_TO_CONSENT_MANAGEMENT = REDIRECT_PREFIX + "/my-account/consents";
	
	private static final String GP_PROFILE_CMS_PAGE ="gpprofilepage";
	private static final String PAYMENT_CMS_PAGE ="gppaymentpage";
	private static final String LISTS_CMS_PAGE ="gplistspage";
	private static final String ADDRESSES_CMS_PAGE ="gpaddressespage";
	private static final String LISTDETAILS_CMS_PAGE ="gplistdetailspage";
	private static final String SUBSCRIPTIONS_CMS_PAGE = "gpSubscriptionspage";
	

	/**
	 * We use this suffix pattern because of an issue with Spring 3.1 where a Uri value is incorrectly extracted if it
	 * contains on or more '.' characters. Please see https://jira.springsource.org/browse/SPR-6164 for a discussion on
	 * the issue and future resolution.
	 */
	private static final String ORDER_CODE_PATH_VARIABLE_PATTERN = "{orderCode:.*}";
	private static final String ADDRESS_CODE_PATH_VARIABLE_PATTERN = "{addressCode:.*}";

	// CMS Pages
	private static final String ACCOUNT_CMS_PAGE = "account";
	private static final String UPDATE_PASSWORD_CMS_PAGE = "updatePassword";
	private static final String UPDATE_PROFILE_CMS_PAGE = "update-profile";
	private static final String UPDATE_EMAIL_CMS_PAGE = "update-email";
	private static final String ADDRESS_BOOK_CMS_PAGE = "address-book";
	private static final String ADD_EDIT_ADDRESS_CMS_PAGE = "add-edit-address";
	private static final String PAYMENT_DETAILS_CMS_PAGE = "payment-details";
	private static final String ORDER_HISTORY_CMS_PAGE = "orders";
	private static final String ORDER_DETAIL_CMS_PAGE = "order";
	private static final String CONSENT_MANAGEMENT_CMS_PAGE = "consents";
	private static final String CLOSE_ACCOUNT_CMS_PAGE = "close-account";
	private static final String SHARED_LIST = "isSharedList";
	private static final String SUPPORT_TICKETS_CMS_PAGE = "gpSupportTicketpage";
	private static final String ANONYMOUS_GUID = "guid_user";
	private static final String ANONYMOUS = "anonymous";
	private static final String IS_ANONYMOUS = "isAnonymous";
	
	private static final Logger LOG = Logger.getLogger(AccountPageController.class);

	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;

	@Resource(name = "acceleratorCheckoutFacade")
	private CheckoutFacade checkoutFacade;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Resource(name = "passwordValidator")
	private PasswordValidator passwordValidator;

	@Resource(name = "addressValidator")
	private AddressValidator addressValidator;

	@Resource(name = "profileValidator")
	private ProfileValidator profileValidator;

	@Resource(name = "emailValidator")
	private EmailValidator emailValidator;

	@Resource(name = "i18NFacade")
	private I18NFacade i18NFacade;

	@Resource(name = "addressVerificationFacade")
	private AddressVerificationFacade addressVerificationFacade;

	@Resource(name = "addressVerificationResultHandler")
	private AddressVerificationResultHandler addressVerificationResultHandler;

	@Resource(name = "orderGridFormFacade")
	private OrderGridFormFacade orderGridFormFacade;

	@Resource(name = "customerConsentDataStrategy")
	protected CustomerConsentDataStrategy customerConsentDataStrategy;

	@Resource(name = "addressDataUtil")
	private AddressDataUtil addressDataUtil;
	
	@Resource(name = "wishlistFacade")
	private GPWishlistFacade wishlistFacade;
	
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	
	protected GPWishlistFacade getGPWishlistFacade()
	{
		return wishlistFacade;
	}

	protected PasswordValidator getPasswordValidator()
	{
		return passwordValidator;
	}

	protected AddressValidator getAddressValidator()
	{
		return addressValidator;
	}

	protected ProfileValidator getProfileValidator()
	{
		return profileValidator;
	}

	protected EmailValidator getEmailValidator()
	{
		return emailValidator;
	}

	protected I18NFacade getI18NFacade()
	{
		return i18NFacade;
	}

	protected AddressVerificationFacade getAddressVerificationFacade()
	{
		return addressVerificationFacade;
	}

	protected AddressVerificationResultHandler getAddressVerificationResultHandler()
	{
		return addressVerificationResultHandler;
	}

	@ModelAttribute("countries")
	public Collection<CountryData> getCountries()
	{
		return checkoutFacade.getDeliveryCountries();
	}

	@ModelAttribute("titles")
	public Collection<TitleData> getTitles()
	{
		return userFacade.getTitles();
	}

	@ModelAttribute("countryDataMap")
	public Map<String, CountryData> getCountryDataMap()
	{
		final Map<String, CountryData> countryDataMap = new HashMap<>();
		for (final CountryData countryData : getCountries())
		{
			countryDataMap.put(countryData.getIsocode(), countryData);
		}
		return countryDataMap;
	}


	@RequestMapping(value = "/addressform", method = RequestMethod.GET)
	public String getCountryAddressForm(@RequestParam("addressCode") final String addressCode,
			@RequestParam("countryIsoCode") final String countryIsoCode, final Model model)
	{
		model.addAttribute("supportedCountries", getCountries());
		populateModelRegionAndCountry(model, countryIsoCode);

		final AddressForm addressForm = new AddressForm();
		model.addAttribute(ADDRESS_FORM_ATTR, addressForm);
		for (final AddressData addressData : userFacade.getAddressBook())
		{
			if (addressData.getId() != null && addressData.getId().equals(addressCode)
					&& countryIsoCode.equals(addressData.getCountry().getIsocode()))
			{
				model.addAttribute(ADDRESS_DATA_ATTR, addressData);
				addressDataUtil.convert(addressData, addressForm);
				break;
			}
		}
		return ControllerConstants.Views.Fragments.Account.CountryAddressForm;
	}

	protected void populateModelRegionAndCountry(final Model model, final String countryIsoCode)
	{
		model.addAttribute(REGIONS_ATTR, getI18NFacade().getRegionsForCountryIso(countryIsoCode));
		model.addAttribute(COUNTRY_ATTR, countryIsoCode);
	}

	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	public String account(final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		if (ResponsiveUtils.isResponsive())
		{
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, "system.error.page.not.found", null);
			return REDIRECT_PREFIX + "/";
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		model.addAttribute(BREADCRUMBS_ATTR, accountBreadcrumbBuilder.getBreadcrumbs(null));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	@RequireHardLogIn
	public String orders(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model)
			throws CMSItemNotFoundException
	{
		// Returns Order History Page
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_HISTORY_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_HISTORY_CMS_PAGE));
		return getViewForPage(model);
	}

	@RequestMapping(value = "/order/" + ORDER_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@RequireHardLogIn
	public String order(@PathVariable("orderCode") final String orderCode, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		// Returns Order Details Page
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_DETAIL_CMS_PAGE));
		final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb(ORDER_HISTORY_PAGE_URL, getMessageSource().getMessage("text.account.orderhistory", null,
                getI18nService().getCurrentLocale()), null));
		breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage("text.account.orderdetails", null,
                getI18nService().getCurrentLocale()), LAST_LINK_CLASS));
		model.addAttribute(BREADCRUMBS_ATTR, GPFunctions.convertToJSON(breadcrumbs));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_DETAIL_CMS_PAGE));
		model.addAttribute(GpcommerceCoreConstants.CONTACT_NUMBER, (cmsSiteService.getSiteConfig(GpcommerceCoreConstants.CONTACT_NUMBER)));
		return getViewForPage(model);
	}

	@RequestMapping(value = "/order/" + ORDER_CODE_PATH_VARIABLE_PATTERN + "/getReadOnlyProductVariantMatrix", method = RequestMethod.GET)
	@RequireHardLogIn
	public String getProductVariantMatrixForResponsive(@PathVariable("orderCode") final String orderCode,
			@RequestParam("productCode") final String productCode, final Model model)
	{
		final OrderData orderData = orderFacade.getOrderDetailsForCodeWithoutUser(orderCode);

		final Map<String, ReadOnlyOrderGridData> readOnlyMultiDMap = orderGridFormFacade.getReadOnlyOrderGridForProductInOrder(
				productCode, Arrays.asList(ProductOption.BASIC, ProductOption.CATEGORIES), orderData);
		model.addAttribute(READ_ONLY_MULTI_D_MAP, readOnlyMultiDMap);

		return ControllerConstants.Views.Fragments.Checkout.ReadOnlyExpandedOrderForm;
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	@RequireHardLogIn
	public String profile(final Model model) throws CMSItemNotFoundException
	{		
		storeCmsPageInModel(model, getContentPageForLabelOrId(GP_PROFILE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(GP_PROFILE_CMS_PAGE));
		model.addAttribute(BREADCRUMBS_ATTR, 
				GPFunctions.convertToJSON(accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_PROFILE)));

		return ControllerConstants.Views.Pages.Account.gpProfilePage;
	}

	protected TitleData findTitleForCode(final List<TitleData> titles, final String code)
	{
		if (code != null && !code.isEmpty() && titles != null && !titles.isEmpty())
		{
			for (final TitleData title : titles)
			{
				if (code.equals(title.getCode()))
				{
					return title;
				}
			}
		}
		return null;
	}

	@RequestMapping(value = "/update-email", method = RequestMethod.GET)
	@RequireHardLogIn
	public String editEmail(final Model model) throws CMSItemNotFoundException
	{
		final CustomerData customerData = customerFacade.getCurrentCustomer();
		final UpdateEmailForm updateEmailForm = new UpdateEmailForm();

		updateEmailForm.setEmail(customerData.getDisplayUid());

		model.addAttribute(UPDATE_EMAIL_FORM, updateEmailForm);

		storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_EMAIL_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_EMAIL_CMS_PAGE));
		model.addAttribute(BREADCRUMBS_ATTR, accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_PROFILE));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/update-email", method = RequestMethod.POST)
	@RequireHardLogIn
	public String updateEmail(final UpdateEmailForm updateEmailForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		getEmailValidator().validate(updateEmailForm, bindingResult);
		String returnAction = REDIRECT_TO_UPDATE_EMAIL_PAGE;

		if (!bindingResult.hasErrors() && !updateEmailForm.getEmail().equals(updateEmailForm.getChkEmail()))
		{
			bindingResult.rejectValue(CHK_EMAIL, "validation.checkEmail.equals", new Object[] {}, "validation.checkEmail.equals");
		}

		if (bindingResult.hasErrors())
		{
			returnAction = setErrorMessagesAndCMSPage(model, UPDATE_EMAIL_CMS_PAGE);
		}
		else
		{
			try
			{
				customerFacade.changeUid(updateEmailForm.getEmail(), updateEmailForm.getPassword());
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
						"text.account.profile.confirmationUpdated", null);

				// Replace the spring security authentication with the new UID
				final String newUid = customerFacade.getCurrentCustomer().getUid().toLowerCase();
				final Authentication oldAuthentication = SecurityContextHolder.getContext().getAuthentication();
				final UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(newUid, null,
						oldAuthentication.getAuthorities());
				newAuthentication.setDetails(oldAuthentication.getDetails());
				SecurityContextHolder.getContext().setAuthentication(newAuthentication);
			}
			catch (final DuplicateUidException e)
			{
				bindingResult.rejectValue("email", "profile.email.unique");
				returnAction = setErrorMessagesAndCMSPage(model, UPDATE_EMAIL_CMS_PAGE);
			}
			catch (final PasswordMismatchException passwordMismatchException)
			{
				bindingResult.rejectValue("password", PROFILE_CURRENT_PASSWORD_INVALID);
				returnAction = setErrorMessagesAndCMSPage(model, UPDATE_EMAIL_CMS_PAGE);
			}
		}

		return returnAction;
	}

	protected String setErrorMessagesAndCMSPage(final Model model, final String cmsPageLabelOrId) throws CMSItemNotFoundException
	{
		GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
		storeCmsPageInModel(model, getContentPageForLabelOrId(cmsPageLabelOrId));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(cmsPageLabelOrId));
		model.addAttribute(BREADCRUMBS_ATTR, accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_PROFILE));
		return getViewForPage(model);
	}


	@RequestMapping(value = "/update-profile", method = RequestMethod.GET)
	@RequireHardLogIn
	public String editProfile(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute(TITLE_DATA_ATTR, userFacade.getTitles());

		final CustomerData customerData = customerFacade.getCurrentCustomer();
		final UpdateProfileForm updateProfileForm = new UpdateProfileForm();

		updateProfileForm.setTitleCode(customerData.getTitleCode());
		updateProfileForm.setFirstName(customerData.getFirstName());
		updateProfileForm.setLastName(customerData.getLastName());

		model.addAttribute(UPDATE_PROFILE_FORM, updateProfileForm);

		storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));

		model.addAttribute(BREADCRUMBS_ATTR, accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_PROFILE));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/update-profile", method = RequestMethod.POST)
	@RequireHardLogIn
	public String updateProfile(final UpdateProfileForm updateProfileForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		getProfileValidator().validate(updateProfileForm, bindingResult);

		String returnAction = REDIRECT_TO_UPDATE_PROFILE;
		final CustomerData currentCustomerData = customerFacade.getCurrentCustomer();
		final CustomerData customerData = new CustomerData();
		customerData.setTitleCode(updateProfileForm.getTitleCode());
		customerData.setFirstName(updateProfileForm.getFirstName());
		customerData.setLastName(updateProfileForm.getLastName());
		customerData.setUid(currentCustomerData.getUid());
		customerData.setDisplayUid(currentCustomerData.getDisplayUid());

		model.addAttribute(TITLE_DATA_ATTR, userFacade.getTitles());

		storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));

		if (bindingResult.hasErrors())
		{
			returnAction = setErrorMessagesAndCMSPage(model, UPDATE_PROFILE_CMS_PAGE);
		}
		else
		{
			try
			{
				customerFacade.updateProfile(customerData);
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
						"text.account.profile.confirmationUpdated", null);

			}
			catch (final DuplicateUidException e)
			{
				bindingResult.rejectValue("email", "registration.error.account.exists.title");
				returnAction = setErrorMessagesAndCMSPage(model, UPDATE_PROFILE_CMS_PAGE);
			}
		}


		model.addAttribute(BREADCRUMBS_ATTR, accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_PROFILE));
		return returnAction;
	}

	@RequestMapping(value = "/update-password", method = RequestMethod.GET)
	@RequireHardLogIn
	public String updatePassword(final Model model) throws CMSItemNotFoundException
	{
		final UpdatePasswordForm updatePasswordForm = new UpdatePasswordForm();

		model.addAttribute(UPDATE_PASSWORD_FORM, updatePasswordForm);

		storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PASSWORD_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PASSWORD_CMS_PAGE));

		model.addAttribute(BREADCRUMBS_ATTR, accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile.updatePasswordForm"));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/update-password", method = RequestMethod.POST)
	@RequireHardLogIn
	public String updatePassword(final UpdatePasswordForm updatePasswordForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		getPasswordValidator().validate(updatePasswordForm, bindingResult);
		if (!bindingResult.hasErrors())
		{
			if (updatePasswordForm.getNewPassword().equals(updatePasswordForm.getCheckNewPassword()))
			{
				try
				{
					customerFacade.changePassword(updatePasswordForm.getCurrentPassword(), updatePasswordForm.getNewPassword());
				}
				catch (final PasswordMismatchException localException)
				{
					bindingResult.rejectValue(CURRENT_PASSWORD, PROFILE_CURRENT_PASSWORD_INVALID, new Object[] {},
							PROFILE_CURRENT_PASSWORD_INVALID);
				}
			}
			else
			{
				bindingResult.rejectValue(CHECK_NEW_PASSWORD, "validation.checkPwd.equals", new Object[] {},
						"validation.checkPwd.equals");
			}
		}

		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
			storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PASSWORD_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PASSWORD_CMS_PAGE));

			model.addAttribute(BREADCRUMBS_ATTR, accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile.updatePasswordForm"));
			return getViewForPage(model);
		}
		else
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
					"text.account.confirmation.password.updated", null);
			return REDIRECT_TO_PASSWORD_UPDATE_PAGE;
		}
	}

	@RequestMapping(value = "/address-book", method = RequestMethod.GET)
	@RequireHardLogIn
	public String getAddressBook(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute(ADDRESS_DATA_ATTR, userFacade.getAddressBook());

		storeCmsPageInModel(model, getContentPageForLabelOrId(ADDRESS_BOOK_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADDRESS_BOOK_CMS_PAGE));
		model.addAttribute(BREADCRUMBS_ATTR, accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_ADDRESS_BOOK));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/add-address", method = RequestMethod.GET)
	@RequireHardLogIn
	public String addAddress(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute(COUNTRY_DATA_ATTR, checkoutFacade.getDeliveryCountries());
		model.addAttribute(TITLE_DATA_ATTR, userFacade.getTitles());
		final AddressForm addressForm = getPreparedAddressForm();
		model.addAttribute(ADDRESS_FORM_ATTR, addressForm);
		model.addAttribute(ADDRESS_BOOK_EMPTY_ATTR, Boolean.valueOf(CollectionUtils.isEmpty(userFacade.getAddressBook())));
		model.addAttribute(IS_DEFAULT_ADDRESS_ATTR, Boolean.FALSE);
		storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));

		final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb(MY_ACCOUNT_ADDRESS_BOOK_URL, getMessageSource().getMessage(TEXT_ACCOUNT_ADDRESS_BOOK, null,
				getI18nService().getCurrentLocale()), null));
		breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage("text.account.addressBook.addEditAddress", null,
				getI18nService().getCurrentLocale()), null));
		model.addAttribute(BREADCRUMBS_ATTR, breadcrumbs);
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	protected AddressForm getPreparedAddressForm()
	{
		final CustomerData currentCustomerData = customerFacade.getCurrentCustomer();
		final AddressForm addressForm = new AddressForm();
		addressForm.setFirstName(currentCustomerData.getFirstName());
		addressForm.setLastName(currentCustomerData.getLastName());
		addressForm.setTitleCode(currentCustomerData.getTitleCode());
		return addressForm;
	}

	@RequestMapping(value = "/add-address", method = RequestMethod.POST)
	@RequireHardLogIn
	public String addAddress(final AddressForm addressForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		getAddressValidator().validate(addressForm, bindingResult);
		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
			storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			setUpAddressFormAfterError(addressForm, model);
			return getViewForPage(model);
		}

		final AddressData newAddress = addressDataUtil.convertToVisibleAddressData(addressForm);

		if (CollectionUtils.isEmpty(userFacade.getAddressBook()))
		{
			newAddress.setDefaultAddress(true);
		}
		else
		{
			newAddress.setDefaultAddress(addressForm.getDefaultAddress() != null && addressForm.getDefaultAddress().booleanValue());
		}

		final AddressVerificationResult<AddressVerificationDecision> verificationResult = getAddressVerificationFacade()
				.verifyAddressData(newAddress);
		final boolean addressRequiresReview = getAddressVerificationResultHandler().handleResult(verificationResult, newAddress,
				model, redirectModel, bindingResult, getAddressVerificationFacade().isCustomerAllowedToIgnoreAddressSuggestions(),
				"checkout.multi.address.added");

		populateModelRegionAndCountry(model, addressForm.getCountryIso());
		model.addAttribute("edit", Boolean.TRUE);
		model.addAttribute(IS_DEFAULT_ADDRESS_ATTR, Boolean.valueOf(userFacade.isDefaultAddress(addressForm.getAddressId())));

		if (addressRequiresReview)
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			return getViewForPage(model);
		}

		userFacade.addAddress(newAddress);


		GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, "account.confirmation.address.added",
				null);

		return REDIRECT_TO_EDIT_ADDRESS_PAGE + newAddress.getId();
	}

	protected void setUpAddressFormAfterError(final AddressForm addressForm, final Model model)
	{
		model.addAttribute(COUNTRY_DATA_ATTR, checkoutFacade.getDeliveryCountries());
		model.addAttribute(TITLE_DATA_ATTR, userFacade.getTitles());
		model.addAttribute(ADDRESS_BOOK_EMPTY_ATTR, Boolean.valueOf(CollectionUtils.isEmpty(userFacade.getAddressBook())));
		model.addAttribute(IS_DEFAULT_ADDRESS_ATTR, Boolean.valueOf(userFacade.isDefaultAddress(addressForm.getAddressId())));
		if (addressForm.getCountryIso() != null)
		{
			populateModelRegionAndCountry(model, addressForm.getCountryIso());
		}
	}

	@RequestMapping(value = "/edit-address/" + ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@RequireHardLogIn
	public String editAddress(@PathVariable("addressCode") final String addressCode, final Model model)
			throws CMSItemNotFoundException
	{
		final AddressForm addressForm = new AddressForm();
		model.addAttribute(COUNTRY_DATA_ATTR, checkoutFacade.getDeliveryCountries());
		model.addAttribute(TITLE_DATA_ATTR, userFacade.getTitles());
		model.addAttribute(ADDRESS_FORM_ATTR, addressForm);
		final List<AddressData> addressBook = userFacade.getAddressBook();
		model.addAttribute(ADDRESS_BOOK_EMPTY_ATTR, Boolean.valueOf(CollectionUtils.isEmpty(addressBook)));


		for (final AddressData addressData : addressBook)
		{
			if (addressData.getId() != null && addressData.getId().equals(addressCode))
			{
				model.addAttribute(REGIONS_ATTR, getI18NFacade().getRegionsForCountryIso(addressData.getCountry().getIsocode()));
				model.addAttribute(COUNTRY_ATTR, addressData.getCountry().getIsocode());
				model.addAttribute(ADDRESS_DATA_ATTR, addressData);
				addressDataUtil.convert(addressData, addressForm);

				if (userFacade.isDefaultAddress(addressData.getId()))
				{
					addressForm.setDefaultAddress(Boolean.TRUE);
					model.addAttribute(IS_DEFAULT_ADDRESS_ATTR, Boolean.TRUE);
				}
				else
				{
					addressForm.setDefaultAddress(Boolean.FALSE);
					model.addAttribute(IS_DEFAULT_ADDRESS_ATTR, Boolean.FALSE);
				}
				break;
			}
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));

		final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb(MY_ACCOUNT_ADDRESS_BOOK_URL, getMessageSource().getMessage(TEXT_ACCOUNT_ADDRESS_BOOK, null,
                getI18nService().getCurrentLocale()), null));
		breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage("text.account.addressBook.addEditAddress", null,
                getI18nService().getCurrentLocale()), null));
		model.addAttribute(BREADCRUMBS_ATTR, breadcrumbs);
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		model.addAttribute("edit", Boolean.TRUE);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/edit-address/" + ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.POST)
	@RequireHardLogIn
	public String editAddress(final AddressForm addressForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		getAddressValidator().validate(addressForm, bindingResult);
		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
			storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			setUpAddressFormAfterError(addressForm, model);
			return getViewForPage(model);
		}

		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);

		final AddressData newAddress = addressDataUtil.convertToVisibleAddressData(addressForm);

		if (Boolean.TRUE.equals(addressForm.getDefaultAddress()) || userFacade.getAddressBook().size() <= 1)
		{
			newAddress.setDefaultAddress(true);
		}

		final AddressVerificationResult<AddressVerificationDecision> verificationResult = getAddressVerificationFacade()
				.verifyAddressData(newAddress);
		final boolean addressRequiresReview = getAddressVerificationResultHandler().handleResult(verificationResult, newAddress,
				model, redirectModel, bindingResult, getAddressVerificationFacade().isCustomerAllowedToIgnoreAddressSuggestions(),
				"checkout.multi.address.updated");

		model.addAttribute(REGIONS_ATTR, getI18NFacade().getRegionsForCountryIso(addressForm.getCountryIso()));
		model.addAttribute(COUNTRY_ATTR, addressForm.getCountryIso());
		model.addAttribute("edit", Boolean.TRUE);
		model.addAttribute(IS_DEFAULT_ADDRESS_ATTR, Boolean.valueOf(userFacade.isDefaultAddress(addressForm.getAddressId())));

		if (addressRequiresReview)
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			return getViewForPage(model);
		}

		userFacade.editAddress(newAddress);

		GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, "account.confirmation.address.updated",
                null);
		return REDIRECT_TO_EDIT_ADDRESS_PAGE + newAddress.getId();
	}

	@RequestMapping(value = "/select-suggested-address", method = RequestMethod.POST)
	public String doSelectSuggestedAddress(final AddressForm addressForm, final RedirectAttributes redirectModel)
	{
		final Set<String> resolveCountryRegions = org.springframework.util.StringUtils
				.commaDelimitedListToSet(Config.getParameter("resolve.country.regions"));

		final AddressData selectedAddress = addressDataUtil.convertToVisibleAddressData(addressForm);

		final CountryData countryData = selectedAddress.getCountry();

		if (!resolveCountryRegions.contains(countryData.getIsocode()))
		{
			selectedAddress.setRegion(null);
		}

		if (Boolean.TRUE.equals(addressForm.getEditAddress()))
		{
			userFacade.editAddress(selectedAddress);
		}
		else
		{
			userFacade.addAddress(selectedAddress);
		}

		GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, "account.confirmation.address.added");

		return REDIRECT_TO_ADDRESS_BOOK_PAGE;
	}

	@RequestMapping(value = "/remove-address/" + ADDRESS_CODE_PATH_VARIABLE_PATTERN, method =
	{ RequestMethod.GET, RequestMethod.POST })
	@RequireHardLogIn
	public String removeAddress(@PathVariable("addressCode") final String addressCode, final RedirectAttributes redirectModel)
	{
		final AddressData addressData = new AddressData();
		addressData.setId(addressCode);
		userFacade.removeAddress(addressData);

		GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, "account.confirmation.address.removed");
		return REDIRECT_TO_ADDRESS_BOOK_PAGE;
	}

	@RequestMapping(value = "/set-default-address/" + ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@RequireHardLogIn
	public String setDefaultAddress(@PathVariable("addressCode") final String addressCode, final RedirectAttributes redirectModel)
	{
		final AddressData addressData = new AddressData();
		addressData.setDefaultAddress(true);
		addressData.setVisibleInAddressBook(true);
		addressData.setId(addressCode);
		userFacade.setDefaultAddress(addressData);
		GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
				"account.confirmation.default.address.changed");
		return REDIRECT_TO_ADDRESS_BOOK_PAGE;
	}

	@RequestMapping(value = "/payment-details", method = RequestMethod.GET)
	@RequireHardLogIn
	public String paymentDetails(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute(CUSTOMER_DATA, customerFacade.getCurrentCustomer());
		model.addAttribute(PAYMENT_INFO_DATA, userFacade.getCCPaymentInfos(true));
		storeCmsPageInModel(model, getContentPageForLabelOrId(PAYMENT_DETAILS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
		model.addAttribute(BREADCRUMBS_ATTR, accountBreadcrumbBuilder.getBreadcrumbs("text.account.paymentDetails"));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/set-default-payment-details", method = RequestMethod.POST)
	@RequireHardLogIn
	public String setDefaultPaymentDetails(@RequestParam final String paymentInfoId)
	{
		CCPaymentInfoData paymentInfoData = null;
		if (StringUtils.isNotBlank(paymentInfoId))
		{
			paymentInfoData = userFacade.getCCPaymentInfoForCode(paymentInfoId);
		}
		userFacade.setDefaultPaymentInfo(paymentInfoData);
		return REDIRECT_TO_PAYMENT_INFO_PAGE;
	}

	@RequestMapping(value = "/remove-payment-method", method = RequestMethod.POST)
	@RequireHardLogIn
	public String removePaymentMethod(@RequestParam(value = "paymentInfoId") final String paymentMethodId,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		userFacade.unlinkCCPaymentInfo(paymentMethodId);
		GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
                "text.account.profile.paymentCart.removed");
		return REDIRECT_TO_PAYMENT_INFO_PAGE;
	}

	@RequestMapping(value = "/consents", method = RequestMethod.GET)
	@RequireHardLogIn
	public String consentManagement(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute(CONSENT_TEMPLATE_DATA_LIST, getConsentFacade().getConsentTemplatesWithConsents());
		storeCmsPageInModel(model, getContentPageForLabelOrId(CONSENT_MANAGEMENT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CONSENT_MANAGEMENT_CMS_PAGE));
		model.addAttribute(BREADCRUMBS_ATTR, accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_CONSENT_MANAGEMENT));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/consents/give/{consentTemplateId}/{version}", method = RequestMethod.POST)
	@RequireHardLogIn
	public String giveConsent(@PathVariable final String consentTemplateId, @PathVariable final Integer version,
			final RedirectAttributes redirectModel)
	{
		try
		{
			getConsentFacade().giveConsent(consentTemplateId, version);
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, TEXT_ACCOUNT_CONSENT_GIVEN);
		}
		catch (final ModelNotFoundException | AmbiguousIdentifierException e)
		{
			LOG.warn(String.format("ConsentTemplate with code [%s] and version [%s] was not found", consentTemplateId, version), e);
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
					TEXT_ACCOUNT_CONSENT_TEMPLATE_NOT_FOUND, null);
		}
		customerConsentDataStrategy.populateCustomerConsentDataInSession();
		return REDIRECT_TO_CONSENT_MANAGEMENT;
	}

	@RequestMapping(value = "/consents/withdraw/{consentCode}", method = RequestMethod.POST)
	@RequireHardLogIn
	public String withdrawConsent(@PathVariable final String consentCode, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		try
		{
			getConsentFacade().withdrawConsent(consentCode);
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, TEXT_ACCOUNT_CONSENT_WITHDRAWN);
		}
		catch (final ModelNotFoundException e)
		{
			LOG.warn(String.format("Consent with code [%s] was not found", consentCode), e);
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, TEXT_ACCOUNT_CONSENT_NOT_FOUND,
					null);
		}
		customerConsentDataStrategy.populateCustomerConsentDataInSession();
		return REDIRECT_TO_CONSENT_MANAGEMENT;
	}

	@RequestMapping(value = "/close-account", method = RequestMethod.GET)
	@RequireHardLogIn
	public String showCloseAccountPage(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(CLOSE_ACCOUNT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CLOSE_ACCOUNT_CMS_PAGE));
		model.addAttribute(BREADCRUMBS_ATTR, accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_CLOSE));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/close-account", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@RequireHardLogIn
	public void closeAccount(final HttpServletRequest request) throws CMSItemNotFoundException, ServletException
	{
		customerFacade.closeAccount();
		request.logout();
	}
	
	@RequestMapping(value = "/paymentdetails", method = RequestMethod.GET)
	public String getPaymentDetails(final Model model)
			throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(PAYMENT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PAYMENT_CMS_PAGE));
		model.addAttribute(BREADCRUMBS_ATTR, 
				GPFunctions.convertToJSON(accountBreadcrumbBuilder.getBreadcrumbs("text.account.paymentdetails")));
		return ControllerConstants.Views.Pages.Account.gpPaymentPage;
	}
	
	
	@RequestMapping(value = "/addresses", method = RequestMethod.GET)
	public String getaddresses(final Model model)
			throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(ADDRESSES_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADDRESSES_CMS_PAGE));
		model.addAttribute(BREADCRUMBS_ATTR, 
				GPFunctions.convertToJSON(accountBreadcrumbBuilder.getBreadcrumbs("text.account.addresses")));
		
		return ControllerConstants.Views.Pages.Account.gpAddressesPage;
	}
	
	@RequestMapping(value = "/lists", method = RequestMethod.GET)
	public String getLists(final Model model)
			throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(LISTS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(LISTS_CMS_PAGE));
		model.addAttribute(BREADCRUMBS_ATTR, 
				GPFunctions.convertToJSON(accountBreadcrumbBuilder.getBreadcrumbs("text.account.lists")));

		return ControllerConstants.Views.Pages.Account.gpListsPage;
	}
	
	@RequestMapping(value = "/listdetails", method = RequestMethod.GET)
	public String getListDetails(final Model model,@RequestParam(required = true) final String listName, 
			final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(LISTDETAILS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(LISTDETAILS_CMS_PAGE));
		WishlistData wishlistData = new WishlistData();
		wishlistData = wishlistFacade.getPrecuratedlist(listName);
		if(null != wishlistData && StringUtils.isNotEmpty(wishlistData.getWishlistType()) && !USER_LIST.equalsIgnoreCase(wishlistData.getWishlistType()) && PRECURATED_LIST.equalsIgnoreCase(wishlistData.getWishlistType())){
		model.addAttribute(IS_PRECURATED, Boolean.TRUE);
		}else {
			model.addAttribute(IS_PRECURATED, Boolean.FALSE);
		}
		model.addAttribute(SHARED_LIST, Boolean.FALSE);
		model.addAttribute(IS_SHARED_CART, Boolean.FALSE);
		final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb(MY_ACCOUNT_LISTS_PAGE_URL, getMessageSource().getMessage("text.account.alllists", null,
                getI18nService().getCurrentLocale()), null));
		breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage("text.account.listdetails", null,
                getI18nService().getCurrentLocale()), LAST_LINK_CLASS));
		model.addAttribute(BREADCRUMBS_ATTR, GPFunctions.convertToJSON(breadcrumbs));
		boolean isAnonymous = false;
		final Cookie anonymousCookie = WebUtils.getCookie(request, ANONYMOUS_GUID);
		if(ANONYMOUS.equalsIgnoreCase(anonymousCookie.getValue())) {
			isAnonymous = true;
		}
		model.addAttribute(IS_ANONYMOUS, isAnonymous);
		return ControllerConstants.Views.Pages.Account.gpListDetailsPage;
	}
	
	@RequestMapping(value = "/supportTickets", method = RequestMethod.GET)
	public String getSupportTicket(final Model model)
			throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(SUPPORT_TICKETS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(SUPPORT_TICKETS_CMS_PAGE));

		return ControllerConstants.Views.Pages.Account.gpSupportTicketPage;
	}
	
	/**
	 * Navigates to subscription page
	 * @param model
	 * @return
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/subscriptions", method = RequestMethod.GET)
	@RequireHardLogIn
	public String getSubscriptions(final Model model)
			throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(SUBSCRIPTIONS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(SUBSCRIPTIONS_CMS_PAGE));

		return ControllerConstants.Views.Pages.Account.gpSubscriptionspage;
	}

}
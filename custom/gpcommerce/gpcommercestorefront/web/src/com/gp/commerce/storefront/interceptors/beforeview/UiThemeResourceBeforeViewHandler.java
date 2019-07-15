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
package com.gp.commerce.storefront.interceptors.beforeview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;

import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gp.commerce.storefront.web.view.UiExperienceViewResolver;
import com.gp.hybris.site.GPSiteConfigData;
import com.gpintegration.constants.GpintegrationConstants;

import de.hybris.platform.acceleratorfacades.device.DeviceDetectionFacade;
import de.hybris.platform.acceleratorfacades.device.data.DeviceData;
import de.hybris.platform.acceleratorservices.addonsupport.RequiredAddOnsNameProvider;
import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.interceptors.BeforeViewHandler;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.navigation.data.NavigationUrlData;
import de.hybris.platform.commercefacades.oauth.data.ApplicationAuthParams;
import de.hybris.platform.commercefacades.thirdpartyapps.data.AppData;
import de.hybris.platform.commercefacades.thirdpartyapps.data.LiveAgentData;
import de.hybris.platform.commercefacades.thirdpartyapps.data.ThirdPartyAppsData;
import de.hybris.platform.commerceservices.enums.SiteTheme;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.util.Config;
import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;

/**
 * Interceptor to setup the paths to the UI resource paths in the model before passing it to the view. Sets up the path
 * to the web accessible UI resources for the following: * The current site * The current theme * The common resources
 * All of these paths are qualified by the current UiExperienceLevel
 */
public class UiThemeResourceBeforeViewHandler implements BeforeViewHandler
{
	private static final String SHOW_VARIANT_PRODUCTS = "showVariantProducts";

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(UiThemeResourceBeforeViewHandler.class);

	protected static final String COMMON = "common";
	protected static final String SHARED = "shared";
	protected static final String RESOURCE_TYPE_JAVASCRIPT = "javascript";
	protected static final String RESOURCE_TYPE_CSS = "css";
	protected static final String ROOT_CATEGORY = "root.category";
	protected static final String GOOGLE_MAPS_API_KEY = "google.maps.api.key";


	private static final String PATHS = ".paths.";
	private static final String IS_SOCIAL_ENABLED = "isSocialEnabled";
	private static final String IS_SOCIAL_SHARE_FB_ENABLED = "isSocialShareforFBEnabled";
	private static final String IS_SOCIAL_SHARE_TWITTER_ENABLED = "isSocialShareForTwitterEnabled";
	private static final String IS_SOCIAL_SHARE_LINKEDIN_ENABLED = "isSocialShareForLinkedInEnabled";
	private static final String IS_SOCIAL_SHARE_PINTEREST_ENABLED = "isSocialShareForPinterestEnabled";
	private static final String GTMID = "GTMID";
	protected static final String SHOW_TAX_EXEMPTION="showTaxExemption";
	protected static final String SHOW_COMPANY_LINKS="showCompanyLinks";
	protected static final String SHOW_LIVE_CHAT="showLiveChat";
	protected static final String SHOW_SOCIAL_LOGIN="showSocialLogin";
	protected static final String IS_CHECKOUT_ENABLED="isCheckoutEnabled";
	protected static final String IS_BAZAARVOICE_ENABLED="isBazaarVoiceEnabled";
	protected static final String SHOW_PAYPAL="showPaypal";
	protected static final String PAYPAL_ENV="paypalEnv";
	protected static final String KRUX_ENABLE_SITES="gp.krux.sites.enable";
	protected static final String MARDIGRAS_CONTACT_US_EMAIL = "mardigras.contactus.email";
	protected static final String MARDIGRAS_SITE = "mardigras";
	private static final String LIVE_AGENT_BUTTON_ID = "live.agent.button.id";
	private static final String LIVE_AGENT_DEPLOYMENT_ID = "live.agent.deployment.id";
	private static final String LIVE_AGENT_ORG_ID = "live.agent.org.id";
	private static final String LIVE_AGENT_ENDPOINT = "live.agent.endpoint";
	private static final String LIVECHAT_DEPLOY_DEFAULT_URL = "https://c.la1-c2cs-iad.salesforceliveagent.com/content/g/js/43.0/deployment.js";
	private static final String LIVECHAT_DEPLOY_URL = "livechatDeployUrl";
	protected static final String SHOW_DOWNLOAD="showDownload";
	protected static final String IS_RELATED_ENABLED="isRelatedEnabled";	
	public static final String IS_CONTENT_INDEXING_ENABLED = "isContentIndexingEnabled";
	protected static final String COLORCODE_KEY="gpb2bstorefront.pdf.download.colorCodes";
	protected static final String COLORCODES="colorCodes";
	protected static final String IS_BULK_ENABLED="isBulkEnabled";
	protected static final String SINGLE_PRODUCT_ENABLED="singleProductEnabled";		
	protected static final String IS_SHARABLE="isSharable";
	protected static final String IS_DOWNLOADABLE="isDownloadable";
	protected static final String IS_EXCELDOWNLOADABLE="isExcelDownloadable";
	protected static final String ADD_PRODUCTS="isAddProduct";
	protected static final String IS_DOB_ENABLED="isDOBEnabled";
	protected static final String IS_GENDER_ENABLED="isGenderEnabled";
	protected static final String SHOW_CUSTOMTABS="showCustomTabs";
	private static final String GP_SITE_CONFIG = "gpSiteConfig";
	private static final String ENV_PAYPAL = "gp.paypal.checkoutjs.env";
	private static final String ASM_AGENT_LOGGED_IN="isAsmAgentLoggedIn";
	private static final String ASM_MODE_LAUNCHED="isAsmModeLaunched";
	private static final String IS_CATEGORY_DESCRIPTION_ENABLED = "isCategoryDescriptionEnabled";


	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;

	@Resource(name = "uiExperienceService")
	private UiExperienceService uiExperienceService;

	@Resource(name = "deviceDetectionFacade")
	private DeviceDetectionFacade deviceDetectionFacade;

	@Resource(name = "siteConfigService")
	private SiteConfigService siteConfigService;

	@Resource(name = "reqAddOnsNameProvider")
	private RequiredAddOnsNameProvider requiredAddOnsNameProvider;

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	@Resource(name = "viewResolver")
	private UiExperienceViewResolver uiExperienceViewResolver;

	@Resource(name = "assistedServiceFacade")
	private AssistedServiceFacade assistedServiceFacade;

	private String defaultThemeName;

	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
	{
		final CMSSiteModel currentSite = cmsSiteService.getCurrentSite();

		boolean isFavoritesEnabled = false;
		final String isFavoritesConfigured = cmsSiteService.getSiteConfig("isFavoritesEnabled");
		if(Boolean.TRUE.equals(Boolean.valueOf(isFavoritesConfigured))){
			isFavoritesEnabled = true;
		}

		final String siteName = currentSite.getUid();
		final String themeName = getThemeNameForSite(currentSite);
		final String uiExperienceCode = uiExperienceService.getUiExperienceLevel().getCode();
		final String uiExperienceCodeLower = uiExperienceViewResolver.getUiExperienceViewPrefix().isEmpty()
				? uiExperienceCode.toLowerCase()
				: StringUtils.remove(
						uiExperienceViewResolver.getUiExperienceViewPrefix().get(uiExperienceService.getUiExperienceLevel()), "/");
		final Object urlEncodingAttributes = request.getAttribute(WebConstants.URL_ENCODING_ATTRIBUTES);
		final String contextPath = StringUtils.remove(request.getContextPath(),
				urlEncodingAttributes != null ? urlEncodingAttributes.toString() : "");

		final String siteRootUrl = contextPath + "/_ui/" + uiExperienceCodeLower;
		final String sharedResourcePath = contextPath + "/_ui/" + SHARED;
		final String siteResourcePath = siteRootUrl + "/site-" + siteName;
		final String themeResourcePath = siteRootUrl + "/theme-" + themeName;
		final String commonResourcePath = siteRootUrl + "/" + COMMON;
		final String encodedContextPath = request.getContextPath();
		final LanguageModel currentLanguage = commerceCommonI18NService.getCurrentLanguage();
        final String gtmId = cmsSiteService.getSiteConfig(GTMID);

		modelAndView.addObject("contextPath", contextPath);
		modelAndView.addObject("sharedResourcePath", sharedResourcePath);
		modelAndView.addObject("siteResourcePath", siteResourcePath);
		modelAndView.addObject("themeResourcePath", themeResourcePath);
		modelAndView.addObject("commonResourcePath", commonResourcePath);
		modelAndView.addObject("encodedContextPath", encodedContextPath);
		modelAndView.addObject("siteRootUrl", siteRootUrl);
		modelAndView.addObject("language", currentLanguage != null ? currentLanguage.getIsocode() : "en");
		modelAndView.addObject("themeName", themeName);
		modelAndView.addObject("uiExperienceLevel", uiExperienceCode);
		modelAndView.addObject("rootCategory", cmsSiteService.getSiteConfig(ROOT_CATEGORY));
		modelAndView.addObject("gtmId", gtmId);
		modelAndView.addObject("googleMapsApiKey", cmsSiteService.getSiteConfig(GOOGLE_MAPS_API_KEY));
		modelAndView.addObject("kruxEnabledSites", Config.getParameter(KRUX_ENABLE_SITES));
		modelAndView.addObject("isBazaarVoiceEnabled", null!=cmsSiteService.getSiteConfig(IS_BAZAARVOICE_ENABLED)?cmsSiteService.getSiteConfig(IS_BAZAARVOICE_ENABLED):false);
		modelAndView.addObject("showDownload", null!=cmsSiteService.getSiteConfig(SHOW_DOWNLOAD)?cmsSiteService.getSiteConfig(SHOW_DOWNLOAD):false);
		boolean isRelatedEnabled = null!=cmsSiteService.getSiteConfig(IS_RELATED_ENABLED) ? Boolean.valueOf(cmsSiteService.getSiteConfig(IS_RELATED_ENABLED)):false;
 		modelAndView.addObject(IS_RELATED_ENABLED, isRelatedEnabled);
		boolean singleProductEnabled = null!=cmsSiteService.getSiteConfig(SINGLE_PRODUCT_ENABLED) ? Boolean.valueOf(cmsSiteService.getSiteConfig(SINGLE_PRODUCT_ENABLED)):false;
 		modelAndView.addObject(SINGLE_PRODUCT_ENABLED, singleProductEnabled);
		boolean isBulkEnabled = null!=cmsSiteService.getSiteConfig(IS_BULK_ENABLED) ? Boolean.valueOf(cmsSiteService.getSiteConfig(IS_BULK_ENABLED)):false;
 		modelAndView.addObject(IS_BULK_ENABLED, isBulkEnabled);
 		modelAndView.addObject("showCustomTabs", null!=cmsSiteService.getSiteConfig(SHOW_CUSTOMTABS)?cmsSiteService.getSiteConfig(SHOW_CUSTOMTABS):false);
		modelAndView.addObject("isCategoryDescriptionEnabled", null!=cmsSiteService.getSiteConfig(IS_CATEGORY_DESCRIPTION_ENABLED)?cmsSiteService.getSiteConfig(IS_CATEGORY_DESCRIPTION_ENABLED):false);

		 
		if (currentSite.getUid().equalsIgnoreCase(MARDIGRAS_SITE))
		{
			modelAndView.addObject("contactUsEmail", cmsSiteService.getSiteConfig(MARDIGRAS_CONTACT_US_EMAIL));
		}
		if (null != currentSite.getDefaultCatalog())
		{
			modelAndView.addObject("productCatalog", currentSite.getDefaultCatalog().getId());
		}
		modelAndView.addObject("isFavoritesEnabled", isFavoritesEnabled);
		final String detectedUiExperienceCode = uiExperienceService.getDetectedUiExperienceLevel().getCode();
		modelAndView.addObject("detectedUiExperienceCode", detectedUiExperienceCode);

		final UiExperienceLevel overrideUiExperienceLevel = uiExperienceService.getOverrideUiExperienceLevel();
		if (overrideUiExperienceLevel == null)
		{
			modelAndView.addObject("uiExperienceOverride", Boolean.FALSE);
		}
		else
		{
			modelAndView.addObject("uiExperienceOverride", Boolean.TRUE);
			modelAndView.addObject("overrideUiExperienceCode", overrideUiExperienceLevel.getCode());
		}

		final DeviceData currentDetectedDevice = deviceDetectionFacade.getCurrentDetectedDevice();
		modelAndView.addObject("detectedDevice", currentDetectedDevice);

		final List<String> dependantAddOns = requiredAddOnsNameProvider
				.getAddOns(request.getServletContext().getServletContextName());

		modelAndView.addObject("addOnCommonCssPaths", getAddOnCommonCSSPaths(contextPath, uiExperienceCodeLower, dependantAddOns));
		modelAndView.addObject("addOnThemeCssPaths",
				getAddOnThemeCSSPaths(contextPath, themeName, uiExperienceCodeLower, dependantAddOns));
		modelAndView.addObject("addOnJavaScriptPaths",
				getAddOnJSPaths(contextPath, siteName, uiExperienceCodeLower, dependantAddOns));
		modelAndView.addObject("navigations", GPFunctions.convertObjectToJSON(getNavigationUrls()));
		modelAndView.addObject("thirdPartyApps", GPFunctions.convertObjectToJSON(getThirdPartyAppData()));
		modelAndView.addObject("socialFlagsMap", GPFunctions.convertObjectToJSON(getSocialFlagsMap()));
		modelAndView.addObject("applicationAuthParams",GPFunctions.convertObjectToJSON(getApplicationAuthParams()));
		
		boolean isSharable = null!=cmsSiteService.getSiteConfig(IS_SHARABLE) ? Boolean.valueOf(cmsSiteService.getSiteConfig(IS_SHARABLE)):false;
		modelAndView.addObject(IS_SHARABLE, isSharable);		
		boolean isDownloadable = null!=cmsSiteService.getSiteConfig(IS_DOWNLOADABLE) ? Boolean.valueOf(cmsSiteService.getSiteConfig(IS_DOWNLOADABLE)):false;
		modelAndView.addObject(IS_DOWNLOADABLE, isDownloadable);		
		boolean isExcelDownloadable = null!=cmsSiteService.getSiteConfig(IS_EXCELDOWNLOADABLE) ? Boolean.valueOf(cmsSiteService.getSiteConfig(IS_EXCELDOWNLOADABLE)):false;
		modelAndView.addObject(IS_EXCELDOWNLOADABLE, isExcelDownloadable);
		boolean isAddProduct = null!=cmsSiteService.getSiteConfig(ADD_PRODUCTS) ? Boolean.valueOf(cmsSiteService.getSiteConfig(ADD_PRODUCTS)):false;
		modelAndView.addObject(ADD_PRODUCTS, isAddProduct);	

		// Favicon Image Url
		modelAndView.addObject("faviconImageUrl", currentSite.getFaviconUrl());
		//tax exemption will be false
		modelAndView.addObject(SHOW_TAX_EXEMPTION, false);

		//never show company section in B2C site
		modelAndView.addObject(SHOW_COMPANY_LINKS, false);
		if(StringUtils.isNotEmpty(cmsSiteService.getSiteConfig(SHOW_LIVE_CHAT)) && Boolean.valueOf(cmsSiteService.getSiteConfig(SHOW_LIVE_CHAT))) {
			modelAndView.addObject(SHOW_LIVE_CHAT, true);
		}else {
			modelAndView.addObject(SHOW_LIVE_CHAT, false);
		}
		if(StringUtils.isNotEmpty(cmsSiteService.getSiteConfig(SHOW_SOCIAL_LOGIN)) && Boolean.valueOf(cmsSiteService.getSiteConfig(SHOW_SOCIAL_LOGIN))) {
		modelAndView.addObject(SHOW_SOCIAL_LOGIN, true);
	}else {
		modelAndView.addObject(SHOW_SOCIAL_LOGIN, false);
	}

	if(StringUtils.isNotEmpty(cmsSiteService.getSiteConfig(IS_CHECKOUT_ENABLED)) && Boolean.valueOf(cmsSiteService.getSiteConfig(IS_CHECKOUT_ENABLED))) {
			modelAndView.addObject(IS_CHECKOUT_ENABLED, true);
		}
	boolean showPayPal = null!=cmsSiteService.getSiteConfig(SHOW_PAYPAL) ? Boolean.valueOf(cmsSiteService.getSiteConfig(SHOW_PAYPAL)):false;
	modelAndView.addObject(SHOW_PAYPAL, showPayPal);
	String getPayPalEnv = Config.getString(ENV_PAYPAL, "sandbox");
	modelAndView.addObject(PAYPAL_ENV, getPayPalEnv);
	modelAndView.addObject(IS_CONTENT_INDEXING_ENABLED, Boolean.valueOf(cmsSiteService.getSiteConfig(IS_CONTENT_INDEXING_ENABLED)));

	modelAndView.addObject(COLORCODES,GPFunctions.convertObjectToJSON(getColorCodeMap()));
	
	boolean isDOBEnabled = null !=cmsSiteService.getSiteConfig(IS_DOB_ENABLED) ?Boolean.valueOf(cmsSiteService.getSiteConfig(IS_DOB_ENABLED)) :false;
	modelAndView.addObject(IS_DOB_ENABLED, isDOBEnabled);
	boolean isGenderEnabled = null !=cmsSiteService.getSiteConfig(IS_GENDER_ENABLED) ?Boolean.valueOf(cmsSiteService.getSiteConfig(IS_GENDER_ENABLED)) :false;
	modelAndView.addObject(IS_GENDER_ENABLED, isGenderEnabled);

	modelAndView.addObject(LIVECHAT_DEPLOY_URL, null!=cmsSiteService.getSiteConfig(LIVECHAT_DEPLOY_URL)?cmsSiteService.getSiteConfig(LIVECHAT_DEPLOY_URL):LIVECHAT_DEPLOY_DEFAULT_URL);
	modelAndView.addObject(SHOW_VARIANT_PRODUCTS, Boolean.valueOf(cmsSiteService.getSiteConfig(SHOW_VARIANT_PRODUCTS)));

    
	boolean isAsmAgentLoggedIn = assistedServiceFacade.isAssistedServiceAgentLoggedIn();
	boolean isAsmModeLaunched = assistedServiceFacade.isAssistedServiceModeLaunched();
	modelAndView.addObject(ASM_AGENT_LOGGED_IN, isAsmAgentLoggedIn);
	modelAndView.addObject(ASM_MODE_LAUNCHED, isAsmModeLaunched);
		
	modelAndView.addObject(GP_SITE_CONFIG, GPFunctions.convertObjectToJSON(getGPSiteConfigs()));
	
	modelAndView.addObject("applePayMerchantName", Config.getParameter(GpcommerceFacadesConstants.APPLE_PAY_MERCHANT_NAME+currentSite.getUid()));
	
	}
	
	/** This method is used to retrieve color code that are to be
	 * displayed in modal windows for download
	 * @return HashMap<String,String>
	 */
	private HashMap<String,String> getColorCodeMap(){
		String colorCodes = Config.getParameter(COLORCODE_KEY);
		
		String[] colorCodesArray = colorCodes.split(GpintegrationConstants.SEMI_COLON);
		HashMap<String,String> colorCodeMap= new HashMap<String,String>(); 
		String[] codesArray = null;
		for (String code : colorCodesArray) {
			codesArray = code.split(GpcommerceFacadesConstants.COMMA);
			colorCodeMap.put(codesArray[0], codesArray[1]);
		}
		return colorCodeMap;
	}

	private NavigationUrlData getNavigationUrls()
	{
		final NavigationUrlData navigations = new NavigationUrlData();

		navigations.setEmpty(GpcommerceFacadesConstants.EMPTY_PAGE_URL);
		navigations.setCart(GpcommerceFacadesConstants.CART_PAGE_URL);
		navigations.setAddAddress(GpcommerceFacadesConstants.ADD_ADDRESS_PAGE_URL);
		navigations.setCheckout(GpcommerceFacadesConstants.CHECKOUT_PAGE_URL);
		navigations.setCompareProducts(GpcommerceFacadesConstants.COMPARE_PRODUCTS_PAGE_URL);
		navigations.setContactUs(GpcommerceFacadesConstants.CONTACT_US_PAGE_URL);
		navigations.setHome(GpcommerceFacadesConstants.HOME_PAGE_URL);
		navigations.setLogin(GpcommerceFacadesConstants.LOGIN_PAGE_URL);
		navigations.setLogout(GpcommerceFacadesConstants.LOGOUT_PAGE_URL);
		navigations.setOrderConfirmation(GpcommerceFacadesConstants.ORDER_CONFIRMATION_PAGE_URL);
		navigations.setPdpPage(GpcommerceFacadesConstants.PDP_PAGE_URL);
		navigations.setQuickOrder(GpcommerceFacadesConstants.QUICK_ORDER_PAGE_URL);
		navigations.setRegister(GpcommerceFacadesConstants.REGISTER_PAGE_URL);
		navigations.setSearchResults(GpcommerceFacadesConstants.SEARCH_RESULTS_PAGE_URL);
		navigations.setBusinessUnitsLanding(GpcommerceFacadesConstants.BUSINESS_UNITS_LANDING_PAGE_URL);
		navigations.setPermissionLanding(GpcommerceFacadesConstants.PERMISSION_LANDING_PAGE_URL);
		navigations.setUserGroupLanding(GpcommerceFacadesConstants.USER_GROUP_LANDING_PAGE_URL);
		navigations.setUserDetails(GpcommerceFacadesConstants.USER_DETAILS_PAGE_URL);
		navigations.setUserGroups(GpcommerceFacadesConstants.USER_GROUPS_PAGE_URL);
		navigations.setUserLanding(GpcommerceFacadesConstants.USER_LANDING_PAGE_URL);
		navigations.setOrderDetails(GpcommerceFacadesConstants.ORDER_DETAILS_PAGE_URL);
		navigations.setTermsCondition(GpcommerceFacadesConstants.TERMS_CONDITIONS);
		navigations.setCludo(GpcommerceFacadesConstants.CLUDO);
		navigations.setMyFav(GpcommerceFacadesConstants.FAV_DETAILS_PAGE_URL);
		navigations.setMyList(GpcommerceFacadesConstants.MY_LIST_DETAILS_PAGE_URL);
		navigations.setSupportTickets(GpcommerceFacadesConstants.SUPPORT_TICKETS_PAGE_URL);
		navigations.setListDetails(GpcommerceFacadesConstants.LIST_DETAILS_PAGE_URL);
		navigations.setLearnMore(GpcommerceFacadesConstants.LEARN_MORE_URL);
		navigations.setSubscriptionLanding(GpcommerceFacadesConstants.SUBSCRIPTION_LANDING);
		navigations.setBundlePage(GpcommerceFacadesConstants.BUNDLE_PAGE);
		return navigations;
	}

	private ThirdPartyAppsData getThirdPartyAppData()
	{
		final ThirdPartyAppsData thirdPartyAppsData = new ThirdPartyAppsData();
		final AppData gooleAppData = new AppData();
		final AppData facebookAppData = new AppData();
		final LiveAgentData agentData = new LiveAgentData();
		final LiveAgentData yformsAppData = new LiveAgentData();
		final LiveAgentData qplesData = new LiveAgentData();

		gooleAppData.setAppId(cmsSiteService.getSiteConfig(GpcommerceFacadesConstants.GOOGLE_APP_ID));
		gooleAppData.setMapsKey(Config.getParameter(GpcommerceFacadesConstants.GOOGLE_MAPS_KEY));
		facebookAppData.setAppId(cmsSiteService.getSiteConfig(GpcommerceFacadesConstants.FACEBOOK_APP_ID));
		agentData.setEndPoint(cmsSiteService.getSiteConfig(LIVE_AGENT_ENDPOINT));
		agentData.setOrgId(cmsSiteService.getSiteConfig(LIVE_AGENT_ORG_ID));
		agentData.setDeploymentId(cmsSiteService.getSiteConfig(LIVE_AGENT_DEPLOYMENT_ID));
		agentData.setButtonId(cmsSiteService.getSiteConfig(LIVE_AGENT_BUTTON_ID));
		yformsAppData.setEndPoint(Config.getParameter(GpcommerceFacadesConstants.YFORMS_ENDPOINT));
		qplesData.setEndPoint(Config.getParameter(GpcommerceFacadesConstants.QPLES_ENDPOINT));


		thirdPartyAppsData.setGoogle(gooleAppData);
		thirdPartyAppsData.setFacebook(facebookAppData);
		thirdPartyAppsData.setLiveAgent(agentData);
		thirdPartyAppsData.setYforms(yformsAppData);
		thirdPartyAppsData.setQples(qplesData);
		

		return thirdPartyAppsData;
	}

	private ApplicationAuthParams getApplicationAuthParams() {
		final ApplicationAuthParams params = new ApplicationAuthParams();
		params.setClient_id(Config.getParameter(GpcommerceFacadesConstants.AUTH_PARAMS_CLIENT_ID));
		params.setClient_secret(Config.getParameter(GpcommerceFacadesConstants.AUTH_PARAMS_CLIENT_SECRET));
		params.setGrant_type(Config.getParameter(GpcommerceFacadesConstants.AUTH_PARAMS_GRANT_TYPE));
		return params;
	}

	protected List getAddOnCommonCSSPaths(final String contextPath, final String uiExperience, final List<String> addOnNames)
	{
		final String[] propertyNames = new String[]
		{ RESOURCE_TYPE_CSS + ".paths", //
				RESOURCE_TYPE_CSS + PATHS + uiExperience //
		};

		return getAddOnResourcePaths(contextPath, addOnNames, propertyNames);
	}

	protected List getAddOnThemeCSSPaths(final String contextPath, final String themeName, final String uiExperience,
			final List<String> addOnNames)
	{
		final String[] propertyNames = new String[]
		{ RESOURCE_TYPE_CSS + PATHS + uiExperience + "." + themeName };

		return getAddOnResourcePaths(contextPath, addOnNames, propertyNames);
	}

	protected List getAddOnJSPaths(final String contextPath, final String siteName, final String uiExperience,
			final List<String> addOnNames)
	{
		final String[] propertyNames = new String[]
		{ RESOURCE_TYPE_JAVASCRIPT + ".paths", //
				RESOURCE_TYPE_JAVASCRIPT + PATHS + uiExperience //
		};

		return getAddOnResourcePaths(contextPath, addOnNames, propertyNames);
	}


	protected List getAddOnResourcePaths(final String contextPath, final List<String> addOnNames, final String[] propertyNames)
	{
		final List<String> addOnResourcePaths = new ArrayList<String>();

		for (final String addon : addOnNames)
		{
			for (final String propertyName : propertyNames)
			{
				addAddOnResourcePaths(contextPath, addOnResourcePaths, addon, propertyName);
			}
		}
		return addOnResourcePaths;
	}

	protected void addAddOnResourcePaths(final String contextPath, final List<String> addOnResourcePaths, final String addon,
			final String propertyName)
	{
		final String addOnResourcePropertyValue = siteConfigService.getProperty(addon + "." + propertyName);
		if (addOnResourcePropertyValue != null)
		{
			final String[] propertyPaths = addOnResourcePropertyValue.split(";");
			for (final String propertyPath : propertyPaths)
			{
				addOnResourcePaths.add(contextPath + "/_ui/addons/" + addon + propertyPath);
			}
		}
	}

	protected String getThemeNameForSite(final CMSSiteModel site)
	{
		final SiteTheme theme = site.getTheme();
		if (theme != null)
		{
			final String themeCode = theme.getCode();
			if (themeCode != null && !themeCode.isEmpty())
			{
				return themeCode;
			}
		}
		return getDefaultThemeName();
	}

	protected String getDefaultThemeName()
	{
		return defaultThemeName;
	}

	@Required
	public void setDefaultThemeName(final String defaultThemeName)
	{
		this.defaultThemeName = defaultThemeName;
	}

	private Map<String,Boolean> getSocialFlagsMap()
	{
		final Map<String, Boolean> socialFlagsMap = new HashMap<>();

		boolean isSocialEnabled = false;
		final String isSocialConfigured = cmsSiteService.getSiteConfig(IS_SOCIAL_ENABLED);
		if(Boolean.TRUE.equals(Boolean.valueOf(isSocialConfigured))){
			isSocialEnabled = true;
		}
		socialFlagsMap.put(IS_SOCIAL_ENABLED, isSocialEnabled);
		if(isSocialEnabled)
		{
			socialFlagsMap.put(IS_SOCIAL_SHARE_FB_ENABLED,
					Boolean.TRUE.toString().equalsIgnoreCase(cmsSiteService.getSiteConfig(IS_SOCIAL_SHARE_FB_ENABLED)));
			socialFlagsMap.put(IS_SOCIAL_SHARE_TWITTER_ENABLED,
					Boolean.TRUE.toString().equalsIgnoreCase(cmsSiteService.getSiteConfig(IS_SOCIAL_SHARE_TWITTER_ENABLED)));
			socialFlagsMap.put(IS_SOCIAL_SHARE_LINKEDIN_ENABLED,
					Boolean.TRUE.toString().equalsIgnoreCase(cmsSiteService.getSiteConfig(IS_SOCIAL_SHARE_LINKEDIN_ENABLED)));
			socialFlagsMap.put(IS_SOCIAL_SHARE_PINTEREST_ENABLED,
					Boolean.TRUE.toString().equalsIgnoreCase(cmsSiteService.getSiteConfig(IS_SOCIAL_SHARE_PINTEREST_ENABLED)));
		}
		return socialFlagsMap;
	}

	private GPSiteConfigData getGPSiteConfigs()
	{
		final GPSiteConfigData siteConfig = new GPSiteConfigData();
		siteConfig.setAddToCartEnabled(getSiteConfigProperty("addToCartEnabled"));
		siteConfig.setIsApplePayEnabled(getSiteConfigProperty("isApplePayEnabled"));
		siteConfig.setIsCallUsEnabled(getSiteConfigProperty("isCallUsEnabled"));
		siteConfig.setIsContactUsFormEnabled(getSiteConfigProperty("isContactUsFormEnabled"));
		siteConfig.setIsDinersCardAvailable(getSiteConfigProperty("isDinersCardAvailable"));
		siteConfig.setIsEmailUsEnabled(getSiteConfigProperty("isEmailUsEnabled"));
		siteConfig.setIsGiftingEnabled(getSiteConfigProperty("isGiftingEnabled"));
		siteConfig.setIsGooglePayEnabled(getSiteConfigProperty("isGooglePayEnabled"));
		siteConfig.setIsMobileMyProfileEnabled(getSiteConfigProperty("isMobileMyProfileEnabled"));
		siteConfig.setIsMultipleShippingEnabled(getSiteConfigProperty("isMultipleShippingEnabled"));
		siteConfig.setIsPayPalEnabled(getSiteConfigProperty("isPayPalEnabled"));
		siteConfig.setIsProductCompareEnabled(getSiteConfigProperty("isProductCompareEnabled"));
		siteConfig.setIsProductIdEnabled(getSiteConfigProperty("isProductIdEnabled"));
		siteConfig.setIsProductPriceEnabled(getSiteConfigProperty("isProductPriceEnabled"));
		siteConfig.setIsQuantitySelectorEnabled(getSiteConfigProperty("isQuantitySelectorEnabled"));
		siteConfig.setIsSaveCartEnabled(getSiteConfigProperty("isSaveCartEnabled"));
		siteConfig.setIsSocailShareEnabled(getSiteConfigProperty("isSocailShareEnabled"));
		siteConfig.setIsSupportTicketsEnabled(getSiteConfigProperty("isSupportTicketsEnabled"));
		siteConfig.setIsVariantsEnabled(getSiteConfigProperty("isVariantsEnabled"));
		siteConfig.setIsWriteUsEnabled(getSiteConfigProperty("isWriteUsEnabled"));
		siteConfig.setPdpLocateAStoreEnabled(getSiteConfigProperty("pdpLocateAStoreEnabled"));
		siteConfig.setPromoTextEnabled(getSiteConfigProperty("promoTextEnabled"));
		siteConfig.setShowDeliveryInstructions(getSiteConfigProperty("showDeliveryInstructions"));
		siteConfig.setShowInStockMsg(getSiteConfigProperty("showInStockMsg"));
		siteConfig.setShowInventoryMessages(getSiteConfigProperty("showInventoryMessages"));
		siteConfig.setShowPoBoxShippingMsg(getSiteConfigProperty("showPoBoxShippingMsg"));
		siteConfig.setShowProductFeatures(getSiteConfigProperty("showProductFeatures"));
		siteConfig.setShowProductIndicators(getSiteConfigProperty("showProductIndicators"));
		siteConfig.setShowProductResources(getSiteConfigProperty("showProductResources"));
		siteConfig.setShowProductSpecs(getSiteConfigProperty("showProductSpecs"));
		siteConfig.setShowProductStatus(getSiteConfigProperty("showProductStatus"));
		siteConfig.setIsFindADistributorEnabled(getSiteConfigProperty("isFindADistributorEnabled"));
		siteConfig.setShowForgotPassword(getSiteConfigProperty("showForgotPassword"));
		siteConfig.setCreateAcountMoreDetailsEnabled(getSiteConfigProperty("createAcountMoreDetailsEnabled"));
		siteConfig.setEditPasswordEnabled(getSiteConfigProperty("editPasswordEnabled"));
		siteConfig.setEditCommPrefEnabled(getSiteConfigProperty("editCommPrefEnabled"));
		siteConfig.setHasTaxExemption(getSiteConfigProperty("hasTaxExemption"));
		siteConfig.setAddAddressEnabled(getSiteConfigProperty("addAddressEnabled"));
		siteConfig.setDeleteAddressEnabled(getSiteConfigProperty("deleteAddressEnabled"));
		siteConfig.setAddPaymentEnabled(getSiteConfigProperty("addPaymentEnabled"));
		siteConfig.setDeletePaymentEnabled(getSiteConfigProperty("deletePaymentEnabled"));
		siteConfig.setIs3DImageEnabled(getSiteConfigProperty("is3DImageEnabled"));
		siteConfig.setReorderEnabled(getSiteConfigProperty("reorderEnabled"));
		siteConfig.setAddCustomAttrs(getSiteConfigProperty("addCustomAttrs"));
		siteConfig.setAddToListBySku(getSiteConfigProperty("addToListBySku"));
		siteConfig.setDownloadListImage(getSiteConfigProperty("downloadListImage"));
		siteConfig.setDownloadListPdf(getSiteConfigProperty("downloadListPdf"));
		siteConfig.setDownloadPdpImage(getSiteConfigProperty("downloadPdpImage"));
		siteConfig.setDownloadPdpPdf(getSiteConfigProperty("downloadPdpPdf"));
		siteConfig.setDownloadPrdListInfo(getSiteConfigProperty("downloadPrdListInfo"));
		siteConfig.setHasCmirCode(getSiteConfigProperty("hasCmirCode"));
		siteConfig.setIsFindAStoreEnabled(getSiteConfigProperty("isFindAStoreEnabled"));
		siteConfig.setShowLowInventoryMessage(getSiteConfigProperty("showLowInventoryMessage"));
		siteConfig.setPdpimageOrVideoConstraintEnabled(getSiteConfigProperty("pdpimageOrVideoConstraintEnabled"));
		siteConfig.setIsReferAFriend(getSiteConfigProperty("isReferAFriend"));
		siteConfig.setShowProductIcons(getSiteConfigProperty("showProductIcons"));
		siteConfig.setIsGuestList(getSiteConfigProperty("isGuestList"));
		siteConfig.setContentSearchEnabled(getSiteConfigPropertyTrue("contentSearchEnabled"));
		siteConfig.setRatingReviewEnabledForPDP(getSiteConfigPropertyTrue("ratingReviewEnabledForPDP"));
		siteConfig.setRatingReviewEnabledForSRP(getSiteConfigPropertyTrue("ratingReviewEnabledForSRP"));
		siteConfig.setShareProductEnabled(getSiteConfigPropertyTrue("shareProductEnabled"));
		siteConfig.setShipToLockersEnabled(getSiteConfigPropertyTrue("shipToLockersEnabled"));
		siteConfig.setInstallationSchedulingEnabled(getSiteConfigPropertyTrue("installationSchedulingEnabled"));
		siteConfig.setPaypalCreditEnabled(getSiteConfigPropertyTrue("paypalCreditEnabled"));
		siteConfig.setQplesEnabled(getSiteConfigPropertyTrue("qplesEnabled"));
		siteConfig.setIsCategoryDescriptionEnabled(getSiteConfigProperty("isCategoryDescriptionEnabled"));
		siteConfig.setShowDeliveryCost(getSiteConfigPropertyTrue("showDeliveryCost"));
		siteConfig.setIsShareWishlistEnabled(getSiteConfigProperty("customShareWishlistEnabled"));
		siteConfig.setShowFavIcon(getSiteConfigProperty("showFavIcon"));
		siteConfig.setIsBrandLogoMobile(getSiteConfigProperty("isBrandLogoMobile"));
		siteConfig.setShowCheckoutLogo(getSiteConfigProperty("showCheckoutLogo"));
		siteConfig.setEnableDisplayAttributes(getSiteConfigProperty("enableDisplayAttributes"));
		siteConfig.setIsRelatedEnabled(getSiteConfigProperty("isRelatedEnabled"));
		return siteConfig;
	}

	private Boolean getSiteConfigProperty(final String key)
	{
		return null != cmsSiteService.getSiteConfig(key) ? Boolean.valueOf(cmsSiteService.getSiteConfig(key)) : Boolean.FALSE;
	}
	
	private Boolean getSiteConfigPropertyTrue(final String key)
	{
		return null != cmsSiteService.getSiteConfig(key) ? Boolean.valueOf(cmsSiteService.getSiteConfig(key)) : Boolean.TRUE;
	}


}

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
package com.gp.commerce.v2.controller;

import de.hybris.platform.commercefacades.i18n.I18NFacade;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.QuplesData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commercewebservicescommons.dto.order.CardTypeListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.storesession.CurrencyListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.storesession.LanguageListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.CountryListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.RegionWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.RegionsListWSDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.TitleListWsDTO;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gp.commerce.errors.ErrorLogWSDTO;
import com.gp.commerce.facades.cart.GPCartFacade;
import com.gp.commerce.facades.customer.GpCustomerFacade;
import com.gp.commerce.facades.marketing.data.UpdatePreferenceData;
import com.gp.commerce.facades.payment.GPPaymentFacade;
import com.gp.commerce.facades.product.GPProductFacade;
import com.gp.commerce.facades.user.impl.GPDefaultUserFacade;
import com.gp.commerce.marketing.data.UpdatePreferenceWsDTO;
import com.gp.commerce.order.data.CardTypeDataList;
import com.gp.commerce.product.data.GPProductDataList;
import com.gp.commerce.product.data.GPProductListWsDTO;
import com.gp.commerce.storesession.data.CurrencyDataList;
import com.gp.commerce.storesession.data.LanguageDataList;
import com.gp.commerce.swagger.ApiBaseSiteIdParam;
import com.gp.commerce.user.data.CountryDataList;
import com.gp.commerce.user.data.QuplesWsDTO;
import com.gp.commerce.user.data.RegionDataList;
import com.gp.commerce.user.data.TitleDataList;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Misc Controller
 */
@Secured(
{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT","ROLE_ASAGENTSALESMANAGERGROUP" ,"ROLE_CLIENT","ROLE_GUEST"})
@Controller
@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 1800)
@Api(tags = "Miscs")
public class MiscsController extends BaseCommerceController {
	private static final String QUPLES_TOKEN_FIELDS = "contactKey,email,optIn,knownUser,quplesEndPoint";
	private static final String SUGGEST_ADDRESS_FIELDS = "titleCode,firstName,lastName,line1,line2,town,postalCode,country,region,defaultAddress,phone,email,shippingAddress,billingAddress,defaultBillingAddress,visibleInAddressBook,approvalStatus";
	private static final String VERIFY_ADDRESS_FIELDS = "titleCode,firstName,lastName,line1,line2,town,postalCode,country,region,defaultAddress,phone,email,shippingAddress,billingAddress,defaultBillingAddress,visibleInAddressBook,approvalStatus,companyName,palletShipment";
	private static final String DEFAULT_PAGE_SIZE_GPXPRESS = "1";
	private static final Logger LOG = Logger.getLogger(MiscsController.class);
	@Resource(name = "userFacade")
	private UserFacade userFacade;
	@Resource(name = "storeSessionFacade")
	private StoreSessionFacade storeSessionFacade;
	@Resource(name = "checkoutFacade")
	private CheckoutFacade checkoutFacade;
	@Resource(name = "gpCarFacade")
	private GPCartFacade gpCarFacade;
	@Resource(name = "i18NFacade")
	private I18NFacade i18NFacade;
	@Resource(name = "gpDefaultUserFacade")
	GPDefaultUserFacade gpDefaultUserFacade;
	@Resource(name="configurationService")
	ConfigurationService configurationService;
	@Resource(name="userService")
	UserService userService;
	@Resource(name="gpDefaultPaymentFacade")
	GPPaymentFacade paymentFacade;
	@Resource(name = "gpDefaultCustomerFacade")
	private GpCustomerFacade gpCustomerFacade;
	@Resource(name = "productFacade")
	private GPProductFacade productFacade;
	
	private static final String MY_ACCOUNT="my-account";
	private static final String IS_ERROR_ENABLED="enable.error.log";

	private static final String US_COUNTRYISO="US";
	private static final String US_CONTACTUS_COUNTRYISO="USC";
	private static final String CREATE_CUSTOMER_PREFS_FIELDS = "email,opt,firstName,lastName,mobilePhoneNumber,postalCode,age,address1,address2,fromPage";


	@RequestMapping(value = "/{baseSiteId}/languages", method = RequestMethod.GET)
	@Cacheable(value = "miscsCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getLanguages',#fields)")
	@ResponseBody
	@ApiOperation(value = "Get a list of available languages.", notes = "Lists all available languages (all languages used for a particular store). If the list of languages for a base store "
			+ "is empty, a list of all languages available in the system will be returned.")
	@ApiBaseSiteIdParam
	public LanguageListWsDTO getLanguages(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields) {
		final LanguageDataList dataList = new LanguageDataList();
		dataList.setLanguages(storeSessionFacade.getAllLanguages());
		return getDataMapper().map(dataList, LanguageListWsDTO.class, fields);
	}

	@RequestMapping(value = "/{baseSiteId}/currencies", method = RequestMethod.GET)
	@Cacheable(value = "miscsCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getCurrencies',#fields)")
	@ResponseBody
	@ApiOperation(value = "Get a list of available currencies.", notes = "Lists all available currencies (all usable currencies for the current store).If the list of currencies for stores is "
			+ "empty, a list of all currencies available in the system is returned.")
	@ApiBaseSiteIdParam
	public CurrencyListWsDTO getCurrencies(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields) {
		final CurrencyDataList dataList = new CurrencyDataList();
		dataList.setCurrencies(storeSessionFacade.getAllCurrencies());
		return getDataMapper().map(dataList, CurrencyListWsDTO.class, fields);
	}

	@RequestMapping(value = "/{baseSiteId}/deliverycountries", method = RequestMethod.GET)
	@Cacheable(value = "miscsCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getDeliveryCountries',#fields)")
	@ResponseBody
	@ApiOperation(value = "Get a list of supported countries.", notes = "Lists all supported delivery countries for the current store. The list is sorted alphabetically.")
	@ApiBaseSiteIdParam
	public CountryListWsDTO getDeliveryCountries(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final CountryDataList dataList = new CountryDataList();
		dataList.setCountries(checkoutFacade.getDeliveryCountries());
		return getDataMapper().map(dataList, CountryListWsDTO.class, fields);
	}

	@RequestMapping(value = "/{baseSiteId}/titles", method = RequestMethod.GET)
	@Cacheable(value = "miscsCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getTitles',#fields)")
	@ResponseBody
	@ApiOperation(value = "Get a list of all localized titles.", notes = "Lists all localized titles.")
	@ApiBaseSiteIdParam
	public TitleListWsDTO getTitles(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields) {
		final TitleDataList dataList = new TitleDataList();
		dataList.setTitles(userFacade.getTitles());
		return getDataMapper().map(dataList, TitleListWsDTO.class, fields);
	}

	@RequestMapping(value = "/{baseSiteId}/cardtypes", method = RequestMethod.GET)
	@Cacheable(value = "miscsCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getCardTypes',#fields)")
	@ResponseBody
	@ApiOperation(value = "Get a list of supported payment card types.", notes = "Lists supported payment card types.")
	@ApiBaseSiteIdParam
	public CardTypeListWsDTO getCardTypes(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields) {
		final CardTypeDataList dataList = new CardTypeDataList();
		dataList.setCardTypes(checkoutFacade.getSupportedCardTypes());
		return getDataMapper().map(dataList, CardTypeListWsDTO.class, fields);
	}

	@RequestMapping(value = "/{baseSiteId}/verifyaddress", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Verification of given address", notes = " Address verification")
	@ApiBaseSiteIdParam
	public AddressWsDTO verifyAddress(@ApiParam(value = "Request body parameter (DTO in xml or json format) which contains details like : Customer's first name(firstName), Customer's last name(lastName), Customer's title code(titleCode), "
					+ "country(country.isocode), first part of address(line1) , second part of address(line2), town (town), postal code(postalCode), region (region.isocode)", required = true) @RequestBody final AddressWsDTO address,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = FieldSetLevelHelper.FULL_LEVEL) final String fields) {
		if((!userFacade.isAnonymousUser() && address.getFromPage()!=null) && (address.getFromPage().equals(MY_ACCOUNT))){
			gpDefaultUserFacade.populateApprovalStatusForL2L3(address);
		}
		validate(address, "address", getAddressDTOValidator());
		AddressData addressData = getDataMapper().map(address, AddressData.class, VERIFY_ADDRESS_FIELDS);
		addressData = gpCarFacade.verifyAddress(addressData);
		final AddressWsDTO addressDto=getDataMapper().map(addressData, AddressWsDTO.class, fields);
		addressDto.setFormattedAddress(addressData.getFormattedAddress());
		if(addressData.getRegion()!=null) {
		final RegionWsDTO regionDto=new RegionWsDTO();
		regionDto.setName(addressData.getRegion().getName());
		regionDto.setIsocode(addressData.getRegion().getIsocode());
		regionDto.setIsocodeShort(addressData.getRegion().getIsocodeShort());
		regionDto.setCountryIso(addressData.getRegion().getCountryIso());
		addressDto.setRegion(regionDto);
		addressDto.setDefaultBillingAddress(addressData.isDefaultBillingAddress());
		}
		return addressDto;
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{baseSiteId}/suggestaddress", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Address Suggestions", notes = "Suggestions to be listed for the user to choose")
	@ApiBaseSiteIdParam
	public List<AddressWsDTO> suggestAddress(
			@ApiParam(value = "Request body parameter (DTO in xml or json format) which contains details like : Customer's first name(firstName), Customer's last name(lastName), Customer's title code(titleCode), "
					+ "country(country.isocode), first part of address(line1) , second part of address(line2), town (town), postal code(postalCode), region (region.isocode)", required = true) @RequestBody final AddressWsDTO address,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = FieldSetLevelHelper.FULL_LEVEL) final String fields) {
		validate(address, "address", getAddressDTOValidator());
		final AddressData addressData = getDataMapper().map(address, AddressData.class, SUGGEST_ADDRESS_FIELDS);
		final List<AddressWsDTO> addressDTOList = new ArrayList<AddressWsDTO>();
		final List<AddressData> addressList = gpCarFacade.suggestAddresses(addressData);
		for (final AddressData tempAddressData : addressList) {
			final AddressWsDTO tempAddressDTO = getDataMapper().map(tempAddressData, AddressWsDTO.class, fields);
			addressDTOList.add(tempAddressDTO);
		}
		return addressDTOList;
	}

	/**
	 * Gets the regions for the given country isocode
	 * @param countryIso
	 * @param fields
	 * @return regions list
	 */
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value="/{baseSiteId}/regions/{countryIsoCode}", method= RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="Regions List", notes="Regions for a particular country")
	@ApiBaseSiteIdParam
	public RegionsListWSDTO getRegionsForCountry(@ApiParam(value="Requires country isocode",required=true) @PathVariable final String countryIsoCode,@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL")
	@RequestParam(defaultValue = FieldSetLevelHelper.FULL_LEVEL ) final String fields,
	@RequestParam(required = false) final boolean contactUs)
	{
		final RegionDataList regionsDataList = new RegionDataList();
		final List<RegionData> regionsForCountryIso = getI18NFacade().getRegionsForCountryIso(countryIsoCode);
		if(contactUs) {
			if(regionsForCountryIso.get(0).getCountryIso().equals(US_COUNTRYISO)){
				final List<RegionData> regionsForContactUS = getI18NFacade().getRegionsForCountryIso(US_CONTACTUS_COUNTRYISO);
				regionsDataList.setRegions(regionsForContactUS);
				return getDataMapper().map(regionsDataList,RegionsListWSDTO.class,fields);
			}
		}
		regionsDataList.setRegions(regionsForCountryIso);
		return getDataMapper().map(regionsDataList,RegionsListWSDTO.class,fields);

	}

	/**
	 * Logs the error details
	 * @return
	 */
	@RequestMapping(value="/{baseSiteId}/error", method= RequestMethod.POST, consumes= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value="sets error logs if configured")
	public void setErrorLog(@ApiParam(value= "Request body parameter (DTO in xml or json format) which contains details like : Url, Request, Log") @RequestBody final ErrorLogWSDTO errorDetails,
	@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = FieldSetLevelHelper.FULL_LEVEL) final String fields)
	{
		final boolean errorLogEnabled = configurationService.getConfiguration().getBoolean(IS_ERROR_ENABLED, false);
		if(errorLogEnabled)
		{
			LOG.error("Error log is :" + errorDetails.getLog() + ", url is :" + errorDetails.getUrl() + ", request is :" +errorDetails.getRequest()
			+ "for user :" +userService.getCurrentUser().getUid());
		}
	}

	/**
	 * Service to fetch supported credit card types for payment
	 * @return
	 */
	@RequestMapping(value = "/{baseSiteId}/creditcardtypes", method = RequestMethod.GET)
	@Cacheable(value = "miscsCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getCreditCardTypes',#fields)")
	@ResponseBody
	@ApiOperation(value = "Get a list of supported payment credit card types.", notes = "List of supported payment credit card types.")
	@ApiBaseSiteIdParam
	public CardTypeListWsDTO getCreditCardTypes(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields) {
		final CardTypeDataList dataList = paymentFacade.getCreditCardTypes();
		return getDataMapper().map(dataList, CardTypeListWsDTO.class, fields);
	}

	/**
	 * Service to fetch credit card expiry year list
	 * @return
	 */
	@RequestMapping(value = "/{baseSiteId}/expireyearlist", method = RequestMethod.GET)
	@Cacheable(value = "miscsCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getExpireYearList',#fields)")
	@ResponseBody
	@ApiOperation(value = "Get credit card expiry year list", notes = "Get credit card expiry year list")
	@ApiBaseSiteIdParam
	public List<String> getExpiryYearList(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields) {
		return paymentFacade.getExpiryYearList();
	}

	public I18NFacade getI18NFacade() {
		return i18NFacade;
	}

	@RequestMapping(value = "/{baseSiteId}/termsAndConditions" , method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get the term and condition content", notes = "Get term and cond data")
	@ApiBaseSiteIdParam
	public String getTermsAndConditions(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		return gpDefaultUserFacade.getTermAndCondContent();
	}

	@RequestMapping(value = "/{baseSiteId}/getQuplesToken", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Get Quples Token link", notes = "Quples token is sent for a specific user")
	@ApiBaseSiteIdParam
	public QuplesWsDTO getQuplesToken(
			@ApiParam(value = "Request body parameter (DTO in xml or json format) which contains details like : Customer's email address", required = true) @RequestBody final QuplesWsDTO quplesWsDTO,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(required = false, defaultValue = FieldSetLevelHelper.FULL_LEVEL) final String fields) throws Exception{
		QuplesData quplesData = getDataMapper().map(quplesWsDTO, QuplesData.class, QUPLES_TOKEN_FIELDS);
		quplesData = gpDefaultUserFacade.getQuplesToken(quplesData);
		final QuplesWsDTO tempQuplesDTO = getDataMapper().map(quplesData, QuplesWsDTO.class, fields);

		return tempQuplesDTO;
	}

	@RequestMapping(value = "/{baseSiteId}/shippingcountries", method = RequestMethod.GET)
	@Cacheable(value = "miscsCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getShippingCountries',#fields)")
	@ResponseBody
	@ApiOperation(value = "Get a list of supported countries.", notes = "Lists all supported shipping countries for the current store. The list is sorted alphabetically.")
	@ApiBaseSiteIdParam
	public CountryListWsDTO getShippingCountries(
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final CountryDataList dataList = new CountryDataList();
		dataList.setCountries(gpCarFacade.getShippingCountries());
		return getDataMapper().map(dataList, CountryListWsDTO.class, fields);
	}
	
	@RequestMapping(value = "/{baseSiteId}/createQuplesTokes", method = RequestMethod.POST, consumes =
		{ MediaType.APPLICATION_JSON_VALUE })
		@ResponseBody
		@ApiOperation(value = "update the selected preferences to customer information, sync it to ymarketing ang generate quples token using the same", notes = "Returns stauts 200 OK.")
		@ResponseStatus(HttpStatus.OK)
		public QuplesData createQuplesToken(
				@ApiParam(value = "marketing preferences list object.", required = true) @RequestBody final UpdatePreferenceWsDTO marketingPreferences)
		{
			final UpdatePreferenceData preferenceData = getDataMapper().map(marketingPreferences, UpdatePreferenceData.class,
					CREATE_CUSTOMER_PREFS_FIELDS);
			return gpCustomerFacade.createQuplesToken(preferenceData);
		}
	
	/**
	 * 
	 * @param timeStamp
	 * @param pageNumber
	 * @param resend
	 * @param fields
	 * @return
	 */
	@RequestMapping(value = "/{baseSiteId}/gpXpressProductAlertlist", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get products with old and new attirbute values", notes = "Returns the product list with given attribute information.")
	@ApiBaseSiteIdParam
	public GPProductListWsDTO getGPXpressAlertProducts(
			@ApiParam(value = "timestamp") @RequestParam(required = true) final String timeStamp,
			@ApiParam(value = "page") @RequestParam(required = false, defaultValue =DEFAULT_PAGE_SIZE_GPXPRESS) final int pageNumber,
			@ApiParam(value = "resend") @RequestParam(required = false) final boolean resend,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final GPProductDataList gpProductDataList = productFacade.getGPXpressAlertProductList(timeStamp, pageNumber, resend);
		return getDataMapper().map(gpProductDataList, GPProductListWsDTO.class, fields);
	}

}
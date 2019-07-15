/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.user.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.QuplesData;
import de.hybris.platform.commercefacades.user.impl.DefaultUserFacade;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;
import org.springframework.web.util.WebUtils;
import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import de.hybris.platform.core.model.order.CartModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ui.Model;

import com.gp.commerce.core.company.services.GPB2BUnitsService;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.GPApprovalEnum;
import com.gp.commerce.core.enums.MarketingFrequencyEnum;
import com.gp.commerce.core.enums.OfferTypeEnum;
import com.gp.commerce.core.enums.SelectionType;
import com.gp.commerce.core.event.EmailSubjectType;
import com.gp.commerce.core.model.GPHeaderNavigationComponentModel;
import com.gp.commerce.core.model.GPXpressCMSNavigationNodeModel;
import com.gp.commerce.core.model.MarketingPreferenceModel;
import com.gp.commerce.core.model.MarketingPreferenceTypeModel;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.services.GPUserService;
import com.gp.commerce.core.services.event.GPEmailItemEvent;
import com.gp.commerce.core.services.impl.DefaultGPCustomerAccountService;
import com.gp.commerce.core.strategies.GPDefaultCustomerNameStrategy;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gp.commerce.facades.data.user.data.FrequencyData;
import com.gp.commerce.facades.data.user.data.MarketingPreferenceData;
import com.gp.commerce.facades.data.user.data.MarketingPreferenceDataList;
import com.gp.commerce.facades.data.user.data.MarketingPreferenceQuestionAnsData;
import com.gp.commerce.facades.marketing.data.UpdatePreferenceData;
import com.gp.commerce.facades.user.GPUserFacade;
import com.gp.commerce.facades.user.data.MarketingPreferenceQuestionAnsDataList;
import com.gp.commerce.gpcommerceaddon.facades.GPCategoryNavigationData;
import com.gp.commerce.gpcommerceaddon.facades.GPHeaderNavigationComponentData;
import com.gp.commerce.gpcommerceaddon.facades.GPHeaderOptionsData;
import com.gp.commerce.gpcommerceaddon.facades.GPMenuData;
import com.gp.commerce.gpcommerceaddon.facades.GPMenuOptionData;
import com.gp.commerce.gpcommerceaddon.facades.GPNavData;
import com.gp.commerce.gpcommerceaddon.facades.GPNavLinksData;
import com.gpintegration.service.GPYMarketingSyncService;
import com.gpintegration.service.impl.GPDefaultQuplesTokenService;

/**
 * This class extends the DefaultUserFacade and fives the implementations of some super class methods.
 */
public class GPDefaultUserFacade extends DefaultUserFacade implements GPUserFacade {
	private static final Logger LOG = Logger.getLogger(GPDefaultUserFacade.class);
	@Resource(name = "defaultGPCustomerAccountService")
	DefaultGPCustomerAccountService defaultGPCustomerAccountService;
	private GPDefaultQuplesTokenService quplesService;
	private GPUserService userService;
	private BaseSiteService baseSiteService;
	private ModelService modelService;
	private Converter<MarketingPreferenceModel, MarketingPreferenceData> marketingPreferenceConverter;
	@Resource(name = "cardPaymentInfoReverseConverter")
	private Converter<CCPaymentInfoData, CreditCardPaymentInfoModel> cardPaymentInfoReverseConverter;
	@Resource(name = "addressReverseConverter")
	private Converter<AddressData, AddressModel> addressReverseConverter;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	@Resource(name = "gpB2BUnitsService")
	protected GPB2BUnitsService<B2BUnitModel, UserModel> gpB2BUnitsService;
	@Resource(name="flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;
	@Resource(name = "enumerationService")
	private EnumerationService enumerationService;
	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;
	@Resource(name = "eventService")
	private EventService eventService;
	@Resource(name = "cmsPageService")
	private CMSPageService cmsPageService;
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;
	@Resource(name="gpYMarketingSyncService")
	private GPYMarketingSyncService yMarketingService;
	@Resource(name = "gpDefaultCustomerNameStrategy")
	private GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource(name = "commerceCartService")
	private CommerceCartService commerceCartService;
	private static final String PENDING_BY_ADMIN = "PENDINGBYADMIN";
	private static final String PENDING_BY_GP = "PENDINGBYGP";
	private static final String B2BUNITL2 = "b2b.unit.level.l2";
	private static final String B2BUNITL3 = "b2b.unit.level.l3";
	private static final String ORDER_CONFIRMATION_URI = "checkout/orderConfirmation";
	public static final String CHECKOUT_URI = "checkout";
	public static final String CHECKOUT_CONFIRMATION_URI = "checkout/checkoutConfirmation";
	public static final String MY_ACCOUNT = "/my-account/profile";
	public static final String TERM_AND_COND_PAGE_LABEL = "termAndCondPageLabel";
	private static final String ADDRESS_DATA = "addressData";
	private static final String PAYMENT_INFO = "paymentInfo";
	private static final String PAYMENT_INFO_ID = "paymentInfoId";
	private static final String RETAIL_SOLD_TO = "gpxpress.retail.navigation.soldToId";
	private static final String BUSINESS_USER_EMAIL = "gp.businessuser.emailId";
	private static final String GP_BUSINESS_USER_NAME = "gp.businessuser.name";

	/**
	 * Overrides the super implementation which treats default address as both
	 * default shipping address and default billing address. This method treats them
	 * differently i.e. default billing and default shipping addresses can be different.
	 * @param addressData The address data object
	 */
	@Override
	public void addAddress(final AddressData addressData) {
		validateParameterNotNullStandardMessage(ADDRESS_DATA, addressData);
		final CustomerModel currentCustomer = getCurrentUserForCheckout();
		final boolean makeThisDefaultShippingAddress = addressData.isDefaultAddress();
		final boolean makeThisDefaultBillingAddress = addressData.isDefaultBillingAddress();
		// Create the new address model
		final AddressModel newAddress = getModelService().create(AddressModel.class);
		getAddressReversePopulator().populate(addressData, newAddress);
		// Store the address against the user
		defaultGPCustomerAccountService.saveAddressEntry(currentCustomer, newAddress);
		// Update the address ID in the newly created address
		addressData.setId(newAddress.getPk().toString());
		if (makeThisDefaultShippingAddress || makeThisDefaultBillingAddress) {
			defaultGPCustomerAccountService.setDefaultAddressEntry(currentCustomer, newAddress, makeThisDefaultShippingAddress, makeThisDefaultBillingAddress);
		}
	}

	/**
	 * Overrides the super implementation which treats default address as both
	 * default shipping address and default billing address. This method treats them
	 * differently i.e. default billing and default shipping addresses can be different.
	 * @param addressData The address data object.
	 */
	@Override
	public void editAddress(final AddressData addressData) {
		validateParameterNotNullStandardMessage(ADDRESS_DATA, addressData);
		final CustomerModel currentCustomer = getCurrentUserForCheckout();
		 AddressModel addressModel=null;
				addressModel = defaultGPCustomerAccountService.getAddressForCode(currentCustomer, addressData.getId());
				addressModel.setRegion(null);
				getAddressReversePopulator().populate(addressData, addressModel);
				defaultGPCustomerAccountService.saveAddressEntry(currentCustomer, addressModel);
				setDefaultAddressEntry(addressData, addressData.isDefaultAddress(), addressData.isDefaultBillingAddress());
				// If the the address is not default shipping address but is set as default currently, then remove it from default shipping address field of customer.
				if (!Boolean.TRUE.equals(addressData.isDefaultAddress()) && addressModel.equals(currentCustomer.getDefaultShipmentAddress())) {
					defaultGPCustomerAccountService.clearDefaultAddressEntry(currentCustomer, true, false);
				}
				//If the address is not default billing address but is set as default currently, then remove it from default billing address field of customer.
				if (!Boolean.TRUE.equals(addressData.isDefaultBillingAddress()) && addressModel.equals(currentCustomer.getDefaultPaymentAddress())) {
					defaultGPCustomerAccountService.clearDefaultAddressEntry(currentCustomer, false, true);
				}
	}

	/**
	 * to edit address for B2B
	 * @param addressData The address data object
	 */
	@Override
	public void editAddressForB2B(final AddressData addressData, final boolean editAddress) {
		validateParameterNotNullStandardMessage(ADDRESS_DATA, addressData);
		AddressModel addressModel = null;
		if (addressData.getUserId() != null) {
			addressModel = defaultGPCustomerAccountService.getAddressForB2BCustomer(addressData.getId(), addressData.getUserId());
			final GPApprovalEnum beforeSaveApprovalStatus = addressModel.getApprovalStatus();
			addressModel.setRegion(null);
			getAddressReversePopulator().populate(addressData, addressModel);
			final UserModel userModel = getUserService().getUserForUID(addressData.getUserId());
			final B2BCustomerModel customerModel = (B2BCustomerModel) userModel;
			defaultGPCustomerAccountService.saveAddressEntry(customerModel, addressModel);
			if (editAddress) {
				setDefaultAddressEntryForB2BCustomer(addressData, addressData.isDefaultAddress(), addressData.isDefaultBillingAddress(), customerModel);
				// If the the address is not default shipping address but is set as default currently, then remove it from default shipping address field of customer.
				if (!Boolean.TRUE.equals(addressData.isDefaultAddress()) && addressModel.equals(customerModel.getDefaultShipmentAddress())) {
					defaultGPCustomerAccountService.clearDefaultAddressEntry(customerModel, true, false);
				}
				// If the address is not default billing address but is set as default currently, then remove it from default billing address field of customer.
				if (!Boolean.TRUE.equals(addressData.isDefaultBillingAddress()) && addressModel.equals(customerModel.getDefaultPaymentAddress())) {
					defaultGPCustomerAccountService.clearDefaultAddressEntry(customerModel, false, true);
				}
			}
			if (null != customerModel && customerModel instanceof B2BCustomerModel && null != customerModel.getDefaultB2BUnit()) {
				final boolean pendingByAdmin = (GpcommerceCoreConstants.PENDING_BY_ADMIN.equals(beforeSaveApprovalStatus.getCode())) ? true : false;
				final boolean pendingByGP = (GpcommerceCoreConstants.PENDING_BY_GP.equals(addressData.getApprovalStatus())) ? true : false;
				final boolean rejected = (GpcommerceCoreConstants.REJECTED.equals(addressData.getApprovalStatus())) ? true : false;
				// check if admin is approving buyer address
				if (!"L1".equalsIgnoreCase(((B2BCustomerModel) userModel).getDefaultB2BUnit().getB2bUnitLevel()) && pendingByAdmin && (pendingByGP || rejected)) {
					final GPEmailItemEvent item = new GPEmailItemEvent();
					item.setAddress(addressModel);
					if (pendingByGP) {
						item.setEmailSubject(EmailSubjectType.ADDRESS_SUBMITTED.getSubject());
						 List<String> toEmails = new ArrayList<>();
						 toEmails.add(cmsSiteService.getSiteConfig((BUSINESS_USER_EMAIL)));
						item.setToEmails(toEmails);
						item.setAdminName(cmsSiteService.getSiteConfig(GP_BUSINESS_USER_NAME));
					}
					if (rejected) {
						item.setEmailSubject(EmailSubjectType.ADDRESS_REJECTED.getSubject());
					}
					eventService.publishEvent(initializeEvent(item, (B2BCustomerModel) userModel));
				}
			}
		}
	}

	/**
	 * to get property from config
	 * @param String The property
	 */
	private String getProperty(final String property) {
		return configurationService.getConfiguration().getString(property);
	}

	/**
	 * to set values in event
	 * @param AbstractCommerceUserEvent The event
	 * @param B2BCustomerModel          The customerModel
	 */
	protected AbstractCommerceUserEvent initializeEvent(final AbstractCommerceUserEvent event,
			final B2BCustomerModel customerModel) {
		event.setBaseStore(customerModel.getSite().getStores().get(0));
		event.setSite(customerModel.getSite());
		event.setCustomer(customerModel);
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		return event;
	}
	
	/**
	 * to populate approval status for L2 L3 for B2B
	 * @param addressData The addressData
	 * @return address data
	 */
	public AddressWsDTO populateApprovalStatusForL2L3(final AddressWsDTO addressData) {
		final CustomerModel currentCustomer = getCurrentUserForCheckout();
		if((currentCustomer instanceof B2BCustomerModel)){
			if(((B2BCustomerModel) currentCustomer).getDefaultB2BUnit()!=null){
				final B2BUnitModel b2bUnit = ((B2BCustomerModel) currentCustomer).getDefaultB2BUnit();
				if((getProperty(B2BUNITL2).equals(b2bUnit.getB2bUnitLevel()))||(getProperty(B2BUNITL3).equals(b2bUnit.getB2bUnitLevel()))) {
					if(gpB2BUnitsService.isB2BAdmin((B2BCustomerModel)currentCustomer)){
						addressData.setApprovalStatus(PENDING_BY_GP);
					}else{
						addressData.setApprovalStatus(PENDING_BY_ADMIN);
					}
				}
			}
		}
		return addressData;
	}

	/**
	 * to check if B2B user is Admin
	 */
	public boolean isAdmin(){
		final CustomerModel currentCustomer = getCurrentUserForCheckout();
		boolean isB2BAdmin=false;
		if(currentCustomer instanceof B2BCustomerModel){
			isB2BAdmin=gpB2BUnitsService.isB2BAdmin((B2BCustomerModel)currentCustomer);
		}
		return isB2BAdmin;
	}

	/**
	 * Returns the default shipping address or defaultBillingAddress depending on
	 * the boolean values passed as arguments. If no default shipping/billing
	 * address is available then returns the first shipping/billing address
	 * @param getShippingAddress Boolean indicating if the default shipping address is to be fetched.
	 * @param getBillingAddress  Boolean indicating if the default billing address is to be fetched.
	 * @return Returns the default shipping address or defaultBillingAddress depending on the boolean values passed as arguments.
	 */
	@Override
	public AddressData getDefaultAddress(final boolean getShippingAddress, final boolean getBillingAddress) {
		final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
		AddressData defaultAddressData = null;
		AddressModel defaultAddress = null;
		if (getShippingAddress) {
			defaultAddress = currentCustomer.getDefaultShipmentAddress();
		} else { // A default address is either a shipping address or a billing address or both.
			defaultAddress = currentCustomer.getDefaultPaymentAddress();
		}
		if (defaultAddress != null) {
			defaultAddressData = getAddressConverter().convert(defaultAddress);
		}
		return defaultAddressData;
	}
	/**
	 * Returns the default shipping address or defaultBillingAddress depending on the boolean values passed as arguments.
	 * If no default shipping/billing address is available then returns the first shipping/billing address
	 * @param getShippingAddress Boolean indicating if the default shipping address is to be fetched.
	 * @param getBillingAddress Boolean indicating if the default billing address is to be fetched.
	 * @return Returns the default shipping address or defaultBillingAddress depending on the boolean values passed as arguments.
	 */
	@Override
	public AddressData getDefaultAddressForCustomer(final boolean getShippingAddress, final boolean getBillingAddress, final CustomerModel customer) {
		AddressData defaultAddressData = null;
		AddressModel defaultAddress = null;
		if (getShippingAddress) {
			defaultAddress = customer.getDefaultShipmentAddress();
		} else { // A default address is either a shipping address or a billing address or both.
			defaultAddress = customer.getDefaultPaymentAddress();
		}
		if (defaultAddress != null) {
			defaultAddressData = getAddressConverter().convert(defaultAddress);
		}
		return defaultAddressData;
	}

	/**
	 * Gets user address for code.
	 * @param code   the code
	 * @param userId the user id
	 * @return user address data
	 */
	public AddressData getUsersAddressForCode(final String code, final String userId) {
		final AddressModel address = defaultGPCustomerAccountService.getAddressForB2BCustomer(code,userId);
		return null != address ?  getAddressConverter().convert(address) : null;
	}
	
	/**
	 * Adds the given address as default shipping or default billing or both
	 * depending on the boolean indicators in arguments.
	 * @param addressData       The address to be added.
	 * @param isDefaultShipping Indicates if the address is to be added as default shipping address.
	 * @param isDefaultBilling  Indicated if the address is to be added as default billing address.
	 */
	@Override
	public void setDefaultAddressEntry(final AddressData addressData, final boolean isDefaultShipping, final boolean isDefaultBilling) {
		validateParameterNotNullStandardMessage(ADDRESS_DATA, addressData);
		final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
		final AddressModel addressModel = getCustomerAccountService().getAddressForCode(currentCustomer, addressData.getId());
		if (addressModel != null) {
			defaultGPCustomerAccountService.setDefaultAddressEntry(currentCustomer, addressModel, isDefaultShipping, isDefaultBilling);
		}
	}
	/**
	 * Adds the given address as default shipping or default billing or both depending on the boolean indicators in arguments.
	 * @param addressData The address to be added.
	 * @param isDefaultShipping Indicates if the address is to be added as default shipping address.
	 * @param isDefaultBilling Indicated if the address is to be added as default billing address.
	 */
	@Override
	public void setDefaultAddressEntryForB2BCustomer(final AddressData addressData, final boolean isDefaultShipping,
			final boolean isDefaultBilling, final CustomerModel customer) {
		validateParameterNotNullStandardMessage(ADDRESS_DATA, addressData);
		final AddressModel addressModel = defaultGPCustomerAccountService.getAddressForB2BCustomer(addressData.getId(),customer.getUid());
		if (addressModel != null) {
			defaultGPCustomerAccountService.setDefaultAddressEntry(customer, addressModel, isDefaultShipping, isDefaultBilling);
		}
	}

	/**
	 * Returns all the addresses pertaining to the current customer.
	 * @return All the addresses of the current customer.
	 */
	@Override
	public List<AddressData> getWholeAddressBook() {
		// Get the current customer's addresses
		final CustomerModel currentUser = (CustomerModel) getUserService().getCurrentUser();
		final Collection<AddressModel> addresses = defaultGPCustomerAccountService.getAddressBookEntries(currentUser);
		return (CollectionUtils.isNotEmpty(addresses)) ? getCustomerAddressBook(addresses) : Collections.emptyList();
	}
	/**
	 * Returns all the active addresses pertaining to the current customer.
	 * @return All the addresses of the current customer.
	 */
	@Override
	public List<AddressData> getActiveAddresses() {
		// Get the current customer's addresses
		final CustomerModel currentUser = (CustomerModel) getUserService().getCurrentUser();
		final Collection<AddressModel> addresses = defaultGPCustomerAccountService.getActiveAddressBookEntries(currentUser,true);
		return (CollectionUtils.isNotEmpty(addresses)) ? getCustomerAddressBook(addresses) : Collections.emptyList();
	}

	/**
	 * Add current users' credit card payment info.
	 * @param paymentInfo new payment info data
	 */
	@Override
	public void addCCPaymentInfo(final CCPaymentInfoData paymentInfo) {
		validateParameterNotNullStandardMessage(PAYMENT_INFO, paymentInfo);
		defaultGPCustomerAccountService.addCCPaymentInfo(paymentInfo);
	}

	@Override
	public void updateCCPaymentInfo(final CCPaymentInfoData paymentInfo) {
		validateParameterNotNullStandardMessage(PAYMENT_INFO, paymentInfo);
		validateParameterNotNullStandardMessage(PAYMENT_INFO_ID, paymentInfo.getId());
		defaultGPCustomerAccountService.updateCCPaymentInfo(paymentInfo);
	}

	@Override
	public MarketingPreferenceQuestionAnsDataList getAllMarketingPreferences() {
		final CMSSiteModel site = (CMSSiteModel) baseSiteService.getCurrentBaseSite();
		final List<MarketingPreferenceTypeModel> marketingPrefTypeList = userService.getDistMarketingPreferences(site);
		final MarketingPreferenceQuestionAnsDataList mPQuesList = new MarketingPreferenceQuestionAnsDataList();
		final List<MarketingPreferenceQuestionAnsData> listQuesAns = new ArrayList<MarketingPreferenceQuestionAnsData>();
		final CustomerModel currentCustomer = (CustomerModel)userService.getCurrentUser();
		for (final MarketingPreferenceTypeModel M : marketingPrefTypeList) {
			final MarketingPreferenceQuestionAnsData markPrefQuesAnsData = new MarketingPreferenceQuestionAnsData();
			if(OfferTypeEnum.FREQUENCY.getCode().equals(M.getOfferType().getCode())) {
				final List<MarketingFrequencyEnum> enumValues = enumerationService.getEnumerationValues(MarketingFrequencyEnum.class);
				final FrequencyData fd = new FrequencyData();
				fd.setQuestion(M.getHeaderText());
				fd.setFreqEnumValues(getEnumSelectedString(enumValues));
				mPQuesList.setFrequency(fd);
				continue;
			}
			final List<MarketingPreferenceModel> marketingPreferences = userService.getMarketingPreferencesForSiteAndType(site, M);
			if (!marketingPreferences.isEmpty()) {
				final List<MarketingPreferenceData> marketingPreferencesList = Converters.convertAll(marketingPreferences, marketingPreferenceConverter);
				markPrefQuesAnsData.setQuestion(M.getHeaderText());
				markPrefQuesAnsData.setMarketingPreferencesAnsList(marketingPreferencesList);
				markPrefQuesAnsData.setSelectionType(M.getSelectionType().getCode());
				determineSelectedRadioOption(currentCustomer, M, marketingPreferences, marketingPreferencesList);
				listQuesAns.add(markPrefQuesAnsData);
			}
		}
		mPQuesList.setMarketingPrefQuestionData(listQuesAns);
		return mPQuesList;
	}

	/**
	 * This method determines the selected Answer of the opt-in/opt-out question. If the user is logging in for the first time
	 * @param currentCustomer the current customer
	 * @param M the m
	 * @param marketingPreferences the marketing preferences
	 * @param marketingPreferencesList the marketing preferences list
	 */
	private void determineSelectedRadioOption(final CustomerModel currentCustomer, final MarketingPreferenceTypeModel M,
			final List<MarketingPreferenceModel> marketingPreferences,
			final List<MarketingPreferenceData> marketingPreferencesList) {
		if(SelectionType.RADIO.getCode().equals(M.getSelectionType().getCode())) {
			marketingPreferences.stream().forEach(pref-> {
					if (currentCustomer != null && currentCustomer.getMarketingPreferences().isEmpty()
							&& (Boolean.parseBoolean(pref.getValue()) == currentCustomer.getAddToMarketComm().booleanValue())) {
						marketingPreferencesList.stream().forEach(prefData->{
							if(pref.getPreferenceTypeId().equals(prefData.getPreferenceTypeId())){
								prefData.setSelected(true);
							}else {
								prefData.setSelected(false);
							}
						});
					}
			});
		}
	}

	protected List<MarketingPreferenceData> getMarketingPreferenceData(final List<MarketingPreferenceModel> marketingPreferences) {
		return Converters.convertAll(marketingPreferences, marketingPreferenceConverter);
	}

	@Override
	public void updateCustomerPreferences(final MarketingPreferenceDataList preferenceList) {
		final CMSSiteModel site = (CMSSiteModel)baseSiteService.getCurrentBaseSite();
		final CustomerModel currentCustomer =  (CustomerModel)userService.getCurrentUser();
		final List<MarketingPreferenceModel> sitePreferences = userService.getMarketingPreferences(site);
		//create the list of selected preference id's which user has opted for.
		final List<String> selectedPreferenceIds = preferenceList.getMarketingPreferences().stream()
				.filter(marketingPreference -> marketingPreference.isSelected())
				.map(marketingPreference -> marketingPreference.getPreferenceTypeId()).collect(Collectors.toList());
		//create the list of rejected preference id's which user had opted for before but has now opted out.
		final List<String> rejectedPreferenceIds = preferenceList.getMarketingPreferences().stream()
				.filter(marketingPreference -> !marketingPreference.isSelected())
				.map(marketingPreference -> marketingPreference.getPreferenceTypeId()).collect(Collectors.toList());
		if (StringUtils.isNotEmpty(preferenceList.getFrequency())) {
			currentCustomer.setMarkFrequency(enumerationService.getEnumerationValue(MarketingFrequencyEnum.class, preferenceList.getFrequency()));
		}
		if (!sitePreferences.isEmpty()) {
			final List<MarketingPreferenceModel> selectedCustomerPreferences = getPreferenceModelsForPreferenceIds(sitePreferences, selectedPreferenceIds);
			final List<MarketingPreferenceModel> rejectedCustomerPreferences = getPreferenceModelsForPreferenceIds(sitePreferences, rejectedPreferenceIds);
			final List<MarketingPreferenceModel> newPreferenceList = CollectionUtils.isEmpty(currentCustomer.getMarketingPreferences()) ? new ArrayList<>()
																								: new ArrayList<>(currentCustomer.getMarketingPreferences());
			if (!selectedCustomerPreferences.isEmpty()) { //Adding only those entries of selectedPreferences which are not there in newPreferenceList already.
				selectedCustomerPreferences.removeAll(newPreferenceList);
				newPreferenceList.addAll(selectedCustomerPreferences);
			}
			if (!rejectedCustomerPreferences.isEmpty()) {
				newPreferenceList.removeAll(rejectedCustomerPreferences);
			}
			syncToYMarketing(currentCustomer, rejectedCustomerPreferences, newPreferenceList);
			updateCustomerModel(currentCustomer, newPreferenceList);
			userService.reviseMarketingPreferenceUpdate(selectedCustomerPreferences, site, currentCustomer, true);
			userService.reviseMarketingPreferenceUpdate(rejectedCustomerPreferences, site, currentCustomer, false);
		}
	}
	private void updateCustomerModel(final CustomerModel currentCustomer, final List<MarketingPreferenceModel> newPreferenceList) {
		currentCustomer.setMarketingPreferences(newPreferenceList);
		defaultGPCustomerAccountService.updateCustomer(currentCustomer);
	}
	private List<MarketingPreferenceModel> getPreferenceModelsForPreferenceIds(
			final List<MarketingPreferenceModel> sitePreferences, final List<String> selectedPreferenceIds) {
		return sitePreferences.stream().filter(sitePreference -> selectedPreferenceIds.contains(sitePreference.getPreferenceTypeId())).collect(Collectors.toList());
	}
	private void syncToYMarketing(final CustomerModel currentCustomer, final List<MarketingPreferenceModel> rejectedCustomerPreferences, final List<MarketingPreferenceModel> newPreferenceList) {
		final String preferenceTypeIdToBeSynced = Config.getParameter(GpcommerceFacadesConstants.YMARKETING);
		final Optional<MarketingPreferenceModel> preferenceToBeSynced = newPreferenceList.stream().filter(pref->pref.getMarketingPreferenceTypeId().getMarketingPreferenceId().equalsIgnoreCase(preferenceTypeIdToBeSynced)).findAny();
		if(preferenceToBeSynced.isPresent()) {
			final UpdatePreferenceData syncRequest = new UpdatePreferenceData();
			syncRequest.setEmail(currentCustomer.getContactEmail());
			final String[] name = gpDefaultCustomerNameStrategy.splitName(currentCustomer.getName());
			syncRequest.setFirstName(name[0]);
			syncRequest.setLastName(name[1]);
			syncRequest.setOpt(Boolean.valueOf(preferenceToBeSynced.get().getValue())?"Y":"N");
			syncRequest.setMobilePhoneNumber(currentCustomer.getCellPhone());
			try {
				yMarketingService.createOrUpdateContact(syncRequest,false);
				currentCustomer.setAddToMarketComm(Boolean.valueOf(preferenceToBeSynced.get().getValue()));
			}catch(final Exception e) {
				LOG.error(e.getMessage(),e);
				//If the sync fails, don't update these preferences.
				newPreferenceList.removeIf(pref-> pref.getMarketingPreferenceTypeId().getMarketingPreferenceId().equalsIgnoreCase(preferenceTypeIdToBeSynced));
				rejectedCustomerPreferences.removeIf(pref-> pref.getMarketingPreferenceTypeId().getMarketingPreferenceId().equalsIgnoreCase(preferenceTypeIdToBeSynced));
			}
		}
	}

	public Converter<MarketingPreferenceModel, MarketingPreferenceData> getMarketingPreferenceConverter() {
		return marketingPreferenceConverter;
	}
	@Required
	public void setMarketingPreferenceConverter(final Converter<MarketingPreferenceModel, MarketingPreferenceData> marketingPreferenceConverter) {
		this.marketingPreferenceConverter = marketingPreferenceConverter;
	}
	@Override
	public GPUserService getUserService() {
		return userService;
	}
	@Required
	public void setUserService(final GPUserService userService) {
		this.userService = userService;
	}
	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}
	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}
	public Converter<CCPaymentInfoData, CreditCardPaymentInfoModel> getCardPaymentInfoReverseConverter() {
		return cardPaymentInfoReverseConverter;
	}
	public Converter<AddressData, AddressModel> getAddressReverseConverter() {
		return addressReverseConverter;
	}

	/**
	 * Gets marketing freq list using enum values.
	 * @param enumValues the enum values
	 * @return marketing freq value
	 */
	public List<String> getEnumSelectedString(final List<MarketingFrequencyEnum> enumValues) {
		final List<String> marketingFreqList = new ArrayList<String>();
		final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
		final MarketingFrequencyEnum freq = currentCustomer.getMarkFrequency();
		for (final MarketingFrequencyEnum enValue : enumValues) {
			if (enValue.equals(freq)) {
				marketingFreqList.add(enValue+"-true");
			} else {
				marketingFreqList.add(enValue+"-false");
			}
		}
		return marketingFreqList;
	}

	/**
	 * Returns the content of the Term and Condition since it require from different locations If no content available will return null
	 */
	@Override
	public String getTermAndCondContent() {
		String content = null;
		try {
			final ContentPageModel pageForRequest = cmsPageService.getPageForLabel(cmsSiteService.getSiteConfig(TERM_AND_COND_PAGE_LABEL));
			if (null != pageForRequest && CollectionUtils.isNotEmpty(pageForRequest.getContentSlots())) {
				final CMSParagraphComponentModel paragraph = (CMSParagraphComponentModel) pageForRequest
						.getContentSlots().get(0).getContentSlot().getCmsComponents().get(0);
				content = paragraph.getContent();
			}
		} catch (final Exception e) {
			LOG.error("Failed to find the term and Condition Content", e);
		}
		return content;
	}

	@Override
	public GPHeaderNavigationComponentData getHeaderData(final HttpServletRequest request,
			final GPHeaderNavigationComponentModel component, final Model model) {
		final GPHeaderNavigationComponentData gpHeaderNavigation = new GPHeaderNavigationComponentData();
		gpHeaderNavigation.setHeaderOptions(getGPHeaderOptionsData(component, request));
		if (null != component.getFindStoreMenu()) {
			gpHeaderNavigation.setFindMenu(getGPFindMenuData(component.getFindStoreMenu()));
		}
		if (userService.isAnonymousUser(userService.getCurrentUser())) {
			if (null != component.getLoginMenu()) {
				gpHeaderNavigation.setLoginMenu(getMenuData(component.getLoginMenu()));
			}
		} else {
			if (null != component.getLogoutMenu()) {
				gpHeaderNavigation.setLoginMenu(getMenuDataForLogout(component.getLogoutMenu()));
			}
			if (null != component.getAccountNavigationMenu()) {
				gpHeaderNavigation.setAccountMenu(getAccountData(component.getAccountNavigationMenu()));
				model.addAttribute("accountMenu", GPFunctions.convertToJSON(getAccountData(component.getAccountNavigationMenu())));
			}
		}
		if (null != component.getCategoryNavigationMenu()) {
			gpHeaderNavigation.setNavMenu(getGPCategoryNavigationData(component.getCategoryNavigationMenu()));
		}
		return gpHeaderNavigation;
	}

	/**
	 * Gets the json of header options data in header navigation component.
	 * @param component for component
	 * @return GPHeaderOptionsData
	 */
	private GPHeaderOptionsData getGPHeaderOptionsData(final GPHeaderNavigationComponentModel component, final HttpServletRequest request) {
		boolean checkoutFlag = false;
		final GPHeaderOptionsData gpHeaderOptionsData = new GPHeaderOptionsData();
		gpHeaderOptionsData.setIncludeMiniCart(component.isIncludeMiniCart());
		gpHeaderOptionsData.setIncludeSearchBox(component.isIncludeSearchBox());
		gpHeaderOptionsData.setShowFindAStore(null== component.getShowFindAStore()?false:component.getShowFindAStore());
		gpHeaderOptionsData.setShowLoginRegister(null== component.getShowLoginRegister()?false:component.getShowLoginRegister());
		gpHeaderOptionsData.setShowQuickOrder(component.isShowQuickOrder());
		gpHeaderOptionsData.setPromoTxt(component.getPromoTxt());
		gpHeaderOptionsData.setPromoUrl(component.getPromoUrl());
		if (null != component.getIsExternalLink()) {
			gpHeaderOptionsData.setIsExternalLink(component.getIsExternalLink());
		}
		gpHeaderOptionsData.setBrandLogo(component.getBrandLogo());
		if (null != component.getBrandMobileLogo()) {
			gpHeaderOptionsData.setBrandMobileLogo(component.getBrandMobileLogo());
		}
		if (null != component.getCheckoutLogo()) {
			gpHeaderOptionsData.setCheckoutLogo(component.getCheckoutLogo());
		}
		if (request.getRequestURI().contains(CHECKOUT_URI) && !request.getRequestURI().contains(CHECKOUT_CONFIRMATION_URI)
				&& !request.getRequestURI().contains(ORDER_CONFIRMATION_URI)) {
			checkoutFlag = true;
		}
		gpHeaderOptionsData.setIsCheckout(checkoutFlag);
		if (null != component.getCheckoutMenu()) {
			gpHeaderOptionsData.setCheckoutMenu(getMenuData(component.getCheckoutMenu()));
		}
		gpHeaderOptionsData.setShowMyList(null== component.getShowMyList()?false:component.getShowMyList());
		return gpHeaderOptionsData;
	}

	/**
	 * Gets the menu data in a particular format.
	 * @param component for component
	 * @return GPMenuOptionData
	 */
	private GPMenuOptionData getGPFindMenuData(final CMSNavigationNodeModel component) {
		GPMenuOptionData gpMenuOptionData = null;
		if (component != null) {
			for (final CMSNavigationEntryModel navEntry : component.getEntries()) {
				gpMenuOptionData = getMenuOptionData(navEntry);
				break;
			}
		}
		return gpMenuOptionData;
	}

	/**
	 * Gets the menu options data with link names/url in a particular format.
	 * @param component for component
	 * @return GPMenuOptionData
	 */
	private GPMenuOptionData getMenuOptionData(final CMSNavigationEntryModel navEntry) {
		final GPMenuOptionData optionData = new GPMenuOptionData();
		final CMSLinkComponentModel linkComponent = (CMSLinkComponentModel) navEntry.getItem();
		if (null != linkComponent) {
			optionData.setOption(linkComponent.getLinkName(commerceCommonI18NService.getCurrentLocale()));
			optionData.setOptionLink(linkComponent.getUrl());
			optionData.setExternal(linkComponent.isExternal());
			optionData.setMarkAsViewAll(null != linkComponent.getMarkAsViewAll() ? linkComponent.getMarkAsViewAll() : false);
			optionData.setSocialLink(null != linkComponent.getSocialLink() ? linkComponent.getSocialLink() : false);
		}
		return optionData;
	}

	/**
	 * Gets the menu options data with link names/url in a particular format.
	 * @param component for component
	 * @return GPMenuData
	 */
	private GPMenuData getMenuData(final CMSNavigationNodeModel navNode) {
		final GPMenuData menuData = new GPMenuData();
		final List<GPMenuOptionData> optionDatas = new ArrayList<GPMenuOptionData>();
		for (final CMSNavigationEntryModel navEntry : navNode.getEntries()) {
			optionDatas.add(getMenuOptionData(navEntry));
		}
		menuData.setTitle(navNode.getTitle(commerceCommonI18NService.getCurrentLocale()));
		menuData.setMenuOptions(optionDatas);
		return menuData;
	}

	/**
	 * Gets the menu data for logout in a particular format.
	 * @param component for component
	 * @return GPMenuData
	 */
	private GPMenuData getMenuDataForLogout(final CMSNavigationNodeModel navNode) {
		final GPMenuData menuData = new GPMenuData();
		final List<GPMenuOptionData> optionDatas = new ArrayList<GPMenuOptionData>();
		final GPMenuOptionData optionData = new GPMenuOptionData();
		final String fullname= userService.getCurrentUser().getName();
		final String[] name = gpDefaultCustomerNameStrategy.splitName(fullname);
		optionData.setOption(name[0]);
		optionData.setOptionLink(MY_ACCOUNT);
		optionData.setExternal(false);
		optionDatas.add(optionData);
		for (final CMSNavigationEntryModel navEntry : navNode.getEntries()) {
			optionDatas.add(getMenuOptionData(navEntry));
		}
		menuData.setTitle(navNode.getTitle(commerceCommonI18NService.getCurrentLocale()));
		menuData.setMenuOptions(optionDatas);
		return menuData;
	}

	/**
	 * Gets the category menu data in a particular format.
	 * @param CMSNavigationNodeModel for navigation node
	 * @return List<GPCategoryNavigationData>
	 */
	private List<GPCategoryNavigationData> getGPCategoryNavigationData(final CMSNavigationNodeModel component) {
		List<CMSNavigationNodeModel> children = component.getChildren();
		final List<GPCategoryNavigationData> categoryNavDataList = new ArrayList<GPCategoryNavigationData>();
		GPCategoryNavigationData categoryNavData = null;
		if((component instanceof GPXpressCMSNavigationNodeModel) && !(userService.isAnonymousUser(getUserService().getCurrentUser()))) {
			final B2BUnitModel unit = sessionService.getAttribute(GpcommerceCoreConstants.SOLD_TO_ID);
			List<CMSNavigationNodeModel> gproNavNode = new ArrayList<>();
			List<CMSNavigationNodeModel> retailNavNode = new ArrayList<>();
			for (final CMSNavigationNodeModel navNode : children) {
				if (StringUtils.isBlank(((GPXpressCMSNavigationNodeModel) navNode).getSoldTo())) {
					gproNavNode = navNode.getChildren();
				} else {
					retailNavNode = navNode.getChildren();
				}
			}
			children = (null != unit && unit.getUid().equalsIgnoreCase(getProperty(RETAIL_SOLD_TO))) ? retailNavNode : gproNavNode;
		}
		for (final CMSNavigationNodeModel primaryNode : children) {
			categoryNavData = new GPCategoryNavigationData();
			if (CollectionUtils.isNotEmpty(primaryNode.getEntries())) {
				final GPMenuOptionData primaryLink = getMenuOptionData(primaryNode.getEntries().get(0));
				categoryNavData.setPrimary(primaryNode.getTitle(commerceCommonI18NService.getCurrentLocale()));
				categoryNavData.setPrimaryLink(primaryLink.getOptionLink());
				categoryNavData.setExternal(primaryLink.isExternal());
			}
			final List<CMSNavigationNodeModel> secondaryNavNodes = primaryNode.getChildren();
			final GPMenuData secondaryLinksData = new GPMenuData();
			List<GPMenuOptionData> optionDataList = null;
			for (final CMSNavigationNodeModel secondaryNavNode : secondaryNavNodes) {
				final List<CMSNavigationNodeModel> subNavChildren = secondaryNavNode.getChildren();
				optionDataList = new ArrayList<GPMenuOptionData>();
				for (final CMSNavigationNodeModel subNavChild : subNavChildren) {
					if (CollectionUtils.isNotEmpty(subNavChild.getEntries())) {
						optionDataList.add(getMenuOptionData(subNavChild.getEntries().get(0)));
					}
				}
				secondaryLinksData.setTitle(secondaryNavNode.getTitle(commerceCommonI18NService.getCurrentLocale()));
				secondaryLinksData.setMenuOptions(optionDataList);
			}
			categoryNavData.setSecondary(secondaryLinksData);
			categoryNavDataList.add(categoryNavData);
		}
		return categoryNavDataList;
	}

	/**
	 * Gets the account data for user in a particular format.
	 * @param component for component
	 * @return List<GPNavData>
	 */
	private List<GPNavData> getAccountData(final CMSNavigationNodeModel navNode) {
		final List<CMSNavigationNodeModel> navs = navNode.getChildren();
		final List<GPNavData> columns = new ArrayList<>();
		for (final CMSNavigationNodeModel nav : navs) {
			final GPNavData navData = new GPNavData();
			final List<GPNavLinksData> navLinks = new ArrayList<>();
			final List<CMSNavigationNodeModel> subNavs = nav.getChildren();
			for (final CMSNavigationNodeModel nav1 : subNavs) {
				for (final CMSNavigationEntryModel nav2 : nav1.getEntries()) {
					final CMSLinkComponentModel linkComponent = (CMSLinkComponentModel) nav2.getItem();
					if (linkComponent != null) {
						final GPNavLinksData navLink = new GPNavLinksData();
						navLink.setLinkText(linkComponent.getLinkName(commerceCommonI18NService.getCurrentLocale()));
						navLink.setLinkTo(linkComponent.getUrl());
						navLinks.add(navLink);
					}
				}
			}
			navData.setTitle(nav.getTitle(commerceCommonI18NService.getCurrentLocale()));
			navData.setLinks(navLinks);
			columns.add(navData);
		}
		return columns;
	}

	public QuplesData getQuplesToken(QuplesData quplesData) throws Exception{
	quplesData=getQuplesService().getQuplesToken(quplesData);
	return quplesData;
	}

	/**
	 * Removes payment method without updating the default payment.
	 * @param id payment method ID
	 */
	@Override
	public void removeCCPaymentInfo(final String id) {
		validateParameterNotNullStandardMessage("id", id);
		final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
		for (final CreditCardPaymentInfoModel creditCardPaymentInfo : getCustomerAccountService().getCreditCardPaymentInfos( currentCustomer, false)) {
			if (creditCardPaymentInfo.getPk().toString().equals(id)) {
				getCustomerAccountService().deleteCCPaymentInfo(currentCustomer, creditCardPaymentInfo);
				break;
			}
		}
	}

	@Override
	public void removeAddress(final AddressData addressData) {
		validateParameterNotNullStandardMessage(ADDRESS_DATA, addressData);
		final CustomerModel currentCustomer = getCurrentUserForCheckout();
		for (final AddressModel addressModel : defaultGPCustomerAccountService.getAddressBookEntries(currentCustomer)) {
			if (addressData.getId().equals(addressModel.getPk().getLongValueAsString())) {
				addressModel.setVisibleInAddressBook(false);
				getModelService().save(addressModel);
				break;
			}
		}
	}

	private List<AddressData> getCustomerAddressBook(final Collection<AddressModel> addresses) {
				final List<AddressData> result = new ArrayList<AddressData>();
		for (final AddressModel address : addresses) {
					if (address.getOwner() instanceof CustomerModel) {
						final CustomerModel currentCustomer = (CustomerModel) address.getOwner();
						final AddressData defaultAddress = getDefaultAddressForCustomer(true, false, currentCustomer);
						final AddressData defaultBillingAddress = getDefaultAddressForCustomer(false, true, currentCustomer);
						final AddressData addressData = getAddressConverter().convert(address);
						if (defaultBillingAddress != null && defaultBillingAddress.getId() != null
								&& defaultBillingAddress.getId().equals(addressData.getId())) {
							addressData.setDefaultBillingAddress(true);
						}
						if (defaultAddress != null && defaultAddress.getId() != null
								&& defaultAddress.getId().equals(addressData.getId())) {
							addressData.setDefaultAddress(true);
							result.add(0, addressData);
						} else {
							result.add(addressData);
						}
					}
				}
				return result;
	}


	/**
	 * Change the cart user by taking cartID from Cookie
	 * @param request & cartID
	 */
	public void changeSubscripCartUser(final HttpServletRequest request,final String cartId) {
		final Cookie subCartCookie = WebUtils.getCookie(request, "subscrCartId");
		if (null != subCartCookie && null != subCartCookie.getValue()) {
			CartModel cart = commerceCartService.getCartForCodeAndUser(subCartCookie.getValue(),getUserService().getAnonymousUser());
			if (null != cart && null != cart.getIsSubscription() && cart.getIsSubscription()) {
			cart.setUser(getUserService().getCurrentUser());
			getModelService().save(cart);
		}
	}
	}
	public GPDefaultQuplesTokenService getQuplesService() {
		return quplesService;
	}
	public void setQuplesService(final GPDefaultQuplesTokenService quplesService) {
		this.quplesService = quplesService;
	}
	/**
	 * Gets ymarketing service.
	 * @return ymarketing service
	 */
	public GPYMarketingSyncService getyMarketingService() {
		return yMarketingService;
	}
	/**
	 * Sets ymarketing service.
	 * @param yMarketingService the new y marketing service
	 */
	public void setyMarketingService(final GPYMarketingSyncService yMarketingService) {
		this.yMarketingService = yMarketingService;
	}
	@Override
	public ModelService getModelService() {
		return modelService;
	}
	@Override
	public void setModelService(final ModelService modelService) {
		this.modelService = modelService;
	}
}

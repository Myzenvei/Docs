/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.hybris.ymkt.gpsapymktexpressway.services;

import com.gp.commerce.core.enums.GPUserApprovalStatusEnum;
import com.gp.commerce.core.enums.GenderEnum;
import com.gp.commerce.core.strategies.GPDefaultCustomerNameStrategy;
import com.gp.commerce.core.util.GPSiteConfigUtils;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSSiteDao;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.ymkt.gpsapymktexpressway.constants.GpsapymktexpresswayConstants;

/**
 * The Class CustomerSyncService.
 */
public class CustomerSyncService extends AbstractImportHeaderSyncService<CustomerModel>
{
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(CustomerSyncService.class);

	private static final String DEFAULTSITE = "default";
	
	private static final String GENDER_NOT_KNOWN = "Gender not known";
	
	private static final String ALLOWED_USER_STATUS="ymkt.alloweduser.status";
	
	private static final String USER_STATUS_DELIMITER=",";

	
	
	@Resource
	private ConfigurationService configurationService;

	
	@Resource
	private CMSSiteDao cmsSiteDao ;
	
	@Resource(name = "gpDefaultCustomerNameStrategy")
	private GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy;

	protected Map<String, Object> convertAddressModelToMap(final AddressModel address)
	{
		final Map<String, Object> map = new LinkedHashMap<>();

		//<Property Name="City" Type="Edm.String" MaxLength="40" sap:unicode="false" sap:label="City"/>
		this.optionalPut(map, "City", address, AddressModel::getTown);

		//<Property Name="CountryDescription" Type="Edm.String" MaxLength="40" sap:unicode="false" sap:label="Country"/>
		this.optionalPut(map, "CountryDescription", address, AddressModel::getCountry, CountryModel::getIsocode);


		//<Property Name="FirstName" Type="Edm.String" MaxLength="40" sap:unicode="false" sap:label="First name"/>
		this.optionalPut(map, "FirstName", address, AddressModel::getFirstname);

		//<Property Name="LastName" Type="Edm.String" MaxLength="40" sap:unicode="false" sap:label="Last name"/>
		this.optionalPut(map, "LastName", address, AddressModel::getLastname);


		//<Property Name="HouseNumber" Type="Edm.String" MaxLength="10" sap:unicode="false" sap:label="House Number"/>
		this.optionalPut(map, "HouseNumber", address, AddressModel::getStreetnumber);

		// yMKT seems to validate phone number according to https://en.wikipedia.org/wiki/E.123

		//<Property Name="MobilePhoneNumber" Type="Edm.String" MaxLength="30" sap:unicode="false" sap:label="Telephone no."/>
		this.optionalPut(map, "MobilePhoneNumber", address, AddressModel::getCellphone);

		//<Property Name="PhoneNumber" Type="Edm.String" MaxLength="30" sap:unicode="false" sap:label="Telephone no."/>
		this.optionalPut(map, "PhoneNumber", address, AddressModel::getPhone1);

		//<Property Name="RegionDescription" Type="Edm.String" MaxLength="40" sap:unicode="false" sap:label="Region"/>
		this.optionalPut(map, "RegionDescription", address, AddressModel::getRegion, RegionModel::getIsocodeShort);

		//<Property Name="Street" Type="Edm.String" MaxLength="60" sap:unicode="false" sap:label="Street"/>
		this.optionalPut(map, "Street", address, AddressModel::getStreetname);

		//<Property Name="TitleDescription" Type="Edm.String" MaxLength="40" sap:unicode="false" sap:label="Title"/>
		this.optionalPut(map, "TitleDescription", address, AddressModel::getTitle, TitleModel::getCode);

		return map;
	}

	protected Optional<AddressModel> selectAddressForCustomer(final CustomerModel customer)
	{
		final AddressModel defaultPaymentAddress = customer.getDefaultPaymentAddress();
		if (defaultPaymentAddress != null)
		{
			return Optional.of(defaultPaymentAddress);
		}

		return Optional.ofNullable(customer.getAddresses()) //
				.flatMap(addresses -> addresses.stream().min(Comparator.comparing(ItemModel::getPk)));
	}

	@Override
	protected Map<String, Object> convertModelToMap(final CustomerModel customer, final Map<String, Object> parameters)
	{
		LOG.debug(" Customer sync job{} "  ,customer.getContactEmail());

		final Map<String, Object> map = new LinkedHashMap<>();

		final String customerId = customer.getContactEmail();
		if (customerId == null || customerId.isEmpty())
		{
			LOG.debug("Customer with e-mail {} will not be sent to Hybris Marketing. Missing customerID.", customer.getUid());
			return Collections.emptyMap();
		}
		
		final String userApprovedStatus = getConfigurationService().getConfiguration().getString(ALLOWED_USER_STATUS,GPUserApprovalStatusEnum.APPROVED.getCode());
		final List<String> userStatusList = new ArrayList<>();
		if(userApprovedStatus.contains(USER_STATUS_DELIMITER)) {
			String[] statusArray = userApprovedStatus.split(USER_STATUS_DELIMITER);
			userStatusList.addAll(Arrays.asList(statusArray));
		} else {
			userStatusList.addAll(Collections.singletonList(userApprovedStatus));
		}

		//B2B Customers needs to be approved status before being synced to Ymkt systems
		if ( customer instanceof B2BCustomerModel) {
			if(!userStatusList.contains(((B2BCustomerModel) customer).getUserApprovalStatus().getCode())) {
				LOG.warn(" Unable to send {} to ymarketing because of {} - userapproval status", customer.getContactEmail(), ((B2BCustomerModel) customer).getUserApprovalStatus().getCode());
				return Collections.emptyMap();
			}
		}

		//<Property Name="Id" Type="Edm.String" Nullable="false" MaxLength="255" sap:unicode="false" sap:label="Text"/>
		//Setting customer email as the ID attribute
		map.put("Id", customerId);

		//<Property Name="IdOrigin" Type="Edm.String" Nullable="false" MaxLength="20" sap:unicode="false" sap:label="ID Origin"/>
		final String siteId = customer.getSite() != null ? customer.getSite().getUid():null;
		if(siteId != null) {
			CMSSiteModel cmsSiteModel = cmsSiteDao.findCMSSiteById(siteId);
			final String siteIdOrigin = GPSiteConfigUtils.getSiteConfig(cmsSiteModel,GpsapymktexpresswayConstants.GP_YMARKETING_SITEID);
			map.put("IdOrigin", siteIdOrigin != null ?siteIdOrigin : DEFAULTSITE);



		}

		//<Property Name="EMailAddress" Type="Edm.String" MaxLength="241" sap:unicode="false" sap:label="E-Mail Address"/>
		this.optionalPut(map, "EMailAddress", customer, CustomerModel::getContactEmail);

		//<Property Name="Timestamp" Type="Edm.DateTime" Nullable="false" Precision="7" sap:unicode="false" sap:label="Time Stamp"/>
		map.put("Timestamp", new Date()); // if address is modified or non-default address is deleted, customer isn't updated

		//<Property Name="FullName" Type="Edm.String" MaxLength="80" sap:unicode="false" sap:label="Name"/>
		map.put("FullName", gpDefaultCustomerNameStrategy.getName(customer.getName()));

		//<Property Name="IsConsumer" Type="Edm.Boolean" sap:unicode="false" sap:label="TRUE"/>
		map.put("IsConsumer", Boolean.TRUE);
		
		if(customer.getGender() !=null) {
			//<Property Name="GenderDescription" Type="Edm.String" MaxLength="40" sap:unicode="false" sap:label="Gender"/>
			map.put("GenderDescription", GenderEnum.PREFERNOTTOIDENTIFY.getCode().equalsIgnoreCase(customer.getGender().getCode()) ? GENDER_NOT_KNOWN :customer.getGender().getCode());
		}

		//<Property Name="DateOfBirth" Type="Edm.DateTime" Precision="0" sap:display-format="Date" sap:unicode="false" sap:label="Date of Birth"/>
		if(customer.getDateOfBirth() != null) {

			Date dateOfBirth = customer.getDateOfBirth();
			LOG.debug(" Customer  Date of birth {}" , dateOfBirth);
			LocalDate localDate = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			// <Property Name="YY1_BirthDay_MPS" Type="Edm.String" MaxLength="3" sap:text="YY1_BirthDay_MPST" sap:label="BirthDay"/>
			map.put("YY1_BirthDay_MPS",localDate.getDayOfMonth());

			//<Property Name="YY1_BirthMonth_MPS" Type="Edm.String" MaxLength="3" sap:text="YY1_BirthMonth_MPST" sap:label="BirthMonth" />
			map.put("YY1_BirthMonth_MPS",localDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.US));


		}

		this.selectAddressForCustomer(customer) //
				.map(this::convertAddressModelToMap) //
				.ifPresent(map::putAll);

		return map;
	}

	@Override
	protected String getImportHeaderNavigationProperty()
	{
		return "Contacts";
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
	
	
}

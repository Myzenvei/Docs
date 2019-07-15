/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gpsaporderexchange.outbound.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.sap.core.configuration.global.SAPGlobalConfigurationService;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.OrderEntryCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.PartnerCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.PartnerRoles;
import de.hybris.platform.sap.orderexchange.constants.SaporderexchangeConstants;
import de.hybris.platform.sap.orderexchange.outbound.RawItemContributor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import com.gpsaporderexchange.constants.GPOrderCsvColumns;


/**
 * @author Siddharth Jain
 *
 */

/**
 * Builds the Row map for the CSV files for the Partner in an Order
 */
public class GPPartnerContributor implements RawItemContributor<ConsignmentModel> {


	private SAPGlobalConfigurationService sAPGlobalConfigurationService;

	private CustomerAccountService customerAccountService;

	private Map<String, String> batchIdAttributes;

	public Map<String, String> getBatchIdAttributes() {
		return batchIdAttributes;
	}

	@Required
	public void setBatchIdAttributes(final Map<String, String> batchIdAttributes) {
		this.batchIdAttributes = batchIdAttributes;
	}

	@Override
	public Set<String> getColumns() {
		final Set<String> columns = new HashSet<>(Arrays.asList(OrderCsvColumns.ORDER_ID, PartnerCsvColumns.PARTNER_ROLE_CODE,
				PartnerCsvColumns.PARTNER_CODE, PartnerCsvColumns.DOCUMENT_ADDRESS_ID, PartnerCsvColumns.FIRST_NAME,
				PartnerCsvColumns.LAST_NAME, PartnerCsvColumns.STREET, PartnerCsvColumns.CITY,
				PartnerCsvColumns.TEL_NUMBER, PartnerCsvColumns.HOUSE_NUMBER, PartnerCsvColumns.POSTAL_CODE,
				PartnerCsvColumns.REGION_ISO_CODE, PartnerCsvColumns.COUNTRY_ISO_CODE, PartnerCsvColumns.EMAIL,
				PartnerCsvColumns.LANGUAGE_ISO_CODE, PartnerCsvColumns.MIDDLE_NAME, PartnerCsvColumns.MIDDLE_NAME2,
				PartnerCsvColumns.DISTRICT, PartnerCsvColumns.BUILDING, PartnerCsvColumns.APPARTMENT,
				PartnerCsvColumns.POBOX, PartnerCsvColumns.FAX, PartnerCsvColumns.TITLE,
				OrderEntryCsvColumns.ENTRY_NUMBER, GPOrderCsvColumns.COMPANY_NAME));
		columns.addAll(getBatchIdAttributes().keySet());

		return columns;
	}

	@Override
	public List<Map<String, Object>> createRows(final ConsignmentModel consignment) {
		final OrderModel order = getOrderForCode(consignment.getOrder());
		if (null != order) {
			final List<Map<String, Object>> result = new ArrayList<>(2);
			AddressModel paymentAddress = order.getPaymentAddress();
			AddressModel deliveryAddress = consignment.getShippingAddress();
			if (deliveryAddress == null && paymentAddress!=null)
			{
				deliveryAddress = paymentAddress;
			}
			if (paymentAddress == null && deliveryAddress != null)
			{
				paymentAddress = deliveryAddress;
			}

			final String b2cCustomer = determineB2CCustomer(order);
			final String sapcommon_Customer = b2cCustomer != null ? b2cCustomer
					: order.getStore().getSAPConfiguration().getSapcommon_referenceCustomer();
			final String soldToId = order.getStore().getSAPConfiguration().getSoldToID();

			final String deliveryAddressNumber = getShipToAddressNumber();
			Map<String, Object> row = mapAddressData(consignment, deliveryAddress, order);
			row.put(PartnerCsvColumns.PARTNER_ROLE_CODE, PartnerRoles.SHIP_TO.getCode());
			row.put(PartnerCsvColumns.PARTNER_CODE, sapcommon_Customer);
			row.put(PartnerCsvColumns.DOCUMENT_ADDRESS_ID, deliveryAddressNumber);
			getBatchIdAttributes().forEach(row::putIfAbsent);
			row.put("dh_batchId", consignment.getCode());
			result.add(row);

			row = mapAddressData(consignment, paymentAddress, order);


			row.put(PartnerCsvColumns.PARTNER_ROLE_CODE, PartnerRoles.SOLD_TO.getCode());
			row.put(PartnerCsvColumns.PARTNER_CODE, soldToId);
			getBatchIdAttributes().forEach(row::putIfAbsent);
			row.put("dh_batchId", consignment.getCode());
			result.add(row);

			return result;
		}
		return Collections.emptyList();
	}

	protected String getSoldToAddressNumber() {
		return SaporderexchangeConstants.ADDRESS_ONE;
	}

	protected String getShipToAddressNumber() {
		return SaporderexchangeConstants.ADDRESS_TWO;
	}

	protected Map<String, Object> mapAddressData(final ConsignmentModel consignment, final AddressModel address, final OrderModel order) {
		final Map<String, Object> row = new HashMap<>();
		row.put(OrderCsvColumns.ORDER_ID, consignment.getCode());
		row.put(PartnerCsvColumns.LAST_NAME, address.getFirstname() != null && address.getLastname() != null ? address.getFirstname() + " " +address.getLastname() : "");
		row.put(PartnerCsvColumns.STREET, address.getStreetname());
		row.put(PartnerCsvColumns.CITY, address.getTown());
		row.put(PartnerCsvColumns.TEL_NUMBER, address.getPhone1());
		row.put(PartnerCsvColumns.HOUSE_NUMBER, address.getStreetnumber());
		row.put(PartnerCsvColumns.POSTAL_CODE, address.getPostalcode());
		row.put(PartnerCsvColumns.REGION_ISO_CODE,
				(address.getRegion() != null) ? address.getRegion().getIsocodeShort() : "");
		row.put(PartnerCsvColumns.COUNTRY_ISO_CODE,
				(address.getCountry() != null) ? address.getCountry().getIsocode() : "");
		if (consignment.getOrder().getUser() instanceof CustomerModel)
		{
			final CustomerModel customer = (CustomerModel) consignment.getOrder().getUser();
			row.put(PartnerCsvColumns.EMAIL, customer.getOriginalUid());
		}
		if (consignment.getOrder().getUser() instanceof B2BCustomerModel)
		{
			final B2BCustomerModel customer = (B2BCustomerModel) consignment.getOrder().getUser();
			row.put(PartnerCsvColumns.EMAIL, customer.getOriginalUid());
		}
		row.put(PartnerCsvColumns.MIDDLE_NAME, address.getMiddlename());
		row.put(PartnerCsvColumns.MIDDLE_NAME2, address.getMiddlename2());
		row.put(PartnerCsvColumns.DISTRICT, address.getDistrict());
		row.put(PartnerCsvColumns.BUILDING, address.getBuilding());
		row.put(PartnerCsvColumns.APPARTMENT, address.getAppartment());
		row.put(PartnerCsvColumns.POBOX, address.getPobox());
		row.put(PartnerCsvColumns.FAX, address.getFax());
		row.put(PartnerCsvColumns.LANGUAGE_ISO_CODE, order.getLanguage().getIsocode());
		row.put(PartnerCsvColumns.TITLE, (address.getTitle() != null) ? address.getTitle().getCode() : "");

		return row;
	}

	private OrderModel getOrderForCode(final AbstractOrderModel order){

		final OrderModel orderModel = getCustomerAccountService().getOrderForCode(order.getCode(), order.getStore());
		return orderModel;
	}

	@SuppressWarnings("javadoc")
	public SAPGlobalConfigurationService getsAPGlobalConfigurationService() {
		return sAPGlobalConfigurationService;
	}

	@SuppressWarnings("javadoc")
	public void setsAPGlobalConfigurationService(final SAPGlobalConfigurationService sAPGlobalConfigurationService) {
		this.sAPGlobalConfigurationService = sAPGlobalConfigurationService;
	}

	@SuppressWarnings("javadoc")
	public CustomerAccountService getCustomerAccountService() {
		return customerAccountService;
	}

	@SuppressWarnings("javadoc")
	public void setCustomerAccountService(final CustomerAccountService customerAccountService) {
		this.customerAccountService = customerAccountService;
	}

	public String determineB2CCustomer(final AbstractOrderModel order) {
		return Boolean.TRUE.equals(sAPGlobalConfigurationService.getProperty("replicateregistereduser"))
				? ((CustomerModel) order.getUser()).getSapConsumerID() : null;
	}
}

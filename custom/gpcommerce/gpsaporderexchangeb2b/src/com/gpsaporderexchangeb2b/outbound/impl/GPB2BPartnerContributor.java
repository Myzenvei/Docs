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
package com.gpsaporderexchangeb2b.outbound.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.PartnerCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.PartnerRoles;
import de.hybris.platform.sap.orderexchange.constants.SaporderexchangeConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;

import com.gpsaporderexchange.constants.GPOrderCsvColumns;
import com.gpsaporderexchange.outbound.impl.GPPartnerContributor;



/**
 * Partner contributor for B2B orders to be replicated to SAP ERP system. Considers partner roles soldTo (AG), contact
 * (AP), billTo (RE) and shipTo (WE)
 *
 */
public class GPB2BPartnerContributor extends GPPartnerContributor
{

	private B2BUnitService<B2BUnitModel, ?> b2bUnitService;

	private CustomerAccountService customerAccountService;

	@Override
	public List<Map<String, Object>> createRows(final ConsignmentModel consignment)
	{
		return isB2BOrder(consignment) ? createB2BRows(consignment) : super.createRows(consignment);
	}

	protected List<Map<String, Object>> createB2BRows(final ConsignmentModel consignment)
	{
		final Map<String, Object> soldToRow = createPartnerRow(consignment, PartnerRoles.SOLD_TO, soldToFromSAPConfig(consignment));
		final Map<String, Object> shipToRow = createAddressRow(consignment, PartnerRoles.SHIP_TO, SaporderexchangeConstants.ADDRESS_ONE);


		final List<Map<String, Object>> result = new ArrayList<>(3);

		if (!MapUtils.isEmpty(soldToRow)) {
			result.add(soldToRow);
		}
		if (!MapUtils.isEmpty(shipToRow)) {
			result.add(shipToRow);
		}
		return result;
	}

	protected String contactFromOrder(final ConsignmentModel consignment)
	{
		return ((B2BCustomerModel) consignment.getOrder().getUser()).getCustomerID();
	}

	protected String soldToFromOrder(final ConsignmentModel consignment)
	{
		final CompanyModel rootUnit = getB2bUnitService().getRootUnit(consignment.getOrder().getUnit());
		return rootUnit.getUid();
	}

	protected String soldToFromSAPConfig(final ConsignmentModel consignment)
	{
		final String soldToId = consignment.getOrder().getStore().getSAPConfiguration().getSoldToID();
		return soldToId;
	}

	protected String shipToFromSAPConfig(final ConsignmentModel consignment)
	{
		final String shipToId = consignment.getOrder().getStore().getSAPConfiguration().getSapcommon_referenceCustomer();
		return shipToId;
	}


	protected Map<String, Object> createPartnerRow(final ConsignmentModel consignment, final PartnerRoles partnerRole, final String partnerId)
	{
		final Map<String, Object> row = new HashMap<>();
		row.put(OrderCsvColumns.ORDER_ID, consignment.getCode());
		row.put(PartnerCsvColumns.PARTNER_ROLE_CODE, partnerRole.getCode());
		row.put(PartnerCsvColumns.PARTNER_CODE, partnerId);
		row.put(PartnerCsvColumns.DOCUMENT_ADDRESS_ID, "");
		return row;
	}

	protected Map<String, Object> createAddressRow(final ConsignmentModel consignment, final PartnerRoles partnerRole,
			final String addressNumber)
	{
		final AddressModel address = addressForPartnerRole(consignment, partnerRole);
		Map<String, Object> row = null;
		if (address != null)
		{
			row = new HashMap<>();
			row.put(OrderCsvColumns.ORDER_ID, consignment.getCode());
			row.put(PartnerCsvColumns.PARTNER_ROLE_CODE, partnerRole.getCode());
			final String sapCustomer = address.getSapCustomerID();
			if (sapCustomer == null || sapCustomer.isEmpty())
			{
				row.put(PartnerCsvColumns.PARTNER_CODE, shipToFromSAPConfig(consignment));
				row.put(PartnerCsvColumns.DOCUMENT_ADDRESS_ID, addressNumber);
				mapAddressData(consignment, address, row);
			}
			else
			{
				row.put(PartnerCsvColumns.PARTNER_CODE, sapCustomer);
				row.put(PartnerCsvColumns.DOCUMENT_ADDRESS_ID, "");
			}
		}
		return row;
	}

	protected AddressModel addressForPartnerRole(final ConsignmentModel consignment, final PartnerRoles partnerRole)
	{
		AddressModel result = null;
		if (partnerRole == PartnerRoles.SHIP_TO)
		{
			result = consignment.getShippingAddress();
		}
		else if (partnerRole == PartnerRoles.BILL_TO)
		{
			result = consignment.getOrder().getPaymentAddress();
		}
		return result;
	}

	protected void mapAddressData(final ConsignmentModel consignment, final AddressModel address, final Map<String, Object> row)
	{
		row.put(PartnerCsvColumns.LAST_NAME, address.getFirstname() != null && address.getLastname() != null ? address.getFirstname() + " " +address.getLastname() : "");
		row.put(PartnerCsvColumns.STREET, address.getStreetname());
		row.put(PartnerCsvColumns.CITY, address.getTown());
		row.put(PartnerCsvColumns.TEL_NUMBER, address.getPhone1());
		row.put(PartnerCsvColumns.HOUSE_NUMBER, address.getStreetnumber());
		row.put(PartnerCsvColumns.POSTAL_CODE, address.getPostalcode());
		row.put(PartnerCsvColumns.REGION_ISO_CODE, (address.getRegion() != null) ? address.getRegion().getIsocodeShort() : "");
		row.put(PartnerCsvColumns.COUNTRY_ISO_CODE, (address.getCountry() != null) ? address.getCountry().getIsocode() : "");
		row.put(PartnerCsvColumns.EMAIL, address.getEmail());
		row.put(PartnerCsvColumns.MIDDLE_NAME, address.getMiddlename());
		row.put(PartnerCsvColumns.MIDDLE_NAME2, address.getMiddlename2());
		row.put(PartnerCsvColumns.DISTRICT, address.getDistrict());
		row.put(PartnerCsvColumns.BUILDING, address.getBuilding());
		row.put(PartnerCsvColumns.APPARTMENT, address.getAppartment());
		row.put(PartnerCsvColumns.POBOX, address.getPobox());
		row.put(PartnerCsvColumns.FAX, address.getFax());
		final OrderModel order = getOrderForCode(consignment.getOrder());
		row.put(PartnerCsvColumns.LANGUAGE_ISO_CODE, order.getLanguage().getIsocode());
		row.put(PartnerCsvColumns.TITLE, (address.getTitle() != null) ? address.getTitle().getCode() : "");
		row.put(GPOrderCsvColumns.COMPANY_NAME, address.getCompany());
	}

	protected boolean isB2BOrder(final ConsignmentModel consignment)
	{
		return consignment.getOrder().getSite().getChannel() == SiteChannel.B2B;
	}

	private OrderModel getOrderForCode(final AbstractOrderModel order) {

		final OrderModel orderModel = getCustomerAccountService().getOrderForCode(order.getCode(), order.getStore());
		return orderModel;
	}


	@SuppressWarnings("javadoc")
	public B2BUnitService<B2BUnitModel, ?> getB2bUnitService()
	{
		return b2bUnitService;
	}

	@SuppressWarnings("javadoc")
	public void setB2bUnitService(final B2BUnitService<B2BUnitModel, ?> b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}

	@Override
	@SuppressWarnings("javadoc")
	public CustomerAccountService getCustomerAccountService() {
		return customerAccountService;
	}

	@Override
	@SuppressWarnings("javadoc")
	public void setCustomerAccountService(final CustomerAccountService customerAccountService) {
		this.customerAccountService = customerAccountService;
	}

}

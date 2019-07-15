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

import com.gp.commerce.core.util.GPSiteConfigUtils;


import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSSiteDao;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.ymkt.common.product.ProductURLService;
import com.hybris.ymkt.gpsapymktexpressway.constants.GpsapymktexpresswayConstants;


/**
 * Send {@link CartModel} to CUAN_IMPORT_SRV
 */
public class AbandonedCartSyncService extends AbstractImportHeaderSyncService<CartModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(AbandonedCartSyncService.class);

	private static final String DEFAULTSITE = "default";
	
	private static final String NAVIGATION_URL = "NavigationURL";

	protected ProductURLService productURLService;
	
	@Resource
	private CMSSiteDao cmsSiteDao ;

	protected Map<String, Object> convertCartEntryModelToMap(final CartEntryModel cartEntry, final Map<String, Object> parameters)
	{
		final Map<String, Object> map = new HashMap<>();
		final Locale loc = this.getLocale(parameters);
		final ProductModel product = cartEntry.getProduct();

		
		//<Property Name="ItemType" Type="Edm.String" Nullable="false" MaxLength="30" sap:unicode="false" sap:label="Origin of Product" sap:updatable="false"/>
		map.put("ItemType", "SAP_HYBRIS_PRODUCT");

		//<Property Name="Amount" Type="Edm.Decimal" Precision="31" Scale="2" sap:unicode="false" sap:label="Amount" sap:updatable="false"/>
		map.put("Amount", cartEntry.getTotalPrice());

		if(product != null) {
			//<Property Name="ItemId" Type="Edm.String" Nullable="false" MaxLength="50" sap:unicode="false" sap:label="Product ID" sap:updatable="false"/>
			map.put("ItemId", product.getCode());

			//<Property Name="Description" sap:updatable="false" sap:label="Product Desc." sap:unicode="false" MaxLength="120" Type="Edm.String"/>
			this.optionalPut(map, "Description", product, //
					p -> p.getDescription(loc), //
					d -> d.replaceAll("\\<.*?\\>", ""));

			//<Property Name="ImageUrl" sap:updatable="false" sap:label="Short URI" sap:unicode="false" MaxLength="1333" Type="Edm.String" sap:filterable="false" sap:sortable="false"/>
			this.productURLService.getProductImageURL(product).ifPresent(img -> map.put("ImageUrl", img));

			//<Property Name="Key" sap:updatable="false" sap:label="GUID 16" sap:unicode="false" MaxLength="32" Type="Edm.String"/>

			//<Property Name="Name" sap:updatable="false" sap:label="Product" sap:unicode="false" MaxLength="40" Type="Edm.String"/>
			this.optionalPut(map, "Name", product, p -> p.getName(loc));

			//<Property Name="NavigationURL" sap:updatable="false" sap:label="Short URI" sap:unicode="false" MaxLength="1333" Type="Edm.String" sap:filterable="false" sap:sortable="false"/>
			map.put(NAVIGATION_URL, this.productURLService.getProductURL(product));

			final CMSSiteModel productSite = CollectionUtils.isNotEmpty(product.getSite()) ? product.getSite().stream().findFirst().orElse(null):null;
			if(productSite != null) {
				final String siteIdOrigin = GPSiteConfigUtils.getSiteConfig(productSite,GpsapymktexpresswayConstants.GP_YMARKETING_SITEID );
				//Set source system id to same as idorigin
				//<Property Name="SourceSystemId" Type="Edm.String" MaxLength="255" sap:unicode="false" sap:label="Source Syst. ID" sap:updatable="false"/>
				map.put(GpsapymktexpresswayConstants.SOURCE_SYSTEM_ID, siteIdOrigin != null ?siteIdOrigin :GpsapymktexpresswayConstants.DEFAULT_SOURCE_SYSTEM_ID);
			}
		} else {
			LOG.warn("Ymkt abandonedcart job:: No product for cart Entry PK {} , entry {} " , cartEntry.getPk(), cartEntry.getEntryNumber()); 
		}
		//<Property Name="Quantity" Type="Edm.Decimal" Precision="22" Scale="5" sap:unicode="false" sap:unit="UnitOfMeasure" sap:label="Quantity" sap:updatable="false"/>
		map.put("Quantity", cartEntry.getQuantity());

		//<Property Name="UnitOfMeasure" Type="Edm.String" MaxLength="3" sap:unicode="false" sap:label="Unit of Measure" sap:updatable="false" sap:sortable="false"/>
		map.put("UnitOfMeasure", cartEntry.getUnit().getCode());

		//<Property Name="SourceItemNumber" Type="Edm.Int32" sap:unicode="false" sap:label="Source Item Number" sap:updatable="false"/>
		map.put("SourceItemNumber", cartEntry.getOrder().getEntries().size() - cartEntry.getEntryNumber());

		return map;
	}

	@Override
	protected Map<String, Object> convertModelToMap(final CartModel cart, final Map<String, Object> parameters)
	{
		final Map<String, Object> map = new HashMap<>();
		final Optional<String> ymktID = Optional.ofNullable(cart.getYmktTrackingId()).filter(s -> !s.isEmpty());
		final UserModel user = cart.getUser();
		final boolean isAnonymous = UserConstants.ANONYMOUS_CUSTOMER_UID.equals(user.getUid()) && !ymktID.isPresent();

		//<Property Name="Key" Type="Edm.String" Nullable="false" MaxLength="32" sap:unicode="false" sap:label="Interaction Key" sap:updatable="false"/>
		map.put("Key", cart.getCode());

		//<Property Name="CommunicationMedium" Type="Edm.String" MaxLength="20" sap:unicode="false" sap:label="Communication Medium" sap:updatable="false"/>
		map.put("CommunicationMedium", "ONLINE_SHOP");

		//<Property Name="ContactId" Type="Edm.String" MaxLength="255" sap:unicode="false" sap:label="Contact Id" sap:updatable="false"/>
		final Optional<String> contactId = convertUserModelToContactId(user);
		if (!contactId.isPresent())
		{
			LOG.debug("Cannot map contact ID. Abandoned cart with code {} not sent.", cart.getCode());
			return Collections.emptyMap();
		}
		map.put("ContactId", isAnonymous ? null : contactId.get());

		//<Property Name="IsAnonymous" Type="Edm.Boolean" sap:unicode="false" sap:label="Is Anonymous" sap:updatable="false"/>
		map.put("IsAnonymous", Boolean.valueOf(isAnonymous));

		//<Property Name="ContactIdOrigin" Type="Edm.String" MaxLength="20" sap:unicode="false" sap:label="Contact Id Origin" sap:updatable="false"/>
		final String siteId = cart.getSite() != null ? cart.getSite().getUid():null;
		String siteIdOrigin  = null;
		if(StringUtils.isNotEmpty(siteId)) {
			List<CMSSiteModel> cmsSiteList = cmsSiteDao.findCMSSitesById(siteId);
			if(CollectionUtils.isNotEmpty(cmsSiteList)) {
				final CMSSiteModel cmsSiteModel = cmsSiteList.stream().findFirst().orElse(null);
				if(cmsSiteModel != null) {
					siteIdOrigin = GPSiteConfigUtils
							.getSiteConfig(cmsSiteModel,GpsapymktexpresswayConstants.GP_YMARKETING_SITEID);
					map.put("ContactIdOrigin", StringUtils.isNotBlank(siteIdOrigin) ?siteIdOrigin : DEFAULTSITE);
					final String marketingAreaId = GPSiteConfigUtils
							.getSiteConfig(cmsSiteModel,GpsapymktexpresswayConstants.GP_YMARKETING_AREA_ID);
					map.put("MarketingAreaId", StringUtils.isNotBlank(marketingAreaId)?marketingAreaId : DEFAULTSITE);

				}
			} else {
				LOG.warn(" Unable to find cmssite for {} " , siteId);
			}
		}

		//<Property Name="ContentTitle" Type="Edm.String" MaxLength="255" sap:unicode="false" sap:label="Content Title" sap:updatable="false" sap:sortable="false"/>
		map.put("ContentTitle", cart.getCode());

		//<Property Name="InteractionType" Type="Edm.String" MaxLength="20" sap:unicode="false" sap:label="Interaction Type" sap:updatable="false"/>
		map.put("InteractionType", "SHOP_CART_ABANDONED");

		//<Property Name="SourceObjectId" Type="Edm.String" MaxLength="50" sap:unicode="false" sap:label="Source Object ID" sap:updatable="false"/>
		map.put("SourceObjectId", cart.getCode());

		//<Property Name="SourceObjectType" Type="Edm.String" MaxLength="30" sap:unicode="false" sap:label="Source Object Type" sap:updatable="false"/>
		map.put("SourceObjectType", "SAP_HYBRIS_CART");

		//<Property Name="SourceSystemId" Type="Edm.String" MaxLength="255" sap:unicode="false" sap:label="Source System Id" sap:updatable="false"/>
		map.put(GpsapymktexpresswayConstants.SOURCE_SYSTEM_ID, siteIdOrigin != null ?siteIdOrigin : GpsapymktexpresswayConstants.DEFAULT_SOURCE_SYSTEM_ID);

		//<Property Name="SourceSystemType" Type="Edm.String" MaxLength="20" sap:unicode="false" sap:label="Source System Type" sap:updatable="false"/>
		map.put("SourceSystemType", "COM");

		//<Property Name="Timestamp" Type="Edm.DateTime" Precision="7" sap:unicode="false" sap:label="Timestamp" sap:updatable="false"/>
		map.put("Timestamp", cart.getModifiedtime());

		//<Property Name="Amount" Type="Edm.Decimal" Precision="31" Scale="2" sap:unicode="false" sap:unit="Currency" sap:label="Amount" sap:updatable="false"/>
		map.put("Amount", cart.getTotalPrice());

		//<Property Name="Currency" Type="Edm.String" MaxLength="3" sap:unicode="false" sap:label="Currency Code" sap:updatable="false" sap:sortable="false" sap:semantics="currency-code"/>
		map.put("Currency", cart.getCurrency().getIsocode());

		//<NavigationProperty Name="Products" Relationship="CUAN_IMPORT_SRV.InteractionInteractionProducts" FromRole="FromRole_InteractionInteractionProducts" ToRole="ToRole_InteractionInteractionProducts" sap:label="Products"/>
		final List<Map<String, Object>> products = cart.getEntries().stream() //
				.map(CartEntryModel.class::cast) //
				.map(cartEntry -> this.convertCartEntryModelToMap(cartEntry, parameters)) //
				.collect(Collectors.toList());
		map.put("Products", products);

		if (ymktID.isPresent())
		{
			final Map<String, Object> campaignReference = new HashMap<>();

			//<Property Name="ObjectId" Type="Edm.String" Nullable="false" MaxLength="50" sap:unicode="false" sap:label="Object ID" sap:updatable="false"/>
			campaignReference.put("ObjectId", ymktID.get());

			//<Property Name="ObjectType" Type="Edm.String" Nullable="false" MaxLength="30" sap:unicode="false" sap:label="Object Type" sap:updatable="false"/>
			campaignReference.put("ObjectType", "CUAN_CAMPAIGN_OUTBOUND");

			//<NavigationProperty Name="AdditionalObjectReferences" Relationship="CUAN_IMPORT_SRV.InteractionInteractionAdditionalObjRef" FromRole="FromRole_InteractionInteractionAdditionalObjRef"
			//ToRole="ToRole_InteractionInteractionAdditionalObjRef" sap:label="Additional Object References"/>
			map.put("AdditionalObjectReferences", campaignReference);
		}

		return map;
	}

	@Override
	protected String getImportHeaderNavigationProperty()
	{
		return "Interactions";
	}

	@Required
	public void setProductURLService(final ProductURLService productURLService)
	{
		this.productURLService = productURLService;
	}
}

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
package com.hybris.ymkt.gpsapymktexpressway.clickstream.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.zip.GZIPOutputStream;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.order.dao.GpOrderDao;
import com.gp.commerce.core.product.dao.GPProductDao;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.util.GPSiteConfigUtils;
import com.gp.ymkt.common.services.GPYmktProductService;
import com.hybris.ymkt.common.http.HttpURLConnectionRequest;
import com.hybris.ymkt.common.odata.ODataService;
import com.hybris.ymkt.common.product.ProductURLService;
import  com.hybris.ymkt.gpsapymktexpressway.constants.GpsapymktexpresswayConstants;

import de.hybris.eventtracking.model.events.AbstractCartAwareTrackingEvent;
import de.hybris.eventtracking.model.events.AbstractProductAndCartAwareTrackingEvent;
import de.hybris.eventtracking.model.events.AbstractProductAwareTrackingEvent;
import de.hybris.eventtracking.model.events.AbstractTrackingEvent;
import de.hybris.eventtracking.model.events.AddToCartEvent;
import de.hybris.eventtracking.model.events.SuccessfulCheckoutEvent;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSSiteDao;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;



/**
 * Send {@link AbstractTrackingEvent} to CUAN_IMPORT_SRV
 */
public class GPClickStreamService
{

	protected static final String APPLICATION_JSON = "application/json";
	protected static final String IMPORT_HEADERS = "ImportHeaders";

	private static final Logger LOG = LoggerFactory.getLogger(GPClickStreamService.class);

	protected static final String SAP_MERCH_SHOP = "SAP_MERCH_SHOP";

	protected static final Short SHORT_ONE = Short.valueOf((short) 1);
	protected static final Short SHORT_ZERO = Short.valueOf((short) 0);

	protected static final String SOURCE_OBJECT_ID = "SourceObjectId";
	protected static final String SOURCE_SYSTEM_ID = "SourceSystemId";
	protected static final String SOURCE_SYSTEM_TYPE = "SourceSystemType";

	protected static final String MARKETING_AREA_ID = "MarketingAreaId";

	protected static final Predicate<String> STRING_IS_EMPTY = String::isEmpty;
	protected static final Predicate<String> STRING_IS_EMPTY_NOT = STRING_IS_EMPTY.negate();

	protected static final String URL_PK_CAMPAIGN = "pk_campaign=";
	protected static final String URL_SAP_OUTBOUND_ID = "sap-outbound-id=";
	

	protected boolean linkAnonymousAndLoggedInUsers;
	protected final Map<String, String> interactionTypes = new HashMap<>();
	protected ODataService oDataService;
	protected ProductURLService productURLService;

	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;

	@Resource(name="gpProductDao")
	private GPProductDao productDao;

	@Resource(name = "impersonationService")
	private ImpersonationService impersonationService;

	@Resource
	private CMSSiteDao cmsSiteDao ;
	
	@Resource(name = "userService")
	private UserService userService;
	
	@Resource
	private BaseStoreService baseStoreService;
	
	@Resource(name="gpYmktProductService")
	private GPYmktProductService gpYmktProductService;
	
	
	@Resource
	private GpOrderDao gpOrderDao;

	

	protected byte[] compressGZIP(final byte[] payload) throws IOException
	{
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			try (GZIPOutputStream gzos = new GZIPOutputStream(baos))
			{
				gzos.write(payload);
			}
			return baos.toByteArray();
		}
	}

	protected Map<String, Object> createImportHeader()
	{
		final Map<String, Object> map = new LinkedHashMap<>();
		map.put("Id", "");
		map.put(SOURCE_SYSTEM_TYPE, "COM");
		map.put(SOURCE_SYSTEM_ID, SAP_MERCH_SHOP);
		return map;
	}

	protected Map<String, Object> createInteraction(final AbstractTrackingEvent event)
	{
		final Map<String, Object> interaction = new HashMap<>();

		this.populateInteraction(interaction, event);

		if (event instanceof AbstractProductAwareTrackingEvent)
		{
			Optional.of(event) //
					.map(AbstractProductAwareTrackingEvent.class::cast) //
					.map(AbstractProductAwareTrackingEvent::getProductName) //
					.filter(STRING_IS_EMPTY_NOT) //
					.ifPresent(name -> interaction.put("ContentTitle", name));
			interaction.put("Products", this.createInteractionProduct((AbstractProductAwareTrackingEvent) event));
		}

		if (event instanceof AbstractCartAwareTrackingEvent)
		{
			final AbstractCartAwareTrackingEvent eCart = (AbstractCartAwareTrackingEvent) event;
			if(StringUtils.isBlank(eCart.getCartId()) && StringUtils.isNotBlank(eCart.getEc_id())) {
				eCart.setCartId(eCart.getEc_id());
			}
			interaction.put(SOURCE_OBJECT_ID, eCart.getCartId());
			if(StringUtils.isNotEmpty(eCart.getCartId()) &&  event instanceof SuccessfulCheckoutEvent) {
				interaction.put("Products",createProductDetailsForOrder(event));
			}
		}
		else if (event instanceof AbstractProductAndCartAwareTrackingEvent)
		{
			final AbstractProductAndCartAwareTrackingEvent eCart = (AbstractProductAndCartAwareTrackingEvent) event;
			interaction.put(SOURCE_OBJECT_ID, eCart.getCartId());
		}
		else
		{
			interaction.put(SOURCE_OBJECT_ID, event.getSessionId());
		}

		return interaction;
	}

	protected Map<String, Object> createInteractionProduct(final AbstractProductAwareTrackingEvent event)
	{
		final Map<String, Object> product = new HashMap<>();

		if(StringUtils.isNotEmpty(event.getProductId())) {
			product.put("ItemId", event.getProductId());
			updateProductDetails(event.getProductId(), product,event.getIdsite());
		} else {
			LOG.warn(" Empty product for event {} " , event.getEventType());
		}

		product.put("ItemType", "SAP_HYBRIS_PRODUCT");

		Optional.ofNullable(event.getProductName()) //
				.filter(STRING_IS_EMPTY_NOT) //
				.ifPresent(name -> product.put("Name", name));



		if (event instanceof AbstractProductAndCartAwareTrackingEvent)
		{
			product.put("Key", ((AbstractProductAndCartAwareTrackingEvent) event).getCartId());
		}

		if (event instanceof AddToCartEvent)
		{
			product.put("Quantity", ((AddToCartEvent) event).getQuantity());
		}
        final String siteId = event.getIdsite() != null ? event.getIdsite() : GpsapymktexpresswayConstants.GP_DEFAULT;
        product.put(SOURCE_SYSTEM_ID, getYmarketingAttribute(siteId,GpsapymktexpresswayConstants.GP_YMARKETING_SITEID));

		return product;
	}
	
	/**
	 * Method to create interaction product for given event
	 * @param event
	 * @param productCode
	 * @return
	 */
	protected Map<String, Object> createInteractionProduct(final AbstractCartAwareTrackingEvent event, final String productCode)
	{
		final Map<String, Object> product = new HashMap<>();
		product.put("ItemId", productCode);
		updateProductDetails(productCode, product,event.getIdsite());
		product.put("ItemType", "SAP_HYBRIS_PRODUCT");
		product.put("Key", ((AbstractCartAwareTrackingEvent) event).getCartId());
		final String siteId = event.getIdsite() != null ? event.getIdsite() : GpsapymktexpresswayConstants.GP_DEFAULT;
		product.put(SOURCE_SYSTEM_ID, getYmarketingAttribute(siteId,GpsapymktexpresswayConstants.GP_YMARKETING_SITEID));
		return product;
	}
	
	/**
	 * Method to fetch the product details for order 
	 * @param orderCode , order code 
	 * @param event
	 * @return List of product details map
	 */
	private List <Map<String, Object>> createProductDetailsForOrder(final AbstractTrackingEvent event) {
		BaseStoreModel currentBaseStore = null;
		final List <Map<String, Object>> productList = new ArrayList<>();
		final AbstractCartAwareTrackingEvent eCart = (AbstractCartAwareTrackingEvent) event;
		final CMSSiteModel siteModel =  getCurrentSite(event.getIdsite());
		final List<BaseStoreModel> baseStores = null!= siteModel ? siteModel.getStores() : Collections.emptyList();
		if(CollectionUtils.isNotEmpty(baseStores)) {
			currentBaseStore = baseStores.stream().findFirst().orElse(null);
		}
		LOG.debug(" order code {} " , eCart.getCartId());
		List<OrderModel> orderList = getGpOrderDao().getOrderForCode(eCart.getCartId(),currentBaseStore);
		if(orderList != null && orderList.stream().findFirst().isPresent()) {
			final OrderModel orderModel = orderList.get(0);
			if(orderModel != null) {
				orderModel.getEntries().stream().forEach(entry-> {
					productList.add(this.createInteractionProduct((AbstractCartAwareTrackingEvent) event, entry.getProduct().getCode()));
				});
			} else {
				LOG.warn(" Order model null for {} ", eCart.getCartId());
			}
		}
		return productList;

	}

	/**
	 * Method to fetch product details and update it in product interactions
	 * @param productCode , product code
	 * @param product , Map containing product details
	 * @param siteId , site id for the interaction
	 */
	private void updateProductDetails(final String productCode,final Map<String, Object> product , final String siteId) {
		final ImpersonationContext impersonationContext = new ImpersonationContext();
		impersonationContext.setUser(getUserService().getAdminUser());
		final CMSSiteModel siteModel =  getCurrentSite(siteId);
		try
		{
			getImpersonationService().executeInContext(impersonationContext,
					new ImpersonationService.Executor<Boolean, Exception>()
					{
						@Override
						public Boolean execute() throws Exception
						{
							List<ProductModel> productModelList = productDao.getProductsForCodes(Collections.singletonList(productCode));
							if(CollectionUtils.isNotEmpty(productModelList)) {
								final ProductModel productModel = productModelList.stream().findFirst().orElse(null);
								if(null != productModel)
								{
									final Optional<String> productImageURL = gpYmktProductService.getProductImageURL(productModel);
									productImageURL.ifPresent(url -> product.put("ImageUrl",url));
									final String navigationurl = gpYmktProductService.getProductURL(siteModel, productModel);
									product.put("NavigationURL", navigationurl);
									product.put("Name" ,null!=productModel.getName()?productModel.getName():"");
									LOG.debug(" Navigation url for product {}" ,navigationurl);
								}
							}

							return Boolean.TRUE;
						}
					});
		}
		catch (Exception e)
		{
			LOG.error("Exception while fetching the product details",e);
		}
	}

	protected String extractInitiativeId(final String url1, final String url2)
	{
		final String param1 = this.extractURLParameter(URL_PK_CAMPAIGN, url1);
		final String param2 = this.extractURLParameter(URL_PK_CAMPAIGN, url2);
		return Optional.ofNullable(param1).orElse(param2);
	}

	protected String extractSAPTrackingId(final String url1, final String url2)
	{
		final String param1 = this.extractURLParameter(URL_SAP_OUTBOUND_ID, url1);
		final String param2 = this.extractURLParameter(URL_SAP_OUTBOUND_ID, url2);
		return Optional.ofNullable(param1).orElse(param2);
	}

	protected String extractURLParameter(final String parameterName, final String url)
	{
		if (url == null)
		{
			return null;
		}
		final int beginIndex = url.indexOf(parameterName) + parameterName.length();
		if (beginIndex == parameterName.length() - 1)
		{
			return null;
		}
		final int endIndex = url.indexOf('&', beginIndex);
		return endIndex == -1 ? url.substring(beginIndex) : url.substring(beginIndex, endIndex);
	}

	protected int getReadTimeout()
	{
		return 300000; // 5 Minutes
	}

	protected void populateInteraction(final Map<String, Object> interaction, final AbstractTrackingEvent event)
	{
		final String sapTrackingId = this.extractSAPTrackingId(event.getPageUrl(), event.getRefUrl());
		final boolean isTrackingId = sapTrackingId != null && !sapTrackingId.isEmpty();

		final String contactId = isTrackingId ? sapTrackingId : event.getYmktContactId();
        final String siteId =  StringUtils.isNotEmpty(event.getIdsite()) ? event.getIdsite() : null;


		// No anonymous tracking
		final boolean isAnonymous = contactId == null ? true : false;

		interaction.put("Key", Integer.toHexString(event.hashCode()));
		interaction.put("CommunicationMedium", "ONLINE_SHOP");

		if (!isAnonymous)
		{
			interaction.put("ContactId", contactId);
			interaction.put("ContactIdOrigin", getYmarketingAttribute(siteId,GpsapymktexpresswayConstants.GP_YMARKETING_SITEID));
		}

		Optional.ofNullable(this.extractInitiativeId(event.getPageUrl(), event.getRefUrl())) //
				.filter(STRING_IS_EMPTY_NOT) //
				.ifPresent(initiativeId -> interaction.put("InitiativeId", initiativeId));

		interaction.put("InteractionType", this.interactionTypes.get(event.getEventType()));
		interaction.put("IsAnonymous", Boolean.valueOf(isAnonymous));
		interaction.put("Quantifier", SHORT_ONE);
		interaction.put("SourceDataUrl", event.getPageUrl());

		interaction.put("SourceObjectType", isTrackingId ? "SAP_TRACKING_ID" : "WEB_SESSION");
		interaction.put(SOURCE_SYSTEM_ID, getYmarketingAttribute(siteId,GpsapymktexpresswayConstants.GP_YMARKETING_SITEID));
		interaction.put(SOURCE_SYSTEM_TYPE, "COM");
		interaction.put("Timestamp", Long.parseLong(event.getInteractionTimestamp()) * 1000);
		interaction.put("Valuation", SHORT_ZERO);
		interaction.put(MARKETING_AREA_ID, getYmarketingAttribute(siteId,GpsapymktexpresswayConstants.GP_YMARKETING_AREA_ID));

		if (isTrackingId)
		{
			final Map<String, Object> campaignReference = new HashMap<>();
			campaignReference.put("ObjectId", sapTrackingId);
			campaignReference.put("ObjectType", "CUAN_CAMPAIGN_OUTBOUND");
			interaction.put("AdditionalObjectReferences", campaignReference);
		}
	}

	/**
	 * Transform and send the {@link AbstractTrackingEvent}s to yMKT.
	 *
	 * @param events
	 *           {@link List} of {@link AbstractTrackingEvent} to send to yMKT.
	 * @return true if the transfer was successful. false otherwise.
	 */
	public boolean sendEvents(final List<? extends AbstractTrackingEvent> events)
	{
		try
		{
			final URL url = this.oDataService.createURL(IMPORT_HEADERS);
			final HttpURLConnectionRequest request = new HttpURLConnectionRequest("POST", url);
			request.getRequestProperties().put("Accept", APPLICATION_JSON);
			request.getRequestProperties().put("Content-Type", APPLICATION_JSON);
			request.setReadTimeout(this.getReadTimeout());

			final Map<String, Object> data = this.createImportHeader();

			final List<Map<String, Object>> interactions = new ArrayList<>();
			for (final AbstractTrackingEvent model : events)
			{
				interactions.add(this.createInteraction(model));
			}
			if(CollectionUtils.isNotEmpty(interactions)) {
				data.put(SOURCE_SYSTEM_ID,interactions.get(0).get(SOURCE_SYSTEM_ID) != null ? interactions.get(0).get(SOURCE_SYSTEM_ID):SAP_MERCH_SHOP);
			}
			data.put("Interactions", interactions);
			final byte[] payload = this.oDataService.convertMapToJSONPayload(IMPORT_HEADERS, data);
			final byte[] payloadGZIP = this.compressGZIP(payload);
			LOG.debug(" Clickstream Data {}", new String(payload, StandardCharsets.UTF_8));
			request.getRequestProperties().put("Content-Encoding", "gzip");
			request.setPayload(payloadGZIP);
			LOG.debug("GZIP compression {} Bytes -> {} Bytes, {}% reduction.", payload.length, payloadGZIP.length,
					(float) 100.0 * (payload.length - payloadGZIP.length) / payload.length);
			this.oDataService.executeWithRetry(request);

			return true;
		}
		catch (final IOException e)
		{
			LOG.error("Error sending '{}' events to YMKT", events.size(), e);
			return false;
		}
	}

	/**
	 *
	 * Links the anonymous and logged-in users by making the anonymous user a facet of the logged-in user.<br>
	 * In SAP Hybris Marketing, the anonymous & logged-in events are linked to a single contact golden record.
	 *
	 * @param anonymousUserId
	 *           User ID before login or register.
	 * @param anonymousUserOrigin
	 *           yMKT Origin ID.
	 * @param loggedInUserId
	 *           User ID after login or register.
	 * @param loggedInUserOrigin
	 *           yMKT Origin ID.
	 *
	 * @return <code>true</code> if the transfer was successful. <code>false</code> otherwise.
	 */
	public boolean linkAnonymousAndLoggedInUsers(final String anonymousUserId, final String anonymousUserOrigin,
			final String loggedInUserId, final String loggedInUserOrigin)
	{
		if (!this.linkAnonymousAndLoggedInUsers)
		{
			return false;
		}

		try
		{
			final URL url = this.oDataService.createURL(IMPORT_HEADERS);
			final HttpURLConnectionRequest request = new HttpURLConnectionRequest("POST", url);
			request.getRequestProperties().put("Accept", APPLICATION_JSON);
			request.getRequestProperties().put("Content-Type", APPLICATION_JSON);
			request.setReadTimeout(this.getReadTimeout());

			final Map<String, Object> importHeader = this.createImportHeader();

			final Map<String, Object> contact = new HashMap<>();
			contact.put("Id", anonymousUserId);
			contact.put("IdOrigin", anonymousUserOrigin);
			contact.put("Timestamp", new Date());

			final Map<String, Object> facet = new HashMap<>();
			facet.put("Id", loggedInUserId);
			facet.put("IdOrigin", loggedInUserOrigin);
			contact.put("Facets", facet);
			importHeader.put("Contacts", contact);

			final byte[] payload = this.oDataService.convertMapToJSONPayload(IMPORT_HEADERS, importHeader);

			final byte[] payloadGZIP = this.compressGZIP(payload);
			request.getRequestProperties().put("Content-Encoding", "gzip");
			request.setPayload(payloadGZIP);
			LOG.debug("GZIP compression {} Bytes -> {} Bytes, {}% reduction.", payload.length, payloadGZIP.length,
					(float) 100.0 * (payload.length - payloadGZIP.length) / payload.length);

			this.oDataService.executeWithRetry(request);

			return true;
		}
		catch (final IOException e)
		{
			LOG.error("Error sending anonymous facet to YMKT", e);
			return false;
		}
	}

	/**
	 * Method to get the site config attributes
	 * @param siteId , the site id parameter
	 * @param siteConfigKey , the site config key to be fetched
	 * @return ,site config parameter
	 */
	private String getYmarketingAttribute(String siteId,String siteConfigKey)
	{
		final CMSSiteModel cmsSiteModel = getCurrentSite(siteId);
		if(cmsSiteModel != null) {
			final String siteAttribute = GPSiteConfigUtils.getSiteConfig(cmsSiteModel,siteConfigKey);
			return StringUtils.isNotEmpty(siteAttribute) ?	siteAttribute:GpsapymktexpresswayConstants.GP_DEFAULT;
		}
		return GpsapymktexpresswayConstants.GP_DEFAULT;
	}
	
	
	/** 
	 * Method to get the current CMS Site with given siteID
	 * @param siteId , site id 
	 * @return CMSSIteModel
	 */
	private CMSSiteModel getCurrentSite(String siteId) {
		if(StringUtils.isNotEmpty(siteId)) {
			List<CMSSiteModel> cmsSiteList = cmsSiteDao.findCMSSitesById(siteId);
			if(CollectionUtils.isNotEmpty(cmsSiteList)) {
				return cmsSiteList.stream().findFirst().orElse(null);
			}else {
				LOG.warn(" Unable to find cmssite for {} " , siteId);
			}
		}else {
			LOG.warn("Clickstream empty site id ");
		}
		return null;
	}


	/**
	 * Build a map to link commerce event to yMKT event type.<br>
	 * SPRO - SAP Customizing Implementation Guide - SAP Hybris Marketing - Contacts and Profiles - Interactions - Define
	 * Interaction Types : <br>
	 * <ul>
	 * <li>PROD_REVIEW_VIEW - Product Review Read</li>
	 * <li>SHOP_CART_ABANDONED - Shopping Cart Abandoned</li>
	 * <li>SHOP_CART_VIEW - View Shopping Cart</li>
	 * <li><strike>SHOP_CHECKOUT_ABNDND - Checkout Abandoned</strike>. No matching commerce event.</li>
	 * <li>SHOP_CHECKOUT_START - Proceeded to Checkout</li>
	 * <li>SHOP_CHECKOUT_SUCCES - Checkout Successful</li>
	 * <li>SHOP_ITEM_ADD - Product Added to Shopping Cart</li>
	 * <li>SHOP_ITEM_REMOVE - Product Removed from Shopping Cart</li>
	 * <li>SHOP_ITEM_VIEW - Product Viewed</li>
	 * </ul>
	 *
	 * @param interactionTypeMapping
	 *           Mapping from ECP event type to yMKT interaction type.
	 */
	@Required
	public void setInteractionTypeMapping(final Map<String, String> interactionTypeMapping)
	{
		LOG.debug("interactionTypeMapping={}", interactionTypeMapping);
		this.interactionTypes.clear();
		interactionTypeMapping.forEach((k, v) -> this.interactionTypes.put(k.intern(), v.intern()));
	}

	@Required
	public void setODataService(final ODataService oDataService)
	{
		this.oDataService = oDataService;
	}

	@Required
	public void setProductURLService(final ProductURLService productURLService)
	{
		this.productURLService = productURLService;
	}

	@Required
	public void setLinkAnonymousAndLoggedInUsers(final boolean linkAnonymousAndLoggedInUsers)
	{
		LOG.debug("linkAnonymousAndLoggedInUsers={}", linkAnonymousAndLoggedInUsers);
		this.linkAnonymousAndLoggedInUsers = linkAnonymousAndLoggedInUsers;
	}


	public GPProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(GPProductDao productDao) {
		this.productDao = productDao;
	}

	public ImpersonationService getImpersonationService() {
		return impersonationService;
	}

	public void setImpersonationService(ImpersonationService impersonationService) {
		this.impersonationService = impersonationService;
	}

	public ProductURLService getProductURLService() {
		return productURLService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public BaseStoreService getBaseStoreService() {
		return baseStoreService;
	}

	public void setBaseStoreService(BaseStoreService baseStoreService) {
		this.baseStoreService = baseStoreService;
	}

	public GpOrderDao getGpOrderDao() {
		return gpOrderDao;
	}

	public void setGpOrderDao(GpOrderDao gpOrderDao) {
		this.gpOrderDao = gpOrderDao;
	}

	public GPYmktProductService getGpYmktProductService() {
		return gpYmktProductService;
	}

	public void setGpYmktProductService(GPYmktProductService gpYmktProductService) {
		this.gpYmktProductService = gpYmktProductService;
	}

	
	


}

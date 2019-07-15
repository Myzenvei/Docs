/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.constants.GeneratedCatalogConstants.Enumerations.ProductReferenceTypeEnum;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.couponservices.model.AbstractCouponModel;

import com.gp.commerce.core.model.CartProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.coupon.dao.GPCouponDAO;
import com.gp.commerce.core.dao.GPCartDao;
import com.gp.commerce.core.model.GPEndUserLegalTermsModel;
import com.gp.commerce.core.model.ShippingRestrictionModel;
import com.gp.commerce.core.model.SplitEntryModel;
import com.gp.commerce.core.services.GPCartService;
import com.gp.commerce.core.services.event.ShareCartEvent;
import de.hybris.platform.wishlist2.Wishlist2Service;
import de.hybris.platform.wishlist2.model.Wishlist2Model;
import de.hybris.platform.servicelayer.exceptions.BusinessException;
import com.gp.commerce.core.wishlist.services.GPWishlistService;
import reactor.util.CollectionUtils;

/**
 * This class is used for GP cart service
 */
public class DefaultGPCartService implements GPCartService{

	private static final Logger LOG = Logger.getLogger(DefaultGPCartService.class);
	private EventService eventService;
	private BusinessProcessService businessProcessService;
	private GPCartDao gpCartDao ;
	private GPCouponDAO gpCouponDao;

	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource
	private Wishlist2Service wishlistService;
	@Resource
	private GPWishlistService gpWishlistService;
	private static final String SHARE_CART_EMAIL_PROCESS = "shareCartEmailProcess";


	/**
	 * Share the products in the Cart as Email
	 * @param toEmail
	 *           Email address of the recipient
	 * @param cart
	 *           The cart in session.          
	 */
	@Override
	public void shareCart(final String toEmail,final String senderEmail,final String senderName, final CartModel cart) {
		 final Map<String, Object> map = new HashMap<>();
		map.put(GpcommerceCoreConstants.CART_LABEL, cart);
		final CartProcessModel cartProcessModel = (CartProcessModel) getBusinessProcessService()
				.createProcess(toEmail + cart.getCode() + System.currentTimeMillis(), SHARE_CART_EMAIL_PROCESS, map);
		 cartProcessModel.setCart(cart);
		 Wishlist2Model wishlistModel=null;
		 		 try {
		 			 wishlistModel = gpWishlistService.createShareCartWishList(cart);
		 		} catch (BusinessException e) {
		 			LOG.error(e.getMessage(),e);
		 		}
		 		cartProcessModel.setWishlist(wishlistModel);
		 final ShareCartEvent shareCartEvent = new ShareCartEvent(cartProcessModel);
		 shareCartEvent.setToEmail(toEmail);
		 shareCartEvent.setSenderEmail(senderEmail);
		 shareCartEvent.setSenderName(senderName);
		 getEventService().publishEvent(shareCartEvent);
	}

	@Override
	public  List<ShippingRestrictionModel>  fetchShippingRestrictions(final String productCode, final String country,final String state){
			return getGpCartDao().fetchShippingRestrictions( productCode,  country, state) ;
	}

	@Override
	public List<GPEndUserLegalTermsModel> getLeaseAgreementForCountry(final String country) {

		return getGpCartDao().getLeaseAgreementForCountry(country);
		
	}

	/**
	 * Check if there are incompatible products in cart.
	 * @param cart
	 *           The cart in session.
	 * @return true if there are incompatible products in cart, false otherwise.
	 */
	@Override
	public List<ProductModel> checkIncompatibleProducts(final CartModel cart)
	{
		//Extract all the asset codes present in the cart.
		final List<String> assetCodesInCart = cart.getEntries().stream()
				.filter(entry -> StringUtils.isNotEmpty(entry.getProduct().getAssetCode()))
				.map(entry -> entry.getProduct().getAssetCode()).collect(Collectors.toList());

		//Get the configured asset codes.
		final List<String> configuredCompatibleAssets = Arrays
				.asList(Config.getParameter(GpcommerceCoreConstants.COMPATIBLE_ASSET_CODES).split(","));

		//If no asset codes are available in the products or the compatible products are not configured then the logic will not move ahead.
		if (CollectionUtils.isEmpty(assetCodesInCart) || CollectionUtils.isEmpty(configuredCompatibleAssets))
		{
			return Collections.emptyList();
		}

		final List<ProductModel> dispenserProducts = new ArrayList<>();
		final List<ProductModel> refillProducts = new ArrayList<>();

		//For each dispenser:refill configuration, we will collect all the products for the dispenser asset code and the refill asset code separately from the cart.
		for (final String compatibleAsset : configuredCompatibleAssets)
		{
			final String[] array = compatibleAsset.split(":"); //array[0] will contain dispenser asset code and array[1] will contain the refill asset code.

			//If the cart contains a product of the configured refill asset code then collect the all the corresponding dispenser products as well as the refill products from the cart.
			if (assetCodesInCart.contains(array[1]))
			{
				//Extract the list of all the products from the cart whose asset code is not empty and is equal to array[0]. These are our dispenser products.
				dispenserProducts.addAll(cart.getEntries().stream()
						.filter(entry -> StringUtils.isNotEmpty(entry.getProduct().getAssetCode())
								&& entry.getProduct().getAssetCode().equals(array[0]))
						.map(entry -> entry.getProduct()).collect(Collectors.toList()));

				//Extract the list of all the products from the cart whose asset code is not empty and is equal to array[1]. These are our refill products.
				refillProducts.addAll(cart.getEntries().stream()
						.filter(entry -> StringUtils.isNotEmpty(entry.getProduct().getAssetCode())
								&& entry.getProduct().getAssetCode().equals(array[1]))
						.map(entry -> entry.getProduct()).collect(Collectors.toList()));
			}
		}

		//If there are no dispensers in cart then no compatibility has to be checked.
		if (CollectionUtils.isEmpty(dispenserProducts))
		{
			return Collections.emptyList();
		}

		//Out of all the refill products we collected above, remove those refill products which are there in the CROSSELLING product reference of at least 1 dispenser product in cart.
		//These are the compatible refills i.e for which there is at least 1 valid dispenser product in cart.
		final List<ProductModel> compatibleRefills = refillProducts.stream().filter(refill -> dispenserProducts.stream()
								.anyMatch(dispenser -> dispenser.getProductReferences().stream()
										.anyMatch(reference -> ProductReferenceTypeEnum.CROSSELLING.equals(
											reference.getReferenceType().getCode()) && reference.getTarget().equals(refill))))
				.collect(Collectors.toList());


		//Remove all the compatible refill products from the list of all refill products in cart.
		//This will give us the refill products which are not a CROSSELLING product reference of any dispenser product in cart. These are the incompatible refill products.
		refillProducts.removeAll(compatibleRefills);

		if (!CollectionUtils.isEmpty(refillProducts))
		{
			return refillProducts;
		}

		return Collections.emptyList();
	}

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(final EventService eventService) {
		this.eventService = eventService;
	}

	public BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}

	public void setBusinessProcessService(final BusinessProcessService businessProcessService) {
		this.businessProcessService = businessProcessService;
	}

	public GPCartDao getGpCartDao() {
		return gpCartDao;
	}

	public void setGpCartDao(final GPCartDao gpCartDao) {
		this.gpCartDao = gpCartDao;
	}

	@Override
	public void updateSplitEntry(final SplitEntryModel model)
	{
		if (null != model)
		{
			modelService.save(model);
		}
	}

	@Override
	public void updateDeliveryInstruction(final DeliveryModeModel deliverymode)
	{
		if (null != deliverymode)
		{
			modelService.save(deliverymode);
		}
	}

	@Override
	public void updateLease(final CartModel cart)
	{
		if (null != cart)
		{
			modelService.save(cart);
		}
	}

	@Override
	public CartModel getCartForCodeAndSite(String guid, BaseSiteModel currentBaseSite) {
		return gpCartDao.getCartForCodeAndSite(guid,currentBaseSite);
	}

	@Override
	public AbstractCouponModel getCouponForCode(String code) {
		return gpCouponDao.getCouponForCode(code);
	}

	@Required
	public void setGpCouponDao(GPCouponDAO gpCouponDao) {
		this.gpCouponDao = gpCouponDao;
	}
}

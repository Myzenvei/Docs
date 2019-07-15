/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.strategies;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.util.GPCommerceCoreUtils;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceAddToCartStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import javax.annotation.Resource;

public class GPDefaultCommerceAddToCartStrategy extends DefaultCommerceAddToCartStrategy {

	private GPCommerceCoreUtils gpCommerceCoreUtils;
	
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	
	
	
	
	/**
	 * Do add to cart.
	 *
	 * @param parameter
	 *           the parameter
	 * @return the commerce cart modification
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 */
	protected CommerceCartModification doAddToCart(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		CommerceCartModification modification;

		final CartModel cartModel = parameter.getCart();
		final ProductModel productModel = parameter.getProduct();
		final long quantityToAdd = parameter.getQuantity();
		final PointOfServiceModel deliveryPointOfService = parameter.getPointOfService();

		this.beforeAddToCart(parameter);
		validateAddToCart(parameter);

		if (isProductForCode(parameter).booleanValue())
		{
			// So now work out what the maximum allowed to be added is (note that this may be negative!)
			final long actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(cartModel, productModel, quantityToAdd,
					deliveryPointOfService);
			Integer maxOrderQuantity = getMaxOrderQuantityFromProduct(productModel);
			final long cartLevel = checkCartLevel(productModel, cartModel, deliveryPointOfService);
			final long cartLevelAfterQuantityChange = actualAllowedQuantityChange + cartLevel;

			if (actualAllowedQuantityChange > 0)
			{
				// We are allowed to add items to the cart
				final CartEntryModel entryModel = addCartEntry(parameter, actualAllowedQuantityChange);
				getModelService().save(entryModel);

				final String statusCode = getStatusCodeAllowedQuantityChange(actualAllowedQuantityChange, maxOrderQuantity,
						quantityToAdd, cartLevelAfterQuantityChange);

				modification = createAddToCartResp(parameter, statusCode, entryModel, actualAllowedQuantityChange);
			}
			else
			{
				// Not allowed to add any quantity, or maybe even asked to reduce the quantity
				// Do nothing!
				final String status = getStatusCodeForNotAllowedQuantityChange(maxOrderQuantity, maxOrderQuantity);

				modification = createAddToCartResp(parameter, status, createEmptyCartEntry(parameter), 0);

			}
		}
		else
		{
			modification = createAddToCartResp(parameter, CommerceCartModificationStatus.UNAVAILABLE,
					createEmptyCartEntry(parameter), 0);
		}

		return modification;
	}

	private Integer getMaxOrderQuantityFromProduct(final ProductModel productModel)
	{
		Integer maxOrderQuantity = (Integer) getGpCommerceCoreUtils().getProductAttribute(productModel, "maxOrderQuantity");
		
		//YCOM-12089 -> gpxpress change for sample product
		String gpxpressSiteId = configurationService.getConfiguration().getString(GpcommerceCoreConstants.GPXPRESS_SITE_ID);
		if(cmsSiteService.getCurrentSite().getUid().equalsIgnoreCase(gpxpressSiteId) && productModel.getSample())
		{
			maxOrderQuantity = productModel.getSampleOrderLimit();
		}
		return maxOrderQuantity;
	}
	
	protected long getAllowedCartAdjustmentForProduct(final CartModel cartModel, final ProductModel productModel,
			final long quantityToAdd, final PointOfServiceModel pointOfServiceModel)
	{
		final long cartLevel = checkCartLevel(productModel, cartModel, pointOfServiceModel);
		final long stockLevel = getAvailableStockLevel(productModel, pointOfServiceModel);

		// How many will we have in our cart if we add quantity
		final long newTotalQuantity = cartLevel + quantityToAdd;

		// Now limit that to the total available in stock
		final long newTotalQuantityAfterStockLimit = Math.min(newTotalQuantity, stockLevel);

		// So now work out what the maximum allowed to be added is (note that
		// this may be negative!)
		Integer maxOrderQuantity = getMaxOrderQuantityFromProduct(productModel);
		if (isMaxOrderQuantitySet(maxOrderQuantity))
		{
			final long newTotalQuantityAfterProductMaxOrder = Math
					.min(newTotalQuantityAfterStockLimit, maxOrderQuantity.longValue());
			return newTotalQuantityAfterProductMaxOrder - cartLevel;
		}
		return newTotalQuantityAfterStockLimit - cartLevel;
	}

	public GPCommerceCoreUtils getGpCommerceCoreUtils() {
		return gpCommerceCoreUtils;
	}

	public void setGpCommerceCoreUtils(GPCommerceCoreUtils gpCommerceCoreUtils) {
		this.gpCommerceCoreUtils = gpCommerceCoreUtils;
	}
	
}

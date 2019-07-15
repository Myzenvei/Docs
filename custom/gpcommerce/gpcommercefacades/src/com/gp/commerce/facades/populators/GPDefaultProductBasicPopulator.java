/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import java.util.List;

import javax.annotation.Resource;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.model.GPSubscriptionFrequencyModel;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.facades.subscription.data.GPSubscriptionFrequencyData;

import de.hybris.platform.commercefacades.product.converters.populator.ProductBasicPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;


/**
 * Populate the product data with the most basic product data
 */
public class GPDefaultProductBasicPopulator extends ProductBasicPopulator
{

	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	
	private static final String GPXPRESS_RETAILSOLDTO = "gpxpress.retail.soldto";
	@Override
	public void populate(final ProductModel productModel, final ProductData productData)
	{
		super.populate(productModel, productData);
		if(productModel != null && productData != null)
		{
			productData.setSellingstmt((String) getProductAttribute(productModel, ProductModel.SELLINGSTATEMENT));
			productData.setSubtitle((String) getProductAttribute(productModel, ProductModel.PRODUCTSUBTITLE));
			productData.setFeatureList((List<String>) getProductAttribute(productModel, ProductModel.FEATURELIST));
			Integer maxQty = (Integer) getProductAttribute(productModel, ProductModel.MAXORDERQUANTITY);
			if(maxQty != null && maxQty>0)
			{
				productData.setMaxOrderQuantity(maxQty);
			}
			else
			{
				productData.setMaxOrderQuantity(null);
			}
			Integer minQty = (Integer) getProductAttribute(productModel, ProductModel.MINORDERQUANTITY);
			if(minQty != null && minQty>0)
			{
				productData.setMinOrderQuantity(minQty);
			}
			else
			{
				productData.setMinOrderQuantity(null);
			}
			if(null != cmsSiteService.getSiteConfig(GPXPRESS_RETAILSOLDTO)&& cmsSiteService.getSiteConfig(GPXPRESS_RETAILSOLDTO).equals(getSessionService().getAttribute("soldToUnitId")))
			{
				productData.setPriceText((String)getProductAttribute(productModel, ProductModel.PRICETEXT));
			}
			String gpxpressSiteId = configurationService.getConfiguration().getString(GpcommerceCoreConstants.GPXPRESS_SITE_ID);
			if(cmsSiteService.getCurrentSite().getUid().equalsIgnoreCase(gpxpressSiteId) && productModel.getSample())
			{
				productData.setMaxOrderQuantity(productModel.getSampleOrderLimit());
			}
			productData.setCode(productModel.getCode());
			productData.setPromoText(((String) getProductAttribute(productModel, ProductModel.PROMOTEXT)));
		    
			Boolean subscribable= (Boolean) getProductAttribute(productModel, ProductModel.SUBSCRIBABLE);
			productData.setIsSubscribable(subscribable);
		
		}
		

	}
	public SessionService getSessionService() {
		return sessionService;
	}
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
}

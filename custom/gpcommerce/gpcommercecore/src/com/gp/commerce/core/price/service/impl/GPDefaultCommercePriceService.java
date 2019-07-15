/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.price.service.impl;

import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import com.gp.commerce.core.price.service.GPCommercePriceService;

/**
 * This class provides services for the product commerce pricing
 */
public class GPDefaultCommercePriceService implements GPCommercePriceService, CommercePriceService {

    @Resource(name = "gpPriceFactory")
    GPDefaultEurope1PriceFactory gpPriceFactory;

    @Resource(name = "modelService")
    private ModelService modelService;

    @Override
    public Double getMapPriceForProduct(final ProductModel product) {

        return new Double(gpPriceFactory.getFilteredPriceContainerForProducts(product).getMapPrice());
    }

    public GPDefaultEurope1PriceFactory getGpPriceFactory() {
        return gpPriceFactory;
    }

    public void setGpPriceFactory(GPDefaultEurope1PriceFactory gpPriceFactory) {
        this.gpPriceFactory = gpPriceFactory;
    }

    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    @Override
    public PriceInformation getFromPriceForProduct(ProductModel product) {
        return null;
    }

    @Override
    public PriceInformation getWebPriceForProduct(ProductModel product) {
        return null;
    }
}


/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.price.service;

import de.hybris.platform.core.model.product.ProductModel;

/**
 * This interface is for GP Commerce Price service
 */
public interface GPCommercePriceService {

    /**
     * Retrieve the MAP price for a product
     *
     * @param product the product
     * @return Double
     */
    Double getMapPriceForProduct(ProductModel product);

}

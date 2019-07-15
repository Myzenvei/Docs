/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.interceptor.validate;

import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;


/**
 * A validator intercepter for PriceRow model.
 */
public class PriceRowModelValidateInterceptor implements ValidateInterceptor<PriceRowModel> {
    @Override
    public void onValidate(final PriceRowModel priceRow, final InterceptorContext ctx) throws InterceptorException {
        final Double mapPrice = priceRow.getMapPrice();
        final Double webPrice = priceRow.getWeblistPrice();
        final Double price = priceRow.getPrice();
        boolean failed = false;
        if (null != mapPrice && null != price && price < mapPrice) {
            failed = true;
        } else if (null != mapPrice && null != webPrice && webPrice < mapPrice) {

            failed = true;
        }

        if (failed) {
            throw new InterceptorException("  Price or WebPrice cannot be less than MAP price. ");
        }

    }

}

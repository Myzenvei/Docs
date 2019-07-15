/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.order.strategies.calculation.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.strategies.calculation.impl.FindPricingWithCurrentPriceFactoryStrategy;
import de.hybris.platform.util.PriceValue;

public class GPFindPricingwithCurrentPriceFactoryStrategy extends FindPricingWithCurrentPriceFactoryStrategy {


    @Override
    public PriceValue findBasePrice(final AbstractOrderEntryModel entry) throws CalculationException
    {
        final AbstractOrderEntry entryItem = getModelService().getSource(entry);
        try
        {
            return getCurrentPriceFactory().getBasePrice(entryItem);
        }
        catch (final JaloPriceFactoryException e)
        {
            throw new CalculationException(e);
        }
    }


}

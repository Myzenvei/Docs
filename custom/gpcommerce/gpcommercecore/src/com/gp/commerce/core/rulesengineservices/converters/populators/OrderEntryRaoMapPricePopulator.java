/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.rulesengineservices.converters.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;

public class OrderEntryRaoMapPricePopulator implements Populator<AbstractOrderEntryModel, OrderEntryRAO> {


    @Override
    public void populate(AbstractOrderEntryModel source, OrderEntryRAO target)
            throws ConversionException {

        if (source != null && target != null) {
            Double mapPrice = source.getMapPrice();
            BigDecimal mPrice = null;
            if(mapPrice != null) {
                mPrice = BigDecimal.valueOf(mapPrice);

            }else{
                mPrice = BigDecimal.valueOf(0d);
            }
            target.setMapPrice(mPrice);

        }
    }
}

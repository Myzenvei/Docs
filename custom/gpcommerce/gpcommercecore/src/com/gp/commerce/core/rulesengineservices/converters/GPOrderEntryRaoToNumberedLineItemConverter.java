/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.rulesengineservices.converters;

import com.gp.commerce.core.rulesengineservices.calculation.GPNumberedLineItem;
import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import de.hybris.platform.ruleengineservices.calculation.NumberedLineItem;
import de.hybris.platform.ruleengineservices.converters.OrderEntryRaoToNumberedLineItemConverter;
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.servicelayer.util.ServicesUtil;

/**
 * Added to add MAPPrice to the LineItem
 */
public class GPOrderEntryRaoToNumberedLineItemConverter extends OrderEntryRaoToNumberedLineItemConverter {

    /**
     * override to add in the map price to the LineItem to be used in promotion calculation
     * @param entryRao
     * @return
     */
    @Override
    public NumberedLineItem convert(OrderEntryRAO entryRao) {

        ServicesUtil.validateParameterNotNull(entryRao, "order entry rao must not be null");
        ServicesUtil.validateParameterNotNull(entryRao.getOrder(), "corresponding entry cart rao must not be null");
        AbstractOrderRAO rao = entryRao.getOrder();
        Currency currency = (Currency)getAbstractOrderRaoToCurrencyConverter().convert(rao);
        Money money = new Money(entryRao.getBasePrice(), currency);
        GPNumberedLineItem lineItem = new GPNumberedLineItem(money, entryRao.getQuantity());
        lineItem.setEntryNumber(entryRao.getEntryNumber());
        if(entryRao.getMapPrice()!=null) {
        Money mapPriceMoney = new Money(entryRao.getMapPrice(),currency);
        lineItem.setMapPrice(mapPriceMoney);
        }

        return lineItem;
    }
}

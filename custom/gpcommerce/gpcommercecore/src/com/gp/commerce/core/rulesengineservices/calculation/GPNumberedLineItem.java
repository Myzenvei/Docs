/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.rulesengineservices.calculation;

import de.hybris.order.calculation.money.Money;
import de.hybris.platform.ruleengineservices.calculation.NumberedLineItem;

/**
 * Adding new attribute MAP price, so we can use it during promotion calculation
 */
public class GPNumberedLineItem extends NumberedLineItem {

    private Money mapPrice;

    public GPNumberedLineItem(Money basePrice) {
        super(basePrice);
    }

    public GPNumberedLineItem(Money basePrice, int numberOfUnits) {
        super(basePrice, numberOfUnits);
    }

    public Money getMapPrice() {
        return mapPrice;
    }

    public void setMapPrice(Money mapPrice) {
        this.mapPrice = mapPrice;
    }


}



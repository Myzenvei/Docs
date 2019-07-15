/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.rulesengineservices.calculation.impl;

import com.gp.commerce.core.price.service.GPCommercePriceService;
import com.gp.commerce.core.rulesengineservices.calculation.GPNumberedLineItem;
import de.hybris.order.calculation.domain.LineItem;
import de.hybris.order.calculation.domain.LineItemDiscount;
import de.hybris.order.calculation.money.Money;
import de.hybris.platform.ruleengineservices.calculation.NumberedLineItem;
import de.hybris.platform.ruleengineservices.calculation.impl.DefaultMinimumAmountValidationStrategy;

import javax.annotation.Resource;
import org.apache.log4j.Logger;


/**
 * This checks that the promotion won't violate the MAP price for a LineItem.  E.G.,  basePrice=$100, MAP Price=$90,
 * discount=$15, the totalPrice is below MAP price and the promotion will be removed.
 */
public class GPDefaultMinimumAmountValidationStrategy extends DefaultMinimumAmountValidationStrategy {

    @Resource(name="gpDefaultCommercePriceService")
    private GPCommercePriceService gpDefaultCommercePriceService;

    private static final Logger LOG = Logger.getLogger(GPDefaultMinimumAmountValidationStrategy.class);


    public boolean isLineItemLowerLimitValid(LineItem lineItem, LineItemDiscount discount) {

        if (lineItem.getDiscounts().contains(discount)) {
            throw new IllegalArgumentException("The line item already has the discount.");
        } else {
            boolean violatesMAPPricing;
            try {
                lineItem.addDiscount(discount);
                violatesMAPPricing = this.isLineItemLowerLimitValid(lineItem) &&
                        this.isOrderLowerLimitValid(lineItem.getOrder());

            } finally {
                lineItem.removeDiscount(discount);
            }

            return violatesMAPPricing;
        }
    }

	protected boolean isLineItemLowerLimitValid(LineItem lineItem) {

		boolean validTotal = true;
		double diff = 0d;

		if (lineItem instanceof NumberedLineItem) {

			GPNumberedLineItem gpNumberedLineItem = (GPNumberedLineItem) lineItem;
			Money mapPrice = gpNumberedLineItem.getMapPrice();
			int qty = lineItem.getNumberOfUnitsForCalculation();
			double totalMapPrice = 0.0d;
			if (mapPrice != null) {
				totalMapPrice = mapPrice.getAmount().doubleValue() * qty;
				Money liSubTotalWDiscounts = lineItem.getSubTotal().subtract(lineItem.getTotalDiscount());
				if (liSubTotalWDiscounts != null) {
					diff = totalMapPrice - liSubTotalWDiscounts.getAmount().doubleValue();
				}
			}
			if (diff > 0d) {
				validTotal = false;

				LOG.debug("LineItem Discount on order=" + lineItem.getOrder() + " violates the MAP Price of "
						+ totalMapPrice);

			}
		}

		return validTotal;
	}
}

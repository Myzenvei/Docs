/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.rulesengineservices.calculation.impl;

import de.hybris.order.calculation.domain.LineItem;
import de.hybris.order.calculation.domain.LineItemDiscount;
import de.hybris.order.calculation.money.AbstractAmount;
import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import de.hybris.platform.ruleengineservices.calculation.impl.DefaultRuleEngineCalculationService;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;

import java.math.BigDecimal;

/**
 * This class is for processing Rule Engine Calculation service
 */
public class GPDefaultRuleEngineCalculationService extends DefaultRuleEngineCalculationService {


	/**
	 * this is a fix for OOTB code that is using equals instead of compareTo and
	 * always returning false
	 * 
	 * @param lineItem        the line item
	 * @param amount          the amount to be processed
	 * @param applicableUnits the applicable units
	 * @param perUnit         the perunit measure
	 * @return {@link DiscountRAO}
	 */
    protected DiscountRAO createAbsoluteDiscountRAO(LineItem lineItem, BigDecimal amount, int applicableUnits, boolean perUnit) {

        int appliedToQuantity = perUnit ? applicableUnits : lineItem.getNumberOfUnits();
        Currency currency = lineItem.getBasePrice().getCurrency();
        BigDecimal adjustedAmount = amount.divide(BigDecimal.valueOf((long)appliedToQuantity), 10, 1);
        AbstractAmount discountAmount = new Money(adjustedAmount, currency);
        LineItemDiscount discount = new LineItemDiscount(discountAmount, true, appliedToQuantity);
        discount = this.validateLineItemDiscount(lineItem, true, currency, discount);
        DiscountRAO discountRAO = new DiscountRAO();
        discountRAO.setPerUnit(perUnit);
        discountRAO.setAppliedToQuantity((long)appliedToQuantity);
        Money money = (Money)discount.getAmount();

        //this is a fix for OOTB code that is using equals instead of compareTo and always returning false
        if(money.getAmount().compareTo(BigDecimal.ZERO) == 0){
            discountRAO.setValue(BigDecimal.ZERO);
        }else {
            discountRAO.setValue(adjustedAmount);
        }

        discountRAO.setCurrencyIsoCode(money.getCurrency().getIsoCode());
        return discountRAO;
    }



}

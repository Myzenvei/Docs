/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.common.math.DoubleMath;
import com.gp.commerce.core.services.GPDebitCalculationService;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;

public class GPDebitCalculationServiceImpl implements GPDebitCalculationService{
	
	 private static final double EPSILON = 0.0d;

	private static final Logger LOG = Logger.getLogger(GPDebitCalculationServiceImpl.class);
	
	private static final String DECIMAL_FORMAT = "##.00";
	
	@Override
	public BigDecimal calculateDebit(final Integer shippedQuantity, final ConsignmentEntryModel consignmentEntry)
	{
		DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
		if(LOG.isDebugEnabled()){
			LOG.debug("**** Inside GPDebitCalculationServiceImpl ****");
		}
		OrderEntryModel orderEntry = (OrderEntryModel) consignmentEntry.getOrderEntry();
		ConsignmentModel consignment = consignmentEntry.getConsignment();
		OrderModel order = (OrderModel) orderEntry.getOrder();
		BigDecimal adjOrderDiscount = BigDecimal.valueOf(0.0);
		BigDecimal adjShippingCost = BigDecimal.valueOf(0.0);
		BigDecimal adjTaxCost = BigDecimal.valueOf(0.0);
		BigDecimal debitAmount = BigDecimal.valueOf(0.0);
		int orderQuantity = 0;
		int orderEntryQty = orderEntry.getQuantity().intValue();
		int consignmentQty = 0;
		for(ConsignmentEntryModel consignmentEntryModel: consignment.getConsignmentEntries()) {
			consignmentQty = consignmentQty + consignmentEntryModel.getQuantity().intValue();
		}
		for (AbstractOrderEntryModel entry : order.getEntries()) 
		{
			orderQuantity = orderQuantity + entry.getQuantity().intValue();
		}
		BigDecimal adjItemCost = BigDecimal.valueOf((orderEntry.getTotalPrice()/orderEntryQty)*shippedQuantity);
		if(LOG.isDebugEnabled()){
			LOG.debug("Adjusted Item cost: " + adjItemCost);
		}
		if(consignmentEntry.getDiscountPrice() != null)
		{
			adjOrderDiscount = BigDecimal.valueOf((consignmentEntry.getDiscountPrice()/consignmentEntry.getQuantity())*shippedQuantity);
			if(LOG.isDebugEnabled()){
				LOG.debug("Adjusted Order Distcount: " + adjOrderDiscount);
			}
		}
		if(consignmentEntry.getDeliveryCost() != null)
		{
			adjShippingCost = BigDecimal.valueOf((consignmentEntry.getDeliveryCost()/consignmentQty)*shippedQuantity);
			if(LOG.isDebugEnabled()){
				LOG.debug("Adjusted Shipping Cost: " + adjShippingCost);
			}
		}
		if(consignmentEntry.getTotalTax() != null)
		{
			adjTaxCost = BigDecimal.valueOf((consignmentEntry.getTotalTax()/consignmentEntry.getQuantity())*shippedQuantity);
			if(LOG.isDebugEnabled()){
				LOG.debug(" Tax Cost: " + consignmentEntry.getTotalTax());
				LOG.debug("Adjusted Tax Cost: " + adjTaxCost);
			}
		}
		
		debitAmount = adjItemCost.add(adjShippingCost).add(adjTaxCost).subtract(adjOrderDiscount);
		if(LOG.isDebugEnabled()){
			LOG.debug("Total Debit Calculated: " + debitAmount);
		}
		if(isLastConsignmentEntry(consignmentEntry, order)) {
			LOG.info("This is last consignment Entry of last Consignment ");
			double orderPrice = order.getTotalPrice().doubleValue()+order.getTotalTax();
			double actualDebitAmount = 0.0d; 
			for(PaymentTransactionEntryModel transactionEntries : order.getPaymentTransactions().get(0).getEntries()) {
				if(transactionEntries.getType().equals(PaymentTransactionType.CAPTURE) && "ACCEPTED".equals(transactionEntries.getTransactionStatus())) {
					actualDebitAmount = actualDebitAmount+ transactionEntries.getAmount().doubleValue();
				}
			}
			BigDecimal ajustment = BigDecimal.valueOf(orderPrice).subtract(BigDecimal.valueOf(actualDebitAmount).add(new BigDecimal(decimalFormat.format(debitAmount))));
			if(DoubleMath.fuzzyCompare(ajustment.doubleValue(), 0, EPSILON)!=0) {
				debitAmount = new BigDecimal(decimalFormat.format(debitAmount.doubleValue())).add(ajustment);
				LOG.info("Debit adjustment needed: "+ajustment);
			}
		}
		
		return debitAmount;
	}
	
	private boolean isLastConsignmentEntry(final ConsignmentEntryModel consignmentEntry, OrderModel order) {
		int lastEntryIndex=0;
		for(ConsignmentModel consignment : consignmentEntry.getConsignment().getOrder().getConsignments()) {
			for(ConsignmentEntryModel entry : consignment.getConsignmentEntries()) {
				lastEntryIndex++;
			}
		}
		int debitIndex=0;
		for(PaymentTransactionEntryModel transactionEntries : order.getPaymentTransactions().get(0).getEntries()) {
			if(transactionEntries.getType().equals(PaymentTransactionType.CAPTURE) && "ACCEPTED".equals(transactionEntries.getTransactionStatus())) {
				debitIndex++;
			}
		}
		if(lastEntryIndex-debitIndex==1) {
			return true;
		}
		else {
			return false;
		}
			

	}
		
}

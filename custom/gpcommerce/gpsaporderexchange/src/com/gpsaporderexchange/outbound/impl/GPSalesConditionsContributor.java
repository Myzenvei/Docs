/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gpsaporderexchange.outbound.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.jalo.ConsignmentEntry;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.promotions.model.AbstractPromotionActionModel;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.SalesConditionCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.SaporderexchangeConstants;
import de.hybris.platform.sap.orderexchange.outbound.RawItemContributor;
import de.hybris.platform.sap.sapmodel.model.SAPPricingConditionModel;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.math.DoubleMath;
import com.gp.commerce.core.enums.ServiceProductTypeEnum;
import com.gp.commerce.core.model.GPCommerceProductModel;
import com.gp.commerce.core.model.GPServiceProductModel;
import com.gpsaporderexchange.constants.GPOrderCsvColumns;


/**
 * @author Siddharth Jain
 *
 */

/**
 * Builds the Row map for the CSV files for the Sales Conditions in an Order
 */
public class GPSalesConditionsContributor implements RawItemContributor<ConsignmentModel>
{

	private static final Logger LOGGER = LoggerFactory.getLogger(GPSalesConditionsContributor.class);
	// Header conditions
	private static final int CONDITION_COUNTER_DELIVERY_COST = 1;
	private static final int CONDITION_COUNTER_PAYMENT_COST = 2;
	// reserve condition counter 15-25 for order discount rows
	private static final int CONDITION_COUNTER_START_ORDER_DISCOUNT = 15;
	private static final int CONDITION_COUNTER_START_TAX = 25;

	// Item conditions
	private static final int CONDITION_COUNTER_SELL_PRICE = 3;
	private static final int CONDITION_COUNTER_WEB_PRICE = 4;
	// reserve condition counter 5-15 for product discount rows
	private static final int CONDITION_COUNTER_START_PRODUCT_DISCOUNT = 5;
	private static DecimalFormat formatter = new DecimalFormat("#.00");
	private static final String DECIMAL_FORMAT = "##.00";
	private static final double EPSILON = 0.0d;
	public static final String PROMOTION_DISCOUNT_CODE_PREFIX = "Action";
	private static final int PERUNITQUANTITY = 1;
	public static final String GP_HEADER_ENTRY = "0";

	private String tax1;
	private String grossPrice;
	private String deliveryCosts;
	private String paymentCosts;

	private int conditionCounterDeliveryCost = CONDITION_COUNTER_DELIVERY_COST;
	private int conditionCounterPaymentCost = CONDITION_COUNTER_PAYMENT_COST;
	private int conditionCounterTax = CONDITION_COUNTER_START_TAX;
	private int conditionCounterGrossPrice = CONDITION_COUNTER_SELL_PRICE;
	private int conditionCounterStartProductDiscount = CONDITION_COUNTER_START_PRODUCT_DISCOUNT;
	private int conditionCounterStartOrderDiscount = CONDITION_COUNTER_START_ORDER_DISCOUNT;

	private Map<String, String> batchIdAttributes;

	public Map<String, String> getBatchIdAttributes()
	{
		return batchIdAttributes;
	}

	@Required
	public void setBatchIdAttributes(final Map<String, String> batchIdAttributes)
	{
		this.batchIdAttributes = batchIdAttributes;
	}

	private RuleService ruleService;

	public RuleService getRuleService()
	{
		return ruleService;
	}

	@Required
	public void setRuleService(final RuleService ruleService)
	{
		this.ruleService = ruleService;
	}

	@Override
	public Set<String> getColumns()
	{
		final Set<String> columns = new HashSet<>(
				Arrays.asList(OrderCsvColumns.ORDER_ID, SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER,
						SalesConditionCsvColumns.CONDITION_CODE, SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE,
						SalesConditionCsvColumns.CONDITION_VALUE, SalesConditionCsvColumns.ABSOLUTE,
						SalesConditionCsvColumns.CONDITION_UNIT_CODE, SalesConditionCsvColumns.CONDITION_PRICE_QUANTITY,
						SalesConditionCsvColumns.CONDITION_COUNTER, GPOrderCsvColumns.CONDITION_ITEM_NUMBER_FOR_S4));
		columns.addAll(getBatchIdAttributes().keySet());
		return columns;
	}



	@Override
	public List<Map<String, Object>> createRows(final ConsignmentModel consignment)
	{
		final Set<ConsignmentEntryModel> entries = consignment.getConsignmentEntries();
		return syncPricingInactive(entries) ? createRowsHybrisPricing(consignment, entries)
				: createRowsSyncPricing(consignment, entries);

	}

	protected boolean syncPricingInactive(final Set<ConsignmentEntryModel> entries)
	{
		return entries.iterator().next().getOrderEntry().getSapPricingConditions() == null
				|| entries.iterator().next().getOrderEntry().getSapPricingConditions().isEmpty();
	}

	private List<Map<String, Object>> createRowsHybrisPricing(final ConsignmentModel consignment,
			final Set<ConsignmentEntryModel> entries)
	
	{
		
		final List<Map<String, Object>> result = new ArrayList<>();
		final Double basePriceTotal = getTotal(consignment);		
		
		for (final ConsignmentEntryModel entry : entries)
		{
			createWeblistPriceRow(consignment, result, entry);
			createSellPriceRow(consignment, result, entry);
			createItemLevelPromoPriceRow(consignment, result, entry);
			createSalesTaxRows(consignment, result, entry, basePriceTotal,
			calculateItemTaxWithDecimalCorrection(consignment, entry));
			createGiftWrapPriceRow(consignment, result, entry);

			if (isInstallable(entry)) {
				List<ConsignmentEntryModel> gpInstallLineItemEntries = installationItem(consignment, entries);

				if (gpInstallLineItemEntries != null) 
				   {

					for (final ConsignmentEntryModel matchEntry : gpInstallLineItemEntries) {
						ProductModel prodMain = entry.getOrderEntry().getProduct();
						ProductModel prodInst = matchEntry.getOrderEntry().getProduct();
						if (matchEntry != null && prodMain instanceof GPCommerceProductModel) 
						   {
							
							GPCommerceProductModel product = ((GPCommerceProductModel) prodMain)
									.getInstallationProduct();
							if (product.equals(prodInst)) {
								LOGGER.info("Main Product Entry Number *******************  " + entry.getOrderEntry());
								LOGGER.info("Installation Product Entry Number *******************  "
										+ matchEntry.getOrderEntry());
								LOGGER.info("gpInstallLineItemEntry Product Code *******************  " + prodInst);
								createInstallablePriceRow(consignment, result, entry, matchEntry);
							}

						}

					}
				}

			}

		}
		createDeliveryCostRow(consignment, result);
		createOrderLevelPromoPriceRow(consignment, result);


		return result;
	}
	
	
	
	
	
	private boolean isInstallable(final ConsignmentEntryModel entry)
	{
		ProductModel prod = entry.getOrderEntry().getProduct();
		if(prod instanceof GPCommerceProductModel)
		{
			
			Boolean installable = ((GPCommerceProductModel) prod).getInstallationProduct() != null && ((GPCommerceProductModel) prod).getInstallationProduct() instanceof GPServiceProductModel;
			LOGGER.info("Installable Decision  *********************** "+installable);
			return installable;
		}
		else
		{
			return false;
		}
			
		
		
	}
	

	private boolean isLastConsignmentEntry(final ConsignmentModel consignment, final ConsignmentEntryModel entry)
	{

		final Iterator<ConsignmentEntryModel> itr = consignment.getConsignmentEntries().iterator();
		ConsignmentEntryModel lastElement = itr.next();
		while (itr.hasNext())
		{
			lastElement = itr.next();
		}
		if (lastElement.getConsignmentEntryNumber().intValue() == entry.getConsignmentEntryNumber().intValue())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private boolean isLastConsignment(final ConsignmentModel consignment)
	{

		final Iterator<ConsignmentModel> itr = consignment.getOrder().getConsignments().iterator();
		ConsignmentModel lastElement = itr.next();
		while (itr.hasNext())
		{
			lastElement = itr.next();
		}
		if (lastElement.getCode().equalsIgnoreCase(consignment.getCode()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private double calculateItemTaxWithDecimalCorrection(final ConsignmentModel consignment, final ConsignmentEntryModel entry)
	{
		double adjustmentTax = 0.0;
		final DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
		if (isLastConsignment(consignment) && isLastConsignmentEntry(consignment, entry))
		{
			double totalTax = 0.0;
			for (final ConsignmentModel cons : consignment.getOrder().getConsignments())
			{
				double totalConsignmentTax = 0.0;
				for (final ConsignmentEntryModel consignmentEntry : cons.getConsignmentEntries())
				{
					if (null != consignmentEntry.getTotalTax())
					{
						totalConsignmentTax = totalConsignmentTax + consignmentEntry.getTotalTax();
					}
				}
				totalTax = totalTax + totalConsignmentTax;
			}
			if (DoubleMath.fuzzyCompare(
					Double.valueOf(decimalFormat.format((consignment.getOrder().getTotalTax().doubleValue() - totalTax))), 0,
					EPSILON) != 0)
			{
				adjustmentTax = Double.valueOf(decimalFormat.format(consignment.getOrder().getTotalTax().doubleValue() - totalTax));
			}
		}

		return Double.valueOf(decimalFormat.format(entry.getTotalTax() + adjustmentTax));
	}
	
	private List<ConsignmentEntryModel> installationItem(final ConsignmentModel consignment,
			final Set<ConsignmentEntryModel> entries)
	
	{	
		
		List<ConsignmentEntryModel> installationEntries =  entries.stream()  
                .filter(entry ->entry.getOrderEntry().getProduct() instanceof GPServiceProductModel)  
                .collect(Collectors.toList());  
        System.out.println(installationEntries);
        return CollectionUtils.isNotEmpty(installationEntries) ? installationEntries : null;        
		
	}
	

	private List<Map<String, Object>> createRowsSyncPricing(final ConsignmentModel consignment,
			final Set<ConsignmentEntryModel> entries)
	{
		final List<Map<String, Object>> result = new ArrayList<>();

		for (final ConsignmentEntryModel entry : entries)
		{
			final Iterator<SAPPricingConditionModel> it = entry.getOrderEntry().getSapPricingConditions().iterator();
			while (it.hasNext())
			{
				final SAPPricingConditionModel condition = it.next();
				final Map<String, Object> row = new HashMap<>();

				row.put(OrderCsvColumns.ORDER_ID, consignment.getCode());
				row.put(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER, entry.getConsignmentEntryNumber());
				row.put(GPOrderCsvColumns.CONDITION_ITEM_NUMBER_FOR_S4, ((entry.getConsignmentEntryNumber().intValue() + 1) * 10));
				row.put(SalesConditionCsvColumns.CONDITION_CODE, condition.getConditionType());
				row.put(SalesConditionCsvColumns.CONDITION_VALUE, condition.getConditionRate());
				row.put(SalesConditionCsvColumns.CONDITION_UNIT_CODE, condition.getConditionUnit());
				row.put(SalesConditionCsvColumns.CONDITION_PRICE_QUANTITY, condition.getConditionPricingUnit());
				row.put(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, condition.getCurrencyKey());
				row.put(SalesConditionCsvColumns.CONDITION_COUNTER, condition.getConditionCounter());
				getBatchIdAttributes().forEach(row::putIfAbsent);
				row.put("dh_batchId", consignment.getCode());
				result.add(row);
			}
		}
		return result;
	}



	public static List<DiscountValue> safe(final List<DiscountValue> other)
	{
		return other == null ? Collections.emptyList() : other;
	}

	public static <T> Iterable<T> emptyIfNull(final Iterable<T> iterable)
	{
		return iterable == null ? Collections.<T> emptyList() : iterable;
	}



	protected void createWeblistPriceRow(final ConsignmentModel consignment, final List<Map<String, Object>> result,
			final ConsignmentEntryModel entry)
	{
		if (entry.getOrderEntry().getProduct().getEurope1Prices().iterator().next().getWeblistPrice() != null)
		{
			final Map<String, Object> row = new HashMap<>();
			row.put(OrderCsvColumns.ORDER_ID, consignment.getCode());
			row.put(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER, entry.getConsignmentEntryNumber());
			row.put(GPOrderCsvColumns.CONDITION_ITEM_NUMBER_FOR_S4, ((entry.getConsignmentEntryNumber().intValue() + 1) * 10));
			row.put(SalesConditionCsvColumns.CONDITION_CODE, "ZPB4");
			row.put(SalesConditionCsvColumns.CONDITION_VALUE,
					entry.getOrderEntry().getProduct().getEurope1Prices().iterator().next().getWeblistPrice());
			row.put(SalesConditionCsvColumns.CONDITION_UNIT_CODE, entry.getOrderEntry().getUnit().getCode());
			row.put(SalesConditionCsvColumns.CONDITION_PRICE_QUANTITY, entry.getQuantity());
			row.put(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, consignment.getOrder().getCurrency().getIsocode());
			row.put(SalesConditionCsvColumns.ABSOLUTE, Boolean.TRUE);
			row.put(SalesConditionCsvColumns.CONDITION_COUNTER, CONDITION_COUNTER_WEB_PRICE);
			getBatchIdAttributes().forEach(row::putIfAbsent);
			row.put("dh_batchId", consignment.getCode());
			result.add(row);
		}
	}


	protected void createSellPriceRow(final ConsignmentModel consignment, final List<Map<String, Object>> result,
			final ConsignmentEntryModel entry)
	{
		final Map<String, Object> row = new HashMap<>();
		row.put(OrderCsvColumns.ORDER_ID, consignment.getCode());
		row.put(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER, entry.getConsignmentEntryNumber());
		row.put(GPOrderCsvColumns.CONDITION_ITEM_NUMBER_FOR_S4, ((entry.getConsignmentEntryNumber().intValue() + 1) * 10));
		row.put(SalesConditionCsvColumns.CONDITION_CODE, "ZHP1");
		row.put(SalesConditionCsvColumns.CONDITION_VALUE, entry.getOrderEntry().getBasePrice());
		row.put(SalesConditionCsvColumns.CONDITION_UNIT_CODE, entry.getOrderEntry().getUnit().getCode());
		row.put(SalesConditionCsvColumns.CONDITION_PRICE_QUANTITY, PERUNITQUANTITY);
		row.put(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, consignment.getOrder().getCurrency().getIsocode());
		row.put(SalesConditionCsvColumns.ABSOLUTE, Boolean.TRUE);
		row.put(SalesConditionCsvColumns.CONDITION_COUNTER, CONDITION_COUNTER_SELL_PRICE);
		getBatchIdAttributes().forEach(row::putIfAbsent);
		row.put("dh_batchId", consignment.getCode());
		result.add(row);
	}


	protected void createItemLevelPromoPriceRow(final ConsignmentModel consignment, final List<Map<String, Object>> result,
			final ConsignmentEntryModel entry)
	{
		final Map<String, Object> row = new HashMap<>();
		row.put(OrderCsvColumns.ORDER_ID, consignment.getCode());
		row.put(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER, entry.getConsignmentEntryNumber());
		row.put(GPOrderCsvColumns.CONDITION_ITEM_NUMBER_FOR_S4, ((entry.getConsignmentEntryNumber().intValue() + 1) * 10));
		row.put(SalesConditionCsvColumns.CONDITION_CODE, "ZHD2");
		final Double totalPromoValue = Double.valueOf(formatter.format(calculateItemLevelPromo(entry)));
		final Double promoValueToS4 = totalPromoValue * -1;
		row.put(SalesConditionCsvColumns.CONDITION_VALUE, promoValueToS4);
		row.put(SalesConditionCsvColumns.CONDITION_UNIT_CODE, entry.getOrderEntry().getUnit().getCode());
		row.put(SalesConditionCsvColumns.CONDITION_PRICE_QUANTITY, entry.getQuantity());
		row.put(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, consignment.getOrder().getCurrency().getIsocode());
		row.put(SalesConditionCsvColumns.ABSOLUTE, Boolean.TRUE);
		row.put(SalesConditionCsvColumns.CONDITION_COUNTER, CONDITION_COUNTER_START_PRODUCT_DISCOUNT);
		getBatchIdAttributes().forEach(row::putIfAbsent);
		row.put("dh_batchId", consignment.getCode());
		result.add(row);
	}

	private double calculateItemLevelPromo(final ConsignmentEntryModel entry)
	{
		final double perUnitPromoValue = ((entry.getOrderEntry().getBasePrice().doubleValue()
				* entry.getOrderEntry().getQuantity().intValue()) - entry.getOrderEntry().getTotalPrice().doubleValue())
				/ entry.getOrderEntry().getQuantity().intValue();
		final double conEntryPromoValue = perUnitPromoValue * entry.getQuantity().intValue();
		return conEntryPromoValue;

	}

	protected void createSalesTaxRows(final ConsignmentModel consignment, final List<Map<String, Object>> result,
			final ConsignmentEntryModel entry, final Double sum, final double tax)
	{
		final Iterator<TaxValue> taxIterator = entry.getTotalTaxValues().iterator();
		while (taxIterator.hasNext())
		{
			final TaxValue next = taxIterator.next();
			final Map<String, Object> row = new HashMap<>();
			row.put(OrderCsvColumns.ORDER_ID, consignment.getCode());
			row.put(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER, entry.getConsignmentEntryNumber());
			row.put(GPOrderCsvColumns.CONDITION_ITEM_NUMBER_FOR_S4, ((entry.getConsignmentEntryNumber().intValue() + 1) * 10));
			row.put(SalesConditionCsvColumns.CONDITION_CODE, "ZHS4");
			LOGGER.info("totalItemTax for entry:::::::::::::::::::: " + entry.getConsignmentEntryNumber() + "---" + tax);
			row.put(SalesConditionCsvColumns.CONDITION_VALUE, tax);
			row.put(SalesConditionCsvColumns.CONDITION_COUNTER, CONDITION_COUNTER_START_TAX);

			if (next.isAbsolute())
			{
				row.put(SalesConditionCsvColumns.ABSOLUTE, Boolean.TRUE);
				row.put(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, consignment.getOrder().getCurrency().getIsocode());
				row.put(SalesConditionCsvColumns.CONDITION_UNIT_CODE, entry.getOrderEntry().getUnit().getCode());
				row.put(SalesConditionCsvColumns.CONDITION_PRICE_QUANTITY, entry.getQuantity());
			}
			else
			{
				row.put(SalesConditionCsvColumns.ABSOLUTE, Boolean.FALSE);
			}

			getBatchIdAttributes().forEach(row::putIfAbsent);
			row.put("dh_batchId", consignment.getCode());

			result.add(row);
			break;
		}
	}

	private Double getTotal(final ConsignmentModel consignment)
	{

		Double sum = 0.0;
		for (final ConsignmentEntryModel entry : consignment.getConsignmentEntries())
		{
			sum = sum + entry.getOrderEntry().getBasePrice();
		}
		return sum;
	}


	protected void createGiftWrapPriceRow(final ConsignmentModel consignment, final List<Map<String, Object>> result,
			final ConsignmentEntryModel entry)
	{
		if (entry.getOrderEntry().getProduct() instanceof GPServiceProductModel)
		{
			final GPServiceProductModel product = (GPServiceProductModel) entry.getOrderEntry().getProduct();
			if (product.getServiceType() != null
					&& product.getServiceType().getCode().equals(ServiceProductTypeEnum.GIFT_WRAP.getCode()))
			{
				final Map<String, Object> row = new HashMap<>();
				row.put(OrderCsvColumns.ORDER_ID, consignment.getCode());
				row.put(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER, entry.getConsignmentEntryNumber());
				row.put(GPOrderCsvColumns.CONDITION_ITEM_NUMBER_FOR_S4, ((entry.getConsignmentEntryNumber().intValue() + 1) * 10));
				row.put(SalesConditionCsvColumns.CONDITION_CODE, "ZHS2");
				row.put(SalesConditionCsvColumns.CONDITION_VALUE, entry.getOrderEntry().getBasePrice());
				row.put(SalesConditionCsvColumns.CONDITION_UNIT_CODE, entry.getOrderEntry().getUnit().getCode());
				row.put(SalesConditionCsvColumns.CONDITION_PRICE_QUANTITY, entry.getQuantity());
				row.put(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, consignment.getOrder().getCurrency().getIsocode());
				row.put(SalesConditionCsvColumns.ABSOLUTE, Boolean.TRUE);
				row.put(SalesConditionCsvColumns.CONDITION_COUNTER, CONDITION_COUNTER_SELL_PRICE);
				getBatchIdAttributes().forEach(row::putIfAbsent);
				row.put("dh_batchId", consignment.getCode());
				result.add(row);
			}
		}
	}

	protected void createInstallablePriceRow(final ConsignmentModel consignment, final List<Map<String, Object>> result,
			final ConsignmentEntryModel entry, final ConsignmentEntryModel gpInstallLineItemEntry)
	{			
				final Map<String, Object> row = new HashMap<>();
				row.put(OrderCsvColumns.ORDER_ID, consignment.getCode());
				row.put(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER, entry.getConsignmentEntryNumber());
				row.put(GPOrderCsvColumns.CONDITION_ITEM_NUMBER_FOR_S4, ((entry.getConsignmentEntryNumber().intValue() + 1) * 10));
				row.put(SalesConditionCsvColumns.CONDITION_CODE, "ZHS1");
				row.put(SalesConditionCsvColumns.CONDITION_VALUE, gpInstallLineItemEntry.getOrderEntry().getBasePrice());
				row.put(SalesConditionCsvColumns.CONDITION_UNIT_CODE, entry.getOrderEntry().getUnit().getCode());
				row.put(SalesConditionCsvColumns.CONDITION_PRICE_QUANTITY, entry.getQuantity());
				row.put(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, consignment.getOrder().getCurrency().getIsocode());
				row.put(SalesConditionCsvColumns.ABSOLUTE, Boolean.TRUE);
				row.put(SalesConditionCsvColumns.CONDITION_COUNTER, CONDITION_COUNTER_SELL_PRICE);
				getBatchIdAttributes().forEach(row::putIfAbsent);
				row.put("dh_batchId", consignment.getCode());
				result.add(row);		
	}

	protected void createDeliveryCostRow(final ConsignmentModel consignment, final List<Map<String, Object>> result)
	{
		final Map<String, Object> row = new HashMap<>();
		row.put(OrderCsvColumns.ORDER_ID, consignment.getCode());
		row.put(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER, SaporderexchangeConstants.HEADER_ENTRY);
		row.put(GPOrderCsvColumns.CONDITION_ITEM_NUMBER_FOR_S4, GP_HEADER_ENTRY);
		row.put(SalesConditionCsvColumns.CONDITION_CODE, "ZHF1");
		row.put(SalesConditionCsvColumns.CONDITION_VALUE, consignment.getConsignmentEntries().iterator().next().getDeliveryCost());
		row.put(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, consignment.getOrder().getCurrency().getIsocode());
		row.put(SalesConditionCsvColumns.CONDITION_COUNTER, getConditionCounterDeliveryCost());
		row.put(SalesConditionCsvColumns.ABSOLUTE, Boolean.TRUE);
		getBatchIdAttributes().forEach(row::putIfAbsent);
		row.put("dh_batchId", consignment.getCode());
		result.add(row);
	}

	protected void createOrderLevelPromoPriceRow(final ConsignmentModel consignment, final List<Map<String, Object>> result)
	{
		final Map<String, Object> row = new HashMap<>();
		row.put(OrderCsvColumns.ORDER_ID, consignment.getCode());
		row.put(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER, SaporderexchangeConstants.HEADER_ENTRY);
		row.put(GPOrderCsvColumns.CONDITION_ITEM_NUMBER_FOR_S4, GP_HEADER_ENTRY);
		row.put(SalesConditionCsvColumns.CONDITION_CODE, "ZHD1");
		final Double oderPromoValueToS4 = (consignment.getProratedDiscount() != null ? consignment.getProratedDiscount() : 0.0d)
				* -1;
		row.put(SalesConditionCsvColumns.CONDITION_VALUE, oderPromoValueToS4);
		row.put(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, consignment.getOrder().getCurrency().getIsocode());
		row.put(SalesConditionCsvColumns.CONDITION_COUNTER, CONDITION_COUNTER_START_PRODUCT_DISCOUNT);
		row.put(SalesConditionCsvColumns.ABSOLUTE, Boolean.TRUE);
		getBatchIdAttributes().forEach(row::putIfAbsent);
		row.put("dh_batchId", consignment.getCode());
		result.add(row);
	}


	// determine sap code corresponding to hybris promotion code
	protected String determinePromotionDiscountCode(final AbstractOrderModel order, final DiscountValue discountValue)
	{

		final AbstractPromotionActionModel abstractAction = order.getAllPromotionResults().stream()
				.flatMap(pr -> pr.getActions().stream()).filter(action -> action.getGuid().equals(discountValue.getCode()))
				.collect(Collectors.toList()).stream().map(Optional::ofNullable).findFirst().flatMap(Function.identity())
				.orElse(null);

		if (abstractAction != null && abstractAction instanceof AbstractRuleBasedPromotionActionModel)
		{

			final AbstractRuleModel rule = getRuleService()
					.getRuleForCode(((AbstractRuleBasedPromotionActionModel) abstractAction).getRule().getCode());

			if (rule != null)
			{

				if (rule.getSapConditionType() != null)
				{
					return rule.getSapConditionType();
				}
				else
				{
					LOGGER.warn(String.format(
							"The promotion rule with code [%s] is missing the SAP Condition Type; therefore, the promotion discount has not been sent to SAP-ERP!",
							rule.getCode()));
				}
				return null;
			}

		}

		LOGGER.warn(String.format(
				"The promotion rule with discount value [%s] is not configured properly; therefore, the promotion discount has not been sent to SAP-ERP!",
				discountValue));
		return null;

	}

	@SuppressWarnings("javadoc")
	@Required
	public void setTax1(final String tax1)
	{
		this.tax1 = tax1;
	}

	@SuppressWarnings("javadoc")
	public void setGrossPrice(final String grossPrice)
	{
		this.grossPrice = grossPrice;
	}

	@SuppressWarnings("javadoc")
	public void setDeliveryCosts(final String deliveryCosts)
	{
		this.deliveryCosts = deliveryCosts;
	}

	@SuppressWarnings("javadoc")
	public void setPaymentCosts(final String paymentCosts)
	{
		this.paymentCosts = paymentCosts;
	}

	@SuppressWarnings("javadoc")
	public int getConditionCounterDeliveryCost()
	{
		return conditionCounterDeliveryCost;
	}

	@SuppressWarnings("javadoc")
	public void setConditionCounterDeliveryCost(final int conditionCounterDeliveryCost)
	{
		this.conditionCounterDeliveryCost = conditionCounterDeliveryCost;
	}

	@SuppressWarnings("javadoc")
	public int getConditionCounterPaymentCost()
	{
		return conditionCounterPaymentCost;
	}

	@SuppressWarnings("javadoc")
	public void setConditionCounterPaymentCost(final int conditionCounterPaymentCost)
	{
		this.conditionCounterPaymentCost = conditionCounterPaymentCost;
	}

	@SuppressWarnings("javadoc")
	public int getConditionCounterTax()
	{
		return conditionCounterTax;
	}

	@SuppressWarnings("javadoc")
	public void setConditionCounterTax(final int conditionCounterTax)
	{
		this.conditionCounterTax = conditionCounterTax;
	}

	@SuppressWarnings("javadoc")
	public int getConditionCounterStartProductDiscount()
	{
		return conditionCounterStartProductDiscount;
	}

	@SuppressWarnings("javadoc")
	public void setConditionCounterStartProductDiscount(final int conditionCounterStartProductDiscount)
	{
		this.conditionCounterStartProductDiscount = conditionCounterStartProductDiscount;
	}

	@SuppressWarnings("javadoc")
	public int getConditionCounterStartOrderDiscount()
	{
		return conditionCounterStartOrderDiscount;
	}

	@SuppressWarnings("javadoc")
	public void setConditionCounterStartOrderDiscount(final int conditionCounterStartOrderDiscount)
	{
		this.conditionCounterStartOrderDiscount = conditionCounterStartOrderDiscount;
	}

	@SuppressWarnings("javadoc")
	public int getConditionCounterGrossPrice()
	{
		return conditionCounterGrossPrice;
	}

	@SuppressWarnings("javadoc")
	public void setConditionCounterGrossPrice(final int conditionCounterGrossPrice)
	{
		this.conditionCounterGrossPrice = conditionCounterGrossPrice;
	}

	@SuppressWarnings("javadoc")
	public String getGrossPrice()
	{
		return grossPrice;
	}

	@SuppressWarnings("javadoc")
	public String getTax1()
	{
		return tax1;
	}

}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.calculation.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.gp.commerce.core.calculation.AvalaraResponseDetailDTO;
import com.gp.commerce.core.calculation.AvalaraResponseLineItemDTO;
import com.gp.commerce.core.calculation.AvalaraSplitEntryTax;
import com.gp.commerce.core.calculation.AvalaraTaxResponseDTO;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.dto.onesource.GPOneSourceSplitEntryTax;
import com.gp.commerce.core.dto.onesource.OutdataInvoiceType;
import com.gp.commerce.core.dto.onesource.OutdataLineType;
import com.gp.commerce.core.dto.onesource.OutdataTaxType;
import com.gp.commerce.core.dto.onesource.OutdataType;
import com.gp.commerce.core.dto.onesource.TaxCalculationResponse;
import com.gp.commerce.core.model.SplitEntryModel;
import com.gp.commerce.core.price.service.GPFilteredPriceContainer;
import com.gp.commerce.core.price.service.impl.GPDefaultEurope1PriceFactory;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.impl.DefaultCalculationService;
import de.hybris.platform.order.strategies.calculation.OrderRequiresCalculationStrategy;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.TaxValue;

/**
 * GPCalculationService class is used for tax calculation.
 *
 */
public class GPCalculationService extends DefaultCalculationService{
	
	private transient OrderRequiresCalculationStrategy orderRequiresCalculationStrategy;	
	private transient CommonI18NService commonI18NService;
	@Resource(name = "gpPriceFactory")
	GPDefaultEurope1PriceFactory gpPriceService;

	boolean calculateTaxFlag;
	private transient AvalaraTaxResponseDTO avalaraTaxResponseDTO = null;

	boolean isTaXByAvalaraService;
	private static final Logger LOG = Logger.getLogger(GPDefaultAvalaraTaxCalculationService.class);

	private transient GPDefaultAvalaraTaxCalculationService gpAvalaraTaxService;
	private transient GPDefaultOneSourceTaxCalculationService gpOneSourceTaxService;

	private boolean requiresTaxCalculations = true;

	private transient CartService cartService;

	@Override
	public void calculateTotals(final AbstractOrderModel order, final boolean recalculate,
			final Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap) throws CalculationException
	{
		boolean isSubscription=order.getIsSubscription() ;
		calculateTaxFlag = true;
		if(!isSubscription) {
			disableTaxCalculationForCart();
		}

		isTaXByAvalaraService = Config.getBoolean(GpcommerceCoreConstants.IS_TAX_CAL_FROM_AVALARA, true);
		if (recalculate || orderRequiresCalculationStrategy.requiresCalculation(order))
		{
			final CurrencyModel curr = order.getCurrency();
			final int digits = curr.getDigits().intValue();
			double slashedDiscountPerProduct = 0.0;
			boolean isDeliveryCostChanged= false;
			double deliveryCost = 0.0d;
			boolean entryWithoutSplitEntry = false;
			if(CollectionUtils.isNotEmpty(order.getEntries())) {
				final List<String> deliveryModeCode = new ArrayList<>();
				for(final AbstractOrderEntryModel entry : order.getEntries()) {
					slashedDiscountPerProduct += slashedPriceDiff(entry.getProduct(), entry.getQuantity().doubleValue(), isSubscription);
					if( entry.getGiveAway().equals(false) &&CollectionUtils.isEmpty(entry.getSplitEntry()) ) { 
						entryWithoutSplitEntry = true;
					}
					if(CollectionUtils.isNotEmpty(entry.getSplitEntry())){
						for(final SplitEntryModel splitEntry:entry.getSplitEntry()){
							if(null != splitEntry.getDeliveryMode() && null != splitEntry.getDeliveryMode().getDeliveryPrice()) {
								if(null != splitEntry.getDeliveryAddress() && !deliveryModeCode.contains(splitEntry.getDeliveryAddress().getPk().toString())) {
									isDeliveryCostChanged = true;
									deliveryCost += splitEntry.getDeliveryMode().getDeliveryFormattedPrice();
									deliveryModeCode.add(splitEntry.getDeliveryAddress().getPk().toString());
								}
							}
						}
					}
				}
			}
			if(isDeliveryCostChanged) {
				order.setDeliveryCost(deliveryCost);
				getModelService().save(order);
			}
			if(entryWithoutSplitEntry) {  
				if(CollectionUtils.isNotEmpty(order.getEntries())) {
					for(final AbstractOrderEntryModel entry : order.getEntries()) {
						if(CollectionUtils.isNotEmpty(entry.getSplitEntry())){
							for(final SplitEntryModel splitEntry:entry.getSplitEntry()){
								splitEntry.setDeliveryMode(null);
							}
						}
					}
				}
					order.setTotalDeliveryCost(0.0d);
					order.setDeliveryCost(0.0d);
					modelService.save(order);
			}
			order.setAdditionalDiscount(slashedDiscountPerProduct);
			if(order.getIsSubscription()) {
					if(CollectionUtils.isNotEmpty(order.getEntries())) {
						AbstractOrderEntryModel entry =order.getEntries().get(0) ;
						order.setSubtotal(calculateSubscriptionPrice(entry,order.getSubtotal().doubleValue()));
					}
				} 
			LOG.info("Slashed price: "+slashedDiscountPerProduct);
			LOG.info("Subtotal Order: "+order.getSubtotal());
			// subtotal
			 double subtotal = order.getSubtotal().doubleValue();
		
			//discounts
			final double totalDiscounts = calculateDiscountValues(order, recalculate);
			final double roundedTotalDiscounts = commonI18NService.roundCurrency(totalDiscounts, digits);
			LOG.info("Other discounts: "+roundedTotalDiscounts);
			order.setTotalDiscounts(Double.valueOf(roundedTotalDiscounts));
			// set total
			final double total = subtotal + order.getPaymentCost().doubleValue() + order.getDeliveryCost().doubleValue()
					- roundedTotalDiscounts;
			final double totalRounded = commonI18NService.roundCurrency(total, digits);
			order.setTotalPrice(Double.valueOf(totalRounded));
			// taxes
			if(null != order.getDeliveryAddress()) {
				for(final AbstractOrderEntryModel orderEntry: order.getEntries()) {
					if(null != orderEntry && CollectionUtils.isEmpty(orderEntry.getSplitEntry())){

						calculateTaxFlag=false;
						order.setTotalTax(0.00d);
						order.setTotalTaxValues(null);
						break;
					}
					if(null != orderEntry)
					{
						for(final SplitEntryModel splitEntry: orderEntry.getSplitEntry()) {
							if(splitEntry.getDeliveryAddress()== null){
								calculateTaxFlag=false;
								order.setTotalTax(0.00d);
								order.setTotalTaxValues(null);
								break;
							}
						}
					}
				}

				if (calculateTaxFlag && isRequiresTaxCalculations()) {
						if(isTaXByAvalaraService) {
						try {
							if(order instanceof OrderModel) {
								LOG.debug("Place order call setting Avalara tax commit as true and type as SalesInvoice");
                                avalaraTaxResponseDTO = gpAvalaraTaxService.calculateTax(order, true);
							 } else {
								 LOG.debug("Cart related call setting Avalara tax commit as false and type as SalesOrder");
								 avalaraTaxResponseDTO = gpAvalaraTaxService.calculateTax(order, false);
							 }

							if (null != avalaraTaxResponseDTO) {
								setAvalaraTaxForSplitEntry(order, avalaraTaxResponseDTO);
								saveOrder(order);
							}
						} catch (final Exception gpe) {
							LOG.error("Avalara tax calculation failed with execption:", gpe);
						}
					} else {
						try {
							final TaxCalculationResponse taxResponse = gpOneSourceTaxService.calculateTax(order);
								if(null != taxResponse) {
								setTaxForSplitEntry(order,taxResponse);
								saveOrder(order);
							}
						} catch(final Exception gpe) {
							LOG.error("OneSource tax calculation failed with execption:", gpe);
						}

					}
				}
			}



			setCalculatedStatus(order);   
			saveOrder(order);

			if (order instanceof CartModel)
			{
				getModelService().refresh(order);
				cartService.setSessionCart((CartModel)order);
			}
		}
	}

	private void disableTaxCalculationForCart() {
		try {
			if (((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getParameter("calculationType")!=null &&
					"cart".equalsIgnoreCase(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getParameter("calculationType"))) {
				setRequiresTaxCalculations(false);
			}
		} catch (Exception ex) {
			LOG.error(ex);
			LOG.info("TaxCalculation not Required as user is on cart");
		}
	}

	/**
	 * Calculates Subscription price based on the order entry and price
	 * 
	 * @param entry the order entry
	 * @param price the price
	 * @return the calculated subscription price
	 */
	public double calculateSubscriptionPrice(AbstractOrderEntryModel entry, double price) {
		double subscriptionPrice = 0.0;
		double additionalDiscount = 0.0;
		double productPrice = 0.0;
		GPFilteredPriceContainer gpFilteredPriceContainer = gpPriceService
				.getFilteredPriceContainerForProducts(entry.getProduct());  
		PriceRowModel listPriceRow = gpFilteredPriceContainer.getListPricePriceRow();
		PriceRowModel salePriceRow = gpFilteredPriceContainer.getSalePricePriceRow();
		double listPrice = gpFilteredPriceContainer.getListPrice();
		double salePrice = gpFilteredPriceContainer.getSalePrice();
		if (listPrice > salePrice && salePrice >= 0d) {
			productPrice=salePrice*entry.getQuantity() ;
			if (Double.compare(productPrice, price)!=0) {
				
				additionalDiscount = productPrice - price;
				price = productPrice;
			}
		} else {
			productPrice=listPrice*entry.getQuantity() ;
			if (Double.compare(productPrice, price)!=0) {
				additionalDiscount = productPrice - price;
				price = productPrice;
				
			}
		}
		if (salePriceRow != null) {
			subscriptionPrice = gpFilteredPriceContainer.getSubscriptionPrice(salePriceRow.getPercentageDiscount(),
					salePriceRow.getAmountOff(), price, Double.valueOf(entry.getQuantity()));
		} else {
			subscriptionPrice = gpFilteredPriceContainer.getSubscriptionPrice(listPriceRow.getPercentageDiscount(),
					listPriceRow.getAmountOff(), price,Double.valueOf(entry.getQuantity()));
		}
		return subscriptionPrice - additionalDiscount;
	}
	
	

	@Override
	protected void resetAllValues(final AbstractOrderEntryModel entry) throws CalculationException
	{
		// taxes
		final Collection<TaxValue> entryTaxes = findTaxValues(entry);
		PriceValue pv =null ;
		entry.setTaxValues(entryTaxes);
		final AbstractOrderModel order = entry.getOrder();
		 pv = findBasePrice(entry);
		final PriceValue actualbasePrice = convertPriceIfNecessary(pv, order.getNet().booleanValue(), order.getCurrency(), entryTaxes);
		if(order.getIsSubscription()) {
		double	subsPrice = calculateSubscriptionPrice(entry,pv.getValue()*entry.getQuantity()) ;
		pv=	new PriceValue(order.getCurrency().getIsocode(), subsPrice/entry.getQuantity(), order.getNet().booleanValue());
		} 
		final PriceValue basePrice = convertPriceIfNecessary(pv, order.getNet().booleanValue(), order.getCurrency(), entryTaxes);
		entry.setBasePrice(Double.valueOf(basePrice.getValue()));
		final List<DiscountValue> entryDiscounts = findDiscountValues(entry);
		entry.setDiscountValues(entryDiscounts);   
		entry.setActualBasePrice(Double.valueOf(actualbasePrice.getValue()));
		 
	} 
	
	
	@Override
	public void calculateTotals(final AbstractOrderEntryModel entry, final boolean recalculate)
	{
		if (recalculate || orderRequiresCalculationStrategy.requiresCalculation(entry))
		{
			final AbstractOrderModel order = entry.getOrder();   
			final CurrencyModel curr = order.getCurrency();
			final int digits = curr.getDigits().intValue();  
			
			double totalPriceWithoutDiscount = commonI18NService.roundCurrency(entry.getBasePrice().doubleValue()
					* entry.getQuantity().longValue(), digits);

			final double quantity = entry.getQuantity().doubleValue();
			/*
			 * apply discounts (will be rounded each) convert absolute discount values in case their currency doesn't match
			 * the order currency
			 */
			//YTODO : use CalculatinService methods to apply discounts
			final List appliedDiscounts = DiscountValue.apply(quantity, totalPriceWithoutDiscount, digits,   
					convertDiscountValues(order, entry.getDiscountValues()), curr.getIsocode());
			entry.setDiscountValues(appliedDiscounts);
			if(order.getIsSubscription()) {
				double subscDiscount=(entry.getActualBasePrice()*entry.getQuantity())-(entry.getBasePrice()*entry.getQuantity()) ;
				totalPriceWithoutDiscount=totalPriceWithoutDiscount+subscDiscount ;  
			}
			double totalPrice = totalPriceWithoutDiscount;
			for (final Iterator it = appliedDiscounts.iterator(); it.hasNext();)   
			{
				totalPrice -= ((DiscountValue) it.next()).getAppliedValue();
			}
			// set total price
			entry.setTotalPrice(Double.valueOf(totalPrice));
			// apply tax values too
			//YTODO : use CalculatinService methods to apply taxes
			calculateTotalTaxValues(entry);
			setCalculatedStatus(entry);
			getModelService().save(entry);

		}
	}
	
	private void setTaxForSplitEntry(final AbstractOrderModel abstractOrderModel, final TaxCalculationResponse taxResponse) {
		final Map<String, GPOneSourceSplitEntryTax> splitEntryTaxValues = convertOneSourceResponseToSplitEntries(abstractOrderModel,taxResponse);
		for(final AbstractOrderEntryModel orderEntry: abstractOrderModel.getEntries()) {
			for(final SplitEntryModel splitEntry :orderEntry.getSplitEntry()) {
				final GPOneSourceSplitEntryTax splitEntryLineTax = splitEntryTaxValues.get(splitEntry.getCode());
				if(null != splitEntryLineTax) {
					if(!CollectionUtils.isEmpty(splitEntryLineTax.getSplitEntryTaxValue()))
					{
						splitEntry.setTaxValues(splitEntryLineTax.getSplitEntryTaxValue());
					}
					else
					{
						splitEntry.setTaxValues(null);
					}
					if(splitEntryLineTax.getSplitEntryTotalTax()!=null) {
						splitEntry.setTotalTax(BigDecimal.valueOf(splitEntryLineTax.getSplitEntryTotalTax()).setScale(2, RoundingMode.FLOOR).doubleValue());
						splitEntry.setShippingTax(splitEntryLineTax.getSplitEntryShipmentTax());
						splitEntry.setMerchantTax(splitEntryLineTax.getSplitEntryMerchantTax());
					} else {
						splitEntry.setTotalTax(0.0d);
						splitEntry.setShippingTax(0.0d);
						splitEntry.setMerchantTax(0.0d);
					}
				}
				//For multiple line items delivery mode is set on splitEntry level, so read delivery mode details from splitEntry
				if(null != splitEntry.getDeliveryMode()) {
					final GPOneSourceSplitEntryTax splitEntryShipMethodTax = splitEntryTaxValues.get(splitEntry.getDeliveryMode().getCode());
					if(null != splitEntryShipMethodTax) {
						abstractOrderModel.setTotalTaxValues(splitEntryShipMethodTax.getSplitEntryTaxValue());
					}
				} 
			}
			getModelService().saveAll(orderEntry.getSplitEntry());
		}
	}

	private Map<String, GPOneSourceSplitEntryTax> convertOneSourceResponseToSplitEntries(final AbstractOrderModel abstractOrderModel, final TaxCalculationResponse taxResponse){
		final Map<String, GPOneSourceSplitEntryTax> splitEntriesTaxValue = new HashMap<>();
		final OutdataType outData = taxResponse.getOUTDATA();
		final int taxValueItemMaxCount = Config.getInt(GpcommerceCoreConstants.ONE_SOURCE_TAX_VALUE_LIMIT, 4);
		if(null != outData) {
			for(final OutdataInvoiceType outDataInvoice: outData.getINVOICE()) {
				for(final OutdataLineType outDataLine: outDataInvoice.getLINE()) {
					if(!outDataLine.getID().endsWith("SHIP")) {
						int taxValueItemCount = 0;
						final GPOneSourceSplitEntryTax splitEntryTax = new GPOneSourceSplitEntryTax();
						final List<TaxValue> splitEntryTaxValueList = new ArrayList<>();
						for(final OutdataTaxType outDataTax: outDataLine.getTAX()) {

							final TaxValue entryTaxValue = new TaxValue(outDataTax.getAUTHORITYTYPE(), Double.valueOf(outDataTax.getTAXRATE()), true, "USD");
							splitEntryTaxValueList.add(entryTaxValue);
							taxValueItemCount++;
							if(taxValueItemCount > taxValueItemMaxCount)
							{
								break;
							}
						}
						splitEntryTax.setSplitEntryTaxValue(splitEntryTaxValueList);
						splitEntryTax.setSplitEntryTotalTax(Double.parseDouble(outDataLine.getTOTALTAXAMOUNT()));
						splitEntryTax.setSplitEntryMerchantTax(splitEntryTax.getSplitEntryTotalTax());
						splitEntriesTaxValue.put(outDataLine.getID(), splitEntryTax);
					} else {
						final GPOneSourceSplitEntryTax splitEntryTax=splitEntriesTaxValue.get(StringUtils.removeEnd(outDataLine.getID(),"-SHIP"));
						splitEntryTax.setSplitEntryShipmentTax(Double.parseDouble(outDataLine.getTOTALTAXAMOUNT()));
			            splitEntryTax.setSplitEntryTotalTax(splitEntryTax.getSplitEntryTotalTax() + Double.parseDouble(outDataLine.getTOTALTAXAMOUNT()));
			            splitEntriesTaxValue.put(StringUtils.removeEnd(outDataLine.getID(),"-SHIP"),splitEntryTax);
					}
				}
				abstractOrderModel.setTotalTax(Double.valueOf(outDataInvoice.getTOTALTAXAMOUNT()));
			}
		}
		return splitEntriesTaxValue;
	}


	private void setAvalaraTaxForSplitEntry(final AbstractOrderModel cart, final AvalaraTaxResponseDTO taxResponse) {
		final Map<String, AvalaraSplitEntryTax> splitEntryTaxValues = convertAvalaraResponseToSplitEntries(cart,taxResponse);
		for(final AbstractOrderEntryModel orderEntry: cart.getEntries()) {
			for(final SplitEntryModel splitEntry :orderEntry.getSplitEntry()) {
				final AvalaraSplitEntryTax splitEntryLineTax = splitEntryTaxValues.get(splitEntry.getCode());
				if(null != splitEntryLineTax){
					if(!CollectionUtils.isEmpty(splitEntryLineTax.getSplitEntryTaxValue()) )
					{
						splitEntry.setTaxValues(splitEntryLineTax.getSplitEntryTaxValue());
					}
					else {
						splitEntry.setTaxValues(null);
					}
					if(splitEntryLineTax.getSplitEntryTotalTax()!=null) {
						splitEntry.setTotalTax(BigDecimal.valueOf(splitEntryLineTax.getSplitEntryTotalTax()).setScale(2, RoundingMode.FLOOR).doubleValue());
						splitEntry.setShippingTax(splitEntryLineTax.getSplitEntryShipmentTax());
						splitEntry.setMerchantTax(splitEntryLineTax.getSplitEntryMerchantTax());
					}
					else {
						splitEntry.setTotalTax(0.0d);
						splitEntry.setShippingTax(0.0d);
						splitEntry.setMerchantTax(0.0d);
					}

				}
				if(null != splitEntry.getDeliveryMode()) {
					final AvalaraSplitEntryTax splitEntryShipMethodTax = splitEntryTaxValues.get(splitEntry.getDeliveryMode().getCode());
					if(null != splitEntryShipMethodTax){
						cart.setTotalTaxValues(splitEntryShipMethodTax.getSplitEntryTaxValue());
					}
				}
			}
			cart.setTotalTax(Double.parseDouble(taxResponse.getTotalTax()));
			getModelService().saveAll(orderEntry.getSplitEntry());
		}
	}
	private Map<String, AvalaraSplitEntryTax> convertAvalaraResponseToSplitEntries(final AbstractOrderModel cart, final AvalaraTaxResponseDTO taxResponse){
		final Map<String, AvalaraSplitEntryTax> splitEntriesTaxValue = new HashMap<>();
		final List<AvalaraResponseLineItemDTO> listLineItem = taxResponse.getLines();

		if(null != listLineItem) {
			for(final AvalaraResponseLineItemDTO lineItem: listLineItem) {
				if(!lineItem.getLineNumber().endsWith("SHIP")) {
					final AvalaraSplitEntryTax splitEntryTax = new AvalaraSplitEntryTax();
					final List<TaxValue> splitEntryTaxValueList = new ArrayList<>();
					for (final AvalaraResponseDetailDTO details : lineItem.getDetails()) {
						final TaxValue entryTaxValue = new TaxValue(details.getTaxName(),details.getRate(), true, "USD");
						splitEntryTaxValueList.add(entryTaxValue);
					}
					splitEntryTax.setSplitEntryTaxValue(splitEntryTaxValueList);
					splitEntryTax.setSplitEntryTotalTax(lineItem.getTax());
					splitEntriesTaxValue.put(lineItem.getLineNumber(), splitEntryTax);
					splitEntryTax.setSplitEntryMerchantTax(splitEntryTax.getSplitEntryTotalTax());
				}else{
					final AvalaraSplitEntryTax splitEntryTax =splitEntriesTaxValue.get(StringUtils.removeEnd(lineItem.getLineNumber(),"-SHIP"));
					splitEntryTax.setSplitEntryShipmentTax(lineItem.getTax());
					splitEntryTax.setSplitEntryTotalTax(splitEntryTax.getSplitEntryTotalTax()+lineItem.getTax());
					splitEntriesTaxValue.put(StringUtils.removeEnd(lineItem.getLineNumber(),"-SHIP"),splitEntryTax);
				}
			}
			cart.setTotalTax(Double.parseDouble(taxResponse.getTotalTax()));
		}

		return splitEntriesTaxValue;
	}
	

	private double slashedPriceDiff(final ProductModel product, final double quantity, final boolean isSubscription) {

		GPFilteredPriceContainer gpFilteredPriceContainer = gpPriceService
				.getFilteredPriceContainerForProducts(product);
		if (isSubscription) {
			return gpFilteredPriceContainer.slashedPriceDiffForSubscription(quantity);
		}
		return gpFilteredPriceContainer.slashedPriceDiff(quantity);
	}
	

	
	
	
	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}
	@Override
	public void setCommonI18NService(final CommonI18NService commonI18NService) {
			this.commonI18NService=commonI18NService;
			super.setCommonI18NService(commonI18NService);
	}
	
	public OrderRequiresCalculationStrategy getOrderRequiresCalculationStrategy() {
		return orderRequiresCalculationStrategy;
	}
	
	@Override
	public void setOrderRequiresCalculationStrategy(final OrderRequiresCalculationStrategy orderRequiresCalculationStrategy) {
		this.orderRequiresCalculationStrategy=orderRequiresCalculationStrategy;
		super.setOrderRequiresCalculationStrategy(orderRequiresCalculationStrategy);
	}

	public GPDefaultAvalaraTaxCalculationService getGpAvalaraTaxService() {
		return gpAvalaraTaxService;
	}
	public void setGpAvalaraTaxService(final GPDefaultAvalaraTaxCalculationService gpAvalaraTaxService) {
		this.gpAvalaraTaxService = gpAvalaraTaxService;
	}
	public GPDefaultOneSourceTaxCalculationService getGpOneSourceTaxService() {
		return gpOneSourceTaxService;
	}

	public void setGpOneSourceTaxService(GPDefaultOneSourceTaxCalculationService gpOneSourceTaxService) {
		this.gpOneSourceTaxService = gpOneSourceTaxService;
	}

	public boolean isRequiresTaxCalculations() {
		return requiresTaxCalculations;
	}

	public void setRequiresTaxCalculations(boolean requiresTaxCalculations) {
		this.requiresTaxCalculations = requiresTaxCalculations;
	}

	public void setCartService(CartService cartService) {
		this.cartService = cartService;
	}
}
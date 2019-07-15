/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.order.converters.populator;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.MessageSource;
import org.springframework.util.Assert;

import com.gp.commerce.core.model.GPOrderEntryAttributeModel;
import com.gp.commerce.core.model.SplitEntryModel;
import com.gp.commerce.core.price.service.GPFilteredPriceContainer;
import com.gp.commerce.core.price.service.impl.GPDefaultEurope1PriceFactory;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.facade.order.data.SplitEntryData;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gp.commerce.facades.populators.GPWishlistEntryPopulator;

public class GPOrderEntryPopulator implements Populator<AbstractOrderEntryModel, OrderEntryData> {

	@Resource(name = "gpPriceFactory")
	GPDefaultEurope1PriceFactory gpPriceService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@Resource(name = "i18NService")
	private I18NService i18NService;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

	@Resource(name = "wishlistEntryPopulator")
	private GPWishlistEntryPopulator wishlistEntryPopulator;
	
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;

	private static final String INSTALLED_ENTRY = "installedEntry";
	protected static final String LEASE_UNAVAILABLE = "Lease Approval required for purchase";
	private static final String ADD_GIFT_ENTRY = "addGiftEntry";
	private static final String GP_LOW_STOCK_MESSAGE = "gp.low.stock.message.cart";
	private static final String GP_NO_STOCK_MESSAGE = "gp.no.stock.message.cart";

	private Converter<SplitEntryModel, SplitEntryData> gpSplitEntryConverter;

	@Override
	public void populate(final AbstractOrderEntryModel source, final OrderEntryData target) throws ConversionException {
		Assert.notNull(source, GpcommerceFacadesConstants.SOURCE_VALIDATION_ERROR);
		Assert.notNull(target, GpcommerceFacadesConstants.TARGET_VALIDATION_ERROR);
		boolean isSubscriptionOrder= source.getOrder() != null && source.getOrder().getIsSubscription() != null ? source.getOrder().getIsSubscription() : false;
		addLeaseLogic(source, target);
		addGiftWrapLogic(source, target);
		target.setVisible(true);     
		if(source.getFrequency()!=null) {
			target.setSubscriptionFrequency(source.getFrequency()) ;
		}
		if(isSubscriptionOrder) {
			checkSaveSlashedPriceForSubscription(source,target);
		}else {
		checkSaveSlashedPrice(source,target);
		}
		
		populateLowStockMessage(target);
	
		if (CollectionUtils.isNotEmpty(source.getAdditionalAttributes())) {
			final Map<String, String> additionalAttributes = new HashMap<>();
			for (final GPOrderEntryAttributeModel additionalAttribute : source.getAdditionalAttributes()) {
				final String attributeKey = additionalAttribute.getName();
				if(INSTALLED_ENTRY.equalsIgnoreCase(attributeKey)) {
					target.setVisible(false);
				}
				if(ADD_GIFT_ENTRY.equalsIgnoreCase(attributeKey)) {
					target.setVisible(false);
				}
				additionalAttributes.put(attributeKey, additionalAttribute.getValue());
			}
			target.setAdditionalAttributes(additionalAttributes);
		}

		if(null != source.getSplitEntry()) {
			target.setSplitEntries(getGpSplitEntryConverter().convertAll(source.getSplitEntry()));
		}
		if(null!=source.getGiveAway()) {
		 target.setGiveAway(source.getGiveAway());  
		}

		if (Boolean.TRUE.equals(source.isPromotionsRevoked()))
		{
			target.setPromotionsRevoked(true);
		}
		BigDecimal promotionPriceDiff=new BigDecimal(0) ;
		if(null!=source && null!=source.getBasePrice() && null!=source.getQuantity()&& null!=source.getTotalPrice()) {
			final double total =source.getBasePrice()*source.getQuantity();
			final BigDecimal totalWithDecimal = (BigDecimal.valueOf(total)).setScale(2, RoundingMode.HALF_UP);
			final BigDecimal totalPriceWithDecimal = (BigDecimal.valueOf(source.getTotalPrice())).setScale(2, RoundingMode.HALF_UP);
			promotionPriceDiff = totalWithDecimal.subtract(totalPriceWithDecimal,new MathContext(4)) ;
		}
		String formattedTotalPrice = null;
		final PriceData promotionPriceData=getPriceData(source,target,promotionPriceDiff);
		target.setProductPromotion(promotionPriceData);
		if(target.getBasePrice() != null ) {
			final CurrencyModel currency = commonI18NService.getCurrency(target.getBasePrice().getCurrencyIso());
			if(null != target.getSlashSavedPrice()) {
				formattedTotalPrice = wishlistEntryPopulator.formatPrice(target.getSlashSavedPrice().getValue().add(promotionPriceDiff),currency);
				target.setSlashPriceValue(formattedTotalPrice);
			}else {
				formattedTotalPrice = wishlistEntryPopulator.formatPrice(promotionPriceDiff.setScale(2, BigDecimal.ROUND_FLOOR),currency);
			}
		}
	}

	private void checkSaveSlashedPriceForSubscription(AbstractOrderEntryModel source, OrderEntryData target) {
		final ProductModel product = source.getProduct();
		BigDecimal savingsPrice; 
		GPFilteredPriceContainer gpFilteredPriceContainer = gpPriceService
				.getFilteredPriceContainerForProducts(product);
		if(source.getQuantity()!=null) {
		double slashPriceDiff = gpFilteredPriceContainer.slashedPriceDiffForSubscription(source.getQuantity()) ;
		savingsPrice = BigDecimal.valueOf(slashPriceDiff);
		final PriceData savingPriceData = getPriceData(source, target, savingsPrice);
		target.setSlashSavedPrice(savingPriceData);
		target.setSlashPriceValue(savingsPrice.toString());
		}
	
		
	}

	private void populateLowStockMessage(final OrderEntryData target) {
		if(cmsSiteService.isSampleSite())
		{
			return;
		}
		final StockData stockData = target.getProduct().getStock();
		if(null != stockData)
		{
			final Long stockLevel = stockData.getStockLevel();
			if(null != stockLevel && stockLevel <=0) {
				stockData.setLowStockMessage(GP_NO_STOCK_MESSAGE);
			}else if(null !=  stockLevel && null != target && null != target.getQuantity() && stockLevel < target.getQuantity())
			{
				stockData.setLowStockMessage(GP_LOW_STOCK_MESSAGE);
			}
		}
	}


	protected void addGiftWrapLogic(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry) {

		entry.setGiftWrapped(orderEntry.isGiftWrapped());
		entry.setGiftMessage(orderEntry.getGiftMessage());

	}
	protected void addLeaseLogic(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry)
	{
		if (null != orderEntry.getProduct().getLeaseable())
		{
			entry.setLeasable(orderEntry.getProduct().getLeaseable().booleanValue());
			entry.setLeasableMessage(LEASE_UNAVAILABLE);
		}
	}

	private void checkSaveSlashedPrice(final AbstractOrderEntryModel source, final OrderEntryData target) {
		final ProductModel product = source.getProduct();
		final List<PriceRow> productPrices = gpPriceService.getPriceInformationsForProduct(product);
		BigDecimal savingsPrice;
		if (CollectionUtils.isNotEmpty(productPrices)) {
			final PriceRow productPrice = productPrices.get(0);
			final PriceRowModel prmModel = modelService.get(productPrice.getPK());
			if (prmModel.getWeblistPrice() != null && prmModel.getWeblistPrice() > prmModel.getPrice()
					&& source.getQuantity() != null) {
				savingsPrice = BigDecimal
						.valueOf((prmModel.getWeblistPrice().doubleValue() - prmModel.getPrice().doubleValue())
								* source.getQuantity().doubleValue());
				final PriceData savingPriceData = getPriceData(source, target, savingsPrice);
				target.setSlashSavedPrice(savingPriceData);
				target.setSlashPriceValue(savingsPrice.toString());
			} else {
				GPFilteredPriceContainer gpFilteredPriceContainer = gpPriceService
						.getFilteredPriceContainerForProducts(product);
				if(source.getQuantity()!=null) {
				double slashPriceDiff = gpFilteredPriceContainer.slashedPriceDiff(source.getQuantity());
				savingsPrice = BigDecimal.valueOf(slashPriceDiff);
				final PriceData savingPriceData = getPriceData(source, target, savingsPrice);
				target.setSlashSavedPrice(savingPriceData);
				target.setSlashPriceValue(savingsPrice.toString());
				}
			}
		}
	}

	public PriceData getPriceData(final AbstractOrderEntryModel source,final OrderEntryData target,final BigDecimal price) {
		final ProductModel product = source.getProduct();
		final PriceDataType priceType;
		 PriceData priceData =null ;
		if (CollectionUtils.isEmpty(product.getVariants()))
		{
			priceType = PriceDataType.BUY;
		}
		else
		{
			priceType = PriceDataType.FROM;
		}

		if(target.getBasePrice() != null ) {
			final CurrencyModel currency = commonI18NService.getCurrency(target.getBasePrice().getCurrencyIso());
			 priceData = priceDataFactory.create(priceType, price.setScale(2, BigDecimal.ROUND_FLOOR),currency);
		}
		return priceData  ;

	}

	public Converter<SplitEntryModel, SplitEntryData> getGpSplitEntryConverter() {
		return gpSplitEntryConverter;
	}

	public void setGpSplitEntryConverter(final Converter<SplitEntryModel, SplitEntryData> gpSplitEntryConverter) {
		this.gpSplitEntryConverter = gpSplitEntryConverter;
	}

}

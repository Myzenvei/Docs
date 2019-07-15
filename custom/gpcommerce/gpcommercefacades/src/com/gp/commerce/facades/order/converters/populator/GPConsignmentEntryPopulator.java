/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.order.converters.populator;


import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.gp.commerce.core.enums.GPConsignmentEntryStatus;
import com.gp.commerce.core.model.TrackingModel;
import com.gp.commerce.facade.data.TrackingData;

import de.hybris.platform.commercefacades.order.converters.populator.ConsignmentEntryPopulator;
import de.hybris.platform.commercefacades.order.data.ConsignmentEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

public class GPConsignmentEntryPopulator  extends ConsignmentEntryPopulator {

	private  Converter<TrackingModel, TrackingData> gpConsignmentTrackingConverter;
	private static final String IN_PROCESS = "In Process";
	@Resource(name="priceDataFactory")
	private PriceDataFactory priceDataFactory;
	@Resource(name="commercePriceService")
	private CommercePriceService commercePriceService ;
	@Resource(name="productService")
	private ProductService productService;
	
	@Override
	public void populate(ConsignmentEntryModel source, ConsignmentEntryData target) throws ConversionException {
		final PriceDataType priceType;
		final PriceInformation info;
		super.populate(source, target);
		
		String entryStatus = StringUtils.EMPTY;
		GPConsignmentEntryStatus consignmentEntryStatus = source.getConsignmentEntryStatus();
		if(consignmentEntryStatus.equals(GPConsignmentEntryStatus.READY) || consignmentEntryStatus.equals(GPConsignmentEntryStatus.PARTIALLY_SHIPPED)
				|| consignmentEntryStatus.equals(GPConsignmentEntryStatus.PAYMENT_ERROR)) {
			entryStatus = IN_PROCESS;
		}
		else {
			entryStatus = consignmentEntryStatus.getCode();
			entryStatus = entryStatus.toUpperCase().replace(entryStatus.substring(1), entryStatus.substring(1).toLowerCase());
		}
		target.setConsignmentEntryStatus(entryStatus);
		
		if(CollectionUtils.isNotEmpty(source.getTrackingList())) {
			target.setTrackingList(getGpConsignmentTrackingConverter().convertAll(source.getTrackingList()));
		}
	
		Long totalQuantity=source.getQuantity() ;
		 if(CollectionUtils.isNotEmpty(source.getTrackingList())){
			 Long unshippedQty=0L;
			 for(TrackingModel tracking : source.getTrackingList()) {
				 unshippedQty +=tracking.getQuantityShipped() ;
			}
			 target.setUnshippedQuantity(totalQuantity-unshippedQty) ;
		 }
			double consignmentEntryPrice=source.getOrderEntry().getBasePrice().doubleValue()*source.getQuantity();
			ProductModel productModel = productService.getProductForCode(source.getOrderEntry().getProduct().getCode());
			if (CollectionUtils.isEmpty(productModel.getVariants()))
			{
				priceType = PriceDataType.BUY;
				info = commercePriceService.getWebPriceForProduct(productModel);
			}
			else
			{
				priceType = PriceDataType.FROM;
				info = commercePriceService.getFromPriceForProduct(productModel);
			}
			if (info != null) {
			final PriceData priceData = priceDataFactory.create(priceType, BigDecimal.valueOf(consignmentEntryPrice),
					info.getPriceValue().getCurrencyIso());
			target.setConsignmentEntryPrice(priceData);
			}
	}
	public Converter<TrackingModel, TrackingData> getGpConsignmentTrackingConverter() {
		return gpConsignmentTrackingConverter;
	}
	public void setGpConsignmentTrackingConverter(Converter<TrackingModel, TrackingData> gpConsignmentTrackingConverter) {
		this.gpConsignmentTrackingConverter = gpConsignmentTrackingConverter;
	}
	

}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.model.SplitEntryModel;
import com.gp.commerce.facade.order.data.SplitEntryData;

import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.ZoneDeliveryModeData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.product.ProductModel;

public class GPSplitEntryPopulator implements Populator<SplitEntryModel, SplitEntryData>{

	private Converter<AddressModel, AddressData> addressConverter;
	private Converter<DeliveryModeModel, DeliveryModeData> deliveryModeConverter;
	private Converter<ZoneDeliveryModeModel, ZoneDeliveryModeData> zoneDeliveryModeConverter;
	private PriceDataFactory priceDataFactory;
	@Resource(name="commercePriceService")
	private CommercePriceService commercePriceService ;
	@Resource(name="productService")
	private ProductService productService;

	@Override
	public void populate(SplitEntryModel source, SplitEntryData target) throws ConversionException {
		final PriceDataType priceType;
		final PriceInformation info;
		target.setCode(source.getCode());
		target.setQty(source.getQty());
		if(null != source.getDeliveryAddress()) {
			target.setDeliveryAddress(getAddressConverter().convert(source.getDeliveryAddress()));
		}
		target.setDeliveryInstruction(source.getDeliveryInstruction());
		
		ProductModel productModel = productService.getProductForCode(source.getProductCode());
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
		final PriceData priceData = getPriceDataFactory().create(priceType, BigDecimal.valueOf(source.getPrice()),
				info.getPriceValue().getCurrencyIso());
		target.setPrice(priceData);
		}
		target.setProductCode(source.getProductCode()) ;
		target.setVisible(source.isVisible());
		addDeliveryMethod(source,target);
	}
	
	protected void addDeliveryMethod(final SplitEntryModel source, final SplitEntryData prototype)
	{
		final DeliveryModeModel deliveryMode = source.getDeliveryMode();
		if (deliveryMode != null)
		{
			DeliveryModeData deliveryModeData;
			if (deliveryMode instanceof ZoneDeliveryModeModel)
			{
				deliveryModeData = getZoneDeliveryModeConverter().convert((ZoneDeliveryModeModel) deliveryMode);
			}
			else
			{
				deliveryModeData = getDeliveryModeConverter().convert(deliveryMode);
			}

			
			prototype.setDeliveryMode(deliveryModeData);
		}
	}
	
	public Converter<AddressModel, AddressData> getAddressConverter() {
		return addressConverter;
	}
	
	public void setAddressConverter(Converter<AddressModel, AddressData> addressConverter) {
		this.addressConverter = addressConverter;
	}
	
	public Converter<ZoneDeliveryModeModel, ZoneDeliveryModeData> getZoneDeliveryModeConverter() {
		return zoneDeliveryModeConverter;
	}
	
	public void setZoneDeliveryModeConverter(
			Converter<ZoneDeliveryModeModel, ZoneDeliveryModeData> zoneDeliveryModeConverter) {
		this.zoneDeliveryModeConverter = zoneDeliveryModeConverter;
	}
	
	public Converter<DeliveryModeModel, DeliveryModeData> getDeliveryModeConverter() {
		return deliveryModeConverter;
	}
	
	public void setDeliveryModeConverter(Converter<DeliveryModeModel, DeliveryModeData> deliveryModeConverter) {
		this.deliveryModeConverter = deliveryModeConverter;
	}
	
	public PriceDataFactory getPriceDataFactory() {
		return priceDataFactory;
	}
	
	public void setPriceDataFactory(PriceDataFactory priceDataFactory) {
		this.priceDataFactory = priceDataFactory;
	}

}

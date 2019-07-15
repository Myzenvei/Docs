/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.price.service.GPFilteredPriceContainer;
import com.gp.commerce.core.price.service.impl.GPDefaultEurope1PriceFactory;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.converters.populator.ProductVariantMatrixPopulator;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commercefacades.product.data.VariantMatrixElementData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantValueCategoryModel;

public class GPProductVariantMatrixPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends ProductVariantMatrixPopulator<SOURCE, TARGET> {

	@Resource(name = "gpPriceFactory")
	GPDefaultEurope1PriceFactory gpPriceService;
	
	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;
	
	private Converter<VariantProductModel, VariantOptionData> variantOptionDataConverter;
	
	private Converter<ProductModel, StockData> stockConverter;
	
	private UrlResolver<ProductModel> productModelUrlResolver;

	@Override
	protected void createNodesForVariant(final VariantProductModel variant, VariantMatrixElementData currentParentNode) {
	
		final List<VariantValueCategoryModel> valuesCategories = getVariantValuesCategories(variant);
		for (final VariantValueCategoryModel valueCategory : valuesCategories)
		{
			final VariantMatrixElementData existingNode = getExistingNode(currentParentNode, valueCategory);

			if (existingNode == null)
			{
				final VariantMatrixElementData createdNode = createNode(currentParentNode, valueCategory);
				createdNode.getVariantOption().setCode(variant.getCode());
				setPriceInVariantOption(variant, createdNode.getVariantOption());
				createdNode.getVariantOption().setStock(getStockConverter().convert(variant));
				createdNode.getVariantOption().setUrl(getProductModelUrlResolver().resolve(variant));
				currentParentNode = createdNode;
			}
			else
			{
				currentParentNode = existingNode;
			}
		}
	
	}

	private void setPriceInVariantOption(final VariantProductModel variant, final VariantOptionData variantOption) {
		GPFilteredPriceContainer gpFilteredPriceContainer = gpPriceService.getFilteredPriceContainerForProducts(variant);


		double listPrice = gpFilteredPriceContainer.getListPrice();
		double salePrice = gpFilteredPriceContainer.getSalePrice();
		PriceData priceData;
		final PriceDataType priceType;
		if (CollectionUtils.isEmpty(variant.getVariants()))
		{
			priceType = PriceDataType.BUY;
		}
		else
		{
			priceType = PriceDataType.FROM;
		}
		if (listPrice > salePrice && salePrice >=0d)
		{
			priceData = priceDataFactory.create(priceType, BigDecimal.valueOf(salePrice),
					gpFilteredPriceContainer.getCurrency());
			variantOption.setPriceData(priceData);

		}else{

		priceData = priceDataFactory.create(priceType, BigDecimal.valueOf(listPrice),
				gpFilteredPriceContainer.getCurrency());
		variantOption.setPriceData(priceData);
	}
	}

	public GPDefaultEurope1PriceFactory getGpPriceService() {
		return gpPriceService;
	}

	public void setGpPriceService(GPDefaultEurope1PriceFactory gpPriceService) {
		this.gpPriceService = gpPriceService;
	}
	
	protected Converter<VariantProductModel, VariantOptionData> getVariantOptionDataConverter()
	{
		return variantOptionDataConverter;
	}

	public void setVariantOptionDataConverter(final Converter<VariantProductModel, VariantOptionData> variantOptionDataConverter)
	{
		this.variantOptionDataConverter = variantOptionDataConverter;
	}

	public PriceDataFactory getPriceDataFactory() {
		return priceDataFactory;
	}

	public void setPriceDataFactory(PriceDataFactory priceDataFactory) {
		this.priceDataFactory = priceDataFactory;
	}

	public Converter<ProductModel, StockData> getStockConverter() {
		return stockConverter;
	}

	public void setStockConverter(Converter<ProductModel, StockData> stockConverter) {
		this.stockConverter = stockConverter;
	}

	public UrlResolver<ProductModel> getProductModelUrlResolver() {
		return productModelUrlResolver;
	}

	public void setProductModelUrlResolver(UrlResolver<ProductModel> productModelUrlResolver) {
		this.productModelUrlResolver = productModelUrlResolver;
	}
}

/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.order.hook.impl;

import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.order.hook.CommerceAddToCartMethodHook;
import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.exceptions.GPLowStockException;
import com.gp.commerce.core.exceptions.GPMaximumStockException;
import com.gp.commerce.core.model.GPCommerceProductModel;
import com.gp.commerce.core.model.GPCommerceSizeVariantProductModel;
import com.gp.commerce.core.model.GPCommerceStyleVariantProductModel;
import com.gp.commerce.core.model.GPOrderEntryAttributeModel;


public class GPCommerceAddToCartMethodHook implements CommerceAddToCartMethodHook{


	private ModelService modelService;
	private ProductService productService;
	private CommercePriceService commercePriceService;

	private CommonI18NService commonI18NService;
	private CommerceCommonI18NService commerceCommonI18NService;
	private I18NService i18NService;
	private final ConcurrentMap<String, NumberFormat> currencyFormats = new ConcurrentHashMap<>();

	private static final String INSTALLABLE_PRODUCT = "installableProductCode";
	private static final String GIFTABLE_PRODUCT = "giftableProduct";
	private static final String GIFTABLE_PRODUCT_PRICE = "giftableProductPrice";
	private static final String SUCCESS = "success";


	@Override
	public void beforeAddToCart(final CommerceCartParameter parameters) throws CommerceCartModificationException {
		// TODO Auto-generated method stub

	}


	@Override
	public void afterAddToCart(final CommerceCartParameter parameters, final CommerceCartModification result)
			throws CommerceCartModificationException {
		Collection<GPOrderEntryAttributeModel> additionalAttributes = null;
		if (null != result && null != result.getStatusCode() && !result.getStatusCode().equalsIgnoreCase(SUCCESS)
				&& result.getQuantityAdded() == 0)
		{
			if(CommerceCartModificationStatus.MAX_ORDER_QUANTITY_EXCEEDED.equals(result.getStatusCode())) {
				throw new GPMaximumStockException(GpcommerceCoreConstants.MAX_ORDER_QUANTITY_ERROR_CODE, Long.toString(result.getQuantityAdded()));
			}else {
				throw new GPLowStockException(GpcommerceCoreConstants.STOCK_LEVEL_ERROR_CODE, Long.toString(result.getQuantityAdded()));
			}
		}
		if (CollectionUtils.isNotEmpty(result.getEntry().getAdditionalAttributes())) {
			additionalAttributes = result.getEntry().getAdditionalAttributes();
		} else {
			additionalAttributes = new ArrayList<>();
		}

		if (MapUtils.isNotEmpty(parameters.getAdditionalAttributes())) {
			for (final Map.Entry<String, String> entry : parameters.getAdditionalAttributes().entrySet()) {
				final GPOrderEntryAttributeModel additionalAttribute = getModelService()
						.create(GPOrderEntryAttributeModel.class);
				additionalAttribute.setName(entry.getKey());
				additionalAttribute.setValue(entry.getValue());
				additionalAttributes.add(additionalAttribute);
			}

		}

		if (null != result.getEntry().getProduct() && result.getEntry().getProduct() instanceof GPCommerceSizeVariantProductModel ) {
			final GPCommerceStyleVariantProductModel style = (GPCommerceStyleVariantProductModel) ((GPCommerceSizeVariantProductModel) result
					.getEntry().getProduct()).getBaseProduct();
			if (null != style && style.getBaseProduct() instanceof GPCommerceProductModel) {
				final GPCommerceProductModel parent = (GPCommerceProductModel) style.getBaseProduct();
				if (null != parent) {

					if (null != parent.getInstallationProduct()) {
						final GPOrderEntryAttributeModel additionalAttribute = getModelService()
								.create(GPOrderEntryAttributeModel.class);
						additionalAttribute.setName(INSTALLABLE_PRODUCT);
						additionalAttribute.setValue(parent.getInstallationProduct().getCode());
						additionalAttributes.add(additionalAttribute);
					}
					if (null != parent.getGiftWrapProduct()) {
						final GPOrderEntryAttributeModel giftAdditionalAttribute = getModelService()
								.create(GPOrderEntryAttributeModel.class);
						giftAdditionalAttribute.setName(GIFTABLE_PRODUCT);
						giftAdditionalAttribute.setValue(parent.getGiftWrapProduct().getCode());
						additionalAttributes.add(giftAdditionalAttribute);

						final ProductModel productModel = getProductService().getProductForCode(parent.getGiftWrapProduct().getCode());
						final PriceInformation info = getCommercePriceService().getWebPriceForProduct(productModel);
						if(null != info) {
							final CurrencyModel currency = getCommonI18NService().getCurrency(info.getPriceValue().getCurrencyIso());
							final String formattedPrice = formatPrice(BigDecimal.valueOf(info.getPriceValue().getValue()),currency);
							final GPOrderEntryAttributeModel giftAdditionalAttribute1 = getModelService()
								.create(GPOrderEntryAttributeModel.class);
							giftAdditionalAttribute1.setName(GIFTABLE_PRODUCT_PRICE);
							giftAdditionalAttribute1.setValue(formattedPrice);
							additionalAttributes.add(giftAdditionalAttribute1);
						}
					}
				}
			}
		}else {
			if( result.getEntry().getProduct() instanceof GPCommerceProductModel) {
				if (null != ((GPCommerceProductModel)result.getEntry().getProduct()).getInstallationProduct()) {
					final GPOrderEntryAttributeModel additionalAttribute = getModelService()
							.create(GPOrderEntryAttributeModel.class);
					additionalAttribute.setName(INSTALLABLE_PRODUCT);
					additionalAttribute.setValue(((GPCommerceProductModel)result.getEntry().getProduct()).getInstallationProduct().getCode());
					additionalAttributes.add(additionalAttribute);
				}
				if (null != ((GPCommerceProductModel)result.getEntry().getProduct()).getGiftWrapProduct()) {
					final GPOrderEntryAttributeModel giftAdditionalAttribute = getModelService()
							.create(GPOrderEntryAttributeModel.class);
					giftAdditionalAttribute.setName(GIFTABLE_PRODUCT);
					giftAdditionalAttribute.setValue(((GPCommerceProductModel)result.getEntry().getProduct()).getGiftWrapProduct().getCode());
					additionalAttributes.add(giftAdditionalAttribute);

					final ProductModel productModel = getProductService().getProductForCode(((GPCommerceProductModel)result.getEntry().getProduct()).getGiftWrapProduct().getCode());
					final PriceInformation info = getCommercePriceService().getWebPriceForProduct(productModel);
					if(null != info) {
						final CurrencyModel currency = getCommonI18NService().getCurrency(info.getPriceValue().getCurrencyIso());
						final String formattedPrice = formatPrice(BigDecimal.valueOf(info.getPriceValue().getValue()),currency);
						final GPOrderEntryAttributeModel giftAdditionalAttribute1 = getModelService()
							.create(GPOrderEntryAttributeModel.class);
						giftAdditionalAttribute1.setName(GIFTABLE_PRODUCT_PRICE);
						giftAdditionalAttribute1.setValue(formattedPrice);
						additionalAttributes.add(giftAdditionalAttribute1);
					}
				}
			}
		}

		modelService.saveAll(additionalAttributes);
		final AbstractOrderEntryModel entry = result.getEntry();
		entry.setAdditionalAttributes(additionalAttributes);
		modelService.save(entry);
		modelService.refresh(entry);
	}

	public String formatPrice(final BigDecimal value, final CurrencyModel currency)
	{
		final LanguageModel currentLanguage = getCommonI18NService().getCurrentLanguage();
		Locale locale = getCommerceCommonI18NService().getLocaleForLanguage(currentLanguage);
		if (locale == null)
		{
			// Fallback to session locale
			locale = getI18NService().getCurrentLocale();
		}

		final NumberFormat currencyFormat = createCurrencyFormat(locale, currency);
		return currencyFormat.format(value).toString();
	}

	protected NumberFormat createCurrencyFormat(final Locale locale, final CurrencyModel currency)
	{
		final String key = locale.getISO3Country() + "_" + currency.getIsocode();

		NumberFormat numberFormat = currencyFormats.get(key);
		if (numberFormat == null)
		{
			final NumberFormat currencyFormat = createNumberFormat(locale, currency);
			numberFormat = currencyFormats.putIfAbsent(key, currencyFormat);
			if (numberFormat == null)
			{
				numberFormat = currencyFormat;
			}
		}
		// don't allow multiple references
		return (NumberFormat) numberFormat.clone();
	}

	protected NumberFormat createNumberFormat(final Locale locale, final CurrencyModel currency)
	{
		final DecimalFormat currencyFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
		adjustDigits(currencyFormat, currency);
		adjustSymbol(currencyFormat, currency);
		return currencyFormat;
	}

	protected DecimalFormat adjustDigits(final DecimalFormat format, final CurrencyModel currencyModel)
	{
		final int tempDigits = currencyModel.getDigits() == null ? 0 : currencyModel.getDigits().intValue();
		final int digits = Math.max(0, tempDigits);

		format.setMaximumFractionDigits(digits);
		format.setMinimumFractionDigits(digits);
		if (digits == 0)
		{
			format.setDecimalSeparatorAlwaysShown(false);
		}

		return format;
	}

	protected DecimalFormat adjustSymbol(final DecimalFormat format, final CurrencyModel currencyModel)
	{
		final String symbol = currencyModel.getSymbol();
		if (symbol != null)
		{
			final DecimalFormatSymbols symbols = format.getDecimalFormatSymbols(); // does cloning
			final String iso = currencyModel.getIsocode();
			boolean changed = false;
			if (!iso.equalsIgnoreCase(symbols.getInternationalCurrencySymbol()))
			{
				symbols.setInternationalCurrencySymbol(iso);
				changed = true;
			}
			if (!symbol.equals(symbols.getCurrencySymbol()))
			{
				symbols.setCurrencySymbol(symbol);
				changed = true;
			}
			if (changed)
			{
				format.setDecimalFormatSymbols(symbols);
			}
		}
		return format;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(final ModelService modelService) {
		this.modelService = modelService;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(final ProductService productService) {
		this.productService = productService;
	}

	public CommercePriceService getCommercePriceService() {
		return commercePriceService;
	}

	public void setCommercePriceService(final CommercePriceService commercePriceService) {
		this.commercePriceService = commercePriceService;
	}


	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}


	public void setCommonI18NService(final CommonI18NService commonI18NService) {
		this.commonI18NService = commonI18NService;
	}


	public CommerceCommonI18NService getCommerceCommonI18NService() {
		return commerceCommonI18NService;
	}


	public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService) {
		this.commerceCommonI18NService = commerceCommonI18NService;
	}


	public I18NService getI18NService() {
		return i18NService;
	}


	public void setI18NService(final I18NService i18nService) {
		i18NService = i18nService;
	}

}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;

import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.facades.data.WishlistEntryData;

import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.converters.populator.ProductPricePopulator;
import de.hybris.platform.commercefacades.product.converters.populator.ProductStockPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.converters.ConfigurablePopulator;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;

public class GPWishlistEntryPopulator implements Populator<Wishlist2EntryModel, WishlistEntryData> {

	private Converter<ProductModel, ProductData> productConverter;
	
	@Resource(name = "productPricePopulator")
	private ProductPricePopulator<ProductModel, ProductData> productPricePopulator;
	
	@Resource(name = "productStockPopulator")
	private ProductStockPopulator<ProductModel, ProductData> productStockPopulator;
	
	private CommonI18NService commonI18NService;
	private I18NService i18NService;
	private CommerceCommonI18NService commerceCommonI18NService;
	private final ConcurrentMap<String, NumberFormat> currencyFormats = new ConcurrentHashMap<>();
	private ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator;

	@Override
	public void populate(Wishlist2EntryModel source, WishlistEntryData target) throws ConversionException {
		boolean pdfdownload=false ;
		final Collection<ProductOption> options = Arrays.asList(ProductOption.DESCRIPTION,ProductOption.BASIC,ProductOption.CLASSIFICATION,ProductOption.SPECIFICATIONS,ProductOption.REFERENCES,ProductOption.BUNDLE);
		if(null != source) {
			if(source.getWishlist()!=null && source.getWishlist().isPdfImage() && source.getProduct() != null) {
				pdfdownload=true;
				source.getProduct().setIsPdfImage(pdfdownload);
			}
			target.setProduct(getProductConverter().convert(source.getProduct()));
			target.setWishlistUid(source.getWishlist().getWishlistUid());
			target.setWishlistName(source.getWishlist().getName());
			target.setQuantity(source.getQuantity());
			target.setCustomAttr1Value(source.getCustomAttr1Value());
			target.setCustomAttr2Value(source.getCustomAttr2Value());
			target.setCustomAttr3Value(source.getCustomAttr3Value());
			
			productPricePopulator.populate(source.getProduct(), target.getProduct());
			if(null!=target.getProduct().getPrice()) {
				BigDecimal bigDecimalQuantiy = BigDecimal.valueOf(source.getQuantity());
				BigDecimal totalPrice = target.getProduct().getPrice().getValue().multiply(bigDecimalQuantiy);
				CurrencyModel currency = getCommonI18NService().getCurrency(target.getProduct().getPrice().getCurrencyIso());
				String formattedTotalPrice = formatPrice(totalPrice,currency);
				target.setTotalPrice(formattedTotalPrice);
			}
			productStockPopulator.populate(source.getProduct(), target.getProduct());
			getProductConfiguredPopulator().populate(source.getProduct(), target.getProduct(), options);
		}
	}
	
	public String formatPrice(final BigDecimal value, final CurrencyModel currency)
	{
		final LanguageModel currentLanguage = getCommonI18NService().getCurrentLanguage();
		Locale locale = commerceCommonI18NService.getLocaleForLanguage(currentLanguage);
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
	
	protected NumberFormat createNumberFormat(Locale locale, CurrencyModel currency) {
		
		final DecimalFormat currencyFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
		GPFunctions.adjustDigits(currencyFormat, currency);
		GPFunctions.adjustSymbol(currencyFormat, currency);
		return currencyFormat;
	}
	
	public Converter<ProductModel, ProductData> getProductConverter() {
		return productConverter;
	}

	public void setProductConverter(Converter<ProductModel, ProductData> productConverter) {
		this.productConverter = productConverter;
	}

	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	public void setCommonI18NService(CommonI18NService commonI18NService) {
		this.commonI18NService = commonI18NService;
	}

	public I18NService getI18NService() {
		return i18NService;
	}

	public void setI18NService(I18NService i18nService) {
		i18NService = i18nService;
	}

	public CommerceCommonI18NService getCommerceCommonI18NService() {
		return commerceCommonI18NService;
	}

	public void setCommerceCommonI18NService(CommerceCommonI18NService commerceCommonI18NService) {
		this.commerceCommonI18NService = commerceCommonI18NService;
	}
	
	public ConfigurablePopulator<ProductModel, ProductData, ProductOption> getProductConfiguredPopulator() {
		return productConfiguredPopulator;
	}

	public void setProductConfiguredPopulator(ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator) {
		this.productConfiguredPopulator = productConfiguredPopulator;
	}

}

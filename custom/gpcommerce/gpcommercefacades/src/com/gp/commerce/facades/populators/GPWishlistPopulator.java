/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;

import com.gp.commerce.core.wishlist.services.GPWishlistService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.gp.commerce.facades.data.WishlistData;
import com.gp.commerce.facades.data.WishlistEntryData;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.acceleratorfacades.device.ResponsiveMediaFacade;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;
import com.gp.commerce.core.util.GPFunctions;

public class GPWishlistPopulator implements Populator<Wishlist2Model, WishlistData>{

	private Converter<Wishlist2EntryModel, WishlistEntryData> wishlistEntryConverter;

	/** The responsive media facade. */
	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	/** The commerce common I 18 N service. */
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;
	
	/** The GP Wishlist service. */
	@Resource(name = "gpWishlistService")
	private GPWishlistService gpWishlistService;
	
	private CommonI18NService commonI18NService;
	private I18NService i18NService;
	private final ConcurrentMap<String, NumberFormat> currencyFormats = new ConcurrentHashMap<>();

	
	@Override
	public void populate(final Wishlist2Model source, final WishlistData target) throws ConversionException
	{
		if (null != source)
		{
			target.setDescription(source.getDescription());
			if(null != source.getSite())
			{
				target.setCmssite(source.getSite().getUid());
			}
			if (null != source.getWishlistType())
			{
				target.setWishlistType(source.getWishlistType().toString());
			}
			target.setName(source.getName());
			target.setWishlistUid(source.getWishlistUid());
			target.setWishlistEntries(getWishlistEntryConverter().convertAll(source.getEntries()));
			target.setCtaText(source.getCtaText());
			target.setCustomAttr1(source.getCustomAttr1());
			target.setCustomAttr2(source.getCustomAttr2());
			target.setCustomAttr3(source.getCustomAttr3());
			final List<ImageData> mediaList = responsiveMediaFacade
					.getImagesFromMediaContainer(source.getWishListMedia(commerceCommonI18NService.getCurrentLocale()));
			if(CollectionUtils.isNotEmpty(mediaList))
			{
				for (final ImageData imageData : mediaList)
				{
					if(imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
					{
						target.setBannerImageD(imageData.getUrl());
					}
					if(imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.MOBILE.toString()))
					{
						target.setBannerImageM(imageData.getUrl());
					}
					if(imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.TABLET.toString()))
					{
						target.setBannerImageT(imageData.getUrl());
					}
				}
			}
			

			if (CollectionUtils.isNotEmpty(target.getWishlistEntries()))
			{
				String currencyIso = null;
				for(WishlistEntryData entry: target.getWishlistEntries()) {
					if(null!=entry.getProduct().getPrice()) {
						currencyIso=entry.getProduct().getPrice().getCurrencyIso();
					}
					break;
				}
				if(StringUtils.isNotEmpty(currencyIso) && currencyIso != null)
				{
					CurrencyModel currency = getCommonI18NService().getCurrency(currencyIso);
					String formattedSubTotal = formatPrice(gpWishlistService.calculateSubtotal(target.getWishlistEntries()).setScale(2, BigDecimal.ROUND_FLOOR),currency);
					target.setSubtotal(formattedSubTotal);
				}
				target.setNoOfProducts(getToTalQtyForProduct(target.getWishlistEntries()).intValue());
				
			 
			}
			
			target.setModifiedTime(source.getModifiedtime());
			
		}
	}
	
	protected Long getToTalQtyForProduct(final List<WishlistEntryData> wishlistEntryList) {
		Long totalQty=0L ;
		for(WishlistEntryData wishlistData : wishlistEntryList) {
			totalQty+=wishlistData.getQuantity() ;
		}
		return totalQty ;
	}
	
	protected String formatPrice(final BigDecimal value, final CurrencyModel currency)
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
	
	protected NumberFormat createNumberFormat(final Locale locale, final CurrencyModel currency)
	{
		final DecimalFormat currencyFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
		GPFunctions.adjustDigits(currencyFormat, currency);
		GPFunctions.adjustSymbol(currencyFormat, currency);
		return currencyFormat;
	}
	
	public Converter<Wishlist2EntryModel, WishlistEntryData> getWishlistEntryConverter() {
		return wishlistEntryConverter;
	}

	public void setWishlistEntryConverter(final Converter<Wishlist2EntryModel, WishlistEntryData> wishlistEntryConverter) {
		this.wishlistEntryConverter = wishlistEntryConverter;
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

}

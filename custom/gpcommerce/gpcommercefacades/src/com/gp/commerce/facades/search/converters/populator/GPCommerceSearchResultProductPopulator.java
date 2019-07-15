/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.search.converters.populator;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.commercefacades.search.converters.populator.SearchResultVariantOptionsProductPopulator;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.variants.model.VariantProductModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.price.service.impl.GPDefaultEurope1PriceFactory;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.services.GPUserService;

public class GPCommerceSearchResultProductPopulator extends SearchResultVariantOptionsProductPopulator{

	private static final String SHOW_VARIANT_PRODUCTS = "showVariantProducts";

	private static final String IS_AVAILABLE_ENABLED = "isAvailable.enabled";

	private static final String PRODUCT_DISTRIBUTORS = "productDistributors";

	private static final String STOCK_LEVEL_STATUS = "stockLevelStatus";

	private static final String WEBLIST_PRICE = "weblistPrice";

	private static final String NEXT_DELIVERY_DATE = "nextDeliveryDate";

	private static final String REPLACEMENT_PRODUCT_CODE = "replacementProductCode";

	private static final String ONLINE_DATE = "onlineDate";

	private static final String OFFLINE_DATE = "offlineDate";

	private static final String ORIGINAL_CODE = "originalCode";

	private static final String NUMBER_OF_REVIEWS = "numberOfReviews";

	private static final String REVIEW_AVG_RATING = "reviewAvgRating";

	private static final String SEASONAL = "seasonal";

	private static final String CUSTOMIZABLE = "customizable";

	private static final String BUNDLE_AVAILABLE = "bundleAvailable";

	private static final String BULK_BUY = "bulkBuy";

	private static final String SUBSCRIBABLE = "subscribable";

	private static final String ONLINE_ONLY = "onlineOnly";

	private static final String CERTIFICATIONS = "certifications";

	private static final String PROMO_TEXT = "promoText";

	private static final String ASSET_CODE = "assetCode";

	private static final String DROP_SHIP_ELIGIBLE = "dropShipEligible";

	private static final String MATERIAL_STATUS = "materialStatus";

	private static final String MAX_ORDER_QUANTITY = "maxOrderQuantity";

	private static final String MIN_ORDER_QUANTITY = "minOrderQuantity";

	private static final String SAMPLE = "sample";

	private static final String OBSELETE_OUT_OF_STOCK = "obseleteOutOfStock";

	private static final String SELLING_STATEMENT = "sellingStatement";

	private static final String CERTIFICATE ="certificate";
	private static final String FREESHIPPING = "freeShipping";
	private static final String ONLINE ="online_only";
	private static final String BUNDLE ="bundle_available";
	private static final String INSTALLABLE ="installable";

	private static final int MAXICONS=5;

	@Resource(name = "gpPriceFactory")
	private GPDefaultEurope1PriceFactory gpPriceService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name="productService")
	private ProductService productService;

	@Resource(name="variantOptionDataConverter")
	private Converter<VariantProductModel, VariantOptionData> variantOptionDataConverter;

	@Resource(name = "userService")
	private GPUserService userService;

	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	private Converter<MediaModel, ImageData> imageConverter;

	@Override
	public void populate(final SearchResultValueData source, final ProductData target)
	{
		super.populate(source, target);
		target.setCode(this.<String> getValue(source, ORIGINAL_CODE));
		target.setBvNumberOfReviews(this.<Integer> getValue(source, NUMBER_OF_REVIEWS));
		target.setBvAverageRating((this.<Double> getValue(source, REVIEW_AVG_RATING)));
		target.setIsSeasonal(this.<Boolean> getValue(source, SEASONAL));
		target.setIsCustomizable(this.<Boolean> getValue(source, CUSTOMIZABLE));
		target.setIsBundleAvailable(this.<Boolean> getValue(source, BUNDLE_AVAILABLE));
		target.setIsBulkBuy(this.<Boolean> getValue(source, BULK_BUY));
		target.setIsSubscribable(this.<Boolean> getValue(source, SUBSCRIBABLE));
		target.setIsOnlineOnly(this.<Boolean> getValue(source, ONLINE_ONLY));
		target.setIsCertified(this.<Boolean> getValue(source, CERTIFICATIONS));
		target.setPromoText(this.<String> getValue(source, PROMO_TEXT));
		target.setAssetCode(this.<String> getValue(source, ASSET_CODE));
		target.setIsDropShipEligible(this.<Boolean> getValue(source, DROP_SHIP_ELIGIBLE));
		target.setMaterialStatus(this.<String> getValue(source, MATERIAL_STATUS));
		target.setSellingstmt(this.<String> getValue(source, SELLING_STATEMENT));
		final Integer maxOrderQuantity = this.<Integer> getValue(source, MAX_ORDER_QUANTITY);
		target.setMaxOrderQuantity(maxOrderQuantity);
		final Integer minOrderQuantity = this.<Integer> getValue(source, MIN_ORDER_QUANTITY);
		target.setMinOrderQuantity(minOrderQuantity);
		if (null != maxOrderQuantity)
		{
			if (null != target.getStock() && null != target.getStock().getStockLevel())
			{
					target.setMaxOrderableQuantity(
								Math.min(maxOrderQuantity, target.getStock().getStockLevel().intValue()));
			}
			else
			{
					target.setMaxOrderableQuantity(maxOrderQuantity);
			}
		}
		else if (null != target.getStock() && null != target.getStock().getStockLevel())
		{
			target.setMaxOrderableQuantity(target.getStock().getStockLevel().intValue());
		}
		target.setOfflineDate(this.<String> getValue(source, OFFLINE_DATE));
		target.setOnlineDate(this.<String> getValue(source, ONLINE_DATE));
		target.setReplacementProductCode(this.<String> getValue(source, REPLACEMENT_PRODUCT_CODE));

		final Collection<VariantProductModel> variants = productService.getProductForCode(target.getCode()).getVariants();
		if ((target.getBaseProduct() == null || target.getCode().equalsIgnoreCase(target.getBaseProduct()))
				&& CollectionUtils.isNotEmpty(variants))
		{
			target.setHasVariant(Boolean.TRUE);
			target.setVariantOptions(Converters.convertAll(variants, variantOptionDataConverter));
			if(!Boolean.valueOf(cmsSiteService.getSiteConfig(SHOW_VARIANT_PRODUCTS))) {
				VariantOptionData firstVariant = null;
				for(VariantOptionData variant: target.getVariantOptions()) {
					if(!StockLevelStatus.OUTOFSTOCK.equals(variant.getStock().getStockLevelStatus())) {
						firstVariant = variant;
						break;
					}
				}
				if(firstVariant == null) {
					firstVariant = target.getVariantOptions().get(0);
				}
				final String code = firstVariant.getCode();
				target.setUrl(firstVariant.getUrl());
				variants.stream().filter(product-> product.getCode().equals(code)).findFirst().ifPresent(variant->{
					if(null!=variant.getThumbnail()) {
						List<ImageData> images = new ArrayList<>();
						addImageData(variant.getThumbnail(), images, "thumbnail");
						addImageData(variant.getPicture(), images, "product");
						target.setImages(images);
					}
				});
				
			}
			
		}else {
			target.setHasVariant(Boolean.FALSE);
		}
		if(target.getStock() != null)
		{
			target.getStock().setNextAvailableDate(getValue(source,NEXT_DELIVERY_DATE));
		}

		final UserModel userModel = userService.getCurrentUser();

		populateWeblistPrice(source,target);
		populateCmirCodes(source,target, userModel);


		if (Boolean.valueOf(cmsSiteService.getSiteConfig(IS_AVAILABLE_ENABLED)))
		{
			populateIsAvailable(source, target);
		}

		populateApprovedSampleStatus(source, target, userModel);

		target.setIsSample(this.<Boolean> getValue(source, SAMPLE));

		target.setObseleteOutOfStock(this.<Boolean> getValue(source, OBSELETE_OUT_OF_STOCK));
		target.setCompetitorReplacementProductCodes(getValue(source, "competitorReplacementProducts"));

		target.setProductWidth(this.<String> getValue(source, "product_width"));
		target.setProductWidthUOM(this.<String> getValue(source, "product_uom"));
		target.setProductHeight(this.<String> getValue(source, "product_height"));
		target.setProductHeightUOM(this.<String> getValue(source, "product_uom"));
		target.setProductLength(this.<String> getValue(source, "product_length"));
		target.setProductLengthUOM(this.<String> getValue(source, "product_uom"));
		target.setItemPerEachUOM(this.<String> getValue(source, "product_itemspereachuom"));
		target.setEachPerShipUnit(this.<String> getValue(source, "product_eachpershipunit"));
		target.setEachPerShipUnitUOM(this.<String> getValue(source, "product_eachpershipunituom"));
		target.setProductCapacity(this.<String> getValue(source, "product_capacity"));
		target.setItemPerEach(this.<String> getValue(source, "product_itemspereach"));

		populateProductIcons(target,source);
	}

	private void populateProductIcons(final ProductData target, final SearchResultValueData source)
	{
		final List<String> productIcons = new ArrayList<String>();
		final String gpxpressSiteId = configurationService.getConfiguration().getString(GpcommerceCoreConstants.GPXPRESS_SITE_ID);
		if(cmsSiteService.getCurrentSite().getUid().equalsIgnoreCase(gpxpressSiteId))
		{
			if (checkValue(source, CERTIFICATIONS))
			{
				productIcons.add(CERTIFICATE);
			}
			if (checkValue(source, SEASONAL))
			{
				productIcons.add(SEASONAL);
			}
			if (checkValue(source, CUSTOMIZABLE))
			{
				productIcons.add(CUSTOMIZABLE);
			}
			if (checkValue(source, SAMPLE))
			{
				productIcons.add(SAMPLE);
			}
		}
		else
		{
   		if (checkValue(source, CERTIFICATIONS))
   		{
   			productIcons.add(CERTIFICATE);
   		}
   		if (checkValue(source, FREESHIPPING))
   		{
   			productIcons.add(FREESHIPPING);
   		}
   		if (checkValue(source,ONLINE_ONLY))
   		{
   			productIcons.add(ONLINE);
   		}
   		if (checkValue(source, SUBSCRIBABLE))
   		{
   			productIcons.add(SUBSCRIBABLE);
   		}
   		if (checkValue(source, SEASONAL))
   		{
   			productIcons.add(SEASONAL);
   		}
   		if (productIcons.size() < MAXICONS && checkValue(source, BUNDLE_AVAILABLE))
   		{
   			productIcons.add(BUNDLE);
   		}
   		if (productIcons.size() < MAXICONS && checkValue(source, CUSTOMIZABLE))
   		{
   			productIcons.add(CUSTOMIZABLE);
   		}
   		if (productIcons.size() < MAXICONS && checkValue(source, INSTALLABLE))
   		{
   				productIcons.add(INSTALLABLE);
   		}
		}
		target.setProductIcons(productIcons);
	}

	private boolean checkValue(final SearchResultValueData source, final String property)
	{
		if (null != this.<Boolean> getValue(source, property) && (this.<Boolean> getValue(source, property)).equals(Boolean.TRUE))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private void addImageData(final MediaModel media, final List<ImageData> images, final String format) {
		final ImageData img = getImageConverter().convert(media);
		img.setImageType(ImageDataType.PRIMARY);
		img.setFormat(format);
		images.add(img);
	}

	private void populateCmirCodes(final SearchResultValueData source, final ProductData target, final UserModel userModel) {
		if(userModel instanceof B2BCustomerModel){
			final B2BCustomerModel customer = (B2BCustomerModel) userModel;
			final List<B2BUnitModel> b2bUnits = customer.getGroups().stream().
					filter(i -> i instanceof B2BUnitModel).map(i-> (B2BUnitModel) i).collect(Collectors.toList());
			if(null != b2bUnits){
				final List<String> b2bUid =b2bUnits.stream().map(i->i.getUid()).collect(Collectors.toList());
				final List<String> cmirCodes = getValue(source, "cmirCodes");
				if(null!=cmirCodes){
					final List<String[]> codes = cmirCodes.stream().map((i->i.split(":"))).collect(Collectors.toList());
					for (final String[] code : codes) {
						for (int j = 0; j < code.length; j++) {
							if(b2bUid.contains(code[j])){
								target.setCmirCode(j+1 <= code.length ? code[j+1] : "");
								break;
							}
						}
					}
				}
			}


		}


	}

	/**
	 * Checks if the product is available with the b2b unit of the user.
	 *
	 * @param source
	 * @param target
	 */
	private void populateIsAvailable(final SearchResultValueData source, final ProductData target)
	{
		final List<String> distributorIds = getValue(source, PRODUCT_DISTRIBUTORS);
		// user instance check is taken care while setting session attribute
		if (null != distributorIds && null != sessionService.getAttribute(GpcommerceCoreConstants.SOLD_TO_ID)
				&& distributorIds.contains(
						((B2BUnitModel) sessionService.getAttribute(GpcommerceCoreConstants.SOLD_TO_ID)).getUid())) {
			target.setIsAvailableForLocation(true);
		} else {
			target.setIsAvailableForLocation(false);
		}
	}


	/**
	 * populate user approved sample status for b2b user.
	 *
	 * @param source
	 * @param target
	 */
	private void populateApprovedSampleStatus(final SearchResultValueData source, final ProductData target, final UserModel userModel)
	{
		// user instance check is taken care while setting session attribute
		if ( null != sessionService.getAttribute(GpcommerceCoreConstants.SOLD_TO_ID) && userModel instanceof B2BCustomerModel) {
			final B2BCustomerModel customer = (B2BCustomerModel) userModel;
			target.setApprovedSampleStatus(customer.getApprovedSampleStatus());
		} else {
			target.setApprovedSampleStatus(false);
		}
	}

	private void populateWeblistPrice(final SearchResultValueData source, final ProductData target)
	{

		final PriceDataType priceType;
		if (CollectionUtils.isEmpty(source.getVariants()))
		{
			priceType = PriceDataType.BUY;
		}
		else
		{
			priceType = PriceDataType.FROM;
		}

		final Double webPriceValue = this.<Double> getValue(source, WEBLIST_PRICE);
		final Double priceValue = this.<Double> getValue(source, "priceValue");

		if (webPriceValue != null && priceValue !=null && webPriceValue > priceValue)
		{
			final PriceData priceData = getPriceDataFactory().create(priceType, BigDecimal.valueOf(webPriceValue),
					getCommonI18NService().getCurrentCurrency());
			target.setWeblistPrice(priceData);
		}

	}

	/**
	 * populate live stock in search results
	 *
	 * @param source
	 * @param target
	 */
	@Override
	protected void populateStock(final SearchResultValueData source, final ProductData target)
	{
		final String stockLevelStatus = this.<String> getValue(source, STOCK_LEVEL_STATUS);
		if (StringUtils.isNotEmpty(stockLevelStatus))
		{
			final StockLevelStatus stockLevelStatusEnum = StockLevelStatus.valueOf(stockLevelStatus);

			if (StockLevelStatus.LOWSTOCK.equals(stockLevelStatusEnum) || StockLevelStatus.INSTOCK.equals(stockLevelStatusEnum))
			{
				try
				{
					// In case of low stock then make a call to the stock service to determine if in or out of stock.
					// In this case (low stock) it is ok to load the product from the DB and do the real stock check
					final ProductModel productModel = getProductService().getProductForCode(target.getCode());
					if (productModel != null)
					{
						target.setStock(getStockConverter().convert(productModel));
					}
				}
				catch (final UnknownIdentifierException ex)
				{
					// If the product is no longer visible to the customergroup then this exception can be thrown

					// We can't remove the product from the results, but we can mark it as out of stock
					target.setStock(getStockLevelStatusConverter().convert(StockLevelStatus.OUTOFSTOCK));
				}
			}
			else
			{
				target.setStock(getStockLevelStatusConverter().convert(stockLevelStatusEnum));
			}
		}
	}

	public GPDefaultEurope1PriceFactory getGpPriceFactory() {
		return gpPriceService;
	}

	public void setGpPriceFactory(final GPDefaultEurope1PriceFactory gpPriceFactory) {
		this.gpPriceService = gpPriceFactory;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(final ModelService modelService) {
		this.modelService = modelService;
	}

	@Override
	public ProductService getProductService() {
		return productService;
	}

	@Override
	public void setProductService(final ProductService productService) {
		this.productService = productService;
	}

	public Converter<VariantProductModel, VariantOptionData> getVariantOptionDataConverter() {
		return variantOptionDataConverter;
	}

	public void setVariantOptionDataConverter(final Converter<VariantProductModel, VariantOptionData> variantOptionDataConverter) {
		this.variantOptionDataConverter = variantOptionDataConverter;
	}

	public Converter<MediaModel, ImageData> getImageConverter() {
		return imageConverter;
	}

	public void setImageConverter(final Converter<MediaModel, ImageData> imageConverter) {
		this.imageConverter = imageConverter;
	}
}

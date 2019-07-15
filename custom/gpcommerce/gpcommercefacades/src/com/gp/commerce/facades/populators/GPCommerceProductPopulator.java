/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.converters.populator.ProductPopulator;
import de.hybris.platform.commercefacades.product.data.CategoryData ;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.commerceservices.stock.strategies.CommerceAvailabilityCalculationStrategy;
import de.hybris.platform.commerceservices.stock.strategies.WarehouseSelectionStrategy;
import de.hybris.platform.contextualattributevalues.model.ContextualAttributeValueModel;
import de.hybris.platform.contextualattributevalues.services.ContextualAttributeValuesSessionService;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.variants.model.VariantCategoryModel;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantValueCategoryModel;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.core.catalog.GPVariantUtils;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.CertificationsEnum;
import com.gp.commerce.core.enums.ServiceProductTypeEnum;
import com.gp.commerce.core.model.AlternateUOMModel;
import com.gp.commerce.core.model.GPCertificationsModel;
import com.gp.commerce.core.model.GPCommerceProductModel;
import com.gp.commerce.core.model.GPDefaultVariantCategoryModel;
import com.gp.commerce.core.model.GPDefaultVariantValueCategoryModel;
import com.gp.commerce.core.model.GPProductKitModel;
import com.gp.commerce.core.model.GPProductTabInfoModel;
import com.gp.commerce.core.model.GPServiceProductModel;
import com.gp.commerce.core.model.GPVariantProductKitModel;
import com.gp.commerce.core.price.service.GPFilteredPriceContainer;
import com.gp.commerce.core.price.service.impl.GPDefaultEurope1PriceFactory;
import com.gp.commerce.core.product.services.GPProductService;
import com.gp.commerce.core.product.services.impl.GPB2BDefaultProductService;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.util.GPCommerceCoreUtils;
import com.gp.commerce.core.util.GPProductSpecificationUtils;
import com.gp.commerce.core.util.GPSiteConfigUtils;
import com.gp.commerce.facades.product.data.GiftWrapData;
import com.gp.commerce.facades.product.data.GpCertificationsData;
import com.gp.commerce.facades.product.data.InstallableData;
import com.gp.commerce.facades.product.data.ProductInfoTabData;

public class GPCommerceProductPopulator extends ProductPopulator
{

	private static final Logger LOG = Logger.getLogger(GPCommerceProductPopulator.class);
	private static final String ENABLE_SAMPLECART = "enable.samplecart";

	private static final String IS_AVAILABLE_ENABLED = "isAvailable.enabled";
	private static final String IMAGE_TYPE = "_3D";

	private static final String PCE ="PCE";
	private static final String EA ="EA";
	
	private static final String CERTIFICATE ="certificate";
	private static final String FREESHIPPING = "freeShipping";
	private static final String ONLINE ="online_only";
	private static final String SUBSCRIBABLE ="subscribable";
	private static final String SEASONAL ="seasonal";
	private static final String BUNDLE ="bundle_available";
	private static final String CUSTOMIZABLE ="customizable";
	private static final String INSTALLABLE ="installable";
	private static final String SAMPLE ="sample";
	
	private static final int MAXICONS=5; 
	private static final String CS = "CS";
	private static final String ZSU = "ZSU";

	@Resource(name = "gpB2BProductService")
	private GPB2BDefaultProductService gpB2BProductService;

	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "gpPriceFactory")
	GPDefaultEurope1PriceFactory gpPriceService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;
	
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	private ConfigurationService configurationService;

	@Resource
	private CommerceStockService commerceStockService;

	@Resource
	private BaseStoreService baseStoreService;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name="stockService")
	private StockService stockService;

	@Resource(name="warehouseSelectionStrategy")
	private WarehouseSelectionStrategy warehouseSelectionStrategy;

	@Resource(name="commerceStockLevelCalculationStrategy")
	private CommerceAvailabilityCalculationStrategy commerceStockLevelCalculationStrategy;

	@Resource(name="gpProductService")
	private GPProductService gpProductService;


	@Resource(name = "gpProductSpecificationUtils")
	private GPProductSpecificationUtils gpProductSpecificationUtils;
	
	@Resource(name = "gpCommerceCoreUtils")
	private GPCommerceCoreUtils gpCommerceCoreUtils ;

	private Converter<CategoryModel, CategoryData> categoryConverter;
	private Converter<GPProductTabInfoModel, ProductInfoTabData> productInfoTabConverter;
	
	@Resource
	private GPVariantUtils gpVariantUtils ;
 
	public Converter<GPProductTabInfoModel, ProductInfoTabData> getProductInfoTabConverter() {
		return productInfoTabConverter;
	}

	public void setProductInfoTabConverter(final Converter<GPProductTabInfoModel, ProductInfoTabData> productInfoTabConverter) {
		this.productInfoTabConverter = productInfoTabConverter;
	}

	public CommerceStockService getCommerceStockService() {
		return commerceStockService;
	}

	public void setCommerceStockService(final CommerceStockService commerceStockService) {
		this.commerceStockService = commerceStockService;
	}

	public BaseStoreService getBaseStoreService() {
		return baseStoreService;
	}

	public void setBaseStoreService(final BaseStoreService baseStoreService) {
		this.baseStoreService = baseStoreService;
	}

	@Override
	public void populate(final ProductModel source, final ProductData target)
	{
		//Set Base attributes
		target.setCode(source.getCode());
		target.setName(source.getName());
		target.setProductURL(source.getCanonicalUrl());

		//set Indicators
		target.setIsCustomizable(source.getCustomizable());
		target.setIsBulkBuy(source.getBulkBuy());
		target.setIsBundleAvailable(source.getBundleAvailable());
		target.setIsLeaseable(source.getLeaseable());
		target.setIsDropShipEligible(source.getDropShipEligible());
		target.setIsLockerEligible(source.getLockerEligible());
		target.setIsPrivateLabel(source.getPrivateLabel());
		target.setIsSample(source.getSample());
		target.setIsOnlineOnly(source.getOnlineOnly());
		target.setIsSubscribable(source.getSubscribable());
		target.setIsSeasonal(source.getSeasonal());
		target.setIsFreeShipping(source.getIsFreeShippingAllowed());
		target.setEan(source.getSapEAN());
		target.setIsSample(source.getSample());
		target.setKaonUrl(source.getKaonUrl());
		final List<String> galleryImagesUrl = new ArrayList<>();
		if(!source.getGalleryImages().isEmpty()) {
			for(final MediaContainerModel mediaContainer: source.getGalleryImages()) {
				for(final MediaModel media: mediaContainer.getMedias()) {
					if(media.getRealFileName() !=null && !media.getRealFileName().contains(IMAGE_TYPE)) {
						galleryImagesUrl.add(media.getURL());
					}
				}
			}
		}
		if(source.isIsPdfImage()){
		final List<String> encodedImageList = new ArrayList<>();
		if (target.getImages()!=null && !(target.getImages().isEmpty())) {
			 final ImageData imageData = target.getImages().iterator().next() ;
				final String imageUrl = imageData.getUrl();
				try {
					final URL url = new URL(imageUrl);
					final InputStream is = url.openStream();
					final byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(is);
					final String encodedImage = Base64.getEncoder().encodeToString(bytes);
					encodedImageList.add(encodedImage);
				} catch (final Exception e) {
					LOG.error("Error while encoding image to Base64" + e.getMessage(),e);

				}
		}
			target.setEncodedImages(encodedImageList);
		}
		if(!galleryImagesUrl.isEmpty()) {

			target.setGalleryImages(galleryImagesUrl);
		}

		if (null != source.getReplacedBy())
		{
			target.setReplacementProductCode(source.getReplacedBy().getCode());
		}

		if (null != source.getUnit() && null != source.getUnit().getName())
		{
			target.setUnit(source.getUnit().getName());
			final String pallet = configurationService.getConfiguration().getString("sales.unit.name.pallet");
			if(pallet.equalsIgnoreCase(source.getUnit().getName())) {
				target.setIsPallet(true);
			}
		}

		if (source instanceof GPCommerceProductModel)
		{
			final GPCommerceProductModel productModel = (GPCommerceProductModel) findBaseProduct(source);

			final GiftWrapData giftWrapdata = new GiftWrapData();
			giftWrapdata.setGiftWrapped(false);
			if (null != productModel.getGiftWrapProduct())
			{
				giftWrapdata.setGiftWrapProductCode(Integer.parseInt(productModel.getGiftWrapProduct().getCode()));
				giftWrapdata.setGiftMessage(productModel.getGiftWrapProduct().getDescription());
				giftWrapdata.setGiftWrapped(true);
			}
			target.setGiftWrapProduct(giftWrapdata);
			final InstallableData installableData = new InstallableData();
			installableData.setInstallable(false);
			if (null != productModel.getInstallationProduct())
			{
				installableData.setInstallableProductCode(productModel.getInstallationProduct().getCode());
				installableData.setInstallable(true);
				installableData.setInstallableMessage(productModel.getInstallationProduct().getDescription());
			}
			target.setInstallationProduct(installableData);

		}

		target.setDisplayAttributes(source.getDisplayAttributes());
		
		target.setAssetCode(source.getAssetCode());
		/**Added to get productInfoTab name,sequence and content**/
		final List<ProductInfoTabData> productInfoDataList = Converters.convertAll(source.getTabInfo(), getProductInfoTabConverter());
		target.setTabInfo(productInfoDataList);
		target.setDefaultTabName(source.getDefaultTabName());

		/**Added to get category name and description**/
		Collection<CategoryModel> categoryModels = GPCommerceCoreUtils.getBaseProduct(source).getSupercategories();
		List<CategoryModel> displayCategories = GPCommerceCoreUtils.getDisplayCategories(categoryModels);
		final List<CategoryData> categoryDataList = Converters.convertAll(displayCategories,
				getCategoryConverter());
		final Collection<CategoryData> categoryDataCollection = new ArrayList<CategoryData>(categoryDataList);
		target.setCategories(categoryDataCollection);

		if (null != source.getMaxOrderQuantity())
		{
			target.setMaxPurchaseableQuantity(source.getMaxOrderQuantity());
		}

		Integer stockLevel = null;
		final Collection<StockLevelModel> stockLevels=getStockLevels(source, getBaseStoreService().getCurrentBaseStore());

		final Long stockLevelFromService = CollectionUtils.isNotEmpty(stockLevels) ? getStockForProduct(stockLevels):null;

		if (null != stockLevelFromService)
		{
			stockLevel = stockLevelFromService.intValue();
			if(source instanceof GPProductKitModel || source instanceof GPVariantProductKitModel)
			{
			stockLevel = stockLevel.intValue()/gpProductService.getKitComponentQuantity(source, stockLevels).intValue();
			}
		}

		if (null != source.getMaxOrderQuantity() || null!=stockLevel)
		{
			if (null != source.getMaxOrderQuantity())
			{
					final Integer maxOrderQuantity = source.getMaxOrderQuantity();
					final Integer availableStock = (null != stockLevel ? stockLevel : maxOrderQuantity);
					target.setMaxOrderableQuantity(Math.min(availableStock, maxOrderQuantity));
			}
			else if (null != stockLevel)
			{
					target.setMaxOrderableQuantity(stockLevel);
			}
		}

		if (null != source.getGpcertifications() && !source.getGpcertifications().isEmpty())
		{
			final List<GpCertificationsData> gpCertifications = new ArrayList<>();
			for (final GPCertificationsModel certificate : source.getGpcertifications())
			{
				final GpCertificationsData certData = new GpCertificationsData();

				certData.setId(certificate.getId().getCode());
				certData.setUrl(certificate.getUrl());
				certData.setDescription(certificate.getDescription());
				gpCertifications.add(certData);
			}
			target.setGpCertifications(gpCertifications);
			target.setIsCertified(true);
		}

		if (null != source.getMaterialStatus())
		{
			target.setMaterialStatus(source.getMaterialStatus().getCode());
		}

		GPFilteredPriceContainer gpFilteredPriceContainer = gpPriceService.getFilteredPriceContainerForProducts(source);
		PriceRowModel listPriceRow = gpFilteredPriceContainer.getListPricePriceRow() ;
		PriceRowModel salePriceRow=gpFilteredPriceContainer.getSalePricePriceRow() ;
		String saleAmountOff=null ;
		String salePercentageOff=null ;
		String saleAmountPercentageOff=null ;
		if(salePriceRow!=null) {
		saleAmountOff=salePriceRow.getAmountOff();
		 salePercentageOff=gpFilteredPriceContainer.calculateRoundOffPercentage(salePriceRow.getPercentageDiscount()); 
		}
 
		double listPrice = gpFilteredPriceContainer.getListPrice();
		double salePrice = gpFilteredPriceContainer.getSalePrice();
		double subscriptionPrice=0.0 ;
		PriceData priceData;
		PriceData subscriptionPriceData;
		final PriceDataType priceType;
		if (CollectionUtils.isEmpty(source.getVariants()))
		{
			priceType = PriceDataType.BUY;
		}
		else
		{
			priceType = PriceDataType.FROM;
		}
		if (listPrice > salePrice && salePrice >= 0d) {
			if ((Boolean) gpCommerceCoreUtils.getProductAttribute(source, ProductModel.SUBSCRIBABLE)) {
				if(salePercentageOff!=null) {
				target.setSubsPercentageDiscount(salePercentageOff);
				}else {
				if(saleAmountOff!=null) {
					saleAmountPercentageOff=gpFilteredPriceContainer.calculatePercentage(saleAmountOff,salePrice);
					target.setSubsPercentageDiscount(saleAmountPercentageOff);
					}
				}
				subscriptionPrice=	gpFilteredPriceContainer.getSubscriptionPrice(salePercentageOff,saleAmountOff,salePrice,1);
				subscriptionPriceData = priceDataFactory.create(priceType, BigDecimal.valueOf(subscriptionPrice),
							gpFilteredPriceContainer.getCurrency());
				target.setSubscriptionPrice(subscriptionPriceData);
				target.setIsSubscribable(true);
				} 
				priceData = priceDataFactory.create(priceType, BigDecimal.valueOf(salePrice),
							gpFilteredPriceContainer.getCurrency());
				target.setPrice(priceData);
		
				priceData = priceDataFactory.create(priceType, BigDecimal.valueOf(listPrice),
							gpFilteredPriceContainer.getCurrency());
				target.setWeblistPrice(priceData);
				} else {
				if ((Boolean) gpCommerceCoreUtils.getProductAttribute(source, ProductModel.SUBSCRIBABLE)) {
					if(listPriceRow.getPercentageDiscount()!=null) {
					target.setSubsPercentageDiscount(listPriceRow.getPercentageDiscount());
					}else{
						if(listPriceRow.getAmountOff()!=null) {
						String listDiscount=gpFilteredPriceContainer.calculatePercentage(listPriceRow.getAmountOff(),listPrice);
						target.setSubsPercentageDiscount(listDiscount);
						}
					}
					subscriptionPrice=	gpFilteredPriceContainer.getSubscriptionPrice(listPriceRow.getPercentageDiscount(),listPriceRow.getAmountOff(),listPrice,1);
					subscriptionPriceData = priceDataFactory.create(priceType, BigDecimal.valueOf(subscriptionPrice),
							gpFilteredPriceContainer.getCurrency());
					target.setSubscriptionPrice(subscriptionPriceData);
					subscriptionPriceData = priceDataFactory.create(priceType, BigDecimal.valueOf(subscriptionPrice),gpFilteredPriceContainer.getCurrency());
						target.setSubscriptionPrice(subscriptionPriceData);
						target.setIsSubscribable(true);
					}
					priceData = priceDataFactory.create(priceType, BigDecimal.valueOf(listPrice),gpFilteredPriceContainer.getCurrency()); 
					target.setPrice(priceData);
				}


		if (null != source.getBvAverageRating())
		{
			target.setBvAverageRating(source.getBvAverageRating());
		}

		if (null != source.getBvNumberOfReviews())
		{
			target.setBvNumberOfReviews(source.getBvNumberOfReviews());
		}

		target.setCanonicalUrl(source.getCanonicalUrl());
		target.setSeoIndex(source.getSeoIndex());
		if (source.getVariantType() != null && CollectionUtils.isNotEmpty(source.getVariants())){
			target.setHasVariant(Boolean.TRUE);
		}else {
			target.setHasVariant(Boolean.FALSE);
		}

		final UserModel user = userService.getCurrentUser();
		if (user instanceof B2BCustomerModel)
		{
			boolean isAvailable = false;
			boolean approvedSampleStatus = false;
			final B2BCustomerModel b2bUser = (B2BCustomerModel) user;

			//Customer material
			populateCMIRCode(source, target, b2bUser);

			approvedSampleStatus = getApprovedSampleStatus(user);
			final B2BUnitModel unit = sessionService.getAttribute(GpcommerceCoreConstants.SOLD_TO_ID);
			if (Boolean.valueOf(cmsSiteService.getSiteConfig(IS_AVAILABLE_ENABLED)) && null != source.getDistributorIds() && null != unit)
			{
				final Optional<B2BUnitModel> soldTo = source.getDistributorIds().stream().filter(i->i.getUid().equalsIgnoreCase(unit.getUid())).findFirst();
				if(null != soldTo)
				{
					isAvailable = true;
				}
			}
			target.setApprovedSampleStatus(approvedSampleStatus);
			setAvailableFlag(isAvailable, target);
		}

		if (null != source.getGtin())
 		{
 		target.setGtin(source.getGtin());
 		}
 		if (null != source.getUpc()) {
 			target.setUpc(source.getUpc());
 		}
 		if(null!=source.getUnspsc()) {
 			target.setUnspsc(source.getUnspsc());
 		}
 		if(null!=source.getUnspscdesc()) {
 			target.setUnspscdesc(source.getUnspscdesc());
 		}
 		if(null!=source.getEachResaleable()) {
 			target.setEachResaleable(source.getEachResaleable());
 		}
 		if(null!=source.getEachheight()) {
 			target.setEachheight(source.getEachheight());
 		}
 		if(null!=source.getWidth()){
 			target.setWidth(source.getWidth());
 		}
 		if(null!=source.getOriginCountry()){
 			target.setOriginCountry(source.getOriginCountry());
 		}
 		if(null!=source.getTariff()){
 			target.setTariff(source.getTariff());
 		}
 		if(null!=source.getProductType()) {
 			target.setProductType(source.getProductType());
 		}
 		if(null!=source.getProductClass()) {
 			target.setProductClass(source.getProductClass());
 		}
 		populateProductIcons(source,target,user);
		populateUOMattributes(source, target);
		populateProductCSVattributes(source,target);
		String gpxpressSiteId = configurationService.getConfiguration().getString(GpcommerceCoreConstants.GPXPRESS_SITE_ID);
		if(cmsSiteService.getCurrentSite().getUid().equalsIgnoreCase(gpxpressSiteId) && source.getSample())
		{
			target.setMaxOrderQuantity(source.getSampleOrderLimit());
		}
		target.setSoldIndividually(BooleanUtils.toBoolean(source.getSoldIndividually()));
	}
	
	private boolean getApprovedSampleStatus(UserModel user) {
		boolean approvedSampleStatus = false;
		if(user instanceof B2BCustomerModel){
			final B2BCustomerModel b2bUser = (B2BCustomerModel) user;
			if (Boolean.valueOf(cmsSiteService.getSiteConfig(ENABLE_SAMPLECART)) && null != b2bUser.getApprovedSampleStatus()
					&& b2bUser.getApprovedSampleStatus())
			{
				approvedSampleStatus = true;
			}
		}
		
		return approvedSampleStatus;
	}
	
	private void populateProductIcons(final ProductModel source, final ProductData target, final UserModel user)
	{
		final List<String> productIcons = new ArrayList<String>();
		String gpxpressSiteId = configurationService.getConfiguration().getString(GpcommerceCoreConstants.GPXPRESS_SITE_ID);
		if(cmsSiteService.getCurrentSite().getUid().equalsIgnoreCase(gpxpressSiteId))
		{
			if (CollectionUtils.isNotEmpty(source.getGpcertifications()))
			{
				productIcons.add(CERTIFICATE);
			}
			if (source.getSeasonal())
			{
				productIcons.add(SEASONAL);
			}
			if (source.getCustomizable())
			{
				productIcons.add(CUSTOMIZABLE);
			}
			if (source.getSample() && getApprovedSampleStatus(user))
			{
				productIcons.add(SAMPLE);
			}
		}
		else
		{
   		if (CollectionUtils.isNotEmpty(source.getGpcertifications()))
   		{
   			productIcons.add(CERTIFICATE);
   		}
   		if (source.getIsFreeShippingAllowed())
   		{
   			productIcons.add(FREESHIPPING);
   		}
   		if (source.getOnlineOnly())
   		{
   			productIcons.add(ONLINE);
   		}
   		if (source.getSubscribable())
   		{
   			productIcons.add(SUBSCRIBABLE);
   		}
   		if (source.getSeasonal())
   		{
   			productIcons.add(SEASONAL);
   		}
   		if (productIcons.size() < MAXICONS && (source.getBundleAvailable()!=null && source.getBundleAvailable()))
   		{
   			productIcons.add(BUNDLE);
   		}
   		if (productIcons.size() < MAXICONS && (source.getCustomizable()!=null && source.getCustomizable()))
   		{
   			productIcons.add(CUSTOMIZABLE);
   		}
   		if (productIcons.size() < MAXICONS &&  source instanceof GPCommerceProductModel)
   		{
   			final GPCommerceProductModel gpServiceProductModel = (GPCommerceProductModel) source;
   			if (gpServiceProductModel.getInstallationProduct()!= null)
   			{
   				productIcons.add(INSTALLABLE);
   			}
   		}
		}
		target.setProductIcons(productIcons);

	}

	private void populateUOMattributes(final ProductModel source, final ProductData target)
	{
		final AlternateUOMModel gpAlternateUOMForProduct = gpProductSpecificationUtils.getGPAlternateUOMForProduct(source, PCE);
		if (null != gpAlternateUOMForProduct)
		{
			target.setProductWidth(gpAlternateUOMForProduct.getWidth());
			target.setProductWidthUOM(gpAlternateUOMForProduct.getDimensionsuom());
			target.setProductLength(gpAlternateUOMForProduct.getLength());
			target.setProductLengthUOM(gpAlternateUOMForProduct.getDimensionsuom());
			target.setProductHeight(gpAlternateUOMForProduct.getHeight());
			target.setProductHeightUOM(gpAlternateUOMForProduct.getDimensionsuom());
		}
		final AlternateUOMModel gpAlternateUOMForProduct_EA = gpProductSpecificationUtils.getGPAlternateUOMForProduct(source, EA);
		if (null != gpAlternateUOMForProduct_EA)
		{
			target.setEachPerShipUnit(gpAlternateUOMForProduct_EA.getAltX());
			target.setEachPerShipUnitUOM(gpAlternateUOMForProduct_EA.getAltXUOM());
		}
		if(null != source.getItemsPerEachUOM())
		{
			target.setItemPerEachUOM(source.getItemsPerEachUOM());
		}
		
	}
	

	/**
	 * Populate all csv specific product attributes 
	 * @param source
	 * @param target
	 */
	private void populateProductCSVattributes(ProductModel source, ProductData target) {
		final AlternateUOMModel alternateUOMForProductZSU = gpProductSpecificationUtils.getGPAlternateUOMForProduct(source, ZSU);
		if(null != alternateUOMForProductZSU)
		{
			target.setUnitQty(alternateUOMForProductZSU.getAltX());
		}
		final AlternateUOMModel alternateUOMForProductCS = gpProductSpecificationUtils.getGPAlternateUOMForProduct(source, CS);
		if(null!= alternateUOMForProductCS)
		{
			target.setCaseWeightUOM(alternateUOMForProductCS.getWeightUOM());

		}
		if(null != source.getUnit() && source.getUnit().getCode().equalsIgnoreCase(CS))
		{
			target.setCaseGtin(source.getGtin());
			target.setCaseWidth(source.getWidth());
			target.setCaseLength(source.getLength());
			target.setCaseHeight(source.getHeight());
			target.setCaseDimensionsUOM(source.getDimensionsUOM());
			target.setCaseVolume(source.getVolume());
			target.setCaseNetWeight(source.getNetweight());
			target.setCaseGrossWeight(source.getGrossWeight());
		}
		else if(null!= alternateUOMForProductCS)
		{
			target.setCaseGtin(alternateUOMForProductCS.getGtin());
			target.setCaseWidth(alternateUOMForProductCS.getWidth());
			target.setCaseLength(alternateUOMForProductCS.getLength());
			target.setCaseHeight(alternateUOMForProductCS.getHeight());
			target.setCaseDimensionsUOM(alternateUOMForProductCS.getDimensionsuom());
			target.setCaseVolume(alternateUOMForProductCS.getVolume());
			target.setCaseNetWeight(alternateUOMForProductCS.getNetWeight());
			target.setCaseGrossWeight(alternateUOMForProductCS.getGrossweight());
		}
		
		
		final AlternateUOMModel alternateUOMForProductPCE = gpProductSpecificationUtils.getGPAlternateUOMForProduct(source, PCE);
		if(null!= alternateUOMForProductPCE)
		{
			target.setCaseTotal(alternateUOMForProductPCE.getAltX());
			target.setCaseTotalUOM(alternateUOMForProductPCE.getAltXUOM());
		}
		
		if(null!= source.getReplacedBy())
		{
			target.setReplacedBy(source.getReplacedBy().getCode());
		}
		final AlternateUOMModel alternateUOMForProductEA = gpProductSpecificationUtils.getGPAlternateUOMForProduct(source, EA);
		if(null!= alternateUOMForProductEA) {
			target.setEachWeightUOM(alternateUOMForProductEA.getWeightUOM());	
		}
		
		if(null != source.getUnit() && source.getUnit().getCode().equalsIgnoreCase(EA))
		{
			target.setEachNetWeight(source.getNetweight());
			target.setEachGrossWeight(source.getGrossWeight());
			target.setEachProdWidth(source.getWidth());
			target.setEachProdLength(source.getLength());
			target.setEachProdHeight(source.getHeight());
			target.setEachDimensionsUOM(source.getDimensionsUOM());
			target.setEachvolume(source.getVolume());
			target.setEachvolumeUOM(source.getEachvolumeuom());
		}
		else if(null!= alternateUOMForProductEA)
		{
			target.setEachNetWeight(alternateUOMForProductEA.getNetWeight());
			target.setEachGrossWeight(alternateUOMForProductEA.getGrossweight());
			target.setEachProdWidth(alternateUOMForProductEA.getWidth());
			target.setEachProdLength(alternateUOMForProductEA.getLength());
			target.setEachProdHeight(alternateUOMForProductEA.getHeight());
			target.setEachDimensionsUOM(alternateUOMForProductEA.getDimensionsuom());
			target.setEachvolume(alternateUOMForProductEA.getVolume());
			target.setEachvolumeUOM(alternateUOMForProductEA.getVolumeuom());
		}
		
		target.setBrandOwner(source.getBrandOwner());
		target.setSubbrand(source.getSubbrand());
		target.setBrandTrademark(source.getBrandTrademark());
		target.setSummary(source.getSummary(commerceCommonI18NService.getCurrentLocale()));
		target.setDescription(source.getDescription(commerceCommonI18NService.getCurrentLocale()));
		target.setMinPCW(source.getMinPCW());
		target.setMinRecycled(source.getMinRecycled());
		target.setEcologoCCD(source.getEcologoCCD());
		target.setLeedOM(source.getLeedOM());
		target.setLeedMRCriteria(source.getLeedMRCriteria());
		target.setLeedIEQCriteria(source.getLeedIEQCriteria());
		target.setLeedINVCriteria(source.getLeedINVCriteria());
		target.setAltMaterials(source.getAltMaterials());
	}

	/**
	 * Sets AvailableForLocation flag on the product
	 *
	 * @param isAvailable
	 * @param target
	 */
	private void setAvailableFlag(final boolean isAvailable, final ProductData target) {
		target.setIsAvailableForLocation(isAvailable);

	}

	/**
	 * Fetch the stock level for the product, to determine minimum of stock level and max order quantity
	 *
	 * @param productModel
	 *           The product for which the stock level is to be fetched.
	 * @param baseStore
	 *           The store for which the stock level has to be fetched.
	 * @return Returns stock level of the product
	 */
	private Long getStockForProduct(final Collection<StockLevelModel> stockLevels) {
		// TODO Auto-generated method stub
		return commerceStockLevelCalculationStrategy.calculateAvailability(stockLevels);
	}

	private Collection<StockLevelModel> getStockLevels(final ProductModel productModel, final BaseStoreModel baseStore) {
		// TODO Auto-generated method stub
		return stockService.getStockLevels(productModel, warehouseSelectionStrategy.getWarehousesForBaseStore(baseStore));
	}

	/**
	 * Populates the CMIR code in the productData. Fetches the CMIR code from the CustomerMaterialInformation table.
	 *
	 * @param source
	 *           The product for which the CMIR code is to be found.
	 * @param productData
	 *           The target data object.
	 */
	private void populateCMIRCode(final ProductModel source, final ProductData productData, final B2BCustomerModel customer)
	{
		B2BUnitModel unit = sessionService.getAttribute(GpcommerceCoreConstants.SOLD_TO_ID);
		if (null == unit) {
			unit = customer.getDefaultB2BUnit();
		}
		final String cmir = gpB2BProductService.getCMIRCodeForProductAndB2BUnit(source.getCode(), unit.getUid());
		if (!StringUtils.isEmpty(cmir))
		{
			productData.setCmirCode(cmir);
		}
	}

	/**
	 * Find base for the product
	 *
	 * @param source
	 * @return
	 */
	private ProductModel findBaseProduct(final ProductModel source)
	{
		ProductModel sourceBaseProduct = null;
		if (source instanceof VariantProductModel)
		{
			sourceBaseProduct = ((VariantProductModel) source).getBaseProduct();
			while (!(sourceBaseProduct instanceof GPCommerceProductModel)
					&& ((VariantProductModel) sourceBaseProduct).getBaseProduct() != null)
			{
				sourceBaseProduct = ((VariantProductModel) sourceBaseProduct).getBaseProduct();
			}
			return sourceBaseProduct;
		}
		return source;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public Converter<CategoryModel, CategoryData> getCategoryConverter() {
		return categoryConverter;
	}

	public void setCategoryConverter(final Converter<CategoryModel, CategoryData> categoryConverter) {
		this.categoryConverter = categoryConverter;
	}

}

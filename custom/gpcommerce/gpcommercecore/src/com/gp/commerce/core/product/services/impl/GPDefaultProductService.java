/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.product.services.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static java.lang.String.format;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.product.impl.DefaultProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.Config;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import com.gp.commerce.core.catalog.GPProductAuditAttribute;
import com.gp.commerce.core.catalog.GPProductAuditRecord;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.GPXpressProdAttributeType;
import com.gp.commerce.core.exceptions.GPException;
import com.gp.commerce.core.model.GPAttributeConfigModel;
import com.gp.commerce.core.model.GPCompetitorProductModel;
import com.gp.commerce.core.model.GPComponentProductsModel;
import com.gp.commerce.core.model.GPCustomerMaterialInfoModel;
import com.gp.commerce.core.model.GPProductKitModel;
import com.gp.commerce.core.model.GPProductType2ClassificationAttributeModel;
import com.gp.commerce.core.model.GPVariantColorCodesModel;
import com.gp.commerce.core.model.GPVariantProductKitModel;
import com.gp.commerce.core.model.GPXpressAlertProdHistoryModel;
import com.gp.commerce.core.product.dao.GPProductDao;
import com.gp.commerce.core.product.services.GPProductService;
import com.gp.commerce.core.services.GPUserService;
import com.gp.commerce.product.data.GPProductAttributeChangeData;
import com.gp.commerce.product.data.GPProductAttributeData;
import com.gp.commerce.product.data.GPProductDataList;


/**
 * This class is for processing Product services
 */
public class GPDefaultProductService extends DefaultProductService implements GPProductService {

	private static final Logger LOG = Logger.getLogger(GPDefaultProductService.class);
	private static final String DATE_FORMAT_GP_XPRESS = "MM-dd-yy:HH:mm:ss";

	private static final String ALL_TYPE = "all";
	private static final String IMG_TYPE = "PRIMARY";
	private static final String PRID = "prid";
	private static final String WIDTH = "Wx";
	private static final String HEIGHT = "H";
	private static final String LXWXH = "LxWxH";
	private static final String EACH_PER_UNIT = "Each per Ship Unit, 11005";
	private static final String ITEM_PER_EACH = "Items Per Each, 11006";


	private transient CMSSiteService cmsSiteService;

	private transient ConfigurationService configurationService;


	private transient GPProductDao gpProductDao;

	@Resource(name = "userService")
	private transient GPUserService userService;

	@Resource(name = "flexibleSearchService")
	transient FlexibleSearchService flexibleSearchService;

	@Resource(name = "classificationService")
	private transient ClassificationService classificationService;

	@Resource(name = "productPrimaryImagePopulator")
	private transient Populator<ProductModel, ProductData> productPrimaryImagePopulator;

	private static final String PRODUCT_FORMAT = "product";

	private transient Feature eachPerUnit;
	private transient Feature itemPerEach;


	/**
	 * Fetches all the available color names and corresponding codes.
	 *
	 * @return A map of color name as a key and color code as value.
	 */
	@Override
	public Map<String, String> fetchAllColorCodes()
	{
		final List<GPVariantColorCodesModel> colorCodeModels = gpProductDao.getAllColorCodes();
		final Map<String, String> colorMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(colorCodeModels))
		{
			colorCodeModels.stream().forEach(item -> {
				colorMap.put(item.getColorName(), item.getColorCode());
			});
		}

		return colorMap;
	}

	@Override
	public List<String> getAttributesOfProduct(final String assetCode,final String pageType)
	{
		validateParameterNotNull(assetCode, "Parameter assetCode must not be null");
		List<GPAttributeConfigModel> attributeList;
		final List<String> masterList = new ArrayList<>();

		final CMSSiteModel currentSite = getCmsSiteService().getCurrentSite();
		Assert.notNull(currentSite, "CMSSite should not be null");

		attributeList = gpProductDao.getAttributeList(currentSite.getUid(),assetCode);

		if(attributeList != null)
		{
			for(final GPAttributeConfigModel attributeConfig : attributeList)
			{
				final String attrClassification = (new StringBuilder()).append(attributeConfig.getName().trim()).append(", ").append(attributeConfig.getClassificationCode()).toString();
				if(attributeConfig.getCompareEnabled() && pageType.equalsIgnoreCase(configurationService.getConfiguration().getString("product.compare.pagetype"))
						|| attributeConfig.getSearchEnabled() && pageType.equalsIgnoreCase(configurationService.getConfiguration().getString("product.search.pagetype"))
						|| attributeConfig.getDetailEnabled() && pageType.equalsIgnoreCase(configurationService.getConfiguration().getString("product.detail.pagetype")))
				{
					masterList.add(attrClassification.toLowerCase(userService.getCurrentLocale()));
				}
			}
		}
		return masterList;
	}

	@Override
	public ProductModel getProductForCodeOrUpc(final String code) {
		validateParameterNotNull(code, "Parameter code must not be null");
		final List<ProductModel> products = getGpProductDao().getProductForCodeOrUpc(code);

		validateIfSingleResult(products, format("Product with code '%s' not found!", code),
				format("Product code '%s' is not unique, %d products found!", code, Integer.valueOf(products.size())));

		return products.get(0);
	}

	protected ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	protected GPProductDao getGpProductDao()
	{
		return gpProductDao;
	}

	public void setGpProductDao(final GPProductDao gpProductDao)
	{
		this.gpProductDao = gpProductDao;
	}

	protected CMSSiteService getCmsSiteService()
	{
		return cmsSiteService;
	}

	public void setCmsSiteService(final CMSSiteService cmsSiteService) {
		this.cmsSiteService = cmsSiteService;
	}

	@Override
	public List<GPCustomerMaterialInfoModel> getCMIRCodeForProduct(final String productCode) {
		return gpProductDao.getCMIRCodeForProduct(productCode);
	}

	@Override
	public GPProductDataList getProductListForGPXpressAlert(final String timeStamp, final int pageNumber, final boolean resend)
	{
		validateParameterNotNull(timeStamp, "Parameter attribute must not be null");

		final GPProductDataList alertData = new GPProductDataList();
		final SearchPageData<GPXpressAlertProdHistoryModel> gpXpressproductList;
		final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_GP_XPRESS);
		Date date = null;
		try {
			date = dateFormat.parse(timeStamp);
		}catch(final ParseException e) {
			LOG.error(" GPXpressAlert failed to parse date from timeStamp. Provided timestamp: "+ timeStamp, e);
			return alertData;
		}
		final int pageSize =  Config.getInt(GpcommerceCoreConstants.GPXPRESS_PAGESIZE,1000);
		gpXpressproductList = gpProductDao.getGPXpressProductList(date, pageNumber, pageSize, resend);

		updateProcessedStatus(gpXpressproductList.getResults());

		final List<GPProductAttributeData> gpProductAttributeDataList = transformData(gpXpressproductList, dateFormat);

		alertData.setTotal(gpProductAttributeDataList.size());
		alertData.setProdAttributes(gpProductAttributeDataList);
		alertData.setProcessed(gpXpressproductList.getResults().size());
		return alertData;
	}

	/**
	 * Transforms the results returned by database to data object.
	 * @param gpXpressproductList results returned by database.
	 * @param dateFormat The date format.
	 * @return
	 */
	private List<GPProductAttributeData> transformData(
			final SearchPageData<GPXpressAlertProdHistoryModel> gpXpressproductList, final SimpleDateFormat dateFormat) {
		final List<GPProductAttributeData> gpProductAttributeDataList = new ArrayList<>();

		if (!CollectionUtils.isEmpty(gpXpressproductList.getResults()))
		{
			for (final GPXpressAlertProdHistoryModel gpProdAlertModel : gpXpressproductList.getResults())
			{
				final Optional<GPProductAttributeData> productAttributeData = gpProductAttributeDataList.stream().filter(attribData-> attribData.getProductCode().equals(gpProdAlertModel.getProductCode())).findFirst();

				if(productAttributeData.isPresent()) {
					final GPProductAttributeChangeData changeData = new GPProductAttributeChangeData();
					poulateChangeData(dateFormat, gpProdAlertModel, changeData);
					productAttributeData.get().getUpdatedAttributes().add(changeData);
				}else {
					final GPProductAttributeData gpProductAttributeData = new GPProductAttributeData();
					if (null != gpProdAlertModel.getProduct())
					{
						final ProductModel product = gpProdAlertModel.getProduct();
						if (CollectionUtils.isEmpty(product.getDistributorIds()))
						{ //No need to send updates for products for which there are no distributors configured.
							continue;
						}
						gpProductAttributeData.setSoldTos(getSoldToCodes(product));
						gpProductAttributeData.setTitle(product.getName());
						gpProductAttributeData.setDescription(product.getDescription());
						if(null!=product.getMaterialStatus()) {
							gpProductAttributeData.setStatus(product.getMaterialStatus().getCode());
						}
						final ProductData productData = new ProductData();
						productPrimaryImagePopulator.populate(product, productData);
						if (null != productData.getImages())
						{
							final Collection<ImageData> images = productData.getImages();
							for (final ImageData imageData : images)
							{
								if (imageData.getFormat().equalsIgnoreCase(PRODUCT_FORMAT))
								{
									gpProductAttributeData.setProductHeroImage(imageData.getUrl());
								}
							}
						}
					}else {
						//Product should always be present in history models with attribute source as Product. 
						//If it is not there then probably that product is deleted and there is no point 
						//sending alerts for that product.
						continue; 
					}
					final List<GPProductAttributeChangeData> updatedAttributes = new ArrayList<>();
					final GPProductAttributeChangeData changeData = new GPProductAttributeChangeData();

					gpProductAttributeData.setProductCode(gpProdAlertModel.getProductCode());
					gpProductAttributeData.setProcessed(gpProdAlertModel.isProcessed());
					if(null!=gpProdAlertModel.getProcessedDate()) {
						gpProductAttributeData.setProcessedDate(dateFormat.format(gpProdAlertModel.getProcessedDate()));
					}

					poulateChangeData(dateFormat, gpProdAlertModel, changeData);
					
					updatedAttributes.add(changeData);
					gpProductAttributeData.setUpdatedAttributes(updatedAttributes);
					gpProductAttributeDataList.add(gpProductAttributeData);
				}

			}

		}
		return gpProductAttributeDataList;
	}

	private void poulateChangeData(final SimpleDateFormat dateFormat,
			final GPXpressAlertProdHistoryModel gpProdAlertModel, final GPProductAttributeChangeData changeData) {
		changeData.setAttributeName(gpProdAlertModel.getAttributeName());
		changeData.setValue(gpProdAlertModel.getNewValue());
		if(null!=gpProdAlertModel.getAttributeModifiedTime()) {
			changeData.setChangeDate(dateFormat.format(gpProdAlertModel.getAttributeModifiedTime()));
		}
	}

	/**
	 * Updates the status by processing the given GPXpress alerts
	 * 
	 * @param alertModels list of {@link GPXpressAlertProdHistoryModel}s
	 */
	public void updateProcessedStatus(final List<GPXpressAlertProdHistoryModel> alertModels) {
		for(final GPXpressAlertProdHistoryModel alertModel : alertModels) {
			final long currentTime = System.currentTimeMillis();
			alertModel.setProcessed(true);
			alertModel.setProcessedDate(new Date(currentTime));
		}
		modelService.saveAll(alertModels);
	}

	@Override
	public void updateProductAlertHistory(final Map<String, GPProductAuditRecord> gpProductAuditRecordMap)
	{
		final Set<String> productCodes = gpProductAuditRecordMap.keySet();
		final List<GPXpressAlertProdHistoryModel> alertHistomerModels = new ArrayList<>();
		GPProductAuditRecord gpAuditRecord;
		for(final String productCode : productCodes) {
			gpAuditRecord = gpProductAuditRecordMap.get(productCode);
			Set<String> attributeNames = null;
			if(null!=gpAuditRecord.getProductAttributeMap() && !gpAuditRecord.getProductAttributeMap().isEmpty()) {
				attributeNames = gpAuditRecord.getProductAttributeMap().keySet();
				GPXpressProdAttributeType attributeSource = null;
				if(gpAuditRecord.getProduct() instanceof GPCompetitorProductModel) {
					attributeSource = GPXpressProdAttributeType.COMPETITOR;
				}
				else
				{
					attributeSource = GPXpressProdAttributeType.PRODUCT;
				}
				extractAlertHistoryModelsFromMap(alertHistomerModels, gpAuditRecord.getProductAttributeMap(), productCode, attributeNames, attributeSource, gpAuditRecord.getProduct());
			}
			if(null!=gpAuditRecord.getProductFeatureAttributeMap() && !gpAuditRecord.getProductFeatureAttributeMap().isEmpty()) {
				attributeNames = gpAuditRecord.getProductFeatureAttributeMap().keySet();
				extractAlertHistoryModelsFromFeaturesMap(alertHistomerModels, gpAuditRecord, productCode, attributeNames);
			}
			if(null != gpAuditRecord.getShippingRestrictionAttributeMap() && !gpAuditRecord.getShippingRestrictionAttributeMap().isEmpty())
			{
				attributeNames = gpAuditRecord.getShippingRestrictionAttributeMap().keySet();
				extractAlertHistoryModelsFromMap(alertHistomerModels, gpAuditRecord.getShippingRestrictionAttributeMap(), productCode, attributeNames, GPXpressProdAttributeType.SHIPPINGRESTRICION, null);
			}

			if(null!=gpAuditRecord.getProduct()) {
				final ProductModel gpProduct = gpAuditRecord.getProduct();
				if(CollectionUtils.isNotEmpty(gpAuditRecord.getAssociatedDistributors())) {
					final List<String> currentDistributorIds = new ArrayList<>();
					gpAuditRecord.getAssociatedDistributors().stream().forEach(b2bUnit-> currentDistributorIds.add(b2bUnit.getUid()));
					extractRelationData(alertHistomerModels, gpProduct, currentDistributorIds, "distributorIds");
				}

				if(CollectionUtils.isNotEmpty(gpAuditRecord.getAssociatedProducts())) {
					final List<String> currentProductsList = new ArrayList<>();
					gpAuditRecord.getAssociatedProducts().stream().forEach(product-> currentProductsList.add(product.getCode()));
					extractRelationData(alertHistomerModels, gpProduct, currentProductsList, "products");
				}

				if(CollectionUtils.isNotEmpty(gpAuditRecord.getAssociatedAddresses())) {
					final AddressModel address = gpAuditRecord.getAssociatedAddresses().iterator().next();
					extractAddressRelationData(alertHistomerModels, gpProduct, address);
				}
			}

		}

		if(CollectionUtils.isNotEmpty(alertHistomerModels)) {
			modelService.saveAll(alertHistomerModels);
		}


	}

	private void extractRelationData(final List<GPXpressAlertProdHistoryModel> alertHistomerModels, final ProductModel gpProduct,
			final List<String> currentList, final String attributeName) {

		final List<GPXpressAlertProdHistoryModel> gpXpressProductList = gpProductDao.getExistingAlertHistoryRecords(gpProduct,
				attributeName);
		if (CollectionUtils.isNotEmpty(gpXpressProductList)) {
			final GPXpressAlertProdHistoryModel historyModel = gpXpressProductList.get(0);
			final List<String> savedList = null != historyModel.getNewValue()
					? Arrays.asList(historyModel.getNewValue().split(","))
					: new ArrayList<>();

			if (isDifferent(currentList, savedList)) {
				historyModel.setOldVlaue(historyModel.getNewValue());
				historyModel.setNewValue(String.join(",", currentList));
				historyModel.setAttributeModifiedTime(new Date(System.currentTimeMillis()));
				alertHistomerModels.add(historyModel);
			}
		} else {
			final GPXpressAlertProdHistoryModel historyModel = modelService.create(GPXpressAlertProdHistoryModel.class);
			historyModel.setProductCode(gpProduct.getCode());
			historyModel.setAttributeName(attributeName);
			historyModel.setAttributeModifiedTime(new Date(System.currentTimeMillis()));
			historyModel.setNewValue(String.join(",", currentList));
			historyModel.setProcessed(false);
			historyModel.setProduct(gpProduct);
			historyModel.setProductAttributeSource(GPXpressProdAttributeType.PRODUCT);
			alertHistomerModels.add(historyModel);
		}
	}

	private void extractAddressRelationData(final List<GPXpressAlertProdHistoryModel> alertHistomerModels, final ProductModel gpProduct,
			final AddressModel currentAddress) {
		final Set<String> attributeNames = new HashSet<>();
		attributeNames.add("country");
		attributeNames.add("state");
		attributeNames.add("city");
		attributeNames.add("street");
		attributeNames.add("postalcode");
		for(final String addressField : attributeNames) {
			final List<GPXpressAlertProdHistoryModel> gpXpressProductList = gpProductDao.getExistingAlertHistoryRecords(gpProduct, addressField);
			final String currentValue = getAddressField(currentAddress, addressField);;
			if(CollectionUtils.isNotEmpty(gpXpressProductList)) {
				final GPXpressAlertProdHistoryModel historyModel =  gpXpressProductList.get(0);
				final String savedValue = historyModel.getNewValue();
				changeExistingHistoryModel(alertHistomerModels, savedValue, currentValue, historyModel);
			}else {
				createNewHistoryModel(alertHistomerModels, gpProduct, addressField, currentValue, GPXpressProdAttributeType.COMPETITOR);
			}
		}
	}

	private String getAddressField(final AddressModel currentAddress, final String attributeName) {
		if("country".equals(attributeName)) {
			return (null!=currentAddress.getCountry())?currentAddress.getCountry().getName():"";
		}else if("state".equals(attributeName)) {
			return (null!=currentAddress.getRegion())?currentAddress.getRegion().getName():"";
		}else if("city".equals(attributeName)) {
			return currentAddress.getTown();
		}else if("street".equals(attributeName)) {
			return currentAddress.getStreetname();
		}else if("postalcode".equals(attributeName)) {
			return currentAddress.getPostalcode();
		}

		return null;
	}

	private void changeExistingHistoryModel(final List<GPXpressAlertProdHistoryModel> alertHistomerModels, final String oldValue,
			final String newValue, final GPXpressAlertProdHistoryModel historyModel) {
		if(!StringUtils.equals(oldValue, newValue)) {
			historyModel.setOldVlaue(oldValue);
			historyModel.setNewValue(newValue);
			historyModel.setAttributeModifiedTime(new Date(System.currentTimeMillis()));
			alertHistomerModels.add(historyModel);
		}
	}

	private void createNewHistoryModel(final List<GPXpressAlertProdHistoryModel> alertHistomerModels, final ProductModel gpProduct,
			final String attributeName, final String newValue, final GPXpressProdAttributeType attributeSource) {
		final GPXpressAlertProdHistoryModel historyModel = modelService.create(GPXpressAlertProdHistoryModel.class);
		historyModel.setProductCode(gpProduct.getCode());
		historyModel.setAttributeName(attributeName);
		historyModel.setAttributeModifiedTime(new Date(System.currentTimeMillis()));
		historyModel.setNewValue(newValue);
		historyModel.setProcessed(false);
		historyModel.setProductAttributeSource(attributeSource);
		alertHistomerModels.add(historyModel);
	}

	private boolean isDifferent(final List<String> list1, final List<String> list2) {
		if(list1.size()!=list2.size()) {
			return true;
		}else if(CollectionUtils.isNotEmpty(list1)){
			final Object[] list1Array = list1.toArray();
			final Object[] list2Array = list2.toArray();
			Arrays.sort(list1Array);
			Arrays.sort(list2Array);
			return Arrays.equals(list1Array, list2Array);
		}

		return false;
	}

	private void extractAlertHistoryModelsFromMap(final List<GPXpressAlertProdHistoryModel> alertHistomerModels,
			final Map<String, GPProductAuditAttribute> productCode2AuditAttributeMap, final String productCode, final Set<String> attributeNames, final GPXpressProdAttributeType attributeSource, final ProductModel product) {
		final List<GPXpressAlertProdHistoryModel> gpXpressProductList = gpProductDao.getExistingAlertHistoryRecords(productCode, attributeNames, attributeSource);
		for(final String attributeName : attributeNames) {
			final Optional<GPXpressAlertProdHistoryModel> alertModel = gpXpressProductList.stream().filter(obj-> obj.getProductCode().equals(productCode) && obj.getAttributeName().equals(attributeName)).findFirst();
			final GPProductAuditAttribute attributeDetails = productCode2AuditAttributeMap.get(attributeName);
			final GPXpressAlertProdHistoryModel gpXpressAlertProdHistory;
			if(alertModel.isPresent()) {
				gpXpressAlertProdHistory = alertModel.get();
			}else {
				gpXpressAlertProdHistory = modelService.create(GPXpressAlertProdHistoryModel.class);
				gpXpressAlertProdHistory.setAttributeName(attributeName);
				gpXpressAlertProdHistory.setProductCode(productCode);
				gpXpressAlertProdHistory.setProduct(product);
			}
			if(null != attributeDetails.getOldAttributeValue()) {
				gpXpressAlertProdHistory.setOldVlaue(attributeDetails.getOldAttributeValue().toString());
			}
			if (null != attributeDetails.getNewAttributeValue()) {
				gpXpressAlertProdHistory.setNewValue(attributeDetails.getNewAttributeValue().toString());
			}
			if(null != attributeDetails.getDateOfChange()) {
				gpXpressAlertProdHistory.setAttributeModifiedTime(attributeDetails.getDateOfChange());
			}else {
				gpXpressAlertProdHistory.setAttributeModifiedTime(new Date(System.currentTimeMillis()));
			}
			gpXpressAlertProdHistory.setProcessed(false);
			gpXpressAlertProdHistory.setProductAttributeSource(attributeSource);
			alertHistomerModels.add(gpXpressAlertProdHistory);
		}
	}

	private List<String> getSoldToCodes(final ProductModel product) {
		List<String> soldToCodes = null;
		if(null!=product && CollectionUtils.isNotEmpty(product.getDistributorIds())) {
			soldToCodes = product.getDistributorIds().stream().map(unit->unit.getUid()).collect(Collectors.toList());
		}
		return soldToCodes;
	}

	private void extractAlertHistoryModelsFromFeaturesMap(final List<GPXpressAlertProdHistoryModel> alertHistomerModels,
			final GPProductAuditRecord gpAuditRecord, final String productCode, final Set<String> classificationFeatures) {
		final List<GPXpressAlertProdHistoryModel> gpXpressProductList = gpProductDao
				.getExistingAlertHistoryRecords(productCode, classificationFeatures, GPXpressProdAttributeType.PRODUCT);
		for (final String feature : classificationFeatures) {
			final Map<String, GPProductAuditAttribute> featureAttributes = gpAuditRecord.getProductFeatureAttributeMap()
					.get(feature);
			final Optional<GPXpressAlertProdHistoryModel> alertModel = gpXpressProductList.stream()
					.filter(obj -> obj.getProductCode().equals(productCode) && obj.getAttributeName().equals(feature))
					.findFirst();
			GPProductAuditAttribute attributeDetails = null;
			if (null != featureAttributes.get("stringvalue")) {
				attributeDetails = featureAttributes.get("stringvalue");
			} else {
				attributeDetails = featureAttributes.get("rawvalue");
			}

			final GPXpressAlertProdHistoryModel gpXpressAlertProdHistory;
			if (alertModel.isPresent()) {
				gpXpressAlertProdHistory = alertModel.get();
			} else {
				gpXpressAlertProdHistory = modelService.create(GPXpressAlertProdHistoryModel.class);
				gpXpressAlertProdHistory.setAttributeName(feature);
				gpXpressAlertProdHistory.setProductCode(productCode);
				gpXpressAlertProdHistory.setProduct(gpAuditRecord.getProduct());
			}
			if (null != attributeDetails.getOldAttributeValue()) {
				gpXpressAlertProdHistory.setOldVlaue(attributeDetails.getOldAttributeValue().toString());
			}
			if (null != attributeDetails.getNewAttributeValue()) {
				gpXpressAlertProdHistory.setNewValue(attributeDetails.getNewAttributeValue().toString());
			}
			if(null != attributeDetails.getDateOfChange()) {
				gpXpressAlertProdHistory.setAttributeModifiedTime(attributeDetails.getDateOfChange());
			}else {
				gpXpressAlertProdHistory.setAttributeModifiedTime(new Date(System.currentTimeMillis()));
			}
			gpXpressAlertProdHistory.setProcessed(false);
			gpXpressAlertProdHistory.setProductAttributeSource(GPXpressProdAttributeType.PRODUCT);

			alertHistomerModels.add(gpXpressAlertProdHistory);

		}
	}

	@Override
	public File exportImages(final ProductData productData, final String imageFormat,final String resolution, final String source, final String zipFile, final boolean allimages) throws MalformedURLException, FileNotFoundException, IOException {

		final byte[] b = new byte[2048];
		int count = 1;
		final File sourceFile = new File(source);
		final File zip = new File(zipFile);
		//Create temp directory and download images based on filters
		try {
			if (sourceFile.mkdir()) {
				if (allimages) {
					for (final ImageData image : productData.getDamGalleryImages()) {
									final String imageUrl = image.getUrl();
								final UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(imageUrl);
								if(imageUrl.contains(PRID)){
								urlBuilder.replaceQueryParam(PRID, resolution+WIDTH+resolution+HEIGHT);
								final URL url = new URL(urlBuilder.build().toUriString());
								final String destName = sourceFile + "///" + productData.getCode() + "_" + count +"."+imageFormat;
								try (InputStream is = url.openStream(); OutputStream os = new FileOutputStream(destName);) {
									int length;
									while ((length = is.read(b)) != -1) {
										os.write(b, 0, length);
									}
								}
							count++;
					}
					}
				} else {
					for (final ImageData image : productData.getImages()) {
						if (!image.getUrl().isEmpty() && IMG_TYPE.equals(image.getImageType().toString())) {
							String primaryUrl;
							primaryUrl = null != image.getThumbnailUrl() ? image.getThumbnailUrl(): image.getZoomUrl();
							if(!primaryUrl.contains(resolution)) {
							final UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(primaryUrl);
							urlBuilder.replaceQueryParam(PRID, resolution+WIDTH+resolution+HEIGHT);
							primaryUrl = urlBuilder.build().toUriString();
							}
							final URL url = new URL(primaryUrl);
							final String destName = sourceFile + "///" + image.getName()+"."+imageFormat;
							try(InputStream is = url.openStream();
									OutputStream os = new FileOutputStream(destName);) {
								int length;
								while ((length = is.read(b)) != -1) {
									os.write(b, 0, length);
								}
							}
							break;
						}
					}
				}
				//create zip based on the images folder
				final FileOutputStream fout = new FileOutputStream(zip);
				addImagesToZip(fout, sourceFile, resolution);
				fout.close();
				fout.flush();
				final File zipFileDownload = new File(zipFile);
				if (zipFileDownload.length() > 1) {
					return zipFileDownload;
				}
			}
		}
		finally {
			delete(sourceFile);
		}
		return null;
	}

	@Override
	public String getDisplayAttributesForProduct(final ProductModel product) {

		final StringBuilder displayAttributeBuilder = new StringBuilder();

		if (!StringUtils.isNotBlank(product.getProductType())) {
			return StringUtils.EMPTY;
		} else {
			final Set<Feature> features = filterFeaturesForProduct(product);

			return populatedisplayAttributes(displayAttributeBuilder, features);
		}

	}

	private Set<Feature> filterFeaturesForProduct(final ProductModel product) {
		final GPProductType2ClassificationAttributeModel prdTypeClassAttr = getGpProductDao()
				.getProductType2ClassAttributes(product.getProductType());
		final Set<ClassificationAttributeModel> classAttrList = new HashSet<>();

		if (null != prdTypeClassAttr && !CollectionUtils.isEmpty(prdTypeClassAttr.getClassAttributes())) {
			classAttrList.addAll(prdTypeClassAttr.getClassAttributes());
		}
		final Set<ClassAttributeAssignmentModel> classAttrAssSet = new HashSet<>();
		if (CollectionUtils.isNotEmpty(product.getClassificationClasses())) {
			product.getClassificationClasses().forEach(classClass -> {
				classAttrAssSet.addAll(classClass.getAllClassificationAttributeAssignments());
			});

		}
		final Set<Feature> features = new HashSet<Feature>();
		eachPerUnit = null;
		itemPerEach = null;

		classAttrList.forEach(classAttr -> {
			final ClassAttributeAssignmentModel classAttrAss = findAssignmentWithCode(classAttrAssSet,
					classAttr.getCode());

			final Feature feature = classificationService.getFeature(product, classAttrAss);
			if (null != feature.getValues() && !feature.getValues().isEmpty()) {

				features.add(feature);
				if (feature.getClassAttributeAssignment().getClassificationAttribute().getCode()
						.contains(EACH_PER_UNIT)) {
					eachPerUnit = feature;
				}
				if (feature.getClassAttributeAssignment().getClassificationAttribute().getCode()
						.contains(ITEM_PER_EACH)) {
					itemPerEach = feature;
				}

			}
		});
		return features;
	}

	private String populatedisplayAttributes(final StringBuilder displayAttributeBuilder, final Set<Feature> features) {
		if(ObjectUtils.allNotNull(eachPerUnit,itemPerEach)) {
			displayAttributeBuilder.append(eachPerUnit.getValues().get(0).getValue().toString());
			displayAttributeBuilder.append(" @ ");
			displayAttributeBuilder.append(itemPerEach.getValues().get(0).getValue().toString());
			displayAttributeBuilder.append(" ,");
		}

		features.stream().filter(feature -> {

			return !(feature.equals(eachPerUnit) || feature.equals(itemPerEach));

		}).forEach(feature -> {
			if (feature.getName().contains(LXWXH)) {
				displayAttributeBuilder.append(feature.getName());
				displayAttributeBuilder.append(" ");
				displayAttributeBuilder.append(feature.getValues().get(0).getValue().toString());
				displayAttributeBuilder.append(" ,");

			} else {
				displayAttributeBuilder.append(feature.getValues().get(0).getValue().toString());
				displayAttributeBuilder.append(" ,");
			}

		});

		return StringUtils.chomp(displayAttributeBuilder.toString(), ",");
	}

	private ClassAttributeAssignmentModel findAssignmentWithCode(
			final Collection<ClassAttributeAssignmentModel> assignments, final String code) {
		for (final ClassAttributeAssignmentModel assignment : assignments) {
			if (assignment.getClassificationAttribute().getCode().equals(code)) {
				return assignment;
			}
		}
		return null;
	}

	private void addImagesToZip(final FileOutputStream fout, final File sourceFile, final String resolution) throws IOException {

		// Check resolution and write images to zip.
		try (ZipOutputStream zout = new ZipOutputStream(fout);) {
			final File[] files = sourceFile.listFiles();
			if (files.length >= 1) {
				for (final File file : files) {
					if (!ALL_TYPE.equals(resolution)) {
						final BufferedImage bufferedImage = ImageIO.read(file);
						if (bufferedImage.getWidth() != Integer.parseInt(resolution)
								&& bufferedImage.getHeight() != Integer.parseInt(resolution)) {
							file.delete();
						}
					}
					if (file.exists()) {
						try (FileInputStream fin = new FileInputStream(file);) {
							zout.putNextEntry(new ZipEntry(file.getName()));
							int ln;
							final int fileLength = (int) file.length();
							final byte[] b = new byte[fileLength];
							while ((ln = fin.read(b)) > 0) {
								zout.write(b, 0, ln);
							}
							zout.closeEntry();
						}
					}
				}
			} else {
				throw new GPException("3333", "No images are currently available.");
			}
		}

	}

	//Delete the temp files after creation of zip.
		private void delete(final File sourceFile) {
			final String[] sou = sourceFile.list();
			for (final String s : sou) {
				final File currentFile = new File(sourceFile.getPath(), s);
				currentFile.delete();
			}
			sourceFile.delete();
		}

		public Long getKitComponentQuantity(final ProductModel product, final Collection<StockLevelModel> stockLevels)
	{
		if (product instanceof GPProductKitModel) {
			final Collection<GPComponentProductsModel> components = ((GPProductKitModel) product).getKitComponent();

			if (CollectionUtils.isNotEmpty(components) && CollectionUtils.isNotEmpty(stockLevels)) {
				for (final GPComponentProductsModel component : components) {
					for (final StockLevelModel stocklevel : stockLevels) {
						if (null!=stocklevel && null != component && null != component.getKitcomponent() && stocklevel.getProductCode()
								.equalsIgnoreCase(component.getKitcomponent().getCode())) {
							return Long.valueOf(component.getQuantity());
						}

					}
				}
			}
		} else if (product instanceof GPVariantProductKitModel) {
			final Collection<GPComponentProductsModel> components = ((GPVariantProductKitModel) product).getKitComponent();

			if (CollectionUtils.isNotEmpty(components)) {
				for (final GPComponentProductsModel component : components) {
					for (final StockLevelModel stocklevel : stockLevels) {
						if (null != component && null != component.getKitcomponent() && stocklevel.getProductCode()
								.equalsIgnoreCase(component.getKitcomponent().getCode())) {
							return Long.valueOf(component.getQuantity());
						}

					}
				}
			}
		}

		return Long.valueOf(0);
	}
	@Override
	public List<ProductModel> getAllProductsForSpecsPopulatorJob(final Date endTime, final Date featureUpdateDate, final String productCode, final String catalogVersion) {
		final List<ProductModel> products = getGpProductDao().getAllProductsForSpecsPopulatorJob(endTime, featureUpdateDate, productCode, catalogVersion);
		return products;
	}
	
	public List<ProductModel> getAllProductsForGpBrandLabelPopulatorJob(final Date endTime, final Date featureUpdateDate, final String productCode, final String catalogVersion) {
		final List<ProductModel> products = getGpProductDao().getAllProductsForGpBrandLabelPopulatorJob(endTime, featureUpdateDate, productCode, catalogVersion);
		return products;
	}

	@Override
	public List<ProductModel> getProductsForSiteAndCatalogVersion(final CMSSiteModel site,
			final CatalogVersionModel catalogVersionModel)
	{
		final List<ProductModel> products = getGpProductDao().getProductsForSiteAndCatalogVersion(site, catalogVersionModel);
		return products;
	}
	
	@Override
	public FeatureList getFeaturesForProduct(final String productCode)
	{
		if (null != productCode)
		{
		final ProductModel product = getProductForCodeOrUpc(productCode);
		return product != null ? classificationService.getFeatures(product) : null;
		}
		return null;

	}
}

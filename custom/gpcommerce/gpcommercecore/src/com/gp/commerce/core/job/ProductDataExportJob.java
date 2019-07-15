/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.job;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.core.enums.BrandLabelEnum;
import com.gp.commerce.core.enums.BrandsEnum;
import com.gp.commerce.core.model.GPCommerceProductModel;
import com.gp.commerce.core.model.ProductDataExportCronjobModel;
import com.gp.commerce.core.product.dao.GPProductDao;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.contextualattributevalues.model.ContextualAttributeValueModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.ModelInitializationException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.type.TypeService;

public class ProductDataExportJob extends AbstractJobPerformable<ProductDataExportCronjobModel> {

	private static final Logger LOG = Logger.getLogger(ProductDataExportJob.class);

	@Resource(name = "typeService")
	TypeService typeService;
	@Resource(name = "cmsSiteService")
	CMSSiteService cmsSiteService;
	@Resource(name = "catalogVersionService")
	CatalogVersionService catalogVersionService;
	@Resource(name = "mediaService")
	private MediaService mediaService;
	@Resource
	ClassificationService classificationService;
	@Resource
	ConfigurationService configurationService;

	private GPProductDao gpProductDao;

	private HashMap<String, String> attributesMap = new HashMap<String, String>();

	LinkedHashSet<String> attributesCodeSet = new LinkedHashSet<String>();

	LinkedList<String> csvHeaderList = new LinkedList<String>();

	StringBuilder csvData = new StringBuilder();

	private final String GS1FORMAT = "gs1";
	private final String MASSCOFORMAT = "massco";
	private final String SALSIFYFORMAT = "salsify";
	private final String RESTOCKFORMAT = "restock";
	private static final String IMAGE_TYPE = "_3D";

	@Override
	public PerformResult perform(ProductDataExportCronjobModel cronJob) {

		try {
			generatCsv(cronJob);
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		} catch (final Exception e) {
			LOG.error("Exception occurred during product data export", e);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
	}

	private void generatCsv(ProductDataExportCronjobModel cronJob) {

		Map<String, String> exportAttributes = cronJob.getExportAttributes();

		String prodIds = cronJob.getProductIds();
		String[] productIds = null;
		if (StringUtils.isNotBlank(prodIds)) {
			productIds = prodIds.split(",");
		}
		int key=getKey(cronJob,null != productIds ? Arrays.asList(productIds) : Collections.emptyList());
		List<GPCommerceProductModel> productList = getProductToExport(cronJob,productIds,key);

		if(CollectionUtils.isNotEmpty(csvHeaderList)){
			csvHeaderList.clear();
		}
		if(StringUtils.isNotEmpty(csvData.toString())){
			csvData.setLength(0);
		}
		createCsvHeader(cronJob);
		csvData.append(String.join(",", csvHeaderList));
		csvData.append("\r\n");
		for (GPCommerceProductModel product : productList) {
			Map<String, String> dataMap = getDataMap(product, exportAttributes, cronJob);
			if(key==2) {
				dataMap.put("Sold-To/B2B Unit ID", checkSpecialCharacters(cronJob.getSoldToUnit().getUid()));
				
			}
			csvData.append(createCsvLineItem(dataMap));
			csvData.append("\r\n");
		}

		cronJob.setExportedData(createMediaModel(IOUtils.toInputStream(csvData.toString(),StandardCharsets.UTF_8), cronJob));
		modelService.save(cronJob);
	}

	private int getKey(ProductDataExportCronjobModel cronJob, List<String> productIds) {
		int key = 0;
		if (!CollectionUtils.isEmpty(productIds)) {
			key = 1;
		} else if (ObjectUtils.allNotNull(cronJob.getSoldToUnit())) {
			key = 2;
		} else if (ObjectUtils.allNotNull(cronJob.getBrandLabel(),cronJob.getExportCategory())) {
			key = 3;
		}else if (ObjectUtils.allNotNull(cronJob.getBrandLabel())) {
			key = 4;
		}else if (ObjectUtils.allNotNull(cronJob.getExportCategory())) {
			key = 5;
		}
		return key;
	}

	private List<GPCommerceProductModel> getProductToExport(ProductDataExportCronjobModel cronJob,String[] productIds,int key) 
	{
		B2BUnitModel soldtoUnit=cronJob.getSoldToUnit();
		CategoryModel rootCategory=cronJob.getExportCategory();
		BrandLabelEnum brandLabel=cronJob.getBrandLabel();
		return getGpProductDao().getGPCommerceProductsForCodes(
				null != productIds ? Arrays.asList(productIds) : Collections.emptyList(),soldtoUnit,brandLabel,rootCategory,key);
	}

	private void createCsvHeader(ProductDataExportCronjobModel cronJob) {
		String excelFormat = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(cronJob.getInputFormat())) {
			excelFormat = configurationService.getConfiguration()
					.getString("export.attributes." + cronJob.getInputFormat());
		}

		final String[] attributeArray = excelFormat.split(";");
		String[] exportAttributeArray = null;

		for (final String attribute : attributeArray) {
			exportAttributeArray = attribute.split(":");
			attributesMap.put(exportAttributeArray[0], exportAttributeArray.length==1?"":exportAttributeArray[1]);
			attributesCodeSet.add(exportAttributeArray[1]);
			csvHeaderList.add(exportAttributeArray[0]);
		}
		

	}

	private Map<String, String> getDataMap(ProductModel productModel, Map<String, String> exportAttributesMap,
			ProductDataExportCronjobModel cronJob) {

		final LinkedHashMap<String, String> gs1DataMap = new LinkedHashMap<String, String>();
		
		int featureIndex=0;
		int imageIndex=0;
		ContextualAttributeValueModel contextualAttributeValueModel=null;
		if (CollectionUtils.isNotEmpty(productModel.getContextualAttributeValues())) {
			for (final ContextualAttributeValueModel contextualAttributeValue : productModel
					.getContextualAttributeValues()) {
				if (null!=contextualAttributeValue && contextualAttributeValue.getContext().getCode().toLowerCase()
						.contains(cronJob.getSite().getUid().toLowerCase())) {
					contextualAttributeValueModel=contextualAttributeValue;
				}
			}
			
		}
		for (String key : csvHeaderList) {
			String attr = attributesMap.get(key);
			
			boolean hasAttrUOM = false;
			String itemsPerEach = null;
			String eachPerShipUnit = null;
			String tempAttr = null;
			for (final ProductFeatureModel prodFeatureModel : productModel.getFeatures()) {
				
				hasAttrUOM = false;
				String[] unitMeasureArr = null;
				String featureValue = null;
				if (attr.contains("_")) {
					hasAttrUOM = attr.contains("UOM") ? true : false;
					unitMeasureArr = attr.split("_");
					tempAttr = unitMeasureArr[0];
				}else{
					tempAttr = attr;
				}

				if (prodFeatureModel.getQualifier().contains(tempAttr)) {
					featureValue = String.valueOf(prodFeatureModel.getValue());
					if (MASSCOFORMAT.equalsIgnoreCase(cronJob.getInputFormat()) && tempAttr.contains("size lxwxh")) {
						if (tempAttr.contains("size lxwxh")) {
							gs1DataMap.put("Each LxWxH", checkSpecialCharacters(featureValue));
							continue;
						} 
					}
					
					if (tempAttr.toLowerCase().contains("code")) {
						continue;
					}

					if (tempAttr.toLowerCase().contains("case dimensions")) {

						if(!hasAttrUOM){
							if (null!=unitMeasureArr) {
								String splitChar = (null!=featureValue && featureValue.contains("*"))?"*":((null!=featureValue && featureValue.contains("x"))?"x":"");
								if(null!=splitChar) {
									if ("l".equalsIgnoreCase(unitMeasureArr[1])) {
										featureValue = featureValue.split(splitChar)[0];
									} else if ("w".equalsIgnoreCase(unitMeasureArr[1])) {
										featureValue = featureValue.split(splitChar)[1];
									} else if ("h".equalsIgnoreCase(unitMeasureArr[1])) {
										featureValue = featureValue.split(splitChar)[2];
									}
								}
							}
						}else {
							featureValue = "INCHES";
						}
						if(SALSIFYFORMAT.equalsIgnoreCase(cronJob.getInputFormat())){
							gs1DataMap.put(key, checkSpecialCharacters(featureValue));
						}
					}else {
						if (hasAttrUOM && featureValue.contains(" ")) {
							
							String[] splitValues=featureValue.split(" ");
							if(splitValues.length>1){
								featureValue = featureValue.substring(splitValues[0].length());
							}
							
						}
					}
					if("brand".equalsIgnoreCase(tempAttr)) {
						gs1DataMap.put(key, featureValue);
					}else {
						gs1DataMap.put(key, checkSpecialCharacters(featureValue));
					}
				}
				if (MASSCOFORMAT.equalsIgnoreCase(cronJob.getInputFormat())){
					if(prodFeatureModel.getQualifier().contains("each per ship unit")){
						eachPerShipUnit = String.valueOf(prodFeatureModel.getValue());
					}
					if(prodFeatureModel.getQualifier().contains("items per each")){
						itemsPerEach = String.valueOf(prodFeatureModel.getValue());
					}
					if(StringUtils.isNotEmpty(eachPerShipUnit) && StringUtils.isNotEmpty(itemsPerEach)){
						gs1DataMap.put("ITEM PKG QTY", checkSpecialCharacters(eachPerShipUnit.concat("@").concat(itemsPerEach)));
					}
				}
			}
			
			
			
			boolean hasfeatureList = false;
		
					if(null!=contextualAttributeValueModel) 
					{
						if(CollectionUtils.isNotEmpty(contextualAttributeValueModel.getFeatureList()))
						{
							hasfeatureList=true;
						}		
							if ("feature".equalsIgnoreCase(attr)
									&& CollectionUtils.isNotEmpty(contextualAttributeValueModel.getFeatureList())) {
								if (GS1FORMAT.equalsIgnoreCase(cronJob.getInputFormat())) {
									String contextFeatureList = String.join("; ", contextualAttributeValueModel.getFeatureList());
									gs1DataMap.put(key,checkSpecialCharacters(contextFeatureList));
								} else {
									if(featureIndex<contextualAttributeValueModel.getFeatureList().size())
									{
										gs1DataMap.put(key,
												checkSpecialCharacters(contextualAttributeValueModel.getFeatureList().get(featureIndex)));									}
									else
									{
										gs1DataMap.put(key,"");	
									}
									featureIndex++;
								}
							}else if("description".equalsIgnoreCase(attr)){
								gs1DataMap.put(key,checkSpecialCharacters(contextualAttributeValueModel.getDescription()));	
							}else if ("name".equalsIgnoreCase(attr)){
								gs1DataMap.put(key,checkSpecialCharacters(contextualAttributeValueModel.getName()));	
							}
							
					} 
				
			
			
			if (!hasfeatureList) {
				if ("feature".equalsIgnoreCase(attr) && CollectionUtils.isNotEmpty(productModel.getFeatureList())) {
					if (GS1FORMAT.equalsIgnoreCase(cronJob.getInputFormat())) {
						String features = String.join("; ", productModel.getFeatureList());
						gs1DataMap.put(key, checkSpecialCharacters(features));
					} else {
						if(featureIndex<productModel.getFeatureList().size())
						{
							gs1DataMap.put(key, checkSpecialCharacters(productModel.getFeatureList().get(featureIndex)));
						}
						else
						{
							gs1DataMap.put(key,"");	
						}
						featureIndex++;
					}
				}
			}
			
			LinkedList<String> galleryImagesUrl = new LinkedList<>();
			if ((SALSIFYFORMAT.equalsIgnoreCase(cronJob.getInputFormat()) || RESTOCKFORMAT.equalsIgnoreCase(cronJob.getInputFormat())) && !productModel.getGalleryImages().isEmpty()) {
				for (MediaContainerModel mediaContainer : productModel.getGalleryImages()) {
					for (MediaModel media : mediaContainer.getMedias()) {
						if (media.getRealFileName() != null && !media.getRealFileName().contains(IMAGE_TYPE)) {
							galleryImagesUrl.add(media.getURL());
						}
					}
				}
			}
			
			if (!attr.contains("-") && !attr.contains("/") && !attr.contains(" ")
					&& !"feature".equalsIgnoreCase(attr) && !"mfg".equalsIgnoreCase(attr)) {
				if ("imageUrl".equalsIgnoreCase(attr)) {
					if(CollectionUtils.isNotEmpty(galleryImagesUrl)){
						if(imageIndex<galleryImagesUrl.size())
						{
							gs1DataMap.put(key, checkSpecialCharacters(galleryImagesUrl.get(imageIndex)));	
						}
						else
						{
							gs1DataMap.put(key,"");	
						}
						imageIndex++;
					}
				} else if (gs1DataMap.containsKey(key) && null!=gs1DataMap.get(key)){
					continue;
				}else {
					AttributeDescriptorModel attributeDesc = null;
					try {
						attributeDesc = typeService.getAttributeDescriptor(
								typeService.getComposedType(GPCommerceProductModel.class), attr);
					} catch (Exception e) {
						LOG.error(e.getMessage(),e);
					}
					if (null != attributeDesc) {
						final Object attributeValue = modelService.getAttributeValue(productModel, attr);

						if (attributeValue instanceof CountryModel) {
							gs1DataMap.put(key, ((CountryModel) attributeValue).getSapCode());
						} else if (attributeValue instanceof BrandsEnum) {
							gs1DataMap.put(key, ((BrandsEnum) attributeValue).getCode());
						}else if (attributeValue instanceof BrandLabelEnum) {
							gs1DataMap.put(key, ((BrandLabelEnum) attributeValue).getCode());
						} else if (attr.equalsIgnoreCase(GPCommerceProductModel.SUPERCATEGORIES) && attributeValue instanceof List<?>) {
							
							if(attributeValue instanceof List<?>)
							{
								List<CategoryModel> list = (List<CategoryModel>)attributeValue;
								List<String> catergories=list.stream().map(cat->cat.getName()).collect(Collectors.toList());
								gs1DataMap.put(key,checkSpecialCharacters(String.join("; ",catergories)));
							}
						} else if(attr.equalsIgnoreCase(GPCommerceProductModel.KEYWORDS)){
							
							if(attributeValue instanceof List<?>)
							{
								List<KeywordModel> list = (List<KeywordModel>)attributeValue;
								List<String> keywords=list.stream().map(keyWord->keyWord.getKeyword()).collect(Collectors.toList());
								gs1DataMap.put(key,checkSpecialCharacters(String.join("; ",keywords)));
							}
							
						}else {
							if(null!=attributeValue) {
								gs1DataMap.put(key, checkSpecialCharacters((String)attributeValue));
							}else {
								gs1DataMap.put(key, "");
							}
						}
					}
				}
			}
			
			
		}

		return gs1DataMap;
	}

	private String checkSpecialCharacters(String input) {
		if(null!=input) {
			input = StringEscapeUtils.escapeCsv(input);	
		}
		return input;
	}
	
	private StringBuilder createCsvLineItem(Map<String, String> dataMap) {

		StringBuilder csvline = new StringBuilder();

		for (String key : csvHeaderList) {
			String value = dataMap.get(key);
			if (StringUtils.isNotBlank(value)) {
				csvline.append(value + ",");
			} else {
				csvline.append(",");
			}
		}
		
		return csvline;
	}

	private MediaModel createMediaModel(InputStream resource, ProductDataExportCronjobModel cronJob) {
		if (resource == null) {
			throw new IllegalArgumentException("Given resource is null");
		}
		String fileName = null;
		if(cronJob.getInputFormat().equalsIgnoreCase(GS1FORMAT)){
			fileName = "GS1ExportFile";
		}else if(cronJob.getInputFormat().equalsIgnoreCase(MASSCOFORMAT)){
			fileName = "MasscoExportFile";
		}else if(cronJob.getInputFormat().equalsIgnoreCase(SALSIFYFORMAT)){
			fileName = "SalsifyExportFile";
		}else{
			fileName = "ReStockItExportFile";
		}
		final String mediaCode = fileName + "_" + System.currentTimeMillis();
		final CatalogUnawareMediaModel mediaInput = prepareMedia(mediaCode, mediaCode + ".csv", "text/csv");

		try {
			this.modelService.initDefaults(mediaInput);
		} catch (ModelInitializationException var6) {
			throw new SystemException(var6);
		}

		this.modelService.save(mediaInput);
		this.mediaService.setStreamForMedia(mediaInput, resource);
		return mediaInput;
	}

	private CatalogUnawareMediaModel prepareMedia(final String code, final String reportFileName,
			final String mimeType) {
		final CatalogUnawareMediaModel media = modelService.create(CatalogUnawareMediaModel.class);
		media.setCode(code);
		media.setMime(mimeType);
		media.setRealFileName(reportFileName);
		return media;
	}

	public GPProductDao getGpProductDao() {
		return gpProductDao;
	}

	public void setGpProductDao(GPProductDao gpProductDao) {
		this.gpProductDao = gpProductDao;
	}

}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.core.cronjob;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gp.commerce.core.model.GPCompetitorProductModel;
import com.gp.commerce.core.model.GPXpressAlertProdHistoryModel;
import com.gp.commerce.core.model.ProductReplicationCronJobModel;
import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.dto.sfdc.Competitor;
import com.gpintegration.dto.sfdc.GPProductReplicationRequestDTO;
import com.gpintegration.dto.sfdc.Product;
import com.gpintegration.dto.sfdc.ProductCatalog;
import com.gpintegration.dto.sfdc.Product_;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.service.impl.GPDefaultSFDCProductReplicationService;
import com.gpintegration.utils.GPIntegrationUtils;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionService;
/**
 * 
 * @author spandiyan
 *
 */
public class GPSFDCProductReplicationJob extends AbstractJobPerformable<ProductReplicationCronJobModel>{

	private ConfigurationService configurationService;

	private SessionService sessionService;

	private GPDefaultSFDCProductReplicationService sfdcProductReplicationService;

	private static final Logger LOG = Logger.getLogger(GPSFDCProductReplicationJob.class);
	
	public static final String SFDC_PRODUCT_REPLICATION_LAST_RUNTIME_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static final String PRODUCT_REPLICATION_QUERY_BY_MANUAL_LAST_RUNTIME= "SELECT {gpxAlert.pk} FROM {GPXpressAlertProdHistory as gpxAlert} WHERE {gpxAlert.attributeName} IN (?attributeName) AND {gpxAlert.modifiedtime} >= ?modifiedtime";
	
	public static final String PRODUCT_REPLICATION_QUERY_BY_PRODUCT_CODE = "SELECT {gpxAlert.pk} FROM {GPXpressAlertProdHistory as gpxAlert} WHERE {gpxAlert.attributeName} IN (?attributeName) AND {gpxAlert.productCode} = ?productCode";
	
	public static final String PRODUCT_REPLICATION_QUERY_BY_DEFAULT_LAST_RUNTIME = "SELECT {gpxAlert.pk} FROM {GPXpressAlertProdHistory as gpxAlert} WHERE {gpxAlert.attributeName} IN (?attributeName) AND {gpxAlert.modifiedtime} >= ?modifiedtime";
	
	public static final String  GET_COMPETITOR_INFO_QUERY = "SELECT {cp.pk} FROM {GPCompetitorProduct as cp JOIN CatalogVersion AS cv ON {cp.catalogVersion} = {cv.pk} and {cv.version} = 'Online'}";
	
	public static final String GET_COMPETITOR_BY_ID = "SELECT {cp.pk} FROM {GPCompetitorProduct as cp JOIN CatalogVersion AS cv ON {cp.catalogVersion} = {cv.pk} and {cv.version} = 'Online' and {cp.code} IN (?comeptitorCodes)}";
	
	public static final String TYPE_COMPETITOR = "COMPETITOR";
	
	public static final String GET_MEDIA_MODEL_QUERY = "SELECT {pk} FROM {Media as m} WHERE {m.pk} = ?thumbnailPK";

	@Override
	public PerformResult perform(ProductReplicationCronJobModel replicationCronJobModel) {
		LOG.info("GPSFDCProductReplicationJob started successfully");
		GPProductReplicationRequestDTO productReplicationReq = null;
		try
		{
			while(true)
			{
				if (clearAbortRequestedIfNeeded(replicationCronJobModel))
				{
					LOG.debug("The job is aborted.");
					return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
				}
				productReplicationReq = getProductsForReplication(replicationCronJobModel);
				if(CollectionUtils.isNotEmpty(productReplicationReq.getProductCatalog().getProducts()))
				{
					replicateProduct(productReplicationReq, replicationCronJobModel);
				} 
				else 
				{
					LOG.info("No products found for replication.");
				}
				return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
				
			}
		} 
		catch(JsonProcessingException | GPIntegrationException gpie) {
			LOG.error("Product replication failed with exception:"+gpie.getMessage(), gpie);
		} catch(Exception exception) {
			LOG.error("Product replication failed with exception:"+exception.getMessage(), exception);
		}
		LOG.info("GPSFDCProductReplicationJob completed successfully");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}
	
	public void replicateProduct(GPProductReplicationRequestDTO productReplicationRequest, ProductReplicationCronJobModel replicationCronJobModel) throws JsonProcessingException {
		final ObjectMapper responseMapper = new ObjectMapper();
		int maxRetryCount = configurationService.getConfiguration().getInt(GpintegrationConstants.GP_SFDC_PRODUCT_REPLICATION_RETRY_COUNT);
		ResponseEntity<String> replicationResponse = null;
		String replciationStatusCode = null;
		int retryCount = 0;
		String productSKUList=" ";
		List<Product> productList= productReplicationRequest.getProductCatalog().getProducts();
		for(Product product: productList) {
			productSKUList +=product.getProduct().getCode().concat(",") ;
		}
		
		do {
			try {
				replicationResponse = sfdcProductReplicationService.replicateProduct(productReplicationRequest);
				LOG.info("Response Object from SCPI Replication Service {}"+responseMapper.writeValueAsString(replicationResponse));
			} catch(GPIntegrationException e) {
				LOG.error(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) +"  SFDC Product Replication Failed For:"+ productSKUList);
	        	LOG.error("Product replication call failed with exception:", e);
			}
			retryCount++;
			if(null != replicationResponse) {
				replciationStatusCode = replicationResponse.getStatusCode().toString();
				LOG.info("Product replication status code:"+replciationStatusCode);
				if(GpintegrationConstants.HTTP_STATUS_CODE_SUCCESS.equals(replciationStatusCode)) {
					break;
				}
			}
		} while(retryCount < maxRetryCount);
		
		if(retryCount == maxRetryCount && !GpintegrationConstants.HTTP_STATUS_CODE_SUCCESS.equals(replciationStatusCode)) {
			LOG.info("Not able to replicate products even after retry limit:"+"Product SKU details:"+productSKUList+":Service response:"+responseMapper.writeValueAsString(replicationResponse));
		}
	}
	
	/**
	 * Fetches all the records from GPXpressAlertProdHistory table based on (Jobs lastRunDate, manualDate or product code).
	 * 
	 * @param replicationCronJobModel
	 * @return SCPI specific product replication request
	 */
	public GPProductReplicationRequestDTO getProductsForReplication(ProductReplicationCronJobModel replicationCronJobModel) throws GPIntegrationException{
		GPProductReplicationRequestDTO productReplicationRequest = new GPProductReplicationRequestDTO();
		ProductCatalog productCatalogDTO = new ProductCatalog();
		List<Product> productsDTOList = new ArrayList<>();
		final String attributes= configurationService.getConfiguration().getString("sfdc.product.attribute.list");
		final String timeZone = configurationService.getConfiguration().getString(GpintegrationConstants.GP_NETSUITE_CUSTOMER_ORDER_REPLICATION_REMORSE_PERIOD_TIMEZONE);
		final long dayToSubstract = configurationService.getConfiguration().getLong("sfdc.product.replication.start.date.minus.days");
		LOG.info("Product attribute list from properties file:"+attributes);
		List<String> attributeList =null;
		List<GPXpressAlertProdHistoryModel> productAttributeList = null;
		
		if(StringUtils.isNotBlank(attributes))
		{
			attributeList = Arrays.asList(StringUtils.split(attributes, ","));
		}
		if(null != replicationCronJobModel.getProductUpdateDate()) {
			LOG.info("Product updated time has been set through job override property (productUpdateDate), filtering products for replication based on that.");
			final FlexibleSearchQuery searchProdByManualLastRuntime = new FlexibleSearchQuery(PRODUCT_REPLICATION_QUERY_BY_MANUAL_LAST_RUNTIME);
			searchProdByManualLastRuntime.addQueryParameter("attributeName", attributeList);
			searchProdByManualLastRuntime.addQueryParameter("modifiedtime", GPIntegrationUtils.getJobLastRunTime(replicationCronJobModel.getProductUpdateDate(), false, timeZone, dayToSubstract));
			SearchResult<GPXpressAlertProdHistoryModel> productHistorySearchResult = getFlexibleSearchService().search(searchProdByManualLastRuntime);
			if(productHistorySearchResult.getCount()> 0) {
				productAttributeList = productHistorySearchResult.getResult();
				if(CollectionUtils.isNotEmpty(productAttributeList)) {
					groupAndExtractProductAndCompetitorChanges(productAttributeList, productsDTOList);
				}
			} else {
				LOG.info("No products found for replication - Search by manual last runtime");
			}
		} else if(null != replicationCronJobModel.getProductCode()) {
			LOG.info("ProductCode has been set through job override property (productCode), filtering products for replication based on that.");
			final FlexibleSearchQuery searchProductByCode = new FlexibleSearchQuery(PRODUCT_REPLICATION_QUERY_BY_PRODUCT_CODE);
			searchProductByCode.addQueryParameter("attributeName", attributeList);
			searchProductByCode.addQueryParameter("productCode", replicationCronJobModel.getProductCode());
			SearchResult<GPXpressAlertProdHistoryModel> productHistorySearchResult = getFlexibleSearchService().search(searchProductByCode);
			if(productHistorySearchResult.getCount()> 0) {
				productAttributeList = productHistorySearchResult.getResult();
				if(CollectionUtils.isNotEmpty(productAttributeList)) {
					groupAndExtractProductAndCompetitorChanges(productAttributeList, productsDTOList);
				}
			} else {
				LOG.info("No products found for replication - Search by Product Code");
			}
		} else {
			LOG.info("Filetering products for replication based on jobs last run time.");
			final FlexibleSearchQuery searchProdByLastRuntime = new FlexibleSearchQuery(PRODUCT_REPLICATION_QUERY_BY_DEFAULT_LAST_RUNTIME);
			searchProdByLastRuntime.addQueryParameter("attributeName", attributeList);
			String lastRunDate = GPIntegrationUtils.getJobLastRunTime(replicationCronJobModel.getStartTime(), true, timeZone, dayToSubstract);
			searchProdByLastRuntime.addQueryParameter("modifiedtime", lastRunDate);
			SearchResult<GPXpressAlertProdHistoryModel> productHistorySearchResult = getFlexibleSearchService().search(searchProdByLastRuntime);
			if(productHistorySearchResult.getCount()> 0) {
				productAttributeList = productHistorySearchResult.getResult();
				LOG.info("Number of Products matched for replication:"+productAttributeList.size());
				if(CollectionUtils.isNotEmpty(productAttributeList)) {
					groupAndExtractProductAndCompetitorChanges(productAttributeList, productsDTOList);
				}
			} else {
				LOG.info("No products found for replication - Search by default last runtime");
			}
		}
		productCatalogDTO.setProducts(productsDTOList);
		productReplicationRequest.setProductCatalog(productCatalogDTO);
		return productReplicationRequest;
	}
	
	/**
	 * Groups the product and competitor related changes based on (PRODUCT or COMPETITOR).
	 * 
	 * @param productAttributeList
	 * @param productsDTOList
	 */
	private void groupAndExtractProductAndCompetitorChanges(List<GPXpressAlertProdHistoryModel> productAttributeList, List<Product> productsDTOList){
		LOG.info("Grouping and extracting product information by Product and Competitor");
		
		final Map<String, List<GPXpressAlertProdHistoryModel>> competitorDataMap = productAttributeList.stream().filter(productAttribute -> productAttribute.getProductAttributeSource().getCode().equals(TYPE_COMPETITOR)).collect(Collectors.groupingBy(GPXpressAlertProdHistoryModel::getProductCode));
		final Map<String, List<GPXpressAlertProdHistoryModel>> productDataMap = productAttributeList.stream().filter(productAttribute -> !productAttribute.getProductAttributeSource().getCode().equals(TYPE_COMPETITOR)).collect(Collectors.groupingBy(GPXpressAlertProdHistoryModel::getProductCode));
		List<GPCompetitorProductModel> competitorInfo = getCompetitorData();
		if(productDataMap.size() == 0 && competitorDataMap.size() != 0) {
			LOG.info("There is no product changes, only competitor changes found. Mapping the competitor to assocatied product(s).");
			productsDTOList.addAll(mapCompetitorDetailsToProduct(competitorDataMap, competitorInfo));
		} else {
			LOG.info("Found product and competitor changes. Mapping respective product and competitor.");
			List<String> unassociatedCompetitors = null;
			unassociatedCompetitors = getUnassociatedCompetitors(productDataMap, competitorDataMap);
			if(CollectionUtils.isNotEmpty(unassociatedCompetitors)) {
				LOG.info("Found following unassociated competitors:"+unassociatedCompetitors.toString());
				productsDTOList.addAll(mapProductCodeForCompetitors(unassociatedCompetitors, competitorInfo, competitorDataMap));
			}
			productDataMap.keySet().stream().forEach(code ->productsDTOList.add(mapProductData(productDataMap.get(code),code, competitorInfo, competitorDataMap)));
		}
	}
	
	/**
	 * Fetches the competitor details, who doesn't have association with given product changes.
	 * 
	 * @param productMap
	 * @param competitorMap
	 * @return list of competitor product code.
	 */
	private List<String> getUnassociatedCompetitors(Map<String, List<GPXpressAlertProdHistoryModel>> productMap, Map<String, List<GPXpressAlertProdHistoryModel>> competitorMap) {
		List<String> unassocaitedCompetitors = new ArrayList<>();
		List<String> proudctCodeList = new ArrayList<>();
		List<String> competitorCodeList = new ArrayList<>();
		List<GPCompetitorProductModel> competitorInfo = null;
		Map<String, List<String>> competitorDetailsMap = new HashMap<>();
		
		if(MapUtils.isNotEmpty(productMap)) {
			productMap.keySet().forEach(productCode ->{
				proudctCodeList.add(productCode);
			});
		}
		
		if(MapUtils.isNotEmpty(competitorMap)) {
			competitorMap.keySet().forEach(competitorCode ->{
				competitorCodeList.add(competitorCode);
			});
		}
		
		List<String> competitorProductList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(competitorCodeList)) {
			competitorInfo = getCompetitorById(competitorCodeList);
			if(CollectionUtils.isNotEmpty(competitorInfo)) {
				competitorInfo.stream().forEach(competitor ->{
					List<String> competitorProductCode = new ArrayList<>();
					Collection<ProductModel> productList = competitor.getProduct();
					if(CollectionUtils.isNotEmpty(productList)) {
						productList.stream().forEach(product ->{
							competitorProductList.add(product.getCode());
							competitorProductCode.add(product.getCode());
						});
					}
					competitorDetailsMap.put(competitor.getCode(), competitorProductCode);
				});
			}
		}
		
		LOG.info("Product codes from alert table:"+proudctCodeList.toString());
		LOG.info("Competitor codes from alert table:"+competitorCodeList.toString());
		LOG.info("Competitor product code:"+competitorProductList.toString());
		
		if(CollectionUtils.isNotEmpty(proudctCodeList) && CollectionUtils.isNotEmpty(competitorProductList)) {
			if(proudctCodeList.size() == 1) {
				proudctCodeList.add(GpintegrationConstants.DUMMY_VALUE);
			}
			if(!proudctCodeList.retainAll(competitorProductList.stream().distinct().collect(Collectors.toList()))) {
				unassocaitedCompetitors.addAll(competitorCodeList);
			} else {
				proudctCodeList.stream().forEach(productCode ->{
					Iterator<Map.Entry<String, List<String>>> iterator = competitorDetailsMap.entrySet().iterator();
					while(iterator.hasNext()) {
						Map.Entry<String, List<String>> entry = iterator.next();
						if(entry.getValue().contains(productCode)) {
							iterator.remove();
						}
					}
				});
				competitorDetailsMap.keySet().forEach(com ->{
					unassocaitedCompetitors.add(com);
				});
			}
		}
		LOG.info("Unassociated competitor list:"+unassocaitedCompetitors.toString());
		return unassocaitedCompetitors;
	}
	
	/**
	 * Creates competitor request based on products associated to the
	 * competitor. This method will be called if the GPXpressAlertProdHistory 
	 * contains products changes and competitor changes, but there is no changes
	 * in competitor associated to the product changes.
	 * 
	 * We will get the list of competitor codes and fetch Products associated to
	 * those Competitor. For each product we will push the competitor related
	 * changes. 
	 * 
	 * @param competitorCodeList
	 * @param competitorModelList
	 * @param competitorDataMap
	 * @return SCPI specific Product request only contains competitor changes.
	 */
	private List<Product> mapProductCodeForCompetitors(List<String> competitorCodeList, List<GPCompetitorProductModel> competitorModelList, Map<String, List<GPXpressAlertProdHistoryModel>> competitorDataMap){
		LOG.info("Found competitors which dont have an assocation with product changes, mapping competitor details based on assoicated product details.");
		List<Product> productDTOList = new ArrayList<>();
		List<String> productCodeList = new ArrayList<>();
		List<ProductModel> productModelList = new ArrayList<>();
		MultiValueMap<String, List<GPXpressAlertProdHistoryModel>> competitorGroupMap = new LinkedMultiValueMap<String, List<GPXpressAlertProdHistoryModel>>();
		
		if(CollectionUtils.isNotEmpty(competitorCodeList)) {
			competitorCodeList.stream().forEach(competitorCode ->{
				competitorModelList.stream().forEach(competitorModel ->{
					if(competitorModel.getCode().equals(competitorCode)) {
						Collection<ProductModel> productList = competitorModel.getProduct();
						if(CollectionUtils.isNotEmpty(productList)) {
							productList.stream().forEach(product ->{
								if(!productCodeList.contains(product.getCode())) {
									productCodeList.add(product.getCode());
									productModelList.add(product);
								}
							});
						}
					}
				});
			});
		}
		
		if(MapUtils.isNotEmpty(competitorDataMap)) {
			competitorDataMap.keySet().forEach(competitorCode ->{
				productModelList.stream().forEach(product -> {
					Collection<GPCompetitorProductModel> competitors = product.getCompetitorProduct();
					if(CollectionUtils.isNotEmpty(competitors)) {
						competitors.stream().forEach(competitorModel ->{
							if(competitorCode.equals(competitorModel.getCode())) {
								List<GPXpressAlertProdHistoryModel> competiorChanges = competitorDataMap.get(competitorCode);
								if(CollectionUtils.isNotEmpty(competiorChanges)) {
									competitorGroupMap.add(product.getCode(), competiorChanges);
									}
								}
							});
					}
					});
				});
		}
		
		if(MapUtils.isNotEmpty(competitorGroupMap)) {
			competitorGroupMap.keySet().forEach(productCode ->{
				List<List<GPXpressAlertProdHistoryModel>> competitorChangeList = competitorGroupMap.get(productCode);
				List<Competitor> competitorReqList = new ArrayList<>();
				Product productDTO = new Product();
				Product_ productData = new Product_();
				productData.setCode(productCode);
				productDTO.setProduct(productData);
				if(CollectionUtils.isNotEmpty(competitorChangeList)) {
					competitorChangeList.stream().forEach(competitorChangesList ->{
						Competitor competitorReq = mapCompetitorData(competitorChangesList, productCode);
						competitorReqList.add(competitorReq);
					});
				}
				productData.setCompetitors(competitorReqList);
				productDTOList.add(productDTO);
			});
		}
		
		return productDTOList;
	}

	/**
	 * Creates competitor request based on products associated to the
	 * Competitor. This method will be called if the GPXpressAlertProdHistory
	 * contains only Competitor related changes.
	 * 
	 * @param competitorDataMap
	 * @param competitorList
	 * @return SCPI specific product request.
	 */
	public List<Product> mapCompetitorDetailsToProduct(Map<String, List<GPXpressAlertProdHistoryModel>> competitorDataMap, List<GPCompetitorProductModel> competitorList) {
		LOG.info("Found only competitor changes, mapping competitor details based on assoicated product details.");
		List<String> productCodeList = new ArrayList<>();
		List<ProductModel> productModelList = new ArrayList<>();
		List<Product> productDTOList = new ArrayList<>();
		LOG.info("Competitor Map Size:"+competitorDataMap.size());
		MultiValueMap<String, List<GPXpressAlertProdHistoryModel>> competitorGroupMap = new LinkedMultiValueMap<String, List<GPXpressAlertProdHistoryModel>>();
		MultiValueMap<String, Map<String, List<GPXpressAlertProdHistoryModel>>> testmap1 = new LinkedMultiValueMap<String, Map<String, List<GPXpressAlertProdHistoryModel>>>();
		if(MapUtils.isNotEmpty(competitorDataMap)){
			competitorDataMap.keySet().forEach(competitorCode ->{
				if(CollectionUtils.isNotEmpty(competitorList)) {
					competitorList.stream().forEach(competitor ->{
						if(competitorCode.equals(competitor.getCode())) {
							Collection<ProductModel> productList = competitor.getProduct();
							if(CollectionUtils.isNotEmpty(productList)) {
								productList.stream().forEach(product ->{
									if(!productCodeList.contains(product.getCode())) {
										productCodeList.add(product.getCode());
										productModelList.add(product);
									}
								});
							} else {
								LOG.info("Competitor code:"+competitor.getCode()+": doesn't have any associated product.");
							}
						}
					});
				}
			});
		}
		
		if(MapUtils.isNotEmpty(competitorDataMap)) {
			competitorDataMap.keySet().forEach(competitorCode ->{
				if(CollectionUtils.isNotEmpty(productModelList)) {
					productModelList.stream().forEach(product -> {
						Collection<GPCompetitorProductModel> competitors = product.getCompetitorProduct();
						if(CollectionUtils.isNotEmpty(competitors)) {
							competitors.stream().forEach(competitorModel ->{
								if(competitorCode.equals(competitorModel.getCode())) {
									testmap1.add(product.getCode(), competitorDataMap);
									List<GPXpressAlertProdHistoryModel> competiorChanges = competitorDataMap.get(competitorCode);
									if(CollectionUtils.isNotEmpty(competiorChanges)) {
										competitorGroupMap.add(product.getCode(), competiorChanges);
										}
									}
								});
						}
						});
				}
				});
		}
		
		if(MapUtils.isNotEmpty(competitorGroupMap)) {
			competitorGroupMap.keySet().forEach(productCode ->{
				List<List<GPXpressAlertProdHistoryModel>> competitorChangeList = competitorGroupMap.get(productCode);
				List<Competitor> competitorReqList = new ArrayList<>();
				Product productDTO = new Product();
				Product_ productData = new Product_();
				productData.setCode(productCode);
				productDTO.setProduct(productData);
				if(CollectionUtils.isNotEmpty(competitorChangeList)) {
					competitorChangeList.stream().forEach(competitorChangesList ->{
						Competitor competitorReq = mapCompetitorData(competitorChangesList, productCode);
						competitorReqList.add(competitorReq);
					});
				}
				productData.setCompetitors(competitorReqList);
				productDTOList.add(productDTO);
			});	
		}
			
		return productDTOList;
	}
	
	/**
	 * Fetches all competitor information.
	 * 
	 * @param competitorCode
	 * @return All Competitor details. 
	 */
	public List<GPCompetitorProductModel> getCompetitorData() {
		LOG.info("Fetching all the competitor details.");
		List<GPCompetitorProductModel> competitorList = null;
		final FlexibleSearchQuery searchProdByLastRuntime = new FlexibleSearchQuery(GET_COMPETITOR_INFO_QUERY);
		SearchResult<GPCompetitorProductModel> productResut = getFlexibleSearchService().search(searchProdByLastRuntime);
		if(productResut.getCount() > 0) {
			competitorList = productResut.getResult();
		}
		return competitorList;
	}
	
	/**
	 * Fetches competitors based on competitor id.
	 * 
	 * @param competitorCodes
	 * @return list of GPCompetitorProductModel
	 */
	public List<GPCompetitorProductModel> getCompetitorById(List<String> competitorCodes){
		LOG.info("Fetching competitor based on id");
		List<GPCompetitorProductModel> competitorList = null;
		final FlexibleSearchQuery searchByCompetitorCode = new FlexibleSearchQuery(GET_COMPETITOR_BY_ID);
		searchByCompetitorCode.addQueryParameter("comeptitorCodes", competitorCodes);
		SearchResult<GPCompetitorProductModel> competitorResult = getFlexibleSearchService().search(searchByCompetitorCode);
		if(competitorResult.getCount() > 0) {
			competitorList = competitorResult.getResult();
		}
		return competitorList;
	}
	
	/**
	 * Creates SCPI Product replication request based on GPXpressAlertProdHistory data.
	 * 
	 * @param productAttributeList
	 * @param productCode
	 * @param competitorList
	 * @param competitorMap
	 * @return SCPI specific Product request.
	 */
	private Product mapProductData(List<GPXpressAlertProdHistoryModel> productAttributeList,String productCode, List<GPCompetitorProductModel> competitorList, Map<String, List<GPXpressAlertProdHistoryModel>> competitorMap) {
		Product product = new Product();
		Product_ productData = new Product_();
		productData.setCode(productCode);
		List<Competitor> competitorReqList = new ArrayList<>();
		LOG.info(" Attribute list count in getProductData {}"+productAttributeList.size());
		Map<String, String> productAttributeMap = new HashMap<>();
		productAttributeList.stream().forEach(productAttribute ->{
			LOG.debug(" attribute name {}"+productAttribute.getAttributeName());
			productAttributeMap.put(productAttribute.getAttributeName(),productAttribute.getNewValue());
		});
		productAttributeMap.keySet().stream().forEach(attribute ->{
			LOG.info(  "product attribute {}- {}"+attribute+","+productAttributeMap.get(attribute));
		});
		if(productAttributeMap.keySet().contains(GpintegrationConstants.HAS_SAMPLE)) {
			productData.setSampleIndicator(productAttributeMap.get(GpintegrationConstants.HAS_SAMPLE));
		}
		if(productAttributeMap.keySet().contains(GpintegrationConstants.IS_SAMPLE)) {
			productData.setSamplesActive(productAttributeMap.get(GpintegrationConstants.IS_SAMPLE));
		}
		if(productAttributeMap.keySet().contains(GpintegrationConstants.THUMBNAIL)) {
			LOG.info("Thumbanail value:"+productAttributeMap.get(GpintegrationConstants.THUMBNAIL));
			if(null != productAttributeMap.get(GpintegrationConstants.THUMBNAIL)) {
				MediaModel thumbnailMediaModel = getThumnailMediaModel(productAttributeMap.get(GpintegrationConstants.THUMBNAIL));
				if(null != thumbnailMediaModel) {
					LOG.info("Thumbnail URL:"+thumbnailMediaModel.getURL());
					productData.setThumbnailURL(thumbnailMediaModel.getURL());
				}
			}
		}
		if(productAttributeMap.keySet().contains(GpintegrationConstants.CODE)) {
			productData.setCode(productAttributeMap.get(GpintegrationConstants.CODE));
		}
		if(productAttributeMap.keySet().contains(GpintegrationConstants.SAMPLE_ORDER_LIMIT)) {
			productData.setGpmsMaxOrderLimit(productAttributeMap.get(GpintegrationConstants.SAMPLE_ORDER_LIMIT));
		}
		if(productAttributeMap.keySet().contains(GpintegrationConstants.SAMPLE_UOM_DESCRIPTION)) {
			productData.setSampleUOMDescription(productAttributeMap.get(GpintegrationConstants.SAMPLE_UOM_DESCRIPTION));
		}
		if(productAttributeMap.keySet().contains(GpintegrationConstants.COUNTRY_ISO_CODE)) {
			productData.setShippingRestrictionCountries(productAttributeMap.get(GpintegrationConstants.COUNTRY_ISO_CODE));
		}
		if(productAttributeMap.keySet().contains(GpintegrationConstants.DELIVERY_MODE)) {
			productData.setShippingRestrictionMethods(productAttributeMap.get(GpintegrationConstants.DELIVERY_MODE));
		}
		if(productAttributeMap.keySet().contains(GpintegrationConstants.REGION)) {
			productData.setShippingRestrictionStates(productAttributeMap.get(GpintegrationConstants.REGION));
		}
		
		if(CollectionUtils.isNotEmpty(competitorList) && null != competitorMap) {
			competitorList.stream().forEach(competitor -> {
				Collection<ProductModel> productList = competitor.getProduct();
				if(CollectionUtils.isNotEmpty(productList)) {
					productList.stream().forEach(compProductCode -> {
						if (compProductCode.getCode().equals(productCode)) {
							LOG.info("Competitor code for product code:" + productCode + ":" + competitor.getCode());
							String competitorCode = competitor.getCode();
							final List<GPXpressAlertProdHistoryModel> alertHistoryList = competitorMap.get(competitorCode);
							if (CollectionUtils.isNotEmpty(alertHistoryList)) {
								Competitor competitorReq = mapCompetitorData(alertHistoryList, productCode);
								competitorReqList.add(competitorReq);
								productData.setCompetitors(competitorReqList);
							} else {
								LOG.info("No data found from competitorMap for given key:"+competitorCode);
							}
						} else {
							LOG.info("No competitor data found for given product");
						}
					});
				} else {
					//LOG.info("Competitor doesn't have any product association");
				}
			});
		} else {
			LOG.info("No competitor data in GPXpressAlertProdHistory table.");
		}
		product.setProduct(productData);
		return product;

	}
	
	/**
	 * Fetches MediaModel of thumbnail based on PK.
	 * 
	 * @param thumbnailPK
	 * @return MediaModel of thumbnail PK
	 */
	private MediaModel getThumnailMediaModel(String thumbnailPK) {
		MediaModel thumnailMediaModel = null;
		final FlexibleSearchQuery searchMediaModel = new FlexibleSearchQuery(GET_MEDIA_MODEL_QUERY);
		searchMediaModel.addQueryParameter("thumbnailPK", thumbnailPK);
		SearchResult<MediaModel> mediaModelSearchResult = getFlexibleSearchService().search(searchMediaModel);
		if(mediaModelSearchResult.getCount()> 0) {
			thumnailMediaModel = mediaModelSearchResult.getResult().get(0);
		}
		return thumnailMediaModel;
	}
	
	/**
	 * Creates SCPI Competitor request based on GPXpressAlertProdHistory data.
	 * 
	 * @param productAttributeList
	 * @param productCode
	 * @param competitorId
	 * @return SCPI specific competitor request.
	 */
	private Competitor mapCompetitorData(List<GPXpressAlertProdHistoryModel> productAttributeList,String productCode) {
		LOG.info("Inside mapCompetitorData method call");
		Competitor competitor=new Competitor();
		Map<String, String> competitorAttributeMap = new HashMap<>();
		
		productAttributeList.stream().forEach(competitorAttribute -> {
			competitorAttributeMap.put(competitorAttribute.getAttributeName(), competitorAttribute.getNewValue());
		});
		
		if(competitorAttributeMap.keySet().contains(GpintegrationConstants.CASE_BUOM)) {
			competitor.setCompetitorCaseBUOM(competitorAttributeMap.get(GpintegrationConstants.CASE_BUOM));
		}
		if(competitorAttributeMap.keySet().contains(GpintegrationConstants.COMPETITOR_NAME)) {
			competitor.setCompetitorName(competitorAttributeMap.get(GpintegrationConstants.COMPETITOR_NAME));
		}
		if(competitorAttributeMap.keySet().contains(GpintegrationConstants.COMPETITOR_NUMBER)) {
			competitor.setCompetitorNumber(competitorAttributeMap.get(GpintegrationConstants.COMPETITOR_NUMBER));
		}
		if(competitorAttributeMap.keySet().contains(GpintegrationConstants.CASE_QTY_BUOM)) {
			competitor.setCompetitorCaseQtyBUOM(competitorAttributeMap.get(GpintegrationConstants.CASE_QTY_BUOM));
		}
		if(competitorAttributeMap.keySet().contains(GpintegrationConstants.MATERIAL)) {
			competitor.setCompetitorMaterial(competitorAttributeMap.get(GpintegrationConstants.MATERIAL));
		}
		if(competitorAttributeMap.keySet().contains(GpintegrationConstants.COLOR)) {
			competitor.setCompetitorMaterialColor(competitorAttributeMap.get(GpintegrationConstants.COLOR));
		}
		if(competitorAttributeMap.keySet().contains(GpintegrationConstants.EXTERNAL_ID)) {
			competitor.setExternalID(competitorAttributeMap.get(GpintegrationConstants.EXTERNAL_ID));
		}
		if(competitorAttributeMap.keySet().contains(GpintegrationConstants.BRAND)) {
			competitor.setCompetitorBrand(competitorAttributeMap.get(GpintegrationConstants.BRAND));
		}
		if(competitorAttributeMap.keySet().contains(GpintegrationConstants.CODE)) {
			competitor.setCompetitorProductNumber(competitorAttributeMap.get(GpintegrationConstants.CODE));
		}
		
		competitor.setGpSku(productCode);
		
		if(competitorAttributeMap.keySet().contains(GpintegrationConstants.CODE)) {
			competitor.setCompSKU(competitorAttributeMap.get(GpintegrationConstants.CODE));
		}
		
		if(competitorAttributeMap.keySet().contains(GpintegrationConstants.RELATIONSHIP_TYPE)) {
			competitor.setRelationshipType(competitorAttributeMap.get(GpintegrationConstants.RELATIONSHIP_TYPE));
		}
		
		if(competitorAttributeMap.keySet().contains(GpintegrationConstants.COMPETITOR_ID)) {
			competitor.setCompetitorID(competitorAttributeMap.get(GpintegrationConstants.COMPETITOR_ID));
		}
		
		if(competitorAttributeMap.keySet().contains(GpintegrationConstants.DESCRIPTION)) {
			String description = competitorAttributeMap.get(GpintegrationConstants.DESCRIPTION);
			if(StringUtils.isNotEmpty(description)) {
				competitor.setCompetitorMaterialDescription(competitorAttributeMap.get(GpintegrationConstants.DESCRIPTION));
			}
		}
		
		// Address attribute mappings
		
		if(competitorAttributeMap.keySet().contains(GpintegrationConstants.STREET)) {
			competitor.setStreet(competitorAttributeMap.get(GpintegrationConstants.STREET));
		}
		
		if(competitorAttributeMap.keySet().contains(GpintegrationConstants.CITY)) {
			competitor.setCity(competitorAttributeMap.get(GpintegrationConstants.CITY));
		}
		
		if(competitorAttributeMap.keySet().contains(GpintegrationConstants.STATE)) {
			competitor.setState(competitorAttributeMap.get(GpintegrationConstants.STATE));
		}
		
		if(competitorAttributeMap.keySet().contains(GpintegrationConstants.COUNTRY)) {
			competitor.setCountry(competitorAttributeMap.get(GpintegrationConstants.COUNTRY));
		}
		
		if(competitorAttributeMap.keySet().contains(GpintegrationConstants.POSTALCODE)) {
			competitor.setPostalCode(competitorAttributeMap.get(GpintegrationConstants.POSTALCODE));
		}
		
		return competitor;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	public GPDefaultSFDCProductReplicationService getSfdcProductReplicationService() {
		return sfdcProductReplicationService;
	}

	public void setSfdcProductReplicationService(GPDefaultSFDCProductReplicationService sfdcProductReplicationService) {
		this.sfdcProductReplicationService = sfdcProductReplicationService;
	}
	@Override
	public boolean isAbortable() {
		return true;
	} 
}
/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.core.cronjob;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gp.commerce.core.model.ProductReferenceReplicationCronJobModel;
import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.dto.sfdc.GPProductRelationshipRequestDTO;
import com.gpintegration.dto.sfdc.ProductRelationships;
import com.gpintegration.dto.sfdc.Relationship;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.service.impl.GPDefaultProductReferenceReplicationService;
import com.gpintegration.utils.GPIntegrationUtils;

import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionService;

/**
 * 
 * @author spandiyan
 *
 */

public class GPProductReferenceReplicationJob extends AbstractJobPerformable<ProductReferenceReplicationCronJobModel>{

	private ConfigurationService configurationService;

	private SessionService sessionService;
	
	private GPDefaultProductReferenceReplicationService gpProductReferenceReplicationService;
	
	private static final Logger LOG = Logger.getLogger(GPProductReferenceReplicationJob.class);
	
	public static final String PRODUCT_REFERENCE_REPLICATION_QUERY_BY_PRODUCT_CODE = "SELECT {pr.pk} FROM {ProductReference as pr JOIN Product AS p ON {pr.source} = {p.pk} JOIN CatalogVersion AS cv ON {p.catalogVersion} = {cv.pk} AND {cv.version} = 'Online' AND {p.code} = ?productCode }";
	
	public static final String PRODUCT_REFERENCE_REPLICATION_QUERY_BY_LAST_RUN_TIME = "SELECT {pr.pk} FROM {ProductReference as pr JOIN Product AS p ON {pr.source} = {p.pk} JOIN CatalogVersion AS cv ON {p.catalogVersion} = {cv.pk} AND {cv.version} = 'Online'} WHERE {pr.modifiedtime} >= ?modifiedTime";
	
	@Override
	public PerformResult perform(ProductReferenceReplicationCronJobModel productReferenceCronJobModel) {
		LOG.info("Product reference replication job started successfully");
		List<ProductReferenceModel> productReferenceList = null;
		try {
			while(true)
			{
				if (clearAbortRequestedIfNeeded(productReferenceCronJobModel))
				{
					LOG.debug("The job is aborted.");
					return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
				}
				productReferenceList = getProductReference(productReferenceCronJobModel);
				if(CollectionUtils.isNotEmpty(productReferenceList))
				{
					GPProductRelationshipRequestDTO productRelationRequest = createProductRelationRequest(productReferenceList);
					GPProductRelationshipRequestDTO productRelationDeleteRequest = createRelationRequestForDeletion(productReferenceList);
					if(CollectionUtils.isNotEmpty(productRelationRequest.getProductRelationships().getRelationship())) {
						LOG.info("Replicating product relations for create and update operation.");
						replicateReference(productRelationRequest);
					}
					if(CollectionUtils.isNotEmpty(productRelationDeleteRequest.getProductRelationships().getRelationship())) {
						LOG.info("Replicating product relations for delete operation.");
						replicateReference(productRelationDeleteRequest);
					}
				}
				else 
				{
					LOG.info("No product references found for replication.");
				}
				return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
			}
			} 
		catch(JsonProcessingException | GPIntegrationException gpie) {
			LOG.error("Product replication failed with exception:"+gpie.getMessage(), gpie);
		} catch(Exception exception) {
			LOG.error("Product replication failed with exception:"+exception.getMessage(), exception);
		}
		LOG.info("Product reference replication job completed successfully");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}
	
	public void replicateReference(GPProductRelationshipRequestDTO productRelationRequest) throws JsonProcessingException{
		final ObjectMapper responseMapper = new ObjectMapper();
		int maxRetryCount = configurationService.getConfiguration().getInt(GpintegrationConstants.GP_SFDC_PRODUCT_REFERENCE_REPLICATION_RETRY_COUNT);
		ResponseEntity<String> refReplicaitonResponse = null;
		int retryCount = 0;
		String refReplicationStatusCode = null;
		do {
			try {
				refReplicaitonResponse = gpProductReferenceReplicationService.replicateProductReference(productRelationRequest);
				LOG.info("Reference Replication Response Object from SCPI Service {}"+responseMapper.writeValueAsString(refReplicaitonResponse));
			} catch (GPIntegrationException e) {
				LOG.error(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) +"  SFDC Product Reference Replication Failed For:");
	        	LOG.error("Product reference replication call failed with exception:", e);
			}
			
			retryCount++;
			if(null != refReplicaitonResponse) {
				refReplicationStatusCode = refReplicaitonResponse.getStatusCode().toString();
				LOG.info("Reference replication service response status code:"+refReplicationStatusCode);
				if(GpintegrationConstants.HTTP_STATUS_CODE_SUCCESS.equals(refReplicationStatusCode)) {
					break;
				}
			}
		} while(retryCount < maxRetryCount);
		
		if(retryCount == maxRetryCount && !GpintegrationConstants.HTTP_STATUS_CODE_SUCCESS.equals(refReplicationStatusCode)) {
			LOG.info("Not able to replicate reference products even after retry limit Service response:"+responseMapper.writeValueAsString(refReplicaitonResponse));
		}
	}
	
	public List<ProductReferenceModel> getProductReference(ProductReferenceReplicationCronJobModel productReferenceCronJobModel) throws GPIntegrationException{
		final String timeZone = configurationService.getConfiguration().getString(GpintegrationConstants.GP_NETSUITE_CUSTOMER_ORDER_REPLICATION_REMORSE_PERIOD_TIMEZONE);
		final long dayToSubstract = configurationService.getConfiguration().getLong("sfdc.product.replication.start.date.minus.days");
		List<ProductReferenceModel> productReferenceList = null;
		if(null != productReferenceCronJobModel.getProductUpdateDate()) {
			LOG.info("Product updated time has been set through job override property (productUpdateDate), filtering products for replication based on that.");
			final FlexibleSearchQuery productReferenceSearch = new FlexibleSearchQuery(PRODUCT_REFERENCE_REPLICATION_QUERY_BY_LAST_RUN_TIME);
			productReferenceSearch.addQueryParameter("modifiedTime", GPIntegrationUtils.getJobLastRunTime(productReferenceCronJobModel.getProductUpdateDate(), false, timeZone, dayToSubstract));
			SearchResult<ProductReferenceModel> productRefenceSearchResult = getFlexibleSearchService().search(productReferenceSearch);
			if(productRefenceSearchResult.getCount() > 0) {
				productReferenceList = productRefenceSearchResult.getResult();
			} else {
				LOG.info("No product references found for replication - Search by manual last runtime.");
			}
		} else if(null != productReferenceCronJobModel.getProductCode()) {
			LOG.info("ProductCode has been set through job override property (productCode), filtering products for replication based on that.");
			final FlexibleSearchQuery productReferenceSearch = new FlexibleSearchQuery(PRODUCT_REFERENCE_REPLICATION_QUERY_BY_PRODUCT_CODE);
			productReferenceSearch.addQueryParameter("productCode", productReferenceCronJobModel.getProductCode());
			SearchResult<ProductReferenceModel> productRefenceSearchResult = getFlexibleSearchService().search(productReferenceSearch);
			if(productRefenceSearchResult.getCount() > 0) {
				productReferenceList = productRefenceSearchResult.getResult();
			} else {
				LOG.info("No product references found for replication - Search by Product Code.");
			}
		} else {
			LOG.info("Filetering products for reference replication based on jobs last run time.");
			final FlexibleSearchQuery productReferenceSearch = new FlexibleSearchQuery(PRODUCT_REFERENCE_REPLICATION_QUERY_BY_LAST_RUN_TIME);
			productReferenceSearch.addQueryParameter("modifiedTime", GPIntegrationUtils.getJobLastRunTime(productReferenceCronJobModel.getStartTime(), true, timeZone, dayToSubstract));
			SearchResult<ProductReferenceModel> productRefenceSearchResult = getFlexibleSearchService().search(productReferenceSearch);
			if(productRefenceSearchResult.getCount() > 0) {
				productReferenceList = productRefenceSearchResult.getResult();
			} else {
				LOG.info("No product references found for replication - Search by default last runtime.");
			}
		}
		return productReferenceList;
	}
	
	public GPProductRelationshipRequestDTO createProductRelationRequest(List<ProductReferenceModel> productReferenceList) {
		LOG.info("Creating product relation request for create & update operation.");
		GPProductRelationshipRequestDTO productRelationRequest = new GPProductRelationshipRequestDTO();
		ProductRelationships productRelationships = new ProductRelationships();
		List<Relationship> relationshipList = new ArrayList<>();
		productReferenceList.stream().forEach(productReference ->{
			if(productReference.getActive()) {
				Relationship relationshipDTO = new Relationship();
				relationshipDTO.setMainProduct(productReference.getSource().getCode());
				relationshipDTO.setRelatedProduct(productReference.getTarget().getCode());
				relationshipDTO.setRelationshipType(productReference.getReferenceType().getCode());
				relationshipList.add(relationshipDTO);
			}
		});
		productRelationships.setRelationship(relationshipList);
		productRelationRequest.setProductRelationships(productRelationships);
		return productRelationRequest;
	}
	
	public GPProductRelationshipRequestDTO createRelationRequestForDeletion(List<ProductReferenceModel> productReferenceList) {
		LOG.info("Creating product relation request for delete operation.");
		GPProductRelationshipRequestDTO productRelationDeleteRequest = new GPProductRelationshipRequestDTO();
		ProductRelationships productRelationships = new ProductRelationships();
		List<Relationship> relationshipList = new ArrayList<>();
		productReferenceList.stream().forEach(productReference ->{
			if(!productReference.getActive()) {
				Relationship relationshipDTO = new Relationship();
				relationshipDTO.setMainProduct(productReference.getSource().getCode());
				relationshipDTO.setRelatedProduct(productReference.getTarget().getCode());
				relationshipDTO.setRelationshipType(productReference.getReferenceType().getCode());
				relationshipDTO.setDeletionFlag(GpintegrationConstants.PROD_REFERENCE_DELETION_YES);
				relationshipList.add(relationshipDTO);
			}
		});
		productRelationships.setRelationship(relationshipList);
		productRelationDeleteRequest.setProductRelationships(productRelationships);
		return productRelationDeleteRequest;
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

	public GPDefaultProductReferenceReplicationService getGpProductReferenceReplicationService() {
		return gpProductReferenceReplicationService;
	}

	public void setGpProductReferenceReplicationService(
			GPDefaultProductReferenceReplicationService gpProductReferenceReplicationService) {
		this.gpProductReferenceReplicationService = gpProductReferenceReplicationService;
	}
	
	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}
	@Override
	public boolean isAbortable() {
		return true;
	} 
}
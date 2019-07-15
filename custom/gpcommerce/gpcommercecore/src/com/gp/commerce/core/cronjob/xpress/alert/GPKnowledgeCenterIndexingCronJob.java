/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.cronjob.xpress.alert;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.util.Config;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.gp.commerce.core.dto.knowledge.center.GPKnowledgeCenterSkuResponse;
import com.gp.commerce.core.dto.knowledge.center.InfoResourcesDTO;
import com.gp.commerce.core.dto.knowledge.center.SupportResourcesDTO;
import com.gp.commerce.core.model.GPKnowledgeCenterIndexingCronJobModel;
import com.gp.commerce.core.product.dao.GPProductDao;

import reactor.util.CollectionUtils;

public class GPKnowledgeCenterIndexingCronJob extends AbstractJobPerformable<GPKnowledgeCenterIndexingCronJobModel> {

	private static final String ONLINE = "Online";

	private static final String CATALOG_VERSION = "catalogVersion";

	private static final String CATALOG_ID = "catalogId";

	private static final String MODIFIEDTIME_DATE = "modifiedtime_date";

	private static final String IS_KNOWLEDGE_CENTER_DOCUMENT = "isKnowledgeCenterDocument_boolean";

	private static final String RESOURCE_IMAGE_URL = "resourceImageUrl_string";

	private static final String CONTENT_TYPE = "contentType_string";

	private static final String ID = "id";

	private static final String LABEL = "label_string";

	private static final String DESCRIPTION = "description_en_text";

	private static final String PAGE_URL = "pageUrl_en_string";

	private static final String TITLE = "title_en_text";

	private static final String TAGS = "tags_text_mv";

	private static final String SKU = "sku_text";

	private static final String SEARCH_TERM = "searchTerms_text_mv";

	private static final Logger LOG = Logger.getLogger(GPKnowledgeCenterIndexingCronJob.class);

	private static final String ACCEPT = "Accept";
	private static final String DATE_FORMAT = "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'";
	private static final String KC_DATE_FORMAT = "yyyy-MM-dd\'T\'HH:mm:ss";
	private static final String TITLE_STRING = "pageTitle_en_string";
	private transient GPProductDao gpProductDao;

	@Override
	public PerformResult perform(final GPKnowledgeCenterIndexingCronJobModel arg0) {
		LOG.debug("GPKnowledgeCenterIndexingCronJob started with last start time: "+arg0.getSite());
		final List<SolrInputDocument> solrDocuments = new ArrayList<>();
		final List<ProductModel> products = gpProductDao.getNonObsoleteProductsForSite(arg0.getSite());
		final List<String> productCodes = products.stream().map(prod->prod.getCode()).collect(Collectors.toList());
		for(final String product : productCodes) {
			final GPKnowledgeCenterSkuResponse knowledgeDocuments = getSKUDataFromKC(product);
			if(null!=knowledgeDocuments && !CollectionUtils.isEmpty(knowledgeDocuments.getInfoResources())) {
				knowledgeDocuments.getInfoResources().stream().forEach(inforResource-> {
					solrDocuments.add(createSolrDocument(inforResource, arg0.getSite()));
				});
			}

			if(null!=knowledgeDocuments && !CollectionUtils.isEmpty(knowledgeDocuments.getSupportResources())) {
				knowledgeDocuments.getSupportResources().stream().forEach(supportResource->{
					solrDocuments.add(createSolrDocument(supportResource, arg0.getSite()));
				});
			}
		}

		if(solrDocuments.isEmpty()) {
			LOG.error("No Solr Documents");
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
		}

		final SolrClient client = getSolrClient();

		UpdateRequest updateReq = createNewSolrRequest();
		final String indexName=arg0.getSite().getSiteConfig().get("solr.core");
		//Deleting existing documents to perform a full index.
		try {
			updateReq.deleteByQuery("isKnowledgeCenterDocument_boolean:true");
			updateReq.process(client, indexName);
			updateReq.commit(client, indexName);
			}catch(SolrServerException|IOException e) {
				LOG.error("error during deleteing existing documents results", e);
				return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
			}

		updateReq = createNewSolrRequest();
		updateReq.add(solrDocuments);
		UpdateResponse response=null;

		try {
			response=updateReq.process(client, indexName);
			updateReq.commit(client, indexName);
		} catch (SolrServerException|IOException e) {
			LOG.error("error during fetching results", e);
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 * Creates a Solr client.
	 * @return the solrClient
	 */
	private HttpSolrClient getSolrClient() {
				final String solrUrl = Config.getParameter("solrServerUrl");
		return new HttpSolrClient.Builder(solrUrl)
		    .withConnectionTimeout(10000)
		    .withSocketTimeout(60000)
		    .build();
	}

	private UpdateRequest createNewSolrRequest() {
		final UpdateRequest updateReq = new UpdateRequest();

		final String userName = Config.getParameter("solr.username");
		final String password = Config.getParameter("solr.password");
		updateReq.setBasicAuthCredentials(userName, password);

		return updateReq;
	}

	/**
	 * Hits the Knowledge Center webservice and fetches documents data for the product code passed.
	 * @param sku the code of the product for which the documents are to be fetched.
	 * @return Return the response object containing document data.
	 */
	private GPKnowledgeCenterSkuResponse getSKUDataFromKC(final String sku) {
		GPKnowledgeCenterSkuResponse responseBody = null;
		ResponseEntity<GPKnowledgeCenterSkuResponse> kcResponse=null;
		final RestTemplate restTemplate = new RestTemplate();
		try {
			final String endPointUrlScpi = Config.getParameter("gp.knowledge.center.endpoint");
			final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endPointUrlScpi).queryParam("sku", sku);
			final HttpHeaders headers = new HttpHeaders();
			headers.set(ACCEPT, MediaType.APPLICATION_JSON_VALUE);
			final HttpEntity<?> entity = new HttpEntity<>(headers);
			kcResponse = restTemplate.exchange(builder.toUriString(),
					HttpMethod.GET, entity, GPKnowledgeCenterSkuResponse.class);
			LOG.debug("Response from knowledge center:" + kcResponse);
			if (null != kcResponse && null != kcResponse.getBody()) {
				responseBody = kcResponse.getBody();
				return responseBody;
			}

		} catch (final Exception e) {
			LOG.error("Exeception occured at Knowledge center API call for SKU ->"+sku, e);
		}
		return responseBody;
	}

	/**
	 * Creates a solr document on the basis of document received from knowledge center.
	 * @param resource The document received from knowledge center.
	 * @return Solr Document
	 */
	private SolrInputDocument createSolrDocument(final Object resource, final CMSSiteModel site) {
		final SolrInputDocument solrDoc = new SolrInputDocument();
		final SimpleDateFormat formatToSave = new SimpleDateFormat(DATE_FORMAT);
		final SimpleDateFormat formatForParsing = new SimpleDateFormat(KC_DATE_FORMAT);
		if(resource instanceof InfoResourcesDTO)
		{
			final InfoResourcesDTO infoResource = (InfoResourcesDTO)resource;
			solrDoc.addField(TITLE, infoResource.getResourceTitle());
			solrDoc.addField(PAGE_URL, infoResource.getResourceURL());
			solrDoc.addField(DESCRIPTION, infoResource.getResourceText());
			solrDoc.addField(LABEL, infoResource.getSku());
			solrDoc.addField(ID, infoResource.getResourceId());
			solrDoc.addField(CONTENT_TYPE, infoResource.getMediaType());
			solrDoc.addField(RESOURCE_IMAGE_URL, infoResource.getResourceImageURL());
			solrDoc.addField(IS_KNOWLEDGE_CENTER_DOCUMENT, true);
			solrDoc.addField(TITLE_STRING, infoResource.getResourceTitle());
			if(null != infoResource.getTags()){
				solrDoc.addField(TAGS, infoResource.getTags().stream().collect(Collectors.joining(",")));
			}
			solrDoc.addField(SKU, infoResource.getSku());
			if(null != infoResource.getSearchTerms()){
				solrDoc.addField(SEARCH_TERM, infoResource.getSearchTerms().stream().collect(Collectors.joining(",")));
			}
			if(null!=infoResource.getStartDate()) {
				Date date = null;
				try {
					date = formatForParsing.parse(infoResource.getStartDate());
					solrDoc.addField(MODIFIEDTIME_DATE, formatToSave.format(date));
				}catch(final ParseException e) {
					LOG.error("Error in parsing Knowledge Center Date: "+ infoResource.getStartDate(), e);
				}catch(final Exception e) {
					LOG.error("Error occurred in saving the creation date", e);
				}
			}
		}else if(resource instanceof SupportResourcesDTO)
		{
			final SupportResourcesDTO supportResource = (SupportResourcesDTO) resource;
			solrDoc.addField(TITLE, supportResource.getResourceTitle());
			solrDoc.addField(TITLE_STRING, supportResource.getResourceTitle());
			solrDoc.addField(PAGE_URL, supportResource.getResourceURL());
			solrDoc.addField(DESCRIPTION, supportResource.getResourceText());
			solrDoc.addField(LABEL, supportResource.getSku());
			solrDoc.addField(ID, supportResource.getResourceId());
			solrDoc.addField(CONTENT_TYPE, supportResource.getMediaType());
			solrDoc.addField(RESOURCE_IMAGE_URL, supportResource.getResourceImageURL());
			solrDoc.addField(IS_KNOWLEDGE_CENTER_DOCUMENT, true);
			if(null != supportResource.getTags()){
				solrDoc.addField(TAGS, supportResource.getTags().stream().collect(Collectors.joining(",")));
			}
			solrDoc.addField(SKU, supportResource.getSku());
			if(null != supportResource.getSearchTerms()){
				solrDoc.addField(SEARCH_TERM, supportResource.getSearchTerms().stream().collect(Collectors.joining(",")));
			}
			if(null!=supportResource.getStartDate()) {
				Date date = null;
				try {
					date = formatForParsing.parse(supportResource.getStartDate());
					solrDoc.addField(MODIFIEDTIME_DATE, formatToSave.format(date));
				}catch(final ParseException e) {
					LOG.error("Error in parsing Knowledge Center Date: "+ supportResource.getStartDate(), e);
				}catch(final Exception e) {
					LOG.error("Error occurred in saving the creation date", e);
				}
			}
		}

		if(!CollectionUtils.isEmpty(site.getContentCatalogs())) {
			solrDoc.addField(CATALOG_ID, site.getContentCatalogs().get(0).getId());
			solrDoc.addField(CATALOG_VERSION, ONLINE);
		}

		return solrDoc;
	}

	public GPProductDao getGpProductDao() {
		return gpProductDao;
	}

	public void setGpProductDao(final GPProductDao gpProductDao) {
		this.gpProductDao = gpProductDao;
	}

}

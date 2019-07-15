/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.hybris.ymkt.gpsapymktexpressway.services;

import de.hybris.platform.core.model.ItemModel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.batch.BatchException;
import org.apache.olingo.odata2.api.client.batch.BatchChangeSet;
import org.apache.olingo.odata2.api.client.batch.BatchPart;
import org.apache.olingo.odata2.api.client.batch.BatchSingleResponse;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.ymkt.common.http.HttpURLConnectionRequest;
import com.hybris.ymkt.common.http.HttpURLConnectionResponse;

/**
 * Extension of {@link AbstractSyncService}.
 *
 * @param <M> the generic type
 */
public abstract class Abstract1708SyncService<M extends ItemModel> extends AbstractSyncService<M> {
	private static final Logger LOG = LoggerFactory.getLogger(Abstract1708SyncService.class);

	protected abstract BatchChangeSet convertModelToChangeSet(Map<String, Object> parameters, M model)
			throws IOException, EdmException;

	@Override
	public boolean sendModels(List<M> models, Map<String, Object> parameters) {
		try {
			final String boundary = "batch";
			final URL url = this.oDataService.createURL("$batch");
			final HttpURLConnectionRequest request = new HttpURLConnectionRequest("POST", url);
			request.getRequestProperties().put("Accept", APPLICATION_JSON);
			request.getRequestProperties().put("Content-Type", "multipart/mixed;boundary=" + boundary);
			request.setReadTimeout(this.getReadTimeout());

			final List<BatchPart> batchParts = new ArrayList<>();
			for (M model : models) {
				BatchChangeSet convertModelToChangeSet = this.convertModelToChangeSet(parameters, model);
				batchParts.add(convertModelToChangeSet);
			}

			try (InputStream writeBatchRequest = EntityProvider.writeBatchRequest(batchParts, boundary)) {
				byte[] payload = this.oDataService.bufferStream(writeBatchRequest);
				LOG.debug("{}", new String(payload, StandardCharsets.UTF_8));

				final byte[] payloadGZIP = this.compressGZIP(payload);
				request.getRequestProperties().put("Content-Encoding", "gzip");
				request.setPayload(payloadGZIP);
				LOG.debug("GZIP compression {} Bytes -> {} Bytes, {}% reduction.", payload.length, payloadGZIP.length,
						(float) 100.0 * (payload.length - payloadGZIP.length) / payload.length);
			}

			final HttpURLConnectionResponse response = this.oDataService.executeWithRetry(request);
			LOG.debug("{}", new String(response.getPayload(), StandardCharsets.UTF_8));
			LOG.debug("{}", response.getHeaderFields());

			try (ByteArrayInputStream content = new ByteArrayInputStream(response.getPayload())) {
				final String contentType = response.getHeaderField("content-type").get(0);
				for (BatchSingleResponse batchResponse : EntityProvider.parseBatchResponse(content, contentType)) {
					// TODO : implement per model error handling instead of
					// returning true/false by this sendModels method.
					batchResponse.getStatusCode();
				}
			}

			return true;
		} catch (IOException | BatchException | EdmException e) {
			LOG.error("Error sending '{}' models to YMKT", models.size(), e);
			return false;
		}
	}
}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.product.dao.impl;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.product.dao.GPClassificationAttributeAssignmentDAO;

/**
 * The Class DefaultGPClassificationAttributeAssignmentDAO.
 */
public class DefaultGPClassificationAttributeAssignmentDAO implements GPClassificationAttributeAssignmentDAO
{
	/** The Constant FIND_CLASSIFICATION_ATTRIBUTE_ASSIGNMENT. */
	protected static final String FIND_CLASSIFICATION_ATTRIBUTE_ASSIGNMENT = "SELECT {caa:pk} FROM {ClassAttributeAssignment as caa JOIN ClassificationClass as cc ON {cc:pk} = {caa:classificationClass} JOIN ClassificationAttribute as ca ON {caa:classificationAttribute} = {ca:pk} JOIN ClassificationSystemVersion as csv ON {caa:systemVersion} = {csv:pk} JOIN Catalog as c ON {csv:catalog} = {c:pk}} WHERE {cc:code} = ?ClassificationClasscode AND LCASE({ca:code}) = ?ClassificationAttributecode AND {csv:version} = ?version AND {c:id} = ?id";

	/** The flexible search service. */
	private FlexibleSearchService flexibleSearchService;

	/**
	 * Gets the classification attribute assignmnent.
	 *
	 * @param catalogId the catalog id
	 * @param systemVersion the system version
	 * @param classificationClass the classification class
	 * @param attribute the attribute
	 * @return the classification attribute assignmnent
	 */
	public ClassAttributeAssignmentModel getClassificationAttributeAssignmnent(final String catalogId, final String systemVersion,
			final String classificationClass, final String attribute)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_CLASSIFICATION_ATTRIBUTE_ASSIGNMENT);
		query.addQueryParameter("id", catalogId);
		query.addQueryParameter("version", systemVersion);
		query.addQueryParameter("ClassificationClasscode", classificationClass);
		query.addQueryParameter("ClassificationAttributecode", attribute.toLowerCase());
		return (ClassAttributeAssignmentModel) this.flexibleSearchService.searchUnique(query);
	}

	/**
	 * Sets the flexible search service.
	 *
	 * @param flexibleSearchService the new flexible search service
	 */
	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}
}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.impl;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.core.product.dao.GPClassificationAttributeAssignmentDAO;
import com.gp.commerce.core.services.GPClassificationAttributeAssignmentService;


/**
 * This class is used for processing GP Classification Attribute assignment services
 */
public class DefaultGPClassificationAttributeAssignmentService implements GPClassificationAttributeAssignmentService
{
	protected static final Pattern PATTERN = Pattern.compile(
			"(?<catalogId>[^/]+)/(?<systemVersion>[^/]+)/(?<classificationClassCode>[^.]+)\\.(?<classificationAttributeCode>.+)");
	private static final Logger LOG = Logger.getLogger(DefaultGPClassificationAttributeAssignmentService.class);
	private GPClassificationAttributeAssignmentDAO gpclassificationAttributeAssignmentDAO;

	public ClassAttributeAssignmentModel findClassAttributeAssignment(final String classificationAttributeQualifier)
	{
		final Matcher matcher = PATTERN.matcher(classificationAttributeQualifier);
		if (matcher.find())
		{
			final String catalogId = matcher.group("catalogId");
			final String systemVersion = matcher.group("systemVersion");
			final String classificationClassCode = matcher.group("classificationClassCode");
			final String classificationAttributeCode = matcher.group("classificationAttributeCode");
			if (StringUtils.isNotBlank(catalogId) && StringUtils.isNotBlank(systemVersion)
					&& StringUtils.isNotBlank(classificationClassCode) && StringUtils.isNotBlank(classificationAttributeCode))
			{
				return this.findClassAttributeAssignment(catalogId, systemVersion, classificationClassCode,
						classificationAttributeCode);
			}
		}

		LOG.warn(String.format(
				"\'%s\' is not correct classification attribute qualifier. Expected pattern : {catalogId}/{systemVersion}/{classificationClassCode}.{classificationAttributeCode}",
				new Object[]
				{ classificationAttributeQualifier }));
		return null;
	}

	public ClassAttributeAssignmentModel findClassAttributeAssignment(final String catalogId, final String systemVersionId,
			final String classificationClassCode, final String attributeCode)
	{
		return this.gpclassificationAttributeAssignmentDAO.getClassificationAttributeAssignmnent(catalogId, systemVersionId,
				classificationClassCode, attributeCode);
	}

	/**
	 * @param gpclassificationAttributeAssignmentDAO
	 */
	public void setGpclassificationAttributeAssignmentDAO(
			final GPClassificationAttributeAssignmentDAO gpclassificationAttributeAssignmentDAO)
	{
		this.gpclassificationAttributeAssignmentDAO = gpclassificationAttributeAssignmentDAO;
	}

	}
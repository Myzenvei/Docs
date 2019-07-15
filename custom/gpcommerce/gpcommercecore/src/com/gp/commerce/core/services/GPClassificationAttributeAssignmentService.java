/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;


/**
 * This interface is for processing class attribute assignment
 */
public interface GPClassificationAttributeAssignmentService
{
	
	/**
	 * Returns ClassAttributeAssignment for the given qualifier.
	 *
	 * @param classificationAttributeQualifier the classification attribute qualifier
	 * @return {@link ClassAttributeAssignmentModel}
	 */
	ClassAttributeAssignmentModel findClassAttributeAssignment(String classificationAttributeQualifier);

	/**
	 * Returns ClassAttributeAssignment for the given catalog ID, system version,
	 * classification class and attribute3
	 * 
	 * @param catalogId           the catalog ID
	 * @param systemVersion       the system version
	 * @param classificationClass the classification class
	 * @param attribute3          the attribute
	 * @return {@link ClassAttributeAssignmentModel}
	 */
	ClassAttributeAssignmentModel findClassAttributeAssignment(String catalogId, String systemVersion, String classificationClass,
			String attribute3);
}
/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.product.dao;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;


/**
 *
 */
public interface GPClassificationAttributeAssignmentDAO
{
	/**
	 * @param catalogId
	 * @param systemVersion
	 * @param classificationClass
	 * @param attribute
	 * @return
	 */
	ClassAttributeAssignmentModel getClassificationAttributeAssignmnent(String catalogId, String systemVersion,
			String classificationClass, String attribute);
}
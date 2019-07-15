/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service;

import com.gpintegration.knowledgecenter.dto.GPKnowledgeCenterSkuResponse;

/**
 *  Interface for Knowledge center service.
 */
public interface GPKnowledgeCenterService {
	
	/**
	 * This method gets SKU data from KC based on sku.
	 *
	 * @param sku the sku
	 * @return sku response
	 */
	GPKnowledgeCenterSkuResponse getSKUDataFromKC(String sku);
}

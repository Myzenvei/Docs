/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.strategies;

import org.apache.log4j.Logger;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.promotionengine.impl.DefaultPromotionEngineService;

/**
 * The Class GPDefaultPromotionEngineService.
 */
public class GPDefaultPromotionEngineService extends DefaultPromotionEngineService{
	
	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(GPDefaultPromotionEngineService.class);

	/**
	 * Cleanup abstract order.
	 *
	 * @param cart the cart
	 */
	@Override
	protected void cleanupAbstractOrder(AbstractOrderModel cart) {
        try {
	        super.cleanupAbstractOrder(cart);
        }
        catch(Exception e) {
        	LOG.error("Exception in Promotion rule engine", e);
        }
  }

		
}

/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gpsaporderexchange.outbound.impl;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.sap.orderexchange.outbound.impl.AbstractRawItemBuilder;

import org.apache.log4j.Logger;


/**
 * @author Siddharth Jain
 *
 */
public class GPRawHybrisOrderBuilder extends AbstractRawItemBuilder<ConsignmentModel>
{
	private static final Logger LOG = Logger.getLogger(GPRawHybrisOrderBuilder.class);

	@Override
	protected Logger getLogger()
	{
		return LOG;
	}

}
/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import org.springframework.util.Assert;

import de.hybris.platform.commercefacades.order.converters.populator.ConsignmentPopulator;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public class GPConsignmentPopulator extends ConsignmentPopulator {

	@Override
	public void populate(final ConsignmentModel source, final ConsignmentData target)
	{
		super.populate(source, target);
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		if(source.getProcessingStatus() != null)
		{
			target.setProcessingStatus(source.getProcessingStatus());
		}
	}
	
}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bcommercefacades.company.converters.populators.B2BUnitPopulator;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;


/**
 * GP Unit Populator
 */
public class GPB2BUnitPopulator extends B2BUnitPopulator
{

	@Override
	public void populate(final B2BUnitModel source, final B2BUnitData target)
	{
		super.populate(source, target);
		target.setRole(source.getRole());
		target.setNoOfEmployees(source.getNoOfEmployees());
		target.setTypeOfBusiness(source.getTypeOfBusiness());
	}
}

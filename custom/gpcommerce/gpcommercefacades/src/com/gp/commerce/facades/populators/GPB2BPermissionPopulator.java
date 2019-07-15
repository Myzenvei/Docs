/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.facades.populators;

import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.b2bapprovalprocessfacades.company.converters.populators.B2BPermissionPopulator;
import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionData;
import com.gp.commerce.core.util.GPSiteConfigUtils;


public class GPB2BPermissionPopulator extends B2BPermissionPopulator
{
	@Override
	public void populate(final B2BPermissionModel source, final B2BPermissionData target)
	{
		super.populate(source, target);

		if (null != target.getValue())
		{
			target.setFormattedValue(GPSiteConfigUtils.getDecimalFormatValue(target.getValue()));
		}
	}
}

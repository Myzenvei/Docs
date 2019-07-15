/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.setup;

import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupParameter;

import java.util.List;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;


/**
 * This class provides hooks into the system's initialization and update processes.
 */
@SystemSetup(extension = GpcommerceCoreConstants.EXTENSIONNAME)
public abstract class GPAbstractSystemSetup extends AbstractSystemSetup
{

	/**
	 * Helper method for checking setting of a multi-select setup parameter.
	 *
	 * @param key
	 * @param label
	 * @param sites
	 * @return SystemSetupParameter
	 */
	protected SystemSetupParameter createMultiSelectSystemSetupParameter(final String key, final String label,
			final List<String> stores)
	{
		final SystemSetupParameter syncProductsParam = new SystemSetupParameter(key);
		syncProductsParam.setLabel(label);
		syncProductsParam.setMultiSelect(true);
		syncProductsParam.addValues(stores.toArray(new String[stores.size()]));
		return syncProductsParam;
	}
}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.savedvalus.dao;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.hmc.model.SavedValuesModel;

import java.util.List;


public interface GPSavedValuesDao
{

	/**
	 * This method returns the list of saved instances or changed values at different instances for the specified item
	 * Model.
	 * 
	 * @param itemModel
	 *           the item model for which the changed value list is needed
	 * @return the list of saved values
	 */
	List<SavedValuesModel> getChangedLogs(final ItemModel itemModel);

}

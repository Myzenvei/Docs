/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.dao;

import de.hybris.platform.core.model.order.CartEntryModel;

import java.util.List;

public interface GPBundleDao {

    List<CartEntryModel> getOrderEntriesByBundleNo(int bundleNo, String cartId);
}

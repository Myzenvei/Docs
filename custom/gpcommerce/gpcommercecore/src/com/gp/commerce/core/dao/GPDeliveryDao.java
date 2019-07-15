/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.dao;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;

import java.util.List;

public interface GPDeliveryDao {


	List<ZoneDeliveryModeValueModel> getDeliveryModeValueList(CurrencyModel curr, CountryModel country,
			DeliveryModeModel deliveryMode, double totalPrice) throws Exception;

}

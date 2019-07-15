/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordermanagementfacades.cancellation.data.OrderCancelEntryData;
import de.hybris.platform.ordermanagementfacades.cancellation.data.OrderCancelRequestData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.user.UserService;

public class GPOrderCancelRequestPopulator implements Populator<OrderModel, OrderCancelRequestData>{

	@Resource(name = "userService")
	private UserService userService;
	
	@Override
	public void populate(OrderModel source, OrderCancelRequestData target) throws ConversionException {
		target.setOrderCode(source.getCode());
		target.setEntries(prepareOrderCancelEntryData(source));
		target.setUserId(getUserService().getCurrentUser().getUid());
	}
	
	private List<OrderCancelEntryData> prepareOrderCancelEntryData(final OrderModel order)
	{
		final Map<Integer, Integer> cancelEntryQuantityMap = order.getEntries().stream().collect(Collectors.toMap(entry -> entry.getEntryNumber(), entry->entry.getQuantity().intValue()));
		final List<OrderCancelEntryData> result = new ArrayList<>();

		cancelEntryQuantityMap.forEach((entryNum, cancelQty) -> {
			final OrderCancelEntryData orderCancelEntryData = new OrderCancelEntryData();
			orderCancelEntryData.setOrderEntryNumber(entryNum);
			orderCancelEntryData.setCancelQuantity(Long.valueOf(cancelQty));
			orderCancelEntryData.setCancelReason(CancelReason.CUSTOMERREQUEST);
			result.add(orderCancelEntryData);
		});
		return result;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
}

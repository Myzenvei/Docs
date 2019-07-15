/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.strategies.impl;

import java.util.Optional;

import com.gp.commerce.core.model.WishlistProcessModel;

import de.hybris.platform.acceleratorservices.process.strategies.impl.AbstractProcessContextStrategy;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

public class ShareWishlistProcessContextStrategy extends AbstractProcessContextStrategy
{
	@Override
	public BaseSiteModel getCmsSite(final BusinessProcessModel businessProcessModel)
	{
		ServicesUtil.validateParameterNotNull(businessProcessModel, BUSINESS_PROCESS_MUST_NOT_BE_NULL_MSG);

		return Optional.of(businessProcessModel)
				.filter(businessProcess -> businessProcess instanceof WishlistProcessModel)
				.map(businessProcess -> ((WishlistProcessModel) businessProcess).getSite())
				.orElse(null);
	}

	@Override
	protected CustomerModel getCustomer(final BusinessProcessModel businessProcess)
	{
		return Optional.of(businessProcess)
				.filter(bp -> bp instanceof WishlistProcessModel)
				.map(bp -> ((CustomerModel) ((WishlistProcessModel) businessProcess).getWishlist().getUser()))
				.orElse(null);
	}
}

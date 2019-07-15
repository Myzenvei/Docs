/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.subscription.dao;

import java.util.List;

import com.gp.commerce.core.model.GPSubscriptionCartModel;
import de.hybris.platform.core.model.user.UserModel;

public interface GPSubscriptionDao {
	List<GPSubscriptionCartModel> cancelSubscription(String code);
	
	List<GPSubscriptionCartModel> getActiveSubscriptions(UserModel currentUser) ;
	
	public List<GPSubscriptionCartModel> getSubscriptionCartModels(String subscriptionCartStatus, boolean isActive);

	List<GPSubscriptionCartModel> getInActiveSubscriptions(UserModel currentUser);
}

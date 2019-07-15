/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.suites;


import com.gp.actions.SendOrderToDataHubActionTest;
import com.gp.actions.SetCompletionStatusActionTest;
import com.gp.actions.SetConfirmationStatusActionTest;
import com.gp.actions.UpdateERPOrderStatusActionTest;
import com.gp.jobs.OrderCancelRepairJobTest;
import com.gp.jobs.OrderExchangeRepairJobTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@SuppressWarnings("javadoc")
@RunWith(Suite.class)
@SuiteClasses(
{ UpdateERPOrderStatusActionTest.class, SetConfirmationStatusActionTest.class, SendOrderToDataHubActionTest.class,
		SetCompletionStatusActionTest.class, OrderExchangeRepairJobTest.class, OrderCancelRepairJobTest.class })
public class UnitTestSuite
{
	// Intentionally left blank
}

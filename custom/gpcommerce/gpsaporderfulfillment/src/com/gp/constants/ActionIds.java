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
package com.gp.constants;

/**
 * IDs of Actions as defined in order process
 * 
 */
public final class ActionIds
{
	@SuppressWarnings("javadoc")
	public static final String WAIT_FOR_GOODS_ISSUE = "waitForGoodsIssue";

	@SuppressWarnings("javadoc")
	public static final String WAIT_FOR_CONSIGNMENT_CREATION = "waitForConsignmentCreation";

	@SuppressWarnings("javadoc")
	public static final String WAIT_FOR_ERP_CONFIRMATION = "waitForERPConfirmation";

	@SuppressWarnings("javadoc")
	public static final String SET_CANCEL_STATUS = "setCancelStatus";

	@SuppressWarnings("javadoc")
	public static final String SEND_ORDER_AS_IDOC = "sendOrderToErp";

}

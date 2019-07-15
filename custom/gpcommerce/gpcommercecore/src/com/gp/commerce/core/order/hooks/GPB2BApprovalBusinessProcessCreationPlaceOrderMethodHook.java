/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.order.hooks;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.order.hooks.B2BApprovalBusinessProcessCreationPlaceOrderMethodHook;
import de.hybris.platform.b2b.strategies.BusinessProcessStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
/**
 * Overriding business process for pending approval case
 */
public class GPB2BApprovalBusinessProcessCreationPlaceOrderMethodHook
		extends B2BApprovalBusinessProcessCreationPlaceOrderMethodHook
{

	private static final Logger LOG = Logger.getLogger(GPB2BApprovalBusinessProcessCreationPlaceOrderMethodHook.class);

	private BusinessProcessStrategy businessProcessCreationStrategy;

	@Override
	public void afterPlaceOrder(final CommerceCheckoutParameter commerceCheckoutParameter, final CommerceOrderResult commerceOrderResult)
	{
		LOG.info("#######Inside GPB2BApprovalBusinessProcessCreationPlaceOrderMethodHook ######");

		//Not implementing OOTB hook strategies for B2B Orders

	}

	@Override
	public void beforePlaceOrder(final CommerceCheckoutParameter commerceCheckoutParameter)
	{
		// not implemented
	}

	@Override
	public void beforeSubmitOrder(final CommerceCheckoutParameter commerceCheckoutParameter, final CommerceOrderResult commerceOrderResult)
	{
		// not implemented
	}

	@Override
	protected boolean isB2BContext(final AbstractOrderModel order)
	{
		if (order != null && order.getUser() != null)
		{
			return order.getUser() instanceof B2BCustomerModel;
		}
		else
		{
			return false;
		}
	}

	
	@Required
	@Override
	public void setBusinessProcessCreationStrategy(final BusinessProcessStrategy businessProcessCreationStrategy)
	{
		this.businessProcessCreationStrategy = businessProcessCreationStrategy;
	}

	@Override
	protected BusinessProcessStrategy getBusinessProcessCreationStrategy()
	{
		return businessProcessCreationStrategy;
	}

}

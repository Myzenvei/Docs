/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.cockpits.ticket.email.context;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ticket.email.context.CustomerTicketContext;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;


public class AcceleratorCustomerTicketContext extends CustomerTicketContext
{

	public AcceleratorCustomerTicketContext(final CsTicketModel ticket, final CsTicketEventModel event)
	{
		super(ticket, event);
	}

	@Override
	public String getTo()
	{
		if (getTicket().getCustomer() instanceof CustomerModel)
		{
			final CustomerModel customer = (CustomerModel) getTicket().getCustomer();
			return customer.getOriginalUid();
		}
		return super.getTo();
	}
}

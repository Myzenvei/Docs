/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.fulfilmentprocess.ticket.strategies.impl;

import de.hybris.platform.ticket.email.context.AbstractTicketContext;
import de.hybris.platform.ticket.enums.CsEmailRecipients;
import de.hybris.platform.ticket.events.model.CsTicketEmailModel;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketEventEmailConfigurationModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.strategies.impl.DefaultTicketEventEmailStrategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;


public class GPTicketEventEmailStrategy extends DefaultTicketEventEmailStrategy{
	private static final Logger LOG = Logger.getLogger(GPTicketEventEmailStrategy.class);

	@Override
	public void sendEmailsForAssignAgentTicketEvent(CsTicketModel ticket, CsTicketEventModel event,
			CsEmailRecipients recepientType) {
		List filteredConfigurations = this.getApplicableConfigs(event, recepientType);
		if (filteredConfigurations.isEmpty()) {

			if(LOG.isDebugEnabled()){
				LOG.debug("No email events found for type [" + this.getTicketEventCommentTypeString(event) + "]");
			}
		} else {
			Iterator arg5 = filteredConfigurations.iterator();
		

		while (arg5.hasNext()) {
			CsTicketEventEmailConfigurationModel config = (CsTicketEventEmailConfigurationModel) arg5.next();

			AbstractTicketContext ticketContext = this.createContextForEvent(config, ticket, event);
			if (ticketContext != null &&  !config.getRecipientType().equals(CsEmailRecipients.CUSTOMER)) {
			
				CsTicketEmailModel email = this.constructAndSendEmail(ticketContext, config);
				if (email != null) {

					ArrayList emails = new ArrayList();
					emails.addAll(event.getEmails());
					emails.add(email);
					event.setEmails(emails);
				}
			}
		}

		this.getModelService().save(event);
		}
	}
	
	@Override
	protected CsTicketEmailModel constructAndSendEmail(AbstractTicketContext ticketContext,
			CsTicketEventEmailConfigurationModel config) {
		if(LOG.isDebugEnabled()){
			LOG.debug(" sending email to [" + config.getRecipientType() + "]. Context was [" + ticketContext + "]");
		}

		if(!config.getRecipientType().equals(CsEmailRecipients.CUSTOMER)) {
			return super.constructAndSendEmail(ticketContext, config);
		}
		return null;
	}

}

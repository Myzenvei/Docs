/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.process.email.context;
//com.gp.commerce.facades.process.email.context.GPCSROrderApprovalEmailContext
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.site.strategies.SiteChannelValidationStrategy;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;

public class GPCSROrderApprovalEmailContext extends OrderNotificationEmailContext {
	
	private static final String CSR_ORDER_NOTIFY_EMAIL = "CSR_ORDER_NOTIFY_EMAIL";
	private static final String DELIMITER = ",";
	private static final String CSR_EMAIL="csr.email";
	public static final String ORDER_FRAUD_TYPE="orderFraudType";
	private static final Logger LOG = Logger.getLogger(GPCSROrderApprovalEmailContext.class);
	
	
	@Resource(name = "emailService")
	private EmailService emailService;
	
	private SiteChannelValidationStrategy siteChannelValidationStrategy;

	
	
	@Override
	public void init(final OrderProcessModel businessProcessModel, final EmailPageModel emailPageModel)
	{

		super.init(businessProcessModel, emailPageModel);
		
		final String csrEmail = getConfigurationService().getConfiguration().getString(CSR_EMAIL);
		if(LOG.isDebugEnabled()) {
			LOG.debug("FailedOrderContext | CSR email is configured to email address : " +csrEmail);
		}
		final List<EmailAddressModel> toEmailList = new ArrayList<>();
		EmailAddressModel toCsrEmail;
		if(StringUtils.isNotBlank(csrEmail))
		{
			if(StringUtils.contains(csrEmail, DELIMITER))
			{
				final String[] csrRecipientEmails = StringUtils.split(csrEmail, DELIMITER);
				for (final String csrRecipient : csrRecipientEmails)
				{
					toCsrEmail= getEmailService().getOrCreateEmailAddressForEmail(csrRecipient, StringUtils.EMPTY);
					toEmailList.add(toCsrEmail);
				}
			}
			else
			{
				toCsrEmail= getEmailService().getOrCreateEmailAddressForEmail(csrEmail, StringUtils.EMPTY);
				toEmailList.add(toCsrEmail);
			}
		}
		if(null!=businessProcessModel.getFraudType())
		{
			put(ORDER_FRAUD_TYPE,businessProcessModel.getFraudType());
		}else
		{
			put(ORDER_FRAUD_TYPE,"");
		}
		put(EMAIL,csrEmail);
		put(CSR_ORDER_NOTIFY_EMAIL, true);
		
	}



	public SiteChannelValidationStrategy getSiteChannelValidationStrategy() {
		return siteChannelValidationStrategy;
	}



	public void setSiteChannelValidationStrategy(SiteChannelValidationStrategy siteChannelValidationStrategy) {
		this.siteChannelValidationStrategy = siteChannelValidationStrategy;
	}



	public EmailService getEmailService() {
		return emailService;
	}



	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	
	
	
	
}

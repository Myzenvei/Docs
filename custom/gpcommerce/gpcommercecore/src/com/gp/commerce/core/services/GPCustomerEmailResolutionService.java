/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import org.springframework.beans.factory.annotation.Required;
import de.hybris.platform.site.BaseSiteService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerEmailResolutionService;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.mail.MailUtils;

/**
 * This class is used for processing Email resoulution services
 */
public class GPCustomerEmailResolutionService extends DefaultCustomerEmailResolutionService{
	
	private ConfigurationService configurationService;
	private BaseSiteService baseSiteService;
	
	private static final Logger LOG = Logger.getLogger(GPCustomerEmailResolutionService.class);
	private static final String BASESITE_DELIMITER = "gpcommercewebservices.user.delimiter";

	/* (non-Javadoc)
	 * @see de.hybris.platform.commerceservices.customer.impl.DefaultCustomerEmailResolutionService#validateAndProcessEmailForCustomer(de.hybris.platform.core.model.user.CustomerModel)
	 * validate the customer email
	 */
	@Override
	public String validateAndProcessEmailForCustomer(final CustomerModel customerModel) {
		validateParameterNotNullStandardMessage("customerModel", customerModel);
		String email = null;
		if(null != getBaseSiteService() && null != getBaseSiteService().getCurrentBaseSite() && ("gpxpress".equalsIgnoreCase(getBaseSiteService().getCurrentBaseSite().getUid())))
		{
			email = customerModel instanceof B2BCustomerModel ? ((B2BCustomerModel) customerModel).getEmail() : email;
		}
		else
		{
		String uidDelimiter = configurationService.getConfiguration().getString(BASESITE_DELIMITER);
		if (CustomerType.REGISTERED.equals(customerModel.getType())) {
			email = StringUtils.substringBefore(customerModel.getOriginalUid(), uidDelimiter);
		}
		else if (CustomerType.GUEST.equals(customerModel.getType())) {
			email = StringUtils.substringAfter(customerModel.getUid(), uidDelimiter);
		} 
		}
		try {
			if(StringUtils.isNotEmpty(email)){
				MailUtils.validateEmailAddress(email, "customer email");
				return email;
			}
			
		} catch (final EmailException e) // NOSONAR
		{
			LOG.error("Given uid is not appropriate email. Customer PK: " + String.valueOf(customerModel.getPk()), e);
		}
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see de.hybris.platform.commerceservices.customer.impl.DefaultCustomerEmailResolutionService#getEmailForCustomer(de.hybris.platform.core.model.user.CustomerModel)
	 * validate the customer email using UID
	 */
	@Override
	public String getEmailForCustomer(final CustomerModel customerModel)
	{
		final String emailAfterProcessing = validateAndProcessEmailForCustomer(customerModel);
		if (StringUtils.isNotEmpty(emailAfterProcessing))
		{
			return emailAfterProcessing;
		}
		
		return customerModel.getUid();
	}
	
	@Override
    public ConfigurationService getConfigurationService()
    {
        return configurationService;
    }

    @Required
    public void setConfigurationService(final ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
    
    protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}
}

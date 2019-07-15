/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service.impl;

import com.gpintegration.service.GPQuplesTokenService;
import com.gpintegration.utils.GPHeaderHandlerResolver;

import java.util.List;

import javax.xml.ws.WebServiceException;
import com.gpintegration.quples.*;
import com.gpintegration.exception.GPIntegrationException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.apache.log4j.Logger; 


import de.hybris.platform.commercefacades.user.data.QuplesData;

/**
 * Default implementation of {@link GPQuplesTokenService}
 */
public class GPDefaultQuplesTokenService implements GPQuplesTokenService {
	private static final String QUPLES= "QUPLES";
	private ConfigurationService configurationService;
	private static final Logger LOG = Logger.getLogger(GPDefaultQuplesTokenService.class); 

	@Override
	public QuplesData getQuplesToken(QuplesData quplesData) throws GPIntegrationException{

		
		try {
			CMMConsumer rts = getRealTimeServices();
			final ObjectFactory objFactory = new ObjectFactory();
			final Contacts contact = objFactory.createContacts();
			final NewOperation newOperation = objFactory.createNewOperation();
			String email=quplesData.getEmail();

			if (email != null) {
				contact.setEmail(email);
			}
			
			newOperation.getContactRequest().add(contact);
			List<ContactsResponse> contactsResponseList = rts.newOperation(newOperation.getContactRequest());

			if (contactsResponseList != null && !contactsResponseList.isEmpty() && contactsResponseList.size()<2) {
				for(ContactsResponse tempResponse: contactsResponseList){
					quplesData.setContactKey(tempResponse.getContactKey());
					quplesData.setPermission(tempResponse.getMKTPermission());
				}

			}

		} catch (final WebServiceException wse) {
			LOG.error(" Error in GetQuples Token " + wse.getMessage(),wse);
			throw new GPIntegrationException(GPIntegrationException.QUPLES_ERROR + ":" +wse.getMessage());
		} catch (final Exception ex) {
			LOG.error(" Error in GetQuples Token " + ex.getMessage(),ex);
			throw new GPIntegrationException(GPIntegrationException.QUPLES_ERROR + ":" +ex.getMessage());
		}

		return quplesData;

	}
	
	public CMMConsumer getRealTimeServices() {
		final CMMConsumer_Service dss = new CMMConsumer_Service();
		final GPHeaderHandlerResolver gpHandlerResolver = new GPHeaderHandlerResolver(null,QUPLES);
		dss.setHandlerResolver(gpHandlerResolver);
		return dss.getCMMConsumerSOAP();
	}
	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.core.event;

import java.util.Collection;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.service.impl.GPDefaultCommerceSCPIService;
import com.gpintegration.user.impl.GPAddCustomerToSfdcResponse;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.AfterSaveEvent;
import de.hybris.platform.tx.AfterSaveListener;

public class CustomerRegDataSynchEventListener implements AfterSaveListener, ClusterAwareEvent {

	@Resource(name = "modelService")
	private transient ModelService modelService;

	@Resource
	private transient ConfigurationService configurationService;

	private GPDefaultCommerceSCPIService gpCommerceSCPIService;
	private static final Logger LOG = Logger.getLogger(CustomerRegDataSynchEventListener.class);

	private void addCustomerUsingSCPI(final CustomerModel customerModel) {
		Integer retryCount = customerModel.getAddToSfdcRetryCount() != null? customerModel.getAddToSfdcRetryCount() : 0;
		if(retryCount < configurationService.getConfiguration().getInt(GpintegrationConstants.SCPI_CUSTOMER_RETRY_MAX_COUNT)) {
			GPAddCustomerToSfdcResponse gpAddCustomerToSfdcResponse = getGpCommerceSCPIService()
					.addCustomerToSfdc(customerModel);
			if (null == gpAddCustomerToSfdcResponse) {
				LOG.debug("SCPI service not available");
				customerModel.setAddToSfdcStatus(false);
				retryCount++;
				// 502 is if service is not available
				customerModel.setAddToSfdcErrorCode(GpintegrationConstants.SERVICE_UNAVAILABLE_ERROR_CODE);
			} else {
				if (!gpAddCustomerToSfdcResponse.getContactResponse().isStatus()) {
					retryCount++;
					LOG.error("Error Code received from SCPI:"
							+ gpAddCustomerToSfdcResponse.getContactResponse().getErrorCode());
					LOG.error("Error Msg received from SCPI:"
							+ gpAddCustomerToSfdcResponse.getContactResponse().getErrorMessage());
					customerModel.setAddToSfdcStatus(false);
					customerModel.setAddToSfdcErrorCode(gpAddCustomerToSfdcResponse.getContactResponse().getErrorCode());
				} else {
					LOG.info("Customer Data updated to SFDC sucessfully");
					customerModel.setAddToSfdcStatus(true);
					customerModel.setAddToSfdcContactId(gpAddCustomerToSfdcResponse.getContactResponse().getContactId());
				}
			}
			customerModel.setAddToSfdcRetryCount(retryCount);
			modelService.save(customerModel);
		} else {
			LOG.error(" SFDC Customer replication : Unable to replicate as customer  has exceeded max retry - "+ customerModel.getUid() );
		}
	}

	public GPDefaultCommerceSCPIService getGpCommerceSCPIService() {
		return gpCommerceSCPIService;
	}

	public void setGpCommerceSCPIService(GPDefaultCommerceSCPIService gpCommerceSCPIService) {
		this.gpCommerceSCPIService = gpCommerceSCPIService;
	}

	@Override
	public boolean publish(int arg0, int arg1) {
		return true;
	}

	@Override
	public void afterSave(Collection<AfterSaveEvent> events) {
		if (configurationService.getConfiguration().getBoolean(GpintegrationConstants.SCPI_FEATURE)) {
			try {
				for (final AfterSaveEvent event : events) {
					final int type = event.getType();

					if ((AfterSaveEvent.UPDATE == type) || ((AfterSaveEvent.CREATE == type))) {
						if ((modelService.get(event.getPk()) instanceof CustomerModel)) {
							final CustomerModel customerModel = (CustomerModel) modelService.get(event.getPk());
							if(gpCommerceSCPIService.isDataReplicationNeeded(customerModel))
							{
								addCustomerUsingSCPI(customerModel);
							} else {
									LOG.error("SFDC Contact replication failed validation: Invalid customer id" +customerModel.getCustomerID() + "or duplicate record  " + customerModel.getAddToSfdcErrorCode());
								}
							}
						}
					}
			} catch (Exception e) {
				LOG.error("Error in response from SCPI" + e.getMessage(),e);
			}
		} else {
			LOG.debug("Add customer to SFDC is not enabled");
		}
	}

}

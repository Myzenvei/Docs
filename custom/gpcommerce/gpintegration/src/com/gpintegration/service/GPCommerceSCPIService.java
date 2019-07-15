/**
 *
 */
package com.gpintegration.service;

import de.hybris.platform.core.model.user.CustomerModel;

import com.gpintegration.user.impl.GPAddCustomerToSfdcResponse;

/**
 * The Interface GPCommerceSCPIService.
 *
 * @author vdannina
 */
public interface GPCommerceSCPIService {
	
	/**
	 * Adds customer to sdfc.
	 *
	 * @param customerModel the customer model
	 * @return GPAddCustomerToSfdcResponse
	 */
	GPAddCustomerToSfdcResponse addCustomerToSfdc(CustomerModel customerModel);
	
	
	/**
	 * Checks if is data replication needed.
	 *
	 * @param customerModel the customer model
	 * @return true, if is data replication needed
	 */
	boolean  isDataReplicationNeeded(CustomerModel customerModel);


}

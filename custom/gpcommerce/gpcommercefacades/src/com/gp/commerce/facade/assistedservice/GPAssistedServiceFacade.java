/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facade.assistedservice;

import java.util.Map;

import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceException;
import de.hybris.platform.core.model.user.UserModel;

/**
 * The Interface GPAssistedServiceFacade.
 */
public interface GPAssistedServiceFacade extends AssistedServiceFacade{
	
	/**
	 * Logs in Assisted Service agent using provided credentials.
	 *
	 * @param username            uid
	 * @throws AssistedServiceException            In case of bad credentials
	 */
	void loginAssistedServiceAgent(final String username) throws AssistedServiceException;
	
	/**
	 * Verify that provided user participate in a parent AS agent group.
	 *
	 * @param asmAgent the asm agent
	 * @return true when he is
	 */
	boolean isGPAssistedServiceAgent(final UserModel asmAgent);

	/**
	 * ASM attributes along with cartId will be added to a map.
	 *
	 * @param cartId the cart id
	 * @return Map of asm attributes
	 */
	Map<String, Object> getAssistedServiceSessionAttributes(final String cartId);

}

/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facade.assistedservice;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import de.hybris.platform.assistedservicefacades.constants.AssistedservicefacadesConstants;
import de.hybris.platform.assistedservicefacades.impl.DefaultAssistedServiceFacade;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceAgentBadCredentialsException;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceAgentBlockedException;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceException;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.impl.DefaultCartService;
import de.hybris.platform.servicelayer.exceptions.ClassMismatchException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.util.Config;

/**
 * The Class GPDefaultAssistedServiceFacade.
 */
public class GPDefaultAssistedServiceFacade extends DefaultAssistedServiceFacade implements GPAssistedServiceFacade{
	
	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(GPDefaultAssistedServiceFacade.class);

	/** The Constant SESSION_CART_KEY. */
	private static final String SESSION_CART_KEY = "cart";
	
	/** The Constant AGENT_TIMER_KEY. */
	private static final String AGENT_TIMER_KEY = "agentTimer";
	
	/** The Constant CREATE_DISABLED_KEY. */
	private static final String CREATE_DISABLED_KEY = "createDisabled";
	
	/** The Constant EMULATE_BY_ORDER_KEY. */
	private static final String EMULATE_BY_ORDER_KEY = "emulateByOrder";
	
	/** The Constant ASM_ERROR_MESSAGE_KEY. */
	private static final String ASM_ERROR_MESSAGE_KEY = "asm_message";
	
	/** The Constant ASM_ERROR_MESSAGE_ARGS_KEY. */
	private static final String ASM_ERROR_MESSAGE_ARGS_KEY = "asm_message_args";
	
	/** The Constant ASM_AGENT_STORE. */
	private static final String ASM_AGENT_STORE = "asm_agent_store";

	/**
	 * Login assisted service agent.
	 *
	 * @param username the username
	 * @throws AssistedServiceException the assisted service exception
	 */
	public void loginAssistedServiceAgent(final String username) throws AssistedServiceException {
		try
		{
			final UserModel agent = getUserService().getUserForUID(username, EmployeeModel.class);

			if (agent.isLoginDisabled()) {
				throw new AssistedServiceAgentBlockedException("Account was blocked.");
			}

			if (getAsmSession() == null) {
				launchAssistedServiceMode();
			}

			loginAssistedServiceAgent(agent);
			LOG.info(String.format("Agent [%s] has been loged in using GP cookie", agent.getUid()));
		}
		catch (final UnknownIdentifierException | ClassMismatchException e)
		{
			LOG.debug(e.getMessage(), e);
			throw new AssistedServiceAgentBadCredentialsException("Unknown user id.");
		}
	
	}

	/**
	 * Checks if is GP assisted service agent.
	 *
	 * @param asmAgent the asm agent
	 * @return true, if is GP assisted service agent
	 */
	@Override
	public boolean isGPAssistedServiceAgent(UserModel asmAgent) {
		return isAssistedServiceAgent(asmAgent);
	}
	
	/**
	 * Gets the assisted service session attributes.
	 *
	 * @param cartId the cart id
	 * @return the assisted service session attributes
	 */
	@Override
	public Map<String, Object> getAssistedServiceSessionAttributes(final String cartId)
	{
		// Populate model with params to be displayed
		final Map<String, Object> attributes = new HashMap();
		attributes.putAll(getAsmSession().getAsmSessionParametersMap());
		if(cartId != null) {
		attributes.put(SESSION_CART_KEY,
					getSessionService().getCurrentSession().getAttribute(cartId));
		} else {
			attributes.put(SESSION_CART_KEY,
					getSessionService().getCurrentSession().getAttribute(DefaultCartService.SESSION_CART_PARAMETER_NAME));
		}
		attributes.put(AGENT_TIMER_KEY, String.valueOf(getAssistedServiceSessionTimerValue()));
		attributes.put(CREATE_DISABLED_KEY, Config.getParameter(AssistedservicefacadesConstants.CREATE_DISABLED_PROPERTY));
		attributes.put(EMULATE_BY_ORDER_KEY, Config.getParameter(AssistedservicefacadesConstants.EMULATE_BY_ORDER_PROPERTY));
		attributes.put(ASM_AGENT_STORE, getAssistedServiceAgentStore(getAsmSession().getAgent()));

		final String errorMessage = getAsmSession().getFlashErrorMessage();
		if (errorMessage != null)
		{
			attributes.put(ASM_ERROR_MESSAGE_KEY, errorMessage);
			attributes.put(ASM_ERROR_MESSAGE_ARGS_KEY, getAsmSession().getFlashErrorMessageArgs());
		}
		return attributes;
	}
	
}

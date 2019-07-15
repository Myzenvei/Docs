/*
 *  
 * [y] hybris Platform
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.contextualattributevalues.jalo;

import de.hybris.platform.contextualattributevalues.constants.ContextualattributevaluesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class ContextualattributevaluesManager extends GeneratedContextualattributevaluesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ContextualattributevaluesManager.class.getName() );
	
	public static final ContextualattributevaluesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ContextualattributevaluesManager) em.getExtension(ContextualattributevaluesConstants.EXTENSIONNAME);
	}
	
}

/*
 *  
 * [y] hybris Platform
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.contextualattributevaluesbackoffice.jalo;

import de.hybris.contextualattributevaluesbackoffice.constants.ContextualattributevaluesbackofficeConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class ContextualattributevaluesbackofficeManager extends GeneratedContextualattributevaluesbackofficeManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ContextualattributevaluesbackofficeManager.class.getName() );
	
	public static final ContextualattributevaluesbackofficeManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ContextualattributevaluesbackofficeManager) em.getExtension(ContextualattributevaluesbackofficeConstants.EXTENSIONNAME);
	}
	
}

package com.gp.commerce.initialdata.jalo;

import com.gp.commerce.initialdata.constants.GpcommerceInitialDataConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class GpcommerceInitialDataManager extends GeneratedGpcommerceInitialDataManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( GpcommerceInitialDataManager.class.getName() );
	
	public static final GpcommerceInitialDataManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (GpcommerceInitialDataManager) em.getExtension(GpcommerceInitialDataConstants.EXTENSIONNAME);
	}
	
}

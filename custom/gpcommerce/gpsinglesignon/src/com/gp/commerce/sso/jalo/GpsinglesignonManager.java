package com.gp.commerce.sso.jalo;

import com.gp.commerce.sso.constants.GpsinglesignonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class GpsinglesignonManager extends GeneratedGpsinglesignonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( GpsinglesignonManager.class.getName() );
	
	public static final GpsinglesignonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (GpsinglesignonManager) em.getExtension(GpsinglesignonConstants.EXTENSIONNAME);
	}
	
}

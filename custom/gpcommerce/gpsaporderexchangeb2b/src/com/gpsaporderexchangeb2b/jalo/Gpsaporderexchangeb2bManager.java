package com.gpsaporderexchangeb2b.jalo;

import com.gpsaporderexchangeb2b.constants.Gpsaporderexchangeb2bConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class Gpsaporderexchangeb2bManager extends GeneratedGpsaporderexchangeb2bManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( Gpsaporderexchangeb2bManager.class.getName() );
	
	public static final Gpsaporderexchangeb2bManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (Gpsaporderexchangeb2bManager) em.getExtension(Gpsaporderexchangeb2bConstants.EXTENSIONNAME);
	}
	
}

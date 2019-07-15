package com.gpsapreturnorder.jalo;

import com.gpsapreturnorder.constants.GpsapreturnorderConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class GpsapreturnorderManager extends GeneratedGpsapreturnorderManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( GpsapreturnorderManager.class.getName() );
	
	public static final GpsapreturnorderManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (GpsapreturnorderManager) em.getExtension(GpsapreturnorderConstants.EXTENSIONNAME);
	}
	
}

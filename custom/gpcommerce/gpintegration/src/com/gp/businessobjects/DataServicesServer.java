/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.businessobjects;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.Config;
import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

import java.util.logging.Logger;
import java.util.logging.Level;
import de.hybris.platform.util.Config;

public class DataServicesServer extends Service
{
	public final static Logger LOG=Logger.getLogger(DataServicesServer.class.getName());
	
	final static String wsdlURL=Config.getString("sap.dqm.addr.verification.wsdl.url","http://gplvdbdsd:8080/DataServices/servlet/webservices?ver=2.1&label=sap_dqm&wsdlxml");
	public final static URL WSDL_LOCATION;

	public final static QName SERVICE = new QName("http://www.businessobjects.com", "DataServices_Server");
	public final static QName RealTimeServices = new QName("http://www.businessobjects.com", "Real-time_Services");
	static
	{
		URL url = null;
		try
		{
			url = new URL(wsdlURL);
		}
		catch (final MalformedURLException e)
		{
			LOG.log(Level.INFO,"Can not initialize the default wsdl from {0}", wsdlURL);
		}
		WSDL_LOCATION = url;
	}

	public DataServicesServer(final URL wsdlLocation)
	{
		super(wsdlLocation, SERVICE);
	}

	public DataServicesServer(final URL wsdlLocation, final QName serviceName)
	{
		super(wsdlLocation, serviceName);
	}

	public DataServicesServer()
	{
		super(WSDL_LOCATION, SERVICE);
	}

	public DataServicesServer(final WebServiceFeature... features)
	{
		super(WSDL_LOCATION, SERVICE, features);
	}

	public DataServicesServer(final URL wsdlLocation, final WebServiceFeature... features)
	{
		super(wsdlLocation, SERVICE, features);
	}

	public DataServicesServer(final URL wsdlLocation, final QName serviceName, final WebServiceFeature... features)
	{
		super(wsdlLocation, serviceName, features);
	}


	/**
	 *
	 * @return returns RealTimeServices
	 */
	public RealTimeServices getRealTimeServices()
	{
		return super.getPort(RealTimeServices, RealTimeServices.class);
	}

	/**
	 *
	 * @param features
	 *           A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy. Supported features not in
	 *           the <code>features</code> parameter will have their default values.
	 * @return returns RealTimeServices
	 */
	public RealTimeServices getRealTimeServices(final WebServiceFeature... features)
	{
		return super.getPort(RealTimeServices, RealTimeServices.class, features);
	}
	
}
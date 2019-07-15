/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.core.calculation.service.impl;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import com.gp.commerce.core.calculation.service.GPOneSourceTaxService;
import de.hybris.platform.util.Config;

/**
 * This class provides services to process one source tax calculation
 *
 */
public class OneSourceTaxCalculationService extends Service{

	
	private final static URL TAXCALCULATIONSERVICE_WSDL_LOCATION;
    private final static WebServiceException TAXCALCULATIONSERVICE_EXCEPTION;
    private final static QName TAXCALCULATIONSERVICE_QNAME = new QName("http://www.sabrix.com/services/taxcalculationservice/2011-09-01", "TaxCalculationService");
    private final static String ONESOURCE_WSDL_URL = Config.getParameter("gp.tax.calculation.service.wsdl.url");
    
    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL(ONESOURCE_WSDL_URL);
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        TAXCALCULATIONSERVICE_WSDL_LOCATION = url;
        TAXCALCULATIONSERVICE_EXCEPTION = e;
    }

    /**
     * Constructor
     */
    public OneSourceTaxCalculationService() {
        super(__getWsdlLocation(), TAXCALCULATIONSERVICE_QNAME);
    }

    /**
     * Constructor
     * @param features webservice features
     */
    public OneSourceTaxCalculationService(WebServiceFeature... features) {
        super(__getWsdlLocation(), TAXCALCULATIONSERVICE_QNAME, features);
    }

	/**
	 * Constructor
	 * 
	 * @param wsdlLocation the {@link URL}
	 */
    public OneSourceTaxCalculationService(URL wsdlLocation) {
        super(wsdlLocation, TAXCALCULATIONSERVICE_QNAME);
    }

	/**
	 * Constructor
	 * 
	 * @param wsdlLocation the {@link URL}
	 * @param features     web service features
	 */
    public OneSourceTaxCalculationService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, TAXCALCULATIONSERVICE_QNAME, features);
    }

	/**
	 * Constructor
	 * 
	 * @param wsdlLocation the {@link URL}
	 * @param serviceName  the service name
	 */
    public OneSourceTaxCalculationService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }
    
	/**
	 * Constructor
	 * 
	 * @param wsdlLocation the {@link URL}
	 * @param serviceName  the service name
	 * @param features     web service features
	 */
    public OneSourceTaxCalculationService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns TaxCalculationService
     */
    @WebEndpoint(name = "TaxCalculationServicePort")
    public GPOneSourceTaxService getTaxCalculationServicePort() {
        return super.getPort(new QName("http://www.sabrix.com/services/taxcalculationservice/2011-09-01", "TaxCalculationServicePort"), GPOneSourceTaxService.class);
    }

    /**
     * Returns {@link GPOneSourceTaxService} based on web service features provided 
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns TaxCalculationService
     */
    @WebEndpoint(name = "TaxCalculationServicePort")
    public GPOneSourceTaxService getTaxCalculationServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.sabrix.com/services/taxcalculationservice/2011-09-01", "TaxCalculationServicePort"), GPOneSourceTaxService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (TAXCALCULATIONSERVICE_EXCEPTION!= null) {
            throw TAXCALCULATIONSERVICE_EXCEPTION;
        }
        return TAXCALCULATIONSERVICE_WSDL_LOCATION;
    }
}

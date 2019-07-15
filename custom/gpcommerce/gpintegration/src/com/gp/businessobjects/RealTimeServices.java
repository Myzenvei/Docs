/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.businessobjects;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(targetNamespace = "http://www.businessobjects.com", name = "Real-time_Services")
@XmlSeeAlso(
{ 		com.gp.businessobjects.service.addr.cleanse.output.ObjectFactory.class,
		com.gp.businessobjects.service.addr.cleanse.input.ObjectFactory.class,
		com.gp.businessobjects.service.addr.suggestions.input.ObjectFactory.class,
		com.gp.businessobjects.service.addr.suggestions.output.ObjectFactory.class })
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface RealTimeServices
{

	@WebMethod(operationName = "Service_Realtime_DQ_SAP_Address_Cleanse_Suggestions", action = "service=Service_Realtime_DQ_SAP_Address_Cleanse_Suggestions")
	@WebResult(name = "DataSet", targetNamespace = "http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse_Suggestions/output", partName = "outputBody")
	com.gp.businessobjects.service.addr.suggestions.output.DataSet addrCleanseSuggestionsHybris(
			@WebParam(partName = "inputBody", name = "DataSet", targetNamespace = "http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse_Suggestions/input") com.gp.businessobjects.service.addr.suggestions.input.DataSet inputBody);

	@WebMethod(operationName = "Service_Realtime_DQ_SAP_Address_Cleanse", action = "service=Service_Realtime_DQ_SAP_Address_Cleanse")
	@WebResult(name = "DataSet", targetNamespace = "http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/output", partName = "outputBody")
	com.gp.businessobjects.service.addr.cleanse.output.DataSet addrCleanseHybris(
			@WebParam(partName = "inputBody", name = "DataSet", targetNamespace = "http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input") com.gp.businessobjects.service.addr.cleanse.input.DataSet inputBody);

}

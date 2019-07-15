/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gpintegration.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;


import de.hybris.platform.servicelayer.session.SessionService;

public class GPHeaderHandlerResolver implements HandlerResolver{

	@Resource
	private SessionService sessionService;
	private GPHeaderHandler headerHandler;
	private String serviceName;

	public GPHeaderHandlerResolver(String baseSiteId){
		headerHandler = new GPHeaderHandler(baseSiteId);
	}

	public GPHeaderHandlerResolver(String baseSiteId,String service){
		if(baseSiteId != null){
	   	headerHandler = new GPHeaderHandler(baseSiteId);
	 }
		serviceName = service;
	}



	@Override
	public List<Handler> getHandlerChain(final PortInfo portInfo) {
			final List<Handler> handlerChain = new ArrayList<>();


			if(headerHandler != null) {
				handlerChain.add(headerHandler);
			}
			handlerChain.add(new GPSoapLoggerHandler("GP :" + serviceName));

			return handlerChain;
	}
}

/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.util;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

public class GPSoapLoggerHandler implements SOAPHandler<SOAPMessageContext>{

	private static final Logger LOGGER = Logger.getLogger(GPSoapLoggerHandler.class);
	
	private String sessionId;
	
	public GPSoapLoggerHandler(String sessionId){
		this.sessionId = sessionId;
	}
	

	@Override
	public void close(MessageContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		logMessage(context);
		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		logMessage(context);
		return true;
	}

	@Override
	public Set<QName> getHeaders() {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}
	
	private void logMessage(SOAPMessageContext smc){
		try {
			Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			SOAPMessage message = smc.getMessage();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			message.writeTo(os);
			if (outboundProperty.booleanValue()) {
				LOGGER.debug("=================================================SOAP REQUEST BEGIN================================================= sessionId:"+sessionId);
				LOGGER.debug(new String(os.toByteArray(), StandardCharsets.US_ASCII));
				LOGGER.debug("=================================================SOAP REQUEST END===================================================" );
			} else {
				LOGGER.debug("=================================================SOAP RESPONSE BEGIN================================================ sessionId:"+sessionId);
				LOGGER.debug(new String(os.toByteArray(), StandardCharsets.US_ASCII));
				LOGGER.debug("=================================================SOAP RESPONSE END===================================================" );
			}
		} catch (Exception ex) {
			LOGGER.error("Error in Soap log handler sessionId:"+sessionId , ex);
		}
	}

}

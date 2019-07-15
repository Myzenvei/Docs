/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.core.util;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

public class GPHeaderHandler implements SOAPHandler<SOAPMessageContext> {
	

	private static final String SECURITY = "Security";

	private static final String WSS_SECURITY_SECEXT_URL = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

	private static final String WSSE = "wsse";

	

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public void close(MessageContext context) {
		
	}

	@Override
	public Set<QName> getHeaders() {
		final QName securityHeader = new QName(WSS_SECURITY_SECEXT_URL, SECURITY, WSSE);
		final Set headers = new HashSet();
		headers.add(securityHeader);
		
		return headers;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		// TODO Auto-generated method stub
		return false;
	}
}

/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gpintegration.utils;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

import com.gpintegration.constants.GpintegrationConstants;

import de.hybris.platform.util.Config;

public class GPHeaderHandler implements SOAPHandler<SOAPMessageContext> {
	
	private static final String TYPE = "Type";

	private static final String WSS_USERNAME_TOKEN_URL = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText";

	private static final String XMLNS_WSU = "xmlns:wsu";

	private static final String WSSECURITY_UTILITY_URL = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";

	private static final String PASSWORD = "Password";

	private static final String USERNAME = "Username";

	private static final String USERNAME_TOKEN = "UsernameToken";

	private static final String SECURITY = "Security";

	private static final String WSS_SECURITY_SECEXT_URL = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

	private static final String WSSE = "wsse";

	private static final Logger LOGGER = Logger.getLogger(GPHeaderHandler.class);
	
	private String MERCHANT_ID = "";
	private String MERCHANT_KEY = Config.getParameter(GpintegrationConstants.MERCHANT_KEY);
	
	public GPHeaderHandler(String baseSiteId){
		MERCHANT_ID = Config.getParameter(GpintegrationConstants.MERCHANT_ID+baseSiteId);
		MERCHANT_KEY = Config.getParameter(GpintegrationConstants.MERCHANT_KEY+baseSiteId);
	}
	@Override
	public boolean handleMessage(final SOAPMessageContext smc) {
		final Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outboundProperty.booleanValue()) {
			try {
				final SOAPEnvelope envelope = smc.getMessage().getSOAPPart().getEnvelope();
				final SOAPHeader header = envelope.getHeader();

				final SOAPElement security = header.addChildElement(SECURITY, WSSE, WSS_SECURITY_SECEXT_URL);

				final SOAPElement usernameToken = security.addChildElement(USERNAME_TOKEN, WSSE);
				usernameToken.addAttribute(new QName(XMLNS_WSU), WSSECURITY_UTILITY_URL);

				final SOAPElement username = usernameToken.addChildElement(USERNAME, WSSE);
				username.addTextNode(MERCHANT_ID);
				final SOAPElement password = usernameToken.addChildElement(PASSWORD, WSSE);
				password.setAttribute(TYPE,	WSS_USERNAME_TOKEN_URL);
				password.addTextNode(MERCHANT_KEY);
			}
			catch(final SOAPException ex) {
				LOGGER.error("Unable to add security header: " , ex);
			}
		}
		return outboundProperty.booleanValue();
	}

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
}

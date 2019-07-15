
/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.b2baccaddon.commerce.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import com.gp.b2baccaddon.commerce.security.GPWebAuthenticationDetails;

public class GPWebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, GPWebAuthenticationDetails> {
	String siteId; 
	
	@Override
	public GPWebAuthenticationDetails buildDetails(HttpServletRequest context) {
		// TODO Auto-generated method stub
		 return new GPWebAuthenticationDetails(context,context.getParameter("baseSiteId"));
	}
	
	

}

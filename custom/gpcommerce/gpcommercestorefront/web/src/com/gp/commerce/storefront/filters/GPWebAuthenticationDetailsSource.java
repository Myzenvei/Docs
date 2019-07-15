/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.storefront.filters;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;

public class GPWebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, GPWebAuthenticationDetails> {
	String siteId; 
	
	@Override
	public GPWebAuthenticationDetails buildDetails(HttpServletRequest context) {
		// TODO Auto-generated method stub
		 return new GPWebAuthenticationDetails(context,context.getParameter("baseSiteId"));
	}
	
	

}

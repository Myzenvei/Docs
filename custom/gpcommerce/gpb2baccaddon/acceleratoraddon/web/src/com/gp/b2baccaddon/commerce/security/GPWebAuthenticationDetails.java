/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.b2baccaddon.commerce.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class GPWebAuthenticationDetails extends WebAuthenticationDetails{
	String siteId;
	
	public GPWebAuthenticationDetails(HttpServletRequest request,String siteId) {
		super(request);
		this.siteId = siteId;
	}
	
	public String getSiteId() {
		return siteId;
	}

}

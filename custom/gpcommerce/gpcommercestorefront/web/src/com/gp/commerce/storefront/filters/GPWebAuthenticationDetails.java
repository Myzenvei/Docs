/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.storefront.filters;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class GPWebAuthenticationDetails extends WebAuthenticationDetails{
	String siteId;
	String loginType;
	String token;
	String kochAuthAccessToken;
	String kochAuthIdToken;
	String kochAuthRefreshToken;
	String kochAuthTS;
	String kochEmailId;
	
	public GPWebAuthenticationDetails(HttpServletRequest request,String siteId) {
		super(request);
		this.siteId = siteId;
		this.loginType = request.getParameter("loginType");
		this.token = request.getParameter("token");
		this.kochAuthAccessToken = request.getParameter("kochAuthAccessToken");
		this.kochAuthIdToken = request.getParameter("kochAuthIdToken");
		this.kochAuthRefreshToken = request.getParameter("kochAuthRefreshToken");
		this.kochAuthTS = request.getParameter("kochAuthTS");
		this.kochEmailId = request.getParameter("kochEmailId");
	}
	
	public String getSiteId() {
		return siteId;
	}
	
	public String getLoginType() {
		return loginType;
	}
	
	public String getToken() {
		return token;
	}
	
	public String getKochAuthAccessToken() {
		return kochAuthAccessToken;
	}
	
	public String getKochAuthIdToken() {
		return kochAuthIdToken;
	}
	
	public String getKochAuthRefreshToken() {
		return kochAuthRefreshToken;
	}

	public String getKochAuthTS() {
		return kochAuthTS;
	}
	
	public String getKochEmailId() {
		return kochEmailId;
	}

}

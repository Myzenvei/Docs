/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service;

import com.gpintegration.dto.paypal.PayPalAuthorizationRequest;
import com.gpintegration.dto.paypal.PayPalAuthorizationResponse;
import com.gpintegration.dto.paypal.PayPalCaptureRequest;
import com.gpintegration.dto.paypal.PayPalCaptureResponse;
import com.gpintegration.dto.paypal.PayPalExecuteRequest;
import com.gpintegration.dto.paypal.PayPalExecuteResponse;
import com.gpintegration.dto.paypal.PayPalRefundRequest;
import com.gpintegration.dto.paypal.PayPalRefundResponse;

/**
 * The Interface GPPayPalIntegrationService.
 */
public interface GPPayPalIntegrationService {
	
	/**
	 * Gets the access token.
	 *
	 * @return the access token
	 */
	public String getAccessToken();
	
	/**
	 * Authorize.
	 *
	 * @param payPalPaymentRequest the pay pal payment request
	 * @return the pay pal authorization response
	 */
	public PayPalAuthorizationResponse authorize(PayPalAuthorizationRequest payPalPaymentRequest);
	
	/**
	 * Execute.
	 *
	 * @param payPalExecuteRequest the pay pal execute request
	 * @param token the token
	 * @return the pay pal execute response
	 */
	public PayPalExecuteResponse execute(PayPalExecuteRequest payPalExecuteRequest, String token);
	
	/**
	 * Capture.
	 *
	 * @param payPalCaptureRequest the pay pal capture request
	 * @param token the token
	 * @return the pay pal capture response
	 */
	public PayPalCaptureResponse capture(PayPalCaptureRequest payPalCaptureRequest, String token);
	
	/**
	 * Refund.
	 *
	 * @param payPalCaptureRequest the pay pal capture request
	 * @param token the token
	 * @return the pay pal refund response
	 */
	public PayPalRefundResponse refund(PayPalRefundRequest payPalCaptureRequest, String token);
}

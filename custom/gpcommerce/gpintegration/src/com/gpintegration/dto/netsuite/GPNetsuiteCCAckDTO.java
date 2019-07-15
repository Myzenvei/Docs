/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.dto.netsuite;

import java.io.Serializable;
/**
 * @author vdannina
 *
 */
public class GPNetsuiteCCAckDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String status;
	
	private String message;
	
	private String nsPaymentId;

	public String getNsPaymentId() {
		return nsPaymentId;
	}

	public void setNsPaymentId(String nsPaymentId) {
		this.nsPaymentId = nsPaymentId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
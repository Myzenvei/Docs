/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.dto.netsuite;

import java.io.Serializable;
/**
 * @author spandiyan
 *
 */
public class GPNetsuiteResponseDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String status;
	
	private String message;
	
	private String netsuiteId;
	
	private int retryCount;

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

	public String getNetsuiteId() {
		return netsuiteId;
	}

	public void setNetsuiteId(String netsuiteId) {
		this.netsuiteId = netsuiteId;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	@Override
	public String toString() {
		return "GPNetsuiteResponseDTO [status=" + status + ", message=" + message + ", netsuiteId=" + netsuiteId
				+ ", retryCount=" + retryCount + "]";
	}

	
}
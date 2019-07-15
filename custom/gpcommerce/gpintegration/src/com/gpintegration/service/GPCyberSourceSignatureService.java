/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service;

import com.gpintegration.utils.GPSignaturePojo;
import com.gpintegration.utils.GPSignatureResponseDTO;

/**
 *  Interface for Cyber Source Signature.
 */
public interface GPCyberSourceSignatureService {
	
	/**
	 * Gets the signature.
	 *
	 * @param pojo the signature pojo
	 * @param baseSiteId the base site id
	 * @return Signature response
	 */
	GPSignatureResponseDTO getTheSignature(GPSignaturePojo pojo, String baseSiteId);
}

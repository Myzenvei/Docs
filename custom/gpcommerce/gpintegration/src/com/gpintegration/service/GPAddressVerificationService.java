/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service;

import de.hybris.platform.commercefacades.user.data.AddressData;

import java.util.List;

import com.gpintegration.exception.GPIntegrationException;

/**
 * Interface for address verification.
 */
public interface GPAddressVerificationService {

	/**
	 * Verifies address.
	 *
	 * @param addressData the address data
	 * @return address data
	 * @throws GPIntegrationException the GP integration exception
	 */
	AddressData verifyAddress(AddressData addressData) throws GPIntegrationException;

	/**
	 * Suggests addresses.
	 *
	 * @param addressData the address data
	 * @return list of suggested addresses
	 * @throws GPIntegrationException the GP integration exception
	 */
	List<AddressData> suggestAddresses(AddressData addressData) throws GPIntegrationException;
}

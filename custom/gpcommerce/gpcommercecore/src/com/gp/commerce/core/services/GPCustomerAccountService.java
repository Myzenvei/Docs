/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * Interface for customer account service
 */
public interface GPCustomerAccountService {

	/**
	 * This method gets address for b2b customer
	 * @param code
	 * 			the code
	 * @param userId
	 * 			the user id
	 * @return address for b2b customer
	 */
	AddressModel getAddressForB2BCustomer(final String code, final String userId);

	/**
	 * This method gets active address book entries
	 * @param customerModel
	 * 			the customer model
	 * @param fetchActiveAddresses
	 * 			the status of fetch address
	 * @return active address book entries
	 */
	List<AddressModel> getActiveAddressBookEntries(CustomerModel customerModel, Boolean fetchActiveAddresses);

	/**
	 * Update customer
	 *
	 * @param customer the customer
	 */
	void updateCustomer(CustomerModel customer);

	/**
	 * Returns Media for the given Multipart file
	 * 
	 * @param multipartFile the multipart file
	 * @return {@link MediaModel}
	 * @throws ConversionException on conversion error
	 */
	MediaModel convertMultipartFileToMedia(final MultipartFile multipartFile) throws ConversionException;

	/**
	 * Update Payment info
	 *
	 * @param paymentInfo the {@link CCPaymentInfoData}
	 */
	void updateCCPaymentInfo(final CCPaymentInfoData paymentInfo);

	/**
	 * Add payment info
	 *
	 * @param paymentInfo the {@link CCPaymentInfoData}
	 */
	void addCCPaymentInfo(final CCPaymentInfoData paymentInfo);

	/**
	 * Publish event for register B2B customer
	 *
	 * @param b2bCustomerModel the B2B customer
	 */
	void registerB2BCustomerEvent(final B2BCustomerModel b2bCustomerModel);
}

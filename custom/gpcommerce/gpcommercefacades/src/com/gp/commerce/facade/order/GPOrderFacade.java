/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facade.order;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gp.commerce.facade.data.NsConsignmentData;
import com.gp.commerce.facade.order.data.LeaseAgreementData;
import com.gp.commerce.facade.order.data.SplitEntryListWsDTO;

import de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO;

/**
 * The Interface GPOrderFacade.
 */
public interface GPOrderFacade {

	/**
	 * Method to get update order status by consignment data.
	 *
	 * @param consignmentData the consignment data
	 * @return NsConsignmentData
	 */
	NsConsignmentData updateOrderStatus(NsConsignmentData consignmentData);

	/**
	 * Method to get Lease Agreement by Lease Id.
	 *
	 * @param leaseId the lease id
	 * @return LeaseAgreementData
	 */
	LeaseAgreementData getLeaseAgreementById(String leaseId);

	/**
	 * Method to check whether the Users Approval Status is in Review.
	 *
	 * @return true, if is user in review
	 */
	boolean isUserInReview();

	/**
	 * Reset ASM cart cookie once the order is placed by checking flag.
	 *
	 * @param request the request
	 * @param response the response
	 * @param cartId the cart id
	 */
	void resetCartCookieForASM(final HttpServletRequest request, final HttpServletResponse response, String cartId);

	/**
	 * Method to check whether the Address Approval Status is Approved.
	 *
	 * @return true, if is address approved
	 */
	boolean isAddressApproved();
	
	Map<String, SplitEntryListWsDTO> getDeliveryGroup(OrderWsDTO order);

}

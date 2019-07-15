/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.facades.cart;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;

import java.util.List;
import java.util.Map;

import com.gp.commerce.core.exceptions.GPSampleCartException;
import com.gp.commerce.dto.group.dto.DeliveryAddressGroupWsDTO;
import com.gp.commerce.facade.data.IncompatibleProductsData;
import com.gp.commerce.facade.order.data.GPRestrictionProductListWsDTO;
import com.gp.commerce.facade.order.data.LeaseAgreementData;
import com.gp.commerce.facade.order.data.SaveAgreementData;
import com.gp.commerce.facade.order.data.ShippingRestrictionsData;
import com.gp.commerce.order.data.GPSampleCartResponseData;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.leaseagreement.dto.GPLeaseAgreementDTO;

/**
 * The Interface is for GP cart facade.
 */
public interface GPCartFacade
{
	
	/**
	 * method to share cart.
	 *
	 * @param toEmail the to email
	 * @param senderEmail the sender email
	 * @param senderName the sender name
	 * @param cartData the cart data
	 */
	void shareCart(final String toEmail, String senderEmail, String senderName, final CartData cartData);

	/**
	 * method to verify address.
	 *
	 * @param data the data
	 * @return address data
	 */
	AddressData verifyAddress(AddressData data);

	/**
	 * method to suggest addresses.
	 *
	 * @param data the data
	 * @return list of suggested address data
	 */
	List<AddressData> suggestAddresses(AddressData data);


	/**
	 * method to fetch shipping restrisctions.
	 *
	 * @param shippingProductList the shipping product list
	 * @return list of shipping restrictions
	 * @throws CommerceCartModificationException the commerce cart modification exception
	 */
	List<ShippingRestrictionsData> fetchShippingRestrictions(GPRestrictionProductListWsDTO shippingProductList)
			throws CommerceCartModificationException;

	/**
	 * method to get lease agreement for country.
	 *
	 * @param country the country
	 * @return lease agreement data
	 */
	LeaseAgreementData getLeaseAgreementForCountry(String country);

	/**
	 * method to validate coupon for site.
	 *
	 * @param siteId the site id
	 * @param couponCode the coupon code
	 * @return status
	 */
	boolean validateCouponforSite(String siteId, String couponCode);

	/**
	 * method to save lease agreement.
	 *
	 * @param saveLeaseAgreementReq the save lease agreement req
	 * @return saved agreement data
	 * @throws GPIntegrationException the GP integration exception
	 */
	SaveAgreementData saveLeaseAgreement(GPLeaseAgreementDTO saveLeaseAgreementReq) throws GPIntegrationException;


	/**
	 * method to get supported delivery modes.
	 *
	 * @return supported delivery modes
	 * @throws Exception the exception
	 */
	List<? extends DeliveryModeData> getSupportedDeliveryModes() throws Exception;

	/**
	 * method to get supported delivery modes.
	 *
	 * @param deliveryGroupDto the delivery group dto
	 * @return list of supported delivery modes
	 * @throws Exception the exception
	 */
	Map<String, List<DeliveryModeData>> getSupportedDeliveryModes(DeliveryAddressGroupWsDTO deliveryGroupDto)
			throws Exception;

	/**
	 * method to update split entries with delivery modes.
	 *
	 * @param deliveryModeList the delivery mode list
	 */
	void updateSpliEntriesWithDeliveryModes(Map<String, List<DeliveryModeData>> deliveryModeList);

	/**
	 * method to update delivery instructions.
	 *
	 * @param DeliveryInst the delivery inst
	 */
	void updateDeliveryInstruction(String DeliveryInst);

	/**
	 * method to update lease name.
	 *
	 * @param leaseName the lease name
	 */
	void updateLeaseName(String leaseName);

	/**
	 * method to update lease agreement id.
	 *
	 * @param agreementId the agreement id
	 * @param leaseTermId the lease term id
	 */
	void updateLeaseAgreementId(String agreementId, String leaseTermId);

	/**
	 * Checks if there are incompatible products in cart.
	 *
	 * @return IncompatibleProductsData
	 */
	IncompatibleProductsData checkIncompatibleProducts();

	/**
	 * Get the supported shippping countries. The list is sorted alphabetically.
	 *
	 * @return list of supported shippping countries.
	 */
	List<CountryData> getShippingCountries();
	
	/**
	 * method to add SampleCart.
	 *
	 * @return GPSampleCartResponseData
	 * @throws GPSampleCartException the GP sample cart exception
	 */
	GPSampleCartResponseData orderSampleCart() throws GPSampleCartException;


	/**
	 * Update previous delivery mode.
	 *
	 * @param deliveryModeId the delivery mode id
	 */
	void updatePreviousDeliveryMode(final String deliveryModeId);
 

}

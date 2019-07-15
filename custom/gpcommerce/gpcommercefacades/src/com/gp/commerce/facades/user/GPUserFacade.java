/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.user;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.QuplesData;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;

import com.gp.commerce.core.model.GPHeaderNavigationComponentModel;
import com.gp.commerce.facades.data.user.data.MarketingPreferenceDataList;
import com.gp.commerce.facades.user.data.MarketingPreferenceQuestionAnsDataList;
import com.gp.commerce.gpcommerceaddon.facades.GPHeaderNavigationComponentData;
/**
 * Interface for user facade
 */
public interface GPUserFacade
{

	/**
	 * Returns the default shipping address or defaultBillingAddress depending on the boolean values passed as arguments.
	 * If no default shipping/billing address is available then returns the first shipping/billing address
	 *
	 * @param getShippingAddress
	 *           Boolean indicating if the default shipping address is to be fetched.
	 * @param getBillingAddress
	 *           Boolean indicating if the default billing address is to be fetched.
	 * @return Returns the default shipping address or defaultBillingAddress depending on the boolean values passed as
	 *         arguments.
	 */
	AddressData getDefaultAddress(final boolean getShippingAddress, final boolean getBillingAddress);

	/**
	 * Gets quples token.
	 *
	 * @param quplesData the quples data
	 * @return quples token
	 * @throws Exception the exception
	 */
	QuplesData getQuplesToken(QuplesData quplesData) throws Exception;
	
	/**
	 * Adds the given address as default shipping or default billing or both depending on the boolean indicators in
	 * arguments.
	 *
	 * @param addressData
	 *           The address to be added.
	 * @param isDefaultShipping
	 *           Indicates if the address is to be added as default shipping address.
	 * @param isDefaultBilling
	 *           Indicated if the address is to be added as default billing address.
	 */
	void setDefaultAddressEntry(final AddressData addressData, final boolean isDefaultShipping,
			final boolean isDefaultBilling);

	/**
	 * Adds the given address as default shipping or default billing or both depending on the boolean indicators in
	 * arguments.
	 *
	 * @param addressData
	 *           The address to be added.
	 * @param isDefaultShipping
	 *           Indicates if the address is to be added as default shipping address.
	 * @param isDefaultBilling
	 *           Indicated if the address is to be added as default billing address.
	 * @param customer
	 * 			the customer	
	 */
	void setDefaultAddressEntryForB2BCustomer(final AddressData addressData, final boolean isDefaultShipping,
			final boolean isDefaultBilling,CustomerModel customer);

	/**
	 * Returns all the addresses pertaining to the current customer.
	 *
	 * @return All the addresses of the current customer.
	 */
	List<AddressData> getWholeAddressBook();

	/**
	 * method to get list of the marketing preferences for current site
	 *
	 * @return list of the marketing preferences for current site & its Header
	 */
	MarketingPreferenceQuestionAnsDataList getAllMarketingPreferences();

	/**
	 * method to update the selected marketing preferences to customer.
	 *
	 * @param preferencesDataList
	 *           The list of marketing preferences opted in or out by the customer.
	 *
	 */
	void updateCustomerPreferences(MarketingPreferenceDataList preferencesDataList);

	/**
	 * Add current users' credit card payment info
	 *
	 * @param paymentInfo
	 *           - new payment info data
	 *
	 */
	void addCCPaymentInfo(CCPaymentInfoData paymentInfo);

	/**
	 * Edits address for b2b
	 * 
	 * @param addressData
	 * 			the address data
	 * @param editAddress
	 * 			boolean parameter to edit address
	 */
	void editAddressForB2B(final AddressData addressData,final boolean editAddress);

	/**
	 * Returns the default shipping address or defaultBillingAddress depending on the boolean values passed as arguments.
	 * If no default shipping/billing address is available then returns the first shipping/billing address
	 *
	 * @param getShippingAddress
	 *           Boolean indicating if the default shipping address is to be fetched.
	 * @param getBillingAddress
	 *           Boolean indicating if the default billing address is to be fetched.
	 * @param customer
	 * 			the customer
	 * @return Returns the default shipping address or defaultBillingAddress depending on the boolean values passed as
	 *         arguments.
	 */
	AddressData getDefaultAddressForCustomer(final boolean getShippingAddress, final boolean getBillingAddress,CustomerModel customer);

	/**
	 * @return the content of the Term and Condition since it require from different locations
	 * 		If no content available will return null
	 */
	String getTermAndCondContent();
	
	/**
	 * to get active address for b2b customer and all addresses for b2c
	 * @return active addresses
	 */
	List<AddressData> getActiveAddresses();

	/**
	 * Gets header data.
	 *
	 * @param request 			the request to get header data
	 * @param component 			header navigation component model
	 * @param model 			the model
	 * @return the header data
	 */
	GPHeaderNavigationComponentData getHeaderData(final HttpServletRequest request,
			final GPHeaderNavigationComponentModel component, Model model);
	
	/**
	 * Change the cart user by taking cartID from Cookie.
	 *
	 * @param request & cartID
	 * @param cartId the cart id
	 */
	public void changeSubscripCartUser(final HttpServletRequest request,final String cartId);
}

/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gpintegration.utils;

import java.math.BigInteger;
import java.util.Map;

import com.cybersource.schemas.transaction_data_1.BillTo;
import com.cybersource.schemas.transaction_data_1.Card;
import com.cybersource.schemas.transaction_data_1.Item;
import com.cybersource.schemas.transaction_data_1.PurchaseTotals;
import com.gpintegration.constants.GpintegrationConstants;

public class BuildCybsCallData {

	private BuildCybsCallData() {

	}

	/**
	 * Populates Item object which is used to pass as a request parameter for
	 * cybersource commands
	 *
	 * @param itemId
	 * @param unitPrice
	 * @param quantity
	 * @return Object of Item class
	 */
	public static Item buildItem(final String itemId, final String unitPrice,
	        final String quantity) {
			final Item item = new Item();
			item.setId(new BigInteger(itemId));
			item.setUnitPrice(unitPrice);
			item.setQuantity(quantity);
			return item;
	}

	/**
	 * Populates PurchaseTotals object which is used to pass as a request
	 * parameter for cybersource commands
	 *
	 * @param currency
	 * @param totalAmount
	 * @return Object of PurchaseTotals class
	 */
	public static PurchaseTotals buildPurchaseTotals(final String currency,
	        final String totalAmount) {
			final PurchaseTotals purchaseTotals = new PurchaseTotals();
			purchaseTotals.setCurrency(currency);
			purchaseTotals.setGrandTotalAmount(totalAmount);
			return purchaseTotals;
	}

	/**
	 * Populates Credit Card object which is used to pass as a request parameter
	 * for cybersource commands
	 *
	 * @param cardNumber
	 * @param expMonth
	 * @param expYear
	 * @return Object of credit Card class
	 */
	public static Card buildCard(final String cardNumber,
	        final String expMonth, final String expYear, final String cvv) {
			final Card card = new Card();
			card.setAccountNumber(cardNumber);
			card.setExpirationMonth(expMonth != null ? (new BigInteger(expMonth))
			        : null);
			card.setExpirationYear(expYear != null ? (new BigInteger(expYear))
			        : null);
			card.setCvNumber(cvv);
			return card;
	}

	/**
	 * Populates Billing address object which is used to pass as a request
	 * parameter for cybersource commands
	 *
	 * @param addressMap
	 * @return Object of Billing class
	 */
	public static BillTo buildBillTo(final Map<String, String> addressMap) {
			final BillTo billTo = new BillTo();
			billTo.setFirstName(addressMap
			        .get(GpintegrationConstants.CYB_FIRST_NAME));
			billTo.setLastName(addressMap
			        .get(GpintegrationConstants.CYB_LAST_NAME));
			billTo.setStreet1(addressMap
			        .get(GpintegrationConstants.CYB_ADDRESS_LINE_1));
			billTo.setCity(addressMap.get(GpintegrationConstants.CYB_CITY));
			billTo.setState(addressMap.get(GpintegrationConstants.CYB_STATE));
			billTo.setPostalCode(addressMap
			        .get(GpintegrationConstants.CYB_POSTAL_CODE));
			billTo.setCountry(addressMap
			        .get(GpintegrationConstants.CYB_COUNTRY));
			billTo.setEmail(addressMap.get(GpintegrationConstants.CYB_EMAIL));
			return billTo;
	}
}

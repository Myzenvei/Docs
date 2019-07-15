/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.payment.enums;

import java.util.HashMap;
import java.util.Map;

import de.hybris.platform.acceleratorservices.payment.cybersource.enums.CardTypeEnum;

/**
 * This enum represents the supported credit card types
 */
public enum CreditCardTypesEnum
{

	/**
	 * CREDIT CARD TYPES VISA
	 */
	visa("001", "VISA"), 
	/**
	 * CREDIT CARD TYPES MASTER
	 */
	master("002", "MASTER"),
	/**
	 * CREDIT CARD TYPES AMEX
	 */
	amex("003", "AMEX"), 
	/**
	 * CREDIT CARD TYPES DISCOVER
	 */
	discover("004", "DISCOVER"), 
	/**
	 * CREDIT CARD TYPES DINERS
	 */
	diners("005", "DINERS"), 
	/**
	 * CREDIT CARD TYPES JCB
	 */
	jcb("007", "JCB"), 
	/**
	 * CREDIT CARD TYPES MAESTRO
	 */
	maestro("024", "MAESTRO"), 
	/**
	 * CREDIT CARD TYPES SWITCH
	 */
	SWITCH("switch", "SWITCH");

	private final String code;
	
	private final String name;
	
	private CreditCardTypesEnum(final String code, final String name)
	{
		this.code=code;
		this.name=name;
	}

	/**
	 * gets a String value of the Credit Card.
	 * @return - a String object.
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * gets code of the Credit Card.
	 * @return - a String object.
	 */
	public String getCode()
	{
		return this.code;
	}

}

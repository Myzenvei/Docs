/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.payment.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import com.gp.commerce.facades.payment.GPPaymentFacade;
import com.gp.commerce.facades.payment.enums.CreditCardTypesEnum;
import com.gp.commerce.order.data.CardTypeDataList;

import de.hybris.platform.commercefacades.order.data.CardTypeData;
import de.hybris.platform.servicelayer.config.ConfigurationService;


/**
 * Supported methods of credit card payment
 *
 */
public class GPDefaultPaymentFacade implements GPPaymentFacade
{

	@Resource(name="configurationService")
	ConfigurationService configurationService;
	
	private static final String EXPIRYYEARLIST_MAXCOUNT = "10";
	private static final String EXPIRYYEARLIST_MAXCOUNT_PROPERTY = "creditCard.expiryYearList.maxCount";
	
	@Override
	public CardTypeDataList getCreditCardTypes()
	{
		CardTypeData cardTypeData;  
		List<CardTypeData> cardTypes = new ArrayList<CardTypeData>();
		CardTypeDataList cardTypeDataList =  new CardTypeDataList();
		for (final CreditCardTypesEnum creditCardTypesEnum : CreditCardTypesEnum.values())
		{
			cardTypeData = new CardTypeData();			
			cardTypeData.setCode(creditCardTypesEnum.getCode());
			cardTypeData.setName(creditCardTypesEnum.getName());
			cardTypes.add(cardTypeData);	
		}		
		cardTypeDataList.setCardTypes(cardTypes);
		return cardTypeDataList;
	}

	@Override
	public List<String> getExpiryYearList()
	{
		String yearListCount = configurationService.getConfiguration().getString(EXPIRYYEARLIST_MAXCOUNT_PROPERTY);
		if(null == yearListCount)
		{
			yearListCount = EXPIRYYEARLIST_MAXCOUNT;
		}
		int maxNumberOfYears = Integer.parseInt(yearListCount);
		int year = Calendar.getInstance().get(Calendar.YEAR);
		List<String> yearList = new ArrayList<String>();
		for(int i=0; i<=maxNumberOfYears; i++) 
		{
			yearList.add(Integer.toString(year+i));
		}		
		return yearList;
	}
	
}

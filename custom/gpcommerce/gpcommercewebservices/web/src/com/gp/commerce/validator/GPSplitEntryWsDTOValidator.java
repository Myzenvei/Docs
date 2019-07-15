/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.validator;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.gp.commerce.facade.order.data.SplitEntryWsDTO;
import com.gp.commerce.order.entry.data.GPSplitListWsDTO;
import com.gp.commerce.order.entry.data.GPSplitWsDTO;

public class GPSplitEntryWsDTOValidator implements Validator{
	
	@Override
	public boolean supports(Class<?> clazz) {
		return String.class.equals(clazz) || GPSplitListWsDTO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors error) {
		GPSplitListWsDTO wsDTO =(GPSplitListWsDTO)target;
		if(CollectionUtils.isEmpty(wsDTO.getSplitEntry())){
			error.reject("Entries can't be empty.");
			return;
		}

		for(GPSplitWsDTO splitEntry:wsDTO.getSplitEntry()) {
			if(null == splitEntry.getEntryNumber()) {
				error.reject("Entry number can not be null");
			}
			for(SplitEntryWsDTO entry : splitEntry.getSplitEntry()) {
				if(StringUtils.isBlank(entry.getQty())) {
					error.reject("Quantity can not be Empty");
				}if(null == entry.getDeliveryAddress()) {
					error.reject("Address can not be null");
				}
			}
		}
		
	}
	
	
}

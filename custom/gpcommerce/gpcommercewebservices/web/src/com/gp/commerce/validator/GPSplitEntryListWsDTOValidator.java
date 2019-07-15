/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.validator;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.gp.commerce.facade.order.data.SplitEntryListWsDTO;

public class GPSplitEntryListWsDTOValidator implements Validator{
	
	@Override
	public boolean supports(Class<?> clazz) {
		return String.class.equals(clazz) || SplitEntryListWsDTO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		SplitEntryListWsDTO wsDTO =(SplitEntryListWsDTO)target;
		if(CollectionUtils.isEmpty(wsDTO.getSplitEntries())) {
			errors.reject("Entries can't be empty.");
		}
		if(null == wsDTO.getDeliveryMode()) {
			errors.reject("Entries can't be empty.");
		}
	}

	
}

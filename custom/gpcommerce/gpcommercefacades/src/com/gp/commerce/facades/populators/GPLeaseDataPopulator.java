/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import com.gp.commerce.core.model.GPEndUserLegalTermsModel;
import com.gp.commerce.facade.order.data.LeaseAgreementData;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class GPLeaseDataPopulator implements Populator<GPEndUserLegalTermsModel, LeaseAgreementData> {

	@Override
	public void populate(GPEndUserLegalTermsModel source, LeaseAgreementData target) throws ConversionException {
		target.setCountry(source.getCountry());
		target.setLegalLanguage(source.getLegalLanguage());
		target.setLegalTermName(source.getLegalTermName());
		target.setLegalTermsText(source.getLegalTermsText());
		target.setVersion(source.getVersion());
		target.setLegalTermsId(source.getId());
	}
	
	 
}

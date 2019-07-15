/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.event;

import java.util.List;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.media.MediaModel;

/**
 * 
 * TaxExemption Submit review event, implementation of {@link AbstractCommerceUserEvent}
 *
 */

@SuppressWarnings({ "serial", "rawtypes" })
public class GPTaxExemptionSubmitReviewEvent extends AbstractCommerceUserEvent {

	private List<MediaModel> taxExemptionDocumentList;

	public List<MediaModel> getTaxExemptionDocumentList() {
		return taxExemptionDocumentList;
	}

	public void setTaxExemptionDocumentList(List<MediaModel> taxExemptionDocumentList) {
		this.taxExemptionDocumentList = taxExemptionDocumentList;
	}
}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.event;

import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;

import com.gp.commerce.core.event.GPTaxExemptionSubmitReviewEvent;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.AddressModel;

/**
 * 
 * TaxExemption Submit review event, implementation of {@link AbstractCommerceUserEvent}
 *
 */

@SuppressWarnings({ "serial", "rawtypes" })
public class GPTaxExemptionSubmitReviewEventTest extends AbstractCommerceUserEvent {

	@InjectMocks
	private GPTaxExemptionSubmitReviewEvent gpTaxExemptionSubmitReviewEvent = new GPTaxExemptionSubmitReviewEvent();
	
	@Test
	public void gpEmailItemEvent() {
		List<MediaModel> taxDocuments = Arrays.asList(new MediaModel(),new MediaModel());
		gpTaxExemptionSubmitReviewEvent.setTaxExemptionDocumentList(taxDocuments);
	}

}

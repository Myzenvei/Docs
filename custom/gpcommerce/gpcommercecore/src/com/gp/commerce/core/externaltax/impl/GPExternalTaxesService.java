/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.externaltax.impl;

import org.springframework.util.Assert;

import de.hybris.platform.commerceservices.externaltax.RecalculateExternalTaxesStrategy;
import de.hybris.platform.commerceservices.externaltax.impl.DefaultExternalTaxesService;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.externaltax.ExternalTaxDocument;

/**
 * This class is for External Tax services
 */
public class GPExternalTaxesService extends DefaultExternalTaxesService{
	
	@Override
	public boolean calculateExternalTaxes(AbstractOrderModel abstractOrder) {
		if (getDecideExternalTaxesStrategy().shouldCalculateExternalTaxes(abstractOrder))
		{
			if (getRecalculateExternalTaxesStrategy().recalculate(abstractOrder))
			{
				final ExternalTaxDocument exTaxDocument = getCalculateExternalTaxesStrategy().calculateExternalTaxes(abstractOrder);
				Assert.notNull(exTaxDocument, "ExternalTaxDocument should not be null");
				// check if external tax calculation was successful
				if (!exTaxDocument.getAllTaxes().isEmpty())
				{
					getApplyExternalTaxesStrategy().applyExternalTaxes(abstractOrder, exTaxDocument);
					getSessionService().setAttribute(SESSION_EXTERNAL_TAX_DOCUMENT, exTaxDocument);
					saveOrder(abstractOrder);
					return true;
				}
				else
				{
					// the external tax calculation failed
					getSessionService().removeAttribute(RecalculateExternalTaxesStrategy.SESSION_ATTIR_ORDER_RECALCULATION_HASH);
					clearSessionTaxDocument();
					clearTaxValues(abstractOrder);
					saveOrder(abstractOrder);
				}
			}
			else
			{
				// get the cached tax document
				getApplyExternalTaxesStrategy().applyExternalTaxes(abstractOrder, getSessionExternalTaxDocument(abstractOrder));
				saveOrder(abstractOrder);
				return true;
			}
		}
		return false;
	}

}

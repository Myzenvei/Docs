/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.order.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartMergingStrategy;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;

public class GPDefaultCommerceCartMergingStrategy  extends DefaultCommerceCartMergingStrategy{

	@Override
	public void mergeCarts(final CartModel fromCart, final CartModel toCart, final List<CommerceCartModification> modifications)
			throws CommerceCartMergingException
	{
		// validate before merge
		validationBeforeMerge(fromCart, toCart, modifications, getUserService().getCurrentUser());

		// copy entry group
		copyEntryGroups(fromCart, toCart);
		
		//Remove Give Away Products
	    fromCart.setEntries(fromCart.getEntries().stream().filter(x -> !x.getGiveAway()).collect(Collectors.toList()));
	    toCart.setEntries(toCart.getEntries().stream().filter(x -> !x.getGiveAway()).collect(Collectors.toList()));
	    getModelService().save(toCart);
	    
		// merge entry with cart
		for (final AbstractOrderEntryModel entry : fromCart.getEntries())
		{
			modifications.add(mergeEntryWithCart(entry, toCart));
		}

		// after merge
		// TODO payment transactions - to clear or not to clear...
		toCart.setCalculated(Boolean.FALSE);
		fromCart.setEntries(Collections.emptyList());
		getModelService().save(toCart);
		getModelService().remove(fromCart);
	}
}

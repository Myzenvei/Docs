/**
 *
 */
package com.deloitte.service;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;

import java.util.List;

import com.deloitte.model.SubscriptionCartModel;


/**
 * @author asomjal
 *
 */
public interface SubscriptionCartService
{
	CommerceCartModification addToCart(final CommerceCartParameter parameter) throws CommerceCartModificationException;

	List<SubscriptionCartModel> getSubscriptionCartModels();

	List<SubscriptionCartModel> getSubcriptionCartsForUser(String userId);
}

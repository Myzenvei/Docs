/**
 *
 */
package com.deloitte.facade;

import de.hybris.platform.commercefacades.order.data.AddToCartParams;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;

import java.util.List;

import com.deloitte.model.SubscriptionCartModel;


/**
 * @author asomjal
 *
 */
public interface SubscriptionCartFacade
{
	CartModificationData addToCartInternal(final String code, final long quantity, String subscriptionFrequecy, String cartId)
			throws CommerceCartModificationException;

	CartModificationData addToCart(final AddToCartParams addToCartParams) throws CommerceCartModificationException;

	List<SubscriptionCartModel> getSubscriptionCartModels();

	List<CartData> getSubcriptionCartsForUser(String userId);
}

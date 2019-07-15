/**
 *
 */
package com.deloitte.facade.impl;

import de.hybris.platform.commercefacades.order.data.CartRestorationData;
import de.hybris.platform.commercefacades.order.impl.DefaultCartFacade;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;


/**
 * @author asomjal
 *
 */
public class SubscriptionDefaultCartFacade extends DefaultCartFacade
{
	@Override
	public CartRestorationData restoreSavedCart(final String guid) throws CommerceCartRestorationException
	{
		if (!hasEntries() && !hasEntryGroups())
		{
			getCartService().setSessionCart(null);
		}

		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		//final CartModel cartForGuidAndSiteAndUser = getCommerceCartService().getCartForGuidAndSiteAndUser(guid,
		//	getBaseSiteService().getCurrentBaseSite(), getUserService().getCurrentUser());
		parameter.setCart(null);

		return getCartRestorationConverter().convert(getCommerceCartService().restoreCart(parameter));
	}
}

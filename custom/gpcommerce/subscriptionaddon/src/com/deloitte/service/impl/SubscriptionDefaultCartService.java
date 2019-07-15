/**
 *
 */
package com.deloitte.service.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartFactory;
import de.hybris.platform.order.impl.DefaultCartService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Collection;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.deloitte.model.SubscriptionCartModel;


/**
 * @author asomjal
 *
 */
public class SubscriptionDefaultCartService extends DefaultCartService
{
	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	@Resource(name = "guidKeyGenerator")
	private KeyGenerator guidKeyGenerator;

	@Resource(name = "cartFactory")
	private CartFactory cartFactory;

	@Resource(name = "userService")
	private UserService userService;

	@Override
	public void removeSessionCart()
	{
		if (hasSessionCart())
		{
			final CartModel sessionCart = getSessionCart();
			getSessionService().removeAttribute(SESSION_CART_PARAMETER_NAME);
			if (!(sessionCart instanceof SubscriptionCartModel))
			{
				getModelService().remove(sessionCart);
			}
			removeRedundantCarts();
		}
	}

	/**
	 *
	 */
	private void removeRedundantCarts()
	{
		final UserModel userModel = userService.getCurrentUser();
		final Collection<CartModel> cartModelList = userModel.getCarts();
		if (CollectionUtils.isNotEmpty(cartModelList))
		{
			for (final CartModel cartModel : cartModelList)
			{
				if (!(cartModel instanceof SubscriptionCartModel))
				{
					modelService.remove(cartModel);
				}
			}
		}
	}
}

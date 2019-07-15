package de.hybris.multicountry.backoffice.widgets.actions;

import de.hybris.platform.core.model.product.ProductModel;

import java.util.function.Predicate;


/**
 * @author cyrill.pedol@sap.com
 *
 */
public class ProductUnlockAction extends AbstractProductLockAction
{
	/**
	 * Product unlocking can only be performed on product models that are unlockable by the current user.
	 * @return True or false
	 */
	@Override
	protected Predicate<Object> getPerformCondition()
	{
		return object -> object instanceof ProductModel && getProductLockService().isUnlockableByCurrentUser((ProductModel) object);
	}

	/**
	 * Unlocks the current product.
	 *
	 * @param product A non-null product
	 * @return True if the product is now unlocked, false otherwise.
	 */
	@Override
	protected boolean performAction(final ProductModel product)
	{
		getProductLockService().unlockByCurrentUser(product);
		return !getProductLockService().hasLock(product);
	}
}
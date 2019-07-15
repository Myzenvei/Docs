/**
 *
 */
package de.hybris.multicountry.backoffice.services.impl;

import de.hybris.multicountry.backoffice.services.ProductLockService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.multicountry.productcockpit.services.ProductLockingService;
import de.hybris.platform.servicelayer.user.UserService;

import org.springframework.beans.factory.annotation.Required;


/**
 * @author cyrill.pedol@sap.com
 *
 */
public class DefaultProductLockService implements ProductLockService
{

	private ProductLockingService productLockingService;

	private UserService userService;

	@Override
	public boolean isLockedForCurrentUser(final ProductModel productModel)
	{
		return getProductLockingService().isPageLockedFor(productModel, getUserService().getCurrentUser());
	}

	@Override
	public boolean isLockedByCurrentUser(final ProductModel productModel)
	{
		return productModel.getLockedBy().contains(getUserService().getCurrentUser());
	}

	@Override
	public boolean isLockableByCurrentUser(final ProductModel productModel)
	{
		return !isLockedForCurrentUser(productModel) && !isLockedByCurrentUser(productModel);
	}

	@Override
	public boolean isUnlockableByCurrentUser(final ProductModel productModel)
	{
		/*
		TODO: review the logic for this condition, currently an admin (who would normally be able to unlock) cannot remove locks from other users

		In ProductLockServiceImpl.unlockPage, unlocking could in theory be done by any user that holds a lock or by a user who is authorized to unlock.
		Instead, maybe this should be:

		return isLockedByCurrentUser(productModel) || getUserService().getCurrentUser().isAuthorizedToUnlockPages(); ???
		*/
		return !isLockedForCurrentUser(productModel) && isLockedByCurrentUser(productModel);
	}

	@Override
	public boolean hasLock(final ProductModel productModel)
	{
		return productModel.getLockedBy() != null && !productModel.getLockedBy().isEmpty();
	}

	@Override
	public void lockByCurrentUser(final ProductModel productModel)
	{
		getProductLockingService().setPageLocked(productModel, getUserService().getCurrentUser(), true);
	}

	@Override
	public void unlockByCurrentUser(final ProductModel productModel)
	{
		getProductLockingService().setPageLocked(productModel, getUserService().getCurrentUser(), false);
	}

	@Required
	public void setProductLockingService(final ProductLockingService productLockingService)
	{
		this.productLockingService = productLockingService;
	}

	public ProductLockingService getProductLockingService()
	{
		return productLockingService;
	}


	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}


	public UserService getUserService()
	{
		return userService;
	}


}

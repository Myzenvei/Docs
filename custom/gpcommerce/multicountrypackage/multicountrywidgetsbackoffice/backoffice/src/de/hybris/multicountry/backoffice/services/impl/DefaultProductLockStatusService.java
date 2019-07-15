/**
 *
 */
package de.hybris.multicountry.backoffice.services.impl;

import de.hybris.multicountry.backoffice.services.ProductLockService;
import de.hybris.multicountry.backoffice.services.ProductLockStatusService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * @author cyrill.pedol@sap.com
 *
 */
public class DefaultProductLockStatusService implements ProductLockStatusService
{
	private static final String STATUS_LOCKED = "locked";
	private static final String STATUS_OWNED = "owned";
	private static final String STATUS_UNLOCKED = "unlocked";

	private ProductLockService productLockService;


	@Override
	public String getStatusText(final ProductModel productModel)
	{
		return getProductLockService().isLockedForCurrentUser(productModel)
				? STATUS_LOCKED
				: getProductLockService().isLockedByCurrentUser(productModel) ? STATUS_OWNED : STATUS_UNLOCKED;
	}

	@Required
	public void setProductLockService(final ProductLockService productLockService)
	{
		this.productLockService = productLockService;
	}

	public ProductLockService getProductLockService()
	{
		return productLockService;
	}
}

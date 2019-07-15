/**
 *
 */
package de.hybris.multicountry.backoffice.services;

import de.hybris.platform.core.model.product.ProductModel;


/**
 * @author cyrill.pedol@sap.com
 *
 */
public interface ProductLockService
{
	boolean isLockedForCurrentUser(ProductModel productModel);

	boolean isLockedByCurrentUser(ProductModel productModel);

	boolean isLockableByCurrentUser(ProductModel productModel);

	boolean isUnlockableByCurrentUser(ProductModel productModel);

	boolean hasLock(ProductModel productModel);

	void lockByCurrentUser(ProductModel productModel);

	void unlockByCurrentUser(ProductModel productModel);
}

/**
 *
 */
package de.hybris.platform.multicountry.productcockpit.services;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;


/**
 * @author santosh.ritti
 *
 */
public interface ProductLockingService
{

	/**
	 * Method checks if the product is locked for given user. If the product is locked and was locked by given user it
	 * returns false, but if it was locked by other user it returns true.
	 *
	 * @param productModel
	 *           - product model for which lock will be checked
	 * @param userModel
	 *           - user model for which lock will be checked
	 * @return true if the product is locked for specified combination of user and product.
	 */
	boolean isPageLockedFor(ProductModel productModel, UserModel userModel);

	/**
	 * Method that allows to set and unset product lock for given combination of user and product. This method should
	 * first check if product is not locked by other user and then perform lock/unlock for given parameters. Also will
	 * perform unlock if the given user does not set lock for given product, but has permission to unlock any product.
	 *
	 * @param productModel
	 *           - product model for which the lock will be set or unset
	 * @param userModel
	 *           - user model for which the lock will be set or unset
	 * @param lock
	 *           - if set to true the lock will be set for given combination, otherwise it will be unset.
	 */
	void setPageLocked(ProductModel productModel, UserModel userModel, boolean lock);

}

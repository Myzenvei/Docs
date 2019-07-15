/**
 *
 */
package de.hybris.multicountry.backoffice.advisors;

import de.hybris.multicountry.backoffice.services.ProductLockService;
import de.hybris.platform.core.model.product.ProductModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * @author cyrill.pedol@sap.com
 *
 */
public class ProductInstancePermissionAdvisor extends AbstractMultiCountryInstancePermissionAdvisor<ProductModel>
{
	private ProductLockService productLockService;

	@Override
	public boolean isApplicableTo(final Object instance)
	{
		return instance instanceof ProductModel;
	}

	@Override
	protected boolean isObjectWritable(final ProductModel productModel)
	{
		boolean locked = false;
		if (getProductLockService().isLockedForCurrentUser(productModel))
		{
			locked = true;
		}
		return !locked;
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

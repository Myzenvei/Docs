package de.hybris.platform.multicountry.productcockpit.services.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.multicountry.productcockpit.services.ProductLockingService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;


/**
 * @author santosh.ritti
 *
 */
public class ProductLockingServiceImpl implements ProductLockingService
{
	protected final static Logger LOG = Logger.getLogger(ProductLockingServiceImpl.class.getName());

	private ModelService modelService;

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	public boolean isPageLockedFor(final ProductModel productModel, final UserModel userModel)
	{
		if (productModel == null || userModel == null)
		{
			LOG.warn("Cannot check product lock for arguments: productModel + " + productModel + ", userModel " + userModel);
			return false;
		}
		else
		{
			return CollectionUtils.isNotEmpty(productModel.getLockedBy()) && !productModel.getLockedBy().contains(userModel);
		}
	}

	@Override
	public void setPageLocked(final ProductModel productModel, final UserModel userModel, final boolean lock)
	{

		if (productModel == null || userModel == null)
		{
			LOG.warn("Cannot set/unset product lock for arguments: productModel + " + productModel + ", userModel " + userModel);
		}
		else
		{
			if (lock)
			{
				lockPage(productModel, userModel);
			}
			else
			{
				unlockPage(productModel, userModel);
			}
		}
	}

	private void lockPage(final ProductModel productModel, final UserModel userModel)
	{
		final Collection<EmployeeModel> employeeModels = productModel.getLockedBy();
		UserModel locker = null;
		for (final EmployeeModel model : employeeModels)
		{
			locker = model;
		}

		if (locker == null)
		{
			final Collection<EmployeeModel> empModel = new ArrayList<EmployeeModel>();
			empModel.add((EmployeeModel) userModel);
			productModel.setLockedBy(empModel);
			getModelService().save(productModel);
		}
		else
		{
			LOG.warn("Product " + productModel + " is already locked for user " + userModel);
		}
	}

	private void unlockPage(final ProductModel productModel, final UserModel userModel)
	{
		final Collection<EmployeeModel> employeeModels = productModel.getLockedBy();
		UserModel locker = null;
		for (final EmployeeModel model : employeeModels)
		{
			locker = model;
		}
		if (locker == null)
		{
			LOG.warn("Try to unlock Product " + productModel + " which is not locked by any user.");
		}
		else
		{
			if (userModel.equals(locker))
			{
				productModel.setLockedBy(null);
				getModelService().save(productModel);
			}
			else if (userModel.isAuthorizedToUnlockPages())
			{
				productModel.setLockedBy(null);
				getModelService().save(productModel);
				LOG.debug("Product " + productModel + " unlock forced by user " + userModel);
			}
			else
			{
				LOG.warn("No permission to unlock product " + productModel + " for user " + userModel);
			}
		}
	}

}

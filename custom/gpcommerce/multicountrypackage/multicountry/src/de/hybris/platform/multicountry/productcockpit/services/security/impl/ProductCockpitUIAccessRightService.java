/**
 *
 */
package de.hybris.platform.multicountry.productcockpit.services.security.impl;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.security.impl.DefaultUIAccessRightService;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.europe1.model.TaxRowModel;
import de.hybris.platform.multicountry.model.productavailabilitygroup.ProductAvailabilityAssignmentModel;
import de.hybris.platform.multicountry.model.productavailabilitygroup.ProductAvailabilityGroupModel;
import de.hybris.platform.multicountry.productcockpit.services.ProductLockingService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;


/**
 * @author miguel
 *
 */
public class ProductCockpitUIAccessRightService extends DefaultUIAccessRightService
{

	final static Logger LOG = Logger.getLogger(ProductCockpitUIAccessRightService.class.getName());

	private ProductLockingService productLockingService;

	private final boolean lockForm = Boolean.parseBoolean(Config.getString("productcockpit.lockform", "false"));

	/**
	 * @return the productLockingService
	 */
	public ProductLockingService getProductLockingService()
	{
		return productLockingService;
	}

	/**
	 * @param productLockingService
	 *           the productLockingService to set
	 */
	public void setProductLockingService(final ProductLockingService productLockingService)
	{
		this.productLockingService = productLockingService;
	}

	@Override
	public boolean isWritable(final ObjectType type, final TypedObject item)
	{
		final boolean accessOk = super.isWritable(type, item);
		return accessOk && isObjectWritable(item);
	}

	@Override
	public boolean isWritable(final ObjectType type, final TypedObject item, final PropertyDescriptor propDescr,
			final boolean creationMode)
	{
		final boolean accessOk = super.isWritable(type, item, propDescr, creationMode);
		return accessOk && isObjectWritable(item);
	}


	@Override
	public boolean isWritable(final ObjectType type, final PropertyDescriptor propDescr, final boolean creationMode)
	{
		return this.isWritable(type, null, propDescr, creationMode);
	}

	protected boolean isObjectWritable(final TypedObject item)
	{
		boolean locked = false;

		if (lockForm && item != null)
		{

			final List<ProductModel> productsToCheck = new ArrayList<ProductModel>();
			if (getTypeService().getBaseType("Product").isAssignableFrom(item.getType()))
			{
				productsToCheck.add((ProductModel) item.getObject());
			}

			for (final ProductModel product : productsToCheck)
			{
				if (getProductLockingService().isPageLockedFor(product, getUserService().getCurrentUser()))
				{
					locked = true;
					break;
				}
			}


			// Filter edit on ProductAvailabilityAssignment
			if (item.getObject() instanceof ProductAvailabilityAssignmentModel)
			{
				final ProductAvailabilityAssignmentModel asign = (ProductAvailabilityAssignmentModel) item.getObject();
				final ProductAvailabilityGroupModel group = asign.getAvailabilityGroup();
				if (group != null)
				{
					final Set<BaseStoreModel> baseStores = group.getStores();
					locked = true;
					for (final BaseStoreModel baseStore : baseStores)
					{
						if (checkEmployeeOnBaseStore(baseStore.getEmployees(), getUserService().getCurrentUser()))
						{
							locked = false;
							break;
						}
					}
				}
			}

			// Filter edit on PriceRow
			if (item.getObject() instanceof PriceRowModel)
			{
				final PriceRowModel priceRowModel = (PriceRowModel) item.getObject();
				final HybrisEnumValue userPriceGroup = priceRowModel.getUg();
				if (userPriceGroup != null)
				{
					final EmployeeModel currentUser = (EmployeeModel) getUserService().getCurrentUser();
					final Set<BaseStoreModel> baseStores = currentUser.getManagedStores();
					locked = true;
					for (final BaseStoreModel baseStore : baseStores)
					{
						if (baseStore.getUserPriceGroup().getCode().equals(userPriceGroup.getCode()))
						{
							locked = false;
							break;
						}
					}
				}
			}

			// Filter edit on TaxRow
			if (item.getObject() instanceof TaxRowModel)
			{
				final TaxRowModel taxRowModel = (TaxRowModel) item.getObject();
				final HybrisEnumValue userTaxGroup = taxRowModel.getUg();
				if (userTaxGroup != null)
				{
					final EmployeeModel currentUser = (EmployeeModel) getUserService().getCurrentUser();
					final Set<BaseStoreModel> baseStores = currentUser.getManagedStores();
					locked = true;
					for (final BaseStoreModel baseStore : baseStores)
					{
						if (baseStore.getTaxGroup().getCode().equals(userTaxGroup.getCode()))
						{
							locked = false;
							break;
						}
					}
				}
			}


		}

		return !locked;
	}

	protected boolean checkEmployeeOnBaseStore(final Collection<EmployeeModel> employees, final UserModel currentUser)
	{
		for (final EmployeeModel employee : employees)
		{
			if (employee.equals(currentUser))
			{
				return true;
			}
		}
		return false;
	}

}

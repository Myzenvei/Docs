/**
 * 
 */
package de.hybris.platform.multicountryhmc.hmc;

import de.hybris.platform.hmc.util.action.ActionResult;
import de.hybris.platform.hmc.webchips.DisplayState;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.multicountry.constants.MulticountryConstants;
import de.hybris.platform.multicountry.jalo.productavailabilitygroup.ProductAvailabilityAssignment;
import de.hybris.platform.multicountry.model.productavailabilitygroup.ProductAvailabilityAssignmentModel;
import de.hybris.platform.servicelayer.hmc.ServicelayerHMCCreateAction;
import de.hybris.platform.servicelayer.hmc.ServicelayerHMCHelper;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;


/**
 * @author i309827
 * 
 */
public class MultiCountyServicelayerHMCCreateAction extends ServicelayerHMCCreateAction
{

	private final ServicelayerHMCHelper helper = new ServicelayerHMCHelper();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.servicelayer.hmc.ServicelayerHMCCreateAction#create(de.hybris.platform.jalo.type.ComposedType,
	 * de.hybris.platform.hmc.webchips.DisplayState, java.util.Map, java.util.Map,
	 * de.hybris.platform.hmc.util.action.ActionResult)
	 */
	@Override
	protected ActionResult create(final ComposedType comptyp, final DisplayState displayState, final Map initialValues,
			final Map currentValues, final ActionResult actionResult)
	{

		final List<ProductAvailabilityAssignment> origPAAs = (List) currentValues
				.get(MulticountryConstants.Attributes.Product.AVAILABILITY);

		if (!Product.class.isAssignableFrom(comptyp.getJaloClass()) || origPAAs == null || origPAAs.size() == 0)
		{
			return super.create(comptyp, displayState, initialValues, currentValues, actionResult);
		}

		// currentValues.remove(MulticountryConstants.Attributes.Product.AVAILABILITY);

		// clone origPAAs 

		final ModelService modelService = helper.getModelService();

		List<ProductAvailabilityAssignmentModel> origPAAModels = new ArrayList(CollectionUtils.size(origPAAs));
		final List<ProductAvailabilityAssignmentModel> newPAAModels = new ArrayList(CollectionUtils.size(origPAAs));
		origPAAModels = modelService.getAll(origPAAs, origPAAModels);

		for (final ProductAvailabilityAssignmentModel origPAA : origPAAModels)
		{
			final ProductAvailabilityAssignmentModel cloned = modelService.clone(origPAA);
			newPAAModels.add(cloned);
			modelService.detach(origPAA);
			modelService.detach(cloned);
		}

		// let the HMCaction create the cloned product overwriting product references in paa (the old logic is executed without any variations)
		// in this method the following main actions are performed:
		// 1. cloned product insert into products
		// 2. update of product reference in paa 
		//    modified attributes like status and online/offline date are updated by hmc chip after the create method has been completed
		//    it seems like there isn't any call to paa prepare interceptor during paa update (!)

		final ActionResult result = super.create(comptyp, displayState, initialValues, currentValues, actionResult);

		// check outcome

		if (result.getResult() != ActionResult.OK)
		{
			return result;
		}

		// save cloned PAAs
		// NOTE do I need to handle exception during save?

		modelService.saveAll(newPAAModels);
		modelService.detachAll();

		return result;

		// modified paa attributes are updated by chip after the create method as been executed

	}

}

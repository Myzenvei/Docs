/**
 *
 */
package de.hybris.platform.multicountry.cscockpit.impersonation.impl;

import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.impl.DefaultImpersonationService;
import de.hybris.platform.multicountry.services.MulticountryRestrictionService;


/**
 * @author i303807
 *
 */
public class DefaultMulticountryImpersonationService extends DefaultImpersonationService
{
	private MulticountryRestrictionService multicountryRestrictionService;

	/**
	 * Sets the current product availability groups after configuring the base session.
	 * 
	 * @param context
	 *           The context
	 */
	@Override
	protected void configureSession(final ImpersonationContext context)
	{
		super.configureSession(context);
		getMulticountryRestrictionService().setCurrentProductAvailabilityGroups(context.getProductAvailabilityGroups());

	}

	/**
	 * Executes the wrapper in the execution context after setting the product availability groups. {@inheritDoc}
	 */
	@Override
	public <R, T extends Throwable> R executeInContext(final ImpersonationContext context, final Executor<R, T> wrapper) throws T
	{
		context.setProductAvailabilityGroups(getMulticountryRestrictionService().getCurrentProductAvailabilityGroup());
		return super.executeInContext(context, wrapper);
	}

	public MulticountryRestrictionService getMulticountryRestrictionService()
	{
		return multicountryRestrictionService;
	}

	public void setMulticountryRestrictionService(final MulticountryRestrictionService multicountryRestrictionService)
	{
		this.multicountryRestrictionService = multicountryRestrictionService;
	}
}

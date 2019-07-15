package de.hybris.platform.multicountry.cscockpit;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService.Executor;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultCallContextController;
import de.hybris.platform.multicountry.constants.MulticountryConstants;
import de.hybris.platform.multicountry.model.productavailabilitygroup.ProductAvailabilityGroupModel;
import de.hybris.platform.multicountry.services.MulticountryRestrictionService;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Collection;
import java.util.Collections;

import org.springframework.util.ObjectUtils;


/**
 * @author luca.zangari
 *
 */
public class DefaultMulticountryCallContextController extends DefaultCallContextController
{
	private SessionService sessionService;
	private MulticountryRestrictionService multicountryRestrictionService;

	@Override
	public boolean setCurrentSite(final TypedObject baseSite)
	{
		if (baseSite != null && baseSite.getObject() instanceof BaseSiteModel)
		{
			final BaseSiteModel newBaseSite = (BaseSiteModel) baseSite.getObject();
			addAvailabilityGroupOnSession(newBaseSite);

			//if the base site isn't the current site
			if (isValidSite(newBaseSite) && !(ObjectUtils.nullSafeEquals(newBaseSite, getBaseSiteService().getCurrentBaseSite())))
			{
				getBaseSiteService().setCurrentBaseSite(newBaseSite, false);
				final CurrencyModel currency = getSiteDefaultCurrency();
				setCallContextCurrency(getCockpitTypeService().wrapItem(currency));
				getCommonI18NService().setCurrentCurrency(currency);
				setImpersonationContextChanged();
				return true;
			}
		}
		return false;
	}

	private void addAvailabilityGroupOnSession(final BaseSiteModel newBaseSite)
	{
		for (final BaseStoreModel baseStore : newBaseSite.getStores())
		{
			final Session session = getSessionService().getCurrentSession();
			final Collection<ProductAvailabilityGroupModel> groups = baseStore.getAvailabilityGroups();
			session.setAttribute(MulticountryConstants.AVAILABILITY_GROUPS, groups != null ? groups : Collections.emptySet());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R, T extends Throwable> R executeInContext(final Executor<R, T> wrapper) throws T
	{
		final Collection<ProductAvailabilityGroupModel> groups = getMulticountryRestrictionService()
				.getCurrentProductAvailabilityGroup();
		final ImpersonationContext context = new ImpersonationContext();
		context.setUser(getCurrentCustomerModelOrAnonymousModel());
		context.setSite(getBaseSiteService().getCurrentBaseSite());
		context.setCurrency((CurrencyModel) getCallContextCurrency().getObject());
		context.setLanguage(getCommonI18NService().getLanguage(UISessionUtils.getCurrentSession().getGlobalDataLanguageIso()));
		context.setProductAvailabilityGroups(groups);
		return getImpersonationService().executeInContext(context, wrapper);
	}

	public MulticountryRestrictionService getMulticountryRestrictionService()
	{
		return multicountryRestrictionService;
	}

	public void setMulticountryRestrictionService(final MulticountryRestrictionService multicountryRestrictionService)
	{
		this.multicountryRestrictionService = multicountryRestrictionService;
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}

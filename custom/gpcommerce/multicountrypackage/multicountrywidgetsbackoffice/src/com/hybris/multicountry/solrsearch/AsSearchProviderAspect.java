/**
 *
 */
package com.hybris.multicountry.solrsearch;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsSearchQueryData;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.multicountry.model.productavailabilitygroup.ProductAvailabilityGroupModel;
import de.hybris.platform.multicountry.services.MulticountryRestrictionService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;


/**
 * @author i304602
 *
 */
public class AsSearchProviderAspect
{
	private MulticountryRestrictionService multicountryRestrictionService;
	private SessionService sessionService;





	private static final String AVAILABILITY = "availability";
	private static final String BASE_STORE = "baseStore";

	public static final String ALL_CATEGORIES_FIELD = "allCategories";



	public Object search(final ProceedingJoinPoint joinPoint) throws Throwable
	{
		if (!joinPoint.getSignature().getName().equals("search"))
		{
			return joinPoint.proceed();
		}

		if (joinPoint.getArgs().length != 2)
		{
			return joinPoint.proceed();
		}

		final AsSearchProfileContext context = (AsSearchProfileContext) joinPoint.getArgs()[0];
		final AsSearchQueryData searchQuery = (AsSearchQueryData) joinPoint.getArgs()[1];

		final Object[] params =
		{ context, searchQuery };

		addAvailibityField(searchQuery);
		addCategory(context, searchQuery);
		return joinPoint.proceed(params);
	}



	private void addCategory(final AsSearchProfileContext context, final AsSearchQueryData searchQuery)
	{
		if (CollectionUtils.isEmpty(context.getCategoryPath()))
		{
			return;
		}

		final Set<String> values = new HashSet<String>();
		final CategoryModel category = context.getCategoryPath().get(context.getCategoryPath().size() - 1);
		values.add(category.getCode());

		if (searchQuery.getFacetValues() == null)
		{
			searchQuery.setFacetValues(new HashMap<String, Set<String>>());
		}

		String solrCategoryField = ALL_CATEGORIES_FIELD;
		final BaseStoreModel store = sessionService.getAttribute(BASE_STORE);
		if (store!=null){
			solrCategoryField += "_" + store.getUid() + "_string_mv";
		}

		searchQuery.getFacetValues().put(solrCategoryField, values);
		context.getCategoryPath().clear();
	}


	private void addAvailibityField(final AsSearchQueryData searchQuery)
	{
		final Collection<ProductAvailabilityGroupModel> availabilities = multicountryRestrictionService
				.getCurrentProductAvailabilityGroup();
		if (CollectionUtils.isEmpty(availabilities))
		{
			return;
		}

		final Set<String> values = new HashSet<String>();
		for (final ProductAvailabilityGroupModel availability : availabilities)
		{
			values.add(availability.getId());
		}

		if (values.size() == 0)
		{
			return;
		}

		if (searchQuery.getFacetValues() == null)
		{
			searchQuery.setFacetValues(new HashMap<String, Set<String>>());
		}

		searchQuery.getFacetValues().put(AVAILABILITY, values);

	}




	public void setMulticountryRestrictionService(final MulticountryRestrictionService multicountryRestrictionService)
	{
		this.multicountryRestrictionService = multicountryRestrictionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}


}

/**
 *
 */
package de.hybris.platform.multicountry.productcockpit.search;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.search.impl.GenericSearchSubQuery;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.multicountry.productcockpit.selector.BaseStoreSectionSelectionSection;
import de.hybris.platform.productcockpit.services.search.impl.ProductPerspectiveQueryProvider;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.BaseStoreModel;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Extends the standard search provider to filter using the selected <code>BaseStoreModel</code>
 *
 * @author brendan
 *
 */
public class ProductCockpitSearchProvider extends ProductPerspectiveQueryProvider
{
	private static final Logger LOG = LoggerFactory.getLogger(ProductCockpitSearchProvider.class);

	private boolean propagateToVariant = true;

	private static final String PRODUCT_LEVEL_QUERY = //
	"EXISTS ({{ SELECT 1 FROM {ProductAvailabilityAssignment as paa "
			+ " JOIN ProductAvailabilityGroup as pag ON {paa:availabilityGroup} = {pag:pk}"
			+ " JOIN ProductAvailabilityGroup2BaseStoreRel as pag2bs ON {pag:pk} = {pag2bs:source} }"
			+ " WHERE {item:pk} = {paa:product} AND {pag2bs:target} = %s" + "}})";

	private static final String VARIANT_LEVEL_1_QUERY = "(" //
			+ "(NOT EXISTS({{ SELECT 1 FROM {ProductAvailabilityAssignment* as paa "
			+ "					  JOIN ProductAvailabilityGroup as pag ON {paa:availabilityGroup} = {pag:pk}"
			+ "					  JOIN ProductAvailabilityGroup2BaseStoreRel as pag2bs ON {pag:pk} = {pag2bs:source} }"
			+ "					 WHERE {item:pk} = {paa.product} "
			+ "						AND {pag2bs:target} = %s  }}) "
			+ " AND EXISTS ({{" //
			+ " 	SELECT 1 FROM {VariantProduct as vp "
			+ "			 JOIN ProductAvailabilityAssignment as paa ON {paa:product} = {vp:baseProduct}"
			+ "			 JOIN ProductAvailabilityGroup as pag ON {paa:availabilityGroup} = {pag:pk}"
			+ " 			 JOIN ProductAvailabilityGroup2BaseStoreRel as pag2bs ON {pag:pk} = {pag2bs:source} }"
			+ "	WHERE {item:pk} = {vp:pk} "
			+ "	  AND {pag2bs:target} = %s"
			+ "}})) " //
			+ "OR " //
			+ "(EXISTS ({{" //
			+ "		SELECT 1 FROM {ProductAvailabilityAssignment as paa "
			+ "			    	JOIN ProductAvailabilityGroup as pag ON {paa:availabilityGroup} = {pag:pk}"
			+ "					JOIN ProductAvailabilityGroup2BaseStoreRel as pag2bs ON {pag:pk} = {pag2bs:source} }"
			+ "		WHERE {item:pk} = {paa.product} " //
			+ "		  AND {pag2bs:target} = %s" //
			+ " }})" //
			+ " AND ("
			+ "  		NOT EXISTS ({{ SELECT 1 FROM {VariantProduct as vp} WHERE {item:pk} = {vp:pk} }})"
			+ "  		OR"
			+ "  		EXISTS ({{"
			+ " 				SELECT 1 FROM {VariantProduct as vp "
			+ "					 JOIN ProductAvailabilityAssignment as paa ON {paa:product} = {vp:baseProduct}"
			+ "					 JOIN ProductAvailabilityGroup as pag ON {paa:availabilityGroup} = {pag:pk}"
			+ "					 JOIN ProductAvailabilityGroup2BaseStoreRel as pag2bs ON {pag:pk} = {pag2bs:source} }"
			+ "			WHERE {item:pk} = {vp:pk} " //
			+ "           AND {pag2bs:target} = %s" //
			+ "}}) ) ))";

	private SessionService sessionService;

	/**
	 * Re-implementation of {@link ProductPerspectiveQueryProvider#createConditions(Query, GenericQuery)} to allow for
	 * multiple catalog versions.
	 * 
	 * @param query
	 *           The query
	 * @param genQuery
	 *           The generic query
	 * @return The conditions
	 */
	@Override
	public List<GenericCondition> createConditions(final Query query, final GenericQuery genQuery)
	{
		final List<GenericCondition> conditions = super.createConditions(query, genQuery);
		final GenericCondition baseStoreCondition = createBaseSiteCondition(query);
		if (baseStoreCondition != null)
		{
			conditions.add(baseStoreCondition);
		}
		return conditions;
	}

	@Override
	protected GenericCondition createAdvancedSearchCondition(final Query query, final GenericQuery genQuery)
	{
		if (isProductOrCategorySearch(query.getSelectedTypes()))
		{
			final List<SearchParameterValue> params = query.getParameterValues();
			final Optional<SearchParameterValue> sourceParam = params.stream().filter(this::isCatalogVersionParam).findFirst();
			if (sourceParam.isPresent()) //are we searching with catalog version?
			{
				//				final List<SearchParameterValue> newParams = copyParamsWithoutSource(params, sourceParam.get());
				//				query.setParameterValues(newParams.isEmpty() ? null : newParams);
				replaceCatalogVersionSearchParam(query, sourceParam.get());
			}
		}
		return super.createAdvancedSearchCondition(query, genQuery);
	}

	/**
	 * Replace the single catalog version search param with an IN clause for multiple versions, based on the user.
	 *
	 * @param query
	 *           The query
	 * @param source
	 *           The search parameter to replace
	 */
	protected void replaceCatalogVersionSearchParam(final Query query, final SearchParameterValue source)
	{
		if (isAdminUser()) //for admin, just remove the search parameter
		{
			final List<SearchParameterValue> newParams = copyParamsWithoutSource(query.getParameterValues(), source);
			query.setParameterValues(newParams.isEmpty() ? null : newParams);
		}
		else
		{
			//get all catalog versions for the current user based on employee->store relationship
			final Set<CatalogVersionModel> userCatalogVersions = getUserCatalogVersions();
			//replace it with a new parameter that allows all those catalogs
			final SearchParameterValue newParam = new SearchParameterValue(source.getParameterDescriptor(), userCatalogVersions,
					Operator.IN, source.getLanguage());
			//get a new list of params that excludes the original source param
			final List<SearchParameterValue> newParams = copyParamsWithoutSource(query.getParameterValues(), source);
			//add our new param
			newParams.add(newParam);
			query.setParameterValues(newParams);
		}
	}

	/**
	 * Creates a copy of the provided list without the provided source parameter.
	 * 
	 * @param params
	 *           The list
	 * @param source
	 *           The param
	 * @return A list
	 */
	protected List<SearchParameterValue> copyParamsWithoutSource(final List<SearchParameterValue> params,
			final SearchParameterValue source)
	{
		return params.stream().filter(param -> param != source).collect(Collectors.toList());
	}

	/**
	 * Is the current user admin?
	 * 
	 * @return True or false
	 */
	protected boolean isAdminUser()
	{
		return UISessionUtils.getCurrentSession().getUser().getUid().equals("admin");
	}

	/**
	 * Are we searching for a product or a category?
	 * 
	 * @param searchTypes
	 *           The search types
	 * @return True or false
	 */
	protected boolean isProductOrCategorySearch(final Set<SearchType> searchTypes)
	{
		return searchTypes.stream().anyMatch(type -> "Product".equals(type.getCode()) || "Category".equals(type.getCode()));
	}

	/**
	 * Is the provided param a catalog version parameter?
	 * 
	 * @param param
	 *           The search parameter
	 * @return True or false
	 */
	protected boolean isCatalogVersionParam(final SearchParameterValue param)
	{
		return param.getParameterDescriptor().getQualifier().equals("Category.catalogVersion")
				|| param.getParameterDescriptor().getQualifier().equals("Product.catalogVersion");
	}

	/**
	 * Find all default category catalogs the user can access for all sites where user is an employee of the related
	 * store.
	 * 
	 * @return A set of catalog versions
	 */
	protected Set<CatalogVersionModel> getUserCatalogVersions()
	{
		//query relies on the Backend_BaseStore search restriction to limit to current user's stores
		final String query = "SELECT DISTINCT {site.defaultCategoryCatalog} " + "FROM {" + "CMSSite AS site "
				+ "JOIN StoresForCMSSite AS ssrel ON {site:PK} = {ssrel:source} "
				+ "JOIN BaseStore AS store ON {store:PK} = {ssrel:target} " + "} ";
		final List<CatalogModel> catalogs = getFlexibleSearchService().<CatalogModel> search(query).getResult();
		final Set<CatalogVersionModel> catalogVersions = new HashSet<>(catalogs.size() * 2);
		catalogs.forEach(catalog -> catalogVersions.addAll(catalog.getCatalogVersions()));
		return catalogVersions;
	}

	/**
	 * User the Product2BaseStore relation to filter the products base on the selected <code>BaseStoreModel</code>
	 *
	 * @param query
	 * @return
	 */
	public GenericCondition createBaseSiteCondition(final Query query)
	{
		final BaseStoreModel baseStore = sessionService.getAttribute(BaseStoreSectionSelectionSection.SELECTED_BASESTORE);

		if (baseStore != null)
		{
			final String queryString = getProductRestrictionQuery(baseStore);

			@SuppressWarnings("deprecation")
			final GenericCondition condition = GenericSearchSubQuery.createSearchSubQuery(queryString);
			return condition;
		}
		else
		{
			return null;
		}
	}

	private String getProductRestrictionQuery(final BaseStoreModel baseStore)
	{
		final String baseStorePk = baseStore.getPk().toString();
		final String queryString;
		if (propagateToVariant)
		{
			queryString = String.format(VARIANT_LEVEL_1_QUERY, baseStorePk, baseStorePk, baseStorePk, baseStorePk);
		}
		else
		{
			queryString = String.format(PRODUCT_LEVEL_QUERY, baseStorePk);
		}
		return queryString;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public void setPropagateToVariant(final boolean propagateToVariant)
	{
		this.propagateToVariant = propagateToVariant;
	}
}

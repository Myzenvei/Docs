/**
 *
 */
package com.gp.commerce.content.search.service.solrfcetsearch.strategies.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSUtilService;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.SolrFacetSearchConfigSelectionStrategy;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.solrfacetsearch.daos.SolrFacetSearchConfigDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.content.search.service.solrfcetsearch.strategies.GPSolrFacetSearchConfigSelectionStrategy;


/**
 * /**
 * Default implementation of {@link SolrFacetSearchConfigSelectionStrategy} that checks:
 * <ul>
 * <li>SolrFacetSearchConfig that is bound with current base site</li>
 * <li>SolrFacetSearchConfig that is bound with current base store</li>
 * <li>SolrFacetSearchConfig that is bound with current content catalog versions</li>
 * </ul>
 *
 *
 */
public class GPDefaultSolrFacetSearchConfigSelectionStrategy implements GPSolrFacetSearchConfigSelectionStrategy
{
	private SolrFacetSearchConfigDao solrFacetSearchConfigDao;
	private CatalogVersionService catalogVersionService;
	@Resource(name = "cmsUtilService")
	private DefaultCMSUtilService defaultCMSUtilService;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.gp.commerce.content.search.service.solrfcetsearch.strategies.GPSolrFacetSearchConfigSelectionStrategy#
	 * getContentSolrFacetSearchConfig()
	 */
	@Override
	public SolrFacetSearchConfigModel getContentSolrFacetSearchConfig() throws NoValidSolrConfigException
	{
		final Collection<CatalogVersionModel> sessionProductCatalogVersions = getSessionCatalogVersions();
		for (final SolrFacetSearchConfigModel solrConfigModel : solrFacetSearchConfigDao.findAllFacetSearchConfigs())
		{
			if (solrConfigModel.getCatalogVersions() != null
					&& solrConfigModel.getCatalogVersions().containsAll(sessionProductCatalogVersions))
			{
				return solrConfigModel;
			}
		}
		return null;
	}

	protected Collection<CatalogVersionModel> getSessionCatalogVersions()
	{

		final Collection<CatalogVersionModel> sessionCatalogVersions = getCatalogVersionService().getSessionCatalogVersions();

		final Collection<CatalogVersionModel> result = new ArrayList();
		if (CollectionUtils.isNotEmpty(sessionCatalogVersions))
		{
			for (final CatalogVersionModel sessionCatalogVersion : sessionCatalogVersions)
			{
				if (sessionCatalogVersion != null && defaultCMSUtilService.isContentCatalog(sessionCatalogVersion))
				{
					result.add(sessionCatalogVersion);
				}
			}
		}
		return result;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	public SolrFacetSearchConfigDao getSolrFacetSearchConfigDao()
	{
		return solrFacetSearchConfigDao;
	}

	@Required
	public void setSolrFacetSearchConfigDao(final SolrFacetSearchConfigDao solrFacetSearchConfigDao)
	{
		this.solrFacetSearchConfigDao = solrFacetSearchConfigDao;
	}


}

/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import com.gp.commerce.core.dao.GPBundleTemplateDao;
import de.hybris.platform.configurablebundleservices.daos.impl.DefaultBundleTemplateDao;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import javax.annotation.Nonnull;


/**
 * Default implementation of the {@link GPBundleTemplateDao}.
 */
public class GPDefaultBundleTemplateDao extends DefaultBundleTemplateDao implements GPBundleTemplateDao
{
	private static final String RESTRICTION_ONLY_ONLINE = " AND {catalogversion} IN ( "
			+ " {{ select {pk} from {CatalogVersion} where {version} = 'Online' }} "
			+ ")";

	private static final String FIND_BUNDLETEMPLATE_QUERY_BY_VERSION = "SELECT {" + BundleTemplateModel.PK + "} FROM {"
			+ BundleTemplateModel._TYPECODE + "} where {" + BundleTemplateModel.ID + "}= ?uid and  {" + BundleTemplateModel.VERSION
			+ "}=?version" + RESTRICTION_ONLY_ONLINE;

	@Override
	@Nonnull
	public BundleTemplateModel findBundleTemplateByIdAndVersion(final String bundleId, final String version)
	{
		validateParameterNotNullStandardMessage("bundleId", bundleId);
		validateParameterNotNullStandardMessage("version", version);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_BUNDLETEMPLATE_QUERY_BY_VERSION);
		query.addQueryParameter("uid", bundleId);
		query.addQueryParameter("version", version);
		return searchUnique(query);
	}
}

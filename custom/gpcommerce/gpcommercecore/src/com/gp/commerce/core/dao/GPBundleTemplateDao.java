/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package com.gp.commerce.core.dao;

import de.hybris.platform.configurablebundleservices.daos.BundleTemplateDao;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import javax.annotation.Nonnull;
import java.lang.Override;
/**
 * Data Access Object for looking up items related to {@link BundleTemplateModel}.
 *
 * @spring.bean bundleTemplateDao
 */

public interface GPBundleTemplateDao extends BundleTemplateDao
{

	/**
	 * This method returns the {@code BundleTemplateModel} corresponding to the bundleID and version
	 *
	 * @param bundleId
	 * @param version
	 *           Bundle Version
	 * @return {@link BundleTemplateModel}
	 */
	@Nonnull
	@Override
	BundleTemplateModel findBundleTemplateByIdAndVersion(String bundleId, String version);

}

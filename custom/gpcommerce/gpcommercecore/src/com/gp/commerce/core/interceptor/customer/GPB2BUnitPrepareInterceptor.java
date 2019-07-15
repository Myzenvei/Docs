/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.core.interceptor.customer;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import javax.annotation.Resource;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

/**
 * Add Category to B2BUnit
 */
public class GPB2BUnitPrepareInterceptor implements PrepareInterceptor {

	public final static String ONLINE_CATALOG_VERSION = "Online";
	private static final String DEFAULT_ROOTCATEGORY = "b2bunit.default.rootCategory";

	private CatalogVersionModel onlineCatalogVersionModel;
	private String gpDefaultCatalogId;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "categoryService")
	private CategoryService categoryService;

	@Resource(name = "catalogVersionService")
	private CatalogVersionService catalogVersionService;

	@Resource(name = "modelService")
	private ModelService modelService;
	
	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;
	
	@Resource(name = "sessionService")
	private SessionService sessionService;
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.servicelayer.interceptor.PrepareInterceptor#onPrepare(java
	 * .lang.Object, de.hybris.platform.servicelayer.interceptor.InterceptorContext)
	 */
	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException {
		final B2BUnitModel unit = (B2BUnitModel) model;

		if (model instanceof B2BUnitModel) {
			this.onlineCatalogVersionModel = catalogVersionService.getCatalogVersion(gpDefaultCatalogId, ONLINE_CATALOG_VERSION);

			// setting Default Category to b2bUnit
			if (ctx.getModelService().isNew(model)) {
				sessionService.executeInLocalView(new SessionExecutionBody()
				{
					@Override
					public void executeWithoutResult()
					{
						searchRestrictionService.disableSearchRestrictions();
						final CategoryModel category = categoryService.getCategoryForCode(onlineCatalogVersionModel,
								configurationService.getConfiguration().getString(DEFAULT_ROOTCATEGORY));
						unit.setRootCategoryReference(category);

					}
				});
			}
		}

	}

	/**
	 * @return the gpDefaultCatalogId
	 */
	public String getGpDefaultCatalogId() {
		return gpDefaultCatalogId;
	}

	/**
	 * @param gpDefaultCatalogId the gpDefaultCatalogId to set
	 */
	public void setGpDefaultCatalogId(String gpDefaultCatalogId) {
		this.gpDefaultCatalogId = gpDefaultCatalogId;
	}

}

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
package com.gp.commerce.b2b.storefront.breadcrumbs;

import java.util.Collection;

import com.gp.commerce.core.model.GPDefaultVariantCategoryModel;
import com.gp.commerce.core.model.GPDefaultVariantValueCategoryModel;
import com.gp.commerce.core.util.GPCommerceCoreUtils;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.ProductBreadcrumbBuilder;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.variants.model.VariantCategoryModel;
import de.hybris.platform.variants.model.VariantValueCategoryModel;

public class GPProductBreadcrumbBuilder extends ProductBreadcrumbBuilder {
	
	@Override
	protected CategoryModel processCategoryModels(final Collection<CategoryModel> categoryModels, final CategoryModel toDisplay)
	{
		CategoryModel categoryToDisplay = toDisplay;

		for (final CategoryModel categoryModel : categoryModels)
		{
			if(GPCommerceCoreUtils.isDisplayCategory(categoryModel)) {
				if (categoryToDisplay == null) {
					categoryToDisplay = categoryModel;
				}
				if (getBrowseHistory().findEntryMatchUrlEndsWith(categoryModel.getCode()) != null) {
					break;
				}
			}
		}
		return categoryToDisplay;
	}

}

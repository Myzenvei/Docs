/**
 *
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.DefaultCategorySource;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Overriding the method collectSuperCategries to not collect root category in facet values.
 */
public class GPDefaultCategorySource extends DefaultCategorySource
{

	@Override
	protected Set<CategoryModel> collectSuperCategories(final CategoryModel category, final Set<CategoryModel> rootCategories,
			final Set<CategoryModel> path)
	{
		if (category == null || isBlockedCategory(category))
		{
			// If this category is blocked or null then return empty set as this whole branch is not viable
			return Collections.emptySet();
		}

		if (path.contains(category))
		{
			// Loop detected, category has already been seen. this whole branch is not viable
			return Collections.emptySet();
		}

		// This category is ok, so add it to our path
		path.add(category);

		if (rootCategories.contains(category))
		{
			// We have found the root, so that is the end of this path
			//dont consider root categories for display
			path.remove(category);

			return path;
		}
		else
		{
			final List<CategoryModel> superCategories = category.getSupercategories();
			if (superCategories == null || superCategories.isEmpty())
			{
				// No super categories, and we haven't found the root yet, so this whole branch is not viable
				return Collections.emptySet();
			}

			if (superCategories.size() == 1)
			{
				// Optimization for 1 super-category we can reuse our 'path' set
				return collectSuperCategories(superCategories.iterator().next(), rootCategories, path);
			}
			else
			{
				final HashSet<CategoryModel> result = new HashSet<CategoryModel>();

				for (final CategoryModel superCategory : superCategories)
				{
					if (!isBlockedCategory(superCategory))
					{
						// Collect the super category branch for each super-category with a copy of the path so far
						// Combine together the results
						result.addAll(collectSuperCategories(superCategory, rootCategories, new HashSet<CategoryModel>(path)));
					}
				}

				return result;
			}
		}
	}

}

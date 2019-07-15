/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.gp.commerce.core.model.GPDefaultVariantCategoryModel;
import com.gp.commerce.core.model.GPDefaultVariantValueCategoryModel;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.contextualattributevalues.model.ContextualAttributeValueModel;
import de.hybris.platform.contextualattributevalues.services.ContextualAttributeValuesSessionService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.model.VariantCategoryModel;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantValueCategoryModel;

public class GPCommerceCoreUtils {
	
	private ContextualAttributeValuesSessionService contextualAttributeValuesSessionService;
	private ModelService modelService;
	private TypeService typeService;
	
	private GPCommerceCoreUtils() {
		
	}

	public static void getProductRootCategories(final Collection<CategoryModel> rootCategories,
			final Collection<CategoryModel> categories) {
		if (categories != null && !categories.isEmpty()) {
			for (CategoryModel category : categories) {
				if (category.getAllSupercategories() != null && !category.getAllSupercategories().isEmpty()) {
					int index = 0;
					for(CategoryModel supercat: category.getAllSupercategories())
					{
						if ((supercat instanceof ClassificationClassModel)
								|| (supercat instanceof VariantCategoryModel)) {
							index++;
						}						
					}
					if(index == category.getAllSupercategories().size())
					{
						rootCategories.add(category);
					}
					else
					{
						getProductRootCategories(rootCategories, category.getAllSupercategories());
					}
				} else if (!(category instanceof ClassificationClassModel)
						&& !(category instanceof VariantCategoryModel)) {
					rootCategories.add(category);
				}
			}
		}
	}

	public Object getProductAttribute(final ProductModel productModel, final String attribute)
	{
		final ComposedTypeModel ct = getTypeService().getComposedTypeForClass(ContextualAttributeValueModel.class);

		Object defaultValue = getModelService().getAttributeValue(productModel, attribute);
		Object contextualValue = null;
		if (defaultValue == null && productModel instanceof VariantProductModel)
		{
			final ProductModel baseProduct = ((VariantProductModel) productModel).getBaseProduct();
			if (baseProduct != null)
			{
				defaultValue = getProductAttribute(baseProduct, attribute);
			}
		}

		if (!productModel.getContextualAttributeValues().isEmpty()
				|| getContextualAttributeValuesSessionService().getCurrentContext() != null)
		{
			for (final ContextualAttributeValueModel contextualModel : productModel.getContextualAttributeValues())
			{
				if (contextualModel.getContext().equals(getContextualAttributeValuesSessionService().getCurrentContext())
						&& attribute != ProductModel.CATALOGVERSION)
				{

					if (getTypeService().hasAttribute(ct, attribute))
					{
						contextualValue = getModelService().getAttributeValue(contextualModel, attribute);
					}
				}
			}

		}


		if (contextualValue != null)
		{
			if (contextualValue instanceof Collection && ((Collection) contextualValue).isEmpty())
			{
				contextualValue = null;
			}

		}


		return contextualValue != null ? contextualValue : defaultValue;
	}
	
	public static List<CategoryModel> getDisplayCategories(Collection<CategoryModel> categoryModels) {
		List<CategoryModel> displayCategories = new ArrayList<CategoryModel>();
		for (final CategoryModel categoryModel : categoryModels)
		{
			if(isDisplayCategory(categoryModel))
			{
				displayCategories.add(categoryModel);
			}
		}
		return displayCategories;
	}

	public static boolean isDisplayCategory(final CategoryModel categoryModel) {
		if (!(categoryModel instanceof ClassificationClassModel || categoryModel instanceof VariantCategoryModel
				|| categoryModel instanceof GPDefaultVariantCategoryModel
				|| categoryModel instanceof VariantValueCategoryModel
				|| categoryModel instanceof GPDefaultVariantValueCategoryModel)
				&& (null != categoryModel.getCategorizationCategory()
						&& categoryModel.getCategorizationCategory().equals(Boolean.FALSE))) {
			return true;
		}
		return false;
	}
	
	public static ProductModel getBaseProduct(final ProductModel product)
	{
		if (product instanceof VariantProductModel)
		{
			return getBaseProduct(((VariantProductModel) product).getBaseProduct());
		}
		return product;
	}

	public ContextualAttributeValuesSessionService getContextualAttributeValuesSessionService() {
		return contextualAttributeValuesSessionService;
	}

	public void setContextualAttributeValuesSessionService(ContextualAttributeValuesSessionService contextualAttributeValuesSessionService) {
		this.contextualAttributeValuesSessionService = contextualAttributeValuesSessionService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	public TypeService getTypeService() {
		return typeService;
	}

	public void setTypeService(TypeService typeService) {
		this.typeService = typeService;
	}
}

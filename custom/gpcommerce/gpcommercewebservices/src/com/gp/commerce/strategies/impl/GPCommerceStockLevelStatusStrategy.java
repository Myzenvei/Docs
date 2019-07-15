/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.strategies.impl;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commerceservices.product.impl.DefaultCommerceProductReferenceService;
import de.hybris.platform.commerceservices.stock.strategies.impl.CommerceStockLevelStatusStrategy;
import de.hybris.platform.contextualattributevalues.model.ContextualAttributeValueModel;
import de.hybris.platform.contextualattributevalues.services.ContextualAttributeValuesSessionService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.model.VariantProductModel;
import com.gp.commerce.core.util.GPCommerceCoreUtils;


public class GPCommerceStockLevelStatusStrategy extends CommerceStockLevelStatusStrategy {
	private ProductService productService;
	private ContextualAttributeValuesSessionService contextualAttributeValuesSessionService;
	private ModelService modelService;
	private TypeService typeService;

	public ProductService getProductService() {
		return productService;
	}
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
	
	@Override
	public StockLevelStatus checkStatus(final StockLevelModel stockLevel)
	{
		
		StockLevelStatus resultStatus = StockLevelStatus.OUTOFSTOCK;
		if (stockLevel == null)
		{
			resultStatus = StockLevelStatus.OUTOFSTOCK;
		}
		else if (InStockStatus.FORCEINSTOCK.equals(stockLevel.getInStockStatus()))
		{
			resultStatus = StockLevelStatus.INSTOCK;
		}
		else if (InStockStatus.FORCEOUTOFSTOCK.equals(stockLevel.getInStockStatus()))
		{
			resultStatus = StockLevelStatus.OUTOFSTOCK;
		}
		else
		{
			String code = stockLevel.getProductCode();
			final ProductModel productModel = getProductService().getProductForCode(code);
			Integer stockLevelValue = 0;
			if(null!= getProductAttribute(productModel, ProductModel.LOWSTOCKLEVELTHRESHOLD)) {
				stockLevelValue = (Integer) getProductAttribute(productModel, ProductModel.LOWSTOCKLEVELTHRESHOLD);
			}
			
			final int result = getCommerceStockLevelCalculationStrategy().calculateAvailability(
					Collections.singletonList(stockLevel)).intValue();
			if (result <= 0)
			{
				resultStatus = StockLevelStatus.OUTOFSTOCK;
			}
			else if (result > stockLevelValue)
			{
				resultStatus = StockLevelStatus.INSTOCK;
			}
			else
			{
				resultStatus = StockLevelStatus.LOWSTOCK;
			}
		}

		return resultStatus;
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
				|| contextualAttributeValuesSessionService.getCurrentContext() != null)
		{
			for (final ContextualAttributeValueModel contextualModel : productModel.getContextualAttributeValues())
			{
				if (contextualModel.getContext().equals(contextualAttributeValuesSessionService.getCurrentContext())
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
	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the typeService
	 */
	protected TypeService getTypeService()
	{
		return typeService;
	}

	/**
	 * @param typeService
	 *           the typeService to set
	 */
	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	/**
	 * @return the contextualAttributeValuesSessionService
	 */
	protected ContextualAttributeValuesSessionService getContextualAttributeValuesSessionService()
	{
		return contextualAttributeValuesSessionService;
	}

	/**
	 * @param contextualAttributeValuesSessionService
	 *           the contextualAttributeValuesSessionService to set
	 */
	@Required
	public void setContextualAttributeValuesSessionService(
			final ContextualAttributeValuesSessionService contextualAttributeValuesSessionService)
	{
		this.contextualAttributeValuesSessionService = contextualAttributeValuesSessionService;
	}

}

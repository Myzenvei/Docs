/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.product.converters.populator;

import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.variants.model.VariantProductModel;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import com.gp.commerce.core.model.GPCommerceSizeVariantProductModel;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;

/**
 *   Populates default size variant URL for style variant product
 */
public class GPVariantOptionDataPopulator  implements Populator<VariantProductModel, VariantOptionData>
{
	private UrlResolver<ProductModel> productModelUrlResolver;
	private ConfigurationService configurationService;

	/**
	 * @return
	 */
	protected UrlResolver<ProductModel> getProductModelUrlResolver()
	{
		return productModelUrlResolver;
	}

	/**
	 * @param productModelUrlResolver
	 */
	public void setProductModelUrlResolver(final UrlResolver<ProductModel> productModelUrlResolver)
	{
		this.productModelUrlResolver = productModelUrlResolver;
	}

	/**
	 * @return
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	@Override
	public void populate(final VariantProductModel source, final VariantOptionData target)
	{
		Assert.notNull(source, GpcommerceFacadesConstants.SOURCE_VALIDATION_ERROR);
		Assert.notNull(target, GpcommerceFacadesConstants.TARGET_VALIDATION_ERROR);

		if (source.getBaseProduct() != null && CollectionUtils.isNotEmpty(source.getVariants()) && source.getVariants().iterator().hasNext())
		{
				target.setUrl(getProductModelUrlResolver().resolve(getSizeVariantProductForStyle(source)));
			}
		}

	private VariantProductModel getSizeVariantProductForStyle(final VariantProductModel source)
	{
		final String defaultSize = configurationService.getConfiguration().getString("variant.size.default");
		VariantProductModel defaultProduct = null;

		for(final VariantProductModel product: source.getVariants())
		{
			if(product instanceof GPCommerceSizeVariantProductModel && !((GPCommerceSizeVariantProductModel)product).getSize().isEmpty() && ((GPCommerceSizeVariantProductModel)product).getSize().equalsIgnoreCase(defaultSize))
			{
					defaultProduct = product;
			}

		}
		if(null == defaultProduct)
		{
			defaultProduct = source.getVariants().iterator().next();
		}
		return defaultProduct;

	}

}

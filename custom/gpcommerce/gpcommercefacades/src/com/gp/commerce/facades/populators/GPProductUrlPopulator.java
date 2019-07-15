/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.commercefacades.product.converters.populator.ProductUrlPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.variants.model.VariantProductModel;

public class GPProductUrlPopulator extends ProductUrlPopulator implements Populator<ProductModel, ProductData>{

	private Converter<VariantProductModel, VariantOptionData> variantOptionDataConverter;
	
	private UrlResolver<ProductModel> productModelUrlResolver;
	
	@Override
	public UrlResolver<ProductModel> getProductModelUrlResolver() {
		return productModelUrlResolver;
	}

	@Override
	public void setProductModelUrlResolver(UrlResolver<ProductModel> productModelUrlResolver) {
		this.productModelUrlResolver = productModelUrlResolver;
	}

	@Override
	public void populate(final ProductModel source, final ProductData target)
	{
		super.populate(source, target);
		
		if (CollectionUtils.isNotEmpty(source.getVariants())){
			target.setHasVariant(Boolean.TRUE);
			final List<VariantOptionData> variantOptions = new ArrayList<VariantOptionData>();
			for (final VariantProductModel variantProductModel : source.getVariants())
			{
				variantOptions.add(getVariantOptionDataConverter().convert(variantProductModel));
			}
			target.setVariantOptions(variantOptions);	
		}
	}

	public Converter<VariantProductModel, VariantOptionData> getVariantOptionDataConverter() {
		return variantOptionDataConverter;
	}

	public void setVariantOptionDataConverter(Converter<VariantProductModel, VariantOptionData> variantOptionDataConverter) {
		this.variantOptionDataConverter = variantOptionDataConverter;
	}
}

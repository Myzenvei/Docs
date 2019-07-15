/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facade.configurablebundle.populator;

import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;
import com.gp.commerce.facades.populators.GPCommerceProductPopulator;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.configurablebundleservices.model.BundleSelectionCriteriaModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.configurablebundleservices.model.PickExactlyNBundleSelectionCriteriaModel;
import de.hybris.platform.configurablebundleservices.model.PickNToMBundleSelectionCriteriaModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.commercefacades.product.data.StockData;

public class GPBundlePopulator implements Populator<BundleTemplateModel, BundleTemplateData> {


	GPCommerceProductPopulator gpCommerceProductPopulator;

    private Converter<ProductModel, StockData> stockConverter;

    private Converter<ProductModel, ProductData> productConverter;

    @Override
    public void populate(BundleTemplateModel rootBundleTemplate, BundleTemplateData bundleTemplateData) throws ConversionException {

        if(rootBundleTemplate != null) {
        	if(bundleTemplateData == null) {
        		bundleTemplateData = new BundleTemplateData();
        	}
            bundleTemplateData.setName(rootBundleTemplate.getName());
            bundleTemplateData.setId(rootBundleTemplate.getId());
            bundleTemplateData.setDescription(rootBundleTemplate.getDescription(Locale.US));
            bundleTemplateData.setBundleTemplates(setChildBundles(rootBundleTemplate.getChildTemplates()));
        }
    }

    protected List<BundleTemplateData> setChildBundles(List<BundleTemplateModel> childTemplates) {
        List<BundleTemplateData> bundleTemplateDTOList = new ArrayList<BundleTemplateData>();
        childTemplates.stream().forEach(childTemplate -> {
            BundleTemplateData bundleTemplateDTO = new BundleTemplateData();
            bundleTemplateDTO.setName(childTemplate.getName(Locale.US));
            bundleTemplateDTO.setId(childTemplate.getId());
            bundleTemplateDTO.setProducts(buildProductList(childTemplate));
            bundleTemplateDTO = setupMinMax(childTemplate,bundleTemplateDTO);
            bundleTemplateDTOList.add(bundleTemplateDTO);

        });
        return bundleTemplateDTOList;
    }

    protected List<ProductData> buildProductList(BundleTemplateModel childTemplate) {
        List<ProductModel> productList = childTemplate.getProducts();
        List <ProductData> productDataList = new ArrayList<ProductData>();
		if(productList != null) {
            
			for(ProductModel productModel : productList) {
				ProductData productData = getProductConverter().convert(productModel);
                productData.setStock(getStockConverter().convert(productModel));
				productDataList.add(productData);
            }
        }
		return productDataList;
    }

    protected BundleTemplateData setupMinMax(BundleTemplateModel childTemplate, BundleTemplateData bundleTemplateDTO) {

        BundleSelectionCriteriaModel bundleSelectionCriteria = childTemplate.getBundleSelectionCriteria();
        if(bundleTemplateDTO == null) {
        	bundleTemplateDTO = new BundleTemplateData();
        }


        if (bundleSelectionCriteria.getClass().equals(PickExactlyNBundleSelectionCriteriaModel.class)) {
            PickExactlyNBundleSelectionCriteriaModel pickExactlyNCriteria = (PickExactlyNBundleSelectionCriteriaModel) bundleSelectionCriteria;
            bundleTemplateDTO.setMinItemsAllowed(pickExactlyNCriteria.getN());
            bundleTemplateDTO.setMaxItemsAllowed(pickExactlyNCriteria.getN());
            bundleTemplateDTO.setStarter(pickExactlyNCriteria.isStarter());
        }


        if (bundleSelectionCriteria.getClass().equals(PickNToMBundleSelectionCriteriaModel.class)) {
            PickNToMBundleSelectionCriteriaModel pickNToMCriteria = (PickNToMBundleSelectionCriteriaModel) bundleSelectionCriteria;
            bundleTemplateDTO.setMinItemsAllowed(pickNToMCriteria.getN());
            bundleTemplateDTO.setMaxItemsAllowed(pickNToMCriteria.getM());
            bundleTemplateDTO.setStarter(pickNToMCriteria.isStarter());
        }
        return bundleTemplateDTO;
    }

	public GPCommerceProductPopulator getGpCommerceProductPopulator() {
		return gpCommerceProductPopulator;
	}

	public void setGpCommerceProductPopulator(GPCommerceProductPopulator gpCommerceProductPopulator) {
		this.gpCommerceProductPopulator = gpCommerceProductPopulator;
	}

    public Converter<ProductModel, StockData> getStockConverter() {
        return stockConverter;
    }

    public void setStockConverter(Converter<ProductModel, StockData> stockConverter) {
        this.stockConverter = stockConverter;
    }

    public Converter<ProductModel, ProductData> getProductConverter() {
        return productConverter;
      }

    public void setProductConverter(Converter<ProductModel, ProductData> productConverter) {
        this.productConverter = productConverter;
    }
  
}

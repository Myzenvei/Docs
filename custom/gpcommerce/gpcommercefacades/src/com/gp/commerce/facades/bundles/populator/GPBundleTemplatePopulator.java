/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.bundles.populator;

import javax.annotation.Resource;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;
import de.hybris.platform.configurablebundleservices.model.BundleSelectionCriteriaModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.configurablebundleservices.model.PickExactlyNBundleSelectionCriteriaModel;
import de.hybris.platform.configurablebundleservices.model.PickNToMBundleSelectionCriteriaModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

public class GPBundleTemplatePopulator implements Populator<BundleTemplateModel, BundleTemplateData>{

	@Resource(name = "imageConverter")
	private Converter<MediaModel, ImageData> imageConverter;
	
	
	public void populate(BundleTemplateModel source, BundleTemplateData target) throws ConversionException {
		
		if(null!=source.getParentTemplate() && null != source.getParentTemplate().getBundleImage()){
			target.setBundleImage(imageConverter.convertAll(source.getParentTemplate().getBundleImage().getMedias()));
		}
		target.setMinItemsAllowed(getMinValue(source));
	}

	/**
	 *  Find the minimum number of products to be selected for the Bundle Template
	 * @param bundleTemplate
	 */
	private int getMinValue(BundleTemplateModel bundleTemplate) {
		int result = 0;

		BundleSelectionCriteriaModel bundleSelectionCriteria = bundleTemplate.getBundleSelectionCriteria();
		if(bundleSelectionCriteria == null){
			result = 0;
		}else if (bundleSelectionCriteria.getClass().equals(PickExactlyNBundleSelectionCriteriaModel.class)) {
			PickExactlyNBundleSelectionCriteriaModel pickExactlyNCriteria = (PickExactlyNBundleSelectionCriteriaModel) bundleSelectionCriteria;
			result = pickExactlyNCriteria.getN();
		}else if (bundleSelectionCriteria.getClass().equals(PickNToMBundleSelectionCriteriaModel.class)) {
			PickNToMBundleSelectionCriteriaModel pickNToMCriteria = (PickNToMBundleSelectionCriteriaModel) bundleSelectionCriteria;
			result = pickNToMCriteria.getN();
		}
		return result;
	}
}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.order.hook.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import com.gp.commerce.core.model.GPOrderEntryAttributeModel;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.hook.CommerceUpdateCartEntryHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;

public class GPCommerceUpdateCartMethodHook implements CommerceUpdateCartEntryHook{


	private ModelService modelService;

	@Override
	public void afterUpdateCartEntry(CommerceCartParameter parameter, CommerceCartModification result) {
		if(CollectionUtils.isNotEmpty(result.getEntry().getAdditionalAttributes()) && MapUtils.isNotEmpty(parameter.getAdditionalAttributes())) {
				Collection<GPOrderEntryAttributeModel>  entryAttributes= result.getEntry().getAdditionalAttributes();
				for(Map.Entry<String, String> attributes:parameter.getAdditionalAttributes().entrySet()) {
					 GPOrderEntryAttributeModel attributeModel = entryAttributes.stream().filter(entry -> entry.getName().equalsIgnoreCase(attributes.getKey())).findFirst().orElse(null);
					 if(null != attributeModel) {
							attributeModel.setValue(attributes.getValue());
							getModelService().save(attributeModel);
					 }
				}
			}
		if(MapUtils.isNotEmpty(parameter.getAdditionalAttributes())) {
			Collection<GPOrderEntryAttributeModel> additionalAttributes = new ArrayList<>();
			for (Map.Entry<String, String> entry : parameter.getAdditionalAttributes().entrySet()) {
				 GPOrderEntryAttributeModel additionalAttribute = getModelService().create(GPOrderEntryAttributeModel.class);
				 additionalAttribute.setName(entry.getKey());
				 additionalAttribute.setValue(entry.getValue());
				 additionalAttributes.add(additionalAttribute);
			}
			AbstractOrderEntryModel entry =  result.getEntry();
			if(CollectionUtils.isNotEmpty(entry.getAdditionalAttributes())) {
				 additionalAttributes.addAll(entry.getAdditionalAttributes());
			}
			modelService.saveAll(additionalAttributes);
			entry.setAdditionalAttributes(additionalAttributes);
			modelService.save(entry);
			modelService.refresh(entry);
		}
}

	@Override
	public void beforeUpdateCartEntry(CommerceCartParameter parameter) {
		// TODO Auto-generated method stub

	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

}

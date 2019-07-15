/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.order.hook.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.model.GPCommerceProductModel;
import com.gp.commerce.core.model.GPCommerceSizeVariantProductModel;
import com.gp.commerce.core.model.GPCommerceStyleVariantProductModel;
import com.gp.commerce.core.model.GPOrderEntryAttributeModel;
import com.gp.commerce.core.model.GPServiceProductModel;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;

@UnitTest
public class GPCommerceUpdateCartMethodHookTest {
	
	
	@InjectMocks
	private final GPCommerceUpdateCartMethodHook gpCommerceUpdateCartMethodHook = new GPCommerceUpdateCartMethodHook();
	
	@Mock
	private ModelService modelService;
	
	@Before
	public void setUp()
	{
		
		MockitoAnnotations.initMocks(this);
		when(modelService.create(GPOrderEntryAttributeModel.class)).thenReturn(new GPOrderEntryAttributeModel());
	}
	
	@Test
	public void testAfterAddToCart() throws CommerceCartModificationException{
		
		//method call with data
		gpCommerceUpdateCartMethodHook.afterUpdateCartEntry(getCommerceCartParameters(), getCommerceCartModification());

		//method call without data
		gpCommerceUpdateCartMethodHook.afterUpdateCartEntry(new CommerceCartParameter(), getEmptyCartModificationData());

	}

	private CommerceCartParameter getCommerceCartParameters() {
		
		CommerceCartParameter parameters = new CommerceCartParameter();
		Map<String, String> additionalAttributes = new HashMap<>(1);
		additionalAttributes.put("key1", "value1");
		parameters.setAdditionalAttributes(additionalAttributes);
		return parameters;
	}
	
	private CommerceCartModification getCommerceCartModification() {
		
		CommerceCartModification commerceCartModification = new CommerceCartModification();
		AbstractOrderEntryModel abstractOrderEntryModel = new AbstractOrderEntryModel();
		GPCommerceSizeVariantProductModel sizeVariantProductModel = new GPCommerceSizeVariantProductModel();
		GPCommerceStyleVariantProductModel styleVariantProductModel = new GPCommerceStyleVariantProductModel();
		GPCommerceProductModel gpCommerceProductModel = new GPCommerceProductModel();
		GPServiceProductModel gpServiceProductModel = new GPServiceProductModel();
		gpServiceProductModel.setCode("H00023");
		List<GPOrderEntryAttributeModel> orderEntryAttributeModelList = new ArrayList<>();
		GPOrderEntryAttributeModel orderEntryAttributeModel = new GPOrderEntryAttributeModel();
		orderEntryAttributeModel.setName("key1");
		orderEntryAttributeModel.setValue("value2"); 
		orderEntryAttributeModelList.add(orderEntryAttributeModel);
		abstractOrderEntryModel.setAdditionalAttributes(orderEntryAttributeModelList);
		sizeVariantProductModel.setBaseProduct(styleVariantProductModel);
		gpCommerceProductModel.setInstallationProduct(gpServiceProductModel);
		gpCommerceProductModel.setGiftWrapProduct(gpServiceProductModel);
		styleVariantProductModel.setBaseProduct(gpCommerceProductModel);
		abstractOrderEntryModel.setProduct(sizeVariantProductModel);
		commerceCartModification.setEntry(abstractOrderEntryModel);
		
		return commerceCartModification;
	}
	
	private CommerceCartModification getEmptyCartModificationData() {
		
		CommerceCartModification commerceCartModification = new CommerceCartModification();
		AbstractOrderEntryModel abstractOrderEntryModel = new AbstractOrderEntryModel();
		commerceCartModification.setEntry(abstractOrderEntryModel);
		
		return commerceCartModification;
	}

}
/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.storefront.controllers.cms;

import de.hybris.platform.acceleratorcms.model.components.ProductReferencesComponentModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.model.GPProductReferencesComponentModel;
import com.gp.commerce.core.util.GPFunctions;

@Controller("GPProductReferencesComponent")
@RequestMapping(value = "/view/" + GPProductReferencesComponentModel._TYPECODE + "Controller")
public class GPProductReferencesComponentController extends ProductReferencesComponentController
{
	@Resource(name = "productVariantFacade")
	private ProductFacade productFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final ProductReferencesComponentModel component)
	{
		if (component instanceof GPProductReferencesComponentModel)
		{
			final GPProductReferencesComponentModel gpComponent = (GPProductReferencesComponentModel) component;
			final String productCodes = gpComponent.getProductCodes();
			if (StringUtils.isNotEmpty(productCodes))
			{
				final List<String> products = Arrays.asList(productCodes.split(","));
				final List<ProductData> productDataList = new ArrayList<>();
				for (final String product : products)
				{
					productDataList.add(productFacade.getProductForCodeAndOptions(product, PRODUCT_OPTIONS));
				}
				model.addAttribute("title", gpComponent.getTitle());
				model.addAttribute("products", GPFunctions.convertToJSON(productDataList));
			}
			else
			{
				super.fillModel(request, model, gpComponent);
			}
		}
	}
}

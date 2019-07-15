/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.controllers.cms;

import de.hybris.platform.acceleratorfacades.productcarousel.ProductCarouselFacade;
import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.model.GPProductCarouselComponentModel;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.facades.component.data.GPImagetileComponentdata;
import com.gp.commerce.facades.component.data.GPProductCarouseldata;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultProductCarouselFacade;
import com.gp.commerce.gpcommerceaddon.populators.GPProductCarouselPopulator;


/**
 * A Component controller for gp product carousel component.
 */
@Controller("GPProductCarouselComponentController")
@RequestMapping(value = "/view/" + GPProductCarouselComponentModel._TYPECODE + "Controller")
public class GPProductCarouselComponentController extends AbstractCMSAddOnComponentController<GPProductCarouselComponentModel>
{

	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE);

	@Resource(name = "productSearchFacade")
	private ProductSearchFacade<ProductData> productSearchFacade;

	@Resource(name = "productCarouselFacade")
	private ProductCarouselFacade productCarouselFacade;
	
	@Resource(name = "gpProductCarouselFacade")
	GPDefaultProductCarouselFacade gpProductCarouselFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPProductCarouselComponentModel component)
	{

		final List<ProductData> products = new ArrayList<ProductData>();
		
		products.addAll(collectLinkedProducts(component));
		products.addAll(collectSearchProducts(component));
		final GPImagetileComponentdata gpImagetileComponentdata = new GPImagetileComponentdata();
		gpProductCarouselFacade.populateProductCarousel(component, gpImagetileComponentdata);
		
		final List<GPProductCarouseldata> productList = new ArrayList<GPProductCarouseldata>();
		if (CollectionUtils.isNotEmpty(products))
		{
			for (final ProductData productData : products)
			{
				final GPProductCarouseldata product = new GPProductCarouseldata();
				gpProductCarouselFacade.populateProductCarouselData(product, productData);
				productList.add(product);
			}
		}
		gpImagetileComponentdata.setComponentId(component.getUid());
		gpImagetileComponentdata.setProducts(productList);
		model.addAttribute("productCarousel", GPFunctions.convertToJSON(gpImagetileComponentdata));
	}

	protected List<ProductData> collectLinkedProducts(final ProductCarouselComponentModel component)
	{
		return productCarouselFacade.collectProducts(component);
	}

	protected List<ProductData> collectSearchProducts(final ProductCarouselComponentModel component)
	{
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(component.getSearchQuery());
		final String categoryCode = component.getCategoryCode();

		if (searchQueryData.getValue() != null && categoryCode != null)
		{
			final SearchStateData searchState = new SearchStateData();
			searchState.setQuery(searchQueryData);

			final PageableData pageableData = new PageableData();
			pageableData.setPageSize(100); // Limit to 100 matching results

			return productSearchFacade.categorySearch(categoryCode, searchState, pageableData).getResults();
		}

		return Collections.emptyList();
	}
	
	
	
}

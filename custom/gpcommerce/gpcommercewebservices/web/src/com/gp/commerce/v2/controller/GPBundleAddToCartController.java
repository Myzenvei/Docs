/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.v2.controller;

import com.gp.commerce.bundle.data.BundleProductData;
import com.gp.commerce.cart.impl.CommerceWebServicesCartFacade;
import com.gp.commerce.dto.cart.GPCartModificationListWSDTO;
import com.gp.commerce.dto.product.ProductWrapperWSDTO;
import com.gp.commerce.facades.bundles.dto.BundleCartUpdateWsDTO;
import com.gp.commerce.facades.bundles.dto.BundleProductWsDTO;
import de.hybris.platform.acceleratorfacades.product.data.ProductWrapperData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.CartModificationListData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commercewebservicescommons.dto.order.CartModificationWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartEntryException;
import de.hybris.platform.configurablebundlefacades.order.BundleCartFacade;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Secured(
		{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT","ROLE_ASAGENTSALESMANAGERGROUP" ,"ROLE_CLIENT"})
@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/carts")
@CacheControl(directive = CacheControlDirective.NO_CACHE)
@Api(tags = "Carts")
public class GPBundleAddToCartController extends BaseCommerceController {

	@Resource(name = "bundleCartFacade")
	private BundleCartFacade bundleCartFacade;

	@Resource(name = "gpCommerceWebServicesCartFacade")
	private CommerceWebServicesCartFacade cartFacade;


	private static final Logger LOG = Logger.getLogger(GPBundleAddToCartController.class);

	@RequestMapping(value = "/{cartId}/addBundleToCart", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	@ApiOperation(value = ".")
	public List<ProductWrapperWSDTO> addBundleToCart(@RequestBody final BundleCartUpdateWsDTO bundleCartRequestWsDTO,
													   @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields )throws CommerceCartModificationException
	{

		try {
			bundleCartFacade.startBundle(bundleCartRequestWsDTO.getStarterBundleEntry().getBundleTemplateId(), bundleCartRequestWsDTO.getStarterBundleEntry().getProductCode(), 1);
		}catch (CommerceCartModificationException e){
			LOG.error(" exception starting a bundle for product "+ bundleCartRequestWsDTO.getStarterBundleEntry().getProductCode() +" with exception "+e.getMessage());
			throw new CommerceCartModificationException(" exception starting a bundle with exception "+e.getMessage(),e);
		}

		CartData cartData=cartFacade.getSessionCartWithEntryOrdering(false);
		List<ProductWrapperWSDTO> productWrapperWSDTOList=new ArrayList<>();
		List<ProductWrapperData> productWrapperDataList=cartFacade.addBundleProductsToCart(bundleCartRequestWsDTO,cartData.getRootGroups().get(0));
		productWrapperDataList.forEach(productWrapperData -> {
			productWrapperWSDTOList.add(getDataMapper().map(productWrapperData,ProductWrapperWSDTO.class,fields));
		});

		return productWrapperWSDTOList;
	}


	@RequestMapping(value = "/{cartId}/updateBundleInCart", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	@ApiOperation(value = ".")
	public List<ProductWrapperWSDTO> updateBundleInCart(@RequestBody final BundleCartUpdateWsDTO bundleCartRequestWsDTO,
													   @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields )throws CommerceCartModificationException
	{
		List<ProductWrapperWSDTO> productWrapperWSDTOList=new ArrayList<>();
		List<ProductWrapperData> productWrapperDataList = cartFacade.updateBundleProductsInCart(bundleCartRequestWsDTO);

		productWrapperDataList.forEach(productWrapperData -> {
			productWrapperWSDTOList.add(getDataMapper().map(productWrapperData,ProductWrapperWSDTO.class,fields));
		});

		return productWrapperWSDTOList;
	}

	@RequestMapping(value = "/{cartId}/bundleCartDetail", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ApiOperation(value = ".")
	public BundleProductWsDTO getBundleProduct(
						@ApiParam(value = "The actual string user searched.") @RequestParam(required = false) final int bundleNo,
						@ApiParam(value = "The actual string user searched.") @RequestParam(required = false) final String bundleId,
						@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields )throws CommerceCartModificationException
	{
		BundleProductData bundleProductData= cartFacade.getBundleProductsInfo(bundleNo,bundleId);

		if(bundleProductData==null){

			throw new CartEntryException("Bundle not found", "Bundle not found for "+bundleNo);
		}
		return getDataMapper().map(bundleProductData, BundleProductWsDTO.class,fields);
	}

	@RequestMapping(value = "/{cartId}/deleteBundle/{bundleNo}", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseBody
	@ApiOperation(value = ".")
	public List<ProductWrapperWSDTO> deleteBundleInCart(
						  @ApiParam(value = "The actual string user searched.") @PathVariable(required = false) final int bundleNo,
						  @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)throws CommerceCartModificationException
	{
		List<ProductWrapperWSDTO> productWrapperWSDTOList=new ArrayList<>();
		List<ProductWrapperData> productWrapperDataList = cartFacade.deleteBundleFromCart(bundleNo);
		productWrapperDataList.forEach(productWrapperData -> {
			productWrapperWSDTOList.add(getDataMapper().map(productWrapperData,ProductWrapperWSDTO.class,fields));
		});

		return productWrapperWSDTOList;
	}
}
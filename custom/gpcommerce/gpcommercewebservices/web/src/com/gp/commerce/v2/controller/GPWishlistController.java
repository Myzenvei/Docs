/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.v2.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.exceptions.GPWishlistException;
import com.gp.commerce.core.forms.ShareProductForm;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.dto.wishlist.WishlistEntryListRequestDTO;
import com.gp.commerce.dto.wishlist.WishlistEntryRequestDTO;
import com.gp.commerce.dto.wishlist.WishlistListWsDTO;
import com.gp.commerce.dto.wishlist.WishlistListPageWsDTO;
import com.gp.commerce.dto.wishlist.WishlistWsDTO;
import com.gp.commerce.facades.data.WishlistData;
import com.gp.commerce.facades.data.WishlistMultipleData;
import com.gp.commerce.facades.wishlist.GPWishlistFacade;
import com.gp.commerce.swagger.ApiBaseSiteIdUserIdAndWishlistIdParam;
import com.gp.commerce.v2.helper.ProductsHelper;
import com.gp.commerce.validator.GPWishListWsDtoValidator;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commercewebservicescommons.dto.product.CategoryWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.wishlist2.model.Wishlist2Model;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.gp.commerce.dto.wishlist.WishlistMultipleEntryRequestDTO;
import com.gp.commerce.dto.wishlist.WishlistMultipleWsDTO;

@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_ASAGENTSALESMANAGERGROUP", "ROLE_CLIENT" })
@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/wishlist")
public class GPWishlistController extends BaseController {
	private static final String ERROR_WHILE_CREATING_WISHLIST = "Erro While creating wishlist";
	private static final String ERROR_WHILE_CREATING_THE_LIST = "Error while creating the list";
	private static final String WISHLIST = "Wishlist";
	private static final String CREATE_WISHLIST_ERROR_CODE = "1001";
	private static final String ADD_WISHLIST_ERROR_CODE = "1002";
	private static final String FAVORITES_WISHLIST_ERROR_CODE = "1003";
	private static final String INVALID_PRODUCTLIST = "invalid.quickorder.productlist";
	private static final String CATEGORY_TYPE ="category";
    private static final String GP_VARIANT_CATEGORY_TYPE ="GPDefaultVariantCategory";
    private static final String GP_VARIANT_VALUE_CATEGORY_TYPE ="GPDefaultVariantValueCategory";
    private static final String VARIANT_VALUE_CATEGORY_TYPE ="VariantValueCategory";
	private static final String VARIANT_CATEGORY_TYPE ="VariantCategory";

	private static final Logger LOG = Logger.getLogger(GPWishlistController.class);
	private static final String GPXPRESS_RETAILSOLDTO = "gpxpress.retail.soldto";

	@Resource(name = "wishlistFacade")
	private GPWishlistFacade wishlistFacade;
	@Resource(name = "wishlistWsDTOValidator")
	private GPWishListWsDtoValidator wishListWsDtoValidator;
	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;
	@Resource(name = "productsHelper")
	private ProductsHelper productsHelper;
	@Resource(name = "messageSource")
	private MessageSource messageSource;
	@Resource(name = "i18NService")
	private I18NService i18NService;
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "userFacade")
	private UserFacade userFacade;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	
	
	/**
	 * @param wishlist
	 * @param fields
	 * @return
	 * @throws RequestParameterException
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public WishlistWsDTO createWishlist(@RequestBody final WishlistWsDTO wishlist,
			@ApiParam(value = "The selected sold to searched.") @RequestParam(required = false) final String soldToId,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws GPWishlistException {
		validate(wishlist, WISHLIST, wishListWsDtoValidator);
		WishlistData wishlistData = new WishlistData();
		try {
			wishlistData = wishlistFacade.createWishlist(wishlist.getName(), wishlist.getDescription());
		} catch (final WebserviceValidationException e) {
			throw new GPWishlistException(CREATE_WISHLIST_ERROR_CODE, ERROR_WHILE_CREATING_WISHLIST, e);
		} catch (final Exception e) {
			throw new GPWishlistException(CREATE_WISHLIST_ERROR_CODE, ERROR_WHILE_CREATING_THE_LIST, e);
		}
		return getDataMapper().map(wishlistData, WishlistWsDTO.class, fields);

	}

	/**
	 * @param wishlist
	 * @param fields
	 * @throws RequestParameterException
	 */
	@RequestMapping(value = "/{wishlistuuid}", method = RequestMethod.GET)
	@ResponseBody
	public WishlistWsDTO getWishlist(final HttpServletRequest request, final HttpServletResponse response,
			@PathVariable final String wishlistuuid,
			@ApiParam(value = "pdfImage") @RequestParam(required = false) final boolean pdfImage,
			@ApiParam(value = "The selected sold to searched.") @RequestParam(required = false) final String soldToId,
			@ApiParam(value = "Flag for shareWishListEmail")@RequestParam(required = false) final String isShareList,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws GPWishlistException {

		WishlistData wishlistData = new WishlistData();
		try {
			String b2bwhitelabelSiteId = configurationService.getConfiguration().getString(GpcommerceCoreConstants.B2BWHITELABEL_SITE_ID);
			if(null !=isShareList && Boolean.valueOf(isShareList)
					&& null != cmsSiteService.getCurrentSite() && cmsSiteService.getCurrentSite().getUid().equalsIgnoreCase(b2bwhitelabelSiteId)){
					wishlistData = wishlistFacade.createSharedlistForUser(wishlistuuid,null);
			}
			else {
					wishlistData = wishlistFacade.getWishlist(wishlistuuid, request, response, pdfImage);
			} 
			B2BUnitModel b2bUnit = wishlistFacade.getB2BUnit();
			if (b2bUnit != null && null != wishlistData) {
				wishlistData.setSoldToId(b2bUnit.getUid());
			}
		}catch (final WebserviceValidationException e) {
				throw new GPWishlistException(CREATE_WISHLIST_ERROR_CODE, ERROR_WHILE_CREATING_WISHLIST, e);
		}catch (final Exception e) {
				throw new GPWishlistException(CREATE_WISHLIST_ERROR_CODE, ERROR_WHILE_CREATING_THE_LIST, e);
		}
		WishlistWsDTO wishlistWsDTO = getDataMapper().map(wishlistData, WishlistWsDTO.class, fields);
		wishlistWsDTO.setWishlistEntriesGroup(getEntriesGroup(wishlistWsDTO.getWishlistEntries()));
		return wishlistWsDTO;
	}

	/**
	 * @param wishlist
	 * @param fields
	 * @throws RequestParameterException
	 */
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	public WishlistWsDTO addToWishlist(@RequestBody final WishlistEntryRequestDTO wishlistEntryRequestDTO,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws GPWishlistException {

		WishlistData wishlistData  = null;
		try{
			wishlistData  = wishlistFacade.addWishlistEntry(wishlistEntryRequestDTO);
		} catch (final GPWishlistException e) {
			throw new GPWishlistException(e.getCode(), e.getMessage(), e);
		}catch(final Exception e){
			throw new GPWishlistException(ADD_WISHLIST_ERROR_CODE, e.getMessage(), e);
		}
		return getDataMapper().map(wishlistData, WishlistWsDTO.class, fields);
	}
	
	/**
	 * @param wishlist
	 * @param fields
	 * @throws RequestParameterException
	 */
	@RequestMapping(value = "/addmultiplelists", method = RequestMethod.PUT)
	@ResponseBody
	public WishlistMultipleWsDTO addMultipleProductsToWishlist(
			@RequestBody final WishlistMultipleEntryRequestDTO wishlistEntryRequestDTO,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws GPWishlistException {

		WishlistMultipleData wishlistData  = null;
		try{
			wishlistData  = wishlistFacade.addMultipleWishlistEntries(wishlistEntryRequestDTO);
		} catch (final GPWishlistException e) {
			throw new GPWishlistException(e.getCode(), e.getMessage(), e);
		}catch(final Exception e){
			throw new GPWishlistException(ADD_WISHLIST_ERROR_CODE, e.getMessage(), e);
		}
		return getDataMapper().map(wishlistData, WishlistMultipleWsDTO.class, fields);
	}
	
	@RequestMapping(value = "/addCustomAttr", method = RequestMethod.PUT)
	@ResponseBody
	public WishlistWsDTO addCustomAttrToWishlist(@RequestBody final WishlistWsDTO wishlistWsDTO,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws GPWishlistException {

		WishlistData wishlistData  = null;
		try{
			wishlistData  = wishlistFacade.addCustomAtrrWishlistEntry(wishlistWsDTO);
		} catch (final GPWishlistException e) {
			throw new GPWishlistException(e.getCode(), e.getMessage(), e);
		}catch(final Exception e){
			throw new GPWishlistException(ADD_WISHLIST_ERROR_CODE, e.getMessage(), e);
		}
		return getDataMapper().map(wishlistData, WishlistWsDTO.class, fields);
	}
	
			@RequestMapping(value = "/saveCustomAttribute", method = RequestMethod.PUT)
			@ResponseBody
			public WishlistWsDTO saveCustomAtrributeToWishList(@RequestBody final WishlistEntryRequestDTO wishlistEntryWsDTO,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws GPWishlistException {

				WishlistData wishlistData  = null;
				try{
					wishlistData  = wishlistFacade.saveCustomAtrribute(wishlistEntryWsDTO);
		} catch (final GPWishlistException e) {
					throw new GPWishlistException(e.getCode(), e.getMessage(), e);
				}catch(final Exception e){
					throw new GPWishlistException(ADD_WISHLIST_ERROR_CODE, e.getMessage(), e);
				}
				return getDataMapper().map(wishlistData, WishlistWsDTO.class, fields);
			}

	/**
	 * @param wishlist
	 * @param fields
	 * @throws RequestParameterException
	 */
	@RequestMapping(value = "/additems", method = RequestMethod.PUT)
	@ResponseBody
	public WishlistWsDTO addItemsToWishlist(@RequestBody final WishlistWsDTO wishlistWsDTO,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws GPWishlistException {

		WishlistData wishlistData  = null;

		try{
			  wishlistFacade.addItemsToWishlist(wishlistWsDTO);
		} catch (final Exception e) {
			throw new GPWishlistException(ADD_WISHLIST_ERROR_CODE, e.getMessage(), e);
		}
		return getDataMapper().map(wishlistData, WishlistWsDTO.class, fields);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	@ResponseBody
	public WishlistListPageWsDTO getAllWishlist(
			@ApiParam(value = "The selected sold to searched.") @RequestParam(required = false) final String soldToId,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) {
		return getDataMapper().map(wishlistFacade.getAllWishlistByType(), WishlistListPageWsDTO.class, fields);
	}

	/**
	 * @param wishlist
	 * @return
	 */
	@RequestMapping(value = "/{wishlistuuid}", method = RequestMethod.DELETE)
	@ResponseBody
	public WishlistWsDTO removeWishlist(@PathVariable final String wishlistuuid,
			@ApiParam(value = "The selected sold to searched.") @RequestParam(required = false) final String soldToId,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) {

		return getDataMapper().map(wishlistFacade.removeWishlist(wishlistuuid), WishlistWsDTO.class, fields);

	}

	/**
	 *
	 * @param wishlistList
	 * @param sortParam
	 * @param sortOrder
	 * @return
	 */
	@RequestMapping(value = "/sort", method = RequestMethod.POST)
	@ResponseBody
	public WishlistListWsDTO sortWishlistsByParam(@RequestParam(required = true) final String sortParam,
			@RequestParam(required = true) final String sortOrder,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) {
		return getDataMapper().map(wishlistFacade.sortWishlistsByParam(sortParam, sortOrder), WishlistListWsDTO.class,
				fields);
	}

	/**
	 * Method to remove a product entry from wishlist
	 * 
	 * @param wishlist
	 * @param fields
	 * @return
	 */
	@RequestMapping(value = "/removeentry", method = RequestMethod.DELETE)
	@ResponseBody
	public WishlistWsDTO removeWishlistEntryForProduct(@RequestBody final WishlistEntryRequestDTO wishlist,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) {

		return getDataMapper().map(wishlistFacade.removeWishlistEntryForProduct(wishlist.getProduct().getCode(),
				wishlist.getWishlistUid()), WishlistWsDTO.class, fields);

	}
	
	/**
	 * Method to remove category from list
	 * 
	 * @param wishlist
	 * @param fields
	 * @return
	 */
	@RequestMapping(value = "/removecategory", method = RequestMethod.DELETE)
	@ResponseBody
	public WishlistWsDTO removeCategory(@RequestBody final WishlistEntryRequestDTO wishlist,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) {

		final List<String> productCodeList = Arrays.asList(wishlist.getProduct().getCode().split(","));
		return getDataMapper().map(wishlistFacade.removeWishlistEntriesForCategory(productCodeList,
				wishlist.getCategory(), wishlist.getWishlistUid()), WishlistWsDTO.class, fields);

	}

	/**
	 * Remove selected wish list by passing in multiple UUIDs
	 *
	 * @param multiplewishlistuuid
	 * @param fields
	 * @return
	 */
	@RequestMapping(value = "/removeSelectedLists", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public void removeSelectedWishlists(@RequestBody final List<String> multiplewishlistuuid,
			@ApiParam(value = "The selected sold to searched.") @RequestParam(required = false) final String soldToId,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) {
		wishlistFacade.removeSelectedWishlists(multiplewishlistuuid);
	}

	/**
	 * Update the quantity for specific product in wish list entry
	 *
	 * @param wishlist
	 * @param fields
	 * @return
	 */
	@RequestMapping(value = "/updateQuantity", method = RequestMethod.POST)
	@ResponseBody
	public WishlistWsDTO updateWishListEntryForQuantity(@RequestBody final WishlistEntryRequestDTO wishlist,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) {

		WishlistWsDTO wishlistWsDTO = getDataMapper().map(wishlistFacade.updateWishListEntryForQuantity(wishlist),
				WishlistWsDTO.class, fields);
		wishlistWsDTO.setWishlistEntriesGroup(getEntriesGroup(wishlistWsDTO.getWishlistEntries()));
		return wishlistWsDTO;
	}

	/**
	 * Get Favorites
	 * 
	 * @param wishlist
	 * @param fields
	 * @throws GPWishlistException
	 */
	@RequestMapping(value = "/favorites", method = RequestMethod.GET)
	@ResponseBody
	public WishlistWsDTO getFavorites() throws GPWishlistException {

		WishlistData wishlistData = new WishlistData();
		try{
			wishlistData = wishlistFacade.getFavorites();
		} catch (final WebserviceValidationException e) {
			throw new GPWishlistException(FAVORITES_WISHLIST_ERROR_CODE, "Error in finding/creating fovorites wishlist",
					e);
		}
		WishlistWsDTO wishlistWsDTO = getDataMapper().map(wishlistData, WishlistWsDTO.class);
		wishlistWsDTO.setWishlistEntriesGroup(getEntriesGroup(wishlistWsDTO.getWishlistEntries()));
		return wishlistWsDTO;
	}

	/**
	 * Get Precurated List(wish list prepared by backoffice user) while passing
	 * precurated list ID
	 * 
	 * @param precuratedlistid
	 *            is wishlistUuid
	 * @param fields
	 * @return
	 * @throws GPWishlistException
	 */
	@RequestMapping(value = "/getprecuratedlistbyid/{precuratedlistid}", method = RequestMethod.GET)
	@ResponseBody
	public WishlistWsDTO getPrecuratedListById(@PathVariable final String precuratedlistid,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws GPWishlistException {

		WishlistData wishlistData = new WishlistData();
		try {
			wishlistData = wishlistFacade.getPrecuratedlist(precuratedlistid);
		} catch (final WebserviceValidationException e) {
			throw new GPWishlistException(CREATE_WISHLIST_ERROR_CODE, "Error while fetching precurated list", e);
		} catch (final Exception e) {
			throw new GPWishlistException(CREATE_WISHLIST_ERROR_CODE, "Error while fetching precurated list", e);
		}
		WishlistWsDTO wishlistWsDTO = getDataMapper().map(wishlistData, WishlistWsDTO.class, fields);
		wishlistWsDTO.setWishlistEntriesGroup(getEntriesGroup(wishlistWsDTO.getWishlistEntries()));
		return wishlistWsDTO;
	}

	/**
	 * Get Shared List(wish list shared by customer) by passing list ID
	 * 
	 * @param sharedlistid
	 *            is wishlistUuid
	 * @param fields
	 * @return
	 * @throws GPWishlistException
	 */
	@RequestMapping(value = "/getsharedlist/{sharedlistid}", method = RequestMethod.GET)
	@ResponseBody
	public WishlistWsDTO getSharedListById(@PathVariable final String sharedlistid,

			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		WishlistData wishlistData = new WishlistData();
		try {
			wishlistData = wishlistFacade.getSharedlist(sharedlistid);
		} catch (final WebserviceValidationException e) {
			throw new GPWishlistException(CREATE_WISHLIST_ERROR_CODE, "Error while fetching precurated list", e);
		}
		WishlistWsDTO wishlistWsDTO = getDataMapper().map(wishlistData, WishlistWsDTO.class, fields);
		wishlistWsDTO.setWishlistEntriesGroup(getEntriesGroup(wishlistWsDTO.getWishlistEntries()));
		return wishlistWsDTO;
	}

    /**
	 * Update/save precurated list(wish list prepared by backoffice user)
	 * 
	 * @param wishlist
	 * @param fields
	 * @return
	 */
	@RequestMapping(value = "/saveprecuratedlist", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public void savePrecuratedList(@RequestBody final WishlistWsDTO wishlistdto,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) {

		wishlistFacade.savePrecuratedList(wishlistdto);
	}

	/**
	 * Update wish list name for specific wishlist ID
	 *
	 * @param listName
	 * @param fields
	 * @return
	 */
	@RequestMapping(value = "/updateWishlistName/{wishlistuuid}", method = RequestMethod.POST)
	@ResponseBody
	public WishlistWsDTO updateWishlistName(@PathVariable final String wishlistuuid,
			@RequestParam(required = true) final String listName,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) {
		return getDataMapper().map(wishlistFacade.updateWishlistName(wishlistuuid, listName), WishlistWsDTO.class,
				fields);
	}

	@RequestMapping(value = "/{wishlistId}/sharewishlist", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@ApiOperation(value = "Share current wishlist functionality", notes = "Generates share wishlist email with csv attachement to the recipient.")
	@ApiBaseSiteIdUserIdAndWishlistIdParam
	public void shareWishlist(@ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,
			@ApiParam(value = "wishlistId") @RequestParam final String wishlistId,
			@ApiParam(value = "Recipient's emailId to share wishlist", required = true) @RequestParam final String recipientemails)
			throws CommerceCartRestorationException {
		WishlistData wishlistData = wishlistFacade.getSharedlist(wishlistId);
		if(null==wishlistData || null==wishlistData.getWishlistUid()) {
			wishlistData=wishlistFacade.getPrecuratedlist(wishlistId);
		}
		wishlistFacade.shareWishlist(recipientemails, wishlistData);

	}
	
	/**
	 * Takes the form details from user and triggers email and sends status back as
	 * response
	 *
	 * @param form
	 */
	@RequestMapping(value = "/{wishlistId}/sharelist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Share Wishlist Email Process", notes = "The API takes in submit form details from user and triggers Email, sends response.")
	@ApiBaseSiteIdUserIdAndWishlistIdParam
	public void shareWishlistDetails(@ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,@ApiParam(value = "wishlistId") @RequestParam final String wishlistId,
			@RequestBody final ShareProductForm form) {
		if(LOG.isDebugEnabled()){
			LOG.debug("In GpWishlist Controller " + form);
		}
		WishlistData wishlistData = new WishlistData();
		String gpxpressSiteId = configurationService.getConfiguration().getString(GpcommerceCoreConstants.GPXPRESS_SITE_ID);
			if(!userFacade.isAnonymousUser() && null != cmsSiteService.getCurrentSite() && cmsSiteService.getCurrentSite().getUid().equalsIgnoreCase(gpxpressSiteId)
					&& null != cmsSiteService.getSiteConfig(GPXPRESS_RETAILSOLDTO)&& cmsSiteService.getSiteConfig(GPXPRESS_RETAILSOLDTO).equals(sessionService.getAttribute("soldToUnitId"))) 
			{	
				wishlistData = wishlistFacade.createSharedlistForUser(wishlistId,form);
			}
			else
			{
				wishlistData = wishlistFacade.getSharedlist(wishlistId);
			}
		wishlistFacade.shareWishlist(form, wishlistData);
	}
	
	@RequestMapping(value = "/{wishlistId}/additembyproductid",method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "Add products to wishlist from wishlist", notes = "Add products to wishlist from wishlist.")
	@ApiBaseSiteIdUserIdAndWishlistIdParam
	public WishlistWsDTO addItemByProductId(
			@ApiParam(value = "wishlistid", required = true) @RequestParam final String wishlistid,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@ApiParam(value = "productCodes", required = true) @RequestParam final String productcodes)
			throws GPWishlistException {

		WishlistData wishlistData  = null;
		final Locale currentLocale = i18NService.getCurrentLocale();
		
		try{
			final List<ProductData> productList = productsHelper.prepareProductList(productcodes);
			if(!productsHelper.validateProductList(productList)){
				throw new RequestParameterException(messageSource.getMessage(INVALID_PRODUCTLIST, null, currentLocale),
						RequestParameterException.INVALID, "product list");
			}
			wishlistData  = wishlistFacade.addItemByProductId(wishlistid, productcodes);
		}catch(final GPWishlistException e){
			throw new GPWishlistException(e.getCode(), e.getMessage(), e);
		}catch(final Exception e){
			throw new GPWishlistException(INVALID_PRODUCTLIST, e.getMessage(), e);
		}
		return getDataMapper().map(wishlistData, WishlistWsDTO.class, fields);
		
	}
	
	/**
	 * @param wishlistid
	 * @param response
	 * @param redirectModel
	 * @throws IOException
	 */
	@RequestMapping(value = "/{wishlistuuid}/exportimage", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Downloads wishlist product images.", notes = "Downloads wishlist product images.")
	@ApiBaseSiteIdUserIdAndWishlistIdParam
	public void exportImages(@ApiParam(value = "wishlistid", required = true) @RequestParam final String wishlistid,
			@ApiParam(value = "imageformat", required = true) @RequestParam final String imageformat,
			@ApiParam(value = "resolution", required = true) @RequestParam final String resolution,
			@ApiParam(value = "allimages", required = true) @RequestParam final boolean allimages,
			final HttpServletRequest request, final HttpServletResponse response) throws IOException
	
	{
		try {
			WishlistData wishlistData = wishlistFacade.getWishlist(wishlistid,request,response,false);
			
			File file = wishlistFacade.exportImages(wishlistData,imageformat,resolution, allimages);
			
			ServletOutputStream outstream = response.getOutputStream();
			response.reset();

			try {
				response.addHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
				response.setContentLength((int) file.length());
				response.setContentType("application/octet-stream");
				
				FileUtils.copyFile(file, outstream);
			} finally {
				outstream.flush();
				outstream.close();
				file.delete();
			}
			
	    } catch(Exception e) {
	    	throw new GPWishlistException("3333", "No images to download"+e.getMessage(),e);
	    }
	}

	@RequestMapping(value = "/getProducts/{wishlistuuid}", method = RequestMethod.GET)
	@ResponseBody
	public void exportProductDetails(@PathVariable final String wishlistuuid,
			@RequestParam(required = true) final String listkey, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		List<ProductData> products = new ArrayList<>();
		List<String> files =new ArrayList<>();
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String zipFileName;
		files = wishlistFacade.getAssociatedFiles(files,timestamp,listkey);
		if("currentList".equalsIgnoreCase(listkey))
		{
			products = wishlistFacade.getWishListProducts(wishlistuuid,request,response);
			zipFileName = timestamp + GpcommerceCoreConstants.ZIP_FILE_EXTENSION;
		} else {
			products= wishlistFacade.getAllBrandSpecificProducts();
			zipFileName = "AllProducts" + GpcommerceCoreConstants.ZIP_FILE_EXTENSION;
		}
		if (CollectionUtils.isNotEmpty(products)) {
			InputStream inputStream = wishlistFacade.getInputStream(products, files, zipFileName);
			try {
				wishlistFacade.setZipFile(zipFileName,inputStream, response,products);
			} catch (final Exception e) {
				LOG.error("IOException occurred while writing excel file to response output stream", e);
				response.sendRedirect("/error");
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
		}
	}
	
	private Map<String,WishlistEntryListRequestDTO>  getEntriesGroup(List<WishlistEntryRequestDTO> wishlistEntries) {
		Map<String,WishlistEntryListRequestDTO>  wishListEntryGroup = new TreeMap<>();
		
		if (CollectionUtils.isNotEmpty(wishlistEntries)) {
			for (WishlistEntryRequestDTO entryDTO :wishlistEntries) {
				String key = null;
				for(CategoryWsDTO category: entryDTO.getProduct().getCategories()) {
					if (CATEGORY_TYPE.equalsIgnoreCase(category.getType())
							|| VARIANT_CATEGORY_TYPE.equalsIgnoreCase(category.getType())
							|| GP_VARIANT_CATEGORY_TYPE.equalsIgnoreCase(category.getType()) 
							|| GP_VARIANT_VALUE_CATEGORY_TYPE.equalsIgnoreCase(category.getType())
							|| VARIANT_VALUE_CATEGORY_TYPE.equalsIgnoreCase(category.getType())) {
						key = category.getName();
						break;
					}
				}
				if(key != null && null != wishListEntryGroup.get(key)) {
					WishlistEntryListRequestDTO entry = wishListEntryGroup.get(key);
					List<WishlistEntryRequestDTO> wishlistGroup = new ArrayList<>();
					wishlistGroup.add(entryDTO);
					entry.getWishlistEntryGroup().addAll(wishlistGroup);
					
				} else if(key!=null){
					List<WishlistEntryRequestDTO> wishlistGroup = new ArrayList<>();
					wishlistGroup.add(entryDTO);
					WishlistEntryListRequestDTO newEntry = new WishlistEntryListRequestDTO();
					newEntry.setWishlistEntryGroup(wishlistGroup);
					wishListEntryGroup.put(key,newEntry);
				}
			}
		}
		return wishListEntryGroup;
	}

}

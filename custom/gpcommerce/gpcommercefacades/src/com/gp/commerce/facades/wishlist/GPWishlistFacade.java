/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.wishlist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gp.commerce.core.exceptions.GPWishlistException;
import com.gp.commerce.core.forms.ShareProductForm;
import com.gp.commerce.dto.wishlist.WishlistEntryRequestDTO;
import com.gp.commerce.dto.wishlist.WishlistListData;
import com.gp.commerce.dto.wishlist.WishlistMultipleEntryRequestDTO;
import com.gp.commerce.dto.wishlist.WishlistWsDTO;
import com.gp.commerce.facades.data.WishlistData;
import com.gp.commerce.facades.data.WishlistMultipleData;
import com.gp.commerece.facades.component.data.GPPrecuratedListData;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.product.data.ProductData;


/**
 * The Interface GPWishlistFacade.
 *
 * @author psaraswat
 */
public interface GPWishlistFacade
{

	/**
	 * Creates the wishlist.
	 *
	 * @param name
	 *           the name
	 * @param description
	 *           the description
	 * @return the wishlist data
	 */
	WishlistData createWishlist(final String name, final String description);

	/**
	 * Gets the wishlist.
	 *
	 * @param uuid     the uuid
	 * @param request  the request
	 * @param response the response
	 * @param pdfImage the pdf image
	 * @return the wishlist
	 */
	WishlistData getWishlist(String uuid, HttpServletRequest request, HttpServletResponse response,boolean pdfImage);

	/**
	 * Gets the all wishlist.
	 *
	 * @return the all wishlist
	 */
	WishlistListData getAllWishlist();

	/**
	 * Adds the wishlist entry.
	 *
	 * @param dto the dto
	 * @return the wishlist data
	 * @throws GPWishlistException the GP wishlist exception
	 */
	WishlistData addWishlistEntry(WishlistEntryRequestDTO dto) throws GPWishlistException;

	/**
	 * Removes the wishlist entry for product.
	 *
	 * @param productCode
	 *           the product code
	 * @param uuid
	 *           the uuid
	 * @return true, if successful
	 */
	WishlistData removeWishlistEntryForProduct(String productCode, String uuid);

	/**
	 * Removes the wishlist.
	 *
	 * @param uuid
	 *           the uuid
	 * @return true, if successful
	 */
	WishlistData removeWishlist(String uuid);


	/**
	 * Gets the precurated wish list.
	 *
	 * @return list of GPPreCuratedWishlistdata
	 */
	GPPrecuratedListData getPrecuratedWishList();

	/**
	 * Remove selected wish list by passing in multiple UUIDs.
	 *
	 * @param multiplewishlistuuid the multiplewishlistuuid
	 * @throws GPWishlistException the GP wishlist exception
	 */
	void removeSelectedWishlists(List<String> multiplewishlistuuid) throws GPWishlistException;

	/**
	 * Update the quantity for specific product in wish list entry.
	 *
	 * @param dto the dto
	 * @return the wishlist data
	 */
	WishlistData updateWishListEntryForQuantity(WishlistEntryRequestDTO dto);

	/**
	 * Gets the favorites.
	 *
	 * @return favorites wishlist for customer
	 * If doesn't exist  will create new one and return
	 */
	WishlistData getFavorites();

	/**
	 * Sort wishlists by param.
	 *
	 * @param sortParam the sort param
	 * @param sortOrder the sort order
	 * @return the wishlist list data
	 */
	WishlistListData sortWishlistsByParam(final String sortParam, final String sortOrder);

	/**
	 * Adds items to wishlist.
	 *
	 * @param wishlistWsDTO the wishlist ws DTO
	 * @return wishlist data
	 */
	WishlistData addItemsToWishlist(WishlistWsDTO wishlistWsDTO);

	/**
	 * Update/save precurated list(wish list prepared by backoffice user).
	 *
	 * @param wishlistdto the wishlistdto
	 */
	void savePrecuratedList(final WishlistWsDTO wishlistdto);

	/**
	 * Update wish list name for specific wishlist ID.
	 *
	 * @param wishlistuuid the wishlistuuid
	 * @param listName the list name
	 * @return the wishlist data
	 */
	WishlistData updateWishlistName(String wishlistuuid,String listName);

	/**
	 * Get Precurated list by specific list name.
	 *
	 * @param listName the list name
	 * @return the precuratedlist
	 */
	WishlistData getPrecuratedlist(String listName);

	/**
	 * Shares wish list to recipients.
	 *
	 * @param recipientemails the recipientemails
	 * @param wishlistData the wishlist data
	 */
	void shareWishlist(String recipientemails, WishlistData wishlistData);
	
	/**
	 * Get Shared list by specific list name.
	 *
	 * @param listName the list name
	 * @return the sharedlist
	 */
	WishlistData getSharedlist(String listName);
	
	/**
	 * Fetch all wishlists of the user by type.
	 *
	 * @return     WishlistListData
	 */
	WishlistListData getAllWishlistByType();
	
	/**
	 * Add products to wishList in list.
	 *
	 * @param wishListUid the wish list uid
	 * @param productcodes the productcodes
	 * @return WishlistData
	 */
	WishlistData addItemByProductId(String wishListUid, String productcodes);
	
	/**
	 * Export images.
	 *
	 * @param wishlistData the wishlist data
	 * @param imageformat the imageformat
	 * @param resolution the resolution
	 * @param allimages the allimages
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	File exportImages(WishlistData wishlistData, String imageformat, String resolution, boolean allimages) throws IOException;

	/**
	 * Adds the custom atrr wishlist entry.
	 *
	 * @param dto the dto
	 * @return the wishlist data
	 * @throws GPWishlistException the GP wishlist exception
	 */
	WishlistData addCustomAtrrWishlistEntry(WishlistWsDTO dto) throws GPWishlistException;

	/**
	 * Save custom atrribute.
	 *
	 * @param wishlistEntryWsDTO the wishlist entry ws DTO
	 * @return the wishlist data
	 */
	WishlistData saveCustomAtrribute(WishlistEntryRequestDTO wishlistEntryWsDTO);

	/**
	 * Adds the multiple wishlist entries.
	 *
	 * @param dto the dto
	 * @return the wishlist multiple data
	 * @throws GPWishlistException the GP wishlist exception
	 */
	WishlistMultipleData addMultipleWishlistEntries(WishlistMultipleEntryRequestDTO dto) throws GPWishlistException;
	
	/**
	 * Generates zip and excel files for product data.
	 *
	 * @param products the products
	 * @param files the files
	 * @param zipFileName the zip file name
	 * @return the input stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	InputStream getInputStream(List<ProductData> products,List<String> files,String zipFileName) throws IOException;

	/**
	 * Sets the generated zip file to the HttpServletResponse for download.
	 *
	 * @param zipFileName the zip file name
	 * @param inputStream the input stream
	 * @param response the response
	 * @param products the products
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void setZipFile(String zipFileName, InputStream inputStream, HttpServletResponse response, List<ProductData> products) throws IOException;

	
	/**
	 * Retrieves products list based on Sold To .
	 *
	 * @return productList
	 */
	List<ProductData> getAllBrandSpecificProducts();

	/**
	 * Gets the wish list products.
	 *
	 * @param wishlistuuid the wishlistuuid
	 * @param request the request
	 * @param response the response
	 * @return productList
	 */
	List<ProductData> getWishListProducts(String wishlistuuid, HttpServletRequest request, HttpServletResponse response);

	/**
	 * Sets file names .
	 *
	 * @param files     the files
	 * @param timestamp the timestamp
	 * @param listkey   the listkey
	 * @return the associated files
	 */
	List<String> getAssociatedFiles(List<String> files, String timestamp, String listkey);
	
	/**
	 * Gets the b2B unit.
	 *
	 * @return the b2B unit
	 */
	B2BUnitModel getB2BUnit();

	/**
	 * Removes the wishlist entries for category.
	 *
	 * @param productCodeList the product code list
	 * @param category        the category
	 * @param uuid            the uuid
	 * @return the wishlist data
	 */
	WishlistData removeWishlistEntriesForCategory(List<String> productCodeList, String category, String uuid);

	/**
	 * Trigger share wishlist with the contents and wishlistData from the form object.
	 *
	 * @param form         the form
	 * @param wishlistData the wishlist data
	 */
	void shareWishlist(ShareProductForm form, WishlistData wishlistData);

	/**
	 * creates wishlist for the specified user .
	 *
	 * @param listName the list name
	 * @param form     the form
	 * @return WishlistData
	 */
	WishlistData createSharedlistForUser(final String listName, ShareProductForm form);
	
}

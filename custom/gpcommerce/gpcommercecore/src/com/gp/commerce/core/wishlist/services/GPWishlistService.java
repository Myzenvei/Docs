/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.wishlist.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import com.gp.commerce.core.exceptions.GPWishlistException;
import com.gp.commerce.dto.wishlist.WishlistEntryRequestDTO;
import com.gp.commerce.dto.wishlist.WishlistWsDTO;
import com.gp.commerce.facades.data.GPQuickOrderData;
import com.gp.commerce.facades.data.WishlistData;
import com.gp.commerce.facades.data.WishlistEntryData;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.BusinessException;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;


/**
 * GP Wishlist service interface to perform
 * functionalities on wishlists
 */
public interface GPWishlistService
{
	/**
	 * Update the quantity for specific product in wish list entry
	 *
	 * @param dto the wish list entry request DTO
	 * @return {@link WishlistData}
	 */
	WishlistData updateWishListEntryForQuantity(WishlistEntryRequestDTO dto);

	/**
	 * Remove the selected wish list by passing multiple selected IDs
	 *
	 * @param multiplewishlistuuid list of wishlist uuids
	 * @throws GPWishlistException on error
	 */
	void removeSelectedWishlists(List<String> multiplewishlistuuid) throws GPWishlistException;

	/**
	 * Get Favorites
	 * Method to calculate the total of product prices in a wishlist
	 * @param wishlistEntryData list of wish list entries
	 * @return subtotalCalculated
	 */
	BigDecimal calculateSubtotal(List<WishlistEntryData> wishlistEntryData);
	
	/**
	 * Return wishlist by name if exist against user else null
	 * @param user the user
	 * @param name the name
	 * @return Wishlist2Model : wishlist matching the type passed
	 */
	Wishlist2Model getWishlistByName(UserModel user, String name);

	/**
	 * Update user precurated list using wish list prepared by backoffice user
	 * @param wishlistdto the {@link WishlistWsDTO}
	 * @param savedPrecurated the saved precurated
	 * @param gpUserPrecurated  the user precurated
	 */
	void updatePrecuratedList(WishlistWsDTO wishlistdto, Wishlist2Model savedPrecurated, Wishlist2Model gpUserPrecurated);

	/**
	 * Update wish list name
	 * @param wishlist2Model the white list
	 * @param listName the list name
	 * @return {@link WishlistData}
	 */
	WishlistData updateWishlistName(Wishlist2Model wishlist2Model, String listName);

	/**
	 * Get Precurated list by specific list name
	 * @param listName the list name
	 * @return {@link WishlistData}
	 */
	WishlistData getPrecuratedlist(String listName);
	/**
	 * Method to get wishlist by id
	 * @param wishListId the list ID
	 * @return Wishlist2Model : wishlist matching the ID passed
	 */
	Wishlist2Model getWishlistById(String wishListId);

	/**
	 * Method to get wishlist by type
	 * @param user the user
	 * @param type the type
	 * @return Wishlist2Model : wishlist matching the type passed
	 */
	Wishlist2Model getWishlistByType(UserModel user, String type);

	/**
	 * Method to create new precurated wishlist
	 * @param wishlistdto the wish list Ws DTO
	 * @param gpUserPrecurated the Wishlist
	 */
	void createPrecuratedList(WishlistWsDTO wishlistdto, Wishlist2Model gpUserPrecurated);

	/**
	 * Method to create wishlistentry
	 * @param wishlist2Model the wishlist
	 * @param product the product
	 * @return Wishlist2EntryModel : wishlist entry created
	 */
	Wishlist2EntryModel createWishlistEntry(Wishlist2Model wishlist2Model, ProductModel product);


	/**
	 * Creates quick order wishlist
	 * @param gpQuickOrderData
	 * 			quick order data
	 * @return quick order wishlist
	 * 			quick order wishlist
	 * @throws BusinessException on error
	 */
	Wishlist2Model createQuickOrderWishList(GPQuickOrderData gpQuickOrderData) throws BusinessException;

	/**
	 * Creates wishlist along with a unique guid.
	 *
	 * @param name
	 *           The name for the wishlist
	 * @param description
	 *           Description of the wishlist.
	 * @param defaultWL
	 *           If the wishlist is a default one or not.
	 * @return The Wishlist2Model created.
	 */
	Wishlist2Model createWishlistWithGUID(final String name, final String description,
			final Boolean defaultWL);

	/**
	 * Update wishlist
	 * 
	 * @param wishlistEntry wishlist entry
	 */
	void updateWishlist(Wishlist2EntryModel wishlistEntry);
	
	/**
	 * Method to Create WishlistEntryModel with the Products in the Cart
	 * @param wishlist
	 *           Wishlist Model
	 * @param product
	 *           Product in the wishlist
	 * @param quantity
	 *          Quantity of the product
	 * @return
	 *       Wishlist with Products
	 */
	Wishlist2EntryModel createShareCartEntry(Wishlist2Model wishlist, ProductModel product, Long quantity);
	
	/**
	 * Create a List with the Products in the Cart
	 * @param cart
	 *       CartModel
	 * @return
	 *      Wishlist
	 * @throws BusinessException on error
	 */
	Wishlist2Model createShareCartWishList(CartModel cart) throws BusinessException;
	
	/**
	 * Creates WIshlist for the cart based on he given name, description and boolean default WL
	 * @param name
	 *  	Name of the Wishlist
	 * @param description
	 * 		Description of the Wishlist
	 * @param defaultWL
	 * 		Boolean Default Wishlist
	 * @return {@link Wishlist2Model}
	 */
	Wishlist2Model createWishlistForCart(final String name, final String description,
			final Boolean defaultWL);
	
	/**
	 * Get Shared list by specific list name
	 * @param listName the list name
	 * @return {@link WishlistData}
	 */
	WishlistData getSharedlist(String listName);
	
    /**
     * Fetches all wishlists by type
     * @return
     *      List of Wishlists
     */
    List<Wishlist2Model> getAllWishlistByType();
    
	/**
	 * Fetches all products for the given product codes
	 * 
	 * @param productCodes the product codes
	 * @return list of products
	 */
    List<ProductModel> getProductsForCodes(String productCodes);
    
 
	/**
	 * Exports image files for the goven wishlist data, resolution, source.
	 *
	 * @param wishlistData the wishlist data
	 * @param imageformat  the image format
	 * @param resolution   the resolution
	 * @param source       the source path
	 * @param zipFile      the zip file
	 * @param allimages the allimages
	 * @return image file
	 * @throws IOException on input error
	 */
	File exportImages(WishlistData wishlistData, String imageformat, String resolution, String source, String zipFile, boolean allimages) throws IOException;

	/**
	 * Retrieves Zipped file of excel sheets
	 * 
	 * @param products    list of product
	 * @param files       list of files
	 * @param zipFileName zip file name
	 * @return inputStream {@link InputStream}
	 * @throws IOException on error
	 */
    InputStream getInputStream(List<ProductData> products,List<String> files,String zipFileName) throws IOException;
    
	/**
	 * Fetches guest wish list for the given uuid and user
	 * 
	 * @param uuid        the uuid
	 * @param currentUser the current user
	 * @return WishList
	 */
	Wishlist2Model getGuestWishList(String uuid, UserModel currentUser);
	
	/**
	 * Fetches products associated to the b2bunit
	 * @param b2bunit the b2b unit
	 * @return list of products
	 */
	List<ProductModel> getAllBrandSpecificProducts(B2BUnitModel b2bunit);
	
	/**
	 * Creates wishlist for the user specified
	 * @param wishlist the wishlist
	 * @param user the user
	 * @return WishlistData the wishlist data
	 */
	WishlistData createWishListForLoggedInUser(final Wishlist2Model wishlist, UserModel user);
	
	/**
	 * Fetches B2B customer with associated email
	 * @param email the customer email
	 * @return B2BCustomerModel the B2B customer
	 */
	B2BCustomerModel getB2BCustomerForEmail(String email); 
}

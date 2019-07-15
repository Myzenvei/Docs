/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.wishlist.dao;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.Date;
import java.util.List;

import com.gp.commerce.core.enums.WishlistTypeEnum;

/**
 * Interface class to perform DAO functionalities for GP Wishlist
 * @author megverma
 *
 */
public interface GPWishlistDao
{

	/**
	 * Query DB and returns Precurated list by specific list name
	 * @param listName
	 * @return
	 */
	Wishlist2Model getPrecuratedlist(String listName);

	/**
	 * Query DB and returns wishlist by name if exist else null
	 * @param user
	 * @param favoritesWishlist
	 * @return
	 */
	Wishlist2Model getWishlistByName(UserModel user, String name);

	/**
	 * Method to find wishlist by id
	 * @param wishListId
	 * @return
	 */
	Wishlist2Model getWishlistById(String wishListId);

	/** Method to find wishlist by type
	 * @param user
	 * @param type
	 * @return
	 */
	Wishlist2Model getWishlistByType(UserModel user, String type);
	
	/**
	 * Fetch all the wishlists of particular Type
	 * @param site
	 *          Site in which wishlist belongs
	 * @param type
	 *          Type of the Wishlist  
	 * @param modifiedtime
	 *         Created time of the wishlist before the modified time     
	 * @return
	 * 			List of Wishlists
	 */
	List<Wishlist2Model> getWishlistsByType(final WishlistTypeEnum type, final CMSSiteModel site ,Date modifiedBefore);
	
	/**
	 * Query DB and returns Shared list by specific list name
	 * @param listName
	 * @return
	 */
	Wishlist2Model getSharedlist(String listName ,String type);
	
	
	/**
	 * @param user
	 *          User
	 * @param type
	 *         WishlistType
	 * @return
	 *     List of wishlists
	 */
	List<Wishlist2Model> getAllWishlistByType(UserModel user, WishlistTypeEnum type);
	
    /**
     * @param productCodes
     * @return List of Products
     */
    List<ProductModel> getProductsForCodes(String productCodes);
    
    /**
     * @param uuid
     * @param currentUser
     * @return
     */
    Wishlist2Model getGuestWishList(String uuid, UserModel currentUser);

    /**
     * Fetches products associated to the b2bunit
     * @param b2bunit
     * @return list of products
     */
    List<ProductModel> getAllBrandSpecificProducts(B2BUnitModel b2bunit);

    /**
     * Fetches B2B customer with associated email
     * @param email
     * @return list of b2bcustomers
     */
    List<B2BCustomerModel> getB2BCustomerForEmail(String email);
}

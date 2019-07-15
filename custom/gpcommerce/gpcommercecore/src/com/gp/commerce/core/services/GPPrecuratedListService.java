/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services;

import java.util.List;

import de.hybris.platform.wishlist2.model.Wishlist2Model;

/**
 * This interface is for processing GP Precurated List services
 */
public interface GPPrecuratedListService
{
	/**
	 * @return list of wishlist2 model
	 */
	List<Wishlist2Model> getPrecuratedWishList();
}

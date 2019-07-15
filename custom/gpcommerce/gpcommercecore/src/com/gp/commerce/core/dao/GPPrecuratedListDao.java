/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.dao;

import java.util.List;

import de.hybris.platform.wishlist2.model.Wishlist2Model;

public interface GPPrecuratedListDao
{
	/**
	 * @return list of wishlist2 model
	 */
	List<Wishlist2Model> getPrecuratedWishList();
}

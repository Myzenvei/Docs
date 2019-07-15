/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.impl;

import java.util.List;

import javax.annotation.Resource;

import com.gp.commerce.core.dao.GPPrecuratedListDao;
import com.gp.commerce.core.services.GPPrecuratedListService;

import de.hybris.platform.wishlist2.model.Wishlist2Model;

/**
 * This class is used for processing GP precurated list services
 */
public class GPDefaultPrecuratedListService implements GPPrecuratedListService
{
	
	@Resource(name="gpPrecuratedListDao")
	private GPPrecuratedListDao gpPrecuratedListDao;

	@Override
	public List<Wishlist2Model> getPrecuratedWishList()
	{
		return gpPrecuratedListDao.getPrecuratedWishList();
	}

}

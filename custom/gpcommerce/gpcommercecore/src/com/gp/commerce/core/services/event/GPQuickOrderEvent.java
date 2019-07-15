/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

public class GPQuickOrderEvent extends AbstractCommerceUserEvent<BaseSiteModel>{

	private Wishlist2Model wishlistModel;
	private String emailIds;
	private String wishlistUid;

	/**
	 * Default constructor
	 * @param process 
	 * 
	 */
	public GPQuickOrderEvent()
	{
		super();
	}

	public Wishlist2Model getWishlistModel() {
		return wishlistModel;
	}

	public void setWishlistModel(Wishlist2Model wishlistModel) {
		this.wishlistModel = wishlistModel;
	}

	public String getEmailIds() {
		return emailIds;
	}

	public void setEmailIds(String emailIds) {
		this.emailIds = emailIds;
	}

	public String getWishlistUid() {
		return wishlistUid;
	}

	public void setWishlistUid(String wishlistUid) {
		this.wishlistUid = wishlistUid;
	}
	
	
}

/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.validator;

import com.gp.commerce.dto.wishlist.WishlistWsDTO;
import org.springframework.validation.Validator;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;

import de.hybris.platform.wishlist2.Wishlist2Service;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

public class GPWishListWsDtoValidator implements Validator{
	
	private Wishlist2Service wishlistService;

	@Override
	public boolean supports(Class<?> clazz) {
		return String.class.equals(clazz) || WishlistWsDTO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors error) {
		WishlistWsDTO wsDTO =(WishlistWsDTO)target;
		if(StringUtils.isEmpty(wsDTO.getName())){
			error.reject("Invalid list name. Try again.");
		}
		List<Wishlist2Model> wishlist2Models = getWishlistService().getWishlists();
		if(CollectionUtils.isNotEmpty(wishlist2Models)) {
			if(StringUtils.isNotEmpty(wsDTO.getName())){
				if(wishlist2Models.stream().anyMatch(wishlist -> wishlist.getName().equalsIgnoreCase(wsDTO.getName()))) {
					error.reject("That list name already exists.");
				}
			}
		}

		
	}
	
	public Wishlist2Service getWishlistService() {
		return wishlistService;
	}
	
	public void setWishlistService(Wishlist2Service wishlistService) {
		this.wishlistService = wishlistService;
	}
}

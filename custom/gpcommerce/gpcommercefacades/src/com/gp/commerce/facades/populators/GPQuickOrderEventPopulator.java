/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.gp.commerce.core.services.event.GPQuickOrderEvent;
import com.gp.commerce.core.wishlist.services.GPWishlistService;
import com.gp.commerce.facade.quickorder.impl.GPQuickOrderFacadeImpl;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gp.commerce.facades.data.GPQuickOrderData;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import javax.xml.ws.WebServiceException;

public class GPQuickOrderEventPopulator implements Populator<GPQuickOrderData, GPQuickOrderEvent>{

	private static final Logger LOG = Logger.getLogger(GPQuickOrderFacadeImpl.class);
	
	@Resource(name = "userService")
	private UserService userService;
	
	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;
	
	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;
	
	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;
	
	@Resource(name = "gpWishlistService")
	private GPWishlistService gpWishlistService;
	
	@Override
	public void populate(GPQuickOrderData source, GPQuickOrderEvent target) throws ConversionException 
	{
		final CustomerModel customerModel = (CustomerModel) getUserService().getCurrentUser();
		target.setBaseStore(getBaseStoreService().getCurrentBaseStore());
		target.setSite(getBaseSiteService().getCurrentBaseSite());
		target.setLanguage(getCommonI18NService().getCurrentLanguage());
		target.setCurrency(getCommonI18NService().getCurrentCurrency());
		target.setCustomer(customerModel);
		Wishlist2Model wishList = null;
		try {
			wishList = getGpWishlistService().createQuickOrderWishList(source);
		} catch (final Exception e) 
		{
			LOG.error("Exception while fetching product for share quick order " + e.getMessage(), e);
			throw new WebServiceException(GpcommerceFacadesConstants.QUICK_ORDER_WISHLIST_EXCEPTION);
		}
		target.setWishlistModel(wishList);
		target.setEmailIds(source.getEmailIds());
	}
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}

	public void setBaseSiteService(BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}

	public BaseStoreService getBaseStoreService() {
		return baseStoreService;
	}

	public void setBaseStoreService(BaseStoreService baseStoreService) {
		this.baseStoreService = baseStoreService;
	}

	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	public void setCommonI18NService(CommonI18NService commonI18NService) {
		this.commonI18NService = commonI18NService;
	}

	public GPWishlistService getGpWishlistService() {
		return gpWishlistService;
	}

	public void setGpWishlistService(GPWishlistService gpWishlistService) {
		this.gpWishlistService = gpWishlistService;
	}
}

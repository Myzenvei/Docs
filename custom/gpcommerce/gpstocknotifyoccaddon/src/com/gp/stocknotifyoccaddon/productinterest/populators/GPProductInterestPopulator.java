/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.stocknotifyoccaddon.productinterest.populators;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestData;
import de.hybris.platform.customerinterestsfacades.productinterest.populators.ProductInterestPopulator;
import de.hybris.platform.customerinterestsservices.model.ProductInterestModel;
import de.hybris.platform.servicelayer.user.UserService;

public class GPProductInterestPopulator extends ProductInterestPopulator {
	
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "impersonationService")
	private ImpersonationService impersonationService;
	
	private static final Logger LOG = Logger.getLogger(GPProductInterestPopulator.class);

	
	@Override
	public void populate(final ProductInterestModel source, final ProductInterestData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		
		target.setExpiryDate(source.getExpiryDate());
		target.setNotificationType(source.getNotificationType());

		final ProductData productData = getProductConverter().convert(source.getProduct());
		getProductPriceAndStockConverter().convert(source.getProduct(), productData);

		target.setProduct(productData);
		target.setCreationDate(source.getCreationtime());
		
		target.setEmailAddress(source.getNotifyMeEmailId());

		final ImpersonationContext impersonationContext = new ImpersonationContext();
		impersonationContext.setUser(getUserService().getAnonymousUser());

		try
		{
			getImpersonationService().executeInContext(impersonationContext,
					new ImpersonationService.Executor<Boolean, Exception>()
					{
						@Override
						public Boolean execute() throws Exception
						{
							target.setNotificationChannels(
									getNotificationPreferenceFacade().getValidNotificationPreferences(source.getNotificationChannels()));
							return Boolean.TRUE;
						}
					});
		}
		catch (Exception e)
		{
			LOG.error("Exception while fetching the notifictaion channels",e);
		}
	}


	/**
	 * @return the userService
	 */
	public UserService getUserService() {
		return userService;
	}


	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * @return the impersonationService
	 */
	public ImpersonationService getImpersonationService() {
		return impersonationService;
	}


	/**
	 * @param impersonationService the impersonationService to set
	 */
	public void setImpersonationService(ImpersonationService impersonationService) {
		this.impersonationService = impersonationService;
	}

}

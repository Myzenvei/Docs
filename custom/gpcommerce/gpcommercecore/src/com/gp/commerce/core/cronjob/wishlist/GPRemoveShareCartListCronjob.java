/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.cronjob.wishlist;

import com.gp.commerce.core.enums.WishlistTypeEnum;
import com.gp.commerce.core.model.RemoveShareCartListCronJobModel;
import com.gp.commerce.core.wishlist.dao.GPWishlistDao;
import com.gp.commerce.core.wishlist.dao.impl.GPDefaultWishlistDao;

import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.util.Config;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

public class GPRemoveShareCartListCronjob extends AbstractJobPerformable<RemoveShareCartListCronJobModel> {

	private static final String DEFAULT_WISHLIST_MAX_AGE = "cart.wishlist.expiry.age";

	private static final Logger LOG = Logger.getLogger(GPRemoveShareCartListCronjob.class.getName());

	@Resource(name = "gpWishlistDao")
	private GPWishlistDao gpWishlistDao;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource
	private TimeService timeService;

	@Override
	public PerformResult perform(RemoveShareCartListCronJobModel job) {

		LOG.debug("Remove Share Cart List CronJob started sucessfully");

		try
			{
					final int wishlistAge = job.getWishlistRemovalAge() != null
							? Integer.valueOf(Config.getParameter(DEFAULT_WISHLIST_MAX_AGE))
							: job.getWishlistRemovalAge().intValue();
					List<Wishlist2Model> wishlist = gpWishlistDao.getWishlistsByType(WishlistTypeEnum.SHARE_CART_LIST, job.getSite(),
							new DateTime(timeService.getCurrentTime()).minusSeconds(wishlistAge).toDate());
					if (CollectionUtils.isNotEmpty(wishlist))
					{
						for(Wishlist2Model wishlistCurrent: wishlist) {
							if (clearAbortRequestedIfNeeded(job))
							{
								LOG.debug("The job is aborted.");
								return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
							}
							modelService.remove(wishlistCurrent);
						}
					}
					
			}
		catch (Exception e) 
		{
			LOG.error(e.getMessage(), e);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}
	@Override
	public boolean isAbortable() {
		return true;
	} 

}

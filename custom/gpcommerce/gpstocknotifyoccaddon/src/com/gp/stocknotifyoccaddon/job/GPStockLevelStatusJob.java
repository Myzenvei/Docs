/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.stocknotifyoccaddon.job;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.gp.commerce.core.enums.MaterialStatusEnum;
import com.gp.stocknotifyoccaddon.jalo.GPStockLevelStatusCronJob;
import com.gp.stocknotifyoccaddon.model.GPStockLevelStatusCronJobModel;

import de.hybris.platform.stocknotificationservices.cronjob.StockLevelStatusJob;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.customerinterestsservices.model.ProductInterestModel;
import de.hybris.platform.notificationservices.model.SiteMessageModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.stocknotificationservices.constants.StocknotificationservicesConstants;

public class GPStockLevelStatusJob extends StockLevelStatusJob {
	
	@Resource(name = "sessionService")
	private SessionService sessionService;
	
	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService() {
		return sessionService;
	}

	/**
	 * @param sessionService the sessionService to set
	 */
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	public PerformResult perform(final CronJobModel job)
	{
		getSessionService().setAttribute("currentSite", ((GPStockLevelStatusCronJobModel)job).getSite());
		return super.perform(job);
	}
	
	@Override
	protected boolean isProductInStock(final ProductInterestModel productInterest, final Date now)
	{
		final ProductModel product = productInterest.getProduct();
		if (product == null)
		{
			modelService.remove(productInterest);
			return false;
		}
		if (!isProductOnSale(product, now))
		{
			return false;
		}
		if(MaterialStatusEnum.COMING_SOON.equals(product.getMaterialStatus())) {
			return false;
		}

		final BaseStoreModel currentBaseStore = productInterest.getBaseStore();
		final List<WarehouseModel> wareHoustList = getWarehouseSelectionStrategy().getWarehousesForBaseStore(currentBaseStore);
		final StockLevelStatus stockLevelStatus = getStockService().getProductStatus(productInterest.getProduct(), wareHoustList);
		return StockLevelStatus.INSTOCK.equals(stockLevelStatus);
	}

}

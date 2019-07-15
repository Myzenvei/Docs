/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.gp.commerce.core.dao.GPConsignmentDao;
import com.gp.commerce.core.model.ShippingNotificationModel;
import com.gp.commerce.core.model.TrackingModel;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;


/**
 * GP DAO class to perform custom Debit DAO functionalities
 */
public class GPConsignmentDaoImpl implements GPConsignmentDao
{

	private static final Logger LOG = Logger.getLogger(GPConsignmentDaoImpl.class.getName());
	private static final String SHIPPING_NOTIFICATION_BY_CODE = "SELECT {sn:PK} FROM {ShippingNotification as sn} where {sn.code}=?code";
	private static final String SHIPPING_NOTIFICATION_TO_PROCESS = "SELECT {sn:PK} FROM {ShippingNotification as sn} where {isProcessed}=0 and {retryCount}<=?retryCount";
	private static final String CONSIGNMENT_ENTRY_BY_CODE = "SELECT {ce:PK} FROM {ConsignmentEntry as ce JOIN Consignment as c ON {c.pk}={ce.consignment}} where {ce.consignmentEntryNumber}=?entryNumber and {c.code}=?consignmentCode";
	private static final String TRACKING_DETAILS = "SELECT {t:PK} FROM {Tracking as t} where {t.emailProcessedTime} IS NULL";
	
	private static final String ENTRY_NUMBER = "entryNumber";
	private static final String CONSIGNMENT_CODE = "consignmentCode";

    @Resource(name = "cmsSiteService")
    private CMSSiteService cmsSiteService;
    private FlexibleSearchService flexibleSearchService;

	@Override
	public ShippingNotificationModel getShippingNotificationDetails(final String shippingNotificationCode) {
		if(LOG.isDebugEnabled()){
			LOG.debug("Inside GPConsignmentDaoImpl");
		}
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SHIPPING_NOTIFICATION_BY_CODE);
		fQuery.addQueryParameter("code", shippingNotificationCode);
		final SearchResult<ShippingNotificationModel> searchResult = getFlexibleSearchService().search(fQuery);
		LOG.info("Inside GPConsignmentDaoImpl");
		return searchResult.getResult().iterator().next();
	}

	@Override
	public List<ShippingNotificationModel> getShippingCodeToBeProcessed(int retryCount) {
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SHIPPING_NOTIFICATION_TO_PROCESS);
		fQuery.addQueryParameter("retryCount", retryCount);
		final SearchResult<ShippingNotificationModel> searchResult = getFlexibleSearchService().search(fQuery);
		LOG.debug("Inside GPConsignmentDaoImpl");
		return searchResult.getResult();
	}
	
	@Override
	public ConsignmentEntryModel getConsignmentEntryByEntryNumber(final Integer entryNumber, final String consignmentCode) {
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(CONSIGNMENT_ENTRY_BY_CODE);
		fQuery.addQueryParameter(ENTRY_NUMBER, entryNumber);
		fQuery.addQueryParameter(CONSIGNMENT_CODE, consignmentCode);
		final SearchResult<ConsignmentEntryModel> searchResult = getFlexibleSearchService().search(fQuery);
		return searchResult.getResult().iterator().next();
	}
	
	@Override
	public List<TrackingModel> getTrackingDetailsForEmail() {
		if(LOG.isDebugEnabled()){
			LOG.debug("**** Inside GPConsignmentDaoImpl:getTrackingDetailsForEmail() ****");
		}
		FlexibleSearchQuery fQuery = new FlexibleSearchQuery(TRACKING_DETAILS);
		final SearchResult<TrackingModel> searchResult = getFlexibleSearchService().search(fQuery);
		return searchResult.getResult();
	}
	
	/**
	 *
	 * @return FlexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	/**
	 *
	 * @param flexibleSearchService
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}
}
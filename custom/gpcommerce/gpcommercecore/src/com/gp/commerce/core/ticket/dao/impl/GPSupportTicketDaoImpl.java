/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.ticket.dao.impl;

import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;
import com.gp.commerce.core.company.services.GPB2BUnitsService;
import com.gp.commerce.core.model.GPSupportTicketModel;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.ticket.dao.GPSupportTicketDao;
import com.gp.commerce.core.util.GPFunctions;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

public class GPSupportTicketDaoImpl implements GPSupportTicketDao {

	private static final String SFDC_CASE_NUMBER = "ticket.sfdc.case.number";
	protected static final String SUPPORT_TICKET_CONFIGURED_DAYS="viewTicketsForDays";
	
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private ConfigurationService configurationService;
	
	@Resource(name = "gpB2BUnitsService")
	protected GPB2BUnitsService<B2BUnitModel, UserModel> gpB2BUnitsService;
	
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;

	private static final String SUPPORT_TICKET_QUERY = "SELECT {" + GPSupportTicketModel.PK + "} FROM {"
			+ GPSupportTicketModel._TYPECODE + " } WHERE {" + GPSupportTicketModel.CUSTOMER + "} = ?currentUser"
			+ " AND {" + GPSupportTicketModel.BASESITE + "} =?currentBaseSite AND {" + GPSupportTicketModel.CREATIONTIME + "} "
			+ " BETWEEN ?date AND ?currentdate" ;
	
	private static final String TICKET_SORT_QUERY = " ORDER BY (CASE {" + GPSupportTicketModel.SFDCCASENUMBER + "} WHEN ?sfdcCaseNumber THEN 1 ELSE 0 END) DESC";
			
	
	private static final String SUPPORT_TICKET_B2B_QUERY = "SELECT {" + GPSupportTicketModel.PK + "} FROM {"
			+ GPSupportTicketModel._TYPECODE + " AS GP JOIN  " + B2BCustomerModel._TYPECODE
			+ " AS C ON {GP.CUSTOMER}={C.PK} JOIN " + B2BUnitModel._TYPECODE + " AS BU ON {BU.PK}={C.DEFAULTB2BUNIT}}"
			+ " WHERE {BU.UID} = ?b2bunit" + " AND {" + GPSupportTicketModel.BASESITE + "} =?currentBaseSite" 
			+ " AND {" + GPSupportTicketModel.CREATIONTIME + "} BETWEEN ?date AND ?currentdate" ;
					
	
	@Override
	public List<GPSupportTicketModel> getTicketDetails(UserModel currentUser, BaseSiteModel currentBaseSite, String sort){
		String sfdcCaseNumber = configurationService.getConfiguration().getString(SFDC_CASE_NUMBER);
		String siteConfigDays = cmsSiteService.getSiteConfig(SUPPORT_TICKET_CONFIGURED_DAYS);
		Integer days = Integer.valueOf(siteConfigDays);
		Calendar calendar = Calendar.getInstance();
		String currentDate = GPFunctions.getFormattedDate(calendar.getTime());
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		String configuredDate = GPFunctions.getFormattedDate(calendar.getTime());
		FlexibleSearchQuery query =null ;
		if (sort != null) {
			query = new FlexibleSearchQuery(SUPPORT_TICKET_QUERY + getOrderByString(sort));
			query.addQueryParameter("sfdcCaseNumber", sfdcCaseNumber);
		} else {
			query = new FlexibleSearchQuery(SUPPORT_TICKET_QUERY);
		}
		query.addQueryParameter("currentUser", currentUser);
		query.addQueryParameter("currentBaseSite", currentBaseSite);
		query.addQueryParameter("date", configuredDate); 
		query.addQueryParameter("currentdate",currentDate);
		final SearchResult<GPSupportTicketModel> searchResult  = getFlexibleSearchService().search(query) ;
		return searchResult.getResult();
	}
	
	@Override
	public List<GPSupportTicketModel> getTicketDetailsForB2B(UserModel currentUser, BaseSiteModel currentBaseSite,
			String b2bunit, String sort){
		boolean	isB2BAdmin=false;
		FlexibleSearchQuery query =null ;
		String sfdcCaseNumber = configurationService.getConfiguration().getString(SFDC_CASE_NUMBER);
		String siteConfigDays = cmsSiteService.getSiteConfig(SUPPORT_TICKET_CONFIGURED_DAYS);
		Integer days = Integer.valueOf(siteConfigDays);
		Calendar calendar = Calendar.getInstance();
		String currentDate = GPFunctions.getFormattedDate(calendar.getTime());
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		String configuredDate = GPFunctions.getFormattedDate(calendar.getTime());
		if(currentUser instanceof B2BCustomerModel){
		isB2BAdmin=gpB2BUnitsService.isB2BAdmin((B2BCustomerModel)currentUser);
		}
		if(isB2BAdmin) {
			if(sort!=null) {
				query = new FlexibleSearchQuery(SUPPORT_TICKET_B2B_QUERY + getOrderByString(sort));
				query.addQueryParameter("sfdcCaseNumber", sfdcCaseNumber);
				}else {
				 query = new FlexibleSearchQuery(SUPPORT_TICKET_B2B_QUERY);
				}
	
		query.addQueryParameter("b2bunit", b2bunit);
		query.addQueryParameter("currentBaseSite", currentBaseSite);
		query.addQueryParameter("date", configuredDate); 
		query.addQueryParameter("currentdate", currentDate);
		
		final SearchResult<GPSupportTicketModel> searchResult  = getFlexibleSearchService().search(query) ;
		return searchResult.getResult();  
		}else {
			return  getTicketDetails( currentUser,  currentBaseSite, sort) ;
		}
	}
	
	protected String getOrderByString(final String sort)
	{
		final String sortOrder = "ASC".equals(sort.trim()) ? "ASC" : "DESC";
		final String orderByStatus = TICKET_SORT_QUERY;
		final String orderByTime = ",{" + GPSupportTicketModel.CREATIONTIME +"}" + sortOrder; 
		return orderByStatus + orderByTime;
	}
	
	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.ticket.dao;

import com.gp.commerce.core.model.GPSupportTicketModel;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.* ;
public interface GPSupportTicketDao {


	List<GPSupportTicketModel> getTicketDetailsForB2B(UserModel currentUser, BaseSiteModel currentBaseSite,
			String b2bunit, String sortCode);

	List<GPSupportTicketModel> getTicketDetails(UserModel currentUser, BaseSiteModel currentBaseSite, String sortCode);

}

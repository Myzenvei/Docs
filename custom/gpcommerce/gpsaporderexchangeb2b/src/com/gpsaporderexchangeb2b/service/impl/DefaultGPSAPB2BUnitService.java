/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gpsaporderexchangeb2b.service.impl;

import com.sap.hybris.sapcustomerb2b.inbound.DefaultSAPB2BUnitService;

import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;

/**
 * The Class DefaultGPSAPB2BUnitService.
 */
public class DefaultGPSAPB2BUnitService extends DefaultSAPB2BUnitService {

	/**
	 * Overrides the default implementation of this method in DefaultB2BUnitsService. This implementation just has one change.
	 * It doesn't set the B2B_DEFAULT_PRICE_GROUP in session so that it cannot be used as default pricing group.
	 */
	@Override
	public void updateBranchInSession(final Session session, final UserModel currentUser)
	{
		if (currentUser instanceof B2BCustomerModel)
		{
			final Object[] branchInfo = (Object[]) getSessionService().executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public Object[] execute()
				{
					getSearchRestrictionService().disableSearchRestrictions();
					final B2BCustomerModel currentCustomer = (B2BCustomerModel) currentUser;
					final B2BUnitModel unitOfCustomer = getParent(currentCustomer);


					/**
					 * Europe1PriceFactory does not allow a user to belong to multiple price groups with themselves have
					 * different UPGs assigned see https://jira.hybris.com/browse/BTOB-488 get the upg assigned to the parent
					 * unit and set it in the context if none is assigned default to 'B2B_DEFAULT_PRICE_GROUP'
					 */
					final EnumerationValueModel userPriceGroup = (unitOfCustomer.getUserPriceGroup() != null ? getTypeService()
							.getEnumerationValue(unitOfCustomer.getUserPriceGroup()) : lookupPriceGroupFromClosestParent(unitOfCustomer));
					return new Object[]
					{ getRootUnit(unitOfCustomer), getBranch(unitOfCustomer), unitOfCustomer, userPriceGroup };
				}
			});

			getSessionService().setAttribute(B2BConstants.CTX_ATTRIBUTE_ROOTUNIT, branchInfo[0]);
			getSessionService().setAttribute(B2BConstants.CTX_ATTRIBUTE_BRANCH, branchInfo[1]);
			getSessionService().setAttribute(B2BConstants.CTX_ATTRIBUTE_UNIT, branchInfo[2]);
			if (branchInfo[3] instanceof EnumerationValueModel && !((EnumerationValueModel)branchInfo[3]).getCode().equalsIgnoreCase(B2BConstants.B2BDEFAULTPRICEGROUP))
			{
				getSessionService().setAttribute(Europe1Constants.PARAMS.UPG, branchInfo[3]);
			} 
		}
	}
	
}

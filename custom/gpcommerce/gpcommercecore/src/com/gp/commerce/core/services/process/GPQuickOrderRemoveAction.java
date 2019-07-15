/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.process;

import javax.annotation.Resource;

import com.gp.commerce.core.model.QuickOrderEmailProcessModel;

import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

public class GPQuickOrderRemoveAction extends AbstractProceduralAction{

	@Resource(name = "modelService")
	private ModelService modelService;
	
	@Override
	public void executeAction(BusinessProcessModel processModel) throws RetryLaterException, Exception {

		if(processModel instanceof QuickOrderEmailProcessModel){
			Wishlist2Model wishlistModel = ((QuickOrderEmailProcessModel) processModel).getWishlist();
			modelService.remove(wishlistModel);
		}
		
	}

	
}

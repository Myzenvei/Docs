/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.controllers.cms;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.model.GPPreCuratedListComponentModel;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.facades.wishlist.GPWishlistFacade;
import com.gp.commerece.facades.component.data.GPPrecuratedListData;


@Controller("GPPreCuratedListComponentController")
@RequestMapping(value = "/view/" + GPPreCuratedListComponentModel._TYPECODE + "Controller")

public class GPPreCuratedListComponentController extends AbstractCMSAddOnComponentController<GPPreCuratedListComponentModel>
{

	@Resource(name = "wishlistFacade")
	private GPWishlistFacade wishlistFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPPreCuratedListComponentModel component)
	{
		final GPPrecuratedListData precuratedWishList = wishlistFacade.getPrecuratedWishList();
		if (null != component.getPaginationRows())
		{
			precuratedWishList.setPaginationNo(component.getPaginationRows());
		}
		model.addAttribute("precuratedList", GPFunctions.convertToJSON(precuratedWishList));
	}

}

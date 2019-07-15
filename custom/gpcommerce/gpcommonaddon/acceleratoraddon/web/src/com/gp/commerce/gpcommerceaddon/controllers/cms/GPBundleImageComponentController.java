/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.gpcommerceaddon.controllers.cms;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import com.gp.commerce.core.model.GPBundleImageComponentModel;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.facades.component.data.GPBundleImageData;
import com.gp.commerce.core.model.GPMediaContainerlinkModel;
import com.gp.commerce.facades.component.data.GPImageComponentdata;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultBundleImageFacade;

import de.hybris.platform.acceleratorfacades.device.ResponsiveMediaFacade;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;

/**
 * Controller for  GPBundleImageComponent
 */
@Controller("GPBundleImageComponentController")
@RequestMapping(value = "/view/" + GPBundleImageComponentModel._TYPECODE + "Controller")
public class GPBundleImageComponentController extends AbstractCMSAddOnComponentController<GPBundleImageComponentModel>
{

	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;
	
	@Resource(name = "gpBundleImageFacade")
	private GPDefaultBundleImageFacade gpBundleImageFacade;

	/**
	 * Gets the complete json of Bundle Image  component for GpEmployee.
	 *
	 * @param component
	 *           for component
	 */
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPBundleImageComponentModel component)
	{

		final GPBundleImageData gpBundleData = new GPBundleImageData();
		gpBundleImageFacade.populateBundleTitle(component,gpBundleData);
		
		final List<GPImageComponentdata> imageCompList = new ArrayList<GPImageComponentdata>();
		if(CollectionUtils.isNotEmpty(component.getBundleImageList()))
		{
			for(final GPMediaContainerlinkModel mediaContainer:component.getBundleImageList())

			{
				final GPImageComponentdata gpImageComponentData = new GPImageComponentdata();

				final List<ImageData> mediaList = responsiveMediaFacade
					.getImagesFromMediaContainer(mediaContainer.getMedia(commerceCommonI18NService.getCurrentLocale()));
				if(CollectionUtils.isNotEmpty(mediaList))
				{
					for (final ImageData imageData : mediaList)
					{
						gpBundleImageFacade.populateImageComponent(imageData,gpImageComponentData);
						
					}
					gpBundleImageFacade.populateImageLink(mediaContainer, gpImageComponentData);
					

				}
				imageCompList.add(gpImageComponentData);
			}
	}
		gpBundleData.setImageList(imageCompList);
		model.addAttribute("bundleData", GPFunctions.convertObjectToJSON(gpBundleData));
	}
}
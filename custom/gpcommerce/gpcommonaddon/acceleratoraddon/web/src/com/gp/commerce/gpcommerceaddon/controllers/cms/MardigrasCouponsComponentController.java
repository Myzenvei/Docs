/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.controllers.cms;

import de.hybris.platform.acceleratorfacades.device.ResponsiveMediaFacade;
import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.media.MediaContainerModel;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.gpcommerceaddon.facades.GPImageAndTextData;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultImageAndTextDataFacade;
import com.gp.commerce.gpcommerceaddon.model.MardigrasCouponsComponentModel;
import com.gp.commerce.gpcommerceaddon.populators.GPImageAndTextDataPopulator;


/**
 * Controller for mardigras coupons component.
 */
@Controller("MardigrasCouponsComponentController")
@RequestMapping(value = "/view/" + MardigrasCouponsComponentModel._TYPECODE + "Controller")
public class MardigrasCouponsComponentController extends AbstractCMSAddOnComponentController<MardigrasCouponsComponentModel>
{
	/** The responsive media facade. */
	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	/** The commerce common I 18 N service. */
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	@Resource(name = "gpImageAndTextDataFacade")
	private GPDefaultImageAndTextDataFacade gpImageAndTextDataFacade;

	private static final String DOCUMENT = "document";

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final MardigrasCouponsComponentModel component)
	{
		final GPImageAndTextData couponsData = new GPImageAndTextData();
		gpImageAndTextDataFacade.populateImageAndTextData(component, couponsData);
		if(null != component.getBackGroundImage())
		{
			final List<ImageData> medias = responsiveMediaFacade
					.getImagesFromMediaContainer( component.getBackGroundImage(commerceCommonI18NService.getCurrentLocale()));
			if (CollectionUtils.isNotEmpty(medias))
			{
				for (final ImageData imageData : medias)
				{
					if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
					{
						couponsData.setBackGroundImage(imageData.getUrl());
					}
				}
			}
		}
		if(null != component.getIsSignUp())
		{
			couponsData.setIsSignUP(component.getIsSignUp());
		}
		if(null != component.getDownloadFile())
		{
			final MediaContainerModel mediaFile = component.getDownloadFile().getMedia(commerceCommonI18NService.getCurrentLocale());
			if (null != mediaFile)
			{
				final List<ImageData> downloadFile = responsiveMediaFacade.getImagesFromMediaContainer(mediaFile);
				if (CollectionUtils.isNotEmpty(downloadFile))
				{
					for (final ImageData file : downloadFile)
					{
						if (file.getFormat().equalsIgnoreCase(DOCUMENT))
						{
							couponsData.setDownloadURL(file.getUrl());
						}
					}
				}
				couponsData.setDownloadBtnLabel(component.getDownloadFile().getName());
			}
		}
		model.addAttribute("couponsData", GPFunctions.convertToJSON(couponsData));
	}


}

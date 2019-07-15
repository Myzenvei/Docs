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

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.model.GPImageComponentModel;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.facades.component.data.Rotatingbannerdata;


@Controller("GPImageComponentController")
@RequestMapping(value = "/view/" + GPImageComponentModel._TYPECODE + "Controller")
public class GPImageComponentController extends AbstractCMSAddOnComponentController<GPImageComponentModel>
{

	private static final String EXTENSION_NAME = "gpcommonaddon";
	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;


	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPImageComponentModel component)
	{

		Rotatingbannerdata bannerImageData = new Rotatingbannerdata();
		final List<ImageData> mediaList = responsiveMediaFacade
				.getImagesFromMediaContainer(component.getMedia(commerceCommonI18NService.getCurrentLocale()));
		if (CollectionUtils.isNotEmpty(mediaList))
		{
			bannerImageData =  getImageData(mediaList, component);
		}
		model.addAttribute("bannerImageData", GPFunctions.convertToJSON(bannerImageData));
	}


	private Rotatingbannerdata getImageData(final List<ImageData> mediaList, final GPImageComponentModel gpImageComponentModel)
	{
		final Rotatingbannerdata rotatingbannerdata = new Rotatingbannerdata();
		rotatingbannerdata.setComponetType("imageWrapper");
		for (final ImageData imageData : mediaList)
		{
			if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
			{
				rotatingbannerdata.setBackgroundImageD(imageData.getUrl());
				rotatingbannerdata
						.setBackgroundImageAltTextD(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
			}
			if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.MOBILE.toString()))
			{
				rotatingbannerdata.setBackgroundImageM(imageData.getUrl());
				rotatingbannerdata
						.setBackgroundImageAltTextM(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());

			}
			if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.TABLET.toString()))
			{
				rotatingbannerdata.setBackgroundImageT(imageData.getUrl());
				rotatingbannerdata
						.setBackgroundImageAltTextT(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());

			}
		}

		if (gpImageComponentModel.getHeadingText() != null)
		{
			rotatingbannerdata.setHeadingText(gpImageComponentModel.getHeadingText());
		}
		if (gpImageComponentModel.getUrlLink() != null)
		{
			rotatingbannerdata.setLinkTo(gpImageComponentModel.getUrlLink());
		}
		if (gpImageComponentModel.getIsExternalLink() != null)
		{
			rotatingbannerdata.setIsExternalLink(gpImageComponentModel.getIsExternalLink());
		}
		if (gpImageComponentModel.getCtaText() != null)
		{
			rotatingbannerdata.setCtaText(gpImageComponentModel.getCtaText());
		}
		if (gpImageComponentModel.getCtaColor() != null)
		{
			rotatingbannerdata.setCtaColor(gpImageComponentModel.getCtaColor());
		}
		if (gpImageComponentModel.getCtaBgColor() != null)
		{
			rotatingbannerdata.setCtaBgColor(gpImageComponentModel.getCtaBgColor());
		}
		if (gpImageComponentModel.getSubHeadBgColor() != null)
		{
			rotatingbannerdata.setSubHeadBgColor(gpImageComponentModel.getSubHeadBgColor());
		}
		if (gpImageComponentModel.getHeadingColor() != null)
		{
			rotatingbannerdata.setHeadingColor(gpImageComponentModel.getHeadingColor());
		}
		if (gpImageComponentModel.getSubHeadingColor() != null)
		{
			rotatingbannerdata.setSubHeadingColor(gpImageComponentModel.getSubHeadingColor());
		}
		if (gpImageComponentModel.getTextPosition() != null)
		{
			rotatingbannerdata.setTextPosition(gpImageComponentModel.getTextPosition());
		}
		if (gpImageComponentModel.getSubHeadingText() != null)
		{
			rotatingbannerdata.setSubHeadingText(gpImageComponentModel.getSubHeadingText());
		}
		if (null != gpImageComponentModel.getCtaStyle())
		{
			rotatingbannerdata.setCtaStyle(gpImageComponentModel.getCtaStyle());
		}
		if (null != gpImageComponentModel.getComponentTheme())
		{
			rotatingbannerdata.setComponentTheme(gpImageComponentModel.getComponentTheme());
		}
		if (null != gpImageComponentModel.getImageLink())
		{
			rotatingbannerdata.setImageLink(gpImageComponentModel.getImageLink());
		}
		if (null != gpImageComponentModel.getIsExternalImage())
		{
			rotatingbannerdata.setIsExternalImage(gpImageComponentModel.getIsExternalImage());
		}
		return rotatingbannerdata;
	}


	@Override
	protected String getAddonUiExtensionName(final GPImageComponentModel component)
	{
		return EXTENSION_NAME;
	}
}
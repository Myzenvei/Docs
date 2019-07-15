/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.populators;

import com.gp.commerce.core.model.GPImageLinkComponentModel;
import com.gp.commerce.gpcommerceaddon.facades.MardigrasMenuOptionData;

import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;

public class MardigrasHeaderNavigationPopulator {

	public MardigrasMenuOptionData getSocialIconData(final GPImageLinkComponentModel socialIcon)
	{
		final MardigrasMenuOptionData socialIconData = new MardigrasMenuOptionData();
		if (null != socialIcon.getUrl())
		{
			socialIconData.setIsSocialIcon(true);
			socialIconData.setLinkURL(socialIcon.getUrl());
			socialIconData.setIsSocialLink(false);
			if (null != socialIcon.getMedia())
			{
				socialIconData.setLinkImg(socialIcon.getMedia().getURL());
				socialIconData.setBannerDesc(socialIcon.getMedia().getAltText());
			}
			socialIconData.setLinkText(socialIcon.getName());
		}
		return socialIconData;
	}

	public MardigrasMenuOptionData getSocialLinkData(final CMSLinkComponentModel socialLink)
	{
		final MardigrasMenuOptionData socialLinkData = new MardigrasMenuOptionData();
		if (null != socialLink.getUrl())
		{
			socialLinkData.setIsSocialIcon(false);
			socialLinkData.setLinkURL(socialLink.getUrl());
			socialLinkData.setIsSocialLink(true);
			socialLinkData.setLinkText(socialLink.getName());
		}
		return socialLinkData;
	}
}

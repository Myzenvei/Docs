/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.populators;

import de.hybris.platform.acceleratorcms.model.components.FooterNavigationComponentModel;
import de.hybris.platform.cms2.enums.LinkTargets;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.converters.Populator;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.gp.commerce.gpcommerceaddon.facades.GPFooterData;
import com.gp.commerce.gpcommerceaddon.facades.GPFooterLinksData;


/**
 * Populator for GP Footer Data
 *
 */
public class GPFooterDataPopulator implements Populator<FooterNavigationComponentModel, List<GPFooterData>>
{

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	/**
	 * Populating GPFooterData with one level of navigation nodes
	 */
	@Override
	public void populate(final FooterNavigationComponentModel source, final List<GPFooterData> target)
	{
		final List<CMSNavigationNodeModel> footerNavs = source.getNavigationNode().getChildren();
		final GPFooterData footerData = new GPFooterData();
		final List<GPFooterLinksData> footerLinks = new ArrayList<>();
		for (final CMSNavigationNodeModel nav : footerNavs)
		{
			for (final CMSNavigationEntryModel navEntries : nav.getEntries())
			{
				final CMSLinkComponentModel linkComponent = (CMSLinkComponentModel) navEntries.getItem();
				if (linkComponent != null)
				{
					final GPFooterLinksData footerLink = new GPFooterLinksData();
					footerLink.setLinkText(linkComponent.getLinkName(commerceCommonI18NService.getCurrentLocale()));
					footerLink.setLinkTo(linkComponent.getUrl());
					footerLink.setExternal(linkComponent.isExternal());
					if (linkComponent.getTarget() != null)
					{
						footerLink.setNewWindow(
								linkComponent.getTarget().compareTo(LinkTargets.NEWWINDOW) == 0 ? Boolean.TRUE : Boolean.FALSE);
					}
					footerLinks.add(footerLink);
				}
			}
			footerData.setLinks(footerLinks);
		}
		target.add(footerData);
	}
}

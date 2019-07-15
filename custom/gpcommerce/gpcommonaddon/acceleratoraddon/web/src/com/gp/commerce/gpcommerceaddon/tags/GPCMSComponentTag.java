/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.tags;

import de.hybris.platform.acceleratorcms.services.CMSDynamicAttributeService;
import de.hybris.platform.acceleratorcms.tags2.CMSComponentTag;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.PageContext;


/**
 * GP CMS component tag
 */
public class GPCMSComponentTag extends CMSComponentTag
{

	@Override
	protected Map<String, String> getElementAttributes()
	{
		Map<String, String> mergedAttributes = new HashMap<>(Collections.singletonMap("class", getElementCssClass()));
		String classStr = mergedAttributes.get("class");
		if (dynamicAttributes != null && (dynamicAttributes.get("class") == null || dynamicAttributes.get("class").isEmpty()))
		{
			dynamicAttributes.put("class", "no-space yComponentWrapper");
		}
		if (currentComponent.getCompStyleDesktop() != null)
		{
			classStr = classStr + " " + currentComponent.getCompStyleDesktop();
			mergedAttributes.put("class", classStr);
		}
		if (currentComponent.getCompStyleMobile() != null)
		{
			classStr = classStr + " " + currentComponent.getCompStyleMobile();
			mergedAttributes.put("class", classStr);
		}
		if (dynamicAttributes != null)
		{
			mergedAttributes = htmlElementHelper.mergeAttributeMaps(mergedAttributes, dynamicAttributes);
		}
		if (currentCmsPageRequestContextData.isLiveEdit())
		{
			mergedAttributes = htmlElementHelper.mergeAttributeMaps(mergedAttributes, createLiveEditAttributes());
		}

		final ContentSlotModel contentSlot = (ContentSlotModel) pageContext.getAttribute("contentSlot", PageContext.REQUEST_SCOPE);
		for (final CMSDynamicAttributeService attributeService : cmsDynamicAttributeServices)
		{
			mergedAttributes = htmlElementHelper.mergeAttributeMaps(mergedAttributes,
					attributeService.getDynamicComponentAttributes(currentComponent, contentSlot));
		}
		return mergedAttributes;

	}

}
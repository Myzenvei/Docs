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

package com.gp.commerce.storefront.renderer;

import de.hybris.platform.acceleratorcms.component.renderer.CMSComponentRenderer;
import de.hybris.platform.acceleratorcms.model.components.ImageMapComponentModel;
import org.apache.commons.lang.StringUtils;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;


public class ImageMapComponentRenderer implements CMSComponentRenderer<ImageMapComponentModel>
{

	protected static final PolicyFactory policy = new HtmlPolicyBuilder().allowStandardUrlProtocols()
			.allowElements("div", "img", "map", "area").allowAttributes("src", "class", "title", "usemap", "alt", "srcset")
			.onElements("img").allowAttributes("shape", "coords", "href", "alt", "target").onElements("area").allowAttributes("name")
			.onElements("map").toFactory();

	@Override
	public void renderComponent(final PageContext pageContext, final ImageMapComponentModel component)
			throws ServletException, IOException
	{
		final String altText = StringUtils.defaultIfBlank(component.getMedia().getAltText(), "");
		String html = "<div>" + "<img";
		if (StringUtils.isNotBlank(altText))
		{
			html = html +" title=\"" + altText + "\" " + " alt=\"" + altText + "\" ";
		}
		html = html + " src=\"" + component.getMedia().getURL() + "\" " + " usemap=\"#map\">" + "<map name=\"map\">" +
				component.getImageMapHTML() + "</map>" + "</div>";

		final String sanitizedHTML = policy.sanitize(html);
		final JspWriter out = pageContext.getOut();
		out.write(sanitizedHTML);
	}
}

/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.component.renderer;

import de.hybris.platform.acceleratorcms.component.renderer.impl.CMSParagraphComponentRenderer;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;


/**
 * Renderer for OOB paragraph component.
 */
public class GPCMSParagraphComponentRenderer extends CMSParagraphComponentRenderer
{
	@Override
	public void renderComponent(final PageContext pageContext, final CMSParagraphComponentModel component)
			throws ServletException, IOException
	{
		final JspWriter out = pageContext.getOut();
		out.write("<div class=\"content \" id = " + component.getUid() + ">");
		out.write(component.getContent() == null ? StringUtils.EMPTY : component.getContent());
		out.write("</div>");
	}

}

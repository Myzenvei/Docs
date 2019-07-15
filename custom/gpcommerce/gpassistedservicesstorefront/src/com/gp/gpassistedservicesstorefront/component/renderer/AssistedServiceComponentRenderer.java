/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.gpassistedservicesstorefront.component.renderer;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.util.WebUtils;

import com.gp.commerce.facade.assistedservice.GPAssistedServiceFacade;
import com.gp.gpassistedservicesstorefront.constants.GpassistedservicesstorefrontConstants;

import de.hybris.platform.addonsupport.renderer.impl.DefaultAddOnCMSComponentRenderer;
import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;
import de.hybris.platform.assistedserviceservices.constants.AssistedserviceservicesConstants;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

public class AssistedServiceComponentRenderer<C extends AbstractCMSComponentModel> extends DefaultAddOnCMSComponentRenderer<C>
{

	private final static Logger LOG = Logger.getLogger(AssistedServiceComponentRenderer.class); //NOSONAR
	public static final String ASM_CUSTOMERID= "asmCustomerId";
	public static final String ASM_CARTID= "asmCartId";
	
	GPAssistedServiceFacade gpAssistedServiceFacade;

	@Override
	public void renderComponent(final PageContext pageContext, final C component) throws ServletException, IOException
	{
		ServletRequest request = pageContext.getRequest();
		
		final String asmRequestParam = request.getParameter(GpassistedservicesstorefrontConstants.ASM_REQUEST_PARAM);
		
		boolean asmSessionStatus = getGpAssistedServiceFacade().isAssistedServiceModeLaunched();

		// Check for "asm" parameter in HTTP request
		if (asmRequestParam != null)
		{
			// change behavior only when it's 'true' or 'false' as a value
			if (asmRequestParam.equalsIgnoreCase(Boolean.TRUE.toString()))
			{
				asmSessionStatus = true;
				if (!getGpAssistedServiceFacade().isAssistedServiceModeLaunched())
				{
					getGpAssistedServiceFacade().launchAssistedServiceMode();
				}
			}
			else if (asmRequestParam.equalsIgnoreCase(Boolean.FALSE.toString()))
			{
				getGpAssistedServiceFacade().quitAssistedServiceMode();
				asmSessionStatus = false;
			}
		}

		// render component only when it's necessary
		if (asmSessionStatus || getGpAssistedServiceFacade().isAssistedServiceAgentLoggedIn())
		{

			final String asmModuleView = "/WEB-INF/views/addons/" + getAddonUiExtensionName(component) + "/"
					+ getUIExperienceFolder() + "/cms/asm/assistedServiceComponent.jsp";
			final Map<String, Object> exposedVariables = exposeVariables(pageContext, component);
			pageContext.include(asmModuleView);
			exposedVariables.remove(AssistedserviceservicesConstants.AGENT); // agent can be used for other jsp\tags
			unExposeVariables(pageContext, component, exposedVariables);
		}
	}

	@Override
	protected Map<String, Object> getVariablesToExpose(final PageContext pageContext, final C component)
	{
		ServletRequest request = pageContext.getRequest();
		final Cookie asmCartIdCookie = WebUtils.getCookie((HttpServletRequest) request, ASM_CARTID);
		String cartId = asmCartIdCookie!=null ? asmCartIdCookie.getValue():null;
		
		final Map<String, Object> exposedVariables = super.getVariablesToExpose(pageContext, component);
		exposedVariables.putAll(getGpAssistedServiceFacade().getAssistedServiceSessionAttributes(cartId));
		exposedVariables.put("cartId", cartId);
		return exposedVariables;
	}


	protected void handleException(final Throwable throwable, final C component)
	{
		LOG.warn("Error processing component tag. currentComponent [" + component + "] exception: " + throwable.getMessage());
	}

	public GPAssistedServiceFacade getGpAssistedServiceFacade() {
		return gpAssistedServiceFacade;
	}

	public void setGpAssistedServiceFacade(GPAssistedServiceFacade gpAssistedServiceFacade) {
		this.gpAssistedServiceFacade = gpAssistedServiceFacade;
	}



}
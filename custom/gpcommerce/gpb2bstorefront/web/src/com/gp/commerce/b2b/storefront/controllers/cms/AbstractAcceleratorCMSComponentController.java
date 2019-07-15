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
package com.gp.commerce.b2b.storefront.controllers.cms;

import java.io.IOException;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.ui.Model;

import com.gp.commerce.b2b.storefront.controllers.ControllerConstants;
import com.gp.commerce.config.bundlesources.GPMessageResourcesAccessor;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.cms.AbstractCMSComponentController;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.servicelayer.i18n.I18NService;


/**
 * Abstract accelerator CMS component controller providing a common implementation for the getView method.
 */
public abstract class AbstractAcceleratorCMSComponentController<T extends AbstractCMSComponentModel>
		extends AbstractCMSComponentController<T>
{
	private static final Logger LOG = Logger.getLogger(AbstractAcceleratorCMSComponentController.class);

	@Resource(name = "gpMessageResourceAccessor")
	private GPMessageResourcesAccessor gpMessageResourceAccessor;

	@Resource(name = "i18nService")
	private I18NService i18nService;

	@Override
	protected String getView(final T component)
	{
		// build a jsp response based on the component type
		return ControllerConstants.Views.Cms.ComponentPrefix + StringUtils.lowerCase(getTypeCode(component));
	}

	@Override
	protected String handleComponent(final HttpServletRequest request, final HttpServletResponse response, final Model model,
			final T component) //NOSONAR
	{
		final Locale currentLocale = i18nService.getCurrentLocale();
		final ObjectMapper mapperObj = new ObjectMapper();
		String jsonResp = null;

		try {
			jsonResp = mapperObj
					.writeValueAsString(gpMessageResourceAccessor.getAllMessages(currentLocale, component.getUid()));
		} catch (final IOException e) {
			// TODO : Need to find better way
			// TODO : Log errors using Logging Framework
			LOG.error(e.getMessage(), e);
		}
		model.addAttribute("JSON_ATTRIBUTE", jsonResp);
		return super.handleComponent(request, response, model, component);
	}

}

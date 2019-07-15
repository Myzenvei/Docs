/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.config.bundlesources;

import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Utilities;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.StringUtils;


public class GPMessageResourcesAccessor extends ReloadableResourceBundleMessageSource
{
	/**
	 * path, containing messages
	 */
	public static final String GP_MESSAGE_PATH = "/web/webroot/WEB-INF/messages/";
	public static final String COMMON_FILE = "common";
	private static final String GPSERVICE_EXTENSION = "gpcommercewebservices";
	private BaseSiteService baseSiteService;


	@SuppressWarnings("unchecked")
	public Map<String, String> getAllMessages(final Locale locale, final String componentName)
	{
		final String path = getMessageSourcePath() + getBaseSiteService().getCurrentBaseSite().getUid() + "/";
		final Map propertyMap = new HashMap<>();

		setBasename(path + COMMON_FILE);
		final PropertiesHolder commonPropsHolder = getMergedProperties(locale);
		propertyMap.putAll(commonPropsHolder.getProperties());

		setBasename(path + componentName);
		final PropertiesHolder propsHolder = getMergedProperties(locale);
		propertyMap.putAll(propsHolder.getProperties());

		return propertyMap;
	}

	@Override
	protected PropertiesHolder getMergedProperties(final Locale locale)
	{
		PropertiesHolder mergedHolder = null;

		final Properties mergedProps = newProperties();
		long latestTimestamp = -1;
		final String[] basenames = StringUtils.toStringArray(getBasenameSet());
		for (int i = basenames.length - 1; i >= 0; i--)
		{
			final List<String> filenames = calculateAllFilenames(basenames[i], locale);
			for (int j = filenames.size() - 1; j >= 0; j--)
			{
				final String filename = filenames.get(j);
				final PropertiesHolder propHolder = getProperties(filename);
				if (propHolder.getProperties() != null)
				{
					mergedProps.putAll(propHolder.getProperties());
					if (propHolder.getFileTimestamp() > latestTimestamp)
					{
						latestTimestamp = propHolder.getFileTimestamp();
					}
				}
			}
		}
		mergedHolder = new PropertiesHolder(mergedProps, latestTimestamp);
		return mergedHolder;
	}

	/**
	 * Getting full path for addOn messages properties file
	 *
	 * @return String
	 */
	protected String getMessageSourcePath()
	{
		return "file:///" + Utilities.getExtensionInfo(GPSERVICE_EXTENSION).getExtensionDirectory() + getGpMessagePath();
	}


	public String getGpMessagePath()
	{
		return GP_MESSAGE_PATH;
	}

	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

}

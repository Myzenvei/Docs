/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.populators;

import de.hybris.platform.servicelayer.i18n.I18NService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.gp.commerce.config.bundlesources.GPMessageResourcesAccessor;
import com.gp.commerce.core.services.GPCMSSiteService;


/**
 * Populator for populating JSP Include properties Data
 *
 */
public class GPJspIncludePropertiesDataPopulator
{

	private static final Logger LOG = Logger.getLogger(GPJspIncludePropertiesDataPopulator.class);

	private static final String SOCIAL_AGGR_START_DATE_B2C_KEY = "socialaggrstartdate.b2c";
	private static final String SOCIAL_AGGR_START_DATE = "startDate";
	private static final String SOCIAL_AGGR_END_DATE_B2C_KEY = "socialaggrenddate.b2c";
	private static final String SOCIAL_AGGR_END_DATE = "endDate";
	private static final String SOCIAL_AGGR_FEEDID_B2C_KEY = "socialaggrfeedId.b2c";
	private static final String SOCIAL_AGGR_FEEDID = "feedId";
	private static final String SOCIAL_AGGR_TRUNCATE_KEY = "socialaggrtruncate.b2c";
	private static final String SOCIAL_AGGR_TRUNCATE = "truncate";
	private static final String SOCIAL_AGGR_GUTTER_KEY = "socialaggrgutter.b2c";
	private static final String SOCIAL_AGGR_GUTTER = "gutter";
	private static final String SOCIAL_AGGR_COLUMNS_KEY = "socialaggrcolumns.b2c";
	private static final String SOCIAL_AGGR_COLUMNS = "columns";
	private static final String SOCIAL_AGGR_INTERVAL_KEY = "socialaggrinterval.b2c";
	private static final String SOCIAL_AGGR_INTERVAL = "interval";
	private static final String SOCIAL_AGGR_FILTER_KEY = "socialaggrfilter.b2c";
	private static final String SOCIAL_AGGR_FILTER = "filter";
	private static final String SOCIAL_AGGR_STYLE_KEY = "socialaggrstyle.b2c";
	private static final String SOCIAL_AGGR_STYLE = "style";
	private static final String SOCIAL_AGGR_OVERLAY_KEY = "socialaggroverlay.b2c";
	private static final String SOCIAL_AGGR_OVERLAY = "overlay";
	private static final String SOCIAL_AGGR_DATAPER = "dataPer";
	private static final String SOCIAL_AGGR_DATAPER_KEY = "socialaggdataper.b2c";

	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;

	@Resource(name = "gpMessageResourceAccessor")
	private GPMessageResourcesAccessor gpMessageResourceAccessor;

	@Resource(name = "i18nService")
	private I18NService i18nService;


	/**
	 * @param source
	 *           - JSPIncludeComponent uid
	 * @return jsp properties data
	 */
	public String populate(final String source)
	{
		final Locale currentLocale = i18nService.getCurrentLocale();
		final ObjectMapper mapperObj = new ObjectMapper();
		String response = null;
		try
		{
			response = mapperObj.writeValueAsString(gpMessageResourceAccessor.getAllMessages(currentLocale, source));
		}
		catch (final IOException e)
		{
			LOG.error(e.getMessage(),e);
		}

		return response;
	}
	
	/**
	 * Method to set up social aggregator data
	 * @param source
	 * @return juicerfeed
	 */
	public String populateJuicerFeed()
	{
		final Map fieldsMap = new HashMap<>();

		fieldsMap.put(SOCIAL_AGGR_START_DATE,cmsSiteService.getSiteConfig(SOCIAL_AGGR_START_DATE_B2C_KEY));
		fieldsMap.put(SOCIAL_AGGR_END_DATE,cmsSiteService.getSiteConfig(SOCIAL_AGGR_END_DATE_B2C_KEY));
		fieldsMap.put(SOCIAL_AGGR_FEEDID,cmsSiteService.getSiteConfig(SOCIAL_AGGR_FEEDID_B2C_KEY));
		fieldsMap.put(SOCIAL_AGGR_TRUNCATE,cmsSiteService.getSiteConfig(SOCIAL_AGGR_TRUNCATE_KEY));
		fieldsMap.put(SOCIAL_AGGR_GUTTER,cmsSiteService.getSiteConfig(SOCIAL_AGGR_GUTTER_KEY));
		fieldsMap.put(SOCIAL_AGGR_COLUMNS,cmsSiteService.getSiteConfig(SOCIAL_AGGR_COLUMNS_KEY));
		fieldsMap.put(SOCIAL_AGGR_INTERVAL,cmsSiteService.getSiteConfig(SOCIAL_AGGR_INTERVAL_KEY));
		fieldsMap.put(SOCIAL_AGGR_FILTER,cmsSiteService.getSiteConfig(SOCIAL_AGGR_FILTER_KEY));
		fieldsMap.put(SOCIAL_AGGR_STYLE,cmsSiteService.getSiteConfig(SOCIAL_AGGR_STYLE_KEY));
		fieldsMap.put(SOCIAL_AGGR_OVERLAY,cmsSiteService.getSiteConfig(SOCIAL_AGGR_OVERLAY_KEY));
		fieldsMap.put(SOCIAL_AGGR_DATAPER,cmsSiteService.getSiteConfig(SOCIAL_AGGR_DATAPER_KEY));


		final ObjectMapper mapperObj = new ObjectMapper();
		String juicerFeed = null;
		try {
			juicerFeed = mapperObj.writeValueAsString(fieldsMap);
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}

		return juicerFeed;
	}
}

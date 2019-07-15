/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.log4j.Logger;

import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.exception.GPIntegrationException;

public class GPIntegrationUtils {
	public static final Logger LOG = Logger.getLogger(GPIntegrationUtils.class);
	
	private GPIntegrationUtils() {
		
	}
	
	public static String formatDate(String dateString, String inputFormat, String outputFormat) {
		SimpleDateFormat inputDateFormatter = new SimpleDateFormat(inputFormat);
		SimpleDateFormat outputDateFormatter = new SimpleDateFormat(outputFormat);
		String formattedDate = null;
		Date date;
		try {
			date = inputDateFormatter.parse(dateString);
			formattedDate = outputDateFormatter.format(date);
		} catch (ParseException parseException) {
			LOG.error("Integration utils date conversion failed with exception:",parseException);
		}
		return formattedDate;
	}
	
	public static String getJobLastRunTime(Date inputDate, boolean needsCalculation, String timeZone, long days) throws GPIntegrationException{
		final DateTimeFormatter remorsePeriodDateFormat = DateTimeFormatter
				.ofPattern(GpintegrationConstants.SFDC_PRODUCT_REPLICATION_LAST_RUNTIME_DATE_FORMAT);
		LocalDateTime lastRunDateTime = null;
		try {
			if(needsCalculation) {
				lastRunDateTime = inputDate.toInstant().atZone(ZoneId.of(timeZone)).toLocalDateTime()
						.minusDays(days);
			} else {
				lastRunDateTime = inputDate.toInstant().atZone(ZoneId.of(timeZone)).toLocalDateTime();
			}
		} catch(DateTimeException dte) {
			throw new GPIntegrationException("GPIntegrationUtils date calculation failed:"+dte.getMessage(),dte);
		} catch(Exception e) {
			throw new GPIntegrationException("GPIntegrationUtils date calculation failed:"+e.getMessage(),e);
		}
		return remorsePeriodDateFormat.format(lastRunDateTime);
	}
}

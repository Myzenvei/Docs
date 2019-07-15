/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.util;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gp.commerce.core.calculation.service.impl.GPDefaultOneSourceTaxCalculationService;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import de.hybris.platform.util.Config;

import de.hybris.platform.core.model.c2l.CurrencyModel;

/**
 * Created by sandubey on 6/15/18.
 */
public class GPFunctions {
	private static final Logger LOG = Logger.getLogger(GPFunctions.class);
    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

    public static <T> String convertToJSON(final T data) {
        if (data == null) {
            return "";
        }

        return gson.toJson(data);
    }

    public static String convertStringToJSON(final String data) {
        if (data == null) {
            return "";
        }

        return gson.toJson(data);
    }

	public static String convertObjectToJSON(final Object data)
	{
		if (data == null)
		{
			return "";
		}

		return gson.toJson(data);
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
	
	public static String getStartDateRange(Date selectedDate) {
		final Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf =null ;
		calendar.setTime(selectedDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		selectedDate = calendar.getTime();
		String localEnv  = Config.getParameter("gp.order.history.env");
		if(localEnv!=null && localEnv.equalsIgnoreCase(GpcommerceCoreConstants.ENV_LOCAL)) {
			sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			return "to_date('"+sdf.format(selectedDate)+"','DD-MM-YYYY HH24:MI:SS')";
		}else {
		 sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		}
		return sdf.format(selectedDate);
	}
	
	public static String getFormattedDate(Date selectedDate) {
		final Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf =null ;
		calendar.setTime(selectedDate);
		selectedDate = calendar.getTime();
		String localEnv  = Config.getParameter("gp.order.history.env");
		if(localEnv!=null && localEnv.equalsIgnoreCase(GpcommerceCoreConstants.ENV_LOCAL)) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}else {
		 sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		}
		return sdf.format(selectedDate);
	}

	public static String getEndDateRange(Date selectedDate) {
		final Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf=null ;
		calendar.setTime(selectedDate);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		selectedDate = calendar.getTime();
		String localEnv  = Config.getParameter("gp.order.history.env");
		if(localEnv!=null && localEnv.equalsIgnoreCase(GpcommerceCoreConstants.ENV_LOCAL)) {
			sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			return "to_date('"+sdf.format(selectedDate)+"','DD-MM-YYYY HH24:MI:SS')";
		}else {
		 sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		}
		return sdf.format(selectedDate);
	}
	
	
	public static DecimalFormat adjustDigits(DecimalFormat format, CurrencyModel currencyModel) {
		final int tempDigits = currencyModel.getDigits() == null ? 0 : currencyModel.getDigits().intValue();
		final int digits = Math.max(0, tempDigits);

		format.setMaximumFractionDigits(digits);
		format.setMinimumFractionDigits(digits);
		if (digits == 0)
		{
			format.setDecimalSeparatorAlwaysShown(false);
		}

		return format;
	}

	public static DecimalFormat adjustSymbol(DecimalFormat format, CurrencyModel currencyModel) {
		final String symbol = currencyModel.getSymbol();
		if (symbol != null)
		{
			final DecimalFormatSymbols symbols = format.getDecimalFormatSymbols(); // does cloning
			final String iso = currencyModel.getIsocode();
			boolean changed = false;
			if (!iso.equalsIgnoreCase(symbols.getInternationalCurrencySymbol()))
			{
				symbols.setInternationalCurrencySymbol(iso);
				changed = true;
			}
			if (!symbol.equals(symbols.getCurrencySymbol()))
			{
				symbols.setCurrencySymbol(symbol);
				changed = true;
			}
			if (changed)
			{
				format.setDecimalFormatSymbols(symbols);
			}
		}
		return format;
	}

	/**
	 * Target url from session attribute.
	 *
	 * @param request
	 *           the request
	 * @return the string
	 */
	public static String targetUrlFromSessionAttribute(final HttpServletRequest request)
	{
		final HttpSession session = request.getSession(true);
		final String redirectionUri = (String) session.getAttribute("uri");
		session.removeAttribute("uri");
		return redirectionUri;
	}
	
	/**
	 * Check for Subscrible cart from session attribute.
	 *
	 * @param request
	 *           the request
	 * @return the string key
	 */
	public static String getSessionAttributeValue(final HttpServletRequest request,final String key)
	{
		final HttpSession session = request.getSession(true);
		final String value = (String) session.getAttribute(key);
		session.removeAttribute(key);
		return value;
	}
}
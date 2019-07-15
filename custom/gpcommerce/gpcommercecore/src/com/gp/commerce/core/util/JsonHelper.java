/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.util;


import org.apache.log4j.Logger;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * This class is acts as a JsonHelper class.
 *
 * @author uchandrasekaran
 */

public class JsonHelper
{

	private static final Logger LOG = Logger.getLogger(JsonHelper.class);
	/**
	 * Defaulf constructor.
	 */

	public JsonHelper()
	{

	}


	/**
	 * This method is used to convert object into JSON string.
	 *
	 * @param obj
	 *           input parameter to be converted into JSON string.
	 * @return json string from given input object.
	 */
	public static String getJson(final Object obj)
	{
		String json = null;
		try
		{
			final ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.ALWAYS);
			json = mapper.writer().writeValueAsString(obj);
		}
		catch (final Exception exception)
		{
			LOG.error("Exception occured when converting Java object to json string. Exception message: ", exception);
		}
		return json;
	}

	/**
	 * JSON string to object conversion
	 *
	 * @param json
	 * @param typeReference
	 * @return
	 */
	public static <T> T getObject(final String json, final TypeReference<T> typeReference)
	{
		T obj = null;
		try
		{
			obj = new ObjectMapper().readValue(json, typeReference);
		}
		catch (final Exception exception)
		{
			LOG.error("Exception occured when converting string into object. Exception message: ", exception);
		}
		return obj;
	}
	
	/**
	 * JSON string to object conversion
	 *
	 * @param json
	 * @param typeReference - Class type
	 * @return
	 */
	
	public static <T> T getJSONObject(final String json, final Class<T> typeReference)
	{
		T obj = null;
		try
		{
			obj = new ObjectMapper().readValue(json, typeReference);
		}
		catch (final Exception exception)
		{
			LOG.error("Exception occured when converting string into object. Exception message: ", exception);
		}
		return obj;
	}



}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.strategies;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.commerceservices.strategies.impl.DefaultCustomerNameStrategy;

public class GPDefaultCustomerNameStrategy extends DefaultCustomerNameStrategy {
	
	private static final String SEPARATOR_SPACE = " ";
	private static final String SEPARATOR_PIPE = "|";

	@Override
	public String[] splitName(final String name)
	{
		final String trimmedName = StringUtils.trimToNull(name);
		return new String[] { StringUtils.substringBeforeLast(trimmedName, SEPARATOR_PIPE),
				StringUtils.substringAfterLast(trimmedName, SEPARATOR_PIPE) };
	}
	
	public String[] splitNameBySpace(final String name) {
		return name.split(SEPARATOR_SPACE);
	}

	@Override
	public String getName(final String firstName, final String lastName)
	{
		final String result = StringUtils.trimToEmpty(firstName) + SEPARATOR_PIPE + StringUtils.trimToEmpty(lastName);
		return StringUtils.trimToNull(result);
	}
	
	public String getName(final String name)
	{
		String[] splitNames = splitName(name);
		String lastName="";
		if(splitNames.length>1) {
			lastName= SEPARATOR_SPACE + StringUtils.trimToEmpty(splitNames[1]);
		}
	
		return StringUtils.trimToEmpty(splitNames[0]) + lastName;
	}

}

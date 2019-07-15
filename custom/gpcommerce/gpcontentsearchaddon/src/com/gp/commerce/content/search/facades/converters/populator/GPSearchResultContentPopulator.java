/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.content.search.facades.converters.populator;

import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.gp.commerce.content.search.facades.data.GPContentPageData;

public class GPSearchResultContentPopulator implements Populator<SearchResultValueData, GPContentPageData>{

	private CommonI18NService commonI18NService;
	private UrlResolver<ContentPageModel> urlResolver;

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	@Override
	public void populate(final SearchResultValueData source, final GPContentPageData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		target.setDescription(this.<String> getValue(source, "description"));
		target.setTitle(this.<String> getValue(source, "title"));
		target.setContentType(this.<String> getValue(source, "contentType"));
		target.setIsKnowledgeCenterDocument(this.<Boolean> getValue(source, "isKnowledgeCenterDocument"));
		target.setResourceImageUrl(this.<String> getValue(source, "resourceImageUrl"));
		populateUrl(source, target);
	}


	protected void populateUrl(final SearchResultValueData source, final GPContentPageData target)
	{
		final String url = this.<String> getValue(source, "pageUrl");
		if (StringUtils.isEmpty(url))
		{
			target.setUrl("");
		}
		else
		{
			target.setUrl(url);
		}
	}

	protected <T> T getValue(final SearchResultValueData source, final String propertyName)
	{
		if (source.getValues() == null)
		{
			return null;
		}

		// DO NOT REMOVE the cast (T) below, while it should be unnecessary it is required by the javac compiler
		return (T) source.getValues().get(propertyName);
	}

	public UrlResolver<ContentPageModel> getUrlResolver() {
		return urlResolver;
	}

	public void setUrlResolver(UrlResolver<ContentPageModel> urlResolver) {
		this.urlResolver = urlResolver;
	}

}

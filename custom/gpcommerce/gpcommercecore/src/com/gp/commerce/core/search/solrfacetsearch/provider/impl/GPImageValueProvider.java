/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class GPImageValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider {
	private static final Logger LOG = Logger.getLogger(GPImageValueProvider.class);

	private String mediaFormat;
	private MediaService mediaService;
	private MediaContainerService mediaContainerService;
	private FieldNameProvider fieldNameProvider;
	public String getMediaFormat() {
		return mediaFormat;
	}

	@Required
	public void setMediaFormat(final String mediaFormat) {
		this.mediaFormat = mediaFormat;
	}

	public MediaService getMediaService() {
		return mediaService;
	}

	@Required
	public void setMediaService(final MediaService mediaService) {
		this.mediaService = mediaService;
	}

	public MediaContainerService getMediaContainerService() {
		return mediaContainerService;
	}

	@Required
	public void setMediaContainerService(final MediaContainerService mediaContainerService) {
		this.mediaContainerService = mediaContainerService;
	}

	public FieldNameProvider getFieldNameProvider() {
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider) {
		this.fieldNameProvider = fieldNameProvider;
	}

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof PriceRowModel)
		{
			final MediaFormatModel mediaFormatModel = getMediaService().getFormat(getMediaFormat());
			if (mediaFormatModel != null)
			{
				final ProductModel product = ((PriceRowModel)model).getProduct();
				final MediaModel media = findMedia(product, mediaFormatModel);
				if (media != null)
				{
					return createFieldValues(indexedProperty, media);
				}
				if (LOG.isDebugEnabled())
				{
					LOG.debug("No [" + mediaFormatModel.getQualifier() + "] image found for product ["
							+ product.getCode() + "]");
				}
			}
		}
		return Collections.emptyList();
	}

	protected MediaModel findMedia(final ProductModel product, final MediaFormatModel mediaFormat)
	{
		if (product != null && mediaFormat != null)
		{
			List<MediaContainerModel> productImages = new ArrayList<>();

			// If there is a primary image for the product then pick that container otherwise pick gallery image container.
			if(null!=product.getPicture() && null != product.getPicture().getMediaContainer()) {
				productImages.add(product.getPicture().getMediaContainer());
			}else {
				productImages = product.getGalleryImages();
			}
			if (productImages != null && !productImages.isEmpty())
			{
				// Search each media container in the gallery for an image of the right format
				for (final MediaContainerModel container : productImages)
				{
					try
					{
						final MediaModel media = getMediaContainerService().getMediaForFormat(container, mediaFormat);
						if (media != null)
						{
							return media;
						}
					}
					catch (final ModelNotFoundException ignore)
					{
						// ignore
					}
				}
			}

			// Failed to find media in product
			if (product instanceof VariantProductModel)
			{
				// Look in the base product
				return findMedia(((VariantProductModel) product).getBaseProduct(), mediaFormat);
			}
		}
		return null;
	}

	protected Collection<FieldValue> createFieldValues(final IndexedProperty indexedProperty, final MediaModel media)
	{
		return createFieldValues(indexedProperty, media.getURL());
	}

	protected Collection<FieldValue> createFieldValues(final IndexedProperty indexedProperty, final String value)
	{
		final List<FieldValue> fieldValues = new ArrayList<>();

		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}

		return fieldValues;
	}


}

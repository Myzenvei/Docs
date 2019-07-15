/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.type.converter;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import de.hybris.platform.cmsfacades.data.ComponentTypeAttributeData;
import de.hybris.platform.cmsfacades.data.ComponentTypeData;
import de.hybris.platform.cmsfacades.types.converter.ComponentTypeStructureConverter;
import de.hybris.platform.cmsfacades.types.service.ComponentTypeStructure;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

/**
 * Gp Converter for Blacklisting attributes in custom cms components.
 */
public class GPDefaultCmsComponentTypeStructureModelConverter extends ComponentTypeStructureConverter
{

	private static final String GPBRANDBARCOMPONENT = "GPBrandBarComponent";
	private static final String GPIMAGETILECOMPONENT = "GPImageTileComponent";
	private static final String GPBANNERCOMPONENT = "GPBannerComponent";
	private static final String GPMARKETINGCOMPONENT = "GpMarketingSideBySideComponent";
	private static final String GPHEROCOMPONENT = "GPRotatingImagesComponent";
	private static final String GPIMAGETEXTCOMPONENT = "GPImageTextComponent";
	private static final String GPUNSUBSCRIBECOMPONENT = "GPUnsubscribeComponent";
	private static final String FOOTERNAVCOMPONENT = "FooterNavigationComponent";
	private static final String GPIMAGELINKCOMPONENT = "GPImageLinkComponent";
	private static final String GPTABBEDIMAGETILECOMPONENT = "GPTabbedImageTileComponent";


	@Override
	public ComponentTypeData convert(final ComponentTypeStructure source, final ComponentTypeData target)
			throws ConversionException
	{
		final ComposedTypeModel composedType = getTypeService().getComposedTypeForCode(source.getTypecode());

		// populates the category, but if there is a populator that populates it again, it will be overridden by the populator.
		target.setCategory(source.getCategory().name());

		// Populate component type properties
		source.getPopulators().forEach(populator -> populator.populate(composedType, target));

		// Convert attributes
		target.setAttributes(source.getAttributes().stream() //
				.map(attribute -> convertAttribute(attribute, getAttributeDescriptor(composedType, attribute))) //
				.filter(componentTypeAttributeData -> isNotBlank(componentTypeAttributeData.getCmsStructureType()))
				.collect(Collectors.toList()));

		getBlackListAttributes(source, target);

		getStringDecapitalizer() //
				.decapitalize(source.getTypeDataClass()) //
				.ifPresent(typeData -> target.setType(typeData));
		return target;
	}

	/**
	 * A method to retrieve the list of blacklisting attributes for the respective components.
	 *
	 * @param source
	 * @param target
	 */
	private void getBlackListAttributes(final ComponentTypeStructure source, final ComponentTypeData target)
	{
		if (source.getTypecode().equalsIgnoreCase(GPBRANDBARCOMPONENT))
		{
			final String brandBarAttributes = Config.getParameter("blacklist.brandbar");
			if(StringUtils.isNotBlank(brandBarAttributes))
			{
				final String[] blacklistAttributes = brandBarAttributes.split(",");
				blackListComponentAttributes(target, blacklistAttributes);
			}
		}
		else if (source.getTypecode().equalsIgnoreCase(GPBANNERCOMPONENT))
		{
			final String bannerAttributes = Config.getParameter("blacklist.banner");
			if (StringUtils.isNotBlank(bannerAttributes))
			{
				final String[] blacklistBannerAttributes = bannerAttributes.split(",");
				blackListComponentAttributes(target, blacklistBannerAttributes);
			}
		}
		else if (source.getTypecode().equalsIgnoreCase(GPIMAGETILECOMPONENT))
		{
			final String imageTileAttributes = Config.getParameter("blacklist.imagetile");
			if (StringUtils.isNotBlank(imageTileAttributes))
			{
				final String[] blacklistImageTileAttributes = imageTileAttributes.split(",");
				blackListComponentAttributes(target, blacklistImageTileAttributes);
			}
		}
		else if (source.getTypecode().equalsIgnoreCase(GPMARKETINGCOMPONENT))
		{
			final String marketingAttributes = Config.getParameter("blacklist.marketingSidebySide");
			if (StringUtils.isNotBlank(marketingAttributes))
			{
				final String[] blacklistMarketingAttributes = marketingAttributes.split(",");
				blackListComponentAttributes(target, blacklistMarketingAttributes);
			}
		}
		else if (source.getTypecode().equalsIgnoreCase(GPHEROCOMPONENT))
		{
			final String heroBannerAttributes = Config.getParameter("blacklist.rotatingImage");
			if (StringUtils.isNotBlank(heroBannerAttributes))
			{
				final String[] blacklistHeroBannerAttributes = heroBannerAttributes.split(",");
				blackListComponentAttributes(target, blacklistHeroBannerAttributes);
			}
		}
		else if (source.getTypecode().equalsIgnoreCase(GPIMAGETEXTCOMPONENT))
		{
			final String imageTextAttributes = Config.getParameter("blacklist.imagetext");
			if (StringUtils.isNotBlank(imageTextAttributes))
			{
				final String[] blacklistImageTextAttributes = imageTextAttributes.split(",");
				blackListComponentAttributes(target, blacklistImageTextAttributes);
			}
		}
		else if (source.getTypecode().equalsIgnoreCase(GPUNSUBSCRIBECOMPONENT))
		{
			final String unsubscribeAttributes = Config.getParameter("blacklist.unsubscribe");
			if (StringUtils.isNotBlank(unsubscribeAttributes))
			{
				final String[] blackUnsubscribeAttributes = unsubscribeAttributes.split(",");
				blackListComponentAttributes(target, blackUnsubscribeAttributes);
			}
		}
		else if (source.getTypecode().equalsIgnoreCase(FOOTERNAVCOMPONENT))
		{
			final String footerAttributes = Config.getParameter("blacklist.footer");
			if (StringUtils.isNotBlank(footerAttributes))
			{
				final String[] blackFooterAttributes = footerAttributes.split(",");
				blackListComponentAttributes(target, blackFooterAttributes);
			}
		}
		else if (source.getTypecode().equalsIgnoreCase(GPIMAGELINKCOMPONENT))
		{
			final String imagelinkAttributes = Config.getParameter("blacklist.imagelink");
			if (StringUtils.isNotBlank(imagelinkAttributes))
			{
				final String[] blackImageLinkAttributes = imagelinkAttributes.split(",");
				blackListComponentAttributes(target, blackImageLinkAttributes);
			}
		}
		else if (source.getTypecode().equalsIgnoreCase(GPTABBEDIMAGETILECOMPONENT))
		{
			final String imagelinkAttributes = Config.getParameter("blacklist.tabbedimagetile");
			if (StringUtils.isNotBlank(imagelinkAttributes))
			{
				final String[] blackImageLinkAttributes = imagelinkAttributes.split(",");
				blackListComponentAttributes(target, blackImageLinkAttributes);
			}
		}

	}


	/**
	 * A method to remove blacklisting attributes from the target.
	 *
	 * @param target
	 * @param blacklistAttributes
	 *
	 */
	private void blackListComponentAttributes(final ComponentTypeData target, final String[] blacklistAttributes)
	{
		final List<ComponentTypeAttributeData> attributes = target.getAttributes();
		final List<ComponentTypeAttributeData> toRemove = new ArrayList<ComponentTypeAttributeData>();
		final List<String> blacklist = Arrays.asList(blacklistAttributes);
		for (final ComponentTypeAttributeData componentTypeAttributeStructure : attributes)
		{
			if (blacklist.contains(componentTypeAttributeStructure.getQualifier()))
			{
				toRemove.add(componentTypeAttributeStructure);
			}
		}
		attributes.removeAll(toRemove);
		target.setAttributes(attributes);
	}

}

/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.ymkt.gpymktrecommendationaddon.facades;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.commerceservices.product.data.ReferenceData;
import de.hybris.platform.converters.ConfigurablePopulator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.facades.data.WishlistData;
import com.gp.commerce.facades.wishlist.GPWishlistFacade;
import com.hybris.ymkt.recommendation.dao.ImpressionContext;
import com.hybris.ymkt.recommendation.dao.ProductRecommendationData;
import com.hybris.ymkt.recommendation.dao.RecommendationContext;
import com.hybris.ymkt.recommendation.services.ImpressionService;
import com.hybris.ymkt.recommendation.services.RecommendationService;


/**
 * Facade for product recommendation controller.
 */
public class ProductRecommendationManagerFacade
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductRecommendationManagerFacade.class);

	private static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.values());

	protected ImpressionService impressionService;
	protected ProductService productService;
	protected RecommendationService recommendationService;
	protected Converter<ReferenceData<ProductReferenceTypeEnum, ProductModel>, ProductReferenceData> referenceDataProductReferenceConverter;
	protected ConfigurablePopulator<ProductModel, ProductData, ProductOption> referenceProductConfiguredPopulator;

	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;

	@Resource(name = "wishlistFacade")
	private GPWishlistFacade gpDefaultWishlistFacade;

	private void setFavoriteFlagForProducts(final List<ProductReferenceData> productReferenceDataList)
	{
		final String isFavoritesConfigured = cmsSiteService.getSiteConfig("isFavoritesEnabled");
		if (productReferenceDataList != null && Boolean.TRUE.equals(Boolean.valueOf(isFavoritesConfigured)))
		{
					final WishlistData wishlistData = gpDefaultWishlistFacade.getFavorites();
					productReferenceDataList.stream().forEach(productData -> {
						final ProductData product = productData.getTarget();
						product.setIsFavorite(false);
						if (null != wishlistData.getWishlistEntries() && wishlistData.getWishlistEntries().stream()
							.anyMatch(wishData -> wishData.getProduct().getCode().equalsIgnoreCase(product.getCode())))
							{
								product.setIsFavorite(true);
							}
					});
		}
	}

	protected ProductReferenceData createProductReferenceData(final ReferenceData<ProductReferenceTypeEnum, ProductModel> ref)
	{
		final ProductReferenceData data = this.referenceDataProductReferenceConverter.convert(ref);
		this.referenceProductConfiguredPopulator.populate(ref.getTarget(), data.getTarget(), PRODUCT_OPTIONS);
		return data;
	}



	protected ReferenceData<ProductReferenceTypeEnum, ProductModel> createReferenceData(final ProductModel product)
	{
		final ReferenceData<ProductReferenceTypeEnum, ProductModel> referenceData = new ReferenceData<>();
		referenceData.setQuantity(1);
		referenceData.setReferenceType(ProductReferenceTypeEnum.OTHERS);
		referenceData.setTarget(product);
		return referenceData;
	}

	/**
	 * Read {@link ProductModel} using {@link ProductModel#getCode()}.
	 *
	 * @param productCode
	 *           {@link ProductModel#getCode()}.
	 * @return {@link Optional} of {@link ProductModel}.
	 */
	public Optional<ProductModel> findProduct(String productCode)
	{
		try
		{
			return Optional.of(this.productService.getProductForCode(productCode));
		}
		catch (UnknownIdentifierException | AmbiguousIdentifierException e)
		{
			LOGGER.error("Product '{}' not found.", productCode, e);
			return Optional.empty();
		}
	}

	/**
	 * Read a {@link List} of {@link ProductReferenceData} according to {@link RecommendationContext}.
	 *
	 * @param context
	 *           {@link RecommendationContext}.
	 * @return {@link List} of {@link ProductReferenceData}.
	 */
	public List<ProductReferenceData> getProductRecommendation(RecommendationContext context)
	{
		final List<ProductReferenceData> productReferenceDataList = this.recommendationService.getProductRecommendation(context)
				.stream() //
				.map(ProductRecommendationData::getProductCode) //
				.map(this::findProduct) //
				.filter(Optional::isPresent) //
				.map(Optional::get) //
				.map(this::createReferenceData) //
				.map(this::createProductReferenceData) //
				.collect(Collectors.toList());
		setFavoriteFlagForProducts(productReferenceDataList);
		return productReferenceDataList;
	}

	/**
	 * Save individual impressions when UI component is viewed.
	 *
	 * @param context
	 *           {@link ImpressionContext}
	 */
	public void saveImpression(final ImpressionContext context)
	{
		this.impressionService.saveImpression(context);
	}

	@Required
	public void setImpressionService(ImpressionService impressionService)
	{
		this.impressionService = impressionService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	@Required
	public void setRecommendationService(RecommendationService recommendationService)
	{
		this.recommendationService = recommendationService;
	}

	@Required
	public void setReferenceDataProductReferenceConverter(
			final Converter<ReferenceData<ProductReferenceTypeEnum, ProductModel>, ProductReferenceData> referenceDataProductReferenceConverter)
	{
		this.referenceDataProductReferenceConverter = referenceDataProductReferenceConverter;
	}

	@Required
	public void setReferenceProductConfiguredPopulator(
			final ConfigurablePopulator<ProductModel, ProductData, ProductOption> referenceProductConfiguredPopulator)
	{
		this.referenceProductConfiguredPopulator = referenceProductConfiguredPopulator;
	}
}

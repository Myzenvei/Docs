/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.product.impl;

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.data.BaseOptionData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantMatrixElementData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.commercefacades.product.data.VariantOptionQualifierData;
import de.hybris.platform.commercefacades.product.impl.DefaultProductFacade;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.core.company.services.GPB2BUnitsService;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.product.services.GPProductService;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gp.commerce.facades.data.WishlistData;
import com.gp.commerce.facades.product.GPProductFacade;
import com.gp.commerce.facades.product.data.DataSheetsData;
import com.gp.commerce.facades.product.data.ProductResourcesVideosData;
import com.gp.commerce.facades.search.compare.GPSearchCompareProductsFacade;
import com.gp.commerce.facades.wishlist.impl.GPDefaultWishlistFacade;
import com.gp.commerce.product.data.GPProductDataList;
import com.gpintegration.knowledgecenter.dto.GPKnowledgeCenterSkuResponse;
import com.gpintegration.knowledgecenter.dto.InfoResourcesDTO;
import com.gpintegration.knowledgecenter.dto.SupportResourcesDTO;
import com.gpintegration.service.GPKnowledgeCenterService;


/**
 * Implementation of product related functionalities.
 */
public class GPDefaultProductFacade extends DefaultProductFacade implements GPProductFacade,ProductFacade
{
	private static final String STYLE_VARIANT_PRODUCT = "StyleVariantProduct";
	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	@Resource(name = "gpProductService")
	GPProductService gpProductService;	

	@Resource(name = "searchCompareProductsFacade")
	private GPSearchCompareProductsFacade searchCompareProductsFacade;

	@Resource(name = "wishlistFacade")
	GPDefaultWishlistFacade gpDefaultWishlistFacade;

	@Resource(name = "gpKnowledgeCenterService")
	GPKnowledgeCenterService gpKnowledgeCenterService;

	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	
	@Resource(name = "gpB2BUnitsService")
	private GPB2BUnitsService<B2BUnitModel, UserModel> gpB2BUnitsService; 
	
	@Resource(name = "sessionService")
	private SessionService sessionService;
	
	@Resource(name = "userService")
	private UserService userService;

	private static final Logger LOG = Logger.getLogger(GPDefaultProductFacade.class);

	private static final String DELIMITER = "_";
	
	private static final String IMG_3D_SUFFIX = "3D";


	private static final Character DELIMITER_CHAR = '_';

	@Override
	public void populateAdditionalFields(final ProductData product)
	{
		populateSpecifications(product);
		
		if (CollectionUtils.isNotEmpty(product.getImages()))
		{
			product.setImages(restructureImageData(product.getImages(),product.getKaonUrl()));
			LOG.debug(format( "Kaon URL for product '%1$s'is '%2$s' " ,product.getCode(), product.getKaonUrl()));
		}
		
		if (product.getIsSubscribable().equals(Boolean.TRUE)
				&& userService.getCurrentUser() instanceof B2BCustomerModel) {
			B2BCustomerModel b2bCustomer = (B2BCustomerModel) userService.getCurrentUser();
			if (null != b2bCustomer.getUserApprovalStatus() && b2bCustomer.getUserApprovalStatus().getCode()
					.equalsIgnoreCase((GpcommerceCoreConstants.PENDING))) {
				product.setDisableSubscribeButton(true);
			}
		}

		updateBaseOptions(product);
		setColorCodesInStyleVariants(product);
		setFavoriteFlagForProduct(product);
		populateKnwoledgeCenterData(product);
	}

	/**
	 * Sets the hexadecimal color codes corresponding to the color names in variant options.
	 *
	 * @param productData
	 *           The product
	 */
	private void setColorCodesInStyleVariants(final ProductData productData)
	{
		if (CollectionUtils.isNotEmpty(productData.getBaseOptions()))
		{
			final List<BaseOptionData> baseOptions = productData.getBaseOptions();

			if (CollectionUtils.isNotEmpty(baseOptions))
			{
				final Map<String, String> colorCodeMap = gpProductService.fetchAllColorCodes();
				final List<BaseOptionData> styleBaseOption = baseOptions.stream()
						.filter(baseOpt -> baseOpt.getVariantType().contains(STYLE_VARIANT_PRODUCT)
								&& CollectionUtils.isNotEmpty(baseOpt.getOptions()))
						.collect(Collectors.toList());

				for (final BaseOptionData variant : styleBaseOption)
				{
					variant.getOptions().stream().filter(var -> CollectionUtils.isNotEmpty(var.getVariantOptionQualifiers()))
							.forEach(var -> {
								var.getVariantOptionQualifiers().stream().forEach(optData -> {
									if (Config.getParameter(GpcommerceCoreConstants.DUMMY_STYLE_VARIANT_COLOR)
											.equalsIgnoreCase(optData.getValue()))
									{
										variant.setIsStyleVariant(false);
									}
									else
									{
										optData.setHexCode(colorCodeMap.get(optData.getValue()));
									}
								});
							});
				}
			}
		}

	}

	/**
	 * @param product
	 */
	private void updateBaseOptions(final ProductData product)
	{
		final List<BaseOptionData> baseOptions = product.getBaseOptions();
		if (CollectionUtils.isNotEmpty(baseOptions))
		{
			baseOptions.stream().forEach(baseOption -> {
				baseOption.setIsStyleVariant(baseOption.getVariantType().contains(STYLE_VARIANT_PRODUCT) ? true : false);
				baseOption.setIsSizeVariant(!baseOption.isIsStyleVariant());
				final String code = baseOption.getSelected().getCode();
				if (CollectionUtils.isNotEmpty(baseOption.getOptions()))
				{
					final Optional<VariantOptionData> selectedOption = baseOption.getOptions().stream()
							.filter(base -> code.equals(base.getCode())).findFirst();
					if (selectedOption.isPresent())
					{
						selectedOption.get().setIsSelected(true);
					}
				}
			
			if(CollectionUtils.isNotEmpty(product.getVariantMatrix()) && CollectionUtils.isNotEmpty(baseOption.getOptions())) {
				baseOption.getOptions().stream().forEach(option -> {
					addQualifierData(option, product.getVariantMatrix());
				});
			}
				
			});
			
			
		}
	}
	
	private void addQualifierData(VariantOptionData option, List<VariantMatrixElementData> elements) {
		for(VariantMatrixElementData element : elements) {
			if(element.getVariantOption().getCode().equals(option.getCode())) {
				option.getVariantOptionQualifiers().add(createVariantQualifiers(element));
			}
			if(CollectionUtils.isNotEmpty(element.getElements())) {
				addQualifierData(option, element.getElements());
			}
		}	
	}
	
	private VariantOptionQualifierData createVariantQualifiers(VariantMatrixElementData element) {
		VariantOptionQualifierData qualifierData = new VariantOptionQualifierData();
		
		qualifierData.setQualifier("variantValue");
		qualifierData.setName(element.getParentVariantCategory().getName());
		qualifierData.setValue(element.getVariantValueCategory().getName());
		qualifierData.setSequence(Integer.toString(element.getVariantValueCategory().getSequence()));
		if(null != element.getVariantOption() && CollectionUtils.isNotEmpty(element.getVariantOption().getVariantOptionQualifiers()) && "color".equalsIgnoreCase(element.getParentVariantCategory().getName())) {
			qualifierData.setSwatchImageUrl(element.getVariantValueCategory().getSwatchImageUrl());
			qualifierData.setHexCode(element.getVariantValueCategory().getHexCode());
		}
		
		return qualifierData;
	}

	/**
	 * Populates specification data using the classification data
	 *
	 * @param product
	 *           The product data object.
	 */
	private void populateSpecifications(final ProductData product)
	{
		if (StringUtils.isNotEmpty(product.getAssetCode()))
		{
			product.setSpecifications(searchCompareProductsFacade.getSpecifications(product));
		}
	}

	private Collection<ImageData> restructureImageData(final Collection<ImageData> images , final String kaonUrl)
	{
		//Group Images by image type.

		//Image Type : Product
		final List<ImageData> groupedPrimaryImages = images.stream().filter(img -> ImageDataType.PRIMARY.equals(img.getImageType()))
				.collect(Collectors.toList());
		//Image Type: Gallery. Image object with same gallery index are grouped in a list and mapped to the index.

		final Map<Integer, List<ImageData>> groupedGalleryImages = images.stream()
				.filter(img -> ImageDataType.GALLERY.equals(img.getImageType()))
				.collect(Collectors.groupingBy(ImageData::getGalleryIndex));
		
		
		//New Image List
		final List<ImageData> newImages = new ArrayList<>();

		//Consolidating all grouped Product Image type objects in one object and adding them to the list.
		newImages.add(consolidateImages(groupedPrimaryImages,kaonUrl));

		//Consolidating all Gallery Image type objects in one object per one gallery index
		groupedGalleryImages.keySet().stream().forEach(indx -> newImages.add(consolidateImages(groupedGalleryImages.get(indx),kaonUrl)));

		//Removing those gallery images which are also present as primary image.
		final List<String> primaryImageUrls = newImages.stream().filter(img->ImageDataType.PRIMARY.equals(img.getImageType())).map(img->img.getThumbnailUrl()).collect(Collectors.toList());
		newImages.removeIf(img->ImageDataType.GALLERY.equals(img.getImageType()) && primaryImageUrls.contains(img.getThumbnailUrl()));

		//Sorting Images on the basis of Name. If the images end with _# then they are sorted on the basis of the number after the underscore.
		newImages.sort((final ImageData image1, final ImageData image2) -> {
			final String img1 = image1.getName();
			final String img2 = image2.getName();
			if (null == img1 && null == img2)
			{
				return 0;
			}
			if (null == img1 || null == img2)
			{
				return (null == img1) ? 1 : -1;
			}
			//3D images should be displayed at the last 
			if(img1.contains(IMG_3D_SUFFIX) || img2.contains(IMG_3D_SUFFIX)) {
				LOG.debug( " img1/2 has 3d" + img1  + " " +img2);
				return (img1.contains(IMG_3D_SUFFIX)) ? 1 : -1;
			}
			if (img1.contains(DELIMITER) && img2.contains(DELIMITER))
			{
				int result = 0;
				try
				{
					result = Integer.valueOf(img1.substring(img1.lastIndexOf(DELIMITER_CHAR) + 1))
							- Integer.valueOf(img2.substring(img2.lastIndexOf(DELIMITER_CHAR) + 1));
				}
				catch (final NumberFormatException e)
				{
					LOG.error("Number format exception in sorting the images " + img1 + " and " + img2, e);
					return img1.compareTo(img2);
				}
				return result;
			}
			else if (img1.contains(DELIMITER) || img2.contains(DELIMITER))
			{
				return img1.contains(DELIMITER) ? -1 : 1;
			}

			return img1.compareTo(img2);
		});

		//Adding a primary image at top.
		newImages.stream().filter(img -> ImageDataType.PRIMARY.equals(img.getImageType())).findFirst()
				.ifPresent(img -> {
					newImages.remove(img);
					newImages.add(0,   img);
				});

		return newImages;
	}

	private ImageData consolidateImages(final List<ImageData> images, final String kaonUrl)
	{
		final ImageData consolidatedImageData = new ImageData();
		if (CollectionUtils.isNotEmpty(images))
		{
			final ImageData firstImage = images.get(0);
			consolidatedImageData.setAltText(firstImage.getAltText());
			consolidatedImageData.setGalleryIndex(firstImage.getGalleryIndex());
			consolidatedImageData.setImageType(firstImage.getImageType());
			consolidatedImageData.setWidth(firstImage.getWidth());
			consolidatedImageData.setMimeType(firstImage.getMimeType());
			consolidatedImageData.setName(firstImage.getName());
			//Looping through the images and populating the urls depending the on the format type.
			images.stream().forEach((entry) -> {

				if (GpcommerceFacadesConstants.FORMAT_TYPE_ZOOM.equals(entry.getFormat()))
				{
					consolidatedImageData.setZoomUrl(entry.getUrl());
				}
				else if (GpcommerceFacadesConstants.FORMAT_TYPE_PRODUCT.equals(entry.getFormat()))
				{
					consolidatedImageData.setUrl(entry.getFormat());
				}
				else if (GpcommerceFacadesConstants.FORMAT_TYPE_THUMBNAIL.equals(entry.getFormat()))
				{
					consolidatedImageData.setThumbnailUrl(entry.getUrl());
				}
				else if (GpcommerceFacadesConstants.FORMAT_TYPE_CART_ICON.equals(entry.getFormat()))
				{
					consolidatedImageData.setCartIconUrl(entry.getUrl());
				}
				if(entry.getName().toUpperCase().contains(IMG_3D_SUFFIX)){
					consolidatedImageData.setKaonUrl(kaonUrl);
					LOG.debug(" setting kaon url  " + entry.getName()+ " "+ consolidatedImageData.getKaonUrl());

				}

			});

		}
		return consolidatedImageData;
	}

	/**
	 * Sets the favorite flag for product.
	 *
	 * @param product
	 */
	private void setFavoriteFlagForProduct(final ProductData product)
	{
		final String isFavoritesConfigured = cmsSiteService.getSiteConfig("isFavoritesEnabled");
		if (Boolean.TRUE.equals(Boolean.valueOf(isFavoritesConfigured)))
		{
			final WishlistData wishlistData = gpDefaultWishlistFacade.getFavorites();
				if (null != wishlistData.getWishlistEntries() && wishlistData.getWishlistEntries().stream()
						.anyMatch(wishData -> wishData.getProduct().getCode().equalsIgnoreCase(product.getCode())))
				{
					product.setIsFavorite(true);
				}

     }
	}
	
	/**
	 * Sets the favorite flag for all the products in the list.
	 *
	 * @param products List of products for which the flag is to be set.
	 */
	public void setFavoriteFlagForProducts(final List<ProductData> products)
	{
		final String isFavoritesConfigured = cmsSiteService.getSiteConfig("isFavoritesEnabled");
		if (Boolean.TRUE.equals(Boolean.valueOf(isFavoritesConfigured)))
		{
			final WishlistData wishlistData = gpDefaultWishlistFacade.getFavorites();
			products.stream().forEach(product->{
				if (null != wishlistData.getWishlistEntries() && wishlistData.getWishlistEntries().stream()
						.anyMatch(wishData -> wishData.getProduct().getCode().equalsIgnoreCase(product.getCode())))
				{
					product.setIsFavorite(true);
				}
			});
     }
	}
	/**
	 * populate knowledge center data to product
	 *
	 * @param product
	 */
	@Override
	public void populateKnwoledgeCenterData(final ProductData product) {
		final String defaultResourceURL = cmsSiteService.getSiteConfig("defaultResourceURL");
		try {
			final DataSheetsData dataSheetdata = product.getDataSheets() != null ? product.getDataSheets() : new DataSheetsData();
			final ProductResourcesVideosData productResourceVideosData = product.getProductResourceVideos() != null
					? product.getProductResourceVideos()
					: new ProductResourcesVideosData();
			if (null != product.getCode()) {
				
				final GPKnowledgeCenterSkuResponse kcResponse = gpKnowledgeCenterService.getSKUDataFromKC(product.getCode());
				if (null != kcResponse) {
					if (null != kcResponse.getInfoResources()) {
						final List<InfoResourcesDTO> infoResourceList = kcResponse.getInfoResources();

						final List<ImageData> imageDataList = infoResourceList.stream().map(infoResource -> {
							final ImageData imageData = new ImageData();
							imageData.setAltText(infoResource.getResourceTitle());
							imageData.setUrl(infoResource.getResourceImageURL());
							imageData.setMimeType(infoResource.getMediaType());
							imageData.setDescription(infoResource.getResourceText());
							try {
								imageData.setStartDate(
										new SimpleDateFormat(DATE_FORMAT).parse(infoResource.getStartDate()));
							} catch (final java.text.ParseException e) {
								LOG.error(e.getMessage());
							}
							imageData.setResourceURL(infoResource.getResourceURL());
							if(StringUtils.isNotEmpty(defaultResourceURL)) {
								LOG.info("defaultResourceURL from siteconfig:"+defaultResourceURL);
								imageData.setDefaultResourceURL(defaultResourceURL);
							}
							imageData.setResourceId(infoResource.getResourceId());
							imageData.setTrainingId(infoResource.getTrainingId());
							imageData.setTrainingGroupId(infoResource.getTrainingGroupId());
							imageData.setTags(infoResource.getTags());
							imageData.setTrainingBrands(infoResource.getTrainingBrands());
							return imageData;
						}).collect(Collectors.toList());
						if (null != dataSheetdata.getDataSheets())
						{
							imageDataList.addAll(dataSheetdata.getDataSheets());
						}
						dataSheetdata.setDataSheets(imageDataList);
						product.setDataSheets(dataSheetdata);

					}

					if (null != kcResponse.getSupportResources()) {
						final List<SupportResourcesDTO> supportResourceList = kcResponse.getSupportResources();

						final List<ImageData> imageDataList = supportResourceList.stream().map(supportResource -> {
							final ImageData imageData = new ImageData();
							imageData.setAltText(supportResource.getResourceTitle());
							imageData.setUrl(supportResource.getResourceImageURL());
							imageData.setMimeType(supportResource.getMediaType());
							imageData.setDescription(supportResource.getResourceText());
							try {
								imageData.setStartDate(
										new SimpleDateFormat(DATE_FORMAT).parse(supportResource.getStartDate()));
							} catch (final java.text.ParseException e) {
								LOG.error(e.getMessage());
							}
							imageData.setResourceURL(supportResource.getResourceURL());
							if(StringUtils.isNotEmpty(defaultResourceURL)) {
								LOG.info("defaultResourceURL from siteconfig:"+defaultResourceURL);
								imageData.setDefaultResourceURL(defaultResourceURL);
							}
							imageData.setResourceId(supportResource.getResourceId());
							imageData.setTrainingId(supportResource.getTrainingId());
							imageData.setTrainingGroupId(supportResource.getTrainingGroupId());
							imageData.setTags(supportResource.getTags());
							imageData.setTrainingBrands(supportResource.getTrainingBrands());
							return imageData;
						}).collect(Collectors.toList());
						if (null != productResourceVideosData.getVideo())
						{
							imageDataList.addAll(productResourceVideosData.getVideo());
						}
						productResourceVideosData.setVideo(imageDataList);
						product.setProductResourceVideos(productResourceVideosData);
					}

				}
			}
		} catch (final Exception e) {
			LOG.error("Error occured while mapping Knowledge center response to ProductData",e);
		}
	}

	@Override
	public GPProductDataList getGPXpressAlertProductList(final String timeStamp, int pageNumber, boolean resend)
	{
		return gpProductService.getProductListForGPXpressAlert(timeStamp, pageNumber, resend);
	}

	@Override
	public File exportImages(ProductData productData, String imageFormat,
			String resolution, String productCode, boolean allimages) throws IOException {

		final String tempPath = Config.getParameter(GpcommerceFacadesConstants.PRODUCT_EMAIL);
		final String temp = tempPath.substring(0, tempPath.length() - 7);
		if(productCode.contains("/")) {
			productCode = productCode.replace("/", "-");
		}
		String source = temp + GpcommerceFacadesConstants.IMAGE_FILE_PATH + productCode;
		String zipFile = temp + GpcommerceFacadesConstants.PRODUCT_FORMAT_IMAGE_FILE_PATH + productCode
				+ GpcommerceFacadesConstants.ZIP_FILE_EXTENSION;

		return gpProductService.exportImages(productData, imageFormat, resolution, source, zipFile, allimages);
	}
}

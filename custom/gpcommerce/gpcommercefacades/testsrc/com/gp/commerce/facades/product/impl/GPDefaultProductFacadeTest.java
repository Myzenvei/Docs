/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.product.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.exceptions.GPCommerceDataException;
import com.gp.commerce.core.product.services.impl.GPDefaultProductService;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gp.commerce.facades.data.WishlistData;
import com.gp.commerce.facades.data.WishlistEntryData;
import com.gp.commerce.facades.product.GPProductFacade;
import com.gp.commerce.facades.product.data.DataSheetsData;
import com.gp.commerce.facades.product.data.ProductResourcesVideosData;
import com.gp.commerce.facades.search.compare.GPSearchCompareProductsFacade;
import com.gp.commerce.facades.search.compare.data.CompareSpecificationsData;
import com.gp.commerce.facades.wishlist.impl.GPDefaultWishlistFacade;
import com.gpintegration.knowledgecenter.dto.GPKnowledgeCenterSkuResponse;
import com.gpintegration.knowledgecenter.dto.InfoResourcesDTO;
import com.gpintegration.knowledgecenter.dto.SupportResourcesDTO;
import com.gpintegration.service.GPKnowledgeCenterService;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.BaseOptionData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.commercefacades.product.data.VariantOptionQualifierData;
import de.hybris.platform.commercefacades.product.impl.DefaultProductFacade;
import de.hybris.platform.converters.ConfigurablePopulator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.util.Config;
import static org.mockito.BDDMockito.given;
import org.powermock.modules.junit4.PowerMockRunner;

@UnitTest
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Config.class })
public class GPDefaultProductFacadeTest{

	@InjectMocks
	GPDefaultProductFacade gpDefaultProductFacade = new GPDefaultProductFacade();

	@Mock
	GPDefaultProductService productService;

	@Mock
	private GPCMSSiteService cmsSiteService;

	@Mock
	GPDefaultWishlistFacade gpDefaultWishlistFacade;

	@Mock
	private GPSearchCompareProductsFacade searchCompareProductsFacade;
	
	@Mock
	GPKnowledgeCenterService gpKnowledgeCenterService;

	@Mock
	private Converter<ProductModel, ProductData> productConverter;
	
	@Mock
	private ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator;
	
	ProductData productData = new ProductData();
	List<Integer> trainingGroupId = new ArrayList<>();
	List<String> tags = new ArrayList<>();
	List<String> trainingBrands = new ArrayList<>();
	GPKnowledgeCenterSkuResponse response = new GPKnowledgeCenterSkuResponse();

	public GPDefaultProductFacadeTest() {
		MockitoAnnotations.initMocks(this);
		gpDefaultProductFacade.setProductConverter(productConverter);
		gpDefaultProductFacade.setProductConfiguredPopulator(productConfiguredPopulator);
	}

	@Before
	public void setUpProductImageData() {
		List<ImageData> images = new ArrayList<>();
		List<ImageData> video = new ArrayList<>();
		ImageData datas = new ImageData();
		DataSheetsData dataSheets = new DataSheetsData();
		WishlistData wld = new WishlistData();
		productData.setCode("testCode");
		List<WishlistEntryData> wishlistEntries = new ArrayList<>();
		WishlistEntryData wl = new WishlistEntryData();
		wl.setProduct(productData);
		wishlistEntries.add(wl);
		wld.setWishlistEntries(wishlistEntries);
		productData.setDataSheets(dataSheets);
		video.add(datas);
		dataSheets.setDataSheets(video);
		ProductResourcesVideosData productResourceVideos = new ProductResourcesVideosData();
		productData.setProductResourceVideos(productResourceVideos);
		productResourceVideos.setVideo(video);
		ImageData image1 = new ImageData();
		ImageData image2 = new ImageData();
		ImageData image3 = new ImageData();
		ImageData image4 = new ImageData();
		ImageData image5 = new ImageData();
		ImageDataType imageType1 = ImageDataType.GALLERY;
		ImageDataType imageType2 = ImageDataType.PRIMARY;
		ImageDataType imageType3 = ImageDataType.PRIMARY;
		ImageDataType imageType4 = ImageDataType.PRIMARY;
		ImageDataType imageType5 = ImageDataType.PRIMARY;
		image1.setImageType(imageType1);
		image1.setName("image_1");
		image1.setGalleryIndex(1);
		image1.setThumbnailUrl("thumbnailUrl");
		image2.setImageType(imageType2);
		image2.setName("image_2");
		image2.setGalleryIndex(1);
		image2.setFormat(GpcommerceFacadesConstants.FORMAT_TYPE_ZOOM);
		image3.setImageType(imageType3);
		image3.setName("image_3");
		image3.setGalleryIndex(2);
		image3.setFormat(GpcommerceFacadesConstants.FORMAT_TYPE_PRODUCT);
		image4.setImageType(imageType4);
		image4.setName("image_4");
		image4.setGalleryIndex(3);
		image4.setFormat(GpcommerceFacadesConstants.FORMAT_TYPE_THUMBNAIL);
		image5.setImageType(imageType5);
		image5.setName("image_5");
		image5.setGalleryIndex(4);
		image5.setFormat(GpcommerceFacadesConstants.FORMAT_TYPE_CART_ICON);
		images.add(image1);
		images.add(image2);
		images.add(image3);
		images.add(image4);
		images.add(image5);
		productData.setImages(images);
		Mockito.when(gpDefaultWishlistFacade.getFavorites()).thenReturn(wld);
	}

	@Before
	public void setUpBaseOptionsData() {
		final List<BaseOptionData> baseOptions = new ArrayList<BaseOptionData>();
		VariantOptionData data = new VariantOptionData();
		List<VariantOptionData> listData = new ArrayList<>();
		List<VariantOptionQualifierData> vlist = new ArrayList<>();
		List<VariantOptionQualifierData> vlist1 = new ArrayList<>();
		VariantOptionQualifierData vodata = new VariantOptionQualifierData();
		VariantOptionQualifierData vodata1 = new VariantOptionQualifierData();
		BaseOptionData bd = new BaseOptionData();
		VariantOptionData vdata = new VariantOptionData();
		VariantOptionData vdata1 = new VariantOptionData();
		vodata.setValue("dummy");
		vlist.add(vodata);
		vodata1.setValue("dummy1");
		vlist1.add(vodata1);
		vdata.setCode("testCode");
		vdata.setVariantOptionQualifiers(vlist);
		listData.add(vdata);
		vdata1.setCode("testCode1");
		vdata1.setVariantOptionQualifiers(vlist1);
		listData.add(vdata1);
		data.setCode("testCode");
		bd.setVariantType("StyleVariantProduct");
		bd.setIsStyleVariant(true);
		bd.setSelected(data);
		bd.setOptions(listData);
		baseOptions.add(bd);
		productData.setBaseOptions(baseOptions);
		String assetCode = "assetCode";
		productData.setAssetCode(assetCode);
	}

	@Before
	public void setUpResourceData() {

		SupportResourcesDTO resource = new SupportResourcesDTO();
		List<SupportResourcesDTO> supportResources = new ArrayList<>();
		List<InfoResourcesDTO> infoResources = new ArrayList<>();
		InfoResourcesDTO info = new InfoResourcesDTO();
		trainingGroupId.add(2);
		trainingBrands.add("brand");
		tags.add("test");
		resource.setResourceText("resourceText");
		resource.setResourceImageURL("resourceImageURL");
		resource.setMediaType("mediaType");
		resource.setStartDate("2018-09-29T18:46:19");
		resource.setResourceURL("resourceURL");
		resource.setResourceId(5);
		resource.setTrainingId(5);
		resource.setTrainingGroupId(trainingGroupId);
		resource.setTags(tags);
		resource.setTrainingBrands(trainingBrands);
		supportResources.add(resource);
		response.setSupportResources(supportResources);
		info.setResourceText("text");
		info.setResourceImageURL("resourceImageURL");
		info.setMediaType("mediaType");
		info.setStartDate("2018-09-29T18:46:19");
		info.setResourceURL("resourceURL");
		info.setResourceId(4);
		info.setTrainingId(4);
		info.setTrainingGroupId(trainingGroupId);
		info.setTags(tags);
		info.setTrainingBrands(trainingBrands);
		infoResources.add(info);
		response.setInfoResources(infoResources);
		Mockito.when(gpKnowledgeCenterService.getSKUDataFromKC(Mockito.anyString())).thenReturn(response);

	}

	@Test
	public void populateAdditionalFieldstest() {

		final Map<String, String> colorCodes = new HashMap<String, String>();
		colorCodes.put("test", "test");
		colorCodes.put("dummy1", "dummy1");
		Mockito.when(productService.fetchAllColorCodes()).thenReturn(colorCodes);
		Mockito.when(cmsSiteService.getSiteConfig("isFavoritesEnabled")).thenReturn("true");
		String propertyValue = "dummy";
		PowerMockito.mockStatic(Config.class);
		Mockito.when(Config.getParameter(GpcommerceCoreConstants.DUMMY_STYLE_VARIANT_COLOR)).thenReturn(propertyValue);
		List<CompareSpecificationsData> value = new ArrayList<CompareSpecificationsData>();
		Mockito.when(searchCompareProductsFacade.getSpecifications(productData)).thenReturn(value);
		gpDefaultProductFacade.populateAdditionalFields(productData);
		Assert.assertEquals(Boolean.TRUE, productData.getIsFavorite());
		Assert.assertNotEquals(colorCodes, "dummy");
		Assert.assertEquals(2, colorCodes.size());

	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = GPCommerceDataException.class)
	public void getProductForCodeAndOptionsTest() {
		final String code = "code";
		String uid = "uid";
		final Collection<ProductOption> options = null;
		final ProductModel productModel = Mockito.mock(ProductModel.class);
		productModel.setCode(code);
		Mockito.when(productModel.getIsAvailableForSite().booleanValue()).thenReturn(Boolean.TRUE);
		Mockito.when(productService.getProductForCode(code)).thenReturn(productModel);
		Mockito.when(productConverter.convert(productModel)).thenReturn(productData);
		Mockito.when(gpDefaultProductFacade.getProductForCodeAndOptions(code, options)).thenReturn(productData);
		ProductData prodData = gpDefaultProductFacade.getProductForCodeAndOptions(code, options);
		Assert.assertEquals("testCode", prodData.getCode());
		Mockito.when(productModel.getIsAvailableForSite().booleanValue()).thenReturn(Boolean.FALSE);
		CMSSiteModel site = Mockito.mock(CMSSiteModel.class);
		Mockito.when(cmsSiteService.getCurrentSite()).thenReturn(site);
		Mockito.when(site.getUid()).thenReturn(uid);
		Mockito.when(gpDefaultProductFacade.getProductForCodeAndOptions(code, options)).thenThrow(GPCommerceDataException.class);
	}
	
	@Test
	public void setFavoriteFlagForProductsTest() {
		Mockito.when(cmsSiteService.getSiteConfig("isFavoritesEnabled")).thenReturn("true");
		List<ProductData> products= Arrays.asList(productData);
		WishlistData wld = new WishlistData();
		productData.setCode("testCode");
		List<WishlistEntryData> wishlistEntries = new ArrayList<>();
		WishlistEntryData wl = new WishlistEntryData();
		wl.setProduct(productData);
		wishlistEntries.add(wl);
		wld.setWishlistEntries(wishlistEntries);
		Mockito.when(gpDefaultWishlistFacade.getFavorites()).thenReturn(wld);
		Assert.assertEquals(productData.getCode(), wl.getProduct().getCode());
		gpDefaultProductFacade.setFavoriteFlagForProducts(products);
		Assert.assertEquals(productData.getCode(), wl.getProduct().getCode());
		Assert.assertEquals(1, gpDefaultWishlistFacade.getFavorites().getWishlistEntries().size());
	}

}

/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.search.compare.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ClassificationData;
import de.hybris.platform.commercefacades.product.data.FeatureData;
import de.hybris.platform.commercefacades.product.data.FeatureUnitData;
import de.hybris.platform.commercefacades.product.data.FeatureValueData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.exceptions.GPCommerceDataException;
import com.gp.commerce.core.product.services.impl.GPDefaultProductService;
import com.gp.commerce.facades.search.compare.data.ComparisonData;


/**
 * @author prmaram
 *
 */
@UnitTest
public class GPDefaultSearchCompareProductsFacadeTest
{

	@Mock
	private ProductFacade productFacade;
	
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private Configuration config;
	
	@Mock
	private GPDefaultProductService gpDefaultProductService;
	
	@Mock 
	CMSSiteService cmssite;
	
	@Mock
	CMSSiteModel cmsSiteModel;

	
	@InjectMocks
	private GPDefaultSearchCompareProductsFacade defaultSearchCompareProductsFacade;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		defaultSearchCompareProductsFacade = new GPDefaultSearchCompareProductsFacade();
		defaultSearchCompareProductsFacade.setConfigurationService(configurationService);
		defaultSearchCompareProductsFacade.setProductFacade(productFacade);
		defaultSearchCompareProductsFacade.setGpProductService(gpDefaultProductService);
		gpDefaultProductService.setCmsSiteService(cmssite);
		BDDMockito.given(cmssite.getCurrentSite()).willReturn(cmsSiteModel);
		BDDMockito.given(cmsSiteModel.getUid()).willReturn("vanityfair");

	}

	
	@Test
	public void testCompare() throws Exception
	{
		final ProductData compareProd1 = getProduct(true);
		final ProductData compareProd2 = getProduct();
		final ProductData compareProd3 = getProduct2(true);
		final ProductData compareProd4 = getProduct();
		ComparisonData comparison1 = new ComparisonData();
		ProductData cpd = new ProductData();
		cpd.setCode("553637");
		List<ProductData> cpds = new ArrayList<ProductData>();
		cpds.add(cpd);
		comparison1.setProducts(cpds);
		final List<ProductOption> options = new ArrayList<>(Arrays.asList(ProductOption.BASIC, ProductOption.URL,
				ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY,
				ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.CLASSIFICATION, ProductOption.STOCK));
		final List<String> productCodes = new ArrayList<String>();
		final List<String> masterList = new ArrayList<String>();
		final List<ProductData> productsList = new ArrayList<ProductData>();
		final String productCode1 = "553637";
		final String productCode2 = "553638";
		final String productCode3 = "553639";
		final String productCode4 = "553636";
		productCodes.add(productCode1);
		productCodes.add(productCode2);
		productCodes.add(productCode3);
		productCodes.add(productCode4);
		final String query = productCode1 + ":" + productCode2 + ":" + productCode3 + ":" + productCode4;
		Mockito.when(productFacade.getProductForCodeAndOptions(Mockito.eq(productCode1), Mockito.anyList())).thenReturn(compareProd1);
		Mockito.when(productFacade.getProductForCodeAndOptions(Mockito.eq(productCode2), Mockito.anyList())).thenReturn(compareProd2);
		Mockito.when(productFacade.getProductForCodeAndOptions(Mockito.eq(productCode3), Mockito.anyList())).thenReturn(compareProd3);
		Mockito.when(productFacade.getProductForCodeAndOptions(Mockito.eq(productCode4), Mockito.anyList())).thenReturn(compareProd4);
		Mockito.when(configurationService.getConfiguration()).thenReturn(config);
		Mockito.when(config.getString("search.compare.maxProducts")).thenReturn("4");
		productsList.add(compareProd2);
		productsList.add(compareProd1);
		productsList.add(compareProd3);
		productsList.add(compareProd4);
		assertEquals(compareProd1.getAssetCode(),compareProd3.getAssetCode());
		masterList.add("memory slots, 23");
		defaultSearchCompareProductsFacade.getProductFacade();
		defaultSearchCompareProductsFacade.getConfigurationService();
		defaultSearchCompareProductsFacade.getGpProductService();
		defaultSearchCompareProductsFacade.getSpecifications(cpd);
		given(gpDefaultProductService.getAttributesOfProduct("camera","SEARCH")).willReturn(masterList);
		ComparisonData comparison = defaultSearchCompareProductsFacade.compare(query, "SEARCH");
		assertEquals(((ProductData)comparison1.getProducts().get(0)).getCode(), ((ProductData)comparison.getProducts().get(0)).getCode());
	}
	
	
	@Test
	public void testCompareDiffProducts() throws Exception
	{
		final ProductData compareProd1 = getProduct5(true);
		final ProductData compareProd2 = getProduct3(false);
		final ProductData compareProd3 = getProduct2(true);
		final ProductData compareProd4 = getProduct4(false);
		
		ComparisonData comparison1 = new ComparisonData();
		ProductData cpd = new ProductData();
		cpd.setCode("553637");
		List<ProductData> cpds = new ArrayList<ProductData>();
		cpds.add(cpd);
		comparison1.setProducts(cpds);
		final List<ProductOption> options = new ArrayList<>(Arrays.asList(ProductOption.BASIC, ProductOption.URL,
				ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY,
				ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.CLASSIFICATION, ProductOption.STOCK));
		final List<String> productCodes = new ArrayList<String>();
		final List<ProductData> productsList = new ArrayList<ProductData>();
		final String productCode1 = "553637";
		final String productCode2 = "553638";
		final String productCode3 = "553639";
		final String productCode4 = "553636";
		productCodes.add(productCode1);
		productCodes.add(productCode2);
		productCodes.add(productCode3);
		productCodes.add(productCode4);
		final String query = productCode1 + ":" + productCode2 + ":" + productCode3 + ":" + productCode4;
		Mockito.when(productFacade.getProductForCodeAndOptions(Mockito.eq(productCode1), Mockito.anyList())).thenReturn(compareProd1);
		Mockito.when(productFacade.getProductForCodeAndOptions(Mockito.eq(productCode2), Mockito.anyList())).thenReturn(compareProd2);
		Mockito.when(productFacade.getProductForCodeAndOptions(Mockito.eq(productCode3), Mockito.anyList())).thenReturn(compareProd3);
		Mockito.when(productFacade.getProductForCodeAndOptions(Mockito.eq(productCode4), Mockito.anyList())).thenReturn(compareProd4);
		given(configurationService.getConfiguration()).willReturn(config);
		given(config.getString("search.compare.maxProducts")).willReturn("4");
		productsList.add(compareProd2);
		productsList.add(compareProd1);
		productsList.add(compareProd3);
		productsList.add(compareProd4);
		ComparisonData comparison = defaultSearchCompareProductsFacade.compare(query, "SEARCH");
		assertEquals(((ProductData)comparison1.getProducts().get(0)).getCode(), ((ProductData)comparison.getProducts().get(0)).getCode());
	}
	
	@Test
	public void testCompareProductsNull() throws Exception
	{
		final List<String> productCodes = new ArrayList<String>();
		final String productCode1 = "553637";
		productCodes.add(productCode1);
		productCodes.add("  ");
		final String query = productCode1 + ":" + " ";
		
		given(configurationService.getConfiguration()).willReturn(config);
		given(config.getString("search.compare.maxProducts")).willReturn("4");
		try
		{
			defaultSearchCompareProductsFacade.compare(query, "SEARCH");
		}
		catch(Exception e)
		{
			assertEquals(e.getMessage(), "The product codes are either incorrect or missing");
		}
		
	}
	
	@Test
	public void testCompareProductsExceedingMaxLimit() throws Exception
	{
		final List<String> productCodes = new ArrayList<String>();
		final String productCode1 = "553637";
		final String productCode2 = "553638";
		final String productCode3 = "553639";
		final String productCode4 = "553636";
		final String productCode5 = "553635";
		productCodes.add(productCode1);
		productCodes.add(productCode2);
		productCodes.add(productCode3);
		productCodes.add(productCode4);
		productCodes.add(productCode5);
		final String query = productCode1 + ":" + productCode2 + ":" + productCode3 + ":" + productCode4 + ":" + productCode5;
		given(configurationService.getConfiguration()).willReturn(config);
		given(config.getString("search.compare.maxProducts")).willReturn("4");
		try
		{
			defaultSearchCompareProductsFacade.compare(query, "SEARCH");
		}
		catch(Exception e)
		{
			assertEquals(e.getMessage(), "Invalid request: exceeded max number of products allowed for comparison");
		}
		
	}

	
	@Test(expected=GPCommerceDataException.class)
	public void testCompareProductAssetCodesNotMatching() throws Exception
	{
		final ProductData compareProd1 = getProduct5(true);
		final ProductData compareProd2 = getProduct3(false);
		final ProductData compareProd3 = getProduct2(true);
		final ProductData compareProd6 = getProduct6(false);
		
		ComparisonData comparison1 = new ComparisonData();
		ProductData cpd = new ProductData();
		cpd.setCode("553637");
		List<ProductData> cpds = new ArrayList<ProductData>();
		cpds.add(cpd);
		comparison1.setProducts(cpds);
		final List<ProductOption> options = new ArrayList<>(Arrays.asList(ProductOption.BASIC, ProductOption.URL,
				ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY,
				ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.CLASSIFICATION, ProductOption.STOCK));
		final List<String> productCodes = new ArrayList<String>();
		final List<ProductData> productsList = new ArrayList<ProductData>();
		final String productCode1 = "553637";
		final String productCode2 = "553638";
		final String productCode3 = "553639";
		final String productCode6 = "553636";
		productCodes.add(productCode1);
		productCodes.add(productCode2);
		productCodes.add(productCode3);
		productCodes.add(productCode6);
		final String query = productCode1 + ":" + productCode2 + ":" + productCode3 + ":" + productCode6;
		Mockito.when(productFacade.getProductForCodeAndOptions(Mockito.eq(productCode1), Mockito.anyList())).thenReturn(compareProd1);
		Mockito.when(productFacade.getProductForCodeAndOptions(Mockito.eq(productCode2), Mockito.anyList())).thenReturn(compareProd2);
		Mockito.when(productFacade.getProductForCodeAndOptions(Mockito.eq(productCode3), Mockito.anyList())).thenReturn(compareProd3);
		Mockito.when(productFacade.getProductForCodeAndOptions(Mockito.eq(productCode6), Mockito.anyList())).thenReturn(compareProd6);
		given(configurationService.getConfiguration()).willReturn(config);
		given(config.getString("search.compare.maxProducts")).willReturn("4");
		productsList.add(compareProd2);
		productsList.add(compareProd1);
		productsList.add(compareProd3);
		productsList.add(compareProd6);
		defaultSearchCompareProductsFacade.compare(query, "SEARCH");
		
	}

	
	public ProductData getProduct() {
		return getProduct(false);
	}
	
	public ProductData getProduct(boolean flag) {
		ProductData productdata = new ProductData();
		StockData sd = new StockData();
		PriceData pd = new PriceData();
		productdata.setCode("553637");
		productdata.setStock(sd);
		productdata.setPrice(pd);
		productdata.setAssetCode("camera");
		
		ClassificationData cd=new ClassificationData();
		cd.setName("Memory");
		List<ClassificationData> cds = new ArrayList<ClassificationData>();
		cds.add(cd);
		productdata.setClassifications(cds);
		
		FeatureData fd= new FeatureData();
		fd.setName("Internal Memory");
		fd.setCode("Internal Memory, 21");
		List<FeatureData> fds = new ArrayList<FeatureData>();
		fds.add(fd);
		cd.setFeatures(fds);
		fd.setRange(flag);
		
		
		FeatureValueData fvd = new FeatureValueData();
		fvd.setValue("20");
		FeatureValueData fvd1 = new FeatureValueData();
		fvd.setValue("30");
		List<FeatureValueData> fvds = new ArrayList<FeatureValueData>();
		fvds.add(fvd);
		fvds.add(fvd1);
		fd.setFeatureValues(fvds);
		
		FeatureUnitData fud = new FeatureUnitData();
		fud.setSymbol("MB");
		fd.setFeatureUnit(fud);
		
		return productdata;
	}
	
	public ProductData getProduct2(boolean flag) {
		ProductData productdata = new ProductData();
		StockData sd = new StockData();
		PriceData pd = new PriceData();
		productdata.setCode("553637");
		productdata.setStock(sd);
		productdata.setPrice(pd);
		productdata.setAssetCode("camera");
		
		ClassificationData cd=new ClassificationData();
		cd.setName("Memory");
		List<ClassificationData> cds = new ArrayList<ClassificationData>();
		cds.add(cd);
		productdata.setClassifications(cds);
		
		FeatureData fd= new FeatureData();
		fd.setName("Memory Slots");
		fd.setCode("Memory Slots, 23");
		List<FeatureData> fds = new ArrayList<FeatureData>();
		fds.add(fd);
		cd.setFeatures(fds);
		fd.setRange(flag);
		//test for scenario where in range is true and unit not available
		
		FeatureValueData fvd = new FeatureValueData();
		fvd.setValue("2");
		FeatureValueData fvd1 = new FeatureValueData();
		fvd.setValue("3");
		List<FeatureValueData> fvds = new ArrayList<FeatureValueData>();
		fvds.add(fvd);
		fvds.add(fvd1);
		fd.setFeatureValues(fvds);
		
		FeatureUnitData fud = new FeatureUnitData();
		fud.setSymbol("MB");
		fd.setFeatureUnit(fud);
		
		return productdata;
	}
	
	public ProductData getProduct3(boolean flag) {
		ProductData productdata = new ProductData();
		StockData sd = new StockData();
		PriceData pd = new PriceData();
		productdata.setCode("553637");
		productdata.setStock(sd);
		productdata.setPrice(pd);
		productdata.setAssetCode("camera");
		return productdata;
	}
	
	public ProductData getProduct4(boolean flag) {
		ProductData productdata = new ProductData();
		StockData sd = new StockData();
		PriceData pd = new PriceData();
		productdata.setCode("553637");
		productdata.setStock(sd);
		productdata.setPrice(pd);
		productdata.setAssetCode("camera");
		
		ClassificationData cd=new ClassificationData();
		cd.setName("Memory");
		List<ClassificationData> cds = new ArrayList<ClassificationData>();
		cds.add(cd);
		productdata.setClassifications(cds);
		
		FeatureData fd= new FeatureData();
		fd.setName("Memory Slots");
		fd.setCode("Memory Slots, 23");
		List<FeatureData> fds = new ArrayList<FeatureData>();
		fds.add(fd);
		cd.setFeatures(fds);
		fd.setRange(flag);
		//test for scenario where in range is true and unit not available
		
		return productdata;
	}
	
	public ProductData getProduct5(boolean flag) {
		ProductData productdata = new ProductData();
		StockData sd = new StockData();
		PriceData pd = new PriceData();
		productdata.setCode("553637");
		productdata.setStock(sd);
		productdata.setPrice(pd);
		productdata.setAssetCode("camera");
		
		ClassificationData cd=new ClassificationData();
		cd.setName("Memory");
		List<ClassificationData> cds = new ArrayList<ClassificationData>();
		cds.add(cd);
		productdata.setClassifications(cds);
		
		return productdata;
	}
	
	public ProductData getProduct6(boolean flag) {
		ProductData productdata = new ProductData();
		StockData sd = new StockData();
		PriceData pd = new PriceData();
		productdata.setCode("553637");
		productdata.setStock(sd);
		productdata.setPrice(pd);
		productdata.setAssetCode("lens");
		
		ClassificationData cd=new ClassificationData();
		cd.setName("Memory");
		List<ClassificationData> cds = new ArrayList<ClassificationData>();
		cds.add(cd);
		productdata.setClassifications(cds);
		
		return productdata;
	}
}

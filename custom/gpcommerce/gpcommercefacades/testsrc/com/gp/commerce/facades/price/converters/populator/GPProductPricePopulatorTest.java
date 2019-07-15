/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.price.converters.populator;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;

import com.gp.commerce.core.price.service.impl.GPDefaultEurope1PriceFactory;
import com.gp.commerce.facades.price.converters.populator.GPProductPricePopulator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CurrencyModel;

@UnitTest
public class GPProductPricePopulatorTest {

	@InjectMocks
    private GPProductPricePopulator gpProductPricePopulatorUnderTest;
    
    @Mock
	private PriceDataFactory priceDataFactory;
    
    @Mock
	private GPDefaultEurope1PriceFactory gpPriceService;
    
    @Mock
	private ModelService modelService;
    
    private PriceData priceData;
    private CurrencyModel currencyModel;
    @Mock
    private ProductModel source;
    private ProductData target=new ProductData();;
	private PriceDataType priceType;
	private PriceRowModel prmModel;
	private VariantProductModel variant;
    
    List<PriceRow> productPrices=new ArrayList<>();
    Collection<VariantProductModel> variants=new ArrayList<>();

    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
        gpProductPricePopulatorUnderTest = new GPProductPricePopulator<>();
        gpProductPricePopulatorUnderTest.gpPriceService = Mockito.mock(GPDefaultEurope1PriceFactory.class);
        ReflectionTestUtils.setField(gpProductPricePopulatorUnderTest, "modelService", modelService);
        ReflectionTestUtils.setField(gpProductPricePopulatorUnderTest, "priceDataFactory", priceDataFactory);
        PriceRow productPrice = Mockito.mock(PriceRow.class);
        
		prmModel = new PriceRowModel();
		variant=Mockito.mock(VariantProductModel.class);
		variants.add(variant);
		prmModel.setPrice(new Double(10));
		prmModel.setWeblistPrice(new Double(12));
		priceType=PriceDataType.FROM;
		 priceData = new PriceData();
		 source=mock(ProductModel.class);
		 productPrices.add(productPrice);
		 currencyModel=new CurrencyModel();
		 prmModel.setCurrency(currencyModel);
		 priceData.setValue(BigDecimal.valueOf(12.0d));
		 priceData.setFormattedValue("12.00");
		 priceData.setPriceType(priceType);
		 
		 Mockito.when(gpProductPricePopulatorUnderTest.gpPriceService.getPriceInformationsForProduct(source)).thenReturn(productPrices);
		 when(productPrice.getPK()).thenReturn(PK.BIG_PK);
		 Mockito.when(modelService.get(productPrice.getPK())).thenReturn(prmModel);
		 Mockito.when(priceDataFactory.create(priceType, BigDecimal.valueOf(prmModel.getWeblistPrice().doubleValue()),
					prmModel.getCurrency())).thenReturn(priceData);
		 
    }

    @Test
    public void testPopulate() {
    	priceType=PriceDataType.BUY;
    	Mockito.when(priceDataFactory.create(priceType, BigDecimal.valueOf(prmModel.getWeblistPrice().doubleValue()),
				prmModel.getCurrency())).thenReturn(priceData);
        gpProductPricePopulatorUnderTest.populate(source, target);
        assertEquals(target.getWeblistPrice().getValue(),BigDecimal.valueOf(12.00));
    }
    
    @Test
    public void testBuyPopulate() {
    	Mockito.when(source.getVariants()).thenReturn(variants);
        gpProductPricePopulatorUnderTest.populate(source, target);
        assertEquals(priceType,target.getWeblistPrice().getPriceType());
    }
}

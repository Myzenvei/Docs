package com.gp.commerce.core.price.service.impl;

import com.gp.commerce.core.constants.GeneratedGpcommerceCoreConstants;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * JUnit test suite for {@link GPFilteredPriceContainerImpl}
 */
@UnitTest
public class GPFilteredPriceContainerImplTest {

    private static final String TEST_CURRENCY = "USD";
    private static final Boolean TEST_NET = Boolean.FALSE;

    private GPFilteredPriceContainerImpl gpFilteredPriceContainer;


    @Mock
    private PriceRowModel priceRowModel1;

    @Mock
    private PriceRowModel priceRowModel2;

    @Mock
    private PriceRowModel priceRowModel3;

    @Mock
    private PriceRowModel listPrice;
    @Mock
    private PriceRowModel salePrice;
    @Mock
    private PriceRowModel listPrice2;
    @Mock
    private PriceRowModel salePrice2;
    @Mock
    private PriceRowModel mapPrice;
    @Mock
    private PriceRowModel mapPrice2;

    @Mock
    private Date startDate1;

    @Mock
    private Date endDate1;

    @Mock
    private Date startDate2;

    @Mock
    private Date endDate2;

    @Mock
    private CurrencyModel currencyModel;


    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        given(salePrice.getSalePrice()).willReturn(Boolean.TRUE);
        given(salePrice.getMinAdvertisedPrice()).willReturn(Boolean.FALSE);
        given(salePrice.getCurrency()).willReturn(currencyModel);

        given(mapPrice.getMinAdvertisedPrice()).willReturn(Boolean.TRUE);
        given(mapPrice.getSalePrice()).willReturn(Boolean.FALSE);

        given(listPrice.getMinAdvertisedPrice()).willReturn(Boolean.FALSE);
        given(listPrice.getSalePrice()).willReturn(Boolean.FALSE);
        given(listPrice.getCurrency()).willReturn(currencyModel);
    }
    
    @Test
    public void testAddPriceRow() {

        gpFilteredPriceContainer = new GPFilteredPriceContainerImpl();

        gpFilteredPriceContainer.addPriceRow(listPrice);
        gpFilteredPriceContainer.addPriceRow(listPrice);
        gpFilteredPriceContainer.addPriceRow(salePrice);
        gpFilteredPriceContainer.addPriceRow(salePrice);
        gpFilteredPriceContainer.addPriceRow(mapPrice);
        gpFilteredPriceContainer.addPriceRow(mapPrice);
        gpFilteredPriceContainer.addPriceRow(null);
        Assert.assertEquals(2, gpFilteredPriceContainer.getListPrices().size());
        Assert.assertEquals(2, gpFilteredPriceContainer.getSalePrices().size());
        Assert.assertEquals(2, gpFilteredPriceContainer.getMapPrices().size());
    }

   @Test
    public void testCalculateSlashedPriceDiff() {

       gpFilteredPriceContainer = new GPFilteredPriceContainerImpl();

       //sale < list
       given(salePrice.getPrice()).willReturn(new Double(50));
       given(listPrice.getPrice()).willReturn(new Double(100));
       gpFilteredPriceContainer.addPriceRow(listPrice);
       gpFilteredPriceContainer.addPriceRow(salePrice);
       Assert.assertEquals(50d,gpFilteredPriceContainer.slashedPriceDiff(1),0d);

       //sale == list
       given(salePrice.getPrice()).willReturn(new Double(100));
       given(listPrice.getPrice()).willReturn(new Double(100));
       gpFilteredPriceContainer.addPriceRow(listPrice);
       gpFilteredPriceContainer.addPriceRow(salePrice);
       Assert.assertEquals(0d,gpFilteredPriceContainer.slashedPriceDiff(1),0d);

       //sale > list
       given(salePrice.getPrice()).willReturn(new Double(110));
       given(listPrice.getPrice()).willReturn(new Double(100));
       gpFilteredPriceContainer.addPriceRow(listPrice);
       gpFilteredPriceContainer.addPriceRow(salePrice);
       Assert.assertEquals(0d,gpFilteredPriceContainer.slashedPriceDiff(1),0d);

       //sale < list qty 2
       given(salePrice.getPrice()).willReturn(new Double(50));
       given(listPrice.getPrice()).willReturn(new Double(100));
       gpFilteredPriceContainer.addPriceRow(listPrice);
       gpFilteredPriceContainer.addPriceRow(salePrice);
       Assert.assertEquals(100d,gpFilteredPriceContainer.slashedPriceDiff(2),0d);

    }

    @Test
    public void filterPrices() {
        gpFilteredPriceContainer = new GPFilteredPriceContainerImpl();

        //empty list
        List<PriceRowModel> priceRowModelList = new ArrayList<PriceRowModel>();
        Assert.assertEquals(0,gpFilteredPriceContainer.filterPrices(priceRowModelList).size());

        //2nd price row has shorter date span
        given(startDate1.getTime()).willReturn(946702800000l);//01/01/2001
        given(endDate1.getTime()).willReturn(1009843200000l);//01/01/2002
        given(startDate2.getTime()).willReturn(981003600000l);//02/01/2001
        given(endDate2.getTime()).willReturn(986101200000l);//04/01/2001

        System.out.println("startDate1="+startDate1.getTime()+",endDate1="+endDate1.getTime());
        System.out.println("startDate1="+startDate2.getTime()+",endDate1="+endDate2.getTime());
        priceRowModel1.setStartTime(startDate1);
        priceRowModel1.setEndTime(endDate1);
        priceRowModel2.setStartTime(startDate2);
        priceRowModel2.setEndTime(endDate2);
        priceRowModelList.add(priceRowModel1);
        priceRowModelList.add(priceRowModel2);
        List<PriceRowModel> retPriceRowList = gpFilteredPriceContainer.filterPrices(priceRowModelList);
        Assert.assertEquals(2,retPriceRowList.size());
        Assert.assertEquals(retPriceRowList.get(0),priceRowModel2);

        //start are null so 1st model is 2
        priceRowModelList.clear();
        retPriceRowList.clear();
        priceRowModel1.setStartTime(null);
        priceRowModel1.setEndTime(endDate1);
        priceRowModel2.setStartTime(null);
        priceRowModel2.setEndTime(endDate2);
        priceRowModelList.add(priceRowModel1);
        priceRowModelList.add(priceRowModel2);
        retPriceRowList = gpFilteredPriceContainer.filterPrices(priceRowModelList);
        Assert.assertEquals(2,retPriceRowList.size());
        Assert.assertEquals(retPriceRowList.get(0),priceRowModel2);

        //end dates are null
        priceRowModelList.clear();
        retPriceRowList.clear();
        priceRowModel1.setStartTime(startDate1);
        priceRowModel1.setEndTime(null);
        priceRowModel2.setStartTime(startDate2);
        priceRowModel2.setEndTime(null);
        priceRowModelList.add(priceRowModel1);
        priceRowModelList.add(priceRowModel2);
        retPriceRowList = gpFilteredPriceContainer.filterPrices(priceRowModelList);
        Assert.assertEquals(2,retPriceRowList.size());
        Assert.assertEquals(retPriceRowList.get(0),priceRowModel2);

        //all dates are null, so model1 should be first
        priceRowModelList.clear();
        retPriceRowList.clear();
        priceRowModel1.setStartTime(null);
        priceRowModel1.setEndTime(null);
        priceRowModel2.setStartTime(null);
        priceRowModel2.setEndTime(null);
        priceRowModelList.add(priceRowModel1);
        priceRowModelList.add(priceRowModel2);
        retPriceRowList = gpFilteredPriceContainer.filterPrices(priceRowModelList);
        Assert.assertEquals(2,retPriceRowList.size());
        Assert.assertEquals(retPriceRowList.get(0),priceRowModel1);


    }

    @Test
    public void getCurrency(){

        gpFilteredPriceContainer = new GPFilteredPriceContainerImpl();


        //no currency
        Assert.assertNull("got null",gpFilteredPriceContainer.getCurrency());

        //list currency
        gpFilteredPriceContainer.addPriceRow(listPrice);
        Assert.assertEquals(currencyModel,gpFilteredPriceContainer.getCurrency());

        //list currency
        gpFilteredPriceContainer.getListPrices().clear();
        gpFilteredPriceContainer.addPriceRow(salePrice);
        Assert.assertEquals(currencyModel,gpFilteredPriceContainer.getCurrency());

    }

    /*
    @Test
    public void print() {
    }

   */

    @Test
    public void testGetSinglePrice() {

        gpFilteredPriceContainer = new GPFilteredPriceContainerImpl();


        //empty models
        Assert.assertEquals(-1d,gpFilteredPriceContainer.getSinglePrice(gpFilteredPriceContainer.getMapPrices()),0d);

        //null priceRow
        List<PriceRowModel> priceRowModelList = new ArrayList<>();
        given(mapPrice.getPrice()).willReturn(null);
        priceRowModelList.add(mapPrice);
        Assert.assertEquals(-1d,gpFilteredPriceContainer.getSinglePrice(priceRowModelList),0d);

        //valid 2 prices in list
        given(mapPrice.getPrice()).willReturn(new Double(50));
        given(mapPrice2.getPrice()).willReturn(new Double(75));
        gpFilteredPriceContainer.addPriceRow(mapPrice);
        gpFilteredPriceContainer.addPriceRow(mapPrice2);
        Assert.assertEquals(50d,gpFilteredPriceContainer.getSinglePrice(gpFilteredPriceContainer.getMapPrices()),0d);


    }

@Test
public void testGetSalePrice() {

    gpFilteredPriceContainer = new GPFilteredPriceContainerImpl();

    //empty
    List<PriceRowModel> priceRowModelList = new ArrayList<>();
    Assert.assertEquals(-1d,gpFilteredPriceContainer.getSinglePrice(priceRowModelList),0d);

    given(salePrice.getPrice()).willReturn(new Double(50));
    given(salePrice2.getPrice()).willReturn(new Double(75));
    priceRowModelList.add(salePrice);
    priceRowModelList.add(salePrice2);
    Assert.assertEquals(50d,gpFilteredPriceContainer.getSinglePrice(priceRowModelList),0d);
}


    @Test
    public void testGetListPrice() {

        gpFilteredPriceContainer = new GPFilteredPriceContainerImpl();

        //empty
        List<PriceRowModel> priceRowModelList = new ArrayList<>();
        Assert.assertEquals(-1d,gpFilteredPriceContainer.getSinglePrice(priceRowModelList),0d);

        given(listPrice.getPrice()).willReturn(new Double(50));
        given(listPrice2.getPrice()).willReturn(new Double(75));
        priceRowModelList.add(listPrice);
        priceRowModelList.add(listPrice2);
        Assert.assertEquals(50d,gpFilteredPriceContainer.getSinglePrice(priceRowModelList),0d);
    }


    @Test
    public void testGetMapPrice() {

        gpFilteredPriceContainer = new GPFilteredPriceContainerImpl();

        //empty
        List<PriceRowModel> priceRowModelList = new ArrayList<>();
        Assert.assertEquals(-1d,gpFilteredPriceContainer.getSinglePrice(priceRowModelList),0d);

        given(mapPrice.getPrice()).willReturn(new Double(50));
        given(mapPrice2.getPrice()).willReturn(new Double(75));
        priceRowModelList.add(mapPrice);
        priceRowModelList.add(mapPrice2);
        Assert.assertEquals(50d,gpFilteredPriceContainer.getSinglePrice(priceRowModelList),0d);
    }

    /*@Test
    public void testFindBasePrice() {

    }*/
}

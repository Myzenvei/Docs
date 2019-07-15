/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Double;
import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.gp.commerce.core.dao.GPDeliveryDao;
import com.gp.commerce.dto.group.dto.DeliveryAddressGroupWsDTO;
import com.gp.commerce.facade.order.data.SplitEntryListWsDTO;
import com.gp.commerce.facade.order.data.SplitEntryWsDTO;
import com.gp.commerce.core.services.GPDeliveryService;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

@UnitTest
public class GPDeliveryServiceTest {

	@InjectMocks
	GPDeliveryService service = new GPDeliveryService();
	
	@Mock
	private GPDeliveryDao gpDeliveryDao;
    @Mock
    private ModelService modelService;
    @Mock
	private BaseStoreService baseStoreService;
    @Mock
	private CommonI18NService commonI18NService;
    @Mock
    private BaseStoreModel baseStoreModel;
    @Mock
    private CountryModel country;
    @Mock
    private AbstractOrderModel abstractOrder;
    @Mock
    private DeliveryModeModel deliveryMode;
    @Mock
    private DeliveryAddressGroupWsDTO deliveryGroupDto;
    @Mock
    private CurrencyModel curr;
    @Mock
    private SplitEntryWsDTO splitEntry;
    @Mock
    private PriceData price;
    
    @Before
    public void setUp() throws Exception {
         MockitoAnnotations.initMocks(this);
         service.setGpDeliveryDao(gpDeliveryDao);
         service.setModelService(modelService);
         ReflectionTestUtils.setField(service, "baseStoreService", baseStoreService);
         ReflectionTestUtils.setField(service, "commonI18NService", commonI18NService);
         
         Mockito.when(baseStoreService.getCurrentBaseStore()).thenReturn(baseStoreModel);
     
         List<CountryModel> countryModel = new ArrayList<>();
         countryModel.add(country);
         Mockito.when(commonI18NService.getAllCountries()).thenReturn(countryModel);
         Mockito.when(baseStoreModel.getShippingCountries()).thenReturn(countryModel);
         
         splitEntry.setPrice(price);
    }

    public void testGetGpDeliveryDao()
    {
        Assert.assertNotNull(service.getGpDeliveryDao());
    }
    
    @Test
    public void testGetModelService()
    {
        Assert.assertNotNull(service.getModelService());
    }
    
    @Test
    public void testGetDeliveryCostForDeliveryModeAndAbstractOrder() throws Exception
    {
        AbstractOrderModel abstractOrder = Mockito.mock(AbstractOrderModel.class);
        DeliveryModeModel deliveryMode = Mockito.mock(DeliveryModeModel.class);
        DeliveryAddressGroupWsDTO deliveryGroupDto = Mockito.mock(DeliveryAddressGroupWsDTO.class);
        
        CurrencyModel curr = Mockito.mock(CurrencyModel.class);
        Mockito.when(abstractOrder.getCurrency()).thenReturn(curr);
        
        AddressModel deliveryAddress = Mockito.mock(AddressModel.class);
        Mockito.when(abstractOrder.getDeliveryAddress()).thenReturn(deliveryAddress);
        CountryModel country = Mockito.mock(CountryModel.class);
        Mockito.when(deliveryAddress.getCountry()).thenReturn(country);
        
        Map<String, SplitEntryListWsDTO> addressGroupMap = new HashMap<>();
        SplitEntryListWsDTO splitEntryListWsDTO = Mockito.mock(SplitEntryListWsDTO.class);
        addressGroupMap.put("key", splitEntryListWsDTO);
        Mockito.when(deliveryGroupDto.getDeliveryGroup()).thenReturn(addressGroupMap);
        
        List<SplitEntryWsDTO> splitEntries = new ArrayList<>();
        SplitEntryWsDTO splitEntry = Mockito.mock(SplitEntryWsDTO.class);
        splitEntries.add(splitEntry);
        Mockito.when(splitEntryListWsDTO.getSplitEntries()).thenReturn(splitEntries);
        
        Mockito.when(splitEntry.getPrice()).thenReturn(price);
        Mockito.when(price.getValue()).thenReturn(BigDecimal.valueOf(11.0));
        Mockito.when(splitEntry.getQty()).thenReturn("1");

        Map<String,Double> productPriceList = new HashMap<>();
        Double totalPrice = new Double(11.0);
        productPriceList.put("product1", totalPrice);
        
        List<ZoneDeliveryModeValueModel> deliveryModeValueList = new ArrayList<>();
        ZoneDeliveryModeValueModel bestMatch = Mockito.mock(ZoneDeliveryModeValueModel.class);
        deliveryModeValueList.add(bestMatch);
        Mockito.when(gpDeliveryDao.getDeliveryModeValueList(curr,country,deliveryMode,11.0)).thenReturn(deliveryModeValueList);
        
        CurrencyModel myCurr = Mockito.mock(CurrencyModel.class);
        Mockito.when(bestMatch.getCurrency()).thenReturn(myCurr);
        
        Mockito.when(curr.getIsocode()).thenReturn("USD");
        Mockito.when(bestMatch.getValue()).thenReturn(totalPrice);
        
        ZoneDeliveryModeModel zoneDeliveryModeModel = Mockito.mock(ZoneDeliveryModeModel.class);
        
        Mockito.when(bestMatch.getDeliveryMode()).thenReturn(zoneDeliveryModeModel);
        Mockito.when(zoneDeliveryModeModel.getNet()).thenReturn(Boolean.TRUE);
        
        Assert.assertNotNull(service.getDeliveryCostForDeliveryModeAndAbstractOrder(deliveryMode,abstractOrder,deliveryGroupDto));
    }
    
    @Test
    public void testGetDeliveryCostforAddress() throws Exception
    {
        Map<String,Double> productPriceList = new HashMap<>();
        Double totalPrice = new Double(11.0);
        productPriceList.put("product1", totalPrice);
        
        CurrencyModel curr = Mockito.mock(CurrencyModel.class);
        CountryModel country = Mockito.mock(CountryModel.class);
        DeliveryModeModel deliveryMode = Mockito.mock(DeliveryModeModel.class);
        
        List<ZoneDeliveryModeValueModel> deliveryModeValueList = new ArrayList<>();
        ZoneDeliveryModeValueModel bestMatch = Mockito.mock(ZoneDeliveryModeValueModel.class); 
        deliveryModeValueList.add(bestMatch);
        Mockito.when(gpDeliveryDao.getDeliveryModeValueList(curr,country,deliveryMode,11.0)).thenReturn(deliveryModeValueList);
        
        CurrencyModel myCurr = Mockito.mock(CurrencyModel.class);
        Mockito.when(bestMatch.getCurrency()).thenReturn(myCurr);
        
        Mockito.when(curr.getIsocode()).thenReturn("USD");
        Mockito.when(bestMatch.getValue()).thenReturn(totalPrice);
        
        ZoneDeliveryModeModel zoneDeliveryModeModel = Mockito.mock(ZoneDeliveryModeModel.class);
        
        Mockito.when(bestMatch.getDeliveryMode()).thenReturn(zoneDeliveryModeModel);
        Mockito.when(zoneDeliveryModeModel.getNet()).thenReturn(Boolean.TRUE);
        
        Assert.assertNotNull(service.getDeliveryCostforAddress(productPriceList,curr,country,deliveryMode));
    }
    
    @Test
    public void testGetValueAsPrimitive()
    {
        ZoneDeliveryModeValueModel bestMatch = Mockito.mock(ZoneDeliveryModeValueModel.class);
        Double value = new Double(10);
        Mockito.when(bestMatch.getValue()).thenReturn(value);
        Assert.assertEquals(value.doubleValue(), service.getValueAsPrimitive(bestMatch),1);
    }
    
    @Test
    public void testIsNetAsPrimitive()
    {
        ZoneDeliveryModeValueModel bestMatch = Mockito.mock(ZoneDeliveryModeValueModel.class);
        ZoneDeliveryModeModel deliveryMode = Mockito.mock(ZoneDeliveryModeModel.class);
        
        Mockito.when(bestMatch.getDeliveryMode()).thenReturn(deliveryMode);
        Mockito.when(deliveryMode.getNet()).thenReturn(Boolean.TRUE);
        
        Assert.assertTrue(service.isNetAsPrimitive(bestMatch));
    }
    
    @Test
    public void testGetShippingCountries()
    {
    	Assert.assertTrue(service.getShippingCountries().get(0).equals(country));
    }
    

}

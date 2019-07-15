/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.delivery.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.dao.GPCartDao;
import com.gp.commerce.core.model.ShippingRestrictionModel;

import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

public class GPCountryZoneDeliveryModeDaoTest {

	private static final String TEST_COUNTRY_ISOCODE = "US";
	private static final String TEST_REGION_ISOCODESHORT = "US-CA";
	private static final String TEST_PRODUCT_CODE = "12345";
	private static final String TEST_DELIVERY_MODE = "test-delivery-mode";
	List<ShippingRestrictionModel> shippingRestrictionList = new ArrayList<>();
	
	@InjectMocks
	private GPCountryZoneDeliveryModeDao dao = new GPCountryZoneDeliveryModeDao();

	@Mock
	private CommonI18NService commonI18NService;
	
	@Mock
	private BaseStoreService baseStoreService;
	
	@Mock
	private SearchRestrictionService searchRestrictionService;
	
	@Mock
	private FlexibleSearchService flexibleSearchService;
	
	@Mock
	private GPCartDao gpCartDao ;
 
	@Before
	public void setUpWithShippingRestirction() {
		MockitoAnnotations.initMocks(this);
		ShippingRestrictionModel shippingRestriction = new ShippingRestrictionModel();
		shippingRestriction.setDeliveryMode(TEST_DELIVERY_MODE);
		shippingRestrictionList.add(shippingRestriction);
	}
	
	@Test
	public void findDeliveryModesTest()
	{
		AbstractOrderModel order = new AbstractOrderModel();
		AddressModel deliveryAddress = new AddressModel();
		CountryModel country = new CountryModel();
		country.setIsocode(TEST_COUNTRY_ISOCODE);
		deliveryAddress.setCountry(country);
		RegionModel region = new RegionModel();
		region.setIsocodeShort(TEST_REGION_ISOCODESHORT);
		deliveryAddress.setRegion(region);
		order.setDeliveryAddress(deliveryAddress);
		
		CurrencyModel currency = new CurrencyModel();
		order.setCurrency(currency);
		
		order.setNet(true);
		
		BaseStoreModel store = new BaseStoreModel();
		order.setStore(store);
		
		Mockito.doNothing().when(searchRestrictionService).disableSearchRestrictions();
		List<DeliveryModeModel> deliveryMode = new ArrayList<>();
		deliveryMode.add(Mockito.mock(DeliveryModeModel.class));
		final SearchResult searchResult = new SearchResultImpl(deliveryMode, 1, 1, 1);
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		
		List<AbstractOrderEntryModel> orderEntryList = new ArrayList<AbstractOrderEntryModel>();
		AbstractOrderEntryModel orderEntry = new AbstractOrderEntryModel();
		ProductModel product = new ProductModel();
		product.setCode(TEST_PRODUCT_CODE);
		orderEntry.setProduct(product);
		orderEntryList.add(orderEntry);
		order.setEntries(orderEntryList);
		
		when(gpCartDao.fetchShippingRestrictions(TEST_PRODUCT_CODE, TEST_COUNTRY_ISOCODE, TEST_REGION_ISOCODESHORT)).thenReturn(shippingRestrictionList);
		
		Mockito.when(commonI18NService.getCountry(Mockito.anyString())).thenReturn(Mockito.mock(CountryModel.class));
		dao.findDeliveryModes(order);
	}	
}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.cart.impl; 

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.model.GPEndUserLegalTermsModel;
import com.gp.commerce.core.model.ShippingRestrictionModel;
import com.gp.commerce.core.model.SplitEntryModel;
import com.gp.commerce.core.services.GPCartService;
import com.gp.commerce.core.services.GPDeliveryService;
import com.gp.commerce.core.strategies.GPDefaultCustomerNameStrategy;
import com.gp.commerce.dto.group.dto.DeliveryAddressGroupWsDTO;
import com.gp.commerce.facade.order.data.GPRestrictionProductListWsDTO;
import com.gp.commerce.facade.order.data.GPRestrictionProductWsDTO;
import com.gp.commerce.facade.order.data.LeaseAgreementData;
import com.gp.commerce.facade.order.data.ShippingRestrictionsData;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.leaseagreement.dto.GPAgreementResponseDTO;
import com.gpintegration.leaseagreement.dto.GPLeaseAgreementDTO;
import com.gpintegration.leaseagreement.dto.GPLeaseAgreementResponseDTO;
import com.gpintegration.service.GPCommerceLeaseAgreementService;
import com.gpintegration.service.impl.GPDefaultAddressVerificationService;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.ZoneDeliveryModeData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commercewebservicescommons.dto.user.CountryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.RegionWsDTO;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.PriceValue;
 
@UnitTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(Config.class )
@PowerMockIgnore(
{ "org.apache.logging.log4j.*" })
 public class GPDefaultCartFacadeTest {
	
 	@InjectMocks
	private GPDefaultCartFacade facade = new GPDefaultCartFacade();

	private static final String LEASE_AGREEMENT_NAME="leaseAgreementName";
	
 	@Mock
	private Converter<CountryModel, CountryData> countryConverter;
	
 	@Mock
 	private Converter<GPEndUserLegalTermsModel, LeaseAgreementData> gpLeaseDataConverter;
	
 	@Mock
 	private GPCartService gpCartService;
	
 	@Mock
 	private CartService cartService;
	
	@Mock
	private GPDefaultAddressVerificationService avsService;
	
	@Mock
	private Converter<ShippingRestrictionModel, ShippingRestrictionsData> gpShippingRestrictionConverter;

	@Mock
	private GPCommerceLeaseAgreementService gpCommerceLeaseAgreementService;
	
	@Mock
	private UserService userService;

	@Mock
	private DeliveryService deliveryService;
	
	@Mock
	private ModelService modelService;
	
	@Mock
	private Converter<ZoneDeliveryModeModel, ZoneDeliveryModeData> zoneDeliveryModeConverter;
	
	@Mock
	private Converter<DeliveryModeModel, DeliveryModeData> deliveryModeConverter;
	
	@Mock
	private PriceDataFactory priceDataFactory;
	
	@Mock
	private GPDeliveryService gpDeliveryService;
	
 	@Mock
	private Converter<DeliveryModeData, DeliveryModeModel> deliveryModeReverseConverter;
	
	@Mock
	private GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy;

	@Mock
	private SessionService sessionService;
 	
 	@Before
	public void setUp() throws Exception {
 		MockitoAnnotations.initMocks(this);
		facade.setGpShippingRestrictionConverter(gpShippingRestrictionConverter);
		facade.setZoneDeliveryModeConverter(zoneDeliveryModeConverter);
		facade.setDeliveryModeConverter(deliveryModeConverter);
		facade.setPriceDataFactory(priceDataFactory);
		facade.setModelService(modelService);
		facade.setGpDeliveryService(gpDeliveryService);
		facade.setDeliveryService(deliveryService);
		facade.setGpCartService(gpCartService);
		facade.setAvsService(avsService);
		facade.setDeliveryModeReverseConverter(deliveryModeReverseConverter);
		facade.setCartService(cartService);
		facade.setgpCommerceLeaseAgreementService(gpCommerceLeaseAgreementService);
		facade.setUserService(userService);
		facade.setSessionService(sessionService);
		facade.setGpLeaseDataConverter(gpLeaseDataConverter);
		ReflectionTestUtils.setField(facade, "gpDefaultCustomerNameStrategy", gpDefaultCustomerNameStrategy);
		ReflectionTestUtils.setField(facade, "countryConverter", countryConverter);
	}
	
	@Test
	public void testGetGpShippingRestrictionConverter()
	{
		Assert.assertNotNull(facade.getGpShippingRestrictionConverter());
	}
 
	@Test
	public void testGetZoneDeliveryModeConverter()
	{
		Assert.assertNotNull(facade.getZoneDeliveryModeConverter());
	}
 
	@Test
	public void testGetDeliveryModeConverter()
	{
		Assert.assertNotNull(facade.getDeliveryModeConverter());
	}

	@Test
	public void testGetPriceDataFactory()
	{
		Assert.assertNotNull(facade.getPriceDataFactory());
	}

	@Test
	public void testGetModelService()
	{
		Assert.assertNotNull(facade.getModelService());
	}

	@Test
	public void testGetGpDeliveryService()
	{
		Assert.assertNotNull(facade.getGpDeliveryService());
	}

	@Test
	public void testGetDeliveryService()
	{
		Assert.assertNotNull(facade.getDeliveryService());
	}

	@Test
	public void testGetGpCartService()
	{
		Assert.assertNotNull(facade.getGpCartService());
	}
	
	@Test
	public void testGetAvsService()
	{
		Assert.assertNotNull(facade.getAvsService());
	}

	@Test
	public void testGetDeliveryModeReverseConverter()
	{
		Assert.assertNotNull(facade.getDeliveryModeReverseConverter());
 	}
 
 	@Test
	public void testGetCartService()
	{
		Assert.assertNotNull(facade.getCartService());
	}
	
	@Test
	public void testGetgpCommerceLeaseAgreementService()
	{
		Assert.assertNotNull(facade.getgpCommerceLeaseAgreementService());
	}
	
	@Test
	public void testGetUserService()
	{
		Assert.assertNotNull(facade.getUserService());
	}
 
	@Test
	public void testGetSessionService()
	{
		Assert.assertNotNull(facade.getSessionService());
	}
	
	@Test
	public void testShareCart()
	{
		String toEmail = "abc@xyz.com";
		final CartModel cart = Mockito.mock(CartModel.class);
		
		Mockito.when(cartService.getSessionCart()).thenReturn(cart);
		Mockito.doNothing().when(gpCartService).shareCart(toEmail,toEmail,toEmail, cart);
		
		facade.shareCart(toEmail, toEmail,toEmail,Mockito.mock(CartData.class));
	}
	
	@Test
	public void testFetchShippingRestrictions() throws CommerceCartModificationException
	{
		GPRestrictionProductListWsDTO shippingProductList = Mockito.mock(GPRestrictionProductListWsDTO.class);
		
		GPRestrictionProductWsDTO shippingProd = Mockito.mock(GPRestrictionProductWsDTO.class);
		List<GPRestrictionProductWsDTO> shippingDetails = new ArrayList<>();
		shippingDetails.add(shippingProd);
		
		Mockito.when(shippingProductList.getShippingDetails()).thenReturn(shippingDetails);
		Mockito.when(shippingProd.getProductCode()).thenReturn("pCode");
		
		CountryWsDTO country = Mockito.mock(CountryWsDTO.class);
		Mockito.when(shippingProd.getCountry()).thenReturn(country);
		Mockito.when(country.getIsocode()).thenReturn("US");
		
		RegionWsDTO region = Mockito.mock(RegionWsDTO.class);
		Mockito.when(shippingProd.getRegion()).thenReturn(region);
		Mockito.when(region.getIsocodeShort()).thenReturn("TX");
		
		ShippingRestrictionModel restrictedListItem = Mockito.mock(ShippingRestrictionModel.class);
		List<ShippingRestrictionModel> restrictedList = new ArrayList<>();
		restrictedList.add(restrictedListItem);
		Mockito.when(gpCartService.fetchShippingRestrictions(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(restrictedList);
		
		ShippingRestrictionsData shippingRestData = Mockito.mock(ShippingRestrictionsData.class);
		Mockito.when(gpShippingRestrictionConverter.convert(restrictedListItem)).thenReturn(shippingRestData);
		
		Assert.assertNotNull(facade.fetchShippingRestrictions(shippingProductList));
	}
	@Test
	public void testGetLeaseAgreementForCountry() {
		
		GPEndUserLegalTermsModel leaseAgreementmodel = Mockito.mock(GPEndUserLegalTermsModel.class);
		List<GPEndUserLegalTermsModel> GPEndUserLegalTerms = new ArrayList<>();
		GPEndUserLegalTerms.add(leaseAgreementmodel);
		Mockito.when(gpCartService.getLeaseAgreementForCountry("USA")).thenReturn(GPEndUserLegalTerms);
		LeaseAgreementData leaseAgreementData=Mockito.mock(LeaseAgreementData.class);
		Mockito.when(gpLeaseDataConverter.convert(leaseAgreementmodel)).thenReturn(leaseAgreementData);
		assertTrue(facade.getLeaseAgreementForCountry("USA").equals(leaseAgreementData));
		
	}
		
	@Test
	public void testSaveLeaseAgreement() throws GPIntegrationException
	{
		GPLeaseAgreementDTO saveLeaseAgreementReq = Mockito.mock(GPLeaseAgreementDTO.class);
		
		CustomerModel customer=Mockito.mock(B2BCustomerModel.class);
		Mockito.when(userService.getCurrentUser()).thenReturn(customer);
		Mockito.when(customer.getName()).thenReturn("Customer Name");
		Mockito.when(customer.getContactEmail()).thenReturn("CUstomer@email.com");
		AddressModel address=Mockito.mock(AddressModel.class);
		Mockito.when(customer.getAddresses()).thenReturn(Collections.singletonList(address));
		Mockito.when(address.getPhone1()).thenReturn("1234567891");
		Mockito.when(address.getStreetname()).thenReturn("Street Name");
		Mockito.when(address.getCompany()).thenReturn("Company Name");
		Mockito.when(address.getStreetnumber()).thenReturn("12");
		Mockito.when(address.getTown()).thenReturn("ABC town");
		Mockito.when(address.getRegion()).thenReturn(Mockito.mock(RegionModel.class));
		Mockito.when(address.getRegion().getIsocode()).thenReturn("US-NYC");
		Mockito.when(address.getCountry()).thenReturn(Mockito.mock(CountryModel.class));
		Mockito.when(address.getCountry().getIsocode()).thenReturn("US");
		String[] names= {"Customer","Name"};
		Mockito.when(gpDefaultCustomerNameStrategy.splitName("Customer Name")).thenReturn(names);
		
		GPLeaseAgreementResponseDTO GPLeaseAgreementResponseDTO = Mockito.mock(GPLeaseAgreementResponseDTO.class);
		Mockito.when(gpCommerceLeaseAgreementService.createLeaseAgreement(saveLeaseAgreementReq)).thenReturn(GPLeaseAgreementResponseDTO);
		
		GPAgreementResponseDTO resp = Mockito.mock(GPAgreementResponseDTO.class);
		Mockito.when(GPLeaseAgreementResponseDTO.getAgreementResponse()).thenReturn(resp);
		Mockito.when(resp.getAgreementId()).thenReturn("agreementID");
		Mockito.when(resp.getStatus()).thenReturn("status");
		Mockito.when(resp.getErrorCode()).thenReturn("errorCode");
		Mockito.when(resp.getErrorMessage()).thenReturn("errorMsg");
		
		Assert.assertNotNull(facade.saveLeaseAgreement(saveLeaseAgreementReq));
	}
	
	@Test
	public void testValidateCouponforSiteReturnsTrue()
	{
		String siteId = "siteId";
		String couponCode = "code1-code2";
		
		AbstractCouponModel couponModel = Mockito.mock(AbstractCouponModel.class);
		Mockito.when(gpCartService.getCouponForCode(couponCode)).thenReturn(couponModel);
		
		BaseSiteModel baseSite = Mockito.mock(BaseSiteModel.class);
		Mockito.when(couponModel.getBaseSite()).thenReturn(baseSite);
		Mockito.when(baseSite.getUid()).thenReturn(siteId);
		Assert.assertTrue(facade.validateCouponforSite(siteId, couponCode));
	}
	
	@Test
	public void testValidateCouponforSiteReturnsFalse()
	{
		String siteId = "siteId";
		String couponCode = "code1-code2";
		
		AbstractCouponModel couponModel = null;
		Mockito.when(gpCartService.getCouponForCode(couponCode)).thenReturn(couponModel);
		Assert.assertFalse(facade.validateCouponforSite(siteId, couponCode));
	}
	
	@Test
	public void testVerifyAddress() throws GPIntegrationException
	{
		AddressData data = Mockito.mock(AddressData.class);
		Mockito.when(avsService.verifyAddress(data)).thenReturn(data);
		assertTrue(facade.verifyAddress(data).equals(data));
	}
	@Test
	public void testVerifyAddressError() throws GPIntegrationException
	{
		AddressData data = Mockito.mock(AddressData.class);
		GPIntegrationException exception = Mockito.mock(GPIntegrationException.class);
		Mockito.when(exception.getMessage()).thenReturn("GPIntegrationException");
		Mockito.when(avsService.verifyAddress(data)).thenThrow(exception);
		assertTrue(facade.verifyAddress(data).equals(data));
	}
	
	@Test
	public void testSuggestAddresses() throws GPIntegrationException
	{
		AddressData data = Mockito.mock(AddressData.class);
		List<AddressData> addressList = new ArrayList<>();
		addressList.add(data);
		
		Mockito.when(avsService.suggestAddresses(data)).thenReturn(addressList);
		Assert.assertNotNull(facade.suggestAddresses(data));
	}
	@Test
	public void testSuggestAddressesError() throws GPIntegrationException
	{
		AddressData data = Mockito.mock(AddressData.class);
		GPIntegrationException exception = Mockito.mock(GPIntegrationException.class);
		List<AddressData> addressList = new ArrayList<>();
		addressList.add(data);
		
		Mockito.when(avsService.suggestAddresses(data)).thenThrow(exception);
		Mockito.when(exception.getMessage()).thenReturn("GPIntegrationException");
		assertTrue(facade.suggestAddresses(data).contains(data));
	}
	
	@Test
	public void testGetSupportedDeliveryModesWithArgs() throws Exception
	{
		DeliveryAddressGroupWsDTO deliveryGroupDto = Mockito.mock(DeliveryAddressGroupWsDTO.class);
		final CartModel cartModel = Mockito.mock(CartModel.class);
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);
		
		ZoneDeliveryModeModel deliveryModeModel = Mockito.mock(ZoneDeliveryModeModel.class);
		ZoneDeliveryModeModel deliveryModeModel1 = Mockito.mock(ZoneDeliveryModeModel.class);
		List<DeliveryModeModel> deliveryModeModels = new ArrayList<>();
		deliveryModeModels.add(deliveryModeModel);
		deliveryModeModels.add(deliveryModeModel1);
		Mockito.when(deliveryService.getSupportedDeliveryModeListForOrder(cartModel)).thenReturn(deliveryModeModels);
		
		Map<String, PriceValue> deliveryCostList = new HashMap<>();
		PriceValue deliveryCost = Mockito.mock(PriceValue.class);
		deliveryCostList.put("key", deliveryCost);
		Mockito.when(gpDeliveryService.getDeliveryCostForDeliveryModeAndAbstractOrder(deliveryModeModel, cartModel, deliveryGroupDto)).thenReturn(deliveryCostList);
		Mockito.when(gpDeliveryService.getDeliveryCostForDeliveryModeAndAbstractOrder(deliveryModeModel1, cartModel, deliveryGroupDto)).thenReturn(deliveryCostList);
		
		ZoneDeliveryModeData zoneDeliveryModeData = Mockito.mock(ZoneDeliveryModeData.class);
		Mockito.when(zoneDeliveryModeConverter.convert(deliveryModeModel)).thenReturn(zoneDeliveryModeData);
		Mockito.when(zoneDeliveryModeConverter.convert(deliveryModeModel1)).thenReturn(zoneDeliveryModeData);
		
		ZoneDeliveryModeValueModel zoneDeliveryModeValueModel = Mockito.mock(ZoneDeliveryModeValueModel.class);
		List<ZoneDeliveryModeValueModel> zoneDeliveryList = new ArrayList<>();
		zoneDeliveryList.add(zoneDeliveryModeValueModel);
		
		Set<ZoneDeliveryModeValueModel> values = new HashSet<>();
		values.add(zoneDeliveryModeValueModel);
		Mockito.when(deliveryModeModel.getValues()).thenReturn(values);
		Mockito.when(deliveryModeModel1.getValues()).thenReturn(values);
		
		ZoneModel zone = Mockito.mock(ZoneModel.class);
		Mockito.when(zoneDeliveryModeValueModel.getZone()).thenReturn(zone);
		
		CountryModel country = Mockito.mock(CountryModel.class);
		Set<CountryModel> countries = new HashSet<>();
		countries.add(country);
		Mockito.when(zone.getCountries()).thenReturn(countries);
		AddressModel address = Mockito.mock(AddressModel.class);
		Mockito.when(cartModel.getDeliveryAddress()).thenReturn(address);
		Mockito.when(address.getCountry()).thenReturn(country);
		
		Double cost = new Double(10);
		Mockito.when(zoneDeliveryModeValueModel.getValue()).thenReturn(cost);
		Mockito.when(deliveryCost.getValue()).thenReturn(cost);
		Mockito.when(zoneDeliveryModeValueModel.getDeliveryModeType()).thenReturn(GpcommerceFacadesConstants.PERCENTAGE);
		Mockito.when(cartModel.getSubtotal()).thenReturn(cost);
		
		CurrencyModel currency = Mockito.mock(CurrencyModel.class);
		Mockito.when(cartModel.getCurrency()).thenReturn(currency);
		Mockito.when(currency.getIsocode()).thenReturn("USD");
		
		PriceData priceData = Mockito.mock(PriceData.class);
		Mockito.when(priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(cost), "USD")).thenReturn(priceData);
		
		Assert.assertNotNull(facade.getSupportedDeliveryModes(deliveryGroupDto));
	}
	
	@Test
	public void testUpdateSpliEntriesWithDeliveryModes()
	{
		PK addressPK = PK.BIG_PK;
		Map<String, List<DeliveryModeData>> deliveryModeMap = new HashMap<>();
		DeliveryModeData deliveryMode = Mockito.mock(DeliveryModeData.class);
		List<DeliveryModeData> deliveryModeData = new ArrayList<>();
		deliveryModeData.add(deliveryMode);
		deliveryModeMap.put(addressPK.toString(), deliveryModeData);
		
		final CartModel cartModel = Mockito.mock(CartModel.class);
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);
		
		AbstractOrderEntryModel entry = Mockito.mock(AbstractOrderEntryModel.class);
		List<AbstractOrderEntryModel> entries = new ArrayList<>();
		entries.add(entry);
		
		SplitEntryModel splitEntryModel = Mockito.mock(SplitEntryModel.class);
		List<SplitEntryModel> splitEntryModels = new ArrayList<>();
		splitEntryModels.add(splitEntryModel);
		Mockito.when(cartModel.getEntries()).thenReturn(entries);
		Mockito.when(entry.getSplitEntry()).thenReturn(splitEntryModels);
		
		AddressModel deliveryAddress = Mockito.mock(AddressModel.class);
		Mockito.when(splitEntryModel.getDeliveryAddress()).thenReturn(deliveryAddress);
		Mockito.when(entry.getSplitEntry()).thenReturn(splitEntryModels);
		Mockito.when(deliveryAddress.getPk()).thenReturn(addressPK);
		
		List<DeliveryModeModel> deliveryModeModelList = new ArrayList<>();
		Mockito.when(deliveryModeReverseConverter.convertAll(deliveryModeData)).thenReturn(deliveryModeModelList);
		Mockito.doNothing().when(modelService).save(splitEntryModel);
		
		facade.updateSpliEntriesWithDeliveryModes(deliveryModeMap);
		Mockito.verify(gpCartService).updateSplitEntry(splitEntryModel);
	}
	
	@Test
	public void testAddDeliveryModeToMap()
	{
		Map<String, DeliveryModeData> deliveryModeMap = new HashMap<>();
		Map<String, List<DeliveryModeData>> deliveryModeListMap = new HashMap<>();
		DeliveryModeData deliveryModeData = Mockito.mock(DeliveryModeData.class);
		deliveryModeMap.put("key", deliveryModeData);
		facade.addDeliveryModeToMap(deliveryModeMap, deliveryModeListMap);
	}
	
	@Test
	public void testGetSupportedDeliveryModesDeliveryReturnsDeliveryModeModel() throws Exception
	{
		final CartModel cartModel = Mockito.mock(CartModel.class);
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);
		
		DeliveryModeModel deliveryModeModel = Mockito.mock(DeliveryModeModel.class);
		List<DeliveryModeModel> deliveryModeModels = new ArrayList<>();
		deliveryModeModels.add(deliveryModeModel);
		Mockito.when(deliveryService.getSupportedDeliveryModeListForOrder(cartModel)).thenReturn(deliveryModeModels);
		
		DeliveryModeData deliveryModeData = Mockito.mock(DeliveryModeData.class);
		Mockito.when(deliveryModeConverter.convert(deliveryModeModel)).thenReturn(deliveryModeData);
		
		Assert.assertNotNull(facade.getSupportedDeliveryModes());
	}
	
	@Test
	public void testGetSupportedDeliveryModesDeliveryReturnsZoneDeliveryModeData() throws Exception
	{
		final CartModel cartModel = Mockito.mock(CartModel.class);
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);
		
		ZoneDeliveryModeModel deliveryModeModel = Mockito.mock(ZoneDeliveryModeModel.class);
		List<DeliveryModeModel> deliveryModeModels = new ArrayList<>();
		deliveryModeModels.add(deliveryModeModel);
		Mockito.when(deliveryService.getSupportedDeliveryModeListForOrder(cartModel)).thenReturn(deliveryModeModels);

		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);
		PriceValue deliveryCost = Mockito.mock(PriceValue.class);
		Mockito.when(deliveryService.getDeliveryCostForDeliveryModeAndAbstractOrder(deliveryModeModel, cartModel)).thenReturn(deliveryCost);

		ZoneDeliveryModeData zoneDeliveryModeData = Mockito.mock(ZoneDeliveryModeData.class);
		Mockito.when(zoneDeliveryModeConverter.convert(deliveryModeModel)).thenReturn(zoneDeliveryModeData);
		
		ZoneDeliveryModeValueModel zoneDeliveryModeValueModel = Mockito.mock(ZoneDeliveryModeValueModel.class);
		List<ZoneDeliveryModeValueModel> zoneDeliveryList = new ArrayList<>();
		zoneDeliveryList.add(zoneDeliveryModeValueModel);
		
		Set<ZoneDeliveryModeValueModel> values = new HashSet<>();
		values.add(zoneDeliveryModeValueModel);
		Mockito.when(deliveryModeModel.getValues()).thenReturn(values);
		
		ZoneModel zone = Mockito.mock(ZoneModel.class);
		Mockito.when(zoneDeliveryModeValueModel.getZone()).thenReturn(zone);
		
		CountryModel country = Mockito.mock(CountryModel.class);
		Set<CountryModel> countries = new HashSet<>();
		countries.add(country);
		Mockito.when(zone.getCountries()).thenReturn(countries);
		AddressModel address = Mockito.mock(AddressModel.class);
		Mockito.when(cartModel.getDeliveryAddress()).thenReturn(address);
		Mockito.when(address.getCountry()).thenReturn(country);
		
		Double cost = new Double(10);
		Mockito.when(zoneDeliveryModeValueModel.getValue()).thenReturn(cost);
		Mockito.when(deliveryCost.getValue()).thenReturn(cost);
		Mockito.when(zoneDeliveryModeValueModel.getDeliveryModeType()).thenReturn(GpcommerceFacadesConstants.PERCENTAGE);
		Mockito.when(cartModel.getSubtotal()).thenReturn(cost);
		
		CurrencyModel currency = Mockito.mock(CurrencyModel.class);
		Mockito.when(cartModel.getCurrency()).thenReturn(currency);
		Mockito.when(currency.getIsocode()).thenReturn("USD");
		
		PriceData priceData = Mockito.mock(PriceData.class);
		Mockito.when(priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(cost), "USD")).thenReturn(priceData);
		
		Assert.assertNotNull(facade.getSupportedDeliveryModes());
	}
	
	@Test
	public void testUpdateDeliveryInstruction()
	{
		String deliveryInst = "instruction";
		final CartModel cartModel = Mockito.mock(CartModel.class);
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);
		Mockito.doNothing().when(modelService).refresh(cartModel);
		DeliveryModeModel deliverymode = Mockito.mock(DeliveryModeModel.class);
		Mockito.when(cartModel.getDeliveryMode()).thenReturn(deliverymode);
		Mockito.doNothing().when(modelService).save(deliverymode);
		facade.updateDeliveryInstruction(deliveryInst);
	}
	
	@Test
	public void testUpdateLeaseName()
	{
		CartModel sessionCart = Mockito.mock(CartModel.class);
		Mockito.when(cartService.getSessionCart()).thenReturn(sessionCart);		
		String leaseName = "leaseName";
		Mockito.doNothing().when(modelService).save(sessionCart);
		facade.updateLeaseName(leaseName);
 	}

	@Test
	public void testUpdateLeaseAgreementId()
	{
		CartModel sessionCart = Mockito.mock(CartModel.class);
		Mockito.when(cartService.getSessionCart()).thenReturn(sessionCart);
		Mockito.when(cartService.hasSessionCart()).thenReturn(true);
		
		String agreementId = "agreementId";
		String leaseTermId="leaseTermId";
		PowerMockito.mockStatic(Config.class);
		Mockito.when(Config.getBoolean(GpcommerceCoreConstants.ENABLE_LEASE_AGREEMENT, true)).thenReturn(true);
		facade.updateLeaseAgreementId(agreementId, leaseTermId);
		Mockito.verify(gpCartService).updateLease(sessionCart);
		
 	}
	@Test
	public void testGetShippingCountries()
	{
		CountryModel countryModel=Mockito.mock(CountryModel.class);
		CountryData countrydata=Mockito.mock(CountryData.class);
		Mockito.when(gpDeliveryService.getShippingCountries()).thenReturn(Collections.singletonList(countryModel));
		Mockito.when(countryConverter.convert(countryModel)).thenReturn(countrydata);
		assertTrue(facade.getShippingCountries().contains(countrydata));
		
	}
	@Test
	public void testCheckIncompatibleProducts()
	{
		CartModel sessionCart = Mockito.mock(CartModel.class);
		ProductModel productModel = Mockito.mock(ProductModel.class);
		Mockito.when(cartService.getSessionCart()).thenReturn(sessionCart);
		Mockito.when(cartService.hasSessionCart()).thenReturn(true);
		Mockito.when(productModel.getName()).thenReturn("product name");
		Mockito.when(gpCartService.checkIncompatibleProducts(sessionCart)).thenReturn(Collections.singletonList(productModel));
		assertTrue(facade.checkIncompatibleProducts().getProductNames().contains("product name"));
		
		
	}
}

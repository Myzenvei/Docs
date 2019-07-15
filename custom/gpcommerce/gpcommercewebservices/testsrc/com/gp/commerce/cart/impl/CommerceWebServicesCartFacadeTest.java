package com.gp.commerce.cart.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.calculation.AvalaraResponseDetailDTO;
import com.gp.commerce.core.calculation.AvalaraResponseLineItemDTO;
import com.gp.commerce.core.calculation.AvalaraTaxResponseDTO;
import com.gp.commerce.core.calculation.service.impl.GPDefaultAvalaraTaxCalculationService;
import com.gp.commerce.core.model.GPOrderEntryAttributeModel;
import com.gp.commerce.core.model.LeadTimeModel;
import com.gp.commerce.core.model.ScheduleInstallationModel;
import com.gp.commerce.core.model.ShippingRestrictionModel;
import com.gp.commerce.core.model.SplitEntryModel;
import com.gp.commerce.core.services.GPCartService;
import com.gp.commerce.dto.GPScheduleInstallationWsDTO;
import com.gp.commerce.dto.cybersource.GPSignatureDTO;
import com.gp.commerce.facade.order.data.ShippingRestrictionsData;
import com.gp.commerce.facade.order.data.SplitEntryData;
import com.gp.commerce.facade.order.data.SplitEntryListWsDTO;
import com.gp.commerce.facade.order.data.SplitEntryWsDTO;
import com.gp.commerce.facades.data.GPScheduleInstallationData;
import com.gp.commerce.order.entry.data.GPSplitListWsDTO;
import com.gp.commerce.order.entry.data.GPSplitWsDTO;
import com.gp.commerce.order.entry.data.GiftWrapListWsDTO;
import com.gp.commerce.order.entry.data.GiftWrapWsDTO;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.service.impl.DefaultGPCyberSourceSignatureService;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.AddToCartParams;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commercewebservicescommons.dto.order.ZoneDeliveryModeWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

@UnitTest
public class CommerceWebServicesCartFacadeTest {
	@InjectMocks
	CommerceWebServicesCartFacade facade = new CommerceWebServicesCartFacade();

	@Mock
	private CartService cartService;

	@Mock
	CartModel cart;
	
	@Mock
	AbstractOrderModel abstractOrderModel;

	@Mock
	private ConfigurationService configurationService;
	@Mock
	private Configuration configuration;

	@Mock
	private SessionService sessionService;

	@Mock
	private ModelService modelService;

	@Mock
	private CommerceCartService commerceCartService;
	@Mock
	private GPDefaultAvalaraTaxCalculationService gpAvalaraTaxService;
	@Mock
	private Converter<CartModel, CartData> cartConverter;
	@Mock
	private GPCartService gpCartService;
	@Mock
	private Converter<ShippingRestrictionModel, ShippingRestrictionsData> gpShippingRestrictionConverter;
	@Mock
	private Converter<AddToCartParams, CommerceCartParameter> commerceCartParameterConverter;
	@Mock
	private Converter<CommerceCartModification, CartModificationData> cartModificationConverter;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private UserService userService;
	@Mock
	private DeliveryService deliveryService;
	@Mock
	private Converter<DeliveryModeModel, DeliveryModeData> deliveryModeConverter;
    @Mock
    private DefaultGPCyberSourceSignatureService signatureService;
    @Mock
    private Converter<AddressModel, AddressData> addressConverter;
	
	AvalaraTaxResponseDTO responseDto;
	CartData cartData;
	CartModel cartModel;

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		cartData = new CartData();
		List<OrderEntryData> orderEntries = new ArrayList<>();
		OrderEntryData orderEntry = new OrderEntryData();
		ProductData product = new ProductData();
		StockData stock = new StockData();
		stock.setLowStockMessage("low stock");
		product.setStock(stock);
		orderEntry.setProduct(product);
		orderEntries.add(orderEntry);
		cartData.setEntries(orderEntries);
        cart = new CartModel();
		AddressModel address = new AddressModel();
		cart.setDeliveryAddress(address);
		List<AbstractOrderEntryModel> entries = new ArrayList<>();
		AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
        List<SplitEntryModel> splitEntries = new ArrayList<>();
		SplitEntryModel entryModel = new SplitEntryModel();
		entryModel.setCode("test");
		
		AddressModel adr = new AddressModel();
		CountryModel country = new CountryModel();
		country.setIsocode("us");
		RegionModel region = new RegionModel();
		region.setIsocodeShort("us");
		adr.setRegion(region);
		adr.setCountry(country);
		entryModel.setDeliveryAddress(adr);
		DeliveryModeModel deliveryMode = new DeliveryModeModel();
		deliveryMode.setCode("test");
		entryModel.setDeliveryMode(deliveryMode);
		splitEntries.add(entryModel);
		entry.setSplitEntry(splitEntries);
		ProductModel pdt = new ProductModel();
		pdt.setCode("test");
		entry.setProduct(pdt);
        entries.add(entry);
		cart.setEntries(entries);
		abstractOrderModel = new AbstractOrderModel();
		responseDto = new AvalaraTaxResponseDTO();
		responseDto.setTotalTax("33");
		List<AvalaraResponseLineItemDTO> lines = new ArrayList<>();
		AvalaraResponseLineItemDTO line = new AvalaraResponseLineItemDTO();
		List<AvalaraResponseDetailDTO> details = new ArrayList<>();
		AvalaraResponseDetailDTO detail = new AvalaraResponseDetailDTO();
		detail.setTaxName("taxName");
		detail.setRate(33.33);
		details.add(detail);
		line.setDetails(details);
		lines.add(line);
		responseDto.setLines(lines);
		facade.setCommerceCartService(commerceCartService);

	}

	@Test
	public void getSessionCartTest() throws GPIntegrationException {

		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configurationService.getConfiguration().getBoolean("gp.tax.calculation.serivce.avalara", true))
				.thenReturn(true);
		when(configurationService.getConfiguration().getString(Mockito.anyString(), Mockito.anyString()))
				.thenReturn("message");

		Mockito.when(cartService.getSessionCart()).thenReturn(cart);
		Object value = "cart";
		Mockito.when(sessionService.getAttribute("calculationType")).thenReturn(value);
		Mockito.doNothing().when(modelService).save(cart);
		Mockito.doNothing().when(modelService).save(Mockito.any(AbstractOrderModel.class));
		Mockito.doNothing().when(modelService).refresh(cart);
		// Mockito.doNothing().when(commerceCartService).calculateCart(cart);
		Mockito.doNothing().when(modelService).saveAll(Mockito.any(AbstractOrderEntryModel.class));
		Mockito.doNothing().when(modelService).saveAll(Mockito.anyListOf(AbstractOrderEntryModel.class));
		Mockito.doNothing().when(modelService).saveAll(cart.getEntries());
		Mockito.when(gpAvalaraTaxService.calculateTax(abstractOrderModel, true)).thenReturn(responseDto);
		Mockito.when(cartConverter.convert(cart)).thenReturn(cartData);
		CartData cData = facade.getSessionCart();
		assertNotNull(cData);

	}

	@Test
	public void getSessionCartNotCartValueTest(){
		Object test = "test";
		List<ShippingRestrictionModel> models = new ArrayList<>();
		ShippingRestrictionModel model = new ShippingRestrictionModel();
		model.setDeliveryMode("0");
        models.add(model);
		ShippingRestrictionsData data = new ShippingRestrictionsData();
		Mockito.when(gpShippingRestrictionConverter.convert(model)).thenReturn(data);
		Mockito.when(gpCartService.fetchShippingRestrictions(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(models);
		Mockito.when(sessionService.getAttribute("calculationType")).thenReturn(test);
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configurationService.getConfiguration().getBoolean("gp.tax.calculation.serivce.avalara", true))
		.thenReturn(true);
		Mockito.when(cartService.getSessionCart()).thenReturn(cart);
		Mockito.when(cartConverter.convert(cart)).thenReturn(cartData);
		CartData cData =facade.getSessionCart();
		assertNotNull(cData);

	}
	
	@Test
	public void updateInstalledQuantityTest() throws CommerceCartModificationException{
		long entryNumber=44;
		Long qty=9L;
		CartModel cartModel = new CartModel();
		List<AbstractOrderEntryModel> entries = new ArrayList<>();
		AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		List<GPOrderEntryAttributeModel> addAtt = new ArrayList<>();
		GPOrderEntryAttributeModel add = new GPOrderEntryAttributeModel();
		entry.setEntryNumber(8);
		add.setName("installedEntry");
		add.setValue("44");
		addAtt.add(add);
		entry.setAdditionalAttributes(addAtt);
		entries.add(entry);
		cartModel.setEntries(entries);
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);
		//Mockito.doNothing().when(commerceCartService).updateQuantityForCartEntry(Mockito.any(CommerceCartParameter.class));
		facade.updateInstalledQuantity(entryNumber, qty);
		}
	
	@Test
	public void deleteInstallableProductTest() throws CommerceCartModificationException{
	long entryNumber=8;
	CartModel cartModel = new CartModel();
	List<AbstractOrderEntryModel> entries = new ArrayList<>();
	AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
	List<GPOrderEntryAttributeModel> addAtt = new ArrayList<>();
	GPOrderEntryAttributeModel add = new GPOrderEntryAttributeModel();
	add.setValue("8");
    add.setName("installedEntry");
	addAtt.add(add);
	entry.setEntryNumber(8);
	entry.setAdditionalAttributes(addAtt);
	entries.add(entry);
	cartModel.setEntries(entries);
	Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);
//	Mockito.doNothing().when(commerceCartService).updateQuantityForCartEntry(Mockito.any(CommerceCartParameter.class));
	facade.deleteInstallableProduct(entryNumber);	

	}
	
	@Test
	public void giftWrapTrueTest() throws CommerceCartModificationException{
	CartModel cartModel = new CartModel();	
	GiftWrapListWsDTO giftWrap = new GiftWrapListWsDTO();
	CommerceCartParameter parameter = new CommerceCartParameter();
	CommerceCartModification modification = new CommerceCartModification();
	CartModificationData data = new CartModificationData();
	List<AbstractOrderEntryModel> entries = new ArrayList<>();
	AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
	List<GPOrderEntryAttributeModel> addAtt = new ArrayList<>();
	GPOrderEntryAttributeModel add = new GPOrderEntryAttributeModel();
	entry.setQuantity(8L);
	entry.setGiftWrapped(false);
	add.setName("giftableProduct");
	add.setValue("8");
	addAtt.add(add);
	entry.setAdditionalAttributes(addAtt);
	entry.setEntryNumber(8);
	entries.add(entry);
	cartModel.setEntries(entries);
	List<GiftWrapWsDTO> giftWrapList = new ArrayList<>();
	GiftWrapWsDTO wrap = new GiftWrapWsDTO();
	wrap.setEntryNumber(8);
	wrap.setGiftWrapped(true);
	wrap.setGiftMessage("giftMessage");
	giftWrapList.add(wrap);
	giftWrap.setGiftWrap(giftWrapList);
	Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);
	Mockito.when(commerceCartService.addToCart(parameter)).thenReturn(modification);
	Mockito.when(commerceCartParameterConverter.convert(Mockito.any(AddToCartParams.class))).thenReturn(parameter);
	Mockito.when(cartModificationConverter.convert(modification)).thenReturn(data);
	Mockito.doNothing().when(modelService).save(entry);
	facade.giftWrap(giftWrap);	
		
	}
	
	@Test
	public void giftWrapFalseTest() throws CommerceCartModificationException{
		CartModel cartModel = new CartModel();
		GiftWrapListWsDTO giftWrap = new GiftWrapListWsDTO();
		List<GiftWrapWsDTO> giftWrapList = new ArrayList<>();
		List<AbstractOrderEntryModel> entries = new ArrayList<>();
		AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		List<GPOrderEntryAttributeModel> addAtt = new ArrayList<>();
		GPOrderEntryAttributeModel add = new GPOrderEntryAttributeModel();
		add.setValue("8");
		addAtt.add(add);
		entry.setAdditionalAttributes(addAtt);
		entry.setGiftWrapped(true);
		entry.setEntryNumber(8);
		entries.add(entry);
		cartModel.setEntries(entries);
		GiftWrapWsDTO wrap = new GiftWrapWsDTO();	
		wrap.setGiftWrapped(false);
		wrap.setEntryNumber(8);
		giftWrapList.add(wrap);
		giftWrap.setGiftWrap(giftWrapList);
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);
		//Mockito.doNothing().when(commerceCartService).updateQuantityForCartEntry(Mockito.any(CommerceCartParameter.class));
		Mockito.doNothing().when(modelService).remove(entry);
		facade.giftWrap(giftWrap);
		
	}
	
	@Test
	public void isCurrentUserCartTest(){
		String guid = "test";
		CartModel cartModel = new CartModel();
		BaseSiteModel site = new BaseSiteModel();
		Mockito.when(baseSiteService.getCurrentBaseSite()).thenReturn(site);
		UserModel user = new UserModel();
		Mockito.when(userService.getCurrentUser()).thenReturn(user);
		Mockito.when(commerceCartService.getCartForGuidAndSiteAndUser(guid, site, user)).thenReturn(cartModel);
		boolean value =facade.isCurrentUserCart(guid);
		assertTrue(value);
		}
	
	
	@Test
	public void isAnonymousUserCartTest(){
		
		String guid = "test";
		CartModel cartModel = new CartModel();
		BaseSiteModel site = new BaseSiteModel();
		Mockito.when(baseSiteService.getCurrentBaseSite()).thenReturn(site);
		CustomerModel user = new CustomerModel();
		Mockito.when(userService.getAnonymousUser()).thenReturn(user);
		Mockito.when(commerceCartService.getCartForGuidAndSiteAndUser(guid, site, user)).thenReturn(cartModel);
		boolean value =facade.isAnonymousUserCart(guid);
		assertTrue(value);
		
	}
	
	@Test
	public void scheduleInstallationTest(){
		CartModel cartModel = new CartModel();
		ScheduleInstallationModel model = new ScheduleInstallationModel();
		cartModel.setScheduleInstallation(model);
		GPScheduleInstallationWsDTO scheduleWsDTO = new GPScheduleInstallationWsDTO();
		scheduleWsDTO.setName("test");
		scheduleWsDTO.setPhoneNo("99999999999");
		scheduleWsDTO.setExtraInfo("extraInfo");
		scheduleWsDTO.setPreferredTime("time");
		scheduleWsDTO.setPreferredDate("preferredDate");
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);
		Mockito.doNothing().when(modelService).saveAll(model,cartModel);
		facade.scheduleInstallation(scheduleWsDTO);	
	}
	
	@Test
	public void scheduleInstallationNullTest(){
		
		CartModel cartModel = new CartModel();
		ScheduleInstallationModel insatallationModel = new ScheduleInstallationModel();
		GPScheduleInstallationWsDTO scheduleWsDTO = new GPScheduleInstallationWsDTO();
		scheduleWsDTO.setName("test");
		scheduleWsDTO.setPhoneNo("99999999999");
		scheduleWsDTO.setExtraInfo("extraInfo");
		scheduleWsDTO.setPreferredTime("time");
		scheduleWsDTO.setPreferredDate("preferredDate");
		cartModel.setScheduleInstallation(null);
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);
		Mockito.doNothing().when(modelService).saveAll(insatallationModel,cartModel);
		Mockito.when(modelService.create(ScheduleInstallationModel.class)).thenReturn(insatallationModel);
		facade.scheduleInstallation(scheduleWsDTO);
		
	}
	
	
	@Before
	public void setCartModelData() {
		cartModel = new CartModel();
		List<AbstractOrderEntryModel> entries = new ArrayList<>();
		AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		List<SplitEntryModel> splitEntries = new ArrayList<>();
		SplitEntryModel split = new SplitEntryModel();
		AddressModel address = new AddressModel();
		RegionModel region = new RegionModel();
		List<LeadTimeModel> leadTime = new ArrayList<>();
		LeadTimeModel lead = new LeadTimeModel();
		lead.setSiteId("baseStoreId");
		lead.setLeadTime("12:00");
		leadTime.add(lead);
		region.setLeadTimes(leadTime);
		address.setRegion(region);
		split.setDeliveryAddress(address);
		splitEntries.add(split);
		entry.setSplitEntry(splitEntries);
		List<GPOrderEntryAttributeModel> addAtt = new ArrayList<>();
		GPOrderEntryAttributeModel add = new GPOrderEntryAttributeModel();
		add.setName("installed");
		addAtt.add(add);
		entry.setAdditionalAttributes(addAtt);
		entries.add(entry);
		cartModel.setEntries(entries);
		
	}
	
	@Test
	public void getScheduleInstallationTest() {

		CartData cartData = new CartData();
		String baseStoreId = "baseStoreId";
		List<OrderEntryData> entries = new ArrayList<>();
		BaseSiteModel site = new BaseSiteModel();
		OrderEntryData entry = new OrderEntryData();
		List<SplitEntryData> splitEntries = new ArrayList<>();
		SplitEntryData entryData = new SplitEntryData();
		AddressData deliveryAddress = new AddressData();
		deliveryAddress.setId("id");
		cartData.setDeliveryAddress(deliveryAddress);
		entryData.setDeliveryAddress(deliveryAddress);
		splitEntries.add(entryData);
		entry.setSplitEntries(splitEntries);
		Map<String, String> additionalAttributes = new HashMap<>();
		entry.setQuantity(8L);
		entry.setAdditionalAttributes(additionalAttributes);
		additionalAttributes.put("installedEntry", "value");
		additionalAttributes.put("installed", "value");
		entries.add(entry);
		cartData.setEntries(entries);
		GPScheduleInstallationData scheduleInstallation = new GPScheduleInstallationData();
		cartData.setScheduleInstallation(scheduleInstallation);
		Mockito.when(facade.getSessionCart()).thenReturn(cartData);
		Mockito.when(baseSiteService.getCurrentBaseSite().getUid()).thenReturn(baseStoreId);
	    given(cartService.getSessionCart()).willReturn(cartModel);
		GPScheduleInstallationData data = facade.getScheduleInstallation();
		assertNotNull(data);

	}
	
	@Test
	public void updateSpliEntriesWithDeliveryModeTest() throws CommerceCartModificationException{
		
	CartModel model = new CartModel();	
	List<SplitEntryListWsDTO> splitEntries = new ArrayList<>();
	SplitEntryListWsDTO entry = new SplitEntryListWsDTO();
	entry.setDeliveryInstruction("deliveryInstruction");
	ZoneDeliveryModeWsDTO deliveryMode = new ZoneDeliveryModeWsDTO();
	deliveryMode.setCode("test");
	entry.setDeliveryMode(deliveryMode);
	List<SplitEntryWsDTO> sEntries =new ArrayList<>();
	SplitEntryWsDTO dto = new SplitEntryWsDTO();
	dto.setCode("test");
    sEntries.add(dto);
	entry.setSplitEntries(sEntries);
	splitEntries.add(entry);
	DeliveryModeModel deliveryModel = new DeliveryModeModel();
	List<AbstractOrderEntryModel> entries = new ArrayList<>();
	AbstractOrderEntryModel entryy = new AbstractOrderEntryModel();
	List<SplitEntryModel> entryList = new ArrayList<>();
	SplitEntryModel sem = new SplitEntryModel();
	List<DeliveryModeModel> deliveryListModes = new ArrayList<>();
	DeliveryModeModel dm = new DeliveryModeModel();
	dm.setCode("test-test");
	dm.setDeliveryPrice("12$");
	deliveryListModes.add(dm);
	sem.setDeliveryModes(deliveryListModes);
	sem.setCode("test-test");
	entryList.add(sem);
	entryy.setSplitEntry(entryList);
	entries.add(entryy);
	model.setEntries(entries);
	Mockito.when(deliveryService.getDeliveryModeForCode("test")).thenReturn(deliveryModel);
	Mockito.when(cartService.getSessionCart()).thenReturn(model);
    Mockito.doNothing().when(modelService).save(dm);
    DeliveryModeData deliveryModeData = Mockito.mock(DeliveryModeData.class);
	Mockito.when(deliveryModeConverter.convert(dm)).thenReturn(deliveryModeData);
    Mockito.doNothing().when(modelService).save(sem);
    Mockito.doNothing().when(modelService).save(model);
    Mockito.doNothing().when(modelService).refresh(model);
	facade.updateSpliEntriesWithDeliveryMode(splitEntries);	
		
}
	
	@Test
	public void updateSpliEntriesTest() throws CommerceCartModificationException{
		
		CartModel model = new CartModel(); 
		PK addressPK = PK.BIG_PK;
		AbstractOrderEntryModel entryModel = new AbstractOrderEntryModel();
		PriceData price=new PriceData();
		price.setValue(new BigDecimal("8.8"));
		SplitEntryModel splitEntryModel = new SplitEntryModel();
		GPSplitListWsDTO splitEntries = new GPSplitListWsDTO();
		List<GPSplitWsDTO> splitEntryList = new ArrayList<>();
		GPSplitWsDTO split = new GPSplitWsDTO();
		List<SplitEntryWsDTO> splitEntry = new ArrayList<>();
		SplitEntryWsDTO se = new SplitEntryWsDTO();
	    AddressWsDTO deliveryAddress = new AddressWsDTO();
		deliveryAddress.setId("id");
		se.setProductCode("test");
		se.setPrice(price);
		se.setDeliveryAddress(deliveryAddress);
		se.setQty("8");
		splitEntry.add(se);
		split.setSplitEntry(splitEntry);
		split.setEntryNumber(8);
		splitEntryList.add(split);
		splitEntries.setSplitEntry(splitEntryList);
		model.setCode("test");
	    Mockito.when(cartService.getSessionCart()).thenReturn(model);
		Mockito.when(facade.getCartEntryForNumber(cartModel, 8)).thenReturn(entryModel);
		Mockito.when(modelService.create(SplitEntryModel.class)).thenReturn(splitEntryModel);
		List<AddressModel> addresses = new ArrayList<>();
		AddressModel address = new AddressModel();
		addresses.add(address);
		model.setDeliveryAddress(null);
		Mockito.when(address.getPk()).thenReturn(addressPK);
		Mockito.when(deliveryService.getSupportedDeliveryAddressesForOrder(model, false)).thenReturn(addresses);
	    Mockito.doNothing().when(modelService).save(model);
	    Mockito.doNothing().when(modelService).refresh(model);
	    Mockito.doNothing().when(modelService).save(splitEntryModel);
	    Mockito.doNothing().when(modelService).save(entryModel);
		facade.updateSpliEntries(splitEntries,"current");
		
		}
	
	@Test
	public void getSignatureTest(){
		
		GPSignatureDTO signature = new GPSignatureDTO();
		String baseSiteId="gppro";
		signature.setBill_to_address_city("city");
		signature.setBill_to_address_country("country");
		signature.setBill_to_address_line1("test");
		signature.setBill_to_address_postal_code("postal");
		signature.setBill_to_address_state("state");
		signature.setBill_to_email("email@rtest.com");
		signature.setBill_to_forename("name");
		signature.setBill_to_phone("222222222");
		signature.setBill_to_surname("surname");
		signature.setPayment_method("debit");
		signature.setReference_number("reference_number");
		signature.setTransaction_type("transaction_type");
		signature.setSigned_field_names("signed_field_names");
		signature.setUnsigned_field_names("test");
		signature.setSigned_date_time("test");
		facade.getSignature(signature, baseSiteId);
		}
	
	@Test
	public void updateCartEntryTest() throws CommerceCartModificationException{
		
		CartModificationData data = new CartModificationData();
		CommerceCartModification modification = new CommerceCartModification();
		CartModel model = new CartModel();
		long entryNumber=8;
		long quantity=8;
		Map<String, String> additionalAttributes = new HashMap<>();
		List<AbstractOrderEntryModel> entries = new ArrayList<>();
		AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		entry.setEntryNumber(8);
		List<GPOrderEntryAttributeModel> addAtt = new ArrayList<>();
		GPOrderEntryAttributeModel add = new GPOrderEntryAttributeModel();
		add.setName("installedEntry");
		add.setValue("8");
		addAtt.add(add);
		entry.setAdditionalAttributes(addAtt);
		entries.add(entry);
		model.setEntries(entries);
		additionalAttributes.put("installed", "false");
		CommerceCartParameter parameter= new CommerceCartParameter();
		Mockito.when(commerceCartParameterConverter.convert(Mockito.any(AddToCartParams.class))).thenReturn(parameter);
		Mockito.when(cartService.getSessionCart()).thenReturn(model);
		given(commerceCartService.updateQuantityForCartEntry(Mockito.anyObject())).willReturn(modification);
	    Mockito.when(cartModificationConverter.convert(modification)).thenReturn(data);
		facade.updateCartEntry(entryNumber, quantity, additionalAttributes);
		
		}
	
	@Test
	public void getGuestUserAddresssesTest(){
		
		CartModel model = new CartModel();
		UserModel user = new UserModel();
		List<AddressModel> address = new ArrayList<>();
		AddressModel add = new AddressModel();
		List<AddressData> addressData = new ArrayList<>();
		address.add(add);
		user.setAddresses(address);
		model.setUser(user);
		Mockito.when(cartService.getSessionCart()).thenReturn(model);
		Mockito.when(addressConverter.convertAll(model.getUser().getAddresses())).thenReturn(addressData);
		facade.getGuestUserAddressses();
		
		
	}
}

package com.gp.commerce.facades.process.email.context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.facade.order.data.SplitEntryData;
import com.gp.commerce.facades.data.GPScheduleInstallationData;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.commercefacades.coupon.data.CouponData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.PromotionOrderEntryConsumedData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

@UnitTest
public class OrderNotificationEmailContextTest {
	@InjectMocks
	OrderNotificationEmailContext context = new OrderNotificationEmailContext();
	@Mock
	private Converter<OrderModel, OrderData> orderConverter;
    @Mock
	OrderProcessModel orderProcessModel;
    @Mock
	EmailPageModel emailPageModel;

	OrderData orderData = new OrderData();

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		 emailPageModel = new EmailPageModel();
		 orderProcessModel = new OrderProcessModel();
		OrderModel order = new OrderModel();
		orderProcessModel.setOrder(order);
		
		LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		emailPageModel = new EmailPageModel();
		ItemModelContextImpl itemModelContext = (ItemModelContextImpl) emailPageModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
		
		DeliveryModeData deliveryMode = new DeliveryModeData();
		deliveryMode.setDescription("test");
		orderData.setDeliveryMode(deliveryMode);
		GPScheduleInstallationData scheduleInstallation = new GPScheduleInstallationData();
		scheduleInstallation.setName("name");
		orderData.setScheduleInstallation(scheduleInstallation);
		List<OrderEntryData> entries = new ArrayList<>();
		OrderEntryData entry = new OrderEntryData();
		List<SplitEntryData> splitEntries = new ArrayList<>();
		SplitEntryData en = new SplitEntryData();
		AddressData deliveryAddress = new AddressData();
		deliveryAddress.setId("testId");
		en.setDeliveryAddress(deliveryAddress);
		splitEntries.add(en);
		entry.setSplitEntries(splitEntries);
		entry.setEntryNumber(3);
		entries.add(entry);
		orderData.setEntries(entries);
		Date created = new Date();
		orderData.setCreated(created);
		List<PromotionResultData> appliedProductPromotions = new ArrayList<>();
		PromotionResultData resultData = new PromotionResultData();
		List<PromotionOrderEntryConsumedData> consumedEntries = new ArrayList<>();
		PromotionOrderEntryConsumedData data = new PromotionOrderEntryConsumedData();
		data.setOrderEntryNumber(7);
		consumedEntries.add(data);
		resultData.setConsumedEntries(consumedEntries);
		resultData.setDescription("description");
		appliedProductPromotions.add(resultData);
		orderData.setAppliedProductPromotions(appliedProductPromotions);
		List<PromotionResultData> appliedOrderPromotions = new ArrayList<>();
		PromotionResultData otrderData = new PromotionResultData();
		List<CouponData> giveAwayCouponCodes = new ArrayList<>();
		CouponData couponData = new CouponData();
		giveAwayCouponCodes.add(couponData);
		otrderData.setGiveAwayCouponCodes(giveAwayCouponCodes);
		appliedOrderPromotions.add(otrderData);
		orderData.setAppliedOrderPromotions(appliedOrderPromotions);
		emailPageModel.setFromEmail("test@test.com", Locale.ENGLISH);
	}

	@Test
	public void initTest() {

		OrderNotificationEmailContext spy = Mockito.spy(new OrderNotificationEmailContext());
		Mockito.doNothing().when((AbstractEmailContext<OrderProcessModel>) spy).init(orderProcessModel, emailPageModel);
		Mockito.when(orderConverter.convert(orderProcessModel.getOrder())).thenReturn(orderData);
		context.init(orderProcessModel, emailPageModel);
	}

}

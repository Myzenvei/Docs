package com.gp.commerce.facades.process.email.context;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.orderprocessing.model.OrderModificationProcessModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.acceleratorservices.urlencoder.UrlEncoderService;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

@UnitTest
public class OrderPartiallyRefundedEmailContextTest {
	@InjectMocks
	OrderPartiallyRefundedEmailContext context = new OrderPartiallyRefundedEmailContext();
	@Mock
	private PriceDataFactory priceDataFactory;
	@Mock
	OrderModificationProcessModel orderProcessModel;
	@Mock
	EmailPageModel emailPageModel;
	PriceData priceData = new PriceData();
	@Mock
	private UrlEncoderService urlEncoderService;
	@Mock
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;
	

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		emailPageModel = new EmailPageModel();
		orderProcessModel = new OrderModificationProcessModel();
		OrderModel order = new OrderModel();
		BaseSiteModel site = new BaseSiteModel();
		order.setSite(site);
		orderProcessModel.setOrder(order);
		LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		ItemModelContextImpl itemModelContext = (ItemModelContextImpl) emailPageModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
		
	}

	@Test
	public void initTest() {
		List<OrderEntryData> data = new ArrayList<>();
		OrderEntryData order = new OrderEntryData();
		PriceData totalPrice = new PriceData();
		totalPrice.setValue(BigDecimal.TEN);
		order.setTotalPrice(totalPrice);
		data.add(order);
		OrderPartiallyRefundedEmailContext spy = Mockito.spy(new OrderPartiallyRefundedEmailContext());
		Mockito.doNothing().when((OrderPartiallyModifiedEmailContext) spy).init(orderProcessModel, emailPageModel);
		Mockito.doReturn(data).when((OrderPartiallyModifiedEmailContext) spy).getModifiedEntries();
		Mockito.when(urlEncoderService.getUrlEncodingPatternForEmail(Mockito.any(OrderModificationProcessModel.class)))
				.thenReturn("test");
		Mockito.when(priceDataFactory.create(Mockito.any(PriceDataType.class), Mockito.any(BigDecimal.class),
				Mockito.anyString())).thenReturn(priceData);

		Mockito.when(siteBaseUrlResolutionService.getWebsiteUrlForSite(Mockito.any(BaseSiteModel.class),
				Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString())).thenReturn("test");

		context.init(orderProcessModel, emailPageModel);

	}

}

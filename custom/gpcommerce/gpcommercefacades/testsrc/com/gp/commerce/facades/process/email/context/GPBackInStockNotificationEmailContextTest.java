package com.gp.commerce.facades.process.email.context;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestData;
import de.hybris.platform.customerinterestsservices.model.ProductInterestModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import com.gp.commerce.facades.process.email.context.BackInStockNotificationEmailContext;
import de.hybris.platform.stocknotificationservices.model.StockNotificationProcessModel;

@UnitTest
public class GPBackInStockNotificationEmailContextTest {

	@InjectMocks
	GPBackInStockNotificationEmailContext context = new GPBackInStockNotificationEmailContext();

	@Mock
	private Converter<ProductInterestModel, ProductInterestData> productInterestConverter;
    @Mock
	StockNotificationProcessModel stockNotificationProcessModel;
	@Mock
	EmailPageModel emailPageModel;
	ProductInterestData productInterestData = new ProductInterestData();
	 private BaseSiteModel site;

	@Before
	public void setUp(){
		emailPageModel = new EmailPageModel();
		stockNotificationProcessModel = new StockNotificationProcessModel();
		MockitoAnnotations.initMocks(this);
		ProductInterestModel value = new ProductInterestModel();
		CustomerModel customer = new CustomerModel();
		TitleModel title = new TitleModel();
		LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		ItemModelContextImpl storeItemModelContextt = (ItemModelContextImpl) title.getItemModelContext();
		storeItemModelContextt.setLocaleProvider(localeProvider);
		title.setName("test", Locale.ENGLISH);
		customer.setTitle(title);
		value.setCustomer(customer);
		stockNotificationProcessModel.setProductInterest(value);
		productInterestData.setEmailAddress("test@test.com");
		
		
		ItemModelContextImpl itemModelContext = (ItemModelContextImpl) emailPageModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
		emailPageModel.setFromEmail("test@test.com", Locale.ENGLISH);
		itemModelContext.setLocaleProvider(localeProvider);
		
	}

	@Test
	public void initTest(){
		GPBackInStockNotificationEmailContext spy = Mockito.spy(new GPBackInStockNotificationEmailContext());
		Mockito.doNothing().when((BackInStockNotificationEmailContext) spy).init(stockNotificationProcessModel, emailPageModel);
		Mockito.doReturn(productInterestData).when(productInterestConverter).convert(stockNotificationProcessModel.getProductInterest());
		context.init(stockNotificationProcessModel, emailPageModel);
	}
    
	@Test
	public void updateTitleTest(){
		final Locale locale = new Locale("en");
		context.updateTitle(stockNotificationProcessModel, locale);
	}
	
}

package com.gp.commerce.facades.process.email.context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

@UnitTest
public class CustomerEmailContextTest {
	@InjectMocks
	CustomerEmailContext context = new CustomerEmailContext();
	@Mock
	private Converter<UserModel, CustomerData> customerConverter;
    @Mock
	StoreFrontCustomerProcessModel storeFrontCustomerProcessModel;
    @Mock
	EmailPageModel emailPageModel = new EmailPageModel();

	@Before
	public void setUp() {
        
		storeFrontCustomerProcessModel= new StoreFrontCustomerProcessModel();
		emailPageModel= new EmailPageModel();
		MockitoAnnotations.initMocks(this);
		BaseSiteModel site = new BaseSiteModel();
		storeFrontCustomerProcessModel.setSite(site);

	}

	@Test
	public void initTest(){
		CustomerData data = new CustomerData();
		CustomerEmailContext spy = Mockito.spy(new CustomerEmailContext());
		Mockito.doNothing().when((AbstractEmailContext<StoreFrontCustomerProcessModel>) spy).init(storeFrontCustomerProcessModel,emailPageModel);
		Mockito.when(customerConverter.convert(Mockito.any(UserModel.class))).thenReturn(data);
		context.init(storeFrontCustomerProcessModel, emailPageModel);
	}

}

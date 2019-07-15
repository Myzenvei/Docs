package com.gp.commerce.facades.process.email.context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.util.Config;

@UnitTest
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Config.class })
public class GPOrderApprovalEmailContextTest {
	@InjectMocks
	GPOrderApprovalEmailContext context = new GPOrderApprovalEmailContext();
	@Mock
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;
	
	OrderProcessModel businessProcessModel = new OrderProcessModel();
	EmailPageModel abstractPageModel = new EmailPageModel();
	
	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(Config.class);
		OrderModel order = new OrderModel();
		order.setCode("test");
		B2BUnitModel unit = new B2BUnitModel();
		order.setUnit(unit);
		UserModel user = new UserModel();
		user.setName("test");
		order.setUser(user);
		businessProcessModel.setOrder(order);
		B2BCustomerModel admin = new B2BCustomerModel();
		admin.setEmail("test@test.com");
		businessProcessModel.setAdminDetails(admin);
		context.setSiteBaseUrlResolutionService(siteBaseUrlResolutionService);
	}

	@Test
	public void initTest() {
		GPOrderApprovalEmailContext spy = Mockito.spy(new GPOrderApprovalEmailContext());
		Mockito.doNothing().when((OrderNotificationEmailContext) spy).init(businessProcessModel, abstractPageModel);
		String baseUrl = "test";
		Mockito.when(siteBaseUrlResolutionService.getWebsiteUrlForSite(Mockito.any(BaseSiteModel.class), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyString())).thenReturn(baseUrl);
		PowerMockito.mock(Config.class);
		
		Mockito.when(Config.getParameter("b2b.approval.admin.email")).thenReturn("b2badmin@gppro.com");
		context.init(businessProcessModel, abstractPageModel);
	}

}

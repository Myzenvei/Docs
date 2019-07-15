package com.gp.commerce.core.event;

import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;

import com.gp.commerce.core.model.GPItemProcessModel;
import com.gp.commerce.core.services.event.GPEmailItemEvent;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.site.strategies.SiteChannelValidationStrategy;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.cluster.ClusterService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.tenant.TenantService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class GPEmailItemEventListenerTest {

	@Mock
	private EmailService emailService;
	@Mock
	private MessageSource messageSource;
	@Mock
	private I18NService i18nService;
	@Mock
	private UserService userService;

	private GPEmailItemEvent itemEvent;

	@Mock
	private B2BCustomerModel customer;

	@Mock
	private BusinessProcessService businessProcessService;

	@Mock
	private ClusterService clusterService;

	@Mock
	private ModelService modelService;

	@Mock
	private SiteChannelValidationStrategy siteChannelValidationStrategy;
	@Mock
	private TenantService tenantService;

	@Mock
	private GPItemProcessModel itemProcessModel;

	@InjectMocks
	private GPEmailItemEventListener gpEmailItemEventListener = new GPEmailItemEventListener();

	@Before
	public void setup() {

		gpEmailItemEventListener.setBusinessProcessService(businessProcessService);
		gpEmailItemEventListener.setClusterService(clusterService);
		gpEmailItemEventListener.setEmailService(emailService);
		gpEmailItemEventListener.setI18nService(i18nService);
		gpEmailItemEventListener.setMessageSource(messageSource);
		gpEmailItemEventListener.setModelService(modelService);
		gpEmailItemEventListener.setSiteChannelValidationStrategy(siteChannelValidationStrategy);
		gpEmailItemEventListener.setTenantService(tenantService);

		ReflectionTestUtils.setField(gpEmailItemEventListener, "userService", userService);
		
		Mockito.when(businessProcessService.createProcess(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(itemProcessModel);
		
		itemEvent=new GPEmailItemEvent();
		
		
		BaseSiteModel site = new BaseSiteModel();
		site.setChannel(SiteChannel.B2C);
		itemEvent.setSite(site);
		itemEvent.setAddress(Mockito.mock(AddressModel.class));
		itemEvent.setCustomer(customer);
		itemEvent.setCustomerModel(customer);
		itemEvent.setBackOfficeUser(false);
		
		itemEvent.setLanguage(Mockito.mock(LanguageModel.class));
		itemEvent.setCurrency(Mockito.mock(CurrencyModel.class));
		itemEvent.setBaseStore(Mockito.mock(BaseStoreModel.class));
		itemEvent.setToEmails(Collections.singletonList("gpcustomer@gp.com"));
		itemEvent.setAddress(Mockito.mock(AddressModel.class));
		itemEvent.setInvitedCustomer(Mockito.mock(CustomerModel.class));
		itemEvent.setBccEmail("gpCust@gp.com");
		itemEvent.setEmailSubject("email sent");
		itemEvent.setAdminModel(Mockito.mock(CustomerModel.class));
		itemEvent.setToken("AS456S321IDGE");

	}

	@Test
	public void onSiteEventTest() {
		gpEmailItemEventListener.onSiteEvent(itemEvent);
		Mockito.verify(businessProcessService).startProcess(itemProcessModel);
		Mockito.verify(modelService).save(itemProcessModel);
		;

	}

	@Test
	public void getSiteChannelForEventTest() {
		assertTrue(gpEmailItemEventListener.getSiteChannelForEvent(itemEvent).getCode()
				.equalsIgnoreCase(SiteChannel.B2C.getCode()));
	}

}

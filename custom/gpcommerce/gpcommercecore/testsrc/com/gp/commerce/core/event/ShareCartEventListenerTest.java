package com.gp.commerce.core.event;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import com.gp.commerce.core.services.event.ShareCartEvent;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.site.strategies.SiteChannelValidationStrategy;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.model.order.CartModel;
import com.gp.commerce.core.model.CartProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.cluster.ClusterService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.tenant.TenantService;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ShareCartEventListenerTest {
	
	@Mock
	private EmailService emailService;
	@Mock
	private MessageSource messageSource;
	@Mock
	private I18NService i18nService;
	
	@Mock
	private ModelService modelService;
	
	@Mock
	private BusinessProcessService businessProcessService;
	
	@Mock
	private ClusterService clusterService;
	@Mock
	private TenantService tenantService;
	
	@Mock
	private SiteChannelValidationStrategy siteChannelValidationStrategy;
	
	private ShareCartEvent cartEvent;
	
	@Mock
	private CartProcessModel cartProcessModel;
	
	@Mock
	private CartModel cartModel;
	
	@InjectMocks
	private ShareCartEventListener shareCartEventListener=new ShareCartEventListener();
	
	@Before
	public void setup() {

		shareCartEventListener.setBusinessProcessService(businessProcessService);
		shareCartEventListener.setEmailService(emailService);
		shareCartEventListener.setClusterService(clusterService);
		shareCartEventListener.setI18nService(i18nService);
		shareCartEventListener.setModelService(modelService);
		shareCartEventListener.setTenantService(tenantService);
		shareCartEventListener.setSiteChannelValidationStrategy(siteChannelValidationStrategy);
		shareCartEventListener.setMessageSource(messageSource);
	
		
		Mockito.when(businessProcessService.createProcess(Mockito.anyString(), Mockito.anyString()))
		.thenReturn(cartProcessModel);
		
		cartEvent=new ShareCartEvent(cartProcessModel);
		BaseSiteModel site = new BaseSiteModel();
		site.setChannel(SiteChannel.B2C);
		
		
		cartEvent.setToEmail("customer@gp.com");
		
		Mockito.when(cartModel.getSite()).thenReturn(site);
		
		Mockito.when(cartProcessModel.getCart()).thenReturn(cartModel);
		
	}
	
	@Test
	public void onSiteEventTest() {
		
		shareCartEventListener.onSiteEvent(cartEvent);
		Mockito.verify(businessProcessService).startProcess(cartProcessModel);
		Mockito.verify(modelService).save(cartProcessModel);
		assertNull(cartEvent.getProcess());
	}

	@Test
	public void getSiteChannelForEventTest() {
		assertTrue(shareCartEventListener.getSiteChannelForEvent(cartEvent).getCode()
				.equalsIgnoreCase(SiteChannel.B2C.getCode()));
	}
}

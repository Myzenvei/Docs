/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.event;

import static org.junit.Assert.assertTrue;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import com.gp.commerce.core.services.event.UpdateProfileEvent;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.site.strategies.SiteChannelValidationStrategy;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.cluster.ClusterService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.tenant.TenantService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class UpdateProfileEventListenerTest {

	@Mock
	private EmailService emailService;
	@Mock
	private MessageSource messageSource;
	@Mock
	private I18NService i18nService;
	@Mock
	private UserService userService;

	private UpdateProfileEvent updateProfileEvent;

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
	private StoreFrontCustomerProcessModel storeFrontCustomerProcessModel;

	@InjectMocks
	private UpdateProfileEventListner updateProfileEventListner = new UpdateProfileEventListner();

	@Before
	public void setup() {

		updateProfileEventListner.setBusinessProcessService(businessProcessService);
		updateProfileEventListner.setClusterService(clusterService);
		
		updateProfileEventListner.setModelService(modelService);
		updateProfileEventListner.setSiteChannelValidationStrategy(siteChannelValidationStrategy);
		updateProfileEventListner.setTenantService(tenantService);

		
		Mockito.when(businessProcessService.createProcess(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(storeFrontCustomerProcessModel);
		
		updateProfileEvent=new UpdateProfileEvent();
		
		
		BaseSiteModel site = new BaseSiteModel();
		site.setChannel(SiteChannel.B2C);
		updateProfileEvent.setSite(site);
		updateProfileEvent.setCustomer(customer);
		
		updateProfileEvent.setLanguage(Mockito.mock(LanguageModel.class));
		updateProfileEvent.setCurrency(Mockito.mock(CurrencyModel.class));
		updateProfileEvent.setBaseStore(Mockito.mock(BaseStoreModel.class));

	}

	@Test
	public void onSiteEventTest() {
		updateProfileEventListner.onSiteEvent(updateProfileEvent);
		Mockito.verify(businessProcessService).startProcess(storeFrontCustomerProcessModel);
		Mockito.verify(modelService).save(storeFrontCustomerProcessModel);
	}

	@Test
	public void getSiteChannelForEventTest() {
		assertTrue(updateProfileEventListner.getSiteChannelForEvent(updateProfileEvent).getCode()
				.equalsIgnoreCase(SiteChannel.B2C.getCode()));
	}

}

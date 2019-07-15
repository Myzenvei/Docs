package com.gp.commerce.core.event;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;

import com.gp.commerce.core.services.event.ShareWishlistEvent;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.site.strategies.SiteChannelValidationStrategy;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import com.gp.commerce.core.model.WishlistProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.cluster.ClusterService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.tenant.TenantService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ShareWishlistEventListenerTest {
	
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
	private BaseSiteService baseSiteService;

	@Mock
	private B2BCustomerModel customer;
	
	@Mock
	private LanguageModel language;
	
	@Mock
	private SiteChannelValidationStrategy siteChannelValidationStrategy;
	
	private ShareWishlistEvent shareEvent;
	
	@Mock
	private WishlistProcessModel wishListProcess;
	
	@Mock
	private Wishlist2Model wishlistModel;
	
	@InjectMocks
	private ShareWishlistEventListener shareWishlistEventListener=new ShareWishlistEventListener();
	
	private BaseStoreModel baseStore;
	private CurrencyModel currency;
	private BaseSiteModel site;
	@Before
	public void setup() {

		shareWishlistEventListener.setBusinessProcessService(businessProcessService);
		shareWishlistEventListener.setEmailService(emailService);
		shareWishlistEventListener.setClusterService(clusterService);
		shareWishlistEventListener.setI18nService(i18nService);
		shareWishlistEventListener.setModelService(modelService);
		shareWishlistEventListener.setTenantService(tenantService);
		shareWishlistEventListener.setSiteChannelValidationStrategy(siteChannelValidationStrategy);
		shareWishlistEventListener.setMessageSource(messageSource);
		ReflectionTestUtils.setField(shareWishlistEventListener, "baseSiteService", baseSiteService);
		
		site = new BaseSiteModel();
		site.setChannel(SiteChannel.B2C);
		
		baseStore = new BaseStoreModel();
		currency = new CurrencyModel();
		shareEvent = new ShareWishlistEvent(wishListProcess);
		shareEvent.setToEmail("customer@gp.com");
		shareEvent.setBaseStore(baseStore);
		shareEvent.setCustomer(customer);
		shareEvent.setLanguage(language);
		shareEvent.setSite(site);
		shareEvent.setCurrency(currency);
		Mockito.when(businessProcessService.createProcess(Mockito.anyString(), Mockito.anyString()))
		.thenReturn(wishListProcess);
		
		Mockito.when(wishlistModel.getWishlistUid()).thenReturn("WE3456789");
		
		Mockito.when(baseSiteService.getCurrentBaseSite()).thenReturn(site);
		Mockito.when(wishListProcess.getWishlist()).thenReturn(wishlistModel);
		
	}
	
	@Test
	public void onSiteEventTest() {
		
		assertEquals(shareEvent.getCustomer(),customer);
		assertEquals(shareEvent.getBaseStore(),baseStore);
		assertEquals(shareEvent.getLanguage(),language);
		assertEquals(shareEvent.getCurrency(),currency);
		assertEquals(shareEvent.getSite(),site);
		assertNull(shareEvent.getProcess());		
		
		shareWishlistEventListener.onSiteEvent(shareEvent);
		Mockito.verify(businessProcessService).startProcess(wishListProcess);
		Mockito.verify(modelService).save(wishListProcess);

	}

	@Test
	public void getSiteChannelForEventTest() {
		assertTrue(shareWishlistEventListener.getSiteChannelForEvent(shareEvent).getCode()
				.equalsIgnoreCase(SiteChannel.B2C.getCode()));
	}
	
	@Test
	public void onEventTest() {
		shareWishlistEventListener.onEvent(shareEvent);
		Mockito.verify(businessProcessService).startProcess(wishListProcess);
		Mockito.verify(modelService).save(wishListProcess);
		;

	}

}

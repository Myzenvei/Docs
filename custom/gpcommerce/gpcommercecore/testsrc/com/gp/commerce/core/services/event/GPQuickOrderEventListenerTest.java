package com.gp.commerce.core.services.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.gp.commerce.core.model.QuickOrderEmailProcessModel;
import com.gp.commerce.core.services.GPCMSSiteService;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class GPQuickOrderEventListenerTest {

	@InjectMocks
	private GPQuickOrderEventListener gpQuickOrderEventListener = new GPQuickOrderEventListener();

	private GPQuickOrderEvent event;

	@Mock
	private GPCMSSiteService cmsSiteService;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private BaseStoreService baseStoreService;
	@Mock
	private BusinessProcessService businessProcessService;
	@Mock
	private ModelService modelService;
	@Mock
	private QuickOrderEmailProcessModel quickOrderEmailProcessModel;
	
	B2BCustomerModel customer;
	@Mock
	private UserService userService;
	
	private BaseStoreModel baseStore;
	private CurrencyModel currency;
	private BaseSiteModel site;
	private LanguageModel language;
	
	@Before
	public void setup()
	{

		gpQuickOrderEventListener.setBusinessProcessService(businessProcessService);
		Mockito.when(businessProcessService.createProcess(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(quickOrderEmailProcessModel);
		
		event = new GPQuickOrderEvent();
		site = new BaseSiteModel();
		site.setChannel(SiteChannel.B2C);
		
		baseStore = new BaseStoreModel();
		currency = new CurrencyModel();
		language = new LanguageModel();
		customer = new B2BCustomerModel();
		
		event.setSite(site);
		event.setCustomer(customer);
		event.setLanguage(language);
		event.setCurrency(currency);
		event.setBaseStore(baseStore);
		event.setWishlistModel(Mockito.mock(Wishlist2Model.class));
		event.setWishlistUid("WishlistUid");
		event.setEmailIds("Email@deloitte.com");
		
	}
	@Test
	public void onSiteEventTest() {
		assertEquals(event.getWishlistUid(),"WishlistUid");
		gpQuickOrderEventListener.onSiteEvent(event);
		Mockito.verify(businessProcessService).startProcess(quickOrderEmailProcessModel);
	}
	@Test
	public void getSiteChannelForEventTest() {
		assertTrue(gpQuickOrderEventListener.getSiteChannelForEvent(event).getCode()
				.equalsIgnoreCase(SiteChannel.B2C.getCode()));
	}
}

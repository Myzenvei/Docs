package com.gp.commerce.core.event.listener;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.gp.commerce.core.event.GPTaxExemptionSubmitReviewEvent;
import com.gp.commerce.core.model.TaxExemptionSubmitReviewEmailProcessModel;
import com.gp.commerce.core.services.GPCMSSiteService;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class GPTaxExemptionSubmitReviewEventListenerTest {
	@InjectMocks
	GPTaxExemptionSubmitReviewEventListener gpTaxExemptionSubmitReviewEventListener= new GPTaxExemptionSubmitReviewEventListener();
	
	@Mock
	private GPCMSSiteService cmsSiteService;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private BaseStoreService baseStoreService;
	@Mock
	GPTaxExemptionSubmitReviewEvent event;
	@Mock
	private BusinessProcessService businessProcessService;
	@Mock
	private ModelService modelService;
	@Mock
	private TaxExemptionSubmitReviewEmailProcessModel taxExemptionModel;
	@Mock
	B2BCustomerModel customer;
	@Mock
	private UserService userService;
	@Before
	public void setup()
	{
	
		ReflectionTestUtils.setField(gpTaxExemptionSubmitReviewEventListener, "businessProcessService", businessProcessService);
		gpTaxExemptionSubmitReviewEventListener.setCmsSiteService(cmsSiteService);
		gpTaxExemptionSubmitReviewEventListener.setBaseSiteService(baseSiteService);
		gpTaxExemptionSubmitReviewEventListener.setBaseStoreService(baseStoreService);
		gpTaxExemptionSubmitReviewEventListener.setBusinessProcessService(businessProcessService);
		gpTaxExemptionSubmitReviewEventListener.setModelService(modelService);
		Mockito.when(businessProcessService.createProcess(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(taxExemptionModel);
		BaseSiteModel site = Mockito.mock(BaseSiteModel.class);
		Mockito.when(event.getSite()).thenReturn(site);
		Mockito.when(event.getCustomer()).thenReturn(customer);
		Mockito.when(event.getLanguage()).thenReturn(Mockito.mock(LanguageModel.class));
		Mockito.when(event.getCurrency()).thenReturn(Mockito.mock(CurrencyModel.class));
		Mockito.when(event.getBaseStore()).thenReturn(Mockito.mock(BaseStoreModel.class));
		List<MediaModel> medias = new ArrayList<>();
		MediaModel media = Mockito.mock(MediaModel.class);
		medias.add(media);
		Mockito.when(event.getTaxExemptionDocumentList()).thenReturn(medias);
		Mockito.when(baseSiteService.getCurrentBaseSite()).thenReturn(site);
		Mockito.when(site.getChannel()).thenReturn(SiteChannel.B2C);
	}
	@Test
	public void onSiteEventTest() {
		gpTaxExemptionSubmitReviewEventListener.onSiteEvent(event);
		Mockito.verify(businessProcessService).startProcess(taxExemptionModel);
		Mockito.verify(modelService).save(taxExemptionModel);
	}
	
	@Test
	public void getSiteChannelForEventTest() {
		assertTrue(gpTaxExemptionSubmitReviewEventListener.getSiteChannelForEvent(event).getCode()
				.equalsIgnoreCase(SiteChannel.B2C.getCode()));
	}

}

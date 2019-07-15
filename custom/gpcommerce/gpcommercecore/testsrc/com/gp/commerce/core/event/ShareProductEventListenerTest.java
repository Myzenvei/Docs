package com.gp.commerce.core.event;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import com.gp.commerce.core.model.ShareProductEmailProcessModel;
import com.gp.commerce.core.util.ShareProductEvent;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.site.strategies.SiteChannelValidationStrategy;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.cluster.ClusterService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.tenant.TenantService;
import de.hybris.platform.site.BaseSiteService;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ShareProductEventListenerTest {

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

	@Mock
	ShareProductEvent event;

	@Mock
	ShareProductEmailProcessModel shareProcessModel;

	@Mock
	BaseSiteService baseSiteService;

	@Mock
	CartModel cartModel;

	@InjectMocks
	ShareProductEventListener shareProductEventListener = new ShareProductEventListener();

	@Before
	public void setup() {

		shareProductEventListener.setBusinessProcessService(businessProcessService);
		shareProductEventListener.setClusterService(clusterService);
		shareProductEventListener.setModelService(modelService);
		shareProductEventListener.setTenantService(tenantService);
		shareProductEventListener.setBaseSiteService(baseSiteService);

		Mockito.when(businessProcessService.createProcess(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(shareProcessModel);
		BaseSiteModel site = Mockito.mock(BaseSiteModel.class);
		Mockito.when(event.getAddLink()).thenReturn(true);
		Mockito.when(event.getAttachPDF()).thenReturn(true);
		Mockito.when(event.getRecipientEmails()).thenReturn(Collections.singletonList("customer@gp.com"));
		Mockito.when(event.getSenderEmail()).thenReturn("customer@gp.com");
		Mockito.when(event.getSenderMessage()).thenReturn("Message To be sent");
		Mockito.when(event.getSenderName()).thenReturn("Customer");
		Mockito.when(event.getSubject()).thenReturn("Share Product");
//		Mockito.when(event.getPdtCode()).thenReturn("Product123");
//		Mockito.when(event.getProductPageURL()).thenReturn("http://ProductPageUrl");
//		Mockito.when(event.getEmailAttachmentList())
//				.thenReturn(Collections.singletonList(Mockito.mock(EmailAttachmentModel.class)));
//		Mockito.when(baseSiteService.getCurrentBaseSite()).thenReturn(site);
//		Mockito.when(event.getPdtName()).thenReturn("Product123");
//		Mockito.when(event.getPdtDescription()).thenReturn("Product123 Description");
//		Mockito.when(event.getProduct()).thenReturn(Mockito.mock(ProductModel.class));

	}

	@Test
	public void onEventTest() {
		shareProductEventListener.onEvent(event);
		Mockito.verify(businessProcessService).startProcess(shareProcessModel);
		Mockito.verify(modelService).save(shareProcessModel);
		;

	}

}

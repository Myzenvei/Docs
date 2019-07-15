package com.gp.commerce.facades.process.email.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.model.TaxExemptionSubmitReviewEmailProcessModel;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

@UnitTest
public class GPTaxExemptionSubmitReviewEmailContextTest {
	
	@InjectMocks
	GPTaxExemptionSubmitReviewEmailContext context = new GPTaxExemptionSubmitReviewEmailContext();
	@Mock
	TaxExemptionSubmitReviewEmailProcessModel businessProcessModel;
	@Mock
	EmailPageModel emailPageModel;
	@Mock
	private MediaService mediaService;
	@Mock
	private EmailService emailService;
	
	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		businessProcessModel = new TaxExemptionSubmitReviewEmailProcessModel();
		emailPageModel = new EmailPageModel();
		context.setMediaService(mediaService);
		context.setEmailService(emailService);
        LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
        ItemModelContextImpl itemModelContext = (ItemModelContextImpl) emailPageModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
		businessProcessModel.setToEmail("test@test.com");
		List<MediaModel> medias = new ArrayList<>();
		MediaModel media = Mockito.mock(MediaModel.class);
		media.setCode("test.pdf");
		media.setMime("pdf");
		medias.add(media);
		businessProcessModel.setTaxExemptionDocuments(medias);
        emailPageModel.setFromEmail("test@tes.com", Locale.ENGLISH);

	}
	
	@Test
	public void testInit(){
		
		GPTaxExemptionSubmitReviewEmailContext spy = Mockito.spy(new GPTaxExemptionSubmitReviewEmailContext());
		Mockito.doNothing().when((AbstractEmailContext<TaxExemptionSubmitReviewEmailProcessModel>) spy).init(businessProcessModel,
				emailPageModel);
		context.init(businessProcessModel, emailPageModel);	
		
	}

}

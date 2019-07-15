package com.gp.commerce.facades.shareproduct.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.forms.ShareProductForm;
import com.gp.commerce.core.services.GPShareProductService;

import de.hybris.bootstrap.annotations.UnitTest;

@UnitTest
public class GPDefaultShareProductFacadeTest {

	@InjectMocks
	GPDefaultShareProductFacade shareProductfacade=new GPDefaultShareProductFacade();
	
     @Mock
	 GPShareProductService gpShareProductService;
	ShareProductForm form= new ShareProductForm();
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		shareProductfacade.setGpShareProductService(gpShareProductService);
		List<String> recipientEmails = Arrays.asList("test@gmail.com");
		form.setRecipientEmails(recipientEmails);
		form.setSenderEmail("test@ymail.com");
        form.setSenderName("abc");
        form.setSubject("share product email");
        form.setSenderMessage("email test");
//        form.setProductPageURL("/product");
//        form.setAttachPDF(Boolean.TRUE);
//        form.setPdtCode("pdtCode");
	}

	@Test
	public void testShareProduct() {
		shareProductfacade.setGpShareProductService(gpShareProductService);
		shareProductfacade.shareProduct(form);
		assertNotNull(form);
	}

}

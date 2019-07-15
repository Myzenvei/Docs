package com.gp.commerce.core.services.impl;

import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.gp.commerce.core.dao.GPConsignmentDao;
import com.gp.commerce.core.dao.GPFAQDao;
import com.gp.commerce.core.model.FAQModel;

import de.hybris.bootstrap.annotations.UnitTest;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class GPDefaultFAQServiceTest {

	@Mock
	private GPFAQDao faqDao;

	@InjectMocks
	GPDefaultFAQService gpDefaultFAQService = new GPDefaultFAQService();

	@Mock
	FAQModel faq;

	@Before
	public void setup() {

		ReflectionTestUtils.setField(gpDefaultFAQService, "faqDao", faqDao);
	}

	@Test
	public void getFAQsTest() {
		Mockito.when(faqDao.getFAQs()).thenReturn(Collections.singletonList(faq));

		assertTrue(!gpDefaultFAQService.getFAQs().isEmpty());
		assertTrue(gpDefaultFAQService.getFAQs().get(0).equals(faq));
	}

	@Test
	public void getFAQsEmptyTest() {

		Mockito.when(faqDao.getFAQs()).thenReturn(Collections.emptyList());
		assertTrue(gpDefaultFAQService.getFAQs().isEmpty());

	}
}

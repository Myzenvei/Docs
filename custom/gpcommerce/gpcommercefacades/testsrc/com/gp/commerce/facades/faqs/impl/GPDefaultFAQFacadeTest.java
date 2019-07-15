package com.gp.commerce.facades.faqs.impl;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.model.FAQModel;
import com.gp.commerce.core.services.GPFAQService;
import com.gp.commerce.facades.populators.GPFAQPopulator;
import com.gp.commerce.gpcommerceaddon.facades.GPFAQData;


@UnitTest
public class GPDefaultFAQFacadeTest
{

	@Mock
	private GPFAQService faqService;

	@Mock
	private GPFAQPopulator faqPopulator;

	@InjectMocks
	private GPDefaultFAQFacade gpDefaultFAQFacadeTest;

	private List<FAQModel> faqs;

	private List<GPFAQData> faqDataList;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetFaqs()
	{
		faqs = new ArrayList<>();
		final FAQModel faq = new FAQModel();
		faq.setQuestion("q1");
		faq.setAnswer("A1");
		faqs.add(faq);
		final GPFAQData faqData = new GPFAQData();
		faqData.setQuestion("q2");
		faqData.setAnswer("A2");
		when(faqService.getFAQs()).thenReturn(faqs);
		faqDataList = new ArrayList<>();
		faqDataList.add(faqData);
		doNothing().when(faqPopulator).populate(faq, faqData);
		final List faqDlist = gpDefaultFAQFacadeTest.getFaqs();
		assertTrue(faqDlist.size() == 1);
		assertTrue(((GPFAQData) faqDlist.get(0)).getQuestion() == null);
		assertTrue(((GPFAQData) faqDlist.get(0)).getAnswer() == null);

	}
}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.model.FAQModel;
import com.gp.commerce.gpcommerceaddon.facades.GPFAQData;

import de.hybris.bootstrap.annotations.UnitTest;
/**
 * @author akapandey
 */

@UnitTest
public class GPFAQPopulatorTest {

	private static final String ANSWER = "Solution";
	private static final String QUESTION = "Question";
	
	@InjectMocks
	GPFAQPopulator populator = new GPFAQPopulator();
	
	FAQModel source = new FAQModel();
	GPFAQData target = new GPFAQData();
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		source.setAnswer(ANSWER);
		source.setQuestion(QUESTION);
	}
	
	@Test
	public void testPopulate()
	{
		populator.populate(source, target);
		Assert.assertEquals(target.getAnswer(), source.getAnswer());
		Assert.assertEquals(target.getQuestion(), source.getQuestion());
	}
	
}

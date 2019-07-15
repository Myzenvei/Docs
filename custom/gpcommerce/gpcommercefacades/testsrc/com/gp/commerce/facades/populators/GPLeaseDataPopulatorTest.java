/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;
/**
 * @author akapandey
 */
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.model.GPEndUserLegalTermsModel;
import com.gp.commerce.facade.order.data.LeaseAgreementData;

import de.hybris.bootstrap.annotations.UnitTest;

@UnitTest
public class GPLeaseDataPopulatorTest {

	@InjectMocks
	GPLeaseDataPopulator populator = new GPLeaseDataPopulator();
	GPEndUserLegalTermsModel source = new GPEndUserLegalTermsModel();
	LeaseAgreementData target = new LeaseAgreementData();
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		source.setCountry("USA");
		source.setLegalLanguage("English");
		source.setLegalTermName("Legal");
		source.setLegalTermsText("LegalTerms");
		source.setVersion(190);
		source.setId("A109");
	}
	
	@Test
	public void testPopulate()
	{
		populator.populate(source, target);
		Assert.assertEquals(target.getCountry(), source.getCountry());
		Assert.assertEquals(target.getLegalLanguage(), source.getLegalLanguage());
		Assert.assertEquals(target.getLegalTermName(), source.getLegalTermName());
		Assert.assertEquals(target.getLegalTermsText(), source.getLegalTermsText());
		Assert.assertEquals(target.getVersion(), source.getVersion());
		Assert.assertEquals(target.getLegalTermsId(), source.getId());
	}
	
	
	
}

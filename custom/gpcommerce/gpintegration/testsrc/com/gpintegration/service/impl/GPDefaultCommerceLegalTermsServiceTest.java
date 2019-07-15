package com.gpintegration.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;


import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.leaseagreement.dto.GPLegalTermResponseWrapperDTO;

import de.hybris.bootstrap.annotations.UnitTest;

@UnitTest
public class GPDefaultCommerceLegalTermsServiceTest {
	
	@Mock
	private GPDefaultCommerceLegalTermsService gpCommerceLegalTermsService;
	
	@InjectMocks
	private GPLegalTermResponseWrapperDTO legalTermsResponse;
	
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void createLeaseAgreementTest() throws GPIntegrationException {
		gpCommerceLegalTermsService.fetchLegalTerms();
	}
	
	
	

}

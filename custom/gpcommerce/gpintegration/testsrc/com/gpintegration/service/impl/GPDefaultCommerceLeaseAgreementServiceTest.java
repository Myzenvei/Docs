package com.gpintegration.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.leaseagreement.dto.GPLeaseAgreementDTO;
import com.gpintegration.leaseagreement.dto.GPLeaseAgreementResponseDTO;

import de.hybris.bootstrap.annotations.UnitTest;

@UnitTest
public class GPDefaultCommerceLeaseAgreementServiceTest {
	
	@Mock
	private GPDefaultCommerceLeaseAgreementService gpCommerceLeaseAgreementService;
	
	@InjectMocks
	private GPLeaseAgreementResponseDTO leaseAgreementResponse;
	
	@InjectMocks
	private GPLeaseAgreementDTO leaseAgreementRequest;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void createLeaseAgreementTest() throws GPIntegrationException {
		when(gpCommerceLeaseAgreementService.createLeaseAgreement((Mockito.anyObject()))).thenReturn(leaseAgreementResponse);
		assertNotNull(gpCommerceLeaseAgreementService.createLeaseAgreement(leaseAgreementRequest));
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=GPIntegrationException.class)
	public void createLeaseAgreementExceptionTest() throws GPIntegrationException {
		when(gpCommerceLeaseAgreementService.createLeaseAgreement((Mockito.anyObject()))).thenThrow(GPIntegrationException.class);
		assertNotNull(gpCommerceLeaseAgreementService.createLeaseAgreement(leaseAgreementRequest));
	}
	
	

}

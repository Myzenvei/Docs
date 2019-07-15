package com.gpintegration.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.gp.businessobjects.RealTimeServices;
import com.gp.businessobjects.service.addr.cleanse.output.Record;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.service.impl.GPDefaultAddressVerificationService;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.commercefacades.user.data.CountryData;
import org.apache.commons.configuration.Configuration;


@UnitTest
public class GPDefaultAddressVerificationServiceTest {

	GPDefaultAddressVerificationService addressVerificationService = null;
	@Mock
	private RealTimeServices realTimeServices;
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private Configuration configuration;
	@Mock 
	com.gp.businessobjects.service.addr.suggestions.output.DataSet suggestionsDataSetOutput;
	@Mock 
	com.gp.businessobjects.service.addr.cleanse.output.DataSet dataSetOutput;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		addressVerificationService = new GPDefaultAddressVerificationService();
		dataSetOutput = new com.gp.businessobjects.service.addr.cleanse.output.DataSet();
		addressVerificationService.setConfigurationService(configurationService);

	}

	@After
	public void tearDown() {
		addressVerificationService = null;

	}

	@Test
	public void testVerifyAddress() throws GPIntegrationException {

		Record record = new Record();
		record.setCITY1("Hopkins");
		record.setCOUNTRY("US");
		record.setHOUSENUM1("8316");
		record.setSTREET("Jefferson St");
		record.setREGION("MN");
		record.setREGION2("053");
		record.setPOSTCODE1("55343");
		record.setINFOCODE("3030");
		record.setINFOCODEPO("1030");
		record.setSTATUSCODE("S0000");
		record.setSTATUSCODEPO("S0000");
		List<Record> records = new ArrayList<Record>();
		records.add(record);
		String correctionMessage = "test";
		dataSetOutput.setRecord(records);

		GPDefaultAddressVerificationService spyTemp = Mockito.spy(addressVerificationService);
		Mockito.doReturn(realTimeServices).when(spyTemp).getRealTimeServices();
		when(realTimeServices.addrCleanseHybris(Mockito.anyObject())).thenReturn(dataSetOutput);
		when(configurationService.getConfiguration()).thenReturn(configuration);

		Mockito.when(configuration.getString(String.valueOf(Mockito.anyString()))).thenReturn(correctionMessage);
		AddressData addressData = spyTemp.verifyAddress(stubAddressData());
		assertNotNull(addressData);

	}

	@Test(expected = GPIntegrationException.class)
	public void testVerifyAddressWithException() throws GPIntegrationException {

		com.gp.businessobjects.service.addr.cleanse.output.DataSet dataSetOutput = new com.gp.businessobjects.service.addr.cleanse.output.DataSet();
		Record record = new Record();
		record.setCITY1("Hopkins");
		record.setCOUNTRY("US");
		record.setHOUSENUM1("8316");
		record.setSTREET("Jefferson St");
		record.setREGION("MN");
		record.setREGION2("053");
		record.setPOSTCODE1("55343");
		record.setINFOCODE("3030");
		record.setINFOCODEPO("1030");
		record.setSTATUSCODE("S0000");
		record.setSTATUSCODEPO("S0000");
		List<Record> records = new ArrayList<Record>();
		records.add(record);

		dataSetOutput.setRecord(records);

		GPDefaultAddressVerificationService spyTemp = Mockito.spy(addressVerificationService);
		Mockito.doThrow(GPIntegrationException.class).when(spyTemp).getRealTimeServices();

		AddressData addressData = spyTemp.verifyAddress(stubAddressData());
	}

	@Test
	public void testSuggestAddresses() throws GPIntegrationException {
		com.gp.businessobjects.service.addr.suggestions.input.DataSet suggestionsDataSet = new com.gp.businessobjects.service.addr.suggestions.input.DataSet();
		com.gp.businessobjects.service.addr.suggestions.input.Record suggestionsRecord = new com.gp.businessobjects.service.addr.suggestions.input.Record();
		suggestionsRecord.setCITY1("Hopkins");
		suggestionsRecord.setCOUNTRY("US");
		suggestionsRecord.setHOUSENUM1("8316");
		suggestionsRecord.setSTREET("Jefferson St");
		suggestionsRecord.setREGION("MN");
		suggestionsRecord.setPOSTCODE1("55343");

		suggestionsDataSet.getRecord().add(suggestionsRecord);

		GPDefaultAddressVerificationService spyTemp = Mockito.spy(addressVerificationService);
		Mockito.doReturn(realTimeServices).when(spyTemp).getRealTimeServices();
		when(realTimeServices.addrCleanseSuggestionsHybris(suggestionsDataSet)).thenReturn(suggestionsDataSetOutput);
		List<AddressData> addressData = spyTemp.suggestAddresses(stubAddressData());
		assertNotNull(addressData);

	}
	
	@Test(expected = GPIntegrationException.class)
	public void testSuggestWithAddresses() throws GPIntegrationException {
		com.gp.businessobjects.service.addr.suggestions.input.DataSet suggestionsDataSet = new com.gp.businessobjects.service.addr.suggestions.input.DataSet();
		com.gp.businessobjects.service.addr.suggestions.input.Record suggestionsRecord = new com.gp.businessobjects.service.addr.suggestions.input.Record();
		suggestionsRecord.setCITY1("Hopkins");
		suggestionsRecord.setCOUNTRY("US");
		suggestionsRecord.setHOUSENUM1("8316");
		suggestionsRecord.setSTREET("Jefferson St");
		suggestionsRecord.setREGION("MN");
		suggestionsRecord.setPOSTCODE1("55343");

		suggestionsDataSet.getRecord().add(suggestionsRecord);

		GPDefaultAddressVerificationService spyTemp = Mockito.spy(addressVerificationService);
		Mockito.doThrow(GPIntegrationException.class).when(spyTemp).getRealTimeServices();
		when(realTimeServices.addrCleanseSuggestionsHybris(suggestionsDataSet)).thenReturn(suggestionsDataSetOutput);

		List<AddressData> addressData = spyTemp.suggestAddresses(stubAddressData());

	}
	
	public AddressData stubAddressData() {
		AddressData addressData = new AddressData();
		addressData.setLine1("8316");
		addressData.setLine2("Jefferson street");
		addressData.setTown("Hopkins");
		RegionData regionData = new RegionData();
		regionData.setName("Minnesota");
		CountryData country = new CountryData();
		country.setName("USA");
		addressData.setRegion(regionData);
		addressData.setCountry(country);
		addressData.setPostalCode("55343");
		addressData.setFormattedAddress("testr test");
		return addressData;
	}
}

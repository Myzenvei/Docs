package com.gp.commerce.core.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;

import com.gp.commerce.core.model.TrackingModel;
import com.gp.commerce.core.services.GPConsignmentService;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Assert;


@PowerMockIgnore(
{ "org.apache.logging.log4j.*" }) 
@UnitTest
public class GPOrderShipmentCronjobTest {

	@InjectMocks
	GPOrderShipmentCronjob cron = new GPOrderShipmentCronjob();
	@Mock
	private GPConsignmentService GpConsignmentService;
	@Mock
	private EventService eventService;
	@Mock
	private BusinessProcessService businessProcessService;

 
	private List<TrackingModel> trackingDetails;
	private TrackingModel trackingModel;
	private ConsignmentEntryModel consignmentEntryModel;
	private ConsignmentProcessModel consignmentProcessModel;
	private ConsignmentModel consignment;
	private ModelService modelService;

	@Before
	public void setUp() throws Exception {
		cron.setGpConsignmentService(GpConsignmentService);
		cron.setBusinessProcessService(businessProcessService);
		MockitoAnnotations.initMocks(this);
		trackingModel = Mockito.mock(TrackingModel.class);
		consignmentProcessModel = Mockito.mock(ConsignmentProcessModel.class);
		consignment = Mockito.mock(ConsignmentModel.class);
		consignmentEntryModel = Mockito.mock(ConsignmentEntryModel.class);
		modelService=Mockito.mock(ModelService.class);
	 
		cron.setModelService(modelService);

	}

	@Test
	public void testPerform() {
		Map<String, List<TrackingModel>> trackingMap = new HashMap<>();  
		List<ConsignmentEntryModel> consignmentEntryList = new ArrayList<ConsignmentEntryModel>();
		trackingDetails = new ArrayList<TrackingModel>();
		consignmentEntryList.add(consignmentEntryModel);
		trackingDetails.add(trackingModel);
		Mockito.when(GpConsignmentService.getTrackingDetailsForEmail()).thenReturn(trackingDetails);
		Mockito.when(trackingModel.getConsignmentEntries()).thenReturn(consignmentEntryList);
		Mockito.when(consignmentEntryModel.getConsignment()).thenReturn(consignment);
		Mockito.when(consignmentProcessModel.getProcessState()).thenReturn(ProcessState.SUCCEEDED);
		trackingMap.computeIfAbsent(trackingModel.getTrackingID(), k -> new ArrayList<>()).add(trackingModel);
		Mockito.when(businessProcessService.createProcess(Mockito.anyString(),Mockito.anyString())).thenReturn(consignmentProcessModel);
		consignmentProcessModel.setConsignment(consignment);
		consignmentProcessModel.setTrackings(trackingDetails);
		Assert.assertEquals(cron.perform(new CronJobModel()).getResult().getCode(),"SUCCESS");
	}
	@Test
	public void testPerformFailed() {
		Map<String, List<TrackingModel>> trackingMap = new HashMap<>();  
		List<ConsignmentEntryModel> consignmentEntryList = new ArrayList<ConsignmentEntryModel>();
		trackingDetails = new ArrayList<TrackingModel>();
		consignmentEntryList.add(consignmentEntryModel);
		trackingDetails.add(trackingModel);
		Mockito.when(GpConsignmentService.getTrackingDetailsForEmail()).thenReturn(trackingDetails);
		Mockito.when(trackingModel.getConsignmentEntries()).thenReturn(consignmentEntryList);
		Mockito.when(consignmentEntryModel.getConsignment()).thenReturn(consignment);
		Mockito.when(consignmentProcessModel.getProcessState()).thenReturn(ProcessState.FAILED);
		trackingMap.computeIfAbsent(trackingModel.getTrackingID(), k -> new ArrayList<>()).add(trackingModel);
		Mockito.when(businessProcessService.createProcess(Mockito.anyString(),Mockito.anyString())).thenReturn(consignmentProcessModel);
		consignmentProcessModel.setConsignment(consignment);
		consignmentProcessModel.setTrackings(trackingDetails);
		Assert.assertEquals(cron.perform(new CronJobModel()).getResult().getCode(),"FAILED");
	}
	
	@Test
	public void testPerformError() {
		Map<String, List<TrackingModel>> trackingMap = new HashMap<>();  
		List<ConsignmentEntryModel> consignmentEntryList = new ArrayList<ConsignmentEntryModel>();
		trackingDetails = new ArrayList<TrackingModel>();
		consignmentEntryList.add(consignmentEntryModel);
		trackingDetails.add(trackingModel);
		Mockito.when(GpConsignmentService.getTrackingDetailsForEmail()).thenReturn(trackingDetails);
		Mockito.when(trackingModel.getConsignmentEntries()).thenReturn(consignmentEntryList);
		Mockito.when(consignmentEntryModel.getConsignment()).thenReturn(consignment);
		Mockito.when(consignmentProcessModel.getProcessState()).thenReturn(ProcessState.ERROR);
		trackingMap.computeIfAbsent(trackingModel.getTrackingID(), k -> new ArrayList<>()).add(trackingModel);
		Mockito.when(businessProcessService.createProcess(Mockito.anyString(),Mockito.anyString())).thenReturn(consignmentProcessModel);
		consignmentProcessModel.setConsignment(consignment);
		consignmentProcessModel.setTrackings(trackingDetails);
		Assert.assertEquals(cron.perform(new CronJobModel()).getResult().getCode(),"ERROR");
	}
}

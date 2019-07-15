package com.gp.commerce.core.cronjob.addresses;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.enums.GPApprovalEnum;
import com.gp.commerce.core.user.dao.GPUserDao;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import com.gp.commerce.core.cronjob.addresses.GPRemoveRejectedAddressesCronJob;

public class GPRemoveRejectedAddressesCronJobTest {

	@InjectMocks
	GPRemoveRejectedAddressesCronJob gpRemoveRejectedAddressesCronJob = new GPRemoveRejectedAddressesCronJob();
	
	@Mock
	FlexibleSearchService flexibleSearchService;
	
	@Mock
	private ConfigurationService configurationService;
	
	@Mock
	GPUserDao userDao;
	
	@Mock
	private ModelService modelService;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testPerformCronJobModel() {
		List<AddressModel> addresses = new ArrayList<>();
		AddressModel address = new AddressModel();
		address.setApprovalStatus(GPApprovalEnum.REJECTED);
		addresses.add(address);
		final CronJobModel cronJob = Mockito.mock(CronJobModel.class);
		Mockito.when(userDao.getAllAddressesOnStatus(Matchers.anyListOf(GPApprovalEnum.class))).thenReturn(addresses);
		Mockito.doNothing().when(modelService).removeAll(Mockito.anyCollectionOf(AddressModel.class));
		Assert.assertEquals("FINISHED",gpRemoveRejectedAddressesCronJob.perform(cronJob).getStatus().getCode());
		
	}

}

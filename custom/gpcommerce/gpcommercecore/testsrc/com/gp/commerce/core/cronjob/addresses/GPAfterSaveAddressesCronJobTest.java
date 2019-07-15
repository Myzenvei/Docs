package com.gp.commerce.core.cronjob.addresses;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.enums.GPApprovalEnum;
import com.gp.commerce.core.model.AddressSaveCronJobModel;
import com.gp.commerce.core.services.GPUserService;
import com.gp.commerce.core.user.dao.GPUserDao;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;

public class GPAfterSaveAddressesCronJobTest {

	@Mock
	GPUserDao userDao;
	
	@Mock
	GPUserService userService;
	
	@Mock
	private CommonI18NService commonI18NService;
	
	@Mock
	private EventService eventService;
	
	@Mock
	ModelService modelService;
	
	@InjectMocks
	GPAfterSaveAddressesCronJob gpAfterSaveAddressesCronJob = new GPAfterSaveAddressesCronJob();
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testPerformAddressSaveCronJobModel() {
		B2BCustomerModel customer = Mockito.mock(B2BCustomerModel.class);
		List<AddressModel> addresses = new ArrayList<>();
		AddressModel address = new AddressModel();
		address.setOwner(customer);
		address.setApprovalStatus(GPApprovalEnum.REJECTED);
		addresses.add(address);

		BaseStoreModel baseStore = Mockito.mock(BaseStoreModel.class);
		BaseSiteModel baseSite = Mockito.mock(BaseSiteModel.class);
		AddressSaveCronJobModel addressSaveCronJob = Mockito.mock(AddressSaveCronJobModel.class);
		List<BaseStoreModel> baseStores = new ArrayList<>();
		baseStores.add(baseStore);
		
		
		Mockito.when(userDao.getAllAddressesForB2B(addressSaveCronJob.getStartTime(), addressSaveCronJob.getSite())).thenReturn(addresses);
		Mockito.when(userService.isGPAdminApproved(Mockito.any(AddressModel.class))).thenReturn(true);
		Mockito.when(customer.getSite()).thenReturn(baseSite);
		Mockito.when(baseSite.getStores()).thenReturn(baseStores);
		doNothing().when(eventService).publishEvent(Mockito.anyObject());
		doNothing().when(modelService).save(Mockito.anyObject());
		Assert.assertNotNull(gpAfterSaveAddressesCronJob.perform(addressSaveCronJob));
		Assert.assertEquals("FINISHED",gpAfterSaveAddressesCronJob.perform(addressSaveCronJob).getStatus().getCode());

	}

}

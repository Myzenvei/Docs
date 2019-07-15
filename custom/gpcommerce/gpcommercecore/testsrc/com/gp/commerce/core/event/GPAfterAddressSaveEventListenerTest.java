package com.gp.commerce.core.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.gp.commerce.core.enums.GPApprovalEnum;
import com.gp.commerce.core.services.GPUserService;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.tx.AfterSaveEvent;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class GPAfterAddressSaveEventListenerTest 
{
	
	@Mock
	private BaseStoreService baseStoreService;

	@Mock
	private BaseSiteService baseSiteService;

	@Mock
	private CommonI18NService commonI18NService;

	@Mock
	private ConfigurationService configurationService;
	
	@Mock
	private GPUserService userService;
	
	@Mock
	private EventService eventService;
	
	@Mock
	private ModelService modelService;

	@Mock
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
	
	
	private GPAfterAddressSaveEventListener gpAfterAddressSaveEventListener=new GPAfterAddressSaveEventListener();
	
	@Mock
	private AddressModel addressModel;
	
	@Mock
	private AfterSaveEvent event;
	
	
	private PK primaryKey=PK.createFixedUUIDPK(23, 2345687);
	
	@Mock
	B2BCustomerModel customer;
	
	@Mock
	B2BUnitModel unit;
	
	@Mock
	BaseSiteModel site;
	
	@Before
	public void setup()
	{
	
		ReflectionTestUtils.setField(gpAfterAddressSaveEventListener, "baseStoreService", baseStoreService);
		ReflectionTestUtils.setField(gpAfterAddressSaveEventListener, "baseSiteService", baseSiteService);
		ReflectionTestUtils.setField(gpAfterAddressSaveEventListener, "commonI18NService", commonI18NService);
		//ReflectionTestUtils.setField(gpAfterAddressSaveEventListener, "configurationService", configurationService);
		//ReflectionTestUtils.setField(gpAfterAddressSaveEventListener, "userService", userService);
		gpAfterAddressSaveEventListener.setUserService(userService);
		gpAfterAddressSaveEventListener.setB2bUnitService(b2bUnitService);
		gpAfterAddressSaveEventListener.setConfigurationService(configurationService);
		ReflectionTestUtils.setField(gpAfterAddressSaveEventListener, "eventService", eventService);
		ReflectionTestUtils.setField(gpAfterAddressSaveEventListener, "modelService", modelService);
		Mockito.when(event.getType()).thenReturn(1);
		Mockito.when(event.getPk()).thenReturn(primaryKey);
		Mockito.when(modelService.get(primaryKey)).thenReturn(addressModel);
		Mockito.when(addressModel.getOwner()).thenReturn(customer);
		Mockito.when(customer.getDefaultB2BUnit()).thenReturn(unit);
		Mockito.when(unit.getB2bUnitLevel()).thenReturn("L3");
		Mockito.when(userService.isGPAdminApproved(addressModel)).thenReturn(true);
		Mockito.when(addressModel.getApprovalStatus()).thenReturn(GPApprovalEnum.REJECTED);
		
		Mockito.when(customer.getSite()).thenReturn(site);
		Mockito.when(site.getStores()).thenReturn(Collections.singletonList(Mockito.mock(BaseStoreModel.class)));
		Mockito.when(commonI18NService.getCurrentLanguage()).thenReturn(Mockito.mock(LanguageModel.class));
		Mockito.when(commonI18NService.getCurrentCurrency()).thenReturn(Mockito.mock(CurrencyModel.class));
		
	}
	
	@Test
	public void afterSaveTestSuccess()
	{
		final Collection<AfterSaveEvent> events=new ArrayList<>();
		events.add(event);
		gpAfterAddressSaveEventListener.afterSave(events);
		Mockito.verify(eventService).publishEvent(Mockito.any());
		
	}
	
	@Test
	public void afterSaveTestForNonUpdateEvent()
	{
		AfterSaveEvent event1=Mockito.mock(AfterSaveEvent.class);
		Mockito.when(event1.getType()).thenReturn(2);
		final Collection<AfterSaveEvent> events=new ArrayList<>();
		events.add(event1);
		gpAfterAddressSaveEventListener.afterSave(events);
		
	}

	@Test
	public void afterSaveTestForNonAddressTypeCode()
	{
		AfterSaveEvent event1=Mockito.mock(AfterSaveEvent.class);
		 PK primaryK=PK.createFixedUUIDPK(25, 2345687);
		Mockito.when(event1.getPk()).thenReturn(primaryK);
		final Collection<AfterSaveEvent> events=new ArrayList<>();
		events.add(event1);
		gpAfterAddressSaveEventListener.afterSave(events);
		
	}

	@Test
	public void afterSaveTestForNonB2BCustomer()
	{
		final Collection<AfterSaveEvent> events=new ArrayList<>();
		events.add(event);
		Mockito.when(addressModel.getOwner()).thenReturn(null);
		gpAfterAddressSaveEventListener.afterSave(events);
		
	}

}

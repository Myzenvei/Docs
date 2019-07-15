package com.gp.commerce.core.services.event;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.event.impl.EventScope;
import de.hybris.platform.store.BaseStoreModel;

@UnitTest
public class GPEmailItemEventTest {

	@InjectMocks
	private GPEmailItemEvent gpEmailItemEvent = new GPEmailItemEvent();
	
	@Test
	public void gpEmailItemEvent() {
		gpEmailItemEvent.setAddress(mock(AddressModel.class));
		gpEmailItemEvent.setAdminModel(mock(CustomerModel.class));
		gpEmailItemEvent.setBackOfficeUser(true);
		gpEmailItemEvent.setBaseStore(mock(BaseStoreModel.class));
		gpEmailItemEvent.setBccEmail("Email@deloitte.com");
		gpEmailItemEvent.setCurrency(mock(CurrencyModel.class));
		gpEmailItemEvent.setCustomer(mock(CustomerModel.class));
		gpEmailItemEvent.setCustomerModel(mock(CustomerModel.class));
		gpEmailItemEvent.setEmailSubject("EmailSubject");
		gpEmailItemEvent.setFromCluster(true);
		gpEmailItemEvent.setInvitedCustomer(mock(CustomerModel.class));
		gpEmailItemEvent.setLanguage(mock(LanguageModel.class));
		gpEmailItemEvent.setScope(mock(EventScope.class));
		gpEmailItemEvent.setSite(mock(BaseSiteModel.class));
		List<String> list = new ArrayList<>();
		list.add("String1");
		gpEmailItemEvent.setToEmails(list);
		gpEmailItemEvent.setToken("Token");
	}
}

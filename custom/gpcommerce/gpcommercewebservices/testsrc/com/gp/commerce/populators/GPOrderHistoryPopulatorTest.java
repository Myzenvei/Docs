package com.gp.commerce.populators;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.model.SplitEntryModel;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

@UnitTest
public class GPOrderHistoryPopulatorTest {

	@InjectMocks
	GPOrderHistoryPopulator gpOrderHistoryPopulator = new GPOrderHistoryPopulator();
	@Mock
	private Converter<AddressModel, AddressData> addressConverter;

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void populateTest() {
		OrderModel source = new OrderModel();
		OrderHistoryData target = new OrderHistoryData();
		List<AbstractOrderEntryModel> entry = new ArrayList<>();
		AbstractOrderEntryModel orderEntry = new AbstractOrderEntryModel();
		List<SplitEntryModel> splits = new ArrayList<>();
		Set<PrincipalGroupModel> groups = new HashSet<>();
		PrincipalGroupModel testGroup = new PrincipalGroupModel();
		SplitEntryModel se = new SplitEntryModel();
		AddressModel address = new AddressModel();
		UserModel user = new UserModel();
		Date value = Mockito.mock(Date.class);
		testGroup.setUid("b2badmingroup");
		groups.add(testGroup);
		user.setGroups(groups);
		user.setCreationtime(value);
		source.setUser(user);
		se.setDeliveryAddress(address);
		splits.add(se);
		orderEntry.setSplitEntry(splits);
		orderEntry.setQuantity(33L);
		entry.add(orderEntry);
		source.setEntries(entry);
		AddressData addressData = new AddressData();
		addressData.setId("test");
		when(source.getUser().getAllgroups()).thenReturn(groups);
		when(addressConverter.convert(Mockito.any(AddressModel.class))).thenReturn(addressData);
		gpOrderHistoryPopulator.populate(source, target);

	}
}

package com.gp.commerce.facades.populators;


import java.util.Collections;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.model.MarketingPreferenceModel;

import de.hybris.bootstrap.annotations.UnitTest;
import com.gp.commerce.facades.data.user.data.MarketingPreferenceData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.user.UserService;

@UnitTest
public class GPMarketingPreferencePopulatorTest {

	private static final String NAME = "GP-Preference";
	private static final String DESCRIPTION = "GP-Description";
	private static final String PREFERENCE_ID = "GP-1";
	private static final String MARKET_ID = "GPM-1";
	
	@Mock
	private MarketingPreferenceModel source;
	private MarketingPreferenceData  target;
	
	@Mock
	private UserService userService;
	
	@Mock
	private CustomerModel currentUser;
	
	@InjectMocks
	private GPMarketingPreferencePopulator gPMarketingPreferencePopulator = new GPMarketingPreferencePopulator();
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new MarketingPreferenceData();
		
		Mockito.when(source.getName()).thenReturn(NAME);
		Mockito.when(source.getDescription()).thenReturn(DESCRIPTION);
		Mockito.when(source.getPreferenceTypeId()).thenReturn(PREFERENCE_ID);
	}
	
	@Test
	public void testPopulate() throws ConversionException
	{
		Mockito.when(userService.getCurrentUser()).thenReturn(currentUser);
		Mockito.when(currentUser.getMarketingPreferences()).thenReturn(Collections.singletonList(source));
		
		gPMarketingPreferencePopulator.populate(source, target);
		Assert.assertNotNull(target);
		Assert.assertEquals(NAME, target.getName());
		Assert.assertEquals(PREFERENCE_ID, target.getPreferenceTypeId());
		Assert.assertTrue(target.isSelected());
	}
}

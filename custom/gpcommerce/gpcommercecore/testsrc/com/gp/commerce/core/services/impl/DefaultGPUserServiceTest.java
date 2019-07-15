package com.gp.commerce.core.services.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.hmc.model.SavedValueEntryModel;
import de.hybris.platform.hmc.model.SavedValuesModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.PasswordPolicyService;
import de.hybris.platform.servicelayer.user.daos.TitleDao;
import de.hybris.platform.servicelayer.user.daos.UserAuditDao;
import de.hybris.platform.servicelayer.user.daos.UserGroupDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.GPApprovalEnum;
import com.gp.commerce.core.enums.GPUserApprovalStatusEnum;
import com.gp.commerce.core.model.MarketingPreferenceModel;
import com.gp.commerce.core.model.MarketingPreferenceTypeModel;
import com.gp.commerce.core.model.MarketingPreferenceUpdateModel;
import com.gp.commerce.core.savedvalus.dao.GPSavedValuesDao;
import com.gp.commerce.core.user.dao.GPUserDao;
import com.gp.commerce.facades.data.user.data.MarketingPreferenceData;
import com.gp.commerce.facades.data.user.data.MarketingPreferenceDataList;

@UnitTest
public class DefaultGPUserServiceTest {

	private static final String PREFERENCE_ID_1 ="pf1";
	private static final String PREFERENCE_ID_2 ="pf2";
	private static final String PREFERENCE_ID_3 ="pf3";
	private static final String PREFERENCE_ID_4 ="pf4";

	@Mock
	private GPUserDao userDao;
	@Mock
	private ModelService modelService;
	@Mock
	private CMSSiteModel site;
	@Mock
	private CustomerModel currentCustomer;
	@Mock
	private GPSavedValuesDao gpSavedValuesDao;
	
	@Mock
	private UserGroupDao userGroupDao;
	@Mock
	private TitleDao titleDao;
	@Mock
	private UserAuditDao userAuditDao;
	@Mock
	private SearchRestrictionService searchRestrictionService;
	@Mock
	private EventService eventService;
	@Mock
	private PasswordEncoderService passwordEncoderService;
	@Mock
	private PasswordPolicyService passwordPolicyService;

	@Spy
	@InjectMocks
	private final DefaultGPUserService gpUserService =new DefaultGPUserService();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		Mockito.doReturn(currentCustomer).when(gpUserService).getCurrentCustomer();
	}

	@Test
	public void testGetMarketingpreferences()
	{
		gpUserService.getMarketingPreferences(site);
		verify(userDao, times(1)).getMarketingPreferencesForSite(site);
	}

	@Test
	public void testUpdateCustomerPreferences()
	{
		// creating marketing preference data list selected from customer.
		final MarketingPreferenceData pfd1 = new MarketingPreferenceData();
		pfd1.setPreferenceTypeId(PREFERENCE_ID_1);
		pfd1.setSelected(true);

		final MarketingPreferenceData pfd2 = new MarketingPreferenceData();
		pfd1.setPreferenceTypeId(PREFERENCE_ID_2);
		pfd1.setSelected(true);

		final MarketingPreferenceData pfd3 = new MarketingPreferenceData();
		pfd1.setPreferenceTypeId(PREFERENCE_ID_3);
		pfd1.setSelected(false);

		final MarketingPreferenceData pfd4 = new MarketingPreferenceData();
		pfd1.setPreferenceTypeId(PREFERENCE_ID_4);
		pfd1.setSelected(false);

		final MarketingPreferenceDataList preferencesData = new MarketingPreferenceDataList();
		final List<MarketingPreferenceData> marketingPreferences = new ArrayList<>();
		marketingPreferences.add(pfd1);
		marketingPreferences.add(pfd2);
		marketingPreferences.add(pfd3);
		marketingPreferences.add(pfd4);
		preferencesData.setMarketingPreferences(marketingPreferences);

		//creating marketing preference models for site config.
		final MarketingPreferenceModel pf1 = new MarketingPreferenceModel();
		pf1.setPreferenceTypeId(PREFERENCE_ID_1);

		final MarketingPreferenceModel pf2 = new MarketingPreferenceModel();
		pf1.setPreferenceTypeId(PREFERENCE_ID_2);

		final MarketingPreferenceModel pf3 = new MarketingPreferenceModel();
		pf1.setPreferenceTypeId(PREFERENCE_ID_3);

		final MarketingPreferenceModel pf4 = new MarketingPreferenceModel();
		pf1.setPreferenceTypeId(PREFERENCE_ID_4);

		final List<MarketingPreferenceModel> sitePreferences = new ArrayList<>();
		sitePreferences.add(pf1);
		sitePreferences.add(pf2);
		sitePreferences.add(pf3);
		sitePreferences.add(pf4);

		//creating empty customer's preferences
		final List<MarketingPreferenceModel> customerPreferences = new ArrayList<>();
		Mockito.when(currentCustomer.getMarketingPreferences()).thenReturn(customerPreferences);

		Mockito.doNothing().when(modelService).save(Mockito.any(CustomerModel.class));
		Mockito.doNothing().when(modelService).refresh(Mockito.any(CustomerModel.class));
		Mockito.doNothing().when(modelService).save(Mockito.any(MarketingPreferenceUpdateModel.class));

		Mockito.when(userDao.getMarketingPreferencesForSite(site)).thenReturn(sitePreferences);

		gpUserService.updateCustomerPreferences(preferencesData,site);

        Mockito.verify(modelService, times(1)).save(currentCustomer);
      	}

	@Test
	public void testUpdateCustomerPreferencesWhenEmptySitePreferences()
	{
		// creating marketing preference data list selected from customer.
		final MarketingPreferenceData pfd1 = new MarketingPreferenceData();
		pfd1.setPreferenceTypeId(PREFERENCE_ID_1);
		pfd1.setSelected(true);

		final MarketingPreferenceDataList preferencesData = new MarketingPreferenceDataList();
		final List<MarketingPreferenceData> marketingPreferences = new ArrayList<>();
		marketingPreferences.add(pfd1);
		preferencesData.setMarketingPreferences(marketingPreferences);

		Mockito.when(userDao.getMarketingPreferencesForSite(site)).thenReturn(Collections.EMPTY_LIST);

		gpUserService.updateCustomerPreferences(preferencesData,site);

        Mockito.verify(modelService, times(0)).save(currentCustomer);
        Mockito.verify(modelService, times(0)).refresh(currentCustomer);
	}


	@Test
	public void getMarketingPreferencesForSiteAndTypeTest(){

		final MarketingPreferenceTypeModel markPrefType = new MarketingPreferenceTypeModel();
		gpUserService.getMarketingPreferencesForSiteAndType(site, markPrefType);
		verify(userDao, times(1)).getMarketingPreferencesForSiteAndType(site, markPrefType);
	}

	@Test
	public void isCustomerApprovedTest(){
		final B2BCustomerModel b2bCustomer = Mockito.mock(B2BCustomerModel.class);
		final B2BUnitModel unit = Mockito.mock(B2BUnitModel.class);
		final List<SavedValuesModel> changedLogs = new ArrayList<>();
		Mockito.when(unit.getB2bUnitLevel()).thenReturn("L2");
		Mockito.when(b2bCustomer.getDefaultB2BUnit()).thenReturn(unit);
		final Set<SavedValueEntryModel> savedValueEntryModelSet = new HashSet<>(1);
		final SavedValueEntryModel savedValueEntry = Mockito.mock(SavedValueEntryModel.class);
		Mockito.when(savedValueEntry.getModifiedAttribute()).thenReturn("userApprovalStatus");
		Mockito.when(savedValueEntry.getNewValue()).thenReturn(GPUserApprovalStatusEnum.REJECTED);
		Mockito.when(savedValueEntry.getNewValue()).thenReturn(GPUserApprovalStatusEnum.APPROVED);
		Mockito.when(b2bCustomer.getUserApprovalStatus()).thenReturn(GPUserApprovalStatusEnum.APPROVED);
		savedValueEntryModelSet.add(savedValueEntry);
		final SavedValuesModel value = Mockito.mock(SavedValuesModel.class);
		changedLogs.add(value);
		Mockito.when(value.getSavedValuesEntries()).thenReturn(savedValueEntryModelSet);
		Mockito.when(gpSavedValuesDao.getChangedLogs(b2bCustomer)).thenReturn(changedLogs);
		gpUserService.isCustomerApproved(b2bCustomer);

	}

	@Test
	public void isCustomerRejectedTest(){
		final B2BCustomerModel b2bCustomer = Mockito.mock(B2BCustomerModel.class);
		final B2BUnitModel unit = Mockito.mock(B2BUnitModel.class);
		final List<SavedValuesModel> changedLogs = new ArrayList<>();
		Mockito.when(unit.getB2bUnitLevel()).thenReturn("L2");
		Mockito.when(b2bCustomer.getDefaultB2BUnit()).thenReturn(unit);
		
		final Set<SavedValueEntryModel> savedValueEntryModelSet = new HashSet<>(1);
		final SavedValueEntryModel savedValueEntry = Mockito.mock(SavedValueEntryModel.class);
		Mockito.when(savedValueEntry.getModifiedAttribute()).thenReturn("userApprovalStatus");
		Mockito.when(savedValueEntry.getNewValue()).thenReturn(GPUserApprovalStatusEnum.REJECTED);
		
		savedValueEntryModelSet.add(savedValueEntry);
		final SavedValuesModel value = Mockito.mock(SavedValuesModel.class);
		value.getSavedValuesEntries().addAll(savedValueEntryModelSet);
		changedLogs.add(value);
		Mockito.when(gpSavedValuesDao.getChangedLogs(b2bCustomer)).thenReturn(changedLogs);
		final boolean status =gpUserService.isCustomerApproved(b2bCustomer);
		Assert.assertTrue(!status);
	}

	@Test
	public void isGPAdminApprovedTest(){

		final AddressModel addressModel = Mockito.mock(AddressModel.class);
	    final List<SavedValuesModel> changedLogs=new ArrayList<>();
		final Set<SavedValueEntryModel> savedValueEntryModelSet = new HashSet<>();
		final SavedValueEntryModel savedValueEntry = Mockito.mock(SavedValueEntryModel.class);
		Mockito.when(savedValueEntry.getModifiedAttribute()).thenReturn("approvalStatus");
		Mockito.when(savedValueEntry.getOldValue()).thenReturn(GPApprovalEnum.PENDING);
		Mockito.when(savedValueEntry.getNewValue()).thenReturn(GPApprovalEnum.DISABLED);
		savedValueEntryModelSet.add(savedValueEntry);
		final SavedValuesModel value = Mockito.mock(SavedValuesModel.class);
		Mockito.when(value.getSavedValuesEntries()).thenReturn(savedValueEntryModelSet);
		changedLogs.add(value);
		Mockito.when(value.getSavedValuesEntries()).thenReturn(savedValueEntryModelSet);
		Mockito.when(gpSavedValuesDao.getChangedLogs(addressModel)).thenReturn(changedLogs);
		final boolean status = gpUserService.isGPAdminApproved(addressModel);
		Assert.assertTrue(!status);
		}

	@Test
	public void isAdminApprovedTest(){

		final AddressModel addressModel = Mockito.mock(AddressModel.class);
		final List<SavedValuesModel> changedLogs=new ArrayList<>();
		final Set<SavedValueEntryModel> savedValueEntryModelSet = new HashSet<>(1);
		final SavedValueEntryModel savedValueEntry = Mockito.mock(SavedValueEntryModel.class);
		Mockito.when(savedValueEntry.getModifiedAttribute()).thenReturn("approvalStatus");
		Mockito.when(savedValueEntry.getOldValue()).thenReturn(GPApprovalEnum.PENDING);
		Mockito.when(savedValueEntry.getNewValue()).thenReturn(GPApprovalEnum.DISABLED);
		savedValueEntryModelSet.add(savedValueEntry);
		final SavedValuesModel value = Mockito.mock(SavedValuesModel.class);
		value.getSavedValuesEntries().addAll(savedValueEntryModelSet);
		changedLogs.add(value);
		Mockito.when(value.getSavedValuesEntries()).thenReturn(savedValueEntryModelSet);
        Mockito.when(gpSavedValuesDao.getChangedLogs(addressModel)).thenReturn(changedLogs);
		final boolean status = gpUserService.isAdminApproved(addressModel);
		Assert.assertTrue(!status);
		}

	@Test
	public void getDistMarketingPreferencesTest() {
		final List<MarketingPreferenceTypeModel> models = new ArrayList<>();
		Mockito.when(gpUserService.getDistMarketingPreferences(site)).thenReturn(models);
		gpUserService.getDistMarketingPreferences(site);

	}

}

package com.gp.commerce.core.b2bunit.interceptor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.gp.commerce.core.company.services.GPB2BUnitsService;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;

@UnitTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(
{ Config.class })
@PowerMockIgnore(
{ "org.apache.logging.log4j.*" })
public class GPB2BUnitModelValidateInterceptorTest
{

	@Mock
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;

	@Mock
	private UserService userService;

	@Mock
	private ModelService modelService;

	@Mock
	private L10NService l10NService;

	@Mock
	private GPB2BUnitsService<B2BUnitModel, UserModel> gpB2BUnitsService;

	@InjectMocks
	private GPB2BUnitModelValidateInterceptor gpB2BUnitModelValidateInterceptor;

	@Mock
	private InterceptorContext ctx;

	private B2BCustomerModel approver;

	private B2BUnitModel model;

	private Collection<B2BCustomerModel> users;

	private Set childUnits;

	private B2BUnitModel child;

	private B2BCustomerModel cust1;

	private List<UserModel> usersList;

	private UserGroupModel userGroupModel;

	private List<B2BUnitModel> unitList;

	private List<String> stringList;

	private final String unitLevels = "L1";

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		dataSetUp();
		when(userService.getUserGroupForUID(B2BConstants.B2BAPPROVERGROUP)).thenReturn(userGroupModel);
		when(ctx.getModelService()).thenReturn(modelService);
		when(b2bUnitService.getB2BUnits(model)).thenReturn(childUnits);
		doNothing().when(modelService).save(child);
		PowerMockito.mockStatic(Config.class);
		Mockito.when(Config.getParameter(GpcommerceCoreConstants.B2B_UNITLEVEL_CODES)).thenReturn(unitLevels);
		when(gpB2BUnitsService.getUsersOfUserGroup(unitList, stringList)).thenReturn(users);

		doNothing().when(gpB2BUnitsService).updateUserGroupByUnitLevel(unitLevels, usersList);
	}

	public void dataSetUp()
	{
		approver = new B2BCustomerModel();
		final B2BCustomerModel approver1 = new B2BCustomerModel();
		model = new B2BUnitModel();

		final Set approvers = new HashSet();
		approvers.add(approver);
		approvers.add(approver1);

		model.setApprovers(approvers);
		model.setActive(Boolean.FALSE);
		model.setB2bUnitLevel("L1");

		child = new B2BUnitModel();
		child.setActive(Boolean.TRUE);

		childUnits = new HashSet();
		childUnits.add(child);

		unitList = new ArrayList<B2BUnitModel>();
		stringList = new ArrayList<String>();

		cust1 = new B2BCustomerModel();
		//cust1.setDefaultB2BUnit(model1);
		users = new ArrayList<B2BCustomerModel>();
		users.add(cust1);

		final Set groups = new HashSet();
		userGroupModel = new UserGroupModel();
		userGroupModel.setName("Approver");
		groups.add(userGroupModel);
		approver.setGroups(groups);


		final Set groups1 = new HashSet();
		final UserGroupModel userGroupModel1 = new UserGroupModel();
		userGroupModel1.setName("group");
		groups1.add(userGroupModel1);
		approver1.setGroups(groups1);

		final B2BCustomerModel cust2 = new B2BCustomerModel();
		usersList = new ArrayList<>();
	}

	@Test
	public void testGetB2bUnitService()
	{
		assertNotNull(gpB2BUnitModelValidateInterceptor.getB2bUnitService());
	}

	@Test
	public void testGetModelService()
	{
		assertNotNull(gpB2BUnitModelValidateInterceptor.getModelService());
	}

	@Test
	public void testGetL10NService()
	{
		assertNotNull(gpB2BUnitModelValidateInterceptor.getL10NService());
	}

	@Test
	public void testGetUserService()
	{
		assertNotNull(gpB2BUnitModelValidateInterceptor.getUserService());
	}



	@Test
	public void testValidate() throws InterceptorException
	{

		when(userService.isMemberOfGroup(approver, userGroupModel)).thenReturn(Boolean.FALSE);
		when(modelService.isNew(model)).thenReturn(Boolean.FALSE);
		when(ctx.isModified(model, B2BUnitModel.B2BUNITLEVEL)).thenReturn(Boolean.TRUE);
		gpB2BUnitModelValidateInterceptor.onValidate(model, ctx);
		assertTrue(model.getApprovers().size() == 0);
		verify(modelService, times(2)).save(child);
		verify(gpB2BUnitsService).updateUserGroupByUnitLevel(unitLevels, usersList);

	}

	@Test
	public void testValidateNotUnitModel() throws InterceptorException
	{
		when(userService.isMemberOfGroup(approver, userGroupModel)).thenReturn(Boolean.FALSE);
		when(modelService.isNew(model)).thenReturn(Boolean.FALSE);
		when(ctx.isModified(model, B2BUnitModel.B2BUNITLEVEL)).thenReturn(Boolean.TRUE);
		final CompanyModel model = new CompanyModel();
		gpB2BUnitModelValidateInterceptor.onValidate(model, ctx);
		verify(modelService, never()).save(child);
		verify(gpB2BUnitsService, never()).updateUserGroupByUnitLevel(unitLevels, usersList);

	}

	@Test
	public void testValidateAlreadyMember() throws InterceptorException
	{
		when(userService.isMemberOfGroup(approver, userGroupModel)).thenReturn(Boolean.TRUE);
		when(modelService.isNew(model)).thenReturn(Boolean.FALSE);
		when(ctx.isModified(model, B2BUnitModel.B2BUNITLEVEL)).thenReturn(Boolean.TRUE);
		gpB2BUnitModelValidateInterceptor.onValidate(model, ctx);
		assertTrue(model.getApprovers().size() == 1);
		verify(modelService, times(2)).save(child);
		verify(gpB2BUnitsService).updateUserGroupByUnitLevel(unitLevels, usersList);

	}

	@Test
	public void testValidateUnitActiveAndUnitNew() throws InterceptorException
	{
		when(userService.isMemberOfGroup(approver, userGroupModel)).thenReturn(Boolean.FALSE);
		model.setActive(Boolean.TRUE);
		when(ctx.isModified(model, B2BUnitModel.B2BUNITLEVEL)).thenReturn(Boolean.TRUE);
		when(modelService.isNew(model)).thenReturn(Boolean.TRUE);
		gpB2BUnitModelValidateInterceptor.onValidate(model, ctx);
		assertTrue(model.getApprovers().size() == 0);
		verify(modelService, never()).save(child);
		verify(gpB2BUnitsService, never()).updateUserGroupByUnitLevel(unitLevels, usersList);

	}

	@Test
	public void testValidateUnitActiveAndUnitNotNew() throws InterceptorException
	{
		when(userService.isMemberOfGroup(approver, userGroupModel)).thenReturn(Boolean.FALSE);
		when(ctx.isModified(model, B2BUnitModel.B2BUNITLEVEL)).thenReturn(Boolean.TRUE);
		when(modelService.isNew(model)).thenReturn(Boolean.FALSE);
		child.setActive(Boolean.FALSE);
		gpB2BUnitModelValidateInterceptor.onValidate(model, ctx);
		assertTrue(model.getApprovers().size() == 0);
		verify(modelService, times(1)).save(child);
		verify(gpB2BUnitsService).updateUserGroupByUnitLevel(unitLevels, usersList);

	}

	@Test
	public void testValidateCtxNotModified() throws InterceptorException
	{
		when(userService.isMemberOfGroup(approver, userGroupModel)).thenReturn(Boolean.FALSE);
		when(modelService.isNew(model)).thenReturn(Boolean.FALSE);
		when(ctx.isModified(model, B2BUnitModel.B2BUNITLEVEL)).thenReturn(Boolean.FALSE);
		gpB2BUnitModelValidateInterceptor.onValidate(model, ctx);
		assertTrue(model.getApprovers().size() == 0);
		verify(modelService, times(1)).save(child);
		verify(gpB2BUnitsService, never()).updateUserGroupByUnitLevel(unitLevels, usersList);

	}

	@Test
	public void testValidateUnitGroupsNotEmpty() throws InterceptorException
	{
		when(userService.isMemberOfGroup(approver, userGroupModel)).thenReturn(Boolean.FALSE);
		when(modelService.isNew(model)).thenReturn(Boolean.FALSE);
		when(ctx.isModified(model, B2BUnitModel.B2BUNITLEVEL)).thenReturn(Boolean.TRUE);
		final Set groups = new HashSet();
		groups.add(new PrincipalGroupModel());
		model.setGroups(groups);
		gpB2BUnitModelValidateInterceptor.onValidate(model, ctx);
		assertTrue(model.getApprovers().size() == 0);
		verify(modelService, times(1)).save(child);
		verify(gpB2BUnitsService, never()).updateUserGroupByUnitLevel(unitLevels, usersList);

	}

	@Test
	public void testValidateCtxNotModifiedAndGroupsNotEmpty() throws InterceptorException
	{
		when(userService.isMemberOfGroup(approver, userGroupModel)).thenReturn(Boolean.FALSE);
		when(modelService.isNew(model)).thenReturn(Boolean.FALSE);
		when(ctx.isModified(model, B2BUnitModel.B2BUNITLEVEL)).thenReturn(Boolean.FALSE);
		gpB2BUnitModelValidateInterceptor.onValidate(model, ctx);
		assertTrue(model.getApprovers().size() == 0);
		verify(modelService, times(1)).save(child);
		verify(gpB2BUnitsService, never()).updateUserGroupByUnitLevel(unitLevels, usersList);
	}

	@Test
	public void testValidateUnitLevelEmpty() throws InterceptorException
	{
		when(userService.isMemberOfGroup(approver, userGroupModel)).thenReturn(Boolean.FALSE);
		when(modelService.isNew(model)).thenReturn(Boolean.FALSE);
		when(ctx.isModified(model, B2BUnitModel.B2BUNITLEVEL)).thenReturn(Boolean.TRUE);
		model.setB2bUnitLevel("");
		gpB2BUnitModelValidateInterceptor.onValidate(model, ctx);
		assertTrue(model.getApprovers().size() == 0);
		verify(modelService, times(1)).save(child);
		verify(gpB2BUnitsService, never()).updateUserGroupByUnitLevel(unitLevels, usersList);
	}

	@Test
	public void testValidateULEmptynCNMnGNE() throws InterceptorException
	{
		model.setGroups(null);
		model.setB2bUnitLevel(null);
		when(userService.isMemberOfGroup(approver, userGroupModel)).thenReturn(Boolean.FALSE);
		when(modelService.isNew(model)).thenReturn(Boolean.FALSE);
		when(ctx.isModified(model, B2BUnitModel.B2BUNITLEVEL)).thenReturn(Boolean.TRUE);
		gpB2BUnitModelValidateInterceptor.onValidate(model, ctx);
		verify(modelService, times(1)).save(child);
		verify(gpB2BUnitsService, never()).updateUserGroupByUnitLevel(unitLevels, usersList);

	}

	@Test
	public void testValidateULInvalidnCNMnGNE() throws InterceptorException
	{
		model.setGroups(null);
		model.setB2bUnitLevel("L2");
		when(userService.isMemberOfGroup(approver, userGroupModel)).thenReturn(Boolean.FALSE);
		when(modelService.isNew(model)).thenReturn(Boolean.FALSE);
		when(ctx.isModified(model, B2BUnitModel.B2BUNITLEVEL)).thenReturn(Boolean.TRUE);
		gpB2BUnitModelValidateInterceptor.onValidate(model, ctx);
		verify(modelService, times(2)).save(child);
		verify(gpB2BUnitsService, never()).updateUserGroupByUnitLevel(unitLevels, usersList);
	}

	@Test
	public void testValidateApproversNull() throws InterceptorException
	{
		when(userService.isMemberOfGroup(approver, userGroupModel)).thenReturn(Boolean.FALSE);
		when(ctx.isModified(model, B2BUnitModel.B2BUNITLEVEL)).thenReturn(Boolean.TRUE);
		when(modelService.isNew(model)).thenReturn(Boolean.FALSE);
		model.setApprovers(null);
		gpB2BUnitModelValidateInterceptor.onValidate(model, ctx);
		verify(modelService, times(2)).save(child);
		verify(gpB2BUnitsService).updateUserGroupByUnitLevel(unitLevels, usersList);
	}

	@Test
	public void testValidateApproversEmpty() throws InterceptorException
	{
		when(userService.isMemberOfGroup(approver, userGroupModel)).thenReturn(Boolean.FALSE);
		when(modelService.isNew(model)).thenReturn(Boolean.FALSE);
		when(ctx.isModified(model, B2BUnitModel.B2BUNITLEVEL)).thenReturn(Boolean.TRUE);
		model.setApprovers(new HashSet());
		gpB2BUnitModelValidateInterceptor.onValidate(model, ctx);
		verify(modelService, times(2)).save(child);
		verify(gpB2BUnitsService).updateUserGroupByUnitLevel(unitLevels, usersList);
	}

}

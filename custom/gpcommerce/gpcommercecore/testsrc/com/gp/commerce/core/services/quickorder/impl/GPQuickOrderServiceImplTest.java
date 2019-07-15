package com.gp.commerce.core.services.quickorder.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import com.gp.commerce.core.model.GPCustomerMaterialInfoModel;

import com.gp.commerce.core.quickorder.dao.GPQuickOrderDao;
import com.gp.commerce.core.services.quickorder.impl.GPQuickOrderServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

@UnitTest
public class GPQuickOrderServiceImplTest {

	@InjectMocks
	private final GPQuickOrderServiceImpl service = new GPQuickOrderServiceImpl();
	
	@Mock
	private GPQuickOrderDao gpQuickOrderDao;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		service.setGpQuickOrderDao(gpQuickOrderDao);
	}
	
	@Test
	public void testGetGpQuickOrderDao()
	{
		Assert.assertNotNull(service.getGpQuickOrderDao());
	}
	
	@Test
	public void testGetMaterialInfoForB2BUnit()
	{
		List<GPCustomerMaterialInfoModel> GPCustomerMaterialInfoModels = new ArrayList<>();
		GPCustomerMaterialInfoModels.add(Mockito.mock(GPCustomerMaterialInfoModel.class));
		String b2bUnit = "unit";
		String productCodes = "code";
		Mockito.when(gpQuickOrderDao.getMaterialInfoForB2BUnit(b2bUnit, productCodes)).thenReturn(GPCustomerMaterialInfoModels);
		Assert.assertNotNull(service.getMaterialInfoForB2BUnit(b2bUnit, productCodes));
	}
}

package com.gp.commerce.core.services.permission.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.services.permission.impl.GPDefaultB2BOrderThresholdTimespanEvaluationStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.util.B2BDateUtils;
import de.hybris.platform.b2b.model.B2BOrderThresholdTimespanPermissionModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.b2b.dao.B2BOrderDao;
import de.hybris.platform.b2b.enums.B2BPeriodRange;
import de.hybris.platform.util.StandardDateRange;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.b2b.model.B2BPermissionResultModel;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.HashSet;
import java.util.Set;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.lang.Double;

import javax.annotation.Resource;

@UnitTest
public class GPDefaultB2BOrderThresholdTimespanEvaluationStrategyTest {

     @InjectMocks
    private GPDefaultB2BOrderThresholdTimespanEvaluationStrategy strategy = new GPDefaultB2BOrderThresholdTimespanEvaluationStrategy();
     
     @Mock
     private ModelService modelService;
     
     
     private B2BDateUtils b2bDateUtils = new B2BDateUtils();
     
     @Mock
     private B2BOrderDao b2bOrderDao;
     
    @Before
    public void setUp() throws Exception {
         MockitoAnnotations.initMocks(this);
         strategy.setModelService(modelService);
         strategy.setB2bDateUtils(b2bDateUtils);
         strategy.setB2bOrderDao(b2bOrderDao);
    }
    
    @Test
    public void testEvaluate()
    {
        final AbstractOrderModel order = Mockito.mock(AbstractOrderModel.class);
        final B2BCustomerModel employee = Mockito.mock(B2BCustomerModel.class);
        Mockito.when(employee.getUid()).thenReturn("employeeID");
        
        B2BUnitModel unit = Mockito.mock(B2BUnitModel.class);
        Mockito.when(order.getUnit()).thenReturn(unit);
        
        B2BOrderThresholdTimespanPermissionModel permission = Mockito.mock(B2BOrderThresholdTimespanPermissionModel.class);
        Set<B2BPermissionModel> permissions = new HashSet<>();
        permissions.add(permission);
        Mockito.when(unit.getPermissions()).thenReturn(permissions);
        Mockito.when(permission.getThreshold()).thenReturn(new Double(10));
        
        B2BPeriodRange range = B2BPeriodRange.DAY;
        Mockito.when(permission.getRange()).thenReturn(range);
        StandardDateRange dateRange = Mockito.mock(StandardDateRange.class);
        
        UserModel user = Mockito.mock(UserModel.class);
        Mockito.when(order.getUser()).thenReturn(user);
        Mockito.when(dateRange.getStart()).thenReturn(new Date());
        Mockito.when(dateRange.getEnd()).thenReturn(new Date());
        
        List<OrderModel> approvedOrdersForDateRange = new ArrayList<>();
        OrderModel orderModel = Mockito.mock(OrderModel.class);
        approvedOrdersForDateRange.add(orderModel);
        Mockito.when(b2bOrderDao.findOrdersApprovedByDateRange(order.getUser(), dateRange.getStart(),
                dateRange.getEnd())).thenReturn(approvedOrdersForDateRange);
        
        Mockito.when(order.getTotalPrice()).thenReturn(new Double(20));
        
        B2BPermissionResultModel permissionResultModel = Mockito.mock(B2BPermissionResultModel.class);
        Mockito.when(modelService.create(B2BPermissionResultModel.class)).thenReturn(permissionResultModel);
        
        Assert.assertNotNull(strategy.evaluate(order, employee));
    }
}
package com.gp.commerce.core.services.permission.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.services.permission.impl.GPDefaultB2BOrderThresholdEvaluationStrategy;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.model.B2BOrderThresholdPermissionModel;
import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.b2b.model.B2BPermissionResultModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.model.ModelService;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.HashSet;
import java.util.Set;
import java.lang.Double;

@UnitTest
public class GPDefaultB2BOrderThresholdEvaluationStrategyTest {

     @InjectMocks
    private GPDefaultB2BOrderThresholdEvaluationStrategy strategy = new GPDefaultB2BOrderThresholdEvaluationStrategy();
     
     @Mock
     private ModelService modelService;
     
    @Before
    public void setUp() throws Exception {
         MockitoAnnotations.initMocks(this);
         strategy.setModelService(modelService);
    }
    
    @Test
    public void testEvaluate()
    {
        final AbstractOrderModel order = Mockito.mock(AbstractOrderModel.class);
        final B2BCustomerModel employee = Mockito.mock(B2BCustomerModel.class);
        Mockito.when(employee.getUid()).thenReturn("employeeID");
        
        B2BUnitModel unit = Mockito.mock(B2BUnitModel.class);
        Mockito.when(order.getUnit()).thenReturn(unit);
        
        B2BOrderThresholdPermissionModel permission = Mockito.mock(B2BOrderThresholdPermissionModel.class);
        Set<B2BPermissionModel> permissions = new HashSet<>();
        permissions.add(permission);
        Mockito.when(unit.getPermissions()).thenReturn(permissions);
        
        Mockito.when(permission.getThreshold()).thenReturn(new Double(10));
        Mockito.when(order.getTotalPrice()).thenReturn(new Double(20));
        
        B2BPermissionResultModel permissionResultModel = Mockito.mock(B2BPermissionResultModel.class);
        Mockito.when(modelService.create(B2BPermissionResultModel.class)).thenReturn(permissionResultModel);
        
        Assert.assertNotNull(strategy.evaluate(order, employee));
    }
}
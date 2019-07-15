/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.gp.commerce.core.util.GPSiteConfigUtils;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.enums.B2BPermissionTypeEnum;
import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionData;
import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionTypeData;
import de.hybris.platform.servicelayer.dto.converter.Converter;

@UnitTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(GPSiteConfigUtils.class )
public class GPB2BPermissionPopulatorTest {
	
	@Mock
	Converter<B2BPermissionTypeEnum, B2BPermissionTypeData> b2bPermissionTypeConverter;
	@Mock
	private B2BPermissionModel source;
	@Mock
	private B2BPermissionData target;
	
	@InjectMocks
	GPB2BPermissionPopulator gpb2bPermissionPopulator=new GPB2BPermissionPopulator();

	@Before
	public void setup() {
		
		MockitoAnnotations.initMocks(this);
		gpb2bPermissionPopulator.setB2BPermissionTypeConverter(b2bPermissionTypeConverter);
		
		Mockito.when(target.getValue()).thenReturn(10.0);
		Mockito.when(source.getCode()).thenReturn("PermissionModel");
		Mockito.when(source.getItemtype()).thenReturn(B2BPermissionTypeEnum.B2BBUDGETEXCEEDEDPERMISSION.getCode());
		
		PowerMockito.mockStatic(GPSiteConfigUtils.class);
		Mockito.when(GPSiteConfigUtils.getDecimalFormatValue(10.0)).thenReturn("10.0");
		Mockito.when(b2bPermissionTypeConverter.convert(B2BPermissionTypeEnum.B2BBUDGETEXCEEDEDPERMISSION)).thenReturn(Mockito.mock(B2BPermissionTypeData.class));
			
	}
	
	@Test
	public void testPopulate()
	{
		gpb2bPermissionPopulator.populate(source, target);
		Mockito.verify(target).setFormattedValue("10.0");
	}
}

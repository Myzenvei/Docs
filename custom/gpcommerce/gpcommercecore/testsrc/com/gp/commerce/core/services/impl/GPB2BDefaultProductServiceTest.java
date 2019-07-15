package com.gp.commerce.core.services.impl;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.product.dao.impl.GPDefaultProductDao;
import com.gp.commerce.core.product.services.impl.GPB2BDefaultProductService;

import de.hybris.bootstrap.annotations.UnitTest;

@UnitTest
public class GPB2BDefaultProductServiceTest {
	
	private static final String PRODUCTCODE = "553637";
	private static final String B2BUNITCODE = "43214";
	
	@InjectMocks
	private final GPB2BDefaultProductService service = new GPB2BDefaultProductService();;
	
	@Mock
	private GPDefaultProductDao gpProductDao;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetCMIRCodeForProductAndB2BUnit() {
		
		when(gpProductDao.getCMIRCodeForProductAndB2BUnit(PRODUCTCODE, B2BUNITCODE)).thenReturn("123456");
		
		String cmirCode = service.getCMIRCodeForProductAndB2BUnit(PRODUCTCODE, B2BUNITCODE);
		
		assertTrue("123456".equals(cmirCode));
	}

}

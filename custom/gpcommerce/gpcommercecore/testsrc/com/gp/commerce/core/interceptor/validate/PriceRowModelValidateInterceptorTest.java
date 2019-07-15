package com.gp.commerce.core.interceptor.validate;

import org.mockito.MockitoAnnotations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;

/**
 * @author rtarigopula
 *
 */

@UnitTest
public class PriceRowModelValidateInterceptorTest {
	
	@InjectMocks
	PriceRowModelValidateInterceptor priceRowModelValidateInterceptor= new PriceRowModelValidateInterceptor();
	
	@Mock
	private PriceRowModel priceRowModel;
	
	@Mock
	private InterceptorContext interceptorContext;
	
	Double mapPrice = new Double(12345.87);
	Double webPrice= new Double(123567);
	Double price= new Double(46466);
	
	@Before
	public void setUp()
	{
		
		MockitoAnnotations.initMocks(this);
		Mockito.when(priceRowModel.getMapPrice()).thenReturn(mapPrice);		
		Mockito.when(priceRowModel.getWeblistPrice()).thenReturn(webPrice);
		Mockito.when(priceRowModel.getPrice()).thenReturn(price);
	}
	
	@Test
	public void onValidateTest() throws InterceptorException
	{
		priceRowModelValidateInterceptor.onValidate(priceRowModel,interceptorContext);
		Assert.assertNotNull(priceRowModel);
		Assert.assertNotNull(interceptorContext);
	}

}

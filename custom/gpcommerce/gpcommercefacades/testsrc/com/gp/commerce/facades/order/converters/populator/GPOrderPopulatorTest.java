	package com.gp.commerce.facades.order.converters.populator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;

public class GPOrderPopulatorTest {
	
	@InjectMocks
	private GPOrderPopulator populator=new GPOrderPopulator();
	
	@Mock
	private PriceDataFactory priceDataFactory;
	
	AbstractOrderModel source=mock(CartModel.class);
	AbstractOrderData target=new CartData();
	CurrencyModel currency=mock(CurrencyModel.class);
	PriceData price =new PriceData();
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		when(source.getSubtotal()).thenReturn(100d);
		when(source.getCurrency()).thenReturn(currency);
		when(priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(100d), currency)).thenReturn(price);
		
	}

	@Test
	public void testPopulate() {
		populator.populate(source, target);
		
		assertNotNull(target);
	}

	@Test
	public void testPopulateException() {
		when(source.getCurrency()).thenReturn(null);

		boolean isException =false;
		try {
			populator.populate(source, target);
		}catch (Exception e) {
			isException = true;
		}
		assertTrue(isException);
	}

}

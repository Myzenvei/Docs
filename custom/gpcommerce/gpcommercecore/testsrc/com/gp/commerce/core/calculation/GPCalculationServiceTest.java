package com.gp.commerce.core.calculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;

import com.gp.commerce.core.calculation.service.impl.GPCalculationService;
import com.gp.commerce.core.model.SplitEntryModel;
import com.gp.commerce.core.price.service.impl.GPDefaultEurope1PriceFactory;
import org.mockito.Spy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.order.strategies.calculation.OrderRequiresCalculationStrategy;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.TaxValue;

@UnitTest
public class GPCalculationServiceTest {
	@InjectMocks
	GPCalculationService gpCalculationService = new GPCalculationService();
	@Mock
	private GPDefaultEurope1PriceFactory gpPriceService;
	@Mock
	private OrderRequiresCalculationStrategy orderRequiresCalculationStrategy;
	@Mock
	private CommonI18NService commonI18NService;

	private boolean recalculate;
	@Mock
	private OrderModel order;
	private List<PriceRow> productPrices;
	private PriceRow productPrice;
	@Mock
	private ModelService modelService;

	private PriceRowModel priceRowModel;

	private ProductModel productModel;

	private SplitEntryModel splitEntry;
	private DeliveryModeModel deliveryMode;
	boolean isDeliveryCostChanged;
	@Mock
	CurrencyModel currency;
	Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap2;

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		order = Mockito.mock(OrderModel.class);
		currency = Mockito.mock(CurrencyModel.class);
		order.setCurrency(currency);
		currency.setDigits(new Integer(2));
		AbstractOrderEntryModel entry = Mockito.mock(AbstractOrderEntryModel.class);
		entry.setQuantity(new Long(2));
		productModel = Mockito.mock(ProductModel.class);
		entry.setProduct(productModel);
		List<AbstractOrderEntryModel> entries = new ArrayList<AbstractOrderEntryModel>();
		entries.add(entry);
		order.setEntries(entries);
		recalculate = true;
		PriceRow priceRow = Mockito.mock(PriceRow.class);

		priceRowModel = Mockito.mock(PriceRowModel.class);
		priceRowModel.setPrice(new Double(10));
		priceRowModel.setWeblistPrice(new Double(12));

		productPrices = new ArrayList<PriceRow>();
		productPrices.add(priceRow);
		splitEntry = Mockito.mock(SplitEntryModel.class);
		deliveryMode = Mockito.mock(DeliveryModeModel.class);
		deliveryMode.setDeliveryPrice("10");
		isDeliveryCostChanged = true;
		order.setDeliveryCost(new Double(10));
		order.setSubtotal(new Double(10));
		order.setAdditionalDiscount(new Double(10));
		TaxValue tax = Mockito.mock(TaxValue.class);

		Set<TaxValue> taxValue=new HashSet<TaxValue>();
		taxValue.add(tax);
		Map<Set<TaxValue>, Double> taxValueMap=new HashMap<Set<TaxValue>, Double>();
		taxValueMap.put(taxValue, new Double(10));
		taxValueMap2=new HashMap<TaxValue, Map<Set<TaxValue>, Double>>();
		taxValueMap2.put(tax,taxValueMap);
	}

	@Test
	public void setCommonI18NServiceTest() {
		CommonI18NService commonI18NService = Mockito.mock(CommonI18NService.class);

		GPCalculationService spy = Mockito.spy(new GPCalculationService());
		Mockito.doNothing().when((GPCalculationService) spy).setCommonI18NService(commonI18NService);

	}

	@Test
	public void setOrderRequiresCalculationStrategyTest() {
		GPCalculationService spy = Mockito.spy(new GPCalculationService());
		OrderRequiresCalculationStrategy orderRequiresCalculationStrategy = Mockito
				.mock(OrderRequiresCalculationStrategy.class);
		Mockito.doNothing().when((GPCalculationService) spy)
				.setOrderRequiresCalculationStrategy(orderRequiresCalculationStrategy);

	}

	@Test
	public void calculateTotalsTest() throws Exception {
		Mockito.when(order.getCurrency()).thenReturn(currency);
		Mockito.doReturn(true).when(orderRequiresCalculationStrategy).requiresCalculation(order);
		ProductModel product = Mockito.mock(ProductModel.class);

		Mockito.doReturn(productPrices).when(gpPriceService).getPriceInformationsForProduct(product);
		productPrice = productPrices.get(0);
		 PK pk = PK.fromLong(1234L);
		Mockito.when(productPrice.getPK()).thenReturn(pk);
		Mockito.when(modelService.get(pk)).thenReturn(priceRowModel);

		Mockito.doNothing().when(modelService).save(order);

		Mockito.doNothing().when(modelService).refresh(order);
		double totalDiscounts = new Double(10);


		double roundedTotalDiscounts = new Double(10);

		Mockito.doReturn(roundedTotalDiscounts).when(commonI18NService).roundCurrency(totalDiscounts, 2);

		gpCalculationService.calculateTotals(order,recalculate,taxValueMap2);
	}
}

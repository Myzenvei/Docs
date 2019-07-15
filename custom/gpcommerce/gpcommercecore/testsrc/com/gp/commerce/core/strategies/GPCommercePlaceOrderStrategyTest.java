package com.gp.commerce.core.strategies;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.enums.PermissionStatus;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.b2b.model.B2BPermissionResultModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BPermissionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.externaltax.ExternalTaxesService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.TaxValue;
import com.gp.commerce.core.exceptions.GPInsufficientStockLevelException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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

import com.gp.commerce.core.model.SplitEntryModel;
import com.gp.commerce.core.stock.services.GPStockService;

@UnitTest
public class GPCommercePlaceOrderStrategyTest {

	@InjectMocks
	private final GPCommercePlaceOrderStrategy strategy = new GPCommercePlaceOrderStrategy();

	@Mock
	private KeyGenerator consignmentCodeGenerator;

	@Mock
	private CalculationService calculationService;

	@Mock
	private OrderService orderService;

	@Mock
	private TimeService timeService;

	@Mock
	private BaseSiteService baseSiteService;

	@Mock
	private BaseStoreService baseStoreService;

	@Mock
	private CommonI18NService commonI18NService;

	@Mock
	private ModelService modelService;

	@Mock
	private PromotionsService promotionsService;

	@Mock
	private ExternalTaxesService externalTaxesService;

	@Mock
	private B2BPermissionService<B2BCustomerModel, B2BPermissionResultModel> b2bPermissionService;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private GPStockService stockService;

	@Before
	public void setUp() throws Exception {
 		MockitoAnnotations.initMocks(this);
 		strategy.setConsignmentCodeGenerator(consignmentCodeGenerator);
	}

	@Test
	public void testGetConsignmentCodeGenerator()
	{
		Assert.assertNotNull(strategy.getConsignmentCodeGenerator());
	}

	@Test
	public void testPlaceOrder() throws InvalidCartException, CalculationException ,GPInsufficientStockLevelException
	{
		final CommerceCheckoutParameter parameter = Mockito.mock(CommerceCheckoutParameter.class);

		final CartModel cartModel = Mockito.mock(CartModel.class);
		Mockito.when(parameter.getCart()).thenReturn(cartModel);
		Mockito.when(calculationService.requiresCalculation(cartModel)).thenReturn(Boolean.TRUE);
		Mockito.when(cartModel.getCode()).thenReturn("cart1");
		Mockito.when(cartModel.getCalculated()).thenReturn(true);
		final CustomerModel customer = Mockito.mock(CustomerModel.class);
		Mockito.when(cartModel.getUser()).thenReturn(customer);

		final OrderModel orderModel = Mockito.mock(OrderModel.class);
		Mockito.when(orderService.createOrderFromCart(cartModel)).thenReturn(orderModel);
		Mockito.when(timeService.getCurrentTime()).thenReturn(new Date());

		final BaseSiteModel currentBaseSite = Mockito.mock(BaseSiteModel.class);
		Mockito.when(currentBaseSite.getChannel()).thenReturn(SiteChannel.B2B);
		Mockito.when(baseSiteService.getCurrentBaseSite()).thenReturn(currentBaseSite);

		final BaseStoreModel curretnBaseStore = Mockito.mock(BaseStoreModel.class);
		Mockito.when(baseStoreService.getCurrentBaseStore()).thenReturn(curretnBaseStore);

		final LanguageModel currentLanguage = Mockito.mock(LanguageModel.class);
		Mockito.when(commonI18NService.getCurrentLanguage()).thenReturn(currentLanguage);
		Mockito.when(parameter.getSalesApplication()).thenReturn(SalesApplication.WEB);

		final List<AbstractOrderEntryModel> entries = new ArrayList<>();
		final AbstractOrderEntryModel entry = Mockito.mock(AbstractOrderEntryModel.class);
		entries.add(entry);

		final List<SplitEntryModel> splitEntry = new ArrayList<>();
		final SplitEntryModel spltEntry = Mockito.mock(SplitEntryModel.class);
		splitEntry.add(spltEntry);
		Mockito.when(entry.getSplitEntry()).thenReturn(splitEntry);
		final AddressModel deliveryAddress = Mockito.mock(AddressModel.class);
		Mockito.when(spltEntry.getDeliveryAddress()).thenReturn(deliveryAddress);
		final PK pk = PK.BIG_PK;
		Mockito.when(deliveryAddress.getPk()).thenReturn(pk);
		Mockito.when(entry.getEntryNumber()).thenReturn(new Integer(1));

		final ConsignmentModel consignmentModel = Mockito.mock(ConsignmentModel.class);
		Mockito.when(modelService.create(ConsignmentModel.class)).thenReturn(consignmentModel);
		Mockito.when(consignmentCodeGenerator.generate()).thenReturn("consignment1");

		final List<WarehouseModel> warehouses = new ArrayList<>();
		final WarehouseModel warehouse = Mockito.mock(WarehouseModel.class);
		warehouses.add(warehouse);
		Mockito.when(orderModel.getStore()).thenReturn(curretnBaseStore);
		Mockito.when(curretnBaseStore.getWarehouses()).thenReturn(warehouses);
		Mockito.doNothing().when(modelService).save(consignmentModel);

		final ConsignmentEntryModel consignmentEntryModel = Mockito.mock(ConsignmentEntryModel.class);
		Mockito.when(modelService.create(ConsignmentEntryModel.class)).thenReturn(consignmentEntryModel);
		Mockito.when(spltEntry.getEntryNumber()).thenReturn(new Integer(1));
		Mockito.when(spltEntry.getQty()).thenReturn("1");
		Mockito.when(spltEntry.getTotalTax()).thenReturn(new Double(1.0));

		final Collection<TaxValue> taxValues = new ArrayList<>();
		taxValues.add(Mockito.mock(TaxValue.class));
		Mockito.when(spltEntry.getTaxValues()).thenReturn(taxValues);
		Mockito.doNothing().when(modelService).save(consignmentEntryModel);
		Mockito.doNothing().when(modelService).saveAll(customer, orderModel);

		final PaymentInfoModel paymentInfo = Mockito.mock(PaymentInfoModel.class);
		Mockito.when(cartModel.getPaymentInfo()).thenReturn(paymentInfo);
		final AddressModel billingAddress = Mockito.mock(AddressModel.class);
		Mockito.when(paymentInfo.getBillingAddress()).thenReturn(billingAddress);
		Mockito.when(orderModel.getPaymentInfo()).thenReturn(paymentInfo);
		Mockito.doNothing().when(modelService).save(paymentInfo);

		Mockito.when(currentBaseSite.getUid()).thenReturn("siteID");

		final B2BUnitModel b2bUnit = Mockito.mock(B2BUnitModel.class);
		Mockito.when(deliveryAddress.getB2bUnit()).thenReturn(b2bUnit);
		final B2BCustomerModel user = Mockito.mock(B2BCustomerModel.class);
		Mockito.when(orderModel.getUser()).thenReturn(user);

		final Set<B2BPermissionResultModel> evaluatePermissions = new HashSet<>();
		final B2BPermissionResultModel item = Mockito.mock(B2BPermissionResultModel.class);
		evaluatePermissions.add(item);

		Mockito.when(b2bPermissionService.evaluatePermissions(Mockito.any(OrderModel.class),
				Mockito.any(B2BCustomerModel.class), Mockito.anyList())).thenReturn(evaluatePermissions);

		final B2BPermissionModel permission = Mockito.mock(B2BPermissionModel.class);
		Mockito.when(item.getPermission()).thenReturn(permission);
		Mockito.when(item.getStatus()).thenReturn(PermissionStatus.PENDING_APPROVAL);

		Mockito.doNothing().when(modelService).save(orderModel);
		Mockito.doNothing().when(promotionsService).transferPromotionsToOrder(cartModel, orderModel, false);
		Mockito.doNothing().when(calculationService).calculateTotals(orderModel, false);
		Mockito.doNothing().when(modelService).refresh(orderModel);
		Mockito.doNothing().when(modelService).refresh(customer);
		Mockito.doNothing().when(orderService).submitOrder(orderModel);
		Mockito.doNothing().when(externalTaxesService).clearSessionTaxDocument();
		Mockito.doNothing().when(stockService).reserveStockForOrder(orderModel);

		Assert.assertNotNull(strategy.placeOrder(parameter));
	}
}

/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.process.email.context;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.store.BaseStoreModel;

@UnitTest
public class OrderRefundEmailContextTest {

	@InjectMocks
	private OrderRefundEmailContext orderRefundEmail = new OrderRefundEmailContext();
	@Mock
	private Converter<OrderModel, OrderData> orderConverter;
	private OrderData orderData;
	private String orderCode;
	private String orderGuid;
	private boolean guest;
	private String storeName;
	private BigDecimal refundAmount;
	@Mock
	private OrderProcessModel orderProcessModel;
	@Mock
	private CustomerModel customerModel;
	@Mock
	private UserModel userModel;
	private EmailPageModel emailPageModel;
	@Mock
	private AbstractEmailContext emailContext;

	private OrderModel orderModel;

	private BaseStoreModel store;
	@Mock
	private CustomerEmailResolutionService customerEmailResolutionService;
	@Mock
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;
	@Mock
	private Date date;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		emailPageModel = new EmailPageModel();
		ItemModelContextImpl itemModelContext = (ItemModelContextImpl) emailPageModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
		store = new BaseStoreModel();
		ItemModelContextImpl storeItemModelContext = (ItemModelContextImpl) store.getItemModelContext();
		storeItemModelContext.setLocaleProvider(localeProvider);
		orderData = new OrderData();
		orderProcessModel = new OrderProcessModel();
		customerModel = new CustomerModel();
		userModel = new UserModel();
		customerModel.setType(CustomerType.GUEST);
		orderModel = new OrderModel();

		store.setName("store name", Locale.ENGLISH);
		orderModel.setCode("testOrder");
		orderModel.setUser(customerModel);
		orderModel.setStore(store);
		orderProcessModel.setOrder(orderModel);

		emailPageModel.setFromEmail("test@tes.com", Locale.ENGLISH);
		date = new Date();
		orderData.setCreated(date);
	}

	@Test
	public void testInit() throws CommerceCartRestorationException {
		OrderRefundEmailContext spy = Mockito.spy(new OrderRefundEmailContext());
		Mockito.doNothing().when((AbstractEmailContext<OrderProcessModel>) spy).init(orderProcessModel, emailPageModel);
		Mockito.doReturn(orderData).when(orderConverter).convert(orderProcessModel.getOrder());
		orderCode = orderProcessModel.getOrder().getCode();
		orderGuid = orderProcessModel.getOrder().getGuid();
		Mockito.doReturn(customerModel).when(spy).getCustomer(orderProcessModel);
		Assert.assertEquals(CustomerType.GUEST, customerModel.getType());
		Mockito.doReturn(orderData).when(orderConverter).convert(orderProcessModel.getOrder());
		refundAmount = orderProcessModel.getRefundAmount();

		orderRefundEmail.init(orderProcessModel, emailPageModel);
	}

}

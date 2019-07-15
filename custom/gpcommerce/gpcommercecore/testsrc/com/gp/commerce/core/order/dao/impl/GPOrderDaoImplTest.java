package com.gp.commerce.core.order.dao.impl;

import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.model.GPEndUserLegalTermsModel;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import de.hybris.platform.store.BaseStoreModel;

@UnitTest
public class GPOrderDaoImplTest {

	@InjectMocks
	GPOrderDaoImpl dao = new GPOrderDaoImpl();
	
	@Mock
	FlexibleSearchService flexibleSearchService;
	
	@Mock
	CMSSiteService cmsSiteService;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		dao.setFlexibleSearchService(flexibleSearchService);
	}

	@Test
	public void testGetOrdersInError() {
		CMSSiteModel site = Mockito.mock(CMSSiteModel.class);
		OrderModel order = new OrderModel();
		order.setCode("orderId");
		OrderStatus orderProcessingStatus = Mockito.mock(OrderStatus.class);
		List<OrderModel>  orders = new ArrayList<>();
		orders.add(order);
		SearchResult searchResult = new SearchResultImpl<>(orders, 1, 1, 1);
 		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
 		
		Assert.assertEquals("orderId",dao.getOrdersInError(site, orderProcessingStatus).get(0).getCode());
	}

	@Test
	public void testGetConsignmentsInError() {
		CMSSiteModel site = Mockito.mock(CMSSiteModel.class);
		ConsignmentModel consignment = new ConsignmentModel();
		consignment.setCode("code");
		ConsignmentStatus consignProcessingStatus = Mockito.mock(ConsignmentStatus.class);
		List<ConsignmentModel> consignments = new ArrayList<>();
		consignments.add(consignment);
		
		SearchResult result = new SearchResultImpl<>(consignments, 1, 1, 1);
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(result);
		Assert.assertEquals("code",dao.getConsignmentsInError(site, consignProcessingStatus).get(0).getCode());
	}

	@Test
	public void testGetLeaseAgreementById() {
		String leaseId = "leaseId";
		GPEndUserLegalTermsModel legalTermsModel = new GPEndUserLegalTermsModel();
		List<GPEndUserLegalTermsModel> legalTerms = new ArrayList<>();
		legalTerms.add(legalTermsModel);
		
		SearchResult searchResult = new SearchResultImpl<>(legalTerms, 1, 1, 1);
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		Assert.assertEquals(legalTerms,dao.getLeaseAgreementById(leaseId));
		
	}
	
	
	@Test
	public void testGetOrderForCode() {
		String orderCode = "00003433";
		BaseStoreModel baseStoreModel = Mockito.mock(BaseStoreModel.class);
		List<OrderModel> orders = new ArrayList<>();
		OrderModel orderModel = new OrderModel();
		orderModel.setCode(orderCode);
		orders.add(orderModel);
		SearchResult result = new SearchResultImpl<>(orders, 1, 1, 1);
		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(result);
		Assert.assertEquals("00003433",dao.getOrderForCode(orderCode, baseStoreModel).get(0).getCode());
		
		
	}

}

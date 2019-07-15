/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.order.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.GPNetsuiteOrderExportStatus;
import com.gp.commerce.core.model.GPEndUserLegalTermsModel;
import com.gp.commerce.core.order.dao.GpOrderDao;
import com.gp.commerce.facade.order.data.LeaseAgreementData;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.store.BaseStoreModel;

public class GPOrderDaoImpl implements GpOrderDao{
	
	private static final String LEASE_ID = "leaseId";
	private static final String CONSIGN_PROCESSING_STATUS = "consignProcessingStatus";
	private static final String ORDER_PROCESSING_STATUS = "orderProcessingStatus";
	private static final String ORDER_STATUS = "statusCode";
	private static final String CODE = "code";
	private static final String STORE = "store";


	private static final String QUERY_ORDERS_IN_FULFILLMENT_ERROR_STATE ="Select {" +OrderModel.PK+ "} , {"
			+OrderModel.CODE  +"} from {" +OrderModel._TYPECODE + "} where {"+ OrderModel.VERSIONID +"} is null and {" + OrderModel.SITE +" } = ?site and {" + OrderModel.PROCESSINGSTATUS + "} = ?orderProcessingStatus";
	
	private static final String QUERY_ORDERS_IN_FULFILLMENT ="SELECT {or.pk},{or.itemType} FROM {Order as or LEFT JOIN OrderStatus as os ON {or.status} = {os.PK} } "
			+ " WHERE {os.code} =?statusCode ";
	
	private static final String QUERY_CONSIGNMENTS_IN_FULFILLMENT_ERROR_STATE = "Select {" + ConsignmentModel.PK +"} from {" + ConsignmentModel._TYPECODE + " as c join " + OrderModel._TYPECODE + " as o on {o." + OrderModel.PK  + "} = {c." +ConsignmentModel.ORDER + "}} where {o:" + OrderModel.SITE + "} = ?site and {c:" + ConsignmentModel.PROCESSINGSTATUS + "} = ?consignProcessingStatus";
	private static final String FIND_ORDERS_BY_CODE_STORE_QUERY = "SELECT {" + OrderModel.PK + "}, {" + OrderModel.CREATIONTIME
			+ "}, {" + OrderModel.CODE + "} FROM {" + OrderModel._TYPECODE + "} WHERE {" + OrderModel.CODE + "} = ?code AND {"
			+ OrderModel.VERSIONID + "} IS NULL AND {" + OrderModel.STORE + "} = ?store";
	private FlexibleSearchService flexibleSearchService;
	
	/* (non-Javadoc)
	 * @see com.gp.commerce.core.order.dao.GpOrderDao#getOrdersInError(de.hybris.platform.cms2.model.site.CMSSiteModel)
	 */
	@Override
	public List<OrderModel> getOrdersInError(final CMSSiteModel site,final OrderStatus orderProcessingStatus) {
		final FlexibleSearchQuery searchQuery=new FlexibleSearchQuery(QUERY_ORDERS_IN_FULFILLMENT_ERROR_STATE);
		searchQuery.addQueryParameter(GpcommerceCoreConstants.SITE, site);
		searchQuery.addQueryParameter(ORDER_PROCESSING_STATUS, orderProcessingStatus);
		final SearchResult<OrderModel> searchResult = getFlexibleSearchService().search(searchQuery);
		return searchResult.getResult();
	}
	
	@Override
	public List<OrderModel> getOrdersByStatus(OrderStatus orderStatus){
		final FlexibleSearchQuery searchQuery=new FlexibleSearchQuery(QUERY_ORDERS_IN_FULFILLMENT);
		searchQuery.addQueryParameter(ORDER_STATUS, orderStatus.getCode());
		final SearchResult<OrderModel> searchResult = getFlexibleSearchService().search(searchQuery);
		return searchResult.getResult();
		
	}
	/* (non-Javadoc)
	 * @see com.gp.commerce.core.order.dao.GpOrderDao#getConsignmentsInError(de.hybris.platform.cms2.model.site.CMSSiteModel, de.hybris.platform.basecommerce.enums.ConsignmentStatus)
	 */
	@Override
	public List<ConsignmentModel> getConsignmentsInError(final CMSSiteModel site, final ConsignmentStatus consignProcessingStatus) {
		final FlexibleSearchQuery searchQuery=new FlexibleSearchQuery(QUERY_CONSIGNMENTS_IN_FULFILLMENT_ERROR_STATE);
		searchQuery.addQueryParameter(GpcommerceCoreConstants.SITE, site);
		searchQuery.addQueryParameter(CONSIGN_PROCESSING_STATUS, consignProcessingStatus);
		final SearchResult<ConsignmentModel> searchResult = getFlexibleSearchService().search(searchQuery);
		return searchResult.getResult();
	}
	
	/* (non-Javadoc)
	 * @see com.gp.commerce.core.order.dao.GpOrderDao#getLeaseAgreementById(java.lang.String)
	 */
	@Override
	public List<GPEndUserLegalTermsModel> getLeaseAgreementById(final String leaseId) {
		
        final String query = "SELECT {pk} from {GPEndUserLegalTerms} where {id} = ?leaseId";
        final Map<String, Object> params = new HashMap<>();
        params.put(LEASE_ID, leaseId);
        
        final FlexibleSearchQuery flexiQry = new FlexibleSearchQuery(query);
        flexiQry.getQueryParameters().putAll(params); 
        
        return getFlexibleSearchService().<GPEndUserLegalTermsModel>search(flexiQry).getResult();
		
	}
	
	
	public List<OrderModel> getOrderForCode(String code, BaseStoreModel baseStoreModel) {
		final FlexibleSearchQuery searchQuery=new FlexibleSearchQuery(FIND_ORDERS_BY_CODE_STORE_QUERY);
		searchQuery.addQueryParameter(CODE, code);
		searchQuery.addQueryParameter(STORE, baseStoreModel);
		final SearchResult<OrderModel> searchResult = getFlexibleSearchService().search(searchQuery);
		return searchResult.getResult();
	}


	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}

	@Override
	public List<OrderModel> getOrdersInErrorOfNetsuiteAndPayment(CMSSiteModel site) {
		StringBuilder queryBuilder=new StringBuilder();
		queryBuilder.append("select tbl.pk from (");
		queryBuilder.append("{{ SELECT {pk} FROM {order as o join GPNetsuiteOrderExportStatus as g on {o.netsuiteReplicationStatus}={g.pk} } WHERE {g.code} IN ('FAILURE','NOT_ABLE_TO_PROCESS','FAILED_CUSTOMER_REPLICATION') AND {o.site}=?site }}");
		queryBuilder.append(" UNION ALL ");
		queryBuilder.append("{{ SELECT {pk} FROM {order as o join GPNetsuiteOrderExportStatus as g on {o.netsuiteReplicationStatus}={g.pk} join orderstatus as s on {o.status}={s.pk}} WHERE {g.code} IN ('NOTEXPORTED') AND {s.code} IN ('ON_VALIDATION') AND {o.site}=?site }}");
		queryBuilder.append(" UNION ALL ");
		queryBuilder.append("{{ SELECT {pk} from {order as o join orderstatus as s on {o.status}={s.pk}} WHERE {s.code} IN ('PAYMENT_ERROR') AND {o.site}=?site }}");
		queryBuilder.append(")tbl");
		
		final String query =queryBuilder.toString();
		final FlexibleSearchQuery searchQuery=new FlexibleSearchQuery(query);
		searchQuery.addQueryParameter(GpcommerceCoreConstants.SITE, site);
		final SearchResult<OrderModel> searchResult = getFlexibleSearchService().search(searchQuery);
		return searchResult.getResult();
	}

}

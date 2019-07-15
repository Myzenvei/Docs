/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.cronjob.netsuite;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.common.math.DoubleMath;
import com.gp.commerce.core.enums.GPNetsuiteOrderExportStatus;
import com.gp.commerce.core.model.SplitEntryModel;
import com.gp.commerce.core.strategies.GPDefaultCustomerNameStrategy;
import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.dto.netsuite.GPNetsuiteAddressDTO;
import com.gpintegration.dto.netsuite.GPNetsuiteCustomerDTO;
import com.gpintegration.dto.netsuite.GPNetsuiteOrderDTO;
import com.gpintegration.dto.netsuite.GPNetsuiteOrderItemDTO;
import com.gpintegration.dto.netsuite.GPNetsuiteResponseDTO;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.service.impl.GPDefaultNetsuiteReplicationService;
import com.gpintegration.utils.GPIntegrationUtils;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionService;

/**
 * @author spandiyan
 *
 */
public class GPNetsuiteOrderReplicationJob extends AbstractJobPerformable<CronJobModel>{
	
	private static final String ORDER_STATUS = "orderStatus";

	private static final String REMORSE_PERIOD = "remorsePeriod";

	private static final Logger LOG = Logger.getLogger(GPNetsuiteOrderReplicationJob.class);
	
	private GPDefaultNetsuiteReplicationService gpNetsuiteReplicationService;
	
	private SessionService sessionService;
	
	private ConfigurationService configurationService;

	public static final String NETSUITE_ORDERS_REPLICATION_QUERY = "SELECT {o.pk} FROM {Order as o} WHERE {o.netsuiteReplicationStatus} = ?netsuiteReplicationStatus AND {o.creationtime} <= ?remorsePeriod AND {o.status} NOT IN (?orderStatus)";
	
	public static final String NETSUITE_REMORSE_PERIOD_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static final String NETSUITE_RESPONSE_SUCCESS = "success";
	
	public static final String FAILED_CUSTOMER_REPLICATION = "FAILED_CUSTOMER_REPLICATION";
	
	public static final String NOT_ABLE_TO_PROCESS = "NOT_ABLE_TO_PROCESS";
	
	private static final String DECIMAL_FORMAT = "##.00";
	
	private static final double EPSILON = 0.0d;
	
	@Resource(name = "gpDefaultCustomerNameStrategy")
	private GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy;
	
	public PerformResult perform(final CronJobModel cronJobModel) {
		LOG.info("Hybris to Netsuite Customer and Order CronJob started sucessfully");
		
		if(configurationService.getConfiguration().getBoolean(GpintegrationConstants.NETSUITE_REPLICATION_FEATURE)) {
			try {
				List<OrderModel> netsuiteOrders = getOrdersForNetsuiteReplication();
				if(null!=netsuiteOrders && CollectionUtils.isNotEmpty(netsuiteOrders)) {
					LOG.info("Total nubmber of records matched for replication:"+netsuiteOrders.size());
					for(OrderModel orderModel: netsuiteOrders) {
						try {
							LOG.debug("Replicating customer and order for order number:"+orderModel.getCode()+": Order Creation time:"+orderModel.getCreationtime());
							if (clearAbortRequestedIfNeeded(cronJobModel))
							{
								LOG.debug("The job is aborted.");
								return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
							}
							replicateCustomerAndOrder(orderModel);
						} catch(Exception exp) {
							LOG.error("Replicating customer and order failed for order number:"+orderModel.getCode()+"", exp);
						}
					}
				} else {
					LOG.info("No orders found for netsuite replication.");
				}
				
			}catch(Exception exception) {
				LOG.error("Netsuite customer and order replication failed with exception", exception);
			}
		} else {
			LOG.info("Netsuite customer and order replication feature is disabled.");
		}
		LOG.info("Hybris to Netsuite Customer and Order CronJob completed successfully");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}
	
	
	/**
	 * Method to fetch the order details from order table.
	 * 
	 * @return List<OrderModel>
	 */
	private List<OrderModel> getOrdersForNetsuiteReplication(){
		final FlexibleSearchQuery netsuiteOrderExportQuery = new FlexibleSearchQuery(NETSUITE_ORDERS_REPLICATION_QUERY);
		netsuiteOrderExportQuery.addQueryParameter("netsuiteReplicationStatus", GPNetsuiteOrderExportStatus.NOTEXPORTED);
		String remorsePeriod = calculateRemorsePeriod();
		LOG.debug("Calculated remorse period:"+remorsePeriod);
		netsuiteOrderExportQuery.addQueryParameter(REMORSE_PERIOD, remorsePeriod);
		netsuiteOrderExportQuery.addQueryParameters(getOrderStatusFilter());
		final SearchResult<OrderModel> netsuiteOrderExportResult = getFlexibleSearchService().search(netsuiteOrderExportQuery);
		if(netsuiteOrderExportResult.getCount() > 0) {
			return netsuiteOrderExportResult.getResult();
		} else {
			LOG.info("No orders found for the Netsuite replication. Exiting method");
		}
		return Collections.emptyList();
	}
	
	
	public Map<String, Object> getOrderStatusFilter(){
		String commaSeparatedOrderStatus = configurationService.getConfiguration().getString("gp.netsuite.service.replication.order.status.filter.list");
		String[] splittedOrderStatus = commaSeparatedOrderStatus.split(",");
		Map<String, Object> orderStatusFilterMap = new HashMap<String, Object>();
		List<OrderStatus> orderStatusFilterList = new ArrayList<>();
		for(String orderStatus: splittedOrderStatus) {
			orderStatusFilterList.add(OrderStatus.valueOf(orderStatus.trim()));
		}
		orderStatusFilterMap.put(ORDER_STATUS, orderStatusFilterList);
		return orderStatusFilterMap;
	}
	
	
	/**
	 * Method to calculate the Remorse period (current time minus 120 minutes | 2 hours), zone used is UTC.
	 * 
	 * @return remorsePeriod
	 */
	private String calculateRemorsePeriod() {
		final DateTimeFormatter remorsePeriodDateFormat = DateTimeFormatter
				.ofPattern(NETSUITE_REMORSE_PERIOD_DATE_FORMAT);
		Date currentDate = new Date();
		LocalDateTime remorseDateTime = currentDate.toInstant().atZone(ZoneId.of(configurationService.getConfiguration().getString(GpintegrationConstants.GP_NETSUITE_CUSTOMER_ORDER_REPLICATION_REMORSE_PERIOD_TIMEZONE))).toLocalDateTime()
			.minusMinutes(configurationService.getConfiguration().getInt(GpintegrationConstants.GP_NETSUITE_CUSTOMER_ORDER_REPLICATION_REMORSE_PERIOD));
		return remorsePeriodDateFormat.format(remorseDateTime);
    }
	
	private void replicateCustomerAndOrder(OrderModel orderModel) throws GPIntegrationException{
		boolean hasFailedOrderReplication = false;
		final int maxRetryCount = configurationService.getConfiguration().getInt(GpintegrationConstants.GP_NETSUITE_CUSTOMER_ORDER_REPLICATION_RETRY_COUNT);
		int consignmentIndex=0;
		boolean isLastConsignment = false;
		for(ConsignmentModel consignment: orderModel.getConsignments()) {
			consignmentIndex++;
			if(consignmentIndex == orderModel.getConsignments().size()) {
				isLastConsignment = true;
			}
			try {
				if(!"SUCCESS".equalsIgnoreCase(consignment.getNetsuiteReplicationStatus().getCode()) && Integer.parseInt(consignment.getNetsuiteRetryCount()) < maxRetryCount) {
					GPNetsuiteResponseDTO customerReplicationResponse = replicateCustomer(consignment);
					LOG.debug("Netsuite customer replication status for consignmentid:"+consignment.getCode()+":"+customerReplicationResponse.toString());
					if(NETSUITE_RESPONSE_SUCCESS.equalsIgnoreCase(customerReplicationResponse.getStatus())) {
						setNetsuiteConsignmentReplicationStatus(consignment, customerReplicationResponse, null, GPNetsuiteOrderExportStatus.SUCCESS, false, null);
						GPNetsuiteResponseDTO orderReplicationResponse = replicateOrder(consignment, isLastConsignment);
						LOG.debug("Netsuite oreder replication status for consignmentid:"+consignment.getCode()+":"+orderReplicationResponse.toString());
						if(NETSUITE_RESPONSE_SUCCESS.equalsIgnoreCase(orderReplicationResponse.getStatus())) {
							setNetsuiteConsignmentReplicationStatus(consignment, customerReplicationResponse, orderReplicationResponse, GPNetsuiteOrderExportStatus.SUCCESS, true, null);
						} else {
							hasFailedOrderReplication = true;
							if(orderReplicationResponse.getRetryCount() < maxRetryCount) {
								setNetsuiteConsignmentReplicationStatus(consignment, customerReplicationResponse, orderReplicationResponse, GPNetsuiteOrderExportStatus.FAILURE, true, null);
							} else {
								setNetsuiteConsignmentReplicationStatus(consignment, customerReplicationResponse, orderReplicationResponse, GPNetsuiteOrderExportStatus.NOT_ABLE_TO_PROCESS, false, null);
							}
							
						}
					} else {
						hasFailedOrderReplication = true;
						if(customerReplicationResponse.getRetryCount() < maxRetryCount) {
							setNetsuiteConsignmentReplicationStatus(consignment, customerReplicationResponse, customerReplicationResponse, GPNetsuiteOrderExportStatus.FAILED_CUSTOMER_REPLICATION, true, null);
						} else {
							setNetsuiteConsignmentReplicationStatus(consignment, customerReplicationResponse, customerReplicationResponse, GPNetsuiteOrderExportStatus.NOT_ABLE_TO_PROCESS, true, null);
						}
					}
				} else {
					hasFailedOrderReplication = true;
					LOG.debug("Netsuite replication status marked as NOT_ABLE_TO_PROCESS for this Consignment Id:"+consignment.getCode()+". Please process it manually. Proceeding with next consignment.");
				}
				setNetsuiteOrderReplicationStatus(orderModel, hasFailedOrderReplication);
			} catch(Exception exception) {
				setNetsuiteConsignmentReplicationStatus(consignment, null, null, GPNetsuiteOrderExportStatus.FAILURE, false, "System error while processing order.");
				setNetsuiteOrderReplicationStatus(orderModel, true);
				LOG.error("Netsuite customer or order replciation failed for consignmentid"+consignment.getCode()+"with exception", exception);
			}
		}
	}
	
	private GPNetsuiteResponseDTO replicateCustomer(ConsignmentModel consignmentModel) throws GPIntegrationException{
		LOG.debug("Replicating customer for consignment id:"+consignmentModel.getCode());
		final int maxRetryCount = configurationService.getConfiguration().getInt(GpintegrationConstants.GP_NETSUITE_CUSTOMER_ORDER_REPLICATION_RETRY_COUNT);
		int retryCount = Integer.parseInt(consignmentModel.getNetsuiteRetryCount());
		boolean hasCustomerReplicated = false;
		GPNetsuiteResponseDTO customerReplicationResponse = null;
		do {
			 customerReplicationResponse = gpNetsuiteReplicationService.replicateCustomerData(mapNetsuiteCustomerRequest(consignmentModel));
			retryCount++;
			if(NETSUITE_RESPONSE_SUCCESS.equalsIgnoreCase(customerReplicationResponse.getStatus())) {
				hasCustomerReplicated = true;
			}
			customerReplicationResponse.setRetryCount(retryCount);
			if(hasCustomerReplicated) {
				break;
			}
			
		}while(retryCount < maxRetryCount);
		return customerReplicationResponse;
	}
	
	private GPNetsuiteResponseDTO replicateOrder(ConsignmentModel consignmentModel, boolean lastConsignment) throws GPIntegrationException{
		LOG.debug("Replicating order for consignment id:"+consignmentModel.getCode());
		final int maxRetryCount = configurationService.getConfiguration().getInt(GpintegrationConstants.GP_NETSUITE_CUSTOMER_ORDER_REPLICATION_RETRY_COUNT);
		int retryCount = Integer.parseInt(consignmentModel.getNetsuiteRetryCount());
		boolean hasOrderReplicated = false;
		GPNetsuiteResponseDTO orderReplicationResponse = null;
		do {
			orderReplicationResponse = gpNetsuiteReplicationService.replicateOrderData(mapNetsuiteOrderRequest(consignmentModel, lastConsignment));
			retryCount++;
			if(NETSUITE_RESPONSE_SUCCESS.equalsIgnoreCase(orderReplicationResponse.getStatus())) {
				hasOrderReplicated = true;
			}
			orderReplicationResponse.setRetryCount(retryCount);
			if(hasOrderReplicated) {
				break;
			}
		}while(retryCount < maxRetryCount);
		return orderReplicationResponse;
	}
	
	private GPNetsuiteCustomerDTO mapNetsuiteCustomerRequest(ConsignmentModel consignmentModel) {
		GPNetsuiteCustomerDTO netSuiteCustomerDTO = new GPNetsuiteCustomerDTO();
		UserModel userModel = consignmentModel.getOrder().getUser();
		String b2bSiteName = configurationService.getConfiguration().getString(GpintegrationConstants.GP_SITE_B2B_NAME);
		
		if(!StringUtils.isEmpty(userModel.getUid()) && userModel.getUid().contains("|")) {
			final String uid = userModel.getUid();
			final String[] splittedUserId = uid.split(GpintegrationConstants.SITE_DELIMITER);
			//Email id would be in first field for registered user and second field for guest user
			netSuiteCustomerDTO.setEmail(StringUtils.equalsIgnoreCase(GpintegrationConstants.GUEST_USER,userModel.getDisplayName())?
					splittedUserId[1] : splittedUserId[0]);
			netSuiteCustomerDTO.setHybrisCustomerId(getCustomerId(userModel));

			if(b2bSiteName.equalsIgnoreCase(splittedUserId[1])) {
				netSuiteCustomerDTO.setIsCompany(Boolean.valueOf(true));
				netSuiteCustomerDTO.setDepartment(configurationService.getConfiguration().getString(GpintegrationConstants.GP_NETSUITE_CUSTOMER_ORDER_REPLICATION_DEPARTMENT_DIXY_FOOD_SERVICE_DIRECT));
				if(null != consignmentModel.getOrder().getUnit()) {
					netSuiteCustomerDTO.setCompanyName(consignmentModel.getOrder().getUnit().getDisplayName());
				}
				
			} else {
				netSuiteCustomerDTO.setIsCompany(Boolean.valueOf(false));
				netSuiteCustomerDTO.setDepartment(configurationService.getConfiguration().getString(GpintegrationConstants.GP_NETSUITE_CUSTOMER_ORDER_REPLICATION_DEPARTMENT_GP_STORE));
			}
		}
		
		//Split first name and last name based on space 
		String customerName = gpDefaultCustomerNameStrategy.getName(userModel.getName());
		if(!StringUtils.isEmpty(customerName) && StringUtils.contains(customerName, " " )) {
			String[] userName = gpDefaultCustomerNameStrategy.splitNameBySpace(customerName);
	        netSuiteCustomerDTO.setFirstName(userName[0]);
	        netSuiteCustomerDTO.setLastName(userName[1]);
        } else {
        	//Assign same name for first and last in case there is no space as Netsuite expects both parameters
        	 netSuiteCustomerDTO.setFirstName(customerName);
 	         netSuiteCustomerDTO.setLastName(customerName);
        }
		
		netSuiteCustomerDTO.setBillingSameAsShipping(Boolean.valueOf(false));
		netSuiteCustomerDTO.setBillingAddress(mapNetsuiteCustomerBillingAddressRequest(consignmentModel));
		netSuiteCustomerDTO.setShippingAddress(mapNetuiteCustomerShippingAddressRequest(consignmentModel));
		LOG.debug("Customer replication request:"+netSuiteCustomerDTO.toString());
		return netSuiteCustomerDTO;
	}
	
	
	private GPNetsuiteAddressDTO mapNetsuiteCustomerBillingAddressRequest(ConsignmentModel consignmentModel) {
		GPNetsuiteAddressDTO billingAddressDTO = new GPNetsuiteAddressDTO();
		if(null != consignmentModel.getOrder()) {
			if(null != consignmentModel.getOrder().getPaymentInfo()) {
				if(null != consignmentModel.getOrder().getPaymentInfo().getBillingAddress()) {
					AddressModel billingAddressModel = consignmentModel.getOrder().getPaymentInfo().getBillingAddress();
					
					if(!StringUtils.isEmpty(billingAddressModel.getLine1())) {
						billingAddressDTO.setAddr1(billingAddressModel.getLine1());
					}
					
					if(!StringUtils.isEmpty(billingAddressModel.getLine2())) {
						billingAddressDTO.setAddr2(billingAddressModel.getLine2());
					}
					billingAddressDTO.setCity(billingAddressModel.getTown());
					billingAddressDTO.setCountry(billingAddressModel.getCountry().getIsocode());
					
					if(!StringUtils.isEmpty(billingAddressModel.getPhone1())) {
						billingAddressDTO.setPhone(billingAddressModel.getPhone1());
					}
					
					if(null != billingAddressModel.getRegion()) {
						billingAddressDTO.setState(billingAddressModel.getRegion().getIsocodeShort());
					}
					
					billingAddressDTO.setZip(billingAddressModel.getPostalcode());
				}
			}
		}
		
		return billingAddressDTO;
	}
		
	private GPNetsuiteAddressDTO mapNetuiteCustomerShippingAddressRequest(ConsignmentModel consignmentModel) {
		AddressModel shippingAddressModel = null;
		GPNetsuiteAddressDTO shippingAddressDTO = null;
		
		if(null != consignmentModel.getShippingAddress()) {
			shippingAddressModel = consignmentModel.getShippingAddress();
			shippingAddressDTO = new GPNetsuiteAddressDTO();
			
			if(!StringUtils.isEmpty(shippingAddressModel.getLine1())) {
				shippingAddressDTO.setAddr1(shippingAddressModel.getLine1());
			}
			
			if(!StringUtils.isEmpty(shippingAddressModel.getLine2())) {
				shippingAddressDTO.setAddr2(shippingAddressModel.getLine2());
			}
			shippingAddressDTO.setCity(shippingAddressModel.getTown());
			shippingAddressDTO.setCountry(shippingAddressModel.getCountry().getIsocode());
			
			if(!StringUtils.isEmpty(shippingAddressModel.getPhone1())) {
				shippingAddressDTO.setPhone(shippingAddressModel.getPhone1());
			}
			
			if(null != shippingAddressModel.getRegion()) {
				shippingAddressDTO.setState(shippingAddressModel.getRegion().getIsocodeShort());
			}
			
			shippingAddressDTO.setZip(shippingAddressModel.getPostalcode());
		}
		return shippingAddressDTO;
	}
	
	private GPNetsuiteOrderDTO mapNetsuiteOrderRequest(ConsignmentModel consignmentModel, boolean lastConsignment) {
        GPNetsuiteOrderDTO netSuiteOrderRequest = new GPNetsuiteOrderDTO();
        UserModel userModel = consignmentModel.getOrder().getUser();
       
        if(!StringUtils.isEmpty(userModel.getUid())) {
               netSuiteOrderRequest.setCustomerId(getCustomerId(userModel));
        }
       
        netSuiteOrderRequest.setHybrisConsignmentId(consignmentModel.getCode());
        netSuiteOrderRequest.setHybrisOrderId(consignmentModel.getOrder().getCode());
       
        if(isMultipleShipTo(consignmentModel)) {
               LOG.debug("Order has multiple shipment, reading delivery mode from split entry.");
               for(ConsignmentEntryModel consignmentEntry: consignmentModel.getConsignmentEntries()) {
                      if(consignmentEntry.getDeliveryModeCode().contains(GpintegrationConstants.SYMBOL_UNDERSCORE)) {
                            netSuiteOrderRequest.setShipMethod(consignmentEntry.getDeliveryModeCode().substring(0, consignmentEntry.getDeliveryModeCode().indexOf(GpintegrationConstants.SYMBOL_UNDERSCORE)).trim());
                     } else {
                            netSuiteOrderRequest.setShipMethod(consignmentEntry.getDeliveryModeCode());
                     }
                     LOG.debug("Order Delivery cost: "+consignmentEntry.getDeliveryCost());
                     netSuiteOrderRequest.setShippingCost(consignmentEntry.getDeliveryCost());
               }
        } else {
               LOG.debug("Order has single shipment, reading delivery mode from Order.");
               LOG.debug("Order Delivery cost: "+consignmentModel.getOrder().getDeliveryCost());
        if(consignmentModel.getOrder().getDeliveryMode().getCode().contains(GpintegrationConstants.SYMBOL_UNDERSCORE)) {
               netSuiteOrderRequest.setShipMethod(consignmentModel.getOrder().getDeliveryMode().getCode().substring(0, consignmentModel.getOrder().getDeliveryMode().getCode().indexOf(GpintegrationConstants.SYMBOL_UNDERSCORE)).trim());
               } else {
                      netSuiteOrderRequest.setShipMethod(consignmentModel.getOrder().getDeliveryMode().getCode());
               }
               netSuiteOrderRequest.setShippingCost(consignmentModel.getOrder().getDeliveryCost());
        }
       
        String promoCodes = getPromotionCodes(consignmentModel);
        if(!StringUtils.isEmpty(promoCodes)){
               netSuiteOrderRequest.setHeaderPromoCode(promoCodes.substring(0, promoCodes.lastIndexOf(GpintegrationConstants.COMMA_SYMBOL)));
        }
       
        BigDecimal consignmentTax = BigDecimal.valueOf(0.0);
        for(ConsignmentEntryModel consignmentEntry: consignmentModel.getConsignmentEntries()) {
               if(null != consignmentEntry.getTotalTax()) {
                     consignmentTax = consignmentTax.add(BigDecimal.valueOf(consignmentEntry.getTotalTax()));     
               } else {
                     LOG.error("Total tax is null for this consignment entry, associated consigment number is:"+consignmentModel.getCode());
               }
        }
        LOG.debug("Tax before sending to NS"+ consignmentTax.doubleValue());
        if(lastConsignment) {
               netSuiteOrderRequest.setTaxAmount(consignmentTax.doubleValue() + getTaxAdjustment(consignmentModel));
        }
        else {
               netSuiteOrderRequest.setTaxAmount(consignmentTax.doubleValue());
        }
              
 netSuiteOrderRequest.setTranDate(GPIntegrationUtils.formatDate(consignmentModel.getOrder().getCreationtime().toString(), GpintegrationConstants.HYBRIS_ORDER_CREATION_DATE_FORMAT, GpintegrationConstants.NETSUITE_ORDER_TRANSACTION_DATE_FORMAT));
        netSuiteOrderRequest.setItems(mapNetsuiteOrderItem(consignmentModel, lastConsignment));
        LOG.debug("Order replication request:"+netSuiteOrderRequest.toString());
        return netSuiteOrderRequest;
 }

	
	private String getCustomerId(UserModel userModel) {
		String customerId = null;
		if(StringUtils.isNotEmpty(userModel.getUid())) {
			try {
				CustomerModel searchModel = new CustomerModel();
				searchModel.setUid(userModel.getUid());
				final CustomerModel customerModel = flexibleSearchService.getModelByExample(searchModel);
				if(customerModel != null) {
					customerId = customerModel.getCustomerID();
				}
			}catch(Exception e) {
				LOG.warn("Netsuite order replication: Error in getting customer for user " + userModel.getUid(),e);
			}
		}
		LOG.debug(" Customer id for " + userModel.getUid() + " is " + customerId);
		return customerId;
	}
	
	private boolean isMultipleShipTo(ConsignmentModel consignmentModel) {
		boolean isMultipleShipTo = false;
		for(ConsignmentEntryModel consignmentEntry: consignmentModel.getConsignmentEntries()) {
			for(SplitEntryModel splitEntry: consignmentEntry.getOrderEntry().getSplitEntry()) {
				if(null != splitEntry.getDeliveryMode()) {
					isMultipleShipTo = true;
					break;
				} else {
					isMultipleShipTo = false;
					break;
				}
			}
		}
		return isMultipleShipTo;
	}
	
	private String getPromotionCodes(ConsignmentModel consignmentModel) {
		StringBuilder stringBuilder = new StringBuilder();
		
		if(!CollectionUtils.isEmpty(consignmentModel.getOrder().getAppliedCouponCodes())) {
			for(String promoCode: consignmentModel.getOrder().getAppliedCouponCodes()) {
				stringBuilder.append(promoCode).append(GpintegrationConstants.COMMA_SYMBOL);
			}
		}
		return stringBuilder.toString();
	}

	private double getTaxAdjustment(ConsignmentModel consignment) {
		double adjustmentTax = 0.0;
		DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
		double totalTax = 0.0;
			for(ConsignmentModel cons : consignment.getOrder().getConsignments()) {
				double totalConsignmentTax = 0.0;
				for(ConsignmentEntryModel consignmentEntry :cons.getConsignmentEntries()) {
					if(null != consignmentEntry.getTotalTax()) {
						totalConsignmentTax = totalConsignmentTax + consignmentEntry.getTotalTax();
					}
			}
				totalTax = totalTax + totalConsignmentTax;
			}
			if(DoubleMath.fuzzyCompare(Double.valueOf(decimalFormat.format((consignment.getOrder().getTotalTax().doubleValue()-totalTax))), 0, EPSILON)!=0) {
				adjustmentTax = Double.valueOf(decimalFormat.format(consignment.getOrder().getTotalTax().doubleValue()-totalTax));
			}
			return adjustmentTax;
			
		
	}
	
	private List<GPNetsuiteOrderItemDTO> mapNetsuiteOrderItem(ConsignmentModel consignment, boolean lastConsignment){
		List<GPNetsuiteOrderItemDTO> itemList = new ArrayList<GPNetsuiteOrderItemDTO>();
		DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
		double adjustmentDiscount = 0.0;
		if(lastConsignment) {
			double totaldiscount = 0.0;
			for(ConsignmentModel cons : consignment.getOrder().getConsignments()) {
				double totalConsignmentdiscount = 0.0;
				for(ConsignmentEntryModel consignmentEntry :cons.getConsignmentEntries()) {
					if(null != consignmentEntry.getDiscountPrice()) {
						totalConsignmentdiscount = totalConsignmentdiscount + consignmentEntry.getDiscountPrice();
					}
			}
				totaldiscount = totaldiscount + totalConsignmentdiscount;
			}
			if(DoubleMath.fuzzyCompare(Double.valueOf(decimalFormat.format((consignment.getOrder().getTotalDiscounts().doubleValue()-totaldiscount))), 0, EPSILON)!=0) {
				adjustmentDiscount = Double.valueOf(decimalFormat.format(consignment.getOrder().getTotalDiscounts().doubleValue()-totaldiscount));
			}
			
		}
		int consignmentEntryIndex = 0;
		double finalAmount = 0.0d;
		for(ConsignmentEntryModel consignmentEntry :consignment.getConsignmentEntries()) {
			consignmentEntryIndex++;
			GPNetsuiteOrderItemDTO orderItemDTO = new GPNetsuiteOrderItemDTO();
			if(null != consignmentEntry.getDiscountPrice() && consignmentEntry.getDiscountPrice() != 0) {
				double proratedDiscountPerItem = consignmentEntry.getDiscountPrice();
				if(consignmentEntryIndex==consignment.getConsignmentEntries().size()) {
					finalAmount = Double.valueOf(decimalFormat.format((consignmentEntry.getTotalPrice()) - (proratedDiscountPerItem+adjustmentDiscount)));
				}
				else {
					finalAmount = Double.valueOf(decimalFormat.format(consignmentEntry.getTotalPrice() - proratedDiscountPerItem));
				}
			} 
			else {
				finalAmount = consignmentEntry.getTotalPrice();
			}
			orderItemDTO.setAmount(finalAmount);
			orderItemDTO.setQuantity(consignmentEntry.getQuantity().intValue());
			orderItemDTO.setRate(consignmentEntry.getOrderEntry().getBasePrice());
			orderItemDTO.setConsignmentEntryNumber(consignmentEntry.getConsignmentEntryNumber().toString());
			orderItemDTO.setSku(consignmentEntry.getOrderEntry().getProduct().getCode()); // product code
			itemList.add(orderItemDTO);
		}
		return itemList;
	}
	
	private void setNetsuiteConsignmentReplicationStatus(ConsignmentModel consignmentModel, GPNetsuiteResponseDTO customerReplicationResponse, GPNetsuiteResponseDTO orderReplicationResponse, GPNetsuiteOrderExportStatus consigmentReplicationStatus, boolean isCustomerReplication, String exceptionMessage) {
		if(NETSUITE_RESPONSE_SUCCESS.equalsIgnoreCase(consigmentReplicationStatus.getCode())) {
			consignmentModel.setNetsuiteReplicationStatus(consigmentReplicationStatus);
			consignmentModel.setNetsuiteCustomerId(customerReplicationResponse.getNetsuiteId());
			if(null != orderReplicationResponse) {
				consignmentModel.setNetsuiteOrderId(orderReplicationResponse.getNetsuiteId());
				consignmentModel.setNetsuiteRetryCount(String.valueOf(orderReplicationResponse.getRetryCount()));
			}
			consignmentModel.setNetsuiteErrorMessage("");
		} else if(FAILED_CUSTOMER_REPLICATION.equalsIgnoreCase(consigmentReplicationStatus.getCode())){
			consignmentModel.setNetsuiteReplicationStatus(consigmentReplicationStatus);
			consignmentModel.setNetsuiteErrorMessage(customerReplicationResponse.getMessage());
			consignmentModel.setNetsuiteRetryCount(String.valueOf(customerReplicationResponse.getRetryCount()));
		} else if(NOT_ABLE_TO_PROCESS.equalsIgnoreCase(consigmentReplicationStatus.getCode())) {
			consignmentModel.setNetsuiteReplicationStatus(consigmentReplicationStatus);
			if(isCustomerReplication) {
				consignmentModel.setNetsuiteRetryCount(String.valueOf(customerReplicationResponse.getRetryCount()));
				consignmentModel.setNetsuiteErrorMessage(customerReplicationResponse.getMessage());
			} else {
				consignmentModel.setNetsuiteRetryCount(String.valueOf(orderReplicationResponse.getRetryCount()));
				consignmentModel.setNetsuiteErrorMessage(orderReplicationResponse.getMessage());
			}
		} else if(null != exceptionMessage) {
			consignmentModel.setNetsuiteReplicationStatus(consigmentReplicationStatus);
			consignmentModel.setNetsuiteErrorMessage(exceptionMessage);
		}
		modelService.save(consignmentModel);
	}
	
	private void setNetsuiteOrderReplicationStatus(OrderModel orderModel, boolean hasFailedOrderReplication) {
		if(hasFailedOrderReplication) {
			orderModel.setNetsuiteReplicationStatus(GPNetsuiteOrderExportStatus.NOTEXPORTED);
		} else {
			orderModel.setNetsuiteReplicationStatus(GPNetsuiteOrderExportStatus.EXPORTED);
		}
		
		modelService.save(orderModel);
	}

	public GPDefaultNetsuiteReplicationService getGpNetsuiteReplicationService() {
		return gpNetsuiteReplicationService;
	}

	public void setGpNetsuiteReplicationService(GPDefaultNetsuiteReplicationService gpNetsuiteReplicationService) {
		this.gpNetsuiteReplicationService = gpNetsuiteReplicationService;
	}
	
	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
	@Override
	public boolean isAbortable() {
		return true;
	} 
}
/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service.impl;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gp.commerce.core.enums.GPNetsuiteOrderExportStatus;
import com.gp.commerce.core.model.TrackingModel;
import com.gp.commerce.core.strategies.GPDefaultCustomerNameStrategy;
import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.crm.shellorder.dto.GPCRMContactDetailsDTO;
import com.gpintegration.crm.shellorder.dto.GPCRMOrderDTO;
import com.gpintegration.crm.shellorder.dto.GPCRMOrderHeaderDTO;
import com.gpintegration.crm.shellorder.dto.GPCRMShellOrderDTO;
import com.gpintegration.crm.shellorder.dto.GPCRMShellOrderResponseDTO;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.service.GPCRMService;
import com.gpintegration.service.GPCRMShellOrderReplicationService;
import com.gpintegration.user.impl.GPAddCustomerToSfdcResponse;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

/**
 * Default implementation of {@link GPCRMShellOrderReplicationService}.
 */
public class GPDefaultCRMShellOrderReplicationService implements GPCRMShellOrderReplicationService {

	private static final String ERROR_UPDATING_ORDER_STATUS = "Error updating order status ";

	private ConfigurationService configurationService;

	private ModelService modelService;

	private static final Logger LOG = LoggerFactory.getLogger(GPDefaultCRMShellOrderReplicationService.class);

	public  static final String CRM_SHELL_ORDER_CREATE_API = "gp.scpi.crm.shellorder.end.point.url";

	public  static final String CRM_SHELL_MAX_RETRY_COUNT = "gp.scpi.crm.shellorder.max.retrycount";

	public  static final String CRM_SHELL_ORDER_SOURCE_URL = "gp.crm.shellorder.sourceurl";

	public  static final String CRM_SHELL_ORDER_TRACKING_THRESHOLD = "gp.crm.shellorder.tracking.threshold.length";


	public static final String MULTIPLE_SHIPMENTS = "Multiple Shipments";

	public  static final String HOST = "{host}";

	public  static final String SITE_ID = "{siteId}";

	public  static final String ORDER_ID = "{orderId}";

	public static final String E_ORDER = "eSORD";

	public static final String LEFT_PARENTHESIS = "(";

	public static final String RIGHT_PARENTHESIS = ")";

	private String urlEncodingAttributes;
	@Resource
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;

	@Resource(name = "gpDefaultCustomerNameStrategy")
	private GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy;

	@Resource
	private  GPCRMService gpCRMService;


	@Override
	public boolean replicateCRMShellOrder(OrderModel orderModel) {
		boolean result = Boolean.FALSE;
		if(configurationService.getConfiguration().getBoolean(GpintegrationConstants.CRM_SHELL_ORDER_REPLICATION_FLAG)) {
			try {
				GPCRMShellOrderDTO shellOrder = mapOrdertoShellOrder(orderModel);
				GPCRMShellOrderResponseDTO response = replicateShellOrder(shellOrder);
				if(response != null && StringUtils.isNotBlank(response.getCaseNumber())) {
					updateCaseDetails(orderModel,response,null);
					result = Boolean.TRUE;
				} else if(response != null && StringUtils.isNotBlank(response.getErrorstatuscode())) {
						LOG.error(" Error in replicating CRM shell order: {} with error {}" , orderModel.getCode() , response.getErrorstatuscode() );
						updateCaseDetails(orderModel,response,response.getMessage());
						
				}
			}catch(GPIntegrationException e) {
				LOG.error(" Error in replicating CRM shell order: {} {} " + orderModel.getCode() ,e);
				updateCaseDetails(orderModel,null,e.getMessage());
			}catch(Exception e) {
				LOG.error(" Error in replicating CRM shell order {} {} " + orderModel.getCode() ,e);
				updateCaseDetails(orderModel,null,e.getMessage());
			}
		} else {
			LOG.debug(" CRM Shell order replication update is disabled");
		}
		return result;

	}


	@Override
	public void updateCRMShellOrder(OrderModel orderModel) {
		if(configurationService.getConfiguration().getBoolean(GpintegrationConstants.CRM_SHELL_ORDER_REPLICATION_FLAG)) {
			LOG.debug("Updating CRM shell order status for Order id : {} with status  {} " , orderModel.getCode()  ,orderModel.getStatus());
			try {
				//Either create or update shell order based on case id present
				GPCRMShellOrderDTO shellOrder = StringUtils.isNotEmpty(orderModel.getSfdcCaseNumber()) ? mapOrderToUpdateShellOrder(orderModel):mapOrdertoShellOrder(orderModel);
				LOG.debug("Updating CRM shell order status request {}" , shellOrder.toString());
				GPCRMShellOrderResponseDTO response = replicateShellOrder(shellOrder);
				if(response != null && StringUtils.isNotBlank(response.getCaseNumber())) {
					LOG.debug("Updating CRM shell order status for Order id : {} with status  {} " , orderModel.getCode()  ,orderModel.getStatus());
					orderModel.setSfdcReplicationMessage(response.getMessage());
					orderModel.setSfdcCaseNumber(response.getCaseNumber());
					orderModel.setSfdcReplicationStatus(GPNetsuiteOrderExportStatus.EXPORTED);
					modelService.save(orderModel);
				}
			}catch(GPIntegrationException e) {
				LOG.error(" Error in updating CRM shell order status " + orderModel.getCode() + e.getMessage(),e);
				orderModel.setSfdcReplicationMessage(ERROR_UPDATING_ORDER_STATUS + e.getMessage());
				modelService.save(orderModel);
			}catch(Exception e) {
				LOG.error(" Error in updating CRM shell order status exception: " + orderModel.getCode() + e.getMessage(),e);
				orderModel.setSfdcReplicationMessage(ERROR_UPDATING_ORDER_STATUS + e.getMessage());
				modelService.save(orderModel);
			}
		} else {
			LOG.debug(" CRM Shell order update is disabled");
		}
	}


	/**
	 * Method to update Order model with case data
	 * @param OrderModel orderModel
	 * @param GPCRMShellOrderResponseDTO response
	 */
	private void updateCaseDetails(final OrderModel orderModel,final GPCRMShellOrderResponseDTO response,String errorMessage) {
		if(response != null ) {
			if(StringUtils.isNotBlank(response.getCaseNumber())) {
				updateOrderModel(orderModel,GPNetsuiteOrderExportStatus.EXPORTED,response.getCaseNumber(),response.getMessage());
			}else {
				//If SCPI API sends error status code and message, it is data issue and data should not replicated again
				if(StringUtils.isNoneBlank(response.getMessage()) && StringUtils.isNotBlank(response.getErrorstatuscode())) {
					updateOrderModel(orderModel,GPNetsuiteOrderExportStatus.NOT_ABLE_TO_PROCESS,null,response.getMessage());
				} else {
					updateOrderModel(orderModel,GPNetsuiteOrderExportStatus.FAILURE,null,response.getMessage());
				}
			}
			LOG.debug(" CRM shell order replication updating order  {} -{}",orderModel.getCode() , response.getCaseNumber());
		} else {
			updateOrderModel(orderModel,GPNetsuiteOrderExportStatus.FAILURE,null,errorMessage);
		}
		if(orderModel.getSfdcRetryCount() > getConfigurationService().getConfiguration().getInteger(CRM_SHELL_MAX_RETRY_COUNT, 5) ) {
			orderModel.setSfdcReplicationStatus(GPNetsuiteOrderExportStatus.NOT_ABLE_TO_PROCESS);
		}
		modelService.save(orderModel);
	}

	/**
	 * Method to set the sfdc attributes with sfdc status and error message
	 * @param orderModel
	 * @param sfdcStatus
	 * @param message
	 * @param caseNumber
	 */
	private void updateOrderModel(final OrderModel orderModel ,final GPNetsuiteOrderExportStatus sfdcStatus, final String caseNumber,final String message ) {
		orderModel.setSfdcReplicationMessage(message);
		orderModel.setSfdcReplicationStatus(sfdcStatus);
		if(StringUtils.isNotBlank(caseNumber)) {
			orderModel.setSfdcCaseNumber(caseNumber);
		}
		orderModel.setSfdcRetryCount(orderModel.getSfdcRetryCount() != null ? orderModel.getSfdcRetryCount()+ 1 :1 );
	}

	/**
	 * Method to map Order Model to CRM shell order
	 * @param orderModel
	 * @return GPCRMShellOrderDTO
	 * @throws UnknownHostException
	 */
	private GPCRMShellOrderDTO mapOrdertoShellOrder(OrderModel orderModel) throws UnknownHostException {
		GPCRMShellOrderDTO shellOrder = new GPCRMShellOrderDTO();
		GPCRMOrderDTO order = new GPCRMOrderDTO();
		GPCRMContactDetailsDTO  contactDetails = getContactDetails(orderModel);
		order.setContactDetails(contactDetails);
		Date orderDate =  orderModel.getCreationtime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        Instant instant = orderDate.toInstant();
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
		order.setOrderDate(localDate.format(formatter));
		GPCRMOrderHeaderDTO orderHeader = new GPCRMOrderHeaderDTO();
		orderHeader.setEcommerceOrderNumber(E_ORDER + orderModel.getCode());
		orderHeader.setEcommerceOrderStatus(orderModel.getStatus().getCode());
		orderHeader.setServiceLevelofEcommerceOrder(orderModel.getDeliveryMode() != null?
				orderModel.getDeliveryMode().getCode():MULTIPLE_SHIPMENTS);
		BigDecimal orderTotalPrice = new BigDecimal(String.valueOf(orderModel.getTotalPrice()));
		BigDecimal orderTotalTax = new BigDecimal(String.valueOf(orderModel.getTotalTax()));
		BigDecimal debitAmount = orderTotalPrice.add(orderTotalTax);
		orderHeader.setOrderTotalAmount(debitAmount.toString());

		orderHeader.setSourceProductList(getProductData(orderModel));
		getTrackingDetails(orderModel);
		orderHeader.setTrackingId(getTrackingDetails(orderModel));
		//Source URL set to backoffice url
		orderHeader.setSourceUrl(configurationService.getConfiguration().getString(CRM_SHELL_ORDER_SOURCE_URL));
		order.setOrderHeader(orderHeader);
		shellOrder.setOrder(order);
		return shellOrder;
	}

	/**
	 * Method to get tracking details for the order
	 * @param orderModel, OrderModel
	 * @return String, returns tracking id if only one id is present else returns "Multiple Shipments"
	 */
	private String getTrackingDetails(OrderModel orderModel) {
		Set<String> trackingSet = new HashSet<>();
		if(CollectionUtils.isNotEmpty(orderModel.getConsignments()) ) {
			orderModel.getConsignments().stream().forEach(consignment->{
				if(CollectionUtils.isNotEmpty(consignment.getConsignmentEntries())) {
					consignment.getConsignmentEntries().stream().forEach(consignmentEntry -> {
						if(CollectionUtils.isNotEmpty(consignmentEntry.getTrackingList())) {
							trackingSet.addAll(consignmentEntry.getTrackingList().stream().filter(tracking-> StringUtils.isNotEmpty(tracking.getTrackingID()))
						               .map(TrackingModel::getTrackingID).collect(Collectors.toSet()));
							LOG.debug( "trackingSet {} " , trackingSet);
					}});
				}
			});
		}
		if(CollectionUtils.isNotEmpty(trackingSet)) {
			if( trackingSet.stream().distinct().count() > 1) {
				return MULTIPLE_SHIPMENTS;
			} else {
				//Set threshold limit for tracking id as SFDC has a predefined limit
				String trackingId = trackingSet.stream().findFirst().orElse(null);
				return StringUtils.isNotEmpty(trackingId) && trackingId.length() >= getConfigurationService().getConfiguration().getInteger(CRM_SHELL_ORDER_TRACKING_THRESHOLD, 100) ? MULTIPLE_SHIPMENTS: trackingId; 
		}
		}
		return null;
	}

	/**
	 * Method to map Order Model to CRM shell order
	 * @pram orderModel
	 * @return GPCRMShellOrderDTO
	 */
	private GPCRMShellOrderDTO mapOrderToUpdateShellOrder(OrderModel orderModel) {
		GPCRMShellOrderDTO shellOrder = new GPCRMShellOrderDTO();
		GPCRMOrderDTO order = new GPCRMOrderDTO();
		shellOrder.setOrder(order);
		GPCRMOrderHeaderDTO orderHeader = new GPCRMOrderHeaderDTO();
		orderHeader.setEcommerceOrderNumber(E_ORDER + orderModel.getCode());
		orderHeader.setEcommerceOrderStatus(orderModel.getStatus().getCode());
		orderHeader.setTrackingId(getTrackingDetails(orderModel));
		order.setOrderHeader(orderHeader);
		return shellOrder;
	}

	/**
	 * Method to get product data for order
	 * @param  OrderModel order
	 * @return String
	 */
	private String getProductData(final OrderModel order) {
		StringBuilder product = new StringBuilder();
		if(CollectionUtils.isNotEmpty(order.getEntries())) {
			List<AbstractOrderEntryModel> entries = order.getEntries().stream().filter(e -> e.getProduct() != null).collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(entries)) {
				entries.stream().forEach(entry->{
				product.append(entry.getProduct().getCode()).append(LEFT_PARENTHESIS);
					product.append(entry.getQuantity()).append(RIGHT_PARENTHESIS).append(GpintegrationConstants.COMMA_SYMBOL);
			});
				String productData = product.toString();
				if(StringUtils.isNotBlank(productData) && StringUtils.contains(productData,GpintegrationConstants.COMMA_SYMBOL)) {
					return StringUtils.removeEnd(productData, GpintegrationConstants.COMMA_SYMBOL);
		}
	}
		}
		return null;
	}

	/**
	 * Method to get contact details from order model
	 * @param  OrderModel order
	 * @return GPCRMContactDetailsDTO
	 */
	private GPCRMContactDetailsDTO getContactDetails(final OrderModel order) {
		final GPCRMContactDetailsDTO  contactDetails = new GPCRMContactDetailsDTO();
		final UserModel userModel = order.getUser();

		String customerName = gpDefaultCustomerNameStrategy.getName(userModel.getName());
		String[] userName = null;
		if(StringUtils.isNotEmpty(customerName) && StringUtils.contains(customerName, GpintegrationConstants.PIPE_SYMBOL) ) {
				userName = gpDefaultCustomerNameStrategy.splitName(customerName);
		} else if(StringUtils.isNotEmpty(customerName) && StringUtils.contains(customerName.trim(), " "))  {
				userName = gpDefaultCustomerNameStrategy.splitNameBySpace(customerName);
			}
		if(userName != null && userName.length > 1 ) {
			contactDetails.setFirstName(userName[0]);
			contactDetails.setLastName(userName[1]);
		} else if(StringUtils.isNotBlank(customerName)){
			//Set both first name and last name 
			contactDetails.setFirstName(customerName.trim());
			contactDetails.setLastName(customerName.trim());
		}
		CustomerModel customer = (CustomerModel) userModel;
		contactDetails.setEmail(customer.getContactEmail());
		LOG.debug("Contact email " + customer.getContactEmail());
		//update address for multiple shipments for user address
		if(CollectionUtils.isNotEmpty(order.getConsignments()) && order.getConsignments().size()> 1 && userModel.getDefaultShipmentAddress()!=null) {
			updateAddress(contactDetails,  userModel.getDefaultShipmentAddress());
		}else {
			updateAddress(contactDetails,  order.getDeliveryAddress());
		}
		LOG.debug("Contact details:"+contactDetails.toString());
		return contactDetails;
	}

	/**
	 * Method to update address from user shipping address
	 * @param GPCRMContactDetailsDTO contactDetails
	 * @param UserModel userModel
	 */
	private void updateAddress(final GPCRMContactDetailsDTO  contactDetails,final AddressModel defaultShippingAddress) {
		if(defaultShippingAddress != null ) {
		contactDetails.setCity(defaultShippingAddress.getTown());
		contactDetails.setAppartmentNumber(defaultShippingAddress.getLine2());
		contactDetails.setCountryCode(defaultShippingAddress.getCountry().getIsocode());
		contactDetails.setPostalCode(defaultShippingAddress.getPostalcode());
		contactDetails.setStateCode(defaultShippingAddress.getRegion().getIsocodeShort());
		contactDetails.setStreetAddress(defaultShippingAddress.getLine1());
		contactDetails.setTelephoneNumber(defaultShippingAddress.getPhone1());
		contactDetails.setCompanyName(defaultShippingAddress.getCompany());
	}
	}


	/**
	 * Method to replicate shell order.
	 *
	 * @param shellOrderRequest the shell order request
	 * @return GPCRMShellOrderResponseDTO
	 * @throws GPIntegrationException the GP integration exception
	 */
	public GPCRMShellOrderResponseDTO replicateShellOrder(GPCRMShellOrderDTO shellOrderRequest) throws GPIntegrationException {
		GPCRMShellOrderResponseDTO response = null;
		final Map<String, String> pathVariables =  new HashMap<>();
		try {
			final HttpHeaders headers = gpCRMService.getSCPIHeaders();
			final HttpEntity<GPCRMShellOrderDTO> requestEntity = new HttpEntity<>(shellOrderRequest, headers);
			LOG.debug("CRM Shell Order request: {}" , shellOrderRequest.toString());
			final ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
			final RestTemplate restTemplate = new RestTemplate(factory);
			restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
			final ResponseEntity<GPCRMShellOrderResponseDTO> responseEntity =restTemplate.exchange(configurationService.getConfiguration().getString(CRM_SHELL_ORDER_CREATE_API),
					HttpMethod.POST, requestEntity, GPCRMShellOrderResponseDTO.class, pathVariables);
			response = responseEntity.getBody();
			LOG.debug(" CRM Shell Order Response from SFDC {}" , response.toString());
		   }catch (final HttpServerErrorException |HttpClientErrorException e) {
			LOG.error("HTTP Exception occured at replicate shell order through SCPI API {} with message {}", e , e.getMessage());
			return handleError(e);
		}catch(Exception e) {
			LOG.error("Exception in replicating shell order", e);
			LOG.error("Exception in replicating shell order for request {} \n Response:{}", shellOrderRequest.toString(),response);
			throw new GPIntegrationException(e.getMessage(),e);
		}
		return response;
	}

	/**
	 * Method to extract the error form HTTP Exception a
	 * @param e
	 * @return
	 */
	private GPCRMShellOrderResponseDTO handleError(HttpStatusCodeException e) {
		GPCRMShellOrderResponseDTO responseBody = null;
		LOG.debug( "shell order response {}  ",e.getResponseBodyAsString());
		final ObjectMapper mapper = new ObjectMapper();
		if(StringUtils.isNotEmpty(e.getResponseBodyAsString()) ) {
			try {
				 responseBody =mapper.readValue(e.getResponseBodyAsString(), GPCRMShellOrderResponseDTO.class);
				 if(responseBody != null) {
					 return responseBody;
				 }
			} catch (Exception e1) {
				LOG.error("Exeception occured at Add Customer to SFDC through SCPI API {} with message {}", e1 , e1.getMessage());
			}
		}
		return responseBody;
	}
	

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	/**
	 * @param configurationService the configurationService to set
	 */
	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService() {
		return modelService;
	}

	/**
	 * @param modelService the modelService to set
	 */
	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}


	/**
	 * @return the urlEncodingAttributes
	 */
	public String getUrlEncodingAttributes() {
		return urlEncodingAttributes;
	}


	/**
	 * @return the siteBaseUrlResolutionService
	 */
	public SiteBaseUrlResolutionService getSiteBaseUrlResolutionService() {
		return siteBaseUrlResolutionService;
	}


	/**
	 * @param urlEncodingAttributes the urlEncodingAttributes to set
	 */
	public void setUrlEncodingAttributes(String urlEncodingAttributes) {
		this.urlEncodingAttributes = urlEncodingAttributes;
	}



	/**
	 * @param siteBaseUrlResolutionService the siteBaseUrlResolutionService to set
	 */
	public void setSiteBaseUrlResolutionService(SiteBaseUrlResolutionService siteBaseUrlResolutionService) {
		this.siteBaseUrlResolutionService = siteBaseUrlResolutionService;
	}


	/**
	 * @return the gpCRMService
	 */
	public GPCRMService getGpCRMService() {
		return gpCRMService;
	}


	/**
	 * @param gpCRMService the gpCRMService to set
	 */
	public void setGpCRMService(GPCRMService gpCRMService) {
		this.gpCRMService = gpCRMService;
	}





}

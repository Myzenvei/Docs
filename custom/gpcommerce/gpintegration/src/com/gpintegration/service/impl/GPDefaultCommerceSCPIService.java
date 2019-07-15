/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gp.commerce.core.strategies.GPDefaultCustomerNameStrategy;
import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.service.GPCRMService;
import com.gpintegration.service.GPCommerceSCPIService;
import com.gpintegration.user.impl.Address;
import com.gpintegration.user.impl.Customer;
import com.gpintegration.user.impl.GPAddCustomerToSfdcRequest;
import com.gpintegration.user.impl.GPAddCustomerToSfdcResponse;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

/**
 * The Class GPDefaultCommerceSCPIService.
 *
 * @author vdannina
 */

public class GPDefaultCommerceSCPIService implements GPCommerceSCPIService ,Serializable {

	private static final String ACTIVE = "Active";

	private static final String APPLICATION_JSON = "application/json";

	private transient ModelService modelService;

	@Resource
	private transient ConfigurationService configurationService;
	private static final String CHAR_DELIMITER = "|";
	private static final String B2C_CUSTOMER = "Consumer Contact";
	private static final String B2B_CUSTOMER = "General Contact";
	private static final String CUSTOMER_CONTACT_ADDRESS = "Mailing";
	private static final String CUSTOMER_OTHERS_ADDRESS = "Other";
	private static final String DATA_ERRORS = "gp.scpi.contact.replication.data.errors";

	private static final String MISSING_EMAIL = "Missing email id";
	private static final String MISSING_NAME = "Missing name";




	private static final Logger LOG = LoggerFactory.getLogger(GPDefaultCommerceSCPIService.class);


	@Resource(name = "gpDefaultCustomerNameStrategy")
	private transient GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy;


	@Resource
	private transient GPCRMService gpCRMService;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.gpintegration.service.GPCommerceSCPIService#addCustomerToSfdc(de.hybris.
	 * platform.core.model.user.CustomerModel) Sync Data to SFDC through SCPI
	 */
	@Override
	public GPAddCustomerToSfdcResponse addCustomerToSfdc(CustomerModel customerModel) {
		GPAddCustomerToSfdcResponse responseBody = null;
			final ObjectMapper requestMapper = new ObjectMapper();
			final ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
			final RestTemplate restTemplate = new RestTemplate(factory);
			restTemplate.getMessageConverters().add(jacksonSupportsMoreTypes());
			restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
			String endPointUrlScpi = configurationService.getConfiguration()
					.getString(GpintegrationConstants.SCPI_ENDPOINT);
			try {
				GPAddCustomerToSfdcRequest gpCustomerToSfdcRequest = frameRequest(customerModel);
				final HttpHeaders headers =  gpCRMService.getSCPIHeaders();
				final HttpEntity<String> entity = new HttpEntity<>(
						requestMapper.writeValueAsString(gpCustomerToSfdcRequest), headers);
				final ResponseEntity<GPAddCustomerToSfdcResponse> custResponse = restTemplate.exchange(endPointUrlScpi,
						HttpMethod.POST, entity, GPAddCustomerToSfdcResponse.class);
				if (null != custResponse && null != custResponse.getBody()) {
					responseBody = custResponse.getBody();
					return responseBody;
				} else {
					return responseBody;
				}
			}catch (final HttpServerErrorException |HttpClientErrorException e) {
				LOG.error("HTTP Exception occured at Add Customer to SFDC through SCPI API {} with message {}", e , e.getMessage());
				return handleError(e);
			} catch (final Exception e) {
				LOG.error("Exeception occured at Add Customer to SFDC through SCPI API {} with message {}", e , e.getMessage());
				return responseBody;
			}
	}
	
	/**
	 * Method to extract the error form HTTP Exception a.
	 *
	 * @param e the exception
	 * @return the GP add customer to sfdc response
	 */
	private GPAddCustomerToSfdcResponse handleError(HttpStatusCodeException e) {
		GPAddCustomerToSfdcResponse responseBody = null;
		LOG.debug( "customer response {}  ",e.getResponseBodyAsString());
		final ObjectMapper mapper = new ObjectMapper();
		if(StringUtils.isNotEmpty(e.getResponseBodyAsString()) ) {
			try {
				 responseBody =mapper.readValue(e.getResponseBodyAsString(), GPAddCustomerToSfdcResponse.class);
				 if(responseBody != null && responseBody.getContactResponse()!= null) {
					 return responseBody;
				 }
			} catch (Exception e1) {
				LOG.error("Exeception occured at Add Customer to SFDC through SCPI API {} with message {}", e1 , e1.getMessage());
			}
		}
		return responseBody;
	}
	
	
	/**
	 * Method to check if the contact is a duplicate contact in SFDC.
	 *
	 * @param customerModel the customer model
	 * @return , true if the SFDC error code contains contact as DUPLICATE_VALUE else returns false
	 */
	public boolean isDataReplicationNeeded(CustomerModel customerModel) {
		LOG.debug("customerModel.getAddToSfdcErrorCode() {} ",customerModel.getAddToSfdcErrorCode() );
		String errors = configurationService.getConfiguration().getString(DATA_ERRORS);
		List <String> errorList = new ArrayList<>();
		//check for data errors 
		//If contact has data errors , then replication will not be retried
		if(StringUtils.isNotBlank(errors)){
			errorList = errors.contains(GpintegrationConstants.COMMA_SYMBOL) ?Arrays.asList(StringUtils.split(errors,GpintegrationConstants.COMMA_SYMBOL)):
				Collections.singletonList(errors) ;

			LOG.debug(" scpi errorcode {}" ,customerModel.getAddToSfdcErrorCode() );

			return customerModel.getUid().contains(CHAR_DELIMITER) &&  
					!(StringUtils.isNotBlank(customerModel.getAddToSfdcErrorCode()) &&
							errorList.contains(customerModel.getAddToSfdcErrorCode()));
	}
		return  customerModel.getUid().contains(CHAR_DELIMITER) ;
	}
	

	private HttpMessageConverter jacksonSupportsMoreTypes() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(
				Arrays.asList(MediaType.parseMediaType(APPLICATION_JSON), MediaType.APPLICATION_OCTET_STREAM));
		return converter;
	}

	/**
	 * Forms the request by fetching Customer Model for B2B and B2C.
	 *
	 * @param customerModel the customer model
	 * @return the GP add customer to sfdc request
	 * @throws GPIntegrationException the GP integration exception
	 */
	public GPAddCustomerToSfdcRequest frameRequest(CustomerModel customerModel) throws GPIntegrationException {
		GPAddCustomerToSfdcRequest gpCustomerToSfdcRequest = new GPAddCustomerToSfdcRequest();
		Customer cust = new Customer();
		final String uid = customerModel.getUid();
		String contactEmail = customerModel.getContactEmail();
		if (StringUtils.isNotEmpty(customerModel.getDisplayName()) && StringUtils.equalsIgnoreCase(customerModel.getDisplayName().trim(), GpintegrationConstants.GUEST_USER)) {
				final String[] emailId = uid.split(GpintegrationConstants.SITE_DELIMITER);
				contactEmail = emailId[1];
		} else {
				contactEmail = uid.split(GpintegrationConstants.SITE_DELIMITER)[0];
				cust.setRegisteredSite(uid.split(GpintegrationConstants.SITE_DELIMITER)[1]);
		}
		//Setting contact email and original uid to customer email as SFDC cannot have duplicate contacts for the same mail even for multiple sites.
		if(StringUtils.isNotEmpty(contactEmail)) {
			cust.setContactEmail(contactEmail);
			cust.setOriginalUid(contactEmail);
		} else {
			//throw error in case of missing email as uid is mandatory field in SFDC.
			LOG.error("SFDC Contact replication failed validation: Empty email id for <{}> ", customerModel.getUid());
			throw new GPIntegrationException(MISSING_EMAIL);
		}
		updateCustomerName(customerModel,cust);
		
		cust.setCustomerStatus(ACTIVE);
		List<Address> addressList = new ArrayList<>();
		if (customerModel instanceof B2BCustomerModel) {
			formB2BCustomerData(customerModel, cust, addressList);
		} else {
			formB2CCustomerData(customerModel, cust, addressList);
		}
		gpCustomerToSfdcRequest.setCustomer(cust);
		return gpCustomerToSfdcRequest;
	}
	
	/**
	 * Method to get name from customer model.
	 *
	 * @param customerModel the customer model
	 * @param customer the customer
	 * @throws GPIntegrationException the GP integration exception
	 */
	private void updateCustomerName(final CustomerModel customerModel,final Customer customer) throws GPIntegrationException {
		final String customerName = customerModel.getName();
		String[] customerData = null;
		if(StringUtils.isNotEmpty(customerName)) {
			//Split name by '|' 
			if(StringUtils.contains(customerName, GpintegrationConstants.PIPE_SYMBOL)) {
				customerData = gpDefaultCustomerNameStrategy.splitName(customerName);
			} else if(StringUtils.contains(customerName.trim(), GpintegrationConstants.SPACE)) {
				//Split name by space
				customerData = gpDefaultCustomerNameStrategy.splitNameBySpace(customerName.trim());
			} else {
				//Set default value
				customerData = new String[2];
				customerData[0] = customerName;
				customerData[1] = customerName;
			}
			customer.setFirstName(customerData[0]);
			customer.setLastName(customerData[1]);
		} else {
			//throw error in case of missing name as name is mandatory field in SFDC.
			LOG.error("SFDC Contact replication failed validation: Empty name <{}> ", customerModel.getUid());
			throw new GPIntegrationException(MISSING_NAME);
		}
	}


	/**
	 * Only for B2C CustomerModel.
	 *
	 * @param customerModel the customer model
	 * @param cust the customer
	 * @param addressList the address list
	 */
	private void formB2CCustomerData(CustomerModel customerModel, Customer cust, List<Address> addressList) {
		cust.setCustomerType(B2C_CUSTOMER);
		for (AddressModel custAddress : customerModel.getAddresses()) {
			Address address = new Address();
			address.setAddressType(null != custAddress.getShippingAddress() && custAddress.getShippingAddress()?CUSTOMER_CONTACT_ADDRESS : CUSTOMER_OTHERS_ADDRESS);
			address.setCity(custAddress.getTown());
			address.setCountryCode(custAddress.getCountry().getIsocode());
			address.setState(custAddress.getRegion().getName());
			address.setStreet(custAddress.getStreetname());
			if(null!=custAddress.getPhone1()){
				cust.setPhone(custAddress.getPhone1());
			}
			if(null!=custAddress.getCellphone()){
				cust.setCellphone(custAddress.getCellphone());
			}
			address.setPostalCode(custAddress.getPostalcode());
			addressList.add(address);
		}
		cust.setAddress(addressList);
	}

	/**
	 * Form B2B CustomerModel.
	 *
	 * @param customerModel the customer model
	 * @param cust the cust
	 * @param addressList the address list
	 */
	private void formB2BCustomerData(CustomerModel customerModel, Customer cust, List<Address> addressList) {
		cust.setCustomerType(B2B_CUSTOMER);
		B2BCustomerModel b2bcustomerModel = ((B2BCustomerModel) customerModel);
		if (!b2bcustomerModel.getDefaultB2BUnit().getAddresses().isEmpty()) {
			boolean isShippingAddressPresent = b2bcustomerModel.getDefaultB2BUnit().getAddresses().stream().anyMatch(address-> address.getShippingAddress() != null && address.getShippingAddress());
			for (AddressModel custAddress : b2bcustomerModel.getDefaultB2BUnit().getAddresses()) {
				Address address = new Address();
				address.setAddressType(null != custAddress.getShippingAddress() && custAddress.getShippingAddress()?CUSTOMER_CONTACT_ADDRESS : CUSTOMER_OTHERS_ADDRESS);
				address.setCity(custAddress.getTown());
				address.setCountryCode(custAddress.getCountry().getIsocode());
				address.setState(custAddress.getRegion().getName());
				address.setStreet(custAddress.getStreetname());
				if(null != custAddress.getCompany()){
					cust.setCompanyName(custAddress.getCompany());
				}
				if(null!=custAddress.getPhone1()){
					cust.setPhone(custAddress.getPhone1());
				}
				if(null!=custAddress.getCellphone()){
					cust.setCellphone(custAddress.getCellphone());
				}
				address.setPostalCode(custAddress.getPostalcode());
				addressList.add(address);
			}
			//SEt first address as default address in case shipping address is not present
			if(!isShippingAddressPresent) {
				addressList.stream().findFirst().ifPresent(address ->
					address.setAddressType(CUSTOMER_CONTACT_ADDRESS));
			}
			cust.setAddress(addressList);
		}
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
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

/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.dto.netsuite.GPNetsuiteCCAckDTO;
import com.gpintegration.dto.netsuite.GPNetsuiteCustomerDTO;
import com.gpintegration.dto.netsuite.GPNetsuiteOrderDTO;
import com.gpintegration.dto.netsuite.GPNetsuiteOrderUpdateDTO;
import com.gpintegration.dto.netsuite.GPNetsuiteResponseDTO;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.service.GPNetsuiteReplicationService;

import de.hybris.platform.servicelayer.config.ConfigurationService;

/**
 * The Class GPDefaultNetsuiteReplicationService.
 *
 * @author spandiyan
 */
public class GPDefaultNetsuiteReplicationService implements GPNetsuiteReplicationService {
	
	 private static final String UTF_8 = "UTF-8";

	private static final String NETSUITE_ACK_PAYMENT_FAILED = "Netsuite ack Payment Failed:";

	private static final String NETSUITE_CUSTOMER_REPLICATION_FAILED = "Netsuite Customer Replication Failed:";

	private static final String APPLICATION_JSON = "application/json";

	private ConfigurationService configurationService;
	
	private static final Logger LOG = Logger.getLogger(GPDefaultNetsuiteReplicationService.class);

	@Override
	public GPNetsuiteResponseDTO replicateCustomerData(GPNetsuiteCustomerDTO customerData) throws GPIntegrationException{
		MultiValueMap<String, String> netSuiteCustomerHeaders = new LinkedMultiValueMap<String, String>();
		GPNetsuiteResponseDTO customerResponse = null;
		try {
			netSuiteCustomerHeaders.add(GpintegrationConstants.HTTP_HEADER_CONTENT_TYPE, APPLICATION_JSON);
			netSuiteCustomerHeaders.add(GpintegrationConstants.HTTP_HEADER_AUTHORIZATION, formAuthorizeHeader(GpintegrationConstants.HTTP_METHOD_POST,configurationService.getConfiguration().getString("gp.netsuite.service.customer.deploy.id"),configurationService.getConfiguration().getString("gp.netsuite.service.customer.script.id")));
		
			URI netsuiteReplicateCustomerURI = new URI(configurationService.getConfiguration().getString(GpintegrationConstants.GP_NETSUITE_SERVICE_CUSTOMER_URI));
		
			RequestEntity<GPNetsuiteCustomerDTO> customerReplicationRequest = new RequestEntity<GPNetsuiteCustomerDTO>(customerData, netSuiteCustomerHeaders, HttpMethod.POST, netsuiteReplicateCustomerURI);
		
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<GPNetsuiteResponseDTO> customerResponseEntity = restTemplate.exchange(customerReplicationRequest, GPNetsuiteResponseDTO.class);
			if(null != customerResponseEntity) {
				customerResponse = customerResponseEntity.getBody();
			}
		}catch (HttpClientErrorException httpClientException) {
			LOG.error("Error occurred in netsuite customer replicate call"+httpClientException);
			throw new GPIntegrationException(NETSUITE_CUSTOMER_REPLICATION_FAILED+httpClientException.getMessage());
		} catch(Exception exception) {
			LOG.error("Error occurred in netsuite customer replicate call"+exception);
			throw new GPIntegrationException(NETSUITE_CUSTOMER_REPLICATION_FAILED+exception.getMessage());
		}
		return customerResponse;
	}

	@Override
	public GPNetsuiteResponseDTO replicateOrderData(GPNetsuiteOrderDTO orderData) throws GPIntegrationException{
		MultiValueMap<String, String> netSuiteOrderHeaders = new LinkedMultiValueMap<String, String>();
		GPNetsuiteResponseDTO orderResponse = null;
		try {
			netSuiteOrderHeaders.add(GpintegrationConstants.HTTP_HEADER_CONTENT_TYPE, APPLICATION_JSON);
			netSuiteOrderHeaders.add(GpintegrationConstants.HTTP_HEADER_AUTHORIZATION, formAuthorizeHeader(GpintegrationConstants.HTTP_METHOD_POST, configurationService.getConfiguration().getString("gp.netsuite.service.order.deploy.id"), configurationService.getConfiguration().getString("gp.netsuite.service.order.script.id")));
			URI netsuiteReplicateOrderURI = new URI(configurationService.getConfiguration().getString(GpintegrationConstants.GP_NETSUITE_SERVICE_ORDER_URI));
			RequestEntity<GPNetsuiteOrderDTO> orderReplicationRequest = new RequestEntity<GPNetsuiteOrderDTO>(orderData, netSuiteOrderHeaders, HttpMethod.POST, netsuiteReplicateOrderURI);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<GPNetsuiteResponseDTO> orderResponseEntity = restTemplate.exchange(orderReplicationRequest, GPNetsuiteResponseDTO.class);
			if(null != orderResponseEntity) {
				orderResponse = orderResponseEntity.getBody();
			}
		} catch(HttpClientErrorException httpClientException) {
			LOG.error("Error occurred in netsuite order replicate call"+httpClientException);
			throw new GPIntegrationException(NETSUITE_CUSTOMER_REPLICATION_FAILED+httpClientException.getMessage());
			
		} catch(Exception exception) {
			LOG.error("Error occurred in netsuite order replicate call"+exception);
			throw new GPIntegrationException(NETSUITE_CUSTOMER_REPLICATION_FAILED+exception.getMessage());
		}
		return orderResponse;
	}
	
	
	public GPNetsuiteOrderUpdateDTO getShippedOrders() throws GPIntegrationException{

		MultiValueMap<String, String> netSuiteOrderHeaders = new LinkedMultiValueMap<String, String>();
		GPNetsuiteOrderUpdateDTO orderResponse = null;
		try {
			netSuiteOrderHeaders.add(GpintegrationConstants.HTTP_HEADER_CONTENT_TYPE, APPLICATION_JSON);
			netSuiteOrderHeaders.add(GpintegrationConstants.HTTP_HEADER_AUTHORIZATION, formAuthorizeHeader(GpintegrationConstants.HTTP_METHOD_GET, configurationService.getConfiguration().getString("gp.netsuite.service.order.deploy.id"), configurationService.getConfiguration().getString("gp.netsuite.service.order.script.id")));
			URI netsuiteReplicateOrderURI = new URI(configurationService.getConfiguration().getString(GpintegrationConstants.GP_NETSUITE_SERVICE_ORDER_URI));
			RequestEntity<GPNetsuiteCCAckDTO> getshippedOrdersRequest = new RequestEntity<GPNetsuiteCCAckDTO>(netSuiteOrderHeaders, HttpMethod.GET, netsuiteReplicateOrderURI);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<GPNetsuiteOrderUpdateDTO> orderResponseEntity = restTemplate.exchange(getshippedOrdersRequest, GPNetsuiteOrderUpdateDTO.class);
			if(null != orderResponseEntity) {
				orderResponse = orderResponseEntity.getBody();
			}
		} catch(HttpClientErrorException httpClientException) {
			LOG.error("Error occurred in netsuite in fetching the shipped consignments"+httpClientException);
			throw new GPIntegrationException("fetch consignments Payment Failed:"+httpClientException.getMessage());
			
		} catch(Exception exception) {
			LOG.error("Error occurred in fetching the shipped consignments"+exception);
			throw new GPIntegrationException("fetch consignments Failed:"+exception.getMessage());
		}
		return orderResponse;
	
	}
	
	public GPNetsuiteResponseDTO acknowledgeCCOfPayment(GPNetsuiteCCAckDTO ccAckPaymentData) throws GPIntegrationException{

		MultiValueMap<String, String> netSuiteOrderHeaders = new LinkedMultiValueMap<String, String>();
		GPNetsuiteResponseDTO orderResponse = null;
		try {
			netSuiteOrderHeaders.add(GpintegrationConstants.HTTP_HEADER_CONTENT_TYPE, APPLICATION_JSON);
			netSuiteOrderHeaders.add(GpintegrationConstants.HTTP_HEADER_AUTHORIZATION, formAuthorizeHeader(GpintegrationConstants.HTTP_METHOD_PUT, configurationService.getConfiguration().getString("gp.netsuite.service.order.deploy.id"), configurationService.getConfiguration().getString("gp.netsuite.service.order.script.id")));
			URI netsuiteReplicateOrderURI = new URI(configurationService.getConfiguration().getString(GpintegrationConstants.GP_NETSUITE_SERVICE_ORDER_URI));
			RequestEntity<GPNetsuiteCCAckDTO> ccAckPaymentRequest = new RequestEntity<GPNetsuiteCCAckDTO>(ccAckPaymentData, netSuiteOrderHeaders, HttpMethod.PUT, netsuiteReplicateOrderURI);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<GPNetsuiteResponseDTO> orderResponseEntity = restTemplate.exchange(ccAckPaymentRequest, GPNetsuiteResponseDTO.class);
			if(null != orderResponseEntity) {
				orderResponse = orderResponseEntity.getBody();
			}
		} catch(HttpClientErrorException httpClientException) {
			LOG.error("Error occurred in netsuite ack Payment call"+httpClientException);
			throw new GPIntegrationException(NETSUITE_ACK_PAYMENT_FAILED+httpClientException.getMessage());
			
		} catch(Exception exception) {
			LOG.error("Error occurred in netsuite ack Payment call"+exception);
			throw new GPIntegrationException(NETSUITE_ACK_PAYMENT_FAILED+exception.getMessage());
		}
		return orderResponse;
	
		
	}

	/**
	 * Method which forms the OAuth1 authorization header as per the OAuth1
	 * standard.
	 *
	 * @param httpMethod the http method
	 * @param deploy     the deploy
	 * @param script     the script
	 * @return OAuth1 Authorization header details
	 * @throws Exception the exception
	 */
	public String formAuthorizeHeader(String httpMethod, String deploy, String script) throws Exception{
		Calendar calendar = Calendar.getInstance();
		long timeInMilliSeconds = calendar.getTimeInMillis();
		String oauthTimeStamp = (new Long(timeInMilliSeconds/1000)).toString();
		
		String randomString = UUID.randomUUID().toString();
		randomString = randomString.replaceAll("-", "");
		String oauthNonce = randomString;
		
		String parameterString = "deploy="+deploy+"&oauth_consumer_key=" + configurationService.getConfiguration().getString("gp.netsuite.oauth.consumer.key") + "&oauth_nonce=" + oauthNonce + "&oauth_signature_method=" + GpintegrationConstants.NETSUITE_OAUTH_SIGNATURE_METHOD + 
				"&oauth_timestamp=" + oauthTimeStamp + "&oauth_token=" + encode(configurationService.getConfiguration().getString("gp.netsuite.oauth.access.token"), UTF_8) + "&oauth_version="+GpintegrationConstants.NETSUITE_OAUTH_VERSION+"&script="+script;
		
		String signatureBaseString = httpMethod + "&"+ encode(configurationService.getConfiguration().getString("gp.netsuite.service.customer.order.url"), UTF_8) + "&" + encode(parameterString, UTF_8);

		String oauthSignature = "";
		
		oauthSignature = computeOAuthSignature(signatureBaseString, configurationService.getConfiguration().getString("gp.netsuite.oauth.consumer.secret") + "&" + configurationService.getConfiguration().getString("gp.netsuite.oauth.token.secret"), GpintegrationConstants.NETSUITE_OAUTH_SIGNATURE_ALGORITHM);
		
		String authorizationHeaderString = "OAuth realm=\""+configurationService.getConfiguration().getString("gp.netsuite.oauth.realm")+"\",oauth_consumer_key=\"" + configurationService.getConfiguration().getString("gp.netsuite.oauth.consumer.key") + "\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"" + oauthTimeStamp + 
	    		"\",oauth_nonce=\"" + oauthNonce + "\",oauth_version=\""+GpintegrationConstants.NETSUITE_OAUTH_VERSION+"\",oauth_signature=\"" + encode(oauthSignature, UTF_8) + "\",oauth_token=\"" + encode(configurationService.getConfiguration().getString("gp.netsuite.oauth.access.token"), UTF_8) + "\"";
		return authorizationHeaderString;
	}
	
	
	/**
	 * Compute O auth signature.
	 *
	 * @param baseString    the base string
	 * @param keyString     the key string
	 * @param algorithmName the algorithm name
	 * @return the string
	 * @throws GeneralSecurityException     the general security exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public String computeOAuthSignature(String baseString, String keyString, String algorithmName) throws GeneralSecurityException, UnsupportedEncodingException {
	    SecretKey secretKey = null;

	    byte[] keyBytes = keyString.getBytes();
	    secretKey = new SecretKeySpec(keyBytes, algorithmName);

	    Mac mac = Mac.getInstance(algorithmName);
	    mac.init(secretKey);

	    byte[] text = baseString.getBytes();

	    return new String(Base64.encodeBase64(mac.doFinal(text))).trim();
	}
	
	/**
	 * Encodes the parameter value.
	 *
	 * @param value the value
	 * @param encodingType the encoding type
	 * @return encoded value
	 */
	public String encode(String value, String encodingType) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(value, encodingType);
        } catch (UnsupportedEncodingException ignore) {
        	LOG.error(ignore.getMessage(), ignore);
        }
        StringBuilder buf = null;
        if(StringUtils.isNotBlank(encoded)) {
        buf=new StringBuilder(encoded.length());
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                buf.append("%2A");
            } else if (focus == '+') {
                buf.append("%20");
            } else if (focus == '%' && (i + 1) < encoded.length()
                    && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                buf.append('~');
                i += 2;
            } else {
                buf.append(focus);
            }
        }
        }
        return null!=buf ? buf.toString() : null;
    }

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
}
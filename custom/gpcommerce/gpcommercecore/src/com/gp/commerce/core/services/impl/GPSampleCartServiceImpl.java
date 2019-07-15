/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.impl;
//com.gp.commerce.core.services.impl.GPSampleCartServiceImpl
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.nio.charset.Charset;
import java.util.Arrays;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import com.google.api.client.util.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.exceptions.GPSampleCartException;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.core.services.GPSampleCartService;
import com.gp.commerce.order.data.GPOrderSampleErrorResponseData;
import com.gp.commerce.order.data.GPSampleCartResponseData;
import com.gp.commerce.order.data.SampleCartRequestData;

public class GPSampleCartServiceImpl implements GPSampleCartService {

	private CartService cartService;
	private Converter<CartModel, SampleCartRequestData> gpSampleCartConverter;
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	@Resource
	private transient ConfigurationService configurationService;

	private static final String APPLICATION_JSON = "application/json";

	private static final String AUTHORIZATION = "Authorization";

	private static final String BASIC = "Basic ";

	private static final String US_ASCII = "US-ASCII";

	private static final Logger LOG = Logger.getLogger(GPSampleCartServiceImpl.class);

	@Override
	public GPSampleCartResponseData sendRequestToGpExpress() throws GPSampleCartException {
		final CartModel cart = getCartService().getSessionCart();
		final SampleCartRequestData sampleCartData = getGpSampleCartConverter().convert(cart);
		GPSampleCartResponseData responseBody = null;
		final String endPointUrlScpi = configurationService.getConfiguration()
				.getString(GpcommerceCoreConstants.SAMPLECART_ENDPOINT);
		if(LOG.isDebugEnabled()==true)
		{
			LOG.debug("####### end point Url :"+endPointUrlScpi);
		}
		final ObjectMapper requestMapper = new ObjectMapper();
		final RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(jacksonSupportsMoreTypes());
		final String userName = configurationService.getConfiguration()
				.getString(GpcommerceCoreConstants.SAMPLECART_USERNAME);
		final String password = configurationService.getConfiguration()
				.getString(GpcommerceCoreConstants.SAMPLECART_PASSWORD);

		final HttpHeaders headers = new HttpHeaders();
		final String auth = userName + ":" + password;
		final byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName(US_ASCII)));
		final String authHeader = BASIC + new String(encodedAuth);
		headers.set(AUTHORIZATION, authHeader);
		if(LOG.isDebugEnabled()==true)
		{
			LOG.debug("####### authorisation Header: "+auth);
		}
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = null;
		try {
			entity = new HttpEntity<>(requestMapper.writeValueAsString(sampleCartData), headers);
			if(LOG.isDebugEnabled()==true)
			{
				LOG.debug("####### Order Sample Cart Request "+requestMapper.writeValueAsString(sampleCartData));
			}
			final String response = configurationService.getConfiguration()
					.getString(GpcommerceCoreConstants.SENDDUMMY_RESPONSE);
			if ("sampleresponse".equals(response)) {
				responseBody = orderdummyCart();
				return responseBody;
			} else {
				final ResponseEntity<GPSampleCartResponseData> cartResponse = restTemplate.exchange(endPointUrlScpi,
						HttpMethod.POST, entity, GPSampleCartResponseData.class);
				if (null != cartResponse && null != cartResponse.getBody()) {
					responseBody = cartResponse.getBody();
					if(LOG.isDebugEnabled()==true)
					{
						LOG.debug("####### Order Sample Cart Response "+requestMapper.writeValueAsString(responseBody));
					}
					return responseBody;
				} else {
					if(LOG.isDebugEnabled()==true)
					{
						LOG.debug("####### Order Sample Cart Response "+requestMapper.writeValueAsString(responseBody));
					}
					return responseBody;
				}
				
			}
		} catch (final GPSampleCartException sampleCartException) {
			if(LOG.isDebugEnabled()==true)
			{
				LOG.debug("####### error in order Sample Cart: "+sampleCartException.getStackTrace());
			}
			throw sampleCartException;
		}
		catch (final Exception e) {
			LOG.debug(e.getMessage(), e);
			throw new GPSampleCartException(GpcommerceCoreConstants.SAMPLECART_ERROR_CODE,
					configurationService.getConfiguration()
					.getString(GpcommerceCoreConstants.SAMPLECART_ERROR_MESSAGE));
		}

	}

	public GPSampleCartResponseData orderdummyCart() throws GPSampleCartException {
		final GPSampleCartResponseData respdata = new GPSampleCartResponseData();
		final String response = configurationService.getConfiguration()
				.getString(GpcommerceCoreConstants.SENDDUMMY_SUCCESS_RESPONSE);
		if ("SUCCESS".equals(response)) {
			respdata.setSuccess("true");
		} else {
			throw new GPSampleCartException(GpcommerceCoreConstants.SAMPLECART_ERROR_CODE,
					configurationService.getConfiguration()
					.getString(GpcommerceCoreConstants.SAMPLECART_FAIL_RESPONSE));
		}
		return respdata;

	}

	private HttpMessageConverter jacksonSupportsMoreTypes() {
		final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(
				Arrays.asList(MediaType.parseMediaType(APPLICATION_JSON), MediaType.APPLICATION_OCTET_STREAM));
		return converter;
	}

	public CartService getCartService() {
		return cartService;
	}

	public void setCartService(final CartService cartService) {
		this.cartService = cartService;
	}

	public Converter<CartModel, SampleCartRequestData> getGpSampleCartConverter() {
		return gpSampleCartConverter;
	}

	public void setGpSampleCartConverter(final Converter<CartModel, SampleCartRequestData> gpSampleCartConverter) {
		this.gpSampleCartConverter = gpSampleCartConverter;
	}

}

/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceException;


import com.gp.businessobjects.DataServicesServer;
import com.gp.businessobjects.RealTimeServices;
import com.gp.businessobjects.service.addr.cleanse.input.DataSet;
import com.gp.businessobjects.service.addr.cleanse.input.Record;
import com.gp.businessobjects.service.addr.suggestions.output.ENTRY;
import com.gp.businessobjects.service.addr.suggestions.output.SUGGESTION;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.service.GPAddressVerificationService;
import com.gpintegration.utils.GPHeaderHandlerResolver;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Default implementation of {@link GPAddressVerificationService}
 */
public class GPDefaultAddressVerificationService implements GPAddressVerificationService {
	private ConfigurationService configurationService;
	private static final Logger LOG = LoggerFactory.getLogger(GPDefaultAddressVerificationService.class);

	private static final String AVS="AVS";
	
	private static final String CODE_ERROR="avs.error.code";
	
	private static final String CODE_SUCCESS="avs.success.code";
	
	private static final String AVS_STATUS_CODE_SUCCESS="avs.status.code.success";

	private static final String AVS_VALIDATION_ERROR_FLAG = "avs.validation.error.is.enable";
	
	private static final String AVS_ERROR_MESSAGE="avs.error.msg";
	
	private static final String IS_AVS_ENABLED = "isAVSEnabled";
	
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;




	@Override
	public AddressData verifyAddress(AddressData addressData) throws GPIntegrationException {
		//Return input address as default if AVS is not enabled
		if(!isAvsEnabled()) {
			return addressData;
		}

		com.gp.businessobjects.service.addr.cleanse.output.DataSet dsOutput = null;
		try {
			RealTimeServices rts = getRealTimeServices();
			final com.gp.businessobjects.service.addr.cleanse.input.ObjectFactory objFactory = new com.gp.businessobjects.service.addr.cleanse.input.ObjectFactory();
			final DataSet dsInput = objFactory.createDataSet();
			final Record record = objFactory.createRecord();
			String location = addressData.getLine1();

			if (addressData.getLine2() != null) {
				location = location + StringUtils.SPACE + addressData.getLine2();
			}
			record.setSTREET(location);
			record.setCITY1(addressData.getTown());
			record.setCOUNTRY(addressData.getCountry().getIsocode());
			record.setREGION(addressData.getRegion().getIsocodeShort());
			record.setPOSTCODE1(addressData.getPostalCode());

			dsInput.getRecord().add(record);
			dsOutput = rts.addrCleanseHybris(dsInput);

			if (dsOutput != null && dsOutput.getRecord() !=null && dsOutput.getRecord().get(0) !=null) {
				com.gp.businessobjects.service.addr.cleanse.output.Record outputRecord = dsOutput.getRecord().get(0);
				addressData.setTown(outputRecord.getCITY1());
				addressData.setPostalCode(outputRecord.getPOSTCODE1());
				addressData.setLine1(StringUtils.isNotEmpty(outputRecord.getHOUSENUM1())
						? outputRecord.getHOUSENUM1() + StringUtils.SPACE + outputRecord.getSTREET()
						: outputRecord.getSTREET());
				StringBuilder addressLine2 = new StringBuilder();
				if(StringUtils.isNotEmpty(outputRecord.getHOUSENUM2())) {
					addressLine2.append(outputRecord.getHOUSENUM2()).append(StringUtils.SPACE);
				}
				if(StringUtils.isNotEmpty(outputRecord.getSTRSUPPL1())) {
					addressLine2.append(outputRecord.getSTRSUPPL1()).append(StringUtils.SPACE);
				}
				if(StringUtils.isNotEmpty(outputRecord.getSTRSUPPL2())) {
					addressLine2.append(outputRecord.getSTRSUPPL2()).append(StringUtils.SPACE);
				}
				if(StringUtils.isNotEmpty(outputRecord.getSTRSUPPL3())) {
					addressLine2.append(outputRecord.getSTRSUPPL3()).append(StringUtils.SPACE);
				}
				if(StringUtils.isNotEmpty(outputRecord.getLOCATION())) {
					addressLine2.append(outputRecord.getLOCATION()).append(StringUtils.SPACE);
				}
				addressData.setLine2(
						StringUtils.isNotBlank(addressLine2.toString()) ? addressLine2.toString().trim() : null);

				RegionData regionData = addressData.getRegion();
				regionData.setIsocodeShort(outputRecord.getREGION());
				regionData.setIsocode(outputRecord.getCOUNTRY()+"-"+outputRecord.getREGION());
				regionData.setCountryIso(outputRecord.getCOUNTRY());
				regionData.setName(outputRecord.getREGION());
				addressData.setRegion(regionData);
				CountryData countryData = addressData.getCountry();
				countryData.setName(outputRecord.getCOUNTRY());
				addressData.setCountry(countryData);
				String statusCode=outputRecord.getSTATUSCODE();
				if(statusCode!=null) {
					String correctionMessage=configurationService.getConfiguration().getString(String.valueOf(statusCode.charAt(1)));
					addressData.setFormattedAddress(correctionMessage);
					if(StringUtils.isNotBlank(outputRecord.getINFOCODE()) && StringUtils.equals(statusCode, configurationService.getConfiguration().getString(AVS_STATUS_CODE_SUCCESS))
							&& configurationService.getConfiguration().getBoolean(AVS_VALIDATION_ERROR_FLAG)){
						addressData.setCode(configurationService.getConfiguration().getString(CODE_ERROR));
						addressData.setFormattedAddress(configurationService.getConfiguration().getString(AVS_ERROR_MESSAGE));
					} else {
						addressData.setCode(configurationService.getConfiguration().getString(CODE_SUCCESS));
					}
				}
			}

		} catch (final WebServiceException wse) {
			LOG.error("Error in Address Verification service :Web service Exception :" + wse.getMessage(),wse);
			throw new GPIntegrationException(GPIntegrationException.ADDRESS_VERIFICATION_SERVICE_ERROR + wse.getMessage(),wse);
		} catch (final Exception ex) {
			LOG.error(" Error in Address Verification service " + ex.getMessage(),ex);
			throw new GPIntegrationException(GPIntegrationException.ADDRESS_VERIFICATION_SERVICE_ERROR +ex.getMessage(),ex);
		}

		return addressData;

	}


	@Override
	public List<AddressData> suggestAddresses(AddressData addressData) throws GPIntegrationException {

		com.gp.businessobjects.service.addr.suggestions.output.DataSet dsSuggestionOutput = null;
		List<AddressData> addressList = new ArrayList<AddressData>();
		try {
			RealTimeServices rts = getRealTimeServices();
			final com.gp.businessobjects.service.addr.suggestions.input.ObjectFactory suggestionsObjFactory = new com.gp.businessobjects.service.addr.suggestions.input.ObjectFactory();
			com.gp.businessobjects.service.addr.suggestions.input.DataSet suggestionsDataSet = suggestionsObjFactory
					.createDataSet();
			com.gp.businessobjects.service.addr.suggestions.input.Record suggestionsRecord = suggestionsObjFactory
					.createRecord();

			String location = addressData.getLine1();

			if (addressData.getLine2() != null) {
				location = location + StringUtils.SPACE + addressData.getLine2();
			}
			suggestionsRecord.setSTREET(location);
			suggestionsRecord.setCITY1(addressData.getTown());
			suggestionsRecord.setCOUNTRY(addressData.getCountry().getName());
			suggestionsRecord.setREGION(addressData.getRegion().getName());
			suggestionsRecord.setPOSTCODE1(addressData.getPostalCode());

			suggestionsDataSet.getRecord().add(suggestionsRecord);

			dsSuggestionOutput = rts.addrCleanseSuggestionsHybris(suggestionsDataSet);

			if (dsSuggestionOutput != null && dsSuggestionOutput.getRecord() !=null && dsSuggestionOutput.getRecord().get(0) !=null) {

				List<SUGGESTION> suggestions = dsSuggestionOutput.getRecord().get(0).getSUGGESTION();
				if (suggestions != null && suggestions.get(0).getENTRY()!=null) {
					List<ENTRY> entryList = suggestions.get(0).getENTRY();
					for (ENTRY suggestionEntry:entryList) {
						AddressData addressSuggestion = (AddressData) org.apache.commons.lang.SerializationUtils.clone(addressData);
						addressSuggestion.setLine1(suggestionEntry.getSUGGFULLADDRLN());
						addressSuggestion.setLine2(suggestionEntry.getSUGGFULLLASTLN());
						String[] fullAddress = suggestionEntry.getSUGGSINGLEADDR().split(",");
						String postCode = fullAddress[fullAddress.length - 1];
						addressSuggestion.setPostalCode(postCode.trim());
						addressList.add(addressSuggestion);
					}
				}

			}

		} catch (final WebServiceException wse) {
			LOG.error("Error in Address Suggestion service :Web service Exception :" + wse.getMessage(),wse);
			throw new GPIntegrationException(GPIntegrationException.ADDRESS_SUGGESTIONS_SERVICE_ERROR);
		} catch (final Exception ex) {
			LOG.error(" Error in Address Suggestion service " + ex.getMessage(),ex);
			throw new GPIntegrationException(GPIntegrationException.ADDRESS_SUGGESTIONS_SERVICE_ERROR);
		}

		return addressList;
	}

	public RealTimeServices getRealTimeServices() {
			final DataServicesServer dss = new DataServicesServer();
			final GPHeaderHandlerResolver gpHandlerResolver = new GPHeaderHandlerResolver(null,AVS);
			dss.setHandlerResolver(gpHandlerResolver);
			return dss.getRealTimeServices();

	}
	
	private boolean isAvsEnabled() {
		//Get site level AVS configuration , set default to true
		return StringUtils.isNotEmpty(cmsSiteService.getSiteConfig(IS_AVS_ENABLED)) ?Boolean.valueOf(cmsSiteService.getSiteConfig(IS_AVS_ENABLED)) :true;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}




}

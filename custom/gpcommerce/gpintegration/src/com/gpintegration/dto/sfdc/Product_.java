/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.dto.sfdc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "code", "thumbnailURL", "sampleIndicator", "samplesActive", "gpmsMaxOrderLimit",
		"sampleUOMDescription", "shippingRestrictionStates", "shippingRestrictionCountries",
		"shippingRestrictionMethods", "Competitors" })
public class Product_ {

	@JsonProperty("code")
	private String code;
	@JsonProperty("thumbnailURL")
	private String thumbnailURL;
	@JsonProperty("sampleIndicator")
	private String sampleIndicator;
	@JsonProperty("samplesActive")
	private String samplesActive;
	@JsonProperty("gpmsMaxOrderLimit")
	private String gpmsMaxOrderLimit;
	@JsonProperty("sampleUOMDescription")
	private String sampleUOMDescription;
	@JsonProperty("shippingRestrictionStates")
	private String shippingRestrictionStates;
	@JsonProperty("shippingRestrictionCountries")
	private String shippingRestrictionCountries;
	@JsonProperty("shippingRestrictionMethods")
	private String shippingRestrictionMethods;
	@JsonProperty("Competitors")
	private List<Competitor> competitors = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("code")
	public String getCode() {
		return code;
	}

	@JsonProperty("code")
	public void setCode(String code) {
		this.code = code;
	}

	@JsonProperty("thumbnailURL")
	public String getThumbnailURL() {
		return thumbnailURL;
	}

	@JsonProperty("thumbnailURL")
	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}

	@JsonProperty("sampleIndicator")
	public String getSampleIndicator() {
		return sampleIndicator;
	}

	@JsonProperty("sampleIndicator")
	public void setSampleIndicator(String sampleIndicator) {
		this.sampleIndicator = sampleIndicator;
	}

	@JsonProperty("samplesActive")
	public String getSamplesActive() {
		return samplesActive;
	}

	@JsonProperty("samplesActive")
	public void setSamplesActive(String samplesActive) {
		this.samplesActive = samplesActive;
	}

	@JsonProperty("gpmsMaxOrderLimit")
	public String getGpmsMaxOrderLimit() {
		return gpmsMaxOrderLimit;
	}

	@JsonProperty("gpmsMaxOrderLimit")
	public void setGpmsMaxOrderLimit(String gpmsMaxOrderLimit) {
		this.gpmsMaxOrderLimit = gpmsMaxOrderLimit;
	}

	@JsonProperty("sampleUOMDescription")
	public String getSampleUOMDescription() {
		return sampleUOMDescription;
	}

	@JsonProperty("sampleUOMDescription")
	public void setSampleUOMDescription(String sampleUOMDescription) {
		this.sampleUOMDescription = sampleUOMDescription;
	}

	@JsonProperty("shippingRestrictionStates")
	public String getShippingRestrictionStates() {
		return shippingRestrictionStates;
	}

	@JsonProperty("shippingRestrictionStates")
	public void setShippingRestrictionStates(String shippingRestrictionStates) {
		this.shippingRestrictionStates = shippingRestrictionStates;
	}

	@JsonProperty("shippingRestrictionCountries")
	public String getShippingRestrictionCountries() {
		return shippingRestrictionCountries;
	}

	@JsonProperty("shippingRestrictionCountries")
	public void setShippingRestrictionCountries(String shippingRestrictionCountries) {
		this.shippingRestrictionCountries = shippingRestrictionCountries;
	}

	@JsonProperty("shippingRestrictionMethods")
	public String getShippingRestrictionMethods() {
		return shippingRestrictionMethods;
	}

	@JsonProperty("shippingRestrictionMethods")
	public void setShippingRestrictionMethods(String shippingRestrictionMethods) {
		this.shippingRestrictionMethods = shippingRestrictionMethods;
	}

	@JsonProperty("Competitors")
	public List<Competitor> getCompetitors() {
		return competitors;
	}

	@JsonProperty("Competitors")
	public void setCompetitors(List<Competitor> competitors) {
		this.competitors = competitors;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
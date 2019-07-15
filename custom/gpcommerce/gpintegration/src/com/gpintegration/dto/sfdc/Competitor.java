/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.dto.sfdc;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "competitorNumber", "street", "city", "competitorID", "competitorName", "country", "postalCode",
		"state", "competitorProductNumber", "competitorBrand", "competitorCaseBUOM", "competitorCaseQtyBUOM",
		"competitorMaterial", "competitorMaterialColor", "competitorMaterialDescription", "competitorMaterialName",
		"competitorProductType", "externalID", "gpSku", "compSKU", "relationshipType" })
public class Competitor {

	@JsonProperty("competitorNumber")
	private String competitorNumber;
	@JsonProperty("street")
	private String street;
	@JsonProperty("city")
	private String city;
	@JsonProperty("competitorID")
	private String competitorID;
	@JsonProperty("competitorName")
	private String competitorName;
	@JsonProperty("country")
	private String country;
	@JsonProperty("postalCode")
	private String postalCode;
	@JsonProperty("state")
	private String state;
	@JsonProperty("competitorProductNumber")
	private String competitorProductNumber;
	@JsonProperty("competitorBrand")
	private String competitorBrand;
	@JsonProperty("competitorCaseBUOM")
	private String competitorCaseBUOM;
	@JsonProperty("competitorCaseQtyBUOM")
	private String competitorCaseQtyBUOM;
	@JsonProperty("competitorMaterial")
	private String competitorMaterial;
	@JsonProperty("competitorMaterialColor")
	private String competitorMaterialColor;
	@JsonProperty("competitorMaterialDescription")
	private String competitorMaterialDescription;
	@JsonProperty("competitorMaterialName")
	private String competitorMaterialName;
	@JsonProperty("competitorProductType")
	private String competitorProductType;
	@JsonProperty("externalID")
	private String externalID;
	@JsonProperty("gpSku")
	private String gpSku;
	@JsonProperty("compSKU")
	private String compSKU;
	@JsonProperty("relationshipType")
	private String relationshipType;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("competitorNumber")
	public String getCompetitorNumber() {
		return competitorNumber;
	}

	@JsonProperty("competitorNumber")
	public void setCompetitorNumber(String competitorNumber) {
		this.competitorNumber = competitorNumber;
	}
	
	@JsonProperty("street")
	public String getStreet() {
		return street;
	}

	@JsonProperty("street")
	public void setStreet(String street) {
		this.street = street;
	}

	@JsonProperty("city")
	public String getCity() {
		return city;
	}

	@JsonProperty("city")
	public void setCity(String city) {
		this.city = city;
	}

	@JsonProperty("competitorID")
	public String getCompetitorID() {
		return competitorID;
	}

	@JsonProperty("competitorID")
	public void setCompetitorID(String competitorID) {
		this.competitorID = competitorID;
	}

	@JsonProperty("competitorName")
	public String getCompetitorName() {
		return competitorName;
	}

	@JsonProperty("competitorName")
	public void setCompetitorName(String competitorName) {
		this.competitorName = competitorName;
	}

	@JsonProperty("country")
	public String getCountry() {
		return country;
	}

	@JsonProperty("country")
	public void setCountry(String country) {
		this.country = country;
	}

	@JsonProperty("postalCode")
	public String getPostalCode() {
		return postalCode;
	}

	@JsonProperty("postalCode")
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	@JsonProperty("state")
	public String getState() {
		return state;
	}

	@JsonProperty("state")
	public void setState(String state) {
		this.state = state;
	}

	@JsonProperty("competitorProductNumber")
	public String getCompetitorProductNumber() {
		return competitorProductNumber;
	}

	@JsonProperty("competitorProductNumber")
	public void setCompetitorProductNumber(String competitorProductNumber) {
		this.competitorProductNumber = competitorProductNumber;
	}

	@JsonProperty("competitorBrand")
	public String getCompetitorBrand() {
		return competitorBrand;
	}

	@JsonProperty("competitorBrand")
	public void setCompetitorBrand(String competitorBrand) {
		this.competitorBrand = competitorBrand;
	}

	@JsonProperty("competitorCaseBUOM")
	public String getCompetitorCaseBUOM() {
		return competitorCaseBUOM;
	}

	@JsonProperty("competitorCaseBUOM")
	public void setCompetitorCaseBUOM(String competitorCaseBUOM) {
		this.competitorCaseBUOM = competitorCaseBUOM;
	}

	@JsonProperty("competitorCaseQtyBUOM")
	public String getCompetitorCaseQtyBUOM() {
		return competitorCaseQtyBUOM;
	}

	@JsonProperty("competitorCaseQtyBUOM")
	public void setCompetitorCaseQtyBUOM(String competitorCaseQtyBUOM) {
		this.competitorCaseQtyBUOM = competitorCaseQtyBUOM;
	}

	@JsonProperty("competitorMaterial")
	public String getCompetitorMaterial() {
		return competitorMaterial;
	}

	@JsonProperty("competitorMaterial")
	public void setCompetitorMaterial(String competitorMaterial) {
		this.competitorMaterial = competitorMaterial;
	}

	@JsonProperty("competitorMaterialColor")
	public String getCompetitorMaterialColor() {
		return competitorMaterialColor;
	}

	@JsonProperty("competitorMaterialColor")
	public void setCompetitorMaterialColor(String competitorMaterialColor) {
		this.competitorMaterialColor = competitorMaterialColor;
	}

	@JsonProperty("competitorMaterialDescription")
	public String getCompetitorMaterialDescription() {
		return competitorMaterialDescription;
	}

	@JsonProperty("competitorMaterialDescription")
	public void setCompetitorMaterialDescription(String competitorMaterialDescription) {
		this.competitorMaterialDescription = competitorMaterialDescription;
	}

	@JsonProperty("competitorMaterialName")
	public String getCompetitorMaterialName() {
		return competitorMaterialName;
	}

	@JsonProperty("competitorMaterialName")
	public void setCompetitorMaterialName(String competitorMaterialName) {
		this.competitorMaterialName = competitorMaterialName;
	}

	@JsonProperty("competitorProductType")
	public String getCompetitorProductType() {
		return competitorProductType;
	}

	@JsonProperty("competitorProductType")
	public void setCompetitorProductType(String competitorProductType) {
		this.competitorProductType = competitorProductType;
	}

	@JsonProperty("externalID")
	public String getExternalID() {
		return externalID;
	}

	@JsonProperty("externalID")
	public void setExternalID(String externalID) {
		this.externalID = externalID;
	}

	@JsonProperty("gpSku")
	public String getGpSku() {
		return gpSku;
	}

	@JsonProperty("gpSku")
	public void setGpSku(String gpSku) {
		this.gpSku = gpSku;
	}

	@JsonProperty("compSKU")
	public String getCompSKU() {
		return compSKU;
	}

	@JsonProperty("compSKU")
	public void setCompSKU(String compSKU) {
		this.compSKU = compSKU;
	}

	@JsonProperty("relationshipType")
	public String getRelationshipType() {
		return relationshipType;
	}

	@JsonProperty("relationshipType")
	public void setRelationshipType(String relationshipType) {
		this.relationshipType = relationshipType;
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
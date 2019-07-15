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
@JsonPropertyOrder({ "MainProduct", "RelatedProduct", "RelationshipType" })
public class Relationship {

	@JsonProperty("MainProduct")
	private String mainProduct;
	@JsonProperty("RelatedProduct")
	private String relatedProduct;
	@JsonProperty("RelationshipType")
	private String relationshipType;
	@JsonProperty("DeletionFlag")
	private String deletionFlag;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("MainProduct")
	public String getMainProduct() {
		return mainProduct;
	}

	@JsonProperty("MainProduct")
	public void setMainProduct(String mainProduct) {
		this.mainProduct = mainProduct;
	}

	@JsonProperty("RelatedProduct")
	public String getRelatedProduct() {
		return relatedProduct;
	}

	@JsonProperty("RelatedProduct")
	public void setRelatedProduct(String relatedProduct) {
		this.relatedProduct = relatedProduct;
	}

	@JsonProperty("RelationshipType")
	public String getRelationshipType() {
		return relationshipType;
	}

	@JsonProperty("RelationshipType")
	public void setRelationshipType(String relationshipType) {
		this.relationshipType = relationshipType;
	}
	
	@JsonProperty("DeletionFlag")
	public String getDeletionFlag() {
	return deletionFlag;
	}

	@JsonProperty("DeletionFlag")
	public void setDeletionFlag(String deletionFlag) {
	this.deletionFlag = deletionFlag;
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
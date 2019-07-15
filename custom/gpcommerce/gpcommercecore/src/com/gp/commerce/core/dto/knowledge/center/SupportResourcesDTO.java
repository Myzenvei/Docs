/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.dto.knowledge.center;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupportResourcesDTO {
	@JsonProperty("trainingGroupId")
	List<Integer> trainingGroupId;
	
	@JsonProperty("resourceId")
	Integer resourceId;

	@JsonProperty("trainingId")
	Integer trainingId;

	
	@JsonProperty("resourceTitle")
	String resourceTitle;
	
	
	@JsonProperty("resourceText")
	String resourceText;
	
	
	@JsonProperty("startDate")
	String startDate;

	@JsonProperty("resourceImageURL")
	String resourceImageURL;

	@JsonProperty("resourceURL")
	String resourceURL;
	
	@JsonProperty("sku")
	String sku;
	
	@JsonProperty("tags")
	List<String> tags;
	
	@JsonProperty("mediaType")
	String mediaType;
	
	@JsonProperty("trainingBrands")
	List<String> trainingBrands;

	@JsonProperty("searchTerms")
	List<String> searchTerms;

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public Integer getTrainingId() {
		return trainingId;
	}

	public void setTrainingId(Integer trainingId) {
		this.trainingId = trainingId;
	}

	public String getResourceTitle() {
		return resourceTitle;
	}

	public void setResourceTitle(String resourceTitle) {
		this.resourceTitle = resourceTitle;
	}

	public String getResourceText() {
		return resourceText;
	}

	public void setResourceText(String resourceText) {
		this.resourceText = resourceText;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getResourceImageURL() {
		return resourceImageURL;
	}

	public void setResourceImageURL(String resourceImageURL) {
		this.resourceImageURL = resourceImageURL;
	}

	public String getResourceURL() {
		return resourceURL;
	}

	public void setResourceURL(String resourceURL) {
		this.resourceURL = resourceURL;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	
	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public List<Integer> getTrainingGroupId() {
		return trainingGroupId;
	}

	public void setTrainingGroupId(List<Integer> trainingGroupId) {
		this.trainingGroupId = trainingGroupId;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<String> getTrainingBrands() {
		return trainingBrands;
	}

	public void setTrainingBrands(List<String> trainingBrands) {
		this.trainingBrands = trainingBrands;
	}

	public List<String> getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(List<String> searchTerms) {
		this.searchTerms = searchTerms;
	}
	
}

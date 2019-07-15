/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.dto.crm.ticket;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"status",
"message",
"cases",
"CaseNumber",
"CaseId"
})
public class GPCaseDetailsDTO {
	
@JsonProperty("status")
private String status;
@JsonProperty("message")
private String message;
@JsonProperty("cases")
private List<GPCaseDTO> cases = null;
@JsonProperty("CaseNumber")
private Object caseNumber;
@JsonProperty("CaseId")
private Object caseId;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("status")
public String getStatus() {
return status;
}

@JsonProperty("status")
public void setStatus(String status) {
this.status = status;
}

@JsonProperty("message")
public String getMessage() {
return message;
}

@JsonProperty("message")
public void setMessage(String message) {
this.message = message;
}

@JsonProperty("cases")
public List<GPCaseDTO> getCases() {
return cases;
}

@JsonProperty("cases")
public void setCases(List<GPCaseDTO> cases) {
this.cases = cases;
}

@JsonProperty("CaseNumber")
public Object getCaseNumber() {
return caseNumber;
}

@JsonProperty("CaseNumber")
public void setCaseNumber(Object caseNumber) {
this.caseNumber = caseNumber;
}

@JsonProperty("CaseId")
public Object getCaseId() {
return caseId;
}

@JsonProperty("CaseId")
public void setCaseId(Object caseId) {
this.caseId = caseId;
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

@Override
public String toString() {
return new ToStringBuilder(this).append("status", status).append("message", message).append("cases", cases).append("caseNumber", caseNumber).append("caseId", caseId).append("additionalProperties", additionalProperties).toString();
}

}

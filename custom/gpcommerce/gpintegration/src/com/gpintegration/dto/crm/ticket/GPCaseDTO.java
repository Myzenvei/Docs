/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.dto.crm.ticket;


	import java.util.HashMap;
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
	"YourSystemNumber",
	"SupportType",
	"StatusComment",
	"Status",
	"StateProvince",
	"Source",
	"ShipToZip",
	"ShipToState",
	"ShipToNumber",
	"ShipToCity",
	"ShipToAddress",
	"ShipmentStart",
	"ShipmentEnd",
	"Salesperson",
	"ResolutionSummary",
	"ResolutionStage",
	"PostalCode",
	"ParentSource",
	"PADisposition",
	"OwnerName",
	"lineItems",
	"KeyFindings",
	"InvoiceNumber",
	"EUDisposition",
	"EndUserStatus",
	"EndUserName",
	"DistributorNumber",
	"DeliveryNumber",
	"DateTimeOpened",
	"DateTimeClosed",
	"CustomerZip",
	"CustomerState",
	"CustomerPO",
	"CustomerName",
	"CustomerComplaintDescription",
	"CustomerCity",
	"CustomerAddress",
	"CreatedDate",
	"Country",
	"ContactName",
	"ComplaintType",
	"ComplaintReason",
	"City",
	"CaseOwner",
	"CaseOrigin",
	"caseNumber",
	"caseId",
	"Carrier",
	"BillofLadingNumber",
	"Address",
	"AccountNumber"
	})
	public class GPCaseDTO {

	@JsonProperty("YourSystemNumber")
	private String yourSystemNumber;
	@JsonProperty("SupportType")
	private String supportType;
	@JsonProperty("StatusComment")
	private String statusComment;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("StateProvince")
	private String stateProvince;
	@JsonProperty("Source")
	private String source;
	@JsonProperty("ShipToZip")
	private String shipToZip;
	@JsonProperty("ShipToState")
	private String shipToState;
	@JsonProperty("ShipToNumber")
	private String shipToNumber;
	@JsonProperty("ShipToCity")
	private String shipToCity;
	@JsonProperty("ShipToAddress")
	private String shipToAddress;
	@JsonProperty("ShipmentStart")
	private String shipmentStart;
	@JsonProperty("ShipmentEnd")
	private String shipmentEnd;
	@JsonProperty("Salesperson")
	private String salesperson;
	@JsonProperty("ResolutionSummary")
	private String resolutionSummary;
	@JsonProperty("ResolutionStage")
	private String resolutionStage;
	@JsonProperty("PostalCode")
	private String postalCode;
	@JsonProperty("ParentSource")
	private String parentSource;
	@JsonProperty("PADisposition")
	private String pADisposition;
	@JsonProperty("OwnerName")
	private String ownerName;
	@JsonProperty("lineItems")
	private String lineItems;
	@JsonProperty("KeyFindings")
	private String keyFindings;
	@JsonProperty("InvoiceNumber")
	private String invoiceNumber;
	@JsonProperty("EUDisposition")
	private String eUDisposition;
	@JsonProperty("EndUserStatus")
	private String endUserStatus;
	@JsonProperty("EndUserName")
	private String endUserName;
	@JsonProperty("DistributorNumber")
	private String distributorNumber;
	@JsonProperty("DeliveryNumber")
	private String deliveryNumber;
	@JsonProperty("DateTimeOpened")
	private String dateTimeOpened;
	@JsonProperty("DateTimeClosed")
	private String dateTimeClosed;
	@JsonProperty("CustomerZip")
	private String customerZip;
	@JsonProperty("CustomerState")
	private String customerState;
	@JsonProperty("CustomerPO")
	private String customerPO;
	@JsonProperty("CustomerName")
	private String customerName;
	@JsonProperty("CustomerComplaintDescription")
	private String customerComplaintDescription;
	@JsonProperty("CustomerCity")
	private String customerCity;
	@JsonProperty("CustomerAddress")
	private String customerAddress;
	@JsonProperty("CreatedDate")
	private String createdDate;
	@JsonProperty("Country")
	private String country;
	@JsonProperty("ContactName")
	private String contactName;
	@JsonProperty("ComplaintType")
	private String complaintType;
	@JsonProperty("ComplaintReason")
	private String complaintReason;
	@JsonProperty("City")
	private String city;
	@JsonProperty("CaseOwner")
	private String caseOwner;
	@JsonProperty("CaseOrigin")
	private String caseOrigin;
	@JsonProperty("caseNumber")
	private String caseNumber;
	@JsonProperty("caseId")
	private String caseId;
	@JsonProperty("Carrier")
	private String carrier;
	@JsonProperty("BillofLadingNumber")
	private String billofLadingNumber;
	@JsonProperty("Address")
	private String address;
	@JsonProperty("AccountNumber")
	private String accountNumber;

	@JsonProperty("YourSystemNumber")
	public String getYourSystemNumber() {
	return yourSystemNumber;
	}

	@JsonProperty("YourSystemNumber")
	public void setYourSystemNumber(String yourSystemNumber) {
	this.yourSystemNumber = yourSystemNumber;
	}

	@JsonProperty("SupportType")
	public String getSupportType() {
	return supportType;
	}

	@JsonProperty("SupportType")
	public void setSupportType(String supportType) {
	this.supportType = supportType;
	}

	@JsonProperty("StatusComment")
	public String getStatusComment() {
	return statusComment;
	}

	@JsonProperty("StatusComment")
	public void setStatusComment(String statusComment) {
	this.statusComment = statusComment;
	}

	@JsonProperty("Status")
	public String getStatus() {
	return status;
	}

	@JsonProperty("Status")
	public void setStatus(String status) {
	this.status = status;
	}

	@JsonProperty("StateProvince")
	public String getStateProvince() {
	return stateProvince;
	}

	@JsonProperty("StateProvince")
	public void setStateProvince(String stateProvince) {
	this.stateProvince = stateProvince;
	}

	@JsonProperty("Source")
	public String getSource() {
	return source;
	}

	@JsonProperty("Source")
	public void setSource(String source) {
	this.source = source;
	}

	@JsonProperty("ShipToZip")
	public String getShipToZip() {
	return shipToZip;
	}

	@JsonProperty("ShipToZip")
	public void setShipToZip(String shipToZip) {
	this.shipToZip = shipToZip;
	}

	@JsonProperty("ShipToState")
	public String getShipToState() {
	return shipToState;
	}

	@JsonProperty("ShipToState")
	public void setShipToState(String shipToState) {
	this.shipToState = shipToState;
	}

	@JsonProperty("ShipToNumber")
	public String getShipToNumber() {
	return shipToNumber;
	}

	@JsonProperty("ShipToNumber")
	public void setShipToNumber(String shipToNumber) {
	this.shipToNumber = shipToNumber;
	}

	@JsonProperty("ShipToCity")
	public String getShipToCity() {
	return shipToCity;
	}

	@JsonProperty("ShipToCity")
	public void setShipToCity(String shipToCity) {
	this.shipToCity = shipToCity;
	}

	@JsonProperty("ShipToAddress")
	public String getShipToAddress() {
	return shipToAddress;
	}

	@JsonProperty("ShipToAddress")
	public void setShipToAddress(String shipToAddress) {
	this.shipToAddress = shipToAddress;
	}

	@JsonProperty("ShipmentStart")
	public String getShipmentStart() {
	return shipmentStart;
	}

	@JsonProperty("ShipmentStart")
	public void setShipmentStart(String shipmentStart) {
	this.shipmentStart = shipmentStart;
	}

	@JsonProperty("ShipmentEnd")
	public String getShipmentEnd() {
	return shipmentEnd;
	}

	@JsonProperty("ShipmentEnd")
	public void setShipmentEnd(String shipmentEnd) {
	this.shipmentEnd = shipmentEnd;
	}

	@JsonProperty("Salesperson")
	public String getSalesperson() {
	return salesperson;
	}

	@JsonProperty("Salesperson")
	public void setSalesperson(String salesperson) {
	this.salesperson = salesperson;
	}

	@JsonProperty("ResolutionSummary")
	public String getResolutionSummary() {
	return resolutionSummary;
	}

	@JsonProperty("ResolutionSummary")
	public void setResolutionSummary(String resolutionSummary) {
	this.resolutionSummary = resolutionSummary;
	}

	@JsonProperty("ResolutionStage")
	public String getResolutionStage() {
	return resolutionStage;
	}

	@JsonProperty("ResolutionStage")
	public void setResolutionStage(String resolutionStage) {
	this.resolutionStage = resolutionStage;
	}

	@JsonProperty("PostalCode")
	public String getPostalCode() {
	return postalCode;
	}

	@JsonProperty("PostalCode")
	public void setPostalCode(String postalCode) {
	this.postalCode = postalCode;
	}

	@JsonProperty("ParentSource")
	public String getParentSource() {
	return parentSource;
	}

	@JsonProperty("ParentSource")
	public void setParentSource(String parentSource) {
	this.parentSource = parentSource;
	}

	@JsonProperty("PADisposition")
	public String getPADisposition() {
	return pADisposition;
	}

	@JsonProperty("PADisposition")
	public void setPADisposition(String pADisposition) {
	this.pADisposition = pADisposition;
	}

	@JsonProperty("OwnerName")
	public String getOwnerName() {
	return ownerName;
	}

	@JsonProperty("OwnerName")
	public void setOwnerName(String ownerName) {
	this.ownerName = ownerName;
	}

	@JsonProperty("lineItems")
	public String getLineItems() {
	return lineItems;
	}

	@JsonProperty("lineItems")
	public void setLineItems(String lineItems) {
	this.lineItems = lineItems;
	}

	@JsonProperty("KeyFindings")
	public String getKeyFindings() {
	return keyFindings;
	}

	@JsonProperty("KeyFindings")
	public void setKeyFindings(String keyFindings) {
	this.keyFindings = keyFindings;
	}

	@JsonProperty("InvoiceNumber")
	public String getInvoiceNumber() {
	return invoiceNumber;
	}

	@JsonProperty("InvoiceNumber")
	public void setInvoiceNumber(String invoiceNumber) {
	this.invoiceNumber = invoiceNumber;
	}

	@JsonProperty("EUDisposition")
	public String getEUDisposition() {
	return eUDisposition;
	}

	@JsonProperty("EUDisposition")
	public void setEUDisposition(String eUDisposition) {
	this.eUDisposition = eUDisposition;
	}

	@JsonProperty("EndUserStatus")
	public String getEndUserStatus() {
	return endUserStatus;
	}

	@JsonProperty("EndUserStatus")
	public void setEndUserStatus(String endUserStatus) {
	this.endUserStatus = endUserStatus;
	}

	@JsonProperty("EndUserName")
	public String getEndUserName() {
	return endUserName;
	}

	@JsonProperty("EndUserName")
	public void setEndUserName(String endUserName) {
	this.endUserName = endUserName;
	}

	@JsonProperty("DistributorNumber")
	public String getDistributorNumber() {
	return distributorNumber;
	}

	@JsonProperty("DistributorNumber")
	public void setDistributorNumber(String distributorNumber) {
	this.distributorNumber = distributorNumber;
	}

	@JsonProperty("DeliveryNumber")
	public String getDeliveryNumber() {
	return deliveryNumber;
	}

	@JsonProperty("DeliveryNumber")
	public void setDeliveryNumber(String deliveryNumber) {
	this.deliveryNumber = deliveryNumber;
	}

	@JsonProperty("DateTimeOpened")
	public String getDateTimeOpened() {
	return dateTimeOpened;
	}

	@JsonProperty("DateTimeOpened")
	public void setDateTimeOpened(String dateTimeOpened) {
	this.dateTimeOpened = dateTimeOpened;
	}

	@JsonProperty("DateTimeClosed")
	public String getDateTimeClosed() {
	return dateTimeClosed;
	}

	@JsonProperty("DateTimeClosed")
	public void setDateTimeClosed(String dateTimeClosed) {
	this.dateTimeClosed = dateTimeClosed;
	}

	@JsonProperty("CustomerZip")
	public String getCustomerZip() {
	return customerZip;
	}

	@JsonProperty("CustomerZip")
	public void setCustomerZip(String customerZip) {
	this.customerZip = customerZip;
	}

	@JsonProperty("CustomerState")
	public String getCustomerState() {
	return customerState;
	}

	@JsonProperty("CustomerState")
	public void setCustomerState(String customerState) {
	this.customerState = customerState;
	}

	@JsonProperty("CustomerPO")
	public String getCustomerPO() {
	return customerPO;
	}

	@JsonProperty("CustomerPO")
	public void setCustomerPO(String customerPO) {
	this.customerPO = customerPO;
	}

	@JsonProperty("CustomerName")
	public String getCustomerName() {
	return customerName;
	}

	@JsonProperty("CustomerName")
	public void setCustomerName(String customerName) {
	this.customerName = customerName;
	}

	@JsonProperty("CustomerComplaintDescription")
	public String getCustomerComplaintDescription() {
	return customerComplaintDescription;
	}

	@JsonProperty("CustomerComplaintDescription")
	public void setCustomerComplaintDescription(String customerComplaintDescription) {
	this.customerComplaintDescription = customerComplaintDescription;
	}

	@JsonProperty("CustomerCity")
	public String getCustomerCity() {
	return customerCity;
	}

	@JsonProperty("CustomerCity")
	public void setCustomerCity(String customerCity) {
	this.customerCity = customerCity;
	}

	@JsonProperty("CustomerAddress")
	public String getCustomerAddress() {
	return customerAddress;
	}

	@JsonProperty("CustomerAddress")
	public void setCustomerAddress(String customerAddress) {
	this.customerAddress = customerAddress;
	}

	@JsonProperty("CreatedDate")
	public String getCreatedDate() {
	return createdDate;
	}

	@JsonProperty("CreatedDate")
	public void setCreatedDate(String createdDate) {
	this.createdDate = createdDate;
	}

	@JsonProperty("Country")
	public String getCountry() {
	return country;
	}

	@JsonProperty("Country")
	public void setCountry(String country) {
	this.country = country;
	}

	@JsonProperty("ContactName")
	public String getContactName() {
	return contactName;
	}

	@JsonProperty("ContactName")
	public void setContactName(String contactName) {
	this.contactName = contactName;
	}

	@JsonProperty("ComplaintType")
	public String getComplaintType() {
	return complaintType;
	}

	@JsonProperty("ComplaintType")
	public void setComplaintType(String complaintType) {
	this.complaintType = complaintType;
	}

	@JsonProperty("ComplaintReason")
	public String getComplaintReason() {
	return complaintReason;
	}

	@JsonProperty("ComplaintReason")
	public void setComplaintReason(String complaintReason) {
	this.complaintReason = complaintReason;
	}

	@JsonProperty("City")
	public String getCity() {
	return city;
	}

	@JsonProperty("City")
	public void setCity(String city) {
	this.city = city;
	}

	@JsonProperty("CaseOwner")
	public String getCaseOwner() {
	return caseOwner;
	}

	@JsonProperty("CaseOwner")
	public void setCaseOwner(String caseOwner) {
	this.caseOwner = caseOwner;
	}

	@JsonProperty("CaseOrigin")
	public String getCaseOrigin() {
	return caseOrigin;
	}

	@JsonProperty("CaseOrigin")
	public void setCaseOrigin(String caseOrigin) {
	this.caseOrigin = caseOrigin;
	}

	@JsonProperty("caseNumber")
	public String getCaseNumber() {
	return caseNumber;
	}

	@JsonProperty("caseNumber")
	public void setCaseNumber(String caseNumber) {
	this.caseNumber = caseNumber;
	}

	@JsonProperty("caseId")
	public String getCaseId() {
	return caseId;
	}

	@JsonProperty("caseId")
	public void setCaseId(String caseId) {
	this.caseId = caseId;
	}

	@JsonProperty("Carrier")
	public String getCarrier() {
	return carrier;
	}

	@JsonProperty("Carrier")
	public void setCarrier(String carrier) {
	this.carrier = carrier;
	}

	@JsonProperty("BillofLadingNumber")
	public String getBillofLadingNumber() {
	return billofLadingNumber;
	}

	@JsonProperty("BillofLadingNumber")
	public void setBillofLadingNumber(String billofLadingNumber) {
	this.billofLadingNumber = billofLadingNumber;
	}

	@JsonProperty("Address")
	public String getAddress() {
	return address;
	}

	@JsonProperty("Address")
	public void setAddress(String address) {
	this.address = address;
	}

	@JsonProperty("AccountNumber")
	public String getAccountNumber() {
	return accountNumber;
	}

	@JsonProperty("AccountNumber")
	public void setAccountNumber(String accountNumber) {
	this.accountNumber = accountNumber;
	}

	@Override
	public String toString() {
	return new ToStringBuilder(this).append("yourSystemNumber", yourSystemNumber).append("supportType", supportType).append("statusComment", statusComment).append("status", status).append("stateProvince", stateProvince).append("source", source).append("shipToZip", shipToZip).append("shipToState", shipToState).append("shipToNumber", shipToNumber).append("shipToCity", shipToCity).append("shipToAddress", shipToAddress).append("shipmentStart", shipmentStart).append("shipmentEnd", shipmentEnd).append("salesperson", salesperson).append("resolutionSummary", resolutionSummary).append("resolutionStage", resolutionStage).append("postalCode", postalCode).append("parentSource", parentSource).append("pADisposition", pADisposition).append("ownerName", ownerName).append("lineItems", lineItems).append("keyFindings", keyFindings).append("invoiceNumber", invoiceNumber).append("eUDisposition", eUDisposition).append("endUserStatus", endUserStatus).append("endUserName", endUserName).append("distributorNumber", distributorNumber).append("deliveryNumber", deliveryNumber).append("dateTimeOpened", dateTimeOpened).append("dateTimeClosed", dateTimeClosed).append("customerZip", customerZip).append("customerState", customerState).append("customerPO", customerPO).append("customerName", customerName).append("customerComplaintDescription", customerComplaintDescription).append("customerCity", customerCity).append("customerAddress", customerAddress).append("createdDate", createdDate).append("country", country).append("contactName", contactName).append("complaintType", complaintType).append("complaintReason", complaintReason).append("city", city).append("caseOwner", caseOwner).append("caseOrigin", caseOrigin).append("caseNumber", caseNumber).append("caseId", caseId).append("carrier", carrier).append("billofLadingNumber", billofLadingNumber).append("address", address).append("accountNumber", accountNumber).toString();
	}

}

/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.dto.onesource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OutdataInvoiceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutdataInvoiceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="REQUEST_STATUS" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OutdataRequestStatusType" minOccurs="0"/>
 *         &lt;element name="BASIS_PERCENT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}BasisPercentType" minOccurs="0"/>
 *         &lt;element name="CALCULATION_DIRECTION" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CalculationDirectionType" minOccurs="0"/>
 *         &lt;element name="CALLING_SYSTEM_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CallingSystemNumberType" minOccurs="0"/>
 *         &lt;element name="COMPANY_ID" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CompanyIdType" minOccurs="0"/>
 *         &lt;element name="COMPANY_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CompanyNameType" minOccurs="0"/>
 *         &lt;element name="COMPANY_ROLE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CompanyRoleType"/>
 *         &lt;element name="CURRENCY_CODE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CurrencyCodeType"/>
 *         &lt;element name="CURRENCY_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CurrencyNameType"/>
 *         &lt;element name="MIN_ACCOUNTABLE_UNIT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}NillableDecimalType"/>
 *         &lt;element name="ROUNDING_PRECISION" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RoundingPrecisionType"/>
 *         &lt;element name="ROUNDING_RULE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RoundingRuleType"/>
 *         &lt;element name="CUSTOMER_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CustomerNameType" minOccurs="0"/>
 *         &lt;element name="CUSTOMER_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CustomerNumberType" minOccurs="0"/>
 *         &lt;element name="END_USER_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}EndUserNameType" minOccurs="0"/>
 *         &lt;element name="EXTERNAL_COMPANY_ID" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ExternalCompanyIdType" minOccurs="0"/>
 *         &lt;element name="FISCAL_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}FiscalDateType" minOccurs="0"/>
 *         &lt;element name="FUNCTIONAL_CURRENCY_CODE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}FunctionalCurrencyCodeType" minOccurs="0"/>
 *         &lt;element name="HOST_SYSTEM" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}HostSystemType" minOccurs="0"/>
 *         &lt;element name="INDATA" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IndataType" minOccurs="0"/>
 *         &lt;element name="INVOICE_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}InvoiceDateType"/>
 *         &lt;element name="INVOICE_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}InvoiceNumberType" minOccurs="0"/>
 *         &lt;element name="IS_AUDIT_UPDATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IsAuditUpdateType" minOccurs="0"/>
 *         &lt;element name="IS_BUSINESS_SUPPLY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IsBusinessSupplyType" minOccurs="0"/>
 *         &lt;element name="IS_CREDIT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IsCreditType" minOccurs="0"/>
 *         &lt;element name="IS_REPORTED" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IsReportedType" minOccurs="0"/>
 *         &lt;element name="IS_REVERSED" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IsReversedType" minOccurs="0"/>
 *         &lt;element name="MESSAGE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}MessageType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="NATURE_OF_TRANSACTION_CODE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}NatureOfTransactionCodeType" minOccurs="0"/>
 *         &lt;element name="ORIGINAL_INVOICE_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OriginalInvoiceNumberType" minOccurs="0"/>
 *         &lt;element name="STATISTICAL_PROCEDURE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}StatisticalProcedureType" minOccurs="0"/>
 *         &lt;element name="TOTAL_TAX_AMOUNT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TotalTaxAmountType"/>
 *         &lt;element name="TRANSACTION_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TransactionDateType" minOccurs="0"/>
 *         &lt;element name="UNIQUE_INVOICE_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}UniqueInvoiceNumberType" minOccurs="0"/>
 *         &lt;element name="VENDOR_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}VendorNameType" minOccurs="0"/>
 *         &lt;element name="VENDOR_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}VendorNumberType" minOccurs="0"/>
 *         &lt;element name="VENDOR_TAX" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}VendorTaxType" minOccurs="0"/>
 *         &lt;element name="USER_ELEMENT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}UserElementType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="LINE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OutdataLineType" maxOccurs="unbounded"/>
 *         &lt;element name="ORIGINAL_DOCUMENT_ID" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OriginalDocumentIdType" minOccurs="0"/>
 *         &lt;element name="ORIGINAL_DOCUMENT_ITEM" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OriginalDocumentItemType" minOccurs="0"/>
 *         &lt;element name="ORIGINAL_DOCUMENT_TYPE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OriginalDocumentTypeType" minOccurs="0"/>
 *         &lt;element name="ORIGINAL_INVOICE_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OriginalInvoiceDateType" minOccurs="0"/>
 *         &lt;element name="ORIGINAL_MOVEMENT_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OriginalMovementDateType" minOccurs="0"/>
 *         &lt;element name="CUSTOMER_GROUP_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CustomerGroupNameType" minOccurs="0"/>
 *         &lt;element name="CUSTOMER_GROUP_OWNER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CustomerGroupOwnerType" minOccurs="0"/>
 *         &lt;element name="TAX_SUMMARY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OutdataTaxSummaryType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutdataInvoiceType", propOrder = {
    "requeststatus",
    "basispercent",
    "calculationdirection",
    "callingsystemnumber",
    "companyid",
    "companyname",
    "companyrole",
    "currencycode",
    "currencyname",
    "minaccountableunit",
    "roundingprecision",
    "roundingrule",
    "customername",
    "customernumber",
    "endusername",
    "externalcompanyid",
    "fiscaldate",
    "functionalcurrencycode",
    "hostsystem",
    "indata",
    "invoicedate",
    "invoicenumber",
    "isauditupdate",
    "isbusinesssupply",
    "iscredit",
    "isreported",
    "isreversed",
    "message",
    "natureoftransactioncode",
    "originalinvoicenumber",
    "statisticalprocedure",
    "totaltaxamount",
    "transactiondate",
    "uniqueinvoicenumber",
    "vendorname",
    "vendornumber",
    "vendortax",
    "userelement",
    "line",
    "originaldocumentid",
    "originaldocumentitem",
    "originaldocumenttype",
    "originalinvoicedate",
    "originalmovementdate",
    "customergroupname",
    "customergroupowner",
    "taxsummary"
})
public class OutdataInvoiceType {

    @XmlElement(name = "REQUEST_STATUS")
    protected OutdataRequestStatusType requeststatus;
    @XmlElement(name = "BASIS_PERCENT")
    protected String basispercent;
    @XmlElement(name = "CALCULATION_DIRECTION")
    protected String calculationdirection;
    @XmlElement(name = "CALLING_SYSTEM_NUMBER")
    protected String callingsystemnumber;
    @XmlElement(name = "COMPANY_ID")
    protected Long companyid;
    @XmlElement(name = "COMPANY_NAME")
    protected String companyname;
    @XmlElement(name = "COMPANY_ROLE", required = true)
    protected String companyrole;
    @XmlElement(name = "CURRENCY_CODE", required = true)
    protected String currencycode;
    @XmlElement(name = "CURRENCY_NAME", required = true)
    protected String currencyname;
    @XmlElement(name = "MIN_ACCOUNTABLE_UNIT", required = true)
    protected String minaccountableunit;
    @XmlElement(name = "ROUNDING_PRECISION", required = true)
    protected BigDecimal roundingprecision;
    @XmlElement(name = "ROUNDING_RULE", required = true)
    protected String roundingrule;
    @XmlElement(name = "CUSTOMER_NAME")
    protected String customername;
    @XmlElement(name = "CUSTOMER_NUMBER")
    protected String customernumber;
    @XmlElement(name = "END_USER_NAME")
    protected String endusername;
    @XmlElement(name = "EXTERNAL_COMPANY_ID")
    protected String externalcompanyid;
    @XmlElement(name = "FISCAL_DATE")
    protected String fiscaldate;
    @XmlElement(name = "FUNCTIONAL_CURRENCY_CODE")
    protected String functionalcurrencycode;
    @XmlElement(name = "HOST_SYSTEM")
    protected String hostsystem;
    @XmlElement(name = "INDATA")
    protected IndataType indata;
    @XmlElement(name = "INVOICE_DATE", required = true)
    protected String invoicedate;
    @XmlElement(name = "INVOICE_NUMBER")
    protected String invoicenumber;
    @XmlElement(name = "IS_AUDIT_UPDATE")
    protected String isauditupdate;
    @XmlElement(name = "IS_BUSINESS_SUPPLY")
    protected String isbusinesssupply;
    @XmlElement(name = "IS_CREDIT")
    protected String iscredit;
    @XmlElement(name = "IS_REPORTED")
    protected String isreported;
    @XmlElement(name = "IS_REVERSED")
    protected String isreversed;
    @XmlElement(name = "MESSAGE")
    protected List<MessageType> message;
    @XmlElement(name = "NATURE_OF_TRANSACTION_CODE")
    protected String natureoftransactioncode;
    @XmlElement(name = "ORIGINAL_INVOICE_NUMBER")
    protected String originalinvoicenumber;
    @XmlElement(name = "STATISTICAL_PROCEDURE")
    protected String statisticalprocedure;
    @XmlElement(name = "TOTAL_TAX_AMOUNT", required = true)
    protected String totaltaxamount;
    @XmlElement(name = "TRANSACTION_DATE")
    protected String transactiondate;
    @XmlElement(name = "UNIQUE_INVOICE_NUMBER")
    protected String uniqueinvoicenumber;
    @XmlElement(name = "VENDOR_NAME")
    protected String vendorname;
    @XmlElement(name = "VENDOR_NUMBER")
    protected String vendornumber;
    @XmlElement(name = "VENDOR_TAX")
    protected String vendortax;
    @XmlElement(name = "USER_ELEMENT")
    protected List<UserElementType> userelement;
    @XmlElement(name = "LINE", required = true)
    protected List<OutdataLineType> line;
    @XmlElement(name = "ORIGINAL_DOCUMENT_ID")
    protected String originaldocumentid;
    @XmlElement(name = "ORIGINAL_DOCUMENT_ITEM")
    protected String originaldocumentitem;
    @XmlElement(name = "ORIGINAL_DOCUMENT_TYPE")
    protected String originaldocumenttype;
    @XmlElement(name = "ORIGINAL_INVOICE_DATE")
    protected String originalinvoicedate;
    @XmlElement(name = "ORIGINAL_MOVEMENT_DATE")
    protected String originalmovementdate;
    @XmlElement(name = "CUSTOMER_GROUP_NAME")
    protected String customergroupname;
    @XmlElement(name = "CUSTOMER_GROUP_OWNER")
    protected String customergroupowner;
    @XmlElement(name = "TAX_SUMMARY")
    protected OutdataTaxSummaryType taxsummary;

    /**
     * Gets the value of the requeststatus property.
     * 
     * @return
     *     possible object is
     *     {@link OutdataRequestStatusType }
     *     
     */
    public OutdataRequestStatusType getREQUESTSTATUS() {
        return requeststatus;
    }

    /**
     * Sets the value of the requeststatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link OutdataRequestStatusType }
     *     
     */
    public void setREQUESTSTATUS(OutdataRequestStatusType value) {
        this.requeststatus = value;
    }

    /**
     * Gets the value of the basispercent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBASISPERCENT() {
        return basispercent;
    }

    /**
     * Sets the value of the basispercent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBASISPERCENT(String value) {
        this.basispercent = value;
    }

    /**
     * Gets the value of the calculationdirection property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCALCULATIONDIRECTION() {
        return calculationdirection;
    }

    /**
     * Sets the value of the calculationdirection property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCALCULATIONDIRECTION(String value) {
        this.calculationdirection = value;
    }

    /**
     * Gets the value of the callingsystemnumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCALLINGSYSTEMNUMBER() {
        return callingsystemnumber;
    }

    /**
     * Sets the value of the callingsystemnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCALLINGSYSTEMNUMBER(String value) {
        this.callingsystemnumber = value;
    }

    /**
     * Gets the value of the companyid property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCOMPANYID() {
        return companyid;
    }

    /**
     * Sets the value of the companyid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCOMPANYID(Long value) {
        this.companyid = value;
    }

    /**
     * Gets the value of the companyname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPANYNAME() {
        return companyname;
    }

    /**
     * Sets the value of the companyname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPANYNAME(String value) {
        this.companyname = value;
    }

    /**
     * Gets the value of the companyrole property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPANYROLE() {
        return companyrole;
    }

    /**
     * Sets the value of the companyrole property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPANYROLE(String value) {
        this.companyrole = value;
    }

    /**
     * Gets the value of the currencycode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCURRENCYCODE() {
        return currencycode;
    }

    /**
     * Sets the value of the currencycode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCURRENCYCODE(String value) {
        this.currencycode = value;
    }

    /**
     * Gets the value of the currencyname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCURRENCYNAME() {
        return currencyname;
    }

    /**
     * Sets the value of the currencyname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCURRENCYNAME(String value) {
        this.currencyname = value;
    }

    /**
     * Gets the value of the minaccountableunit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMINACCOUNTABLEUNIT() {
        return minaccountableunit;
    }

    /**
     * Sets the value of the minaccountableunit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMINACCOUNTABLEUNIT(String value) {
        this.minaccountableunit = value;
    }

    /**
     * Gets the value of the roundingprecision property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getROUNDINGPRECISION() {
        return roundingprecision;
    }

    /**
     * Sets the value of the roundingprecision property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setROUNDINGPRECISION(BigDecimal value) {
        this.roundingprecision = value;
    }

    /**
     * Gets the value of the roundingrule property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getROUNDINGRULE() {
        return roundingrule;
    }

    /**
     * Sets the value of the roundingrule property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setROUNDINGRULE(String value) {
        this.roundingrule = value;
    }

    /**
     * Gets the value of the customername property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUSTOMERNAME() {
        return customername;
    }

    /**
     * Sets the value of the customername property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUSTOMERNAME(String value) {
        this.customername = value;
    }

    /**
     * Gets the value of the customernumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUSTOMERNUMBER() {
        return customernumber;
    }

    /**
     * Sets the value of the customernumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUSTOMERNUMBER(String value) {
        this.customernumber = value;
    }

    /**
     * Gets the value of the endusername property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getENDUSERNAME() {
        return endusername;
    }

    /**
     * Sets the value of the endusername property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setENDUSERNAME(String value) {
        this.endusername = value;
    }

    /**
     * Gets the value of the externalcompanyid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEXTERNALCOMPANYID() {
        return externalcompanyid;
    }

    /**
     * Sets the value of the externalcompanyid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEXTERNALCOMPANYID(String value) {
        this.externalcompanyid = value;
    }

    /**
     * Gets the value of the fiscaldate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFISCALDATE() {
        return fiscaldate;
    }

    /**
     * Sets the value of the fiscaldate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFISCALDATE(String value) {
        this.fiscaldate = value;
    }

    /**
     * Gets the value of the functionalcurrencycode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFUNCTIONALCURRENCYCODE() {
        return functionalcurrencycode;
    }

    /**
     * Sets the value of the functionalcurrencycode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFUNCTIONALCURRENCYCODE(String value) {
        this.functionalcurrencycode = value;
    }

    /**
     * Gets the value of the hostsystem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHOSTSYSTEM() {
        return hostsystem;
    }

    /**
     * Sets the value of the hostsystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHOSTSYSTEM(String value) {
        this.hostsystem = value;
    }

    /**
     * Gets the value of the indata property.
     * 
     * @return
     *     possible object is
     *     {@link IndataType }
     *     
     */
    public IndataType getINDATA() {
        return indata;
    }

    /**
     * Sets the value of the indata property.
     * 
     * @param value
     *     allowed object is
     *     {@link IndataType }
     *     
     */
    public void setINDATA(IndataType value) {
        this.indata = value;
    }

    /**
     * Gets the value of the invoicedate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINVOICEDATE() {
        return invoicedate;
    }

    /**
     * Sets the value of the invoicedate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINVOICEDATE(String value) {
        this.invoicedate = value;
    }

    /**
     * Gets the value of the invoicenumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINVOICENUMBER() {
        return invoicenumber;
    }

    /**
     * Sets the value of the invoicenumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINVOICENUMBER(String value) {
        this.invoicenumber = value;
    }

    /**
     * Gets the value of the isauditupdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISAUDITUPDATE() {
        return isauditupdate;
    }

    /**
     * Sets the value of the isauditupdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISAUDITUPDATE(String value) {
        this.isauditupdate = value;
    }

    /**
     * Gets the value of the isbusinesssupply property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISBUSINESSSUPPLY() {
        return isbusinesssupply;
    }

    /**
     * Sets the value of the isbusinesssupply property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISBUSINESSSUPPLY(String value) {
        this.isbusinesssupply = value;
    }

    /**
     * Gets the value of the iscredit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISCREDIT() {
        return iscredit;
    }

    /**
     * Sets the value of the iscredit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISCREDIT(String value) {
        this.iscredit = value;
    }

    /**
     * Gets the value of the isreported property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISREPORTED() {
        return isreported;
    }

    /**
     * Sets the value of the isreported property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISREPORTED(String value) {
        this.isreported = value;
    }

    /**
     * Gets the value of the isreversed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISREVERSED() {
        return isreversed;
    }

    /**
     * Sets the value of the isreversed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISREVERSED(String value) {
        this.isreversed = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the message property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMESSAGE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MessageType }
     * 
     * 
     */
    public List<MessageType> getMESSAGE() {
        if (message == null) {
            message = new ArrayList<MessageType>();
        }
        return this.message;
    }

    /**
     * Gets the value of the natureoftransactioncode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNATUREOFTRANSACTIONCODE() {
        return natureoftransactioncode;
    }

    /**
     * Sets the value of the natureoftransactioncode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNATUREOFTRANSACTIONCODE(String value) {
        this.natureoftransactioncode = value;
    }

    /**
     * Gets the value of the originalinvoicenumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORIGINALINVOICENUMBER() {
        return originalinvoicenumber;
    }

    /**
     * Sets the value of the originalinvoicenumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORIGINALINVOICENUMBER(String value) {
        this.originalinvoicenumber = value;
    }

    /**
     * Gets the value of the statisticalprocedure property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTATISTICALPROCEDURE() {
        return statisticalprocedure;
    }

    /**
     * Sets the value of the statisticalprocedure property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTATISTICALPROCEDURE(String value) {
        this.statisticalprocedure = value;
    }

    /**
     * Gets the value of the totaltaxamount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTOTALTAXAMOUNT() {
        return totaltaxamount;
    }

    /**
     * Sets the value of the totaltaxamount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTOTALTAXAMOUNT(String value) {
        this.totaltaxamount = value;
    }

    /**
     * Gets the value of the transactiondate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRANSACTIONDATE() {
        return transactiondate;
    }

    /**
     * Sets the value of the transactiondate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRANSACTIONDATE(String value) {
        this.transactiondate = value;
    }

    /**
     * Gets the value of the uniqueinvoicenumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUNIQUEINVOICENUMBER() {
        return uniqueinvoicenumber;
    }

    /**
     * Sets the value of the uniqueinvoicenumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUNIQUEINVOICENUMBER(String value) {
        this.uniqueinvoicenumber = value;
    }

    /**
     * Gets the value of the vendorname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVENDORNAME() {
        return vendorname;
    }

    /**
     * Sets the value of the vendorname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVENDORNAME(String value) {
        this.vendorname = value;
    }

    /**
     * Gets the value of the vendornumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVENDORNUMBER() {
        return vendornumber;
    }

    /**
     * Sets the value of the vendornumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVENDORNUMBER(String value) {
        this.vendornumber = value;
    }

    /**
     * Gets the value of the vendortax property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVENDORTAX() {
        return vendortax;
    }

    /**
     * Sets the value of the vendortax property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVENDORTAX(String value) {
        this.vendortax = value;
    }

    /**
     * Gets the value of the userelement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the userelement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUSERELEMENT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UserElementType }
     * 
     * 
     */
    public List<UserElementType> getUSERELEMENT() {
        if (userelement == null) {
            userelement = new ArrayList<UserElementType>();
        }
        return this.userelement;
    }

    /**
     * Gets the value of the line property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the line property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLINE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OutdataLineType }
     * 
     * 
     */
    public List<OutdataLineType> getLINE() {
        if (line == null) {
            line = new ArrayList<OutdataLineType>();
        }
        return this.line;
    }

    /**
     * Gets the value of the originaldocumentid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORIGINALDOCUMENTID() {
        return originaldocumentid;
    }

    /**
     * Sets the value of the originaldocumentid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORIGINALDOCUMENTID(String value) {
        this.originaldocumentid = value;
    }

    /**
     * Gets the value of the originaldocumentitem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORIGINALDOCUMENTITEM() {
        return originaldocumentitem;
    }

    /**
     * Sets the value of the originaldocumentitem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORIGINALDOCUMENTITEM(String value) {
        this.originaldocumentitem = value;
    }

    /**
     * Gets the value of the originaldocumenttype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORIGINALDOCUMENTTYPE() {
        return originaldocumenttype;
    }

    /**
     * Sets the value of the originaldocumenttype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORIGINALDOCUMENTTYPE(String value) {
        this.originaldocumenttype = value;
    }

    /**
     * Gets the value of the originalinvoicedate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORIGINALINVOICEDATE() {
        return originalinvoicedate;
    }

    /**
     * Sets the value of the originalinvoicedate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORIGINALINVOICEDATE(String value) {
        this.originalinvoicedate = value;
    }

    /**
     * Gets the value of the originalmovementdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORIGINALMOVEMENTDATE() {
        return originalmovementdate;
    }

    /**
     * Sets the value of the originalmovementdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORIGINALMOVEMENTDATE(String value) {
        this.originalmovementdate = value;
    }

    /**
     * Gets the value of the customergroupname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUSTOMERGROUPNAME() {
        return customergroupname;
    }

    /**
     * Sets the value of the customergroupname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUSTOMERGROUPNAME(String value) {
        this.customergroupname = value;
    }

    /**
     * Gets the value of the customergroupowner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUSTOMERGROUPOWNER() {
        return customergroupowner;
    }

    /**
     * Sets the value of the customergroupowner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUSTOMERGROUPOWNER(String value) {
        this.customergroupowner = value;
    }

    /**
     * Gets the value of the taxsummary property.
     * 
     * @return
     *     possible object is
     *     {@link OutdataTaxSummaryType }
     *     
     */
    public OutdataTaxSummaryType getTAXSUMMARY() {
        return taxsummary;
    }

    /**
     * Sets the value of the taxsummary property.
     * 
     * @param value
     *     allowed object is
     *     {@link OutdataTaxSummaryType }
     *     
     */
    public void setTAXSUMMARY(OutdataTaxSummaryType value) {
        this.taxsummary = value;
    }

}

/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.dto.onesource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.gp.commerce.core.dto.onesource.MessageType;
import com.gp.commerce.core.dto.onesource.OutdataTaxSummaryType;
import com.gp.commerce.core.dto.onesource.OutdataTaxType;
import com.gp.commerce.core.dto.onesource.QuantitiesType;
import com.gp.commerce.core.dto.onesource.UserElementType;


/**
 * <p>Java class for OutdataLineType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutdataLineType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ALLOCATION_LINE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OutdataLineType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ACCOUNTING_CODE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AccountingCodeType" minOccurs="0"/>
 *         &lt;element name="ALLOCATION_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AllocationNameType" minOccurs="0"/>
 *         &lt;element name="BASIS_PERCENT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}BasisPercentType" minOccurs="0"/>
 *         &lt;element name="BILL_TO_BRANCH_ID" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}BranchIdType" minOccurs="0"/>
 *         &lt;element name="COMMODITY_CODE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CommodityCodeType" minOccurs="0"/>
 *         &lt;element name="COUNTRY_OF_ORIGIN" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CountryOfOriginType" minOccurs="0"/>
 *         &lt;element name="CUSTOMER_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CustomerNameType" minOccurs="0"/>
 *         &lt;element name="CUSTOMER_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CustomerNumberType" minOccurs="0"/>
 *         &lt;element name="DEPT_OF_CONSIGN" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}DeptOfConsignType" minOccurs="0"/>
 *         &lt;element name="DESCRIPTION" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}DescriptionType" minOccurs="0"/>
 *         &lt;element name="DISCOUNT_AMOUNT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}DiscountAmountType" minOccurs="0"/>
 *         &lt;element name="END_USER_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}EndUserNameType" minOccurs="0"/>
 *         &lt;element name="DELIVERY_TERMS" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}DeliveryTermsType" minOccurs="0"/>
 *         &lt;element name="FREIGHT_ON_BOARD" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}FreightOnBoardType" minOccurs="0"/>
 *         &lt;element name="GROSS_AMOUNT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}GrossAmountType" minOccurs="0"/>
 *         &lt;element name="INPUT_RECOVERY_AMOUNT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}InputRecoveryAmountType" minOccurs="0"/>
 *         &lt;element name="INPUT_RECOVERY_PERCENT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}InputRecoveryPercentType" minOccurs="0"/>
 *         &lt;element name="MIDDLEMAN_MARKUP_AMOUNT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}MiddlemanMarkupAmountType" minOccurs="0"/>
 *         &lt;element name="MIDDLEMAN_MARKUP_RATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}MiddlemanMarkupRateType" minOccurs="0"/>
 *         &lt;element name="IS_BUSINESS_SUPPLY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IsBusinessSupplyType" minOccurs="0"/>
 *         &lt;element name="ITEM_VALUE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ItemValueType" minOccurs="0"/>
 *         &lt;element name="LINE_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}LineNumberType"/>
 *         &lt;element name="RELATED_LINE_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RelatedLineNumberType" minOccurs="0"/>
 *         &lt;element name="MASS" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}MassType" minOccurs="0"/>
 *         &lt;element name="MESSAGE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}MessageType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="MIDDLEMAN_BRANCH_ID" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}BranchIdType" minOccurs="0"/>
 *         &lt;element name="MODE_OF_TRANSPORT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ModeOfTransportType" minOccurs="0"/>
 *         &lt;element name="PART_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}PartNumberType" minOccurs="0"/>
 *         &lt;element name="POINT_OF_TITLE_TRANSFER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}PointOfTitleTransferType"/>
 *         &lt;element name="PORT_OF_ENTRY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}PortOfEntryType" minOccurs="0"/>
 *         &lt;element name="PORT_OF_LOADING" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}PortOfLoadingType" minOccurs="0"/>
 *         &lt;element name="REGIME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegimeType" minOccurs="0"/>
 *         &lt;element name="SHIP_FROM_BRANCH_ID" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}BranchIdType" minOccurs="0"/>
 *         &lt;element name="SHIP_FROM_COUNTRY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ShipFromCountryType" minOccurs="0"/>
 *         &lt;element name="SHIP_TO_BRANCH_ID" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}BranchIdType" minOccurs="0"/>
 *         &lt;element name="SHIP_TO_COUNTRY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ShipToCountryType" minOccurs="0"/>
 *         &lt;element name="SUPPLEMENTARY_UNIT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}SupplementaryUnitType" minOccurs="0"/>
 *         &lt;element name="SUPPLY_BRANCH_ID" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}BranchIdType" minOccurs="0"/>
 *         &lt;element name="UNIQUE_LINE_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}UniqueLineNumberType" minOccurs="0"/>
 *         &lt;element name="VENDOR_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}VendorNameType" minOccurs="0"/>
 *         &lt;element name="VENDOR_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}VendorNumberType" minOccurs="0"/>
 *         &lt;element name="VENDOR_TAX" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}VendorTaxType" minOccurs="0"/>
 *         &lt;element name="TOTAL_TAX_AMOUNT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TotalTaxAmountType"/>
 *         &lt;element name="TAX_CODE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxCodeType" minOccurs="0"/>
 *         &lt;element name="TAX" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OutdataTaxType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="TRANSACTION_TYPE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TransactionType" minOccurs="0"/>
 *         &lt;element name="UNIT_OF_MEASURE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}UnitOfMeasureType" minOccurs="0"/>
 *         &lt;element name="USER_ELEMENT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}UserElementType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="QUANTITIES" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}QuantitiesType" minOccurs="0"/>
 *         &lt;element name="IS_CREDIT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IsCreditType" minOccurs="0"/>
 *         &lt;element name="INVOICE_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}InvoiceDateType" minOccurs="0"/>
 *         &lt;element name="MOVEMENT_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}MovementDateType" minOccurs="0"/>
 *         &lt;element name="ORIGINAL_DOCUMENT_ID" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OriginalDocumentIdType" minOccurs="0"/>
 *         &lt;element name="ORIGINAL_DOCUMENT_ITEM" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OriginalDocumentItemType" minOccurs="0"/>
 *         &lt;element name="ORIGINAL_DOCUMENT_TYPE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OriginalDocumentTypeType" minOccurs="0"/>
 *         &lt;element name="ORIGINAL_INVOICE_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OriginalInvoiceDateType" minOccurs="0"/>
 *         &lt;element name="ORIGINAL_INVOICE_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OriginalInvoiceNumberType" minOccurs="0"/>
 *         &lt;element name="ORIGINAL_MOVEMENT_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OriginalMovementDateType" minOccurs="0"/>
 *         &lt;element name="CUSTOMER_GROUP_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CustomerGroupNameType" minOccurs="0"/>
 *         &lt;element name="CUSTOMER_GROUP_OWNER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CustomerGroupOwnerType" minOccurs="0"/>
 *         &lt;element name="TITLE_TRANSFER_LOCATION" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TitleTransferLocationType" minOccurs="0"/>
 *         &lt;element name="TAX_SUMMARY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OutdataTaxSummaryType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ID" use="required" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}LineIdType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutdataLineType", propOrder = {
    "allocationline",
    "accountingcode",
    "allocationname",
    "basispercent",
    "billtobranchid",
    "commoditycode",
    "countryoforigin",
    "customername",
    "customernumber",
    "deptofconsign",
    "description",
    "discountamount",
    "endusername",
    "deliveryterms",
    "freightonboard",
    "grossamount",
    "inputrecoveryamount",
    "inputrecoverypercent",
    "middlemanmarkupamount",
    "middlemanmarkuprate",
    "isbusinesssupply",
    "itemvalue",
    "linenumber",
    "relatedlinenumber",
    "mass",
    "message",
    "middlemanbranchid",
    "modeoftransport",
    "partnumber",
    "pointoftitletransfer",
    "portofentry",
    "portofloading",
    "regime",
    "shipfrombranchid",
    "shipfromcountry",
    "shiptobranchid",
    "shiptocountry",
    "supplementaryunit",
    "supplybranchid",
    "uniquelinenumber",
    "vendorname",
    "vendornumber",
    "vendortax",
    "totaltaxamount",
    "taxcode",
    "tax",
    "transactiontype",
    "unitofmeasure",
    "userelement",
    "quantities",
    "iscredit",
    "invoicedate",
    "movementdate",
    "originaldocumentid",
    "originaldocumentitem",
    "originaldocumenttype",
    "originalinvoicedate",
    "originalinvoicenumber",
    "originalmovementdate",
    "customergroupname",
    "customergroupowner",
    "titletransferlocation",
    "taxsummary"
})
public class OutdataLineType {

    @XmlElement(name = "ALLOCATION_LINE")
    protected List<OutdataLineType> allocationline;
    @XmlElement(name = "ACCOUNTING_CODE")
    protected String accountingcode;
    @XmlElement(name = "ALLOCATION_NAME")
    protected String allocationname;
    @XmlElement(name = "BASIS_PERCENT")
    protected String basispercent;
    @XmlElement(name = "BILL_TO_BRANCH_ID")
    protected String billtobranchid;
    @XmlElement(name = "COMMODITY_CODE")
    protected String commoditycode;
    @XmlElement(name = "COUNTRY_OF_ORIGIN")
    protected String countryoforigin;
    @XmlElement(name = "CUSTOMER_NAME")
    protected String customername;
    @XmlElement(name = "CUSTOMER_NUMBER")
    protected String customernumber;
    @XmlElement(name = "DEPT_OF_CONSIGN")
    protected String deptofconsign;
    @XmlElement(name = "DESCRIPTION")
    protected String description;
    @XmlElement(name = "DISCOUNT_AMOUNT")
    protected String discountamount;
    @XmlElement(name = "END_USER_NAME")
    protected String endusername;
    @XmlElement(name = "DELIVERY_TERMS")
    protected String deliveryterms;
    @XmlElement(name = "FREIGHT_ON_BOARD")
    protected String freightonboard;
    @XmlElement(name = "GROSS_AMOUNT")
    protected String grossamount;
    @XmlElement(name = "INPUT_RECOVERY_AMOUNT")
    protected String inputrecoveryamount;
    @XmlElement(name = "INPUT_RECOVERY_PERCENT")
    protected String inputrecoverypercent;
    @XmlElement(name = "MIDDLEMAN_MARKUP_AMOUNT")
    protected BigDecimal middlemanmarkupamount;
    @XmlElement(name = "MIDDLEMAN_MARKUP_RATE")
    protected BigDecimal middlemanmarkuprate;
    @XmlElement(name = "IS_BUSINESS_SUPPLY")
    protected String isbusinesssupply;
    @XmlElement(name = "ITEM_VALUE")
    protected String itemvalue;
    @XmlElement(name = "LINE_NUMBER", required = true)
    protected BigDecimal linenumber;
    @XmlElement(name = "RELATED_LINE_NUMBER")
    protected BigDecimal relatedlinenumber;
    @XmlElement(name = "MASS")
    protected BigDecimal mass;
    @XmlElement(name = "MESSAGE")
    protected List<MessageType> message;
    @XmlElement(name = "MIDDLEMAN_BRANCH_ID")
    protected String middlemanbranchid;
    @XmlElement(name = "MODE_OF_TRANSPORT")
    protected String modeoftransport;
    @XmlElement(name = "PART_NUMBER")
    protected String partnumber;
    @XmlElement(name = "POINT_OF_TITLE_TRANSFER", required = true)
    protected String pointoftitletransfer;
    @XmlElement(name = "PORT_OF_ENTRY")
    protected String portofentry;
    @XmlElement(name = "PORT_OF_LOADING")
    protected String portofloading;
    @XmlElement(name = "REGIME")
    protected String regime;
    @XmlElement(name = "SHIP_FROM_BRANCH_ID")
    protected String shipfrombranchid;
    @XmlElement(name = "SHIP_FROM_COUNTRY")
    protected String shipfromcountry;
    @XmlElement(name = "SHIP_TO_BRANCH_ID")
    protected String shiptobranchid;
    @XmlElement(name = "SHIP_TO_COUNTRY")
    protected String shiptocountry;
    @XmlElement(name = "SUPPLEMENTARY_UNIT")
    protected String supplementaryunit;
    @XmlElement(name = "SUPPLY_BRANCH_ID")
    protected String supplybranchid;
    @XmlElement(name = "UNIQUE_LINE_NUMBER")
    protected String uniquelinenumber;
    @XmlElement(name = "VENDOR_NAME")
    protected String vendorname;
    @XmlElement(name = "VENDOR_NUMBER")
    protected String vendornumber;
    @XmlElement(name = "VENDOR_TAX")
    protected String vendortax;
    @XmlElement(name = "TOTAL_TAX_AMOUNT", required = true)
    protected String totaltaxamount;
    @XmlElement(name = "TAX_CODE")
    protected String taxcode;
    @XmlElement(name = "TAX")
    protected List<OutdataTaxType> tax;
    @XmlElement(name = "TRANSACTION_TYPE")
    protected String transactiontype;
    @XmlElement(name = "UNIT_OF_MEASURE")
    protected String unitofmeasure;
    @XmlElement(name = "USER_ELEMENT")
    protected List<UserElementType> userelement;
    @XmlElement(name = "QUANTITIES")
    protected QuantitiesType quantities;
    @XmlElement(name = "IS_CREDIT")
    protected String iscredit;
    @XmlElement(name = "INVOICE_DATE")
    protected String invoicedate;
    @XmlElement(name = "MOVEMENT_DATE")
    protected String movementdate;
    @XmlElement(name = "ORIGINAL_DOCUMENT_ID")
    protected String originaldocumentid;
    @XmlElement(name = "ORIGINAL_DOCUMENT_ITEM")
    protected String originaldocumentitem;
    @XmlElement(name = "ORIGINAL_DOCUMENT_TYPE")
    protected String originaldocumenttype;
    @XmlElement(name = "ORIGINAL_INVOICE_DATE")
    protected String originalinvoicedate;
    @XmlElement(name = "ORIGINAL_INVOICE_NUMBER")
    protected String originalinvoicenumber;
    @XmlElement(name = "ORIGINAL_MOVEMENT_DATE")
    protected String originalmovementdate;
    @XmlElement(name = "CUSTOMER_GROUP_NAME")
    protected String customergroupname;
    @XmlElement(name = "CUSTOMER_GROUP_OWNER")
    protected String customergroupowner;
    @XmlElement(name = "TITLE_TRANSFER_LOCATION")
    protected String titletransferlocation;
    @XmlElement(name = "TAX_SUMMARY")
    protected OutdataTaxSummaryType taxsummary;
    @XmlAttribute(name = "ID", required = true)
    protected String id;

    /**
     * Gets the value of the allocationline property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the allocationline property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getALLOCATIONLINE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OutdataLineType }
     * 
     * 
     */
    public List<OutdataLineType> getALLOCATIONLINE() {
        if (allocationline == null) {
            allocationline = new ArrayList<OutdataLineType>();
        }
        return this.allocationline;
    }

    /**
     * Gets the value of the accountingcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getACCOUNTINGCODE() {
        return accountingcode;
    }

    /**
     * Sets the value of the accountingcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setACCOUNTINGCODE(String value) {
        this.accountingcode = value;
    }

    /**
     * Gets the value of the allocationname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getALLOCATIONNAME() {
        return allocationname;
    }

    /**
     * Sets the value of the allocationname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setALLOCATIONNAME(String value) {
        this.allocationname = value;
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
     * Gets the value of the billtobranchid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBILLTOBRANCHID() {
        return billtobranchid;
    }

    /**
     * Sets the value of the billtobranchid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBILLTOBRANCHID(String value) {
        this.billtobranchid = value;
    }

    /**
     * Gets the value of the commoditycode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMMODITYCODE() {
        return commoditycode;
    }

    /**
     * Sets the value of the commoditycode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMMODITYCODE(String value) {
        this.commoditycode = value;
    }

    /**
     * Gets the value of the countryoforigin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOUNTRYOFORIGIN() {
        return countryoforigin;
    }

    /**
     * Sets the value of the countryoforigin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOUNTRYOFORIGIN(String value) {
        this.countryoforigin = value;
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
     * Gets the value of the deptofconsign property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDEPTOFCONSIGN() {
        return deptofconsign;
    }

    /**
     * Sets the value of the deptofconsign property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDEPTOFCONSIGN(String value) {
        this.deptofconsign = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCRIPTION() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCRIPTION(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the discountamount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDISCOUNTAMOUNT() {
        return discountamount;
    }

    /**
     * Sets the value of the discountamount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDISCOUNTAMOUNT(String value) {
        this.discountamount = value;
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
     * Gets the value of the deliveryterms property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDELIVERYTERMS() {
        return deliveryterms;
    }

    /**
     * Sets the value of the deliveryterms property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDELIVERYTERMS(String value) {
        this.deliveryterms = value;
    }

    /**
     * Gets the value of the freightonboard property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFREIGHTONBOARD() {
        return freightonboard;
    }

    /**
     * Sets the value of the freightonboard property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFREIGHTONBOARD(String value) {
        this.freightonboard = value;
    }

    /**
     * Gets the value of the grossamount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGROSSAMOUNT() {
        return grossamount;
    }

    /**
     * Sets the value of the grossamount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGROSSAMOUNT(String value) {
        this.grossamount = value;
    }

    /**
     * Gets the value of the inputrecoveryamount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINPUTRECOVERYAMOUNT() {
        return inputrecoveryamount;
    }

    /**
     * Sets the value of the inputrecoveryamount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINPUTRECOVERYAMOUNT(String value) {
        this.inputrecoveryamount = value;
    }

    /**
     * Gets the value of the inputrecoverypercent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINPUTRECOVERYPERCENT() {
        return inputrecoverypercent;
    }

    /**
     * Sets the value of the inputrecoverypercent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINPUTRECOVERYPERCENT(String value) {
        this.inputrecoverypercent = value;
    }

    /**
     * Gets the value of the middlemanmarkupamount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMIDDLEMANMARKUPAMOUNT() {
        return middlemanmarkupamount;
    }

    /**
     * Sets the value of the middlemanmarkupamount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMIDDLEMANMARKUPAMOUNT(BigDecimal value) {
        this.middlemanmarkupamount = value;
    }

    /**
     * Gets the value of the middlemanmarkuprate property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMIDDLEMANMARKUPRATE() {
        return middlemanmarkuprate;
    }

    /**
     * Sets the value of the middlemanmarkuprate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMIDDLEMANMARKUPRATE(BigDecimal value) {
        this.middlemanmarkuprate = value;
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
     * Gets the value of the itemvalue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMVALUE() {
        return itemvalue;
    }

    /**
     * Sets the value of the itemvalue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMVALUE(String value) {
        this.itemvalue = value;
    }

    /**
     * Gets the value of the linenumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLINENUMBER() {
        return linenumber;
    }

    /**
     * Sets the value of the linenumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLINENUMBER(BigDecimal value) {
        this.linenumber = value;
    }

    /**
     * Gets the value of the relatedlinenumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRELATEDLINENUMBER() {
        return relatedlinenumber;
    }

    /**
     * Sets the value of the relatedlinenumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRELATEDLINENUMBER(BigDecimal value) {
        this.relatedlinenumber = value;
    }

    /**
     * Gets the value of the mass property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMASS() {
        return mass;
    }

    /**
     * Sets the value of the mass property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMASS(BigDecimal value) {
        this.mass = value;
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
     * Gets the value of the middlemanbranchid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMIDDLEMANBRANCHID() {
        return middlemanbranchid;
    }

    /**
     * Sets the value of the middlemanbranchid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMIDDLEMANBRANCHID(String value) {
        this.middlemanbranchid = value;
    }

    /**
     * Gets the value of the modeoftransport property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMODEOFTRANSPORT() {
        return modeoftransport;
    }

    /**
     * Sets the value of the modeoftransport property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMODEOFTRANSPORT(String value) {
        this.modeoftransport = value;
    }

    /**
     * Gets the value of the partnumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPARTNUMBER() {
        return partnumber;
    }

    /**
     * Sets the value of the partnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPARTNUMBER(String value) {
        this.partnumber = value;
    }

    /**
     * Gets the value of the pointoftitletransfer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOINTOFTITLETRANSFER() {
        return pointoftitletransfer;
    }

    /**
     * Sets the value of the pointoftitletransfer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOINTOFTITLETRANSFER(String value) {
        this.pointoftitletransfer = value;
    }

    /**
     * Gets the value of the portofentry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPORTOFENTRY() {
        return portofentry;
    }

    /**
     * Sets the value of the portofentry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPORTOFENTRY(String value) {
        this.portofentry = value;
    }

    /**
     * Gets the value of the portofloading property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPORTOFLOADING() {
        return portofloading;
    }

    /**
     * Sets the value of the portofloading property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPORTOFLOADING(String value) {
        this.portofloading = value;
    }

    /**
     * Gets the value of the regime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGIME() {
        return regime;
    }

    /**
     * Sets the value of the regime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGIME(String value) {
        this.regime = value;
    }

    /**
     * Gets the value of the shipfrombranchid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSHIPFROMBRANCHID() {
        return shipfrombranchid;
    }

    /**
     * Sets the value of the shipfrombranchid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSHIPFROMBRANCHID(String value) {
        this.shipfrombranchid = value;
    }

    /**
     * Gets the value of the shipfromcountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSHIPFROMCOUNTRY() {
        return shipfromcountry;
    }

    /**
     * Sets the value of the shipfromcountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSHIPFROMCOUNTRY(String value) {
        this.shipfromcountry = value;
    }

    /**
     * Gets the value of the shiptobranchid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSHIPTOBRANCHID() {
        return shiptobranchid;
    }

    /**
     * Sets the value of the shiptobranchid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSHIPTOBRANCHID(String value) {
        this.shiptobranchid = value;
    }

    /**
     * Gets the value of the shiptocountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSHIPTOCOUNTRY() {
        return shiptocountry;
    }

    /**
     * Sets the value of the shiptocountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSHIPTOCOUNTRY(String value) {
        this.shiptocountry = value;
    }

    /**
     * Gets the value of the supplementaryunit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUPPLEMENTARYUNIT() {
        return supplementaryunit;
    }

    /**
     * Sets the value of the supplementaryunit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUPPLEMENTARYUNIT(String value) {
        this.supplementaryunit = value;
    }

    /**
     * Gets the value of the supplybranchid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUPPLYBRANCHID() {
        return supplybranchid;
    }

    /**
     * Sets the value of the supplybranchid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUPPLYBRANCHID(String value) {
        this.supplybranchid = value;
    }

    /**
     * Gets the value of the uniquelinenumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUNIQUELINENUMBER() {
        return uniquelinenumber;
    }

    /**
     * Sets the value of the uniquelinenumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUNIQUELINENUMBER(String value) {
        this.uniquelinenumber = value;
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
     * Gets the value of the taxcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTAXCODE() {
        return taxcode;
    }

    /**
     * Sets the value of the taxcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTAXCODE(String value) {
        this.taxcode = value;
    }

    /**
     * Gets the value of the tax property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tax property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTAX().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OutdataTaxType }
     * 
     * 
     */
    public List<OutdataTaxType> getTAX() {
        if (tax == null) {
            tax = new ArrayList<OutdataTaxType>();
        }
        return this.tax;
    }

    /**
     * Gets the value of the transactiontype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRANSACTIONTYPE() {
        return transactiontype;
    }

    /**
     * Sets the value of the transactiontype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRANSACTIONTYPE(String value) {
        this.transactiontype = value;
    }

    /**
     * Gets the value of the unitofmeasure property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUNITOFMEASURE() {
        return unitofmeasure;
    }

    /**
     * Sets the value of the unitofmeasure property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUNITOFMEASURE(String value) {
        this.unitofmeasure = value;
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
     * Gets the value of the quantities property.
     * 
     * @return
     *     possible object is
     *     {@link QuantitiesType }
     *     
     */
    public QuantitiesType getQUANTITIES() {
        return quantities;
    }

    /**
     * Sets the value of the quantities property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuantitiesType }
     *     
     */
    public void setQUANTITIES(QuantitiesType value) {
        this.quantities = value;
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
     * Gets the value of the movementdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMOVEMENTDATE() {
        return movementdate;
    }

    /**
     * Sets the value of the movementdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMOVEMENTDATE(String value) {
        this.movementdate = value;
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
     * Gets the value of the titletransferlocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTITLETRANSFERLOCATION() {
        return titletransferlocation;
    }

    /**
     * Sets the value of the titletransferlocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTITLETRANSFERLOCATION(String value) {
        this.titletransferlocation = value;
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

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID(String value) {
        this.id = value;
    }

}

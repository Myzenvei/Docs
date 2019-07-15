/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.dto.onesource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;




/**
 * 
 * 				
 * Serves as the container for an individual invoice. Each batch can contain multiple invoices.
 *             	
 * 			
 * 
 * <p>Java class for IndataInvoiceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IndataInvoiceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ALLOCATION_GROUP_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AllocationGroupNameType" minOccurs="0"/>
 *         &lt;element name="ALLOCATION_GROUP_OWNER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AllocationGroupOwnerType" minOccurs="0"/>
 *         &lt;element name="ALLOCATION_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AllocationNameType" minOccurs="0"/>
 *         &lt;element name="AUTO_CREATE_CERTIFICATES" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AutoCreateCertificatesType" minOccurs="0"/>
 *         &lt;element name="AUTO_CREATE_CUSTOMERS" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AutoCreateCustomersType" minOccurs="0"/>
 *         &lt;element name="BASIS_PERCENT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}BasisPercentType" minOccurs="0"/>
 *         &lt;element name="BILL_TO" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ZoneAddressType" minOccurs="0"/>
 *         &lt;element name="BUYER_PRIMARY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ZoneAddressType" minOccurs="0"/>
 *         &lt;element name="CALLING_SYSTEM_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CallingSystemNumberType" minOccurs="0"/>
 *         &lt;element name="CALCULATION_DIRECTION" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CalculationDirectionType" minOccurs="0"/>
 *         &lt;element name="COMPANY_ID" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CompanyIdType" minOccurs="0"/>
 *         &lt;element name="COMPANY_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CompanyNameType" minOccurs="0"/>
 *         &lt;element name="COMPANY_ROLE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CompanyRoleType" minOccurs="0"/>
 *         &lt;element name="COUNTRY_OF_ORIGIN" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CountryOfOriginType" minOccurs="0"/>
 *         &lt;element name="CURRENCY_CODE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CurrencyCodeType"/>
 *         &lt;element name="CUSTOMER_GROUP_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CustomerGroupNameType" minOccurs="0"/>
 *         &lt;element name="CUSTOMER_GROUP_OWNER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CustomerGroupOwnerType" minOccurs="0"/>
 *         &lt;element name="CUSTOMER_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CustomerNameType" minOccurs="0"/>
 *         &lt;element name="CUSTOMER_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CustomerNumberType" minOccurs="0"/>
 *         &lt;element name="DELIVERY_TERMS" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}DeliveryTermsType" minOccurs="0"/>
 *         &lt;element name="DEPT_OF_CONSIGN" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}DeptOfConsignType" minOccurs="0"/>
 *         &lt;element name="DOCUMENT_TYPE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}DocumentTypeType" minOccurs="0"/>
 *         &lt;element name="END_USE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}EndUseType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="END_USER_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}EndUserNameType" minOccurs="0"/>
 *         &lt;element name="ESTABLISHMENTS" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}EstablishmentsType" minOccurs="0"/>
 *         &lt;element name="EXEMPT_AMOUNT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AmountType" minOccurs="0"/>
 *         &lt;element name="EXEMPT_CERTIFICATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CertificateLocationType" minOccurs="0"/>
 *         &lt;element name="EXEMPT_REASON" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CertificateLocationType" minOccurs="0"/>
 *         &lt;element name="EXTERNAL_COMPANY_ID" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ExternalCompanyIdType" minOccurs="0"/>
 *         &lt;element name="FILTER_GROUP_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}FilterGroupNameType" minOccurs="0"/>
 *         &lt;element name="FILTER_GROUP_OWNER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}FilterGroupOwnerType" minOccurs="0"/>
 *         &lt;element name="FISCAL_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}FiscalDateType" minOccurs="0"/>
 *         &lt;element name="FUNCTIONAL_CURRENCY_CODE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}FunctionalCurrencyCodeType" minOccurs="0"/>
 *         &lt;element name="HOST_SYSTEM" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}HostSystemType" minOccurs="0"/>
 *         &lt;element name="INCLUSIVE_TAX_INDICATORS" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}InclusiveTaxIndicatorsType" minOccurs="0"/>
 *         &lt;element name="INPUT_RECOVERY_TYPE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}InputRecoveryTypeType" minOccurs="0"/>
 *         &lt;element name="INVOICE_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}InvoiceDateType"/>
 *         &lt;element name="INVOICE_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}InvoiceNumberType" minOccurs="0"/>
 *         &lt;element name="IS_AUDITED" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IsAuditedType" minOccurs="0"/>
 *         &lt;element name="IS_AUDIT_UPDATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IsAuditUpdateType" minOccurs="0"/>
 *         &lt;element name="IS_BUSINESS_SUPPLY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IsBusinessSupplyType" minOccurs="0"/>
 *         &lt;element name="IS_CREDIT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IsCreditType" minOccurs="0"/>
 *         &lt;element name="IS_EXEMPT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}FlagAddressType" minOccurs="0"/>
 *         &lt;element name="IS_NO_TAX" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}FlagAddressType" minOccurs="0"/>
 *         &lt;element name="IS_REPORTED" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IsReportedType" minOccurs="0"/>
 *         &lt;element name="IS_REVERSED" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IsReversedType" minOccurs="0"/>
 *         &lt;element name="IS_ROUNDING" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IsRoundingType" minOccurs="0"/>
 *         &lt;element name="IS_SIMPLIFICATION" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IsSimplificationType" minOccurs="0"/>
 *         &lt;element name="LICENSES" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IndataLicensesType" minOccurs="0"/>
 *         &lt;element name="LOCATION" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}LocationNameType" minOccurs="0"/>
 *         &lt;element name="LOCATION_SET" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}LocationSetType" minOccurs="0"/>
 *         &lt;element name="MIDDLEMAN" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ZoneAddressType" minOccurs="0"/>
 *         &lt;element name="MODE_OF_TRANSPORT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ModeOfTransportType" minOccurs="0"/>
 *         &lt;element name="MOVEMENT_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}MovementDateType" minOccurs="0"/>
 *         &lt;element name="MOVEMENT_TYPE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}MovementType" minOccurs="0"/>
 *         &lt;element name="NATURE_OF_TRANSACTION_CODE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}NatureOfTransactionCodeType" minOccurs="0"/>
 *         &lt;element name="ORDER_ACCEPTANCE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ZoneAddressType" minOccurs="0"/>
 *         &lt;element name="ORDER_ORIGIN" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ZoneAddressType" minOccurs="0"/>
 *         &lt;element name="ORIGINAL_DOCUMENT_ID" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OriginalDocumentIdType" minOccurs="0"/>
 *         &lt;element name="ORIGINAL_DOCUMENT_ITEM" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OriginalDocumentItemType" minOccurs="0"/>
 *         &lt;element name="ORIGINAL_DOCUMENT_TYPE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OriginalDocumentTypeType" minOccurs="0"/>
 *         &lt;element name="ORIGINAL_INVOICE_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OriginalInvoiceDateType" minOccurs="0"/>
 *         &lt;element name="ORIGINAL_INVOICE_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OriginalInvoiceNumberType" minOccurs="0"/>
 *         &lt;element name="ORIGINAL_MOVEMENT_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OriginalMovementDateType" minOccurs="0"/>
 *         &lt;element name="OVERRIDE_AMOUNT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AmountType" minOccurs="0"/>
 *         &lt;element name="OVERRIDE_RATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AmountType" minOccurs="0"/>
 *         &lt;element name="POINT_OF_TITLE_TRANSFER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}PointOfTitleTransferType" minOccurs="0"/>
 *         &lt;element name="PORT_OF_ENTRY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}PortOfEntryType" minOccurs="0"/>
 *         &lt;element name="PORT_OF_LOADING" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}PortOfLoadingType" minOccurs="0"/>
 *         &lt;element name="PRODUCT_MAPPING_GROUP_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ProductMappingGroupNameType" minOccurs="0"/>
 *         &lt;element name="PRODUCT_MAPPING_GROUP_OWNER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ProductMappingGroupOwnerType" minOccurs="0"/>
 *         &lt;element name="REGIME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegimeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATIONS" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationsType" minOccurs="0"/>
 *         &lt;element name="SELLER_PRIMARY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ZoneAddressType" minOccurs="0"/>
 *         &lt;element name="SHIP_FROM" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ZoneAddressType" minOccurs="0"/>
 *         &lt;element name="SHIP_TO" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ZoneAddressType" minOccurs="0"/>
 *         &lt;element name="STATISTICAL_PROCEDURE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}StatisticalProcedureType" minOccurs="0"/>
 *         &lt;element name="SUPPLY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ZoneAddressType" minOccurs="0"/>
 *         &lt;element name="SUPPLY_EXEMPT_PERCENT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AmountType" minOccurs="0"/>
 *         &lt;element name="TAX_CODE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxCodeType" minOccurs="0"/>
 *         &lt;element name="TAX_DETERMINATION_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxDeterminationDateType" minOccurs="0"/>
 *         &lt;element name="TAX_EXCHANGE_RATE_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxExchangeRateDateType" minOccurs="0"/>
 *         &lt;element name="TAX_POINT_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxPointDateType" minOccurs="0"/>
 *         &lt;element name="TAX_TREATMENT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxTreatment" minOccurs="0"/>
 *         &lt;element name="TAX_TYPE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AddressType" minOccurs="0"/>
 *         &lt;element name="TITLE_TRANSFER_LOCATION" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TitleTransferLocationType" minOccurs="0"/>
 *         &lt;element name="TRANSACTION_TYPE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TransactionType" minOccurs="0"/>
 *         &lt;element name="VAT_GROUP_REGISTRATION" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}VatGroupRegistrationType" minOccurs="0"/>
 *         &lt;element name="VENDOR_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}VendorNameType" minOccurs="0"/>
 *         &lt;element name="VENDOR_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}VendorNumberType" minOccurs="0"/>
 *         &lt;element name="VENDOR_TAX" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}VendorTaxType" minOccurs="0"/>
 *         &lt;element name="UNIQUE_INVOICE_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}UniqueInvoiceNumberType" minOccurs="0"/>
 *         &lt;element name="USER_ELEMENT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}UserElementType" maxOccurs="50" minOccurs="0"/>
 *         &lt;element name="LINE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IndataLineType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndataInvoiceType", propOrder = {
    "allocationgroupname",
    "allocationgroupowner",
    "allocationname",
    "autocreatecertificates",
    "autocreatecustomers",
    "basispercent",
    "billto",
    "buyerprimary",
    "callingsystemnumber",
    "calculationdirection",
    "companyid",
    "companyname",
    "companyrole",
    "countryoforigin",
    "currencycode",
    "customergroupname",
    "customergroupowner",
    "customername",
    "customernumber",
    "deliveryterms",
    "deptofconsign",
    "documenttype",
    "enduse",
    "endusername",
    "establishments",
    "exemptamount",
    "exemptcertificate",
    "exemptreason",
    "externalcompanyid",
    "filtergroupname",
    "filtergroupowner",
    "fiscaldate",
    "functionalcurrencycode",
    "hostsystem",
    "inclusivetaxindicators",
    "inputrecoverytype",
    "invoicedate",
    "invoicenumber",
    "isaudited",
    "isauditupdate",
    "isbusinesssupply",
    "iscredit",
    "isexempt",
    "isnotax",
    "isreported",
    "isreversed",
    "isrounding",
    "issimplification",
    "licenses",
    "location",
    "locationset",
    "middleman",
    "modeoftransport",
    "movementdate",
    "movementtype",
    "natureoftransactioncode",
    "orderacceptance",
    "orderorigin",
    "originaldocumentid",
    "originaldocumentitem",
    "originaldocumenttype",
    "originalinvoicedate",
    "originalinvoicenumber",
    "originalmovementdate",
    "overrideamount",
    "overriderate",
    "pointoftitletransfer",
    "portofentry",
    "portofloading",
    "productmappinggroupname",
    "productmappinggroupowner",
    "regime",
    "registrations",
    "sellerprimary",
    "shipfrom",
    "shipto",
    "statisticalprocedure",
    "supply",
    "supplyexemptpercent",
    "taxcode",
    "taxdeterminationdate",
    "taxexchangeratedate",
    "taxpointdate",
    "taxtreatment",
    "taxtype",
    "titletransferlocation",
    "transactiontype",
    "vatgroupregistration",
    "vendorname",
    "vendornumber",
    "vendortax",
    "uniqueinvoicenumber",
    "userelement",
    "line"
})
public class IndataInvoiceType {

    @XmlElement(name = "ALLOCATION_GROUP_NAME")
    protected String allocationgroupname;
    @XmlElement(name = "ALLOCATION_GROUP_OWNER")
    protected String allocationgroupowner;
    @XmlElement(name = "ALLOCATION_NAME")
    protected String allocationname;
    @XmlElement(name = "AUTO_CREATE_CERTIFICATES")
    protected String autocreatecertificates;
    @XmlElement(name = "AUTO_CREATE_CUSTOMERS")
    protected String autocreatecustomers;
    @XmlElement(name = "BASIS_PERCENT")
    protected String basispercent;
    @XmlElement(name = "BILL_TO")
    protected ZoneAddressType billto;
    @XmlElement(name = "BUYER_PRIMARY")
    protected ZoneAddressType buyerprimary;
    @XmlElement(name = "CALLING_SYSTEM_NUMBER")
    protected String callingsystemnumber;
    @XmlElement(name = "CALCULATION_DIRECTION")
    protected String calculationdirection;
    @XmlElement(name = "COMPANY_ID")
    protected Long companyid;
    @XmlElement(name = "COMPANY_NAME")
    protected String companyname;
    @XmlElement(name = "COMPANY_ROLE")
    protected String companyrole;
    @XmlElement(name = "COUNTRY_OF_ORIGIN")
    protected String countryoforigin;
    @XmlElement(name = "CURRENCY_CODE", required = true)
    protected String currencycode;
    @XmlElement(name = "CUSTOMER_GROUP_NAME")
    protected String customergroupname;
    @XmlElement(name = "CUSTOMER_GROUP_OWNER")
    protected String customergroupowner;
    @XmlElement(name = "CUSTOMER_NAME")
    protected String customername;
    @XmlElement(name = "CUSTOMER_NUMBER")
    protected String customernumber;
    @XmlElement(name = "DELIVERY_TERMS")
    protected String deliveryterms;
    @XmlElement(name = "DEPT_OF_CONSIGN")
    protected String deptofconsign;
    @XmlElement(name = "DOCUMENT_TYPE")
    protected String documenttype;
    @XmlElement(name = "END_USE")
    protected List<String> enduse;
    @XmlElement(name = "END_USER_NAME")
    protected String endusername;
    @XmlElement(name = "ESTABLISHMENTS")
    protected EstablishmentsType establishments;
    @XmlElement(name = "EXEMPT_AMOUNT")
    protected AmountType exemptamount;
    @XmlElement(name = "EXEMPT_CERTIFICATE")
    protected CertificateLocationType exemptcertificate;
    @XmlElement(name = "EXEMPT_REASON")
    protected CertificateLocationType exemptreason;
    @XmlElement(name = "EXTERNAL_COMPANY_ID")
    protected String externalcompanyid;
    @XmlElement(name = "FILTER_GROUP_NAME")
    protected String filtergroupname;
    @XmlElement(name = "FILTER_GROUP_OWNER")
    protected String filtergroupowner;
    @XmlElement(name = "FISCAL_DATE")
    protected String fiscaldate;
    @XmlElement(name = "FUNCTIONAL_CURRENCY_CODE")
    protected String functionalcurrencycode;
    @XmlElement(name = "HOST_SYSTEM")
    protected String hostsystem;
    @XmlElement(name = "INCLUSIVE_TAX_INDICATORS")
    protected InclusiveTaxIndicatorsType inclusivetaxindicators;
    @XmlElement(name = "INPUT_RECOVERY_TYPE")
    protected String inputrecoverytype;
    @XmlElement(name = "INVOICE_DATE", required = true)
    protected String invoicedate;
    @XmlElement(name = "INVOICE_NUMBER")
    protected String invoicenumber;
    @XmlElement(name = "IS_AUDITED")
    protected String isaudited;
    @XmlElement(name = "IS_AUDIT_UPDATE")
    protected String isauditupdate;
    @XmlElement(name = "IS_BUSINESS_SUPPLY")
    protected String isbusinesssupply;
    @XmlElement(name = "IS_CREDIT")
    protected String iscredit;
    @XmlElement(name = "IS_EXEMPT")
    protected FlagAddressType isexempt;
    @XmlElement(name = "IS_NO_TAX")
    protected FlagAddressType isnotax;
    @XmlElement(name = "IS_REPORTED")
    protected String isreported;
    @XmlElement(name = "IS_REVERSED")
    protected String isreversed;
    @XmlElement(name = "IS_ROUNDING")
    protected String isrounding;
    @XmlElement(name = "IS_SIMPLIFICATION")
    protected String issimplification;
    @XmlElement(name = "LICENSES")
    protected IndataLicensesType licenses;
    @XmlElement(name = "LOCATION")
    protected LocationNameType location;
    @XmlElement(name = "LOCATION_SET")
    protected String locationset;
    @XmlElement(name = "MIDDLEMAN")
    protected ZoneAddressType middleman;
    @XmlElement(name = "MODE_OF_TRANSPORT")
    protected String modeoftransport;
    @XmlElement(name = "MOVEMENT_DATE")
    protected String movementdate;
    @XmlElement(name = "MOVEMENT_TYPE")
    protected String movementtype;
    @XmlElement(name = "NATURE_OF_TRANSACTION_CODE")
    protected String natureoftransactioncode;
    @XmlElement(name = "ORDER_ACCEPTANCE")
    protected ZoneAddressType orderacceptance;
    @XmlElement(name = "ORDER_ORIGIN")
    protected ZoneAddressType orderorigin;
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
    @XmlElement(name = "OVERRIDE_AMOUNT")
    protected AmountType overrideamount;
    @XmlElement(name = "OVERRIDE_RATE")
    protected AmountType overriderate;
    @XmlElement(name = "POINT_OF_TITLE_TRANSFER")
    protected String pointoftitletransfer;
    @XmlElement(name = "PORT_OF_ENTRY")
    protected String portofentry;
    @XmlElement(name = "PORT_OF_LOADING")
    protected String portofloading;
    @XmlElement(name = "PRODUCT_MAPPING_GROUP_NAME")
    protected String productmappinggroupname;
    @XmlElement(name = "PRODUCT_MAPPING_GROUP_OWNER")
    protected String productmappinggroupowner;
    @XmlElement(name = "REGIME")
    protected String regime;
    @XmlElement(name = "REGISTRATIONS")
    protected RegistrationsType registrations;
    @XmlElement(name = "SELLER_PRIMARY")
    protected ZoneAddressType sellerprimary;
    @XmlElement(name = "SHIP_FROM")
    protected ZoneAddressType shipfrom;
    @XmlElement(name = "SHIP_TO")
    protected ZoneAddressType shipto;
    @XmlElement(name = "STATISTICAL_PROCEDURE")
    protected String statisticalprocedure;
    @XmlElement(name = "SUPPLY")
    protected ZoneAddressType supply;
    @XmlElement(name = "SUPPLY_EXEMPT_PERCENT")
    protected AmountType supplyexemptpercent;
    @XmlElement(name = "TAX_CODE")
    protected String taxcode;
    @XmlElement(name = "TAX_DETERMINATION_DATE")
    protected String taxdeterminationdate;
    @XmlElement(name = "TAX_EXCHANGE_RATE_DATE")
    protected String taxexchangeratedate;
    @XmlElement(name = "TAX_POINT_DATE")
    protected String taxpointdate;
    @XmlElement(name = "TAX_TREATMENT")
    protected String taxtreatment;
    @XmlElement(name = "TAX_TYPE")
    protected AddressType taxtype;
    @XmlElement(name = "TITLE_TRANSFER_LOCATION")
    protected String titletransferlocation;
    @XmlElement(name = "TRANSACTION_TYPE")
    protected String transactiontype;
    @XmlElement(name = "VAT_GROUP_REGISTRATION")
    protected String vatgroupregistration;
    @XmlElement(name = "VENDOR_NAME")
    protected String vendorname;
    @XmlElement(name = "VENDOR_NUMBER")
    protected String vendornumber;
    @XmlElement(name = "VENDOR_TAX")
    protected String vendortax;
    @XmlElement(name = "UNIQUE_INVOICE_NUMBER")
    protected String uniqueinvoicenumber;
    @XmlElement(name = "USER_ELEMENT")
    protected List<UserElementType> userelement;
    @XmlElement(name = "LINE", required = true)
    protected List<IndataLineType> line;

    /**
     * Gets the value of the allocationgroupname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getALLOCATIONGROUPNAME() {
        return allocationgroupname;
    }

    /**
     * Sets the value of the allocationgroupname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setALLOCATIONGROUPNAME(String value) {
        this.allocationgroupname = value;
    }

    /**
     * Gets the value of the allocationgroupowner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getALLOCATIONGROUPOWNER() {
        return allocationgroupowner;
    }

    /**
     * Sets the value of the allocationgroupowner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setALLOCATIONGROUPOWNER(String value) {
        this.allocationgroupowner = value;
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
     * Gets the value of the autocreatecertificates property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAUTOCREATECERTIFICATES() {
        return autocreatecertificates;
    }

    /**
     * Sets the value of the autocreatecertificates property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAUTOCREATECERTIFICATES(String value) {
        this.autocreatecertificates = value;
    }

    /**
     * Gets the value of the autocreatecustomers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAUTOCREATECUSTOMERS() {
        return autocreatecustomers;
    }

    /**
     * Sets the value of the autocreatecustomers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAUTOCREATECUSTOMERS(String value) {
        this.autocreatecustomers = value;
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
     * Gets the value of the billto property.
     * 
     * @return
     *     possible object is
     *     {@link ZoneAddressType }
     *     
     */
    public ZoneAddressType getBILLTO() {
        return billto;
    }

    /**
     * Sets the value of the billto property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZoneAddressType }
     *     
     */
    public void setBILLTO(ZoneAddressType value) {
        this.billto = value;
    }

    /**
     * Gets the value of the buyerprimary property.
     * 
     * @return
     *     possible object is
     *     {@link ZoneAddressType }
     *     
     */
    public ZoneAddressType getBUYERPRIMARY() {
        return buyerprimary;
    }

    /**
     * Sets the value of the buyerprimary property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZoneAddressType }
     *     
     */
    public void setBUYERPRIMARY(ZoneAddressType value) {
        this.buyerprimary = value;
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
     * Gets the value of the documenttype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDOCUMENTTYPE() {
        return documenttype;
    }

    /**
     * Sets the value of the documenttype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDOCUMENTTYPE(String value) {
        this.documenttype = value;
    }

    /**
     * Gets the value of the enduse property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the enduse property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getENDUSE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getENDUSE() {
        if (enduse == null) {
            enduse = new ArrayList<String>();
        }
        return this.enduse;
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
     * Gets the value of the establishments property.
     * 
     * @return
     *     possible object is
     *     {@link EstablishmentsType }
     *     
     */
    public EstablishmentsType getESTABLISHMENTS() {
        return establishments;
    }

    /**
     * Sets the value of the establishments property.
     * 
     * @param value
     *     allowed object is
     *     {@link EstablishmentsType }
     *     
     */
    public void setESTABLISHMENTS(EstablishmentsType value) {
        this.establishments = value;
    }

    /**
     * Gets the value of the exemptamount property.
     * 
     * @return
     *     possible object is
     *     {@link AmountType }
     *     
     */
    public AmountType getEXEMPTAMOUNT() {
        return exemptamount;
    }

    /**
     * Sets the value of the exemptamount property.
     * 
     * @param value
     *     allowed object is
     *     {@link AmountType }
     *     
     */
    public void setEXEMPTAMOUNT(AmountType value) {
        this.exemptamount = value;
    }

    /**
     * Gets the value of the exemptcertificate property.
     * 
     * @return
     *     possible object is
     *     {@link CertificateLocationType }
     *     
     */
    public CertificateLocationType getEXEMPTCERTIFICATE() {
        return exemptcertificate;
    }

    /**
     * Sets the value of the exemptcertificate property.
     * 
     * @param value
     *     allowed object is
     *     {@link CertificateLocationType }
     *     
     */
    public void setEXEMPTCERTIFICATE(CertificateLocationType value) {
        this.exemptcertificate = value;
    }

    /**
     * Gets the value of the exemptreason property.
     * 
     * @return
     *     possible object is
     *     {@link CertificateLocationType }
     *     
     */
    public CertificateLocationType getEXEMPTREASON() {
        return exemptreason;
    }

    /**
     * Sets the value of the exemptreason property.
     * 
     * @param value
     *     allowed object is
     *     {@link CertificateLocationType }
     *     
     */
    public void setEXEMPTREASON(CertificateLocationType value) {
        this.exemptreason = value;
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
     * Gets the value of the filtergroupname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFILTERGROUPNAME() {
        return filtergroupname;
    }

    /**
     * Sets the value of the filtergroupname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFILTERGROUPNAME(String value) {
        this.filtergroupname = value;
    }

    /**
     * Gets the value of the filtergroupowner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFILTERGROUPOWNER() {
        return filtergroupowner;
    }

    /**
     * Sets the value of the filtergroupowner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFILTERGROUPOWNER(String value) {
        this.filtergroupowner = value;
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
     * Gets the value of the inclusivetaxindicators property.
     * 
     * @return
     *     possible object is
     *     {@link InclusiveTaxIndicatorsType }
     *     
     */
    public InclusiveTaxIndicatorsType getINCLUSIVETAXINDICATORS() {
        return inclusivetaxindicators;
    }

    /**
     * Sets the value of the inclusivetaxindicators property.
     * 
     * @param value
     *     allowed object is
     *     {@link InclusiveTaxIndicatorsType }
     *     
     */
    public void setINCLUSIVETAXINDICATORS(InclusiveTaxIndicatorsType value) {
        this.inclusivetaxindicators = value;
    }

    /**
     * Gets the value of the inputrecoverytype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINPUTRECOVERYTYPE() {
        return inputrecoverytype;
    }

    /**
     * Sets the value of the inputrecoverytype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINPUTRECOVERYTYPE(String value) {
        this.inputrecoverytype = value;
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
     * Gets the value of the isaudited property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISAUDITED() {
        return isaudited;
    }

    /**
     * Sets the value of the isaudited property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISAUDITED(String value) {
        this.isaudited = value;
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
     * Gets the value of the isexempt property.
     * 
     * @return
     *     possible object is
     *     {@link FlagAddressType }
     *     
     */
    public FlagAddressType getISEXEMPT() {
        return isexempt;
    }

    /**
     * Sets the value of the isexempt property.
     * 
     * @param value
     *     allowed object is
     *     {@link FlagAddressType }
     *     
     */
    public void setISEXEMPT(FlagAddressType value) {
        this.isexempt = value;
    }

    /**
     * Gets the value of the isnotax property.
     * 
     * @return
     *     possible object is
     *     {@link FlagAddressType }
     *     
     */
    public FlagAddressType getISNOTAX() {
        return isnotax;
    }

    /**
     * Sets the value of the isnotax property.
     * 
     * @param value
     *     allowed object is
     *     {@link FlagAddressType }
     *     
     */
    public void setISNOTAX(FlagAddressType value) {
        this.isnotax = value;
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
     * Gets the value of the isrounding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISROUNDING() {
        return isrounding;
    }

    /**
     * Sets the value of the isrounding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISROUNDING(String value) {
        this.isrounding = value;
    }

    /**
     * Gets the value of the issimplification property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISSIMPLIFICATION() {
        return issimplification;
    }

    /**
     * Sets the value of the issimplification property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISSIMPLIFICATION(String value) {
        this.issimplification = value;
    }

    /**
     * Gets the value of the licenses property.
     * 
     * @return
     *     possible object is
     *     {@link IndataLicensesType }
     *     
     */
    public IndataLicensesType getLICENSES() {
        return licenses;
    }

    /**
     * Sets the value of the licenses property.
     * 
     * @param value
     *     allowed object is
     *     {@link IndataLicensesType }
     *     
     */
    public void setLICENSES(IndataLicensesType value) {
        this.licenses = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link LocationNameType }
     *     
     */
    public LocationNameType getLOCATION() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocationNameType }
     *     
     */
    public void setLOCATION(LocationNameType value) {
        this.location = value;
    }

    /**
     * Gets the value of the locationset property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLOCATIONSET() {
        return locationset;
    }

    /**
     * Sets the value of the locationset property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLOCATIONSET(String value) {
        this.locationset = value;
    }

    /**
     * Gets the value of the middleman property.
     * 
     * @return
     *     possible object is
     *     {@link ZoneAddressType }
     *     
     */
    public ZoneAddressType getMIDDLEMAN() {
        return middleman;
    }

    /**
     * Sets the value of the middleman property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZoneAddressType }
     *     
     */
    public void setMIDDLEMAN(ZoneAddressType value) {
        this.middleman = value;
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
     * Gets the value of the movementtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMOVEMENTTYPE() {
        return movementtype;
    }

    /**
     * Sets the value of the movementtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMOVEMENTTYPE(String value) {
        this.movementtype = value;
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
     * Gets the value of the orderacceptance property.
     * 
     * @return
     *     possible object is
     *     {@link ZoneAddressType }
     *     
     */
    public ZoneAddressType getORDERACCEPTANCE() {
        return orderacceptance;
    }

    /**
     * Sets the value of the orderacceptance property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZoneAddressType }
     *     
     */
    public void setORDERACCEPTANCE(ZoneAddressType value) {
        this.orderacceptance = value;
    }

    /**
     * Gets the value of the orderorigin property.
     * 
     * @return
     *     possible object is
     *     {@link ZoneAddressType }
     *     
     */
    public ZoneAddressType getORDERORIGIN() {
        return orderorigin;
    }

    /**
     * Sets the value of the orderorigin property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZoneAddressType }
     *     
     */
    public void setORDERORIGIN(ZoneAddressType value) {
        this.orderorigin = value;
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
     * Gets the value of the overrideamount property.
     * 
     * @return
     *     possible object is
     *     {@link AmountType }
     *     
     */
    public AmountType getOVERRIDEAMOUNT() {
        return overrideamount;
    }

    /**
     * Sets the value of the overrideamount property.
     * 
     * @param value
     *     allowed object is
     *     {@link AmountType }
     *     
     */
    public void setOVERRIDEAMOUNT(AmountType value) {
        this.overrideamount = value;
    }

    /**
     * Gets the value of the overriderate property.
     * 
     * @return
     *     possible object is
     *     {@link AmountType }
     *     
     */
    public AmountType getOVERRIDERATE() {
        return overriderate;
    }

    /**
     * Sets the value of the overriderate property.
     * 
     * @param value
     *     allowed object is
     *     {@link AmountType }
     *     
     */
    public void setOVERRIDERATE(AmountType value) {
        this.overriderate = value;
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
     * Gets the value of the productmappinggroupname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRODUCTMAPPINGGROUPNAME() {
        return productmappinggroupname;
    }

    /**
     * Sets the value of the productmappinggroupname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRODUCTMAPPINGGROUPNAME(String value) {
        this.productmappinggroupname = value;
    }

    /**
     * Gets the value of the productmappinggroupowner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRODUCTMAPPINGGROUPOWNER() {
        return productmappinggroupowner;
    }

    /**
     * Sets the value of the productmappinggroupowner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRODUCTMAPPINGGROUPOWNER(String value) {
        this.productmappinggroupowner = value;
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
     * Gets the value of the registrations property.
     * 
     * @return
     *     possible object is
     *     {@link RegistrationsType }
     *     
     */
    public RegistrationsType getREGISTRATIONS() {
        return registrations;
    }

    /**
     * Sets the value of the registrations property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegistrationsType }
     *     
     */
    public void setREGISTRATIONS(RegistrationsType value) {
        this.registrations = value;
    }

    /**
     * Gets the value of the sellerprimary property.
     * 
     * @return
     *     possible object is
     *     {@link ZoneAddressType }
     *     
     */
    public ZoneAddressType getSELLERPRIMARY() {
        return sellerprimary;
    }

    /**
     * Sets the value of the sellerprimary property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZoneAddressType }
     *     
     */
    public void setSELLERPRIMARY(ZoneAddressType value) {
        this.sellerprimary = value;
    }

    /**
     * Gets the value of the shipfrom property.
     * 
     * @return
     *     possible object is
     *     {@link ZoneAddressType }
     *     
     */
    public ZoneAddressType getSHIPFROM() {
        return shipfrom;
    }

    /**
     * Sets the value of the shipfrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZoneAddressType }
     *     
     */
    public void setSHIPFROM(ZoneAddressType value) {
        this.shipfrom = value;
    }

    /**
     * Gets the value of the shipto property.
     * 
     * @return
     *     possible object is
     *     {@link ZoneAddressType }
     *     
     */
    public ZoneAddressType getSHIPTO() {
        return shipto;
    }

    /**
     * Sets the value of the shipto property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZoneAddressType }
     *     
     */
    public void setSHIPTO(ZoneAddressType value) {
        this.shipto = value;
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
     * Gets the value of the supply property.
     * 
     * @return
     *     possible object is
     *     {@link ZoneAddressType }
     *     
     */
    public ZoneAddressType getSUPPLY() {
        return supply;
    }

    /**
     * Sets the value of the supply property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZoneAddressType }
     *     
     */
    public void setSUPPLY(ZoneAddressType value) {
        this.supply = value;
    }

    /**
     * Gets the value of the supplyexemptpercent property.
     * 
     * @return
     *     possible object is
     *     {@link AmountType }
     *     
     */
    public AmountType getSUPPLYEXEMPTPERCENT() {
        return supplyexemptpercent;
    }

    /**
     * Sets the value of the supplyexemptpercent property.
     * 
     * @param value
     *     allowed object is
     *     {@link AmountType }
     *     
     */
    public void setSUPPLYEXEMPTPERCENT(AmountType value) {
        this.supplyexemptpercent = value;
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
     * Gets the value of the taxdeterminationdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTAXDETERMINATIONDATE() {
        return taxdeterminationdate;
    }

    /**
     * Sets the value of the taxdeterminationdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTAXDETERMINATIONDATE(String value) {
        this.taxdeterminationdate = value;
    }

    /**
     * Gets the value of the taxexchangeratedate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTAXEXCHANGERATEDATE() {
        return taxexchangeratedate;
    }

    /**
     * Sets the value of the taxexchangeratedate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTAXEXCHANGERATEDATE(String value) {
        this.taxexchangeratedate = value;
    }

    /**
     * Gets the value of the taxpointdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTAXPOINTDATE() {
        return taxpointdate;
    }

    /**
     * Sets the value of the taxpointdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTAXPOINTDATE(String value) {
        this.taxpointdate = value;
    }

    /**
     * Gets the value of the taxtreatment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTAXTREATMENT() {
        return taxtreatment;
    }

    /**
     * Sets the value of the taxtreatment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTAXTREATMENT(String value) {
        this.taxtreatment = value;
    }

    /**
     * Gets the value of the taxtype property.
     * 
     * @return
     *     possible object is
     *     {@link AddressType }
     *     
     */
    public AddressType getTAXTYPE() {
        return taxtype;
    }

    /**
     * Sets the value of the taxtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressType }
     *     
     */
    public void setTAXTYPE(AddressType value) {
        this.taxtype = value;
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
     * Gets the value of the vatgroupregistration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVATGROUPREGISTRATION() {
        return vatgroupregistration;
    }

    /**
     * Sets the value of the vatgroupregistration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVATGROUPREGISTRATION(String value) {
        this.vatgroupregistration = value;
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
     * {@link IndataLineType }
     * 
     * 
     */
    public List<IndataLineType> getLINE() {
        if (line == null) {
            line = new ArrayList<IndataLineType>();
        }
        return this.line;
    }

}

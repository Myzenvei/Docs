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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.gp.commerce.core.dto.onesource.AuthorityAttributeType;
import com.gp.commerce.core.dto.onesource.ConvertedCurrencyAmountType;
import com.gp.commerce.core.dto.onesource.CurrencyConversionType;
import com.gp.commerce.core.dto.onesource.FeeType;
import com.gp.commerce.core.dto.onesource.MessageType;
import com.gp.commerce.core.dto.onesource.OutdataLicensesType;
import com.gp.commerce.core.dto.onesource.UomConversionType;


/**
 * <p>Java class for OutdataTaxType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutdataTaxType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ADDRESS_TYPE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxAddressType"/>
 *         &lt;element name="ADMIN_ZONE_LEVEL" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AdminZoneLevelType"/>
 *         &lt;element name="AUTHORITY_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AuthorityNameType"/>
 *         &lt;element name="AUTHORITY_TYPE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AuthorityTypeType"/>
 *         &lt;element name="BASIS_PERCENT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}BasisPercentType" minOccurs="0"/>
 *         &lt;element name="CALCULATION_METHOD" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CalculationMethodType" minOccurs="0"/>
 *         &lt;element name="COMMENT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CommentType" minOccurs="0"/>
 *         &lt;element name="ERP_TAX_CODE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ErpTaxCodeType" minOccurs="0"/>
 *         &lt;element name="AUTHORITY_FIPS" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AuthorityFipsType" minOccurs="0"/>
 *         &lt;element name="EFFECTIVE_ZONE_LEVEL" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}EffectiveZoneLevelType"/>
 *         &lt;element name="EXEMPT_CERTIFICATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ExemptCertificateNumberType" minOccurs="0"/>
 *         &lt;element name="EXEMPT_CERTIFICATE_EXPIRE_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ExemptCertificateExpireDateType" minOccurs="0"/>
 *         &lt;element name="EXEMPT_REASON" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ExemptReasonCodeType" minOccurs="0"/>
 *         &lt;element name="INPUT_RECOVERY_AMOUNT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}InputRecoveryAmountType" minOccurs="0"/>
 *         &lt;element name="INPUT_RECOVERY_PERCENT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}InputRecoveryPercentType" minOccurs="0"/>
 *         &lt;element name="INVOICE_DESCRIPTION" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}InvoiceDescriptionType" minOccurs="0"/>
 *         &lt;element name="JURISDICTION_TEXT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}JurisdictionTextType"/>
 *         &lt;element name="IS_EXEMPT" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IS_INTRASTAT_REPORTED" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IsIntrastatReportedType" minOccurs="0"/>
 *         &lt;element name="IS_NOTAX" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IS_TRIANGULATION" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IS_VAT_REPORTED" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IS_VIES_REPORTED" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="LOCATION_CODE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}LocationCodeType" minOccurs="0"/>
 *         &lt;element name="MESSAGE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}MessageType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="BUYER_REGISTRATION" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationNumberType" minOccurs="0"/>
 *         &lt;element name="SELLER_REGISTRATION" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationNumberType" minOccurs="0"/>
 *         &lt;element name="MIDDLEMAN_REGISTRATION" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationNumberType" minOccurs="0"/>
 *         &lt;element name="OVERRIDE_AMOUNT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="OVERRIDE_RATE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="REVISED_GROSS_AMOUNT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="RULE_ORDER" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="SUPPLY_EXEMPT_PERCENT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="TAXABLE_COUNTRY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxableCountryType" minOccurs="0"/>
 *         &lt;element name="TAXABLE_COUNTRY_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxableCountryNameType" minOccurs="0"/>
 *         &lt;element name="TAXABLE_PROVINCE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxableProvinceType" minOccurs="0"/>
 *         &lt;element name="TAXABLE_STATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxableStateType" minOccurs="0"/>
 *         &lt;element name="TAXABLE_COUNTY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxableCountyType" minOccurs="0"/>
 *         &lt;element name="TAXABLE_CITY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxableCityType" minOccurs="0"/>
 *         &lt;element name="TAXABLE_DISTRICT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxableDistrictType" minOccurs="0"/>
 *         &lt;element name="TAXABLE_GEOCODE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxableGEOCodeType" minOccurs="0"/>
 *         &lt;element name="TAXABLE_POSTCODE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxablePostCodeType" minOccurs="0"/>
 *         &lt;element name="TAX_DIRECTION" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxDirectionType" minOccurs="0"/>
 *         &lt;element name="TAX_RATE_CODE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxRateCodeType" minOccurs="0"/>
 *         &lt;element name="TAX_TYPE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxTypeType"/>
 *         &lt;element name="VAT_GROUP_REGISTRATION" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}VatGroupRegistrationType" minOccurs="0"/>
 *         &lt;element name="ZONE_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ZoneNameType" minOccurs="0"/>
 *         &lt;element name="ZONE_LEVEL" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ZoneLevelType"/>
 *         &lt;element name="FISCAL_REP_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}FiscalRepNameType" minOccurs="0"/>
 *         &lt;element name="FISCAL_REP_ADDRESS1" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}FiscalRepAddress1Type" minOccurs="0"/>
 *         &lt;element name="FISCAL_REP_ADDRESS2" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}FiscalRepAddress2Type" minOccurs="0"/>
 *         &lt;element name="FISCAL_REP_CONTACT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}FiscalRepContactType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_1" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_2" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_3" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_4" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_5" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_6" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_7" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_8" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_9" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_10" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_11" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_12" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_13" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_14" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_15" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_16" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_17" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_18" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_19" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_20" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_21" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_22" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_23" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_24" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_25" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_26" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_27" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_28" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_29" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_30" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_31" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_32" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_33" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_34" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_35" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_36" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_37" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_38" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_39" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_40" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_41" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_42" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_43" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_44" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_45" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_46" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_47" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_48" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_49" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="REGISTRATION_ATTRIBUTE_50" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RegistrationAttributeType" minOccurs="0"/>
 *         &lt;element name="AUTHORITY_ATTRIBUTE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AuthorityAttributeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="TAX_RATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}NillableDecimalType" minOccurs="0"/>
 *         &lt;element name="UOM_CONVERSION" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}UomConversionType" minOccurs="0"/>
 *         &lt;element name="NATURE_OF_TAX" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}NatureOfTaxType" minOccurs="0"/>
 *         &lt;element name="EU_TRANSACTION" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="AUTHORITY_CATEGORY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AuthorityCategoryType" minOccurs="0"/>
 *         &lt;element name="AUTHORITY_OFFICIAL_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AuthorityOfficialNameType" minOccurs="0"/>
 *         &lt;element name="AUTHORITY_TYPE_ALIAS" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AuthorityTypeAliasType" minOccurs="0"/>
 *         &lt;element name="AUTHORITY_UUID" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AuthorityUUIDType" minOccurs="0"/>
 *         &lt;element name="RELATED_LINE_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RelatedLineNumberType" minOccurs="0"/>
 *         &lt;element name="RELATED_ALLOCATION_LINE_NUMBER" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="RULE_REPORTING_CATEGORY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}RuleReportingCategoryType" minOccurs="0"/>
 *         &lt;element name="TAX_TREATMENT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxTreatment" minOccurs="0"/>
 *         &lt;element name="AUTHORITY_CURRENCY_CODE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AuthorityCurrencyCodeType" minOccurs="0"/>
 *         &lt;element name="CURRENCY_CONVERSION" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CurrencyConversionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="EXEMPT_AMOUNT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ConvertedCurrencyAmountType" minOccurs="0"/>
 *         &lt;element name="GROSS_AMOUNT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ConvertedCurrencyAmountType" minOccurs="0"/>
 *         &lt;element name="NON_TAXABLE_BASIS" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ConvertedCurrencyAmountType" minOccurs="0"/>
 *         &lt;element name="TAXABLE_BASIS" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ConvertedCurrencyAmountType" minOccurs="0"/>
 *         &lt;element name="TAX_AMOUNT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ConvertedCurrencyAmountType" minOccurs="0"/>
 *         &lt;element name="FEE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}FeeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="TAX_DETERMINATION_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxDeterminationDateType"/>
 *         &lt;element name="TAX_POINT_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxPointDateType"/>
 *         &lt;element name="INCLUSIVE_TAX" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}InclusiveTaxType" minOccurs="0"/>
 *         &lt;element name="LICENSES" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OutdataLicensesType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutdataTaxType", propOrder = {
    "addresstype",
    "adminzonelevel",
    "authorityname",
    "authoritytype",
    "basispercent",
    "calculationmethod",
    "comment",
    "erptaxcode",
    "authorityfips",
    "effectivezonelevel",
    "exemptcertificate",
    "exemptcertificateexpiredate",
    "exemptreason",
    "inputrecoveryamount",
    "inputrecoverypercent",
    "invoicedescription",
    "jurisdictiontext",
    "isexempt",
    "isintrastatreported",
    "isnotax",
    "istriangulation",
    "isvatreported",
    "isviesreported",
    "locationcode",
    "message",
    "buyerregistration",
    "sellerregistration",
    "middlemanregistration",
    "overrideamount",
    "overriderate",
    "revisedgrossamount",
    "ruleorder",
    "supplyexemptpercent",
    "taxablecountry",
    "taxablecountryname",
    "taxableprovince",
    "taxablestate",
    "taxablecounty",
    "taxablecity",
    "taxabledistrict",
    "taxablegeocode",
    "taxablepostcode",
    "taxdirection",
    "taxratecode",
    "taxtype",
    "vatgroupregistration",
    "zonename",
    "zonelevel",
    "fiscalrepname",
    "fiscalrepaddress1",
    "fiscalrepaddress2",
    "fiscalrepcontact",
    "registrationattribute1",
    "registrationattribute2",
    "registrationattribute3",
    "registrationattribute4",
    "registrationattribute5",
    "registrationattribute6",
    "registrationattribute7",
    "registrationattribute8",
    "registrationattribute9",
    "registrationattribute10",
    "registrationattribute11",
    "registrationattribute12",
    "registrationattribute13",
    "registrationattribute14",
    "registrationattribute15",
    "registrationattribute16",
    "registrationattribute17",
    "registrationattribute18",
    "registrationattribute19",
    "registrationattribute20",
    "registrationattribute21",
    "registrationattribute22",
    "registrationattribute23",
    "registrationattribute24",
    "registrationattribute25",
    "registrationattribute26",
    "registrationattribute27",
    "registrationattribute28",
    "registrationattribute29",
    "registrationattribute30",
    "registrationattribute31",
    "registrationattribute32",
    "registrationattribute33",
    "registrationattribute34",
    "registrationattribute35",
    "registrationattribute36",
    "registrationattribute37",
    "registrationattribute38",
    "registrationattribute39",
    "registrationattribute40",
    "registrationattribute41",
    "registrationattribute42",
    "registrationattribute43",
    "registrationattribute44",
    "registrationattribute45",
    "registrationattribute46",
    "registrationattribute47",
    "registrationattribute48",
    "registrationattribute49",
    "registrationattribute50",
    "authorityattribute",
    "taxrate",
    "uomconversion",
    "natureoftax",
    "eutransaction",
    "authoritycategory",
    "authorityofficialname",
    "authoritytypealias",
    "authorityuuid",
    "relatedlinenumber",
    "relatedallocationlinenumber",
    "rulereportingcategory",
    "taxtreatment",
    "authoritycurrencycode",
    "currencyconversion",
    "exemptamount",
    "grossamount",
    "nontaxablebasis",
    "taxablebasis",
    "taxamount",
    "fee",
    "taxdeterminationdate",
    "taxpointdate",
    "inclusivetax",
    "licenses"
})
public class OutdataTaxType {

    @XmlElement(name = "ADDRESS_TYPE", required = true)
    protected String addresstype;
    @XmlElement(name = "ADMIN_ZONE_LEVEL", required = true)
    protected String adminzonelevel;
    @XmlElement(name = "AUTHORITY_NAME", required = true)
    protected String authorityname;
    @XmlElement(name = "AUTHORITY_TYPE", required = true)
    protected String authoritytype;
    @XmlElement(name = "BASIS_PERCENT")
    protected String basispercent;
    @XmlElement(name = "CALCULATION_METHOD")
    protected String calculationmethod;
    @XmlElement(name = "COMMENT")
    protected String comment;
    @XmlElement(name = "ERP_TAX_CODE")
    protected String erptaxcode;
    @XmlElement(name = "AUTHORITY_FIPS")
    protected String authorityfips;
    @XmlElement(name = "EFFECTIVE_ZONE_LEVEL", required = true)
    protected String effectivezonelevel;
    @XmlElement(name = "EXEMPT_CERTIFICATE")
    protected String exemptcertificate;
    @XmlElement(name = "EXEMPT_CERTIFICATE_EXPIRE_DATE")
    protected String exemptcertificateexpiredate;
    @XmlElement(name = "EXEMPT_REASON")
    protected String exemptreason;
    @XmlElement(name = "INPUT_RECOVERY_AMOUNT")
    protected String inputrecoveryamount;
    @XmlElement(name = "INPUT_RECOVERY_PERCENT")
    protected String inputrecoverypercent;
    @XmlElement(name = "INVOICE_DESCRIPTION")
    protected String invoicedescription;
    @XmlElement(name = "JURISDICTION_TEXT", required = true)
    protected String jurisdictiontext;
    @XmlElement(name = "IS_EXEMPT")
    protected Boolean isexempt;
    @XmlElement(name = "IS_INTRASTAT_REPORTED")
    protected String isintrastatreported;
    @XmlElement(name = "IS_NOTAX")
    protected Boolean isnotax;
    @XmlElement(name = "IS_TRIANGULATION")
    protected Boolean istriangulation;
    @XmlElement(name = "IS_VAT_REPORTED")
    protected Boolean isvatreported;
    @XmlElement(name = "IS_VIES_REPORTED")
    protected Boolean isviesreported;
    @XmlElement(name = "LOCATION_CODE")
    protected String locationcode;
    @XmlElement(name = "MESSAGE")
    protected List<MessageType> message;
    @XmlElement(name = "BUYER_REGISTRATION")
    protected String buyerregistration;
    @XmlElement(name = "SELLER_REGISTRATION")
    protected String sellerregistration;
    @XmlElement(name = "MIDDLEMAN_REGISTRATION")
    protected String middlemanregistration;
    @XmlElement(name = "OVERRIDE_AMOUNT")
    protected BigDecimal overrideamount;
    @XmlElement(name = "OVERRIDE_RATE")
    protected BigDecimal overriderate;
    @XmlElement(name = "REVISED_GROSS_AMOUNT")
    protected BigDecimal revisedgrossamount;
    @XmlElement(name = "RULE_ORDER")
    protected BigDecimal ruleorder;
    @XmlElement(name = "SUPPLY_EXEMPT_PERCENT")
    protected BigDecimal supplyexemptpercent;
    @XmlElement(name = "TAXABLE_COUNTRY")
    protected String taxablecountry;
    @XmlElement(name = "TAXABLE_COUNTRY_NAME")
    protected String taxablecountryname;
    @XmlElement(name = "TAXABLE_PROVINCE")
    protected String taxableprovince;
    @XmlElement(name = "TAXABLE_STATE")
    protected String taxablestate;
    @XmlElement(name = "TAXABLE_COUNTY")
    protected String taxablecounty;
    @XmlElement(name = "TAXABLE_CITY")
    protected String taxablecity;
    @XmlElement(name = "TAXABLE_DISTRICT")
    protected String taxabledistrict;
    @XmlElement(name = "TAXABLE_GEOCODE")
    protected String taxablegeocode;
    @XmlElement(name = "TAXABLE_POSTCODE")
    protected String taxablepostcode;
    @XmlElement(name = "TAX_DIRECTION")
    protected String taxdirection;
    @XmlElement(name = "TAX_RATE_CODE")
    protected String taxratecode;
    @XmlElement(name = "TAX_TYPE", required = true)
    protected String taxtype;
    @XmlElement(name = "VAT_GROUP_REGISTRATION")
    protected String vatgroupregistration;
    @XmlElement(name = "ZONE_NAME")
    protected String zonename;
    @XmlElement(name = "ZONE_LEVEL", required = true)
    protected String zonelevel;
    @XmlElement(name = "FISCAL_REP_NAME")
    protected String fiscalrepname;
    @XmlElement(name = "FISCAL_REP_ADDRESS1")
    protected String fiscalrepaddress1;
    @XmlElement(name = "FISCAL_REP_ADDRESS2")
    protected String fiscalrepaddress2;
    @XmlElement(name = "FISCAL_REP_CONTACT")
    protected String fiscalrepcontact;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_1")
    protected String registrationattribute1;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_2")
    protected String registrationattribute2;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_3")
    protected String registrationattribute3;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_4")
    protected String registrationattribute4;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_5")
    protected String registrationattribute5;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_6")
    protected String registrationattribute6;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_7")
    protected String registrationattribute7;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_8")
    protected String registrationattribute8;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_9")
    protected String registrationattribute9;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_10")
    protected String registrationattribute10;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_11")
    protected String registrationattribute11;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_12")
    protected String registrationattribute12;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_13")
    protected String registrationattribute13;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_14")
    protected String registrationattribute14;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_15")
    protected String registrationattribute15;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_16")
    protected String registrationattribute16;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_17")
    protected String registrationattribute17;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_18")
    protected String registrationattribute18;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_19")
    protected String registrationattribute19;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_20")
    protected String registrationattribute20;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_21")
    protected String registrationattribute21;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_22")
    protected String registrationattribute22;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_23")
    protected String registrationattribute23;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_24")
    protected String registrationattribute24;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_25")
    protected String registrationattribute25;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_26")
    protected String registrationattribute26;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_27")
    protected String registrationattribute27;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_28")
    protected String registrationattribute28;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_29")
    protected String registrationattribute29;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_30")
    protected String registrationattribute30;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_31")
    protected String registrationattribute31;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_32")
    protected String registrationattribute32;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_33")
    protected String registrationattribute33;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_34")
    protected String registrationattribute34;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_35")
    protected String registrationattribute35;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_36")
    protected String registrationattribute36;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_37")
    protected String registrationattribute37;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_38")
    protected String registrationattribute38;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_39")
    protected String registrationattribute39;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_40")
    protected String registrationattribute40;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_41")
    protected String registrationattribute41;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_42")
    protected String registrationattribute42;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_43")
    protected String registrationattribute43;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_44")
    protected String registrationattribute44;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_45")
    protected String registrationattribute45;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_46")
    protected String registrationattribute46;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_47")
    protected String registrationattribute47;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_48")
    protected String registrationattribute48;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_49")
    protected String registrationattribute49;
    @XmlElement(name = "REGISTRATION_ATTRIBUTE_50")
    protected String registrationattribute50;
    @XmlElement(name = "AUTHORITY_ATTRIBUTE")
    protected List<AuthorityAttributeType> authorityattribute;
    @XmlElement(name = "TAX_RATE")
    protected String taxrate;
    @XmlElement(name = "UOM_CONVERSION")
    protected UomConversionType uomconversion;
    @XmlElement(name = "NATURE_OF_TAX")
    protected String natureoftax;
    @XmlElement(name = "EU_TRANSACTION")
    protected Boolean eutransaction;
    @XmlElement(name = "AUTHORITY_CATEGORY")
    protected String authoritycategory;
    @XmlElement(name = "AUTHORITY_OFFICIAL_NAME")
    protected String authorityofficialname;
    @XmlElement(name = "AUTHORITY_TYPE_ALIAS")
    protected String authoritytypealias;
    @XmlElement(name = "AUTHORITY_UUID")
    protected String authorityuuid;
    @XmlElement(name = "RELATED_LINE_NUMBER")
    protected BigDecimal relatedlinenumber;
    @XmlElement(name = "RELATED_ALLOCATION_LINE_NUMBER")
    protected BigDecimal relatedallocationlinenumber;
    @XmlElement(name = "RULE_REPORTING_CATEGORY")
    protected String rulereportingcategory;
    @XmlElement(name = "TAX_TREATMENT")
    protected String taxtreatment;
    @XmlElement(name = "AUTHORITY_CURRENCY_CODE")
    protected String authoritycurrencycode;
    @XmlElement(name = "CURRENCY_CONVERSION")
    protected List<CurrencyConversionType> currencyconversion;
    @XmlElement(name = "EXEMPT_AMOUNT")
    protected ConvertedCurrencyAmountType exemptamount;
    @XmlElement(name = "GROSS_AMOUNT")
    protected ConvertedCurrencyAmountType grossamount;
    @XmlElement(name = "NON_TAXABLE_BASIS")
    protected ConvertedCurrencyAmountType nontaxablebasis;
    @XmlElement(name = "TAXABLE_BASIS")
    protected ConvertedCurrencyAmountType taxablebasis;
    @XmlElement(name = "TAX_AMOUNT")
    protected ConvertedCurrencyAmountType taxamount;
    @XmlElement(name = "FEE")
    protected List<FeeType> fee;
    @XmlElement(name = "TAX_DETERMINATION_DATE", required = true)
    protected String taxdeterminationdate;
    @XmlElement(name = "TAX_POINT_DATE", required = true)
    protected String taxpointdate;
    @XmlElement(name = "INCLUSIVE_TAX")
    protected String inclusivetax;
    @XmlElement(name = "LICENSES")
    protected OutdataLicensesType licenses;

    /**
     * Gets the value of the addresstype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getADDRESSTYPE() {
        return addresstype;
    }

    /**
     * Sets the value of the addresstype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setADDRESSTYPE(String value) {
        this.addresstype = value;
    }

    /**
     * Gets the value of the adminzonelevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getADMINZONELEVEL() {
        return adminzonelevel;
    }

    /**
     * Sets the value of the adminzonelevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setADMINZONELEVEL(String value) {
        this.adminzonelevel = value;
    }

    /**
     * Gets the value of the authorityname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAUTHORITYNAME() {
        return authorityname;
    }

    /**
     * Sets the value of the authorityname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAUTHORITYNAME(String value) {
        this.authorityname = value;
    }

    /**
     * Gets the value of the authoritytype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAUTHORITYTYPE() {
        return authoritytype;
    }

    /**
     * Sets the value of the authoritytype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAUTHORITYTYPE(String value) {
        this.authoritytype = value;
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
     * Gets the value of the calculationmethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCALCULATIONMETHOD() {
        return calculationmethod;
    }

    /**
     * Sets the value of the calculationmethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCALCULATIONMETHOD(String value) {
        this.calculationmethod = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMMENT() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMMENT(String value) {
        this.comment = value;
    }

    /**
     * Gets the value of the erptaxcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getERPTAXCODE() {
        return erptaxcode;
    }

    /**
     * Sets the value of the erptaxcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setERPTAXCODE(String value) {
        this.erptaxcode = value;
    }

    /**
     * Gets the value of the authorityfips property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAUTHORITYFIPS() {
        return authorityfips;
    }

    /**
     * Sets the value of the authorityfips property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAUTHORITYFIPS(String value) {
        this.authorityfips = value;
    }

    /**
     * Gets the value of the effectivezonelevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEFFECTIVEZONELEVEL() {
        return effectivezonelevel;
    }

    /**
     * Sets the value of the effectivezonelevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEFFECTIVEZONELEVEL(String value) {
        this.effectivezonelevel = value;
    }

    /**
     * Gets the value of the exemptcertificate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEXEMPTCERTIFICATE() {
        return exemptcertificate;
    }

    /**
     * Sets the value of the exemptcertificate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEXEMPTCERTIFICATE(String value) {
        this.exemptcertificate = value;
    }

    /**
     * Gets the value of the exemptcertificateexpiredate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEXEMPTCERTIFICATEEXPIREDATE() {
        return exemptcertificateexpiredate;
    }

    /**
     * Sets the value of the exemptcertificateexpiredate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEXEMPTCERTIFICATEEXPIREDATE(String value) {
        this.exemptcertificateexpiredate = value;
    }

    /**
     * Gets the value of the exemptreason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEXEMPTREASON() {
        return exemptreason;
    }

    /**
     * Sets the value of the exemptreason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEXEMPTREASON(String value) {
        this.exemptreason = value;
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
     * Gets the value of the invoicedescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINVOICEDESCRIPTION() {
        return invoicedescription;
    }

    /**
     * Sets the value of the invoicedescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINVOICEDESCRIPTION(String value) {
        this.invoicedescription = value;
    }

    /**
     * Gets the value of the jurisdictiontext property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJURISDICTIONTEXT() {
        return jurisdictiontext;
    }

    /**
     * Sets the value of the jurisdictiontext property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJURISDICTIONTEXT(String value) {
        this.jurisdictiontext = value;
    }

    /**
     * Gets the value of the isexempt property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isISEXEMPT() {
        return isexempt;
    }

    /**
     * Sets the value of the isexempt property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setISEXEMPT(Boolean value) {
        this.isexempt = value;
    }

    /**
     * Gets the value of the isintrastatreported property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISINTRASTATREPORTED() {
        return isintrastatreported;
    }

    /**
     * Sets the value of the isintrastatreported property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISINTRASTATREPORTED(String value) {
        this.isintrastatreported = value;
    }

    /**
     * Gets the value of the isnotax property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isISNOTAX() {
        return isnotax;
    }

    /**
     * Sets the value of the isnotax property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setISNOTAX(Boolean value) {
        this.isnotax = value;
    }

    /**
     * Gets the value of the istriangulation property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isISTRIANGULATION() {
        return istriangulation;
    }

    /**
     * Sets the value of the istriangulation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setISTRIANGULATION(Boolean value) {
        this.istriangulation = value;
    }

    /**
     * Gets the value of the isvatreported property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isISVATREPORTED() {
        return isvatreported;
    }

    /**
     * Sets the value of the isvatreported property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setISVATREPORTED(Boolean value) {
        this.isvatreported = value;
    }

    /**
     * Gets the value of the isviesreported property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isISVIESREPORTED() {
        return isviesreported;
    }

    /**
     * Sets the value of the isviesreported property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setISVIESREPORTED(Boolean value) {
        this.isviesreported = value;
    }

    /**
     * Gets the value of the locationcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLOCATIONCODE() {
        return locationcode;
    }

    /**
     * Sets the value of the locationcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLOCATIONCODE(String value) {
        this.locationcode = value;
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
     * Gets the value of the buyerregistration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBUYERREGISTRATION() {
        return buyerregistration;
    }

    /**
     * Sets the value of the buyerregistration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBUYERREGISTRATION(String value) {
        this.buyerregistration = value;
    }

    /**
     * Gets the value of the sellerregistration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSELLERREGISTRATION() {
        return sellerregistration;
    }

    /**
     * Sets the value of the sellerregistration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSELLERREGISTRATION(String value) {
        this.sellerregistration = value;
    }

    /**
     * Gets the value of the middlemanregistration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMIDDLEMANREGISTRATION() {
        return middlemanregistration;
    }

    /**
     * Sets the value of the middlemanregistration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMIDDLEMANREGISTRATION(String value) {
        this.middlemanregistration = value;
    }

    /**
     * Gets the value of the overrideamount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOVERRIDEAMOUNT() {
        return overrideamount;
    }

    /**
     * Sets the value of the overrideamount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOVERRIDEAMOUNT(BigDecimal value) {
        this.overrideamount = value;
    }

    /**
     * Gets the value of the overriderate property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOVERRIDERATE() {
        return overriderate;
    }

    /**
     * Sets the value of the overriderate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOVERRIDERATE(BigDecimal value) {
        this.overriderate = value;
    }

    /**
     * Gets the value of the revisedgrossamount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getREVISEDGROSSAMOUNT() {
        return revisedgrossamount;
    }

    /**
     * Sets the value of the revisedgrossamount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setREVISEDGROSSAMOUNT(BigDecimal value) {
        this.revisedgrossamount = value;
    }

    /**
     * Gets the value of the ruleorder property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRULEORDER() {
        return ruleorder;
    }

    /**
     * Sets the value of the ruleorder property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRULEORDER(BigDecimal value) {
        this.ruleorder = value;
    }

    /**
     * Gets the value of the supplyexemptpercent property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSUPPLYEXEMPTPERCENT() {
        return supplyexemptpercent;
    }

    /**
     * Sets the value of the supplyexemptpercent property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSUPPLYEXEMPTPERCENT(BigDecimal value) {
        this.supplyexemptpercent = value;
    }

    /**
     * Gets the value of the taxablecountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTAXABLECOUNTRY() {
        return taxablecountry;
    }

    /**
     * Sets the value of the taxablecountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTAXABLECOUNTRY(String value) {
        this.taxablecountry = value;
    }

    /**
     * Gets the value of the taxablecountryname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTAXABLECOUNTRYNAME() {
        return taxablecountryname;
    }

    /**
     * Sets the value of the taxablecountryname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTAXABLECOUNTRYNAME(String value) {
        this.taxablecountryname = value;
    }

    /**
     * Gets the value of the taxableprovince property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTAXABLEPROVINCE() {
        return taxableprovince;
    }

    /**
     * Sets the value of the taxableprovince property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTAXABLEPROVINCE(String value) {
        this.taxableprovince = value;
    }

    /**
     * Gets the value of the taxablestate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTAXABLESTATE() {
        return taxablestate;
    }

    /**
     * Sets the value of the taxablestate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTAXABLESTATE(String value) {
        this.taxablestate = value;
    }

    /**
     * Gets the value of the taxablecounty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTAXABLECOUNTY() {
        return taxablecounty;
    }

    /**
     * Sets the value of the taxablecounty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTAXABLECOUNTY(String value) {
        this.taxablecounty = value;
    }

    /**
     * Gets the value of the taxablecity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTAXABLECITY() {
        return taxablecity;
    }

    /**
     * Sets the value of the taxablecity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTAXABLECITY(String value) {
        this.taxablecity = value;
    }

    /**
     * Gets the value of the taxabledistrict property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTAXABLEDISTRICT() {
        return taxabledistrict;
    }

    /**
     * Sets the value of the taxabledistrict property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTAXABLEDISTRICT(String value) {
        this.taxabledistrict = value;
    }

    /**
     * Gets the value of the taxablegeocode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTAXABLEGEOCODE() {
        return taxablegeocode;
    }

    /**
     * Sets the value of the taxablegeocode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTAXABLEGEOCODE(String value) {
        this.taxablegeocode = value;
    }

    /**
     * Gets the value of the taxablepostcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTAXABLEPOSTCODE() {
        return taxablepostcode;
    }

    /**
     * Sets the value of the taxablepostcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTAXABLEPOSTCODE(String value) {
        this.taxablepostcode = value;
    }

    /**
     * Gets the value of the taxdirection property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTAXDIRECTION() {
        return taxdirection;
    }

    /**
     * Sets the value of the taxdirection property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTAXDIRECTION(String value) {
        this.taxdirection = value;
    }

    /**
     * Gets the value of the taxratecode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTAXRATECODE() {
        return taxratecode;
    }

    /**
     * Sets the value of the taxratecode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTAXRATECODE(String value) {
        this.taxratecode = value;
    }

    /**
     * Gets the value of the taxtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTAXTYPE() {
        return taxtype;
    }

    /**
     * Sets the value of the taxtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTAXTYPE(String value) {
        this.taxtype = value;
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
     * Gets the value of the zonename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZONENAME() {
        return zonename;
    }

    /**
     * Sets the value of the zonename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZONENAME(String value) {
        this.zonename = value;
    }

    /**
     * Gets the value of the zonelevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZONELEVEL() {
        return zonelevel;
    }

    /**
     * Sets the value of the zonelevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZONELEVEL(String value) {
        this.zonelevel = value;
    }

    /**
     * Gets the value of the fiscalrepname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFISCALREPNAME() {
        return fiscalrepname;
    }

    /**
     * Sets the value of the fiscalrepname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFISCALREPNAME(String value) {
        this.fiscalrepname = value;
    }

    /**
     * Gets the value of the fiscalrepaddress1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFISCALREPADDRESS1() {
        return fiscalrepaddress1;
    }

    /**
     * Sets the value of the fiscalrepaddress1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFISCALREPADDRESS1(String value) {
        this.fiscalrepaddress1 = value;
    }

    /**
     * Gets the value of the fiscalrepaddress2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFISCALREPADDRESS2() {
        return fiscalrepaddress2;
    }

    /**
     * Sets the value of the fiscalrepaddress2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFISCALREPADDRESS2(String value) {
        this.fiscalrepaddress2 = value;
    }

    /**
     * Gets the value of the fiscalrepcontact property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFISCALREPCONTACT() {
        return fiscalrepcontact;
    }

    /**
     * Sets the value of the fiscalrepcontact property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFISCALREPCONTACT(String value) {
        this.fiscalrepcontact = value;
    }

    /**
     * Gets the value of the registrationattribute1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE1() {
        return registrationattribute1;
    }

    /**
     * Sets the value of the registrationattribute1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE1(String value) {
        this.registrationattribute1 = value;
    }

    /**
     * Gets the value of the registrationattribute2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE2() {
        return registrationattribute2;
    }

    /**
     * Sets the value of the registrationattribute2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE2(String value) {
        this.registrationattribute2 = value;
    }

    /**
     * Gets the value of the registrationattribute3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE3() {
        return registrationattribute3;
    }

    /**
     * Sets the value of the registrationattribute3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE3(String value) {
        this.registrationattribute3 = value;
    }

    /**
     * Gets the value of the registrationattribute4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE4() {
        return registrationattribute4;
    }

    /**
     * Sets the value of the registrationattribute4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE4(String value) {
        this.registrationattribute4 = value;
    }

    /**
     * Gets the value of the registrationattribute5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE5() {
        return registrationattribute5;
    }

    /**
     * Sets the value of the registrationattribute5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE5(String value) {
        this.registrationattribute5 = value;
    }

    /**
     * Gets the value of the registrationattribute6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE6() {
        return registrationattribute6;
    }

    /**
     * Sets the value of the registrationattribute6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE6(String value) {
        this.registrationattribute6 = value;
    }

    /**
     * Gets the value of the registrationattribute7 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE7() {
        return registrationattribute7;
    }

    /**
     * Sets the value of the registrationattribute7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE7(String value) {
        this.registrationattribute7 = value;
    }

    /**
     * Gets the value of the registrationattribute8 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE8() {
        return registrationattribute8;
    }

    /**
     * Sets the value of the registrationattribute8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE8(String value) {
        this.registrationattribute8 = value;
    }

    /**
     * Gets the value of the registrationattribute9 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE9() {
        return registrationattribute9;
    }

    /**
     * Sets the value of the registrationattribute9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE9(String value) {
        this.registrationattribute9 = value;
    }

    /**
     * Gets the value of the registrationattribute10 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE10() {
        return registrationattribute10;
    }

    /**
     * Sets the value of the registrationattribute10 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE10(String value) {
        this.registrationattribute10 = value;
    }

    /**
     * Gets the value of the registrationattribute11 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE11() {
        return registrationattribute11;
    }

    /**
     * Sets the value of the registrationattribute11 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE11(String value) {
        this.registrationattribute11 = value;
    }

    /**
     * Gets the value of the registrationattribute12 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE12() {
        return registrationattribute12;
    }

    /**
     * Sets the value of the registrationattribute12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE12(String value) {
        this.registrationattribute12 = value;
    }

    /**
     * Gets the value of the registrationattribute13 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE13() {
        return registrationattribute13;
    }

    /**
     * Sets the value of the registrationattribute13 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE13(String value) {
        this.registrationattribute13 = value;
    }

    /**
     * Gets the value of the registrationattribute14 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE14() {
        return registrationattribute14;
    }

    /**
     * Sets the value of the registrationattribute14 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE14(String value) {
        this.registrationattribute14 = value;
    }

    /**
     * Gets the value of the registrationattribute15 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE15() {
        return registrationattribute15;
    }

    /**
     * Sets the value of the registrationattribute15 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE15(String value) {
        this.registrationattribute15 = value;
    }

    /**
     * Gets the value of the registrationattribute16 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE16() {
        return registrationattribute16;
    }

    /**
     * Sets the value of the registrationattribute16 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE16(String value) {
        this.registrationattribute16 = value;
    }

    /**
     * Gets the value of the registrationattribute17 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE17() {
        return registrationattribute17;
    }

    /**
     * Sets the value of the registrationattribute17 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE17(String value) {
        this.registrationattribute17 = value;
    }

    /**
     * Gets the value of the registrationattribute18 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE18() {
        return registrationattribute18;
    }

    /**
     * Sets the value of the registrationattribute18 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE18(String value) {
        this.registrationattribute18 = value;
    }

    /**
     * Gets the value of the registrationattribute19 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE19() {
        return registrationattribute19;
    }

    /**
     * Sets the value of the registrationattribute19 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE19(String value) {
        this.registrationattribute19 = value;
    }

    /**
     * Gets the value of the registrationattribute20 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE20() {
        return registrationattribute20;
    }

    /**
     * Sets the value of the registrationattribute20 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE20(String value) {
        this.registrationattribute20 = value;
    }

    /**
     * Gets the value of the registrationattribute21 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE21() {
        return registrationattribute21;
    }

    /**
     * Sets the value of the registrationattribute21 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE21(String value) {
        this.registrationattribute21 = value;
    }

    /**
     * Gets the value of the registrationattribute22 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE22() {
        return registrationattribute22;
    }

    /**
     * Sets the value of the registrationattribute22 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE22(String value) {
        this.registrationattribute22 = value;
    }

    /**
     * Gets the value of the registrationattribute23 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE23() {
        return registrationattribute23;
    }

    /**
     * Sets the value of the registrationattribute23 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE23(String value) {
        this.registrationattribute23 = value;
    }

    /**
     * Gets the value of the registrationattribute24 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE24() {
        return registrationattribute24;
    }

    /**
     * Sets the value of the registrationattribute24 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE24(String value) {
        this.registrationattribute24 = value;
    }

    /**
     * Gets the value of the registrationattribute25 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE25() {
        return registrationattribute25;
    }

    /**
     * Sets the value of the registrationattribute25 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE25(String value) {
        this.registrationattribute25 = value;
    }

    /**
     * Gets the value of the registrationattribute26 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE26() {
        return registrationattribute26;
    }

    /**
     * Sets the value of the registrationattribute26 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE26(String value) {
        this.registrationattribute26 = value;
    }

    /**
     * Gets the value of the registrationattribute27 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE27() {
        return registrationattribute27;
    }

    /**
     * Sets the value of the registrationattribute27 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE27(String value) {
        this.registrationattribute27 = value;
    }

    /**
     * Gets the value of the registrationattribute28 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE28() {
        return registrationattribute28;
    }

    /**
     * Sets the value of the registrationattribute28 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE28(String value) {
        this.registrationattribute28 = value;
    }

    /**
     * Gets the value of the registrationattribute29 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE29() {
        return registrationattribute29;
    }

    /**
     * Sets the value of the registrationattribute29 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE29(String value) {
        this.registrationattribute29 = value;
    }

    /**
     * Gets the value of the registrationattribute30 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE30() {
        return registrationattribute30;
    }

    /**
     * Sets the value of the registrationattribute30 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE30(String value) {
        this.registrationattribute30 = value;
    }

    /**
     * Gets the value of the registrationattribute31 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE31() {
        return registrationattribute31;
    }

    /**
     * Sets the value of the registrationattribute31 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE31(String value) {
        this.registrationattribute31 = value;
    }

    /**
     * Gets the value of the registrationattribute32 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE32() {
        return registrationattribute32;
    }

    /**
     * Sets the value of the registrationattribute32 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE32(String value) {
        this.registrationattribute32 = value;
    }

    /**
     * Gets the value of the registrationattribute33 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE33() {
        return registrationattribute33;
    }

    /**
     * Sets the value of the registrationattribute33 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE33(String value) {
        this.registrationattribute33 = value;
    }

    /**
     * Gets the value of the registrationattribute34 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE34() {
        return registrationattribute34;
    }

    /**
     * Sets the value of the registrationattribute34 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE34(String value) {
        this.registrationattribute34 = value;
    }

    /**
     * Gets the value of the registrationattribute35 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE35() {
        return registrationattribute35;
    }

    /**
     * Sets the value of the registrationattribute35 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE35(String value) {
        this.registrationattribute35 = value;
    }

    /**
     * Gets the value of the registrationattribute36 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE36() {
        return registrationattribute36;
    }

    /**
     * Sets the value of the registrationattribute36 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE36(String value) {
        this.registrationattribute36 = value;
    }

    /**
     * Gets the value of the registrationattribute37 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE37() {
        return registrationattribute37;
    }

    /**
     * Sets the value of the registrationattribute37 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE37(String value) {
        this.registrationattribute37 = value;
    }

    /**
     * Gets the value of the registrationattribute38 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE38() {
        return registrationattribute38;
    }

    /**
     * Sets the value of the registrationattribute38 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE38(String value) {
        this.registrationattribute38 = value;
    }

    /**
     * Gets the value of the registrationattribute39 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE39() {
        return registrationattribute39;
    }

    /**
     * Sets the value of the registrationattribute39 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE39(String value) {
        this.registrationattribute39 = value;
    }

    /**
     * Gets the value of the registrationattribute40 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE40() {
        return registrationattribute40;
    }

    /**
     * Sets the value of the registrationattribute40 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE40(String value) {
        this.registrationattribute40 = value;
    }

    /**
     * Gets the value of the registrationattribute41 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE41() {
        return registrationattribute41;
    }

    /**
     * Sets the value of the registrationattribute41 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE41(String value) {
        this.registrationattribute41 = value;
    }

    /**
     * Gets the value of the registrationattribute42 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE42() {
        return registrationattribute42;
    }

    /**
     * Sets the value of the registrationattribute42 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE42(String value) {
        this.registrationattribute42 = value;
    }

    /**
     * Gets the value of the registrationattribute43 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE43() {
        return registrationattribute43;
    }

    /**
     * Sets the value of the registrationattribute43 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE43(String value) {
        this.registrationattribute43 = value;
    }

    /**
     * Gets the value of the registrationattribute44 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE44() {
        return registrationattribute44;
    }

    /**
     * Sets the value of the registrationattribute44 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE44(String value) {
        this.registrationattribute44 = value;
    }

    /**
     * Gets the value of the registrationattribute45 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE45() {
        return registrationattribute45;
    }

    /**
     * Sets the value of the registrationattribute45 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE45(String value) {
        this.registrationattribute45 = value;
    }

    /**
     * Gets the value of the registrationattribute46 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE46() {
        return registrationattribute46;
    }

    /**
     * Sets the value of the registrationattribute46 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE46(String value) {
        this.registrationattribute46 = value;
    }

    /**
     * Gets the value of the registrationattribute47 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE47() {
        return registrationattribute47;
    }

    /**
     * Sets the value of the registrationattribute47 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE47(String value) {
        this.registrationattribute47 = value;
    }

    /**
     * Gets the value of the registrationattribute48 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE48() {
        return registrationattribute48;
    }

    /**
     * Sets the value of the registrationattribute48 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE48(String value) {
        this.registrationattribute48 = value;
    }

    /**
     * Gets the value of the registrationattribute49 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE49() {
        return registrationattribute49;
    }

    /**
     * Sets the value of the registrationattribute49 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE49(String value) {
        this.registrationattribute49 = value;
    }

    /**
     * Gets the value of the registrationattribute50 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRATIONATTRIBUTE50() {
        return registrationattribute50;
    }

    /**
     * Sets the value of the registrationattribute50 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRATIONATTRIBUTE50(String value) {
        this.registrationattribute50 = value;
    }

    /**
     * Gets the value of the authorityattribute property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the authorityattribute property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAUTHORITYATTRIBUTE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AuthorityAttributeType }
     * 
     * 
     */
    public List<AuthorityAttributeType> getAUTHORITYATTRIBUTE() {
        if (authorityattribute == null) {
            authorityattribute = new ArrayList<AuthorityAttributeType>();
        }
        return this.authorityattribute;
    }

    /**
     * Gets the value of the taxrate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTAXRATE() {
        return taxrate;
    }

    /**
     * Sets the value of the taxrate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTAXRATE(String value) {
        this.taxrate = value;
    }

    /**
     * Gets the value of the uomconversion property.
     * 
     * @return
     *     possible object is
     *     {@link UomConversionType }
     *     
     */
    public UomConversionType getUOMCONVERSION() {
        return uomconversion;
    }

    /**
     * Sets the value of the uomconversion property.
     * 
     * @param value
     *     allowed object is
     *     {@link UomConversionType }
     *     
     */
    public void setUOMCONVERSION(UomConversionType value) {
        this.uomconversion = value;
    }

    /**
     * Gets the value of the natureoftax property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNATUREOFTAX() {
        return natureoftax;
    }

    /**
     * Sets the value of the natureoftax property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNATUREOFTAX(String value) {
        this.natureoftax = value;
    }

    /**
     * Gets the value of the eutransaction property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEUTRANSACTION() {
        return eutransaction;
    }

    /**
     * Sets the value of the eutransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEUTRANSACTION(Boolean value) {
        this.eutransaction = value;
    }

    /**
     * Gets the value of the authoritycategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAUTHORITYCATEGORY() {
        return authoritycategory;
    }

    /**
     * Sets the value of the authoritycategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAUTHORITYCATEGORY(String value) {
        this.authoritycategory = value;
    }

    /**
     * Gets the value of the authorityofficialname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAUTHORITYOFFICIALNAME() {
        return authorityofficialname;
    }

    /**
     * Sets the value of the authorityofficialname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAUTHORITYOFFICIALNAME(String value) {
        this.authorityofficialname = value;
    }

    /**
     * Gets the value of the authoritytypealias property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAUTHORITYTYPEALIAS() {
        return authoritytypealias;
    }

    /**
     * Sets the value of the authoritytypealias property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAUTHORITYTYPEALIAS(String value) {
        this.authoritytypealias = value;
    }

    /**
     * Gets the value of the authorityuuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAUTHORITYUUID() {
        return authorityuuid;
    }

    /**
     * Sets the value of the authorityuuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAUTHORITYUUID(String value) {
        this.authorityuuid = value;
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
     * Gets the value of the relatedallocationlinenumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRELATEDALLOCATIONLINENUMBER() {
        return relatedallocationlinenumber;
    }

    /**
     * Sets the value of the relatedallocationlinenumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRELATEDALLOCATIONLINENUMBER(BigDecimal value) {
        this.relatedallocationlinenumber = value;
    }

    /**
     * Gets the value of the rulereportingcategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRULEREPORTINGCATEGORY() {
        return rulereportingcategory;
    }

    /**
     * Sets the value of the rulereportingcategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRULEREPORTINGCATEGORY(String value) {
        this.rulereportingcategory = value;
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
     * Gets the value of the authoritycurrencycode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAUTHORITYCURRENCYCODE() {
        return authoritycurrencycode;
    }

    /**
     * Sets the value of the authoritycurrencycode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAUTHORITYCURRENCYCODE(String value) {
        this.authoritycurrencycode = value;
    }

    /**
     * Gets the value of the currencyconversion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the currencyconversion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCURRENCYCONVERSION().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CurrencyConversionType }
     * 
     * 
     */
    public List<CurrencyConversionType> getCURRENCYCONVERSION() {
        if (currencyconversion == null) {
            currencyconversion = new ArrayList<CurrencyConversionType>();
        }
        return this.currencyconversion;
    }

    /**
     * Gets the value of the exemptamount property.
     * 
     * @return
     *     possible object is
     *     {@link ConvertedCurrencyAmountType }
     *     
     */
    public ConvertedCurrencyAmountType getEXEMPTAMOUNT() {
        return exemptamount;
    }

    /**
     * Sets the value of the exemptamount property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConvertedCurrencyAmountType }
     *     
     */
    public void setEXEMPTAMOUNT(ConvertedCurrencyAmountType value) {
        this.exemptamount = value;
    }

    /**
     * Gets the value of the grossamount property.
     * 
     * @return
     *     possible object is
     *     {@link ConvertedCurrencyAmountType }
     *     
     */
    public ConvertedCurrencyAmountType getGROSSAMOUNT() {
        return grossamount;
    }

    /**
     * Sets the value of the grossamount property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConvertedCurrencyAmountType }
     *     
     */
    public void setGROSSAMOUNT(ConvertedCurrencyAmountType value) {
        this.grossamount = value;
    }

    /**
     * Gets the value of the nontaxablebasis property.
     * 
     * @return
     *     possible object is
     *     {@link ConvertedCurrencyAmountType }
     *     
     */
    public ConvertedCurrencyAmountType getNONTAXABLEBASIS() {
        return nontaxablebasis;
    }

    /**
     * Sets the value of the nontaxablebasis property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConvertedCurrencyAmountType }
     *     
     */
    public void setNONTAXABLEBASIS(ConvertedCurrencyAmountType value) {
        this.nontaxablebasis = value;
    }

    /**
     * Gets the value of the taxablebasis property.
     * 
     * @return
     *     possible object is
     *     {@link ConvertedCurrencyAmountType }
     *     
     */
    public ConvertedCurrencyAmountType getTAXABLEBASIS() {
        return taxablebasis;
    }

    /**
     * Sets the value of the taxablebasis property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConvertedCurrencyAmountType }
     *     
     */
    public void setTAXABLEBASIS(ConvertedCurrencyAmountType value) {
        this.taxablebasis = value;
    }

    /**
     * Gets the value of the taxamount property.
     * 
     * @return
     *     possible object is
     *     {@link ConvertedCurrencyAmountType }
     *     
     */
    public ConvertedCurrencyAmountType getTAXAMOUNT() {
        return taxamount;
    }

    /**
     * Sets the value of the taxamount property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConvertedCurrencyAmountType }
     *     
     */
    public void setTAXAMOUNT(ConvertedCurrencyAmountType value) {
        this.taxamount = value;
    }

    /**
     * Gets the value of the fee property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fee property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFEE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FeeType }
     * 
     * 
     */
    public List<FeeType> getFEE() {
        if (fee == null) {
            fee = new ArrayList<FeeType>();
        }
        return this.fee;
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
     * Gets the value of the inclusivetax property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINCLUSIVETAX() {
        return inclusivetax;
    }

    /**
     * Sets the value of the inclusivetax property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINCLUSIVETAX(String value) {
        this.inclusivetax = value;
    }

    /**
     * Gets the value of the licenses property.
     * 
     * @return
     *     possible object is
     *     {@link OutdataLicensesType }
     *     
     */
    public OutdataLicensesType getLICENSES() {
        return licenses;
    }

    /**
     * Sets the value of the licenses property.
     * 
     * @param value
     *     allowed object is
     *     {@link OutdataLicensesType }
     *     
     */
    public void setLICENSES(OutdataLicensesType value) {
        this.licenses = value;
    }

}

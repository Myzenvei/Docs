/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.dto.onesource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				
 * The following elements are contained in the Invoice and Line level address structures for the nine location types supported by ONESOURCE Indirect Tax Determination (Bill To, Buyer Primary, Middleman, Order Acceptance, Order Origin, Seller Primary, Ship From, Ship To, and Supply).
 * 
 * For all levels other than Geocode and Branch ID, you can submit either a full name or a valid ISO, 2 Character, or 3 Character code that is associated with a valid Zone name in ONESOURCE Indirect Tax Determination. These codes can be viewed on the Tax Control Panel's Zones Edit page for the associated Zone.
 * 
 * For Geocode and Branch ID you must submit the full name.
 * 				
 * 			
 * 
 * <p>Java class for ZoneAddressType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZoneAddressType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CommonAddressType">
 *       &lt;sequence>
 *         &lt;element name="COMPANY_BRANCH_ID" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AddressCompanyBranchIDType" minOccurs="0"/>
 *         &lt;element name="IS_BONDED" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AddressIsBondedType" minOccurs="0"/>
 *         &lt;element name="LOCATION_TAX_CATEGORY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AddressLocationTaxCategoryType" minOccurs="0"/>
 *         &lt;element name="ADDRESS_1" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AddressAddress1Type" minOccurs="0"/>
 *         &lt;element name="ADDRESS_2" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AddressAddress2Type" minOccurs="0"/>
 *         &lt;element name="ADDRESS_3" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AddressAddress3Type" minOccurs="0"/>
 *         &lt;element name="ADDRESS_VALIDATION_MODE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AddressAddressValidationModeType" minOccurs="0"/>
 *         &lt;element name="DEFAULT_ADDRESS_VALIDATION_MODE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AddressDefaultAddressValidationModeType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZoneAddressType", propOrder = {
    "companybranchid",
    "isbonded",
    "locationtaxcategory",
    "address1",
    "address2",
    "address3",
    "addressvalidationmode",
    "defaultaddressvalidationmode"
})
public class ZoneAddressType
    extends CommonAddressType
{

    @XmlElement(name = "COMPANY_BRANCH_ID")
    protected String companybranchid;
    @XmlElement(name = "IS_BONDED")
    protected String isbonded;
    @XmlElement(name = "LOCATION_TAX_CATEGORY")
    protected String locationtaxcategory;
    @XmlElement(name = "ADDRESS_1")
    protected String address1;
    @XmlElement(name = "ADDRESS_2")
    protected String address2;
    @XmlElement(name = "ADDRESS_3")
    protected String address3;
    @XmlElement(name = "ADDRESS_VALIDATION_MODE")
    protected String addressvalidationmode;
    @XmlElement(name = "DEFAULT_ADDRESS_VALIDATION_MODE")
    protected String defaultaddressvalidationmode;

    /**
     * Gets the value of the companybranchid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPANYBRANCHID() {
        return companybranchid;
    }

    /**
     * Sets the value of the companybranchid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPANYBRANCHID(String value) {
        this.companybranchid = value;
    }

    /**
     * Gets the value of the isbonded property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISBONDED() {
        return isbonded;
    }

    /**
     * Sets the value of the isbonded property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISBONDED(String value) {
        this.isbonded = value;
    }

    /**
     * Gets the value of the locationtaxcategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLOCATIONTAXCATEGORY() {
        return locationtaxcategory;
    }

    /**
     * Sets the value of the locationtaxcategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLOCATIONTAXCATEGORY(String value) {
        this.locationtaxcategory = value;
    }

    /**
     * Gets the value of the address1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getADDRESS1() {
        return address1;
    }

    /**
     * Sets the value of the address1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setADDRESS1(String value) {
        this.address1 = value;
    }

    /**
     * Gets the value of the address2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getADDRESS2() {
        return address2;
    }

    /**
     * Sets the value of the address2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setADDRESS2(String value) {
        this.address2 = value;
    }

    /**
     * Gets the value of the address3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getADDRESS3() {
        return address3;
    }

    /**
     * Sets the value of the address3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setADDRESS3(String value) {
        this.address3 = value;
    }

    /**
     * Gets the value of the addressvalidationmode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getADDRESSVALIDATIONMODE() {
        return addressvalidationmode;
    }

    /**
     * Sets the value of the addressvalidationmode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setADDRESSVALIDATIONMODE(String value) {
        this.addressvalidationmode = value;
    }

    /**
     * Gets the value of the defaultaddressvalidationmode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDEFAULTADDRESSVALIDATIONMODE() {
        return defaultaddressvalidationmode;
    }

    /**
     * Sets the value of the defaultaddressvalidationmode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDEFAULTADDRESSVALIDATIONMODE(String value) {
        this.defaultaddressvalidationmode = value;
    }

}

/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.dto.onesource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import com.gp.commerce.core.dto.onesource.ZoneAddressType;


/**
 * <p>Java class for CommonAddressType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CommonAddressType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="COUNTRY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AddressCountryType" minOccurs="0"/>
 *         &lt;element name="PROVINCE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AddressProvinceType" minOccurs="0"/>
 *         &lt;element name="STATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AddressStateType" minOccurs="0"/>
 *         &lt;element name="COUNTY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AddressCountyType" minOccurs="0"/>
 *         &lt;element name="CITY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AddressCityType" minOccurs="0"/>
 *         &lt;element name="DISTRICT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AddressDistrictType" minOccurs="0"/>
 *         &lt;element name="POSTCODE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AddressPostCodeType" minOccurs="0"/>
 *         &lt;element name="GEOCODE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}AddressGEOCodeType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CommonAddressType", propOrder = {
    "country",
    "province",
    "state",
    "county",
    "city",
    "district",
    "postcode",
    "geocode"
})
@XmlSeeAlso({
	CommonAddressType.class,
    ZoneAddressType.class
})
public class CommonAddressType {

    @XmlElement(name = "COUNTRY")
    protected String country;
    @XmlElement(name = "PROVINCE")
    protected String province;
    @XmlElement(name = "STATE")
    protected String state;
    @XmlElement(name = "COUNTY")
    protected String county;
    @XmlElement(name = "CITY")
    protected String city;
    @XmlElement(name = "DISTRICT")
    protected String district;
    @XmlElement(name = "POSTCODE")
    protected String postcode;
    @XmlElement(name = "GEOCODE")
    protected String geocode;

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOUNTRY() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOUNTRY(String value) {
        this.country = value;
    }

    /**
     * Gets the value of the province property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPROVINCE() {
        return province;
    }

    /**
     * Sets the value of the province property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPROVINCE(String value) {
        this.province = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTATE() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTATE(String value) {
        this.state = value;
    }

    /**
     * Gets the value of the county property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOUNTY() {
        return county;
    }

    /**
     * Sets the value of the county property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOUNTY(String value) {
        this.county = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCITY() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCITY(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the district property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDISTRICT() {
        return district;
    }

    /**
     * Sets the value of the district property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDISTRICT(String value) {
        this.district = value;
    }

    /**
     * Gets the value of the postcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOSTCODE() {
        return postcode;
    }

    /**
     * Sets the value of the postcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOSTCODE(String value) {
        this.postcode = value;
    }

    /**
     * Gets the value of the geocode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGEOCODE() {
        return geocode;
    }

    /**
     * Sets the value of the geocode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGEOCODE(String value) {
        this.geocode = value;
    }

}

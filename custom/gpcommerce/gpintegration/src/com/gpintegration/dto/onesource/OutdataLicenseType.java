/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.dto.onesource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OutdataLicenseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutdataLicenseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LICENSE_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LICENSE_TYPE_NAME" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LICENSE_END_DATE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LICENSE_CATEGORY" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LICENSE_EXTERNAL_IDENTIFIER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutdataLicenseType", propOrder = {
    "licensenumber",
    "licensetypename",
    "licenseenddate",
    "licensecategory",
    "licenseexternalidentifier"
})
public class OutdataLicenseType {

    @XmlElement(name = "LICENSE_NUMBER", required = true)
    protected String licensenumber;
    @XmlElement(name = "LICENSE_TYPE_NAME", required = true)
    protected String licensetypename;
    @XmlElement(name = "LICENSE_END_DATE")
    protected String licenseenddate;
    @XmlElement(name = "LICENSE_CATEGORY", required = true)
    protected String licensecategory;
    @XmlElement(name = "LICENSE_EXTERNAL_IDENTIFIER")
    protected String licenseexternalidentifier;

    /**
     * Gets the value of the licensenumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLICENSENUMBER() {
        return licensenumber;
    }

    /**
     * Sets the value of the licensenumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLICENSENUMBER(String value) {
        this.licensenumber = value;
    }

    /**
     * Gets the value of the licensetypename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLICENSETYPENAME() {
        return licensetypename;
    }

    /**
     * Sets the value of the licensetypename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLICENSETYPENAME(String value) {
        this.licensetypename = value;
    }

    /**
     * Gets the value of the licenseenddate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLICENSEENDDATE() {
        return licenseenddate;
    }

    /**
     * Sets the value of the licenseenddate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLICENSEENDDATE(String value) {
        this.licenseenddate = value;
    }

    /**
     * Gets the value of the licensecategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLICENSECATEGORY() {
        return licensecategory;
    }

    /**
     * Sets the value of the licensecategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLICENSECATEGORY(String value) {
        this.licensecategory = value;
    }

    /**
     * Gets the value of the licenseexternalidentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLICENSEEXTERNALIDENTIFIER() {
        return licenseexternalidentifier;
    }

    /**
     * Sets the value of the licenseexternalidentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLICENSEEXTERNALIDENTIFIER(String value) {
        this.licenseexternalidentifier = value;
    }

}

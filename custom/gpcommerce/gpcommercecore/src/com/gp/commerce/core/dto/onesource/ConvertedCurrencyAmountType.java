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
 * <p>Java class for ConvertedCurrencyAmountType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConvertedCurrencyAmountType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DOCUMENT_AMOUNT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}NillableDecimalType" minOccurs="0"/>
 *         &lt;element name="UNROUNDED_DOCUMENT_AMOUNT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}NillableDecimalType" minOccurs="0"/>
 *         &lt;element name="AUTHORITY_AMOUNT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}NillableDecimalType"/>
 *         &lt;element name="UNROUNDED_AUTHORITY_AMOUNT" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}NillableDecimalType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConvertedCurrencyAmountType", propOrder = {
    "documentamount",
    "unroundeddocumentamount",
    "authorityamount",
    "unroundedauthorityamount"
})
public class ConvertedCurrencyAmountType {

    @XmlElement(name = "DOCUMENT_AMOUNT")
    protected String documentamount;
    @XmlElement(name = "UNROUNDED_DOCUMENT_AMOUNT")
    protected String unroundeddocumentamount;
    @XmlElement(name = "AUTHORITY_AMOUNT", required = true)
    protected String authorityamount;
    @XmlElement(name = "UNROUNDED_AUTHORITY_AMOUNT", required = true)
    protected String unroundedauthorityamount;

    /**
     * Gets the value of the documentamount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDOCUMENTAMOUNT() {
        return documentamount;
    }

    /**
     * Sets the value of the documentamount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDOCUMENTAMOUNT(String value) {
        this.documentamount = value;
    }

    /**
     * Gets the value of the unroundeddocumentamount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUNROUNDEDDOCUMENTAMOUNT() {
        return unroundeddocumentamount;
    }

    /**
     * Sets the value of the unroundeddocumentamount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUNROUNDEDDOCUMENTAMOUNT(String value) {
        this.unroundeddocumentamount = value;
    }

    /**
     * Gets the value of the authorityamount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAUTHORITYAMOUNT() {
        return authorityamount;
    }

    /**
     * Sets the value of the authorityamount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAUTHORITYAMOUNT(String value) {
        this.authorityamount = value;
    }

    /**
     * Gets the value of the unroundedauthorityamount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUNROUNDEDAUTHORITYAMOUNT() {
        return unroundedauthorityamount;
    }

    /**
     * Sets the value of the unroundedauthorityamount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUNROUNDEDAUTHORITYAMOUNT(String value) {
        this.unroundedauthorityamount = value;
    }

}

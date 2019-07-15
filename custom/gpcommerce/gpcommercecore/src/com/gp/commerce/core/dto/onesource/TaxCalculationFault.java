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
 * <p>Java class for TaxCalculationFault complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TaxCalculationFault">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="additionalMessage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="errorLocation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="errorSource" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaxCalculationFault", propOrder = {
    "additionalMessage",
    "errorLocation",
    "errorSource"
})
public class TaxCalculationFault {

    @XmlElement(required = true)
    protected String additionalMessage;
    @XmlElement(required = true)
    protected String errorLocation;
    @XmlElement(required = true)
    protected String errorSource;

    /**
     * Gets the value of the additionalMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalMessage() {
        return additionalMessage;
    }

    /**
     * Sets the value of the additionalMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalMessage(String value) {
        this.additionalMessage = value;
    }

    /**
     * Gets the value of the errorLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorLocation() {
        return errorLocation;
    }

    /**
     * Sets the value of the errorLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorLocation(String value) {
        this.errorLocation = value;
    }

    /**
     * Gets the value of the errorSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorSource() {
        return errorSource;
    }

    /**
     * Sets the value of the errorSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorSource(String value) {
        this.errorSource = value;
    }

}

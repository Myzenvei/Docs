/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.dto.onesource;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CurrencyConversionStepType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CurrencyConversionStepType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CONVERSION_STEP" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="FROM_CURRENCY_CODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TO_CURRENCY_CODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EXCHANGE_RATE" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CurrencyConversionStepType", propOrder = {
    "conversionstep",
    "fromcurrencycode",
    "tocurrencycode",
    "exchangerate"
})
public class CurrencyConversionStepType {

    @XmlElement(name = "CONVERSION_STEP", required = true)
    protected BigDecimal conversionstep;
    @XmlElement(name = "FROM_CURRENCY_CODE", required = true)
    protected String fromcurrencycode;
    @XmlElement(name = "TO_CURRENCY_CODE", required = true)
    protected String tocurrencycode;
    @XmlElement(name = "EXCHANGE_RATE", required = true)
    protected BigDecimal exchangerate;

    /**
     * Gets the value of the conversionstep property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCONVERSIONSTEP() {
        return conversionstep;
    }

    /**
     * Sets the value of the conversionstep property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCONVERSIONSTEP(BigDecimal value) {
        this.conversionstep = value;
    }

    /**
     * Gets the value of the fromcurrencycode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFROMCURRENCYCODE() {
        return fromcurrencycode;
    }

    /**
     * Sets the value of the fromcurrencycode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFROMCURRENCYCODE(String value) {
        this.fromcurrencycode = value;
    }

    /**
     * Gets the value of the tocurrencycode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTOCURRENCYCODE() {
        return tocurrencycode;
    }

    /**
     * Sets the value of the tocurrencycode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTOCURRENCYCODE(String value) {
        this.tocurrencycode = value;
    }

    /**
     * Gets the value of the exchangerate property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEXCHANGERATE() {
        return exchangerate;
    }

    /**
     * Sets the value of the exchangerate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEXCHANGERATE(BigDecimal value) {
        this.exchangerate = value;
    }

}

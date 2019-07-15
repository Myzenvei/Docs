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
 * <p>Java class for UomConversionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UomConversionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FACTOR" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}NillableDecimalType"/>
 *         &lt;element name="OPERATOR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FROM" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}QuantityType"/>
 *         &lt;element name="TO_ROUNDED" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}QuantityType"/>
 *         &lt;element name="TO_UNROUNDED" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}QuantityType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UomConversionType", propOrder = {
    "factor",
    "operator",
    "from",
    "torounded",
    "tounrounded"
})
public class UomConversionType {

    @XmlElement(name = "FACTOR", required = true)
    protected String factor;
    @XmlElement(name = "OPERATOR", required = true)
    protected String operator;
    @XmlElement(name = "FROM", required = true)
    protected QuantityType from;
    @XmlElement(name = "TO_ROUNDED", required = true)
    protected QuantityType torounded;
    @XmlElement(name = "TO_UNROUNDED", required = true)
    protected QuantityType tounrounded;

    /**
     * Gets the value of the factor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFACTOR() {
        return factor;
    }

    /**
     * Sets the value of the factor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFACTOR(String value) {
        this.factor = value;
    }

    /**
     * Gets the value of the operator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOPERATOR() {
        return operator;
    }

    /**
     * Sets the value of the operator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOPERATOR(String value) {
        this.operator = value;
    }

    /**
     * Gets the value of the from property.
     * 
     * @return
     *     possible object is
     *     {@link QuantityType }
     *     
     */
    public QuantityType getFROM() {
        return from;
    }

    /**
     * Sets the value of the from property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuantityType }
     *     
     */
    public void setFROM(QuantityType value) {
        this.from = value;
    }

    /**
     * Gets the value of the torounded property.
     * 
     * @return
     *     possible object is
     *     {@link QuantityType }
     *     
     */
    public QuantityType getTOROUNDED() {
        return torounded;
    }

    /**
     * Sets the value of the torounded property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuantityType }
     *     
     */
    public void setTOROUNDED(QuantityType value) {
        this.torounded = value;
    }

    /**
     * Gets the value of the tounrounded property.
     * 
     * @return
     *     possible object is
     *     {@link QuantityType }
     *     
     */
    public QuantityType getTOUNROUNDED() {
        return tounrounded;
    }

    /**
     * Sets the value of the tounrounded property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuantityType }
     *     
     */
    public void setTOUNROUNDED(QuantityType value) {
        this.tounrounded = value;
    }

}

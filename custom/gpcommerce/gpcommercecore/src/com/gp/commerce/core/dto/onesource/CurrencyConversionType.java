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
 * <p>Java class for CurrencyConversionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CurrencyConversionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TAX_EXCHANGE_RATE_DATE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}TaxExchangeRateDateType"/>
 *         &lt;element name="EXCHANGE_RATE_SOURCE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CONVERSION_STEPS" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CurrencyConversionStepType" maxOccurs="2" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CurrencyConversionType", propOrder = {
    "taxexchangeratedate",
    "exchangeratesource",
    "conversionsteps"
})
public class CurrencyConversionType {

    @XmlElement(name = "TAX_EXCHANGE_RATE_DATE", required = true)
    protected String taxexchangeratedate;
    @XmlElement(name = "EXCHANGE_RATE_SOURCE")
    protected String exchangeratesource;
    @XmlElement(name = "CONVERSION_STEPS")
    protected List<CurrencyConversionStepType> conversionsteps;

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
     * Gets the value of the exchangeratesource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEXCHANGERATESOURCE() {
        return exchangeratesource;
    }

    /**
     * Sets the value of the exchangeratesource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEXCHANGERATESOURCE(String value) {
        this.exchangeratesource = value;
    }

    /**
     * Gets the value of the conversionsteps property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the conversionsteps property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCONVERSIONSTEPS().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CurrencyConversionStepType }
     * 
     * 
     */
    public List<CurrencyConversionStepType> getCONVERSIONSTEPS() {
        if (conversionsteps == null) {
            conversionsteps = new ArrayList<CurrencyConversionStepType>();
        }
        return this.conversionsteps;
    }

}

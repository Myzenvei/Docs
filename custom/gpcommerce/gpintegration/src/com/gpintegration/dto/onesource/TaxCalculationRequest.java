/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.dto.onesource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.gp.commerce.core.dto.onesource.IndataType;


/**
 * 
 * 				
 * Top level request for a tax calculation call.
 *             	
 * 			
 * 
 * <p>Java class for TaxCalculationRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TaxCalculationRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="INDATA" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IndataType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaxCalculationRequest", propOrder = {
    "indata"
})
public class TaxCalculationRequest {

    @XmlElement(name = "INDATA", required = true)
    protected IndataType indata;

    /**
     * Gets the value of the indata property.
     * 
     * @return
     *     possible object is
     *     {@link IndataType }
     *     
     */
    public IndataType getINDATA() {
        return indata;
    }

    /**
     * Sets the value of the indata property.
     * 
     * @param value
     *     allowed object is
     *     {@link IndataType }
     *     
     */
    public void setINDATA(IndataType value) {
        this.indata = value;
    }

}

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
 * The top level response to a request for tax calculation.				
 * 				
 * 			
 * 
 * <p>Java class for TaxCalculationResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TaxCalculationResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OUTDATA" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OutdataType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaxCalculationResponse", propOrder = {
    "outdata"
})
public class TaxCalculationResponse {

    @XmlElement(name = "OUTDATA", required = true)
    protected OutdataType outdata;

    /**
     * Gets the value of the outdata property.
     * 
     * @return
     *     possible object is
     *     {@link OutdataType }
     *     
     */
    public OutdataType getOUTDATA() {
        return outdata;
    }

    /**
     * Sets the value of the outdata property.
     * 
     * @param value
     *     allowed object is
     *     {@link OutdataType }
     *     
     */
    public void setOUTDATA(OutdataType value) {
        this.outdata = value;
    }

	@Override
	public String toString() {
		return "TaxCalculationResponse [outdata=" + outdata + "]";
	}
    
    

}

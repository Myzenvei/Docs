/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.dto.onesource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.gp.commerce.core.dto.onesource.EstablishmentsLocationType;


/**
 * 
 * 				
 * List of established locations.				
 * 				
 * 			
 * 
 * <p>Java class for EstablishmentsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EstablishmentsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="BUYER_ROLE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}EstablishmentsLocationType" minOccurs="0"/>
 *         &lt;element name="MIDDLEMAN_ROLE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}EstablishmentsLocationType" minOccurs="0"/>
 *         &lt;element name="SELLER_ROLE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}EstablishmentsLocationType" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EstablishmentsType", propOrder = {

})
public class EstablishmentsType {

    @XmlElement(name = "BUYER_ROLE")
    protected EstablishmentsLocationType buyerrole;
    @XmlElement(name = "MIDDLEMAN_ROLE")
    protected EstablishmentsLocationType middlemanrole;
    @XmlElement(name = "SELLER_ROLE")
    protected EstablishmentsLocationType sellerrole;

    /**
     * Gets the value of the buyerrole property.
     * 
     * @return
     *     possible object is
     *     {@link EstablishmentsLocationType }
     *     
     */
    public EstablishmentsLocationType getBUYERROLE() {
        return buyerrole;
    }

    /**
     * Sets the value of the buyerrole property.
     * 
     * @param value
     *     allowed object is
     *     {@link EstablishmentsLocationType }
     *     
     */
    public void setBUYERROLE(EstablishmentsLocationType value) {
        this.buyerrole = value;
    }

    /**
     * Gets the value of the middlemanrole property.
     * 
     * @return
     *     possible object is
     *     {@link EstablishmentsLocationType }
     *     
     */
    public EstablishmentsLocationType getMIDDLEMANROLE() {
        return middlemanrole;
    }

    /**
     * Sets the value of the middlemanrole property.
     * 
     * @param value
     *     allowed object is
     *     {@link EstablishmentsLocationType }
     *     
     */
    public void setMIDDLEMANROLE(EstablishmentsLocationType value) {
        this.middlemanrole = value;
    }

    /**
     * Gets the value of the sellerrole property.
     * 
     * @return
     *     possible object is
     *     {@link EstablishmentsLocationType }
     *     
     */
    public EstablishmentsLocationType getSELLERROLE() {
        return sellerrole;
    }

    /**
     * Sets the value of the sellerrole property.
     * 
     * @param value
     *     allowed object is
     *     {@link EstablishmentsLocationType }
     *     
     */
    public void setSELLERROLE(EstablishmentsLocationType value) {
        this.sellerrole = value;
    }

}

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
 * 
 * 				
 * Establishment for each location type (true or false). 
 * 				
 * 			
 * 
 * <p>Java class for EstablishmentsLocationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EstablishmentsLocationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="BILL_TO" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}BooleanType" minOccurs="0"/>
 *         &lt;element name="BUYER_PRIMARY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}BooleanType" minOccurs="0"/>
 *         &lt;element name="MIDDLEMAN" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}BooleanType" minOccurs="0"/>
 *         &lt;element name="ORDER_ACCEPTANCE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}BooleanType" minOccurs="0"/>
 *         &lt;element name="ORDER_ORIGIN" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}BooleanType" minOccurs="0"/>
 *         &lt;element name="SELLER_PRIMARY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}BooleanType" minOccurs="0"/>
 *         &lt;element name="SHIP_FROM" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}BooleanType" minOccurs="0"/>
 *         &lt;element name="SHIP_TO" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}BooleanType" minOccurs="0"/>
 *         &lt;element name="SUPPLY" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}BooleanType" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EstablishmentsLocationType", propOrder = {

})
public class EstablishmentsLocationType {

    @XmlElement(name = "BILL_TO")
    protected String billto;
    @XmlElement(name = "BUYER_PRIMARY")
    protected String buyerprimary;
    @XmlElement(name = "MIDDLEMAN")
    protected String middleman;
    @XmlElement(name = "ORDER_ACCEPTANCE")
    protected String orderacceptance;
    @XmlElement(name = "ORDER_ORIGIN")
    protected String orderorigin;
    @XmlElement(name = "SELLER_PRIMARY")
    protected String sellerprimary;
    @XmlElement(name = "SHIP_FROM")
    protected String shipfrom;
    @XmlElement(name = "SHIP_TO")
    protected String shipto;
    @XmlElement(name = "SUPPLY")
    protected String supply;

    /**
     * Gets the value of the billto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBILLTO() {
        return billto;
    }

    /**
     * Sets the value of the billto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBILLTO(String value) {
        this.billto = value;
    }

    /**
     * Gets the value of the buyerprimary property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBUYERPRIMARY() {
        return buyerprimary;
    }

    /**
     * Sets the value of the buyerprimary property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBUYERPRIMARY(String value) {
        this.buyerprimary = value;
    }

    /**
     * Gets the value of the middleman property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMIDDLEMAN() {
        return middleman;
    }

    /**
     * Sets the value of the middleman property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMIDDLEMAN(String value) {
        this.middleman = value;
    }

    /**
     * Gets the value of the orderacceptance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDERACCEPTANCE() {
        return orderacceptance;
    }

    /**
     * Sets the value of the orderacceptance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDERACCEPTANCE(String value) {
        this.orderacceptance = value;
    }

    /**
     * Gets the value of the orderorigin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDERORIGIN() {
        return orderorigin;
    }

    /**
     * Sets the value of the orderorigin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDERORIGIN(String value) {
        this.orderorigin = value;
    }

    /**
     * Gets the value of the sellerprimary property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSELLERPRIMARY() {
        return sellerprimary;
    }

    /**
     * Sets the value of the sellerprimary property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSELLERPRIMARY(String value) {
        this.sellerprimary = value;
    }

    /**
     * Gets the value of the shipfrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSHIPFROM() {
        return shipfrom;
    }

    /**
     * Sets the value of the shipfrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSHIPFROM(String value) {
        this.shipfrom = value;
    }

    /**
     * Gets the value of the shipto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSHIPTO() {
        return shipto;
    }

    /**
     * Sets the value of the shipto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSHIPTO(String value) {
        this.shipto = value;
    }

    /**
     * Gets the value of the supply property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUPPLY() {
        return supply;
    }

    /**
     * Sets the value of the supply property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUPPLY(String value) {
        this.supply = value;
    }

}

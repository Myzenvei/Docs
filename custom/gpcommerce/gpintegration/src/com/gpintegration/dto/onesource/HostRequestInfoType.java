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
 * Contains pass-through elements used to identify the originating request from the ERP source.
 * 				
 * 			
 * 
 * <p>Java class for HostRequestInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HostRequestInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="HOST_REQUEST_ID" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}HostRequestIdType" minOccurs="0"/>
 *         &lt;element name="HOST_REQUEST_LOG_ENTRY_ID" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}HostRequestLogEntryIdType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HostRequestInfoType", propOrder = {
    "hostrequestid",
    "hostrequestlogentryid"
})
public class HostRequestInfoType {

    @XmlElement(name = "HOST_REQUEST_ID")
    protected String hostrequestid;
    @XmlElement(name = "HOST_REQUEST_LOG_ENTRY_ID")
    protected String hostrequestlogentryid;

    /**
     * Gets the value of the hostrequestid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHOSTREQUESTID() {
        return hostrequestid;
    }

    /**
     * Sets the value of the hostrequestid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHOSTREQUESTID(String value) {
        this.hostrequestid = value;
    }

    /**
     * Gets the value of the hostrequestlogentryid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHOSTREQUESTLOGENTRYID() {
        return hostrequestlogentryid;
    }

    /**
     * Sets the value of the hostrequestlogentryid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHOSTREQUESTLOGENTRYID(String value) {
        this.hostrequestlogentryid = value;
    }

}

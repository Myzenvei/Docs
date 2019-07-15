/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.quples;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ContactsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContactsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ContactKey" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MKT_Permission" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContactsResponse", propOrder = {
    "contactKey",
    "mktPermission"
})
public class ContactsResponse {

    @XmlElement(name = "ContactKey", required = true)
    protected String contactKey;
    @XmlElement(name = "MKT_Permission", required = true)
    protected String mktPermission;

    /**
     * Gets the value of the contactKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContactKey() {
        return contactKey;
    }

    /**
     * Sets the value of the contactKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContactKey(String value) {
        this.contactKey = value;
    }

    /**
     * Gets the value of the mktPermission property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMKTPermission() {
        return mktPermission;
    }

    /**
     * Sets the value of the mktPermission property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMKTPermission(String value) {
        this.mktPermission = value;
    }

}

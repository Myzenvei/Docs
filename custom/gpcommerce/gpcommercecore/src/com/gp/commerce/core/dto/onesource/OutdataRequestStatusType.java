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
 * 
 * 				
 * A structure which contains a summary of the status of the entire request, included aggregation of severe errors.				
 * 										
 * 			
 * 
 * <p>Java class for OutdataRequestStatusType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutdataRequestStatusType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IS_SUCCESS" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="IS_PARTIAL_SUCCESS" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="ERROR" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OutdataErrorType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutdataRequestStatusType", propOrder = {
    "issuccess",
    "ispartialsuccess",
    "error"
})
public class OutdataRequestStatusType {

    @XmlElement(name = "IS_SUCCESS")
    protected boolean issuccess;
    @XmlElement(name = "IS_PARTIAL_SUCCESS")
    protected boolean ispartialsuccess;
    @XmlElement(name = "ERROR")
    protected List<OutdataErrorType> error;

    /**
     * Gets the value of the issuccess property.
     * 
     */
    public boolean isISSUCCESS() {
        return issuccess;
    }

    /**
     * Sets the value of the issuccess property.
     * 
     */
    public void setISSUCCESS(boolean value) {
        this.issuccess = value;
    }

    /**
     * Gets the value of the ispartialsuccess property.
     * 
     */
    public boolean isISPARTIALSUCCESS() {
        return ispartialsuccess;
    }

    /**
     * Sets the value of the ispartialsuccess property.
     * 
     */
    public void setISPARTIALSUCCESS(boolean value) {
        this.ispartialsuccess = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the error property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getERROR().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OutdataErrorType }
     * 
     * 
     */
    public List<OutdataErrorType> getERROR() {
        if (error == null) {
            error = new ArrayList<OutdataErrorType>();
        }
        return this.error;
    }

}

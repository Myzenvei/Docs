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
 * Contains data for one or more licenses applying to the document.				
 * 				
 * 			
 * 
 * <p>Java class for IndataLicensesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IndataLicensesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CUSTOMER_LICENSE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IndataLicensesDetailType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndataLicensesType", propOrder = {
    "customerlicense"
})
public class IndataLicensesType {

    @XmlElement(name = "CUSTOMER_LICENSE")
    protected List<IndataLicensesDetailType> customerlicense;

    /**
     * Gets the value of the customerlicense property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customerlicense property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCUSTOMERLICENSE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IndataLicensesDetailType }
     * 
     * 
     */
    public List<IndataLicensesDetailType> getCUSTOMERLICENSE() {
        if (customerlicense == null) {
            customerlicense = new ArrayList<IndataLicensesDetailType>();
        }
        return this.customerlicense;
    }

}

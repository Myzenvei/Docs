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
 * Contains information on whether to treat some or all authority taxes for this invoice as inclusive, regardless of the selected rule. 
 * 				
 * 			
 * 
 * <p>Java class for InclusiveTaxIndicatorsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InclusiveTaxIndicatorsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FULLY_INCLUSIVE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}BooleanType" minOccurs="0"/>
 *         &lt;element name="AUTHORITY_TYPE" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InclusiveTaxIndicatorsType", propOrder = {
    "fullyinclusive",
    "authoritytype"
})
public class InclusiveTaxIndicatorsType {

    @XmlElement(name = "FULLY_INCLUSIVE")
    protected String fullyinclusive;
    @XmlElement(name = "AUTHORITY_TYPE")
    protected List<String> authoritytype;

    /**
     * Gets the value of the fullyinclusive property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFULLYINCLUSIVE() {
        return fullyinclusive;
    }

    /**
     * Sets the value of the fullyinclusive property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFULLYINCLUSIVE(String value) {
        this.fullyinclusive = value;
    }

    /**
     * Gets the value of the authoritytype property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the authoritytype property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAUTHORITYTYPE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAUTHORITYTYPE() {
        if (authoritytype == null) {
            authoritytype = new ArrayList<String>();
        }
        return this.authoritytype;
    }

}

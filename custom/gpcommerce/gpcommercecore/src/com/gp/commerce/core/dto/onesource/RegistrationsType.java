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
 * List of registrations by role.				
 * 				
 * 			
 * 
 * <p>Java class for RegistrationsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RegistrationsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BUYER_ROLE" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="MIDDLEMAN_ROLE" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="SELLER_ROLE" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegistrationsType", propOrder = {
    "buyerrole",
    "middlemanrole",
    "sellerrole"
})
public class RegistrationsType {

    @XmlElement(name = "BUYER_ROLE")
    protected List<String> buyerrole;
    @XmlElement(name = "MIDDLEMAN_ROLE")
    protected List<String> middlemanrole;
    @XmlElement(name = "SELLER_ROLE")
    protected List<String> sellerrole;

    /**
     * Gets the value of the buyerrole property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the buyerrole property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBUYERROLE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getBUYERROLE() {
        if (buyerrole == null) {
            buyerrole = new ArrayList<String>();
        }
        return this.buyerrole;
    }

    /**
     * Gets the value of the middlemanrole property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the middlemanrole property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMIDDLEMANROLE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getMIDDLEMANROLE() {
        if (middlemanrole == null) {
            middlemanrole = new ArrayList<String>();
        }
        return this.middlemanrole;
    }

    /**
     * Gets the value of the sellerrole property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sellerrole property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSELLERROLE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSELLERROLE() {
        if (sellerrole == null) {
            sellerrole = new ArrayList<String>();
        }
        return this.sellerrole;
    }

}

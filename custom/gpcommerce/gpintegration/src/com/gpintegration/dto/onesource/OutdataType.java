/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.dto.onesource;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.gp.commerce.core.dto.onesource.OutdataInvoiceType;
import com.gp.commerce.core.dto.onesource.OutdataRequestStatusType;
import com.gp.commerce.core.dto.onesource.VersionType;


/**
 * <p>Java class for OutdataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutdataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="REQUEST_STATUS" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OutdataRequestStatusType" minOccurs="0"/>
 *         &lt;element name="COMPANY_ID" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CompanyIdType" minOccurs="0"/>
 *         &lt;element name="COMPANY_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CompanyNameType" minOccurs="0"/>
 *         &lt;element name="COMPANY_ROLE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CompanyRoleType" minOccurs="0"/>
 *         &lt;element name="EXTERNAL_COMPANY_ID" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ExternalCompanyIdType" minOccurs="0"/>
 *         &lt;element name="SCENARIO_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ScenarioNameType" minOccurs="0"/>
 *         &lt;element name="INVOICE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}OutdataInvoiceType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}VersionType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutdataType", propOrder = {
    "requeststatus",
    "companyid",
    "companyname",
    "companyrole",
    "externalcompanyid",
    "scenarioname",
    "invoice"
})
public class OutdataType {

    @XmlElement(name = "REQUEST_STATUS")
    protected OutdataRequestStatusType requeststatus;
    @XmlElement(name = "COMPANY_ID")
    protected Long companyid;
    @XmlElement(name = "COMPANY_NAME")
    protected String companyname;
    @XmlElement(name = "COMPANY_ROLE")
    protected String companyrole;
    @XmlElement(name = "EXTERNAL_COMPANY_ID")
    protected String externalcompanyid;
    @XmlElement(name = "SCENARIO_NAME")
    protected String scenarioname;
    @XmlElement(name = "INVOICE", required = true)
    protected List<OutdataInvoiceType> invoice;
    @XmlAttribute(name = "version")
    protected VersionType version;

    /**
     * Gets the value of the requeststatus property.
     * 
     * @return
     *     possible object is
     *     {@link OutdataRequestStatusType }
     *     
     */
    public OutdataRequestStatusType getREQUESTSTATUS() {
        return requeststatus;
    }

    /**
     * Sets the value of the requeststatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link OutdataRequestStatusType }
     *     
     */
    public void setREQUESTSTATUS(OutdataRequestStatusType value) {
        this.requeststatus = value;
    }

    /**
     * Gets the value of the companyid property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCOMPANYID() {
        return companyid;
    }

    /**
     * Sets the value of the companyid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCOMPANYID(Long value) {
        this.companyid = value;
    }

    /**
     * Gets the value of the companyname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPANYNAME() {
        return companyname;
    }

    /**
     * Sets the value of the companyname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPANYNAME(String value) {
        this.companyname = value;
    }

    /**
     * Gets the value of the companyrole property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPANYROLE() {
        return companyrole;
    }

    /**
     * Sets the value of the companyrole property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPANYROLE(String value) {
        this.companyrole = value;
    }

    /**
     * Gets the value of the externalcompanyid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEXTERNALCOMPANYID() {
        return externalcompanyid;
    }

    /**
     * Sets the value of the externalcompanyid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEXTERNALCOMPANYID(String value) {
        this.externalcompanyid = value;
    }

    /**
     * Gets the value of the scenarioname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCENARIONAME() {
        return scenarioname;
    }

    /**
     * Sets the value of the scenarioname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCENARIONAME(String value) {
        this.scenarioname = value;
    }

    /**
     * Gets the value of the invoice property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the invoice property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getINVOICE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OutdataInvoiceType }
     * 
     * 
     */
    public List<OutdataInvoiceType> getINVOICE() {
        if (invoice == null) {
            invoice = new ArrayList<OutdataInvoiceType>();
        }
        return this.invoice;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link VersionType }
     *     
     */
    public VersionType getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link VersionType }
     *     
     */
    public void setVersion(VersionType value) {
        this.version = value;
    }

	@Override
	public String toString() {
		return "OutdataType [requeststatus=" + requeststatus + ", companyid=" + companyid + ", companyname="
				+ companyname + ", companyrole=" + companyrole + ", externalcompanyid=" + externalcompanyid
				+ ", scenarioname=" + scenarioname + ", invoice=" + invoice + ", version=" + version + "]";
	}
    
    

}

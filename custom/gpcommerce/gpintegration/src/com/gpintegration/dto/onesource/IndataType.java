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

import com.gp.commerce.core.dto.onesource.HostRequestInfoType;
import com.gp.commerce.core.dto.onesource.IndataInvoiceType;
import com.gp.commerce.core.dto.onesource.VersionType;


/**
 * 
 * 				
 * Contains batch level request fields as well as a list of invoices.
 * 	            
 * 			
 * 
 * <p>Java class for IndataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IndataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="COMPANY_ID" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CompanyIdType" minOccurs="0"/>
 *         &lt;element name="COMPANY_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CompanyNameType" minOccurs="0"/>
 *         &lt;element name="COMPANY_ROLE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CompanyRoleType" minOccurs="0"/>
 *         &lt;element name="EXTERNAL_COMPANY_ID" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ExternalCompanyIdType" minOccurs="0"/>
 *         &lt;element name="SCENARIO_ID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="SCENARIO_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}ScenarioNameType" minOccurs="0"/>
 *         &lt;element name="XML_GROUP_NAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}XmlGroupNameType" minOccurs="0"/>
 *         &lt;element name="XML_GROUP_OWNER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}XmlGroupOwnerType" minOccurs="0"/>
 *         &lt;element name="USERNAME" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}UserNameType" minOccurs="0"/>
 *         &lt;element name="PASSWORD" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}PasswordType" minOccurs="0"/>
 *         &lt;element name="HOST_SYSTEM" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}HostSystemType" minOccurs="0"/>
 *         &lt;element name="HOST_REQUEST_INFO" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}HostRequestInfoType" minOccurs="0"/>
 *         &lt;element name="CALLING_SYSTEM_NUMBER" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}CallingSystemNumberType" minOccurs="0"/>
 *         &lt;element name="INVOICE" type="{http://www.sabrix.com/services/taxcalculationservice/2011-09-01}IndataInvoiceType" maxOccurs="unbounded"/>
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
@XmlType(name = "IndataType", propOrder = {
    "companyid",
    "companyname",
    "companyrole",
    "externalcompanyid",
    "scenarioid",
    "scenarioname",
    "xmlgroupname",
    "xmlgroupowner",
    "username",
    "password",
    "hostsystem",
    "hostrequestinfo",
    "callingsystemnumber",
    "invoice"
})
public class IndataType {

    @XmlElement(name = "COMPANY_ID")
    protected Long companyid;
    @XmlElement(name = "COMPANY_NAME")
    protected String companyname;
    @XmlElement(name = "COMPANY_ROLE")
    protected String companyrole;
    @XmlElement(name = "EXTERNAL_COMPANY_ID")
    protected String externalcompanyid;
    @XmlElement(name = "SCENARIO_ID")
    protected Long scenarioid;
    @XmlElement(name = "SCENARIO_NAME")
    protected String scenarioname;
    @XmlElement(name = "XML_GROUP_NAME")
    protected String xmlgroupname;
    @XmlElement(name = "XML_GROUP_OWNER")
    protected String xmlgroupowner;
    @XmlElement(name = "USERNAME")
    protected String username;
    @XmlElement(name = "PASSWORD")
    protected String password;
    @XmlElement(name = "HOST_SYSTEM")
    protected String hostsystem;
    @XmlElement(name = "HOST_REQUEST_INFO")
    protected HostRequestInfoType hostrequestinfo;
    @XmlElement(name = "CALLING_SYSTEM_NUMBER")
    protected String callingsystemnumber;
    @XmlElement(name = "INVOICE", required = true)
    protected List<IndataInvoiceType> invoice;
    @XmlAttribute(name = "version")
    protected VersionType version;

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
     * Gets the value of the scenarioid property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSCENARIOID() {
        return scenarioid;
    }

    /**
     * Sets the value of the scenarioid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSCENARIOID(Long value) {
        this.scenarioid = value;
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
     * Gets the value of the xmlgroupname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXMLGROUPNAME() {
        return xmlgroupname;
    }

    /**
     * Sets the value of the xmlgroupname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXMLGROUPNAME(String value) {
        this.xmlgroupname = value;
    }

    /**
     * Gets the value of the xmlgroupowner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXMLGROUPOWNER() {
        return xmlgroupowner;
    }

    /**
     * Sets the value of the xmlgroupowner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXMLGROUPOWNER(String value) {
        this.xmlgroupowner = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSERNAME() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSERNAME(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPASSWORD() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPASSWORD(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the hostsystem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHOSTSYSTEM() {
        return hostsystem;
    }

    /**
     * Sets the value of the hostsystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHOSTSYSTEM(String value) {
        this.hostsystem = value;
    }

    /**
     * Gets the value of the hostrequestinfo property.
     * 
     * @return
     *     possible object is
     *     {@link HostRequestInfoType }
     *     
     */
    public HostRequestInfoType getHOSTREQUESTINFO() {
        return hostrequestinfo;
    }

    /**
     * Sets the value of the hostrequestinfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link HostRequestInfoType }
     *     
     */
    public void setHOSTREQUESTINFO(HostRequestInfoType value) {
        this.hostrequestinfo = value;
    }

    /**
     * Gets the value of the callingsystemnumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCALLINGSYSTEMNUMBER() {
        return callingsystemnumber;
    }

    /**
     * Sets the value of the callingsystemnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCALLINGSYSTEMNUMBER(String value) {
        this.callingsystemnumber = value;
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
     * {@link IndataInvoiceType }
     * 
     * 
     */
    public List<IndataInvoiceType> getINVOICE() {
        if (invoice == null) {
            invoice = new ArrayList<IndataInvoiceType>();
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

}

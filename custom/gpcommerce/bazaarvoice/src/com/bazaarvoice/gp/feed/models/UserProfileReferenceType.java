//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.02.04 at 11:04:03 AM IST 
//


package com.bazaarvoice.gp.feed.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UserProfileReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserProfileReferenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="ExternalId" type="{http://www.bazaarvoice.com/xs/PRR/SyndicationFeed/5.6}ExternalIdType" minOccurs="0"/>
 *         &lt;element name="DisplayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Anonymous" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="DisplayLocale" type="{http://www.bazaarvoice.com/xs/PRR/SyndicationFeed/5.6}LocaleType" minOccurs="0"/>
 *       &lt;/all>
 *       &lt;attribute name="id" type="{http://www.bazaarvoice.com/xs/PRR/SyndicationFeed/5.6}ExternalIdType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserProfileReferenceType", propOrder = {

})
public class UserProfileReferenceType {

    @XmlElement(name = "ExternalId")
    protected String externalId;
    @XmlElement(name = "DisplayName")
    protected String displayName;
    @XmlElement(name = "Anonymous")
    protected boolean anonymous;
    @XmlElement(name = "DisplayLocale")
    protected String displayLocale;
    @XmlAttribute(name = "id")
    protected String id;

    /**
     * Gets the value of the externalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalId() {
        return externalId;
    }

    /**
     * Sets the value of the externalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalId(String value) {
        this.externalId = value;
    }

    /**
     * Gets the value of the displayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the anonymous property.
     * 
     */
    public boolean isAnonymous() {
        return anonymous;
    }

    /**
     * Sets the value of the anonymous property.
     * 
     */
    public void setAnonymous(boolean value) {
        this.anonymous = value;
    }

    /**
     * Gets the value of the displayLocale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayLocale() {
        return displayLocale;
    }

    /**
     * Sets the value of the displayLocale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayLocale(String value) {
        this.displayLocale = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}

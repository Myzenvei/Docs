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
 * <p>Java class for ContextDataValueType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContextDataValueType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="ExternalId" type="{http://www.bazaarvoice.com/xs/PRR/SyndicationFeed/5.6}ExternalIdType"/>
 *         &lt;element name="Label" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ContextDataDimension" type="{http://www.bazaarvoice.com/xs/PRR/SyndicationFeed/5.6}ContextDataDimensionType"/>
 *       &lt;/all>
 *       &lt;attribute name="id" use="required" type="{http://www.bazaarvoice.com/xs/PRR/SyndicationFeed/5.6}ExternalIdType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContextDataValueType", propOrder = {

})
public class ContextDataValueType {

    @XmlElement(name = "ExternalId", required = true)
    protected String externalId;
    @XmlElement(name = "Label", required = true)
    protected String label;
    @XmlElement(name = "ContextDataDimension", required = true)
    protected ContextDataDimensionType contextDataDimension;
    @XmlAttribute(name = "id", required = true)
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
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Gets the value of the contextDataDimension property.
     * 
     * @return
     *     possible object is
     *     {@link ContextDataDimensionType }
     *     
     */
    public ContextDataDimensionType getContextDataDimension() {
        return contextDataDimension;
    }

    /**
     * Sets the value of the contextDataDimension property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContextDataDimensionType }
     *     
     */
    public void setContextDataDimension(ContextDataDimensionType value) {
        this.contextDataDimension = value;
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

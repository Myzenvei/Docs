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
 * <p>Java class for RatingDimensionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RatingDimensionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="ExternalId" type="{http://www.bazaarvoice.com/xs/PRR/SyndicationFeed/5.6}ExternalIdType"/>
 *         &lt;element name="RatingRange" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Label" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Label1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Label2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/all>
 *       &lt;attribute name="id" use="required" type="{http://www.bazaarvoice.com/xs/PRR/SyndicationFeed/5.6}ExternalIdType" />
 *       &lt;attribute name="displayType">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="NORMAL"/>
 *             &lt;enumeration value="SLIDER"/>
 *             &lt;enumeration value="RADIO"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="selectedValueInDisplayEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RatingDimensionType", propOrder = {

})
public class RatingDimensionType {

    @XmlElement(name = "ExternalId", required = true)
    protected String externalId;
    @XmlElement(name = "RatingRange")
    protected int ratingRange;
    @XmlElement(name = "Label")
    protected String label;
    @XmlElement(name = "Label1")
    protected String label1;
    @XmlElement(name = "Label2")
    protected String label2;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "displayType")
    protected String displayType;
    @XmlAttribute(name = "selectedValueInDisplayEnabled")
    protected Boolean selectedValueInDisplayEnabled;

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
     * Gets the value of the ratingRange property.
     * 
     */
    public int getRatingRange() {
        return ratingRange;
    }

    /**
     * Sets the value of the ratingRange property.
     * 
     */
    public void setRatingRange(int value) {
        this.ratingRange = value;
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
     * Gets the value of the label1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel1() {
        return label1;
    }

    /**
     * Sets the value of the label1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel1(String value) {
        this.label1 = value;
    }

    /**
     * Gets the value of the label2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel2() {
        return label2;
    }

    /**
     * Sets the value of the label2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel2(String value) {
        this.label2 = value;
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

    /**
     * Gets the value of the displayType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayType() {
        return displayType;
    }

    /**
     * Sets the value of the displayType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayType(String value) {
        this.displayType = value;
    }

    /**
     * Gets the value of the selectedValueInDisplayEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isSelectedValueInDisplayEnabled() {
        if (selectedValueInDisplayEnabled == null) {
            return false;
        } else {
            return selectedValueInDisplayEnabled;
        }
    }

    /**
     * Sets the value of the selectedValueInDisplayEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSelectedValueInDisplayEnabled(Boolean value) {
        this.selectedValueInDisplayEnabled = value;
    }

}

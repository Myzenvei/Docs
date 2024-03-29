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
 * <p>Java class for AverageRatingValueType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AverageRatingValueType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="AverageRating" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="RatingDimension" type="{http://www.bazaarvoice.com/xs/PRR/SyndicationFeed/5.6}RatingDimensionType"/>
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
@XmlType(name = "AverageRatingValueType", propOrder = {

})
public class AverageRatingValueType {

    @XmlElement(name = "AverageRating")
    protected float averageRating;
    @XmlElement(name = "RatingDimension", required = true)
    protected RatingDimensionType ratingDimension;
    @XmlAttribute(name = "id")
    protected String id;

    /**
     * Gets the value of the averageRating property.
     * 
     */
    public float getAverageRating() {
        return averageRating;
    }

    /**
     * Sets the value of the averageRating property.
     * 
     */
    public void setAverageRating(float value) {
        this.averageRating = value;
    }

    /**
     * Gets the value of the ratingDimension property.
     * 
     * @return
     *     possible object is
     *     {@link RatingDimensionType }
     *     
     */
    public RatingDimensionType getRatingDimension() {
        return ratingDimension;
    }

    /**
     * Sets the value of the ratingDimension property.
     * 
     * @param value
     *     allowed object is
     *     {@link RatingDimensionType }
     *     
     */
    public void setRatingDimension(RatingDimensionType value) {
        this.ratingDimension = value;
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

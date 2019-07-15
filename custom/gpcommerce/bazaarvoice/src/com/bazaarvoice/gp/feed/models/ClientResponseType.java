//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.02.04 at 11:04:03 AM IST 
//


package com.bazaarvoice.gp.feed.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ClientResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ClientResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="Department" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Response" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ResponseMarkup" type="{http://www.bazaarvoice.com/xs/PRR/SyndicationFeed/5.6}MarkupType" minOccurs="0"/>
 *         &lt;element name="ResponseType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ResponseSource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="ProductReferences" type="{http://www.bazaarvoice.com/xs/PRR/SyndicationFeed/5.6}ProductReferencesType" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClientResponseType", propOrder = {

})
public class ClientResponseType {

    @XmlElement(name = "Department")
    protected String department;
    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "Response")
    protected String response;
    @XmlElement(name = "ResponseMarkup")
    protected MarkupType responseMarkup;
    @XmlElement(name = "ResponseType")
    protected String responseType;
    @XmlElement(name = "ResponseSource")
    protected String responseSource;
    @XmlElement(name = "Date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar date;
    @XmlElement(name = "ProductReferences")
    protected ProductReferencesType productReferences;

    /**
     * Gets the value of the department property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the value of the department property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepartment(String value) {
        this.department = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the response property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponse(String value) {
        this.response = value;
    }

    /**
     * Gets the value of the responseMarkup property.
     * 
     * @return
     *     possible object is
     *     {@link MarkupType }
     *     
     */
    public MarkupType getResponseMarkup() {
        return responseMarkup;
    }

    /**
     * Sets the value of the responseMarkup property.
     * 
     * @param value
     *     allowed object is
     *     {@link MarkupType }
     *     
     */
    public void setResponseMarkup(MarkupType value) {
        this.responseMarkup = value;
    }

    /**
     * Gets the value of the responseType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseType() {
        return responseType;
    }

    /**
     * Sets the value of the responseType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseType(String value) {
        this.responseType = value;
    }

    /**
     * Gets the value of the responseSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseSource() {
        return responseSource;
    }

    /**
     * Sets the value of the responseSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseSource(String value) {
        this.responseSource = value;
    }

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }

    /**
     * Gets the value of the productReferences property.
     * 
     * @return
     *     possible object is
     *     {@link ProductReferencesType }
     *     
     */
    public ProductReferencesType getProductReferences() {
        return productReferences;
    }

    /**
     * Sets the value of the productReferences property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProductReferencesType }
     *     
     */
    public void setProductReferences(ProductReferencesType value) {
        this.productReferences = value;
    }

}
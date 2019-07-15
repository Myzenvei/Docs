/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.28 at 12:49:10 AM IST 
//


package com.gp.businessobjects.service.addr.cleanse.input;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}TABLE_OFFSET"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}HOUSE_NUM1"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}HOUSE_NUM2"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}STREET"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}CITY1"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}CITY2"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}REGION"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}POST_CODE1"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}POST_CODE2"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}COUNTRY"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}PO_BOX"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}PO_BOX_LOC"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}STR_SUPPL1"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}STR_SUPPL2"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}STR_SUPPL3"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}LOCATION"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}BUILDING"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}FLOOR"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}ROOMNUMBER"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}HOME_CITY"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}PO_BOX_CTY"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}PO_BOX_NUM"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}PO_BOX_REG"/>
 *         &lt;element ref="{http://businessobjects.com/service/Service_Realtime_DQ_SAP_Address_Cleanse/input}NATION"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "tableoffset",
    "housenum1",
    "housenum2",
    "street",
    "city1",
    "city2",
    "region",
    "postcode1",
    "postcode2",
    "country",
    "pobox",
    "poboxloc",
    "strsuppl1",
    "strsuppl2",
    "strsuppl3",
    "location",
    "building",
    "floor",
    "roomnumber",
    "homecity",
    "poboxcty",
    "poboxnum",
    "poboxreg",
    "nation"
})
@XmlRootElement(name = "Record")
public class Record {

    @XmlElement(name = "TABLE_OFFSET", required = true)
    protected String tableoffset;
    @XmlElement(name = "HOUSE_NUM1", required = true)
    protected String housenum1;
    @XmlElement(name = "HOUSE_NUM2", required = true)
    protected String housenum2;
    @XmlElement(name = "STREET", required = true)
    protected String street;
    @XmlElement(name = "CITY1", required = true)
    protected String city1;
    @XmlElement(name = "CITY2", required = true)
    protected String city2;
    @XmlElement(name = "REGION", required = true)
    protected String region;
    @XmlElement(name = "POST_CODE1", required = true)
    protected String postcode1;
    @XmlElement(name = "POST_CODE2", required = true)
    protected String postcode2;
    @XmlElement(name = "COUNTRY", required = true)
    protected String country;
    @XmlElement(name = "PO_BOX", required = true)
    protected String pobox;
    @XmlElement(name = "PO_BOX_LOC", required = true)
    protected String poboxloc;
    @XmlElement(name = "STR_SUPPL1", required = true)
    protected String strsuppl1;
    @XmlElement(name = "STR_SUPPL2", required = true)
    protected String strsuppl2;
    @XmlElement(name = "STR_SUPPL3", required = true)
    protected String strsuppl3;
    @XmlElement(name = "LOCATION", required = true)
    protected String location;
    @XmlElement(name = "BUILDING", required = true)
    protected String building;
    @XmlElement(name = "FLOOR", required = true)
    protected String floor;
    @XmlElement(name = "ROOMNUMBER", required = true)
    protected String roomnumber;
    @XmlElement(name = "HOME_CITY", required = true)
    protected String homecity;
    @XmlElement(name = "PO_BOX_CTY", required = true)
    protected String poboxcty;
    @XmlElement(name = "PO_BOX_NUM", required = true)
    protected String poboxnum;
    @XmlElement(name = "PO_BOX_REG", required = true)
    protected String poboxreg;
    @XmlElement(name = "NATION", required = true)
    protected String nation;

    /**
     * Gets the value of the tableoffset property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTABLEOFFSET() {
        return tableoffset;
    }

    /**
     * Sets the value of the tableoffset property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTABLEOFFSET(String value) {
        this.tableoffset = value;
    }

    /**
     * Gets the value of the housenum1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHOUSENUM1() {
        return housenum1;
    }

    /**
     * Sets the value of the housenum1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHOUSENUM1(String value) {
        this.housenum1 = value;
    }

    /**
     * Gets the value of the housenum2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHOUSENUM2() {
        return housenum2;
    }

    /**
     * Sets the value of the housenum2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHOUSENUM2(String value) {
        this.housenum2 = value;
    }

    /**
     * Gets the value of the street property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTREET() {
        return street;
    }

    /**
     * Sets the value of the street property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTREET(String value) {
        this.street = value;
    }

    /**
     * Gets the value of the city1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCITY1() {
        return city1;
    }

    /**
     * Sets the value of the city1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCITY1(String value) {
        this.city1 = value;
    }

    /**
     * Gets the value of the city2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCITY2() {
        return city2;
    }

    /**
     * Sets the value of the city2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCITY2(String value) {
        this.city2 = value;
    }

    /**
     * Gets the value of the region property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGION() {
        return region;
    }

    /**
     * Sets the value of the region property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGION(String value) {
        this.region = value;
    }

    /**
     * Gets the value of the postcode1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOSTCODE1() {
        return postcode1;
    }

    /**
     * Sets the value of the postcode1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOSTCODE1(String value) {
        this.postcode1 = value;
    }

    /**
     * Gets the value of the postcode2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOSTCODE2() {
        return postcode2;
    }

    /**
     * Sets the value of the postcode2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOSTCODE2(String value) {
        this.postcode2 = value;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOUNTRY() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOUNTRY(String value) {
        this.country = value;
    }

    /**
     * Gets the value of the pobox property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOBOX() {
        return pobox;
    }

    /**
     * Sets the value of the pobox property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOBOX(String value) {
        this.pobox = value;
    }

    /**
     * Gets the value of the poboxloc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOBOXLOC() {
        return poboxloc;
    }

    /**
     * Sets the value of the poboxloc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOBOXLOC(String value) {
        this.poboxloc = value;
    }

    /**
     * Gets the value of the strsuppl1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTRSUPPL1() {
        return strsuppl1;
    }

    /**
     * Sets the value of the strsuppl1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTRSUPPL1(String value) {
        this.strsuppl1 = value;
    }

    /**
     * Gets the value of the strsuppl2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTRSUPPL2() {
        return strsuppl2;
    }

    /**
     * Sets the value of the strsuppl2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTRSUPPL2(String value) {
        this.strsuppl2 = value;
    }

    /**
     * Gets the value of the strsuppl3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTRSUPPL3() {
        return strsuppl3;
    }

    /**
     * Sets the value of the strsuppl3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTRSUPPL3(String value) {
        this.strsuppl3 = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLOCATION() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLOCATION(String value) {
        this.location = value;
    }

    /**
     * Gets the value of the building property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBUILDING() {
        return building;
    }

    /**
     * Sets the value of the building property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBUILDING(String value) {
        this.building = value;
    }

    /**
     * Gets the value of the floor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFLOOR() {
        return floor;
    }

    /**
     * Sets the value of the floor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFLOOR(String value) {
        this.floor = value;
    }

    /**
     * Gets the value of the roomnumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getROOMNUMBER() {
        return roomnumber;
    }

    /**
     * Sets the value of the roomnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setROOMNUMBER(String value) {
        this.roomnumber = value;
    }

    /**
     * Gets the value of the homecity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHOMECITY() {
        return homecity;
    }

    /**
     * Sets the value of the homecity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHOMECITY(String value) {
        this.homecity = value;
    }

    /**
     * Gets the value of the poboxcty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOBOXCTY() {
        return poboxcty;
    }

    /**
     * Sets the value of the poboxcty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOBOXCTY(String value) {
        this.poboxcty = value;
    }

    /**
     * Gets the value of the poboxnum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOBOXNUM() {
        return poboxnum;
    }

    /**
     * Sets the value of the poboxnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOBOXNUM(String value) {
        this.poboxnum = value;
    }

    /**
     * Gets the value of the poboxreg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOBOXREG() {
        return poboxreg;
    }

    /**
     * Sets the value of the poboxreg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOBOXREG(String value) {
        this.poboxreg = value;
    }

    /**
     * Gets the value of the nation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNATION() {
        return nation;
    }

    /**
     * Sets the value of the nation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNATION(String value) {
        this.nation = value;
    }

}
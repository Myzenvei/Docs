/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.dto.onesource;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import com.gp.commerce.core.dto.onesource.AddressType;
import com.gp.commerce.core.dto.onesource.AmountType;
import com.gp.commerce.core.dto.onesource.AuthorityAttributeType;
import com.gp.commerce.core.dto.onesource.CertificateLocationType;
import com.gp.commerce.core.dto.onesource.CommonAddressType;
import com.gp.commerce.core.dto.onesource.ConvertedCurrencyAmountType;
import com.gp.commerce.core.dto.onesource.CurrencyConversionStepType;
import com.gp.commerce.core.dto.onesource.CurrencyConversionType;
import com.gp.commerce.core.dto.onesource.EstablishmentsLocationType;
import com.gp.commerce.core.dto.onesource.EstablishmentsType;
import com.gp.commerce.core.dto.onesource.FeeType;
import com.gp.commerce.core.dto.onesource.FlagAddressType;
import com.gp.commerce.core.dto.onesource.HostRequestInfoType;
import com.gp.commerce.core.dto.onesource.InclusiveTaxIndicatorsType;
import com.gp.commerce.core.dto.onesource.IndataInvoiceType;
import com.gp.commerce.core.dto.onesource.IndataLicensesDetailType;
import com.gp.commerce.core.dto.onesource.IndataLicensesType;
import com.gp.commerce.core.dto.onesource.IndataLineType;
import com.gp.commerce.core.dto.onesource.IndataType;
import com.gp.commerce.core.dto.onesource.LocationNameType;
import com.gp.commerce.core.dto.onesource.MessageType;
import com.gp.commerce.core.dto.onesource.OutdataAdvisoriesType;
import com.gp.commerce.core.dto.onesource.OutdataErrorType;
import com.gp.commerce.core.dto.onesource.OutdataInvoiceType;
import com.gp.commerce.core.dto.onesource.OutdataLicenseType;
import com.gp.commerce.core.dto.onesource.OutdataLicensesType;
import com.gp.commerce.core.dto.onesource.OutdataLineType;
import com.gp.commerce.core.dto.onesource.OutdataRequestStatusType;
import com.gp.commerce.core.dto.onesource.OutdataTaxSummaryType;
import com.gp.commerce.core.dto.onesource.OutdataTaxType;
import com.gp.commerce.core.dto.onesource.OutdataType;
import com.gp.commerce.core.dto.onesource.QuantitiesType;
import com.gp.commerce.core.dto.onesource.QuantityType;
import com.gp.commerce.core.dto.onesource.RegistrationsType;
import com.gp.commerce.core.dto.onesource.TaxCalculationFault;
import com.gp.commerce.core.dto.onesource.TaxCalculationRequest;
import com.gp.commerce.core.dto.onesource.TaxCalculationResponse;
import com.gp.commerce.core.dto.onesource.UomConversionType;
import com.gp.commerce.core.dto.onesource.UserElementType;
import com.gp.commerce.core.dto.onesource.ZoneAddressType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.sabrix.services.taxcalculationservice._2011_09_01 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _TaxCalculationResponse_QNAME = new QName("http://www.sabrix.com/services/taxcalculationservice/2011-09-01", "taxCalculationResponse");
    private final static QName _TaxCalculationFault_QNAME = new QName("http://www.sabrix.com/services/taxcalculationservice/2011-09-01", "taxCalculationFault");
    private final static QName _TaxCalculationRequest_QNAME = new QName("http://www.sabrix.com/services/taxcalculationservice/2011-09-01", "taxCalculationRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.sabrix.services.taxcalculationservice._2011_09_01
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TaxCalculationFault }
     * 
     */
    public TaxCalculationFault createTaxCalculationFault() {
        return new TaxCalculationFault();
    }

    /**
     * Create an instance of {@link TaxCalculationRequest }
     * 
     */
    public TaxCalculationRequest createTaxCalculationRequest() {
        return new TaxCalculationRequest();
    }

    /**
     * Create an instance of {@link TaxCalculationResponse }
     * 
     */
    public TaxCalculationResponse createTaxCalculationResponse() {
        return new TaxCalculationResponse();
    }

    /**
     * Create an instance of {@link ConvertedCurrencyAmountType }
     * 
     */
    public ConvertedCurrencyAmountType createConvertedCurrencyAmountType() {
        return new ConvertedCurrencyAmountType();
    }

    /**
     * Create an instance of {@link AmountType }
     * 
     */
    public AmountType createAmountType() {
        return new AmountType();
    }

    /**
     * Create an instance of {@link IndataLicensesDetailType }
     * 
     */
    public IndataLicensesDetailType createIndataLicensesDetailType() {
        return new IndataLicensesDetailType();
    }

    /**
     * Create an instance of {@link OutdataInvoiceType }
     * 
     */
    public OutdataInvoiceType createOutdataInvoiceType() {
        return new OutdataInvoiceType();
    }

    /**
     * Create an instance of {@link OutdataLicenseType }
     * 
     */
    public OutdataLicenseType createOutdataLicenseType() {
        return new OutdataLicenseType();
    }

    /**
     * Create an instance of {@link CertificateLocationType }
     * 
     */
    public CertificateLocationType createCertificateLocationType() {
        return new CertificateLocationType();
    }

    /**
     * Create an instance of {@link IndataLineType }
     * 
     */
    public IndataLineType createIndataLineType() {
        return new IndataLineType();
    }

    /**
     * Create an instance of {@link OutdataRequestStatusType }
     * 
     */
    public OutdataRequestStatusType createOutdataRequestStatusType() {
        return new OutdataRequestStatusType();
    }

    /**
     * Create an instance of {@link IndataLicensesType }
     * 
     */
    public IndataLicensesType createIndataLicensesType() {
        return new IndataLicensesType();
    }

    /**
     * Create an instance of {@link EstablishmentsType }
     * 
     */
    public EstablishmentsType createEstablishmentsType() {
        return new EstablishmentsType();
    }

    /**
     * Create an instance of {@link AddressType }
     * 
     */
    public AddressType createAddressType() {
        return new AddressType();
    }

    /**
     * Create an instance of {@link RegistrationsType }
     * 
     */
    public RegistrationsType createRegistrationsType() {
        return new RegistrationsType();
    }

    /**
     * Create an instance of {@link MessageType }
     * 
     */
    public MessageType createMessageType() {
        return new MessageType();
    }

    /**
     * Create an instance of {@link OutdataTaxType }
     * 
     */
    public OutdataTaxType createOutdataTaxType() {
        return new OutdataTaxType();
    }

    /**
     * Create an instance of {@link IndataType }
     * 
     */
    public IndataType createIndataType() {
        return new IndataType();
    }

    /**
     * Create an instance of {@link QuantityType }
     * 
     */
    public QuantityType createQuantityType() {
        return new QuantityType();
    }

    /**
     * Create an instance of {@link CurrencyConversionStepType }
     * 
     */
    public CurrencyConversionStepType createCurrencyConversionStepType() {
        return new CurrencyConversionStepType();
    }

    /**
     * Create an instance of {@link FeeType }
     * 
     */
    public FeeType createFeeType() {
        return new FeeType();
    }

    /**
     * Create an instance of {@link CurrencyConversionType }
     * 
     */
    public CurrencyConversionType createCurrencyConversionType() {
        return new CurrencyConversionType();
    }

    /**
     * Create an instance of {@link HostRequestInfoType }
     * 
     */
    public HostRequestInfoType createHostRequestInfoType() {
        return new HostRequestInfoType();
    }

    /**
     * Create an instance of {@link ZoneAddressType }
     * 
     */
    public ZoneAddressType createZoneAddressType() {
        return new ZoneAddressType();
    }

    /**
     * Create an instance of {@link OutdataType }
     * 
     */
    public OutdataType createOutdataType() {
        return new OutdataType();
    }

    /**
     * Create an instance of {@link UomConversionType }
     * 
     */
    public UomConversionType createUomConversionType() {
        return new UomConversionType();
    }

    /**
     * Create an instance of {@link OutdataLineType }
     * 
     */
    public OutdataLineType createOutdataLineType() {
        return new OutdataLineType();
    }

    /**
     * Create an instance of {@link FlagAddressType }
     * 
     */
    public FlagAddressType createFlagAddressType() {
        return new FlagAddressType();
    }

    /**
     * Create an instance of {@link QuantitiesType }
     * 
     */
    public QuantitiesType createQuantitiesType() {
        return new QuantitiesType();
    }

    /**
     * Create an instance of {@link AuthorityAttributeType }
     * 
     */
    public AuthorityAttributeType createAuthorityAttributeType() {
        return new AuthorityAttributeType();
    }

    /**
     * Create an instance of {@link CommonAddressType }
     * 
     */
    public CommonAddressType createCommonAddressType() {
        return new CommonAddressType();
    }

    /**
     * Create an instance of {@link EstablishmentsLocationType }
     * 
     */
    public EstablishmentsLocationType createEstablishmentsLocationType() {
        return new EstablishmentsLocationType();
    }

    /**
     * Create an instance of {@link IndataInvoiceType }
     * 
     */
    public IndataInvoiceType createIndataInvoiceType() {
        return new IndataInvoiceType();
    }

    /**
     * Create an instance of {@link LocationNameType }
     * 
     */
    public LocationNameType createLocationNameType() {
        return new LocationNameType();
    }

    /**
     * Create an instance of {@link UserElementType }
     * 
     */
    public UserElementType createUserElementType() {
        return new UserElementType();
    }

    /**
     * Create an instance of {@link OutdataTaxSummaryType }
     * 
     */
    public OutdataTaxSummaryType createOutdataTaxSummaryType() {
        return new OutdataTaxSummaryType();
    }

    /**
     * Create an instance of {@link OutdataErrorType }
     * 
     */
    public OutdataErrorType createOutdataErrorType() {
        return new OutdataErrorType();
    }

    /**
     * Create an instance of {@link OutdataAdvisoriesType }
     * 
     */
    public OutdataAdvisoriesType createOutdataAdvisoriesType() {
        return new OutdataAdvisoriesType();
    }

    /**
     * Create an instance of {@link OutdataLicensesType }
     * 
     */
    public OutdataLicensesType createOutdataLicensesType() {
        return new OutdataLicensesType();
    }

    /**
     * Create an instance of {@link InclusiveTaxIndicatorsType }
     * 
     */
    public InclusiveTaxIndicatorsType createInclusiveTaxIndicatorsType() {
        return new InclusiveTaxIndicatorsType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TaxCalculationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.sabrix.com/services/taxcalculationservice/2011-09-01", name = "taxCalculationResponse")
    public JAXBElement<TaxCalculationResponse> createTaxCalculationResponse(TaxCalculationResponse value) {
        return new JAXBElement<TaxCalculationResponse>(_TaxCalculationResponse_QNAME, TaxCalculationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TaxCalculationFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.sabrix.com/services/taxcalculationservice/2011-09-01", name = "taxCalculationFault")
    public JAXBElement<TaxCalculationFault> createTaxCalculationFault(TaxCalculationFault value) {
        return new JAXBElement<TaxCalculationFault>(_TaxCalculationFault_QNAME, TaxCalculationFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TaxCalculationRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.sabrix.com/services/taxcalculationservice/2011-09-01", name = "taxCalculationRequest")
    public JAXBElement<TaxCalculationRequest> createTaxCalculationRequest(TaxCalculationRequest value) {
        return new JAXBElement<TaxCalculationRequest>(_TaxCalculationRequest_QNAME, TaxCalculationRequest.class, null, value);
    }

}

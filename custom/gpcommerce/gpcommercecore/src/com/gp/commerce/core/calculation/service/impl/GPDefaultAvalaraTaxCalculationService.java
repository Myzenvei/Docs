/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.calculation.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Base64;
import com.google.common.math.DoubleMath;
import com.gp.commerce.core.calculation.AddressStructureDTO;
import com.gp.commerce.core.calculation.AvalaraLineItemDTO;
import com.gp.commerce.core.calculation.AvalaraRequestAddressesDTO;
import com.gp.commerce.core.calculation.AvalaraTaxRequestDTO;
import com.gp.commerce.core.calculation.AvalaraTaxResponseDTO;
import com.gp.commerce.core.calculation.service.GPAvalaraTaxCalculationService;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.model.SplitEntryModel;
import com.gp.commerce.core.services.impl.DefaultGPProductTaxCodeService;
import com.gp.commerce.core.util.GPFunctions;

import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.promotions.model.PromotionOrderEntryConsumedModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of {@link GPAvalaraTaxCalculationService}
 */
public class GPDefaultAvalaraTaxCalculationService implements GPAvalaraTaxCalculationService {
    private static final Logger LOG = Logger.getLogger(GPDefaultAvalaraTaxCalculationService.class);
    
    private DefaultGPProductTaxCodeService gpProductTaxCodeService;
    private static final double EPSILON = 0.01d;
    public static final String ONE_SOURCE_TAX_SERVICE_INVOICE_DATE_FROMAT = "yyyy-MM-dd";
	public static final String AVALARA_TAX_SERVICE_INVOICE_DATE_FROMAT = "yyyy-MM-dd'T'hh:mm:ssZ";
	public static final String HYBRIS_ORDER_CREATION_DATE_FORMAT = "E MMM dd HH:mm:ss Z yyyy";
	private static final String SHIP = "-SHIP";
	private static final String TAX_TYPE_SALES_INVOICE = "SalesInvoice";
    private static final String TAX_TYPE_SALES_ORDER = "SalesOrder";
	private static final String AUTHORIZATION = "Authorization";
	private static final String BASIC = "Basic ";
	private static final String US_ASCII = "US-ASCII";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String DECIMAL_FORMAT = "##.00";
	
	@Override
    public AvalaraTaxResponseDTO calculateTax(AbstractOrderModel abstractOrderModel, boolean isPlaceOrderCall) {
        AvalaraTaxResponseDTO avalaraTaxResponse=null;
        final ObjectMapper requestMapper = new ObjectMapper();
        String userName = Config.getParameter(GpcommerceCoreConstants.GP_AVALARA_TAX_CALCULATION_SERVICE_USERNAME);
        String password = Config.getParameter(GpcommerceCoreConstants.GP_AVALARA_TAX_CALCULATION_SERVICE_PASSWORD);
        String serviceEndpoint= Config.getParameter(GpcommerceCoreConstants.GP_AVALARA_TAX_CALCULATION_SERVICE_URL);
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        AvalaraTaxRequestDTO avalaraTaxRequest=null;
        try {
                avalaraTaxRequest=createAvalaraTaxRequest(abstractOrderModel, isPlaceOrderCall);
                headers.set(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                String auth = userName + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName(US_ASCII)));
                String authHeader = BASIC + new String(encodedAuth);
                LOG.debug("Avalara endpoint -->"+serviceEndpoint);
                headers.set(AUTHORIZATION, authHeader);
                HttpEntity<String> entity = new HttpEntity<>(requestMapper.writeValueAsString(avalaraTaxRequest), headers);
                LOG.debug("Request Object for avalara:" + requestMapper.writeValueAsString(avalaraTaxRequest));
                final ResponseEntity<AvalaraTaxResponseDTO> avalaraResponse = restTemplate.exchange(serviceEndpoint,
                        HttpMethod.POST, entity, AvalaraTaxResponseDTO.class);
                
                LOG.debug("Response Object from avalara:"+requestMapper.writeValueAsString(avalaraResponse));
                if (null != avalaraResponse && null != avalaraResponse.getBody()) {
                    avalaraTaxResponse = avalaraResponse.getBody();
                    return avalaraTaxResponse;
                } else {
                    return avalaraTaxResponse;
                }
        }catch (Exception e){
        	LOG.error("AValara error->"+e.getMessage(),e);
        	 return avalaraTaxResponse;
        }
    }

	private AvalaraTaxRequestDTO createAvalaraTaxRequest(AbstractOrderModel abstractOrderModel, boolean isPlaceOrderCall) {
        AvalaraTaxRequestDTO avalaraTaxRequestDTO=new AvalaraTaxRequestDTO();
        
        if(isPlaceOrderCall) {
             avalaraTaxRequestDTO.setCommit(Config.getBoolean(GpcommerceCoreConstants.GP_TAX_CALCULATION_SERVICE_AVALARA_COMMIT_TRUE,true));//from avalara model
             avalaraTaxRequestDTO.setType(TAX_TYPE_SALES_INVOICE);
        } else {
            avalaraTaxRequestDTO.setCommit(Config.getBoolean(GpcommerceCoreConstants.GP_TAX_CALCULATION_SERVICE_AVALARA_COMMIT_FALSE,false));//from avalara model
            avalaraTaxRequestDTO.setType(TAX_TYPE_SALES_ORDER);
        }
        
        avalaraTaxRequestDTO.setCompanyCode(Config.getParameter(GpcommerceCoreConstants.GP_TAX_CALCULATION_SERVICE_AVALARA_COMPANY_CODE)); 
        avalaraTaxRequestDTO.setCurrencyCode(Config.getParameter(GpcommerceCoreConstants.GP_TAX_CALCULATION_SERVICE_AVALARA_CURRENCY_CODE));
        avalaraTaxRequestDTO.setDate(GPFunctions.formatDate(abstractOrderModel.getCreationtime().toString(),HYBRIS_ORDER_CREATION_DATE_FORMAT, AVALARA_TAX_SERVICE_INVOICE_DATE_FROMAT));
        avalaraTaxRequestDTO.setLines(createAvalaraLineItemList(abstractOrderModel));
        avalaraTaxRequestDTO.setCode(abstractOrderModel.getCode());
        avalaraTaxRequestDTO.setDescription(abstractOrderModel.getDescription());//product descirption

        if(null != abstractOrderModel.getUser() && abstractOrderModel.getUser() instanceof CustomerModel) {
            CustomerModel custModel=(CustomerModel) abstractOrderModel.getUser();
            if(null != custModel.getNetSuiteCustomerInternalId()){
                avalaraTaxRequestDTO.setCustomerCode(custModel.getNetSuiteCustomerInternalId());
            }
            else{
                avalaraTaxRequestDTO.setCustomerCode(custModel.getContactEmail());//user model for guest user as well
            }
        }
        return avalaraTaxRequestDTO;
    }
    
	private List<AvalaraLineItemDTO> createAvalaraLineItemList(AbstractOrderModel abstarctOrderModel){
        List<AvalaraLineItemDTO> listAvalaraLineItem= new ArrayList<AvalaraLineItemDTO>();
        Map<PK, SplitEntryModel> addressSplitentryMap= new HashMap<>();
         Map<Integer, Double> promoMap = new HashMap<>();
         DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
        
        for(PromotionResultModel proModel:abstarctOrderModel.getAllPromotionResults()) {
            for(PromotionOrderEntryConsumedModel entry:proModel.getConsumedEntries()) {
                   promoMap.put(entry.getOrderEntryNumber(), entry.getAdjustedUnitPrice());
            }
        }
        
        double discounts = 0.0d;
        
		final List<DiscountValue> discountList = abstarctOrderModel.getGlobalDiscountValues(); // discounts on the cart itself
		if (discountList != null && !discountList.isEmpty())
		{
			for (final DiscountValue discount : discountList)
			{
				final double value = discount.getAppliedValue();
				if (DoubleMath.fuzzyCompare(value, 0, EPSILON) > 0
						&& !CommerceServicesConstants.QUOTE_DISCOUNT_CODE.equals(discount.getCode()))
				{
					discounts += value;
				}
			}
		}
		
		if (BigDecimal.valueOf(discounts).compareTo(BigDecimal.valueOf(0.0d)) != 0) {
			decimalFormat.format(discounts);
		}
		
		double totalEntryPrice = 0.0;
		double splitEntriesQuantity = 0;
		boolean hasSplitQuantity = false;
		for (AbstractOrderEntryModel orderEntry : abstarctOrderModel.getEntries()) {
			totalEntryPrice = totalEntryPrice + orderEntry.getTotalPrice();
		}

		decimalFormat.format(totalEntryPrice);
		
        for(AbstractOrderEntryModel orderEntry: abstarctOrderModel.getEntries()) {
        	
        	if (orderEntry.getSplitEntry().size() > 1) {
				hasSplitQuantity = true;
				splitEntriesQuantity = getSplitEntriesQuantity(orderEntry.getSplitEntry());
			} else {
				hasSplitQuantity = false;
				splitEntriesQuantity = 0.0;
			}

			double calculatedOrderEntryDiscount = (orderEntry.getTotalPrice() / totalEntryPrice) * discounts;
			decimalFormat.format(calculatedOrderEntryDiscount);
        	
        	
            for(SplitEntryModel splitEntry: orderEntry.getSplitEntry()) {
                if(null != promoMap.get(orderEntry.getEntryNumber())){
                    listAvalaraLineItem.add(mapAvalaraLineItem(splitEntry, splitEntry.getCode(),abstarctOrderModel.getStore().getDefaultDeliveryOrigin(),promoMap.get(orderEntry.getEntryNumber()),calculatedOrderEntryDiscount, hasSplitQuantity, splitEntriesQuantity));
                }
                else{
                    listAvalaraLineItem.add(mapAvalaraLineItem(splitEntry, splitEntry.getCode(),abstarctOrderModel.getStore().getDefaultDeliveryOrigin(),null,calculatedOrderEntryDiscount, hasSplitQuantity, splitEntriesQuantity));
                }
                if(null!= splitEntry.getDeliveryAddress().getPk() && !addressSplitentryMap.containsKey(splitEntry.getDeliveryAddress().getPk())) {
                    addressSplitentryMap.put(splitEntry.getDeliveryAddress().getPk(),splitEntry);
                    if (null != splitEntry.getDeliveryMode()) {
                        listAvalaraLineItem.add(mapAvalaraShipMethodLineItem(splitEntry,splitEntry.getCode()+SHIP, abstarctOrderModel.getStore().getDefaultDeliveryOrigin()));
                    } else if (null != abstarctOrderModel.getDeliveryMode()){
                        listAvalaraLineItem.add(mapAvalaraOrderShipMethodLineItem(abstarctOrderModel, splitEntry, splitEntry.getCode()+SHIP, abstarctOrderModel.getStore().getDefaultDeliveryOrigin()));
                    }
                }
                }
            		}
       		return listAvalaraLineItem;
    }
    
    private double getSplitEntriesQuantity(List<SplitEntryModel> splitEntryList) {
		double splitEntriesQuantity = 0;
		for (SplitEntryModel splitEntry : splitEntryList) {
			splitEntriesQuantity = splitEntriesQuantity + Double.parseDouble(splitEntry.getQty());
		}
		return splitEntriesQuantity;
	}
    
    
    private AvalaraLineItemDTO mapAvalaraLineItem(SplitEntryModel splitEntryModel, String line,PointOfServiceModel wareHouseAddress,Double adjustedAmount, double calculatedOrderEntryDiscount, boolean hasSplitQuantity, double splitEntriesQuantity){
        AvalaraLineItemDTO avalaraLineItemDTO=new AvalaraLineItemDTO();
        DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
        if(adjustedAmount == null){
        	if(hasSplitQuantity) {
        		avalaraLineItemDTO.setAmount(Double.valueOf(decimalFormat.format((splitEntryModel.getPrice() - (Double.parseDouble(splitEntryModel.getQty()) / splitEntriesQuantity) * calculatedOrderEntryDiscount))));
        	} else {
        		avalaraLineItemDTO.setAmount(Double.valueOf(decimalFormat.format(splitEntryModel.getPrice() - calculatedOrderEntryDiscount)));
        	}
        }
        else{
        	if(hasSplitQuantity) {
        		avalaraLineItemDTO.setAmount(Double.valueOf(decimalFormat.format(adjustedAmount * Double.parseDouble(splitEntryModel.getQty()) - (Double.parseDouble(splitEntryModel.getQty()) / splitEntriesQuantity) * calculatedOrderEntryDiscount)));
        	} else {
        		avalaraLineItemDTO.setAmount((adjustedAmount * Double.parseDouble(splitEntryModel.getQty())) - calculatedOrderEntryDiscount);
        	}
        }
        avalaraLineItemDTO.setItemCode(splitEntryModel.getProductCode());
        avalaraLineItemDTO.setQuantity(Long.parseLong(splitEntryModel.getQty()));
        if(null!=splitEntryModel.getDeliveryAddress().getRegion() && null != splitEntryModel.getProductCode() ){
        	String taxCode=getGpProductTaxCodeService().fetchTaxCode(splitEntryModel.getProductCode(), splitEntryModel.getDeliveryAddress().getRegion().getIsocodeShort());
        	if(null != taxCode){
        		avalaraLineItemDTO.setTaxCode(taxCode);
        	}
        	else{
            	avalaraLineItemDTO.setTaxCode(Config.getParameter(GpcommerceCoreConstants.GP_AVALARA_TAX_CODE));
        	}
        }else{
        	avalaraLineItemDTO.setTaxCode(Config.getParameter(GpcommerceCoreConstants.GP_AVALARA_TAX_CODE));
        }
        avalaraLineItemDTO.setDescription(splitEntryModel.getDeliveryInstruction());
        avalaraLineItemDTO.setNumber(""+line);
        avalaraLineItemDTO.setAddresses(mapAvalaraAddressLineItem(splitEntryModel,wareHouseAddress));
        return avalaraLineItemDTO;
    }

    private AvalaraLineItemDTO mapAvalaraShipMethodLineItem(SplitEntryModel splitEntryModel, String line,PointOfServiceModel wareHouseAddress){
        AvalaraLineItemDTO avalaraLineItemDTO=new AvalaraLineItemDTO();
        avalaraLineItemDTO.setAmount(Double.parseDouble(splitEntryModel.getDeliveryMode().getDeliveryPrice().substring(1).replaceAll(",", "")));
        avalaraLineItemDTO.setItemCode(splitEntryModel.getDeliveryMode().getCode());
        avalaraLineItemDTO.setTaxCode(Config.getParameter(GpcommerceCoreConstants.GP_SHIPPING_TAX_CODE));
        avalaraLineItemDTO.setDescription(splitEntryModel.getDeliveryInstruction());
        avalaraLineItemDTO.setNumber(""+line);
        avalaraLineItemDTO.setAddresses(mapAvalaraAddressLineItem(splitEntryModel,wareHouseAddress));
        return avalaraLineItemDTO;
    }
    private AvalaraLineItemDTO mapAvalaraOrderShipMethodLineItem(AbstractOrderModel abstractOrderModel,SplitEntryModel splitEntryModel, String line, PointOfServiceModel wareHouseAddress) {
        AvalaraLineItemDTO avalaraLineItemDTO=new AvalaraLineItemDTO();
        avalaraLineItemDTO.setAmount(abstractOrderModel.getDeliveryCost());
        avalaraLineItemDTO.setItemCode(abstractOrderModel.getDeliveryMode().getCode());
        avalaraLineItemDTO.setTaxCode(Config.getParameter(GpcommerceCoreConstants.GP_SHIPPING_TAX_CODE));
        avalaraLineItemDTO.setDescription(splitEntryModel.getDeliveryInstruction());
        avalaraLineItemDTO.setNumber(""+line);
        avalaraLineItemDTO.setAddresses(mapAvalaraAddressLineItem(splitEntryModel,wareHouseAddress));
        return avalaraLineItemDTO;
    }

    private AvalaraRequestAddressesDTO mapAvalaraAddressLineItem(SplitEntryModel splitEntryModel,PointOfServiceModel wareHouseAddress){
        AvalaraRequestAddressesDTO avalaraRequestAddressesDTO=new AvalaraRequestAddressesDTO();
        avalaraRequestAddressesDTO.setShipTo(mapAvalaraShipTo(splitEntryModel.getDeliveryAddress()));
        avalaraRequestAddressesDTO.setShipFrom(mapAvalaraShipFrom(wareHouseAddress));
        return avalaraRequestAddressesDTO;
    }
    private AddressStructureDTO mapAvalaraShipTo(AddressModel addressModel){
        AddressStructureDTO addressStructureDTO=new AddressStructureDTO();
        addressStructureDTO.setCity(addressModel.getTown());
        addressStructureDTO.setLine1(addressModel.getLine1());
        if(null != addressModel.getLine2()) {
        	addressStructureDTO.setLine2(addressModel.getLine2());
        }
        addressStructureDTO.setPostalCode(addressModel.getPostalcode());
        if(null != addressModel.getRegion()) {
            addressStructureDTO.setRegion(addressModel.getRegion().getIsocodeShort());
        }
        addressStructureDTO.setCountry(addressModel.getCountry().getIsocode());
        return addressStructureDTO;
    }
    private AddressStructureDTO mapAvalaraShipFrom(PointOfServiceModel wareHouseAddress){
        AddressStructureDTO addressStructureDTO=null;
        
        if(null != wareHouseAddress) {
            AddressModel wareHouseAddrModel = wareHouseAddress.getAddress();
            if(null != wareHouseAddrModel){
            	addressStructureDTO=new AddressStructureDTO();
            	addressStructureDTO.setCity(wareHouseAddrModel.getTown());
            	if(null != wareHouseAddrModel.getLine1()) {
            		addressStructureDTO.setLine1(wareHouseAddrModel.getLine1());
            	}
            	
            	if(null != wareHouseAddrModel.getLine2()) {
            		addressStructureDTO.setLine2(wareHouseAddrModel.getLine2());
            	}
                addressStructureDTO.setPostalCode(wareHouseAddrModel.getPostalcode());
                addressStructureDTO.setCountry(wareHouseAddrModel.getCountry().getIsocode());
                if(null != wareHouseAddrModel.getRegion()){
                    addressStructureDTO.setRegion( wareHouseAddrModel.getRegion().getIsocodeShort());
                }
            }
        }

        return addressStructureDTO;
    }
    public DefaultGPProductTaxCodeService getGpProductTaxCodeService() {
		return gpProductTaxCodeService;
	}

	public void setGpProductTaxCodeService(DefaultGPProductTaxCodeService gpProductTaxCodeService) {
		this.gpProductTaxCodeService = gpProductTaxCodeService;
	}
}
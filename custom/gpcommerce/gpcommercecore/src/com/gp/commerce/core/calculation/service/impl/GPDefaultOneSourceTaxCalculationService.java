/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.calculation.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.google.common.math.DoubleMath;
import com.gp.commerce.core.calculation.service.GPOneSourceTaxCalculationService;
import com.gp.commerce.core.calculation.service.GPOneSourceTaxService;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.dto.onesource.IndataInvoiceType;
import com.gp.commerce.core.dto.onesource.IndataLineType;
import com.gp.commerce.core.dto.onesource.IndataType;
import com.gp.commerce.core.dto.onesource.TaxCalculationFault_Exception;
import com.gp.commerce.core.dto.onesource.TaxCalculationRequest;
import com.gp.commerce.core.dto.onesource.TaxCalculationResponse;
import com.gp.commerce.core.dto.onesource.VersionType;
import com.gp.commerce.core.dto.onesource.ZoneAddressType;
import com.gp.commerce.core.model.SplitEntryModel;
import com.gp.commerce.core.util.GPHeaderHandlerResolver;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.PromotionOrderEntryConsumedData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotions.model.PromotionOrderEntryConsumedModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.site.BaseSiteService;
import com.gp.commerce.core.util.GPFunctions;

/**
 * This class provides services to process one source tax calculation
 * 
 * @author spandiyan
 */
public class GPDefaultOneSourceTaxCalculationService implements GPOneSourceTaxCalculationService {
	private static final Logger LOG = Logger.getLogger(GPDefaultOneSourceTaxCalculationService.class);
	private static final double EPSILON = 0.01d;
	private static final String DECIMAL_FORMAT = "##.00";
	private static final String HYBRIS_ORDER_CREATION_DATE_FORMAT = "E MMM dd HH:mm:ss Z yyyy";
	private static final String ONE_SOURCE_TAX_SERVICE_INVOICE_DATE_FROMAT = "yyyy-MM-dd";
	
	@Resource
	private ProductService productService;
	
	@Resource
	private BaseSiteService baseSiteService;

	/**
	 * This method calculates tax for cart model
	 * 
	 * @param abstractOrderModel
	 *            the cart model
	 * @return tax calculation response
	 * @throws GPIntegrationException
	 */
	public TaxCalculationResponse calculateTax(AbstractOrderModel abstractOrderModel) {
		TaxCalculationResponse taxCalculationResponse = null;
		try {
			GPOneSourceTaxService gpOneSourceTaxService = getOneSourceTaxService();
			LOG.info("Calling OneSource tax calculation service");
			taxCalculationResponse = gpOneSourceTaxService.calculateTax(createTaxCalculationRequest(abstractOrderModel));
		} catch (TaxCalculationFault_Exception e) {
			LOG.error(GpcommerceCoreConstants.ONE_SOURCE_TAX_SERVICE_ERROR +e.getMessage(),e);
		} catch (Exception e) {
			LOG.error(GpcommerceCoreConstants.ONE_SOURCE_TAX_SERVICE_ERROR +e.getMessage(),e);
		}
		return taxCalculationResponse;
	}

	private TaxCalculationRequest createTaxCalculationRequest(AbstractOrderModel abstractOrderModel) {
		TaxCalculationRequest taxCalculationRequest = new TaxCalculationRequest();
		IndataType taxInDataType = new IndataType();
		taxInDataType.setVersion(VersionType.G);
		taxInDataType.getINVOICE().add(mapInvoiceRequest(abstractOrderModel));
		taxCalculationRequest.setINDATA(taxInDataType);
		return taxCalculationRequest;
	}

	private IndataInvoiceType mapInvoiceRequest(AbstractOrderModel abstractOrderModel) {
		IndataInvoiceType taxInvoiceData = new IndataInvoiceType();
		Map<String, Double> proratedDeliveryCostMap = getProratedDelvieryCost(abstractOrderModel);
		LOG.info("Prorated delivery cost map size:"+proratedDeliveryCostMap.size());
		if(null==getBaseSiteService().getCurrentBaseSite()) {
			taxInvoiceData.setEXTERNALCOMPANYID(Config.getParameter(GpcommerceCoreConstants.ONE_SOURCE_TAX_COMPANY_ID+abstractOrderModel.getSite().getUid()));
		}
		else {
		taxInvoiceData.setEXTERNALCOMPANYID(Config.getParameter(GpcommerceCoreConstants.ONE_SOURCE_TAX_COMPANY_ID+getBaseSiteService().getCurrentBaseSite().getUid()));
		}
		taxInvoiceData.setCALCULATIONDIRECTION(Config.getParameter(GpcommerceCoreConstants.ONE_SOURCE_TAX_CAL_DIRECTION));
		taxInvoiceData.setCOMPANYROLE(Config.getParameter(GpcommerceCoreConstants.ONE_SOURCE_TAX_COMPANY_ROLE));
		taxInvoiceData.setCURRENCYCODE(Config.getParameter(GpcommerceCoreConstants.ONE_SOURCE_TAX_CURRENCY_CODE));
		taxInvoiceData.setINVOICEDATE(GPFunctions.formatDate(abstractOrderModel.getCreationtime().toString(), HYBRIS_ORDER_CREATION_DATE_FORMAT, ONE_SOURCE_TAX_SERVICE_INVOICE_DATE_FROMAT));
		taxInvoiceData.setINVOICENUMBER(abstractOrderModel.getCode());
		taxInvoiceData.setISAUDITED(Config.getParameter(GpcommerceCoreConstants.ONE_SOURCE_TAX_IS_AUDITED));
		taxInvoiceData.setTRANSACTIONTYPE(Config.getParameter(GpcommerceCoreConstants.ONE_SOURCE_TAX_TRANS_TYPE));
		taxInvoiceData.setCUSTOMERNUMBER(getCustomerId(abstractOrderModel.getUser()));
		taxInvoiceData.getLINE().addAll(mapInvoiceItem(abstractOrderModel, proratedDeliveryCostMap));
		return taxInvoiceData;
	}

	private String getCustomerId(UserModel userModel) {
		String userId = "";
		if (!StringUtils.isEmpty(userModel.getUid()) && userModel.getUid().contains("|")) {
			final String uid = userModel.getUid();
			final String[] splittedUserId = uid.split(GpcommerceCoreConstants.SITE_DELIMITER);
			// Email id would be in first field for registered user and second field for
			// guest user
			userId = StringUtils.equalsIgnoreCase(GpcommerceCoreConstants.GUEST_USER, userModel.getDisplayName()) ? splittedUserId[1] : splittedUserId[0];
		}
		return userId;
	}
	
	/**
	 * Groups the split entries based on delivery mode to prorate the delivery cost by basePrice (if any of the spilt entries have grossWeight attribute as null)
	 * or grossWeight( if all the items have grossWeight value).
	 * 
	 * @param abstractOrderModel
	 * @return SplitEntry.Code as Key and prorated delivery cost as value. 
	 */
	private Map<String, Double> getProratedDelvieryCost(AbstractOrderModel abstractOrderModel){
		MultiValueMap<String, SplitEntryModel> splitEntryByDeliveryMode = new LinkedMultiValueMap<String, SplitEntryModel>();
		boolean isDeliveryModeFromSplitEntry = true;
		if(null != abstractOrderModel && null != abstractOrderModel.getEntries()) {
			for(AbstractOrderEntryModel orderEntry: abstractOrderModel.getEntries()) {
				for(SplitEntryModel splitEntry: orderEntry.getSplitEntry()) {
					if(null != splitEntry.getDeliveryMode()) {
						LOG.info("Multiple shipment reading delivery mode from split entry.");
						if(splitEntry.getDeliveryMode().getCode().contains(GpcommerceCoreConstants.SYMBOL_UNDERSCORE)) {
							String deliveryMode = splitEntry.getDeliveryMode().getCode().substring(0, splitEntry.getDeliveryMode().getCode().indexOf(GpcommerceCoreConstants.SYMBOL_UNDERSCORE));
							splitEntryByDeliveryMode.add(deliveryMode, splitEntry);
						} else {
							splitEntryByDeliveryMode.add(splitEntry.getDeliveryMode().getCode(), splitEntry);
						}
					} else {
						if(null != abstractOrderModel.getDeliveryMode()) {
							LOG.info("Single shipment reading delivery mode from cart.");
							isDeliveryModeFromSplitEntry = false;
							splitEntryByDeliveryMode.add(abstractOrderModel.getDeliveryMode().getCode(), splitEntry);
						}
					}
				}
			}
		}
		return calculateProratedDeliveryCost(splitEntryByDeliveryMode, isDeliveryModeFromSplitEntry, abstractOrderModel);
	}
	
	
	/**
	 * Helper method decides proration logic (based on basePrice or grossWeight).
	 * 
	 * @param splitEntryMap
	 * @param isDeliveryModeFromSplitEntry
	 * @param abstractOrderModel
	 * @return SplitEntry.Code as Key and prorated delivery cost as value.
	 */
	private Map<String, Double> calculateProratedDeliveryCost(MultiValueMap<String, SplitEntryModel> splitEntryMap, boolean isDeliveryModeFromSplitEntry, AbstractOrderModel abstractOrderModel){
		Map<String, Double> proratedShippingMap = new HashMap<>();
		proratedShippingMap = prorateDeliveryCostByBasePrice(splitEntryMap, isDeliveryModeFromSplitEntry, abstractOrderModel);
		return proratedShippingMap;
	}
	
	/**
	 * Helper method prorate the delivery cost based on product basePrice attribute.
	 * 
	 * @param splitEntryMap
	 * @param isDeliveryModeFromSplitEntry
	 * @param abstractOrderModel
	 * @return SplitEntry.Code as Key and prorated delivery cost as value.
	 */
	private Map<String, Double> prorateDeliveryCostByBasePrice(MultiValueMap<String, SplitEntryModel> splitEntryMap, boolean isDeliveryModeFromSplitEntry, AbstractOrderModel abstractOrderModel){
		LOG.info("One of the item(s) has/have gross weight as null, calculating proration based on product base price."); 
		Map<String, Double> proratedShippingMap = new HashMap<>();
		DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
		if(isDeliveryModeFromSplitEntry) {
			splitEntryMap.keySet().stream().forEach(deliveryMode -> {
				splitEntryMap.get(deliveryMode).stream().forEach(splitEntry ->{
					double sumOfProductBasePrice = getSumOfBasePriceForSplitQuantity(splitEntryMap.get(deliveryMode));
					double prorationValue = (getProductBasePrice(splitEntry, abstractOrderModel) * Double.parseDouble(splitEntry.getQty()) / sumOfProductBasePrice) * Double.parseDouble(splitEntry.getDeliveryMode().getDeliveryPrice().substring(1));
					proratedShippingMap.put(splitEntry.getCode(), Double.parseDouble(decimalFormat.format(prorationValue)));
				});
			});
		} else {
			splitEntryMap.keySet().stream().forEach(deliveryMode -> {
				double sumOfProductBasePrice = getSumOfBasePrice(splitEntryMap.get(deliveryMode), abstractOrderModel);
				splitEntryMap.get(deliveryMode).stream().forEach(splitEntry ->{
					double prorationValue = (getProductBasePrice(splitEntry, abstractOrderModel) * Double.parseDouble(splitEntry.getQty()) / sumOfProductBasePrice) * abstractOrderModel.getDeliveryCost();
					proratedShippingMap.put(splitEntry.getCode(), Double.parseDouble(decimalFormat.format(prorationValue)));
				});
			});
		}
		return proratedShippingMap;
	}
	
	/**
	 * Returns the associated base price for the given split entry based on product code.
	 * 
	 * @param splitEntry
	 * @param abstractOrderModel
	 * @return basePrice for the given product.
	 */
	private double getProductBasePrice(SplitEntryModel splitEntry, AbstractOrderModel abstractOrderModel) {
		double productBasePrice = 0.0;
		for (AbstractOrderEntryModel orderEntry : abstractOrderModel.getEntries()) {
			for (SplitEntryModel cartSplitEntry : orderEntry.getSplitEntry()) {
				if (cartSplitEntry.getCode().equals(splitEntry.getCode())) {
					productBasePrice = orderEntry.getBasePrice();
				}
			}
		}
		LOG.info("Base price for the given product:"+productBasePrice);
		return productBasePrice;
	}
	
	/**
	 * Returns the sum of basePrice for the given list of split entries.
	 * 
	 * @param proudctList
	 * @param abstractOrderModel
	 * @return
	 */
	private double getSumOfBasePrice(List<SplitEntryModel> proudctList, AbstractOrderModel abstractOrderModel) {
		double sumOfBasePrice = 0.0;
		for(SplitEntryModel splitEntry: proudctList) {
			for(AbstractOrderEntryModel orderEntry: abstractOrderModel.getEntries()) {
				for(SplitEntryModel cartSplitEntry: orderEntry.getSplitEntry()) {
					if(cartSplitEntry.getCode().equals(splitEntry.getCode())) {
						sumOfBasePrice = sumOfBasePrice + orderEntry.getBasePrice() * orderEntry.getQuantity();
					}
				}
			}
		}
		LOG.info("Sum of base price for given split entries:"+sumOfBasePrice);
		return sumOfBasePrice;
	}
	
	private double getSumOfBasePriceForSplitQuantity(List<SplitEntryModel> proudctList) {
		double sumOfBasePrice = 0.0;
		for(SplitEntryModel splitEntry: proudctList) {
			sumOfBasePrice = sumOfBasePrice + splitEntry.getPrice(); 
		}
		LOG.info("SumOfSplitQuantity:"+sumOfBasePrice);
		return sumOfBasePrice;
	}
	/**
	 * Returns the sum of grossWeight for the given list of split entries.
	 * 
	 * @param proudctList
	 * @return sum of grossWeight
	 */
	private double getSumOfGrossWeight(List<SplitEntryModel> proudctList) {
		double allProductGross = 0.0;
		for(SplitEntryModel splitEntry: proudctList) {
			ProductModel productModel = productService.getProductForCode(splitEntry.getProductCode());
			allProductGross = allProductGross + Double.parseDouble(productModel.getGrossWeight()) * Double.parseDouble(splitEntry.getQty());
		}
		LOG.info("Sum of gross weight for given split entries:"+allProductGross);
		return allProductGross;
	}
	
	private List<IndataLineType> mapInvoiceItem(AbstractOrderModel abstractOrderModel, Map<String, Double> proratedDeliveryCostMap) {
		List<IndataLineType> lineItemList = new ArrayList<>();
		int lineNumber = 0;
		DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
		Map<Integer, Double> promoMap = new HashMap<>();
		
		for (PromotionResultModel proData : abstractOrderModel.getAllPromotionResults()) {
			for (PromotionOrderEntryConsumedModel entry : proData.getConsumedEntries()) {
				promoMap.put(entry.getOrderEntryNumber(), entry.getAdjustedUnitPrice());
			}
		}

		double discounts = 0.0d;
		final List<DiscountValue> discountList = abstractOrderModel.getGlobalDiscountValues(); // discounts on the cart itself
		if (discountList != null && !discountList.isEmpty()) {
			for (final DiscountValue discount : discountList) {
				final double value = discount.getAppliedValue();
				if (DoubleMath.fuzzyCompare(value, 0, EPSILON) > 0
						&& !CommerceServicesConstants.QUOTE_DISCOUNT_CODE.equals(discount.getCode())) {
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
		for (AbstractOrderEntryModel orderEntry : abstractOrderModel.getEntries()) {
			totalEntryPrice = totalEntryPrice + orderEntry.getTotalPrice();
		}

		decimalFormat.format(totalEntryPrice);

		if (null != abstractOrderModel && null != abstractOrderModel.getEntries()) {
			for (AbstractOrderEntryModel orderEntry : abstractOrderModel.getEntries()) {
				if (null != orderEntry && null != orderEntry.getSplitEntry()) {
					if (orderEntry.getSplitEntry().size() > 1) {
						hasSplitQuantity = true;
						splitEntriesQuantity = getSplitEntriesQuantity(orderEntry.getSplitEntry());
					} else {
						hasSplitQuantity = false;
						splitEntriesQuantity = 0.0;
					}

					double calculatedOrderEntryDiscount = (orderEntry.getTotalPrice() / totalEntryPrice) * discounts;
					decimalFormat.format(calculatedOrderEntryDiscount);

					for (SplitEntryModel splitEntry : orderEntry.getSplitEntry()) {
						int productLineNumber = ++lineNumber;
						if (null != promoMap.get(orderEntry.getEntryNumber())) {
							lineItemList.add(mapInvoiceLineItem(splitEntry, orderEntry.getProduct(), productLineNumber, abstractOrderModel.getStore().getDefaultDeliveryOrigin(), promoMap.get(orderEntry.getEntryNumber()), calculatedOrderEntryDiscount, hasSplitQuantity, splitEntriesQuantity));
						} else {
							lineItemList.add(mapInvoiceLineItem(splitEntry, orderEntry.getProduct(), productLineNumber, abstractOrderModel.getStore().getDefaultDeliveryOrigin(), null, calculatedOrderEntryDiscount, hasSplitQuantity, splitEntriesQuantity));
						}
						
						if(null != splitEntry && null != splitEntry.getDeliveryMode()) {
							lineItemList.add(mapInvoiceShipMethodLineItemFromSplitEntry(splitEntry, splitEntry.getCode() + "-SHIP", ++lineNumber, abstractOrderModel.getStore().getDefaultDeliveryOrigin(), proratedDeliveryCostMap.get(splitEntry.getCode()), productLineNumber));
						} else {
							if(null != splitEntry && null != abstractOrderModel.getDeliveryMode()) {
								lineItemList.add(mapInvoiceShipMethodLineItemFromOrder(abstractOrderModel, splitEntry.getCode() + "-SHIP", ++lineNumber, abstractOrderModel.getStore().getDefaultDeliveryOrigin(),proratedDeliveryCostMap.get(splitEntry.getCode()), productLineNumber));
							}
						}
					}
				}
			}
		}
		return lineItemList;
	}
	
	private double getSplitEntriesQuantity(List<SplitEntryModel> splitEntryList) {
		double splitEntriesQuantity = 0;
		for (SplitEntryModel splitEntry : splitEntryList) {
			splitEntriesQuantity = splitEntriesQuantity + Double.parseDouble(splitEntry.getQty());
		}
		return splitEntriesQuantity;
	}

	private IndataLineType mapInvoiceLineItem(SplitEntryModel splitEntry, ProductModel productModel, int lineNumber, PointOfServiceModel wareHouseAddress, Double adjustedAmount, double calculatedOrderEntryDiscount, boolean hasSplitQuantity, double splitEntriesQuantity) {
		IndataLineType inDataLineType = null;
		DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
		if(null != splitEntry) {
			inDataLineType = new IndataLineType();
			inDataLineType.setID(splitEntry.getCode());
			inDataLineType.setLINENUMBER(BigDecimal.valueOf(lineNumber));
			inDataLineType.setPRODUCTCODE(splitEntry.getProductCode());
			if (!StringUtils.isEmpty(productModel.getExternalMaterialNumber())) {
				inDataLineType.setCOMMODITYCODE(productModel.getExternalMaterialNumber());
			} else {
				inDataLineType.setCOMMODITYCODE(splitEntry.getProductCode());
			}

			if (adjustedAmount == null) {
				if (hasSplitQuantity) {
					inDataLineType.setGROSSAMOUNT(decimalFormat.format((splitEntry.getPrice() - (Double.parseDouble(splitEntry.getQty()) / splitEntriesQuantity) * calculatedOrderEntryDiscount)));
				} else {
					inDataLineType.setGROSSAMOUNT(decimalFormat.format(splitEntry.getPrice() - calculatedOrderEntryDiscount));
				}
			}

			else {
				if (hasSplitQuantity) {
					inDataLineType.setGROSSAMOUNT(decimalFormat.format(adjustedAmount * Double.parseDouble(splitEntry.getQty()) - (Double.parseDouble(splitEntry.getQty()) / splitEntriesQuantity) * calculatedOrderEntryDiscount));
				} else {
					inDataLineType.setGROSSAMOUNT(decimalFormat.format(adjustedAmount * Double.parseDouble(splitEntry.getQty()) - calculatedOrderEntryDiscount));
				}
			}

			inDataLineType.setSHIPFROM(mapShipFromAddress(wareHouseAddress));
			inDataLineType.setSHIPTO(mapShipToAddress(splitEntry.getDeliveryAddress()));
		}
		return inDataLineType;
	}

	private IndataLineType mapInvoiceShipMethodLineItemFromSplitEntry(SplitEntryModel splitEntry, String line, int lineNumber, PointOfServiceModel wareHouseAddress, double proratedDeliveryCost, int relatedLineNumber) {
		IndataLineType inDataLineType = null;
		if (null != splitEntry) {
			inDataLineType = new IndataLineType();
			inDataLineType.setID(line);
			inDataLineType.setLINENUMBER(BigDecimal.valueOf(lineNumber));
			inDataLineType.setPRODUCTCODE(GpcommerceCoreConstants.ONE_SOURCE_TAX_DELIVERY_PRODUCT_CODE);
			inDataLineType.setRELATEDLINENUMBER(new BigDecimal(relatedLineNumber));
			inDataLineType.setGROSSAMOUNT(Double.toString(proratedDeliveryCost));
			inDataLineType.setSHIPFROM(mapShipFromAddress(wareHouseAddress));
			inDataLineType.setSHIPTO(mapShipToAddress(splitEntry.getDeliveryAddress()));
		}
		return inDataLineType;
	}

	private IndataLineType mapInvoiceShipMethodLineItemFromOrder(AbstractOrderModel abstractOrderModel, String line, int lineNumber, PointOfServiceModel wareHouseAddress, double proratedDeliveryCost, int relatedLineNumber) {
		IndataLineType inDataLineType = null;
		if (null != abstractOrderModel) {
			inDataLineType = new IndataLineType();
			inDataLineType.setID(line);
			inDataLineType.setPRODUCTCODE(GpcommerceCoreConstants.ONE_SOURCE_TAX_DELIVERY_PRODUCT_CODE);
			inDataLineType.setLINENUMBER(BigDecimal.valueOf(lineNumber));
			inDataLineType.setRELATEDLINENUMBER(new BigDecimal(relatedLineNumber));
			inDataLineType.setGROSSAMOUNT(Double.toString(proratedDeliveryCost));
			inDataLineType.setSHIPFROM(mapShipFromAddress(wareHouseAddress));
			inDataLineType.setSHIPTO(mapShipToAddress(abstractOrderModel.getDeliveryAddress()));
		}
		return inDataLineType;
	}

	private ZoneAddressType mapShipFromAddress(PointOfServiceModel wareHouseAddress) {

		ZoneAddressType shipFromAddress = null;
		if (null != wareHouseAddress) {
			AddressModel wareHouseAddrModel = wareHouseAddress.getAddress();
			if (null != wareHouseAddrModel) {
				shipFromAddress = new ZoneAddressType();
				shipFromAddress.setCOUNTRY(wareHouseAddrModel.getCountry().getIsocode());
				if (null != wareHouseAddrModel.getRegion()) {
					shipFromAddress.setSTATE(wareHouseAddrModel.getRegion().getIsocodeShort());
				}
				shipFromAddress.setCITY(wareHouseAddrModel.getTown());
				shipFromAddress.setPOSTCODE(wareHouseAddrModel.getPostalcode());
			}
		}
		return shipFromAddress;
	}

	private ZoneAddressType mapShipToAddress(AddressModel shiptToAddress) {
		ZoneAddressType shipToAddress = null;
		if (null != shiptToAddress) {
			shipToAddress = new ZoneAddressType();
			if (null != shiptToAddress.getCountry()) {
				shipToAddress.setCOUNTRY(shiptToAddress.getCountry().getIsocode());
			}

			if (null != shiptToAddress.getRegion()) {
				shipToAddress.setSTATE(shiptToAddress.getRegion().getIsocodeShort());
			}

			shipToAddress.setCITY(shiptToAddress.getTown());
			shipToAddress.setPOSTCODE(shiptToAddress.getPostalcode());
		}
		return shipToAddress;
	}

	

	private GPOneSourceTaxService getOneSourceTaxService() {
		OneSourceTaxCalculationService oneSourceTaxCalculationService = new OneSourceTaxCalculationService();
		final GPHeaderHandlerResolver gpHandlerResolver = new GPHeaderHandlerResolver(null, "OneSource");
		oneSourceTaxCalculationService.setHandlerResolver(gpHandlerResolver);
		GPOneSourceTaxService gpOneSourceTaxService = oneSourceTaxCalculationService.getTaxCalculationServicePort();
		return gpOneSourceTaxService;
	}
	
	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}

	public void setBaseSiteService(BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}
}
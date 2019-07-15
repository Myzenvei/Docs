/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.order.data.ConsignmentEntryData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.PromotionOrderEntryConsumedData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.jalo.Tracking;
import com.gp.commerce.core.model.TrackingModel;
import com.gp.commerce.facade.data.TrackingData;
import com.gp.commerce.facade.order.data.SplitEntryData;
import com.gp.commerce.facades.populators.GPCustomerPopulator;


/**
 * Velocity context for a Ready For Pickup notification email.
 */
public class DeliverySentEmailContext extends GPAbstractEmailContext<ConsignmentProcessModel>
{
	
	public static final String CREATEDDATE = "createdDate";
	public static final String CONSIGNMENT = "consignment";
	public static final String CONSIGNMENT_ENTRIES = "consignmentEntries";
	public static final String PROMOMAP = "promoMap";
	public static final String ORDER = "order";
	public static final String DELIVERY_MODE = "deliveryMode";
	public static final String DELIVERY_MODE_DESC = "deliveryModeDescription";
	public static final String TOTAL_DELIVERY_COST = "totalDeliveryCost";
	public static final String DESCRIPTION_VALUE_NOT_EMPTY = "descriptionValNotEmpty";
	public static final String TRACKING = "tracking";
	public static final String NON_EMPTY = "notempty";
	public static final String FIRSTNAME="firstName";

	private OrderData orderData;
	private TrackingData trackingData;
	private ConsignmentData consignmentData;
	private ConsignmentEntryData consignmentEntryData;
	private Converter<OrderModel, OrderData> orderConverter;
	private Converter<ConsignmentEntryModel, ConsignmentEntryData> consignmentEntryConverter;
	private Converter<TrackingModel, TrackingData> gpConsignmentTrackingConverter;
	private Converter<ConsignmentModel, ConsignmentData> consignmentConverter;
	List<ConsignmentEntryData> consignEntryDataList = new ArrayList<>();
	
	@Resource(name = "customerPopulator")
	private GPCustomerPopulator gpCustomerPopulator;

	@Override
	public void init(final ConsignmentProcessModel consignmentProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(consignmentProcessModel, emailPageModel);
		
		if(consignmentProcessModel.getConsignment() != null && consignmentProcessModel.getConsignment().getOrder() != null)
		{
			CustomerData customerData=new CustomerData();

			orderData = getOrderConverter().convert((OrderModel) consignmentProcessModel.getConsignment().getOrder());
			gpCustomerPopulator.populate((CustomerModel) consignmentProcessModel.getConsignment().getOrder().getUser(), customerData);
			put(FIRSTNAME,customerData.getFirstName());
			consignmentData = getConsignmentConverter().convert(consignmentProcessModel.getConsignment());
			Map<Integer, String> promoMap = new HashMap<>();
			for(PromotionResultData proData: orderData.getAppliedProductPromotions()) {
				for(PromotionOrderEntryConsumedData entry: proData.getConsumedEntries()) {
					promoMap.put(entry.getOrderEntryNumber(), proData.getDescription());
				}
				put(PROMOMAP, promoMap);
			}
		}
		
		if(orderData != null && CollectionUtils.isNotEmpty(consignmentProcessModel.getTrackings()))
		{
			consignmentProcessModel.getTrackings().forEach(trackingModel -> {
				trackingModel.getConsignmentEntries().forEach(consignEntry -> {
					consignmentEntryData = getConsignmentEntryConverter().convert(consignEntry);
					consignmentEntryData.setShippedQuantity(trackingModel.getQuantityShipped());
					
					boolean deliveryModeFlag = false;
					if(CollectionUtils.isNotEmpty(consignmentEntryData.getOrderEntry().getSplitEntries())) {
						for(SplitEntryData splitEntry : consignmentEntryData.getOrderEntry().getSplitEntries()) {
							if(splitEntry.getDeliveryMode() != null && splitEntry.getDeliveryAddress() != null 
									&& splitEntry.getDeliveryAddress().getId().equals(consignmentData.getShippingAddress().getId())) {
								put(DELIVERY_MODE,splitEntry.getDeliveryMode());
								if(StringUtils.isNotEmpty(splitEntry.getDeliveryInstruction()))
								{
									put(DESCRIPTION_VALUE_NOT_EMPTY,NON_EMPTY);
									put(DELIVERY_MODE_DESC,splitEntry.getDeliveryInstruction());
								}
								put(TOTAL_DELIVERY_COST,orderData.getTotalDeliveryCost());
								deliveryModeFlag = true;
								break;
							}
						}
					}
					if(!deliveryModeFlag && orderData.getDeliveryMode() != null)
					{
						put(DELIVERY_MODE,orderData.getDeliveryMode());
						if(StringUtils.isNotEmpty(orderData.getDeliveryMode().getDescription()))
						{
							put(DESCRIPTION_VALUE_NOT_EMPTY,NON_EMPTY);
							put(DELIVERY_MODE_DESC,orderData.getDeliveryMode().getDescription());
						}
						put(TOTAL_DELIVERY_COST, (orderData.getDeliveryCost() != null) ? orderData.getDeliveryCost().getFormattedValue() : StringUtils.EMPTY);
					}
					consignEntryDataList.add(consignmentEntryData);
			});
		 });
		 trackingData = getGpConsignmentTrackingConverter().convert((TrackingModel) consignmentProcessModel.getTrackings().toArray()[0]);	
		}
		
		put(CONSIGNMENT,consignmentData);
		put(TRACKING,trackingData);
		put(CONSIGNMENT_ENTRIES,consignEntryDataList);
		put(ORDER,orderData);
		put(CREATEDDATE, getCreateDate(orderData.getCreated()));
		put(GpcommerceCoreConstants.CONTACT_NUMBER, getCustomerNumber());
	}
	
	private String getCreateDate(Date date) {
	    SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");  
	    String createdDate= formatter.format(date);  
		return createdDate;
	}

	@Override
	protected BaseSiteModel getSite(final ConsignmentProcessModel consignmentProcessModel)
	{
		return consignmentProcessModel.getConsignment().getOrder().getSite();
	}

	@Override
	protected CustomerModel getCustomer(final ConsignmentProcessModel consignmentProcessModel)
	{
		return (CustomerModel) consignmentProcessModel.getConsignment().getOrder().getUser();
	}

	@Override
	protected LanguageModel getEmailLanguage(final ConsignmentProcessModel consignmentProcessModel)
	{
		if (consignmentProcessModel.getConsignment().getOrder() instanceof OrderModel)
		{
			return ((OrderModel) consignmentProcessModel.getConsignment().getOrder()).getLanguage();
		}

		return null;
	}
	
	public Converter<ConsignmentEntryModel, ConsignmentEntryData> getConsignmentEntryConverter()
	{
		return consignmentEntryConverter;
	}

	@Required
	public void setConsignmentEntryConverter(final Converter<ConsignmentEntryModel, ConsignmentEntryData> consignmentEntryConverter)
	{
		this.consignmentEntryConverter = consignmentEntryConverter;
	}

	public Converter<OrderModel, OrderData> getOrderConverter() {
		return orderConverter;
	}

	@Required
	public void setOrderConverter(Converter<OrderModel, OrderData> orderConverter) {
		this.orderConverter = orderConverter;
	}

	public Converter<TrackingModel, TrackingData> getGpConsignmentTrackingConverter() {
		return gpConsignmentTrackingConverter;
	}

	@Required
	public void setGpConsignmentTrackingConverter(Converter<TrackingModel, TrackingData> gpConsignmentTrackingConverter) {
		this.gpConsignmentTrackingConverter = gpConsignmentTrackingConverter;
	}

	public Converter<ConsignmentModel, ConsignmentData> getConsignmentConverter() {
		return consignmentConverter;
	}

	@Required
	public void setConsignmentConverter(Converter<ConsignmentModel, ConsignmentData> consignmentConverter) {
		this.consignmentConverter = consignmentConverter;
	}
	
}

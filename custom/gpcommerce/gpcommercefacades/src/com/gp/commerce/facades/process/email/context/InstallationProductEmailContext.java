/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.process.email.context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.facade.order.data.SplitEntryData;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.coupon.data.CouponData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.PromotionOrderEntryConsumedData;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.acceleratorservices.email.EmailService;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import com.gp.commerce.core.services.GPCMSSiteService;

public class InstallationProductEmailContext extends GPAbstractEmailContext<OrderProcessModel>
{
	private Converter<OrderModel, OrderData> orderConverter;
	private OrderData orderData;
	private List<CouponData> giftCoupons;
	public static final String SPLITENTRYSIZE = "splitEntrySize";
	public static final String SPLITENTRY = "splitEntry";
	public static final String SINGLESHIPIING = "singleShipping";
	public static final String CREATEDDATE = "createdDate";
	public static final String PROMOMAP = "promoMap";
    public static final String DESCRIPTIONVALUENOTEMPTY = "descriptionValNotEmpty";
	public static final String INSTALLATIONVALUENOTEMPTY ="installationNotEmpty";
	public static final String TOTAL_DELIVERY_COST = "totalDeliveryCost";
	public static final String DELIVERY_MODE_DESC = "deliveryModeDescription";
	public static final String NOT_EMPTY = "notempty";
	
	private EmailService emailService;
	private static final String INSTALLATION_NOTIFICATION_EMAIL_ID = "INSTALLATION_NOTIFICATION_EMAIL_ID";
	private static final String INSTALLATION_PRODUCT = "INSTALLATION_PRODUCT";
	private static final String CAPITALIZED_INSTALLATION_TIME = "installationTime";
	private static final String INSTALLATION_DATE = "installationDate";
	
	private static final Logger LOG = Logger.getLogger(InstallationProductEmailContext.class);
	
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;

	@Override
	public void init(final OrderProcessModel orderProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(orderProcessModel, emailPageModel);
		orderData = getOrderConverter().convert(orderProcessModel.getOrder());
		if(null != orderData.getScheduleInstallation() && StringUtils.isNotEmpty(orderData.getScheduleInstallation().getName())) {
			LOG.info("Inside null check of schedule installation product of init method");
			put(INSTALLATIONVALUENOTEMPTY,"installationNotEmpty");
		}
		put(SINGLESHIPIING,getDeliveryGroup(orderData));
		put(CREATEDDATE, getCreateDate(orderData.getCreated()));
		put(GpcommerceCoreConstants.CONTACT_NUMBER, getCustomerNumber());

		final Map<Integer, String> promoMap = new HashMap<>();
		for(final PromotionResultData proData: orderData.getAppliedProductPromotions()) {
			for(final PromotionOrderEntryConsumedData entry: proData.getConsumedEntries()) {
				promoMap.put(entry.getOrderEntryNumber(), proData.getDescription());
			}
		}
		put(PROMOMAP, promoMap);

		if(orderData.getEntries()!= null)
		{
			final List<OrderEntryData> entries = orderData.getEntries();
			for(final OrderEntryData entry :entries)
			{
				put(SPLITENTRYSIZE,entry.getSplitEntries().size());
				if(CollectionUtils.isNotEmpty(entry.getSplitEntries()))
				{
					put(SPLITENTRY,entry.getSplitEntries());
				}
			}
		}
		giftCoupons = orderData.getAppliedOrderPromotions().stream()
				.filter(x -> CollectionUtils.isNotEmpty(x.getGiveAwayCouponCodes())).flatMap(p -> p.getGiveAwayCouponCodes().stream())
				.collect(Collectors.toList());

		if (null != orderData.getEntries()) {
			orderData.getEntries().forEach(entry -> {
				if (null != entry.getSplitEntries()) {
					entry.getSplitEntries().forEach(splitEntry -> {
						if (null != splitEntry.getDeliveryMode()
								&& StringUtils.isNotEmpty(splitEntry.getDeliveryInstruction())) {
							put(TOTAL_DELIVERY_COST, orderData.getTotalDeliveryCost());
							put(DESCRIPTIONVALUENOTEMPTY, NOT_EMPTY);
							put(DELIVERY_MODE_DESC, splitEntry.getDeliveryInstruction());
						} else {
							put(TOTAL_DELIVERY_COST, (orderData.getDeliveryCost() != null) ? orderData.getDeliveryCost().getFormattedValue() : StringUtils.EMPTY);
							if (null != orderData.getDeliveryMode() && StringUtils.isNotEmpty(orderData.getDeliveryMode().getDescription())) {
								put(DESCRIPTIONVALUENOTEMPTY, NOT_EMPTY);
								put(DELIVERY_MODE_DESC, orderData.getDeliveryMode().getDescription());
							}
						}
					});
				}
			});
		}
		if(null != orderData.getScheduleInstallation() && "b2bwhitelabel".equalsIgnoreCase(orderData.getSite())) {
			String gpBusinessUserEmail = cmsSiteService.getSiteConfig("gp.businessuser.email.id");
			put(INSTALLATION_PRODUCT, true);
			final List<EmailAddressModel> toEmails = new ArrayList<>();
			EmailAddressModel toAddress;
			if(StringUtils.isNotEmpty(gpBusinessUserEmail)) {
				LOG.info("gpBusinessUserEmail:"+gpBusinessUserEmail);
				toAddress = getEmailService().getOrCreateEmailAddressForEmail(gpBusinessUserEmail, StringUtils.EMPTY);
				toEmails.add(toAddress);
				put(INSTALLATION_NOTIFICATION_EMAIL_ID, toEmails);
			}
			put(CAPITALIZED_INSTALLATION_TIME, StringUtils.capitalize(orderData.getScheduleInstallation().getPreferredTime()));
			put(INSTALLATION_DATE, formatInstallationDate(orderData.getScheduleInstallation().getPreferredDate()));
		}
	}

	private String getCreateDate(final Date date) {
	    final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
	    final String createdDate= formatter.format(date);
		return createdDate;
	}
	
	private String formatInstallationDate(String installationDateString) {
		DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
		Date installationDate = null;
		try {
			installationDate = format.parse(installationDateString.replaceAll("\\bMonday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday|st|nd|rd|th\\b", "").trim());
		} catch(ParseException e) {
			LOG.error("Installation date parsing failed with exception:", e);
		}
		final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		LOG.info("Formatted installation date:"+dateFormatter.format(installationDate));
	    return dateFormatter.format(installationDate);
	}

	private Integer getDeliveryGroup(final OrderData order) {
		final Map<String, String> addressKey = new HashMap<>();
		if (CollectionUtils.isNotEmpty(order.getEntries())) {
			for (final OrderEntryData entry : order.getEntries()) {
				if (CollectionUtils.isNotEmpty(entry.getSplitEntries())) {
					for (final SplitEntryData splitEntry : entry.getSplitEntries()) {
						final String key = splitEntry.getDeliveryAddress().getId() ;
						if (null == addressKey.get(key)) {
							addressKey.put(key, entry.getEntryNumber().toString());
						}
					}
				}
			}
		}
		return addressKey.size();
	}
	@Override
	protected BaseSiteModel getSite(final OrderProcessModel orderProcessModel)
	{
		return orderProcessModel.getOrder().getSite();
	}

	@Override
	protected CustomerModel getCustomer(final OrderProcessModel orderProcessModel)
	{
		return (CustomerModel) orderProcessModel.getOrder().getUser();
	}

	protected Converter<OrderModel, OrderData> getOrderConverter()
	{
		return orderConverter;
	}

	@Required
	public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

	public OrderData getOrder()
	{
		return orderData;
	}

	@Override
	protected LanguageModel getEmailLanguage(final OrderProcessModel orderProcessModel)
	{
		return orderProcessModel.getOrder().getLanguage();
	}

	public List<CouponData> getCoupons()
	{
		return giftCoupons;
	}

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public GPCMSSiteService getCmsSiteService() {
		return cmsSiteService;
	}

	public void setCmsSiteService(GPCMSSiteService cmsSiteService) {
		this.cmsSiteService = cmsSiteService;
	}
	
}
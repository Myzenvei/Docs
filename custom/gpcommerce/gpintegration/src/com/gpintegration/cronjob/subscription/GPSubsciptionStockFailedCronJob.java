/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.cronjob.subscription;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.cybersource.schemas.transaction_data_1.ReplyMessage;
import com.gp.commerce.core.enums.SubscriptionCartStatusEnum;
import com.gp.commerce.core.exceptions.GPException;
import com.gp.commerce.core.model.GPSubscriptionCartModel;
import com.gp.commerce.core.model.PaypalPaymentInfoModel;
import com.gp.commerce.core.stock.impl.GPCommerceStockServiceImpl;
import com.gp.commerce.core.util.GPCommerceCoreUtils;
import com.gp.commerce.subscription.service.GPSubscriptionService;
import com.gp.commerce.subscription.service.impl.GPSubscriptionServiceImpl;
import com.gpintegration.service.GPCybersourceIntegrationService;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.contextualattributevalues.model.ContextualAttributesContextModel;
import de.hybris.platform.contextualattributevalues.services.ContextualAttributeValuesSessionService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;

public class GPSubsciptionStockFailedCronJob extends AbstractJobPerformable<CronJobModel> {

	private static Logger LOG = Logger.getLogger(GPSubsciptionStockFailedCronJob.class);

	@Resource(name = "subscriptionCartServiceImpl")
	private GPSubscriptionServiceImpl subscriptionCartServiceImpl;
	@Resource(name = "gpSubscriptionOrder")
	private GPSubscriptionOrder gpSubscriptionOrder;
	
	private GPCommerceCoreUtils gpCommerceCoreUtils;
	
	private ContextualAttributeValuesSessionService contextualAttributeValuesSessionService;

	@Override
	public PerformResult perform(final CronJobModel cronjob) {
		LOG.info("-----------------inside subrscription Cronjob---------------------");
		
		List<GPSubscriptionCartModel> subscriptionCartModelList = subscriptionCartServiceImpl
				.getSubscriptionCartModels(SubscriptionCartStatusEnum.OUTOFSTOCK.getCode(),true);
		LOG.info("Number of Failed Subscription orders to be reprocessed: "+subscriptionCartModelList.size());
		if (CollectionUtils.isNotEmpty(subscriptionCartModelList)) {
			for (final GPSubscriptionCartModel subscriptionCartModel : subscriptionCartModelList) {
				try {
					if (clearAbortRequestedIfNeeded( cronjob))
					{
						LOG.debug("The job is aborted.");
						return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
					}
					if(!gpSubscriptionOrder.placeOrder(subscriptionCartModel)) {
						LOG.error("Place Order failed for subscription : "+subscriptionCartModel.getCode());
					}
					else {
						subscriptionCartModel.setSubscriptionCartStatus(SubscriptionCartStatusEnum.ACTIVE);
						subscriptionCartModel.setPaymentTransactions(null);
						subscriptionCartModel.setNextActivationDate(calculateNextFrequencyDate(subscriptionCartModel));
						modelService.save(subscriptionCartModel);
						LOG.info("Order placed successful for subscription : "+subscriptionCartModel.getCode());
					}
				} catch (final Exception e) {
					LOG.error(e.getMessage(), e);
				}

			}
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	public Date calculateNextFrequencyDate(GPSubscriptionCartModel subscriptionCartModel) {
		Map<String, String> frequencyMap = subscriptionCartModel.getFrequency();
		int frequency = 0;
		Iterator<Entry<String, String>> iterator = frequencyMap.entrySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().getKey();
			frequency = Integer.valueOf(frequencyMap.get(key));
		}
		final ContextualAttributesContextModel currentContext =subscriptionCartModel.getStore().getContextualAttributesContext();
		getContextualAttributeValuesSessionService().setCurrentContext(currentContext);
		Integer nDaysBeforeSubscription=  (Integer) getGpCommerceCoreUtils().getProductAttribute(subscriptionCartModel.getEntries().get(0).getProduct(), "nDaysBeforeSubscription");
		if(nDaysBeforeSubscription==null) {
			nDaysBeforeSubscription = new Integer(0);
		}
		LocalDate nextDate = LocalDate.now().plusDays(frequency);
		LOG.info("NextDate: "+nextDate);
		return Date.from(nextDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	public GPSubscriptionOrder getGpSubscriptionOrder() {
		return gpSubscriptionOrder;
	}

	public void setGpSubscriptionOrder(GPSubscriptionOrder gpSubscriptionOrder) {
		this.gpSubscriptionOrder = gpSubscriptionOrder;
	}

	public GPSubscriptionServiceImpl getSubscriptionCartServiceImpl() {
		return subscriptionCartServiceImpl;
	}

	public void setSubscriptionCartServiceImpl(GPSubscriptionServiceImpl subscriptionCartServiceImpl) {
		this.subscriptionCartServiceImpl = subscriptionCartServiceImpl;
	}

	public GPCommerceCoreUtils getGpCommerceCoreUtils() {
		return gpCommerceCoreUtils;
	}

	public void setGpCommerceCoreUtils(GPCommerceCoreUtils gpCommerceCoreUtils) {
		this.gpCommerceCoreUtils = gpCommerceCoreUtils;
	}

	public ContextualAttributeValuesSessionService getContextualAttributeValuesSessionService() {
		return contextualAttributeValuesSessionService;
	}

	public void setContextualAttributeValuesSessionService(
			ContextualAttributeValuesSessionService contextualAttributeValuesSessionService) {
		this.contextualAttributeValuesSessionService = contextualAttributeValuesSessionService;
	}
	@Override
	public boolean isAbortable() {
		return true;
	} 
}

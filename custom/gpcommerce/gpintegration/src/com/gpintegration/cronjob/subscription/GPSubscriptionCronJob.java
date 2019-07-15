/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.cronjob.subscription;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.cybersource.schemas.transaction_data_1.ReplyMessage;
import com.gp.commerce.core.enums.SubscriptionCartStatusEnum;
import com.gp.commerce.core.exceptions.GPException;
import com.gp.commerce.core.model.GPSubscriptionCartModel;
import com.gp.commerce.core.model.GPSubscriptionNextOrderEmailProcessModel;
import com.gp.commerce.core.model.PaypalPaymentInfoModel;
import com.gp.commerce.core.order.impl.GPDefaultCommerceCartCalculationStrategy;

import de.hybris.platform.contextualattributevalues.services.ContextualAttributeValuesSessionService;
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
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.contextualattributevalues.model.ContextualAttributesContextModel;

public class GPSubscriptionCronJob extends AbstractJobPerformable<CronJobModel> {

	private static Logger LOG = Logger.getLogger(GPSubscriptionCronJob.class);

	private GPSubscriptionServiceImpl subscriptionCartServiceImpl;

	private GPCommerceCoreUtils gpCommerceCoreUtils;
	
	private BusinessProcessService businessProcessService;

	private ContextualAttributeValuesSessionService contextualAttributeValuesSessionService;

	private GPSubscriptionOrder gpSubscriptionOrder;

	@Resource(name="promotionsService")
	private PromotionsService promotionsService;
	
	@Resource(name="timeService")
	private TimeService timeService;
	
	@Resource(name="commerceCartCalculationStrategy")
	private GPDefaultCommerceCartCalculationStrategy commerceCartCalculationStrategy ;
 
	
	@Override
	public PerformResult perform(final CronJobModel cronjob) {
		LOG.info("-----------------inside subrscription Cronjob---------------------");
		final List<GPSubscriptionCartModel> subscriptionCartModelList = getSubscriptionCartServiceImpl()
				.getSubscriptionCartModels(SubscriptionCartStatusEnum.ACTIVE.getCode(),true);
		LOG.info("No : of Subscription orders to be created: "+subscriptionCartModelList.size());
		if (CollectionUtils.isNotEmpty(subscriptionCartModelList)) {
			for (final GPSubscriptionCartModel subscriptionCartModel : subscriptionCartModelList) {
				try {
					if (clearAbortRequestedIfNeeded( cronjob))
					{
						LOG.debug("The job is aborted.");
						return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
					}
					if (isReadyForProcess(subscriptionCartModel)) {
						LOG.info("Order in process for cart: "+subscriptionCartModel.getCode());
					
						if (!gpSubscriptionOrder.placeOrder(subscriptionCartModel)) {
							LOG.error("Place Order failed for subscription : " + subscriptionCartModel.getCode());
						} else {
							subscriptionCartModel.setSubscriptionCartStatus(SubscriptionCartStatusEnum.ACTIVE);
							subscriptionCartModel.setPaymentTransactions(null);
							subscriptionCartModel.setNextActivationDate(calculateNextFrequencyDate(subscriptionCartModel));
							modelService.save(subscriptionCartModel);
							calculateNdaysValueAndTriggerMail(subscriptionCartModel);
							LOG.info("Order placed successful for subscription : " + subscriptionCartModel.getCode());
						}}
					else {
						LOG.info("This cart is not scheduled today so skipping cartID: "+subscriptionCartModel.getCode());
					}
					
					promotionsService.updatePromotions(new ArrayList<PromotionGroupModel>(), subscriptionCartModel, true, PromotionsManager.AutoApplyMode.APPLY_ALL,
	                        PromotionsManager.AutoApplyMode.APPLY_ALL, timeService.getCurrentTime());
				} catch (final Exception e) {
					LOG.error(e.getMessage(), e);
				}
				
			}
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}
	public Date calculateNextFrequencyDate(GPSubscriptionCartModel subscriptionCartModel) {
		LOG.info("Entering into Next Gen Cal");
		Map<String, String> frequencyMap = subscriptionCartModel.getFrequency();
		int frequency = 0;
		Iterator<Entry<String, String>> iterator = frequencyMap.entrySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().getKey();
			frequency = Integer.valueOf(frequencyMap.get(key));
		}
		LOG.info("Calculating freq: "+frequency);
		Integer nDaysBeforeSubscription=  (Integer) getGpCommerceCoreUtils().getProductAttribute(subscriptionCartModel.getEntries().get(0).getProduct(), "nDaysBeforeSubscription");
		if(nDaysBeforeSubscription==null) {
			nDaysBeforeSubscription = new Integer(0);
		}
		LOG.info(" nDaysBeforeSubscription: "+nDaysBeforeSubscription);
		LocalDate nextDate = LocalDate.now().plusDays(frequency).plusDays(nDaysBeforeSubscription.intValue());
		LOG.info("NextDate: "+nextDate);
		return Date.from(nextDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public boolean isReadyForProcess(GPSubscriptionCartModel subscriptionCartModel) {
		try {
			Optional<AbstractOrderEntryModel> cartEntry = subscriptionCartModel.getEntries().stream().findFirst();
			final ContextualAttributesContextModel currentContext = subscriptionCartModel.getStore()
					.getContextualAttributesContext();
			getContextualAttributeValuesSessionService().setCurrentContext(currentContext);
			Integer nDays = new Integer(0);
			if (null != cartEntry.get().getProduct()) {
				if(null!=getGpCommerceCoreUtils().getProductAttribute(cartEntry.get().getProduct(),"nDaysBeforeSubscription"))
					nDays = (Integer) getGpCommerceCoreUtils().getProductAttribute(cartEntry.get().getProduct(),"nDaysBeforeSubscription");
				LOG.info(" nDaysBeforeSubscription: " + nDays);
			}
			return checkIfActivationDateIsAfterNdays(nDays,subscriptionCartModel);
		} catch (Exception e) {
			LOG.error("Subscription isReadyForProcess is failed: " + e.getMessage(), e);
			return false;
		}

	}

	private void calculateNdaysValueAndTriggerMail(GPSubscriptionCartModel cartModel) {
		triggerEmailForSubscriptionAfterNdays(cartModel);
	}

	private void triggerEmailForSubscriptionAfterNdays(GPSubscriptionCartModel cartModel) {
		final GPSubscriptionNextOrderEmailProcessModel nextOrderOfSubscription = getBusinessProcessService().createProcess(
				"sendEmailBeforeNdaysOfSubscription-" + cartModel.getCode() + "-" + System.currentTimeMillis(),
				"sendEmailBeforeNdaysOfSubscription");
		nextOrderOfSubscription.setSubscriptionCart(cartModel);
		nextOrderOfSubscription.setSite(cartModel.getSite());
		nextOrderOfSubscription.setStore(cartModel.getStore());
		nextOrderOfSubscription.setCurrency(cartModel.getCurrency());
		nextOrderOfSubscription.setLanguage(cartModel.getSite().getDefaultLanguage());
		nextOrderOfSubscription.setCustomer((CustomerModel)cartModel.getUser());
		modelService.save(nextOrderOfSubscription);
		getBusinessProcessService().startProcess(nextOrderOfSubscription);
	}

	private boolean checkIfActivationDateIsAfterNdays(Integer nDays, GPSubscriptionCartModel subscriptionCartModel) {
		Calendar currentDate=Calendar.getInstance();
		currentDate.set(Calendar.HOUR_OF_DAY, 0); 
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.MILLISECOND, 0);
		if(nDays!=null)
			currentDate.add(Calendar.DAY_OF_YEAR, nDays.intValue());
		else
			currentDate.add(Calendar.DAY_OF_YEAR, 0);
		Calendar activationDate = Calendar.getInstance();
		activationDate.setTime(subscriptionCartModel.getNextActivationDate());
		activationDate.set(Calendar.HOUR_OF_DAY, 0); 
		activationDate.set(Calendar.MINUTE, 0);
		activationDate.set(Calendar.SECOND, 0);
		activationDate.set(Calendar.MILLISECOND, 0);	
		if(currentDate.equals(activationDate) || currentDate.after(activationDate)) {
			return true;
		}
		return false;
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

	
	public BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}

	public void setBusinessProcessService(BusinessProcessService businessProcessService) {
		this.businessProcessService = businessProcessService;
	}

	public ContextualAttributeValuesSessionService getContextualAttributeValuesSessionService() {
		return contextualAttributeValuesSessionService;
	}

	public void setContextualAttributeValuesSessionService(
			ContextualAttributeValuesSessionService contextualAttributeValuesSessionService) {
		this.contextualAttributeValuesSessionService = contextualAttributeValuesSessionService;
	}



}

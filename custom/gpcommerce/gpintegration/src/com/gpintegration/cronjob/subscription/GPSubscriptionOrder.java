/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.cronjob.subscription;

import static de.hybris.platform.multicountry.constants.MulticountryConstants.AVAILABILITY_GROUPS;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.cybersource.schemas.transaction_data_1.ReplyMessage;
import com.gp.commerce.core.calculation.service.impl.GPCalculationService;
import com.gp.commerce.core.enums.MaterialStatusEnum;
import com.gp.commerce.core.enums.SubscriptionCartStatusEnum;
import com.gp.commerce.core.exceptions.GPException;
import com.gp.commerce.core.model.GPSubscriptionCartModel;
import com.gp.commerce.core.model.ShippingRestrictionModel;
import com.gp.commerce.core.model.SubscriptionCartProcessModel;
import com.gp.commerce.core.services.GPCartService;
import com.gp.commerce.core.services.event.SubscriptionOrderStatusEvent;
import com.gp.commerce.core.stock.impl.GPCommerceStockServiceImpl;
import com.gp.commerce.core.util.GPCommerceCoreUtils;
import com.gp.commerce.subscription.service.GPSubscriptionService;
import com.gp.commerce.subscription.service.impl.GPSubscriptionServiceImpl;
import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.service.GPCybersourceIntegrationService;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.contextualattributevalues.model.ContextualAttributesContextModel;
import de.hybris.platform.contextualattributevalues.services.ContextualAttributeValuesSessionService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

public class GPSubscriptionOrder {
	private static final String TRANSACTION_STATUS_SUCCESS = "ACCEPTED";
	private static final String SUBSCRIPTION_ORDER_STATUS_EMAIL_PROCESS = "subscriptionOrderStatusEmailProcess";
	private static final String SUBSCRIPTION_ORDER_STATUS_EMAIL_PROCESS_LABEL = "subscriptionOrderStatusEmailProcess-";
	private static final String SUBSCRIPTION_ORDER_CANCELLED_EMAIL_PROCESS = "subscriptionOrderCancelledEmailProcess";
	private static final String SUBSCRIPTION_ORDER_CANCELLED_EMAIL_PROCESS_LABEL = "subscriptionOrderCancelledEmailProcess-";

	private Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoConverter;

	private static Logger LOG = Logger.getLogger(GPSubscriptionOrder.class);

	@Resource(name = "commerceCheckoutService")
	private CommerceCheckoutService commerceCheckoutService;

	@Resource(name = "subscriptionCartServiceImpl")
	private GPSubscriptionServiceImpl subscriptionCartServiceImpl;

	private ModelService modelService;
	private GPCybersourceIntegrationService gpCyberSource;
	
	@Resource(name="gpCommerceStockServiceImpl")
	GPCommerceStockServiceImpl gpCommerceStockServiceImpl;
	
	@Resource(name = "businessProcessService")
	protected BusinessProcessService businessProcessService;
	
	@Resource(name = "eventService")
	protected EventService eventService;
	
	@Resource(name="gpCommerceCoreUtils")
	private GPCommerceCoreUtils gpCommerceCoreUtils;
	
	@Resource(name="gpCalculationService")
	private GPCalculationService gpCalculationService;
	
	@Resource(name="deliveryService")
	private DeliveryService deliveryService;
	
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "gpCartService")
	private GPCartService gpCartService;
	
	private SessionService sessionService;
	
	private ContextualAttributeValuesSessionService contextualAttributeValuesSessionService;

	private static final String DISCONTINUED_REPLACED_EMAIL_PROCESS = "discontinuedReplacedProductEmailProcess";
	private static final String DISCONTINUED_REPLACED_EMAIL_PROCESS_LABEL = "discontinuedReplacedProductEmailProcess-";

	/**
	 * @param subscriptionCartModel
	 */
	public boolean placeOrder(final GPSubscriptionCartModel subscriptionCartModel) {
		try {
			AbstractOrderEntryModel abstractOrderEntryModel = subscriptionCartModel.getEntries().get(0);
			
			sessionService.getCurrentSession().setAttribute(AVAILABILITY_GROUPS, subscriptionCartModel.getStore().getAvailabilityGroups());
			sessionService.getCurrentSession().setAttribute("soldToRootcategory", "DEFAULT");
			sessionService.getCurrentSession().setAttribute("soldToUnitId", "DEFAULT");
			sessionService.getCurrentSession().setAttribute("Europe1PriceFactory_UPG", subscriptionCartModel.getStore().getUserPriceGroup());
			
			final ContextualAttributesContextModel currentContext =subscriptionCartModel.getStore().getContextualAttributesContext();
			getContextualAttributeValuesSessionService().setCurrentContext(currentContext);
			
			Integer minOrderQuantity = (getGpCommerceCoreUtils().getProductAttribute(abstractOrderEntryModel.getProduct(), "minOrderQuantity")==null)?new Integer(1):
				(Integer)getGpCommerceCoreUtils().getProductAttribute(abstractOrderEntryModel.getProduct(), "minOrderQuantity");
			
			Integer maxOrderQuantity = (getGpCommerceCoreUtils().getProductAttribute(abstractOrderEntryModel.getProduct(), "maxOrderQuantity")==null)?new Integer(100000):
				(Integer)getGpCommerceCoreUtils().getProductAttribute(abstractOrderEntryModel.getProduct(), "maxOrderQuantity");
			
			final SubscriptionCartProcessModel subscriptionCartProcessModel = (SubscriptionCartProcessModel) businessProcessService.createProcess(
					SUBSCRIPTION_ORDER_STATUS_EMAIL_PROCESS_LABEL + subscriptionCartModel.getCode() + "-" + System.currentTimeMillis(),
					SUBSCRIPTION_ORDER_STATUS_EMAIL_PROCESS);
			
			final SubscriptionCartProcessModel subscriptionCartCancelProcessModel = (SubscriptionCartProcessModel) businessProcessService.createProcess(
					SUBSCRIPTION_ORDER_CANCELLED_EMAIL_PROCESS_LABEL + subscriptionCartModel.getCode() + "-" + System.currentTimeMillis(),
					SUBSCRIPTION_ORDER_CANCELLED_EMAIL_PROCESS);
			
			if(minOrderQuantity==null) {
				minOrderQuantity=new Integer(1);
			}
			if(maxOrderQuantity==null) {
				maxOrderQuantity = new Integer(Integer.MAX_VALUE);
			}
			if(!(boolean)getGpCommerceCoreUtils().getProductAttribute(subscriptionCartModel.getEntries().get(0).getProduct(), "subscribable")) {
				subscriptionCartModel.setSubscriptionCartStatus(SubscriptionCartStatusEnum.CANCELLED);
				subscriptionCartModel.setSubscriptionCancellationReason(configurationService.getConfiguration().getString(GpintegrationConstants.SUBSCRIPTION_REASON_NOT_SUBSCRIBLE));
				subscriptionCartModel.setPaymentTransactions(null);
				subscriptionCartModel.setIsActive(false);
				getModelService().save(subscriptionCartModel);
				LOG.error("Order failed due to Product is not subscribable");
				subscriptionCartProcessModel.setSubscriptionCart(subscriptionCartModel);
				final SubscriptionOrderStatusEvent subscriptionStatusEvent = new SubscriptionOrderStatusEvent(subscriptionCartProcessModel);
				eventService.publishEvent(subscriptionStatusEvent);
				return false;
			}
			List<ShippingRestrictionModel> shippingRestrict= getGpCartService().fetchShippingRestrictions(subscriptionCartModel.getEntries().get(0).getProduct().getCode(), 
					subscriptionCartModel.getDeliveryAddress().getCountry().getIsocode(),subscriptionCartModel.getDeliveryAddress().getRegion().getIsocodeShort());
			boolean isRestricted = false;
			if (shippingRestrict != null) {
				for (ShippingRestrictionModel shipping : shippingRestrict) {
					if (shipping.getRegion()
							.equals(subscriptionCartModel.getDeliveryAddress().getRegion().getIsocodeShort()))
						isRestricted = true;
				}
			}
			if(isRestricted) {
				subscriptionCartModel.setSubscriptionCartStatus(SubscriptionCartStatusEnum.CANCELLED);
				subscriptionCartModel.setSubscriptionCancellationReason(configurationService.getConfiguration().getString(GpintegrationConstants.SUBSCRIPTION_REASON_SHIP_RESTRICT));
				subscriptionCartModel.setPaymentTransactions(null);
				subscriptionCartModel.setIsActive(false);
				getModelService().save(subscriptionCartModel);
				LOG.error("Order failed due to Product is shipping restricted");
				subscriptionCartCancelProcessModel.setSubscriptionCart(subscriptionCartModel);
				subscriptionCartCancelProcessModel.setSite(subscriptionCartModel.getSite());
				modelService.save(subscriptionCartCancelProcessModel);
				businessProcessService.startProcess(subscriptionCartCancelProcessModel);
				return false;
			}
			if(abstractOrderEntryModel.getProduct().getMaterialStatus().getCode().equalsIgnoreCase(MaterialStatusEnum.OBSOLETE.getCode())
					&& subscriptionCartModel.getSubscriptionCartStatus().equals(SubscriptionCartStatusEnum.ACTIVE)) {
				subscriptionCartModel.setSubscriptionCartStatus(SubscriptionCartStatusEnum.CANCELLED);
				subscriptionCartModel.setSubscriptionCancellationReason(configurationService.getConfiguration().getString(GpintegrationConstants.SUBSCRIPTION_REASON_DISC));
				subscriptionCartModel.setPaymentTransactions(null);
				subscriptionCartModel.setIsActive(false);
				getModelService().save(subscriptionCartModel);
				LOG.error("Order failed due to Product is discontinued");
				final SubscriptionCartProcessModel subscriptionOrderProcessModel ;
				subscriptionOrderProcessModel = (SubscriptionCartProcessModel) businessProcessService.createProcess(
						DISCONTINUED_REPLACED_EMAIL_PROCESS_LABEL + subscriptionCartModel.getCode() + "-" + System.currentTimeMillis(),
						DISCONTINUED_REPLACED_EMAIL_PROCESS);
				subscriptionOrderProcessModel.setSubscriptionCart(subscriptionCartModel);
				subscriptionOrderProcessModel.setSite(subscriptionCartModel.getSite());
				modelService.save(subscriptionOrderProcessModel);
				businessProcessService.startProcess(subscriptionOrderProcessModel);
				LOG.error("Product is discontinued");
				return false;
			}
			if(!(abstractOrderEntryModel.getQuantity()>=minOrderQuantity && abstractOrderEntryModel.getQuantity()<=maxOrderQuantity)) {
				subscriptionCartModel.setSubscriptionCartStatus(SubscriptionCartStatusEnum.CANCELLED);
				if(abstractOrderEntryModel.getQuantity()<minOrderQuantity)
					subscriptionCartModel.setSubscriptionCancellationReason(configurationService.getConfiguration().getString(GpintegrationConstants.SUBSCRIPTION_REASON_MIN_Q));
				else
					subscriptionCartModel.setSubscriptionCancellationReason(configurationService.getConfiguration().getString(GpintegrationConstants.SUBSCRIPTION_REASON_MAX_Q));
				subscriptionCartModel.setPaymentTransactions(null);
				subscriptionCartModel.setIsActive(false);
				getModelService().save(subscriptionCartModel);
				LOG.error("Order failed due to Min/Max Order quantity violation");
				subscriptionCartCancelProcessModel.setSubscriptionCart(subscriptionCartModel);
				subscriptionCartCancelProcessModel.setSite(subscriptionCartModel.getSite());
				modelService.save(subscriptionCartCancelProcessModel);
				businessProcessService.startProcess(subscriptionCartCancelProcessModel);
				return false;
			}
			
			Long stock = Long.valueOf(0);
			stock = gpCommerceStockServiceImpl.getStockLevelForProductAndBaseStore(abstractOrderEntryModel.getProduct(), subscriptionCartModel.getStore());
			if(Boolean.valueOf(stock.longValue() <= abstractOrderEntryModel.getQuantity().longValue())){
				boolean firstTimeFailure=false;
				if(subscriptionCartModel.getSubscriptionCartStatus().equals(SubscriptionCartStatusEnum.ACTIVE))
					firstTimeFailure=true;
				subscriptionCartModel.setSubscriptionCartStatus(SubscriptionCartStatusEnum.OUTOFSTOCK);
				subscriptionCartModel.setPaymentTransactions(null);
				getModelService().save(subscriptionCartModel);
				LOG.error("Order failed due to OUT OF STOCK");
				if (firstTimeFailure) {
					subscriptionCartProcessModel.setSubscriptionCart(subscriptionCartModel);
					final SubscriptionOrderStatusEvent subscriptionStatusEvent = new SubscriptionOrderStatusEvent(
							subscriptionCartProcessModel);
					eventService.publishEvent(subscriptionStatusEvent);
					LOG.error("Order status OUT OF STOCK mail sent");
				}
				return false;
			}
			final CommerceCheckoutParameter parameter = createCommerceCheckoutParameter(subscriptionCartModel, true);
			parameter.setSalesApplication(SalesApplication.WEB);
			//validateIfProductIsInStockOnline
			if(!authorizePayment(parameter)) {
				boolean firstTimeFailure=false;
				if(subscriptionCartModel.getSubscriptionCartStatus().equals(SubscriptionCartStatusEnum.ACTIVE))
					firstTimeFailure=true;
				subscriptionCartModel.setSubscriptionCartStatus(SubscriptionCartStatusEnum.PAYMENTFAILURE);
				subscriptionCartModel.setPaymentTransactions(null);
				getModelService().save(subscriptionCartModel);
				LOG.error("Order failed due to Payment Auth Error");
				if (firstTimeFailure) {
					subscriptionCartProcessModel.setSubscriptionCart(subscriptionCartModel);
					final SubscriptionOrderStatusEvent subscriptionStatusEvent = new SubscriptionOrderStatusEvent(
							subscriptionCartProcessModel);
					eventService.publishEvent(subscriptionStatusEvent);
					LOG.error("Order status Payment Auth Error mail sent");
				}
				return false;
			}
			OrderModel orderData = commerceCheckoutService.placeOrder(parameter).getOrder();
			LOG.info("Order placed successfully for cart:"+subscriptionCartModel.getCode()+" with OrderId: "+orderData.getCode());
			
		} catch (final Exception e) {
			LOG.error("Error while creating Order from Cart", e);
			return false;
		}
		return true;
	}
	
	public PaymentTransactionEntryModel getCCPaymentAuthentication(CommerceCheckoutParameter cartParameter) {
		if (null != cartParameter.getCart() && null != cartParameter.getCart().getPaymentInfo()) {
			CreditCardPaymentInfoModel creditCardPaymentInfo = (CreditCardPaymentInfoModel) cartParameter.getCart()
					.getPaymentInfo();
			CCPaymentInfoData ccPaymentInfoData = getCreditCardPaymentInfoConverter().convert(creditCardPaymentInfo);
			ccPaymentInfoData.setPaymentToken(creditCardPaymentInfo.getPaymentToken());
			ReplyMessage replyMessage = null;
			try {
				replyMessage = gpCyberSource.authorizeUsingCybersource(ccPaymentInfoData, cartParameter,
						cartParameter.getCart().getSite().getUid(),true,false, false);

				String isTransactionSuccessful = replyMessage.getDecision();
				if ("ACCEPT".equalsIgnoreCase(isTransactionSuccessful)) {
					PaymentTransactionModel ccPaymentTransaction = getModelService()
							.create(PaymentTransactionModel.class);
					PaymentTransactionEntryModel paymentTransactionEntry = createPaymentTransactionEntry(replyMessage);
					paymentTransactionEntry.setTransactionStatus(TRANSACTION_STATUS_SUCCESS);
					paymentTransactionEntry.setType(PaymentTransactionType.AUTHORIZATION);
					ccPaymentTransaction.setEntries(Arrays.asList(paymentTransactionEntry));
					final PaymentInfoModel paymentInfo = cartParameter.getCart().getPaymentInfo();
					ccPaymentTransaction.setOrder(cartParameter.getCart());
					ccPaymentTransaction.setInfo(paymentInfo);
					getModelService().saveAll(cartParameter.getCart(), ccPaymentTransaction);
					return paymentTransactionEntry;

				}
			} catch (Exception e) {
				if (null != replyMessage) {
					LOG.error("Payment Auth failed error code :)" + replyMessage.getReasonCode().toString());
					throw new GPException(replyMessage.getReasonCode().toString(), "Payment Auth failed");
				}
				LOG.error("Payment Auth failed :", e);
			}
		}
		return null;
	}

	public PaymentTransactionEntryModel createPaymentTransactionEntry(ReplyMessage replyMessage) {
		PaymentTransactionEntryModel transactionEntry = getModelService().create(PaymentTransactionEntryModel.class);
		transactionEntry.setAmount(new BigDecimal(replyMessage.getCcAuthReply().getAmount()));
		transactionEntry.setCode(replyMessage.getCcAuthReply().getAuthorizationCode());
		transactionEntry.setRequestId(replyMessage.getRequestID());
		transactionEntry.setRequestToken(replyMessage.getRequestToken());
		transactionEntry.setSubscriptionID(replyMessage.getMerchantReferenceCode());
		return transactionEntry;
	}
	public boolean authorizePayment(CommerceCheckoutParameter parameter) {

		final PaymentTransactionEntryModel paymentTransactionEntryModel = getCCPaymentAuthentication(parameter);
		return paymentTransactionEntryModel != null
				&& (TransactionStatus.ACCEPTED.name().equals(paymentTransactionEntryModel.getTransactionStatus()));

	}

	protected CommerceCheckoutParameter createCommerceCheckoutParameter(final GPSubscriptionCartModel cart,
			final boolean enableHooks) {
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(enableHooks);
		parameter.setCart(cart);
		return parameter;
	}

	public CommerceCheckoutService getCommerceCheckoutService() {
		return commerceCheckoutService;
	}

	public void setCommerceCheckoutService(CommerceCheckoutService commerceCheckoutService) {
		this.commerceCheckoutService = commerceCheckoutService;
	}

	public GPSubscriptionService getSubscriptionCartServiceImpl() {
		return subscriptionCartServiceImpl;
	}

	public void setSubscriptionCartServiceImpl(GPSubscriptionServiceImpl subscriptionCartServiceImpl) {
		this.subscriptionCartServiceImpl = subscriptionCartServiceImpl;
	}

	public Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> getCreditCardPaymentInfoConverter() {
		return creditCardPaymentInfoConverter;
	}

	public void setCreditCardPaymentInfoConverter(
			Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoConverter) {
		this.creditCardPaymentInfoConverter = creditCardPaymentInfoConverter;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	public GPCybersourceIntegrationService getGpCyberSource() {
		return gpCyberSource;
	}

	public void setGpCyberSource(GPCybersourceIntegrationService gpCyberSource) {
		this.gpCyberSource = gpCyberSource;
	}

	public GPCommerceStockServiceImpl getGpCommerceStockServiceImpl() {
		return gpCommerceStockServiceImpl;
	}

	public void setGpCommerceStockServiceImpl(GPCommerceStockServiceImpl gpCommerceStockServiceImpl) {
		this.gpCommerceStockServiceImpl = gpCommerceStockServiceImpl;
	}
	
	public GPCommerceCoreUtils getGpCommerceCoreUtils() {
		return gpCommerceCoreUtils;
	}

	public void setGpCommerceCoreUtils(GPCommerceCoreUtils gpCommerceCoreUtils) {
		this.gpCommerceCoreUtils = gpCommerceCoreUtils;
	}

	public GPCalculationService getGpCalculationService() {
		return gpCalculationService;
	}

	public void setGpCalculationService(GPCalculationService gpCalculationService) {
		this.gpCalculationService = gpCalculationService;
	}

	public DeliveryService getDeliveryService() {
		return deliveryService;
	}

	public void setDeliveryService(DeliveryService deliveryService) {
		this.deliveryService = deliveryService;
	}

	public GPCartService getGpCartService() {
		return gpCartService;
	}

	public void setGpCartService(GPCartService gpCartService) {
		this.gpCartService = gpCartService;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
	
	public ContextualAttributeValuesSessionService getContextualAttributeValuesSessionService() {
		return contextualAttributeValuesSessionService;
	}

	public void setContextualAttributeValuesSessionService(
			ContextualAttributeValuesSessionService contextualAttributeValuesSessionService) {
		this.contextualAttributeValuesSessionService = contextualAttributeValuesSessionService;
	}
	
	public SessionService getSessionService() {
		return sessionService;
	}

	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
	
}

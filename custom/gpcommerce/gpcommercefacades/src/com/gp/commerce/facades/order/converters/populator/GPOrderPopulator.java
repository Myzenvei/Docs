/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.order.converters.populator;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.math.DoubleMath;
import com.gp.commerce.core.calculation.service.impl.GPCalculationService;
import com.gp.commerce.core.model.ApplePayPaymentInfoModel;
import com.gp.commerce.core.model.GooglePayPaymentInfoModel;
import com.gp.commerce.core.model.PaypalPaymentInfoModel;
import com.gp.commerce.core.model.ScheduleInstallationModel;
import com.gp.commerce.core.model.SplitEntryModel;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gp.commerce.facades.data.GPScheduleInstallationData;
import com.gp.commerce.order.data.GooglePayPaymentInfoData;

import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CardTypeData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.commerceservices.enums.DiscountType;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.enums.ExportStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.DiscountValue;

/**
 * @author dadidam
 *
 */
/**
 * @author dadidam
 *
 */
public class GPOrderPopulator  implements Populator<AbstractOrderModel, AbstractOrderData> {

	private static final String PRODUCT = "product";
	private static final String ORDER = "order";
	private static final String FREE_SHIPPING = "free_shipping";
	private PriceDataFactory priceDataFactory;
	@Resource(name = "modelService")
	private ModelService modelService;
	private PromotionsService promotionsService;
	private Converter<PromotionResultModel, PromotionResultData> promotionResultConverter;
	@Resource(name = "gpCalculationService")
	private GPCalculationService gpCalculationService;

	private static final double EPSILON = 0.01d;
	private static final String DECIMAL_FORMAT = "0.00";
	private Converter<ScheduleInstallationModel, GPScheduleInstallationData> gpInstallationConverter;

	@Resource
	private Converter<GooglePayPaymentInfoModel, GooglePayPaymentInfoData> gpGooglePayPaymentInfoConverter;

	/* (non-Javadoc)
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final AbstractOrderModel source, final AbstractOrderData target) throws ConversionException {

		final PaymentInfoModel paymentInfo = source.getPaymentInfo();

		if (paymentInfo instanceof PaypalPaymentInfoModel) {
			PaypalPaymentInfoModel paypalInfo = (PaypalPaymentInfoModel) source.getPaymentInfo();

			CCPaymentInfoData ccPaymentInfoData = createCCPaymentInfo(paypalInfo.getFirstName() + " " + paypalInfo.getLastName(), paypalInfo.getCode()
					, paypalInfo.getEmailId(), GpcommerceFacadesConstants.PAYPAL_PAYMENT_PROVIDER);

			target.setPaymentInfo(ccPaymentInfoData);

		} else if (paymentInfo instanceof GooglePayPaymentInfoModel) {

			GooglePayPaymentInfoModel googlePayPaymentInfoModel = (GooglePayPaymentInfoModel) source.getPaymentInfo();

			CCPaymentInfoData ccPaymentInfoData = createCCPaymentInfo(googlePayPaymentInfoModel.getCcOwner(), googlePayPaymentInfoModel.getType().getCode()
					, googlePayPaymentInfoModel.getGooglePayEmail(), GpcommerceFacadesConstants.GOOGLE_PAY_PAYMENT_PROVIDER);

			target.setPaymentInfo(ccPaymentInfoData);
		} else if (paymentInfo instanceof ApplePayPaymentInfoModel) {
			
			ApplePayPaymentInfoModel applePayPaymentInfoModel = (ApplePayPaymentInfoModel) source.getPaymentInfo();

			CCPaymentInfoData ccPaymentInfoData = createCCPaymentInfo(applePayPaymentInfoModel.getCcOwner(),
					applePayPaymentInfoModel.getType().getCode(), applePayPaymentInfoModel.getNumber(),
					GpcommerceFacadesConstants.APPLE_PAY_PAYMENT_PROVIDER);
			
			target.setPaymentInfo(ccPaymentInfoData);
		}

		final DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
		final double productsDiscountsAmount = getProductsDiscountsAmount(source);
		final double orderDiscountsAmount = getOrderDiscountsAmount(source);
		final double subTotal = source.getSubtotal().doubleValue() + source.getAdditionalDiscount().doubleValue() + productsDiscountsAmount;
		final PriceData subTotalPriceData = createPrice(source, Double.valueOf(subTotal));
		target.setSubTotal(subTotalPriceData);
		target.setTotalItems(calculateTotalEntries(source.getEntries()));
		if (null != source.getScheduleInstallation()) {
			target.setScheduleInstallation(getGpInstallationConverter().convert(source.getScheduleInstallation()));
		}
		if (StringUtils.isNotEmpty(source.getAgreementId())) {
			target.setLeaseId(source.getAgreementId());
		}
		if (StringUtils.isNotEmpty(source.getAgreementName())) {
			target.setLeaseName(source.getAgreementName());
		}
		target.setRemorseWindow(source.getSite().getRemorseWindow());

		target.setUserAdmin(checkAdminUser(source));

		if (StringUtils.isNotEmpty(source.getLeaseTermId())) {
			target.setLeaseTermId(source.getLeaseTermId());
		}

		if (source.getProcessingStatus() != null) {
			target.setProcessingStatus(source.getProcessingStatus());
		}

		if (source instanceof OrderModel) {
			target.setCancellable(false);
			if (CollectionUtils.isNotEmpty(((OrderModel) source).getReturnRequests()) && null != ((OrderModel) source).getReturnRequests().get(0)) {
				target.setReasonCode(((OrderModel) source).getReturnRequests().get(0).getReasonCodeCancellation());
			}
			target.setRefundAmt(getReturnTotal(((OrderModel) source).getReturnRequests()));
			if (!(OrderStatus.CANCELLED.equals(source.getStatus()) || OrderStatus.REJECTED.equals(source.getStatus())) && ExportStatus.NOTEXPORTED.equals(source.getExportStatus()) && isCancelValid(source.getCreationtime(), source.getSite().getRemorseWindow())) {
				target.setCancellable(true);
			}
		}
		final String totalDeliveryCost = GpcommerceFacadesConstants.DOLLAR + decimalFormat.format(source.getTotalDeliveryCost());
		target.setTotalDeliveryCost(totalDeliveryCost);

		addPromotions(source, target);
		target.setTotalDiscounts(createPrice(source, productsDiscountsAmount + orderDiscountsAmount + source.getAdditionalDiscount().doubleValue()));

		target.setTotalProductQty(calculateTotalProducts(source.getEntries()));
		if (source.getCartType() != null) {
			target.setCartType(source.getCartType().getCode());
		}
		target.setIsSubscription(source.getIsSubscription());
		target.setPriceDiscounts(createPrice(source, source.getAdditionalDiscount().doubleValue()));
		target.setPromotionalDiscounts(createPrice(source, productsDiscountsAmount + orderDiscountsAmount));
		target.setApproverComments(source.getApproverComments());
		if (source.getIsSubscription()) {
			AbstractOrderEntryModel entry = source.getEntries().get(0);
			double subscriptionPrice = gpCalculationService.calculateSubscriptionPrice(entry, entry.getActualBasePrice() * entry.getQuantity());
			target.setTotalSubscriptionPrice(createPrice(source, subscriptionPrice));
		}
	}


	private CCPaymentInfoData createCCPaymentInfo(String holderName,String cardTypeCode,String cardNumber,String paymentType){

		CCPaymentInfoData ccPaymentInfoData = new CCPaymentInfoData();
		ccPaymentInfoData.setAccountHolderName(holderName);
		ccPaymentInfoData.setCardType(paymentType);
		CardTypeData cardTypeData = new CardTypeData();
		cardTypeData.setCode(cardTypeCode);
		cardTypeData.setName(paymentType);
		ccPaymentInfoData.setCardTypeData(cardTypeData);
		ccPaymentInfoData.setCardNumber(cardNumber);

		return ccPaymentInfoData;

	}

	protected void addPromotions(final AbstractOrderModel source, final AbstractOrderData prototype)
	{
		addPromotions(source, getPromotionsService().getPromotionResults(source), prototype);

	}

	protected void addPromotions(final AbstractOrderModel source, final PromotionOrderResults promoOrderResults,
								 final AbstractOrderData prototype)
	{
		final double quoteDiscountsAmount = getQuoteDiscountsAmount(source);
		prototype.setQuoteDiscounts(createPrice(source, Double.valueOf(quoteDiscountsAmount)));

		final Pair<DiscountType, Double> quoteDiscountsTypeAndRate = getQuoteDiscountsTypeAndRate(source);
		prototype.setQuoteDiscountsType(quoteDiscountsTypeAndRate.getKey().getCode());
		prototype.setQuoteDiscountsRate(quoteDiscountsTypeAndRate.getValue());

		if (promoOrderResults != null)
		{
			final double productsDiscountsAmount = getProductsDiscountsAmount(source);
			final double orderDiscountsAmount = getOrderDiscountsAmount(source);

			prototype.setProductDiscounts(createPrice(source, Double.valueOf(productsDiscountsAmount)));
			prototype.setOrderDiscounts(createPrice(source, Double.valueOf(orderDiscountsAmount)));

			prototype.setTotalDiscounts(createPrice(source, Double.valueOf(productsDiscountsAmount + orderDiscountsAmount)));
			prototype.setTotalDiscountsWithQuoteDiscounts(createPrice(source,
					Double.valueOf(productsDiscountsAmount + orderDiscountsAmount + quoteDiscountsAmount)));
			prototype.setAppliedOrderPromotions(getPromotions(promoOrderResults.getAppliedOrderPromotions(),ORDER));
			prototype.setAppliedProductPromotions(getPromotions(promoOrderResults.getAppliedProductPromotions(),PRODUCT));
			addFreeShippingATOrderLevel(prototype,promoOrderResults.getAppliedProductPromotions());
		}

	}


	private void addFreeShippingATOrderLevel(final AbstractOrderData prototype,final List<PromotionResult> appliedProductPromotions) {
		final ArrayList<PromotionResultModel> promotionResultModels = getModelService().getAll(appliedProductPromotions,
				new ArrayList<PromotionResultModel>());
		final PromotionResultModel filterPromotions = promotionResultModels.stream()
				.filter(promotion -> promotion.getPromotion().getCode().contains(FREE_SHIPPING)).findFirst().orElse(null);

		if(null != filterPromotions) {
			final PromotionResultData  result = getPromotionResultConverter().convert(filterPromotions);
			if(CollectionUtils.isNotEmpty(prototype.getAppliedOrderPromotions())) {
				prototype.getAppliedOrderPromotions().add(result);
			}else {
				final ArrayList<PromotionResultData> data=new ArrayList<>();
				data.add(result);
				prototype.setAppliedOrderPromotions(data);
			}
		}

	}

	protected double getQuoteDiscountsAmount(final AbstractOrderModel source)
	{
		double discounts = 0.0d;
		final List<DiscountValue> discountList = source.getGlobalDiscountValues(); // discounts on the cart itself
		if (discountList != null && !discountList.isEmpty())
		{
			for (final DiscountValue discount : discountList)
			{
				final double value = discount.getAppliedValue();
				if (DoubleMath.fuzzyCompare(value, 0, EPSILON) > 0
						&& CommerceServicesConstants.QUOTE_DISCOUNT_CODE.equals(discount.getCode()))
				{
					discounts += value;
				}
			}
		}
		return discounts;
	}


	/*
	 * Extracts (and converts to POJOs) promotions from given results.
	 */
	protected List<PromotionResultData> getPromotions(final List<PromotionResult> promotionsResults,final String promotionType)
	{
		ArrayList<PromotionResultModel> promotionResultModels = getModelService().getAll(promotionsResults,
				new ArrayList<PromotionResultModel>());
		List<PromotionResultModel> filterPromotions  = null;
		if(PRODUCT.equalsIgnoreCase(promotionType)) {
			filterPromotions = promotionResultModels.stream()
					.filter(promotion -> !promotion.getPromotion().getCode().contains(FREE_SHIPPING)).collect(Collectors.toList());
			promotionResultModels = (ArrayList<PromotionResultModel>) filterPromotions;
		}
		return getPromotionResultConverter().convertAll(promotionResultModels);
	}

	protected Pair<DiscountType, Double> getQuoteDiscountsTypeAndRate(final AbstractOrderModel source)
	{
		double discounts = 0.0d;
		DiscountType discountType = DiscountType.PERCENT;
		final List<DiscountValue> discountList = source.getGlobalDiscountValues(); // discounts on the cart itself
		if (discountList != null && !discountList.isEmpty())
		{
			for (final DiscountValue discount : discountList)
			{
				final double value = discount.getAppliedValue();
				if (DoubleMath.fuzzyCompare(value, 0, EPSILON) > 0
						&& CommerceServicesConstants.QUOTE_DISCOUNT_CODE.equals(discount.getCode()))
				{
					// for now there is only one quote discount entry
					discounts = discount.getValue();
					if (discount.isAsTargetPrice())
					{
						discountType = DiscountType.TARGET;
					}
					else if (discount.isAbsolute())
					{
						discountType = DiscountType.ABSOLUTE;
					}
					break;
				}
			}
		}
		return Pair.of(discountType, Double.valueOf(discounts));
	}
	/**
	 * Calculation for Product Discounts
	 * @param source
	 * @return
	 */
	protected double getProductsDiscountsAmount(final AbstractOrderModel source)
	{
		double discounts = 0.0d;

		final List<AbstractOrderEntryModel> entries = source.getEntries();
		if (entries != null)
		{
			for (final AbstractOrderEntryModel entry : entries)
			{
				final List<DiscountValue> discountValues = entry.getDiscountValues();
				if (discountValues != null)
				{
					for (final DiscountValue dValue : discountValues)
					{
						discounts += dValue.getAppliedValue();
					}
				}
			}
		}
		return discounts;
	}

	/**
	 * Calculation for Order Discounts
	 * @param source
	 * @return
	 */
	protected double getOrderDiscountsAmount(final AbstractOrderModel source)
	{
		double discounts = 0.0d;
		final List<DiscountValue> discountList = source.getGlobalDiscountValues(); // discounts on the cart itself
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
		return discounts;
	}

	private boolean checkAdminUser(final AbstractOrderModel source) {
		for (final PrincipalGroupModel principalGroupModel : source.getUser().getGroups())
		{
			final String groupId = principalGroupModel.getUid();
			if (groupId != null && groupId.equals(B2BConstants.B2BADMINGROUP))
			{
				return true;

			}
		}
		return false;

	}

	private Integer calculateTotalEntries(final List<AbstractOrderEntryModel> entries) {
		return (int) entries.stream().mapToLong(entry -> entry.getQuantity()).sum();
	}

	private int calculateTotalProducts(final List<AbstractOrderEntryModel> entries) {
		int totalQty = 0;
		for (final AbstractOrderEntryModel orderEntry : entries) {
			final List<SplitEntryModel> spliEntryList = orderEntry.getSplitEntry();
			if (CollectionUtils.isNotEmpty(spliEntryList)) {
				for (final SplitEntryModel splitEntry : spliEntryList) {
					totalQty += Integer.valueOf(splitEntry.getQty());
				}

			}
		}
		return totalQty;
	}

	/**
	 * Gets Price data for products
	 * @param source
	 * @param val
	 * @return
	 */
	protected PriceData createPrice(final AbstractOrderModel source, final Double val)
	{
		if (source == null)
		{
			throw new IllegalArgumentException("source order must not be null");
		}

		final CurrencyModel currency = source.getCurrency();
		if (currency == null)
		{
			throw new IllegalArgumentException("source order currency must not be null");
		}

		// Get double value, handle null as zero
		final double priceValue = val != null ? val.doubleValue() : 0d;

		return getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(priceValue), currency);
	}

	public PriceDataFactory getPriceDataFactory() {
		return priceDataFactory;
	}

	public void setPriceDataFactory(final PriceDataFactory priceDataFactory) {
		this.priceDataFactory = priceDataFactory;
	}

	public Converter<ScheduleInstallationModel, GPScheduleInstallationData> getGpInstallationConverter() {
		return gpInstallationConverter;
	}

	public void setGpInstallationConverter(
			final Converter<ScheduleInstallationModel, GPScheduleInstallationData> gpInstallationConverter) {
		this.gpInstallationConverter = gpInstallationConverter;
	}
	/***
	 *
	 * @param returnReqList
	 * @return The sumtotal of all the returnrequest model of the Order
	 */
	private BigDecimal getReturnTotal(final List<ReturnRequestModel> returnReqList)
	{
		double returnTotal = 0;

		if(CollectionUtils.isNotEmpty(returnReqList))
		{
			for(final ReturnRequestModel returnReq : returnReqList)
			{
				if(null != returnReq.getSubtotal())
				{
					returnTotal = returnTotal + returnReq.getSubtotal().doubleValue();
				}
			}
		}
		return BigDecimal.valueOf(returnTotal);
	}
	/***
	 *
	 * @param createdTime
	 * @return Return whether the order is in within the cancellation limit of Remorse window or not
	 */
	private boolean	isCancelValid(final Date createdTime,final Integer remorseWindow)
	{
		boolean cond = false;

		final Calendar cal = Calendar.getInstance();
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		final Date now = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
		// Set OrderCreated Time
		cal.setTime(createdTime);
		final int diff =   remorseWindow != null ? remorseWindow:0;
		//Adding the Remorse Window in the Order Creation Time
		cal.add(Calendar.HOUR, diff);
		final Date MaxTimeRemorseWin = cal.getTime();

		if(MaxTimeRemorseWin.compareTo(now)>0)
		{
			cond = true;
		}

		return cond;
	}

	public PromotionsService getPromotionsService() {
		return promotionsService;
	}

	public void setPromotionsService(final PromotionsService promotionsService) {
		this.promotionsService = promotionsService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(final ModelService modelService) {
		this.modelService = modelService;
	}

	public Converter<PromotionResultModel, PromotionResultData> getPromotionResultConverter() {
		return promotionResultConverter;
	}

	public void setPromotionResultConverter(
			final Converter<PromotionResultModel, PromotionResultData> promotionResultConverter) {
		this.promotionResultConverter = promotionResultConverter;
	}
}

/**
 *
 */
package com.deloitte.facade.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.voucher.data.VoucherData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.couponfacades.facades.impl.DefaultCouponFacade;
import de.hybris.platform.couponservices.CouponServiceException;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.couponservices.service.data.CouponResponse;
import de.hybris.platform.couponservices.services.CouponCodeGenerationService;
import de.hybris.platform.couponservices.services.CouponManagementService;
import de.hybris.platform.couponservices.services.CouponService;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.deloitte.facade.SubscriptionVoucherFacade;
import com.deloitte.model.SubscriptionCartModel;


/**
 * @author asomjal
 *
 */
public class SubscriptionCouponFacadeImpl extends DefaultCouponFacade implements SubscriptionVoucherFacade
{
	private static final Logger LOG = Logger.getLogger(SubscriptionCouponFacadeImpl.class);

	private CouponService couponService;
	private CartService cartService;
	private AbstractPopulatingConverter<String, VoucherData> couponCodeModelConverter;
	private Converter<AbstractCouponModel, VoucherData> couponModelConverter;

	@Resource(name = "couponManagementService")
	private CouponManagementService couponManagementService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "couponCodeGenerationService")
	private CouponCodeGenerationService couponCodeGenerationService;

	@Resource(name = "calculationService")
	private CalculationService calculationService;

	@Resource(name = "promotionsService")
	private PromotionsService promotionsService;

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Override
	public void applySubscriptionVoucher(final String voucherCode, final SubscriptionCartModel subscriptionCartModel)
			throws VoucherOperationException
	{
		ServicesUtil.validateParameterNotNullStandardMessage("coupon code", voucherCode);
		final CouponResponse couponResponse = this.applyIfCartExistsForSubscription(voucherCode,
				this.getCouponService()::redeemCoupon, subscriptionCartModel);
		if (org.apache.commons.lang3.BooleanUtils.isNotTrue(couponResponse.getSuccess()))
		{
			throw new VoucherOperationException(couponResponse.getMessage());
		}
	}

	protected <R> R applyIfCartExistsForSubscription(final String code, final BiFunction<String, CartModel, R> orderConsumer,
			final SubscriptionCartModel subscriptionCartModel) throws VoucherOperationException
	{
		if (Objects.nonNull(subscriptionCartModel))
		{
			return orderConsumer.apply(code, subscriptionCartModel);
		}
		else
		{
			throw new VoucherOperationException("No cart was found in session");
		}
	}


	@Override
	public void subScriptionReleaseVoucher(final String voucherCode, final SubscriptionCartModel subscriptionCartModel,
			final String baseSite) throws VoucherOperationException
	{
		ServicesUtil.validateParameterNotNullStandardMessage("coupon code", voucherCode);
		releaseCouponCode(voucherCode, subscriptionCartModel, baseSite);
	}

	public void releaseCouponCode(final String couponCode, final AbstractOrderModel order, final String baseSite)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("couponCode", couponCode);
		ServicesUtil.validateParameterNotNullStandardMessage("order", order);

		couponManagementService.releaseCouponCode(couponCode);
		removeCouponAndTriggerCalculation(couponCode, order, baseSite);
	}

	protected void removeCouponAndTriggerCalculation(final String couponCode, final AbstractOrderModel order,
			final String baseSite)
	{
		final Collection couponCodes = order.getAppliedCouponCodes();
		if (CollectionUtils.isNotEmpty(couponCodes) && containsCouponCode(couponCode, order))
		{
			final Set couponCodesFiltered = (Set) couponCodes.stream().filter((c) -> {
				return !c.equals(couponCode);
			}).collect(Collectors.toSet());

			order.setAppliedCouponCodes(couponCodesFiltered);

			order.setCalculated(Boolean.FALSE);
			modelService.save(order);
			recalculateOrderWithPromotion(order, baseSite);
		}
	}

	@Override
	public void recalculateOrderWithPromotion(final AbstractOrderModel order, final String baseSite)
	{
		try
		{
			calculationService.calculate(order);
			promotionsService.updatePromotions(this.getPromotionGroups(baseSite), order);
		}
		catch (final CalculationException arg2)
		{
			LOG.error("Error re-calculating the order", arg2);
			throw new CouponServiceException("coupon.order.recalculation.error");
		}
	}

	protected Collection<PromotionGroupModel> getPromotionGroups(final String baseSite)
	{
		final ArrayList promotionGroupModels = new ArrayList();
		final BaseSiteModel currentBaseSite = baseSiteService.getBaseSiteForUID(baseSite);
		if (Objects.nonNull(currentBaseSite) && Objects.nonNull(currentBaseSite.getDefaultPromotionGroup()))
		{
			promotionGroupModels.add(currentBaseSite.getDefaultPromotionGroup());
		}
		return promotionGroupModels;
	}

	protected boolean containsCouponCode(final String couponCode, final AbstractOrderModel order)
	{
		if (CollectionUtils.isNotEmpty(order.getAppliedCouponCodes()))
		{
			final Optional couponModel = couponManagementService.getCouponForCode(couponCode);
			if (couponModel.isPresent())
			{

				return order.getAppliedCouponCodes().stream()
						.anyMatch(checkMatch((AbstractCouponModel) couponModel.get(), couponCode));
			}
		}
		return false;
	}

	protected Predicate<String> checkMatch(final AbstractCouponModel coupon, final String couponCode)
	{
		return coupon instanceof MultiCodeCouponModel ? (appliedCouponCode) -> {


			final String couponPrefix = couponCodeGenerationService.extractCouponPrefix(couponCode);
			return couponPrefix != null && couponPrefix.equals(couponCodeGenerationService.extractCouponPrefix(appliedCouponCode));


		} : (appliedCouponCode) -> {
			return appliedCouponCode.equals(couponCode);
		};
	}

	@Override
	protected CouponService getCouponService()
	{
		return this.couponService;
	}

	@Override
	@Required
	public void setCouponService(final CouponService couponService)
	{
		this.couponService = couponService;
	}

	@Override
	protected CartService getCartService()
	{
		return this.cartService;
	}

	@Override
	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	@Override
	protected AbstractPopulatingConverter<String, VoucherData> getCouponCodeModelConverter()
	{
		return this.couponCodeModelConverter;
	}

	@Override
	@Required
	public void setCouponCodeModelConverter(final AbstractPopulatingConverter<String, VoucherData> couponCodeModelConverter)
	{
		this.couponCodeModelConverter = couponCodeModelConverter;
	}

	@Override
	protected Converter<AbstractCouponModel, VoucherData> getCouponModelConverter()
	{
		return this.couponModelConverter;
	}

	@Override
	@Required
	public void setCouponModelConverter(final Converter<AbstractCouponModel, VoucherData> couponModelConverter)
	{
		this.couponModelConverter = couponModelConverter;
	}
}

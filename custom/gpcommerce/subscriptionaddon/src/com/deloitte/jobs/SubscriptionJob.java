/**
 *
 */
package com.deloitte.jobs;

import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.deloitte.enums.SubscriptionStatusEnum;
import com.deloitte.facade.SubscriptionCartFacade;
import com.deloitte.facade.impl.SubscriptionCouponFacadeImpl;
import com.deloitte.model.SubscriptionCartModel;
import com.deloitte.model.SubscriptionScheduleModel;


/**
 * @author asomjal
 *
 */
public class SubscriptionJob extends AbstractJobPerformable<CronJobModel>
{

	private static Logger LOG = Logger.getLogger(SubscriptionJob.class);
	private static final String BASESITE = "electronics";

	@Resource(name = "commerceCheckoutService")
	private CommerceCheckoutService commerceCheckoutService;

	@Resource(name = "subscriptionCartFacadeImpl")
	private SubscriptionCartFacade subscriptionCartFacadeImpl;

	@Resource(name = "orderService")
	private OrderService orderService;

	@Resource(name = "subscriptionCouponFacade")
	private SubscriptionCouponFacadeImpl subscriptionCouponFacade;

	@Resource(name = "calculationService")
	private CalculationService calculationService;

	@Resource(name = "cartService")
	private CartService cartService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PerformResult perform(final CronJobModel cronjob)
	{
		LOG.info("-----------------inside subrscription Cronjob---------------------");
		final List<SubscriptionCartModel> subscriptionCartModelList = subscriptionCartFacadeImpl.getSubscriptionCartModels();
		if (CollectionUtils.isNotEmpty(subscriptionCartModelList))
		{
			for (final SubscriptionCartModel subscriptionCartModel : subscriptionCartModelList)
			{
				try
				{
					final List<SubscriptionScheduleModel> subscriptionScheduleList = subscriptionCartModel.getSubscriptionSchedule();
					applyPromotionOnCart(subscriptionCartModel, subscriptionScheduleList);
					subscriptionCouponFacade.recalculateOrderWithPromotion(subscriptionCartModel, BASESITE);
					final OrderModel orderModel = placeOrder(subscriptionCartModel);
					afterPlaceOrder(subscriptionCartModel, orderModel);
				}
				catch (final Exception e)
				{
					LOG.error(e.getMessage(), e);
				}

			}
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 * @param subscriptionCartModel
	 */
	private OrderModel placeOrder(final SubscriptionCartModel subscriptionCartModel)
	{
		try
		{
			final CommerceCheckoutParameter parameter = createCommerceCheckoutParameter(subscriptionCartModel, true);
			parameter.setSalesApplication(SalesApplication.WEB);
			return commerceCheckoutService.placeOrder(parameter).getOrder();
		}
		catch (final InvalidCartException e)
		{
			LOG.error("Error while creating Order from Cart", e);
		}
		return null;
	}

	protected CommerceCheckoutParameter createCommerceCheckoutParameter(final SubscriptionCartModel cart,
			final boolean enableHooks)
	{
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(enableHooks);
		parameter.setCart(cart);
		return parameter;
	}

	protected void afterPlaceOrder(@SuppressWarnings("unused") final SubscriptionCartModel subscriptionCartModel,
			final OrderModel orderModel) throws VoucherOperationException //NOSONAR
	{
		if (orderModel != null)
		{
			modelService.refresh(orderModel);
		}
		final Collection<String> appliedCouponCodes = subscriptionCartModel.getAppliedCouponCodes();
		if (CollectionUtils.isNotEmpty(appliedCouponCodes))
		{
			for (final String appliedCouponCode : appliedCouponCodes)
			{
				subscriptionCouponFacade.subScriptionReleaseVoucher(appliedCouponCode, subscriptionCartModel, BASESITE);
			}
		}
	}

	/**
	 * @param subscriptionCartModel
	 * @param subscriptionScheduleList
	 */
	private void applyPromotionOnCart(final SubscriptionCartModel subscriptionCartModel,
			final List<SubscriptionScheduleModel> subscriptionScheduleList)
	{
		if (CollectionUtils.isNotEmpty(subscriptionScheduleList))
		{
			for (final SubscriptionScheduleModel subscriptionScheduleModel : subscriptionScheduleList)
			{
				if ((subscriptionCartModel.getNextActivationDate().equals(subscriptionScheduleModel.getNextActivationDate()))
						&& subscriptionScheduleModel.getSubscriptionStatus().equals(SubscriptionStatusEnum.PENDING))
				{
					applyVouchers(subscriptionCartModel, subscriptionScheduleModel);
					subscriptionScheduleModel.setSubscriptionStatus(SubscriptionStatusEnum.PROCESSED);
					modelService.save(subscriptionScheduleModel);
				}
			}
		}
	}

	/**
	 * @param subscriptionCartModel
	 * @param subscriptionScheduleModel
	 */
	private void applyVouchers(final SubscriptionCartModel subscriptionCartModel,
			final SubscriptionScheduleModel subscriptionScheduleModel)
	{
		if (CollectionUtils.isNotEmpty(subscriptionScheduleModel.getPromotionId()))
		{
			for (final String promotionId : subscriptionScheduleModel.getPromotionId())
			{
				try
				{
					subscriptionCouponFacade.applySubscriptionVoucher(promotionId, subscriptionCartModel);
				}
				catch (final VoucherOperationException e)
				{
					LOG.error("Error while applying voucher", e);
				}
			}
		}
	}
}

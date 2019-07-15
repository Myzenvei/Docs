/**
 *
 */
package com.deloitte.facade.impl;

import de.hybris.platform.commercefacades.order.data.AddToCartParams;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.deloitte.enums.SubscriptionFrequencyEnum;
import com.deloitte.enums.SubscriptionStatusEnum;
import com.deloitte.facade.SubscriptionCartFacade;
import com.deloitte.model.SubscriptionCartModel;
import com.deloitte.model.SubscriptionScheduleModel;
import com.deloitte.populators.SubscriptionDefaultCartPopulator;
import com.deloitte.service.SubscriptionCartService;


/**
 * @author asomjal
 *
 */
public class SubscriptionCartFacadeImpl implements SubscriptionCartFacade
{
	@Resource(name = "cartModificationConverter")
	private Converter<CommerceCartModification, CartModificationData> cartModificationConverter;

	@Resource(name = "subscriptionCartServiceImpl")
	private SubscriptionCartService subscriptionCartServiceImpl;

	@Resource(name = "subscriptionCommerceCartParameterConverter")
	private Converter<AddToCartParams, CommerceCartParameter> subscriptionCommerceCartParameterConverter;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "cartPopulator")
	private SubscriptionDefaultCartPopulator cartPopulator;

	@Override
	public CartModificationData addToCartInternal(final String code, final long quantity, final String subscriptionFrequecy,
			final String cartId) throws CommerceCartModificationException
	{
		final AddToCartParams params = new AddToCartParams();
		params.setProductCode(code);
		params.setQuantity(quantity);
		params.setCartId(cartId);
		params.setFrequency(subscriptionFrequecy);
		//params.setAdditionalAttributes(null);
		return addToCart(params);
	}

	@Override
	public CartModificationData addToCart(final AddToCartParams addToCartParams) throws CommerceCartModificationException
	{
		final CommerceCartParameter parameter = subscriptionCommerceCartParameterConverter.convert(addToCartParams);
		//parameter.setAdditionalAttributes(addToCartParams.getAdditionalAttributes());
		final CommerceCartModification modification = subscriptionCartServiceImpl.addToCart(parameter);
		final CartModificationData cartModificationData = cartModificationConverter.convert(modification);
		if (parameter.getCart() instanceof SubscriptionCartModel)
		{
			createSubscriptionScheduleForCart(parameter);
		}
		return cartModificationData;
	}

	/**
	 * This method is used to create subscription schedule entries for given cart.
	 *
	 * @param parameter
	 *           CommerceCartParameter
	 */
	private void createSubscriptionScheduleForCart(final CommerceCartParameter parameter)
	{
		final SubscriptionCartModel subscriptionCartModel = (SubscriptionCartModel) parameter.getCart();
		if (CollectionUtils.isEmpty(subscriptionCartModel.getSubscriptionSchedule()))
		{
			final SubscriptionFrequencyEnum subscriptionFrequencyEnum = subscriptionCartModel.getFrequency();
			if (subscriptionFrequencyEnum.equals(SubscriptionFrequencyEnum.DAILY))
			{
				for (int count = 1; count <= 5; count++)
				{
					final LocalDate tomorrow = LocalDate.now().plusDays(count);
					final Date endDate = Date.from(tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant());
					createSubscriptionSchedule(subscriptionCartModel, count, endDate);
				}
			}
			else if (subscriptionFrequencyEnum.equals(SubscriptionFrequencyEnum.WEEKLY))
			{
				for (int count = 1; count <= 5; count++)
				{
					final LocalDate nextWeek = LocalDate.now().plusWeeks(count);
					final Date endDate = Date.from(nextWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
					createSubscriptionSchedule(subscriptionCartModel, count, endDate);
				}
			}
			else if (subscriptionFrequencyEnum.equals(SubscriptionFrequencyEnum.MONTHLY))
			{
				for (int count = 1; count <= 5; count++)
				{
					final LocalDate nextMonth = LocalDate.now().plusMonths(count);
					final Date endDate = Date.from(nextMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
					createSubscriptionSchedule(subscriptionCartModel, count, endDate);
				}
			}
			else
			{
				for (int count = 1; count <= 5; count++)
				{
					final LocalDate nextYear = LocalDate.now().plusYears(count);
					final Date endDate = Date.from(nextYear.atStartOfDay(ZoneId.systemDefault()).toInstant());
					createSubscriptionSchedule(subscriptionCartModel, count, endDate);
				}
			}
		}
	}

	/**
	 * This method is used to create Subscription schedule entries.
	 *
	 * @param subscriptionCartModel
	 *           subscriptionCartModel
	 * @param count
	 *           count
	 * @param endDate
	 *           endDate
	 */
	private void createSubscriptionSchedule(final SubscriptionCartModel subscriptionCartModel, final int count, final Date endDate)
	{
		if (count == 1)
		{
			subscriptionCartModel.setNextActivationDate(endDate);
			modelService.save(subscriptionCartModel);
		}
		final SubscriptionScheduleModel subscriptionScheduleModel = new SubscriptionScheduleModel();
		subscriptionScheduleModel.setNextActivationDate(endDate);
		subscriptionScheduleModel.setSubscriptionCart(subscriptionCartModel);
		subscriptionScheduleModel.setSubscriptionStatus(SubscriptionStatusEnum.PENDING);
		modelService.save(subscriptionScheduleModel);
	}

	@Override
	public List<SubscriptionCartModel> getSubscriptionCartModels()
	{
		return subscriptionCartServiceImpl.getSubscriptionCartModels();
	}

	@Override
	public List<CartData> getSubcriptionCartsForUser(final String userId)
	{
		final List<SubscriptionCartModel> cartModelList = subscriptionCartServiceImpl.getSubcriptionCartsForUser(userId);
		if (CollectionUtils.isNotEmpty(cartModelList))
		{
			final List<CartData> cartDataList = new ArrayList();
			for (final SubscriptionCartModel subscriptionCartModel : cartModelList)
			{
				final CartData cartData = new CartData();
				cartPopulator.populate(subscriptionCartModel, cartData);
				cartDataList.add(cartData);
			}
			return cartDataList;
		}
		return Collections.emptyList();
	}
}

/**
 *
 */
package com.deloitte.populators;

import de.hybris.platform.commercefacades.order.converters.populator.CartPopulator;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.CartModel;

import java.text.SimpleDateFormat;

import com.deloitte.model.SubscriptionCartModel;


/**
 * @author asomjal
 *
 */
public class SubscriptionDefaultCartPopulator extends CartPopulator
{
	@Override
	public void populate(final CartModel source, final CartData target)
	{
		super.populate(source, target);
		if (source instanceof SubscriptionCartModel)
		{
			final SubscriptionCartModel subscriptionCartModel = (SubscriptionCartModel) source;
			target.setFrequency(subscriptionCartModel.getFrequency());
			target.setIsActive(subscriptionCartModel.getIsActive());
			final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.m");
			if (null != subscriptionCartModel.getNextActivationDate())
			{
				target.setNextActivationDate(sf.format(subscriptionCartModel.getNextActivationDate()));
			}
		}
	}
}

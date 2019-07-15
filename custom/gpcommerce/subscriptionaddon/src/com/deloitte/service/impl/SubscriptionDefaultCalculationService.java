/**
 *
 */
package com.deloitte.service.impl;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.impl.DefaultCalculationService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.TaxValue;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.deloitte.service.CustomPriceFactoryService;


/**
 * @author asomjal
 *
 */
public class SubscriptionDefaultCalculationService extends DefaultCalculationService
{
	@Resource(name = "customPriceFactoryService")
	private CustomPriceFactoryService customPriceFactoryService;

	@Override
	protected void resetAllValues(final AbstractOrderEntryModel entry) throws CalculationException
	{
		// taxes
		final Collection<TaxValue> entryTaxes = findTaxValues(entry);
		entry.setTaxValues(entryTaxes);
		PriceValue pv;
		if (null != entry.getFrequency() && null != entry.getProduct())
		{
			pv = catculatePriceValue(entry);
		}
		else
		{
			pv = findBasePrice(entry);
		}
		final AbstractOrderModel order = entry.getOrder();
		final PriceValue basePrice = convertPriceIfNecessary(pv, order.getNet().booleanValue(), order.getCurrency(), entryTaxes);
		entry.setBasePrice(Double.valueOf(basePrice.getValue()));
		final List<DiscountValue> entryDiscounts = findDiscountValues(entry);
		entry.setDiscountValues(entryDiscounts);
	}

	/**
	 * This method is used to calculate price Value based on frequency.
	 *
	 * @param entry
	 *           entry
	 * @param pv
	 *           pv
	 * @return Price Value
	 * @throws CalculationException
	 *            CalculationException
	 */
	private PriceValue catculatePriceValue(final AbstractOrderEntryModel entry) throws CalculationException
	{
		final AbstractOrderModel order = entry.getOrder();
		final List<PriceData> priceDataList = customPriceFactoryService.getAllPricesForProduct(entry.getProduct().getCode());
		if (CollectionUtils.isNotEmpty(priceDataList))
		{
			for (final PriceData priceData : priceDataList)
			{
				if (priceData.getFrequency().equals(entry.getFrequency()))
				{
					final double priceValue = null != priceData.getValue() ? priceData.getValue().doubleValue() : 0;
					return new PriceValue(order.getCurrency().getIsocode(), priceValue, order.getNet().booleanValue());
				}
			}
		}
		return findBasePrice(entry);
	}
}

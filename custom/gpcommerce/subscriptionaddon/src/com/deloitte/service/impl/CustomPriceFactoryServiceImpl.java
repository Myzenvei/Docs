/**
 *
 */
package com.deloitte.service.impl;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commerceservices.strategies.NetGrossStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.deloitte.service.CustomPriceFactoryService;


/**
 * @author asomjal
 *
 */
public class CustomPriceFactoryServiceImpl extends Europe1PriceFactory implements CustomPriceFactoryService
{
	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;

	private NetGrossStrategy netGrossStrategy;
	private TimeService timeService;
	private ModelService modelService;

	@Override
	public List<PriceData> getAllPricesForProduct(final String productCode)
	{
		final List<PriceData> priceDataList = new ArrayList();
		final ProductModel productModel = productService.getProductForCode(productCode);
		final List<PriceRow> priceRowList = getPriceInformationsForProduct(productModel);
		if (CollectionUtils.isNotEmpty(priceRowList))
		{
			for (final PriceRow priceRow : priceRowList)
			{
				final PriceRowModel prmModel = modelService.get(priceRow.getPK());
				final PriceDataType priceType;
				if (CollectionUtils.isEmpty(productModel.getVariants()))
				{
					priceType = PriceDataType.BUY;
				}
				else
				{
					priceType = PriceDataType.FROM;
				}
				final PriceData priceData = priceDataFactory.create(priceType, BigDecimal.valueOf(prmModel.getPrice()),
						prmModel.getCurrency());
				priceData.setFrequency(prmModel.getFrequency());
				priceDataList.add(priceData);
			}
		}
		return priceDataList;

	}

	@Override
	public List<PriceRow> getPriceInformationsForProduct(final ProductModel productModel)
	{
		final boolean net = getNetGrossStrategy().isNet();
		final Product product = (Product) getModelService().getSource(productModel);
		try
		{
			final SessionContext ctx = product.getSession().getSessionContext();
			return this.matchPriceRowsForInfoForSubscription(ctx, product, super.getPPG(ctx, product), ctx.getUser(),
					super.getUPG(ctx, ctx.getUser()), ctx.getCurrency(), timeService.getCurrentTime(), net);
		}
		catch (final JaloPriceFactoryException e)
		{
			throw new SystemException(e.getMessage(), e);
		}

	}

	public List<PriceRow> matchPriceRowsForInfoForSubscription(final SessionContext ctx, final Product product,
			final EnumerationValue productGroup, final User user, final EnumerationValue userGroup, final Currency currency,
			final Date date, final boolean net) throws JaloPriceFactoryException
	{
		if (product == null && productGroup == null)
		{
			throw new JaloPriceFactoryException(
					"cannot match price info without product and product group - at least one must be present", 0);
		}
		else if (user == null && userGroup == null)
		{
			throw new JaloPriceFactoryException("cannot match price info without user and user group - at least one must be present",
					0);
		}
		else if (currency == null)
		{
			throw new JaloPriceFactoryException("cannot match price info without currency", 0);
		}
		else if (date == null)
		{
			throw new JaloPriceFactoryException("cannot match price info without date", 0);
		}
		else
		{
			final Collection rows = this.queryPriceRows4Price(ctx, product, productGroup, user, userGroup, date, currency, false);
			if (rows.isEmpty())
			{
				return Collections.emptyList();
			}
			else
			{
				return new ArrayList(rows);
			}

		}
	}

	public NetGrossStrategy getNetGrossStrategy()
	{
		return netGrossStrategy;
	}

	public void setNetGrossStrategy(final NetGrossStrategy netGrossStrategy)
	{
		this.netGrossStrategy = netGrossStrategy;
	}

	public TimeService getTimeService()
	{
		return timeService;
	}

	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
